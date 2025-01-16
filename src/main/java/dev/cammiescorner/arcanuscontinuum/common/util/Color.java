package dev.cammiescorner.arcanuscontinuum.common.util;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.IntStream;

// TODO replace with SparkWeave's Color class in 1.21
public class Color {
	public static final Ordering DEFAULT_ORDERING = Color.Ordering.RGBA;
	private final byte[] rgba;

	private Color(int red, int green, int blue, int alpha) {
		this.rgba = new byte[]{(byte) red, (byte) green, (byte) blue, (byte) alpha};
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || getClass() != o.getClass())
			return false;
		Color color = (Color) o;
		return Objects.deepEquals(rgba, color.rgba);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(rgba);
	}

	private static Color fromRGBA(int red, int green, int blue, int alpha) {
		validateRange(red, "red");
		validateRange(green, "green");
		validateRange(blue, "blue");
		validateRange(alpha, "alpha");
		return new Color(red, green, blue, alpha);
	}

	public static Color fromRGB(int red, int green, int blue) {
		validateRange(red, "red");
		validateRange(green, "green");
		validateRange(blue, "blue");
		return new Color(red, green, blue, 255);
	}

	public static Color fromFloatsRGBA(float red, float green, float blue, float alpha) {
		return fromRGBA((int) (red * 255.0F), (int) (green * 255.0F), (int) (blue * 255.0F), (int) (alpha * 255.0F));
	}

	public static Color fromFloatsRGB(float red, float green, float blue) {
		return fromRGB((int) (red * 255.0F), (int) (green * 255.0F), (int) (blue * 255.0F));
	}

	public static Color fromIntArray(Color.Ordering ordering, int... values) {
		Preconditions.checkElementIndex(ordering.expectedLength() - 1, values.length);
		int red = ordering.hasRed() ? values[ordering.iR()] : 0x00;
		int green = ordering.hasGreen() ? values[ordering.iG()] : 0x00;
		int blue = ordering.hasBlue() ? values[ordering.iB()] : 0x00;
		int alpha = ordering.hasAlpha() ? values[ordering.iA()] : 0xFF;
		return fromRGBA(red, green, blue, alpha);
	}

	public static Color fromFloatArray(Color.Ordering ordering, float... values) {
		Preconditions.checkElementIndex(ordering.expectedLength() - 1, values.length);
		int red = ordering.hasRed() ? (int) (values[ordering.iR()] * 255.0F) : 0;
		int green = ordering.hasGreen() ? (int) (values[ordering.iG()] * 255.0F) : 0;
		int blue = ordering.hasBlue() ? (int) (values[ordering.iB()] * 255.0F) : 0;
		int alpha = ordering.hasAlpha() ? (int) (values[ordering.iA()] * 255.0F) : 255;
		return fromRGBA(red, green, blue, alpha);
	}

	public int red() {
		return Byte.toUnsignedInt(rgba[0]);
	}

	public float redF() {
		return red() / 255.0F;
	}

	public int green() {
		return Byte.toUnsignedInt(rgba[1]);
	}

	public float greenF() {
		return green() / 255.0F;
	}

	public int blue() {
		return Byte.toUnsignedInt(rgba[2]);
	}

	public float blueF() {
		return blue() / 255.0F;
	}

	public int alpha() {
		return Byte.toUnsignedInt(rgba[3]);
	}

	public float alphaF() {
		return alpha() / 255.0F;
	}

	public float[] asFloatsRGBA() {
		return asFloats(Color.Ordering.RGBA);
	}

	public float[] asFloats(Color.Ordering ordering) {
		float[] values = new float[ordering.expectedLength];
		if(ordering.hasRed()) {
			values[ordering.iR()] = redF();
		}
		if(ordering.hasGreen()) {
			values[ordering.iG()] = greenF();
		}
		if(ordering.hasBlue()) {
			values[ordering.iB()] = blueF();
		}
		if(ordering.hasAlpha()) {
			values[ordering.iA()] = alphaF();
		}
		return values;
	}

	public int asIntARGB() {
		return asInt(Ordering.ARGB);
	}

	public int asInt(Color.Ordering ordering) {
		int value = 0;
		int maxIdx = ordering.expectedLength() - 1;

		if(ordering.hasRed()) {
			assert ordering.iR() <= maxIdx;
			value |= red() << ((maxIdx - ordering.iR()) * 8);
		}
		if(ordering.hasGreen()) {
			assert ordering.iG() <= maxIdx;
			value |= green() << ((maxIdx - ordering.iG()) * 8);
		}
		if(ordering.hasBlue()) {
			assert ordering.iB() <= maxIdx;
			value |= blue() << ((maxIdx - ordering.iB()) * 8);
		}
		if(ordering.hasAlpha()) {
			assert ordering.iA() <= maxIdx;
			value |= alpha() << ((maxIdx - ordering.iA()) * 8);
		}
		return value;
	}

	public Vector4fc asVec() {
		return new Vector4f(redF(), greenF(), blueF(), alphaF());
	}

	public Vector3fc asVec3() {
		return new Vector3f(redF(), greenF(), blueF());
	}

	public static Color fromInt(int intValue, Ordering ordering) {
		int c1 = intValue >> 24 & 255;
		int c2 = intValue >> 16 & 255;
		int c3 = intValue >> 8 & 255;
		int c4 = intValue & 255;
		int[] array = ordering.expectedLength == 3 ? new int[]{c2, c3, c4} : new int[]{c1, c2, c3, c4};
		return fromIntArray(ordering, array);
	}

	public static Color fromARGB(int argb) {
		return fromInt(argb, Ordering.ARGB);
	}

	/**
	 * HSV (hue, saturation, value), also known as HSB (hue, saturation, brightness)
	 *
	 * @param hue        angle as [0, 1], where 1.0 means a full circle and overflows back to 0.0
	 * @param saturation saturation in [0, 1]
	 * @param value      brightness in [0, 1]
	 */
	public static Color fromHSV(float hue, float saturation, float value) {
		int argb = Mth.hsvToRgb(hue, saturation, value);
		return Color.fromInt(argb, Ordering.ARGB);
	}

	public static final Codec<Color> CODEC_ARGB = Codec.INT.xmap(intValue -> Color.fromInt(intValue, Ordering.ARGB), Color::asIntARGB);

	public static final Codec<Color> CODEC = Codec.either(CODEC_ARGB, Ordering.CODEC.dispatch("ordering", c -> DEFAULT_ORDERING, Ordering::dispatchedCodec)).xmap(CodecHelper::unwrapEither, Either::left);

	public enum Ordering implements StringRepresentable {
		RGBA("rgba", 0, 1, 2, 3),
		ARGB("argb", 1, 2, 3, 0),
		RGB("rgb", 0, 1, 2, -1),
		BGR("bgr", 2, 1, 0, -1);

		private final String name;
		private final int redIndex;
		private final int greenIndex;
		private final int blueIndex;
		private final int alphaIndex;

		private final int expectedLength;

		public static final Codec<Ordering> CODEC = StringRepresentable.fromEnum(Ordering::values);

		private final Codec<Color> dispatchedCodec = ExtraCodecs.lazyInitializedCodec(() -> RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("value").forGetter(color -> color.asInt(this))).apply(instance, intValue -> Color.fromInt(intValue, this))));

		Ordering(String name, int redIndex, int greenIndex, int blueIndex, int alphaIndex) {
			this.name = name;
			this.redIndex = redIndex;
			this.greenIndex = greenIndex;
			this.blueIndex = blueIndex;
			this.alphaIndex = alphaIndex;

			this.expectedLength = (int) IntStream.of(redIndex, greenIndex, blueIndex, alphaIndex).filter(v -> v >= 0).count();
		}

		public int iR() {
			Preconditions.checkArgument(this.hasRed(), this + " does not have a RED component!");
			return this.redIndex;
		}

		public int iG() {
			Preconditions.checkArgument(this.hasGreen(), this + " does not have a GREEN component!");
			return this.greenIndex;
		}

		public int iB() {
			Preconditions.checkArgument(this.hasBlue(), this + " does not have a BLUE component!");
			return this.blueIndex;
		}

		public int iA() {
			Preconditions.checkArgument(this.hasAlpha(), this + " does not have an ALPHA component!");
			return this.alphaIndex;
		}

		public boolean hasRed() {
			return this.redIndex != -1;
		}

		public boolean hasGreen() {
			return this.greenIndex != -1;
		}

		public boolean hasBlue() {
			return this.blueIndex != -1;
		}

		public boolean hasAlpha() {
			return this.alphaIndex != -1;
		}

		public int expectedLength() {
			return this.expectedLength;
		}

		@Override
		public String toString() {
			return "Color.Ordering." + this.name().toUpperCase(Locale.ROOT);
		}

		@Override
		public String getSerializedName() {
			return name;
		}

		public Codec<Color> dispatchedCodec() {
			return dispatchedCodec;
		}
	}

	public int placeAt(int value, int length, int idx) {
		return value << ((length - idx) * 8);
	}

	private static void validateRange(int value, String name) {
		Preconditions.checkArgument(value >= 0 && value <= 255, name + " is not within [0, 255]");
	}
}
