package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

public class JSTypeTemplate extends JSType {
	private Collection<JSType> tlist;

	private JSType type;

	public JSTypeTemplate(JSType _type, Collection<JSType> types, int l, int col) {
		super(_type.getType(), l, col);
		tlist = new ArrayList<>();
		type = _type;
		for (JSType ti : types) {
			tlist.add(ti);
		}
	}

	public Collection<JSType> getTypes() {
		return tlist;
	}

	public JSType getBaseType() {
		return type;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
