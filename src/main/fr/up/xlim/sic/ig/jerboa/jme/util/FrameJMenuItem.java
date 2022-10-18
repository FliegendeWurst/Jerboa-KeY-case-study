package fr.up.xlim.sic.ig.jerboa.jme.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class FrameJMenuItem extends JMenuItem implements ActionListener, JMEElementView {
	private static final long serialVersionUID = 7820320264215594185L;
	private JerboaModelerEditor editor;
	private JMERule rule;

	public FrameJMenuItem(JMERule rule, JerboaModelerEditor editor) {
		super(rule.getName());
		this.rule = rule;
		this.editor = editor;
		rule.addView(this);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		editor.openRule(rule);
	}


	@Override
	public void reload() {
		setText(rule.getName());
	}


	@Override
	public void unlink() {
		rule.removeView(editor);
	}

	@Override
	public JMEElement getSourceElement() {
		return rule;
	}
}
