package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.ArcanusConfig;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellEffect;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.Set;

public class SizeComponent implements CommonTickingComponent {
	private static final Set<ScaleType> SUPPORTED_SCALE_TYPES = Set.of(ScaleTypes.HEIGHT, ScaleTypes.WIDTH, ScaleTypes.REACH, ScaleTypes.MOTION);

	private final Entity entity;
	private int timer = 0;
	private int scaleTicks = 0;
	private float unmodifiedHeight = 1.0F;
	private float previousScaleMultiplier = 1.0F;
	private float scaleMultiplier = 1.0F;

	public SizeComponent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void tick() {
		if (ArcanusConfig.sizeChangingIsPermanent) {
			return;
		}

		if (entity instanceof PlayerEntity || (entity instanceof TameableEntity tameable && tameable.getOwnerUuid() != null)) {
			if (ScaleTypes.HEIGHT.getScaleData(entity).getBaseValueModifiers().contains(ArcanusScaleModifier.INSTANCE)) {
				if (timer <= 0) {
					float normalHeight = unmodifiedHeight / ScaleTypes.HEIGHT.getScaleData(entity).getScale();

					if (entity.getHeight() > normalHeight || entity.getWorld().isSpaceEmpty(entity, new Box(entity.getBoundingBox().minX, entity.getBoundingBox().minY, entity.getBoundingBox().minZ, entity.getBoundingBox().maxX, entity.getBoundingBox().maxY * normalHeight, entity.getBoundingBox().maxZ))) {
						if (scaleTicks <= 0)
							resetScale();
						else {
							scaleTicks--;
							SUPPORTED_SCALE_TYPES.forEach(scaleType ->
								scaleType.getScaleData(entity).onUpdate());
						}
					}
				} else {
					if (scaleTicks < 10) {
						scaleTicks++;
						SUPPORTED_SCALE_TYPES.forEach(scaleType ->
							scaleType.getScaleData(entity).onUpdate());
					}

					timer--;
				}
			} else {
				scaleTicks = 0;
			}
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		timer = tag.getInt("Timer");
		scaleTicks = tag.getInt("ScaleTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("Timer", timer);
		tag.putInt("ScaleTicks", scaleTicks);
	}

	public void setScale(SpellEffect effect, double strength) {
		previousScaleMultiplier = MathHelper.lerp((float) scaleTicks / 10, 1.0F, scaleMultiplier);
		scaleMultiplier = ArcanusSpellComponents.SHRINK.is(effect) ? ArcanusConfig.UtilityEffects.ShrinkEffectProperties.baseShrinkAmount : ArcanusConfig.UtilityEffects.EnlargeEffectProperties.baseEnlargeAmount;
		SUPPORTED_SCALE_TYPES.forEach(scaleType -> {
			ScaleData data = scaleType.getScaleData(entity);
			if (!data.getBaseValueModifiers().contains(ArcanusScaleModifier.INSTANCE)) {
				data.getBaseValueModifiers().add(ArcanusScaleModifier.INSTANCE);
				data.onUpdate();
			}
		});
		if (Math.abs(previousScaleMultiplier - scaleMultiplier) > 0.001)
			scaleTicks = 0;
		timer = (int) Math.round((ArcanusSpellComponents.SHRINK.is(effect) ? ArcanusConfig.UtilityEffects.ShrinkEffectProperties.baseEffectDuration : ArcanusConfig.UtilityEffects.EnlargeEffectProperties.baseEffectDuration) * strength);
	}

	public void resetScale() {
		SUPPORTED_SCALE_TYPES.forEach(type -> type.getScaleData(entity).getBaseValueModifiers().remove(ArcanusScaleModifier.INSTANCE));
		scaleMultiplier = 1.0F;
		previousScaleMultiplier = 1.0F;
		timer = 0;
	}

	public static class ArcanusScaleModifier extends ScaleModifier {
		public static final ArcanusScaleModifier INSTANCE = new ArcanusScaleModifier();

		protected ArcanusScaleModifier() {
		}

		public float modifyScale(ScaleData data, float modifiedScale, float delta) {
			ScaleType type = data.getScaleType();
			Entity entity = data.getEntity();

			SizeComponent component = entity.getComponent(ArcanusComponents.SIZE);
			if (type == ScaleTypes.HEIGHT)
				component.unmodifiedHeight = modifiedScale;

			if (component.scaleTicks >= 10)
				return modifiedScale * component.scaleMultiplier;

			float progress = (float) component.scaleTicks + delta;
			float range = modifiedScale * component.scaleMultiplier - modifiedScale * component.previousScaleMultiplier;
			float perTick = data.getScaleType().getDefaultEasing().apply(progress / 10);

			return modifiedScale * component.previousScaleMultiplier + (perTick * range);
		}

		public float modifyPrevScale(ScaleData data, float modifiedScale) {
			Entity entity = data.getEntity();
			SizeComponent component = entity.getComponent(ArcanusComponents.SIZE);

			return MathHelper.lerp((float) component.scaleTicks / 10, modifiedScale * component.previousScaleMultiplier, modifiedScale * component.scaleMultiplier);
		}
	}
}
