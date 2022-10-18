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
public class JSG_2_FreeMarker extends JSG_2_Entity {
	private boolean gmapHasDirectAccess;
	private JSG_2_Entity marker;

	public JSG_2_FreeMarker(boolean _gmapHasDirectAccess, JSG_2_Entity _marker, int l, int c) {
		super(l, c);
		gmapHasDirectAccess = _gmapHasDirectAccess;
		marker = _marker;
	}

	public boolean hasDirectAccessToGMap() {
		return gmapHasDirectAccess;
	}

	public JSG_2_Entity getMarker() {
		return marker;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
