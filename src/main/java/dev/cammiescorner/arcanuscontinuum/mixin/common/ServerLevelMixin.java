package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
	protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) { super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates); }

	@WrapWithCondition(method = "method_31420", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/server/level/ServerLevel;guardEntityTick(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/Entity;)V"
	))
	private boolean slowTime(ServerLevel instance, Consumer<?> consumer, Entity entity) {
		// TODO tie to being in a time dilation entity
		if(entity.position().distanceTo(new Vec3(0, 146, 0)) < 16 && getGameTime() % 2 == 0) {
			entity.setOldPosAndRot();
			entity.tickCount++;
			return false;
		}

		return true;
	}
}
