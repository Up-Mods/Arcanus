package dev.cammiescorner.arcanuscontinuum.common.packets.s2c;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class SyncConfigValuesPacket {
	public static final Identifier ID = Arcanus.id("sync_config_values");

	public static void send(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();

		buf.writeBoolean(ArcanusConfig.castingSpeedHasCoolDown);

		ServerPlayNetworking.send(player, ID, buf);
	}

	@ClientOnly
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		boolean castingSpeedHasCoolDown = buf.readBoolean();

		client.execute(() -> ArcanusClient.castingSpeedHasCoolDown = castingSpeedHasCoolDown);
	}
}
