package dev.cammiescorner.arcanus.fabric.common.compat;

import dev.cammiescorner.arcanus.fabric.common.components.entity.SizeComponent;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.world.entity.Entity;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class PehkuiCompat {

	public static void registerEntityComponents(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(Entity.class, ArcanusComponents.SIZE).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SizeComponent::new);
	}

	public static void registerModifiers() {
		ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, FabricMain.id("size_modifier"), SizeComponent.ArcanusScaleModifier.INSTANCE);
	}
}
