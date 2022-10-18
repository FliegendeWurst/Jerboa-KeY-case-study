package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSInScopeStatic extends JSExpression {

	private JSType left;
	private JSExpression right;

	public JSInScopeStatic(JSType left, JSExpression right, int l, int col) {
		super(l, col);
		this.left = left;
		this.right = right;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSType getLeft() {
		return left;
	}

	public JSExpression getRight() {
		return right;
	}

}
