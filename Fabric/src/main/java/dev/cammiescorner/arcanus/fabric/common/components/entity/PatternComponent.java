package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.api.spells.Pattern;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.List;

public class PatternComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private final List<Pattern> list = new ArrayList<>();

	public PatternComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		list.clear();
		ListTag nbtList = tag.getList("Pattern", Tag.TAG_INT);

		for(int i = 0; i < nbtList.size(); i++)
			list.add(Pattern.values()[nbtList.getInt(i)]);
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		ListTag nbtList = new ListTag();

		for(Pattern pattern : list)
			nbtList.add(IntTag.valueOf(pattern.ordinal()));

		tag.put("Pattern", nbtList);
	}

	public List<Pattern> getPattern() {
		return list;
	}

	public void setPattern(List<Pattern> pattern) {
		list.clear();
		list.addAll(pattern);

		if(entity instanceof Player)
			ArcanusComponents.PATTERN_COMPONENT.sync(entity);
	}

	public void clearPattern() {
		list.clear();

		if(entity instanceof Player)
			ArcanusComponents.PATTERN_COMPONENT.sync(entity);
	}
}
