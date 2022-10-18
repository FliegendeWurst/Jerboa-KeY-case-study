/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_GetMarker implements JSG_Expression {
	private boolean gmapHasDirectAccess;

	public JSG_GetMarker(boolean _gmapHasDirectAccess) {
		gmapHasDirectAccess = _gmapHasDirectAccess;
	}

	public boolean hasDirectAccessToGMap() {
		return gmapHasDirectAccess;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
