package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.blocks.MagicDoorBlock;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.MagicDoorBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusPointsOfInterest;
import net.minecraft.block.BlockState;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedChatMessage;
import net.minecraft.registry.LayeredRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.ServerRegistryLayer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Shadow
	@Final
	private LayeredRegistryManager<ServerRegistryLayer> registryManager;

	@ModifyReceiver(method = "sendChatMessage(Lnet/minecraft/network/message/SignedChatMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
	private List<ServerPlayerEntity> arcanuscontinuum$restrictMagicDoorChatMessage(List<ServerPlayerEntity> original, SignedChatMessage chatMessage, Predicate<ServerPlayerEntity> predicate, @Nullable ServerPlayerEntity player, MessageType.Parameters parameters) {
		if (player != null && this.registryManager.getCompositeManager().get(RegistryKeys.MESSAGE_TYPE).getKey(parameters.messageType()).map(key -> key.equals(MessageType.CHAT)).orElse(false)) {
			ServerWorld world = player.getServerWorld();
			PointOfInterestStorage poiStorage = world.getChunkManager().getPointOfInterestStorage();

			var beep = new boolean[1];

			poiStorage.getInCircle(poiTypeHolder -> poiTypeHolder.isRegistryKey(ArcanusPointsOfInterest.MAGIC_DOOR), player.getBlockPos(), 8, PointOfInterestStorage.OccupationStatus.ANY).map(PointOfInterest::getPos).forEach(pos -> {
				BlockState state = world.getBlockState(pos);
				if (state.getBlock() instanceof MagicDoorBlock doorBlock && world.getBlockEntity(pos) instanceof MagicDoorBlockEntity door) {
					if (chatMessage.getContent().equalsIgnoreCase(door.getPassword())) {
						doorBlock.setOpen(null, world, state, pos, true);
						player.sendMessage(Arcanus.translate("door", "access_granted").formatted(Formatting.GRAY, Formatting.ITALIC), true);
						beep[0] = true;
					}
				}
			});

			if (beep[0]) {
				return List.of();
			}
		}
		return original;
	}
}
