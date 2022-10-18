package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSAlpha extends JSExpression {

	private JSExpression node;
	private JSExpression dim;

	public JSAlpha(JSExpression node, JSExpression dim, int l, int col) {
		super(l, col);
		this.node = node;
		this.dim = dim;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getNode() {
		return node;
	}

	public JSExpression getDim() {
		return dim;
	}

}
