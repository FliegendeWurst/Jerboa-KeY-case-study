package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Index extends JSG_2_Entity {

	private JSG_2_Entity variable;
	private JSG_2_Entity index;

	public JSG_2_Index(JSG_2_Entity var, JSG_2_Entity index, int l, int c) {
		super(l,c);
		this.variable = var;
		this.index = index;
	}

	public JSG_2_Entity getVariable() {
		return variable;
	}

	public JSG_2_Entity getIndex() {
		return index;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
