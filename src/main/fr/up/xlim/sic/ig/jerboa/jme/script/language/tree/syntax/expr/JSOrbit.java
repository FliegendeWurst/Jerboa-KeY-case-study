package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSOrbit extends JSExpression {

	private ArrayList<JSExpression> dimensions;

	public JSOrbit(Collection<JSExpression> dim, int l, int col) {
		super(l, col);
		this.dimensions = new ArrayList<>(dim);
	}

	public JSOrbit(int l, int col, JSInteger... dim) {
		super(l, col);
		this.dimensions = new ArrayList<>();

		for (JSInteger d : dim) {
			dimensions.add(d);
		}
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public List<JSExpression> getDimensions() {
		return dimensions;
	}
}
