package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

public abstract class JSEntity {
	protected int line;

	protected int column;

	public JSEntity(int l, int c) {
		line = l;
		column = c;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public void setLine(int l) {
		line = l;
	}

	public void setColumn(int c) {
		column = c;
	}
}
