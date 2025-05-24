package fr.up.xlim.sic.ig.jerboa.jme.model;

public final class JMEModeler implements JMEElement {
	protected String name;
	protected int dimension;

	public JMEModeler(String name, String module, int dim) {
		this.name = name;
		this.dimension = dim;
	}

	public String getName() {
		return name;
	}

	public int getDimension() {
		return dimension;
	}
}
