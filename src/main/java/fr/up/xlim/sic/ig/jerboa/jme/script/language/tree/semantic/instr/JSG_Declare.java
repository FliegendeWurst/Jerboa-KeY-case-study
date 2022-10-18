package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

/*
 * est ce vraiment gratuit sur la verif du type et de le type de la value
 *
 *
 */
public class JSG_Declare extends JSG_Instruction {

	private JSG_Type type;
	private String name;
	private JSG_Expression value;

	public JSG_Declare(JSG_Type type, String name, JSG_Expression value, int l, int col) {
		super(l, col);
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public JSG_Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public JSG_Expression getValue() {
		return value;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
