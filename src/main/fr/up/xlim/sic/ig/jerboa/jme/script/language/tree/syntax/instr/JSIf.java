package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSIf extends JSInstruction {

	private JSExpression condition;
	private JSInstruction consequence;
	private JSInstruction alternant;

	public JSIf(JSExpression cond, JSInstruction cons, JSInstruction alt, int l, int col) {
		super(l, col);
		this.condition = cond;
		this.consequence = cons;
		this.alternant = alt;
	}

	public JSExpression getCondition() {
		return condition;
	}

	public JSInstruction getConsequence() {
		return consequence;
	}

	public JSInstruction getAlternant() {
		return alternant;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
