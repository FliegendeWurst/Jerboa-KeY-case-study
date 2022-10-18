package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_Orbit extends JSEntity implements JSG_Expression {

	private ArrayList<JSG_Expression> dimensions;

	public JSG_Orbit(Collection<JSG_Expression> dim, int l, int col) {
		super(l, col);
		this.dimensions = new ArrayList<>(dim);
	}

	public JSG_Orbit(int l, int col, JSG_Integer... dim) {
		super(l, col);
		this.dimensions = new ArrayList<>();

		for (JSG_Integer d : dim) {
			dimensions.add(d);
		}
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public List<JSG_Expression> getDimensions() {
		return dimensions;
	}
}
