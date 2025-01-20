package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.entities.living.NecroSkeleton;
import dev.cammiescorner.arcanuscontinuum.common.entities.living.Opossum;
import dev.cammiescorner.arcanuscontinuum.common.entities.living.Wizard;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.*;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;

public class ArcanusEntities {
	public static final RegistryHandler<EntityType<?>> ENTITY_TYPES = RegistryHandler.create(Registries.ENTITY_TYPE, Arcanus.MOD_ID);

	public static final RegistrySupplier<EntityType<Wizard>> WIZARD = ENTITY_TYPES.register("wizard", () -> FabricEntityTypeBuilder.createMob().entityFactory(Wizard::new).defaultAttributes(Wizard::createMobAttributes).dimensions(EntityDimensions.scalable(0.7F, 1.8F)).build());
	public static final RegistrySupplier<EntityType<Opossum>> OPOSSUM = ENTITY_TYPES.register("opossum", () -> FabricEntityTypeBuilder.createMob().entityFactory(Opossum::new).defaultAttributes(Opossum::createMobAttributes).dimensions(EntityDimensions.scalable(0.6F, 0.7F)).build());
	public static final RegistrySupplier<EntityType<NecroSkeleton>> NECRO_SKELETON = ENTITY_TYPES.register("necro_skeleton", () -> FabricEntityTypeBuilder.createMob().entityFactory(NecroSkeleton::new).disableSummon().defaultAttributes(NecroSkeleton::createAttributes).dimensions(EntityDimensions.scalable(0.6F, 1.8F)).build());
	public static final RegistrySupplier<EntityType<ManaShield>> MANA_SHIELD = ENTITY_TYPES.register("mana_shield", () -> FabricEntityTypeBuilder.create().entityFactory(ManaShield::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(4F, 4F)).build());
	public static final RegistrySupplier<EntityType<MagicProjectile>> MAGIC_PROJECTILE = ENTITY_TYPES.register("magic_projectile", () -> FabricEntityTypeBuilder.create().entityFactory(MagicProjectile::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(0.6F, 0.6F)).build());
	public static final RegistrySupplier<EntityType<Smite>> SMITE = ENTITY_TYPES.register("smite", () -> FabricEntityTypeBuilder.create().entityFactory(Smite::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(4F, 4F)).build());
	public static final RegistrySupplier<EntityType<MagicRune>> MAGIC_RUNE = ENTITY_TYPES.register("magic_rune", () -> FabricEntityTypeBuilder.create().entityFactory(MagicRune::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(1F, 0.125F)).build());
	public static final RegistrySupplier<EntityType<AreaOfEffect>> AOE = ENTITY_TYPES.register("area_of_effect", () -> FabricEntityTypeBuilder.create().entityFactory(AreaOfEffect::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(4F, 2.5F)).build());
	public static final RegistrySupplier<EntityType<Beam>> BEAM = ENTITY_TYPES.register("beam", () -> FabricEntityTypeBuilder.create().entityFactory(Beam::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(0.1F, 0.1F)).build());
	public static final RegistrySupplier<EntityType<EntangledOrb>> ENTANGLED_ORB = ENTITY_TYPES.register("entangled_orb", () -> FabricEntityTypeBuilder.create().entityFactory(EntangledOrb::new).trackedUpdateRate(60).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());
	public static final RegistrySupplier<EntityType<Aggressorb>> AGGRESSORB = ENTITY_TYPES.register("aggressorb", () -> FabricEntityTypeBuilder.create().entityFactory(Aggressorb::new).trackedUpdateRate(60).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(0.4F, 0.4F)).build());
	public static final RegistrySupplier<EntityType<PocketDimensionPortal>> PORTAL = ENTITY_TYPES.register("pocket_dimension_portal", () -> FabricEntityTypeBuilder.create().entityFactory(PocketDimensionPortal::new).fireImmune().disableSummon().dimensions(EntityDimensions.fixed(1.5f, 0.1f)).build());
	public static final RegistrySupplier<EntityType<TemporalDilationField>> TEMPORAL_DILATION_FIELD = ENTITY_TYPES.register("temporal_dilation_field", () -> FabricEntityTypeBuilder.create().entityFactory(TemporalDilationField::new).fireImmune().disableSummon().dimensions(EntityDimensions.scalable(6f, 6f)).build());
}
