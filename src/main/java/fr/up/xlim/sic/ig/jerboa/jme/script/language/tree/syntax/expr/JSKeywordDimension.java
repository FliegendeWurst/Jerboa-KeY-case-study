package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSKeywordDimension extends JSExpression {

	public JSKeywordDimension(int l, int c) {
		super(l, c);
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
