package fr.up.xlim.sic.ig.jerboa.jme.view.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeKind;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JMENodeMultiplicityTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JOrbitComponent;
import net.miginfocom.swing.MigLayout;

public class LeftRuleNodeInfo extends JPanelElementView implements ActionListener, JMEElementView {

	private static final long serialVersionUID = -1594993146749995406L;
	private JMENode node;
	private JTextField nodename;
	private JOrbitComponent orbitComponent;
	private JCheckBox isHook;
	private RuleView owner;
	private JMENodeMultiplicityTextField multi;
	private JPanel panelColor;
	

	public LeftRuleNodeInfo(RuleView parent, JMENode node) {
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setPreferredSize(new Dimension(477, 35));
		this.node = node;
		node.addView(this);
		owner = parent;
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);

		JLabel label = new JLabel("Name:");
		horizontalBox.add(label);

		nodename = new JTextField();
		nodename.setText(node.getName());
		nodename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LeftRuleNodeInfo.this.node.setName(nodename.getText());
				owner.refresh();
				parent.check();
			}
		});
		nodename.setColumns(10);
		horizontalBox.add(nodename);

		Box horizontalBox_1 = Box.createHorizontalBox();
		add(horizontalBox_1);

		JLabel label_1 = new JLabel("Orbit:");
		horizontalBox_1.add(label_1);

		orbitComponent = new JOrbitComponent();
		orbitComponent.setText(node.getOrbit().toString());
		orbitComponent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LeftRuleNodeInfo.this.node.setOrbit(orbitComponent.getOrbit());
				owner.refresh();
				parent.check();
			}
		});
		orbitComponent.setColumns(10);
		horizontalBox_1.add(orbitComponent);

		// ----
		if (owner.getRule() instanceof JMEScript) {
			Box horizontalBox_3 = Box.createHorizontalBox();
			add(horizontalBox_3);

			JLabel label_3 = new JLabel("Multiplicity:");
			horizontalBox_3.add(label_3);

			multi = new JMENodeMultiplicityTextField();
			multi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					LeftRuleNodeInfo.this.node.setMultiplicity(multi.getMultiplicity());
					owner.refresh();
					parent.check();
				}
			});
			multi.setColumns(7);
			horizontalBox_3.add(multi);
		}
		// ----

		Box horizontalBox_2 = Box.createHorizontalBox();
		add(horizontalBox_2);

		isHook = new JCheckBox("Hook");
		isHook.setSelected(node.getKind() == JMENodeKind.HOOK);
		isHook.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				LeftRuleNodeInfo.this.node.setKind(isHook.isSelected() ? JMENodeKind.HOOK : JMENodeKind.SIMPLE);
				owner.refresh();
				parent.check();
			}
		});
		horizontalBox_2.add(isHook);

		Box horizEbds = Box.createHorizontalBox();
		add(horizEbds);
		
		JButton btnPrecond = new JButton("Precond");
		btnPrecond.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				owner.openNodePrecondition(LeftRuleNodeInfo.this.node);
			}
		});
		add(btnPrecond);

		JButton btnValid = new JButton("OK");
		btnValid.setVisible(false);
		btnValid.addActionListener(this);
		add(btnValid);

		panelColor = new JPanel();
		panelColor.setBorder(new BevelBorder(BevelBorder.RAISED));
		add(panelColor, "cell 0 0,growy");
		panelColor.setLayout(new MigLayout("", "[grow,fill]", "[]"));
		Component rigidArea1 = Box.createRigidArea(new Dimension(20, 10));
		rigidArea1.setMaximumSize(new Dimension(60, 10));
		rigidArea1.setPreferredSize(new Dimension(60, 10));
		rigidArea1.setMinimumSize(new Dimension(60, 10));
		panelColor.add(rigidArea1);
		panelColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if(SwingUtilities.isLeftMouseButton(e)) {
					Color color = JColorChooser.showDialog(LeftRuleNodeInfo.this, "Select fill color of "+node.getName(), node.getColor());
					if(color != null) {
						node.setColor(color);
					}
				}
			}
		});
		
		reload();
	}

	public JMENode getNode() {
		return node;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		save();
	}

	@Override
	public void reload() {
		orbitComponent.setText(node.getOrbit().toString());
		nodename.setText(node.getName());
		isHook.setSelected(node.getKind() == JMENodeKind.HOOK);
		panelColor.setBackground(node.getColor());
		if (owner.getRule() instanceof JMEScript) {
			multi.setText(node.getMultiplicity());
		}
	}

	@Override
	public void unlink() {
		node.removeView(this);
	}

	public void save() {
		node.setOrbit(orbitComponent.getOrbit());
		node.setName(nodename.getText());
		if (isHook.isSelected())
			node.setKind(JMENodeKind.HOOK);
		else
			node.setKind(JMENodeKind.SIMPLE);
		owner.refresh();
	}

	@Override
	public JMEElement getSourceElement() {
		return node;
	}

}
