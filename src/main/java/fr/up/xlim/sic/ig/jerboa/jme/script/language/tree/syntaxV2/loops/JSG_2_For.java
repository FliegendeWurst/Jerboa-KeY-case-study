package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_For extends JSG_2_Entity {

	private String variable;
	private JSG_2_Type type;
	private JSG_2_Entity start;
	private JSG_2_Entity end;
	private JSG_2_Entity step;
	private JSG_2_Entity body;

	public JSG_2_For(JSG_2_Type type, String variable, JSG_2_Entity start, JSG_2_Entity end, JSG_2_Entity step,
			JSG_2_Entity body, int l, int col) {
		super(l, col);
		this.type = type;
		this.variable = variable;
		this.start = start;
		this.end = end;
		this.step = step;
		this.body = body;
	}

	public String getVariable() {
		return variable;
	}

	public JSG_2_Type getType() {
		return type;
	}

	public JSG_2_Entity getStart() {
		return start;
	}

	public JSG_2_Entity getEnd() {
		return end;
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
