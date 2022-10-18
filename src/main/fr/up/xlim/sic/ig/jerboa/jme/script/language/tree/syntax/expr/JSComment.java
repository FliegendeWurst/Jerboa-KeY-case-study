package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSComment extends JSExpression {
	private String comment;

	public JSComment(String _comment, int l, int col) {
		super(l, col);
		this.comment = _comment;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public String getComment() {
		return comment;
	}

}
