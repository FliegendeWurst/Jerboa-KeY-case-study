package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public abstract class JSExpression extends JSEntity {

	public JSExpression(int l, int c) {
		super(l, c);
	}

	public abstract <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E;

}
