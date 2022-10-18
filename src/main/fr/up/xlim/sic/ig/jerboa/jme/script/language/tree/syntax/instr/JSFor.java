package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * This class represents an ADA-like for loop
 *
 * @author valentin
 *
 */
public class JSFor extends JSInstruction {

	private String variable;
	private JSType type;
	private JSExpression start;
	private JSExpression end;
	private JSInstruction step;
	private JSBlock body;

	public JSFor(JSType type, String variable, JSExpression start, JSExpression end, JSInstruction step, JSBlock body,
			int l, int col) {
		super(l, col);
		this.type = type;
		this.variable = variable;
		this.start = start;
		this.end = end;
		this.step = step;
		this.body = body;
	}

	public String getVariable() {
		return variable;
	}

	public JSType getType() {
		return type;
	}

	public JSExpression getStart() {
		return start;
	}

	public JSExpression getEnd() {
		return end;
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
