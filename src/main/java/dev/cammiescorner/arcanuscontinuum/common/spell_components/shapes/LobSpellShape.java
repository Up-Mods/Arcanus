package dev.cammiescorner.arcanuscontinuum.common.spell_components.shapes;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellGroup;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.MagicProjectile;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusEntities;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class LobSpellShape extends SpellShape {
	public LobSpellShape() {
		super(
			ArcanusConfig.SpellShapes.LobShapeProperties.enabled,
			ArcanusConfig.SpellShapes.LobShapeProperties.weight,
			ArcanusConfig.SpellShapes.LobShapeProperties.manaCost,
			ArcanusConfig.SpellShapes.LobShapeProperties.manaMultiplier,
			ArcanusConfig.SpellShapes.LobShapeProperties.coolDown,
			ArcanusConfig.SpellShapes.LobShapeProperties.minimumLevel,
			ArcanusConfig.SpellShapes.LobShapeProperties.potencyModifier
		);
	}

	@Override
	public void cast(@Nullable LivingEntity caster, Vec3 castFrom, @Nullable Entity castSource, ServerLevel world, ItemStack stack, List<SpellEffect> effects, List<SpellGroup> spellGroups, int groupIndex, double potency) {
		float projectileSpeed = ArcanusConfig.SpellShapes.ProjectileShapeProperties.projectileSpeed;
		potency += getPotencyModifier();

		if(caster != null) {
			List<? extends MagicProjectile> list = world.getEntities(EntityTypeTest.forClass(MagicProjectile.class), entity -> caster.equals(entity.getOwner()));
			Entity sourceEntity = castSource != null ? castSource : caster;
			HitResult target = ArcanusHelper.raycast(sourceEntity, 4.5, true, true);

			for(int i = 0; i < list.size() - 20; i++)
				list.get(i).kill();

			if(projectileSpeed > 3f && target instanceof EntityHitResult hitResult) {
				for(SpellEffect effect : new HashSet<>(effects))
					effect.effect(caster, sourceEntity, world, target, effects, stack, potency);

				SpellShape.castNext(caster, target.getLocation(), hitResult.getEntity(), world, stack, spellGroups, groupIndex, potency);
				world.playSound(hitResult.getEntity(), hitResult.getEntity().blockPosition(), SoundEvents.ARROW_HIT, SoundSource.NEUTRAL, 1F, 1.2F / (world.random.nextFloat() * 0.2F + 0.9F));
			}
			else {
				MagicProjectile projectile = ArcanusEntities.MAGIC_PROJECTILE.get().create(world);

				if(projectile != null) {
					projectile.setProperties(caster, castSource, this, stack, effects, spellGroups, groupIndex, potency, projectileSpeed, false);
					world.addFreshEntity(projectile);
				}
			}
		}
	}
}
