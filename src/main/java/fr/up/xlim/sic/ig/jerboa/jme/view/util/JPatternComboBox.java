package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JComboBox;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;

public class JPatternComboBox extends JComboBox<String> {

	private static final long serialVersionUID = -7801902968482441487L;

	private Pattern pattern;
	private JMEPreferences pref;
	private ModifyComboBoxListener action;
	private JMECategoryComboBoxModel model;
	private PatternComboBoxEditor textfield;

	public JPatternComboBox(JerboaModelerEditor editor, Pattern pattern, boolean force, ModifyComboBoxListener action) {
		
		this.setModel(new JMECategoryComboBoxModel(editor));
		this.pref = editor.getPreferences();
		this.pattern = pattern;
		this.model = (JMECategoryComboBoxModel) super.getModel();
		action.setComboBoxModel(model);

		this.textfield = new PatternComboBoxEditor(pref, pattern, action);

		this.action = action;
		setEditable(true);
		setEditor(textfield);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JPatternComboBox.this.action.action();
			}
		});
	}

	public JPatternComboBox(JerboaModelerEditor editor, Pattern patternModuleOrEmpty,
			ModifyComboBoxListener modifyComboBoxListener) {
		this(editor,patternModuleOrEmpty,false,modifyComboBoxListener);
	}

	@Override
	public void setBackground(Color bg) {
		if(getEditor() != null && getEditor().getEditorComponent() != null)
			this.getEditor().getEditorComponent().setBackground(bg);
		super.setBackground(bg);
	}

	public static final Pattern PATTERN_MODULE = Pattern.compile("[_]*[a-zA-Z][a-zA-Z0-9_]*([.][a-zA-Z_][a-zA-Z0-9_]*)*", Pattern.CASE_INSENSITIVE);
	public static final Pattern PATTERN_MODULE_OR_EMPTY = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*([.][a-zA-Z_][a-zA-Z0-9_]*)*|)", Pattern.CASE_INSENSITIVE);
}
