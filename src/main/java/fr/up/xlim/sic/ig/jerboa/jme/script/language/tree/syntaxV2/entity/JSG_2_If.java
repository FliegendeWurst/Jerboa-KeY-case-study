package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_If extends JSG_2_Entity {

	private JSG_2_Entity condition;
	private JSG_2_Entity consequence;
	private JSG_2_Entity alternant;

	public JSG_2_If(JSG_2_Entity cond, JSG_2_Entity cons, JSG_2_Entity alt, int l, int col) {
		super(l, col);
		this.condition = cond;
		this.consequence = cons;
		this.alternant = alt;
	}

	public JSG_2_Entity getCondition() {
		return condition;
	}

	public JSG_2_Entity getConsequence() {
		return consequence;
	}

	public JSG_2_Entity getAlternant() {
		return alternant;
	}


	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
