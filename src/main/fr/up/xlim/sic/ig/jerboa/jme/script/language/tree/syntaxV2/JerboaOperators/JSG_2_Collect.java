package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Orbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Collect extends JSG_2_Entity {

	private JSG_2_Orbit orbit;
	private JSG_2_Orbit sorb;
	private JSG_2_Entity node;
	private boolean gmapHasDirectAccess;

	public JSG_2_Collect(JSG_2_Orbit orbit, JSG_2_Orbit sorb, JSG_2_Entity node, boolean _gmapHasDirectAccess, int l,
			int col) {
		super(l, col);
		this.orbit = orbit;
		this.sorb = sorb;
		this.node = node;
		gmapHasDirectAccess = _gmapHasDirectAccess;
	}
	
	public JSG_2_Orbit getOrbit() {
		return orbit;
	}

	public JSG_2_Orbit getSubOrbit() {
		return sorb;
	}

	public JSG_2_Entity getNode() {
		return node;
	}

	public boolean hasSubOrbit() {
		return (sorb != null) && (sorb.getDimensions().size() > 0);
	}

	public boolean gmapHasDirectAccess() {
		return gmapHasDirectAccess;
	}

	@Override
	public String toString() {
		return "COLLECT" + orbit + "(" + node + ")";
	}


	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
