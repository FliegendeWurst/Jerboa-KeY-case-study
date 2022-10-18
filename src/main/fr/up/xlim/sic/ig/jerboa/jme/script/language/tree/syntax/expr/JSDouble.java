package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSDouble extends JSLiteral {

	private double value;

	public JSDouble(double val, int l, int col) {
		super(l, col);
		this.value = val;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public double getValue() {
		return value;
	}
}
