package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Header extends JSG_2_Entity {

	private String code;
	private String language;

	public JSG_2_Header(String language, String code, int l, int col) {
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
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
