package dev.cammiescorner.arcanus.fabric.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanus.fabric.common.registry.ArcanusComponents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class WizardLevelCommand {

	public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder.then(Commands.literal("wizard_level")
			.then(Commands.literal("set")
				.requires(serverCommandSource -> serverCommandSource.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.argument("level", IntegerArgumentType.integer(0, 10))
					.then(Commands.argument("player", EntityArgument.player())
						.executes(context -> WizardLevelCommand.setLevel(context, EntityArgument.getPlayer(context, "player")))
					)
					.executes(context -> WizardLevelCommand.setLevel(context, context.getSource().getPlayerOrException()))
				)
			)
			.then(Commands.literal("get")
				.then(Commands.argument("player", EntityArgument.player())
					.requires(serverCommandSource -> serverCommandSource.hasPermission(Commands.LEVEL_GAMEMASTERS))
					.executes(context -> WizardLevelCommand.getLevel(context, EntityArgument.getPlayer(context, "player")))
				)
				.executes(context -> WizardLevelCommand.getLevel(context, context.getSource().getPlayerOrException()))
			)
		);
	}

	public static int getLevel(CommandContext<CommandSourceStack> context, ServerPlayer player) throws CommandSyntaxException {
		int level = ArcanusComponents.getWizardLevel(player);
		context.getSource().sendSuccess(() -> Component.translatable("command.arcanus.wizard_level.get.success", player.getScoreboardName(), level), false);

		return level;
	}

	public static int setLevel(CommandContext<CommandSourceStack> context, ServerPlayer player) throws CommandSyntaxException {
		int level = IntegerArgumentType.getInteger(context, "level");
		ArcanusComponents.setWizardLevel(player, level);

		context.getSource().sendSuccess(() -> Component.translatable("command.arcanus.wizard_level.set.success", player.getScoreboardName(), level), true);

		return Command.SINGLE_SUCCESS;
	}
}
