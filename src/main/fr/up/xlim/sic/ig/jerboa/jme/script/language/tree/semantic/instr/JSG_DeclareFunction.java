/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_DeclareFunction extends JSG_Instruction {

	private String name;
	private ArrayList<JSG_Instruction> arguments;
	private JSG_Block block;
	private JSG_Type returnType;

	int line, column;

	public JSG_DeclareFunction(JSG_Type _returnType, String name, Collection<JSG_Instruction> args, JSG_Block _block,
			int l, int col) {
		super(l, col);
		returnType = _returnType;
		this.name = name;
		this.arguments = new ArrayList<>(args);
		this.block = _block;
	}

	public String getName() {
		return name;
	}

	public ArrayList<JSG_Instruction> getArguments() {
		return arguments;
	}

	public JSG_Block getBlock() {
		return block;
	}

	public JSG_Type getReturnType() {
		return returnType;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
