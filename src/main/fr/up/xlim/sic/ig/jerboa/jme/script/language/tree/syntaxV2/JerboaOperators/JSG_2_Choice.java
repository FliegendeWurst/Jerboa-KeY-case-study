package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Choice extends JSG_2_Entity {

	private ArrayList<JSG_2_Entity> rules;
	private String varResult;

	public JSG_2_Choice(Collection<JSG_2_Entity> alternatives, String resVar, int l, int col) {
		super(l, col);
		this.rules = new ArrayList<>(alternatives);
		this.varResult = resVar;
	}

	public List<JSG_2_Entity> getOptions() {
		return rules;
	}

	public String getVarResult() {
		return varResult;
	}

	public void addAlternativ(JSG_2_ApplyRule rule) {
		rules.add(rule);
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
