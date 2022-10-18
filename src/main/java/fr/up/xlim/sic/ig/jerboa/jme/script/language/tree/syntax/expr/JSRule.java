package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSRule extends JSLiteral {

	private String name;

	public JSRule(String _name, int l, int col) {
		super(l, col);
		this.name = _name;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public String getName() {
		return name;
	}
}
