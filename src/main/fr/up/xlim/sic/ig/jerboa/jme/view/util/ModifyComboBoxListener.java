package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import javax.swing.ComboBoxModel;

public interface ModifyComboBoxListener extends ModifyListener {
	
	void setComboBoxModel(ComboBoxModel<String> model);
}
