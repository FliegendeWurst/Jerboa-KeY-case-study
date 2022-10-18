package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSPackagedType extends JSType {

	private JSType left;
	private JSType right;

	public JSPackagedType(JSType left, JSType right, int l, int col) {
		super("#PackagedType#", l, col);
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

	public JSType getRight() {
		return right;
	}

}
