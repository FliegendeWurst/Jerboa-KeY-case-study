/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_Print extends JSG_2_Entity {

	private ArrayList<JSG_2_Entity> arguments;

	public JSG_2_Print(Collection<JSG_2_Entity> args, int l, int c) {
		super(l, c);
		arguments = new ArrayList<>(args);
	}

	public ArrayList<JSG_2_Entity> getArguments() {
		return arguments;
	}


	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
