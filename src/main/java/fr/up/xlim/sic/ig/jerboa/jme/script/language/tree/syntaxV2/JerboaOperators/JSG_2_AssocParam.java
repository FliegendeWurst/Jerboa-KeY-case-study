package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_AssocParam extends JSG_2_Entity {
	private JSG_2_Entity paramvalue;
	private String paramname;
	private JSG_2_Rule rule;

	public JSG_2_AssocParam(JSG_2_Rule rule, JSG_2_Entity paramvalue, String paramname, int l, int col) {
		super(l, col);
		this.paramvalue = paramvalue;
		this.paramname = paramname;
		this.rule = rule;
	}

	public JSG_2_Entity getParamValue() {
		return paramvalue;
	}

	public String getParamName() {
		return paramname;
	}

	public String getRuleName() {
		return rule.getName();
	}

	public JSG_2_Rule getRule() {
		return rule;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
