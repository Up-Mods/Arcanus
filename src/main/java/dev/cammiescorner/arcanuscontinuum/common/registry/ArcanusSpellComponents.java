package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig.*;
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
	/** DO NOT DELETE OR DISABLE. WILL BREAK THE ENTIRE MOD. **/
	public static final RegistrySupplier<SpellComponent> EMPTY = SPELL_COMPONENTS.register("empty", () -> new SpellShape(true, Weight.NONE, 0, 1, 0, 0, 0) {
		@Override
		public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
			castNext(caster, castFrom, castSource, world, stack, spellGroups, groupIndex, potency);
		}
	});

	//-----Spell Forms-----//
	public static final RegistrySupplier<SpellShape> SELF = SPELL_COMPONENTS.register("self_shape", () -> new SelfSpellShape(SpellShapes.SelfShapeProperties.enabled, SpellShapes.SelfShapeProperties.weight, SpellShapes.SelfShapeProperties.manaCost, SpellShapes.SelfShapeProperties.manaMultiplier, SpellShapes.SelfShapeProperties.coolDown, SpellShapes.SelfShapeProperties.minimumLevel, SpellShapes.SelfShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> TOUCH = SPELL_COMPONENTS.register("touch_shape", () -> new TouchSpellShape(SpellShapes.TouchShapeProperties.enabled, SpellShapes.TouchShapeProperties.weight, SpellShapes.TouchShapeProperties.manaCost, SpellShapes.TouchShapeProperties.manaMultiplier, SpellShapes.TouchShapeProperties.coolDown, SpellShapes.TouchShapeProperties.minimumLevel, SpellShapes.TouchShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> PROJECTILE = SPELL_COMPONENTS.register("projectile_shape", () -> new ProjectileSpellShape(SpellShapes.ProjectileShapeProperties.enabled, SpellShapes.ProjectileShapeProperties.weight, SpellShapes.ProjectileShapeProperties.manaCost, SpellShapes.ProjectileShapeProperties.manaMultiplier, SpellShapes.ProjectileShapeProperties.coolDown, SpellShapes.ProjectileShapeProperties.minimumLevel, SpellShapes.ProjectileShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> LOB = SPELL_COMPONENTS.register("lob_shape", () -> new ProjectileSpellShape(SpellShapes.LobShapeProperties.enabled, SpellShapes.LobShapeProperties.weight, SpellShapes.LobShapeProperties.manaCost, SpellShapes.LobShapeProperties.manaMultiplier, SpellShapes.LobShapeProperties.coolDown, SpellShapes.LobShapeProperties.minimumLevel, SpellShapes.LobShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> BOLT = SPELL_COMPONENTS.register("bolt_shape", () -> new BoltSpellShape(SpellShapes.BoltShapeProperties.enabled, SpellShapes.BoltShapeProperties.weight, SpellShapes.BoltShapeProperties.manaCost, SpellShapes.BoltShapeProperties.manaMultiplier, SpellShapes.BoltShapeProperties.coolDown, SpellShapes.BoltShapeProperties.minimumLevel, SpellShapes.BoltShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> BEAM = SPELL_COMPONENTS.register("beam_shape", () -> new BeamSpellShape(SpellShapes.BeamShapeProperties.enabled, SpellShapes.BeamShapeProperties.weight, SpellShapes.BeamShapeProperties.manaCost, SpellShapes.BeamShapeProperties.manaMultiplier, SpellShapes.BeamShapeProperties.coolDown, SpellShapes.BeamShapeProperties.minimumLevel, SpellShapes.BeamShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> RUNE = SPELL_COMPONENTS.register("rune_shape", () -> new RuneSpellShape(SpellShapes.RuneShapeProperties.enabled, SpellShapes.RuneShapeProperties.weight, SpellShapes.RuneShapeProperties.manaCost, SpellShapes.RuneShapeProperties.manaMultiplier, SpellShapes.RuneShapeProperties.coolDown, SpellShapes.RuneShapeProperties.minimumLevel, SpellShapes.RuneShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> EXPLOSION = SPELL_COMPONENTS.register("explosion_shape", () -> new ExplosionSpellShape(SpellShapes.ExplosionShapeProperties.enabled, SpellShapes.ExplosionShapeProperties.weight, SpellShapes.ExplosionShapeProperties.manaCost, SpellShapes.ExplosionShapeProperties.manaMultiplier, SpellShapes.ExplosionShapeProperties.coolDown, SpellShapes.ExplosionShapeProperties.minimumLevel, SpellShapes.ExplosionShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> COUNTER = SPELL_COMPONENTS.register("counter_shape", () -> new CounterSpellShape(SpellShapes.CounterShapeProperties.enabled, SpellShapes.CounterShapeProperties.weight, SpellShapes.CounterShapeProperties.manaCost, SpellShapes.CounterShapeProperties.manaMultiplier, SpellShapes.CounterShapeProperties.coolDown, SpellShapes.CounterShapeProperties.minimumLevel, SpellShapes.CounterShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> AOE = SPELL_COMPONENTS.register("aoe_shape", () -> new AreaOfEffectSpellShape(SpellShapes.AOEShapeProperties.enabled, SpellShapes.AOEShapeProperties.weight, SpellShapes.AOEShapeProperties.manaCost, SpellShapes.AOEShapeProperties.manaMultiplier, SpellShapes.AOEShapeProperties.coolDown, SpellShapes.AOEShapeProperties.minimumLevel, SpellShapes.AOEShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> SMITE = SPELL_COMPONENTS.register("smite_shape", () -> new SmiteSpellShape(SpellShapes.SmiteShapeProperties.enabled, SpellShapes.SmiteShapeProperties.weight, SpellShapes.SmiteShapeProperties.manaCost, SpellShapes.SmiteShapeProperties.manaMultiplier, SpellShapes.SmiteShapeProperties.coolDown, SpellShapes.SmiteShapeProperties.minimumLevel, SpellShapes.SmiteShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> GUARDIAN_ORB = SPELL_COMPONENTS.register("guardian_orb_shape", () -> new GuardianOrbSpellShape(SpellShapes.GuardianOrbShapeProperties.enabled, SpellShapes.GuardianOrbShapeProperties.weight, SpellShapes.GuardianOrbShapeProperties.manaCost, SpellShapes.GuardianOrbShapeProperties.manaMultiplier, SpellShapes.GuardianOrbShapeProperties.coolDown, SpellShapes.GuardianOrbShapeProperties.minimumLevel, SpellShapes.GuardianOrbShapeProperties.potencyModifier));
	public static final RegistrySupplier<SpellShape> AGGRESSORB = SPELL_COMPONENTS.register("aggressorb_shape", () -> new AggressorbSpellShape(SpellShapes.AggressorbShapeProperties.enabled, SpellShapes.AggressorbShapeProperties.weight, SpellShapes.AggressorbShapeProperties.manaCost, SpellShapes.AggressorbShapeProperties.manaMultiplier, SpellShapes.AggressorbShapeProperties.coolDown, SpellShapes.AggressorbShapeProperties.minimumLevel, SpellShapes.AggressorbShapeProperties.potencyModifier));

	//-----Spell Effects-----//
	public static final RegistrySupplier<SpellEffect> DAMAGE = SPELL_COMPONENTS.register("damage_effect", () -> new DamageSpellEffect(AttackEffects.DamageEffectProperties.enabled, SpellType.ATTACK, AttackEffects.DamageEffectProperties.weight, AttackEffects.DamageEffectProperties.manaCost, AttackEffects.DamageEffectProperties.coolDown, AttackEffects.DamageEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> FIRE = SPELL_COMPONENTS.register("fire_effect", () -> new FireSpellEffect(AttackEffects.FireEffectProperties.enabled, SpellType.ATTACK, AttackEffects.FireEffectProperties.weight, AttackEffects.FireEffectProperties.manaCost, AttackEffects.FireEffectProperties.coolDown, AttackEffects.FireEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> ELECTRIC = SPELL_COMPONENTS.register("electric_effect", () -> new ElectricSpellEffect(AttackEffects.ElectricEffectProperties.enabled, SpellType.ATTACK, AttackEffects.ElectricEffectProperties.weight, AttackEffects.ElectricEffectProperties.manaCost, AttackEffects.ElectricEffectProperties.coolDown, AttackEffects.ElectricEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> ICE = SPELL_COMPONENTS.register("ice_effect", () -> new IceSpellEffect(AttackEffects.IceEffectProperties.enabled, SpellType.ATTACK, AttackEffects.IceEffectProperties.weight, AttackEffects.IceEffectProperties.manaCost, AttackEffects.IceEffectProperties.coolDown, AttackEffects.IceEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> VULNERABILITY = SPELL_COMPONENTS.register("vulnerability_effect", () -> new VulnerabilitySpellEffect(AttackEffects.VulnerabilityEffectProperties.enabled, SpellType.ATTACK, AttackEffects.VulnerabilityEffectProperties.weight, AttackEffects.VulnerabilityEffectProperties.manaCost, AttackEffects.VulnerabilityEffectProperties.coolDown, AttackEffects.VulnerabilityEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> MANA_LOCK = SPELL_COMPONENTS.register("mana_lock_effect", () -> new ManaLockSpellEffect(AttackEffects.ManaLockEffectProperties.enabled, SpellType.ATTACK, AttackEffects.ManaLockEffectProperties.weight, AttackEffects.ManaLockEffectProperties.manaCost, AttackEffects.ManaLockEffectProperties.coolDown, AttackEffects.ManaLockEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> WITHERING = SPELL_COMPONENTS.register("withering_effect", () -> new WitheringSpellEffect(AttackEffects.WitheringEffectProperties.enabled, SpellType.ATTACK, AttackEffects.WitheringEffectProperties.weight, AttackEffects.WitheringEffectProperties.manaCost, AttackEffects.WitheringEffectProperties.coolDown, AttackEffects.WitheringEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> NECROMANCY = SPELL_COMPONENTS.register("necromancy_effect", () -> new NecromancySpellEffect(AttackEffects.NecromancyEffectProperties.enabled, SpellType.ATTACK, AttackEffects.NecromancyEffectProperties.weight, AttackEffects.NecromancyEffectProperties.manaCost, AttackEffects.NecromancyEffectProperties.coolDown, AttackEffects.NecromancyEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> MANA_SPLIT = SPELL_COMPONENTS.register("mana_split_effect", () -> new ManaSplitSpellEffect(AttackEffects.ManaSplitEffectProperties.enabled, SpellType.ATTACK, AttackEffects.ManaSplitEffectProperties.weight, AttackEffects.ManaSplitEffectProperties.manaCost, AttackEffects.ManaSplitEffectProperties.coolDown, AttackEffects.ManaSplitEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> COPPER_CURSE = SPELL_COMPONENTS.register("copper_curse_effect", () -> new CopperCurseSpellEffect(AttackEffects.CopperCurseEffectProperties.enabled, SpellType.ATTACK, AttackEffects.CopperCurseEffectProperties.weight, AttackEffects.CopperCurseEffectProperties.manaCost, AttackEffects.CopperCurseEffectProperties.coolDown, AttackEffects.CopperCurseEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> DISCOMBOBULATE = SPELL_COMPONENTS.register("discombobulate_effect", () -> new DiscombobulateSpellEffect(AttackEffects.DiscombobulateEffectProperties.enabled, SpellType.ATTACK, AttackEffects.DiscombobulateEffectProperties.weight, AttackEffects.DiscombobulateEffectProperties.manaCost, AttackEffects.DiscombobulateEffectProperties.coolDown, AttackEffects.DiscombobulateEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> STOCKPILE = SPELL_COMPONENTS.register("stockpile_effect", () -> new StockpileSpellEffect(AttackEffects.StockpileEffectProperties.enabled, SpellType.ATTACK, AttackEffects.StockpileEffectProperties.weight, AttackEffects.StockpileEffectProperties.manaCost, AttackEffects.StockpileEffectProperties.coolDown, AttackEffects.StockpileEffectProperties.minimumLevel));

	public static final RegistrySupplier<SpellEffect> HEAL = SPELL_COMPONENTS.register("heal_effect", () -> new HealSpellEffect(SupportEffects.HealEffectProperties.enabled, SpellType.SUPPORT, SupportEffects.HealEffectProperties.weight, SupportEffects.HealEffectProperties.manaCost, SupportEffects.HealEffectProperties.coolDown, SupportEffects.HealEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> DISPEL = SPELL_COMPONENTS.register("dispel_effect", () -> new DispelSpellEffect(SupportEffects.DispelEffectProperties.enabled, SpellType.SUPPORT, SupportEffects.DispelEffectProperties.weight, SupportEffects.DispelEffectProperties.manaCost, SupportEffects.DispelEffectProperties.coolDown, SupportEffects.DispelEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> REGENERATE = SPELL_COMPONENTS.register("regenerate_effect", () -> new RegenerateSpellEffect(SupportEffects.RegenerateEffectProperties.enabled, SpellType.SUPPORT, SupportEffects.RegenerateEffectProperties.weight, SupportEffects.RegenerateEffectProperties.manaCost, SupportEffects.RegenerateEffectProperties.coolDown, SupportEffects.RegenerateEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> FORTIFY = SPELL_COMPONENTS.register("fortify_effect", () -> new FortifySpellEffect(SupportEffects.FortifyEffectProperties.enabled, SpellType.SUPPORT, SupportEffects.FortifyEffectProperties.weight, SupportEffects.FortifyEffectProperties.manaCost, SupportEffects.FortifyEffectProperties.coolDown, SupportEffects.FortifyEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> HASTE = SPELL_COMPONENTS.register("haste_effect", () -> new HasteSpellEffect(SupportEffects.HasteEffectProperties.enabled, SpellType.SUPPORT, SupportEffects.HasteEffectProperties.weight, SupportEffects.HasteEffectProperties.manaCost, SupportEffects.HasteEffectProperties.coolDown, SupportEffects.HasteEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> MANA_SHIELD = SPELL_COMPONENTS.register("mana_shield_effect", () -> new ManaShieldSpellEffect(SupportEffects.ManaShieldEffectProperties.enabled, SpellType.SUPPORT, SupportEffects.ManaShieldEffectProperties.weight, SupportEffects.ManaShieldEffectProperties.manaCost, SupportEffects.ManaShieldEffectProperties.coolDown, SupportEffects.ManaShieldEffectProperties.minimumLevel));
//	public static final RegistrySupplier<SpellEffect> TEMPORAL_DILATION = SPELL_COMPONENTS.register("temporal_dilation_effect", () -> new TemporalDilationSpellEffect(temporalDilationEffectProperties.enabled, SpellType.SUPPORT, temporalDilationEffectProperties.weight, temporalDilationEffectProperties.manaCost, temporalDilationEffectProperties.coolDown, temporalDilationEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> DANGER_SENSE = SPELL_COMPONENTS.register("danger_sense_effect", () -> new DangerSenseSpellEffect(SupportEffects.DangerSenseEffectProperties.enabled, SpellType.SUPPORT, SupportEffects.DangerSenseEffectProperties.weight, SupportEffects.DangerSenseEffectProperties.manaCost, SupportEffects.DangerSenseEffectProperties.coolDown, SupportEffects.DangerSenseEffectProperties.minimumLevel));

	public static final RegistrySupplier<SpellEffect> BUILD = SPELL_COMPONENTS.register("build_effect", () -> new BuildSpellEffect(UtilityEffects.BuildEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.BuildEffectProperties.weight, UtilityEffects.BuildEffectProperties.manaCost, UtilityEffects.BuildEffectProperties.coolDown, UtilityEffects.BuildEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> POWER = SPELL_COMPONENTS.register("power_effect", () -> new PowerSpellEffect(UtilityEffects.PowerEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.PowerEffectProperties.weight, UtilityEffects.PowerEffectProperties.manaCost, UtilityEffects.PowerEffectProperties.coolDown, UtilityEffects.PowerEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> ANONYMITY = SPELL_COMPONENTS.register("anonymity_effect", () -> new AnonymitySpellEffect(UtilityEffects.AnonymityEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.AnonymityEffectProperties.weight, UtilityEffects.AnonymityEffectProperties.manaCost, UtilityEffects.AnonymityEffectProperties.coolDown, UtilityEffects.AnonymityEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> MINE = SPELL_COMPONENTS.register("mine_effect", () -> new MineSpellEffect(UtilityEffects.MineEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.MineEffectProperties.weight, UtilityEffects.MineEffectProperties.manaCost, UtilityEffects.MineEffectProperties.coolDown, UtilityEffects.MineEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> GROWTH = SPELL_COMPONENTS.register("growth_effect", () -> new GrowthSpellEffect(UtilityEffects.GrowthEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.GrowthEffectProperties.weight, UtilityEffects.GrowthEffectProperties.manaCost, UtilityEffects.GrowthEffectProperties.coolDown, UtilityEffects.GrowthEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> SHRINK = ArcanusCompat.PEHKUI.isEnabled() ? SPELL_COMPONENTS.register("shrink_effect", () -> new SizeChangeSpellEffect(UtilityEffects.ShrinkEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.ShrinkEffectProperties.weight, UtilityEffects.ShrinkEffectProperties.manaCost, UtilityEffects.ShrinkEffectProperties.coolDown, UtilityEffects.ShrinkEffectProperties.minimumLevel)) : null;
	public static final RegistrySupplier<SpellEffect> ENLARGE = ArcanusCompat.PEHKUI.isEnabled() ? SPELL_COMPONENTS.register("enlarge_effect", () -> new SizeChangeSpellEffect(UtilityEffects.EnlargeEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.EnlargeEffectProperties.weight, UtilityEffects.EnlargeEffectProperties.manaCost, UtilityEffects.EnlargeEffectProperties.coolDown, UtilityEffects.EnlargeEffectProperties.minimumLevel)) : null;
	public static final RegistrySupplier<SpellEffect> SPATIAL_RIFT = SPELL_COMPONENTS.register("spatial_rift_effect", () -> new SpatialRiftSpellEffect(UtilityEffects.SpatialRiftEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.SpatialRiftEffectProperties.weight, UtilityEffects.SpatialRiftEffectProperties.manaCost, UtilityEffects.SpatialRiftEffectProperties.coolDown, UtilityEffects.SpatialRiftEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> WARDING = SPELL_COMPONENTS.register("warding_effect", () -> new WardingSpellEffect(UtilityEffects.WardingEffectProperties.enabled, SpellType.UTILITY, UtilityEffects.WardingEffectProperties.weight, UtilityEffects.WardingEffectProperties.manaCost, UtilityEffects.WardingEffectProperties.coolDown, UtilityEffects.WardingEffectProperties.minimumLevel));

	public static final RegistrySupplier<SpellEffect> PUSH = SPELL_COMPONENTS.register("push_effect", () -> new PushSpellEffect(MovementEffects.PushEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.PushEffectProperties.weight, MovementEffects.PushEffectProperties.manaCost, MovementEffects.PushEffectProperties.coolDown, MovementEffects.PushEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> PULL = SPELL_COMPONENTS.register("pull_effect", () -> new PullSpellEffect(MovementEffects.PullEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.PullEffectProperties.weight, MovementEffects.PullEffectProperties.manaCost, MovementEffects.PullEffectProperties.coolDown, MovementEffects.PullEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> LEVITATE = SPELL_COMPONENTS.register("levitate_effect", () -> new LevitateSpellEffect(MovementEffects.LevitateEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.LevitateEffectProperties.weight, MovementEffects.LevitateEffectProperties.manaCost, MovementEffects.LevitateEffectProperties.coolDown, MovementEffects.LevitateEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> SPEED = SPELL_COMPONENTS.register("speed_effect", () -> new SpeedSpellEffect(MovementEffects.SpeedEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.SpeedEffectProperties.weight, MovementEffects.SpeedEffectProperties.manaCost, MovementEffects.SpeedEffectProperties.coolDown, MovementEffects.SpeedEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> TELEPORT = SPELL_COMPONENTS.register("teleport_effect", () -> new TeleportSpellEffect(MovementEffects.TeleportEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.TeleportEffectProperties.weight, MovementEffects.TeleportEffectProperties.manaCost, MovementEffects.TeleportEffectProperties.coolDown, MovementEffects.TeleportEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> BOUNCY = SPELL_COMPONENTS.register("bouncy_effect", () -> new BouncySpellEffect(MovementEffects.BouncyEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.BouncyEffectProperties.weight, MovementEffects.BouncyEffectProperties.manaCost, MovementEffects.BouncyEffectProperties.coolDown, MovementEffects.BouncyEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> FEATHER = SPELL_COMPONENTS.register("feather_effect", () -> new FeatherSpellEffect(MovementEffects.FeatherEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.FeatherEffectProperties.weight, MovementEffects.FeatherEffectProperties.manaCost, MovementEffects.FeatherEffectProperties.coolDown, MovementEffects.FeatherEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> FLOAT = SPELL_COMPONENTS.register("float_effect", () -> new FloatSpellEffect(MovementEffects.FloatEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.FloatEffectProperties.weight, MovementEffects.FloatEffectProperties.manaCost, MovementEffects.FloatEffectProperties.coolDown, MovementEffects.FloatEffectProperties.minimumLevel));
	public static final RegistrySupplier<SpellEffect> MANA_WINGS = SPELL_COMPONENTS.register("mana_wings_effect", () -> new ManaWingsSpellEffect(MovementEffects.ManaWingsEffectProperties.enabled, SpellType.MOVEMENT, MovementEffects.ManaWingsEffectProperties.weight, MovementEffects.ManaWingsEffectProperties.manaCost, MovementEffects.ManaWingsEffectProperties.coolDown, MovementEffects.ManaWingsEffectProperties.minimumLevel));
}
