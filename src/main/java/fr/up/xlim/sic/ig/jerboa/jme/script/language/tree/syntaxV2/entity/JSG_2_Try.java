package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Try extends JSG_2_Entity {

	private JSG_2_Entity tryBlock;
	private List<JSG_2_Catch> catchList;
	private JSG_2_Entity finallyBlock;

	public JSG_2_Try(JSG_2_Entity _tryBlock, List<JSG_2_Catch> _catchList, JSG_2_Entity _finallyBlock, int l,
			int col) {
		super(l, col);
		tryBlock = _tryBlock;
		catchList = _catchList;
		if (_catchList == null)
			catchList = new ArrayList<JSG_2_Catch>();
			finallyBlock = _finallyBlock;
	}

	public JSG_2_Entity getTryBlock() {
		return tryBlock;
	}

	public List<JSG_2_Catch> getCatchList() {
		return catchList;
	}

	public JSG_2_Entity getFinallyBlock() {
		return finallyBlock;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
