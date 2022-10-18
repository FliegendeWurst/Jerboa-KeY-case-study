/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_New implements JSG_Expression {

	private JSG_Expression obj;

	public JSG_New(JSG_Expression obj) {
		this.obj = obj;
	}

	public JSG_Expression getExp() {
		return obj;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
