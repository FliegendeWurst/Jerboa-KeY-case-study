package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_ForEach extends JSG_2_Entity {
	private JSG_2_Type type;
	private String name;
	private JSG_2_Entity coll;
	private JSG_2_Entity body;

	public JSG_2_ForEach(JSG_2_Type type, String name, JSG_2_Entity coll, JSG_2_Entity body, int l, int col) {
		super(l, col);
		this.type = type;
		this.name = name;
		this.coll = coll;
		this.body = body;
	}

	public JSG_2_Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public JSG_2_Entity getColl() {
		return coll;
	}

	public JSG_2_Entity getBody() {
		return body;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
