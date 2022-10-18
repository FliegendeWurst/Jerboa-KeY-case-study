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
import javax.swing.border.LineBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class ParamTopoView extends JPanel implements JMEElementView {

	private static final long serialVersionUID = -5898958552107361096L;
	private JLabel textName;
	
	private JMEParamTopo model;
	private JButton btnDelete;
	private RuleParamsPanelTopoPart owner;
	private JComboBox<Integer> comboOrder;
	private DefaultComboBoxModel<Integer> comboModelOrder;
	private ItemListener itemListener;
	
	public ParamTopoView(RuleParamsPanelTopoPart view,  JMEParamTopo model) {
		owner = view; 
		if(model != null) {
			this.model = model;
		}
		setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
		setMaximumSize(new Dimension(90000, 50));
		
		comboOrder = new JComboBox<>();
		
		int size = model.getRule().getParamsTopo().size();
		Integer[] modelInt = new Integer[size];
		for (int i = 0; i < size; i++) {
			modelInt[i] = Integer.valueOf(i);
		}
		
		comboModelOrder = new DefaultComboBoxModel<>(modelInt);
		comboOrder.setModel(comboModelOrder);
		comboOrder.setSelectedIndex(model.getOrder());
		
		itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeItem((Integer)e.getItem());
			}
		};
		comboOrder.addItemListener(itemListener);
		add(comboOrder);
		
		
		textName = new JLabel();
		add(textName);
		textName.setText(model.getNode().getName());
		
		btnDelete = new JButton("");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ParamTopoView.this.model.removeView(ParamTopoView.this);
				setVisible(false);
				owner.delParamTopo(ParamTopoView.this);
			}
		});
		btnDelete.setIcon(new ImageIcon(ParamTopoView.class.getResource("/image/cross_16x16.png")));
		add(btnDelete);
		
		if(model != null) {
			this.model = model;
			model.addView(this);
			reload();
		}
		
		setMinimumSize(new Dimension(200, 40));
	}

	
	protected void changeItem(int item) {
		// model.setOrder(item);
		if(item != model.getOrder()) {
			System.out.println("PARAM TOPO NEW ORDER: "+item);
			model.getRule().insertParamTopo(model, item);
			owner.reload();
			owner.revalidate();
			owner.repaint();
		}
	}


	private void updateComboOrders() {
		comboOrder.removeItemListener(itemListener);
		int size = model.getRule().getParamsTopo().size();
		Integer[] modelInt = new Integer[size];
		for(int i = 0;i < size; i++) {
			modelInt[i] = Integer.valueOf(i);
		}
		
		comboModelOrder.removeAllElements();
		for (Integer integer : modelInt) {
			comboModelOrder.addElement(integer);
		}
		
		comboOrder.setSelectedIndex(model.getOrder());
		comboOrder.addItemListener(itemListener);
	}


	public JMEParamTopo getModel() {
		return model;
	}

	@Override
	public void reload() {
		updateComboOrders();
		textName.setText(model.getNode().getName());
		
		owner.revalidate();
		owner.repaint();
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
