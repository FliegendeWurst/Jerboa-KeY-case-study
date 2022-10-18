package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_Break extends JSG_Instruction {

	public JSG_Break(int l, int col) {
		super(l, col);
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
