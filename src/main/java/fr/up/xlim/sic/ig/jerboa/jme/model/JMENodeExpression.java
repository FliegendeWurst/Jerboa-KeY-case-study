package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.HashSet;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class JMENodeExpression implements Comparable<JMENodeExpression>, JMEElement {

	protected String expression;
	protected JMEEmbeddingInfo info;
	private boolean modified;
	protected JMENode defnode;
	protected UndoManager manager;
	protected HashSet<JMEElementView> views;

	public JMENodeExpression(JMENode decl, JMEEmbeddingInfo info, String expr) {
		this.defnode = decl;
		this.info = info;
		this.expression = expr;
		modified = false;
		this.manager = new UndoManager();
		views = new HashSet<>();
	}

	public void setEbdInfo(JMEEmbeddingInfo e) {
		info = e;
	}

	@Override
	public int compareTo(JMENodeExpression o) {
		return info.getName().compareTo(o.info.getName());
	}

	public JMEEmbeddingInfo getEbdInfo() {
		return info;
	}

	public JMENode getNode() {
		return defnode;
	}

	public String getExpression() {
		return expression;
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void undo(UndoItem item) {

	}

	@Override
	public void redo(UndoItem item) {

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
		// if (expression.replaceAll("[\\s|\\n|\\t]", "").length() == 0) {
		// defnode.removeExpression(this);
		// } else if (!defnode.getExplicitExprs().contains(this)) {
		// defnode.addExplicitExpression(this);
		// }
		defnode.update();
	}

	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitExpression(this);
	}

	@Override
	public UndoManager getUndoManager() {
		return manager;
	}

	@Override
	public String toString() {
		return "Expr of : \"" + info.getName() + "\" on node " + defnode.getName() + " '" + expression + "'";
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof JMENodeExpression) {
			JMENodeExpression mene = (JMENodeExpression)obj;
			return (info == mene.info) && (defnode == mene.defnode);
		}
		return false;
	}

	public void setExpression(String text) {
		if(text != null && !text.equals(expression)) {
			this.expression = text;
			defnode.graph.updateAllExprs();
			modified = true;
		}
	}


	@Override
	public void resetModification() {
		modified = false;
	}

	@Override
	public String getName() {
		return toString();
	}

	public boolean equalsEbd(JMENodeExpression mene) {
		return info == mene.info;
	}
	
	public boolean equalsEbd(JMEEmbeddingInfo ebdinfo) {
		return info.equals(ebdinfo);
	}
	
}
