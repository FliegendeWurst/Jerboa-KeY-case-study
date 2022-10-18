package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Type extends JSG_2_Entity  {
	protected String typeName;
	
	public static String INT 	= "int";
	public static String FLOAT 	= "float";
	public static String DOUBLE = "double";
	public static String LONG 	= "long";
	
	public JSG_2_Type(String name, int l, int c) {
		super(l,c);
		typeName = name;
	}

	public String getTypeName() {
		return typeName;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
	
	@Override
	public String toString() {
		return typeName;
	}
}
