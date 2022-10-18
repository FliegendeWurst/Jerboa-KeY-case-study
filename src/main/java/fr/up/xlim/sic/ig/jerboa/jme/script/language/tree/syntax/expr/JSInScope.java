package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSInScope extends JSExpression {

	private JSExpression left;
	private JSExpression right;

	public JSInScope(JSExpression left, JSExpression right, int l, int col) {
		super(l, col);
		this.left = left;
		this.right = right;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getLeft() {
		return left;
	}

	public JSExpression getRight() {
		return right;
	}

}
