package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_For extends JSG_Instruction {

	private String variable;
	private JSG_Type type;
	private JSG_Expression start;
	private JSG_Expression end;
	private JSG_Instruction step;
	private JSG_Instruction body;

	public JSG_For(JSG_Type type, String variable, JSG_Expression start, JSG_Expression end, JSG_Instruction step,
			JSG_Instruction body, int l, int col) {
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

	public JSG_Type getType() {
		return type;
	}

	public JSG_Expression getStart() {
		return start;
	}

	public JSG_Expression getEnd() {
		return end;
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
