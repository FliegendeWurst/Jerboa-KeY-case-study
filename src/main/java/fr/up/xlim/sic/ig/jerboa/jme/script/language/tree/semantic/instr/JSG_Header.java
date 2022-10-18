package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_Header extends JSG_Instruction {

	private String code;
	private String language;

	public JSG_Header(String language, String code, int l, int col) {
		super(l, col);
		this.code = code;
		this.language = language;
	}

	public String getCode() {
		return code;
	}

	public String getLanguage() {
		return language;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
