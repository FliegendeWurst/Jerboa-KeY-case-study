/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSNew extends JSExpression {

	private JSExpression obj;

	public JSNew(JSExpression obj, int l, int c) {
		super(l, c);
		this.obj = obj;
	}

	public JSExpression getExp() {
		return obj;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
