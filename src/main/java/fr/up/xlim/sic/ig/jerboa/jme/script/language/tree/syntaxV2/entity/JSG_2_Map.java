package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Map extends JSG_2_Entity {

	private JSG_2_Type type;
	private String var;
	private JSG_2_Entity body;
	private JSG_2_Entity expr;

	public JSG_2_Map(JSG_2_Type type, String var, JSG_2_Entity body, JSG_2_Entity expr, int l, int col) {
		super(l, col);
		this.type = type;
		this.var = var;
		this.body = body;
		this.expr = expr;
	}

	public JSG_2_Type getType() {
		return type;
	}

	public String getVar() {
		return var;
	}

	public JSG_2_Entity getBody() {
		return body;
	}

	public JSG_2_Entity getExpr() {
		return expr;
	}
	

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
