package dev.cammiescorner.arcanuscontinuum.common.components.entity;

import dev.cammiescorner.arcanuscontinuum.common.entities.magic.Aggressorb;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AggressorbComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private final List<UUID> orbs = new ArrayList<>();
	private final List<UUID> viewOfOrbs = Collections.unmodifiableList(orbs);

	public AggressorbComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		orbs.clear();

		ListTag nbtList = tag.getList("Orbs", Tag.TAG_INT_ARRAY);

		for(Tag nbtElement : nbtList)
			orbs.add(NbtUtils.loadUUID(nbtElement));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag nbtList = new ListTag();

		for(UUID uuid : orbs)
			nbtList.add(NbtUtils.createUUID(uuid));

		tag.put("Orbs", nbtList);
	}

	public List<UUID> getOrbs() {
		return viewOfOrbs;
	}

	public int orbCount() {
		return orbs.size();
	}

	public int orbIndex(Aggressorb orb) {
		return orbs.indexOf(orb.getUUID());
	}

	public void addOrbToEntity(UUID orbId) {
		if(entity.level() instanceof ServerLevel world)
			orbs.removeIf(uuid -> world.getEntity(uuid) == null);

		orbs.add(orbId);
		entity.syncComponent(ArcanusComponents.AGGRESSORB_COMPONENT);
	}

	public void removeOrbFromEntity(UUID orbId) {
		orbs.remove(orbId);
		entity.syncComponent(ArcanusComponents.AGGRESSORB_COMPONENT);
	}
}
