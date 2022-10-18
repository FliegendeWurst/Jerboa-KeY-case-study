package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Indirection extends JSG_2_Entity {
	private JSG_2_Entity exp;

	public JSG_2_Indirection(JSG_2_Entity _exp, int l, int c) {
		super(l, c);
		this.exp = _exp;
	}
	
	public JSG_2_Entity getExp() {
		return exp;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
