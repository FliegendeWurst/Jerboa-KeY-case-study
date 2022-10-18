/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_FreeMarker extends JSG_Instruction {
	private boolean gmapHasDirectAccess;
	private JSG_Expression marker;

	public JSG_FreeMarker(boolean _gmapHasDirectAccess, JSG_Expression _marker, int l, int c) {
		super(l, c);
		gmapHasDirectAccess = _gmapHasDirectAccess;
		marker = _marker;
	}

	public boolean hasDirectAccessToGMap() {
		return gmapHasDirectAccess;
	}

	public JSG_Expression getMarker() {
		return marker;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
