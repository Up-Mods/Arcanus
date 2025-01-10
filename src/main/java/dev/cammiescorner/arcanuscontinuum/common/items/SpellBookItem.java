package dev.cammiescorner.arcanuscontinuum.common.items;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.common.screens.SpellBookScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class SpellBookItem extends Item {
	public SpellBookItem() {
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public Component getName(ItemStack stack) {
		Spell spell = getSpell(stack);

		return ((MutableComponent) super.getName(stack)).append(" (" + spell.getName() + ")");
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		Spell spell = getSpell(stack);

		String manaCost = Arcanus.format(spell.getManaCost());
		String coolDown = Arcanus.format(spell.getCoolDown() / 20D);

		// TODO make ALL of it translatable
		tooltip.add(Component.literal(spell.getName()).withStyle(ChatFormatting.GOLD));
		tooltip.add(Component.translatable("spell_book.arcanuscontinuum.weight").append(": ").withStyle(ChatFormatting.GREEN)
			.append(Component.translatable("spell_book.arcanuscontinuum.weight", spell.getWeight().toString().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY)));
		tooltip.add(Component.translatable("spell_book.arcanuscontinuum.mana_cost").append(": ").withStyle(ChatFormatting.BLUE)
			.append(Component.literal(manaCost).withStyle(ChatFormatting.GRAY)));
		tooltip.add(Component.translatable("spell_book.arcanuscontinuum.cool_down").append(": ").withStyle(ChatFormatting.RED)
			.append(Component.literal(coolDown).append(Component.translatable("spell_book.arcanuscontinuum.seconds")).withStyle(ChatFormatting.GRAY)));

		super.appendHoverText(stack, world, tooltip, context);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);

		if(state.is(Blocks.LECTERN))
			return LecternBlock.tryPlaceBook(context.getPlayer(), world, pos, state, context.getItemInHand()) ? InteractionResult.sidedSuccess(world.isClientSide) : InteractionResult.PASS;

		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Spell spell = getSpell(stack);

		if(spell.isEmpty())
			return super.use(world, player, hand);

		player.openMenu(new ExtendedScreenHandlerFactory() {
			@Override
			public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
				buf.writeItem(stack);
			}

			@Override
			public Component getDisplayName() {
				return Component.literal(spell.getName());
			}

			@Override
			public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
				return new SpellBookScreenHandler(i, playerInventory, stack);
			}
		});

		return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
	}

	public static Spell getSpell(ItemStack stack) {
		return stack.hasTag() ? Spell.fromNbt(stack.getTag().getCompound("Spell")) : new Spell();
	}
}
