package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSMap extends JSInstruction {

	private JSType type;
	private String var;
	private JSInstruction body;
	private JSExpression expr;

	public JSMap(JSType type, String var, JSInstruction body, JSExpression expr, int l, int col) {
		super(l, col);
		this.line = l;
		this.column = col;
		this.type = type;
		this.var = var;
		this.body = body;
		this.expr = expr;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSType getType() {
		return type;
	}

	public String getVar() {
		return var;
	}

	public JSInstruction getBody() {
		return body;
	}

	public JSExpression getExpr() {
		return expr;
	}
}
