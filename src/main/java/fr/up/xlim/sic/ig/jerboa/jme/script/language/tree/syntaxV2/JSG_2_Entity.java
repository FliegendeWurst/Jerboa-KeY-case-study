package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public abstract class JSG_2_Entity implements LineColTraceable{
	protected int line = -666;
	protected int col = -666;
	
	public JSG_2_Entity() {
		
	}
	
	public JSG_2_Entity(int l, int c) {
		this.line = l;
		this.col = c;
	}
	
	public JSG_2_Entity(JSG_2_Entity e) {
		this.line = e.line;
		this.col = e.col;
	}
	
	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getColumn() {
		return col;
	}
	
	@Override
	public String toString() {
		return getClass().getCanonicalName();
	}
	

	public abstract <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E;

}
