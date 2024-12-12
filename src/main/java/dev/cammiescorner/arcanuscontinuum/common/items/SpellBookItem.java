package dev.cammiescorner.arcanuscontinuum.common.items;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.Spell;
import dev.cammiescorner.arcanuscontinuum.common.screens.SpellBookScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.List;
import java.util.Locale;

public class SpellBookItem extends Item {
	public SpellBookItem() {
		super(new QuiltItemSettings().maxCount(1));
	}

	@Override
	public Text getName(ItemStack stack) {
		Spell spell = getSpell(stack);

		return ((MutableText) super.getName(stack)).append(" (" + spell.getName() + ")");
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		Spell spell = getSpell(stack);

		String manaCost = Arcanus.format(spell.getManaCost());
		String coolDown = Arcanus.format(spell.getCoolDown() / 20D);

		tooltip.add(Text.literal(spell.getName()).formatted(Formatting.GOLD));
		tooltip.add(Arcanus.translate("spell_book", "weight").append(": ").formatted(Formatting.GREEN)
				.append(Arcanus.translate("spell_book", "weight", spell.getWeight().toString().toLowerCase(Locale.ROOT)).formatted(Formatting.GRAY)));
		tooltip.add(Arcanus.translate("spell_book", "mana_cost").append(": ").formatted(Formatting.BLUE)
				.append(Text.literal(manaCost).formatted(Formatting.GRAY)));
		tooltip.add(Arcanus.translate("spell_book", "cool_down").append(": ").formatted(Formatting.RED)
				.append(Text.literal(coolDown).append(Arcanus.translate("spell_book", "seconds")).formatted(Formatting.GRAY)));

		super.appendTooltip(stack, world, tooltip, context);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);

		if(state.isOf(Blocks.LECTERN))
			return LecternBlock.putBookIfAbsent(context.getPlayer(), world, pos, state, context.getStack()) ? ActionResult.success(world.isClient) : ActionResult.PASS;

		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		Spell spell = getSpell(stack);

		if(spell.isEmpty())
			return super.use(world, player, hand);

		player.openHandledScreen(new ExtendedScreenHandlerFactory() {
			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
				buf.writeItemStack(stack);
			}

			@Override
			public Text getDisplayName() {
				return Text.literal(spell.getName());
			}

			@Override
			public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
				return new SpellBookScreenHandler(i, playerInventory, stack);
			}
		});

		return TypedActionResult.success(stack, world.isClient());
	}

	public static Spell getSpell(ItemStack stack) {
		return stack.hasNbt() ? Spell.fromNbt(stack.getNbt().getCompound("Spell")) : new Spell();
	}
}
