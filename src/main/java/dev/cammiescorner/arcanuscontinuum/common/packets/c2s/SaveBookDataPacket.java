package dev.cammiescorner.arcanuscontinuum.common.packets.c2s;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.common.items.SpellBookItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;

public class SaveBookDataPacket {
	public static final ResourceLocation ID = Arcanus.id("save_book_data");

	public static void send(BlockPos pos, Spell spell) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(pos);
		buf.writeNbt(spell.toNbt());
		ClientPlayNetworking.send(ID, buf);
	}

	public static void handler(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender sender) {
		BlockPos pos = buf.readBlockPos();
		CompoundTag spellNbt = buf.readNbt();

		server.execute(() -> {
			ServerLevel world = player.serverLevel();

			if(world.getBlockEntity(pos) instanceof LecternBlockEntity lectern && lectern.getBook().getItem() instanceof SpellBookItem) {
				lectern.getBook().getOrCreateTag().put("Spell", spellNbt);

				for(ServerPlayer serverPlayer : world.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false))
					serverPlayer.connection.send(ClientboundBlockEntityDataPacket.create(lectern, BlockEntity::saveWithoutMetadata));
			}
		});
	}
}
