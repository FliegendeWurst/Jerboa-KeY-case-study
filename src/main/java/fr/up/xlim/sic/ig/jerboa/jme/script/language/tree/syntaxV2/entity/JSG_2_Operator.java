/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSOperatorKind;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_Operator extends JSG_2_Entity implements Iterable<JSG_2_Entity> {

	private JSOperatorKind operator;
	private ArrayList<JSG_2_Entity> operands;

	public JSG_2_Operator(JSOperatorKind operator, int l, int col, JSG_2_Entity... operands) {
		super(l, col);
		this.operator = operator;
		this.operands = new ArrayList<>(operands.length);
		for (JSG_2_Entity e : operands) {
			this.operands.add(e);
		}
	}

	public JSG_2_Operator(JSOperatorKind operator, int l, int col, Collection<JSG_2_Entity> operands) {
		super(l, col);
		this.operator = operator;
		this.operands = new ArrayList<>(operands);
	}

	public JSOperatorKind getOperator() {
		return operator;
	}

	public ArrayList<JSG_2_Entity> getOperands() {
		return operands;
	}

	@Override
	public Iterator<JSG_2_Entity> iterator() {
		return operands.iterator();
	}

	public JSG_2_Entity first() {
		return operands.get(0);
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}

