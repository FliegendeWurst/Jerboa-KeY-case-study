package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_CollectEbd extends JSEntity implements JSG_Expression {

	private JSG_Orbit orbit;
	private String embedding;
	private JSG_Expression node;
	private String ebdType;
	private boolean gmapHasDirectAccess;

	public JSG_CollectEbd(JSG_Orbit orbit, String ebd, JSG_Expression node, String _ebdType,
			boolean _gmapHasDirectAccess, int l, int col) {
		super(l, col);
		this.orbit = orbit;
		this.embedding = ebd;
		this.node = node;
		this.ebdType = _ebdType;
		this.gmapHasDirectAccess = _gmapHasDirectAccess;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Orbit getOrbit() {
		return orbit;
	}

	public String getEbdType() {
		return ebdType;
	}

	public String getEmbedding() {
		return embedding;
	}

	public JSG_Expression getNode() {
		return node;
	}

	public boolean gmapHasDirectAccess() {
		return gmapHasDirectAccess;
	}

	@Override
	public String toString() {
		return "COLLECT_EBD" + orbit + "_" + ebdType + "(" + node + ")";
	}

}
