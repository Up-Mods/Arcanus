package dev.cammiescorner.arcanus.fabric.common.components.entity;

import dev.cammiescorner.arcanus.ArcanusConfig;
import dev.cammiescorner.arcanus.api.entities.ArcanusEntityAttributes;
import dev.cammiescorner.arcanus.fabric.common.entities.magic.GuardianOrbEntity;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class GuardianOrbComponent implements ServerTickingComponent {
	public static final UUID uUID = UUID.fromString("ef75422a-3096-4111-965f-9526c3ed55e0");
	private final LivingEntity entity;
	private UUID orbId = Util.NIL_UUID;
	private int strength = 0;
	private boolean dirty = false;

	public GuardianOrbComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(dirty)
			setManaLock(orbId, strength);

		if(!orbId.equals(Util.NIL_UUID) && entity.level() instanceof ServerLevel world) {
			if(world.getEntity(orbId) instanceof GuardianOrbEntity orb && entity == orb.getCaster())
				return;

			setManaLock(Util.NIL_UUID, 0);
		}
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		orbId = tag.getUUID("OrbId");
		strength = tag.getInt("Strength");
		dirty = true;
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putUUID("OrbId", orbId);
		tag.putInt("Strength", strength);
	}

	public UUID getOrbId() {
		return orbId;
	}

	public void setManaLock(UUID orbId, int strength) {
		AttributeInstance maxMana = entity.getAttribute(ArcanusEntityAttributes.MAX_MANA.get());
		AttributeInstance manaLock = entity.getAttribute(ArcanusEntityAttributes.MANA_LOCK.get());

		if(manaLock != null)
			manaLock.removeModifier(uUID);
		if(maxMana != null && manaLock != null && !orbId.equals(Util.NIL_UUID))
			manaLock.addPermanentModifier(new AttributeModifier(uUID, "Orb Mana Lock", maxMana.getValue() * (strength * (ArcanusConfig.SpellShapes.GuardianOrbShapeProperties.maximumManaLock / 11)), AttributeModifier.Operation.ADDITION));

		this.orbId = orbId;
		this.strength = strength;
	}
}
