package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSVariable extends JSExpression {
	private String name;

	public JSVariable(String name, int l, int col) {
		super(l, col);
		this.name = name;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "#VAR(" + name + ")";
	}

}
