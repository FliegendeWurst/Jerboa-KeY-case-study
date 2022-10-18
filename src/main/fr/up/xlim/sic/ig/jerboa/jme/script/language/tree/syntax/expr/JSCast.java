package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSCast extends JSExpression {
	private JSType typeName;
	private JSExpression expr;

	public JSCast(JSType type, JSExpression _expr, int l, int c) {
		super(l, c);
		this.typeName = type;
		this.expr = _expr;
	}

	public JSType getType() {
		return typeName;
	}

	public JSExpression getExpr() {
		return expr;
	}
	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
