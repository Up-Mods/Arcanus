package dev.cammiescorner.arcanuscontinuum.common.command.pocketdimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.command.PocketDimensionCommand;
import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportPocketDimensionCommand {

	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static void register(RequiredArgumentBuilder<CommandSourceStack, EntitySelector> builder) {
		builder.then(Commands.literal("export")
			.requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.executes(context -> ExportPocketDimensionCommand.export(context, PocketDimensionCommand.getPlayerProfile(context)))
		);
	}

	private static int export(CommandContext<CommandSourceStack> context, GameProfile targetProfile) {
		String date = LocalDateTime.now().format(FORMAT);
		var server = context.getSource().getServer();
		var pocketDim = server.getLevel(PocketDimensionComponent.POCKET_DIM);
		if(pocketDim == null) {
			context.getSource().sendFailure(Component.literal("Unable to find %s".formatted(PocketDimensionComponent.POCKET_DIM)));
			return 0;
		}

		var plot = PocketDimensionComponent.get(server).getAssignedPlotSpace(targetProfile.getId());
		if(plot == null) {
			context.getSource().sendFailure(Component.literal("Pocket dimension for player %s has not been created yet!".formatted(targetProfile.getName())));
			return 0;
		}

		var structureId = Arcanus.id("pocket_dimensions/%s/pocket_dimensions_%s".formatted(targetProfile.getId(), date));
		Arcanus.LOGGER.info("Saving pocket dimension for player {} ({}) as '{}'", targetProfile.getName(), targetProfile.getId(), structureId);

		var templateManager = pocketDim.getStructureManager();
		var structure = templateManager.getOrCreate(structureId);
		structure.setAuthor("%s_pocket_dimension_%s_%s".formatted(Arcanus.MOD_ID, targetProfile.getId(), date));
		structure.fillFromWorld(pocketDim, plot.min(), plot.max().offset(1, 1, 1).subtract(plot.min()), true, null);
		templateManager.save(structureId);

		context.getSource().sendSuccess(() -> Component.literal("Successfully exported %s's pocket dimension".formatted(targetProfile.getName())), true);


		return Command.SINGLE_SUCCESS;
	}
}
