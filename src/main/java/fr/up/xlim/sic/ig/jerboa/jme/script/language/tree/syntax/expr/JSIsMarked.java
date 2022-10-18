package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSIsMarked extends JSExpression {

	private JSExpression left;
	private JSVariable mark;

	public JSIsMarked(JSExpression left, JSVariable mark, int l, int col) {
		super(l, col);
		this.left = left;
		this.mark = mark;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getLeft() {
		return left;
	}

	public JSVariable getMark() {
		return mark;
	}

}
