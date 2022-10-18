package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Catch extends JSG_2_Entity {

	private JSG_2_Entity block;
	private JSG_2_Entity decl;

	public JSG_2_Catch(JSG_2_Entity _block, JSG_2_Entity _decl, int l, int col) {
		super(l, col);
		block = _block;
		decl = _decl;
	}

	public JSG_2_Entity getBlock() {
		return block;
	}

	public JSG_2_Entity getDeclar() {
		return decl;
	}
	

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
