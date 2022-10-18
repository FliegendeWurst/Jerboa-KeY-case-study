/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * @author Hakim
 *
 */
public class JSDoWhile extends JSInstruction {

	private JSExpression condition;
	private JSBlock corps;

	public JSDoWhile(JSExpression cond, JSBlock body, int l, int col) {
		super(l, col);
		this.corps = body;
		this.condition = cond;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * up.jerboa.core.script.JSInstruction#visit(up.jerboa.core.script.expr.tool
	 * .JSInstVisitor)
	 */
	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSBlock getBody() {
		return corps;
	}

	public JSExpression getCondition() {
		return condition;
	}
}
