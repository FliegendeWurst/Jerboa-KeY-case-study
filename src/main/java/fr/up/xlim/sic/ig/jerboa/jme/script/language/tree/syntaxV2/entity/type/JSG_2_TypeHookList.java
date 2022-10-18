package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_TypeHookList extends JSG_2_Type{

	private JMERule rule;
	
	public JSG_2_TypeHookList(JMERule rule, int l, int c) {
		super("TypeHookList", l, c);
		this.rule = rule;
	}
	
	public JMERule getRule() {
		return rule;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
	
	@Override
	public String toString() {
		return "HookList";
	}
}
