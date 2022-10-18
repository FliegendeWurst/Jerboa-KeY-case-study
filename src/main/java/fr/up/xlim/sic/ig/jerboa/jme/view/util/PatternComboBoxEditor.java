package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.Component;
import java.util.regex.Pattern;

import javax.swing.ComboBoxEditor;

import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;

class PatternComboBoxEditor extends JPatternTextField implements ComboBoxEditor {
	private static final long serialVersionUID = 6980588635782643305L;

	public PatternComboBoxEditor(JMEPreferences pref, Pattern pattern, ModifyListener action) {
		super(pref, pattern, null);
		setBorder(null);
	}

	@Override
	public Component getEditorComponent() {
		return this;
	}

	@Override
	public Object getItem() {
		return super.getText();
	}
	
	@Override
	public void setItem(Object anObject) {
		if(anObject != null)
			super.setText(anObject.toString());
	}

}