package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSHookCall extends JSEntity {
	private JSExpression nodename;
	private String hookname;

	public JSHookCall(JSExpression nodename, String hookname, int l, int col) {
		super(l, col);
		this.nodename = nodename;
		this.hookname = hookname;
	}

	public JSHookCall(JSExpression nodename, int l, int col) {
		this(nodename, null, l, col);
	}

	public JSExpression getNodename() {
		return nodename;
	}

	public String getHookname() {
		return hookname;
	}

	public boolean hasHookName() {
		return hookname != null;
	}

	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
