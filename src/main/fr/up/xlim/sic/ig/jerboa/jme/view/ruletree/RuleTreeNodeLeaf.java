package fr.up.xlim.sic.ig.jerboa.jme.view.ruletree;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class RuleTreeNodeLeaf extends DefaultMutableTreeNode
		implements RuleTreeNodeInterface, Comparable<RuleTreeNodeInterface>, JMEElementView {

	private static final long serialVersionUID = -7591968774547337296L;
	private JMERule rule;

	public RuleTreeNodeLeaf(JMERule rule) {
		super(rule);
		this.rule = rule;
		this.rule.addView(this);
	}

	public JMERule getRule() {
		return rule;
	}

	@Override
	public void reload() {

	}

	@Override
	public void unlink() {
		rule.removeView(this);
	}

	@Override
	public String toString() {
		return rule.getName() + (rule.isModified()? "*" : "");
	}

	@Override
	public int compareTo(RuleTreeNodeInterface o) {
		String fullname = o.getFullName();
		String myfullname = getFullName();
		return myfullname.compareTo(fullname);
	}

	@Override
	public String getFullName() {
		return rule.getCategory() + "." + rule.getName();
	}

	@Override
	public DefaultMutableTreeNode getTreeNode() {
		return this;
	}

	@Override
	public JMEElement getSourceElement() {
		return rule;
	}
}
