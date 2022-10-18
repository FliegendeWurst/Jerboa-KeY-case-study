package fr.up.xlim.sic.ig.jerboa.jme.view.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JMENodeMultiplicityTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JOrbitComponent;
import net.miginfocom.swing.MigLayout;
import up.jerboa.core.JerboaOrbit;

public class RightRuleNodeInfo extends JPanelElementView implements ActionListener, JMEElementView {

	private static final long serialVersionUID = -1594993146749995406L;
	private JMENode node;
	private JTextField nodename;
	private JOrbitComponent orbitComponent;
	private RuleView owner;
	private Box horizEbds;
	private JButton btnValid;
	private JComboBox<JMEEmbeddingInfo> comboBox;
	private JMENodeMultiplicityTextField multi;
	private JPanel panelColor;

	public RightRuleNodeInfo(RuleView parent, JMENode node) {
		this.owner = parent;
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		setPreferredSize(new Dimension(574, 35));
		this.node = node;
		node.addView(this);

		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);

		JLabel label = new JLabel("Name:");
		horizontalBox.add(label);

		nodename = new JTextField();
		nodename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RightRuleNodeInfo.this.node.setName(nodename.getText());
				save();
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
		orbitComponent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RightRuleNodeInfo.this.node.setOrbit(orbitComponent.getOrbit());
				save();
				owner.refresh();
				parent.check();
			}
		});
		orbitComponent.setColumns(10);
		horizontalBox_1.add(orbitComponent);
		
		//----
		if(owner.getRule() instanceof JMEScript) {
			Box horizontalBox_3 = Box.createHorizontalBox();
			add(horizontalBox_3);

			JLabel label_3 = new JLabel("Multiplicity:");
			horizontalBox_3.add(label_3);

			multi = new JMENodeMultiplicityTextField();
			multi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					RightRuleNodeInfo.this.node.setMultiplicity(multi.getMultiplicity());
					owner.refresh();
					parent.check();
				}
			});
			multi.setColumns(7);
			horizontalBox_3.add(multi);
		}
		//----

		horizEbds = Box.createHorizontalBox();
		add(horizEbds);

		if(owner.getRule() instanceof JMERuleAtomic) {

			Box horizCmds = Box.createHorizontalBox();
			add(horizCmds);

			btnValid = new JButton("EDIT");
			btnValid.addActionListener(this);
			comboBox = new JComboBox<JMEEmbeddingInfo>();

			loadEbds();

			horizCmds.add(comboBox);
			horizCmds.add(Box.createRigidArea(new Dimension(5, 0)));
			horizCmds.add(btnValid);
		}
		
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
					Color color = JColorChooser.showDialog(RightRuleNodeInfo.this, "Select fill color of "+node.getName(), node.getColor());
					if(color != null) {
						node.setColor(color);
					}
				}
			}
		});
		
		
		reload();
	}

	private void loadEbds() {
		if (node != null) { // pour le concepteur d'ihm
			JMEModeler modeler = node.getRule().getModeler();
			List<JMEEmbeddingInfo> ebds = modeler.getEmbeddings();
			for (JMEEmbeddingInfo ebd : ebds) {
				comboBox.addItem(ebd);
			}
		}
	}

	@Override
	public void unlink() {
		node.removeView(this);
	}

	public JMENode getNode() {
		return node;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JMEEmbeddingInfo ebdinfo = comboBox.getItemAt(comboBox.getSelectedIndex());
		if (ebdinfo != null) {
			String title = node.getName() + "#" + ebdinfo.getName();
			System.err.println("Edition: " + title);
			// save();
			owner.openNodeExpression(node, ebdinfo);
		}

	}

	@Override
	public void reload() {
		nodename.setText(node.getName());
		orbitComponent.setText(node.getOrbit());
		panelColor.setBackground(node.getColor());
	}

	public void save() {
		String text = orbitComponent.getText();
		JerboaOrbit orbit = orbitComponent.getOrbit();
		System.err.println("Brute: " + text);
		System.err.println("Orbit: " + orbit);
		node.setOrbit(orbit);
		node.setName(nodename.getText());
		owner.refresh();
	}

	@Override
	public JMEElement getSourceElement() {
		return node;
	}

}
