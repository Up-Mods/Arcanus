package dev.cammiescorner.arcanuscontinuum.common.util.supporters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.arcanuscontinuum.Arcanus;
import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.upcraft.datasync.api.util.Entitlements;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record WizardData(Color magicColor) {

	public static final Identifier ID = Arcanus.id("wizard_data");

	private static final WizardData EMPTY = new WizardData(Arcanus.DEFAULT_MAGIC_COLOUR);

	public static WizardData empty() {
		return EMPTY;
	}

	public static final Codec<WizardData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Color.CODEC.fieldOf("magic_color").forGetter(WizardData::magicColor)
	).apply(instance, WizardData::new));

	public static WizardData getOrEmpty(UUID uuid) {
		return Arcanus.WIZARD_DATA.getOrDefault(uuid, WizardData.empty());
	}

	public static boolean isSupporter(UUID uuid) {
		return Entitlements.getOrEmpty(uuid).keys().contains(ID);
	}
}
