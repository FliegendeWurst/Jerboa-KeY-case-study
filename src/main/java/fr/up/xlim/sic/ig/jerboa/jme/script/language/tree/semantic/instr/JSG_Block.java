package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_Block extends JSG_Instruction {

	private JSG_Instruction body;
	private boolean hasBracket;

	public JSG_Block(JSG_Instruction body, boolean _hasBracket, int l, int c) {
		super(l, c);
		this.body = body;
		this.hasBracket = _hasBracket;
	}

	public JSG_Instruction getBody() {
		return body;
	}

	public boolean hasBracket() {
		return hasBracket;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
