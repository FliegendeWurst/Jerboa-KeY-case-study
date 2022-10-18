package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_While extends JSG_Instruction {
	private JSG_Instruction corps;
	private JSG_Expression condition;
	
	public JSG_While(JSG_Expression cond, JSG_Instruction corps, int l, int col) {
		super(l, col);
		this.condition = cond;
		this.corps = corps;
	}

	public JSG_Instruction getCorps() {
		return corps;
	}

	public JSG_Expression getCondition() {
		return condition;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
