package dev.cammiescorner.arcanuscontinuum.common.packets.s2c;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SyncConfigValuesPacket {
	public static final ResourceLocation ID = Arcanus.id("sync_config_values");

	public static void send(ServerPlayer player) {
		FriendlyByteBuf buf = PacketByteBufs.create();

		buf.writeBoolean(ArcanusConfig.castingSpeedHasCoolDown);

		ServerPlayNetworking.send(player, ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
		boolean castingSpeedHasCoolDown = buf.readBoolean();

		client.execute(() -> ArcanusClient.castingSpeedHasCoolDown = castingSpeedHasCoolDown);
	}
}
