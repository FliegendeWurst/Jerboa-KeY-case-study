package fr.up.xlim.sic.ig.jerboa.jme.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.DockablePanel;

public class OpenedDockItem extends JButton implements ActionListener, JMEElementView {
	private static final long serialVersionUID = 7820320264215594185L;
	private DockablePanel dockpanel;
	private JMEElement model;

	public OpenedDockItem(JerboaModelerEditor editor, DockablePanel dockpanel) {
		super(dockpanel.getTitle());
				
		this.dockpanel = dockpanel;
		model = dockpanel.getSourceElement();
		model.addView(this);
		addActionListener(this);
		
		/*Border raisedBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		setBorder(raisedBorder);*/
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		dockpanel.getWindowContainer().activate();
	}


	@Override
	public void reload() {
		setText(dockpanel.getTitle());
	}


	@Override
	public void unlink() {
		model.removeView(this);
	}

	@Override
	public JMEElement getSourceElement() {
		return dockpanel.getSourceElement();
	}
}
