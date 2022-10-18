/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

/**
 * @author Hakim
 *
 */
public class JSCall extends JSExpression {

	private String name;

	private ArrayList<JSExpression> arguments;

	public JSCall(String name, Collection<JSExpression> args, int l, int col) {
		super(l, col);
		this.name = name;
		arguments = new ArrayList<>(args);
	}

	public String getName() {
		return name;
	}

	public ArrayList<JSExpression> getArguments() {
		return arguments;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
