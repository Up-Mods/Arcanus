package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.LinkedHashMap;

public class ArcanusSoundEvents {
	//-----Sound Map-----//
	public static final LinkedHashMap<SoundEvent, ResourceLocation> SOUNDS = new LinkedHashMap<>();

	//-----Sound Events-----//
	public static final SoundEvent SMITE = create("smite");

	//-----Registry-----//
	public static void register() {
		SOUNDS.keySet().forEach(sound -> Registry.register(BuiltInRegistries.SOUND_EVENT, SOUNDS.get(sound), sound));
	}

	private static SoundEvent create(String name) {
		SoundEvent sound = SoundEvent.createVariableRangeEvent(Arcanus.id(name));
		SOUNDS.put(sound, Arcanus.id(name));
		return sound;
	}

	private static SoundEvent create(String name, float range) {
		SoundEvent sound = SoundEvent.createFixedRangeEvent(Arcanus.id(name), range);
		SOUNDS.put(sound, Arcanus.id(name));
		return sound;
	}
}
