package dev.cammiescorner.arcanuscontinuum.common.packets.s2c;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.compat.ExplosiveEnhancementCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class SyncExplosionParticlesPacket {
	public static final ResourceLocation ID = Arcanus.id("sync_explosion_particles");

	public static void send(ServerPlayer player, double x, double y, double z, float strength, boolean didDestroyBlocks) {
		FriendlyByteBuf buf = PacketByteBufs.create();

		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(strength);
		buf.writeBoolean(didDestroyBlocks);

		ServerPlayNetworking.send(player, ID, buf);
	}

	@ClientOnly
	public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		float strength = buf.readFloat();
		boolean didDestroyBlocks = buf.readBoolean();

		client.execute(() -> {
			Level world = client.level;

			if(QuiltLoader.isModLoaded("explosiveenhancement"))
				ExplosiveEnhancementCompat.spawnEnhancedBooms(world, x, y, z, strength, didDestroyBlocks);
			else
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 1, 1);
		});
	}
}
