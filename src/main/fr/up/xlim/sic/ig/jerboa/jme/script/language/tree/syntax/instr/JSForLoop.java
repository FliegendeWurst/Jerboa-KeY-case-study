package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * This class represents an classic for loop : "for (int i =0; i<x; i++){}"
 *
 * @author valentin
 *
 */
public class JSForLoop extends JSInstruction {
	private JSInstruction init;
	private JSExpression cond;
	private JSInstruction step;
	private JSBlock body;

	public JSForLoop(JSInstruction init, JSExpression cond, JSInstruction step, JSBlock body, int l, int col) {
		super(l, col);
		this.init = init;
		this.cond = cond;
		this.step = step;
		this.body = body;
	}

	public JSInstruction getInit() {
		return init;
	}

	public JSExpression getCond() {
		return cond;
	}

	public JSInstruction getStep() {
		return step;
	}

	public JSBlock getBody() {
		return body;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
