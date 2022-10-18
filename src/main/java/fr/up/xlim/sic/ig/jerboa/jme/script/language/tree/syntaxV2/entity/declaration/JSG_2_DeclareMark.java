package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/*
 * est ce vraiment gratuit sur la verif du type et de le type de la value
 *
 *
 */
public class JSG_2_DeclareMark extends JSG_2_Entity {

	private String name;

	public JSG_2_DeclareMark(String name, int l, int col) {
		super(l, col);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
