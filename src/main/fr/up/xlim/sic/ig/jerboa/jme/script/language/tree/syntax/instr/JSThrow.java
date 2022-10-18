/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * @author Hakim
 *
 */
public class JSThrow extends JSInstruction {

	private JSExpression expr;

	/**
	 *
	 */
	public JSThrow(JSExpression expr) {
		super(-1, -1);
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
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getExpr() {
		return expr;
	}

}
