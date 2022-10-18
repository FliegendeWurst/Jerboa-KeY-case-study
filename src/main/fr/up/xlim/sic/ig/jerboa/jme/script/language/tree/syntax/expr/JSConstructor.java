/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSConstructor extends JSExpression {

	private JSType name;

	private List<JSExpression> arguments;

	public JSConstructor(JSType name, List<JSExpression> args, int l, int col) {
		super(l, col);
		this.name = name;
		arguments = new ArrayList<>(args);
	}

	public JSType getName() {
		return name;
	}

	public List<JSExpression> getArguments() {
		return arguments;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
