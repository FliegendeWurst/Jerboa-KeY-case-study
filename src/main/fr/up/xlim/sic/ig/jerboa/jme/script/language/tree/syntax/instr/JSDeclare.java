package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/*
 * est ce vraiment gratuit sur la verif du type et de le type de la value
 *
 *
 */
public class JSDeclare extends JSInstruction {

	private JSType type;
	private String name;
	private JSExpression value;

	public JSDeclare(JSType type, String name, JSExpression value, int l, int col) {
		super(l, col);
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public JSType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public JSExpression getValue() {
		return value;
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
