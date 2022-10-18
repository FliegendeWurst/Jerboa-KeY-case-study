package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public abstract class JSG_Type extends JSEntity implements JSG_Expression {
	protected String type;

	public String getType() {
		return type;
	}

	protected JSG_Type(String type, int l, int col) {
		super(l, col);
		this.type = type;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
