package dev.cammiescorner.arcanuscontinuum.common.packets.s2c;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.gui.screens.ArcaneWorkbenchScreen;
import dev.cammiescorner.arcanuscontinuum.common.util.WorkbenchMode;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SyncWorkbenchModePacket {
	public static final ResourceLocation ID = Arcanus.id("sync_workbench_mode");

	public static void send(ServerPlayer receiver, WorkbenchMode mode) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeVarInt(mode.ordinal());
		ServerPlayNetworking.send(receiver, ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
		int ordinal = buf.readVarInt();

		client.execute(() -> {
			if(client.screen instanceof ArcaneWorkbenchScreen screen) {
				WorkbenchMode mode = WorkbenchMode.values()[ordinal];
				screen.getMenu().setMode(mode);
				screen.rebuildWidgets();
			}
		});
	}
}
