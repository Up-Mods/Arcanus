package dev.cammiescorner.arcanuscontinuum.common.components.level;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.common.blocks.SpatialRiftExitBlock;
import dev.cammiescorner.arcanuscontinuum.common.blocks.SpatialRiftExitEdgeBlock;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusDimensions;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.PlayerHelper;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PocketDimensionComponent implements dev.onyxstudios.cca.api.v3.component.Component {

	private static final int REPLACE_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_SUPPRESS_DROPS;
	private static final int DIMENSION_PADDING_Y = 8;
	private static final int DIMENSION_PADDING_XZ = 24;
	private static final int POCKET_MARGIN = 24;

	private final Map<UUID, PocketDimensionPlot> existingPlots = new HashMap<>();
	private final Map<UUID, Tuple<ResourceKey<Level>, Vec3>> exitSpot = new HashMap<>();
	private final MinecraftServer server;

	public PocketDimensionComponent(Scoreboard scoreboard, MinecraftServer server) {
		this.server = server;
	}

	public static PocketDimensionComponent get(MinecraftServer server) {
		return server.getScoreboard().getComponent(ArcanusComponents.POCKET_DIMENSION_COMPONENT);
	}

	public static PocketDimensionComponent get(Level world) {
		if (world instanceof ServerLevel serverWorld) {
			return serverWorld.getScoreboard().getComponent(ArcanusComponents.POCKET_DIMENSION_COMPONENT);
		}

		throw new IllegalStateException("World is not a ServerWorld!");
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		ListTag plotNbtList = tag.getList("PlotMap", Tag.TAG_COMPOUND);
		ListTag exitNbtList = tag.getList("ExitSpots", Tag.TAG_COMPOUND);

		existingPlots.clear();
		exitSpot.clear();

		for (int i = 0; i < plotNbtList.size(); i++) {
			var entry = PocketDimensionPlot.fromNbt(plotNbtList.getCompound(i));
			if (entry != null) {
				existingPlots.put(entry.ownerId(), entry);
			}
		}

		for (int i = 0; i < exitNbtList.size(); i++) {
			CompoundTag entry = exitNbtList.getCompound(i);
			exitSpot.put(entry.getUUID("EntityId"), new Tuple<>(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(entry.getString("WorldKey"))), new Vec3(entry.getDouble("X"), entry.getDouble("Y"), entry.getDouble("Z"))));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag plotNbtList = new ListTag();
		ListTag exitNbtList = new ListTag();

		existingPlots.forEach((uuid, plot) -> {
			plotNbtList.add(plot.toNbt());
		});

		exitSpot.forEach((uuid, pair) -> {
			CompoundTag entry = new CompoundTag();
			entry.putUUID("EntityId", uuid);
			entry.putString("WorldKey", pair.getA().location().toString());
			entry.putDouble("X", pair.getB().x());
			entry.putDouble("Y", pair.getB().y());
			entry.putDouble("Z", pair.getB().z());
			exitNbtList.add(entry);
		});

		tag.put("PlotMap", plotNbtList);
		tag.put("ExitSpots", exitNbtList);
	}

	// FIXME this check is too eager sometimes.
	//  not too bad for the moment but should be looked into.
	private static boolean chunksExist(PocketDimensionPlot plot, ServerLevel pocketDim) {
		var chunkManager = pocketDim.getChunkSource();

		return BlockPos.betweenClosedStream(plot.min(), plot.max()).map(ChunkPos::new).distinct().map(cPos -> chunkManager.getChunk(cPos.x, cPos.z, false)).noneMatch(Objects::isNull);
	}

	public void teleportToPocketDimension(GameProfile pocketOwner, Entity entity) {
		if (!entity.level().isClientSide()) {
			ServerLevel pocketDim = server.getLevel(ArcanusDimensions.POCKET_DIMENSION);
			if (pocketDim != null) {
				var plot = getAssignedPlotSpace(pocketOwner.getId());
				if (plot == null) {
					plot = assignNewPlot(pocketDim, pocketOwner, entity.level().getRandom());
					replacePlotSpace(pocketOwner.getId(), pocketDim, RegenerateType.FULL);
				} else if (!chunksExist(plot, pocketDim)) {
					Arcanus.LOGGER.warn("Pocket dimension plot for player {} ({}) failed integrity check! regenerating boundary...", pocketOwner.getName(), pocketOwner.getId());
					replacePlotSpace(pocketOwner.getId(), pocketDim, RegenerateType.WALLS_ONLY);
				}

				var bottomCenterPos = Vec3.atBottomCenterOf(plot.getBounds().getCenter().atY(plot.min().getY() + 1));
				FabricDimensions.teleport(entity, pocketDim, new PortalInfo(bottomCenterPos, Vec3.ZERO, entity.getYRot(), entity.getXRot()));
			}
		}
	}

	public boolean teleportOutOfPocketDimension(Entity entity) {
		if (PlayerHelper.isFakePlayer(entity) || entity.level().isClientSide() || entity.level().dimension() != ArcanusDimensions.POCKET_DIMENSION) {
			return false;
		}

		ArcanusComponents.setPortalCoolDown(entity, 200);

		UUID ownerId = existingPlots.values().stream().filter(plot -> entity.getBoundingBox().intersects(AABB.of(plot.getBounds()))).map(PocketDimensionPlot::ownerId).findFirst().orElse(null);
		if (ownerId != null) {
			Tuple<ResourceKey<Level>, Vec3> pair = exitSpot.get(ownerId);
			if (pair != null) {
				ServerLevel targetWorld = server.getLevel(pair.getA());
				Vec3 targetPos = pair.getB();

				if (targetWorld == null) {
					Arcanus.LOGGER.error("Unable to find dimension {}, defaulting to overworld", pair.getA().location());
					targetWorld = server.overworld();
					targetPos = Vec3.atBottomCenterOf(targetWorld.getSharedSpawnPos());
				}

				FabricDimensions.teleport(entity, targetWorld, new PortalInfo(targetPos, Vec3.ZERO, entity.getYRot(), entity.getXRot()));
				return true;
			}
		}

		if (entity instanceof ServerPlayer player) {
			var profile = player.getGameProfile();
			Arcanus.LOGGER.warn("Failed to determine pocket dimension exit spot for player {} ({}), sending them to their spawn position!", profile.getName(), profile.getId());

			var spawnPos = player.getRespawnPosition();
			var angle = player.getRespawnAngle();
			var world = server.getLevel(player.getRespawnDimension());
			if (!player.isRespawnForced() || world == null || spawnPos == null) {
				world = server.overworld();
				spawnPos = world.getSharedSpawnPos();
				angle = entity.getYRot();
			}
			FabricDimensions.teleport(entity, world, new PortalInfo(Vec3.atBottomCenterOf(spawnPos), Vec3.ZERO, angle, entity.getXRot()));
		}

		Arcanus.LOGGER.warn("Unable to teleport entity out of pocket dimension: {} ({})", entity.getScoreboardName(), entity.getUUID());
		return false;
	}

	public PocketDimensionPlot assignNewPlot(ServerLevel pocketDimension, GameProfile target, RandomSource random) {
		Preconditions.checkArgument(ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketWidth >= 1, "Pocket dimension plots must be at least 1x2, width is too small. Please fix the config values!");
		Preconditions.checkArgument(ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketHeight >= 2, "Pocket dimension plots must be at least 1x2, height is too small. Please fix the config values!");
		var pocketWidth = ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketWidth;
		var pocketHeight = ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketHeight;
		var halfWidth = Mth.ceil(pocketWidth / 2.0F);

		final BoundingBox originalBox = new BoundingBox(0, 0, 0, pocketWidth + 1, pocketHeight + 1, pocketWidth + 1).moved(-halfWidth, 0, -halfWidth);

		var worldBorder = pocketDimension.getWorldBorder();

		if (worldBorder.getAbsoluteMaxSize() - worldBorder.getWarningBlocks() - DIMENSION_PADDING_XZ < halfWidth) {
			Arcanus.LOGGER.error("Pocket dimension plot for player {} ({}) failed integrity check! world border too small!", target.getName(), target.getId());
			return PocketDimensionPlot.of(target.getId(), originalBox);
		}

		int maxOffsetXZ = worldBorder.getAbsoluteMaxSize() - worldBorder.getWarningBlocks() - halfWidth - DIMENSION_PADDING_XZ;

		var minY = pocketDimension.getMinBuildHeight() + DIMENSION_PADDING_Y;
		var maxY = pocketDimension.getMaxBuildHeight() - DIMENSION_PADDING_Y - pocketHeight;

		var existingPlotsWithSpacing = existingPlots.values().stream().map(existing -> existing.getBounds().inflatedBy(POCKET_MARGIN)).toList();

		// TODO better algorithm for finding plot spaces that does not rely on random
		BoundingBox box = originalBox;
		for (int attempts = 0; attempts < 100; attempts++) {
			if (isValidBounds(pocketDimension, box, worldBorder) && existingPlotsWithSpacing.stream().noneMatch(box::intersects)) {
				var plot = PocketDimensionPlot.of(target.getId(), box);
				existingPlots.put(plot.ownerId(), plot);
				return plot;
			}

			var dX = random.nextInt(Mth.floor(worldBorder.getCenterX()) - maxOffsetXZ, Mth.ceil(worldBorder.getCenterX()) + maxOffsetXZ);
			var dZ = random.nextInt(Mth.floor(worldBorder.getCenterZ()) - maxOffsetXZ, Mth.ceil(worldBorder.getCenterZ()) + maxOffsetXZ);
			var dY = random.nextInt(minY, maxY);
			box = originalBox.moved(dX, dY, dZ);
		}

		Arcanus.LOGGER.error("Unable to assign pocket dimension plot for player {} ({}) after 100 attempts, defaulting to center pos!", target.getName(), target.getId());
		return PocketDimensionPlot.of(target.getId(), originalBox);
	}

	private boolean isValidBounds(ServerLevel world, BoundingBox box, WorldBorder worldBorder) {
		var bottomY = world.getMinBuildHeight() + DIMENSION_PADDING_Y;
		// need to use logical height because mojank;
		// else we can get weirdness with chorus fruits etc
		var topY = world.getMinBuildHeight() + world.getLogicalHeight() - DIMENSION_PADDING_Y;

		return box.minY() >= bottomY && box.maxY() <= topY && worldBorder.isWithinBounds(AABB.of(box));
	}

	@Nullable
	public PocketDimensionPlot getAssignedPlotSpace(UUID target) {
		return existingPlots.get(target);
	}

	public enum RegenerateType {
		INTERIOR_ONLY,
		WALLS_ONLY,
		FULL;

		public boolean placeWalls() {
			return this != INTERIOR_ONLY;
		}

		public boolean clearInterior() {
			return this != WALLS_ONLY;
		}
	}

	/**
	 * fills the plot with empty space and builds walls around it
	 *
	 * @param regenerateType whether to force-replace the interior space and/or walls
	 */
	public boolean replacePlotSpace(UUID target, ServerLevel pocketDim, RegenerateType regenerateType) {
		var plot = getAssignedPlotSpace(target);

		// might happen if the command is ran before a player first enters their pocket dimension
		if (plot == null) {
			return false;
		}

		if (regenerateType.clearInterior()) {
			pocketDim.getEntitiesOfClass(Entity.class, AABB.of(plot.getBounds())).forEach(entity -> {
				if (PlayerHelper.isFakePlayer(entity) || !(entity instanceof ServerPlayer player)) {
					entity.discard();
					return;
				}

				var overworld = server.overworld();
				FabricDimensions.teleport(player, overworld, new PortalInfo(Vec3.atBottomCenterOf(overworld.getSharedSpawnPos()), Vec3.ZERO, overworld.getSharedSpawnAngle(), 0.0F));

				// TODO make translatable
				player.sendSystemMessage(Component.literal("The pocket dimension plot you were in has been cleared by an admin."));
			});
		}

		BlockPos.betweenClosedStream(plot.min(), plot.max()).forEach(pos -> {
			var isNotWall = pos.getX() != plot.min().getX() && pos.getX() != plot.max().getX() && pos.getY() != plot.min().getY() && pos.getY() != plot.max().getY() && pos.getZ() != plot.min().getZ() && pos.getZ() != plot.max().getZ();
			if (isNotWall) {
				if (regenerateType.clearInterior()) {
					Clearable.tryClear(pocketDim.getBlockEntity(pos));
					pocketDim.setBlock(pos, Blocks.AIR.defaultBlockState(), REPLACE_FLAGS);
				}
				return;
			}
			else if(!regenerateType.placeWalls()) {
				return;
			}

			pocketDim.setBlock(pos, ArcanusBlocks.UNBREAKABLE_MAGIC_BLOCK.get().defaultBlockState(), REPLACE_FLAGS);

			Optional.ofNullable(pocketDim.getBlockEntity(pos))
				.flatMap(ArcanusComponents.MAGIC_COLOR::maybeGet)
				.ifPresent(component -> component.setSourceId(target));
		});

		if(regenerateType.placeWalls()) {
			var center = plot.getBounds().getCenter().atY(plot.min().getY());
			var pos = center.mutable();
			for (int x = 0; x < 4; x++) {
				for (int z = 0; z < 4; z++) {
					pos.set(center.getX() + x - 2, center.getY(), center.getZ() + z - 2);

					if (x == 0) {
						switch (z) {
							case 0 ->
								pocketDim.setBlock(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().defaultBlockState().setValue(SpatialRiftExitEdgeBlock.CORNER, true), REPLACE_FLAGS);
							case 1, 2 ->
								pocketDim.setBlock(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().defaultBlockState().setValue(SpatialRiftExitEdgeBlock.FACING, Direction.WEST), REPLACE_FLAGS);
							case 3 ->
								pocketDim.setBlock(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().defaultBlockState().setValue(SpatialRiftExitEdgeBlock.FACING, Direction.WEST).setValue(SpatialRiftExitEdgeBlock.CORNER, true), REPLACE_FLAGS);
						}
					} else if (x == 3) {
						switch (z) {
							case 0 ->
								pocketDim.setBlock(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().defaultBlockState().setValue(SpatialRiftExitEdgeBlock.FACING, Direction.EAST).setValue(SpatialRiftExitEdgeBlock.CORNER, true), REPLACE_FLAGS);
							case 1, 2 ->
								pocketDim.setBlockAndUpdate(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().defaultBlockState().setValue(SpatialRiftExitEdgeBlock.FACING, Direction.EAST));
							case 3 ->
								pocketDim.setBlock(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().defaultBlockState().setValue(SpatialRiftExitEdgeBlock.FACING, Direction.SOUTH).setValue(SpatialRiftExitEdgeBlock.CORNER, true), REPLACE_FLAGS);
						}
					} else if (z == 0) {
						pocketDim.setBlock(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().defaultBlockState().setValue(SpatialRiftExitEdgeBlock.FACING, Direction.NORTH), REPLACE_FLAGS);
					} else if (z == 3) {
						pocketDim.setBlock(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().defaultBlockState().setValue(SpatialRiftExitEdgeBlock.FACING, Direction.SOUTH), REPLACE_FLAGS);
					} else {
						pocketDim.setBlock(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT.get().defaultBlockState().setValue(SpatialRiftExitBlock.ACTIVE, x == 1 && z == 1), REPLACE_FLAGS);
					}

					Optional.ofNullable(pocketDim.getBlockEntity(pos)).flatMap(ArcanusComponents.MAGIC_COLOR::maybeGet).ifPresent(component -> component.setSourceId(target));
				}
			}
		}

		BlockPos.betweenClosedStream(plot.min(), plot.max()).forEach(toUpdate -> {
			var block = pocketDim.getBlockState(toUpdate).getBlock();
			pocketDim.blockUpdated(toUpdate, block);
		});

		return true;
	}

	public void setExit(UUID ownerId, Level world, Vec3 pos) {
		exitSpot.put(ownerId, new Tuple<>(world.dimension(), pos));
	}
}
