package dev.cammiescorner.arcanuscontinuum.common.packets.s2c;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.compat.ExplosiveEnhancementCompat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class SyncExplosionParticlesPacket {
	public static final Identifier ID = Arcanus.id("sync_explosion_particles");

	public static void send(ServerPlayerEntity player, double x, double y, double z, float strength, boolean didDestroyBlocks) {
		PacketByteBuf buf = PacketByteBufs.create();

		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(strength);
		buf.writeBoolean(didDestroyBlocks);

		ServerPlayNetworking.send(player, ID, buf);
	}

	@ClientOnly
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		float strength = buf.readFloat();
		boolean didDestroyBlocks = buf.readBoolean();

		client.execute(() -> {
			World world = client.world;

			if(QuiltLoader.isModLoaded("explosiveenhancement"))
				ExplosiveEnhancementCompat.spawnEnhancedBooms(world, x, y, z, strength, didDestroyBlocks);
			else
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 1, 1);
		});
	}
}
