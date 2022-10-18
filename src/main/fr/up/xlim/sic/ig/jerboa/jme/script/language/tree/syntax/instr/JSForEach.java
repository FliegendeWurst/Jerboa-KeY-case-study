package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSForEach extends JSInstruction {
	private JSType type;
	private String name;
	private JSExpression coll;
	private JSBlock body;

	public JSForEach(JSType type, String name, JSExpression coll, JSBlock body, int l, int col) {
		super(l, col);
		this.type = type;
		this.name = name;
		this.coll = coll;
		this.body = body;
	}

	public JSType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public JSExpression getColl() {
		return coll;
	}

	public JSBlock getBody() {
		return body;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
