package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_ForEach extends JSG_Instruction {
	private JSG_Type type;
	private String name;
	private JSG_Expression coll;
	private JSG_Instruction body;

	public JSG_ForEach(JSG_Type type, String name, JSG_Expression coll, JSG_Instruction body, int l, int col) {
		super(l, col);
		this.type = type;
		this.name = name;
		this.coll = coll;
		this.body = body;
	}

	public JSG_Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public JSG_Expression getColl() {
		return coll;
	}

	public JSG_Instruction getBody() {
		return body;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
