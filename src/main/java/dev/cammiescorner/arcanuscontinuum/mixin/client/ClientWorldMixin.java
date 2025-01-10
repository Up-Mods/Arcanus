package dev.cammiescorner.arcanuscontinuum.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin extends Level {
	protected ClientWorldMixin(WritableLevelData worldProperties, ResourceKey<Level> registryKey, RegistryAccess registryManager, Holder<DimensionType> dimension, Supplier<ProfilerFiller> profiler, boolean client, boolean debug, long seed, int maxChainedNeighborUpdates) {
		super(worldProperties, registryKey, registryManager, dimension, profiler, client, debug, seed, maxChainedNeighborUpdates);
	}

	@WrapWithCondition(method = "levelEvent", at = @At(value = "INVOKE",
													   target = "Lnet/minecraft/client/renderer/LevelRenderer;levelEvent(ILnet/minecraft/core/BlockPos;I)V"
	))
	private boolean arcanuscontinuum$noBreakingSoundsOrParticles(LevelRenderer target, int eventId, BlockPos pos, int data) {
		return eventId != 2001 || !ArcanusComponents.isBlockWarded(this, pos);
	}

	@WrapWithCondition(method = "tickNonPassenger", at = @At(value = "INVOKE",
															 target = "Lnet/minecraft/world/entity/Entity;tick()V"
	))
	private boolean arcanuscontinuum$blockEntityTick(Entity entity) {
		return !ArcanusComponents.areUpdatesBlocked(entity);
	}
}
