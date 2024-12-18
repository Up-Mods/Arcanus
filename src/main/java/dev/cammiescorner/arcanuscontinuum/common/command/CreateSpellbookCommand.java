package dev.cammiescorner.arcanuscontinuum.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class CreateSpellbookCommand {

	public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
		builder.then(Commands.literal("create_random_spellbook")
			.requires(serverCommandSource -> serverCommandSource.hasPermission(Commands.LEVEL_MODERATORS))
			.executes(context -> CreateSpellbookCommand.createRandomSpellbook(context, context.getSource().getPlayerOrException()))
		);
	}

	private static int createRandomSpellbook(CommandContext<CommandSourceStack> context, ServerPlayer player) {
		return Command.SINGLE_SUCCESS;
	}
}
