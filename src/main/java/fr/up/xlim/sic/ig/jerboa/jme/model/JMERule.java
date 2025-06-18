package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.List;

public final class JMERule implements JMEElement, Cloneable, Comparable/*<JMERule>*/ {

	//@ ghost \locset footprint;
	//@ invariant \subset(\singleton(footprint), footprint);
	//@ invariant \subset(this.left.footprint, footprint);
	//@ invariant \subset(this.right.footprint, footprint);

	//@ public invariant \invariant_for(left) && \invariant_for(right);
	//@ public accessible \inv: footprint;

	public final JMEModeler modeler;
	protected String name;
	protected String category;

	public final JMEGraph left;
	public final JMEGraph right;

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

	public String toString() {
		return getName();
	}

	public String getCategory() {
		return category;
	}

	public int compareTo(Object o2) {
		JMERule o = (JMERule) o2;

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

	public List/*<JMENode>*/ getHooks() {
		if (left != null)
			return left.getHooks();
		return new ArrayList();
	}
}
