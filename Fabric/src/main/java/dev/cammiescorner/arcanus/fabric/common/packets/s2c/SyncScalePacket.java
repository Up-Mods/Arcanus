package dev.cammiescorner.arcanus.fabric.common.packets.s2c;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.api.spells.SpellEffect;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusSpellComponents;
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
import net.minecraft.world.entity.Entity;

public class SyncScalePacket {
	public static final ResourceLocation ID = FabricMain.id("sync_scale");

	public static void send(ServerPlayer receiver, Entity target, SpellEffect effect, double strength) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(target.getId());
		buf.writeBoolean(ArcanusSpellComponents.SHRINK.is(effect));
		buf.writeDouble(strength);
		ServerPlayNetworking.send(receiver, ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
		int entityId = buf.readInt();
		SpellEffect effect = buf.readBoolean() ? ArcanusSpellComponents.SHRINK.get() : ArcanusSpellComponents.ENLARGE.get();
		double strength = buf.readDouble();

		client.execute(() ->
			ArcanusComponents.setScale(client.level.getEntity(entityId), effect, strength));
	}
}
