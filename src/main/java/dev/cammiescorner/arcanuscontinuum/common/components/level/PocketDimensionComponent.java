package dev.cammiescorner.arcanuscontinuum.common.components.level;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.common.blocks.SpatialRiftExitBlock;
import dev.cammiescorner.arcanuscontinuum.common.blocks.SpatialRiftExitEdgeBlock;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.MagicBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.SpatialRiftExitBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.*;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.quiltmc.qsl.worldgen.dimension.api.QuiltDimensions;

import java.util.*;

public class PocketDimensionComponent implements Component {
	public static final RegistryKey<World> POCKET_DIM = RegistryKey.of(RegistryKeys.WORLD, Arcanus.id("pocket_dimension"));

	private static final int DIMENSION_PADDING_Y = 8;
	private static final int DIMENSION_PADDING_XZ = 24;
	private static final int POCKET_MARGIN = 24;

	private final Map<UUID, Box> existingPlots = new HashMap<>();
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
			serverWorld.getScoreboard().getComponent(ArcanusComponents.POCKET_DIMENSION_COMPONENT);
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
			NbtCompound entry = plotNbtList.getCompound(i);
			existingPlots.put(entry.getUuid("OwnerUuid"), new Box(entry.getInt("MinX"), entry.getInt("MinY"), entry.getInt("MinZ"), entry.getInt("MaxX"), entry.getInt("MaxY"), entry.getInt("MaxZ")));
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

		existingPlots.forEach((uuid, box) -> {
			NbtCompound entry = new NbtCompound();
			entry.putUuid("OwnerUuid", uuid);
			entry.putInt("MinX", (int) box.minX);
			entry.putInt("MinY", (int) box.minY);
			entry.putInt("MinZ", (int) box.minZ);
			entry.putInt("MaxX", (int) box.maxX);
			entry.putInt("MaxY", (int) box.maxY);
			entry.putInt("MaxZ", (int) box.maxZ);
			plotNbtList.add(entry);
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
	private static boolean chunksExist(Box plot, ServerWorld pocketDim) {
		var chunkManager = pocketDim.getChunkManager();

		return BlockPos.stream(plot).map(ChunkPos::new).distinct().map(cPos -> chunkManager.getWorldChunk(cPos.x, cPos.z, false)).noneMatch(Objects::isNull);
	}

	public void teleportToPocketDimension(ServerPlayerEntity ownerOfPocket, Entity entity) {
		if (!entity.getWorld().isClient()) {
			ServerWorld pocketDim = entity.getServer().getWorld(POCKET_DIM);
			if (pocketDim != null) {
				Box plot = existingPlots.get(ownerOfPocket.getUuid());
				if (plot == null) {
					plot = assignNewPlot(ownerOfPocket);
					generatePlotSpace(ownerOfPocket, pocketDim);
				} else if (!chunksExist(plot, pocketDim)) {
					Arcanus.LOGGER.warn("Pocket dimension plot for player {} failed integrity check! regenerating boundary...", ownerOfPocket.getGameProfile().getName());
					generatePlotSpace(ownerOfPocket, pocketDim);
				}

				QuiltDimensions.teleport(entity, pocketDim, new TeleportTarget(plot.getCenter().subtract(0, 11, 0), Vec3d.ZERO, entity.getYaw(), entity.getPitch()));
			}
		}
	}

	public void teleportOutOfPocketDimension(ServerPlayerEntity player) {
		if (player.getWorld().getRegistryKey() == POCKET_DIM) {
			Optional<Map.Entry<UUID, Box>> entry = existingPlots.entrySet().stream().filter(entry1 -> entry1.getValue().intersects(player.getBoundingBox())).findFirst();

			if (entry.isPresent()) {
				UUID ownerId = entry.get().getKey();
				Pair<RegistryKey<World>, Vec3d> pair = exitSpot.get(ownerId);

				ServerWorld targetWorld;
				Vec3d targetPos;

				if (pair == null || (targetWorld = player.getServer().getWorld(pair.getLeft())) == null) {
					targetWorld = player.getServer().getOverworld();
					targetPos = Vec3d.ofBottomCenter(targetWorld.getSpawnPos());
				} else {
					targetPos = pair.getRight();
				}

				ArcanusComponents.setPortalCoolDown(player, 200);
				QuiltDimensions.teleport(player, targetWorld, new TeleportTarget(targetPos, Vec3d.ZERO, player.getYaw(), player.getPitch()));
			} else {
				var spawnPos = player.getSpawnPointPosition();
				var angle = player.getSpawnAngle();
				var world = player.getServer().getWorld(player.getSpawnPointDimension());
				if (!player.isSpawnPointSet() || world == null || spawnPos == null) {
					world = player.getServer().getOverworld();
					spawnPos = world.getSpawnPos();
					angle = player.getYaw();
				}
				QuiltDimensions.teleport(player, world, new TeleportTarget(Vec3d.ofBottomCenter(spawnPos), Vec3d.ZERO, angle, player.getPitch()));
			}
		}
	}

	public Box assignNewPlot(ServerPlayerEntity player) {
		int pocketRadiusXZ = MathHelper.ceil(ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketWidth / 2f) + 1;
		int pocketRadiusY = MathHelper.ceil(ArcanusConfig.UtilityEffects.SpatialRiftEffectProperties.pocketHeight / 2f) + 1;
		final Box originalBox = new Box(-pocketRadiusXZ, -pocketRadiusY, -pocketRadiusXZ, pocketRadiusXZ, pocketRadiusY, pocketRadiusXZ);

		var serverWorld = player.getServerWorld();
		var worldBorder = serverWorld.getWorldBorder();

		if (worldBorder.getMaxRadius() - worldBorder.getWarningBlocks() - DIMENSION_PADDING_XZ < pocketRadiusXZ) {
			Arcanus.LOGGER.error("Pocket dimension plot for player {} ({}) failed integrity check! world border too small!", player.getGameProfile().getName(), player.getGameProfile().getId());
			return originalBox;
		}

		int maxOffsetXZ = worldBorder.getMaxRadius() - worldBorder.getWarningBlocks() - pocketRadiusXZ - DIMENSION_PADDING_XZ;

		var minY = serverWorld.getBottomY() + pocketRadiusY + DIMENSION_PADDING_Y;
		var maxY = serverWorld.getTopY() - pocketRadiusY - DIMENSION_PADDING_Y;

		var existingPlotsWithSpacing = existingPlots.values().stream().map(existing -> existing.expand(POCKET_MARGIN)).toList();

		// TODO better algorithm for finding plot spaces that does not rely on random
		Box box = originalBox;
		for (int attempts = 0; attempts < 100; attempts++) {
			if (isValidBounds(serverWorld, box, worldBorder) && existingPlotsWithSpacing.stream().noneMatch(box::intersects)) {
				existingPlots.put(player.getUuid(), box);
				return box;
			}

			var dX = player.getRandom().rangeClosed(MathHelper.floor(worldBorder.getCenterX()) - maxOffsetXZ, MathHelper.ceil(worldBorder.getCenterX()) + maxOffsetXZ);
			var dZ = player.getRandom().rangeClosed(MathHelper.floor(worldBorder.getCenterZ()) - maxOffsetXZ, MathHelper.ceil(worldBorder.getCenterZ()) + maxOffsetXZ);
			var dY = player.getRandom().rangeClosed(minY, maxY);
			box = originalBox.offset(dX, dY, dZ);
		}

		Arcanus.LOGGER.error("Unable to generate pocket dimension for player {} ({}) after 100 attempts, defaultin to center pos!", player.getEntityName(), player.getGameProfile().getId());
		return originalBox;
	}

	private boolean isValidBounds(ServerWorld world, Box box, WorldBorder worldBorder) {
		var bottomY = world.getBottomY();
		// need to use logical height because mojank;
		// else we can get weirdness with chorus fruits etc
		var topY = bottomY + world.getLogicalHeight();

		return box.minY >= bottomY && box.maxY <= topY && worldBorder.contains(box);
	}

	public boolean generatePlotSpace(PlayerEntity player, ServerWorld pocketDim) {
		var box = existingPlots.get(player.getUuid());

		// might happen if the command is ran before a player first enters their pocket dimension
		if (box == null) {
			return false;
		}

		var color = ArcanusHelper.getMagicColor(player);

		for (BlockPos pos : BlockPos.iterate((int) Math.round(box.minX), (int) Math.round(box.minY), (int) Math.round(box.minZ), (int) Math.round(box.maxX - 1), (int) Math.round(box.maxY - 1), (int) Math.round(box.maxZ - 1))) {
			if (pos.getX() > box.minX && pos.getX() < box.maxX - 1 && pos.getY() > box.minY && pos.getY() < box.maxY - 1 && pos.getZ() > box.minZ && pos.getZ() < box.maxZ - 1) {
				continue;
			}

			pocketDim.setBlockState(pos, ArcanusBlocks.UNBREAKABLE_MAGIC_BLOCK.get().getDefaultState());

			if (pocketDim.getBlockEntity(pos) instanceof MagicBlockEntity magicBlock) {
				magicBlock.setColor(color);
			}
		}

		for (int x = 0; x < 4; x++) {
			for (int z = 0; z < 4; z++) {
				BlockPos pos = new BlockPos((int) Math.round(box.getCenter().getX()) + (x - 2), (int) Math.round(box.minY), (int) Math.round(box.getCenter().getZ()) + (z - 2));

				if (x == 0) {
					switch (z) {
						case 0 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.CORNER, true));
						case 1, 2 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.WEST));
						case 3 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.WEST).with(SpatialRiftExitEdgeBlock.CORNER, true));
					}
				} else if (x == 3) {
					switch (z) {
						case 0 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.EAST).with(SpatialRiftExitEdgeBlock.CORNER, true));
						case 1, 2 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.EAST));
						case 3 ->
							pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.SOUTH).with(SpatialRiftExitEdgeBlock.CORNER, true));
					}
				} else if (z == 0) {
					pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.NORTH));
				} else if (z == 3) {
					pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get().getDefaultState().with(SpatialRiftExitEdgeBlock.FACING, Direction.SOUTH));
				} else {
					pocketDim.setBlockState(pos, ArcanusBlocks.SPATIAL_RIFT_EXIT.get().getDefaultState().with(SpatialRiftExitBlock.ACTIVE, x == 1 && z == 1));

					if (pocketDim.getBlockEntity(pos) instanceof SpatialRiftExitBlockEntity exitBlockEntity) {
						exitBlockEntity.setColor(color);
					}
				}

				if (pocketDim.getBlockEntity(pos) instanceof MagicBlockEntity magicBlock)
					magicBlock.setColor(color);
			}
		}

		return true;
	}

	public void setExit(PlayerEntity ownerOfPocket, World world, Vec3d pos) {
		exitSpot.put(ownerOfPocket.getUuid(), new Pair<>(world.getRegistryKey(), pos));
	}
}
