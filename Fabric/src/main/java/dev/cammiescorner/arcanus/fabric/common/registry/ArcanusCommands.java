package dev.cammiescorner.arcanus.fabric.common.registry;

import com.mojang.brigadier.CommandDispatcher;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
import dev.cammiescorner.arcanus.fabric.common.command.PocketDimensionCommand;
import dev.cammiescorner.arcanus.fabric.common.command.WizardLevelCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ArcanusCommands {
	public static void init(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
		var root = Commands.literal(FabricMain.MOD_ID);
		WizardLevelCommand.register(root);
		PocketDimensionCommand.register(root);
		dispatcher.register(root);
	}

}
