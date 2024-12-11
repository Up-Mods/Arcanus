package dev.cammiescorner.arcanuscontinuum.common.entities.living;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusItems;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusTags;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusTradeOffers;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRetaliateTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WizardEntity extends MerchantEntity implements SmartBrainOwner<WizardEntity>, Angerable {
	private static final TrackedData<Integer> ROBE_COLOR = DataTracker.registerData(WizardEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public WizardEntity(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
		Arrays.fill(armorDropChances, 0.1F);
		Arrays.fill(handDropChances, 0.05F);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
		initEquipment(world.getRandom(), difficulty);
		return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	protected void initEquipment(RandomGenerator random, LocalDifficulty difficulty) {
		var robeColor = newRandomRobeColor(random);
		setRobeColor(robeColor);
		equipStack(EquipmentSlot.HEAD, ArcanusHelper.applyColorToItem(new ItemStack(ArcanusItems.WIZARD_HAT.get()), robeColor));
		equipStack(EquipmentSlot.CHEST, ArcanusHelper.applyColorToItem(new ItemStack(ArcanusItems.WIZARD_ROBES.get()), robeColor));
		equipStack(EquipmentSlot.LEGS, ArcanusHelper.applyColorToItem(new ItemStack(ArcanusItems.WIZARD_PANTS.get()), robeColor));
		equipStack(EquipmentSlot.FEET, ArcanusHelper.applyColorToItem(new ItemStack(ArcanusItems.WIZARD_BOOTS.get()), robeColor));
		equipStack(EquipmentSlot.MAINHAND, getRandomStaff(random));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(ROBE_COLOR, 0xFFFFFF);
	}

	@Override
	public void trade(TradeOffer offer) {
		ambientSoundChance = -getMinAmbientSoundDelay();
		afterUsing(offer);

		if (getCurrentCustomer() instanceof ServerPlayerEntity player)
			Criteria.VILLAGER_TRADE.trigger(player, this, offer.getSellItem());
	}

	@Override
	protected void afterUsing(TradeOffer offer) {
		if (offer.shouldRewardPlayerExperience())
			getWorld().spawnEntity(new ExperienceOrbEntity(getWorld(), getX(), getY() + 0.5, getZ(), 3 + random.nextInt(4)));
	}

	@Override
	protected void fillRecipes() {
		TradeOffers.Factory[] factories = ArcanusTradeOffers.WIZARD_TRADES.get(1);
		TradeOffers.Factory[] factories1 = ArcanusTradeOffers.WIZARD_TRADES.get(2);

		if (factories != null && factories1 != null) {
			TradeOfferList tradeOfferList = getOffers();
			fillRecipesFromPool(tradeOfferList, factories, 6);

			int i = random.nextInt(factories1.length);
			TradeOffers.Factory factory = factories1[i];
			TradeOffer tradeOffer = factory.create(this, random);

			if (tradeOffer != null)
				tradeOfferList.add(tradeOffer);
		}
	}

	@Override
	public boolean isLeveledMerchant() {
		return false;
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return TameableEntity.createAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15);
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!getWorld().isClient()) {
			if (ArcanusComponents.getWizardLevel(player) > 0 || player.getEquippedStack(EquipmentSlot.HEAD).isIn(ArcanusTags.WIZARD_ARMOUR) || player.getEquippedStack(EquipmentSlot.CHEST).isIn(ArcanusTags.WIZARD_ARMOUR) || player.getEquippedStack(EquipmentSlot.LEGS).isIn(ArcanusTags.WIZARD_ARMOUR) || player.getEquippedStack(EquipmentSlot.FEET).isIn(ArcanusTags.WIZARD_ARMOUR)) {
				if (!getOffers().isEmpty()) {
					setCurrentCustomer(player);
					sendOffers(player, getDisplayName(), 1);
				}
			} else {
				player.sendMessage(Arcanus.translate("wizard_dialogue", "no_wizard_armour").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC).styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Arcanus.translate("wizard_dialogue", "put_on_robes").formatted(Formatting.AQUA, Formatting.ITALIC)))), false);
			}
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);

		if (nbt.contains("RobeColor", NbtElement.NUMBER_TYPE)) {
			dataTracker.set(ROBE_COLOR, nbt.getInt("RobeColor"));
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("RobesColor", getRobeColor());
	}

	@Override
	public boolean cannotDespawn() {
		return true;
	}

	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return null;
	}

	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}

	@Override
	protected void mobTick() {
		super.mobTick();
		tickBrain(this);
	}

	@Override
	protected Brain.Profile<?> createBrainProfile() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	public List<ExtendedSensor<WizardEntity>> getSensors() {
		return ObjectArrayList.of(
			new NearbyLivingEntitySensor<>(),
			new HurtBySensor<>()
		);
	}

	@Override
	public BrainActivityGroup<WizardEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
			new FloatToSurfaceOfFluid<>(),
			new LookAtTarget<>(),
			new MoveToWalkTarget<>().stopIf(pathAwareEntity -> getCurrentCustomer() != null)
		);
	}

	@Override
	public BrainActivityGroup<WizardEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
			new FirstApplicableBehaviour<WizardEntity>(
				new SetRetaliateTarget<>(),
				new SetPlayerLookTarget<>(),
				new SetRandomLookTarget<>()
			), new OneRandomBehaviour<>(
				new SetRandomWalkTarget<>().startCondition(pathAwareEntity -> getCurrentCustomer() != null),
				new Idle<>().runFor(entity -> entity.getRandom().nextInt(30) + 30)
			)
		);
	}

	private ItemStack getRandomStaff(RandomGenerator random) {
		// TODO use a tag for this
		List<Item> staves = List.of(
			ArcanusItems.WOODEN_STAFF.get(),
			ArcanusItems.CRYSTAL_STAFF.get(),
			ArcanusItems.DIVINATION_STAFF.get(),
			ArcanusItems.CRESCENT_STAFF.get(),
			ArcanusItems.ANCIENT_STAFF.get()
		);

		return new ItemStack(staves.get(random.nextInt(staves.size())));
	}

	public void setRobeColor(int color) {
		dataTracker.set(ROBE_COLOR, color);
	}

	public int getRobeColor() {
		return dataTracker.get(ROBE_COLOR);
	}

	private int newRandomRobeColor(RandomGenerator random) {
		// Rare Colors
		if (random.nextDouble() <= 0.1) {
			var list = List.of(
				0xff005a, // Folly Red
				0xf2dd50 // Lotus Gold
			);
			return list.get(random.nextInt(list.size()));
		}

		// Normal Colors
		var list = List.of(
			0xffffff,
			0xf9801d,
			0xc74ebd,
			0x3ab3da,
			0xfed83d,
			0x80c71f,
			0xf38baa,
			0x474f52,
			0x9d9d97,
			0x169c9c,
			0x8932b8,
			0x52392a,
			0x3c44aa,
			0x5e7c16,
			0xb02e26,
			0x1d1d21,
			0xfcc973
		);

		return list.get(random.nextInt(list.size()));
	}

	@Override
	public int getAngerTime() {
		return 0;
	}

	@Override
	public void setAngerTime(int ticks) {

	}

	@Nullable
	@Override
	public UUID getAngryAt() {
		return null;
	}

	@Override
	public void setAngryAt(@Nullable UUID uuid) {

	}

	@Override
	public void chooseRandomAngerTime() {

	}
}
