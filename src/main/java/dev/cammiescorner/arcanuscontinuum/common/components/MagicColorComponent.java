package dev.cammiescorner.arcanuscontinuum.common.components;

import dev.cammiescorner.arcanuscontinuum.common.util.Color;
import dev.onyxstudios.cca.api.v3.component.Component;

import java.util.UUID;

public interface MagicColorComponent extends Component {

	String SOURCE_ID_KEY = "SourceId";

	Color getColor();

	UUID getSourceId();

	void setSourceId(UUID ownerId);

}
