package dev.cammiescorner.arcanuscontinuum.common.util;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import dev.cammiescorner.arcanuscontinuum.common.components.MagicColorComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.util.supporters.WizardData;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ArcanusHelper {

	public static Color getMagicColor(@Nullable Object provider) {
		if(provider == null) {
			return Arcanus.DEFAULT_MAGIC_COLOUR;
		}

		var component = ArcanusComponents.MAGIC_COLOR.getNullable(provider);
		if (component != null) {
			return component.getColor();
		}

		// if Entity
		if (provider instanceof TraceableEntity ownable) {
			var owner = ownable.getOwner();
			if (owner != null) {
				return getMagicColor(owner);
			}
		}

		return Arcanus.DEFAULT_MAGIC_COLOUR;
	}

	public static Color getMagicColor(@Nullable UUID playerId) {
		if (playerId == null || Util.NIL_UUID.equals(playerId)) {
			return Arcanus.DEFAULT_MAGIC_COLOUR;
		}

		return WizardData.getOrEmpty(playerId).magicColor();
	}

	public static HitResult raycast(Entity origin, double maxDistance, boolean includeEntities, boolean includeFluids) {
		Vec3 startPos = origin.getEyePosition(1F);
		Vec3 rotation = origin.getViewVector(1F);
		Vec3 endPos = startPos.add(rotation.scale(maxDistance));
		HitResult hitResult = origin.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, includeFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, origin));

		endPos = hitResult.getLocation();
		maxDistance *= maxDistance;
		HitResult entityHitResult = ProjectileUtil.getEntityHitResult(origin, startPos, endPos, origin.getBoundingBox().expandTowards(rotation.scale(maxDistance)).inflate(1.0D, 1D, 1D), entity -> !entity.isSpectator() && entity instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted(), maxDistance);

		if (includeEntities && entityHitResult != null)
			hitResult = entityHitResult;

		return hitResult;
	}

	public static ItemStack applyColorToItem(ItemStack stack, int color) {
		stack.getOrCreateTagElement(ItemStack.TAG_DISPLAY).putInt(ItemStack.TAG_COLOR, color);
		return stack;
	}

	/**
	 * sets the magic color if the entity can store it
	 *
	 * @param sourceId the UUID of the source player to get the magic color from
	 */
	public static void setMagicColorSource(Object obj, UUID sourceId) {
		ArcanusComponents.MAGIC_COLOR.maybeGet(obj).ifPresent(component -> component.setSourceId(sourceId));
	}

	/**
	 * sets the magic color if the entity can store it
	 *
	 * @param from the entity to take the color from.
	 *               If this entity does not have an attached {@link MagicColorComponent},
	 *               will default to {@link Arcanus#DEFAULT_MAGIC_COLOUR}
	 */
	public static void copyMagicColor(Object to, Entity from) {
		ArcanusComponents.MAGIC_COLOR.maybeGet(from).ifPresent(sourceComponent -> setMagicColorSource(to, sourceComponent.getSourceId()));
	}
}
