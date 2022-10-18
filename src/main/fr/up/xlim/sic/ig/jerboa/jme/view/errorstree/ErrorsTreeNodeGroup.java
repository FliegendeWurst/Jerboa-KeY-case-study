package fr.up.xlim.sic.ig.jerboa.jme.view.errorstree;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorSeverity;

public class ErrorsTreeNodeGroup extends DefaultMutableTreeNode implements Comparable<ErrorsTreeNodeGroup> {
	private static final long serialVersionUID = 8302900014292671363L;

	private JMEErrorSeverity severity;
	
	public ErrorsTreeNodeGroup(JMEErrorSeverity severity) {
		super(severity);
		this.severity = severity;
		setUserObject(this);
	}
	
	
	@Override
	public String toString() {
		return severity.toString() + " (" + getChildCount()+")";
	}

	JMEErrorSeverity getSeverity() {
		return severity;
	}
	

	@Override
	public int compareTo(ErrorsTreeNodeGroup o) {
		return Integer.compare(severity.ordinal(), o.getSeverity().ordinal());
	}
	
	
}
