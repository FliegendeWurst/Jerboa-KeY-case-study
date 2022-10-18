package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSIndexInLeftPattern extends JSExpression {

	private JSExpression hookIndex;
	private JSExpression indexInDartList;

	public JSIndexInLeftPattern(JSExpression _hookIndex, JSExpression _indexInDartList, int l, int col) {
		super(l, col);
		this.hookIndex = _hookIndex;
		this.indexInDartList = _indexInDartList;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSExpression getHookIndex() {
		return hookIndex;
	}

	public JSExpression getIndexInDartList() {
		return indexInDartList;
	}

	public String toString() {
		return "@leftPattern." + hookIndex + "[ " + indexInDartList + " ]";
	}
}
