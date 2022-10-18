package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class RuleParamsPanelTopoPart extends JPanel implements JMEElementView {
	private static final long serialVersionUID = -1886649788174443139L;

	private JMERule model;
	private HashMap<JMEParamTopo, ParamTopoView> map;
	private Box panelListParamTopos;

	private RuleView owner;

	public RuleParamsPanelTopoPart(RuleView view, JMERule model) {
		this.owner = view;
		setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		panelListParamTopos = Box.createVerticalBox(); // new JPanel();
		scrollPane.setViewportView(panelListParamTopos);
		// panelListParamTopos.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblParamsTopos = new JLabel("Topological parameters:");
		lblParamsTopos.setLabelFor(scrollPane);
		add(lblParamsTopos, BorderLayout.NORTH);

		JPanel panelCommand = new JPanel();
		add(panelCommand, BorderLayout.SOUTH);

		JButton bntRefresh = new JButton("Refresh");
		bntRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				reload();
			}
		});
		panelCommand.add(bntRefresh);

		map = new HashMap<>();

		if (model != null) {
			this.model = model;
			this.model.addView(this);
			reload();
		}
	}

	@Override
	public void reload() {
		panelListParamTopos.removeAll();
		map.clear();
		List<JMEParamTopo> topos = model.getParamsTopo();
		for (JMEParamTopo p : topos) {
			ParamTopoView v = new ParamTopoView(this, p);
			if (!map.containsKey(p)) {
				map.put(p, v);
				panelListParamTopos.add(v);
			}
		}
		panelListParamTopos.invalidate();
	}

	@Override
	public void unlink() {
		model.removeView(this);

	}

	public void delParamTopo(ParamTopoView paramTopoView) {
		JMEParamTopo p = paramTopoView.getModel();
		panelListParamTopos.remove(paramTopoView);
		owner.getRule().delParamTopo(p);
	}
	
	@Override
	public JMEElement getSourceElement() {
		return model;
	}
}
