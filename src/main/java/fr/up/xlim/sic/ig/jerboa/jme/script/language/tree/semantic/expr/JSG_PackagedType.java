package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_PackagedType extends JSG_Type implements JSG_TypeObject {

	private JSG_Type left;
	private JSG_Type right;

	public JSG_PackagedType(JSG_Type left, JSG_Type right, int l, int c) {
		super("#Package_G_Type#", l, c);
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

	public JSG_Type getRight() {
		return right;
	}

}
