package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSCollect extends JSExpression {

	private JSOrbit orbit;
	private JSOrbit sorb;
	private JSExpression node;

	public JSCollect(JSOrbit orbit, JSOrbit sorb, JSExpression node, int l, int col) {
		super(l, col);
		this.orbit = orbit;
		this.sorb = sorb;
		this.node = node;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSOrbit getOrbit() {
		return orbit;
	}

	public JSOrbit getSubOrbit() {
		return sorb;
	}

	public JSExpression getNode() {
		return node;
	}

	public boolean hasSubOrbit() {
		return (sorb != null) && (sorb.getDimensions().size() > 0);
	}

}
