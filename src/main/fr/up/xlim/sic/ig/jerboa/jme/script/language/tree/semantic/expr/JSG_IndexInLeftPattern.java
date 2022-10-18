package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_IndexInLeftPattern extends JSEntity implements JSG_Expression {

	private JSG_Expression hookIndex;
	private JSG_Expression indexInDartList;

	public JSG_IndexInLeftPattern(JSG_Expression _hookIndex, JSG_Expression _indexInDartList, int l, int c) {
		super(l, c);
		this.hookIndex = _hookIndex;
		this.indexInDartList = _indexInDartList;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Type returnType() {
		if (indexInDartList != null)
			return new JSG_TypeJerboaDart(line, column);
		return new JSG_List(new JSG_TypeJerboaDart(line, column), line, column);
		// TODO: ici ça peut être intéressant d'avoir une liste de brin
		// retourné, mais en C++ en tout cas c'est pas prévu je crois.
	}

	public JSG_Expression getHookIndex() {
		return hookIndex;
	}

	public JSG_Expression getIndexInDartList() {
		return indexInDartList;
	}

	public String toString() {
		return "@leftPattern." + hookIndex + "[ " + indexInDartList + " ]";
	}

}
