package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSWhile extends JSInstruction {
	private JSBlock corps;
	private JSExpression condition;

	public JSWhile(JSExpression cond, JSBlock corps, int l, int col) {
		super(l, col);
		this.condition = cond;
		this.corps = corps;
	}

	public JSBlock getCorps() {
		return corps;
	}

	public JSExpression getCondition() {
		return condition;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
