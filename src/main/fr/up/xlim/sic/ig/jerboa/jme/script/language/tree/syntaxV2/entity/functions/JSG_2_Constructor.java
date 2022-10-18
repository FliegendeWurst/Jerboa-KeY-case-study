/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions;

import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_Constructor extends JSG_2_Entity {

	private JSG_2_Entity name;

	private ArrayList<JSG_2_Entity> arguments;

	public JSG_2_Constructor(JSG_2_Entity name, ArrayList<JSG_2_Entity> args, int l, int c) {
		super(l,c);
		this.name = name;
		arguments = new ArrayList<>(args);
	}

	public JSG_2_Entity getName() {
		return name;
	}

	public ArrayList<JSG_2_Entity> getArguments() {
		return arguments;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
