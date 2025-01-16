package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.common.entities.magic.*;
import dev.cammiescorner.fabric.common.entities.magic.*;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.fabric.common.entities.living.NecroSkeletonEntity;
import dev.cammiescorner.arcanus.fabric.common.entities.living.OpossumEntity;
import dev.cammiescorner.arcanus.fabric.common.entities.living.WizardEntity;
import dev.cammiescorner.arcanus.common.entities.magic.*;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;

public class ArcanusEntities {
	public static final RegistryHandler<EntityType<?>> ENTITY_TYPES = RegistryHandler.create(Registries.ENTITY_TYPE, Arcanus.MOD_ID);

	public static final RegistrySupplier<EntityType<WizardEntity>> WIZARD = ENTITY_TYPES.register("wizard", () -> FabricEntityTypeBuilder.createMob().entityFactory(WizardEntity::new).defaultAttributes(WizardEntity::createMobAttributes).dimensions(EntityDimensions.scalable(0.7F, 1.8F)).build());
	public static final RegistrySupplier<EntityType<OpossumEntity>> OPOSSUM = ENTITY_TYPES.register("opossum", () -> FabricEntityTypeBuilder.createMob().entityFactory(OpossumEntity::new).defaultAttributes(OpossumEntity::createMobAttributes).dimensions(EntityDimensions.scalable(0.6F, 0.7F)).build());
	public static final RegistrySupplier<EntityType<NecroSkeletonEntity>> NECRO_SKELETON = ENTITY_TYPES.register("necro_skeleton", () -> FabricEntityTypeBuilder.createMob().entityFactory(NecroSkeletonEntity::new).disableSummon().defaultAttributes(NecroSkeletonEntity::createAttributes).dimensions(EntityDimensions.scalable(0.6F, 1.8F)).build());
	public static final RegistrySupplier<EntityType<ManaShieldEntity>> MANA_SHIELD = ENTITY_TYPES.register("mana_shield", () -> FabricEntityTypeBuilder.create().entityFactory(ManaShieldEntity::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(4F, 4F)).build());
	public static final RegistrySupplier<EntityType<MagicProjectileEntity>> MAGIC_PROJECTILE = ENTITY_TYPES.register("magic_projectile", () -> FabricEntityTypeBuilder.create().entityFactory(MagicProjectileEntity::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(0.6F, 0.6F)).build());
	public static final RegistrySupplier<EntityType<SmiteEntity>> SMITE = ENTITY_TYPES.register("smite", () -> FabricEntityTypeBuilder.create().entityFactory(SmiteEntity::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(4F, 4F)).build());
	public static final RegistrySupplier<EntityType<MagicRuneEntity>> MAGIC_RUNE = ENTITY_TYPES.register("magic_rune", () -> FabricEntityTypeBuilder.create().entityFactory(MagicRuneEntity::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(1F, 0.125F)).build());
	public static final RegistrySupplier<EntityType<AreaOfEffectEntity>> AOE = ENTITY_TYPES.register("area_of_effect", () -> FabricEntityTypeBuilder.create().entityFactory(AreaOfEffectEntity::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(4F, 2.5F)).build());
	public static final RegistrySupplier<EntityType<BeamEntity>> BEAM = ENTITY_TYPES.register("beam", () -> FabricEntityTypeBuilder.create().entityFactory(BeamEntity::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(0.1F, 0.1F)).build());
	public static final RegistrySupplier<EntityType<GuardianOrbEntity>> GUARDIAN_ORB = ENTITY_TYPES.register("guardian_orb", () -> FabricEntityTypeBuilder.create().entityFactory(GuardianOrbEntity::new).trackedUpdateRate(60).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());
	public static final RegistrySupplier<EntityType<AggressorbEntity>> AGGRESSORB = ENTITY_TYPES.register("aggressorb", () -> FabricEntityTypeBuilder.create().entityFactory(AggressorbEntity::new).trackedUpdateRate(60).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());
	public static final RegistrySupplier<EntityType<PocketDimensionPortalEntity>> PORTAL = ENTITY_TYPES.register("pocket_dimension_portal", () -> FabricEntityTypeBuilder.create().entityFactory(PocketDimensionPortalEntity::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(1.5f, 0.1f)).build());
}
