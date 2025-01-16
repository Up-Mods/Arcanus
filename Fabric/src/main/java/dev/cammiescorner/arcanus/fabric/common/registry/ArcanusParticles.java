package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class ArcanusParticles {
	public static final RegistryHandler<ParticleType<?>> PARTICLE_TYPES = RegistryHandler.create(Registries.PARTICLE_TYPE, FabricMain.MOD_ID);

	public static final RegistrySupplier<SimpleParticleType> COLLAPSE = PARTICLE_TYPES.register("collapse", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> SPEED = PARTICLE_TYPES.register("speed", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> EXCHANGE = PARTICLE_TYPES.register("exchange", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> BOUNCY = PARTICLE_TYPES.register("bouncy", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> FEATHER = PARTICLE_TYPES.register("feather", () -> FabricParticleTypes.simple(true));

}
