package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSList extends JSType {
	JSType t;

	public JSList(JSType type, int l, int col) {
		super("List<" + type.getType() + ">", l, col);
		t = type;
	}

	public JSType getTypedList() {
		return t;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
