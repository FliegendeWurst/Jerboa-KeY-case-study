package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

/*
 * est ce vraiment gratuit sur la verif du type et de le type de la value
 *
 *
 */
public class JSG_DeclareMark extends JSG_Instruction {

	private String name;

	public JSG_DeclareMark(String name, int l, int col) {
		super(l, col);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
