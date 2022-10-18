package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_Indirection implements JSG_Expression {
	private JSG_Expression exp;

	public JSG_Indirection(JSG_Expression _exp) {
		this.exp = _exp;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Expression getExp() {
		return exp;
	}

}
