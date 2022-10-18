package fr.up.xlim.sic.ig.jerboa.jme.model.undo;

public interface UndoableObject {
	void undo(UndoItem item);
	void redo(UndoItem item);
	
	UndoManager getUndoManager();
}
