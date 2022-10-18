package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public abstract class JSG_Instruction extends JSEntity implements JSG_Entity {
	public JSG_Instruction(int l, int c) {
		super(l, c);
	}

	public abstract <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E;
}
