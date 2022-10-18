package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSType extends JSExpression {
	private String type;

	public String getType() {
		return type;
	}

	public JSType(String type, int l, int c) {
		super(l, c);
		this.type = type;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
