package dev.cammiescorner.arcanus.fabric.client.gui.util;

import dev.cammiescorner.arcanus.api.spells.SpellComponent;
import org.joml.Vector2i;

public record Action(SpellComponent component, Vector2i position, Runnable Do, Runnable undo) {
}
