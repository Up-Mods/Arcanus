package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class QuestComponent implements AutoSyncedComponent {
	private final Player player;
	private final List<ResourceLocation> questIds = List.of(FabricMain.id("start"));
	private long lastCompletedQuestTime;

	public QuestComponent(Player player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		lastCompletedQuestTime = tag.getLong("LastCompletedQuestTime");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putLong("LastCompletedQuestTime", lastCompletedQuestTime);
	}

	public List<ResourceLocation> getQuestIds() {
		return questIds;
	}

	public long getLastCompletedQuestTime() {
		return lastCompletedQuestTime;
	}

	public void setLastCompletedQuestTime(long time) {
		this.lastCompletedQuestTime = time;
		ArcanusComponents.QUEST_COMPONENT.sync(player);
	}
}
