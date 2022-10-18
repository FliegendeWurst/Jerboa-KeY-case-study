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
public class JSG_Return extends JSG_Instruction {

	private JSG_Expression expr;

	/**
	 *
	 */
	public JSG_Return(int l, int c, JSG_Expression expr) {
		super(l, c);
		this.expr = expr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * up.jerboa.core.script.expr.JSExpression#visit(up.jerboa.core.script.expr.
	 * tool.JSExprVisitor)
	 */
	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Expression getExpression() {
		return expr;
	}

}
