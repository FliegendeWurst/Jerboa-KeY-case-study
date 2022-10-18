package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSUnreference extends JSExpression {
	private JSExpression exp;

	public JSUnreference(JSExpression exp, int l, int col) {
		super(l, col);
		this.exp = exp;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getExp() {
		return exp;
	}

}
