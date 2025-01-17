package dev.cammiescorner.arcanuscontinuum.client.utils;

import com.google.common.collect.Ordering;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.AreaOfEffectEntity;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.ManaShieldEntity;
import dev.cammiescorner.arcanuscontinuum.common.entities.magic.SmiteEntity;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class EntityRendererSorter {

	private static final List<Class<? extends Entity>> RENDER_LAST = List.of(ManaShieldEntity.class, SmiteEntity.class, AreaOfEffectEntity.class);
	public static final Ordering<Entity> ENTITY_ORDERING = Ordering.from((o1, o2) -> RENDER_LAST.contains(o1.getClass()) ? RENDER_LAST.contains(o2.getClass()) ? 0 : 1 : -1);
}
