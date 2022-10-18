package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSApplyRule extends JSExpression {

	private JSRule rule;
	private ArrayList<JSRuleArg> args;

	public JSApplyRule(JSRule rule, Collection<JSRuleArg> args, int l, int col) {
		super(l, col);
		this.rule = rule;
		this.args = new ArrayList<>(args);
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public String getRuleName() {
		return rule.getName();
	}

	public JSRule getRule() {
		return rule;
	}

	public List<JSRuleArg> getArgs() {
		return args;
	}

}
