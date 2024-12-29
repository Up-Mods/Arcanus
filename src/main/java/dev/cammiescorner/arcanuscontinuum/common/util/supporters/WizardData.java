package dev.cammiescorner.arcanuscontinuum.common.util.supporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.upcraft.datasync.api.util.Entitlements;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record WizardData(Color magicColor, Color pocketDimensionColor) {
	public static final ResourceLocation ID = Arcanus.id("wizard_data");
	private static final WizardData EMPTY = new WizardData(Arcanus.DEFAULT_MAGIC_COLOUR, Arcanus.DEFAULT_MAGIC_COLOUR);

	public static final Codec<WizardData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Color.CODEC.fieldOf("magic_color").forGetter(WizardData::magicColor),
		Color.CODEC.optionalFieldOf("pocket_dimension_color", Arcanus.DEFAULT_MAGIC_COLOUR).forGetter(WizardData::pocketDimensionColor)
	).apply(instance, WizardData::new));

	public static WizardData empty() {
		return EMPTY;
	}

	public static WizardData getOrEmpty(UUID uuid) {
		return Arcanus.WIZARD_DATA.getOrDefault(uuid, WizardData.empty());
	}

	public static boolean isSupporter(UUID uuid) {
		return Entitlements.getOrEmpty(uuid).keys().contains(ID);
	}

	public WizardData withColor(Color color) {
		return new WizardData(color, this.pocketDimensionColor());
	}

	public WizardData withPocketDimensionColor(Color color) {
		return new WizardData(this.magicColor(), color);
	}
}
