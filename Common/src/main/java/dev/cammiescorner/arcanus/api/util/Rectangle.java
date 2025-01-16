package dev.cammiescorner.arcanus.api.util;

public record Rectangle(int x, int y, int width, int height) {
	public int x2() {
		return x + width;
	}

	public int y2() {
		return y + height;
	}

	public boolean isIntersectedByPoint(double x, double y) {
		return x() <= x && y() <= y && x2() >= x && y2() >= y;
	}
}
