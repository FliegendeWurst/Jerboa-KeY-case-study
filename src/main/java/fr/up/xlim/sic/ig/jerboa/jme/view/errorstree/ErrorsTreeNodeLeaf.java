package fr.up.xlim.sic.ig.jerboa.jme.view.errorstree;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;

public class ErrorsTreeNodeLeaf extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -4315309138834605520L;
	
	
	private JMEError error;
	
	
	public ErrorsTreeNodeLeaf(JMEError error) {
		super(error);
		this.error = error;
	}
	
	
	public JMEError getError() {
		return error;
	}
	
}
