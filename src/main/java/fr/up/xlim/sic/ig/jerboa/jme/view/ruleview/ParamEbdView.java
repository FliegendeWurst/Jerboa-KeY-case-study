package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JPatternTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ModifyListener;

public class ParamEbdView extends JScrollPane implements JMEElementView,Comparable<ParamEbdView> {

	private static final long serialVersionUID = -5898958552107361096L;
	private JTextField textName;
	private JTextField textType;
	private JTextField textInitValue;

	private JMEParamEbd model;
	private JButton bntDelParamEbd;
	private RuleParamsPanelEbdPart owner;
	private JLabel lblName;
	private JComboBox<Integer> comboOrder;
	private DefaultComboBoxModel<Integer> comboModelOrder;
	private ItemListener itemListener;

	public ParamEbdView(RuleParamsPanelEbdPart view, JMEParamEbd model) {
		setPreferredSize(new Dimension(760, 50));
		setMaximumSize(new Dimension(90000, 60));
		this.owner = view;

		JPanel root = new JPanel();
		setViewportView(root);

		setBorder(new LineBorder(new Color(0, 0, 0), 3, true));

		comboOrder = new JComboBox<>();

		int size = model.getRule().getParamsEbd().size();
		Integer[] modelInt = new Integer[size];
		for (int i = 0; i < size; i++) {
			modelInt[i] = Integer.valueOf(i);
		}
		
		comboModelOrder = new DefaultComboBoxModel<>(modelInt);

		comboOrder.setModel(comboModelOrder);
		comboOrder.setSelectedIndex(model.getOrder()); // ligne avant l'event
														// itemListener

		itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeItem((Integer) e.getItem());
			}
		};
		comboOrder.addItemListener(itemListener);
		root.add(comboOrder);

		lblName = new JLabel("Name:");
		root.add(lblName);

		textName = new JPatternTextField(view.getPreferences(), JPatternTextField.PATTERN_IDENT, new ModifyListener() {

			@Override
			public void action() {
				ParamEbdView.this.model.setName(textName.getText());
			}
		});
		root.add(textName);
		textName.setColumns(10);

		setMinimumSize(new Dimension(280, 40));

		root.add(new JLabel("Type:"));

		textType = new JPatternTextField(view.getPreferences(), JPatternTextField.PATTERN_COMPLEX_TYPE, new ModifyListener() {
			@Override
			public void action() {
				ParamEbdView.this.model.setType(textType.getText());
			}
		});
		root.add(textType);
		textType.setHorizontalAlignment(SwingConstants.CENTER);
		textType.setColumns(10);

		root.add(new JLabel(" = "));
		textInitValue = new JTextField();
		root.add(textInitValue);
		textInitValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ParamEbdView.this.model.setInitValue(textInitValue.getText());
			}
		});
		textInitValue.setColumns(25);

		bntDelParamEbd = new JButton("");
		bntDelParamEbd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ParamEbdView.this.model.removeView(ParamEbdView.this);
				setVisible(false);
				owner.delParamEbd(ParamEbdView.this);
			}
		});
		bntDelParamEbd.setIcon(new ImageIcon(ParamEbdView.class.getResource("/image/cross_16x16.png")));
		root.add(bntDelParamEbd);

		// add(scrollPane);

		if (model != null) {
			this.model = model;
			this.model.addView(this);
			reload();
		}

	}

	protected void updateModelInt() {
		comboOrder.removeItemListener(itemListener);
		int size = model.getRule().getParamsEbd().size();
		Integer[] modelInt = new Integer[size];
		for (int i = 0; i < size; i++) {
			modelInt[i] = Integer.valueOf(i);
		}
		// int msize = comboModelOrder.getSize();
		comboModelOrder.removeAllElements();
		for (Integer integer : modelInt) {
			comboModelOrder.addElement(integer);
		}
		comboOrder.setSelectedIndex(model.getOrder());
		comboOrder.addItemListener(itemListener);
		
	}

	protected void changeItem(int intValue) {
		if(intValue != model.getOrder()) {
			System.out.println("CHANGE ITEM: "+model+ " -> "+intValue);
			owner.insertOrder(model, intValue);
			owner.reload();
		}
	}

	public JMEParamEbd getModel() {
		return model;
	}

	@Override
	public void reload() {
		updateModelInt();
		//comboOrder.setSelectedIndex(model.getOrder());
		textType.setText(model.getType());
		textName.setText(model.getName());
		textInitValue.setText(model.getInitValue());
		
		owner.revalidate();
		owner.repaint();
	}

	@Override
	public void unlink() {
		model.removeView(this);
	}

	@Override
	public int compareTo(ParamEbdView o) {
		return getModel().compareTo(o.getModel());
	}

	@Override
	public JMEElement getSourceElement() {
		return model;
	}

}
