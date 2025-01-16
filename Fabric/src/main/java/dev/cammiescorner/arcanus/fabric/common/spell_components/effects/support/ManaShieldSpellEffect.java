package dev.cammiescorner.arcanus.fabric.common.spell_components.effects.support;

import dev.cammiescorner.arcanus.ArcanusConfig;
import dev.cammiescorner.arcanus.api.spells.SpellEffect;
import dev.cammiescorner.arcanus.api.spells.SpellType;
import dev.cammiescorner.arcanus.api.spells.Weight;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.ManaShieldEntity;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanus.fabric.common.util.ArcanusHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaShieldSpellEffect extends SpellEffect {
	public ManaShieldSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(target.getType() != HitResult.Type.MISS && caster != null) {
			List<? extends ManaShieldEntity> list = ((ServerLevel) level).getEntities(EntityTypeTest.forClass(ManaShieldEntity.class), entity -> entity.getOwnerId().equals(caster.getUUID()));

			for(int i = 0; i < list.size() - 10; i++)
				list.get(i).kill();

			ManaShieldEntity manaShield = ArcanusEntities.MANA_SHIELD.get().create(level);

			if(manaShield != null) {
				manaShield.setProperties(caster.getUUID(), target.getLocation().add(0.0D, -0.7D, 0.0D), (int) ((ArcanusConfig.SupportEffects.ManaShieldEffectProperties.baseLifeSpan + ArcanusConfig.SupportEffects.ManaShieldEffectProperties.lifeSpanModifier * (effects.stream().filter(ArcanusSpellComponents.MANA_SHIELD::is).count() - 1)) * potency));
				ArcanusHelper.copyMagicColor(manaShield, caster);
				level.addFreshEntity(manaShield);
			}
		}
	}

	@Override
	public boolean shouldTriggerOnceOnExplosion() {
		return true;
	}
}