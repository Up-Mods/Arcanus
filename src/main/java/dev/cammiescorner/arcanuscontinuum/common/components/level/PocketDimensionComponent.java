package dev.cammiescorner.arcanuscontinuum.common.components.level;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.common.blocks.SpatialRiftExitBlock;
import dev.cammiescorner.arcanuscontinuum.common.blocks.SpatialRiftExitEdgeBlock;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.MagicBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.SpatialRiftExitBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.PlayerHelper;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Clearable;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.*;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.worldgen.dimension.api.QuiltDimensions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PocketDimensionComponent implements Component {
	public static final RegistryKey<World> POCKET_DIM = RegistryKey.of(RegistryKeys.WORLD, Arcanus.id("pocket_dimension"));

	private static final int REPLACE_FLAGS = Block.NOTIFY_LISTENERS | Block.SKIP_DROPS;
	private static final int DIMENSION_PADDING_Y = 8;
	private static final int DIMENSION_PADDING_XZ = 24;
	private static final int POCKET_MARGIN = 24;

	private final Map<UUID, PocketDimensionPlot> existingPlots = new HashMap<>();
	private final Map<UUID, Pair<RegistryKey<World>, Vec3d>> exitSpot = new HashMap<>();
	private final MinecraftServer server;

	public PocketDimensionComponent(Scoreboard scoreboard, MinecraftServer server) {
		this.server = server;
	}

	public static PocketDimensionComponent get(MinecraftServer server) {
		return server.getScoreboard().getComponent(ArcanusComponents.POCKET_DIMENSION_COMPONENT);
	}

	public static PocketDimensionComponent get(World world) {
		if (world instanceof ServerWorld serverWorld) {
			return serverWorld.getScoreboard().getComponent(ArcanusComponents.POCKET_DIMENSION_COMPONENT);
		}

		throw new IllegalStateException("World is not a ServerWorld!");
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList plotNbtList = tag.getList("PlotMap", NbtElement.COMPOUND_TYPE);
		NbtList exitNbtList = tag.getList("ExitSpots", NbtElement.COMPOUND_TYPE);

		existingPlots.clear();
		exitSpot.clear();

		for (int i = 0; i < plotNbtList.size(); i++) {
			var entry = PocketDimensionPlot.fromNbt(plotNbtList.getCompound(i));
			if (entry != null) {
				existingPlots.put(entry.ownerId(), entry);
			}
		}

		for (int i = 0; i < exitNbtList.size(); i++) {
			NbtCompound entry = exitNbtList.getCompound(i);
			exitSpot.put(entry.getUuid("EntityId"), new Pair<>(RegistryKey.of(RegistryKeys.WORLD, new Identifier(entry.getString("WorldKey"))), new Vec3d(entry.getDouble("X"), entry.getDouble("Y"), entry.getDouble("Z"))));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList plotNbtList = new NbtList();
		NbtList exitNbtList = new NbtList();

		existingPlots.forEach((uuid, plot) -> {
			plotNbtList.add(plot.toNbt());
		});

		exitSpot.forEach((uuid, pair) -> {
			NbtCompound entry = new NbtCompound();
			entry.putUuid("EntityId", uuid);
			entry.putString("WorldKey", pair.getLeft().getValue().toString());
			entry.putDouble("X", pair.getRight().getX());
			entry.putDouble("Y", pair.getRight().getY());
			entry.putDouble("Z", pair.getRight().getZ());
			exitNbtList.add(entry);
		});

		tag.put("PlotMap", plotNbtList);
		tag.put("ExitSpots", exitNbtList);
	}

	// FIXME this check is too eager sometimes.
	//  not too bad for the moment but should be looked into.
	private static boolean chunksExist(PocketDimensionPlot plot, ServerWorld pocketDim) {
		var chunkManager = pocketDim.getChunkManager();

		return BlockPos.stream(plot.min(), plot.max()).map(ChunkPos::new).distinct().map(cPos -> chunkManager.getWorldChunk(cPos.x, cPos.z, false)).noneMatch(Objects::isNull);
	}

	public void teleportToPocketDimension(GameProfile pocketOwner, Entity entity) {
		if (!entity.getWorld().isClient()) {
			ServerWorld pocketDim = server.getWorld(POCKET_DIM);
			if (pocketDim != null) {
				var plot = getAssignedPlotSpace(pocketOwner.getId());
				if (plot == null) {
					plot = assignNewPlot(pocketDim, pocketOwner, entity.getWorld().getRandom());
					replacePlotSpace(pocketOwner.getId(), pocketDim, false);
				} else if (!chunksExist(plot, pocketDim)) {
					Arcanus.LOGGER.warn("Pocket dimension plot for player {} ({}) failed integrity check! regenerating boundary...", pocketOwner.getName(), pocketOwner.getId());
					replacePlotSpace(pocketOwner.getId(), pocketDim, false);
				}

				var bottomCenterPos = Vec3d.ofBottomCenter(plot.getBounds().getCenter().withY(plot.min().getY() + 1));
				QuiltDimensions.teleport(entity, pocketDim, new TeleportTarget(bottomCenterPos, Vec3d.ZERO, entity.getYaw(), entity.getPitch()));
			}
		}
	}

	public boolean teleportOutOfPocketDimension(Entity entity) {
		if (PlayerHelper.isFakePlayer(entity) || entity.getWorld().isClient() || entity.getWorld().getRegistryKey() != POCKET_DIM) {
			return false;
		}

		ArcanusComponents.setPortalCoolDown(entity, 200);

		UUID ownerId = existingPlots.values().stream().filter(plot -> entity.getBoundingBox().intersects(Box.from(plot.getBounds()))).map(PocketDimensionPlot::ownerId).findFirst().orElse(null);
		if (ownerId != null) {
			Pair<RegistryKey<World>, Vec3d> pair = exitSpot.get(ownerId);
			if (pair != null) {
				ServerWorld targetWorld = server.getWorld(pair.getLeft());
				Vec3d targetPos = pair.getRight();

				if (targetWorld == null) {
					Arcanus.LOGGER.error("Unable to find dimension {}, defaulting to overworld", pair.getLeft().getValue());
					targetWorld = server.getOverworld();
					targetPos = Vec3d.ofBottomCenter(targetWorld.getSpawnPos());
				}

				QuiltDimensions.teleport(entity, targetWorld, new TeleportTarget(targetPos, Vec3d.ZERO, entity.getYaw(), entity.getPitch()));
				return true;
			}
		}

		if (entity instanceof ServerPlayerEntity player) {
			var profile = player.getGameProfile();
			Arcanus.LOGGER.warn("Failed to determine pocket dimension exit spot for player {} ({}), sending them to their spawn position!", profile.getName(), profile.getId());

			var spawnPos = player.getSpawnPointPosition();
			var angle = player.getSpawnAngle();
			var world = server.getWorld(player.getSpawnPointDimension());
			if (!player.isSpawnPointSet() || world == null || spawnPos == null) {
				world = server.getOverworld();
				spawnPos = world.getSpawnPos();
				angle = entity.getYaw();
			}
			QuiltDimensions.teleport(entity, world, new TeleportTarget(Vec3d.ofBottomCenter(spawnPos), Vec3d.ZERO, angle, entity.getPitch()));
		}

		Arcanus.LOGGER.warn("Unable to teleport entity out of pocket dimension: {} ({})", entity.getEntityName(), entity.getUuid());
		return false;
	}

	public PocketDimensionPlot assignNewPlot(ServerWorld pocketDimension, GameProfile target, RandomGenerator random) {
		Preconditions.checkArgument(ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketWidth >= 1, "Pocket dimension plots must be at least 1x2, width is too small. Please fix the config values!");
		Preconditions.checkArgument(ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketHeight >= 2, "Pocket dimension plots must be at least 1x2, height is too small. Please fix the config values!");
		var pocketWidth = ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketWidth;
		var pocketHeight = ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketHeight;
		var halfWidth = MathHelper.ceil(pocketWidth / 2.0F);

		final BlockBox originalBox = new BlockBox(0, 0, 0, pocketWidth + 1, pocketHeight + 1, pocketWidth + 1).offset(-halfWidth, 0, -halfWidth);

		var worldBorder = pocketDimension.getWorldBorder();

		if (worldBorder.getMaxRadius() - worldBorder.getWarningBlocks() - DIMENSION_PADDING_XZ < halfWidth) {
			Arcanus.LOGGER.error("Pocket dimension plot for player {} ({}) failed integrity check! world border too small!", target.getName(), target.getId());
			return PocketDimensionPlot.of(target.getId(), originalBox);
		}

		int maxOffsetXZ = worldBorder.getMaxRadius() - worldBorder.getWarningBlocks() - halfWidth - DIMENSION_PADDING_XZ;

		var minY = pocketDimension.getBottomY() + DIMENSION_PADDING_Y;
		var maxY = pocketDimension.getTopY() - DIMENSION_PADDING_Y - pocketHeight;

		var existingPlotsWithSpacing = existingPlots.values().stream().map(existing -> existing.getBounds().expand(POCKET_MARGIN)).toList();

		// TODO better algorithm for finding plot spaces that does not rely on random
		BlockBox box = originalBox;
		for (int attempts = 0; attempts < 100; attempts++) {
			if (isValidBounds(pocketDimension, box, worldBorder) && existingPlotsWithSpacing.stream().noneMatch(box::intersects)) {
				var plot = PocketDimensionPlot.of(target.getId(), box);
				existingPlots.put(plot.ownerId(), plot);
				return plot;
			}

			var dX = random.rangeClosed(MathHelper.floor(worldBorder.getCenterX()) - maxOffsetXZ, MathHelper.ceil(worldBorder.getCenterX()) + maxOffsetXZ);
			var dZ = random.rangeClosed(MathHelper.floor(worldBorder.getCenterZ()) - maxOffsetXZ, MathHelper.ceil(worldBorder.getCenterZ()) + maxOffsetXZ);
			var dY = random.rangeClosed(minY, maxY);
			box = originalBox.offset(dX, dY, dZ);
		}

		Arcanus.LOGGER.error("Unable to assign pocket dimension plot for player {} ({}) after 100 attempts, defaulting to center pos!", target.getName(), target.getId());
		return PocketDimensionPlot.of(target.getId(), originalBox);
	}

	private boolean isValidBounds(ServerWorld world, BlockBox box, WorldBorder worldBorder) {
		var bottomY = world.getBottomY() + DIMENSION_PADDING_Y;
		// need to use logical height because mojank;
		// else we can get weirdness with chorus fruits etc
		var topY = world.getBottomY() + world.getLogicalHeight() - DIMENSION_PADDING_Y;

		return box.getMinY() >= bottomY && box.getMaxY() <= topY && worldBorder.contains(Box.from(box));
	}

	@Nullable
	public PocketDimensionPlot getAssignedPlotSpace(UUID target) {
		return existingPlots.get(target);
	}

	/**
	 * fills the plot with empty space and builds walls around it
	 *
	 * @param clear whether to remove existing blocks and entities
	 */
	public boolean replacePlotSpace(UUID target, ServerWorld pocketDim, boolean clear) {
		var plot = getAssignedPlotSpace(target);

		// might happen if the command is ran before a player first enters their pocket dimension
		if (plot == null) {
			return false;
		}

		if (clear) {
			pocketDim.getNonSpectatingEntities(Entity.class, Box.from(plot.getBounds())).forEach(entity -> {
				if (PlayerHelper.isFakePlayer(entity) || !(entity instanceof ServerPlayerEntity player)) {
					entity.discard();
					return;
				}

				var overworld = server.getOverworld();
				QuiltDimensions.teleport(player, overworld, new TeleportTarget(Vec3d.ofBottomCenter(overworld.getSpawnPos()), Vec3d.ZERO, overworld.getSpawnAngle(), 0.0F));

				// TODO make translatable
				player.sendSystemMessage(Text.literal("The pocket dimension plot you were in has been cleared by an admin."));
			});
		}

		var color = ArcanusHelper.getMagicColor(target);

		BlockPos.stream(plot.min(), plot.max()).forEach(pos -> {
			var isNotWall = pos.getX() != plot.min().getX() && pos.getX() != plot.max().getX() && pos.getY() != plot.min().getY() && pos.getY() != plot.max().getY() && pos.getZ() != plot.min().getZ() && pos.getZ() != plot.max().getZ();
			if (isNotWall) {
				if (clear) {
					Clearable.clear(pocketDim.getBlockEntity(pos));
					pocketDim.setBlockState(pos, Blocks.AIR.getDefaultState(), REPLACE_FLAGS);
				}
				return;
			}

			pocketDim.setBlockState(pos, ArcanusBlocks.UNBREAKABLE_MAGIC_BLOCK.get().getDefaultState(), REPLACE_FLAGS);

			if (pocketDim.getBlockEntity(pos) instanceof MagicBlockEntity magicBlock) {
				magicBlock.setColor(color);
			}
		});

		var center = plot.getBounds().getCenter().withY(plot.min().getY());
		var pos = center.mutableCopy();
		for (int x = 0; x < 4; x++) {
			for (int z = 0; z < 4; z++) {
				pos.set(center.getX() + x - 2, center.getY(), center.getZ() + z - 2);

				if (x == 0) {
					switch (z) {
						case 0 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.CORNER, true), REPLACE_FLAGS);
						case 1, 2 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.WEST), REPLACE_FLAGS);
						case 3 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.WEST).with(SpatialRiftExitEdgeBlock.CORNER, true), REPLACE_FLAGS);
					}
				} else if (x == 3) {
					switch (z) {
						case 0 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.EAST).with(SpatialRiftExitEdgeBlock.CORNER, true), REPLACE_FLAGS);
						case 1, 2 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.EAST));
						case 3 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.SOUTH).with(SpatialRiftExitEdgeBlock.CORNER, true), REPLACE_FLAGS);
					}
				} else if (z == 0) {
					pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.NORTH), REPLACE_FLAGS);
				} else if (z == 3) {
					pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.SOUTH), REPLACE_FLAGS);
				} else {
					pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT.get().getDefaultState().with(SpatialRiftExitBlock.ACTIVE, x == 1 && z == 1), REPLACE_FLAGS);

					if (pocketDim.getBlockEntity(pos) instanceof SpatialRiftExitBlockEntity exitBlockEntity) {
						exitBlockEntity.setColor(color);
					}
				}

				if (pocketDim.getBlockEntity(pos) instanceof MagicBlockEntity magicBlock) {
					magicBlock.setColor(color);
				}
			}
		}

		BlockPos.stream(plot.min(), plot.max()).forEach(toUpdate -> {
			var block = pocketDim.getBlockState(toUpdate).getBlock();
			pocketDim.updateNeighbors(toUpdate, block);
		});

		return true;
	}

	public void setExit(UUID ownerId, World world, Vec3d pos) {
		exitSpot.put(ownerId, new Pair<>(world.getRegistryKey(), pos));
	}
}
