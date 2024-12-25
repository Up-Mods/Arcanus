package dev.cammiescorner.arcanuscontinuum.common.entities.living;

import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FleeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowOwner;
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

@SuppressWarnings("ALL")
public class
OpossumEntity extends TamableAnimal implements SmartBrainOwner<OpossumEntity> {
	public OpossumEntity(EntityType<? extends TamableAnimal> entityType, Level world) {
		super(entityType, world);
		Arrays.fill(armorDropChances, 1F);
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return TamableAnimal.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10)
				.add(Attributes.ATTACK_DAMAGE, 0)
				.add(Attributes.MOVEMENT_SPEED, 0.28);
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
	public OpossumEntity getBreedOffspring(ServerLevel world, AgeableMob entity) {
		OpossumEntity opossumEntity = ArcanusEntities.OPOSSUM.get().create(world);
		UUID uUID = getOwnerUUID();

		if(uUID != null && opossumEntity != null) {
			opossumEntity.setOwnerUUID(uUID);
			opossumEntity.setTame(true);
		}

		return opossumEntity;
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		tickBrain(this);
	}

	@Override
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	public List<ExtendedSensor<OpossumEntity>> getSensors() {
		return ObjectArrayList.of(
				new NearbyLivingEntitySensor<>(),
				new HurtBySensor<>()
		);
	}

	@Override
	public BrainActivityGroup<OpossumEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				new FloatToSurfaceOfFluid<>(),
				new FleeTarget<>().speedModifier(1.5F).stopIf(entity -> ((OpossumEntity) entity).isOrderedToSit()),
				new LookAtTarget<>(),
				new FollowOwner<>().stopIf(entity -> entity.isOrderedToSit()),
				new MoveToWalkTarget<>().stopIf(entity -> ((OpossumEntity) entity).isOrderedToSit())
		);
	}

	@Override
	public BrainActivityGroup<OpossumEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
				new FirstApplicableBehaviour<OpossumEntity>(
						new SetRetaliateTarget<>(),
						new SetPlayerLookTarget<>(),
						new SetRandomLookTarget<>()
				),
				new OneRandomBehaviour<>(
						new SetRandomWalkTarget<>().stopIf(entity -> ((OpossumEntity) entity).isOrderedToSit()),
						new Idle<>().runFor(entity -> entity.getRandom().nextInt(30) + 30)
				)
		);
	}

	@Override
	public BrainActivityGroup<OpossumEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				// TODO Fix later
//				new ForgetAttackTargetTask().m_nwhekhlv(target -> !target.isAlive() || target.squaredDistanceTo(this) > (32 * 32) || target instanceof PlayerEntity player && player.isCreative())
		);
	}
}
