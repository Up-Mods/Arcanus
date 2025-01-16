package dev.cammiescorner.arcanuscontinuum.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.command.PocketDimensionCommand;
import dev.cammiescorner.arcanuscontinuum.common.command.WizardLevelCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ArcanusCommands {
	public static void init(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
		var root = Commands.literal(Arcanus.MOD_ID);
		WizardLevelCommand.register(root);
		PocketDimensionCommand.register(root);
		dispatcher.register(root);
	}

}
