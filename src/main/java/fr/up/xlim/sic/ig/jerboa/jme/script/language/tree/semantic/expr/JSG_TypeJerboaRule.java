package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_TypeJerboaRule extends JSG_Type implements JSG_TypeObject {
	String ruleName;

	public JSG_TypeJerboaRule(String _ruleName, int l, int col) {
		super("JerboaRule", l, col);
		ruleName = _ruleName;
	}

	public String getRuleName() {
		return ruleName;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	@Override
	public String toString() {
		return "TypeJerboaRule<" + ruleName + ">";
	}
}
