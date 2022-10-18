/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * @author Valentin
 *
 */
public class JSDelete extends JSInstruction {

	private JSExpression name;

	public JSDelete(JSExpression name, int l, int col) {
		super(l, col);
		this.name = name;
	}

	public JSExpression getName() {
		return name;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
