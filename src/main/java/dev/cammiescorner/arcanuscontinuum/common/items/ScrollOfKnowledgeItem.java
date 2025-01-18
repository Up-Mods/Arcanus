package dev.cammiescorner.arcanuscontinuum.common.items;

import dev.cammiescorner.arcanuscontinuum.common.components.entity.WizardLevelComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScrollOfKnowledgeItem extends Item {
	public ScrollOfKnowledgeItem() {
		super(new Item.Properties().stacksTo(16));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		WizardLevelComponent component = ArcanusComponents.WIZARD_LEVEL_COMPONENT.get(user);
		ItemStack stack = user.getItemInHand(hand);

		if(!world.isClientSide()) {
			if(component.getLevel() < component.getMaxLevel()) {
				component.setLevel(component.getLevel() + 1);

				if(!user.isCreative())
					stack.shrink(1);

				user.displayClientMessage(Component.translatable("text.arcanuscontinuum.use_item.scroll_of_knowledge").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC), true);
			}
		}

		if(component.getLevel() >= component.getMaxLevel()) {
			user.displayClientMessage(Component.translatable("text.arcanuscontinuum.use_item.scroll_of_knowledge.max_level").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC), true);
			return InteractionResultHolder.fail(stack);
		}

		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
	}
}
