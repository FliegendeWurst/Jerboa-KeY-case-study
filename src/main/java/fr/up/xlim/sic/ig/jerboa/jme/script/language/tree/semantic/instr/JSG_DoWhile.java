/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

/**
 * @author Hakim
 *
 */
public class JSG_DoWhile extends JSG_Instruction {

	private JSG_Expression condition;
	private JSG_Instruction corps;

	public JSG_DoWhile(JSG_Expression cond, JSG_Instruction body, int l, int col) {
		super(l, col);
		this.corps = body;
		this.condition = cond;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * up.jerboa.core.script.JSInstruction#visit(up.jerboa.core.script.expr.tool
	 * .JSInstVisitor)
	 */
	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Instruction getBody() {
		return corps;
	}

	public JSG_Expression getCondition() {
		return condition;
	}
}
