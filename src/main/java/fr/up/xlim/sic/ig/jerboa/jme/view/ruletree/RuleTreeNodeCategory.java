package fr.up.xlim.sic.ig.jerboa.jme.view.ruletree;

import javax.swing.tree.DefaultMutableTreeNode;

public class RuleTreeNodeCategory extends DefaultMutableTreeNode
		implements RuleTreeNodeInterface, Comparable<RuleTreeNodeInterface> {

	private static final long serialVersionUID = -7591968774547337296L;
	private String category;

	public RuleTreeNodeCategory(String category) {
		super(category);
		this.category = category;

	}

	@Override
	public String toString() {
		return category;
	}

	@Override
	public int compareTo(RuleTreeNodeInterface o) {
		String fullname = o.getFullName();
		return category.compareTo(fullname);
	}

	@Override
	public String getFullName() {
		return category;
	}

	@Override
	public DefaultMutableTreeNode getTreeNode() {
		return this;
	}
}
