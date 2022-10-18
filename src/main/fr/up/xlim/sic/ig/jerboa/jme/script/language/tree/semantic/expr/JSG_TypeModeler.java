package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_TypeModeler extends JSG_Type implements JSG_TypeObject {
	JMEModeler modeler;

	public JSG_TypeModeler(JMEModeler _modeler, int l, int col) {
		super(_modeler.getName(), l, col);
		modeler = _modeler;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
