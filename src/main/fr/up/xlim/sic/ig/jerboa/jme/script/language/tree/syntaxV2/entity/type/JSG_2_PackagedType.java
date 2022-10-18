package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_PackagedType extends JSG_2_Type {

	private JSG_2_Type left;
	private JSG_2_Type right;

	public JSG_2_PackagedType(JSG_2_Type left, JSG_2_Type right, int l, int c) {
		super("#Package_G_Type#", l, c);
		this.left = left;
		this.right = right;
	}

	public JSG_2_Type getLeft() {
		return left;
	}

	public JSG_2_Type getRight() {
		return right;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
	
	@Override
	public String toString() {
		return left + "<"+right+">";
	}
}
