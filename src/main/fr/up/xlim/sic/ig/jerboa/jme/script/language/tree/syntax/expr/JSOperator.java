/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSOperatorKind;

/**
 * @author Hakim
 *
 */
public class JSOperator extends JSExpression implements Iterable<JSExpression> {

	private JSOperatorKind operator;
	private ArrayList<JSExpression> operands;

	public JSOperator(int l, int col, JSOperatorKind operator, JSExpression... operands) {
		super(l, col);
		this.operator = operator;
		this.operands = new ArrayList<>(operands.length);
		for (JSExpression e : operands) {
			this.operands.add(e);
		}
	}

	public JSOperator(int l, int col, JSOperatorKind operator, Collection<JSExpression> operands) {
		super(l, col);
		this.operator = operator;
		this.operands = new ArrayList<>(operands);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * up.jerboa.core.script.expr.JSExpression#visit(up.jerboa.core.script.expr.
	 * tools.JSExprVisitor)
	 */
	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSOperatorKind getOperator() {
		return operator;
	}

	public ArrayList<JSExpression> getOperands() {
		return operands;
	}

	@Override
	public Iterator<JSExpression> iterator() {
		return operands.iterator();
	}

	public JSExpression first() {
		return operands.get(0);
	}
}
