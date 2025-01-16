package dev.cammiescorner.arcanus.fabric.common.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.superkat.explosiveenhancement.ExplosiveEnhancement;
import net.superkat.explosiveenhancement.ExplosiveEnhancementClient;
import net.superkat.explosiveenhancement.api.ExplosiveApi;

public class ExplosiveEnhancementCompat {
	public static void spawnEnhancedBooms(Level world, double x, double y, double z, float power, boolean didDestroyBlocks) {
		boolean isUnderWater = false;
		BlockPos pos = BlockPos.containing(x, y, z);

		if(ExplosiveEnhancementClient.config.underwaterExplosions && world.getFluidState(pos).is(FluidTags.WATER)) {
			isUnderWater = true;

			if(ExplosiveEnhancementClient.config.debugLogs)
				ExplosiveEnhancement.LOGGER.info("particle is underwater!");
		}

		ExplosiveApi.spawnParticles(world, x, y, z, power, isUnderWater, didDestroyBlocks, true);
	}
}
