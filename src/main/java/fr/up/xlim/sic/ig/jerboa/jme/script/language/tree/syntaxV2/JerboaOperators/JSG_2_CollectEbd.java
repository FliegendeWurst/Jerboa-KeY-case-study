package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Orbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_CollectEbd extends JSG_2_Entity {

	private JSG_2_Orbit orbit;
	private String embedding;
	private JSG_2_Entity node;
	private boolean gmapHasDirectAccess;

	public JSG_2_CollectEbd(JSG_2_Orbit orbit, String ebd, JSG_2_Entity node,
			boolean _gmapHasDirectAccess, int l, int col) {
		super(l, col);
		this.orbit = orbit;
		this.embedding = ebd;
		this.node = node;
		this.gmapHasDirectAccess = _gmapHasDirectAccess;
	}

	public JSG_2_Orbit getOrbit() {
		return orbit;
	}


	public String getEmbedding() {
		return embedding;
	}

	public JSG_2_Entity getNode() {
		return node;
	}

	public boolean gmapHasDirectAccess() {
		return gmapHasDirectAccess;
	}

	@Override
	public String toString() {
		return "COLLECT_EBD" + orbit + "_" + embedding + "(" + node + ")";
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
