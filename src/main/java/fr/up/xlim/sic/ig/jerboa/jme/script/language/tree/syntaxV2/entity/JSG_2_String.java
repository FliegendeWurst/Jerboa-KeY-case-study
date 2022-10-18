package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_String extends JSG_2_Entity {

	private String value;

	public JSG_2_String(String val, int l, int col) {
		super(l, col);
		if (val == null)
			this.value = "";
		else
			this.value = val;
	}
	
	public String getValue() {
		return value;
	}
	

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
