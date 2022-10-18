package fr.up.xlim.sic.ig.jerboa.jme.windowsmanager;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;

public interface WindowContainerInterface {
	JerboaModelerEditor getEditor();
	void close();

	void activate();
	
	void switchDialogFrame();
	
	void setTitle(String title);
}