package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_HookCall extends JSG_Instruction {
	private JSG_Expression nodename;
	private String hookname;

	public JSG_HookCall(JSG_Expression nodename, String hookname, int l, int col) {
		super(l, col);
		this.nodename = nodename;
		this.hookname = hookname;
	}

	public JSG_HookCall(JSG_Expression nodename, int l, int col) {
		this(nodename, null, l, col);
	}

	public JSG_Expression getNodename() {
		return nodename;
	}

	public String getHookname() {
		return hookname;
	}

	public boolean hasHookName() {
		return hookname != null;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
