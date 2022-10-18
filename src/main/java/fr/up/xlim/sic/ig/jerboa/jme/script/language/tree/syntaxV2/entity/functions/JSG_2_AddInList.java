/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_AddInList extends JSG_2_Entity {

	private ArrayList<JSG_2_Entity> arguments;
	private JSG_2_Type list;

	public JSG_2_AddInList(Collection<JSG_2_Entity> args, JSG_2_Type listType, int l, int c) {
		super(l,c);
		arguments = new ArrayList<>(args);
		list = listType;
	}

	public ArrayList<JSG_2_Entity> getArgs() {
		return arguments;
	}
	
	public JSG_2_Entity getList() {
		return list;
	}


	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
