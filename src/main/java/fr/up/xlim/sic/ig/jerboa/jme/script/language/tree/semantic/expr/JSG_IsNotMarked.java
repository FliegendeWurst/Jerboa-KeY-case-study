package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_IsNotMarked extends JSEntity implements JSG_Expression {

	private JSG_Expression left;
	private JSG_Expression mark;

	public JSG_IsNotMarked(JSG_Expression left, JSG_Expression mark, int l, int col) {
		super(l, col);
		this.left = left;
		this.mark = mark;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Expression getLeft() {
		return left;
	}

	public JSG_Expression getMark() {
		return mark;
	}

}
