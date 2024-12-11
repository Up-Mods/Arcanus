package dev.cammiescorner.arcanuscontinuum.common.packets.s2c;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.client.gui.screens.ArcaneWorkbenchScreen;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.WorkbenchMode;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class SyncScalePacket {
	public static final Identifier ID = Arcanus.id("sync_scale");

	public static void send(ServerPlayerEntity receiver, Entity target, SpellEffect effect, double strength) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(target.getId());
		buf.writeBoolean(ArcanusSpellComponents.SHRINK.is(effect));
		buf.writeDouble(strength);
		ServerPlayNetworking.send(receiver, ID, buf);
	}

	@ClientOnly
	public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
		int entityId = buf.readInt();
		SpellEffect effect = buf.readBoolean() ?  ArcanusSpellComponents.SHRINK.get() : ArcanusSpellComponents.ENLARGE.get();
		double strength = buf.readDouble();

		client.execute(() ->
			ArcanusComponents.setScale(client.world.getEntityById(entityId), effect, strength));
	}
}
