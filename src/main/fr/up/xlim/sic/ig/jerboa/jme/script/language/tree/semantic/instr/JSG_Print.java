/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_Print extends JSG_Instruction {

	private ArrayList<JSG_Expression> arguments;

	public JSG_Print(int l, int c, Collection<JSG_Expression> args) {
		super(l, c);
		arguments = new ArrayList<>(args);
	}

	public ArrayList<JSG_Expression> getArguments() {
		return arguments;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
