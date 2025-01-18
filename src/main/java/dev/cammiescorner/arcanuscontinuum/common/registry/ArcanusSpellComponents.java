package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.*;
import dev.cammiescorner.arcanuscontinuum.common.compat.ArcanusCompat;
import dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.attack.*;
import dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.movement.*;
import dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.support.*;
import dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility.*;
import dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes.*;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArcanusSpellComponents {
	public static final RegistryHandler<SpellComponent> SPELL_COMPONENTS = RegistryHandler.create(Arcanus.SPELL_COMPONENTS_REGISTRY_KEY, Arcanus.MOD_ID);

	//-----Empty Spell-----//
	/**
	 * DO NOT DELETE OR DISABLE. WILL BREAK THE ENTIRE MOD.
	 **/
	public static final RegistrySupplier<SpellComponent> EMPTY = SPELL_COMPONENTS.register("empty", () -> new SpellShape(true, Weight.NONE, 0, 1, 0, 0, 0) {
		@Override
		public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
			castNext(caster, castFrom, castSource, world, stack, spellGroups, groupIndex, potency);
		}
	});

	//-----Spell Shapes-----//
	public static final RegistrySupplier<SpellShape> SELF = SPELL_COMPONENTS.register("self_shape", SelfSpellShape::new);
	public static final RegistrySupplier<SpellShape> TOUCH = SPELL_COMPONENTS.register("touch_shape", TouchSpellShape::new);
	public static final RegistrySupplier<SpellShape> PROJECTILE = SPELL_COMPONENTS.register("projectile_shape", ProjectileSpellShape::new);
	public static final RegistrySupplier<SpellShape> LOB = SPELL_COMPONENTS.register("lob_shape", LobSpellShape::new);
	public static final RegistrySupplier<SpellShape> BOLT = SPELL_COMPONENTS.register("bolt_shape", BoltSpellShape::new);
	public static final RegistrySupplier<SpellShape> BEAM = SPELL_COMPONENTS.register("beam_shape", BeamSpellShape::new);
	public static final RegistrySupplier<SpellShape> RUNE = SPELL_COMPONENTS.register("rune_shape", RuneSpellShape::new);
	public static final RegistrySupplier<SpellShape> BURST = SPELL_COMPONENTS.register("burst_shape", BurstSpellShape::new);
	public static final RegistrySupplier<SpellShape> GUIDED_SHOT = SPELL_COMPONENTS.register("guided_shot_shape", GuidedShotSpellShape::new);
	public static final RegistrySupplier<SpellShape> COUNTER = SPELL_COMPONENTS.register("counter_shape", CounterSpellShape::new);
	public static final RegistrySupplier<SpellShape> AOE = SPELL_COMPONENTS.register("aoe_shape", AreaOfEffectSpellShape::new);
	public static final RegistrySupplier<SpellShape> SMITE = SPELL_COMPONENTS.register("smite_shape", SmiteSpellShape::new);
	public static final RegistrySupplier<SpellShape> GUARDIAN_ORB = SPELL_COMPONENTS.register("guardian_orb_shape", GuardianOrbSpellShape::new); // TODO replace name with something better. also change the texture file
	public static final RegistrySupplier<SpellShape> AGGRESSORB = SPELL_COMPONENTS.register("aggressorb_shape", AggressorbSpellShape::new); // TODO replace name with something better. also change the texture file

	//-----Spell Effects-----//
	public static final RegistrySupplier<SpellEffect> DAMAGE = SPELL_COMPONENTS.register("damage_effect", DamageSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> FIRE = SPELL_COMPONENTS.register("fire_effect", FireSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> ELECTRIC = SPELL_COMPONENTS.register("electric_effect", ElectricSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> ICE = SPELL_COMPONENTS.register("ice_effect", IceSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> VULNERABILITY = SPELL_COMPONENTS.register("vulnerability_effect", VulnerabilitySpellEffect::new);
	public static final RegistrySupplier<SpellEffect> MANA_LOCK = SPELL_COMPONENTS.register("mana_lock_effect", ManaLockSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> WITHERING = SPELL_COMPONENTS.register("withering_effect", WitheringSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> NECROMANCY = SPELL_COMPONENTS.register("necromancy_effect", NecromancySpellEffect::new);
	public static final RegistrySupplier<SpellEffect> MANA_SPLIT = SPELL_COMPONENTS.register("mana_split_effect", ManaSplitSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> COPPER_CURSE = SPELL_COMPONENTS.register("copper_curse_effect", CopperCurseSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> DISCOMBOBULATE = SPELL_COMPONENTS.register("discombobulate_effect", DiscombobulateSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> STOCKPILE = SPELL_COMPONENTS.register("stockpile_effect", StockpileSpellEffect::new);

	public static final RegistrySupplier<SpellEffect> HEAL = SPELL_COMPONENTS.register("heal_effect", HealSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> DISPEL = SPELL_COMPONENTS.register("dispel_effect", DispelSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> REGENERATE = SPELL_COMPONENTS.register("regenerate_effect", RegenerateSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> FORTIFY = SPELL_COMPONENTS.register("fortify_effect", FortifySpellEffect::new);
	public static final RegistrySupplier<SpellEffect> HASTE = SPELL_COMPONENTS.register("haste_effect", HasteSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> MANA_SHIELD = SPELL_COMPONENTS.register("mana_shield_effect", ManaShieldSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> TEMPORAL_DILATION = SPELL_COMPONENTS.register("temporal_dilation_effect", TemporalDilationSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> DANGER_SENSE = SPELL_COMPONENTS.register("danger_sense_effect", DangerSenseSpellEffect::new);

	public static final RegistrySupplier<SpellEffect> BUILD = SPELL_COMPONENTS.register("build_effect", BuildSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> POWER = SPELL_COMPONENTS.register("power_effect", PowerSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> ANONYMITY = SPELL_COMPONENTS.register("anonymity_effect", AnonymitySpellEffect::new);
	public static final RegistrySupplier<SpellEffect> MINE = SPELL_COMPONENTS.register("mine_effect", MineSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> GROWTH = SPELL_COMPONENTS.register("growth_effect", GrowthSpellEffect::new);
	@Nullable public static final RegistrySupplier<SpellEffect> SHRINK = ArcanusCompat.PEHKUI.orElse(() -> () -> SPELL_COMPONENTS.register("shrink_effect", ShrinkSpellEffect::new), null);
	@Nullable public static final RegistrySupplier<SpellEffect> ENLARGE = ArcanusCompat.PEHKUI.orElse(() -> () -> SPELL_COMPONENTS.register("enlarge_effect", EnlargeSpellEffect::new), null);
	public static final RegistrySupplier<SpellEffect> SPATIAL_RIFT = SPELL_COMPONENTS.register("spatial_rift_effect", SpatialRiftSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> WARDING = SPELL_COMPONENTS.register("warding_effect", WardingSpellEffect::new);

	public static final RegistrySupplier<SpellEffect> PUSH = SPELL_COMPONENTS.register("push_effect", PushSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> PULL = SPELL_COMPONENTS.register("pull_effect", PullSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> LEVITATE = SPELL_COMPONENTS.register("levitate_effect", LevitateSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> SPEED = SPELL_COMPONENTS.register("speed_effect", SpeedSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> TELEPORT = SPELL_COMPONENTS.register("teleport_effect", TeleportSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> BOUNCY = SPELL_COMPONENTS.register("bouncy_effect", BouncySpellEffect::new);
	public static final RegistrySupplier<SpellEffect> FEATHER = SPELL_COMPONENTS.register("feather_effect", FeatherSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> FLOAT = SPELL_COMPONENTS.register("float_effect", FloatSpellEffect::new);
	public static final RegistrySupplier<SpellEffect> MANA_WINGS = SPELL_COMPONENTS.register("mana_wings_effect", ManaWingsSpellEffect::new);
}
