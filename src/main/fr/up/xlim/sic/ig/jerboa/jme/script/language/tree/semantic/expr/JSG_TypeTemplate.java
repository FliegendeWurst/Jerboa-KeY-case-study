package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_TypeTemplate extends JSG_Type implements JSG_TypeObject {
	private ArrayList<JSG_Type> tlist;
	private JSG_Type type;

	public JSG_TypeTemplate(JSG_Type _type, Collection<JSG_Type> _tlist, int l, int col) {
		super(_type.getType() + _tlist, l, col);
		tlist = new ArrayList<>();
		type = _type;
		for (JSG_Type ti : _tlist) {
			tlist.add(ti);
		}
	}

	public ArrayList<JSG_Type> getTypes() {
		return tlist;
	}

	public JSG_Type getBaseType() {
		return type;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
