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

public class ArcanusMobEffects {
	public static final RegistryHandler<MobEffect> MOB_EFFECTS = RegistryHandler.create(Registries.MOB_EFFECT, Arcanus.MOD_ID);

	public static final RegistrySupplier<MobEffect> MANA_LOCK = MOB_EFFECTS.register("mana_lock", () -> new ArcanusStatusEffect(MobEffectCategory.HARMFUL, 0xa89d9b).addAttributeModifier(ArcanusEntityAttributes.MANA_LOCK.get(), "c5fa384f-c7f3-479b-9448-2843ff80a588", 7, AttributeModifier.Operation.ADDITION));
	public static final RegistrySupplier<MobEffect> VULNERABILITY = MOB_EFFECTS.register("vulnerability", () -> new ArcanusStatusEffect(MobEffectCategory.HARMFUL, 0x3a8e99));
	public static final RegistrySupplier<MobEffect> FORTIFY = MOB_EFFECTS.register("fortify", () -> new ArcanusStatusEffect(MobEffectCategory.BENEFICIAL, 0xbbbbbb));
	public static final RegistrySupplier<MobEffect> BOUNCY = MOB_EFFECTS.register("bouncy", () -> new ArcanusStatusEffect(MobEffectCategory.NEUTRAL, 0x77ff88));
	public static final RegistrySupplier<MobEffect> ANONYMITY = MOB_EFFECTS.register("anonymity", () -> new ArcanusStatusEffect(MobEffectCategory.NEUTRAL, 0x555555, true, false));
	public static final RegistrySupplier<MobEffect> COPPER_CURSE = MOB_EFFECTS.register("copper_curse", () -> new ArcanusStatusEffect(MobEffectCategory.HARMFUL, 0xc15a36));
	public static final RegistrySupplier<MobEffect> DISCOMBOBULATE = MOB_EFFECTS.register("discombobulate", () -> new ArcanusStatusEffect(MobEffectCategory.HARMFUL, 0x7b1375));
	//	public static final RegistrySupplier<StatusEffect> TEMPORAL_DILATION = STATUS_EFFECTS.register("temporal_dilation", () -> new ArcanusStatusEffect(StatusEffectType.BENEFICIAL, 0x68e1ff, true, true));
	public static final RegistrySupplier<MobEffect> FLOAT = MOB_EFFECTS.register("float", () -> new ArcanusStatusEffect(MobEffectCategory.NEUTRAL, 0xceffff));
	public static final RegistrySupplier<MobEffect> MANA_WINGS = MOB_EFFECTS.register("mana_wings", () -> new ArcanusStatusEffect(MobEffectCategory.BENEFICIAL, 0x716e8c, true, false));
	public static final RegistrySupplier<MobEffect> STOCKPILE = MOB_EFFECTS.register("stockpile", () -> new ArcanusStatusEffect(MobEffectCategory.BENEFICIAL, 0x550000));
	public static final RegistrySupplier<MobEffect> DANGER_SENSE = MOB_EFFECTS.register("danger_sense", () -> new ArcanusStatusEffect(MobEffectCategory.BENEFICIAL, 0xaeeff2));
	public static final RegistrySupplier<MobEffect> SHRINK = MOB_EFFECTS.register("shrink", () -> new ArcanusStatusEffect(MobEffectCategory.NEUTRAL, 0x00ffc8, true, true));
	public static final RegistrySupplier<MobEffect> ENLARGE = MOB_EFFECTS.register("enlarge", () -> new ArcanusStatusEffect(MobEffectCategory.NEUTRAL, 0xff9600, true, true));
}
