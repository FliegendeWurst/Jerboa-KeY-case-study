package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_ForLoop extends JSG_2_Entity {
	private JSG_2_Entity init;
	private JSG_2_Entity cond;
	private JSG_2_Entity step;
	private JSG_2_Entity body;

	public JSG_2_ForLoop(JSG_2_Entity init, JSG_2_Entity cond, JSG_2_Entity step, JSG_2_Entity body, int l,
			int col) {
		super(l, col);
		this.init = init;
		this.cond = cond;
		this.step = step;
		this.body = body;
	}

	public JSG_2_Entity getInit() {
		return init;
	}

	public JSG_2_Entity getCond() {
		return cond;
	}

	public JSG_2_Entity getStep() {
		return step;
	}

	public JSG_2_Entity getBody() {
		return body;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
