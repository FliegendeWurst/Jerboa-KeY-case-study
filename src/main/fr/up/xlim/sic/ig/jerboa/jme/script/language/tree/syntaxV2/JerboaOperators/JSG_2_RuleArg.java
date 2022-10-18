package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_RuleArg extends JSG_2_Entity {
	private JSG_2_Entity argvalue;
	private String argname;

	public JSG_2_RuleArg(String argname, JSG_2_Entity argvalue) {
		this.argvalue = argvalue;
		this.argname = argname;
	}

	public JSG_2_Entity getArgValue() {
		return argvalue;
	}

	public String getArgName() {
		return argname;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
