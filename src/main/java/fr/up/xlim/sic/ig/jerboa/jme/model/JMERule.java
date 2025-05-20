package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class JMERule implements JMEElement, Cloneable, Comparable<JMERule> {

	protected JMEModeler modeler;
	protected String name;
	protected String comment;
	protected String category;

	protected JMEGraph left;
	protected JMEGraph right;

	protected boolean modified;
	protected String header;
	protected String precondition;
	protected String preprocess;
	protected String postprocess;

	protected ArrayList<JMEParamTopo> paramstopo;

	protected String midprocess;

	protected JMERule(JMEModeler modeler, String name) {
		this.modeler = modeler;
		this.name = name;

		this.comment = "";

		modified = false;

		paramstopo = new ArrayList<>();

		this.header = "";
		category = "";
		precondition = "";
		preprocess = "";
		postprocess = "";
		midprocess = "";


		// Be careful with the order of the following lines
		left = new JMEGraph(this, true);
		right = new JMEGraph(this, false);
	}

	public String getFullName() {
		String res = "";
		if (category == null || "".equals(category))
			res = getName();
		else
			res = getCategory() + "." + getName();
		return res;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
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

	public void setName(String newname) {
		if (newname != null && !newname.equals(name)) {
			modified = true;
			name = newname;
		}
	}

	public void setComment(String newcomment) {
		if (newcomment != null && !newcomment.equals(comment)) {
			modified = true;
			comment = newcomment;
		}
	}

	public void setPrecondition(String text) {
		if (text != null && !text.equals(precondition)) {
			precondition = text;
			modified = true;
		}
	}

	public String getPrecondition() {
		return precondition;
	}

	public void setHeader(String text) {
		if (text != null && !text.equals(header)) {
			header = text;
			modified = true;
		}
	}

	public String getHeader() {
		return header;
	}

	public void setLeft(JMEGraph g) {
		left = g;
	}

	public void setRight(JMEGraph g) {
		right = g;
	}

	/**
     * Return the first hook with the given name.
     * Be careful if there are duplicates.
     * 
	 * @param name
	 * @return
	 */
	public JMENode getHookNode(String name) {
		for (JMENode n : left.getHooks()) {
			if (n.getName().compareTo(name) == 0) {
				return n;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getName();
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	public void setPreProcess(String text) {
		if (text != null && !text.equals(preprocess)) {
			preprocess = text;
			modified = true;
		}
	}

	public String getPreProcess() {
		return preprocess;
	}

	public void setPostProcess(String text) {
		if (text != null && !text.equals(postprocess)) {
			postprocess = text;
			modified = true;
		}
	}

	public String getPostProcess() {
		return postprocess;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String text) {
		if (text != null && !text.equals(category)) {
			this.category = text;
			modified = true;
		}
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

	public void insertParamTopo(JMEParamTopo p, int order) {
		if (p != null && paramstopo.contains(p)) {
			int oldorder = paramstopo.indexOf(p);
			paramstopo.add(order, p);

			if (oldorder >= order)
				oldorder++;
			modified = true;
			paramstopo.remove(oldorder);
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
	
	/**
	 * Try to add a hook to connected components without one (take any node
	 * with full orbit).
	 * @author romain
	 */
	public void enforceHooks() {
		if (left != null)
			left.enforceHooks();
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	public String getMidProcess() {
		return midprocess;
	}

	public void setMidProcess(String p) {
		if (p != null && !p.equals(midprocess)) {
			modified = true;
			this.midprocess = p;
		}
	}
	
	public static final Pattern PATTERN_MODULE = Pattern.compile(
			"([_]*[a-zA-Z][a-zA-Z0-9_]*([.][_]*[a-zA-Z][a-zA-Z0-9_]*)*)(<(.*)>)?\\*?\\z", Pattern.CASE_INSENSITIVE);
	
}
