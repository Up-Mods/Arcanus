package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.utility;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrowthSpellEffect extends SpellEffect {
	public GrowthSpellEffect() {
		super(
			ArcanusConfig.UtilityEffects.GrowthEffectProperties.enabled,
			SpellType.UTILITY,
			ArcanusConfig.UtilityEffects.GrowthEffectProperties.weight,
			ArcanusConfig.UtilityEffects.GrowthEffectProperties.manaCost,
			ArcanusConfig.UtilityEffects.GrowthEffectProperties.coolDown,
			ArcanusConfig.UtilityEffects.GrowthEffectProperties.minimumLevel
		);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level level, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		int growthCount = (int) (effects.stream().filter(ArcanusSpellComponents.GROWTH::is).count() * potency);

		if(target.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) target;

			if(entityHit.getEntity() instanceof Animal animal && animal.isBaby())
				animal.ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(-animal.getAge()) * growthCount, true);
		}
		else if(target.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHit = (BlockHitResult) target;
			BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());

			for(int i = 0; i < growthCount; i++) {
				BoneMealItem.growCrop(ItemStack.EMPTY, level, pos);
				BoneMealItem.growWaterPlant(ItemStack.EMPTY, level, pos, blockHit.getDirection());
			}
		}
	}
}
