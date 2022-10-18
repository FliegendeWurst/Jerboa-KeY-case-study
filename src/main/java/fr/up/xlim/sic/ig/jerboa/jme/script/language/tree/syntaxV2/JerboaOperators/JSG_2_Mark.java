package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Mark extends JSG_2_Entity {

	private JSG_2_Entity left;
	private JSG_2_Entity mark;

	public JSG_2_Mark(JSG_2_Entity left, JSG_2_Entity mark, int l, int col) {
		super(l, col);
		this.left = left;
		this.mark = mark;
	}

	public JSG_2_Entity getLeft() {
		return left;
	}

	public JSG_2_Entity getMark() {
		return mark;
	}
	
	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
