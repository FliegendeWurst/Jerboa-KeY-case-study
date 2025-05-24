package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class JMEModeler implements JMEElement {
	protected String name;
	protected int dimension;

	protected ArrayList<JMERule> rules;

	public JMEModeler(String name, String module, int dim) {
		rules = new ArrayList<>();
		this.name = name;
		this.dimension = dim;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getDimension() {
		return dimension;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		return sb.toString();
	}
}
