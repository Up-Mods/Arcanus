package dev.cammiescorner.arcanus.fabric.common.registry;

import com.google.common.collect.ImmutableMap;
import dev.cammiescorner.arcanus.fabric.common.compat.ArcanusCompat;
import dev.cammiescorner.arcanus.fabric.common.compat.PatchouliCompat;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArcanusTradeOffers {
	public static final Int2ObjectMap<VillagerTrades.ItemListing[]> WIZARD_TRADES = Util.make(() -> {
		List<VillagerTrades.ItemListing> factories = new ArrayList<>(32);
		Collections.addAll(factories,
			new SellItemFactory(Items.GLOWSTONE_DUST, 3, 3, 100, 1),
			new SellItemFactory(Items.LAPIS_LAZULI, 3, 3, 100, 1),
			new SellItemFactory(Items.GUNPOWDER, 3, 5, 100, 1),
			new SellItemFactory(Items.REDSTONE, 3, 5, 100, 1),
			new SellItemFactory(ArcanusItems.WOODEN_STAFF.get(), 5, 1, 100, 3),
			new SellItemFactory(ArcanusItems.CRYSTAL_STAFF.get(), 5, 1, 100, 3),
			new SellItemFactory(ArcanusItems.DIVINATION_STAFF.get(), 5, 1, 100, 3),
			new SellItemFactory(ArcanusItems.CRESCENT_STAFF.get(), 5, 1, 100, 3),
			new SellItemFactory(ArcanusItems.ANCIENT_STAFF.get(), 5, 1, 100, 3),
			new SellItemFactory(ArcanusItems.MAGIC_TOME.get(), 5, 1, 100, 3),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY), 10, 100, 5),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.NIGHT_VISION), 10, 100, 5),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LEAPING), 10, 100, 5),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE), 10, 100, 5),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS), 10, 100, 5),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER_BREATHING), 10, 100, 5),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_REGENERATION), 10, 100, 5),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH), 10, 100, 5),
			new SellItemFactory(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SLOW_FALLING), 10, 100, 5)
		);
		ArcanusCompat.PATCHOULI.ifEnabled(() -> () -> factories.add(0, new SellItemFactory(PatchouliCompat.getCompendiumArcanus(), 2, 100, 1)));

		return new Int2ObjectOpenHashMap<>(
			ImmutableMap.of(
				1, factories.toArray(VillagerTrades.ItemListing[]::new),
				2, new VillagerTrades.ItemListing[]{
					new SellItemFactory(ArcanusItems.SCROLL_OF_KNOWLEDGE.get(), 64, 1, 100, 10)
				}
			)
		);
	});

	private static class SellItemFactory implements VillagerTrades.ItemListing {
		private final ItemStack sell;
		private final int price;
		private final int maxUses;
		private final int experience;

		public SellItemFactory(Item item, int price, int count, int maxUses, int experience) {
			this(new ItemStack(item, count), price, maxUses, experience);
		}

		public SellItemFactory(ItemStack stack, int price, int maxUses, int experience) {
			this.sell = stack;
			this.price = price;
			this.maxUses = maxUses;
			this.experience = experience;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource random) {
			return new MerchantOffer(new ItemStack(Items.AMETHYST_SHARD, price), sell, maxUses, experience, 0.05F);
		}
	}
}
