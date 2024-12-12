package dev.cammiescorner.arcanuscontinuum.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.command.RegeneratePocketDimensionCommand;
import dev.cammiescorner.arcanuscontinuum.common.command.WizardLevelCommand;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ArcanusCommands {
	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext commandBuildContext, CommandManager.RegistrationEnvironment environment) {
		var root = CommandManager.literal(Arcanus.MOD_ID);
		WizardLevelCommand.register(root);
		RegeneratePocketDimensionCommand.register(root);
		dispatcher.register(root);
	}

}
