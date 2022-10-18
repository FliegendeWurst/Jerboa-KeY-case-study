package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_List extends JSG_Type implements JSG_TypeObject {
	JSG_Type t;

	public JSG_List(JSG_Type type, int l, int col) {
		super("List", l, col);
		t = type;
	}

	public JSG_Type getTypedList() {
		return t;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
