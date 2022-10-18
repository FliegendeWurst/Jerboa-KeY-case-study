package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_EbdParam extends JSG_Variable {
	private JSG_Type type;
	private JMEParamEbd ebdParam;

	public JSG_EbdParam(JMEParamEbd param, String paramName, JSG_Type type, int l, int col) {
		super(paramName, l, col);
		this.type = type;
	}

	public JMEParamEbd getEbdParam() {
		return ebdParam;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Type getType() {
		return type;
	}

}
