/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

/**
 * @author Valentin
 *
 */
public class JSG_Call extends JSEntity implements JSG_Expression {

	private String name;

	private List<JSG_Expression> arguments;

	public JSG_Call(String name, List<JSG_Expression> args, int line, int col) {
		super(line, col);
		this.name = name;
		arguments = new ArrayList<>(args);
	}

	public String getName() {
		return name;
	}

	public List<JSG_Expression> getArguments() {
		return arguments;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
