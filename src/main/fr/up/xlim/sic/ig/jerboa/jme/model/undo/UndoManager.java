package fr.up.xlim.sic.ig.jerboa.jme.model.undo;

import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;


public class UndoManager {

	ReentrantLock mutex;
	private LinkedList<UndoItem> undo;
	private LinkedList<UndoItem> redo;

	private ArrayList<Component> mnundo; // menuItem undo
	private ArrayList<Component> mnredo; // menuItem redo

	public UndoManager() {
		mutex = new ReentrantLock();
		undo = new LinkedList<>();
		redo = new LinkedList<>();

		mnundo = new ArrayList<>();
		mnredo = new ArrayList<>();
	}

	public void addUndoComponent(Component u) {
		mutex.lock();
		mnundo.add(u);
		mutex.unlock();
		update();
	}

	public void addRedoComponent(Component u) {
		mutex.lock();
		mnredo.add(u);
		mutex.unlock();
		update();
	}

	private void update() {
		for (Component c : mnundo) {
			c.setEnabled(canUndo());
		}

		for (Component c : mnredo) {
			c.setEnabled(canRedo());
		}
	}

	public void clear() {
		mutex.lock();
		undo.clear();
		redo.clear();
		mnredo.clear();
		mnundo.clear();
		mutex.unlock();
		update();
	}

	public void registerUndo(UndoItem item) {
		// System.out.println("REGISTER UNDO MANAGER: " + item);
		mutex.lock();
		undo.push(item);
		redo.clear();
		mutex.unlock();
		update();
	}

	public void transfertUndo(UndoItem item) {
		mutex.lock();
		undo.push(item);
		mutex.unlock();
		update();
	}

	public void transfertRedo(UndoItem item) {
		mutex.lock();
		redo.push(item);
		mutex.unlock();
		update();
	}

	public boolean canUndo() {
		mutex.lock();
		boolean isE = !undo.isEmpty();
		mutex.unlock();
		return isE;
	}

	public void registerRedo(UndoItem fitem) {
		mutex.lock();
		redo.push(fitem);
		mutex.unlock();
		update();
	}

	public void undo() {
		// System.out.println("UNDO!!! ("+undo+")");
		if (!undo.isEmpty()) {
			mutex.lock();
			UndoItem item = undo.pop();
			mutex.unlock();
			// System.out.println("UNDO ITEM: " + item);
			item.getObject().undo(item);
			update();
		}
	}

	public void redo() {
		if (!redo.isEmpty()) {
			mutex.lock();
			UndoItem item = redo.pop();
			mutex.unlock();
			// System.out.println("REDO ITEM: " + item);
			item.getObject().redo(item);
			update();
		}
	}

	private static UndoManager instance = new UndoManager();

	public static UndoManager getInstance() {
		return instance;
	}

	public boolean canRedo() {
		return !redo.isEmpty();
	}

}
