package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSString extends JSLiteral {

	private String value;

	public JSString(String val, int l, int col) {
		super(l, col);
		if (val == null)
			this.value = "";
		else
			this.value = val;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public String getValue() {
		return value;
	}
}
