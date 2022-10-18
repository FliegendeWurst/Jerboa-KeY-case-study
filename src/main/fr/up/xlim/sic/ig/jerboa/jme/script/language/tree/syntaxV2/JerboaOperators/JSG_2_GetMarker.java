/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_GetMarker extends JSG_2_Entity {
	private boolean gmapHasDirectAccess;

	public JSG_2_GetMarker(boolean _gmapHasDirectAccess, int l, int c) {
		super(l,c);
		gmapHasDirectAccess = _gmapHasDirectAccess;
	}

	public boolean hasDirectAccessToGMap() {
		return gmapHasDirectAccess;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
