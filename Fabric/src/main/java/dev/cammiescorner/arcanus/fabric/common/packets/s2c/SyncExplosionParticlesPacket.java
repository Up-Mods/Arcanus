package dev.cammiescorner.arcanus.fabric.common.packets.s2c;

import dev.cammiescorner.arcanus.fabric.common.compat.ArcanusCompat;
import dev.cammiescorner.arcanus.fabric.common.compat.ExplosiveEnhancementCompat;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class SyncExplosionParticlesPacket {
	public static final ResourceLocation ID = FabricMain.id("sync_explosion_particles");

	public static void send(ServerPlayer player, double x, double y, double z, float strength, boolean didDestroyBlocks) {
		FriendlyByteBuf buf = PacketByteBufs.create();

		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(strength);
		buf.writeBoolean(didDestroyBlocks);

		ServerPlayNetworking.send(player, ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		float strength = buf.readFloat();
		boolean didDestroyBlocks = buf.readBoolean();

		client.execute(() -> {
			Level world = client.level;

			if(ArcanusCompat.EXPLOSIVE_ENHANCEMENT.isEnabled())
				ExplosiveEnhancementCompat.spawnEnhancedBooms(world, x, y, z, strength, didDestroyBlocks);
			else
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 1, 1);
		});
	}
}
