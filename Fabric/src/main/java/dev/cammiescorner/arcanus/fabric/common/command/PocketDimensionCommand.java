package dev.cammiescorner.arcanus.fabric.common.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanus.fabric.common.command.pocketdimension.ExportPocketDimensionCommand;
import dev.cammiescorner.arcanus.fabric.common.command.pocketdimension.RegeneratePocketDimensionCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class PocketDimensionCommand {

	public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
		var cmdRoot = Commands.argument("player", EntityArgument.player());
		ExportPocketDimensionCommand.register(cmdRoot);
		RegeneratePocketDimensionCommand.register(cmdRoot);
		builder.then(Commands.literal("pocket_dimension").then(cmdRoot));
	}

	public static GameProfile getPlayerProfile(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		return EntityArgument.getPlayer(context, "player").getGameProfile();

//		var profile = Optional.ofNullable(server.getUserCache()).flatMap(cache -> cache.getByUuid(target));
	}
}
