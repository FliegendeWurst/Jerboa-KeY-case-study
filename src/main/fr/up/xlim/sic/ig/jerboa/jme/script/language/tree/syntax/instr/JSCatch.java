package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSCatch extends JSInstruction {

	private JSBlock block;
	private JSType typeExep;
	private String nameExep;

	public JSCatch(JSBlock _block, JSType _typeExep, String _nameExep, int l, int col) {
		super(l, col);
		block = _block;
		typeExep = _typeExep;
		nameExep = _nameExep;
	}

	public JSBlock getBlock() {
		return block;
	}

	public JSType getTypeExep() {
		return typeExep;
	}

	public String getNameExep() {
		return nameExep;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
