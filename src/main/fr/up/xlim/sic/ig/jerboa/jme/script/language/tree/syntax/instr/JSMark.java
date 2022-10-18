package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSMark extends JSInstruction {

	private JSExpression left;
	private JSExpression mark;

	public JSMark(JSExpression left, JSExpression mark, int l, int col) {
		super(l, col);
		this.left = left;
		this.mark = mark;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getLeft() {
		return left;
	}

	public JSExpression getMark() {
		return mark;
	}

}
