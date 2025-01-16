package dev.cammiescorner.arcanuscontinuum.client.gui.util;

import dev.cammiescorner.arcanuscontinuum.api.spells.SpellComponent;
import org.joml.Vector2i;

public record Action(SpellComponent component, Vector2i position, Runnable Do, Runnable undo) {
}
