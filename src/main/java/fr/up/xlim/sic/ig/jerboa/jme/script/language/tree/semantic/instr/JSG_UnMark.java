package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_UnMark extends JSG_Instruction {

	private JSG_Expression left;
	private JSG_Expression mark;
	private boolean gmapHasDirectAccess;

	public JSG_UnMark(JSG_Expression left, JSG_Expression mark, boolean _gmapHasDirectAccess, int l, int col) {
		super(l, col);
		this.left = left;
		this.mark = mark;
		this.gmapHasDirectAccess = _gmapHasDirectAccess;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Expression getLeft() {
		return left;
	}

	public JSG_Expression getMark() {
		return mark;
	}

	public boolean gmapHasDirectAccess() {
		return gmapHasDirectAccess;
	}
}
