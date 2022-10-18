package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSRuleArg extends JSExpression {
	private JSExpression argvalue;
	private String argname;

	public JSRuleArg(JSExpression argvalue, String argname, int l, int col) {
		super(l, col);
		this.argvalue = argvalue;
		this.argname = argname;
	}

	public JSExpression getArgValue() {
		return argvalue;
	}

	public String getArgName() {
		return argname;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
