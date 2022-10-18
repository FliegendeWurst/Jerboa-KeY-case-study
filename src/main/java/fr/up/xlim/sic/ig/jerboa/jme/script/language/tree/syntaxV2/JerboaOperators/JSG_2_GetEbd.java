package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_GetEbd extends JSG_2_Entity {
	JMEEmbeddingInfo ebdInfo;
	JSG_2_Entity left;

	public JSG_2_GetEbd(JSG_2_Entity left, JMEEmbeddingInfo ebdInfo, int l, int c) {
		super(l,c);
		this.left = left;
		this.ebdInfo = ebdInfo;
	}

	public JMEEmbeddingInfo getEbdInfo() {
		return ebdInfo;
	}

	public JSG_2_Entity getLeft() {
		return left;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
