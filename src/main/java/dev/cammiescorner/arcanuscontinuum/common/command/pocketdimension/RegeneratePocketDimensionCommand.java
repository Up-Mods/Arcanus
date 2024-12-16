package dev.cammiescorner.arcanuscontinuum.common.command.pocketdimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanuscontinuum.common.command.PocketDimensionCommand;
import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

//TODO translatable texts
public class RegeneratePocketDimensionCommand {

	public static void register(RequiredArgumentBuilder<ServerCommandSource, EntitySelector> builder) {
		builder.then(CommandManager.literal("regenerate")
			.requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(CommandManager.ADMIN_PERMISSION_LEVEL))
			.executes(context -> RegeneratePocketDimensionCommand.regeneratePocket(context, PocketDimensionCommand.getPlayerProfile(context)))
		);
	}

	public static int regeneratePocket(CommandContext<ServerCommandSource> context, GameProfile target) throws CommandSyntaxException {
		var server = context.getSource().getServer();
		var pocketDimension = server.getWorld(PocketDimensionComponent.POCKET_DIM);
		var component = PocketDimensionComponent.get(server);

		if (!component.replacePlotSpace(target.getId(), pocketDimension, true)) {
			context.getSource().sendError(Text.literal("Pocket dimension location not found for player %s (%s)".formatted(target.getName(), target.getId())));
			return 0;
		}

		context.getSource().sendFeedback(() -> Text.literal("Regenerated %s's pocket dimension".formatted(target.getName())), false);
		return Command.SINGLE_SUCCESS;
	}
}
