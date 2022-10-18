package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_GetEbd implements JSG_Expression {
	JMEEmbeddingInfo ebdInfo;
	JSG_Expression left;

	public JSG_GetEbd(JSG_Expression left, JMEEmbeddingInfo ebdInfo) {
		this.left = left;
		this.ebdInfo = ebdInfo;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JMEEmbeddingInfo getEbdInfo() {
		return ebdInfo;
	}

	public JSG_Expression getLeft() {
		return left;
	}

}
