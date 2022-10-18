package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_ForLoop extends JSG_Instruction {
	private JSG_Instruction init;
	private JSG_Expression cond;
	private JSG_Instruction step;
	private JSG_Instruction body;

	public JSG_ForLoop(JSG_Instruction init, JSG_Expression cond, JSG_Instruction step, JSG_Instruction body, int l,
			int col) {
		super(l, col);
		this.init = init;
		this.cond = cond;
		this.step = step;
		this.body = body;
	}

	public JSG_Instruction getInit() {
		return init;
	}

	public JSG_Expression getCond() {
		return cond;
	}

	public JSG_Instruction getStep() {
		return step;
	}

	public JSG_Instruction getBody() {
		return body;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
