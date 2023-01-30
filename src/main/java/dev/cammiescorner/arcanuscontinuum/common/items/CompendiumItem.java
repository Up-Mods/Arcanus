package dev.cammiescorner.arcanuscontinuum.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class CompendiumItem extends Item {
	public CompendiumItem() {
		super(new QuiltItemSettings().maxCount(1));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//		if(user instanceof ServerPlayerEntity player) {
//			Book book = BookRegistry.INSTANCE.books.get(Registries.ITEM.getId(this));
//			PatchouliAPI.get().openBookGUI(player, book.id);
//			user.playSound(PatchouliSounds.getSound(book.openSound, PatchouliSounds.BOOK_OPEN), 1, (float) (0.7 + Math.random() * 0.4));
//		}

		return TypedActionResult.success(user.getStackInHand(hand));
	}
}
