package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_Not extends JSEntity implements JSG_Expression {

	private JSG_Expression expr;

	public JSG_Not(JSG_Expression expr, int l, int col) {
		super(l, col);
		this.expr = expr;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Expression getExpr() {
		return expr;
	}

}
