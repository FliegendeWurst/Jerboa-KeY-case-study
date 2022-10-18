package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class JMEParamEbd implements JMEElement,Comparable<JMEParamEbd> {

	// intrinsic parameters
	private String name;
	private String type;
	private String initvalue;
	private boolean modified;
	private JMERule rule;
	private int order;

	// ihm parameters
	protected Set<JMEElementView> views;
	protected UndoManager manager;

	public JMEParamEbd(JMERule rule, String name, String type, String initvalue, int order) {
		this.rule = rule;
		this.manager = rule.getUndoManager();
		this.name = name;
		this.type = type;
		this.initvalue = initvalue;
		this.order = order;
		this.modified = false;

		views = new HashSet<>();
		views = Collections.synchronizedSet(views);
	}

	public JMERule getRule() {
		return rule;
	}

	public int getOrder() {
		// return rule.getParamsEbd().indexOf(this);
		return order;
	}
	
	public void setOrder(int order) {
		if(this.order != order) {
			this.order = order;
			modified = true;
			update(); 
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && !name.equals(this.name)) {
			UndoItemField field = new UndoItemField(this, "name", this.name, name, !modified);
			manager.registerUndo(field);
			this.name = name;
			modified = true;
			update();
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type != null && !type.equals(this.type)) {
			UndoItemField field = new UndoItemField(this, "type", this.type, type, !modified);
			manager.registerRedo(field);
			this.type = type;
			modified = true;
			update();
		}
	}


	public String getInitValue() {
		return initvalue;
	}

	public void setInitValue(String initvalue) {
		if (initvalue != null && !initvalue.equals(this.initvalue)) {
			UndoItemField field = new UndoItemField(this, "initvalue", this.initvalue, initvalue, !modified);
			manager.registerRedo(field);
			this.initvalue = initvalue;
			modified = true;
			update();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getOrder()).append("-").append(name).append(":").append(type);
		if(initvalue != null && !initvalue.trim().equals(""))
			sb.append(" = ").append(initvalue);

		return sb.toString();
	}

	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch (fitem.field()) {
		case "name":
			name = (String) fitem.value();
			break;
		case "type":
			type = (String) fitem.value();
			break;
		case "initvalue":
			initvalue = (String) fitem.value();
			break;
		case "order":
			order = ((Integer) fitem.value()).intValue();
			// rule.exchangeParamEbd(this, order);
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
		switch (fitem.field()) {
		case "name":
			name = (String) fitem.newValue();
			break;
		case "type":
			type = (String) fitem.newValue();
			break;
		case "initvalue":
			initvalue = (String) fitem.newValue();
			break;
		case "order":
			order = ((Integer) fitem.newValue()).intValue();
			// rule.exchangeParamEbd(this, order);
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
		return visitor.visitParamEbd(this);
	}

	@Override
	public UndoManager getUndoManager() {
		return manager;
	}

	@Override
	public int compareTo(JMEParamEbd o) {
		return Integer.compare(getOrder(), o.getOrder());
	}
	
	@Override
	public boolean isModified() {
		return modified;
	}
	

	@Override
	public void resetModification() {
		modified = false;
	}

	public JMEParamEbd copy(JMERule rule) {
		JMEParamEbd pebd = new JMEParamEbd(rule, name, type, initvalue, order);
		return pebd;
	}

}
