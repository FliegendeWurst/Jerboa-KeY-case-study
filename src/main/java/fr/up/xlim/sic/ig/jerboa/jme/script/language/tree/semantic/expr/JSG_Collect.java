package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_Collect extends JSEntity implements JSG_Expression {

	private JSG_Orbit orbit;
	private JSG_Orbit sorb;
	private JSG_Expression node;
	private boolean gmapHasDirectAccess;

	public JSG_Collect(JSG_Orbit orbit, JSG_Orbit sorb, JSG_Expression node, boolean _gmapHasDirectAccess, int l,
			int col) {
		super(l, col);
		this.orbit = orbit;
		this.sorb = sorb;
		this.node = node;
		gmapHasDirectAccess = _gmapHasDirectAccess;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Orbit getOrbit() {
		return orbit;
	}

	public JSG_Orbit getSubOrbit() {
		return sorb;
	}

	public JSG_Expression getNode() {
		return node;
	}

	public boolean hasSubOrbit() {
		return (sorb != null) && (sorb.getDimensions().size() > 0);
	}

	public boolean gmapHasDirectAccess() {
		return gmapHasDirectAccess;
	}

	@Override
	public String toString() {
		return "COLLECT" + orbit + "(" + node + ")";
	}

}
