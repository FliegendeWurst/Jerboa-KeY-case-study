package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_Cast implements JSG_Expression {
	private JSG_Type typeName;
	private JSG_Expression expr;

	public JSG_Cast(JSG_Type type, JSG_Expression _expr) {
		this.typeName = type;
		this.expr = _expr;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Type getType() {
		return typeName;
	}

	public JSG_Expression getExpr() {
		return expr;
	}

}
