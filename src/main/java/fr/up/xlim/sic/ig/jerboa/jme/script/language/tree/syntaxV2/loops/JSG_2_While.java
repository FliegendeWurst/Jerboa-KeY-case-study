package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_While extends JSG_2_Entity {
	private JSG_2_Entity corps;
	private JSG_2_Entity condition;
	
	public JSG_2_While(JSG_2_Entity cond, JSG_2_Entity corps, int l, int col) {
		super(l, col);
		this.condition = cond;
		this.corps = corps;
	}

	public JSG_2_Entity getBody() {
		return corps;
	}

	public JSG_2_Entity getCondition() {
		return condition;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
