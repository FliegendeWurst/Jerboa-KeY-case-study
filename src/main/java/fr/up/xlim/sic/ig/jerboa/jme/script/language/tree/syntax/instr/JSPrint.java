/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * @author Valentin
 *
 */
public class JSPrint extends JSInstruction {

	private ArrayList<JSExpression> arguments;

	public JSPrint(Collection<JSExpression> args, int l, int col) {
		super(l, col);
		arguments = new ArrayList<>(args);
	}

	public ArrayList<JSExpression> getArguments() {
		return arguments;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
