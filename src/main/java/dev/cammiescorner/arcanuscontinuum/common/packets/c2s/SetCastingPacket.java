package dev.cammiescorner.arcanuscontinuum.common.packets.c2s;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class SetCastingPacket {
	public static final ResourceLocation ID = Arcanus.id("set_casting");

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
