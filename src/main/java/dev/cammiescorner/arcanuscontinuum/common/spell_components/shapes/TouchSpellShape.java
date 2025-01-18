package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class TouchSpellShape extends SpellShape {
	public TouchSpellShape() {
		super(
			ArcanusConfig.SpellShapes.TouchShapeProperties.enabled,
			ArcanusConfig.SpellShapes.TouchShapeProperties.weight,
			ArcanusConfig.SpellShapes.TouchShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.TouchShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.TouchShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.TouchShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.TouchShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		potency += getPotencyModifier();
		double range = caster != null ? ReachEntityAttributes.getAttackRange(caster, caster instanceof Player player && player.isCreative() ? 5 : 4.5) : 4.5;
		Entity sourceEntity = castSource != null ? castSource : caster;
		HitResult target = ArcanusHelper.raycast(sourceEntity, range, true, true);

		if(target.getType() != HitResult.Type.MISS)
			for(SpellEffect effect : new HashSet<>(effects))
				effect.effect(caster, sourceEntity, world, target, effects, stack, potency);

		Entity targetEntity = target.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) target).getEntity() : castSource;
		castNext(caster, target.getLocation(), targetEntity, world, stack, spellGroups, groupIndex, potency);
	}
}
