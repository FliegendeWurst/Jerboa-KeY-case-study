package fr.up.xlim.sic.ig.jerboa.jme.view.util.tab;

import java.awt.Component;

import javax.swing.JPanel;

public interface TabDescription {
	Component getTabComponent();
	JPanel getPanel();
	void OnClose();
	String getInitialTitle();
}

