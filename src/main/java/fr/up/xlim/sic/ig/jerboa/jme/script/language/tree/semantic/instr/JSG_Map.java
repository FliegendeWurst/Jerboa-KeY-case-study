package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_Map extends JSG_Instruction {

	private JSG_Type type;
	private String var;
	private JSG_Instruction body;
	private JSG_Expression expr;

	public JSG_Map(JSG_Type type, String var, JSG_Instruction body, JSG_Expression expr, int l, int col) {
		super(l, col);
		this.type = type;
		this.var = var;
		this.body = body;
		this.expr = expr;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Type getType() {
		return type;
	}

	public String getVar() {
		return var;
	}

	public JSG_Instruction getBody() {
		return body;
	}

	public JSG_Expression getExpr() {
		return expr;
	}
}
