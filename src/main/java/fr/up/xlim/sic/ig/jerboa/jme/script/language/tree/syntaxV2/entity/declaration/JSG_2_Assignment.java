package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Assignment extends JSG_2_Entity {

	private JSG_2_Entity variable;
	private JSG_2_Entity value;

	public JSG_2_Assignment(JSG_2_Entity variable, JSG_2_Entity value, int l, int col) {
		super(l, col);
		this.variable = variable;
		this.value = value;
	}

	public JSG_2_Entity getVariable() {
		return variable;
	}

	public JSG_2_Entity getValue() {
		return value;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
