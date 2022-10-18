package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_LeftRuleNode extends JSG_Variable {
	private JMERule rule;

	public JSG_LeftRuleNode(JMERule rule, String paramName, int l, int col) {
		super(paramName, l, col);
		this.rule = rule;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Type getType() {
		return new JSG_TypeJerboaDart(line, column);
	}

	public JMERule getRule() {
		return rule;
	}

}
