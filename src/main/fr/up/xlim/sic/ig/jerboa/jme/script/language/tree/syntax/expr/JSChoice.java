package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSChoice extends JSExpression {

	private ArrayList<JSApplyRule> rules;

	public JSChoice(Collection<JSApplyRule> alternatives, int l, int col) {
		super(l, col);
		this.rules = new ArrayList<>(alternatives);
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public List<JSApplyRule> getRules() {
		return rules;
	}

	public void addChoice(JSApplyRule rule) {
		rules.add(rule);
	}
}
