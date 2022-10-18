package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_Try extends JSG_Instruction {

	private JSG_Instruction tryBlock;
	private List<JSG_Catch> catchList;
	private JSG_Instruction finallyBlock;

	public JSG_Try(JSG_Instruction _tryBlock, List<JSG_Catch> _catchList, JSG_Instruction _finallyBlock, int l,
			int col) {
		super(l, col);
		tryBlock = _tryBlock;
		catchList = _catchList;
		if (_catchList == null)
			catchList = new ArrayList<JSG_Catch>();
			finallyBlock = _finallyBlock;
	}

	public JSG_Instruction getTryBlock() {
		return tryBlock;
	}

	public List<JSG_Catch> getCatchList() {
		return catchList;
	}

	public JSG_Instruction getFinallyBlock() {
		return finallyBlock;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
