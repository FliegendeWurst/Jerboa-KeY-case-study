package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_TypeJerboaHookList extends JSG_Type implements JSG_TypeObject {
	JMERule rule;

	public JSG_TypeJerboaHookList(JMERule r, int l, int col) {
		super("JerboaHookNode", l, col);
		rule = r;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
