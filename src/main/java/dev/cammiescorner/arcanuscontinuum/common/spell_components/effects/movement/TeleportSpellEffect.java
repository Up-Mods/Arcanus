package dev.cammiescorner.arcanuscontinuum.common.spell_components.effects.movement;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellType;
import dev.cammiescorner.arcanuscontinuum.api.spells.Weight;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeleportSpellEffect extends SpellEffect {
	public TeleportSpellEffect(boolean isEnabled, SpellType type, Weight weight, double manaCost, int coolDown, int minLevel) {
		super(isEnabled, type, weight, manaCost, coolDown, minLevel);
	}

	@Override
	public void effect(@Nullable LivingEntity caster, @Nullable Entity sourceEntity, Level world, HitResult target, List<SpellEffect> effects, ItemStack stack, double potency) {
		if(caster != null && caster.position().distanceTo(target.getLocation()) <= ArcanusConfig.MovementEffects.TeleportEffectProperties.baseTeleportDistance * effects.stream().filter(ArcanusSpellComponents.TELEPORT::is).count() * potency) {
			Vec3 pos = target.getLocation();

			if(target.getType() == HitResult.Type.BLOCK) {
				BlockHitResult blockHit = (BlockHitResult) target;
				pos = pos.add(blockHit.getDirection().getStepX() * 0.5, blockHit.getDirection() == Direction.DOWN ? -2 : 0, blockHit.getDirection().getStepZ() * 0.5);
			}

			world.broadcastEntityEvent(caster, EntityEvent.TELEPORT);

			if(caster.isPassenger())
				caster.dismountTo(pos.x(), pos.y(), pos.z());
			else
				caster.teleportTo(pos.x(), pos.y(), pos.z());

			world.broadcastEntityEvent(caster, EntityEvent.TELEPORT);
		}
	}
}
