package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.List;

public final class JMERule implements JMEElement, Cloneable, Comparable/*<JMERule>*/ {

	//@ public ghost \locset footprint;
	//@ public invariant footprint == \set_union(\singleton(footprint), this.*, this.left.footprint, this.right.footprint);

	//@ public invariant \invariant_for(left) && \invariant_for(right);
	//@ public invariant left.isleft && !right.isleft;
	//@ public accessible \inv: footprint;

	public final JMEModeler modeler;
	protected final String name;
	protected final String category;

	public final JMEGraph left;
	public final JMEGraph right;

	protected final String midprocess;

	/*@ accessible left.nodes.seq;
	  @ helper model public boolean hasHookIfNotEmpty() {
	      return (left.nodes.seq.length > 0) ==>
	       (\exists \bigint i; 0 <= i && i < left.nodes.seq.length; ((JMENode)left.nodes.seq[i]).kind == JMENodeKind.HOOK);
	    }
	  @*/

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

	/*@ public normal_behavior
	  @ requires left.nodesAreUnique();
	  @ ensures \result == left.getHooks();
	  @ assignable \nothing;
	  @*/
	public List/*<JMENode>*/ getHooks() {
		if (left != null)
			return left.getHooks();
		return new ArrayList();
	}
}
