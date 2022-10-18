package fr.up.xlim.sic.ig.jerboa.jme.view.errorstree;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorSeverity;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorType;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class ErrorsTreeViewerModel extends DefaultTreeModel implements JMEElementView{
	private static final long serialVersionUID = -6328969931878437687L;

	private JMEElementWindowable model;
	private DefaultMutableTreeNode racine;
	private ArrayList<JMEError> lastErrors;
	
	private ErrorsTreeNodeGroup severityINFO;
	private ErrorsTreeNodeGroup severityCRITICAL;
	private ErrorsTreeNodeGroup severityWARNING;
	
	private boolean viewhierarchy;
	private boolean[] showErrorType;
	
	
	public ErrorsTreeViewerModel(JMEElementWindowable model) {
		super(new DefaultMutableTreeNode(""));
		
		this.viewhierarchy = false;
		
		showErrorType = new boolean[JMEErrorType.values().length];
		for (int i = 0;i < showErrorType.length; ++i) {
			showErrorType[i] = true;
		}
		
		this.model = model;
		this.model.addView(this);
	
		this.racine = (DefaultMutableTreeNode) root;
		this.severityINFO = new ErrorsTreeNodeGroup(JMEErrorSeverity.INFO);
		this.severityWARNING = new ErrorsTreeNodeGroup(JMEErrorSeverity.WARNING);
		this.severityCRITICAL = new ErrorsTreeNodeGroup(JMEErrorSeverity.CRITIQUE);
		
		racine.add(severityCRITICAL);
		racine.add(severityWARNING);
		racine.add(severityINFO);
		
		reload();
		
	}
	
	
	@Override
	public void reload() {
		super.reload();
		
		severityINFO.removeAllChildren();
		severityWARNING.removeAllChildren();
		severityCRITICAL.removeAllChildren();
		
		if(viewhierarchy)
			lastErrors = new ArrayList<>(model.getAllErrors());
		else
			lastErrors = new ArrayList<>(model.getErrors());
		
		for (JMEError error : lastErrors) {
			ErrorsTreeNodeLeaf leaf = new ErrorsTreeNodeLeaf(error);
			JMEErrorType type = error.getType();
			boolean mustDisplay = showErrorType[type.ordinal()];
			if(mustDisplay) {
				switch(error.getSeverity()) {
				case  INFO:
					severityINFO.add(leaf);
					break;
				case WARNING:
					severityWARNING.add(leaf);
					break;
				case CRITIQUE:
					severityCRITICAL.add(leaf);
					break;
				}
			}
		}
		
	}

	@Override
	public void unlink() {
		model.removeView(this);
	}
	
	public boolean showHierarchyErrors() {
		return viewhierarchy;
	}
	
	public void setShowHierarchyErrors(boolean b) {
		if(viewhierarchy != b) {
			viewhierarchy = b;
			reload();
		}
	}
	
	public boolean showDisplayErrorType(JMEErrorType type) {
		return showErrorType[type.ordinal()];
	}
	
	public void setShowDisplayErrorType(JMEErrorType type, boolean b) {
		final int index = type.ordinal();
		if(showErrorType[index] != b) {
			showErrorType[index] = b;
			reload();
		}
	}


	@Override
	public JMEElement getSourceElement() {
		return model;
	}
}
