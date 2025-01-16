package dev.cammiescorner.arcanus.fabric.common.components.entity;

import com.mojang.datafixers.util.Pair;
import dev.cammiescorner.arcanus.fabric.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.PocketDimensionPortalEntity;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanus.fabric.common.util.ArcanusHelper;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PocketDimensionPortalComponent implements Component {
	public static final ResourceKey<Level> POCKET_DIMENSION_WORLD_KEY = ResourceKey.create(Registries.DIMENSION, Arcanus.id("pocket_dimension"));
	private final Player player;
	private final Map<ResourceKey<Level>, Pair<UUID, Vec3>> portalIds = new HashMap<>();

	public PocketDimensionPortalComponent(Player player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		ListTag list = tag.getList("PortalIds", Tag.TAG_COMPOUND);
		portalIds.clear();

		for(int i = 0; i < list.size(); i++) {
			CompoundTag nbt = list.getCompound(i);
			portalIds.put(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("WorldKey"))), new Pair<>(nbt.getUUID("PortalId"), new Vec3(tag.getInt("PortalPosX"), tag.getInt("PortalPosY"), tag.getInt("PortalPosZ"))));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		ListTag list = new ListTag();

		for(ResourceKey<Level> levelKey : portalIds.keySet()) {
			CompoundTag nbt = new CompoundTag();
			Pair<UUID, Vec3> pair = portalIds.get(levelKey);

			nbt.putString("WorldKey", levelKey.location().toString());
			nbt.putUUID("PortalId", pair.getFirst());
			tag.putDouble("PortalPosX", pair.getSecond().x());
			tag.putDouble("PortalPosY", pair.getSecond().y());
			tag.putDouble("PortalPosZ", pair.getSecond().z());
			list.add(nbt);
		}

		tag.put("PortalIds", list);
	}

	public void createPortal(ServerLevel level, Vec3 pos, double pullStrength) {
		MinecraftServer server = level.getServer();

		for(ResourceKey<Level> levelKey : portalIds.keySet()) {
			Pair<UUID, Vec3> pair = portalIds.get(levelKey);
			UUID portalId = pair.getFirst();

			if(portalId != Util.NIL_UUID) {
				ServerLevel otherWorld = server.getLevel(levelKey);

				if(otherWorld != null) {
					BlockPos blockPos = BlockPos.containing(pair.getSecond());

					otherWorld.getChunkSource().addRegionTicket(TicketType.PORTAL, SectionPos.of(blockPos).chunk(), 1, blockPos);

					Entity oldPortal = otherWorld.getEntity(portalId);

					if(oldPortal != null)
						oldPortal.remove(Entity.RemovalReason.DISCARDED);
				}
			}
		}

		PocketDimensionPortalEntity portal = ArcanusEntities.PORTAL.get().create(level);

		if(portal != null) {
			portalIds.put(level.dimension(), new Pair<>(portal.getUUID(), pos));

			if(level.dimension() != POCKET_DIMENSION_WORLD_KEY)
				PocketDimensionComponent.get(level).setExit(player.getGameProfile().getId(), level, pos);

			portal.setProperties(player.getUUID(), pos, pullStrength);
			ArcanusHelper.copyMagicColor(portal, player);
			level.addFreshEntity(portal);
		}
	}

	public Vec3 getPortalPos(Level level) {
		return portalIds.get(level.dimension()).getSecond();
	}
}
