/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * @author Valentin
 *
 */
public class JSReturn extends JSInstruction {

	private JSExpression expr;

	public JSReturn(JSExpression expr, int l, int col) {
		super(l, col);
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
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getExpression() {
		return expr;
	}
}
