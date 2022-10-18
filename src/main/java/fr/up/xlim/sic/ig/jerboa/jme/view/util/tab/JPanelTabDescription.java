package fr.up.xlim.sic.ig.jerboa.jme.view.util.tab;

import java.awt.Component;

import javax.swing.JPanel;


/**
 * This class defines all panels which can be added to our TabbedPane. In this case, the tab can be
 * closed with a tiny cross button on the right.
 * This class is abstract and must be inherited by any open/closable tab panel.
 * 
 * @author Hakim
 *
 */
public abstract class JPanelTabDescription extends JPanel implements TabDescription {
	private static final long serialVersionUID = 6279191777572901775L;
	
	private JMEButtonTabComponent button;
	private JMETabbedPane pane;
	private String title;
	
	public JPanelTabDescription( String initTitle) {
	}
	
	public JPanelTabDescription(JMETabbedPane pane, String initTitle) {
		this.pane = pane;
		button = new JMEButtonTabComponent(pane);
		this.title = initTitle;
	}
	
	protected void buildClosableTab(JMETabbedPane pane, String initTitle) {
		this.pane = pane;
		button = new JMEButtonTabComponent(pane);
		this.title = initTitle;
	}

	@Override
	public Component getTabComponent() {
		return button;
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public String getInitialTitle() {
		return title;
	}
	
	protected void setTabTitle(String title) {
		this.title = title;
	}
	
	protected JMETabbedPane getTabbedPane() {
		return pane;
	}

}
