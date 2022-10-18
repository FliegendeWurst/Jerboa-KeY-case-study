package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_IndexNodeInGmap implements JSG_Expression {

	//	private JSG_Expression variable;
	private JSG_Expression index;

	public JSG_IndexNodeInGmap( JSG_Expression index) {
		//		this.variable = var;
		this.index = index;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	//	public JSG_Expression getVariable() {
	//		return variable;
	//	}

	public JSG_Expression getIndex() {
		return index;
	}

}
