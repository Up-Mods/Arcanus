package dev.cammiescorner.arcanuscontinuum.mixin.common;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.entities.Targetable;
import net.minecraft.world.entity.Entity;
import org.quiltmc.qsl.entity.multipart.api.EntityPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPart.class)
public interface EntityPartMixin<E extends Entity> extends Targetable {
	@Shadow E getOwner();

	@Override
	default boolean arcanuscontinuum$canBeTargeted() {
		try {
			return getOwner() instanceof Targetable targetable && targetable.arcanuscontinuum$canBeTargeted();
		}
		catch(UnsupportedOperationException e) {
			Arcanus.LOGGER.error("{} does not implement getOwner()", this.getClass().getName(), e);
			return false;
		}
	}
}
