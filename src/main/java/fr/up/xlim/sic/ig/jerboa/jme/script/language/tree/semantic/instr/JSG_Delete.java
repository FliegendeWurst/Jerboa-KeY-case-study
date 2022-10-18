/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_Delete extends JSG_Instruction {

	private JSG_Expression name;

	public JSG_Delete(JSG_Expression name, int l, int col) {
		super(l, col);
		this.name = name;
	}

	public JSG_Expression getName() {
		return name;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
