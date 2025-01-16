package dev.cammiescorner.arcanus.fabric.common.command.pocketdimension;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.cammiescorner.arcanus.fabric.common.command.PocketDimensionCommand;
import dev.cammiescorner.arcanus.fabric.common.components.level.PocketDimensionComponent;
import dev.cammiescorner.arcanus.fabric.common.data.ArcanusDimensions;
import dev.cammiescorner.arcanus.fabric.entrypoints.FabricMain;
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
		var pocketDim = server.getLevel(ArcanusDimensions.POCKET_DIMENSION);
		if(pocketDim == null) {
			context.getSource().sendFailure(Component.translatable("command.arcanus.pocket_dimension.export.error.dimension_not_found", ArcanusDimensions.POCKET_DIMENSION));
			return 0;
		}

		var plot = PocketDimensionComponent.get(server).getAssignedPlotSpace(targetProfile.getId());
		if(plot == null) {
			context.getSource().sendFailure(Component.translatable("command.arcanus.pocket_dimension.export.error.pocket_not_found", targetProfile.getName()));
			return 0;
		}

		var structureId = Arcanus.id("pocket_dimensions/%s/pocket_dimensions_%s".formatted(targetProfile.getId(), date));
		FabricMain.LOGGER.info("Saving pocket dimension for player {} ({}) as '{}'", targetProfile.getName(), targetProfile.getId(), structureId);

		var templateManager = pocketDim.getStructureManager();
		var structure = templateManager.getOrCreate(structureId);
		structure.setAuthor("%s_pocket_dimension_%s_%s".formatted(Arcanus.MOD_ID, targetProfile.getId(), date));
		structure.fillFromWorld(pocketDim, plot.min(), plot.max().offset(1, 1, 1).subtract(plot.min()), true, null);
		templateManager.save(structureId);

		context.getSource().sendSuccess(() -> Component.translatable("command.arcanus.pocket_dimension.export.success", targetProfile.getName()), true);


		return Command.SINGLE_SUCCESS;
	}
}
