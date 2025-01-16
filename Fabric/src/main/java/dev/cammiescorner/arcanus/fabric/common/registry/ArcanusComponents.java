package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.api.spells.Pattern;
import dev.cammiescorner.arcanus.api.spells.SpellEffect;
import dev.cammiescorner.arcanus.api.spells.SpellGroup;
import dev.cammiescorner.arcanus.api.spells.SpellShape;
import dev.cammiescorner.arcanus.fabric.common.blocks.entities.AbstractMagicBlockEntity;
import dev.cammiescorner.arcanus.fabric.common.compat.ArcanusCompat;
import dev.cammiescorner.arcanus.fabric.common.compat.PehkuiCompat;
import dev.cammiescorner.arcanus.fabric.common.components.MagicColorComponent;
import dev.cammiescorner.arcanus.fabric.common.components.chunk.WardedBlocksComponent;
import dev.cammiescorner.arcanus.fabric.common.components.color.GenericMagicColorComponent;
import dev.cammiescorner.arcanus.fabric.common.components.color.PlayerMagicColorComponent;
import dev.cammiescorner.arcanus.fabric.common.components.entity.*;
import dev.cammiescorner.arcanus.fabric.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.*;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.chunk.ChunkComponentInitializer;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArcanusComponents implements BlockComponentInitializer, ChunkComponentInitializer, EntityComponentInitializer, ScoreboardComponentInitializer {

	public static final ComponentKey<MagicColorComponent> MAGIC_COLOR = createComponent("magic_color", MagicColorComponent.class);

	// ----- Scoreboard Components ----- \\
	public static final ComponentKey<PocketDimensionComponent> POCKET_DIMENSION_COMPONENT = createComponent("pocket_dimension", PocketDimensionComponent.class);

	// ----- Chunk Components ----- \\
	public static final ComponentKey<WardedBlocksComponent> WARDED_BLOCKS_COMPONENT = createComponent("warded_blocks", WardedBlocksComponent.class);

	// ----- Entity Components ----- \\
	public static final ComponentKey<WizardLevelComponent> WIZARD_LEVEL_COMPONENT = createComponent("wizard_level", WizardLevelComponent.class);
	public static final ComponentKey<ManaComponent> MANA_COMPONENT = createComponent("mana", ManaComponent.class);
	public static final ComponentKey<BurnoutComponent> BURNOUT_COMPONENT = createComponent("burnout", BurnoutComponent.class);
	public static final ComponentKey<CastingComponent> CASTING_COMPONENT = createComponent("casting", CastingComponent.class);
	public static final ComponentKey<PatternComponent> PATTERN_COMPONENT = createComponent("casting_pattern", PatternComponent.class);
	public static final ComponentKey<LastCastTimeComponent> LAST_CAST_TIME_COMPONENT = createComponent("last_cast_time", LastCastTimeComponent.class);
	public static final ComponentKey<StunComponent> STUN_COMPONENT = createComponent("stun", StunComponent.class);
	public static final ComponentKey<QuestComponent> QUEST_COMPONENT = createComponent("quests", QuestComponent.class);
	public static final ComponentKey<BoltTargetComponent> BOLT_TARGET = createComponent("bolt_target", BoltTargetComponent.class);
	public static final ComponentKey<SpellShapeComponent> SPELL_SHAPE = createComponent("spell_shape", SpellShapeComponent.class);
	public static final ComponentKey<SizeComponent> SIZE = createComponent("size", SizeComponent.class);
	public static final ComponentKey<PocketDimensionPortalComponent> POCKET_DIMENSION_PORTAL_COMPONENT = createComponent("pocket_dimension_portal", PocketDimensionPortalComponent.class);
	public static final ComponentKey<SlowTimeComponent> SLOW_TIME_COMPONENT = createComponent("slow_time", SlowTimeComponent.class);
	public static final ComponentKey<AggressorbComponent> AGGRESSORB_COMPONENT = createComponent("aggressorb", AggressorbComponent.class);
	public static final ComponentKey<GuardianOrbComponent> GUARDIAN_ORB_COMPONENT = createComponent("guardian_orb", GuardianOrbComponent.class);
	public static final ComponentKey<PortalCoolDownComponent> PORTAL_COOL_DOWN_COMPONENT = createComponent("portal_cool_down", PortalCoolDownComponent.class);
	public static final ComponentKey<CounterComponent> COUNTER_COMPONENT = createComponent("counter", CounterComponent.class);

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
		registry.beginRegistration(AbstractMagicBlockEntity.class, MAGIC_COLOR)
				.impl(GenericMagicColorComponent.class)
				.end(GenericMagicColorComponent::new);
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(WARDED_BLOCKS_COMPONENT, WardedBlocksComponent::new);
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(Player.class, WIZARD_LEVEL_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(WizardLevelComponent::new);
		registry.beginRegistration(LivingEntity.class, MANA_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(ManaComponent::new);
		registry.beginRegistration(LivingEntity.class, BURNOUT_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(BurnoutComponent::new);
		registry.beginRegistration(Player.class, CASTING_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(CastingComponent::new);
		registry.beginRegistration(Player.class, PATTERN_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(PatternComponent::new);
		registry.beginRegistration(Player.class, POCKET_DIMENSION_PORTAL_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(PocketDimensionPortalComponent::new);
		registry.beginRegistration(LivingEntity.class, LAST_CAST_TIME_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(LastCastTimeComponent::new);
		registry.beginRegistration(LivingEntity.class, STUN_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(StunComponent::new);
		registry.beginRegistration(Player.class, QUEST_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(QuestComponent::new);
		registry.beginRegistration(LivingEntity.class, BOLT_TARGET).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(BoltTargetComponent::new);
		registry.beginRegistration(MagicProjectileEntity.class, SPELL_SHAPE).end(SpellShapeComponent::new);
		registry.beginRegistration(Entity.class, SLOW_TIME_COMPONENT).end(SlowTimeComponent::new);
		registry.beginRegistration(LivingEntity.class, AGGRESSORB_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(AggressorbComponent::new);
		registry.beginRegistration(LivingEntity.class, GUARDIAN_ORB_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(GuardianOrbComponent::new);
		registry.beginRegistration(Player.class, PORTAL_COOL_DOWN_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(PortalCoolDownComponent::new);
		registry.beginRegistration(LivingEntity.class, COUNTER_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(CounterComponent::new);

		List.of(
				AggressorbEntity.class,
				AreaOfEffectEntity.class,
				BeamEntity.class,
				GuardianOrbEntity.class,
				MagicProjectileEntity.class,
				MagicRuneEntity.class,
				ManaShieldEntity.class,
				PocketDimensionPortalEntity.class,
				SmiteEntity.class
		).forEach(type ->
				registry.beginRegistration(type, MAGIC_COLOR)
						.impl(GenericMagicColorComponent.class)
						.end(GenericMagicColorComponent::new)
		);
		registry.registerForPlayers(MAGIC_COLOR, PlayerMagicColorComponent::new, RespawnCopyStrategy.NEVER_COPY);

		ArcanusCompat.PEHKUI.ifEnabled(() -> () -> {
			PehkuiCompat.registerEntityComponents(registry);
			PehkuiCompat.registerModifiers();
		});
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(FabricMain.id(name), component);
	}

	@Nullable
	private static WardedBlocksComponent getWardedBlocksComponent(Level world, BlockPos pos) {
		ChunkAccess chunk = world.getChunk(pos);
		return !(chunk instanceof EmptyLevelChunk) ? chunk.getComponent(WARDED_BLOCKS_COMPONENT) : null;
	}

	// ----- Helper Methods ----- //
	public static void addWardedBlock(Player player, BlockPos pos) {
		WardedBlocksComponent component = getWardedBlocksComponent(player.level(), pos);
		if(component != null)
			component.addWardedBlock(player, pos);
	}

	public static void removeWardedBlock(Player player, BlockPos pos) {
		WardedBlocksComponent component = getWardedBlocksComponent(player.level(), pos);
		if(component != null)
			component.removeWardedBlock(player, pos);
	}

	public static boolean isOwnerOfBlock(Player player, BlockPos pos) {
		WardedBlocksComponent component = getWardedBlocksComponent(player.level(), pos);
		return component != null && component.isOwnerOfBlock(player, pos);
	}

	public static boolean isBlockWarded(Level world, BlockPos pos) {
		WardedBlocksComponent component = getWardedBlocksComponent(world, pos);
		return component != null && component.isBlockWarded(pos);
	}

	public static Map<BlockPos, java.util.UUID> getWardedBlocks(ChunkAccess chunk) {
		if(chunk instanceof EmptyLevelChunk)
			return Map.of();

		return chunk.getComponent(WARDED_BLOCKS_COMPONENT).getWardedBlocks();
	}

	public static double getMaxMana(LivingEntity entity) {
		return entity.getComponent(MANA_COMPONENT).getMaxMana();
	}

	public static double getManaLock(LivingEntity entity) {
		return entity.getComponent(MANA_COMPONENT).getManaLock();
	}

	public static double getTrueMaxMana(LivingEntity entity) {
		return entity.getComponent(MANA_COMPONENT).getTrueMaxMana();
	}

	public static double getMana(LivingEntity entity) {
		return entity.getComponent(MANA_COMPONENT).getMana();
	}

	public static void setMana(LivingEntity entity, double amount) {
		entity.getComponent(MANA_COMPONENT).setMana(amount);
	}

	public static boolean addMana(LivingEntity entity, double amount, boolean simulate) {
		return entity.getComponent(MANA_COMPONENT).addMana(amount, simulate);
	}

	public static boolean drainMana(LivingEntity entity, double amount, boolean simulate) {
		return entity.getComponent(MANA_COMPONENT).drainMana(amount, simulate);
	}

	public static double getBurnout(LivingEntity entity) {
		return BURNOUT_COMPONENT.get(entity).getBurnout();
	}

	public static void setBurnout(LivingEntity entity, double amount) {
		BURNOUT_COMPONENT.get(entity).setBurnout(amount);
	}

	public static boolean addBurnout(LivingEntity entity, double amount, boolean simulate) {
		return BURNOUT_COMPONENT.get(entity).addBurnout(amount, simulate);
	}

	public static boolean drainBurnout(LivingEntity entity, double amount, boolean simulate) {
		return BURNOUT_COMPONENT.get(entity).drainBurnout(amount, simulate);
	}

	public static int getWizardLevel(LivingEntity entity) {
		return WIZARD_LEVEL_COMPONENT.get(entity).getLevel();
	}

	public static void setWizardLevel(LivingEntity entity, int level) {
		WIZARD_LEVEL_COMPONENT.get(entity).setLevel(level);
	}

	public static int maxSpellSize(LivingEntity entity) {
		int level = getWizardLevel(entity);
		return level > 0 ? level == 1 ? 2 : 2 + level : 0;
	}

	public static void increaseWizardLevel(LivingEntity entity, int amount) {
		setWizardLevel(entity, getWizardLevel(entity) + amount);
	}

	public static boolean isCasting(LivingEntity entity) {
		return CASTING_COMPONENT.get(entity).isCasting();
	}

	public static void setCasting(LivingEntity entity, boolean casting) {
		CASTING_COMPONENT.get(entity).setCasting(casting);
	}

	public static List<Pattern> getPattern(LivingEntity entity) {
		return PATTERN_COMPONENT.get(entity).getPattern();
	}

	public static void setPattern(LivingEntity entity, List<Pattern> pattern) {
		PATTERN_COMPONENT.get(entity).setPattern(pattern);
	}

	public static void clearPattern(LivingEntity entity) {
		PATTERN_COMPONENT.get(entity).clearPattern();
	}

	public static long getLastCastTime(LivingEntity entity) {
		return LAST_CAST_TIME_COMPONENT.get(entity).getLastCastTime();
	}

	public static void setLastCastTime(LivingEntity entity, long time) {
		LAST_CAST_TIME_COMPONENT.get(entity).setLastCastTime(time);
	}

	public static int getStunTimer(LivingEntity entity) {
		return STUN_COMPONENT.get(entity).getStunTimer();
	}

	public static void setStunTimer(LivingEntity entity, int timer) {
		STUN_COMPONENT.get(entity).setStunTimer(timer);
	}

	public static List<ResourceLocation> getQuestIds(Player player) {
		return QUEST_COMPONENT.get(player).getQuestIds();
	}

	public static long getLastCompletedQuestTime(Player player) {
		return QUEST_COMPONENT.get(player).getLastCompletedQuestTime();
	}

	public static void setLastCompletedQuestTime(Player player, long time) {
		QUEST_COMPONENT.get(player).setLastCompletedQuestTime(time);
	}

	public static Color getColor(Entity entity) {
		return MAGIC_COLOR.get(entity).getColor();
	}

	public static Vec3 getBoltPos(LivingEntity entity) {
		return BOLT_TARGET.get(entity).getPos();
	}

	public static void setBoltPos(LivingEntity entity, Vec3 pos) {
		BOLT_TARGET.get(entity).setPos(pos);
	}

	public static boolean shouldRenderBolt(LivingEntity entity) {
		return BOLT_TARGET.get(entity).shouldRender();
	}

	public static void setShouldRenderBolt(LivingEntity entity, boolean shouldRender) {
		BOLT_TARGET.get(entity).setShouldRender(shouldRender);
	}

	public static void setBoltAge(LivingEntity entity, int timer) {
		BOLT_TARGET.get(entity).setAge(timer);
	}

	public static SpellShape getSpellShape(Entity entity) {
		return SPELL_SHAPE.get(entity).getSpellShape();
	}

	public static void setSpellShape(Entity entity, SpellShape shape) {
		SPELL_SHAPE.get(entity).setSpellShape(shape);
	}

	public static void setScale(Entity entity, SpellEffect effect, double strength) {
		SIZE.get(entity).setScale(effect, strength);
	}

	public static void resetScale(Entity entity) {
		entity.getComponent(SIZE).resetScale();
	}

	public static void createPortal(Player player, ServerLevel world, Vec3 pos, double pullStrength) {
		POCKET_DIMENSION_PORTAL_COMPONENT.get(player).createPortal(world, pos, pullStrength);
	}

	public static Vec3 getPortalPos(Player player, Level level) {
		return POCKET_DIMENSION_PORTAL_COMPONENT.get(player).getPortalPos(level);
	}

	public static boolean isTimeSlowed(Entity entity) {
		return entity.getComponent(SLOW_TIME_COMPONENT).isTimeSlowed();
	}

	public static void setSlowTime(Entity entity, boolean slowTime) {
		entity.getComponent(SLOW_TIME_COMPONENT).setSlowTime(slowTime);
	}

	public static boolean areUpdatesBlocked(Entity entity) {
		return entity.getComponent(SLOW_TIME_COMPONENT).areUpdatesBlocked();
	}

	public static void setBlockUpdates(Entity entity, boolean blockUpdates) {
		entity.getComponent(SLOW_TIME_COMPONENT).setBlockUpdates(blockUpdates);
	}

	public static int getBlockUpdatesInterval(Entity entity) {
		return entity.getComponent(SLOW_TIME_COMPONENT).getBlockUpdatesInterval();
	}

	public static void setBlockUpdatesInterval(Entity entity, int interval) {
		entity.getComponent(SLOW_TIME_COMPONENT).setBlockUpdatesInterval(interval);
	}

	public static int aggressorbCount(LivingEntity entity) {
		return entity.getComponent(AGGRESSORB_COMPONENT).orbCount();
	}

	public static int aggressorbIndex(LivingEntity entity, AggressorbEntity orb) {
		return entity.getComponent(AGGRESSORB_COMPONENT).orbIndex(orb);
	}

	public static void addAggressorbToEntity(LivingEntity entity, UUID orbId) {
		entity.getComponent(AGGRESSORB_COMPONENT).addOrbToEntity(orbId);
	}

	public static void removeAggressorbFromEntity(LivingEntity entity, UUID orbId) {
		entity.getComponent(AGGRESSORB_COMPONENT).removeOrbFromEntity(orbId);
	}

	public static UUID getGuardianOrbId(LivingEntity entity) {
		return entity.getComponent(GUARDIAN_ORB_COMPONENT).getOrbId();
	}

	public static void setGuardianOrbManaLock(LivingEntity entity, UUID orbId, int strength) {
		entity.getComponent(GUARDIAN_ORB_COMPONENT).setManaLock(orbId, strength);
	}

	public static void setPortalCoolDown(Entity entity, int cooldownTicks) {
		PORTAL_COOL_DOWN_COMPONENT.maybeGet(entity).ifPresent(component -> component.setCoolDown(cooldownTicks));
	}

	public static boolean hasPortalCoolDown(Entity entity) {
		return PORTAL_COOL_DOWN_COMPONENT.maybeGet(entity).map(PortalCoolDownComponent::hasCoolDown).orElse(false);
	}

	public static void setCounterProperties(LivingEntity entity, @Nullable LivingEntity caster, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> groups, int groupIndex, Color color, double potency, long worldTime) {
		entity.getComponent(COUNTER_COMPONENT).setProperties(caster, stack, effects, groups, groupIndex, color, potency, worldTime);
	}

	public static void removeCounter(LivingEntity entity) {
		entity.getComponent(COUNTER_COMPONENT).removeCounter();
	}

	public static void castCounter(LivingEntity entity, LivingEntity attackingEntity) {
		entity.getComponent(COUNTER_COMPONENT).castCounter(attackingEntity);
	}

	public static boolean isCounterActive(LivingEntity entity) {
		return entity.getComponent(COUNTER_COMPONENT).hasCounterActive(entity.level());
	}

	public static Color getCounterColor(LivingEntity entity) {
		return entity.getComponent(COUNTER_COMPONENT).getColor();
	}

	public static long getCounterEnd(LivingEntity entity) {
		return entity.getComponent(COUNTER_COMPONENT).getEndTime();
	}

	@Override
	public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {
		registry.registerScoreboardComponent(POCKET_DIMENSION_COMPONENT, PocketDimensionComponent::new);
	}
}
