package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSInteger extends JSLiteral {

	private int value;

	public JSInteger(int val, int l, int col) {
		super(l, col);
		this.value = val;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public int getValue() {
		return value;
	}
}
