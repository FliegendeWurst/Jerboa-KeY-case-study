package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSNot extends JSExpression {

	private JSExpression expr;

	public JSNot(JSExpression expr, int l, int col) {
		super(l, col);
		this.expr = expr;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getExpr() {
		return expr;
	}

}
