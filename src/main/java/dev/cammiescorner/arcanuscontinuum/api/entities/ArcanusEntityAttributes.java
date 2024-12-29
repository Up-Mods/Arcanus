package dev.cammiescorner.arcanuscontinuum.api.entities;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.jetbrains.annotations.ApiStatus;

public class ArcanusEntityAttributes {

	private static volatile boolean isInitialized = false;
	private static final RegistryHandler<Attribute> ENTITY_ATTRIBUTES = RegistryHandler.create(Registries.ATTRIBUTE, Arcanus.MOD_ID);

	public static final RegistrySupplier<Attribute> MAX_MANA = ENTITY_ATTRIBUTES.register("max_mana", () -> new RangedAttribute("attribute.name.generic.arcanuscontinuum.max_mana", 10d, 0d, 1024d).setSyncable(true));
	public static final RegistrySupplier<Attribute> MANA_REGEN = ENTITY_ATTRIBUTES.register("mana_regen", () -> new RangedAttribute("attribute.name.generic.arcanuscontinuum.mana_regen", 0.5d, 0d, 1024d).setSyncable(true));
	public static final RegistrySupplier<Attribute> BURNOUT_REGEN = ENTITY_ATTRIBUTES.register("burnout_regen", () -> new RangedAttribute("attribute.name.generic.arcanuscontinuum.burnout_regen", 0.5d, 0d, 1024d).setSyncable(true));
	public static final RegistrySupplier<Attribute> MANA_LOCK = ENTITY_ATTRIBUTES.register("mana_lock", () -> new RangedAttribute("attribute.name.generic.arcanuscontinuum.mana_lock", 0d, 0d, 1024d).setSyncable(true));
	public static final RegistrySupplier<Attribute> SPELL_POTENCY = ENTITY_ATTRIBUTES.register("spell_potency", () -> new RangedAttribute("attribute.name.generic.arcanuscontinuum.spell_potency", 1d, 0d, 1024d).setSyncable(true));
	public static final RegistrySupplier<Attribute> MANA_COST = ENTITY_ATTRIBUTES.register("mana_cost", () -> new RangedAttribute("attribute.name.generic.arcanuscontinuum.mana_cost", 1d, 0d, 1024d).setSyncable(true));
	public static final RegistrySupplier<Attribute> MAGIC_RESISTANCE = ENTITY_ATTRIBUTES.register("magic_resistance", () -> new RangedAttribute("attribute.name.generic.arcanuscontinuum.magic_resistance", 1d, 0d, 1024d).setSyncable(true));
	public static final RegistrySupplier<Attribute> SPELL_COOL_DOWN = ENTITY_ATTRIBUTES.register("spell_cool_down", () -> new RangedAttribute("attribute.name.generic.arcanuscontinuum.spell_cool_down", 1d, 0d, 1024d).setSyncable(true));

	@ApiStatus.Internal
	public static synchronized void registerAll() {
		if(!isInitialized) {
			ArcanusEntityAttributes.ENTITY_ATTRIBUTES.accept(RegistryService.get());
			isInitialized = true;
		}
	}
}
