/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_ExprInstruction extends JSG_Instruction {

	private JSG_Expression expr;

	/**
	 *
	 */
	public JSG_ExprInstruction(JSG_Expression expr) {
		super(-666, -666);
		this.expr = expr;
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

	public JSG_Expression getExpr() {
		return expr;
	}

}
