package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSAssignment extends JSInstruction {

	private JSExpression variable;
	private JSExpression value;

	public JSAssignment(JSExpression variable, JSExpression value, int l, int col) {
		super(l, col);
		this.variable = variable;
		this.value = value;
	}

	public JSExpression getVariable() {
		return variable;
	}

	public JSExpression getValue() {
		return value;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
