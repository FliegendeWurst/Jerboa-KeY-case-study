package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Not extends JSG_2_Entity {

	private JSG_2_Entity expr;

	public JSG_2_Not(JSG_2_Entity expr, int l, int col) {
		super(l, col);
		this.expr = expr;
	}

	public JSG_2_Entity getExpr() {
		return expr;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
