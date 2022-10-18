/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * @author Valentin
 *
 */
public class JSDeclareFunction extends JSInstruction {

	private String name;
	private ArrayList<JSDeclare> arguments;
	private JSBlock block;
	private JSType returnType;

	int line, column;

	public JSDeclareFunction(JSType _returnType, String name, Collection<JSDeclare> args, JSBlock _block, int l,
			int col) {
		super(l, col);
		returnType = _returnType;
		this.name = name;
		this.arguments = new ArrayList<>(args);
		this.block = _block;
	}

	public String getName() {
		return name;
	}

	public ArrayList<JSDeclare> getArguments() {
		return arguments;
	}

	public JSBlock getBlock() {
		return block;
	}

	public JSType getReturnType() {
		return returnType;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
