package dev.cammiescorner.arcanuscontinuum.client.gui.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.*;
import dev.cammiescorner.arcanuscontinuum.api.util.Rectangle;
import dev.cammiescorner.arcanuscontinuum.client.gui.util.Action;
import dev.cammiescorner.arcanuscontinuum.client.gui.util.UndoRedoStack;
import dev.cammiescorner.arcanuscontinuum.client.gui.widgets.SpellComponentWidget;
import dev.cammiescorner.arcanuscontinuum.client.gui.widgets.UndoRedoButtonWidget;
import dev.cammiescorner.arcanuscontinuum.common.items.SpellBookItem;
import dev.cammiescorner.arcanuscontinuum.common.packets.c2s.SaveBookDataPacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanuscontinuum.common.screens.SpellcraftScreenHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SpellcraftScreen extends AbstractContainerScreen<SpellcraftScreenHandler> {
	public static final ResourceLocation BOOK_TEXTURE = Arcanus.id("textures/gui/spell_book.png");
	public static final ResourceLocation PANEL_TEXTURE = Arcanus.id("textures/gui/spell_crafting.png");
	private static final Vector4i VALID_BOUNDS = new Vector4i(30, 40, 197, 114);
	private static List<SpellComponent> spellShapes;
	private static List<SpellComponent> spellEffects;
	private final LinkedList<SpellGroup> spellGroups = new LinkedList<>();
	private final List<SpellComponentWidget> spellShapeWidgets = Lists.newArrayList();
	private final List<SpellComponentWidget> spellEffectWidgets = Lists.newArrayList();
	private final UndoRedoStack undoRedoStack = new UndoRedoStack();
	private SpellComponent draggedComponent = ArcanusSpellComponents.EMPTY.get();
	private EditBox textBox;
	private int leftScroll, rightScroll;
	private double leftKnobPos, rightKnobPos;
	private boolean draggingLeft, draggingRight;

	public SpellcraftScreen(SpellcraftScreenHandler screenHandler, Inventory playerInventory, Component text) {
		super(screenHandler, playerInventory, Component.empty());
	}

	@Override
	protected void init() {
		super.init();
		leftPos = (width - 256) / 2;
		topPos = (height - 180) / 2;
		inventoryLabelY = -10000;
		spellShapes = Arcanus.SPELL_COMPONENTS.stream().filter(component -> !ArcanusSpellComponents.EMPTY.is(component) && component.isEnabled() && ArcanusComponents.getWizardLevel(Minecraft.getInstance().player) >= component.getMinLevel() && component instanceof SpellShape).toList();
		spellEffects = Arcanus.SPELL_COMPONENTS.stream().filter(component -> !ArcanusSpellComponents.EMPTY.is(component) && component.isEnabled() && ArcanusComponents.getWizardLevel(Minecraft.getInstance().player) >= component.getMinLevel() && component instanceof SpellEffect).toList();

		if(minecraft != null) {
			for(SpellComponent component : spellShapes)
				if(ArcanusComponents.getWizardLevel(minecraft.player) >= component.getMinLevel())
					addSpellShapeChild(new SpellComponentWidget(-35, component, widget -> {
						if(spellComponentCount() < ArcanusComponents.maxSpellSize(minecraft.player))
							draggedComponent = widget.getSpellComponent();
					}));
			for(SpellComponent component : spellEffects)
				if(ArcanusComponents.getWizardLevel(minecraft.player) >= component.getMinLevel())
					addSpellEffectChild(new SpellComponentWidget(267, component, widget -> {
						if(spellComponentCount() < ArcanusComponents.maxSpellSize(minecraft.player))
							draggedComponent = widget.getSpellComponent();
					}));
		}

		addCloseButtons();
		textBox = addRenderableWidget(new EditBox(minecraft.font, leftPos + 15, topPos + 8, 88, 14, Component.empty()));
		textBox.setValue(SpellBookItem.getSpell(getMenu().getSpellBook()).getName());
		addRenderableWidget(new UndoRedoButtonWidget((width - 48) / 2, topPos - 8, true, undoRedoStack, button -> undoRedoStack.undo()));
		addRenderableWidget(new UndoRedoButtonWidget(width / 2, topPos - 8, false, undoRedoStack, button -> undoRedoStack.redo()));

		for(SpellGroup group : SpellBookItem.getSpell(getMenu().getSpellBook()).getComponentGroups()) {
			if(!group.isEmpty()) {
				undoRedoStack.addAction(new Action(group.shape(), group.positions().get(0), () -> spellGroups.add(group), () -> spellGroups.remove(group))).Do().run();

				for(int i = 0; i < group.effects().size(); i++) {
					SpellEffect effect = group.effects().get(i);
					Vector2i pos = group.positions().get(i + 1);

					undoRedoStack.addAction(new Action(effect, pos, () -> {
						group.effects().add(effect);
						group.positions().add(pos);
					}, () -> {
						group.effects().remove(effect);
						group.positions().remove(pos);
					}));
				}
			}
		}
	}

	@Override
	protected void renderBg(GuiGraphics gui, float delta, int mouseX, int mouseY) {
		this.renderBackground(gui);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		gui.blit(BOOK_TEXTURE, leftPos, topPos, 0, 0, 256, 180, 256, 256);

		RenderSystem.setShaderTexture(0, PANEL_TEXTURE);
		gui.blit(PANEL_TEXTURE, leftPos - 62, topPos + 1, 0, 0, 380, 178, 384, 256);
	}

	@Override
	protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
		Rectangle leftKnob = getLeftScrollKnob();
		Rectangle rightKnob = getRightScrollKnob();

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShaderTexture(0, PANEL_TEXTURE);

		gui.blit(PANEL_TEXTURE, leftKnob.x(), leftKnob.y(), draggingLeft ? 12 : 0, 184, leftKnob.width(), leftKnob.height(), 384, 256);
		gui.blit(PANEL_TEXTURE, rightKnob.x(), rightKnob.y(), draggingRight ? 12 : 0, 184, rightKnob.width(), rightKnob.height(), 384, 256);

		drawWidgets(gui, mouseX, mouseY, Minecraft.getInstance().getFrameTime());

		super.renderLabels(gui, mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if(isHovering(-62, 1, 58, 178, mouseX, mouseY)) {
			if(leftScroll > 0 && amount > 0)
				leftScroll--;
			if(leftScroll < spellShapes.size() * 2 - 12 && amount < 0)
				leftScroll++;

			leftKnobPos = leftScroll * (148F / (spellShapes.size() * 2 - 12));
		}

		if(isHovering(260, 1, 58, 178, mouseX, mouseY)) {
			if(rightScroll > 0 && amount > 0)
				rightScroll--;
			if(rightScroll < spellEffects.size() * 2 - 12 && amount < 0)
				rightScroll++;

			rightKnobPos = rightScroll * (148F / (spellEffects.size() * 2 - 12));
		}

		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if(draggingLeft) {
			leftKnobPos = Mth.clamp(mouseY - topPos - 16, 0, 148);
			leftScroll = (int) (leftKnobPos / 148 * (spellShapes.size() * 2 - 12));
		}
		else if(draggingRight) {
			rightKnobPos = Mth.clamp(mouseY - topPos - 16, 0, 148);
			rightScroll = (int) (rightKnobPos / 148 * (spellEffects.size() * 2 - 12));
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(button == 0) {
			for(SpellComponentWidget widget : spellShapeWidgets)
				if(isHovering(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), mouseX, mouseY))
					widget.onClick(mouseX, mouseY);
			for(SpellComponentWidget widget : spellEffectWidgets)
				if(isHovering(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), mouseX, mouseY))
					widget.onClick(mouseX, mouseY);

			if(isHovering(-58, 5, 12, 170, mouseX, mouseY)) {
				draggingLeft = true;
				leftKnobPos = Mth.clamp(mouseY - topPos - 16, 0, 148);
				leftScroll = (int) (leftKnobPos / 148 * (spellShapes.size() * 2 - 12));
			}
			else if(isHovering(302, 5, 12, 170, mouseX, mouseY)) {
				draggingRight = true;
				rightKnobPos = Mth.clamp(mouseY - topPos - 16, 0, 148);
				rightScroll = (int) (rightKnobPos / 148 * (spellEffects.size() * 2 - 12));
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(!ArcanusSpellComponents.EMPTY.is(draggedComponent) && button == 0) {
			if(isHovering(VALID_BOUNDS.x(), VALID_BOUNDS.y(), VALID_BOUNDS.z(), VALID_BOUNDS.w(), mouseX, mouseY) && !isTooCloseToComponents(mouseX, mouseY)) {
				Vector2i pos = new Vector2i((int) (mouseX - this.leftPos - 12), (int) (mouseY - this.topPos - 12));

				if(draggedComponent instanceof SpellShape shape) {
					List<Vector2i> positions = new ArrayList<>();
					positions.add(pos);
					SpellGroup group = new SpellGroup(shape, new ArrayList<>(), positions);
					Action action = undoRedoStack.addAction(new Action(draggedComponent, pos, () -> spellGroups.add(group), () -> spellGroups.remove(group)));

					action.Do().run();
				}
				if(draggedComponent instanceof SpellEffect effect && !spellGroups.isEmpty() && !ArcanusSpellComponents.EMPTY.is(spellGroups.getLast().shape())) {
					Action action = undoRedoStack.addAction(new Action(draggedComponent, pos, () -> {
						spellGroups.getLast().effects().add(effect);
						spellGroups.getLast().positions().add(pos);
					}, () -> {
						spellGroups.getLast().effects().remove(effect);
						spellGroups.getLast().positions().remove(pos);
					}));

					action.Do().run();
				}
			}

			draggedComponent = ArcanusSpellComponents.EMPTY.get();
		}

		if(draggingLeft)
			draggingLeft = false;
		if(draggingRight)
			draggingRight = false;

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if(textBox.isFocused()) {
			if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
				textBox.setFocused(false);
				return false;
			}
			if(keyCode == GLFW.GLFW_KEY_E) {
				return false;
			}
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	protected void clearWidgets() {
		super.clearWidgets();
		spellGroups.clear();
		spellShapeWidgets.clear();
		spellEffectWidgets.clear();
	}

	private void drawWidgets(GuiGraphics gui, int mouseX, int mouseY, float delta) {
		PoseStack matrices = gui.pose();
		int scale = (int) minecraft.getWindow().getGuiScale();

		// Render Spell Shapes
		RenderSystem.enableScissor((leftPos - 38) * scale, (topPos + 5) * scale, 30 * scale, 170 * scale);

		for(int i = 0; i < spellShapeWidgets.size(); i++) {
			SpellComponentWidget widget = spellShapeWidgets.get(i);
			widget.setY(8 + (i * 28) - leftScroll * 14);
			widget.render(gui, mouseX - leftPos, mouseY - topPos, delta);

			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

			if(widget.isHoveredOrFocused())
				gui.blit(PANEL_TEXTURE, widget.getX() - 3, widget.getY() - 3, 0, 208, 30, 30, 384, 256);
		}

		RenderSystem.disableScissor();

		// Render Spell Effects
		RenderSystem.enableScissor((leftPos + 264) * scale, (topPos + 5) * scale, 30 * scale, 170 * scale);

		for(int i = 0; i < spellEffectWidgets.size(); i++) {
			SpellComponentWidget widget = spellEffectWidgets.get(i);
			widget.setY(8 + (i * 28) - rightScroll * 14);
			widget.render(gui, mouseX - leftPos, mouseY - topPos, delta);

			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

			if(widget.isHoveredOrFocused())
				gui.blit(PANEL_TEXTURE, widget.getX() - 3, widget.getY() - 3, 0, 208, 30, 30, 384, 256);
		}

		RenderSystem.disableScissor();

		for(int i = 0; i < spellGroups.size(); i++) {
			SpellGroup group = spellGroups.get(i);
			List<Vector2i> positions = group.positions();
			RenderSystem.setShader(GameRenderer::getPositionShader);
			RenderSystem.setShaderColor(0.25F, 0.25F, 0.3F, 1F);
			BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();

			bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
			matrices.pushPose();
			matrices.translate(12, 12, 0);
			Matrix4f matrix = matrices.last().pose();

			for(int j = 0; j < positions.size(); j++) {
				Vector2i pos = positions.get(j);
				Vector2i prevPos = positions.get(Math.max(0, j - 1));

				if(j == 0 && i > 0 && !spellGroups.get(i - 1).isEmpty()) {
					List<Vector2i> prevPositions = spellGroups.get(i - 1).positions();
					prevPos = prevPositions.get(prevPositions.size() - 1);
				}
				if(pos.equals(prevPos))
					continue;

				int x1 = prevPos.x();
				int y1 = prevPos.y();
				int x2 = pos.x();
				int y2 = pos.y();
				float angle = (float) (Math.atan2(y2 - y1, x2 - x1) - (Math.PI * 0.5));
				float dx = Mth.cos(angle);
				float dy = Mth.sin(angle);

				bufferBuilder.vertex(matrix, x2 - dx, y2 - dy, 0).color(0).endVertex();
				bufferBuilder.vertex(matrix, x2 + dx, y2 + dy, 0).color(0).endVertex();
				bufferBuilder.vertex(matrix, x1 + dx, y1 + dy, 0).color(0).endVertex();
				bufferBuilder.vertex(matrix, x1 - dx, y1 - dy, 0).color(0).endVertex();
			}

			BufferUploader.drawWithShader(bufferBuilder.end());
			matrices.popPose();
		}

		for(SpellGroup group : spellGroups) {
			List<Vector2i> positions = group.positions();

			for(int j = 0; j < positions.size(); j++) {
				Vector2i pos = positions.get(j);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				gui.blit(PANEL_TEXTURE, pos.x - 3, pos.y - 3, 60, 208, 30, 30, 384, 256);

				RenderSystem.setShaderColor(0.25F, 0.25F, 0.3F, 1F);
				gui.blit(PANEL_TEXTURE, pos.x - 3, pos.y - 3, 30, 208, 30, 30, 384, 256);

				gui.blit(group.getAllComponents().toList().get(j).getTexture(), pos.x, pos.y, 0, 0, 24, 24, 24, 24);
			}
		}

		if(!ArcanusSpellComponents.EMPTY.is(draggedComponent)) {
			int colour = 0xff0000;

			if((isHovering(VALID_BOUNDS.x(), VALID_BOUNDS.y(), VALID_BOUNDS.z(), VALID_BOUNDS.w(), mouseX, mouseY) && !isTooCloseToComponents(mouseX, mouseY)) && (!(draggedComponent instanceof SpellEffect) || (!spellGroups.isEmpty() && !spellGroups.getLast().isEmpty())))
				colour = 0x00ff00;

			float r = (colour >> 16 & 255) / 255F;
			float g = (colour >> 8 & 255) / 255F;
			float b = (colour & 255) / 255F;

			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			gui.blit(PANEL_TEXTURE, mouseX - leftPos - 15, mouseY - topPos - 15, 60, 208, 30, 30, 384, 256);

			RenderSystem.setShaderColor(r, g, b, 1F);
			gui.blit(PANEL_TEXTURE, mouseX - leftPos - 15, mouseY - topPos - 15, 30, 208, 30, 30, 384, 256);

			RenderSystem.setShaderColor(r, g, b, 1F);
			gui.blit(draggedComponent.getTexture(), mouseX - leftPos - 12, mouseY - topPos - 12, 0, 0, 24, 24, 24, 24);
		}

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		int componentCount = spellComponentCount();
		int maxComponents = ArcanusComponents.maxSpellSize(minecraft.player);
		int componentCounterColour = 0x5555ff;

		if(componentCount >= maxComponents)
			componentCounterColour = 0xcc2222;

		String spellComponentCount = String.valueOf(componentCount);
		String maxSpellComponentCount = String.valueOf(maxComponents);

		gui.drawString(font, spellComponentCount, 118 - font.width(spellComponentCount) / 2, 11, componentCounterColour, false);
		gui.drawString(font, " / ", 128 - font.width(" / ") / 2, 11, 0x555555, false);
		gui.drawString(font, maxSpellComponentCount, 138 - font.width(maxSpellComponentCount) / 2, 11, componentCounterColour, false);

		MutableComponent weight = Component.translatable("spell_book.arcanuscontinuum.weight." + getWeight().toString().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.DARK_GREEN);
		MutableComponent mana = Component.literal(Arcanus.format(getManaCost())).withStyle(ChatFormatting.BLUE);
		MutableComponent coolDown = Component.literal(Arcanus.format(getCoolDown() / 20D)).append(Component.translatable("spell_book.arcanuscontinuum.seconds")).withStyle(ChatFormatting.RED);

		gui.drawString(font, weight, 240 - font.width(weight), 7, 0xffffff, false);
		gui.drawString(font, mana, 240 - font.width(mana), 17, 0xffffff, false);
		gui.drawString(font, coolDown, 240 - font.width(coolDown), 27, 0xffffff, false);

		if(isHovering(109, 8, font.width("12 / 12"), font.lineHeight + 4, mouseX, mouseY))
			gui.renderTooltip(font, Component.translatable("screen.arcanuscontinuum.tooltip.component_count"), mouseX - leftPos, mouseY - topPos);

		for(SpellComponentWidget widget : spellShapeWidgets)
			if(widget.isHoveredOrFocused())
				widget.renderTooltip(gui, mouseX - leftPos, mouseY - topPos);

		for(SpellComponentWidget widget : spellEffectWidgets)
			if(widget.isHoveredOrFocused())
				widget.renderTooltip(gui, mouseX - leftPos, mouseY - topPos);

		for(SpellGroup group : spellGroups) {
			for(int i = 0; i < group.positions().size(); i++) {
				Vector2i position = group.positions().get(i);

				if(isHovering(position.x() - 2, position.y() - 2, 28, 28, mouseX, mouseY)) {
					List<Component> textList = new ArrayList<>();
					SpellComponent component = group.getAllComponents().toList().get(i);

					textList.add(component.getName());
					textList.add(Component.translatable("spell_book.arcanuscontinuum.weight").append(": ").withStyle(ChatFormatting.GREEN).append(Component.translatable("spell_book.arcanuscontinuum.weight", component.getWeight().toString().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY)));
					textList.add(Component.translatable("spell_book.arcanuscontinuum.mana_cost").append(": ").withStyle(ChatFormatting.BLUE).append(Component.literal(component.getManaCostAsString()).withStyle(ChatFormatting.GRAY)));

					if(component instanceof SpellShape shape) {
						if(shape.getManaMultiplier() != 0)
							textList.add(Component.translatable("spell_book.arcanuscontinuum.mana_multiplier").append(": ").withStyle(ChatFormatting.LIGHT_PURPLE).append(Component.literal(shape.getManaMultiplierAsString()).withStyle(ChatFormatting.GRAY)));
						if(shape.getPotencyModifier() != 0)
							textList.add(Component.translatable("spell_book.arcanuscontinuum.potency_modifier").append(": ").withStyle(ChatFormatting.YELLOW).append(Component.literal(shape.getPotencyModifierAsString()).withStyle(ChatFormatting.GRAY)));
					}

					textList.add(Component.translatable("spell_book.arcanuscontinuum.cool_down").append(": ").withStyle(ChatFormatting.RED).append(Component.literal(component.getCoolDownAsString()).append(Component.translatable("spell_book.arcanuscontinuum.seconds")).withStyle(ChatFormatting.GRAY)));

					gui.renderComponentTooltip(font, textList, mouseX - leftPos, mouseY - topPos);
				}
			}
		}
	}

	protected void addCloseButtons() {
		addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
			SaveBookDataPacket.send(getMenu().getPos(), getSpell());
			onClose();
		}).pos(width / 2 - 100, topPos + 170).size(98, 20).build());

		addRenderableWidget(Button.builder(Component.translatable("lectern.take_book"), (button) -> {
			SaveBookDataPacket.send(getMenu().getPos(), getSpell());
			minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
			onClose();
		}).pos(width / 2 + 2, topPos + 170).size(98, 20).build());
	}

	public <T extends SpellComponentWidget> T addSpellShapeChild(T drawable) {
		spellShapeWidgets.add(drawable);
		return drawable;
	}

	public <T extends SpellComponentWidget> T addSpellEffectChild(T drawable) {
		spellEffectWidgets.add(drawable);
		return drawable;
	}

	public Rectangle getLeftScrollKnob() {
		return new Rectangle(-58, (int) (5 + leftKnobPos), 12, 22);
	}

	public Rectangle getRightScrollKnob() {
		return new Rectangle(302, (int) (5 + rightKnobPos), 12, 22);
	}

	public boolean isTooCloseToComponents(double mouseX, double mouseY) {
		return distanceToNearestComponent(mouseX, mouseY) < 40;
	}

	public double distanceToNearestComponent(double mouseX, double mouseY) {
		return spellGroups.stream().mapToDouble(spellGroup -> spellGroup.positions().stream().mapToDouble(position -> position.distance((int) (mouseX - leftPos - 12), (int) (mouseY - topPos - 12))).min().orElse(Double.MAX_VALUE)).min().orElse(Double.MAX_VALUE);
	}

	public int spellComponentCount() {
		int count = 0;

		for(SpellGroup group : spellGroups)
			if(!group.isEmpty())
				count += group.getAllComponents().toList().size();

		return count;
	}

	public Spell getSpell() {
		if(spellGroups.isEmpty())
			return new Spell();

		if(spellGroups.get(0).isEmpty() && spellGroups.size() > 1 && !spellGroups.get(1).isEmpty())
			spellGroups.remove(0);

		return new Spell(spellGroups, textBox.getValue().isBlank() ? "Empty" : textBox.getValue());
	}

	public Weight getWeight() {
		return getSpell().getWeight();
	}

	public double getManaCost() {
		return getSpell().getManaCost();
	}

	public int getCoolDown() {
		return getSpell().getCoolDown();
	}
}
