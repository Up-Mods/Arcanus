package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.PocketDimensionPortalEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class PocketDimensionPortalComponent implements Component {
	private final Player player;
	private ResourceKey<Level> worldKey = Level.OVERWORLD;
	private UUID portalId = Util.NIL_UUID;
	private Vec3 portalPos = Vec3.ZERO;

	public PocketDimensionPortalComponent(Player player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		portalId = tag.getUUID("PortalId");
		worldKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("WorldKey")));
		portalPos = new Vec3(tag.getInt("PortalPosX"), tag.getInt("PortalPosY"), tag.getInt("PortalPosZ"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putUUID("PortalId", portalId);
		tag.putString("WorldKey", worldKey.location().toString());
		tag.putDouble("PortalPosX", portalPos.x());
		tag.putDouble("PortalPosY", portalPos.y());
		tag.putDouble("PortalPosZ", portalPos.z());
	}

	public void createPortal(ServerLevel world, Vec3 pos, double pullStrength) {
		MinecraftServer server = world.getServer();

		if(portalId != Util.NIL_UUID) {
			ServerLevel otherWorld = server.getLevel(worldKey);

			if(otherWorld != null) {
				Entity oldPortal = otherWorld.getEntity(portalId);

				if(oldPortal != null) {
					oldPortal.remove(Entity.RemovalReason.DISCARDED);
				}
			}
		}

		PocketDimensionPortalEntity portal = ArcanusEntities.PORTAL.get().create(world);

		if(portal != null) {
			portalId = portal.getUUID();
			worldKey = world.dimension();
			portalPos = pos;

			PocketDimensionComponent.get(world).setExit(player.getGameProfile().getId(), world, pos);
			portal.setProperties(player.getUUID(), pos, pullStrength);
			ArcanusHelper.copyMagicColor(portal, player);
			world.addFreshEntity(portal);
		}
	}

	public Vec3 getPortalPos() {
		return portalPos;
	}
}
