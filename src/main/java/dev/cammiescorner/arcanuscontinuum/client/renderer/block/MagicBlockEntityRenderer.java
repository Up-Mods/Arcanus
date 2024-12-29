package dev.cammiescorner.arcanuscontinuum.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.client.ArcanusClient;
import dev.cammiescorner.arcanuscontinuum.common.blocks.entities.AbstractMagicBlockEntity;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusBlocks;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.function.Function;

import static dev.cammiescorner.arcanuscontinuum.client.ArcanusClient.renderSide;

public class MagicBlockEntityRenderer<T extends AbstractMagicBlockEntity> implements BlockEntityRenderer<T> {
	private static final RenderType LAYER = ArcanusClient.getMagicCircles(Arcanus.id("textures/block/magic_block.png"));

	private final Function<T, Color> colorGetter;

	public static <T extends AbstractMagicBlockEntity> BlockEntityRendererProvider<T> factory(Function<T, Color> colorGetter) {
		return ctx -> new MagicBlockEntityRenderer<>(ctx, colorGetter);
	}

	private MagicBlockEntityRenderer(BlockEntityRendererProvider.Context ctx, Function<T, Color> colorGetter) {
		this.colorGetter = colorGetter;
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertices, int light, int overlay) {
		if(entity.getLevel() != null) {
			VertexConsumer consumer = vertices.getBuffer(LAYER);
			Matrix4f matrix4f = matrices.last().pose();
			Matrix3f matrix3f = matrices.last().normal();
			Color color = colorGetter.apply(entity);

			for(Direction direction : Direction.values()) {
				BlockPos blockToSide = entity.getBlockPos().relative(direction);
				BlockState stateToSide = entity.getLevel().getBlockState(blockToSide);

				if(stateToSide.isSolidRender(entity.getLevel(), blockToSide) || stateToSide.is(ArcanusBlocks.MAGIC_BLOCK.get()) || stateToSide.is(ArcanusBlocks.SPATIAL_RIFT_WALL.get()) || stateToSide.is(ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get()))
					continue;

				switch(direction) {
					case SOUTH -> renderSide(matrix4f, consumer, 0F, 1F, 0F, 1F, 1F, 1F, 1F, 1F, color, light, overlay, matrix3f, Direction.SOUTH); // south
					case NORTH -> renderSide(matrix4f, consumer, 0F, 1F, 1F, 0F, 0F, 0F, 0F, 0F, color, light, overlay, matrix3f, Direction.NORTH); // north
					case EAST -> renderSide(matrix4f, consumer, 1F, 1F, 1F, 0F, 0F, 1F, 1F, 0F, color, light, overlay, matrix3f, Direction.EAST); // east
					case WEST -> renderSide(matrix4f, consumer, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F, color, light, overlay, matrix3f, Direction.WEST); // west
					case DOWN -> renderSide(matrix4f, consumer, 0F, 1F, 0F, 0F, 0F, 0F, 1F, 1F, color, light, overlay, matrix3f, Direction.DOWN); // down
					case UP -> renderSide(matrix4f, consumer, 0F, 1F, 1F, 1F, 1F, 1F, 0F, 0F, color, light, overlay, matrix3f, Direction.UP); // up
				}
			}
		}
	}
}
