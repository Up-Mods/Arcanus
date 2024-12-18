package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanuscontinuum.common.effects.ArcanusStatusEffect;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ArcanusStatusEffects {
	public static final RegistryHandler<MobEffect> STATUS_EFFECTS = RegistryHandler.create(Registries.MOB_EFFECT, Arcanus.MOD_ID);

	public static final RegistrySupplier<MobEffect> MANA_LOCK = STATUS_EFFECTS.register("mana_lock", () -> new ArcanusStatusEffect(MobEffectCategory.HARMFUL, 0xa89d9b).addAttributeModifier(ArcanusEntityAttributes.MANA_LOCK.get(), "c5fa384f-c7f3-479b-9448-2843ff80a588", 7, AttributeModifier.Operation.ADDITION));
	public static final RegistrySupplier<MobEffect> VULNERABILITY = STATUS_EFFECTS.register("vulnerability", () -> new ArcanusStatusEffect(MobEffectCategory.HARMFUL, 0x3a8e99));
	public static final RegistrySupplier<MobEffect> FORTIFY = STATUS_EFFECTS.register("fortify", () -> new ArcanusStatusEffect(MobEffectCategory.BENEFICIAL, 0xbbbbbb));
	public static final RegistrySupplier<MobEffect> BOUNCY = STATUS_EFFECTS.register("bouncy", () -> new ArcanusStatusEffect(MobEffectCategory.NEUTRAL, 0x77ff88));
	public static final RegistrySupplier<MobEffect> ANONYMITY = STATUS_EFFECTS.register("anonymity", () -> new ArcanusStatusEffect(MobEffectCategory.NEUTRAL, 0x555555));
	public static final RegistrySupplier<MobEffect> COPPER_CURSE = STATUS_EFFECTS.register("copper_curse", () -> new ArcanusStatusEffect(MobEffectCategory.HARMFUL, 0xc15a36));
	public static final RegistrySupplier<MobEffect> DISCOMBOBULATE = STATUS_EFFECTS.register("discombobulate", () -> new ArcanusStatusEffect(MobEffectCategory.HARMFUL, 0x7b1375));
//	public static final RegistrySupplier<StatusEffect> TEMPORAL_DILATION = STATUS_EFFECTS.register("temporal_dilation", () -> new ArcanusStatusEffect(StatusEffectType.BENEFICIAL, 0x68e1ff));
	public static final RegistrySupplier<MobEffect> FLOAT = STATUS_EFFECTS.register("float", () -> new ArcanusStatusEffect(MobEffectCategory.NEUTRAL, 0xceffff));
	public static final RegistrySupplier<MobEffect> MANA_WINGS = STATUS_EFFECTS.register("mana_wings", () -> new ArcanusStatusEffect(MobEffectCategory.BENEFICIAL, 0x716e8c));
	public static final RegistrySupplier<MobEffect> STOCKPILE = STATUS_EFFECTS.register("stockpile", () -> new ArcanusStatusEffect(MobEffectCategory.BENEFICIAL, 0x550000));
	public static final RegistrySupplier<MobEffect> DANGER_SENSE = STATUS_EFFECTS.register("danger_sense", () -> new ArcanusStatusEffect(MobEffectCategory.BENEFICIAL, 0xaeeff2));
}
