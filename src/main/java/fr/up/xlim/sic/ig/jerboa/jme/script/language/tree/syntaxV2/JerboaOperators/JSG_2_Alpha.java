package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Alpha extends JSG_2_Entity {

	private JSG_2_Entity node;
	private JSG_2_Entity dim;

	public JSG_2_Alpha(JSG_2_Entity node, JSG_2_Entity dim, int l, int col) {
		super(l, col);
		this.node = node;
		this.dim = dim;
	}

	public JSG_2_Entity getNode() {
		return node;
	}

	public JSG_2_Entity getDim() {
		return dim;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
