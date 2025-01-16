package dev.cammiescorner.arcanuscontinuum.api.spells;

public enum Weight {
	NONE(0), VERY_LIGHT(0.15), LIGHT(0.3), MEDIUM(0.45), HEAVY(0.6), VERY_HEAVY(0.75);

	private final double speed;

	Weight(double slowdown) {
		this.speed = slowdown;
	}

	public double getSlowdown() {
		return -speed;
	}
}
