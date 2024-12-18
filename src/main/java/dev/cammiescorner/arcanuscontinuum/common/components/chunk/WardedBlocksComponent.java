package dev.cammiescorner.arcanuscontinuum.common.components.chunk;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.api.EnvType;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WardedBlocksComponent implements AutoSyncedComponent {
	private final Map<BlockPos, UUID> wardedBlocks = new HashMap<>();
	private final ChunkAccess chunk;

	public WardedBlocksComponent(ChunkAccess chunk) {
		this.chunk = chunk;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		ListTag nbtList = tag.getList("WardedBlocksMap", Tag.TAG_COMPOUND);
		wardedBlocks.clear();

		for(int i = 0; i < nbtList.size(); i++) {
			CompoundTag compound = nbtList.getCompound(i);
			ListTag blockPosList = compound.getList("BlockPosList", Tag.TAG_COMPOUND);
			UUID ownerUuid = compound.getUUID("OwnerUuid");

			// make sure we have the data cached when we need it
			if(MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT) {
				Arcanus.WIZARD_DATA.get(ownerUuid);
			}

			for(int j = 0; j < blockPosList.size(); j++)
				wardedBlocks.put(NbtUtils.readBlockPos(blockPosList.getCompound(j)), ownerUuid);
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag nbtList = new ListTag();
		Map<UUID, ListTag> map = new HashMap<>();

		wardedBlocks.forEach((blockPos, uuid) -> map.computeIfAbsent(uuid, uuid1 -> new ListTag()).add(NbtUtils.writeBlockPos(blockPos)));

		map.forEach((uuid, nbt) -> {
			CompoundTag compound = new CompoundTag();
			compound.putUUID("OwnerUuid", uuid);
			compound.put("BlockPosList", nbt);

			nbtList.add(compound);
		});

		tag.put("WardedBlocksMap", nbtList);
	}

	public void addWardedBlock(Player player, BlockPos pos) {
		if(!isBlockWarded(pos)) {
			wardedBlocks.put(pos, player.getUUID());
			chunk.syncComponent(ArcanusComponents.WARDED_BLOCKS_COMPONENT);
			chunk.setUnsaved(true);
		}
	}

	public void removeWardedBlock(Player player, BlockPos pos) {
		boolean canOtherPlayersRemoveBlock = ArcanusConfig.UtilityEffects.WardingEffectProperties.canBeRemovedByOthers;

		if(canOtherPlayersRemoveBlock || isOwnerOfBlock(player, pos)) {
			wardedBlocks.remove(pos);
			chunk.syncComponent(ArcanusComponents.WARDED_BLOCKS_COMPONENT);
			chunk.setUnsaved(true);
		}
	}

	public boolean isOwnerOfBlock(Player player, BlockPos pos) {
		return wardedBlocks.getOrDefault(pos, Util.NIL_UUID).equals(player.getUUID());
	}

	public boolean isBlockWarded(BlockPos pos) {
		return wardedBlocks.containsKey(pos);
	}

	public Map<BlockPos, UUID> getWardedBlocks() {
		return Collections.unmodifiableMap(wardedBlocks);
	}
}
