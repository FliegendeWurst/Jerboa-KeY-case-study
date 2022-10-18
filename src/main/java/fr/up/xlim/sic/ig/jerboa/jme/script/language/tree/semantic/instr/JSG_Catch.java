package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_Catch extends JSG_Instruction {

	private JSG_Instruction block;
	private JSG_Instruction decl;

	public JSG_Catch(JSG_Instruction _block, JSG_Instruction _decl, int l, int col) {
		super(l, col);
		block = _block;
		decl = _decl;
	}

	public JSG_Instruction getBlock() {
		return block;
	}

	public JSG_Instruction getDeclar() {
		return decl;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
