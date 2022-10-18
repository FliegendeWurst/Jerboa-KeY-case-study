package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_If extends JSG_Instruction {

	private JSG_Expression condition;
	private JSG_Instruction consequence;
	private JSG_Instruction alternant;

	public JSG_If(JSG_Expression cond, JSG_Instruction cons, JSG_Instruction alt, int l, int col) {
		super(l, col);
		this.condition = cond;
		this.consequence = cons;
		this.alternant = alt;
	}

	public JSG_Expression getCondition() {
		return condition;
	}

	public JSG_Instruction getConsequence() {
		return consequence;
	}

	public JSG_Instruction getAlternant() {
		return alternant;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
