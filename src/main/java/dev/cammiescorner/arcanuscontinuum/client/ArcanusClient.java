package dev.cammiescorner.arcanuscontinuum.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.Pattern;
import dev.cammiescorner.arcanuscontinuum.client.gui.screens.ArcaneWorkbenchScreen;
import dev.cammiescorner.arcanuscontinuum.client.gui.screens.SpellBookScreen;
import dev.cammiescorner.arcanuscontinuum.client.gui.screens.SpellcraftScreen;
import dev.cammiescorner.arcanuscontinuum.client.models.armour.BattleMageArmourModel;
import dev.cammiescorner.arcanuscontinuum.client.models.armour.WizardArmourModel;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.living.OpossumEntityModel;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.living.WizardEntityModel;
import dev.cammiescorner.arcanuscontinuum.client.models.entity.magic.*;
import dev.cammiescorner.arcanuscontinuum.client.models.feature.HaloModel;
import dev.cammiescorner.arcanuscontinuum.client.models.feature.SpellPatternModel;
import dev.cammiescorner.arcanuscontinuum.client.particles.CollapseParticle;
import dev.cammiescorner.arcanuscontinuum.client.renderer.armour.BattleMageArmourRenderer;
import dev.cammiescorner.arcanuscontinuum.client.renderer.armour.WizardArmourRenderer;
import dev.cammiescorner.arcanuscontinuum.client.renderer.block.MagicBlockEntityRenderer;
import dev.cammiescorner.arcanuscontinuum.client.renderer.block.SpatialRiftExitBlockEntityRenderer;
import dev.cammiescorner.arcanuscontinuum.client.renderer.entity.living.OpossumEntityRenderer;
import dev.cammiescorner.arcanuscontinuum.client.renderer.entity.living.WizardEntityRenderer;
import dev.cammiescorner.arcanuscontinuum.client.renderer.entity.magic.*;
import dev.cammiescorner.arcanuscontinuum.client.renderer.item.StaffItemRenderer;
import dev.cammiescorner.arcanuscontinuum.common.compat.ArcanusCompat;
import dev.cammiescorner.arcanuscontinuum.common.compat.FirstPersonCompat;
import dev.cammiescorner.arcanuscontinuum.common.items.BattleMageArmorItem;
import dev.cammiescorner.arcanuscontinuum.common.items.StaffItem;
import dev.cammiescorner.arcanuscontinuum.common.packets.s2c.*;
import dev.cammiescorner.arcanuscontinuum.common.registry.*;
import dev.cammiescorner.arcanuscontinuum.common.util.ArcanusHelper;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BooleanSupplier;

public class ArcanusClient implements ClientModInitializer {
	private static final Identifier HUD_ELEMENTS = Arcanus.id("textures/gui/hud/mana_bar.png");
	private static final Identifier STUN_OVERLAY = Arcanus.id("textures/gui/hud/stunned_vignette.png");
	private static final Identifier MAGIC_CIRCLES = Arcanus.id("textures/entity/feature/magic_circles.png");
	public static final Identifier WHITE = new Identifier("textures/misc/white.png");
	public static final RenderLayer LAYER = ArcanusClient.getMagicCircles(Arcanus.id("textures/block/warded_block.png"));
	public static BooleanSupplier FIRST_PERSON_MODEL_ENABLED = () -> false;
	public static BooleanSupplier FIRST_PERSON_SHOW_HANDS = () -> true;
	public static boolean castingSpeedHasCoolDown;
	private final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient(ModContainer mod) {
		ArcanusCompat.FIRST_PERSON.ifEnabled(() -> FirstPersonCompat::init);

		HandledScreens.register(ArcanusScreenHandlers.SPELLCRAFT_SCREEN_HANDLER.get(), SpellcraftScreen::new);
		HandledScreens.register(ArcanusScreenHandlers.SPELL_BOOK_SCREEN_HANDLER.get(), SpellBookScreen::new);
		HandledScreens.register(ArcanusScreenHandlers.ARCANE_WORKBENCH_SCREEN_HANDLER.get(), ArcaneWorkbenchScreen::new);

		EntityModelLayerRegistry.registerModelLayer(WizardArmourModel.MODEL_LAYER, WizardArmourModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BattleMageArmourModel.MODEL_LAYER, BattleMageArmourModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WizardEntityModel.MODEL_LAYER, WizardEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(OpossumEntityModel.MODEL_LAYER, OpossumEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MagicLobEntityModel.MODEL_LAYER, MagicLobEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MagicProjectileEntityModel.MODEL_LAYER, MagicProjectileEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MagicRuneEntityModel.MODEL_LAYER, MagicRuneEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(AreaOfEffectEntityModel.MODEL_LAYER, AreaOfEffectEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SpellPatternModel.MODEL_LAYER, SpellPatternModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(HaloModel.MODEL_LAYER, HaloModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(GuardianOrbEntityModel.MODEL_LAYER, GuardianOrbEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PocketDimensionPortalEntityModel.MODEL_LAYER, PocketDimensionPortalEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SpatialRiftEntitySigilModel.MODEL_LAYER, SpatialRiftEntitySigilModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(AggressorbEntityModel.MODEL_LAYER, AggressorbEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(ArcanusEntities.WIZARD.get(), WizardEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.OPOSSUM.get(), OpossumEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.NECRO_SKELETON.get(), SkeletonEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.MANA_SHIELD.get(), ManaShieldEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.MAGIC_PROJECTILE.get(), MagicProjectileEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.AOE.get(), AreaOfEffectEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.SMITE.get(), SmiteEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.MAGIC_RUNE.get(), MagicRuneEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.BEAM.get(), BeamEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.GUARDIAN_ORB.get(), GuardianOrbEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.PORTAL.get(), PocketDimensionPortalEntityRenderer::new);
		EntityRendererRegistry.register(ArcanusEntities.AGGRESSORB.get(), AggressorbEntityRenderer::new);

		ArmorRenderer.register(new WizardArmourRenderer(), ArcanusItems.WIZARD_HAT.get(), ArcanusItems.WIZARD_ROBES.get(), ArcanusItems.WIZARD_PANTS.get(), ArcanusItems.WIZARD_BOOTS.get());
		ArmorRenderer.register(new BattleMageArmourRenderer(), ArcanusItems.BATTLE_MAGE_HELMET.get(), ArcanusItems.BATTLE_MAGE_CHESTPLATE.get(), ArcanusItems.BATTLE_MAGE_LEGGINGS.get(), ArcanusItems.BATTLE_MAGE_BOOTS.get());

		ParticleFactoryRegistry.getInstance().register(ArcanusParticles.COLLAPSE.get(), CollapseParticle.Factory::new);

		BlockRenderLayerMap.put(RenderLayer.getCutout(), ArcanusBlocks.MAGIC_DOOR.get(), ArcanusBlocks.ARCANE_WORKBENCH.get());
		BlockRenderLayerMap.put(RenderLayer.getTranslucent(), ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get());
		BlockEntityRendererFactories.register(ArcanusBlockEntities.MAGIC_BLOCK.get(), MagicBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(ArcanusBlockEntities.SPATIAL_RIFT_EXIT.get(), SpatialRiftExitBlockEntityRenderer::new);

		ClientPlayNetworking.registerGlobalReceiver(SyncStatusEffectPacket.ID, SyncStatusEffectPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncWorkbenchModePacket.ID, SyncWorkbenchModePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncStaffTemplatePacket.ID, SyncStaffTemplatePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncConfigValuesPacket.ID, SyncConfigValuesPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncExplosionParticlesPacket.ID, SyncExplosionParticlesPacket::handle);
		ArcanusCompat.PEHKUI.ifEnabled(() -> () -> ClientPlayNetworking.registerGlobalReceiver(SyncScalePacket.ID, SyncScalePacket::handle));

		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> switch (tintIndex) {
			case 0 -> StaffItem.getPrimaryColorRGB(stack);
			case 1 -> StaffItem.getSecondaryColorRGB(stack);
			default -> -1;
		},
			ArcanusItems.WOODEN_STAFF.get(),
			ArcanusItems.CRYSTAL_STAFF.get(),
			ArcanusItems.DIVINATION_STAFF.get(),
			ArcanusItems.CRESCENT_STAFF.get(),
			ArcanusItems.ANCIENT_STAFF.get(),
			ArcanusItems.WAND.get(),
			ArcanusItems.THAUMATURGES_GAUNTLET.get(),
			ArcanusItems.MAGIC_TOME.get(),
			ArcanusItems.MAGE_PISTOL.get()
		);

		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? ((DyeableArmorItem) stack.getItem()).getColor(stack) : -1,
			ArcanusItems.WIZARD_HAT.get(),
			ArcanusItems.WIZARD_ROBES.get(),
			ArcanusItems.WIZARD_PANTS.get(),
			ArcanusItems.WIZARD_BOOTS.get(),
			ArcanusItems.BATTLE_MAGE_HELMET.get(),
			ArcanusItems.BATTLE_MAGE_CHESTPLATE.get(),
			ArcanusItems.BATTLE_MAGE_LEGGINGS.get(),
			ArcanusItems.BATTLE_MAGE_BOOTS.get()
		);

		ModelPredicateProviderRegistry.register(ArcanusItems.BATTLE_MAGE_HELMET.get(), Arcanus.id("oxidation"), (stack, world, entity, seed) -> BattleMageArmorItem.getOxidation(stack).ordinal() / 10f);
		ModelPredicateProviderRegistry.register(ArcanusItems.BATTLE_MAGE_CHESTPLATE.get(), Arcanus.id("oxidation"), (stack, world, entity, seed) -> BattleMageArmorItem.getOxidation(stack).ordinal() / 10f);
		ModelPredicateProviderRegistry.register(ArcanusItems.BATTLE_MAGE_LEGGINGS.get(), Arcanus.id("oxidation"), (stack, world, entity, seed) -> BattleMageArmorItem.getOxidation(stack).ordinal() / 10f);
		ModelPredicateProviderRegistry.register(ArcanusItems.BATTLE_MAGE_BOOTS.get(), Arcanus.id("oxidation"), (stack, world, entity, seed) -> BattleMageArmorItem.getOxidation(stack).ordinal() / 10f);

		ArcanusItems.ITEMS.stream().forEach(holder -> {
			Item item = holder.get();
			if (item instanceof StaffItem) {
				Identifier id = holder.getId();
				StaffItemRenderer staffItemRenderer = new StaffItemRenderer(id);
				ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(staffItemRenderer);
				BuiltinItemRendererRegistry.INSTANCE.register(item, staffItemRenderer);
				ModelLoadingPlugin.register(ctx -> ctx.addModels(new ModelIdentifier(id.withPath(id.getPath() + "_gui"), "inventory"), new ModelIdentifier(id.withPath(id.getPath() + "_handheld"), "inventory")));
			}
		});

		var obj = new Object() {
			int hudTimer, hitTimer;
		};

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			if (!context.camera().isThirdPerson() && !FIRST_PERSON_MODEL_ENABLED.getAsBoolean())
				renderFirstPersonBolt(context);
		});

		WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, outlineContext) -> {
			ClientPlayerInteractionManager interactionManager = client.interactionManager;
			ClientPlayerEntity player = client.player;
			ClientWorld world = context.world();
			MatrixStack matrices = context.matrixStack();
			VertexConsumerProvider vertices = context.consumers();
			Vec3d cameraPos = context.camera().getPos();

			if (player != null && vertices != null && interactionManager != null) {
				if (client.crosshairTarget instanceof BlockHitResult hitResult && ArcanusComponents.isBlockWarded(world, hitResult.getBlockPos()) && player.handSwinging) {
					if (client.options.attackKey.isPressed() && player.handSwingProgress == 0)
						obj.hitTimer = 20;

					if (!ArcanusComponents.isOwnerOfBlock(player, hitResult.getBlockPos()) || obj.hitTimer > 0) {
						BlockPos blockPos = hitResult.getBlockPos();
						float alpha = obj.hitTimer / 20f;

						renderWardedBlock(matrices, vertices, world, cameraPos, blockPos, alpha);
						player.sendMessage(Arcanus.translate("text", "block_is_warded").formatted(Formatting.RED), true);
					}

					if (obj.hitTimer > 0)
						obj.hitTimer -= 1;
				}

				if (player.getMainHandStack().isIn(ArcanusTags.STAVES) || player.getOffHandStack().isIn(ArcanusTags.STAVES)) {
					AtomicReferenceArray<WorldChunk> chunks = context.world().getChunkManager().chunks.chunks;
					float alpha = (float) (Math.sin(world.getTime() * 0.06f) * 0.4f + 0.6f);

					for (int i = 0; i < chunks.length(); i++) {
						Chunk chunk = chunks.get(i);

						if (chunk != null) {
							ArcanusComponents.getWardedBlocks(chunk).forEach((blockPos, uuid) -> {
								if (blockPos.getSquaredDistance(context.camera().getBlockPos()) < 256)
									renderWardedBlock(matrices, vertices, world, cameraPos, blockPos, alpha);
							});
						}
					}
				}
			}

			return true;
		});

		HudRenderCallback.EVENT.register((gui, tickDelta) -> {
			MatrixStack matrices = gui.getMatrices();
			PlayerEntity player = client.player;

			if (player != null && !player.isSpectator()) {
				int stunTimer = ArcanusComponents.getStunTimer(player);

				if (stunTimer > 0) {
					if (stunTimer > 5)
						renderOverlay(STUN_OVERLAY, Math.min(1F, 0.5F + (stunTimer % 5F) / 10F));
					else
						renderOverlay(STUN_OVERLAY, Math.min(1F, stunTimer / 5F));
				}

				if (!client.gameRenderer.getCamera().isThirdPerson() && !FIRST_PERSON_MODEL_ENABLED.getAsBoolean()) {
					List<Pattern> list = ArcanusComponents.getPattern(player);

					if (!list.isEmpty()) {
						VertexConsumerProvider.Immediate vertices = client.getBufferBuilders().getEntityVertexConsumers();
						RenderLayer renderLayer = getMagicCircles(MAGIC_CIRCLES);
						VertexConsumer vertex = vertices.getBuffer(renderLayer);
						Color color = ArcanusHelper.getMagicColor(player);
						float x = client.getWindow().getScaledWidth() / 2F;
						float y = client.getWindow().getScaledHeight() / 2F;
						float scale = 3F;

						matrices.push();
						matrices.translate(x, y, 0);

						for (int i = 0; i < list.size(); i++) {
							Pattern pattern = list.get(i);
							matrices.push();

							if (i == 1)
								matrices.multiply(Axis.Z_POSITIVE.rotationDegrees((player.age + player.getId() + tickDelta) * (5 + (2.5F * i))));
							else
								matrices.multiply(Axis.Z_NEGATIVE.rotationDegrees((player.age + player.getId() + tickDelta) * (5 + (2.5F * i))));

							matrices.scale(scale, scale, 0);
							matrices.translate(-8.5, -8.5, 0);
							drawTexture(vertex, matrices, color, 0, 0, i * 34, pattern == Pattern.LEFT ? 0 : 24, 17, 17, 128, 48);
							matrices.pop();
						}

						matrices.pop();
						vertices.drawCurrentLayer();
					}
				}

				double maxMana = ArcanusComponents.getMaxMana(player);
				double mana = ArcanusComponents.getMana(player);
				double burnout = ArcanusComponents.getBurnout(player);
				double manaLock = ArcanusComponents.getManaLock(player);

				if (player.getMainHandStack().getItem() instanceof StaffItem)
					obj.hudTimer = Math.min(obj.hudTimer + 1, 40);
				else
					obj.hudTimer = Math.max(obj.hudTimer - 1, 0);

				if (obj.hudTimer > 0) {
					int x = 0;
					int y = client.getWindow().getScaledHeight() - 28;
					int width = 96;
					float alpha = obj.hudTimer > 20 ? 1F : obj.hudTimer / 20F;

					RenderSystem.enableBlend();
					RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

					// render frame
					gui.drawTexture(HUD_ELEMENTS, x, y, 0, 0, 101, 28, 256, 256);

					// render mana
					gui.drawTexture(HUD_ELEMENTS, x, y + 5, 0, 32, (int) (width * (mana / maxMana)), 23, 256, 256);

					// render burnout
					int i = (int) Math.ceil(width * ((burnout + manaLock) / maxMana));
					gui.drawTexture(HUD_ELEMENTS, x + (width - i), y + 5, width - i, 56, i, 23, 256, 256);

					// render mana lock
					i = (int) Math.ceil(width * (manaLock / maxMana));
					gui.drawTexture(HUD_ELEMENTS, x + (width - i), y + 5, width - i, 80, i, 23, 256, 256);

					RenderSystem.disableBlend();
					RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				}
			}
		});

//		ClientEntityTickCallback.EVENT.register((entity, isPassengerTick) -> {
//			if (ArcanusComponents.isTimeSlowed(entity)) {
//				entity.setVelocity(entity.getVelocity().multiply(1d / 2d));
//				entity.velocityModified = true;
//			}
//		});

		// FIXME temporal dilation no worky
//		ClientTickEvents.END.register(mcClient -> {
//			ClientWorld world = mcClient.world;
//
//			if(world != null && !mcClient.isPaused()) {
//				List<Entity> loadedEntityList = new ArrayList<>();
//				world.getEntities().forEach(loadedEntityList::add);
//				StatusEffect statusEffect = ArcanusStatusEffects.TEMPORAL_DILATION.get();
//				float radius = 3;
//
//				for(Entity entity : loadedEntityList) {
//					if(ArcanusComponents.isTimeSlowed(entity)) {
//						List<Entity> targets = world.getOtherEntities(entity, new Box(-radius, -radius, -radius, radius, radius, radius).offset(entity.getPos()), target -> target.squaredDistanceTo(entity) <= radius * radius);
//
//						if(targets.stream().noneMatch(target -> target instanceof LivingEntity livingTarget && livingTarget.hasStatusEffect(statusEffect))) {
//							ArcanusComponents.setSlowTime(entity, false);
//							ArcanusComponents.setBlockUpdates(entity, false);
//						}
//					}
//				}
//			}
//		});
	}

	private static void renderWardedBlock(MatrixStack matrices, VertexConsumerProvider vertices, World world, Vec3d cameraPos, BlockPos blockPos, float alpha) {
		VertexConsumer consumer = vertices.getBuffer(LAYER);
		Vec3d pos = Vec3d.ofCenter(blockPos);

		matrices.push();
		matrices.translate(pos.getX() - cameraPos.getX(), pos.getY() - cameraPos.getY(), pos.getZ() - cameraPos.getZ());
		matrices.scale(1.001f, 1.001f, 1.001f);
		matrices.translate(-0.5, -0.5, -0.5);

		Matrix4f matrix4f = matrices.peek().getModel();
		Matrix3f matrix3f = matrices.peek().getNormal();
		int light = world.getLightLevel(blockPos);
		int overlay = OverlayTexture.DEFAULT_UV;
		Color color = ArcanusHelper.getMagicColor(ArcanusComponents.getWardedBlocks(world.getChunk(blockPos)).get(blockPos));
		float r = (color.red() >> 16 & 255) * alpha;
		float g = (color.green() >> 8 & 255) * alpha;
		float b = (color.blue() & 255) * alpha;

		color = Color.fromFloatsRGB(r, g, b);

		for (Direction direction : Direction.values()) {
			BlockPos posToSide = blockPos.offset(direction);
			BlockState stateToSide = world.getBlockState(posToSide);

			if (stateToSide.isSideSolid(world, posToSide, direction.getOpposite(), SideShapeType.FULL) || ArcanusComponents.isBlockWarded(world, posToSide))
				continue;

			switch (direction) {
				case SOUTH ->
					renderSide(matrix4f, consumer, 0F, 1F, 0F, 1F, 1F, 1F, 1F, 1F, color, light, overlay, matrix3f, Direction.SOUTH);
				case NORTH ->
					renderSide(matrix4f, consumer, 0F, 1F, 1F, 0F, 0F, 0F, 0F, 0F, color, light, overlay, matrix3f, Direction.NORTH);
				case EAST ->
					renderSide(matrix4f, consumer, 1F, 1F, 1F, 0F, 0F, 1F, 1F, 0F, color, light, overlay, matrix3f, Direction.EAST);
				case WEST ->
					renderSide(matrix4f, consumer, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F, color, light, overlay, matrix3f, Direction.WEST);
				case DOWN ->
					renderSide(matrix4f, consumer, 0F, 1F, 0F, 0F, 0F, 0F, 1F, 1F, color, light, overlay, matrix3f, Direction.DOWN);
				case UP ->
					renderSide(matrix4f, consumer, 0F, 1F, 1F, 1F, 1F, 1F, 0F, 0F, color, light, overlay, matrix3f, Direction.UP);
			}
		}

		matrices.pop();
	}

	private static void renderFirstPersonBolt(WorldRenderContext context) {
		ClientPlayerEntity player = context.gameRenderer().getClient().player;
		if (player != null) {
			MatrixStack matrices = context.matrixStack();
			Vec3d camPos = context.camera().getPos();
			float tickDelta = context.tickDelta();
			Vec3d startPos = player.getLerpedPos(tickDelta).add(0, player.getEyeHeight(player.getPose()), 0);

			matrices.push();
			matrices.translate(-camPos.getX(), -camPos.getY(), -camPos.getZ());
			ArcanusHelper.renderBolts(player, startPos.add(0, -0.1, 0), matrices, context.consumers());
			matrices.pop();
		}
	}

	public static RenderLayer getMagicCircles(Identifier texture) {
		return RenderLayer.of(Arcanus.id("magic_circle").toString(), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder().shader(RenderLayer.ENTITY_TRANSLUCENT_EMISSIVE_SHADER).texture(new RenderPhase.Texture(texture, false, false)).overlay(RenderPhase.ENABLE_OVERLAY_COLOR).transparency(RenderLayer.ADDITIVE_TRANSPARENCY).writeMaskState(RenderLayer.ALL_MASK).cull(RenderPhase.DISABLE_CULLING).build(false));
	}

	public static RenderLayer getMagicPortal(Identifier texture) {
		return RenderLayer.of(Arcanus.id("magic_portal").toString(), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder().shader(RenderLayer.ENTITY_TRANSLUCENT_EMISSIVE_SHADER).texture(new RenderPhase.Texture(texture, false, false)).writeMaskState(RenderPhase.COLOR_MASK).overlay(RenderPhase.ENABLE_OVERLAY_COLOR).transparency(RenderLayer.ADDITIVE_TRANSPARENCY).writeMaskState(RenderLayer.ALL_MASK).cull(RenderPhase.DISABLE_CULLING).depthTest(RenderLayer.ALWAYS_DEPTH_TEST).build(false));
	}

	public static RenderLayer getMagicCirclesTri(Identifier texture) {
		return RenderLayer.of(Arcanus.id("magic_circle_tri").toString(), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.TRIANGLES, 256, false, true, RenderLayer.MultiPhaseParameters.builder().shader(RenderLayer.ENTITY_TRANSLUCENT_EMISSIVE_SHADER).texture(new RenderPhase.Texture(texture, false, false)).overlay(RenderPhase.ENABLE_OVERLAY_COLOR).transparency(RenderLayer.ADDITIVE_TRANSPARENCY).writeMaskState(RenderLayer.ALL_MASK).cull(RenderPhase.DISABLE_CULLING).build(false));
	}

	public static void drawTexture(VertexConsumer vertex, MatrixStack matrices, Color color, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		drawTexturedQuad(vertex, matrices.peek().getModel(), color, x, x + width, y, y + height, u / (float) textureWidth, (u + width) / (float) textureWidth, v / (float) textureHeight, (v + height) / (float) textureHeight);
	}

	private static void drawTexturedQuad(VertexConsumer vertex, Matrix4f matrix, Color color, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1) {
		vertex.vertex(matrix, x0, y1, 0).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 0, 1).next();
		vertex.vertex(matrix, x1, y1, 0).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 0, 1).next();
		vertex.vertex(matrix, x1, y0, 0).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 0, 1).next();
		vertex.vertex(matrix, x0, y0, 0).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(0, 0, 1).next();
	}

	public static void renderSide(Matrix4f matrix4f, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Color color, int light, int overlay, Matrix3f normal, Direction direction) {
		vertices.vertex(matrix4f, x1, y1, z1).color(color.red(), color.green(), color.blue(), color.alpha()).uv(0, 1).overlay(overlay).light(light).normal(normal, direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ()).next();
		vertices.vertex(matrix4f, x2, y1, z2).color(color.red(), color.green(), color.blue(), color.alpha()).uv(1, 1).overlay(overlay).light(light).normal(normal, direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ()).next();
		vertices.vertex(matrix4f, x2, y2, z3).color(color.red(), color.green(), color.blue(), color.alpha()).uv(1, 0).overlay(overlay).light(light).normal(normal, direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ()).next();
		vertices.vertex(matrix4f, x1, y2, z4).color(color.red(), color.green(), color.blue(), color.alpha()).uv(0, 0).overlay(overlay).light(light).normal(normal, direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ()).next();
	}

	private void renderOverlay(Identifier texture, float opacity) {
		MinecraftClient client = MinecraftClient.getInstance();
		double scaledHeight = client.getWindow().getScaledHeight();
		double scaledWidth = client.getWindow().getScaledWidth();

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
		RenderSystem.setShaderTexture(0, texture);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(0.0, scaledHeight, -90.0).uv(0.0F, 1.0F).next();
		bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0).uv(1.0F, 1.0F).next();
		bufferBuilder.vertex(scaledWidth, 0.0, -90.0).uv(1.0F, 0.0F).next();
		bufferBuilder.vertex(0.0, 0.0, -90.0).uv(0.0F, 0.0F).next();
		tessellator.draw();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
