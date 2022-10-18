package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_GetTopoParam  implements JSG_Expression{
	JMENode node;
	
	/**
	 * a negativ index meens no acces to the list item and just the element
	 */
	JSG_Expression index;
	
	public JSG_GetTopoParam(JMENode n){
		node = n;
		this.index = null;
	}

	public JSG_GetTopoParam(JMENode n, JSG_Expression index){
		node = n;
		this.index = index;
	}
	
	public JSG_Expression getIndex() {
		return index; 
	}
	public JMENode getNode() {
		return node;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
