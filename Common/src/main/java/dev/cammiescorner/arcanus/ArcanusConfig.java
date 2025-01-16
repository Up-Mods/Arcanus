package dev.cammiescorner.arcanus;

import com.teamresourceful.resourcefulconfig.api.annotations.*;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption.Range;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import net.minecraft.util.random.Weight;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

@Config(Arcanus.MOD_ID)
public final class ArcanusConfig {
	@ConfigEntry(id = "castingSpeedHasCoolDown", type = EntryType.BOOLEAN, translation = "config.arcanus.castingSpeedHasCoolDown")
	public static boolean castingSpeedHasCoolDown = false;

	@ConfigEntry(id = "sizeChangingIsPermanent", type = EntryType.BOOLEAN, translation = "config.arcanus.sizeChangingIsPermanent")
	public static boolean sizeChangingIsPermanent = false;

	@Category(value = "enchantments", translation = "config.arcanus.enchantments_category", sortOrder = -1)
	public static final class Enchantments {
		@Category(value = "manaPool", translation = "config.arcanus.mana_pool", sortOrder = 0)
		public static final class ManaPool {
			@ConfigEntry(id = "maxEnchantmentLevel", type = EntryType.INTEGER, translation = "config.arcanus.max_enchantment_level")
			public static int maxLevel = 5;

			@ConfigEntry(id = "manaPerLevel", type = EntryType.DOUBLE, translation = "config.arcanus.mana_per_level")
			public static double manaPerLevel = 0.05;

			@ConfigEntry(id = "manaModifierOperation", type = EntryType.ENUM, translation = "config.arcanus.mana_modifier_operation")
			public static AttributeModifier.Operation manaModifierOperation = AttributeModifier.Operation.MULTIPLY_BASE;
		}
	}

	@Category(value = "spellShapeProperties", translation = "config.arcanus.spellShapesCategory", sortOrder = 0)
	public static final class SpellShapes {
		@Category(value = "selfShapeProperties", translation = "config.arcanus.selfShapeProperties", sortOrder = 0)
		public static final class SelfShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.VERY_LIGHT;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 0.85;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 10;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@ConfigOption.Range(min = 0, max = 10)
			public static int minimumLevel = 1;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;
		}

		@Category(value = "touchShapeProperties", translation = "config.arcanus.touchShapeProperties", sortOrder = 1)
		public static final class TouchShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.VERY_LIGHT;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 15;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 1;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0.2;
		}

		@Category(value = "projectileShapeProperties", translation = "config.arcanus.projectileShapeProperties", sortOrder = 2)
		public static final class ProjectileShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.LIGHT;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 10;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 3;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = -0.25;

			@ConfigEntry(id = "projectileSpeed", type = EntryType.FLOAT, translation = "config.arcanus.projectileSpeed")
			public static float projectileSpeed = 4f;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanus.baseLifeSpan")
			@Range(min = 1, max = 24000)
			public static int baseLifeSpan = 20;
		}

		@Category(value = "lobShapeProperties", translation = "config.arcanus.lobShapeProperties", sortOrder = 3)
		public static final class LobShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.LIGHT;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 20;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 3;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "projectileSpeed", type = EntryType.FLOAT, translation = "config.arcanus.projectileSpeed")
			public static float projectileSpeed = 2f;
		}

		@Category(value = "boltShapeProperties", translation = "config.arcanus.boltShapeProperties", sortOrder = 4)
		public static final class BoltShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.MEDIUM;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 15;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "range", type = EntryType.DOUBLE, translation = "config.arcanus.range")
			@Range(min = 0, max = 32)
			public static double range = 6;
		}

		@Category(value = "beamShapeProperties", translation = "config.arcanus.beamShapeProperties", sortOrder = 5)
		public static final class BeamShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.MEDIUM;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1.25;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 30;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0.25;

			@ConfigEntry(id = "range", type = EntryType.DOUBLE, translation = "config.arcanus.range")
			@Range(min = 0, max = 32)
			public static double range = 16;

			@ConfigEntry(id = "delay", type = EntryType.INTEGER, translation = "config.arcanus.delay")
			@Range(min = 0, max = 24000)
			public static int delay = 40;
		}

		@Category(value = "runeShapeProperties", translation = "config.arcanus.runeShapeProperties", sortOrder = 6)
		public static final class RuneShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 50;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "delay", type = EntryType.INTEGER, translation = "config.arcanus.delay")
			@Range(min = 0, max = 24000)
			public static int delay = 60;
		}

		@Category(value = "explosionShapeProperties", translation = "config.arcanus.explosionShapeProperties", sortOrder = 7)
		public static final class ExplosionShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1.25;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 60;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "strength", type = EntryType.FLOAT, translation = "config.arcanus.strength")
			@FloatRange(min = 0, max = 10)
			public static float strength = 3.5f;
		}

		@Category(value = "counterShapeProperties", translation = "config.arcanus.counterShapeProperties", sortOrder = 8)
		public static final class CounterShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1.2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 300;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(value = "aoeShapeProperties", translation = "config.arcanus.aoeShapeProperties", sortOrder = 9)
		public static final class AOEShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 4;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 60;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanus.baseLifeSpan")
			@Range(min = 1, max = 24000)
			public static int baseLifeSpan = 100;
		}

		@Category(value = "smiteShapeProperties", translation = "config.arcanus.smiteShapeProperties", sortOrder = 10)
		public static final class SmiteShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1.75;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 60;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0.5;
		}

		@Category(value = "guardianOrbShapeProperties", translation = "config.arcanus.guardianOrbShapeProperties", sortOrder = 11)
		public static final class GuardianOrbShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 1.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 100;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "maximumManaLock", type = EntryType.DOUBLE, translation = "config.arcanus.maximumManaLock")
			@Range(min = 0, max = 1)
			public static double maximumManaLock = 0.5;

			@ConfigEntry(id = "baseManaDrain", type = EntryType.DOUBLE, translation = "config.arcanus.baseManaDrain")
			@Range(min = 0, max = 200)
			public static double baseManaDrain = 3;
		}

		@Category(value = "aggressorbShapeProperties", translation = "config.arcanus.aggressorbShapeProperties", sortOrder = 12)
		public static final class AggressorbShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanus.manaMultiplier")
			public static double manaMultiplier = 0.8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 200;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 0, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanus.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "maximumAggressorbs", type = EntryType.INTEGER, translation = "config.arcanus.maximumAggressorbs")
			@Range(min = 0, max = 16)
			public static int maximumAggressorbs = 6;

			@ConfigEntry(id = "projectileSpeed", type = EntryType.FLOAT, translation = "config.arcanus.projectileSpeed")
			public static float projectileSpeed = 3f;
		}
	}

	@Category(value = "attackEffectsCategory", translation = "config.arcanus.attackEffectsCategory", sortOrder = 1)
	public static final class AttackEffects {
		@Category(value = "damageEffectProperties", translation = "config.arcanus.damageEffectProperties", sortOrder = 0)
		public static final class DamageEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 4;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 1;

			@ConfigEntry(id = "baseDamage", type = EntryType.FLOAT, translation = "config.arcanus.baseDamage")
			@FloatRange(min = 0, max = 1000)
			public static float baseDamage = 1.5f;
		}

		@Category(value = "fireEffectProperties", translation = "config.arcanus.fireEffectProperties", sortOrder = 1)
		public static final class FireEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 2;

			@ConfigEntry(id = "baseTimeOnFire", type = EntryType.INTEGER, translation = "config.arcanus.baseTimeOnFire")
			@Range(min = 0, max = 100)
			public static int baseTimeOnFire = 3;
		}

		@Category(value = "electricEffectProperties", translation = "config.arcanus.electricEffectProperties", sortOrder = 2)
		public static final class ElectricEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 2;

			@ConfigEntry(id = "baseStunTime", type = EntryType.INTEGER, translation = "config.arcanus.baseStunTime")
			@Range(min = 0, max = 100)
			public static int baseStunTime = 2;

			@ConfigEntry(id = "wetEntityDamageMultiplier", type = EntryType.FLOAT, translation = "config.arcanus.wetEntityDamageMultiplier")
			@FloatRange(min = 1, max = 1000)
			public static float wetEntityDamageMultiplier = 2f;
		}

		@Category(value = "iceEffectProperties", translation = "config.arcanus.iceEffectProperties", sortOrder = 3)
		public static final class IceEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 2;

			@ConfigEntry(id = "baseFreezingTime", type = EntryType.INTEGER, translation = "config.arcanus.baseFreezingTime")
			@Range(min = 0, max = 100)
			public static int baseFreezingTime = 20;
		}

		@Category(value = "vulnerabilityEffectProperties", translation = "config.arcanus.vulnerabilityEffectProperties", sortOrder = 4)
		public static final class VulnerabilityEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 300;
		}

		@Category(value = "manaLockEffectProperties", translation = "config.arcanus.manaLockEffectProperties", sortOrder = 5)
		public static final class ManaLockEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 200;
		}

		@Category(value = "witheringEffectProperties", translation = "config.arcanus.witheringEffectProperties", sortOrder = 6)
		public static final class WitheringEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 6.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 60;
		}

		@Category(value = "necromancyEffectProperties", translation = "config.arcanus.necromancyEffectProperties", sortOrder = 7)
		public static final class NecromancyEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "baseHealth", type = EntryType.INTEGER, translation = "config.arcanus.baseHealth")
			@Range(min = 1, max = 100)
			public static int baseHealth = 10;
		}

		@Category(value = "manaSplitEffectProperties", translation = "config.arcanus.manaSplitEffectProperties", sortOrder = 8)
		public static final class ManaSplitEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 6.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 6;
		}

		@Category(value = "copperCurseEffectProperties", translation = "config.arcanus.copperCurseEffectProperties", sortOrder = 9)
		public static final class CopperCurseEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 24000;

			@ConfigEntry(id = "effectDurationModifier", type = EntryType.INTEGER, translation = "config.arcanus.effectDurationModifier")
			@Range(min = 0, max = 24000)
			public static int effectDurationModifier = 12000;

			@ConfigEntry(id = "baseChanceToActivate", type = EntryType.DOUBLE, translation = "config.arcanus.baseChanceToActivate")
			@Range(min = 0, max = 1)
			public static double baseChanceToActivate = 0.0625;
		}

		@Category(value = "discombobulateEffectProperties", translation = "config.arcanus.discombobulateEffectProperties", sortOrder = 10)
		public static final class DiscombobulateEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 6;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 60;

			@ConfigEntry(id = "effectDurationModifier", type = EntryType.INTEGER, translation = "config.arcanus.effectDurationModifier")
			@Range(min = 0, max = 24000)
			public static int effectDurationModifier = 15;
		}

		@Category(value = "stockpileEffectProperties", translation = "config.arcanus.stockpileEffectProperties", sortOrder = 11)
		public static final class StockpileEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 4.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 6;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 100;

			@ConfigEntry(id = "effectDurationModifier", type = EntryType.INTEGER, translation = "config.arcanus.effectDurationModifier")
			@Range(min = 0, max = 24000)
			public static int effectDurationModifier = 30;

			@ConfigEntry(id = "damageNeededToIncrease", type = EntryType.FLOAT, translation = "config.arcanus.damageNeededToIncrease")
			@FloatRange(min = 0, max = 1000)
			public static float damageNeededToIncrease = 10f;
		}
	}

	@Category(value = "supportEffectsCategory", translation = "config.arcanus.supportEffectsCategory", sortOrder = 2)
	public static final class SupportEffects {
		@Category(value = "healEffectProperties", translation = "config.arcanus.healEffectProperties", sortOrder = 0)
		public static final class HealEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 1;

			@ConfigEntry(id = "baseHealAmount", type = EntryType.FLOAT, translation = "config.arcanus.baseHealAmount")
			@FloatRange(min = 0, max = 1000)
			public static float baseHealAmount = 3f;
		}

		@Category(value = "dispelEffectProperties", translation = "config.arcanus.dispelEffectProperties", sortOrder = 1)
		public static final class DispelEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 6;
		}

		@Category(value = "regenerateEffectProperties", translation = "config.arcanus.regenerateEffectProperties", sortOrder = 2)
		public static final class RegenerateEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 6;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(value = "fortifyEffectProperties", translation = "config.arcanus.fortifyEffectProperties", sortOrder = 3)
		public static final class FortifyEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 6.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 500;
		}

		@Category(value = "hasteEffectProperties", translation = "config.arcanus.hasteEffectProperties", sortOrder = 4)
		public static final class HasteEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 7;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 4;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 200;
		}

		@Category(value = "manaShieldEffectProperties", translation = "config.arcanus.manaShieldEffectProperties", sortOrder = 5)
		public static final class ManaShieldEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanus.baseLifeSpan")
			@Range(min = 0, max = 24000)
			public static int baseLifeSpan = 100;

			@ConfigEntry(id = "lifeSpanModifier", type = EntryType.INTEGER, translation = "config.arcanus.lifeSpanModifier")
			@Range(min = 0, max = 24000)
			public static int lifeSpanModifier = 40;
		}

		@Category(value = "dangerSenseEffectProperties", translation = "config.arcanus.dangerSenseEffectProperties", sortOrder = 6)
		public static final class DangerSenseEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 100;

			@ConfigEntry(id = "baseChanceToActivate", type = EntryType.DOUBLE, translation = "config.arcanus.baseChanceToActivate")
			@Range(min = 0, max = 1)
			public static double baseChanceToActivate = 0.035;
		}
	}

	@Category(value = "utilityEffectsCategory", translation = "config.arcanus.utilityEffectsCategory", sortOrder = 3)
	public static final class UtilityEffects {
		@Category(value = "buildEffectProperties", translation = "config.arcanus.buildEffectProperties", sortOrder = 0)
		public static final class BuildEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanus.baseLifeSpan")
			@Range(min = 0, max = 24000)
			public static int baseLifeSpan = 220;
		}

		@Category(value = "powerEffectProperties", translation = "config.arcanus.powerEffectProperties", sortOrder = 1)
		public static final class PowerEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 4;

			@ConfigEntry(id = "basePower", type = EntryType.INTEGER, translation = "config.arcanus.basePower")
			@Range(min = 0, max = 16)
			public static int basePower = 4;
		}

		@Category(value = "anonymityEffectProperties", translation = "config.arcanus.anonymityEffectProperties", sortOrder = 2)
		public static final class AnonymityEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 220;
		}

		@Category(value = "mineEffectProperties", translation = "config.arcanus.mineEffectProperties", sortOrder = 3)
		public static final class MineEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 7;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 4;
		}

		@Category(value = "growthEffectProperties", translation = "config.arcanus.growthEffectProperties", sortOrder = 4)
		public static final class GrowthEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 6;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 4;
		}

		@Category(value = "shrinkEffectProperties", translation = "config.arcanus.shrinkEffectProperties", sortOrder = 5)
		public static final class ShrinkEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 2.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "baseShrinkAmount", type = EntryType.FLOAT, translation = "config.arcanus.baseShrinkAmount")
			@FloatRange(min = 0, max = 1)
			public static float baseShrinkAmount = 0.5f;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(value = "enlargeEffectProperties", translation = "config.arcanus.enlargeEffectProperties", sortOrder = 6)
		public static final class EnlargeEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 2.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "baseEnlargeAmount", type = EntryType.FLOAT, translation = "config.arcanus.baseEnlargeAmount")
			@FloatRange(min = 1, max = 2)
			public static float baseEnlargeAmount = 1.5f;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(value = "spatialRiftEffectProperties", translation = "config.arcanus.spatialRiftEffectProperties", sortOrder = 7)
		public static final class SpatialRiftEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 10;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "canSuckEntitiesIn", type = EntryType.BOOLEAN, translation = "config.arcanus.canSuckEntitiesIn")
			public static boolean canSuckEntitiesIn = true;

			@ConfigEntry(id = "portalGrowTime", type = EntryType.INTEGER, translation = "config.arcanus.portalGrowTime")
			@Range(min = 0, max = 24000)
			public static int portalGrowTime = 100;

			@ConfigEntry(id = "pocketWidth", type = EntryType.INTEGER, translation = "config.arcanus.pocketWidth")
			@Comment(value = "Needs to be an even number")
			@Range(min = 2, max = 48)
			public static int pocketWidth = 24;

			@ConfigEntry(id = "pocketHeight", type = EntryType.INTEGER, translation = "config.arcanus.pocketHeight")
			@Comment(value = "Needs to be an even number")
			@Range(min = 2, max = 48)
			public static int pocketHeight = 24;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanus.baseLifeSpan")
			@Range(min = 0, max = 24000)
			public static int baseLifeSpan = 700;
		}

		@Category(value = "wardingEffectProperties", translation = "config.arcanus.wardingEffectProperties", sortOrder = 8)
		public static final class WardingEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 4;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 6;

			@ConfigEntry(id = "canBeRemovedByOthers", type = EntryType.BOOLEAN, translation = "config.arcanus.canBeRemovedByOthers")
			public static boolean canBeRemovedByOthers = true;
		}
	}

	@Category(value = "movementEffectsCategory", translation = "config.arcanus.movementEffectsCategory", sortOrder = 4)
	public static final class MovementEffects {
		@Category(value = "pushEffectProperties", translation = "config.arcanus.pushEffectProperties", sortOrder = 0)
		public static final class PushEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 3;

			@ConfigEntry(id = "basePushAmount", type = EntryType.DOUBLE, translation = "config.arcanus.basePushAmount")
			@Range(min = 0, max = 10)
			public static double basePushAmount = 0.2;
		}

		@Category(value = "pullEffectProperties", translation = "config.arcanus.pullEffectProperties", sortOrder = 1)
		public static final class PullEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 3;

			@ConfigEntry(id = "basePullAmount", type = EntryType.DOUBLE, translation = "config.arcanus.basePullAmount")
			@Range(min = 0, max = 10)
			public static double basePullAmount = 0.2;
		}

		@Category(value = "levitateEffectProperties", translation = "config.arcanus.levitateEffectProperties", sortOrder = 2)
		public static final class LevitateEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 3;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 6;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 60;
		}

		@Category(value = "speedEffectProperties", translation = "config.arcanus.speedEffectProperties", sortOrder = 3)
		public static final class SpeedEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 7;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 300;
		}

		@Category(value = "teleportEffectProperties", translation = "config.arcanus.teleportEffectProperties", sortOrder = 4)
		public static final class TeleportEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 7.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "baseTeleportDistance", type = EntryType.DOUBLE, translation = "config.arcanus.baseTeleportDistance")
			@Range(min = 0, max = 32)
			public static double baseTeleportDistance = 5;
		}

		@Category(value = "bouncyEffectProperties", translation = "config.arcanus.bouncyEffectProperties", sortOrder = 5)
		public static final class BouncyEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 220;
		}

		@Category(value = "featherEffectProperties", translation = "config.arcanus.featherEffectProperties", sortOrder = 6)
		public static final class FeatherEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(value = "floatEffectProperties", translation = "config.arcanus.floatEffectProperties", sortOrder = 7)
		public static final class FloatEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 10;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "removedUponTakingDamage", type = EntryType.BOOLEAN, translation = "config.arcanus.removedUponTakingDamage")
			public static boolean removedUponTakingDamage = true;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 1200;
		}

		@Category(value = "manaWingsEffectProperties", translation = "config.arcanus.manaWingsEffectProperties", sortOrder = 8)
		public static final class ManaWingsEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanus.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanus.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanus.manaCost")
			@Range(min = 0, max = 200)
			public static double manaCost = 8.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanus.coolDown")
			@Range(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanus.minimumLevel")
			@Range(min = 1, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "removedUponTakingDamage", type = EntryType.BOOLEAN, translation = "config.arcanus.removedUponTakingDamage")
			public static boolean removedUponTakingDamage = true;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanus.baseEffectDuration")
			@Range(min = 0, max = 24000)
			public static int baseEffectDuration = 200;

			@ConfigEntry(id = "effectDurationModifier", type = EntryType.INTEGER, translation = "config.arcanus.effectDurationModifier")
			@Range(min = 0, max = 24000)
			public static int effectDurationModifier = 100;
		}
	}
}
