package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_ApplyRule extends JSG_2_Entity {
	public enum JSG_2_RuleReturnType {
		NONE, FULL
	};

	private JMERule rule;
	private JSG_2_Entity ruleExpr;
	private ArrayList<JSG_2_Entity> args;
	private JSG_2_RuleReturnType returnType;

	public JSG_2_ApplyRule(JMERule rule, JSG_2_Entity expr, Collection<JSG_2_Entity> args,
			JSG_2_RuleReturnType _returnType, int l, int col) {
		super(l, col);
		this.rule = rule;
		ruleExpr = expr;
		this.args = new ArrayList<>();
		this.args.addAll(args);
		returnType = _returnType;
	}

	public JSG_2_Entity getRuleExpr() {
		return ruleExpr;
	}

	public JMERule getRule() {
		return rule;
	}

	public JSG_2_RuleReturnType getReturnType() {
		return returnType;
	}

	public List<JSG_2_Entity> getArgs() {
		return args;
	}


	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
