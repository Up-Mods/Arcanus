package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.PocketDimensionPortalEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class PocketDimensionPortalComponent implements Component {
	private final PlayerEntity player;
	private RegistryKey<World> worldKey = World.OVERWORLD;
	private UUID portalId = Util.NIL_UUID;
	private Vec3d portalPos = Vec3d.ZERO;

	public PocketDimensionPortalComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		portalId = tag.getUuid("PortalId");
		worldKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(tag.getString("WorldKey")));
		portalPos = new Vec3d(tag.getInt("PortalPosX"), tag.getInt("PortalPosY"), tag.getInt("PortalPosZ"));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putUuid("PortalId", portalId);
		tag.putString("WorldKey", worldKey.getValue().toString());
		tag.putDouble("PortalPosX", portalPos.getX());
		tag.putDouble("PortalPosY", portalPos.getY());
		tag.putDouble("PortalPosZ", portalPos.getZ());
	}

	public void createPortal(ServerWorld world, Vec3d pos, double pullStrength) {
		MinecraftServer server = world.getServer();

		if(portalId != Util.NIL_UUID) {
			ServerWorld otherWorld = server.getWorld(worldKey);

			if(otherWorld != null) {
				Entity oldPortal = otherWorld.getEntity(portalId);

				if(oldPortal != null) {
					oldPortal.remove(Entity.RemovalReason.DISCARDED);
				}
			}
		}

		PocketDimensionPortalEntity portal = ArcanusEntities.PORTAL.get().create(world);

		if(portal != null) {
			portalId = portal.getUuid();
			worldKey = world.getRegistryKey();
			portalPos = pos;

			PocketDimensionComponent.get(world).setExit(player.getGameProfile().getId(), world, pos);
			portal.setProperties(player.getUuid(), pos, pullStrength);
			ArcanusHelper.copyMagicColor(portal, player);
			world.spawnEntity(portal);
		}
	}

	public Vec3d getPortalPos() {
		return portalPos;
	}
}
