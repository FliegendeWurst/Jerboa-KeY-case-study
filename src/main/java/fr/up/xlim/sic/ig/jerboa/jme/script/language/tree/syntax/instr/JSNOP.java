package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSNOP extends JSInstruction {

	public JSNOP(int l, int col) {
		super(l, col);
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
