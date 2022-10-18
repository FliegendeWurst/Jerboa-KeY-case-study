package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Rule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_AssocParam extends JSG_Instruction {
	private JSG_Expression paramvalue;
	private String paramname;
	private JSG_Rule rule;

	public JSG_AssocParam(JSG_Rule rule, JSG_Expression paramvalue, String paramname, int l, int col) {
		super(l, col);
		this.paramvalue = paramvalue;
		this.paramname = paramname;
		this.rule = rule;
	}

	public JSG_Expression getParamValue() {
		return paramvalue;
	}

	public String getParamName() {
		return paramname;
	}

	public String getRuleName() {
		return rule.getName();
	}

	public JSG_Rule getRule() {
		return rule;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
