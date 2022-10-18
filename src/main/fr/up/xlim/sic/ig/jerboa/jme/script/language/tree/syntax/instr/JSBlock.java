package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSBlock extends JSInstruction {

	private JSInstruction body;
	private boolean hasBracket;

	public JSBlock(JSInstruction body, boolean _hasBracket, int l, int c) {
		super(l, c);
		this.body = body;
		hasBracket = _hasBracket;
	}

	public JSInstruction getBody() {
		return body;
	}

	public boolean hasBracket() {
		return hasBracket;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
