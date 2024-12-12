package dev.cammiescorner.arcanuscontinuum.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class CreateSpellbookCommand {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> builder) {
		builder.then(CommandManager.literal("create_random_spellbook")
			.requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(CommandManager.MODERATOR_PERMISSION_LEVEL))
			.executes(context -> CreateSpellbookCommand.createRandomSpellbook(context, context.getSource().getPlayerOrThrow()))
		);
	}

	private static int createRandomSpellbook(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) {
		return Command.SINGLE_SUCCESS;
	}
}
