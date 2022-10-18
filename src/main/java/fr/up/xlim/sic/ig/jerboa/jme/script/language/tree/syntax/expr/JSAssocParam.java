package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSAssocParam extends JSInstruction {
	private JSExpression paramvalue;
	private String paramname;
	private JSRule rule;

	public JSAssocParam(JSRule rule, JSExpression paramvalue, String paramname, int l, int col) {
		super(l, col);
		this.paramvalue = paramvalue;
		this.paramname = paramname;
		this.rule = rule;
	}

	public JSExpression getParamValue() {
		return paramvalue;
	}

	public String getParamName() {
		return paramname;
	}

	public String getRuleName() {
		return rule.getName();
	}

	public JSRule getRule() {
		return rule;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
