package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Unreference extends JSG_2_Entity {
	private JSG_2_Entity exp;

	public JSG_2_Unreference(JSG_2_Entity exp, int l, int c) {
		super(l,c);
		this.exp = exp;
	}

	public JSG_2_Entity getExp() {
		return exp;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
