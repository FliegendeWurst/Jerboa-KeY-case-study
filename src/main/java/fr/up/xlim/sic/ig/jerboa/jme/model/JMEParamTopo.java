package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class JMEParamTopo implements JMEElement {

	// intrinsic parameters
	private boolean modified;
	private JMERule rule;
	private JMENode node;

	// ihm parameters
	protected Set<JMEElementView> views;
	protected UndoManager manager;

	public JMEParamTopo(JMERule rule, JMENode node) {
		this.manager = rule.getUndoManager();
		this.rule = rule;
		this.node = node;
		this.modified = false;

		views = new HashSet<>();
		views = Collections.synchronizedSet(views);
	}

	public int getOrder() {
		return rule.getParamsTopo().indexOf(this);
	}
	/*
	 * public void setOrder(int order) { if(order != this.order) { this.order =
	 * order; update(); } }
	 */

	public JMENode getNode() {
		return node;
	}

	public JMERule getRule() {
		return rule;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getOrder()).append("-").append(node);

		return sb.toString();
	}

	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch (fitem.field()) {
		case "order":
			// order = ((Integer) fitem.value()).intValue();
			break;
		}
		if (fitem.getModifState())
			modified = false;
		manager.transfertRedo(fitem);
		update();
	}

	@Override
	public void redo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		// int order;
		switch (fitem.field()) {
		case "order":
			// order = ((Integer) fitem.newValue()).intValue();
			break;
		}
		if (fitem.getModifState())
			modified = true;
		manager.transfertUndo(fitem);
		update();
	}

	@Override
	public void addView(JMEElementView view) {
		views.add(view);
	}

	@Override
	public void removeView(JMEElementView view) {
		views.remove(view);
	}

	@Override
	public void update() {
		for (JMEElementView view : views) {
			view.reload();
		}
	}

	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitParamTopo(this);
	}

	@Override
	public UndoManager getUndoManager() {
		return manager;
	}

	
	@Override
	public boolean isModified() {
		return modified;
	}


	@Override
	public void resetModification() {
		modified = false;
	}
	
	@Override
	public String getName() {
		return node.getName();
	}
}
