package dev.cammiescorner.arcanuscontinuum.common.packets.c2s;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.AggressorbEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShootOrbsPacket {
	public static final ResourceLocation ID = Arcanus.id("shoot_orb");

	public static void send(List<UUID> orbIds, UUID ownerId) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

		buf.writeUUID(ownerId);
		buf.writeInt(orbIds.size());

		for(UUID orbId : orbIds)
			buf.writeUUID(orbId);

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
		List<UUID> orbIds = new ArrayList<>();
		UUID ownerId = buf.readUUID();
		int orbCount = buf.readInt();

		for(int i = 0; i < orbCount; i++)
			orbIds.add(buf.readUUID());

		server.execute(() -> {
			ServerLevel world = player.serverLevel();
			Entity owner = world.getEntity(ownerId);

			if(owner instanceof LivingEntity livingEntity)
				shootOrb(orbIds, world, livingEntity);
		});
	}

	private static void shootOrb(List<UUID> orbIds, ServerLevel world, LivingEntity owner) {
		for(UUID orbId : orbIds) {
			if(world.getEntity(orbId) instanceof AggressorbEntity orb && owner != null && orb.isBoundToTarget()) {
				orb.setBoundToTarget(false);
				orb.setPos(orb.getTarget().getEyePosition());
				orb.shootFromRotation(orb.getTarget(), orb.getTarget().getXRot(), orb.getTarget().getYRot(), 0F, ArcanusConfig.SpellShapes.AggressorbShapeProperties.projectileSpeed, 1F);
				world.playSeededSound(null, orb.getX(), orb.getY(), orb.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1f, 1f, 1L);
				ArcanusComponents.removeAggressorbFromEntity(orb.getTarget(), orbId);

				break;
			}
		}
	}
}
