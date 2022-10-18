package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Variable;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_EbdParam extends JSG_2_Variable {

	private JMEParamEbd ebdParam;

	public JSG_2_EbdParam(JMEParamEbd param, String paramName, int l, int col) {
		super(paramName, l, col);
		ebdParam = param;
	}

	public JMEParamEbd getEbdParam() {
		return ebdParam;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
