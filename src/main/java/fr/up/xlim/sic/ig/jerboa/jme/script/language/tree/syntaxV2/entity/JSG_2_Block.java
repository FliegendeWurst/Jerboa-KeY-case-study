package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Block extends JSG_2_Entity {

	private JSG_2_Entity body;
	private boolean hasBracket;
	
	public JSG_2_Block(int l, int c) {
		super(l, c);
		this.body = new JSG_2_NOP(l,c);
		this.hasBracket = true;
	}
	public JSG_2_Block(JSG_2_Entity body, boolean _hasBracket, int l, int c) {
		super(l, c);
		this.body = body;
		this.hasBracket = _hasBracket;
	}

	public JSG_2_Entity getBody() {
		return body;
	}

	public boolean hasBracket() {
		return hasBracket;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
