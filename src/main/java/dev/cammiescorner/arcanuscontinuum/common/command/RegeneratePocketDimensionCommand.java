package dev.cammiescorner.arcanuscontinuum.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

//TODO translatable texts
public class RegeneratePocketDimensionCommand {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> builder) {
		builder.then(CommandManager.literal("regenerate_pocket_dimension")
			.requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(CommandManager.ADMIN_PERMISSION_LEVEL))
			.then(CommandManager.argument("player", EntityArgumentType.player())
				.executes(context -> RegeneratePocketDimensionCommand.regeneratePocket(context, EntityArgumentType.getPlayer(context, "player")))
			)
			.executes(context -> RegeneratePocketDimensionCommand.regeneratePocket(context, context.getSource().getPlayerOrThrow()))
		);
	}

	public static int regeneratePocket(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
		var pocketDimension = context.getSource().getServer().getWorld(PocketDimensionComponent.POCKET_DIM);
		var component = context.getSource().getWorld().getProperties().getComponent(ArcanusComponents.POCKET_DIMENSION_COMPONENT);

		if (!component.generatePlotSpace(player, pocketDimension)) {
			context.getSource().sendError(Text.literal("Pocket dimension location not found for player %s".formatted(player.getEntityName())));
			return 0;
		}

		context.getSource().sendFeedback(() -> Text.literal("Regenerated %s's pocket dimension".formatted(player.getEntityName())), false);
		return Command.SINGLE_SUCCESS;
	}
}
