package dev.cammiescorner.arcanus.fabric.common.command.pocketdimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanus.fabric.common.command.PocketDimensionCommand;
import dev.cammiescorner.arcanus.fabric.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanus.fabric.common.data.ArcanusDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;

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

		if(!component.replacePlotSpace(target.getId(), pocketDimension, regenerateType)) {
			context.getSource().sendFailure(Component.literal("Pocket dimension location not found for player %s (%s)".formatted(target.getName(), target.getId())));
			return 0;
		}

		context.getSource().sendSuccess(() -> switch(regenerateType) {
			case WALLS_ONLY ->
				Component.translatable("command.arcanus.pocket_dimension.regenerate.success.walls_only", target.getName());
			case FULL ->
				Component.translatable("command.arcanus.pocket_dimension.regenerate.success.full", target.getName());
			default -> throw new UnsupportedOperationException();
		}, true);
		return Command.SINGLE_SUCCESS;
	}
}
