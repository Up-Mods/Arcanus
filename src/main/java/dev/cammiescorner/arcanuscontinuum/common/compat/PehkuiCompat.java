package dev.cammiescorner.arcanuscontinuum.common.compat;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.components.entity.SizeComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleRegistries;

public class PehkuiCompat {

	public static void registerEntityComponents(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(Entity.class, ArcanusComponents.SIZE).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SizeComponent::new);
	}

	public static void registerModifiers() {
		ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, Arcanus.id("size_modifier"), SizeComponent.ArcanusScaleModifier.INSTANCE);
	}
}
