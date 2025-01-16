package dev.cammiescorner.arcanus.fabric.common.components;

import dev.cammiescorner.arcanus.fabric.common.util.Color;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.UUID;

public interface MagicColorComponent extends Component {

	String SOURCE_ID_KEY = "SourceId";

	Color getColor();

	Color getPocketDimensionColor();

	UUID getSourceId();

	void setSourceId(UUID ownerId);

}
