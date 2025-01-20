package dev.cammiescorner.arcanuscontinuum.common.entities.living;

import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusItemTags;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusItems;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusTradeOffers;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Wizard extends AbstractVillager implements NeutralMob {
	private static final EntityDataAccessor<Integer> ROBE_COLOR = SynchedEntityData.defineId(Wizard.class, EntityDataSerializers.INT);

	public Wizard(EntityType<? extends AbstractVillager> entityType, Level world) {
		super(entityType, world);
		Arrays.fill(armorDropChances, 0.1f);
		Arrays.fill(handDropChances, 0.05f);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(2, new TradeWithPlayerGoal(this));
		goalSelector.addGoal(2, new LookAtTradingPlayerGoal(this));
		goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Zombie.class, 8f, 1, 2));
		goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Evoker.class, 12f, 1, 2));
		goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Vindicator.class, 8f, 1, 2));
		goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Vex.class, 8f, 1, 2));
		goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Pillager.class, 15f, 1, 2));
		goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Illusioner.class, 12f, 1, 2));
		goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Zoglin.class, 10f, 1, 2));
		goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 1));
		goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(5, new InteractGoal(this, Player.class, 3f, 1f));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 8f));
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityNbt) {
		populateDefaultEquipmentSlots(world.getRandom(), difficulty);
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityNbt);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		var robeColor = newRandomRobeColor(random);
		setRobeColor(robeColor);
		setItemSlot(EquipmentSlot.HEAD, ArcanusHelper.applyColorToItem(new ItemStack(ArcanusItems.WIZARD_HAT.get()), robeColor));
		setItemSlot(EquipmentSlot.CHEST, ArcanusHelper.applyColorToItem(new ItemStack(ArcanusItems.WIZARD_ROBES.get()), robeColor));
		setItemSlot(EquipmentSlot.LEGS, ArcanusHelper.applyColorToItem(new ItemStack(ArcanusItems.WIZARD_PANTS.get()), robeColor));
		setItemSlot(EquipmentSlot.FEET, ArcanusHelper.applyColorToItem(new ItemStack(ArcanusItems.WIZARD_BOOTS.get()), robeColor));
		setItemSlot(EquipmentSlot.MAINHAND, getRandomStaff(random));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ROBE_COLOR, 0xFFFFFF);
	}

	@Override
	public void notifyTrade(MerchantOffer offer) {
		ambientSoundTime = -getAmbientSoundInterval();
		rewardTradeXp(offer);

		if(getTradingPlayer() instanceof ServerPlayer player)
			CriteriaTriggers.TRADE.trigger(player, this, offer.getResult());
	}

	@Override
	protected void rewardTradeXp(MerchantOffer offer) {
		if(offer.shouldRewardExp())
			level().addFreshEntity(new ExperienceOrb(level(), getX(), getY() + 0.5, getZ(), 3 + random.nextInt(4)));
	}

	@Override
	protected void updateTrades() {
		VillagerTrades.ItemListing[] factories = ArcanusTradeOffers.WIZARD_TRADES.get(1);
		VillagerTrades.ItemListing[] factories1 = ArcanusTradeOffers.WIZARD_TRADES.get(2);

		if(factories != null && factories1 != null) {
			MerchantOffers tradeOfferList = getOffers();
			addOffersFromItemListings(tradeOfferList, factories, 6);

			int i = random.nextInt(factories1.length);
			VillagerTrades.ItemListing factory = factories1[i];
			MerchantOffer tradeOffer = factory.getOffer(this, random);

			if(tradeOffer != null)
				tradeOfferList.add(tradeOffer);
		}
	}

	@Override
	public boolean showProgressBar() {
		return false;
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return TamableAnimal.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.ATTACK_DAMAGE, 4).add(Attributes.MOVEMENT_SPEED, 0.15);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if(!level().isClientSide()) {
			if(ArcanusComponents.getWizardLevel(player) > 0 || player.getItemBySlot(EquipmentSlot.HEAD).is(ArcanusItemTags.WIZARD_ARMOR) || player.getItemBySlot(EquipmentSlot.CHEST).is(ArcanusItemTags.WIZARD_ARMOR) || player.getItemBySlot(EquipmentSlot.LEGS).is(ArcanusItemTags.WIZARD_ARMOR) || player.getItemBySlot(EquipmentSlot.FEET).is(ArcanusItemTags.WIZARD_ARMOR)) {
				if(!getOffers().isEmpty()) {
					setTradingPlayer(player);
					openTradingScreen(player, getDisplayName(), 1);
				}
			}
			else {
				player.displayClientMessage(Component.translatable("text.arcanuscontinuum.wizard_dialogue.no_wizard_armor").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC).withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("tooltip.arcanuscontinuum.wizard_dialogue.no_wizard_armor").withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC)))), false);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);

		if(nbt.contains("RobeColor", Tag.TAG_ANY_NUMERIC)) {
			entityData.set(ROBE_COLOR, nbt.getInt("RobeColor"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putInt("RobesColor", getRobeColor());
	}

	@Override
	public boolean requiresCustomPersistence() {
		return true;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		return null;
	}

	@Override
	public boolean canBeLeashed(Player player) {
		return false;
	}

	private ItemStack getRandomStaff(RandomSource random) {
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
		entityData.set(ROBE_COLOR, color);
	}

	public int getRobeColor() {
		return entityData.get(ROBE_COLOR);
	}

	private int newRandomRobeColor(RandomSource random) {
		// Rare Colors
		if(random.nextDouble() <= 0.1) {
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
	public int getRemainingPersistentAngerTime() {
		return 0;
	}

	@Override
	public void setRemainingPersistentAngerTime(int ticks) {

	}

	@Nullable
	@Override
	public UUID getPersistentAngerTarget() {
		return null;
	}

	@Override
	public void setPersistentAngerTarget(@Nullable UUID uuid) {

	}

	@Override
	public void startPersistentAngerTimer() {

	}
}
