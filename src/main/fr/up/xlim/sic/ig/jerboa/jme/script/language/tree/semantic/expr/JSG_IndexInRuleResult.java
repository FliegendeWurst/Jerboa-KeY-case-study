package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_IndexInRuleResult implements JSG_Expression {

	private JSG_Expression variable;
	private JSG_Expression index_first;
	private JSG_Expression index_second;

	public JSG_IndexInRuleResult(JSG_Expression var, JSG_Expression indexFirst, JSG_Expression indexSecond) {
		this.variable = var;
		this.index_first = indexFirst;
		this.index_second = indexSecond;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Expression getVariable() {
		return variable;
	}

	public JSG_Expression getIndexFirst() {
		return index_first;
	}
	
	public JSG_Expression getIndexSecond() {
		return index_second;
	}

}
