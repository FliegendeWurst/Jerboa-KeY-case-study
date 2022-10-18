package fr.up.xlim.sic.ig.jerboa.jme.view.util.tab;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JTabbedPane;

public class JMETabbedPane extends JTabbedPane {
	private static final long serialVersionUID = -5765164859460977250L;

	private HashMap<Component, TabDescription> map;

	public JMETabbedPane() {
		super();
		initialize();
	}

	public JMETabbedPane(int tabPlacement) {
		super(tabPlacement);
		initialize();
	}
	
	public JMETabbedPane(int tabPlacement, int tabPolicy) {
		super(tabPlacement,tabPolicy);
		initialize();
	}
	
	private void initialize() {
		map = new HashMap<>();
	}

	@Override
	public void remove(int index) {
		Component comp = getTabComponentAt(index);
		if(map.containsKey(comp)) {
			map.get(comp).OnClose();
			map.remove(comp);
		}
		super.remove(index);
	}
	
	public void addTabDescription(TabDescription tab) {
		int index = this.getTabCount();
		
		final Component comp = tab.getTabComponent();
		if(comp == null) {
			this.add(tab.getInitialTitle(), tab.getPanel());
			this.setSelectedIndex(index);
		}
		else {
			if(map.containsKey(comp)) {
				TabDescription t = map.get(comp);
				this.setSelectedComponent(t.getPanel());
			}
			else {			
				this.add(tab.getInitialTitle(), tab.getPanel());
				this.setTabComponentAt(index, comp);
				this.setSelectedIndex(index);
				map.put(comp, tab);
			}
		}
	}
	
	public void removeTabDescription(TabDescription tab) {
		final Component comp = tab.getTabComponent();
		map.remove(comp);
		this.remove(tab.getPanel());
		
	}
}
