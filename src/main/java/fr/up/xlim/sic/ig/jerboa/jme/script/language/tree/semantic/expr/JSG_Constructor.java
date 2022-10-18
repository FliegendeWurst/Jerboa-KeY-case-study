/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_Constructor implements JSG_Expression {

	private JSG_Expression name;

	private ArrayList<JSG_Expression> arguments;

	public JSG_Constructor(JSG_Expression name, ArrayList<JSG_Expression> args) {
		this.name = name;
		arguments = new ArrayList<>(args);
	}

	public JSG_Expression getName() {
		return name;
	}

	public ArrayList<JSG_Expression> getArguments() {
		return arguments;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
