package dev.cammiescorner.arcanuscontinuum.mixin.client;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.Pattern;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.client.utils.ClientUtils;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.AggressorbEntity;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.packets.c2s.CastSpellPacket;
import dev.cammiescorner.arcanuscontinuum.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.arcanuscontinuum.common.packets.c2s.ShootOrbsPacket;
import dev.cammiescorner.arcanuscontinuum.common.packets.c2s.SyncPatternPacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin implements ClientUtils {
	@Unique
	private boolean isCasting = false;
	@Unique
	private int timer = 0;
	@Unique
	private int mouseDownTimer = 0;
	@Unique
	private KeyMapping lastMouseDown = null;
	@Unique
	private final List<Pattern> pattern = new ArrayList<>(3);

	@Shadow
	@Nullable
	public LocalPlayer player;
	@Shadow
	@Final
	public Options options;
	@Shadow
	@Nullable
	public ClientLevel level;

	@Shadow
	public abstract float getFrameTime();

	@Shadow
	public abstract boolean isLocalServer();

	@Inject(method = "tick", at = @At("HEAD"))
	public void arcanuscontinuum$tick(CallbackInfo info) {
		if(player == null)
			return;

		ItemStack stack = player.getMainHandItem();

		if(timer == 0 || (lastMouseDown != null && !lastMouseDown.isDown()) || ArcanusComponents.getStunTimer(player) > 0) {
			pattern.clear();
			SyncPatternPacket.send(pattern);
			lastMouseDown = null;
			isCasting = false;
			timer = 0;

			if(ArcanusComponents.getStunTimer(player) > 0)
				player.resetAttackStrengthTicker();
		}

		if(stack.getItem() instanceof StaffItem staff && (ArcanusComponents.getMana(player) > 0 || player.isCreative())) {
			if(timer > 0 && pattern.size() >= 3) {
				isCasting = lastMouseDown != null && lastMouseDown.isDown();

				if(isCasting) {
					mouseDownTimer++;

					if(player.getCooldowns().getCooldownPercent(staff, getFrameTime()) == 0) {
						int index = Arcanus.getSpellIndex(pattern);
						CastSpellPacket.send(index);
						timer = 20;
					}
				}
				else {
					timer = 0;
					mouseDownTimer = 0;
				}
			}
		}
		else {
			timer = 0;
		}

		if(isCasting() && !ArcanusComponents.isCasting(player) && mouseDownTimer > 5)
			SetCastingPacket.send(true);
		if((!isCasting() || ArcanusComponents.getMana(player) <= 0) && ArcanusComponents.isCasting(player))
			SetCastingPacket.send(false);

		if(timer > 0 && player.getAttackStrengthScale(getFrameTime()) == 1F && player.getCooldowns().getCooldownPercent(stack.getItem(), getFrameTime()) == 0)
			timer--;
	}

	@Inject(method = "handleKeybinds", at = @At("HEAD"), cancellable = true)
	private void arcanuscontinuum$handleInputEvents(CallbackInfo info) {
		if(ArcanusComponents.getStunTimer(player) > 0)
			info.cancel();
	}

	@Inject(method = "handleKeybinds", at = @At(value = "INVOKE",
												target = "Lnet/minecraft/client/Minecraft;continueAttack(Z)V"
	), cancellable = true)
	public void arcanuscontinuum$onBlockBreak(CallbackInfo info) {
		if(isCasting)
			info.cancel();
	}

	@Inject(method = "handleKeybinds", at = @At(value = "INVOKE",
												target = "Lnet/minecraft/client/Minecraft;startAttack()Z",
												ordinal = 0
	), cancellable = true)
	public void arcanuscontinuum$onLeftClick(CallbackInfo info) {
		if(isCasting)
			info.cancel();

		if(player != null && !player.isSpectator() && level != null) {
			if(player.getMainHandItem().getItem() instanceof StaffItem staff) {
				if(player.getAttackStrengthScale(getFrameTime()) >= (((isLocalServer() ? ArcanusConfig.castingSpeedHasCoolDown : ArcanusClient.castingSpeedHasCoolDown) || ArcanusComponents.getBurnout(player) > 0) ? 1 : 0.15F) && player.getCooldowns().getCooldownPercent(staff, getFrameTime()) == 0 && ArcanusComponents.getMana(player) > 0 && !isCasting) {
					timer = 20;
					pattern.add(Pattern.LEFT);
					SyncPatternPacket.send(pattern);
					player.swing(InteractionHand.MAIN_HAND);
					player.resetAttackStrengthTicker();
					player.level().playSeededSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1F, 1.3F, 1L);

					if(pattern.size() >= 3)
						lastMouseDown = options.keyAttack;
				}

				info.cancel();
			}
			else {
				List<UUID> orbIds = player.getComponent(ArcanusComponents.AGGRESSORB_COMPONENT).getOrbs();

				if(!orbIds.isEmpty()) {
					ShootOrbsPacket.send(orbIds, player.getUUID());
					shootOrbs(orbIds);
				}
			}
		}
	}

	@Inject(method = "handleKeybinds", at = @At(value = "INVOKE",
												target = "Lnet/minecraft/client/Minecraft;startUseItem()V",
												ordinal = 0
	), cancellable = true)
	public void arcanuscontinuum$onRightClick(CallbackInfo info) {
		if(isCasting)
			info.cancel();

		if(player != null && !player.isSpectator() && level != null && player.getMainHandItem().getItem() instanceof StaffItem staff) {
			if(player.getAttackStrengthScale(getFrameTime()) >= (((isLocalServer() ? ArcanusConfig.castingSpeedHasCoolDown : ArcanusClient.castingSpeedHasCoolDown) || ArcanusComponents.getBurnout(player) > 0) ? 1 : 0.15F) && player.getCooldowns().getCooldownPercent(staff, getFrameTime()) == 0 && ArcanusComponents.getMana(player) > 0 && !isCasting) {
				timer = 20;
				pattern.add(Pattern.RIGHT);
				SyncPatternPacket.send(pattern);
				player.swing(InteractionHand.MAIN_HAND);
				player.resetAttackStrengthTicker();
				player.level().playSeededSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1F, 1.1F, 1L);

				if(pattern.size() >= 3)
					lastMouseDown = options.keyUse;
			}

			info.cancel();
		}
	}

	@Inject(method = "handleKeybinds", at = @At(value = "INVOKE",
												target = "Lnet/minecraft/client/Minecraft;startUseItem()V",
												ordinal = 1
	), cancellable = true)
	public void arcanuscontinuum$onRightClickBlock(CallbackInfo info) {
		if(isCasting || (player != null && !player.isSpectator() && player.getMainHandItem().getItem() instanceof StaffItem))
			info.cancel();
	}

	@Override
	public boolean isCasting() {
		return isCasting && timer > 0;
	}

	@Unique
	private void shootOrbs(List<UUID> orbIds) {
		for(Entity entity : level.entitiesForRendering()) {
			if(entity instanceof AggressorbEntity orb && orbIds.get(0).equals(entity.getUUID()) && orb.isBoundToTarget()) {
				orb.setBoundToTarget(false);
				orb.setPos(orb.getTarget().getEyePosition());
				orb.shootFromRotation(orb.getTarget(), orb.getTarget().getXRot(), orb.getTarget().getYRot(), 0F, ArcanusConfig.SpellShapes.AggressorbShapeProperties.projectileSpeed, 1F);

				break;
			}
		}
	}
}
