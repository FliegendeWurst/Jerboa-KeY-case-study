package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSTry extends JSInstruction {

	private JSBlock tryBlock;
	private List<JSCatch> catchList;
	private JSBlock finallyBlock;

	public JSTry(JSBlock _tryBlock, List<JSCatch> _catchList, JSBlock _finallyBlock, int l, int col) {
		super(l, col);
		tryBlock = _tryBlock;
		catchList = _catchList;
		if (_catchList == null)
			catchList = new ArrayList<JSCatch>();
		finallyBlock = _finallyBlock;
	}

	public JSBlock getTryBlock() {
		return tryBlock;
	}

	public List<JSCatch> getCatchList() {
		return catchList;
	}

	public JSBlock getFinallyBlock() {
		return finallyBlock;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
