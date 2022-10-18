/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_AddInHookList implements JSG_Expression {

	private ArrayList<JSG_Expression> arguments;

	public JSG_AddInHookList(Collection<JSG_Expression> args) {
		arguments = new ArrayList<>(args);
	}

	public ArrayList<JSG_Expression> getArgs() {
		return arguments;
	}


	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
