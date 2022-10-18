package fr.up.xlim.sic.ig.jerboa.jme.view.errorstree;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.tab.JPanelTabDescription;

public class ErrorsPanelTabDescription extends JPanelTabDescription implements JMEElementView {
	private static final long serialVersionUID = 2613362653546660005L;

	private JMEElementWindowable model;
	private ErrorsTreeViewerModel vmodel;
	
	public ErrorsPanelTabDescription(RuleView view) {
		super(view.getTabbedPane(), "Errors");
		
		setLayout(new BorderLayout());
		this.model = view.getRule();
		this.model.addView(this);
		
		this.vmodel = new ErrorsTreeViewerModel(model);
		
		JTree errorsTree = new JTree(vmodel);
		errorsTree.setRootVisible(false);
		
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(errorsTree);
		
		add(pane, BorderLayout.CENTER);
	}
	
	
	@Override
	public void reload() {
		
	}

	@Override
	public void unlink() {
		model.removeView(this);
	}


	@Override
	public void OnClose() {
		
	}


	@Override
	public JMEElement getSourceElement() {
		return model;
	}

}
