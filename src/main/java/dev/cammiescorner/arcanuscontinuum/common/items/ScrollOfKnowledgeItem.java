package dev.cammiescorner.arcanuscontinuum.common.items;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.components.entity.WizardLevelComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ScrollOfKnowledgeItem extends Item {
	public ScrollOfKnowledgeItem() {
		super(new QuiltItemSettings().stacksTo(16));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		WizardLevelComponent wizardLevel = ArcanusComponents.WIZARD_LEVEL_COMPONENT.get(user);
		ItemStack stack = user.getItemInHand(hand);

		if(wizardLevel.getLevel() < 10) {
			wizardLevel.setLevel(wizardLevel.getLevel() + 1);

			if(!user.isCreative())
				stack.shrink(1);

			user.displayClientMessage(Arcanus.translate("scroll_of_knowledge", "level_up").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC), true);

			return InteractionResultHolder.success(stack);
		}

		return super.use(world, user, hand);
	}
}
