/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_Call extends JSG_2_Entity {

	private String name;

	private List<JSG_2_Entity> arguments;

	public JSG_2_Call(String name, List<JSG_2_Entity> args, int line, int col) {
		super(line, col);
		this.name = name;
		arguments = new ArrayList<>(args);
	}

	public String getName() {
		return name;
	}

	public List<JSG_2_Entity> getArguments() {
		return arguments;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
