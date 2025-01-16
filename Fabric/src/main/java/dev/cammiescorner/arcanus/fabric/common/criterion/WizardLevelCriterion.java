package dev.cammiescorner.arcanus.fabric.common.criterion;

import com.google.gson.JsonObject;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class WizardLevelCriterion extends SimpleCriterionTrigger<WizardLevelCriterion.TriggerInstance> {

	public static final ResourceLocation ID = FabricMain.id("wizard_level");

	@Override
	protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
		int level = GsonHelper.getAsInt(json, "level");
		return new TriggerInstance(predicate, level);
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, triggerInstance -> ArcanusComponents.WIZARD_LEVEL_COMPONENT.get(player).getLevel() >= triggerInstance.level);
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {

		private int level;

		public TriggerInstance(ContextAwarePredicate player, int level) {
			super(ID, player);
			this.level = level;
		}

		public static TriggerInstance hasWizardLevel(int level) {
			return new TriggerInstance(ContextAwarePredicate.ANY, level);
		}

		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject json = super.serializeToJson(context);
			json.addProperty("level", level);
			return json;
		}
	}
}
