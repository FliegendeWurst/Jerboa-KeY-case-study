package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSHeader extends JSInstruction {

	private String code;
	private String language;

	public JSHeader(String language, String code, int l, int col) {
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
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}