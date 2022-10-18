/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_KeywordGmap implements JSG_Expression {
	private boolean isDirectlyAccessible;
	private int line;
	private int col;

	/**
	 *
	 */
	public JSG_KeywordGmap(boolean _isDirectlyAccessible, int l, int c) {
		line = l;
		col = c;
		isDirectlyAccessible = _isDirectlyAccessible;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * up.jerboa.core.script.expr.JSExpression#visit(up.jerboa.core.script.expr.
	 * tool.JSExprVisitor)
	 */
	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public boolean hasDirectAccess() {
		return isDirectlyAccessible;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return col;
	}

}
