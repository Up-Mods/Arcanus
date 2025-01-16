package dev.cammiescorner.arcanus.fabric.common.packets.c2s;

import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class SetCastingPacket {
	public static final ResourceLocation ID = FabricMain.id("set_casting");

	public static void send(boolean casting) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBoolean(casting);
		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
		boolean isCasting = buf.readBoolean();

		server.execute(() -> {
			if(!isCasting)
				ArcanusComponents.clearPattern(player);

			ArcanusComponents.setCasting(player, isCasting);
		});
	}
}
