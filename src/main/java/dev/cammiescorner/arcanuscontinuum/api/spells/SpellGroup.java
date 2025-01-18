package dev.cammiescorner.arcanuscontinuum.api.spells;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record SpellGroup(SpellShape shape, List<SpellEffect> effects, List<Vector2i> positions) {
	public static SpellGroup fromNbt(CompoundTag tag) {
		SpellShape shape = (SpellShape) Arcanus.SPELL_COMPONENTS.get(new ResourceLocation(tag.getString("Shape")));
		List<SpellEffect> effects = new ArrayList<>();
		List<Vector2i> positions = new ArrayList<>();
		ListTag nbtEffects = tag.getList("Effects", Tag.TAG_STRING);
		ListTag nbtPoses = tag.getList("Positions", Tag.TAG_COMPOUND);

		for(int i = 0; i < nbtEffects.size(); i++) {
			// TODO remove in 1.21.1
			String nbtId = nbtEffects.getString(i);
			ResourceLocation correctedId = switch(nbtId) {
				case "arcanuscontinuum:explosion_shape" -> ArcanusSpellComponents.BURST.getId();
				case "arcanuscontinuum:guardian_orb_shape" -> ArcanusSpellComponents.GUARDIAN_ORB.getId();
				case "arcanuscontinuum:aggressorb_shape" -> ArcanusSpellComponents.AGGRESSORB.getId();
				default -> new ResourceLocation(nbtId);
			};

			if(Arcanus.SPELL_COMPONENTS.get(correctedId) instanceof SpellEffect effect)
				effects.add(effect);
		}

		for(int i = 0; i < nbtPoses.size(); i++) {
			CompoundTag nbt = nbtPoses.getCompound(i);
			positions.add(new Vector2i(nbt.getInt("X"), nbt.getInt("Y")));
		}

		if(positions.size() != effects.size() + 1)
			return new SpellGroup((SpellShape) ArcanusSpellComponents.EMPTY.get(), List.of(), List.of());

		return new SpellGroup(shape, effects, positions);
	}

	public CompoundTag toNbt() {
		CompoundTag tag = new CompoundTag();
		ListTag effectsList = new ListTag();
		ListTag posesList = new ListTag();

		for(SpellEffect effect : effects)
			effectsList.add(StringTag.valueOf(Arcanus.SPELL_COMPONENTS.getKey(effect).toString()));

		for(Vector2i position : positions) {
			CompoundTag nbt = new CompoundTag();
			nbt.putInt("X", position.x());
			nbt.putInt("Y", position.y());
			posesList.add(nbt);
		}

		tag.putString("Shape", Arcanus.SPELL_COMPONENTS.getKey(shape).toString());
		tag.put("Effects", effectsList);
		tag.put("Positions", posesList);

		return tag;
	}

	public Weight getAverageWeight() {
		int cumulativeWeightIndex = shape.getWeight().ordinal();
		int effectCount = 0;

		for(int i = 0; i < effects.size(); i++) {
			SpellEffect effect = effects().get(i);

			if(effect.getWeight() != Weight.NONE) {
				cumulativeWeightIndex += effect.getWeight().ordinal();
				effectCount++;
			}
		}

		return Weight.values()[Math.round(cumulativeWeightIndex / ((float) effectCount + 1F))];
	}

	public double getManaCost() {
		double cumulativeManaCost = shape().getManaCost();

		for(SpellEffect effect : effects)
			cumulativeManaCost += effect.getManaCost();

		return cumulativeManaCost;
	}

	public int getCoolDown() {
		int cumulativeCoolDown = shape().getCoolDown();

		for(SpellEffect effect : effects)
			cumulativeCoolDown += effect.getCoolDown();

		return cumulativeCoolDown;
	}

	public Stream<SpellComponent> getAllComponents() {
		return Stream.concat(Stream.of(shape), effects.stream());
	}

	public boolean isEmpty() {
		return ArcanusSpellComponents.EMPTY.is(shape);
	}
}
