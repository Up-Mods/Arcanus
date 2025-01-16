package dev.cammiescorner.arcanus.fabric.entrypoints;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanus.api.spells.Pattern;
import dev.cammiescorner.arcanus.client.models.entity.magic.*;
import dev.cammiescorner.arcanus.client.renderer.entity.magic.*;
import dev.cammiescorner.arcanus.common.packets.s2c.*;
import dev.cammiescorner.arcanus.common.registry.*;
import dev.cammiescorner.arcanus.fabric.client.gui.screens.ArcaneWorkbenchScreen;
import dev.cammiescorner.arcanus.fabric.client.gui.screens.SpellBookScreen;
import dev.cammiescorner.arcanus.fabric.client.gui.screens.SpellcraftScreen;
import dev.cammiescorner.arcanus.fabric.client.models.armour.BattleMageArmourModel;
import dev.cammiescorner.arcanus.fabric.client.models.armour.WizardArmourModel;
import dev.cammiescorner.arcanus.fabric.client.models.entity.living.OpossumEntityModel;
import dev.cammiescorner.arcanus.fabric.client.models.entity.living.WizardEntityModel;
import dev.cammiescorner.arcanus.fabric.client.models.entity.magic.*;
import dev.cammiescorner.arcanus.fabric.client.models.feature.HaloModel;
import dev.cammiescorner.arcanus.fabric.client.models.feature.SpellPatternModel;
import dev.cammiescorner.arcanus.fabric.client.particles.CollapseParticle;
import dev.cammiescorner.arcanus.fabric.client.renderer.armour.BattleMageArmourRenderer;
import dev.cammiescorner.arcanus.fabric.client.renderer.armour.WizardArmourRenderer;
import dev.cammiescorner.arcanus.fabric.client.renderer.block.MagicBlockEntityRenderer;
import dev.cammiescorner.arcanus.fabric.client.renderer.block.SpatialRiftExitBlockEntityRenderer;
import dev.cammiescorner.arcanus.fabric.client.renderer.entity.living.OpossumEntityRenderer;
import dev.cammiescorner.arcanus.fabric.client.renderer.entity.living.WizardEntityRenderer;
import dev.cammiescorner.arcanus.fabric.client.renderer.entity.magic.*;
import dev.cammiescorner.arcanus.fabric.client.renderer.item.StaffItemRenderer;
import dev.cammiescorner.arcanus.fabric.common.compat.ArcanusCompat;
import dev.cammiescorner.arcanus.fabric.common.compat.FirstPersonCompat;
import dev.cammiescorner.arcanus.fabric.common.data.ArcanusItemTags;
import dev.cammiescorner.arcanus.fabric.common.items.BattleMageArmorItem;
import dev.cammiescorner.arcanus.fabric.common.items.StaffItem;
import dev.cammiescorner.arcanus.fabric.common.packets.s2c.*;
import dev.cammiescorner.arcanus.fabric.common.registry.*;
import dev.cammiescorner.arcanus.fabric.common.util.ArcanusHelper;
import dev.cammiescorner.arcanus.fabric.common.util.Color;
import dev.cammiescorner.fabric.client.models.entity.magic.*;
import dev.cammiescorner.fabric.client.renderer.entity.magic.*;
import dev.cammiescorner.fabric.common.packets.s2c.*;
import dev.cammiescorner.fabric.common.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BooleanSupplier;

@Environment(EnvType.CLIENT)
public class FabricClient implements ClientModInitializer {
	private static final ResourceLocation HUD_ELEMENTS = FabricMain.id("textures/gui/hud/mana_bar.png");
	private static final ResourceLocation STUN_OVERLAY = FabricMain.id("textures/gui/hud/stunned_vignette.png");
	private static final ResourceLocation MAGIC_CIRCLES = FabricMain.id("textures/entity/feature/magic_circles.png");
	public static final ResourceLocation WHITE = new ResourceLocation("textures/misc/white.png");
	public static final RenderType LAYER = FabricClient.getMagicCircles(FabricMain.id("textures/block/warded_block.png"));
	public static BooleanSupplier FIRST_PERSON_MODEL_ENABLED = () -> false;
	public static BooleanSupplier FIRST_PERSON_SHOW_HANDS = () -> true;
	public static boolean castingSpeedHasCoolDown;
	private final Minecraft client = Minecraft.getInstance();
	private static int hudTimer;
	private static int hitTimer;

	@Override
	public void onInitializeClient() {
		ArcanusCompat.FIRST_PERSON.ifEnabled(() -> FirstPersonCompat::init);

		MenuScreens.register(ArcanusScreenHandlers.SPELLCRAFT_SCREEN_HANDLER.get(), SpellcraftScreen::new);
		MenuScreens.register(ArcanusScreenHandlers.SPELL_BOOK_SCREEN_HANDLER.get(), SpellBookScreen::new);
		MenuScreens.register(ArcanusScreenHandlers.ARCANE_WORKBENCH_SCREEN_HANDLER.get(), ArcaneWorkbenchScreen::new);

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
		EntityRendererRegistry.register(ArcanusEntities.NECRO_SKELETON.get(), SkeletonRenderer::new);
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

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ArcanusBlocks.MAGIC_DOOR.get(), ArcanusBlocks.ARCANE_WORKBENCH.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(), ArcanusBlocks.SPATIAL_RIFT_EXIT_EDGE.get());
		BlockEntityRenderers.register(ArcanusBlockEntities.MAGIC_BLOCK.get(), MagicBlockEntityRenderer.factory(ArcanusHelper::getMagicColor));
		BlockEntityRenderers.register(ArcanusBlockEntities.SPATIAL_RIFT_EXIT.get(), SpatialRiftExitBlockEntityRenderer::new);
		BlockEntityRenderers.register(ArcanusBlockEntities.SPATIAL_RIFT_WALL.get(), MagicBlockEntityRenderer.factory(ArcanusHelper::getPocketDimensionColor));

		ClientPlayNetworking.registerGlobalReceiver(SyncStatusEffectPacket.ID, SyncStatusEffectPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncWorkbenchModePacket.ID, SyncWorkbenchModePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncStaffTemplatePacket.ID, SyncStaffTemplatePacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncConfigValuesPacket.ID, SyncConfigValuesPacket::handle);
		ClientPlayNetworking.registerGlobalReceiver(SyncExplosionParticlesPacket.ID, SyncExplosionParticlesPacket::handle);
		ArcanusCompat.PEHKUI.ifEnabled(() -> () -> ClientPlayNetworking.registerGlobalReceiver(SyncScalePacket.ID, SyncScalePacket::handle));

		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> switch(tintIndex) {
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

		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? ((DyeableLeatherItem) stack.getItem()).getColor(stack) : -1,
				ArcanusItems.WIZARD_HAT.get(),
				ArcanusItems.WIZARD_ROBES.get(),
				ArcanusItems.WIZARD_PANTS.get(),
				ArcanusItems.WIZARD_BOOTS.get()
		);

		ItemProperties.register(ArcanusItems.BATTLE_MAGE_HELMET.get(), FabricMain.id("oxidation"), (stack, world, entity, seed) -> BattleMageArmorItem.getOxidation(stack).ordinal() / 10f);
		ItemProperties.register(ArcanusItems.BATTLE_MAGE_CHESTPLATE.get(), FabricMain.id("oxidation"), (stack, world, entity, seed) -> BattleMageArmorItem.getOxidation(stack).ordinal() / 10f);
		ItemProperties.register(ArcanusItems.BATTLE_MAGE_LEGGINGS.get(), FabricMain.id("oxidation"), (stack, world, entity, seed) -> BattleMageArmorItem.getOxidation(stack).ordinal() / 10f);
		ItemProperties.register(ArcanusItems.BATTLE_MAGE_BOOTS.get(), FabricMain.id("oxidation"), (stack, world, entity, seed) -> BattleMageArmorItem.getOxidation(stack).ordinal() / 10f);

		ArcanusItems.ITEMS.stream().forEach(holder -> {
			if(holder.get() instanceof StaffItem item) {
				ResourceLocation id = holder.getId();
				StaffItemRenderer staffItemRenderer = new StaffItemRenderer(id);
				ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(staffItemRenderer);
				BuiltinItemRendererRegistry.INSTANCE.register(item, staffItemRenderer);
				ModelLoadingPlugin.register(ctx -> ctx.addModels(new ModelResourceLocation(id.withSuffix("_gui"), "inventory"), new ModelResourceLocation(id.withSuffix("_handheld"), "inventory")));
			}
		});

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			if(!context.camera().isDetached() && !FIRST_PERSON_MODEL_ENABLED.getAsBoolean())
				renderFirstPersonBolt(context);
		});

		WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, outlineContext) -> {
			MultiPlayerGameMode interactionManager = client.gameMode;
			LocalPlayer player = client.player;
			ClientLevel world = context.world();
			PoseStack matrices = context.matrixStack();
			MultiBufferSource vertices = context.consumers();
			Vec3 cameraPos = context.camera().getPosition();

			if(player != null && vertices != null && interactionManager != null) {
				if(client.hitResult instanceof BlockHitResult hitResult && ArcanusComponents.isBlockWarded(world, hitResult.getBlockPos()) && player.swinging) {
					if(client.options.keyAttack.isDown() && player.attackAnim == 0) {
						hitTimer = 20;
					}

					if(!ArcanusComponents.isOwnerOfBlock(player, hitResult.getBlockPos()) || hitTimer > 0) {
						BlockPos blockPos = hitResult.getBlockPos();
						float alpha = Mth.clamp(hitTimer / 20f, 0.0F, 1.0F);

						renderWardedBlock(matrices, vertices, world, cameraPos, blockPos, alpha);
						player.displayClientMessage(Component.translatable("text.arcanus.block_is_warded").withStyle(ChatFormatting.RED), true);
					}

					if(hitTimer > 0) {
						hitTimer -= 1;
					}
				}

				if(player.getMainHandItem().is(ArcanusItemTags.STAVES) || player.getOffhandItem().is(ArcanusItemTags.STAVES)) {
					AtomicReferenceArray<LevelChunk> chunks = context.world().getChunkSource().storage.chunks;
					float alpha = Mth.sin(world.getGameTime() * 0.06f) * 0.4f + 0.6f;

					for(int i = 0; i < chunks.length(); i++) {
						ChunkAccess chunk = chunks.get(i);

						if(chunk != null) {
							ArcanusComponents.getWardedBlocks(chunk).forEach((blockPos, uuid) -> {
								if(blockPos.distSqr(context.camera().getBlockPosition()) < 256)
									renderWardedBlock(matrices, vertices, world, cameraPos, blockPos, alpha);
							});
						}
					}
				}
			}

			return true;
		});

		HudRenderCallback.EVENT.register((gui, tickDelta) -> {
			PoseStack matrices = gui.pose();
			Player player = client.player;

			if(player != null && !player.isSpectator()) {
				int stunTimer = ArcanusComponents.getStunTimer(player);

				if(stunTimer > 0) {
					if(stunTimer > 5)
						renderOverlay(STUN_OVERLAY, Math.min(1F, 0.5F + (stunTimer % 5F) / 10F));
					else
						renderOverlay(STUN_OVERLAY, Math.min(1F, stunTimer / 5F));
				}

				if(!client.gameRenderer.getMainCamera().isDetached() && !FIRST_PERSON_MODEL_ENABLED.getAsBoolean()) {
					List<Pattern> list = ArcanusComponents.getPattern(player);

					if(!list.isEmpty()) {
						MultiBufferSource.BufferSource vertices = client.renderBuffers().bufferSource();
						RenderType renderLayer = getMagicCircles(MAGIC_CIRCLES);
						VertexConsumer vertex = vertices.getBuffer(renderLayer);
						Color color = ArcanusHelper.getMagicColor(player);
						float x = client.getWindow().getGuiScaledWidth() / 2F;
						float y = client.getWindow().getGuiScaledHeight() / 2F;
						float scale = 3F;

						matrices.pushPose();
						matrices.translate(x, y, 0);

						for(int i = 0; i < list.size(); i++) {
							Pattern pattern = list.get(i);
							matrices.pushPose();

							if(i == 1)
								matrices.mulPose(Axis.ZP.rotationDegrees((player.tickCount + player.getId() + tickDelta) * (5 + (2.5F * i))));
							else
								matrices.mulPose(Axis.ZN.rotationDegrees((player.tickCount + player.getId() + tickDelta) * (5 + (2.5F * i))));

							matrices.scale(scale, scale, 0);
							matrices.translate(-8.5, -8.5, 0);
							drawTexture(vertex, matrices, color, 0, 0, i * 34, pattern == Pattern.LEFT ? 0 : 24, 17, 17, 128, 48);
							matrices.popPose();
						}

						matrices.popPose();
						vertices.endLastBatch();
					}
				}

				double maxMana = ArcanusComponents.getMaxMana(player);
				double mana = ArcanusComponents.getMana(player);
				double burnout = ArcanusComponents.getBurnout(player);
				double manaLock = ArcanusComponents.getManaLock(player);

				if(player.getMainHandItem().getItem() instanceof StaffItem)
					hudTimer = Math.min(hudTimer + 1, 40);
				else
					hudTimer = Math.max(hudTimer - 1, 0);

				if(hudTimer > 0) {
					int x = 0;
					int y = client.getWindow().getGuiScaledHeight() - 28;
					int width = 96;
					float alpha = hudTimer > 20 ? 1F : hudTimer / 20F;

					RenderSystem.enableBlend();
					RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

					// render frame
					gui.blit(HUD_ELEMENTS, x, y, 0, 0, 101, 28, 256, 256);

					// render mana
					gui.blit(HUD_ELEMENTS, x, y + 5, 0, 32, (int) (width * (mana / maxMana)), 23, 256, 256);

					// render burnout
					int i = (int) Math.ceil(width * ((burnout + manaLock) / maxMana));
					gui.blit(HUD_ELEMENTS, x + (width - i), y + 5, width - i, 56, i, 23, 256, 256);

					// render mana lock
					i = (int) Math.ceil(width * (manaLock / maxMana));
					gui.blit(HUD_ELEMENTS, x + (width - i), y + 5, width - i, 80, i, 23, 256, 256);

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

	public static void renderBolts(LivingEntity entity, Vec3 startPos, PoseStack matrices, MultiBufferSource vertices) {
		if(ArcanusComponents.shouldRenderBolt(entity)) {
			VertexConsumer vertex = vertices.getBuffer(getMagicCircles(WHITE));
			RandomSource random = RandomSource.create((entity.tickCount + entity.getId()) / 2);
			Vec3 endPos = ArcanusComponents.getBoltPos(entity);

			var color = ArcanusHelper.getMagicColor(entity);

			int steps = (int) (startPos.distanceTo(endPos) * 5);

			renderBolt(matrices, vertex, random, startPos, endPos, steps, 0, true, color.redF(), color.greenF(), color.blueF(), OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
		}
	}

	private static void renderBolt(PoseStack matrices, VertexConsumer vertex, RandomSource random, Vec3 startPos, Vec3 endPos, int steps, int currentStep, boolean recurse, float r, float g, float b, int overlay, int light) {
		Vec3 direction = endPos.subtract(startPos);
		Vec3 lastPos = startPos;
		Matrix4f modelMatrix = matrices.last().pose();
		Matrix3f normalMatrix = matrices.last().normal();

		for(int i = currentStep; i < steps; i++) {
			Vec3 randomOffset = new Vec3(random.nextGaussian(), random.nextIntBetweenInclusive(-1 / (steps * 2), 1 / (steps * 2)), random.nextGaussian());
			Vec3 nextPos = startPos.add(direction.scale((i + 1) / (float) steps)).add(randomOffset.scale(1 / 12F));

			for(int j = 0; j < 4; j++) {
				Vec3 vert1 = switch(j) {
					case 0 -> lastPos.add(0.025, 0.025, 0);
					case 1 -> lastPos.add(-0.025, 0.025, 0);
					case 2 -> lastPos.add(-0.025, -0.025, 0);
					case 3 -> lastPos.add(0.025, -0.025, 0);
					default -> lastPos;
				};
				Vec3 vert2 = switch(j) {
					case 0 -> lastPos.add(-0.025, 0.025, 0);
					case 1 -> lastPos.add(-0.025, -0.025, 0);
					case 2 -> lastPos.add(0.025, -0.025, 0);
					case 3 -> lastPos.add(0.025, 0.025, 0);
					default -> lastPos;
				};
				Vec3 vert3 = switch(j) {
					case 0 -> nextPos.add(0.025, 0.025, 0);
					case 1 -> nextPos.add(-0.025, 0.025, 0);
					case 2 -> nextPos.add(-0.025, -0.025, 0);
					case 3 -> nextPos.add(0.025, -0.025, 0);
					default -> nextPos;
				};
				Vec3 vert4 = switch(j) {
					case 0 -> nextPos.add(-0.025, 0.025, 0);
					case 1 -> nextPos.add(-0.025, -0.025, 0);
					case 2 -> nextPos.add(0.025, -0.025, 0);
					case 3 -> nextPos.add(0.025, 0.025, 0);
					default -> nextPos;
				};
				Vec3 normal = vert2.subtract(vert1).cross(vert3.subtract(vert1));

				vertex.vertex(modelMatrix, (float) vert2.x(), (float) vert2.y(), (float) vert2.z()).color(r, g, b, 0.6F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normalMatrix, (float) normal.x(), (float) normal.y(), (float) normal.z()).endVertex();
				vertex.vertex(modelMatrix, (float) vert4.x(), (float) vert4.y(), (float) vert4.z()).color(r, g, b, 0.6F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normalMatrix, (float) normal.x(), (float) normal.y(), (float) normal.z()).endVertex();
				vertex.vertex(modelMatrix, (float) vert3.x(), (float) vert3.y(), (float) vert3.z()).color(r, g, b, 0.6F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normalMatrix, (float) normal.x(), (float) normal.y(), (float) normal.z()).endVertex();
				vertex.vertex(modelMatrix, (float) vert1.x(), (float) vert1.y(), (float) vert1.z()).color(r, g, b, 0.6F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normalMatrix, (float) normal.x(), (float) normal.y(), (float) normal.z()).endVertex();
			}

			while(recurse && random.nextFloat() < 0.2F) {
				Vec3 randomOffset1 = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
				renderBolt(matrices, vertex, random, lastPos, endPos.add(randomOffset1.scale(Math.min(random.nextFloat(), 0.6F))), steps, i + 1, false, r, g, b, overlay, light);
			}

			lastPos = nextPos;
		}
	}

	private static void renderWardedBlock(PoseStack matrices, MultiBufferSource vertices, Level world, Vec3 cameraPos, BlockPos blockPos, float alpha) {
		VertexConsumer consumer = vertices.getBuffer(LAYER);
		Vec3 pos = Vec3.atCenterOf(blockPos);

		matrices.pushPose();
		matrices.translate(pos.x() - cameraPos.x(), pos.y() - cameraPos.y(), pos.z() - cameraPos.z());
		matrices.scale(1.001f, 1.001f, 1.001f);
		matrices.translate(-0.5, -0.5, -0.5);

		Matrix4f matrix4f = matrices.last().pose();
		Matrix3f matrix3f = matrices.last().normal();
		int light = world.getMaxLocalRawBrightness(blockPos);
		int overlay = OverlayTexture.NO_OVERLAY;
		Color color = ArcanusHelper.getMagicColor(ArcanusComponents.getWardedBlocks(world.getChunk(blockPos)).get(blockPos));
		float r = Mth.clamp(color.redF() * alpha, 0.0F, 1.0F);
		float g = Mth.clamp(color.greenF() * alpha, 0.0F, 1.0F);
		float b = Mth.clamp(color.blueF() * alpha, 0.0F, 1.0F);

		color = Color.fromFloatsRGB(r, g, b);

		for(Direction direction : Direction.values()) {
			BlockPos posToSide = blockPos.relative(direction);
			BlockState stateToSide = world.getBlockState(posToSide);

			if(stateToSide.isFaceSturdy(world, posToSide, direction.getOpposite(), SupportType.FULL) || ArcanusComponents.isBlockWarded(world, posToSide))
				continue;

			switch(direction) {
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

		matrices.popPose();
	}

	private static void renderFirstPersonBolt(WorldRenderContext context) {
		LocalPlayer player = context.gameRenderer().getMinecraft().player;
		if(player != null) {
			PoseStack matrices = context.matrixStack();
			Vec3 camPos = context.camera().getPosition();
			float tickDelta = context.tickDelta();
			Vec3 startPos = player.getPosition(tickDelta).add(0, player.getEyeHeight(player.getPose()), 0);

			matrices.pushPose();
			matrices.translate(-camPos.x(), -camPos.y(), -camPos.z());
			renderBolts(player, startPos.add(0, -0.1, 0), matrices, context.consumers());
			matrices.popPose();
		}
	}

	public static RenderType getMagicCircles(ResourceLocation texture) {
		return RenderType.create(FabricMain.id("magic_circle").toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setOverlayState(RenderStateShard.OVERLAY).setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY).setWriteMaskState(RenderType.COLOR_DEPTH_WRITE).setCullState(RenderStateShard.NO_CULL).createCompositeState(false));
	}

	public static RenderType getMagicPortal(ResourceLocation texture) {
		return RenderType.create(FabricMain.id("magic_portal").toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setOverlayState(RenderStateShard.OVERLAY).setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY).setWriteMaskState(RenderType.COLOR_DEPTH_WRITE).setCullState(RenderStateShard.NO_CULL).setDepthTestState(RenderType.NO_DEPTH_TEST).createCompositeState(false));
	}

	public static RenderType getMagicCirclesTri(ResourceLocation texture) {
		return RenderType.create(FabricMain.id("magic_circle_tri").toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256, false, true, RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setOverlayState(RenderStateShard.OVERLAY).setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY).setWriteMaskState(RenderType.COLOR_DEPTH_WRITE).setCullState(RenderStateShard.NO_CULL).createCompositeState(false));
	}

	public static void drawTexture(VertexConsumer vertex, PoseStack matrices, Color color, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		drawTexturedQuad(vertex, matrices.last().pose(), color, x, x + width, y, y + height, u / (float) textureWidth, (u + width) / (float) textureWidth, v / (float) textureHeight, (v + height) / (float) textureHeight);
	}

	private static void drawTexturedQuad(VertexConsumer vertex, Matrix4f matrix, Color color, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1) {
		vertex.vertex(matrix, x0, y1, 0).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(0, 0, 1).endVertex();
		vertex.vertex(matrix, x1, y1, 0).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(0, 0, 1).endVertex();
		vertex.vertex(matrix, x1, y0, 0).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(0, 0, 1).endVertex();
		vertex.vertex(matrix, x0, y0, 0).color(color.red(), color.green(), color.blue(), color.alpha()).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(0, 0, 1).endVertex();
	}

	public static void renderSide(Matrix4f matrix4f, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Color color, int light, int overlay, Matrix3f normal, Direction direction) {
		vertices.vertex(matrix4f, x1, y1, z1).color(color.red(), color.green(), color.blue(), color.alpha()).uv(0, 1).overlayCoords(overlay).uv2(light).normal(normal, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()).endVertex();
		vertices.vertex(matrix4f, x2, y1, z2).color(color.red(), color.green(), color.blue(), color.alpha()).uv(1, 1).overlayCoords(overlay).uv2(light).normal(normal, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()).endVertex();
		vertices.vertex(matrix4f, x2, y2, z3).color(color.red(), color.green(), color.blue(), color.alpha()).uv(1, 0).overlayCoords(overlay).uv2(light).normal(normal, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()).endVertex();
		vertices.vertex(matrix4f, x1, y2, z4).color(color.red(), color.green(), color.blue(), color.alpha()).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normal, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()).endVertex();
	}

	private void renderOverlay(ResourceLocation texture, float opacity) {
		Minecraft client = Minecraft.getInstance();
		double scaledHeight = client.getWindow().getGuiScaledHeight();
		double scaledWidth = client.getWindow().getGuiScaledWidth();

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
		RenderSystem.setShaderTexture(0, texture);
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(0.0, scaledHeight, -90.0).uv(0.0F, 1.0F).endVertex();
		bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0).uv(1.0F, 1.0F).endVertex();
		bufferBuilder.vertex(scaledWidth, 0.0, -90.0).uv(1.0F, 0.0F).endVertex();
		bufferBuilder.vertex(0.0, 0.0, -90.0).uv(0.0F, 0.0F).endVertex();
		tessellator.end();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
