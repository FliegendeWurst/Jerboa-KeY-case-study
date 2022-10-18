/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_AddInList implements JSG_Expression {

	private ArrayList<JSG_Expression> arguments;
	private JSG_List list;

	public JSG_AddInList(Collection<JSG_Expression> args, JSG_List listType) {
		arguments = new ArrayList<>(args);
		list = listType;
	}

	public ArrayList<JSG_Expression> getArgs() {
		return arguments;
	}
	
	public JSG_List getList() {
		return list;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
