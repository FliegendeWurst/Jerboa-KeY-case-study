package fr.up.xlim.sic.ig.jerboa.jme.view.graph;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public abstract class JPanelElementView extends JPanel implements JMEElementView {

	private static final long serialVersionUID = -6067202662036406382L;

	public JPanelElementView() {
	}

	public JPanelElementView(LayoutManager layout) {
		super(layout);
	}


}
