package dev.cammiescorner.arcanuscontinuum.common.compat;

import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.tr7zw.firstperson.FirstPersonModelCore;
import dev.tr7zw.firstperson.api.FirstPersonAPI;

public class FirstPersonCompat {
	public static void init() {
		ArcanusClient.FIRST_PERSON_MODEL_ENABLED = FirstPersonAPI::isEnabled;
		ArcanusClient.FIRST_PERSON_SHOW_HANDS = () -> FirstPersonModelCore.instance.getLogicHandler().showVanillaHands();
	}
}
