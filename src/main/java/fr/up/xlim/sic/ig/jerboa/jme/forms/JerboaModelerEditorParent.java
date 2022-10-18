package fr.up.xlim.sic.ig.jerboa.jme.forms;

import java.awt.Window;

import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;

public interface JerboaModelerEditorParent {
	Window getParent();
	void change(JerboaModelerEditor jme);
	JerboaModelerEditorParent newWindow(JerboaModelerEditor jme);
	void close();
	JMEPreferences getPreferences();
}
