package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_InScopeStatic implements JSG_Expression {

	private JSG_Type left;
	private JSG_Expression right;

	public JSG_InScopeStatic(JSG_Type left, JSG_Expression right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Type getLeft() {
		return left;
	}

	public JSG_Expression getRight() {
		return right;
	}

}
