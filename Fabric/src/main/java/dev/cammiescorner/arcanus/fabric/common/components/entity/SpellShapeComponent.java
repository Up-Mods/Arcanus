package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.api.spells.SpellShape;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SpellShapeComponent implements AutoSyncedComponent {
	private final Entity entity;
	private SpellShape shape = SpellShape.empty();

	public SpellShapeComponent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		shape = (SpellShape) FabricMain.SPELL_COMPONENTS.get(new ResourceLocation(tag.getString("SpellShape")));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putString("SpellShape", FabricMain.SPELL_COMPONENTS.getKey(shape).toString());
	}

	public SpellShape getSpellShape() {
		return shape;
	}

	public void setSpellShape(SpellShape shape) {
		this.shape = shape;
		ArcanusComponents.SPELL_SHAPE.sync(entity);
	}
}
