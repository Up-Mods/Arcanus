package dev.cammiescorner.arcanus.fabric.common.registry;

import dev.cammiescorner.arcanus.fabric.common.criterion.WizardLevelCriterion;
import net.minecraft.advancements.CriteriaTriggers;

public class ArcanusCriteriaTriggers {

	public static final WizardLevelCriterion WIZARD_LEVEL_CRITERION = new WizardLevelCriterion();

	public static void register() {
		CriteriaTriggers.register(WIZARD_LEVEL_CRITERION);
	}
}
