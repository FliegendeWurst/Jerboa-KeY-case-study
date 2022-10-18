package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Orbit extends JSG_2_Entity {

	private ArrayList<JSG_2_Entity> dimensions;

	public JSG_2_Orbit(Collection<JSG_2_Entity> dim, int l, int col) {
		super(l, col);
		this.dimensions = new ArrayList<>(dim);
	}

	public JSG_2_Orbit(int l, int col, JSG_2_Integer... dim) {
		super(l, col);
		this.dimensions = new ArrayList<>();

		for (JSG_2_Integer d : dim) {
			dimensions.add(d);
		}
	}

	public List<JSG_2_Entity> getDimensions() {
		return dimensions;
	}
	

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
