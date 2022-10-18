package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_Alpha extends JSEntity implements JSG_Expression {

	private JSG_Expression node;
	private JSG_Expression dim;

	public JSG_Alpha(JSG_Expression node, JSG_Expression dim, int l, int col) {
		super(l, col);
		this.node = node;
		this.dim = dim;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Expression getNode() {
		return node;
	}

	public JSG_Expression getDim() {
		return dim;
	}

}
