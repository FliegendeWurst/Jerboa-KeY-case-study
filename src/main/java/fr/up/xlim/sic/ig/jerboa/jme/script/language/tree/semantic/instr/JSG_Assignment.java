package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_Assignment extends JSG_Instruction {

	private JSG_Expression variable;
	private JSG_Expression value;

	public JSG_Assignment(JSG_Expression variable, JSG_Expression value, int l, int col) {
		super(l, col);
		this.variable = variable;
		this.value = value;
	}

	public JSG_Expression getVariable() {
		return variable;
	}

	public JSG_Expression getValue() {
		return value;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
