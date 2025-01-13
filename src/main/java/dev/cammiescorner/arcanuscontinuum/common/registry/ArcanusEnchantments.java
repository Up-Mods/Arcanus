package dev.cammiescorner.arcanuscontinuum.common.registry;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.enchantments.ManaPoolEnchantment;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;

public class ArcanusEnchantments {
	public static final RegistryHandler<Enchantment> ENCHANTMENTS = RegistryHandler.create(Registries.ENCHANTMENT, Arcanus.MOD_ID);

	public static final RegistrySupplier<Enchantment> MANA_POOL = ENCHANTMENTS.register("mana_pool", ManaPoolEnchantment::new);
}
