package dev.cammiescorner.arcanuscontinuum.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.api.spells.Pattern;
import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Targetable {
	@Unique private static final UUID uUID = UUID.fromString("e348efa3-7987-4912-b82a-03c5c75eccb1");
	@Unique private final LivingEntity self = (LivingEntity) (Entity) this;
	@Unique private Vec3d prevVelocity;

	@Shadow protected boolean jumping;

	@Shadow public abstract @Nullable EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);
	@Shadow public abstract ItemStack getMainHandStack();
	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);
	@Shadow public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);
	@Shadow public abstract boolean removeStatusEffect(StatusEffect type);
	@Shadow public abstract boolean blockedByShield(DamageSource source);
	@Shadow public abstract boolean clearStatusEffects();
	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);
	@Shadow public abstract float getMovementSpeed();

	@Shadow public abstract boolean teleport(double x, double y, double z, boolean particleEffects);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		if(amount > 0 && !blockedByShield(source)) {
			if(ArcanusComponents.isCounterActive(self) && source.getSource() instanceof LivingEntity attacker)
				ArcanusComponents.castCounter(self, attacker);

			if(hasStatusEffect(ArcanusStatusEffects.MANA_WINGS.get()) && ArcanusConfig.MovementEffects.ManaWingsEffectProperties.removedUponTakingDamage)
				removeStatusEffect(ArcanusStatusEffects.MANA_WINGS.get());
			if(hasStatusEffect(ArcanusStatusEffects.FLOAT.get()) && ArcanusConfig.MovementEffects.FloatEffectProperties.removedUponTakingDamage)
				removeStatusEffect(ArcanusStatusEffects.FLOAT.get());

			if(hasStatusEffect(ArcanusStatusEffects.STOCKPILE.get()) && amount >= ArcanusConfig.AttackEffects.StockpileEffectProperties.damageNeededToIncrease) {
				StatusEffectInstance stockpile = getStatusEffect(ArcanusStatusEffects.STOCKPILE.get());

				if(stockpile.getAmplifier() < 9) {
					clearStatusEffects();

					addStatusEffect(new StatusEffectInstance(stockpile.getEffectType(), stockpile.getDuration(), stockpile.getAmplifier() + MathHelper.floor(Math.round(amount) / 10f)));
				}
			}

			if(hasStatusEffect(ArcanusStatusEffects.DANGER_SENSE.get()) && (source.isTypeIn(DamageTypeTags.IS_PROJECTILE) || source.isTypeIn(DamageTypeTags.IS_EXPLOSION))) {
				StatusEffectInstance dangerSense = getStatusEffect(ArcanusStatusEffects.DANGER_SENSE.get());

				if(random.nextFloat() < ArcanusConfig.SupportEffects.DangerSenseEffectProperties.baseChanceToActivate * (dangerSense.getAmplifier() + 1)) {
					if(getWorld() instanceof ServerWorld world) {
						double d = getX();
						double e = getY();
						double f = getZ();

						for(int i = 0; i < 16; ++i) {
							double g = getX() + (random.nextDouble() - 0.5) * 16.0;
							double h = MathHelper.clamp(
									getY() + random.nextInt(16) - 8,
									world.getBottomY(),
									world.getBottomY() + world.getLogicalHeight() - 1
							);
							double j = getZ() + (random.nextDouble() - 0.5) * 16.0;

							if(hasVehicle())
								stopRiding();

							Vec3d vec3d = getPos();

							if(teleport(g, h, j, true)) {
								world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Context.create(self));
								SoundEvent soundEvent = self instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
								world.playSound(null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1f, 1f);
								playSound(soundEvent, 1f, 1f);
								break;
							}
						}
					}

					removeStatusEffect(ArcanusStatusEffects.DANGER_SENSE.get());
					info.setReturnValue(false);
				}
			}
		}
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float arcanuscontinuum$modifyDamage(float amount, DamageSource source) {
		EntityAttributeInstance attributeInstance = getAttributeInstance(ArcanusEntityAttributes.MAGIC_RESISTANCE.get());

		if(attributeInstance != null && source.isTypeIn(DamageTypeTags.WITCH_RESISTANT_TO))
			amount /= Math.max((float) attributeInstance.getValue(), 0.000001F);
		if(hasStatusEffect(ArcanusStatusEffects.FORTIFY.get()))
			amount /= 1 + (getStatusEffect(ArcanusStatusEffects.FORTIFY.get()).getAmplifier() + 1) * 0.25F;
		if(hasStatusEffect(ArcanusStatusEffects.VULNERABILITY.get()))
			amount *= 1 + 0.8F * ((getStatusEffect(ArcanusStatusEffects.VULNERABILITY.get()).getAmplifier() + 1) / 10F);
		if(source.getAttacker() instanceof LivingEntity attacker && attacker.hasStatusEffect(ArcanusStatusEffects.STOCKPILE.get())) {
			amount *= attacker.getStatusEffect(ArcanusStatusEffects.STOCKPILE.get()).getAmplifier() + 1;
			attacker.removeStatusEffect(ArcanusStatusEffects.STOCKPILE.get());
		}

		return amount;
	}

	@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/particle/BlockStateParticleEffect;<init>(Lnet/minecraft/particle/ParticleType;Lnet/minecraft/block/BlockState;)V"))
	private BlockState arcanuscontinuum$bouncy(BlockState value) {
		if(hasStatusEffect(ArcanusStatusEffects.BOUNCY.get()))
			return Blocks.SLIME_BLOCK.getDefaultState();
		if(hasStatusEffect(ArcanusStatusEffects.FLOAT.get()))
			fallDistance = 0;

		return value;
	}

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$negateFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
		if(prevVelocity != null && !getDamageSources().create(DamageTypes.STALAGMITE).equals(damageSource) && fallDistance > getSafeFallDistance() && hasStatusEffect(ArcanusStatusEffects.BOUNCY.get())) {
			if(!getWorld().isClient) {
				getWorld().playSoundFromEntity(null, this, SoundEvents.BLOCK_SLIME_BLOCK_FALL, getSoundCategory(), 1, 1);

				if(!bypassesLandingEffects()) {
					setVelocity(getVelocity().getX(), -prevVelocity.getY() * 0.99, getVelocity().getZ());
					velocityModified = true;
				}
			}

			info.setReturnValue(false);
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void arcanuscontinuum$tick(CallbackInfo info) {
		if (!getWorld().isClient() && ArcanusComponents.PATTERN_COMPONENT.isProvidedBy(this) && ArcanusComponents.CASTING_COMPONENT.isProvidedBy(this)) {
			prevVelocity = getVelocity();

			EntityAttributeInstance speedAttr = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
			List<Pattern> pattern = ArcanusComponents.getPattern((LivingEntity) (Object) this);
			ItemStack stack = getMainHandStack();

			if (speedAttr != null) {
				if (stack.getItem() instanceof StaffItem && ArcanusComponents.isCasting((LivingEntity) (Object) this) && pattern.size() == 3) {
					int index = Arcanus.getSpellIndex(pattern);
					NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
					NbtList list = tag.getList("Spells", NbtElement.COMPOUND_TYPE);

					if (!list.isEmpty() && index < list.size()) {
						Spell spell = Spell.fromNbt(list.getCompound(index));
						EntityAttributeModifier speedMod = new EntityAttributeModifier(uUID, "Spell Speed Modifier", spell.getWeight().getSlowdown(), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

						if (!speedAttr.hasModifier(speedMod))
							speedAttr.addTemporaryModifier(speedMod);
					}
				} else if (speedAttr.getModifier(uUID) != null)
					speedAttr.removeModifier(uUID);
			}
		}
	}

	@ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
	private static DefaultAttributeContainer.Builder arcanuscontinuum$createPlayerAttributes(DefaultAttributeContainer.Builder builder) {
		ArcanusEntityAttributes.registerAll();

		return builder
			.add(ArcanusEntityAttributes.MAX_MANA.get())
			.add(ArcanusEntityAttributes.MANA_REGEN.get())
			.add(ArcanusEntityAttributes.BURNOUT_REGEN.get())
			.add(ArcanusEntityAttributes.MANA_LOCK.get())
			.add(ArcanusEntityAttributes.SPELL_POTENCY.get())
			.add(ArcanusEntityAttributes.MAGIC_RESISTANCE.get())
			.add(ArcanusEntityAttributes.MANA_COST.get())
			.add(ArcanusEntityAttributes.SPELL_COOL_DOWN.get());
	}

	@WrapOperation(method = "handleFrictionAndCalculateMovement", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;",
		ordinal = 1
	))
	private Vec3d arcanuscontinuum$floatAround(LivingEntity livingEntity, Operation<Vec3d> original, Vec3d movementInput, float slipperiness) {
		// FIXME smooth out vertical movement, currently a bit jolting
		if(hasStatusEffect(ArcanusStatusEffects.FLOAT.get())) {
			return getVelocity().add(0, jumping ? getMovementSpeed() : isSneaking() ? -getMovementSpeed() : 0, 0);
		}

		return original.call(livingEntity);
	}

	@Inject(method = "onStatusEffectRemoved", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$cantRemoveCurse(StatusEffectInstance effect, CallbackInfo info) {
		if(effect.getEffectType() == ArcanusStatusEffects.COPPER_CURSE.get())
			info.cancel();
	}

	@ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
	public Vec3d arcanuscontinuum$invertInput(Vec3d movementInput) {
		if(!(self instanceof PlayerEntity) && hasStatusEffect(ArcanusStatusEffects.DISCOMBOBULATE.get())) {
			movementInput = movementInput.multiply(-1, 1, -1);
		}

		return movementInput;
	}
}
