package dev.cammiescorner.arcanuscontinuum.common.command.pocketdimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.command.PocketDimensionCommand;
import dev.cammiescorner.arcanuscontinuum.common.components.level.PocketDimensionComponent;
import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportPocketDimensionCommand {

	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static void register(RequiredArgumentBuilder<ServerCommandSource, EntitySelector> builder) {
		builder.then(CommandManager.literal("export")
			.requires(source -> source.hasPermissionLevel(CommandManager.GAME_MASTER_PERMISSION_LEVEL))
			.executes(context -> ExportPocketDimensionCommand.export(context, PocketDimensionCommand.getPlayerProfile(context)))
		);
	}

	private static int export(CommandContext<ServerCommandSource> context, GameProfile targetProfile) {
		String date = LocalDateTime.now().format(FORMAT);
		var server = context.getSource().getServer();
		var pocketDim = server.getWorld(PocketDimensionComponent.POCKET_DIM);
		if(pocketDim == null) {
			context.getSource().sendError(Text.literal("Unable to find %s".formatted(PocketDimensionComponent.POCKET_DIM)));
			return 0;
		}

		var plot = PocketDimensionComponent.get(server).getAssignedPlotSpace(targetProfile.getId());
		if(plot == null) {
			context.getSource().sendError(Text.literal("Pocket dimension for player %s has not been created yet!".formatted(targetProfile.getName())));
			return 0;
		}

		var structureId = Arcanus.id("pocket_dimensions/%s/pocket_dimensions_%s".formatted(targetProfile.getId(), date));
		Arcanus.LOGGER.info("Saving pocket dimension for player {} ({}) as '{}'", targetProfile.getName(), targetProfile.getId(), structureId);

		var templateManager = pocketDim.getStructureTemplateManager();
		var structure = templateManager.getStructureOrBlank(structureId);
		structure.setAuthor("%s_pocket_dimension_%s_%s".formatted(Arcanus.MOD_ID, targetProfile.getId(), date));
		structure.saveFromWorld(pocketDim, plot.min(), plot.max().add(1, 1, 1).subtract(plot.min()), true, null);
		templateManager.saveStructure(structureId);

		context.getSource().sendFeedback(() -> Text.literal("Successfully exported %s's pocket dimension".formatted(targetProfile.getName())), true);


		return Command.SINGLE_SUCCESS;
	}
}
