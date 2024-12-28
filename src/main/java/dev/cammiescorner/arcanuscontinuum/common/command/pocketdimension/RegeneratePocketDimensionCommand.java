package dev.cammiescorner.arcanuscontinuum.common.command.pocketdimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanuscontinuum.common.command.PocketDimensionCommand;
import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanuscontinuum.common.data.ArcanusDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;

//TODO translatable texts
public class RegeneratePocketDimensionCommand {

	public static void register(RequiredArgumentBuilder<CommandSourceStack, EntitySelector> builder) {
		builder.then(Commands.literal("regenerate")
			.requires(serverCommandSource -> serverCommandSource.hasPermission(Commands.LEVEL_ADMINS))
			.executes(context -> RegeneratePocketDimensionCommand.regeneratePocket(context, PocketDimensionCommand.getPlayerProfile(context), PocketDimensionComponent.RegenerateType.FULL))
		);
		builder.then(Commands.literal("repair_walls")
			.requires(serverCommandSource -> serverCommandSource.hasPermission(Commands.LEVEL_ADMINS))
			.executes(context -> RegeneratePocketDimensionCommand.regeneratePocket(context, PocketDimensionCommand.getPlayerProfile(context), PocketDimensionComponent.RegenerateType.WALLS_ONLY))
		);
	}

	public static int regeneratePocket(CommandContext<CommandSourceStack> context, GameProfile target, PocketDimensionComponent.RegenerateType regenerateType) throws CommandSyntaxException {
		var server = context.getSource().getServer();
		var pocketDimension = server.getLevel(ArcanusDimensions.POCKET_DIMENSION);
		var component = PocketDimensionComponent.get(server);

		if (!component.replacePlotSpace(target.getId(), pocketDimension, regenerateType)) {
			context.getSource().sendFailure(Component.literal("Pocket dimension location not found for player %s (%s)".formatted(target.getName(), target.getId())));
			return 0;
		}

		var message = switch (regenerateType) {
			case WALLS_ONLY -> "Repaired the walls of %s's pocket dimension".formatted(target.getName());
			case FULL -> "Regenerated %s's pocket dimension".formatted(target.getName());
			default -> throw new UnsupportedOperationException();
		};
		context.getSource().sendSuccess(() -> Component.literal(message), false);
		return Command.SINGLE_SUCCESS;
	}
}
