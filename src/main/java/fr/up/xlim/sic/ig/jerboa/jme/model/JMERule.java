package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.List;

public final class JMERule implements JMEElement, Cloneable, Comparable<JMERule> {

	protected JMEModeler modeler;
	protected String name;
	protected String category;

	protected JMEGraph left;
	protected JMEGraph right;

	protected String midprocess;

	protected JMERule(JMEModeler modeler, String name) {
		this.modeler = modeler;
		this.name = name;

		category = "";
		midprocess = "";


		// Be careful with the order of the following lines
		left = new JMEGraph(this, true);
		right = new JMEGraph(this, false);
	}

	public String getName() {
		return name;
	}

	public JMEGraph getLeft() {
		return left;
	}

	public JMEGraph getRight() {
		return right;
	}

	public String toString() {
		return getName();
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	public String getCategory() {
		return category;
	}

	public int compareTo(JMERule o) {

		int cmpcat = -1 * getCategory().compareTo(o.getCategory());

		String ofullname = (o.getCategory() == null ? "" : o.getCategory()) + "." + o.getName();
		String fullname = (getCategory() == null ? "" : getCategory()) + "." + getName();

		int cmpfull = fullname.compareTo(ofullname);
		if (cmpcat == 0)
			return cmpfull;
		else {
			if ("".equals(getCategory()))
				return 1;
			else if ("".equals(o.getCategory()))
				return -1;
			return cmpfull;
		}
	}

	public List<JMENode> getHooks() {
		if (left != null)
			return left.getHooks();
		return new ArrayList<JMENode>();
	}
}
