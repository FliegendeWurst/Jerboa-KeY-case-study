package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_TypeRule extends JSG_2_Type{

	public JSG_2_TypeRule(int l, int c) {
		super("TypeRule", l, c);
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	@Override
	public String toString() {
		return "JerboaRule";
	}
}
