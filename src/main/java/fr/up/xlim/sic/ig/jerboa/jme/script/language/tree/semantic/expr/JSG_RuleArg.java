package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_RuleArg implements JSG_Expression {
	private JSG_Expression argvalue;
	private String argname;

	public JSG_RuleArg(String argname, JSG_Expression argvalue) {
		this.argvalue = argvalue;
		this.argname = argname;
	}

	public JSG_Expression getArgValue() {
		return argvalue;
	}

	public String getArgName() {
		return argname;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
