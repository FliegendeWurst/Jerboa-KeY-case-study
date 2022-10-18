package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/*
 * est ce vraiment gratuit sur la verif du type et de le type de la value
 *
 *
 */
public class JSG_2_Declare extends JSG_2_Entity {

	private JSG_2_Type type;
	private String name;
	private JSG_2_Entity value;

	public JSG_2_Declare(JSG_2_Type type, String name, JSG_2_Entity value, int l, int col) {
		super(l, col);
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public JSG_2_Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public JSG_2_Entity getValue() {
		return value;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
