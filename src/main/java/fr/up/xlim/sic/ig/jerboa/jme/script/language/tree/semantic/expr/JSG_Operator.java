/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSOperatorKind;

/**
 * @author Valentin
 *
 */
public class JSG_Operator extends JSEntity implements JSG_Expression, Iterable<JSG_Expression> {

	private JSOperatorKind operator;
	private ArrayList<JSG_Expression> operands;

	public JSG_Operator(int l, int col, JSOperatorKind operator, JSG_Expression... operands) {
		super(l, col);
		this.operator = operator;
		this.operands = new ArrayList<>(operands.length);
		for (JSG_Expression e : operands) {
			this.operands.add(e);
		}
	}

	public JSG_Operator(int l, int col, JSOperatorKind operator, Collection<JSG_Expression> operands) {
		super(l, col);
		this.operator = operator;
		this.operands = new ArrayList<>(operands);
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSOperatorKind getOperator() {
		return operator;
	}

	public ArrayList<JSG_Expression> getOperands() {
		return operands;
	}

	@Override
	public Iterator<JSG_Expression> iterator() {
		return operands.iterator();
	}

	public JSG_Expression first() {
		return operands.get(0);
	}
}
