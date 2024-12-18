package dev.cammiescorner.arcanuscontinuum.common.packets.s2c;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class SyncScalePacket {
	public static final ResourceLocation ID = Arcanus.id("sync_scale");

	public static void send(ServerPlayer receiver, Entity target, SpellEffect effect, double strength) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(target.getId());
		buf.writeBoolean(ArcanusSpellComponents.SHRINK.is(effect));
		buf.writeDouble(strength);
		ServerPlayNetworking.send(receiver, ID, buf);
	}

	@ClientOnly
	public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
		int entityId = buf.readInt();
		SpellEffect effect = buf.readBoolean() ?  ArcanusSpellComponents.SHRINK.get() : ArcanusSpellComponents.ENLARGE.get();
		double strength = buf.readDouble();

		client.execute(() ->
			ArcanusComponents.setScale(client.level.getEntity(entityId), effect, strength));
	}
}
