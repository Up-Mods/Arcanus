package dev.cammiescorner.arcanuscontinuum.client.gui.util;

import java.util.LinkedList;

public class UndoRedoStack {
	private final LinkedList<Action> actions = new LinkedList<>();
	private int index = -1;

	public Action addAction(Action action) {
		while(actions.size() > index + 1)
			actions.remove(actions.size() - 1);

		actions.add(action);
		index++;

		return action;
	}

	public void undo() {
		if(canUndo()) {
			actions.get(index).undo().run();
			index--;
		}
	}

	public boolean canUndo() {
		return index >= 0;
	}

	public void redo() {
		if(canRedo()) {
			index++;
			actions.get(index).Do().run();
		}
	}

	public boolean canRedo() {
		return index < actions.size() - 1;
	}
}
