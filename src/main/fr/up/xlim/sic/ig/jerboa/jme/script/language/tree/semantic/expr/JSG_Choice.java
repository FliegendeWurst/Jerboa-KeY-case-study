package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_Choice extends JSEntity implements JSG_Expression {

	private ArrayList<JSG_Expression> rules;
	private String varResult;

	public JSG_Choice(Collection<JSG_Expression> alternatives, String resVar, int l, int col) {
		super(l, col);
		this.rules = new ArrayList<>(alternatives);
		this.varResult = resVar;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public List<JSG_Expression> getOptions() {
		return rules;
	}

	public String getVarResult() {
		return varResult;
	}

	public void addAlternativ(JSG_ApplyRule rule) {
		rules.add(rule);
	}
}
