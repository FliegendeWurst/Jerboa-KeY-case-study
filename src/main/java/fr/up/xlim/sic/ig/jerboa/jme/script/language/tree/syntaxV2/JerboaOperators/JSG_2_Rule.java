package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Rule extends JSG_2_Entity {

	private String name;
	private JMERule rule;

	public JSG_2_Rule(String _name, JMERule _rule, int l, int c) {
		super(l, c);
		this.name = _name;
		this.rule = _rule;
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

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
