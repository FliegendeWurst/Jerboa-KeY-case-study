package fr.up.xlim.sic.ig.jerboa.jme.view.errorstree;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorType;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class ErrorsPanel extends JPanel implements JMEElementView {
	private static final long serialVersionUID = 2613362653546660005L;

	private JMEElementWindowable model;
	private ErrorsTreeViewerModel vmodel;
	private JToggleButton tglbtnOther;
	private JToggleButton tglbtnScript;
	private JToggleButton tglbtnEmbedding;
	private JToggleButton tglbtnTopologicical;
	private JToggleButton tglbtnAllErrors;
	private JButton btnResearch;

	public ErrorsPanel(JMEElementWindowable model) {
		super(new BorderLayout());

		if(model!=null) {
			this.model = model;
			this.model.addView(this);

			this.vmodel = new ErrorsTreeViewerModel(model);

			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			add(toolBar, BorderLayout.NORTH);

			toolBar.addSeparator();
			tglbtnAllErrors = new JToggleButton("Hierarchical errors");
			tglbtnAllErrors.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					changeAllErrors();
				}
			});
			tglbtnAllErrors.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			toolBar.add(tglbtnAllErrors);

			toolBar.addSeparator();

			JLabel lblNewLabel = new JLabel("Filters:");
			toolBar.add(lblNewLabel);

			tglbtnTopologicical = new JToggleButton("Topology");
			tglbtnTopologicical.setSelected(true);
			tglbtnTopologicical.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					filterTopology();
				}
			});
			toolBar.add(tglbtnTopologicical);

			tglbtnEmbedding = new JToggleButton("Embedding");
			tglbtnEmbedding.setSelected(true);
			tglbtnEmbedding.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					filterEmbedding();
				}
			});
			toolBar.add(tglbtnEmbedding);

			tglbtnScript = new JToggleButton("Script");
			tglbtnScript.setSelected(true);
			tglbtnScript.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					filterScript();
				}
			});
			toolBar.add(tglbtnScript);

			tglbtnOther = new JToggleButton("Other");
			tglbtnOther.setSelected(true);
			tglbtnOther.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					filterOther();
				}
			});
			toolBar.add(tglbtnOther);

			toolBar.addSeparator();

			btnResearch = new JButton("Refresh");
			toolBar.add(btnResearch);

			JTree errorsTree = new JTree(vmodel);
			errorsTree.setRootVisible(false);

			JScrollPane pane = new JScrollPane();
			pane.setViewportView(errorsTree);

			add(pane, BorderLayout.CENTER);
		}
	}

	protected void filterOther() {
		vmodel.setShowDisplayErrorType(JMEErrorType.OTHER, tglbtnOther.isSelected());
	}

	protected void filterScript() {
		vmodel.setShowDisplayErrorType(JMEErrorType.SCRIPT, tglbtnScript.isSelected());
	}

	protected void filterEmbedding() {
		vmodel.setShowDisplayErrorType(JMEErrorType.EMBEDDING, tglbtnEmbedding.isSelected());
	}

	protected void filterTopology() {
		vmodel.setShowDisplayErrorType(JMEErrorType.TOPOLOGIC, tglbtnTopologicical.isSelected());
	}

	protected void changeAllErrors() {
		vmodel.setShowHierarchyErrors(tglbtnAllErrors.isSelected());
	}

	@Override
	public void reload() {

	}

	@Override
	public void unlink() {
		model.removeView(this);
	}

	@Override
	public JMEElement getSourceElement() {
		return model;
	}
}
