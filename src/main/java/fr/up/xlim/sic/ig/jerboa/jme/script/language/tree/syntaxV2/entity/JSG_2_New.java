/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_New extends JSG_2_Entity {

	private JSG_2_Entity obj;

	public JSG_2_New(JSG_2_Entity obj, int l, int c) {
		super(l,c);
		this.obj = obj;
	}

	public JSG_2_Entity getExp() {
		return obj;
	}


	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
