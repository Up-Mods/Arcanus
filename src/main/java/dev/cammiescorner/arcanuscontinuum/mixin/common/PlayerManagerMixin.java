package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import dev.cammiescorner.arcanuscontinuum.common.blocks.MagicDoorBlock;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.MagicDoorBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusPointsOfInterest;
import net.minecraft.ChatFormatting;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Predicate;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
	@Shadow
	@Final
	private LayeredRegistryAccess<RegistryLayer> registries;

	@ModifyReceiver(method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
	private List<ServerPlayer> arcanuscontinuum$restrictMagicDoorChatMessage(List<ServerPlayer> original, PlayerChatMessage chatMessage, Predicate<ServerPlayer> predicate, @Nullable ServerPlayer player, ChatType.Bound parameters) {
		if(player != null && this.registries.compositeAccess().registryOrThrow(Registries.CHAT_TYPE).getResourceKey(parameters.chatType()).map(key -> key.equals(ChatType.CHAT)).orElse(false)) {
			ServerLevel world = player.serverLevel();
			PoiManager poiStorage = world.getChunkSource().getPoiManager();

			var beep = new boolean[1];

			poiStorage.getInRange(poiTypeHolder -> poiTypeHolder.is(ArcanusPointsOfInterest.MAGIC_DOOR), player.blockPosition(), 8, PoiManager.Occupancy.ANY).map(PoiRecord::getPos).forEach(pos -> {
				BlockState state = world.getBlockState(pos);
				if(state.getBlock() instanceof MagicDoorBlock doorBlock && world.getBlockEntity(pos) instanceof MagicDoorBlockEntity door) {
					if(chatMessage.signedContent().equalsIgnoreCase(door.getPassword())) {
						doorBlock.setOpen(null, world, state, pos, true);
						player.displayClientMessage(Component.translatable("door.arcanuscontinuum.access_granted").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), true);
						beep[0] = true;
					}
				}
			});

			if(beep[0]) {
				return List.of();
			}
		}
		return original;
	}
}
