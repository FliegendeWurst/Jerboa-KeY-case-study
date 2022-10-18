package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_UserType extends JSG_Type implements JSG_TypeObject {
	JMEEmbeddingInfo ebdInfo;

	public JSG_UserType(JMEEmbeddingInfo ebdInfo, int l, int col) {
		super(ebdInfo == null ? "" : ebdInfo.getType(), l, col);
		this.ebdInfo = ebdInfo;
	}

	public JMEEmbeddingInfo getEbdInfo() {
		return ebdInfo;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
