package dev.cammiescorner.arcanuscontinuum.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

//TODO translatable texts
public class WizardLevelCommand {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> builder) {
		builder.then(CommandManager.literal("wizard_level")
			.then(CommandManager.literal("set")
				.requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(CommandManager.GAME_MASTER_PERMISSION_LEVEL))
				.then(CommandManager.argument("player", EntityArgumentType.player())
					.then(CommandManager.argument("level", IntegerArgumentType.integer(0, 10))
						.executes(context -> WizardLevelCommand.setLevel(context, EntityArgumentType.getPlayer(context, "player")))
					)
				)
				.then(CommandManager.argument("level", IntegerArgumentType.integer(0, 10))
					.executes(context -> WizardLevelCommand.setLevel(context, context.getSource().getPlayerOrThrow()))
				)
			)
			.then(CommandManager.literal("get")
				.then(CommandManager.argument("player", EntityArgumentType.player())
					.executes(context -> WizardLevelCommand.getLevel(context, EntityArgumentType.getPlayer(context, "player")))
				)
				.executes(context -> WizardLevelCommand.getLevel(context, context.getSource().getPlayerOrThrow()))
			)
		);
	}

	public static int getLevel(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
		int level = ArcanusComponents.getWizardLevel(player);
		context.getSource().sendFeedback(() -> Text.literal(String.format("%s's wizard level is %s", player.getEntityName(), level)), false);

		return level;
	}

	public static int setLevel(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
		int level = IntegerArgumentType.getInteger(context, "level");
		ArcanusComponents.setWizardLevel(player, level);

		context.getSource().sendFeedback(() -> Text.literal(String.format("Set %s's level to %s", player.getEntityName(), level)), false);

		return Command.SINGLE_SUCCESS;
	}
}
