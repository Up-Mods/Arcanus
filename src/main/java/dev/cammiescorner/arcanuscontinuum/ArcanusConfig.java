package dev.cammiescorner.arcanuscontinuum;

import com.teamresourceful.resourcefulconfig.common.annotations.*;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

@Config(Arcanus.MOD_ID)
public final class ArcanusConfig {
	@ConfigEntry(id = "castingSpeedHasCoolDown", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.castingSpeedHasCoolDown")
	public static boolean castingSpeedHasCoolDown = false;

	@ConfigEntry(id = "sizeChangingIsPermanent", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.sizeChangingIsPermanent")
	public static boolean sizeChangingIsPermanent = false;

	@Category(id = "enchantments", translation = "config.arcanuscontinuum.enchantments_category", sortOrder = -1)
	public static final class Enchantments {
		@Category(id = "manaPool", translation = "config.arcanuscontinuum.mana_pool", sortOrder = 0)
		public static final class ManaPool {
			@ConfigEntry(id = "maxEnchantmentLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.max_enchantment_level")
			public static int maxLevel = 5;

			@ConfigEntry(id = "manaPerLevel", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.mana_per_level")
			public static double manaPerLevel = 0.05;

			@ConfigEntry(id = "manaModifierOperation", type = EntryType.ENUM, translation = "config.arcanuscontinuum.mana_modifier_operation")
			public static AttributeModifier.Operation manaModifierOperation = AttributeModifier.Operation.MULTIPLY_BASE;
		}
	}

	@Category(id = "spellShapeProperties", translation = "config.arcanuscontinuum.spellShapesCategory", sortOrder = 0)
	public static final class SpellShapes {
		@Category(id = "selfShapeProperties", translation = "config.arcanuscontinuum.selfShapeProperties", sortOrder = 0)
		public static final class SelfShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.VERY_LIGHT;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 0.85;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 10;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 1;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;
		}

		@Category(id = "touchShapeProperties", translation = "config.arcanuscontinuum.touchShapeProperties", sortOrder = 1)
		public static final class TouchShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.VERY_LIGHT;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 15;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 1;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0.2;
		}

		@Category(id = "projectileShapeProperties", translation = "config.arcanuscontinuum.projectileShapeProperties", sortOrder = 2)
		public static final class ProjectileShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.LIGHT;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 10;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 3;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = -0.25;

			@ConfigEntry(id = "projectileSpeed", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.projectileSpeed")
			public static float projectileSpeed = 4f;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseLifeSpan")
			@IntRange(min = 1, max = 24000)
			public static int baseLifeSpan = 20;
		}

		@Category(id = "lobShapeProperties", translation = "config.arcanuscontinuum.lobShapeProperties", sortOrder = 3)
		public static final class LobShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.LIGHT;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 20;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 3;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "projectileSpeed", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.projectileSpeed")
			public static float projectileSpeed = 2f;
		}

		@Category(id = "boltShapeProperties", translation = "config.arcanuscontinuum.boltShapeProperties", sortOrder = 4)
		public static final class BoltShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.MEDIUM;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 15;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "range", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.range")
			@DoubleRange(min = 0, max = 32)
			public static double range = 6;
		}

		@Category(id = "beamShapeProperties", translation = "config.arcanuscontinuum.beamShapeProperties", sortOrder = 5)
		public static final class BeamShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.MEDIUM;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1.25;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 30;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0.25;

			@ConfigEntry(id = "range", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.range")
			@DoubleRange(min = 0, max = 32)
			public static double range = 16;

			@ConfigEntry(id = "delay", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.delay")
			@IntRange(min = 0, max = 24000)
			public static int delay = 40;
		}

		@Category(id = "runeShapeProperties", translation = "config.arcanuscontinuum.runeShapeProperties", sortOrder = 6)
		public static final class RuneShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 50;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "delay", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.delay")
			@IntRange(min = 0, max = 24000)
			public static int delay = 60;
		}

		@Category(id = "explosionShapeProperties", translation = "config.arcanuscontinuum.explosionShapeProperties", sortOrder = 7)
		public static final class ExplosionShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1.25;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 60;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "strength", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.strength")
			@FloatRange(min = 0, max = 10)
			public static float strength = 3.5f;
		}

		@Category(id = "counterShapeProperties", translation = "config.arcanuscontinuum.counterShapeProperties", sortOrder = 8)
		public static final class CounterShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1.2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 300;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(id = "aoeShapeProperties", translation = "config.arcanuscontinuum.aoeShapeProperties", sortOrder = 9)
		public static final class AOEShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 4;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 60;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseLifeSpan")
			@IntRange(min = 1, max = 24000)
			public static int baseLifeSpan = 100;
		}

		@Category(id = "smiteShapeProperties", translation = "config.arcanuscontinuum.smiteShapeProperties", sortOrder = 10)
		public static final class SmiteShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1.75;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 60;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0.5;
		}

		@Category(id = "guardianOrbShapeProperties", translation = "config.arcanuscontinuum.guardianOrbShapeProperties", sortOrder = 11)
		public static final class GuardianOrbShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 1.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 100;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "maximumManaLock", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.maximumManaLock")
			@DoubleRange(min = 0, max = 1)
			public static double maximumManaLock = 0.5;

			@ConfigEntry(id = "baseManaDrain", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.baseManaDrain")
			@DoubleRange(min = 0, max = 200)
			public static double baseManaDrain = 3;
		}

		@Category(id = "aggressorbShapeProperties", translation = "config.arcanuscontinuum.aggressorbShapeProperties", sortOrder = 12)
		public static final class AggressorbShapeProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.VERY_HEAVY;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 0;

			@ConfigEntry(id = "manaMultiplier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaMultiplier")
			public static double manaMultiplier = 0.8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 200;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 0, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "potencyModifier", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.potencyModifier")
			public static double potencyModifier = 0;

			@ConfigEntry(id = "maximumAggressorbs", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.maximumAggressorbs")
			@IntRange(min = 0, max = 16)
			public static int maximumAggressorbs = 6;

			@ConfigEntry(id = "projectileSpeed", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.projectileSpeed")
			public static float projectileSpeed = 3f;
		}
	}

	@Category(id = "attackEffectsCategory", translation = "config.arcanuscontinuum.attackEffectsCategory", sortOrder = 1)
	public static final class AttackEffects {
		@Category(id = "damageEffectProperties", translation = "config.arcanuscontinuum.damageEffectProperties", sortOrder = 0)
		public static final class DamageEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 4;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 1;

			@ConfigEntry(id = "baseDamage", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.baseDamage")
			@FloatRange(min = 0, max = 1000)
			public static float baseDamage = 1.5f;
		}

		@Category(id = "fireEffectProperties", translation = "config.arcanuscontinuum.fireEffectProperties", sortOrder = 1)
		public static final class FireEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 2;

			@ConfigEntry(id = "baseTimeOnFire", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseTimeOnFire")
			@IntRange(min = 0, max = 100)
			public static int baseTimeOnFire = 3;
		}

		@Category(id = "electricEffectProperties", translation = "config.arcanuscontinuum.electricEffectProperties", sortOrder = 2)
		public static final class ElectricEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 2;

			@ConfigEntry(id = "baseStunTime", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseStunTime")
			@IntRange(min = 0, max = 100)
			public static int baseStunTime = 2;

			@ConfigEntry(id = "wetEntityDamageMultiplier", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.wetEntityDamageMultiplier")
			@FloatRange(min = 1, max = 1000)
			public static float wetEntityDamageMultiplier = 2f;
		}

		@Category(id = "iceEffectProperties", translation = "config.arcanuscontinuum.iceEffectProperties", sortOrder = 3)
		public static final class IceEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 2;

			@ConfigEntry(id = "baseFreezingTime", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseFreezingTime")
			@IntRange(min = 0, max = 100)
			public static int baseFreezingTime = 20;
		}

		@Category(id = "vulnerabilityEffectProperties", translation = "config.arcanuscontinuum.vulnerabilityEffectProperties", sortOrder = 4)
		public static final class VulnerabilityEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 300;
		}

		@Category(id = "manaLockEffectProperties", translation = "config.arcanuscontinuum.manaLockEffectProperties", sortOrder = 5)
		public static final class ManaLockEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 200;
		}

		@Category(id = "witheringEffectProperties", translation = "config.arcanuscontinuum.witheringEffectProperties", sortOrder = 6)
		public static final class WitheringEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 6.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 60;
		}

		@Category(id = "necromancyEffectProperties", translation = "config.arcanuscontinuum.necromancyEffectProperties", sortOrder = 7)
		public static final class NecromancyEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "baseHealth", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseHealth")
			@IntRange(min = 1, max = 100)
			public static int baseHealth = 10;
		}

		@Category(id = "manaSplitEffectProperties", translation = "config.arcanuscontinuum.manaSplitEffectProperties", sortOrder = 8)
		public static final class ManaSplitEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 6.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 6;
		}

		@Category(id = "copperCurseEffectProperties", translation = "config.arcanuscontinuum.copperCurseEffectProperties", sortOrder = 9)
		public static final class CopperCurseEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 24000;

			@ConfigEntry(id = "effectDurationModifier", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.effectDurationModifier")
			@IntRange(min = 0, max = 24000)
			public static int effectDurationModifier = 12000;

			@ConfigEntry(id = "baseChanceToActivate", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.baseChanceToActivate")
			@DoubleRange(min = 0, max = 1)
			public static double baseChanceToActivate = 0.0625;
		}

		@Category(id = "discombobulateEffectProperties", translation = "config.arcanuscontinuum.discombobulateEffectProperties", sortOrder = 10)
		public static final class DiscombobulateEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 6;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 60;

			@ConfigEntry(id = "effectDurationModifier", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.effectDurationModifier")
			@IntRange(min = 0, max = 24000)
			public static int effectDurationModifier = 15;
		}

		@Category(id = "stockpileEffectProperties", translation = "config.arcanuscontinuum.stockpileEffectProperties", sortOrder = 11)
		public static final class StockpileEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 4.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 6;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 100;

			@ConfigEntry(id = "effectDurationModifier", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.effectDurationModifier")
			@IntRange(min = 0, max = 24000)
			public static int effectDurationModifier = 30;

			@ConfigEntry(id = "damageNeededToIncrease", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.damageNeededToIncrease")
			@FloatRange(min = 0, max = 1000)
			public static float damageNeededToIncrease = 10f;
		}
	}

	@Category(id = "supportEffectsCategory", translation = "config.arcanuscontinuum.supportEffectsCategory", sortOrder = 2)
	public static final class SupportEffects {
		@Category(id = "healEffectProperties", translation = "config.arcanuscontinuum.healEffectProperties", sortOrder = 0)
		public static final class HealEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 1;

			@ConfigEntry(id = "baseHealAmount", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.baseHealAmount")
			@FloatRange(min = 0, max = 1000)
			public static float baseHealAmount = 3f;
		}

		@Category(id = "dispelEffectProperties", translation = "config.arcanuscontinuum.dispelEffectProperties", sortOrder = 1)
		public static final class DispelEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 6;
		}

		@Category(id = "regenerateEffectProperties", translation = "config.arcanuscontinuum.regenerateEffectProperties", sortOrder = 2)
		public static final class RegenerateEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 6;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(id = "fortifyEffectProperties", translation = "config.arcanuscontinuum.fortifyEffectProperties", sortOrder = 3)
		public static final class FortifyEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 6.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 500;
		}

		@Category(id = "hasteEffectProperties", translation = "config.arcanuscontinuum.hasteEffectProperties", sortOrder = 4)
		public static final class HasteEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 7;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 4;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 200;
		}

		@Category(id = "manaShieldEffectProperties", translation = "config.arcanuscontinuum.manaShieldEffectProperties", sortOrder = 5)
		public static final class ManaShieldEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 8;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseLifeSpan")
			@IntRange(min = 0, max = 24000)
			public static int baseLifeSpan = 100;

			@ConfigEntry(id = "lifeSpanModifier", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.lifeSpanModifier")
			@IntRange(min = 0, max = 24000)
			public static int lifeSpanModifier = 40;
		}

		@Category(id = "dangerSenseEffectProperties", translation = "config.arcanuscontinuum.dangerSenseEffectProperties", sortOrder = 6)
		public static final class DangerSenseEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 100;

			@ConfigEntry(id = "baseChanceToActivate", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.baseChanceToActivate")
			@DoubleRange(min = 0, max = 1)
			public static double baseChanceToActivate = 0.035;
		}
	}

	@Category(id = "utilityEffectsCategory", translation = "config.arcanuscontinuum.utilityEffectsCategory", sortOrder = 3)
	public static final class UtilityEffects {
		@Category(id = "buildEffectProperties", translation = "config.arcanuscontinuum.buildEffectProperties", sortOrder = 0)
		public static final class BuildEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseLifeSpan")
			@IntRange(min = 0, max = 24000)
			public static int baseLifeSpan = 220;
		}

		@Category(id = "powerEffectProperties", translation = "config.arcanuscontinuum.powerEffectProperties", sortOrder = 1)
		public static final class PowerEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 2;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 4;

			@ConfigEntry(id = "basePower", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.basePower")
			@IntRange(min = 0, max = 16)
			public static int basePower = 4;
		}

		@Category(id = "anonymityEffectProperties", translation = "config.arcanuscontinuum.anonymityEffectProperties", sortOrder = 2)
		public static final class AnonymityEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 220;
		}

		@Category(id = "mineEffectProperties", translation = "config.arcanuscontinuum.mineEffectProperties", sortOrder = 3)
		public static final class MineEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 7;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 4;
		}

		@Category(id = "growthEffectProperties", translation = "config.arcanuscontinuum.growthEffectProperties", sortOrder = 4)
		public static final class GrowthEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 6;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 4;
		}

		@Category(id = "shrinkEffectProperties", translation = "config.arcanuscontinuum.shrinkEffectProperties", sortOrder = 5)
		public static final class ShrinkEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 2.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "baseShrinkAmount", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.baseShrinkAmount")
			@FloatRange(min = 0, max = 1)
			public static float baseShrinkAmount = 0.5f;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(id = "enlargeEffectProperties", translation = "config.arcanuscontinuum.enlargeEffectProperties", sortOrder = 6)
		public static final class EnlargeEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 2.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 9;

			@ConfigEntry(id = "baseEnlargeAmount", type = EntryType.FLOAT, translation = "config.arcanuscontinuum.baseEnlargeAmount")
			@FloatRange(min = 1, max = 2)
			public static float baseEnlargeAmount = 1.5f;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(id = "spatialRiftEffectProperties", translation = "config.arcanuscontinuum.spatialRiftEffectProperties", sortOrder = 7)
		public static final class SpatialRiftEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 10;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 7;

			@ConfigEntry(id = "canSuckEntitiesIn", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.canSuckEntitiesIn")
			public static boolean canSuckEntitiesIn = true;

			@ConfigEntry(id = "portalGrowTime", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.portalGrowTime")
			@IntRange(min = 0, max = 24000)
			public static int portalGrowTime = 100;

			@ConfigEntry(id = "pocketWidth", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.pocketWidth")
			@Comment(value = "Needs to be an even number")
			@IntRange(min = 2, max = 48)
			public static int pocketWidth = 24;

			@ConfigEntry(id = "pocketHeight", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.pocketHeight")
			@Comment(value = "Needs to be an even number")
			@IntRange(min = 2, max = 48)
			public static int pocketHeight = 24;

			@ConfigEntry(id = "baseLifeSpan", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseLifeSpan")
			@IntRange(min = 0, max = 24000)
			public static int baseLifeSpan = 700;
		}

		@Category(id = "wardingEffectProperties", translation = "config.arcanuscontinuum.wardingEffectProperties", sortOrder = 8)
		public static final class WardingEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 4;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 6;

			@ConfigEntry(id = "canBeRemovedByOthers", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.canBeRemovedByOthers")
			public static boolean canBeRemovedByOthers = true;
		}
	}

	@Category(id = "movementEffectsCategory", translation = "config.arcanuscontinuum.movementEffectsCategory", sortOrder = 4)
	public static final class MovementEffects {
		@Category(id = "pushEffectProperties", translation = "config.arcanuscontinuum.pushEffectProperties", sortOrder = 0)
		public static final class PushEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 3;

			@ConfigEntry(id = "basePushAmount", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.basePushAmount")
			@DoubleRange(min = 0, max = 10)
			public static double basePushAmount = 0.2;
		}

		@Category(id = "pullEffectProperties", translation = "config.arcanuscontinuum.pullEffectProperties", sortOrder = 1)
		public static final class PullEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 1;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 3;

			@ConfigEntry(id = "basePullAmount", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.basePullAmount")
			@DoubleRange(min = 0, max = 10)
			public static double basePullAmount = 0.2;
		}

		@Category(id = "levitateEffectProperties", translation = "config.arcanuscontinuum.levitateEffectProperties", sortOrder = 2)
		public static final class LevitateEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 3;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 6;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 60;
		}

		@Category(id = "speedEffectProperties", translation = "config.arcanuscontinuum.speedEffectProperties", sortOrder = 3)
		public static final class SpeedEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 7;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 300;
		}

		@Category(id = "teleportEffectProperties", translation = "config.arcanuscontinuum.teleportEffectProperties", sortOrder = 4)
		public static final class TeleportEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 7.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "baseTeleportDistance", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.baseTeleportDistance")
			@DoubleRange(min = 0, max = 32)
			public static double baseTeleportDistance = 5;
		}

		@Category(id = "bouncyEffectProperties", translation = "config.arcanuscontinuum.bouncyEffectProperties", sortOrder = 5)
		public static final class BouncyEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 220;
		}

		@Category(id = "featherEffectProperties", translation = "config.arcanuscontinuum.featherEffectProperties", sortOrder = 6)
		public static final class FeatherEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 8;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 100;
		}

		@Category(id = "floatEffectProperties", translation = "config.arcanuscontinuum.floatEffectProperties", sortOrder = 7)
		public static final class FloatEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 10;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 5;

			@ConfigEntry(id = "removedUponTakingDamage", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.removedUponTakingDamage")
			public static boolean removedUponTakingDamage = true;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 1200;
		}

		@Category(id = "manaWingsEffectProperties", translation = "config.arcanuscontinuum.manaWingsEffectProperties", sortOrder = 8)
		public static final class ManaWingsEffectProperties {
			@ConfigEntry(id = "enabled", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.enabled")
			public static boolean enabled = true;

			@ConfigEntry(id = "weight", type = EntryType.ENUM, translation = "config.arcanuscontinuum.weight")
			public static Weight weight = Weight.NONE;

			@ConfigEntry(id = "manaCost", type = EntryType.DOUBLE, translation = "config.arcanuscontinuum.manaCost")
			@DoubleRange(min = 0, max = 200)
			public static double manaCost = 8.5;

			@ConfigEntry(id = "coolDown", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.coolDown")
			@IntRange(min = 0, max = 24000)
			public static int coolDown = 0;

			@ConfigEntry(id = "minimumLevel", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.minimumLevel")
			@IntRange(min = 1, max = 10)
			public static int minimumLevel = 10;

			@ConfigEntry(id = "removedUponTakingDamage", type = EntryType.BOOLEAN, translation = "config.arcanuscontinuum.removedUponTakingDamage")
			public static boolean removedUponTakingDamage = true;

			@ConfigEntry(id = "baseEffectDuration", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.baseEffectDuration")
			@IntRange(min = 0, max = 24000)
			public static int baseEffectDuration = 200;

			@ConfigEntry(id = "effectDurationModifier", type = EntryType.INTEGER, translation = "config.arcanuscontinuum.effectDurationModifier")
			@IntRange(min = 0, max = 24000)
			public static int effectDurationModifier = 100;
		}
	}
}
