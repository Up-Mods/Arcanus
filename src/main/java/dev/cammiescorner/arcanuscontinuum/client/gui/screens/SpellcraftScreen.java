package dev.cammiescorner.arcanuscontinuum.client.gui.screens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.util.Rectangle;
import dev.cammiescorner.arcanuscontinuum.api.spells.*;
import dev.cammiescorner.arcanuscontinuum.client.gui.util.Action;
import dev.cammiescorner.arcanuscontinuum.client.gui.util.UndoRedoStack;
import dev.cammiescorner.arcanuscontinuum.client.gui.widgets.SpellComponentWidget;
import dev.cammiescorner.arcanuscontinuum.client.gui.widgets.UndoRedoButtonWidget;
import dev.cammiescorner.arcanuscontinuum.common.items.SpellBookItem;
import dev.cammiescorner.arcanuscontinuum.common.packets.c2s.SaveBookDataPacket;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanuscontinuum.common.registry.ArcanusSpellComponents;
import dev.cammiescorner.arcanuscontinuum.common.screens.SpellcraftScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.CommonTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SpellcraftScreen extends HandledScreen<SpellcraftScreenHandler> {
	public static final Identifier BOOK_TEXTURE = Arcanus.id("textures/gui/spell_book.png");
	public static final Identifier PANEL_TEXTURE = Arcanus.id("textures/gui/spell_crafting.png");
	private static final Vector4i VALID_BOUNDS = new Vector4i(30, 40, 197, 114);
	private static List<SpellComponent> spellShapes;
	private static List<SpellComponent> spellEffects;
	private final LinkedList<SpellGroup> spellGroups = new LinkedList<>();
	private final List<SpellComponentWidget> spellShapeWidgets = Lists.newArrayList();
	private final List<SpellComponentWidget> spellEffectWidgets = Lists.newArrayList();
	private final UndoRedoStack undoRedoStack = new UndoRedoStack();
	private SpellComponent draggedComponent = ArcanusSpellComponents.EMPTY.get();
	private TextFieldWidget textBox;
	private int leftScroll, rightScroll;
	private double leftKnobPos, rightKnobPos;
	private boolean draggingLeft, draggingRight;

	public SpellcraftScreen(SpellcraftScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, Text.empty());
	}

	@Override
	protected void init() {
		super.init();
		x = (width - 256) / 2;
		y = (height - 180) / 2;
		playerInventoryTitleY = -10000;
		spellShapes = Arcanus.SPELL_COMPONENTS.stream().filter(component -> !ArcanusSpellComponents.EMPTY.is(component) && ArcanusComponents.getWizardLevel(MinecraftClient.getInstance().player) >= component.getMinLevel() && component instanceof SpellShape).toList();
		spellEffects = Arcanus.SPELL_COMPONENTS.stream().filter(component -> !ArcanusSpellComponents.EMPTY.is(component) && ArcanusComponents.getWizardLevel(MinecraftClient.getInstance().player) >= component.getMinLevel() && component instanceof SpellEffect).toList();

		if(client != null) {
			for(SpellComponent component : spellShapes)
				if(ArcanusComponents.getWizardLevel(client.player) >= component.getMinLevel())
					addSpellShapeChild(new SpellComponentWidget(-35, component, widget -> {
						if(spellComponentCount() < ArcanusComponents.maxSpellSize(client.player))
							draggedComponent = widget.getSpellComponent();
					}));
			for(SpellComponent component : spellEffects)
				if(ArcanusComponents.getWizardLevel(client.player) >= component.getMinLevel())
					addSpellEffectChild(new SpellComponentWidget(267, component, widget -> {
						if(spellComponentCount() < ArcanusComponents.maxSpellSize(client.player))
							draggedComponent = widget.getSpellComponent();
					}));
		}

		addCloseButtons();
		textBox = addDrawableChild(new TextFieldWidget(client.textRenderer, x + 15, y + 8, 88, 14, Text.empty()));
		textBox.setText(SpellBookItem.getSpell(getScreenHandler().getSpellBook()).getName());
		addDrawableChild(new UndoRedoButtonWidget((width - 48) / 2, y - 8, true, undoRedoStack, button -> undoRedoStack.undo()));
		addDrawableChild(new UndoRedoButtonWidget(width / 2, y - 8, false, undoRedoStack, button -> undoRedoStack.redo()));

		for(SpellGroup group : SpellBookItem.getSpell(getScreenHandler().getSpellBook()).getComponentGroups()) {
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
	protected void drawBackground(GuiGraphics gui, float delta, int mouseX, int mouseY) {
		this.renderBackground(gui);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		gui.drawTexture(BOOK_TEXTURE, x, y, 0, 0, 256, 180, 256, 256);

		RenderSystem.setShaderTexture(0, PANEL_TEXTURE);
		gui.drawTexture(PANEL_TEXTURE, x - 62, y + 1, 0, 0, 380, 178, 384, 256);
	}

	@Override
	protected void drawForeground(GuiGraphics gui, int mouseX, int mouseY) {
		Rectangle leftKnob = getLeftScrollKnob();
		Rectangle rightKnob = getRightScrollKnob();

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.setShaderTexture(0, PANEL_TEXTURE);

		gui.drawTexture(PANEL_TEXTURE, leftKnob.x(), leftKnob.y(), draggingLeft ? 12 : 0, 184, leftKnob.width(), leftKnob.height(), 384, 256);
		gui.drawTexture(PANEL_TEXTURE, rightKnob.x(), rightKnob.y(), draggingRight ? 12 : 0, 184, rightKnob.width(), rightKnob.height(), 384, 256);

		drawWidgets(gui, mouseX, mouseY, MinecraftClient.getInstance().getTickDelta());

		super.drawForeground(gui, mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if(isPointWithinBounds(-62, 1, 58, 178, mouseX, mouseY)) {
			if(leftScroll > 0 && amount > 0)
				leftScroll--;
			if(leftScroll < spellShapes.size() * 2 - 12 && amount < 0)
				leftScroll++;

			leftKnobPos = leftScroll * (148F / (spellShapes.size() * 2 - 12));
		}

		if(isPointWithinBounds(260, 1, 58, 178, mouseX, mouseY)) {
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
			leftKnobPos = MathHelper.clamp(mouseY - y - 16, 0, 148);
			leftScroll = (int) (leftKnobPos / 148 * (spellShapes.size() * 2 - 12));
		}
		else if(draggingRight) {
			rightKnobPos = MathHelper.clamp(mouseY - y - 16, 0, 148);
			rightScroll = (int) (rightKnobPos / 148 * (spellEffects.size() * 2 - 12));
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(button == 0) {
			for(SpellComponentWidget widget : spellShapeWidgets)
				if(isPointWithinBounds(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), mouseX, mouseY))
					widget.onClick(mouseX, mouseY);
			for(SpellComponentWidget widget : spellEffectWidgets)
				if(isPointWithinBounds(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), mouseX, mouseY))
					widget.onClick(mouseX, mouseY);

			if(isPointWithinBounds(-58, 5, 12, 170, mouseX, mouseY)) {
				draggingLeft = true;
				leftKnobPos = MathHelper.clamp(mouseY - y - 16, 0, 148);
				leftScroll = (int) (leftKnobPos / 148 * (spellShapes.size() * 2 - 12));
			}
			else if(isPointWithinBounds(302, 5, 12, 170, mouseX, mouseY)) {
				draggingRight = true;
				rightKnobPos = MathHelper.clamp(mouseY - y - 16, 0, 148);
				rightScroll = (int) (rightKnobPos / 148 * (spellEffects.size() * 2 - 12));
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(!ArcanusSpellComponents.EMPTY.is(draggedComponent) && button == 0) {
			if(isPointWithinBounds(VALID_BOUNDS.x(), VALID_BOUNDS.y(), VALID_BOUNDS.z(), VALID_BOUNDS.w(), mouseX, mouseY) && !isTooCloseToComponents(mouseX, mouseY)) {
				Vector2i pos = new Vector2i((int) (mouseX - this.x - 12), (int) (mouseY - this.y - 12));

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
	protected void clearChildren() {
		super.clearChildren();
		spellGroups.clear();
		spellShapeWidgets.clear();
		spellEffectWidgets.clear();
	}

	private void drawWidgets(GuiGraphics gui, int mouseX, int mouseY, float delta) {
		MatrixStack matrices = gui.getMatrices();
		int scale = (int) client.getWindow().getScaleFactor();

		// Render Spell Shapes
		RenderSystem.enableScissor((x - 38) * scale, (y + 5) * scale, 30 * scale, 170 * scale);

		for(int i = 0; i < spellShapeWidgets.size(); i++) {
			SpellComponentWidget widget = spellShapeWidgets.get(i);
			widget.setY(8 + (i * 28) - leftScroll * 14);
			widget.render(gui, mouseX - x, mouseY - y, delta);

			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

			if(widget.isHoveredOrFocused())
				gui.drawTexture(PANEL_TEXTURE, widget.getX() - 3, widget.getY() - 3, 0, 208, 30, 30, 384, 256);
		}

		RenderSystem.disableScissor();

		// Render Spell Effects
		RenderSystem.enableScissor((x + 264) * scale, (y + 5) * scale, 30 * scale, 170 * scale);

		for(int i = 0; i < spellEffectWidgets.size(); i++) {
			SpellComponentWidget widget = spellEffectWidgets.get(i);
			widget.setY(8 + (i * 28) - rightScroll * 14);
			widget.render(gui, mouseX - x, mouseY - y, delta);

			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

			if(widget.isHoveredOrFocused())
				gui.drawTexture(PANEL_TEXTURE, widget.getX() - 3, widget.getY() - 3, 0, 208, 30, 30, 384, 256);
		}

		RenderSystem.disableScissor();

		for(int i = 0; i < spellGroups.size(); i++) {
			SpellGroup group = spellGroups.get(i);
			List<Vector2i> positions = group.positions();
			RenderSystem.setShader(GameRenderer::getPositionShader);
			RenderSystem.setShaderColor(0.25F, 0.25F, 0.3F, 1F);
			BufferBuilder bufferBuilder = Tessellator.getInstance().getBufferBuilder();

			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
			matrices.push();
			matrices.translate(12, 12, 0);
			Matrix4f matrix = matrices.peek().getModel();

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
				float dx = MathHelper.cos(angle);
				float dy = MathHelper.sin(angle);

				bufferBuilder.vertex(matrix, x2 - dx, y2 - dy, 0).color(0).next();
				bufferBuilder.vertex(matrix, x2 + dx, y2 + dy, 0).color(0).next();
				bufferBuilder.vertex(matrix, x1 + dx, y1 + dy, 0).color(0).next();
				bufferBuilder.vertex(matrix, x1 - dx, y1 - dy, 0).color(0).next();
			}

			BufferRenderer.drawWithShader(bufferBuilder.end());
			matrices.pop();
		}

		for(SpellGroup group : spellGroups) {
			List<Vector2i> positions = group.positions();

			for(int j = 0; j < positions.size(); j++) {
				Vector2i pos = positions.get(j);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				gui.drawTexture(PANEL_TEXTURE, pos.x - 3, pos.y - 3, 60, 208, 30, 30, 384, 256);

				RenderSystem.setShaderColor(0.25F, 0.25F, 0.3F, 1F);
				gui.drawTexture(PANEL_TEXTURE, pos.x - 3, pos.y - 3, 30, 208, 30, 30, 384, 256);

				gui.drawTexture(group.getAllComponents().toList().get(j).getTexture(), pos.x, pos.y, 0, 0, 24, 24, 24, 24);
			}
		}

		if(!ArcanusSpellComponents.EMPTY.is(draggedComponent)) {
			int colour = 0xff0000;

			if((isPointWithinBounds(VALID_BOUNDS.x(), VALID_BOUNDS.y(), VALID_BOUNDS.z(), VALID_BOUNDS.w(), mouseX, mouseY) && !isTooCloseToComponents(mouseX, mouseY)) && (!(draggedComponent instanceof SpellEffect) || (!spellGroups.isEmpty() && !spellGroups.getLast().isEmpty())))
				colour = 0x00ff00;

			float r = (colour >> 16 & 255) / 255F;
			float g = (colour >> 8 & 255) / 255F;
			float b = (colour & 255) / 255F;

			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			gui.drawTexture(PANEL_TEXTURE, mouseX - x - 15, mouseY - y - 15, 60, 208, 30, 30, 384, 256);

			RenderSystem.setShaderColor(r, g, b, 1F);
			gui.drawTexture(PANEL_TEXTURE, mouseX - x - 15, mouseY - y - 15, 30, 208, 30, 30, 384, 256);

			RenderSystem.setShaderColor(r, g, b, 1F);
			gui.drawTexture(draggedComponent.getTexture(), mouseX - x - 12, mouseY - y - 12, 0, 0, 24, 24, 24, 24);
		}

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		int componentCount = spellComponentCount();
		int maxComponents = ArcanusComponents.maxSpellSize(client.player);
		int componentCounterColour = 0x5555ff;

		if(componentCount >= maxComponents)
			componentCounterColour = 0xcc2222;

		String spellComponentCount = String.valueOf(componentCount);
		String maxSpellComponentCount = String.valueOf(maxComponents);

		gui.drawText(textRenderer, spellComponentCount, 118 - textRenderer.getWidth(spellComponentCount) / 2, 11, componentCounterColour, false);
		gui.drawText(textRenderer, " / ", 128 - textRenderer.getWidth(" / ") / 2, 11, 0x555555, false);
		gui.drawText(textRenderer, maxSpellComponentCount, 138 - textRenderer.getWidth(maxSpellComponentCount) / 2, 11, componentCounterColour, false);

		MutableText weight = Arcanus.translate("spell_book", "weight", getWeight().toString().toLowerCase(Locale.ROOT)).formatted(Formatting.DARK_GREEN);
		MutableText mana = Text.literal(Arcanus.format(getManaCost())).formatted(Formatting.BLUE);
		MutableText coolDown = Text.literal(Arcanus.format(getCoolDown() / 20D)).append(Arcanus.translate("spell_book", "seconds")).formatted(Formatting.RED);

		gui.drawText(textRenderer, weight, 240 - textRenderer.getWidth(weight), 7, 0xffffff, false);
		gui.drawText(textRenderer, mana, 240 - textRenderer.getWidth(mana), 17, 0xffffff, false);
		gui.drawText(textRenderer, coolDown, 240 - textRenderer.getWidth(coolDown), 27, 0xffffff, false);

		if(isPointWithinBounds(109, 8, textRenderer.getWidth("12 / 12"), textRenderer.fontHeight + 4, mouseX, mouseY))
			gui.drawTooltip(textRenderer, Arcanus.translate("screen", "tooltip.component_count"), mouseX - x, mouseY - y);

		for(SpellComponentWidget widget : spellShapeWidgets)
			if(widget.isHoveredOrFocused())
				widget.renderTooltip(gui, mouseX - x, mouseY - y);

		for(SpellComponentWidget widget : spellEffectWidgets)
			if(widget.isHoveredOrFocused())
				widget.renderTooltip(gui, mouseX - x, mouseY - y);

		for(SpellGroup group : spellGroups) {
			for(int i = 0; i < group.positions().size(); i++) {
				Vector2i position = group.positions().get(i);

				if(isPointWithinBounds(position.x() - 2, position.y() - 2, 28, 28, mouseX, mouseY)) {
					List<Text> textList = new ArrayList<>();
					SpellComponent component = group.getAllComponents().toList().get(i);

					textList.add(component.getTranslatedName());
					textList.add(Arcanus.translate("spell_book", "weight").append(": ").formatted(Formatting.GREEN).append(Arcanus.translate("spell_book", "weight", component.getWeight().toString().toLowerCase(Locale.ROOT)).formatted(Formatting.GRAY)));
					textList.add(Arcanus.translate("spell_book", "mana_cost").append(": ").formatted(Formatting.BLUE).append(Text.literal(component.getManaCostAsString()).formatted(Formatting.GRAY)));

					if(component instanceof SpellShape shape) {
						if(shape.getManaMultiplier() != 0)
							textList.add(Arcanus.translate("spell_book", "mana_multiplier").append(": ").formatted(Formatting.LIGHT_PURPLE).append(Text.literal(shape.getManaMultiplierAsString()).formatted(Formatting.GRAY)));
						if(shape.getPotencyModifier() != 0)
							textList.add(Arcanus.translate("spell_book", "potency_modifier").append(": ").formatted(Formatting.YELLOW).append(Text.literal(shape.getPotencyModifierAsString()).formatted(Formatting.GRAY)));
					}

					textList.add(Arcanus.translate("spell_book", "cool_down").append(": ").formatted(Formatting.RED).append(Text.literal(component.getCoolDownAsString()).append(Arcanus.translate("spell_book", "seconds")).formatted(Formatting.GRAY)));

					gui.drawTooltip(textRenderer, textList, mouseX - x, mouseY - y);
				}
			}
		}
	}

	protected void addCloseButtons() {
		addDrawableChild(ButtonWidget.builder(CommonTexts.DONE, (button) -> {
			SaveBookDataPacket.send(getScreenHandler().getPos(), getSpell());
			closeScreen();
		}).position(width / 2 - 100, y + 170).size(98, 20).build());

		addDrawableChild(ButtonWidget.builder(Text.translatable("lectern.take_book"), (button) -> {
			SaveBookDataPacket.send(getScreenHandler().getPos(), getSpell());
			client.interactionManager.clickButton(handler.syncId, 0);
			closeScreen();
		}).position(width / 2 + 2, y + 170).size(98, 20).build());
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
		return spellGroups.stream().mapToDouble(spellGroup -> spellGroup.positions().stream().mapToDouble(position -> position.distance((int) (mouseX - x - 12), (int) (mouseY - y - 12))).min().orElse(Double.MAX_VALUE)).min().orElse(Double.MAX_VALUE);
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

		return new Spell(spellGroups, textBox.getText().isBlank() ? "Empty" : textBox.getText());
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
