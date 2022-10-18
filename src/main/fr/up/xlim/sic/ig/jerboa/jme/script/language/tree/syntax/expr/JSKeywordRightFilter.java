/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSKeywordRightFilter extends JSExpression {

	/**
	 *
	 */
	public JSKeywordRightFilter(int l, int c) {
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
