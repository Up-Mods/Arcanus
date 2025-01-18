package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.SyncScalePacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShrinkSpellEffect extends SpellEffect {
	public ShrinkSpellEffect() {
		super(
			ArcanusConfig.UtilityEffects.ShrinkEffectProperties.enabled,
			SpellType.UTILITY,
			ArcanusConfig.UtilityEffects.ShrinkEffectProperties.weight,
			ArcanusConfig.UtilityEffects.ShrinkEffectProperties.manaCost,
			ArcanusConfig.UtilityEffects.ShrinkEffectProperties.coolDown,
			ArcanusConfig.UtilityEffects.ShrinkEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(ArcanusSpellComponents.SHRINK != null && target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;
			Entity entity = entityHit.getEntity();

			// TODO make shrink be a potion
//			if(entityHit.getEntity() instanceof LivingEntity entity)
//				entity.addEffect(new MobEffectInstance(ArcanusMobEffects.SHRINK.get(), (int) (ArcanusConfig.UtilityEffects.ShrinkEffectProperties.baseEffectDuration * effects.stream().filter(ArcanusSpellComponents.SHRINK::is).count() * potency), 0, false, true, true));

			ArcanusComponents.setScale(entity, this, effects.stream().filter(ArcanusSpellComponents.SHRINK::is).count() * potency);

			if(!entity.level().isClientSide()) {
				var strength = effects.stream().filter(ArcanusSpellComponents.SHRINK::is).count() * potency;

				for(ServerPlayer lookup : PlayerLookup.tracking(entity))
					SyncScalePacket.send(lookup, entity, this, strength);

				if(entity instanceof ServerPlayer serverPlayer)
					SyncScalePacket.send(serverPlayer, entity, this, strength);
			}
		}
	}
}
