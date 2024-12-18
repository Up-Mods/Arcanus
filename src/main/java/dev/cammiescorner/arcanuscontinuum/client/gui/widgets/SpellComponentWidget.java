package dev.cammiescorner.arcanuscontinuum.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellComponent;
import dev.cammiescorner.arcanuscontinuum.api.spells.SpellShape;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SpellComponentWidget extends AbstractButton {
	private final TooltipSupplier tooltipSupplier;
	private final PressAction onPress;
	private final Minecraft client = Minecraft.getInstance();
	private final SpellComponent component;

	public SpellComponentWidget(int x, SpellComponent component, PressAction onPress) {
		super(x, 0, 24, 24, Component.empty());
		this.component = component;
		this.onPress = onPress;

		List<Component> textList = new ArrayList<>();
		textList.add(component.getTranslatedName());
		textList.add(Arcanus.translate("spell_book", "weight").append(": ").withStyle(ChatFormatting.GREEN).append(Arcanus.translate("spell_book", "weight", component.getWeight().toString().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY)));
		textList.add(Arcanus.translate("spell_book", "mana_cost").append(": ").withStyle(ChatFormatting.BLUE).append(Component.literal(component.getManaCostAsString()).withStyle(ChatFormatting.GRAY)));

		if(component instanceof SpellShape shape) {
			if(shape.getManaMultiplier() != 0)
				textList.add(Arcanus.translate("spell_book", "mana_multiplier").append(": ").withStyle(ChatFormatting.LIGHT_PURPLE).append(Component.literal(shape.getManaMultiplierAsString()).withStyle(ChatFormatting.GRAY)));
			if(shape.getPotencyModifier() != 0)
				textList.add(Arcanus.translate("spell_book", "potency_modifier").append(": ").withStyle(ChatFormatting.YELLOW).append(Component.literal(shape.getPotencyModifierAsString()).withStyle(ChatFormatting.GRAY)));
		}

		textList.add(Arcanus.translate("spell_book", "cool_down").append(": ").withStyle(ChatFormatting.RED).append(Component.literal(component.getCoolDownAsString()).append(Arcanus.translate("spell_book", "seconds")).withStyle(ChatFormatting.GRAY)));


		this.tooltipSupplier = new TooltipSupplier() {
			@Override
			public void onTooltip(SpellComponentWidget spellComponentWidget, GuiGraphics gui, int mouseX, int mouseY) {
				if(client.screen != null)
					gui.renderComponentTooltip(client.font, textList, mouseX, mouseY);
			}

			@Override
			public void supply(Consumer<Component> consumer) {
				consumer.accept(textList.get(0));
			}
		};
	}

	@Override
	public void onPress() {
		onPress.onPress(this);
	}

	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderColor(0.25F, 0.25F, 0.3F, 1F);
		gui.blit(component.getTexture(), getX(), getY(), 0, 0, 24, 24, 24, 24);
	}

	public void renderTooltip(GuiGraphics gui, int mouseX, int mouseY) {
		tooltipSupplier.onTooltip(this, gui, mouseX, mouseY);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput builder) {
		defaultButtonNarrationText(builder);
		tooltipSupplier.supply(text -> builder.add(NarratedElementType.HINT, text));
	}

	public SpellComponent getSpellComponent() {
		return component;
	}

	public interface TooltipSupplier {
		void onTooltip(SpellComponentWidget spellComponentWidget, GuiGraphics gui, int i, int j);

		default void supply(Consumer<Component> consumer) {
		}
	}

	public interface PressAction {
		void onPress(SpellComponentWidget buttonWidget);
	}
}
