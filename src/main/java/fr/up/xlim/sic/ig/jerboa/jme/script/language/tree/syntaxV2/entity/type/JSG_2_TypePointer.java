package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_TypePointer extends JSG_2_Type  {
	protected JSG_2_Type type;
	
	public JSG_2_TypePointer(JSG_2_Type type, int l, int c) {
		super(type.getTypeName()+"*",l,c);
		this.type = type;
	}

	public JSG_2_Type getType() {
		return type;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
	

	@Override
	public String toString() {
		return type+"*";
	}
}
