package fr.up.xlim.sic.ig.jerboa.jme.view.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleView;

public class ArcInfo extends  JPanelElementView implements ActionListener,JMEElementView  {

	private static final long serialVersionUID = 6873475796013563009L;
	private JMEArc arc;
	private RuleView owner;
	private JSpinner dimension;
	private JLabel leftNode;
	private JLabel rightNode;
	private JButton btnOk;
	private JButton btnDeselect;

	public ArcInfo(RuleView parent, JMEArc arc) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.arc = arc;
		this.arc.addView(this);
		this.owner = parent;
		
		leftNode = new JLabel("n1");
		add(leftNode);
		
		JLabel label = new JLabel("---");
		add(label);
		
		dimension = new JSpinner();
		dimension.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		dimension.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				save();
			}
		});
		dimension.setToolTipText("Dimension of the arc");
		dimension.setPreferredSize(new Dimension(50, 20));
		add(dimension);
		
		JLabel label_1 = new JLabel("---");
		add(label_1);
		
		rightNode = new JLabel("n2");
		add(rightNode);
		
		btnOk = new JButton("OK");
		btnOk.setVisible(false);
		btnOk.addActionListener(this);
		add(btnOk);
		
		btnDeselect = new JButton("Deselect");
		btnDeselect.setVisible(false);
		add(btnDeselect);
		reload();
	}
	
	@Override
	public void reload() {
		leftNode.setText(arc.getSource().getName());
		rightNode.setText(arc.getDestination().getName());
		dimension.setValue(arc.getDimension());
	}
	
	public void save() {
		arc.setDimension(((Integer)dimension.getValue()).intValue());
		owner.refresh();
	}
	
	@Override
	public void unlink() {
		arc.removeView(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		save();
	}

	@Override
	public JMEElement getSourceElement() {
		return arc;
	}

}
