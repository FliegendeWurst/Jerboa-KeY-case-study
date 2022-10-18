/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

/**
 * @author Hakim
 *
 */
public class JSKeywordGmap extends JSExpression {

	/**
	 *
	 */
	public JSKeywordGmap(int l, int c) {
		super(l, c);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * up.jerboa.core.script.expr.JSExpression#visit(up.jerboa.core.script.expr.
	 * tool.JSExprVisitor)
	 */
	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
