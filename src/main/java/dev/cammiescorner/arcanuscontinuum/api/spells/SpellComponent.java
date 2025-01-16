package dev.cammiescorner.arcanuscontinuum.api.spells;

import dev.cammiescorner.arcanuscontinuum.Arcanus;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SpellComponent {
	public static final String DISABLED_TRANSLATION_KEY = Util.makeDescriptionId("arcanuscontinuum.spell_component", Arcanus.id("disabled"));
	private static final Component DISABLED_TRANSLATED_NAME = Component.translatable(DISABLED_TRANSLATION_KEY).withStyle(ChatFormatting.OBFUSCATED);
	private final boolean isEnabled;
	private final Weight weight;
	private final double manaCost;
	private final int coolDown;
	private final int minLevel;
	private String translationKey;
	private ResourceLocation texture;

	public SpellComponent(boolean isEnabled, Weight weight, double manaCost, int coolDown, int minLevel) {
		this.isEnabled = isEnabled;
		this.weight = weight;
		this.manaCost = manaCost;
		this.coolDown = coolDown;
		this.minLevel = minLevel;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public Weight getWeight() {
		return weight;
	}

	public double getManaCost() {
		return manaCost;
	}

	public int getCoolDown() {
		return coolDown;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public String getManaCostAsString() {
		return Arcanus.format(getManaCost());
	}

	public String getCoolDownAsString() {
		return Arcanus.format(getCoolDown() / 20D);
	}

	public ResourceLocation getTexture() {
		if(texture == null) {
			ResourceLocation id = Arcanus.SPELL_COMPONENTS.getKey(this);
			String extra = "";

			if(this instanceof SpellShape)
				extra = "shapes/";
			if(this instanceof SpellEffect)
				extra = "effects/";

			texture = new ResourceLocation(id.getNamespace(), "textures/spell_components/" + extra + id.getPath() + ".png");
		}

		return texture;
	}

	public String getTranslationKey() {
		if(translationKey == null)
			translationKey = Util.makeDescriptionId("arcanuscontinuum.spell_component", Arcanus.SPELL_COMPONENTS.getKey(this));

		return translationKey;
	}

	public Component getName() {
		if(!isEnabled())
			return DISABLED_TRANSLATED_NAME;

		return Component.translatable(getTranslationKey());
	}
}
