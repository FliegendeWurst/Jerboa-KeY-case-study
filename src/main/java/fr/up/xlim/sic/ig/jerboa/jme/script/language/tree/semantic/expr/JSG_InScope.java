package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_InScope implements JSG_Expression {

	private JSG_Expression left;
	private JSG_Expression right;

	public JSG_InScope(JSG_Expression left, JSG_Expression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Expression getLeft() {
		return left;
	}

	public JSG_Expression getRight() {
		return right;
	}

}
