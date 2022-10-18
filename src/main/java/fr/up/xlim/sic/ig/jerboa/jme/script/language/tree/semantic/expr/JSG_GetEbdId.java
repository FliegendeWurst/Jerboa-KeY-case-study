package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_GetEbdId implements JSG_Expression {
	JMEEmbeddingInfo ebdInfo;

	public JSG_GetEbdId(JMEEmbeddingInfo ebdInfo) {
		this.ebdInfo = ebdInfo;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JMEEmbeddingInfo getEbdInfo() {
		return ebdInfo;
	}


}
