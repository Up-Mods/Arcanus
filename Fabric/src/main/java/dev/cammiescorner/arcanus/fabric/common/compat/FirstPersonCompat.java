package dev.cammiescorner.arcanus.fabric.common.compat;

import dev.cammiescorner.arcanus.fabric.entrypoints.FabricClient;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.api.FirstPersonAPI;

public class FirstPersonCompat {
	public static void init() {
		FabricClient.FIRST_PERSON_MODEL_ENABLED = FirstPersonAPI::isEnabled;
		FabricClient.FIRST_PERSON_SHOW_HANDS = () -> FirstPersonModelCore.instance.getLogicHandler().showVanillaHands();
	}
}
