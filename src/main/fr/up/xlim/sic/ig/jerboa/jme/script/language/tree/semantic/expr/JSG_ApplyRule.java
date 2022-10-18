package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_ApplyRule extends JSEntity implements JSG_Expression {
	public enum JSG_RuleReturnType {
		NONE, FULL
	};

	private JMERule rule;
	private JSG_Expression ruleExpr;
	private ArrayList<JSG_Expression> args;
	private JSG_RuleReturnType returnType;

	public JSG_ApplyRule(JMERule rule, JSG_Expression expr, Collection<JSG_Expression> args,
			JSG_RuleReturnType _returnType, int l, int col) {
		super(l, col);
		this.rule = rule;
		ruleExpr = expr;
		this.args = new ArrayList<>();
		this.args.addAll(args);
		returnType = _returnType;
	}

	public JSG_Expression getRuleExpr() {
		return ruleExpr;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JMERule getRule() {
		return rule;
	}

	public JSG_RuleReturnType getReturnType() {
		return returnType;
	}

	public List<JSG_Expression> getArgs() {
		return args;
	}


}
