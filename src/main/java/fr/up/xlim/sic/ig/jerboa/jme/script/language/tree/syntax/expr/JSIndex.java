package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSIndex extends JSExpression {

	private JSExpression variable;
	private JSExpression index;

	public JSIndex(JSExpression var, JSExpression index, int l, int col) {
		super(l, col);
		this.variable = var;
		this.index = index;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getVariable() {
		return variable;
	}

	public JSExpression getIndex() {
		return index;
	}

	public String toString() {
		return variable + "[" + index + "]";
	}
}
