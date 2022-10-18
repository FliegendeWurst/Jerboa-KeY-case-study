package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Literal<T2> extends JSG_2_Entity{
	T2 value;
	
	public JSG_2_Literal(T2 v, int l, int c) {
		super(l,c);
		value = v;
	}
	
	public T2 getValue() {
		return value;
	}	
	

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
