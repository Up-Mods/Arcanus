package dev.cammiescorner.arcanuscontinuum.common.util;

import com.mojang.datafixers.util.Either;

import java.util.function.UnaryOperator;

public class CodecHelper {
	/**
	 * @deprecated replace with Either::unwrap when updating to 1.21.1
	 */
	@Deprecated
	public static <T> T unwrapEither(Either<T, T> either) {
		return either.map(UnaryOperator.identity(), UnaryOperator.identity());
	}
}
