package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_GetEbdOrbit extends JSG_2_Entity {
	private JMEEmbeddingInfo ebdInfo;

	public JSG_2_GetEbdOrbit(JMEEmbeddingInfo ebdInfo, int l, int c) {
		super(l,c);
		this.ebdInfo = ebdInfo;
	}


	public JMEEmbeddingInfo getEbdInfo() {
		return ebdInfo;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
