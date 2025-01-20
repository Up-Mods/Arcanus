package dev.cammiescorner.arcanuscontinuum.common.entities.living;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;

public class Opossum extends TamableAnimal {
	public Opossum(EntityType<? extends TamableAnimal> entityType, Level world) {
		super(entityType, world);
		Arrays.fill(armorDropChances, 1f);
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return TamableAnimal.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.ATTACK_DAMAGE, 0).add(Attributes.MOVEMENT_SPEED, 0.28);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
		goalSelector.addGoal(3, new FollowOwnerGoal(this, 1, 2, 4, false));
		goalSelector.addGoal(4, new TemptGoal(this, 1, Ingredient.of(Items.CARROT), false));
		goalSelector.addGoal(4, new BreedGoal(this, 1));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8f));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack handStack = player.getItemInHand(hand);
		ItemStack stack = handStack.copy();
		ItemStack hatStack = getItemBySlot(EquipmentSlot.HEAD).copy();

		if(isTame()) {
			if(handStack.is(ArcanusItems.WIZARD_HAT.get())) {
				setItemSlot(EquipmentSlot.HEAD, stack);

				if(!player.isCreative())
					handStack.shrink(1);
				if(!getItemBySlot(EquipmentSlot.HEAD).isEmpty() && !player.isCreative())
					player.setItemInHand(hand, hatStack);

				return InteractionResult.SUCCESS;
			}

			if(isFood(handStack) && getHealth() < getMaxHealth()) {
				heal(4);

				if(!player.isCreative())
					handStack.shrink(1);

				return InteractionResult.SUCCESS;
			}

			if(handStack.isEmpty() && player.isShiftKeyDown() && !getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
				player.setItemInHand(hand, hatStack);
				setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
				return InteractionResult.SUCCESS;
			}

			if(getOwner() != null && player.getId() == getOwner().getId()) {
				setOrderedToSit(!isOrderedToSit());
				return InteractionResult.SUCCESS;
			}
		}
		else if(handStack.is(Items.CARROT)) {
			if(!level().isClientSide) {
				if(!player.isCreative())
					handStack.shrink(1);

				if(random.nextInt(3) == 0) {
					tame(player);
					navigation.stop();
					level().broadcastEntityEvent(this, (byte) 7);
				}
				else {
					level().broadcastEntityEvent(this, (byte) 6);
				}

				return InteractionResult.SUCCESS;
			}
		}

		return super.mobInteract(player, hand);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(Items.CARROT);
	}

	@Nullable
	@Override
	public Opossum getBreedOffspring(ServerLevel world, AgeableMob entity) {
		Opossum opossum = ArcanusEntities.OPOSSUM.get().create(world);
		UUID uUID = getOwnerUUID();

		if(uUID != null && opossum != null) {
			opossum.setOwnerUUID(uUID);
			opossum.setTame(true);
		}

		return opossum;
	}
}
