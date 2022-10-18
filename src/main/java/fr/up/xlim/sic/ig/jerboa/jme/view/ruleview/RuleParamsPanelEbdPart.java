package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class RuleParamsPanelEbdPart extends JPanel implements JMEElementView, MouseMotionListener {
	private static final long serialVersionUID = -1886649788174443139L;

	private JMERule model;
	private Box panelListParamEbds;
	private JButton btnAdd;

	private RuleView owner;
	private HashMap<JMEParamEbd, ParamEbdView> ebdviews;

	public RuleParamsPanelEbdPart(RuleView view, JMERule model) {
		this.owner = view;
		ebdviews = new HashMap<>();
		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		panelListParamEbds = Box.createVerticalBox();
		scrollPane.setViewportView(panelListParamEbds);

		JLabel lblParamsEbds = new JLabel("Embedding parameters:");
		lblParamsEbds.setLabelFor(scrollPane);
		add(lblParamsEbds, BorderLayout.NORTH);

		JPanel panelCommand = new JPanel();
		JScrollPane scrollPaneCmd = new JScrollPane();
		scrollPaneCmd.setViewportView(panelCommand);
		add(scrollPaneCmd, BorderLayout.SOUTH);

		btnAdd = new JButton("Creat New Embedding Parameter");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewParamEbd();
			}
		});
		panelCommand.add(btnAdd);
		if (model != null) {
			this.model = model;
			this.model.addView(this);
			reload();
		}
	}

	protected void addNewParamEbd() {
		model.addNewParamEbd();
		// reload(); normalement se fait dans la fct just avant.
	}

	@Override
	public void reload() {
		
		Collection<JMEParamEbd> ebds = new ArrayList<>(model.getParamsEbd());
		HashMap<JMEParamEbd, ParamEbdView> newebdviews = new HashMap<>();
		ArrayList<ParamEbdView> orderedPEV = new ArrayList<>();
		
		for (JMEParamEbd pe : ebds) {
			if(ebdviews.containsKey(pe)) {
				ParamEbdView pev = ebdviews.remove(pe);
				newebdviews.put(pe, pev);
				orderedPEV.add(pev);
			}
			else {
				ParamEbdView pev = new ParamEbdView(this, pe);
				newebdviews.put(pe, pev);
				orderedPEV.add(pev);
			}
			
		}
		panelListParamEbds.removeAll();
		
		
		for (ParamEbdView pev: ebdviews.values()) {
			pev.unlink();
		}
		
		Collections.sort(orderedPEV);
		for (ParamEbdView pev : orderedPEV) {
			if(pev != null)
				panelListParamEbds.add(pev);
		}
		
		ebdviews = newebdviews;
		
		revalidate();
		repaint();
	}

	@Override
	public void unlink() {
		model.removeView(this);
		for (Entry<JMEParamEbd, ParamEbdView> p : ebdviews.entrySet()) {
			p.getValue().unlink();
			p.getValue().setVisible(false);
		}
		panelListParamEbds.removeAll();
		ebdviews.clear();
	}	

	public JMEPreferences getPreferences() {
		return owner.getPreferences();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("drag: "+e.toString());
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("move: "+e.toString());		
	}

	public void delParamEbd(ParamEbdView paramEbdView) {
		JMEParamEbd p = paramEbdView.getModel();
		panelListParamEbds.remove(paramEbdView);
		ebdviews.remove(paramEbdView);
		final int porder = p.getOrder();
		for (Entry<JMEParamEbd, ParamEbdView> elt : ebdviews.entrySet()) {
			JMEParamEbd pe = elt.getKey();
			final int peorder = pe.getOrder();
			if(peorder > porder) {
				pe.setOrder(peorder - 1);
			}
		}
		owner.getRule().delParamEbd(p);
	}
	
	public void insertOrder(JMEParamEbd model2, int nextorder) {
		final int maxorder = model2.getOrder();
		for (Entry<JMEParamEbd, ParamEbdView> elt : ebdviews.entrySet()) {
			JMEParamEbd pe = elt.getKey();
			final int peorder = pe.getOrder();
			if(nextorder < maxorder && peorder >= nextorder && peorder < maxorder) {
				pe.setOrder(pe.getOrder() + 1);
			}
			
			if(nextorder > maxorder && peorder > maxorder && peorder <= nextorder) {
				pe.setOrder(pe.getOrder() - 1);
			}
		}
		model2.setOrder(nextorder);
	}

	@Override
	public JMEElement getSourceElement() {
		return model;
	}
}
