package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_Rule extends JSEntity implements JSG_Literal {

	private String name;
	private JMERule rule;

	public JSG_Rule(String _name, JMERule _rule, int l, int c) {
		super(l, c);
		this.name = _name;
		this.rule = _rule;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public String getName() {
		return name;
	}

	public JMERule getRule() {
		return rule;
	}

	@Override
	public String toString() {
		return "RULE<" + name + ">";
	}
}
