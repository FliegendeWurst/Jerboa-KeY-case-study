package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class JMERule implements JMEElement, Cloneable, Comparable<JMERule> {

	protected JMEModeler modeler;
	protected String name;
	protected String category;

	protected JMEGraph left;
	protected JMEGraph right;

	protected boolean modified;
	protected String precondition;
	protected String preprocess;
	protected String postprocess;

	protected ArrayList<JMEParamTopo> paramstopo;

	protected String midprocess;

	protected JMERule(JMEModeler modeler, String name) {
		this.modeler = modeler;
		this.name = name;

		modified = false;

		paramstopo = new ArrayList<>();

		category = "";
		precondition = "";
		preprocess = "";
		postprocess = "";
		midprocess = "";


		// Be careful with the order of the following lines
		left = new JMEGraph(this, true);
		right = new JMEGraph(this, false);
	}

	@Override
	public String getName() {
		return name;
	}

	public JMEGraph getLeft() {
		return left;
	}

	public JMEGraph getRight() {
		return right;
	}

	public void setModeler(JMEModeler _modeler) {
		modeler = _modeler;
	}

	@Override
	public String toString() {
		return getName();
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	public String getCategory() {
		return category;
	}

	public List<JMEParamTopo> getParamsTopo() {
		return paramstopo;
	}

	public void delParamTopo(JMEParamTopo topo) {
			paramstopo.remove(topo);
	}

	public void addParamTopo(JMEParamTopo topo) {
		if (topo != null && !paramstopo.contains(topo)) {
			modified = true;
			paramstopo.add(topo);
		}
	}

	@Override
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

	public void delParamTopo(JMENode node) {
		ArrayList<JMEParamTopo> newParam = new ArrayList<>();
		for (JMEParamTopo topo : paramstopo) {
			if (topo.getNode() != node) {
				newParam.add(topo);
			}
		}
		paramstopo.clear();
		paramstopo.addAll(newParam);
	}
}
