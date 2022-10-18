package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_InScope extends JSG_2_Entity {

	private JSG_2_Entity left;
	private JSG_2_Entity right;
	
	boolean isStatic;

	public JSG_2_InScope(JSG_2_Entity left, JSG_2_Entity right, boolean isStatic, int l, int c) {
		super(l,c);
		this.left = left;
		this.right = right;
		this.isStatic = isStatic;
	}

	public JSG_2_Entity getLeft() {
		return left;
	}

	public JSG_2_Entity getRight() {
		return right;
	}
	
	public boolean isStatic() {
		return isStatic;
	}

	
	public String toString() {
		return "JSG_2_InScope{ " + left + " . " + right +" }"; 
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
