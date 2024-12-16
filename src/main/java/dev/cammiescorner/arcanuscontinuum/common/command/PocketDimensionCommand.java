package dev.cammiescorner.arcanuscontinuum.common.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanuscontinuum.common.command.pocketdimension.ExportPocketDimensionCommand;
import dev.cammiescorner.arcanuscontinuum.common.command.pocketdimension.RegeneratePocketDimensionCommand;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class PocketDimensionCommand {

	public static void register(LiteralArgumentBuilder<ServerCommandSource> builder) {
		var cmdRoot = CommandManager.argument("player", EntityArgumentType.player());
		ExportPocketDimensionCommand.register(cmdRoot);
		RegeneratePocketDimensionCommand.register(cmdRoot);
		builder.then(CommandManager.literal("pocket_dimension").then(cmdRoot));
	}

	public static GameProfile getPlayerProfile(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return EntityArgumentType.getPlayer(context, "player").getGameProfile();

//		var profile = Optional.ofNullable(server.getUserCache()).flatMap(cache -> cache.getByUuid(target));
	}
}
