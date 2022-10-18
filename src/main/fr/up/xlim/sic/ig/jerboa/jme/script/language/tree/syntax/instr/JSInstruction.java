package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public abstract class JSInstruction extends JSEntity {
	public JSInstruction(int l, int c) {
		super(l, c);
	}

	public abstract <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E;
}
