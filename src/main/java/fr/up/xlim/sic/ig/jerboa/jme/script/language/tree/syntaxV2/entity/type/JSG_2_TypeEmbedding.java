package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_TypeEmbedding extends JSG_2_Type{
	private String ebdName; 
	
	public JSG_2_TypeEmbedding(String ebdName, int l, int c) {
		super("TYPEJerboaEmbedding", l, c);
		this.ebdName = ebdName;
	}
	
	public String getEbdName() {
		return ebdName;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
	
	@Override
	public String toString() {
		return "Embedding<"+ebdName+">";
	}
}
