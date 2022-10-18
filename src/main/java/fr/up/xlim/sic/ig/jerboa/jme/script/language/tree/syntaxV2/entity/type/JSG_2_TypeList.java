package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_TypeList extends JSG_2_Type{
	JSG_2_Type listContentType;

	public JSG_2_TypeList(JSG_2_Type listContentType, int l, int c) {
		super("TypeList", l, c);
		this.listContentType = listContentType;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
	
	public JSG_2_Type getListContentType() {
		return listContentType;
	}
	
	@Override
	public String toString() {
		String s = "TS_List<" + listContentType + ">";
		return s;
	}
}
