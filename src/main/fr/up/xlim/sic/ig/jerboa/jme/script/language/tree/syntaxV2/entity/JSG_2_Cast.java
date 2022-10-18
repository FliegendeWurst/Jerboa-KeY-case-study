package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Cast extends JSG_2_Entity {
	private JSG_2_Type typeName;
	private JSG_2_Entity expr;

	public JSG_2_Cast(JSG_2_Type type, JSG_2_Entity _expr, int l, int c) {
		super(l,c);
		this.typeName = type;
		this.expr = _expr;
	}

	public JSG_2_Type getType() {
		return typeName;
	}

	public JSG_2_Entity getExpr() {
		return expr;
	}
	
	@Override
	public String toString() {
		return "JSG_2_Cast{ [" + typeName + "] ("+expr+") }";
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
