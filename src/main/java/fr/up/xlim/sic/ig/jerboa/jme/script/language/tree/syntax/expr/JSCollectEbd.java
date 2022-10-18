package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSCollectEbd extends JSExpression {

	private JSOrbit orbit;
	private String embedding;
	private JSExpression node;

	public JSCollectEbd(JSOrbit orbit, String ebd, JSExpression node, int l, int col) {
		super(l, col);
		this.orbit = orbit;
		this.embedding = ebd;
		this.node = node;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSOrbit getOrbit() {
		return orbit;
	}

	public String getEmbedding() {
		return embedding;
	}

	public JSExpression getNode() {
		return node;
	}

}
