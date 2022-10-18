package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;

public class JPatternTextField extends JTextField {

	private static final long serialVersionUID = 6752408396406450284L;
	private Pattern pattern;
	private JMEPreferences pref;
	private JMEVerifier verifier;
	private ModifyListener action;

	public JPatternTextField(JMEPreferences pref, String pattern, boolean force, ModifyListener action) {
		this(pref, Pattern.compile(pattern),force,action);
	}

	public JPatternTextField(JMEPreferences pref, String pattern, int flags, boolean force, ModifyListener action) {
		this(pref, Pattern.compile(pattern, flags),force,action);
	}

	public JPatternTextField(JMEPreferences pref, Pattern pattern, ModifyListener action) {
		this(pref,pattern,false,action);
	}

	public JPatternTextField(JMEPreferences pref, Pattern pattern, boolean force, ModifyListener action) {
		this.pref = pref;
		this.pattern = pattern;
		this.verifier = new JMEVerifier();
		if(force) // on ne peut pas sortir du composant sans une vrai valeur
			setInputVerifier(verifier);
		addActionListener(verifier);
		addKeyListener(verifier);
		this.action = action;
	}

	@Override
	public void setText(String t) {
		if(t != null && !t.equals(getText())) {
			super.setText(t);
			verifier.verify(this);
		}
	}

	public void setSimpleText(String t) {
		if(t != null && !t.equals(getText())) {
			super.setText(t);
		}
	}

	private boolean recursiveCheckType(String type) {
		Matcher matcher = pattern.matcher(type);
		if(matcher.matches()) {
			boolean s = true;
			if(pattern == PATTERN_MODULE) {
				String templates = matcher.group(4);
				if(templates != null) {
					String[] splited =  templates.split(",");
					for (String subtype : splited) {
						s = s && recursiveCheckType(subtype.trim());
					}
				}
			}
			return s;
		}
		return false;
	}

	class JMEVerifier extends InputVerifier implements ActionListener,KeyListener {

		@Override
		public boolean verify(JComponent input) {
			if(input instanceof JTextField) {
				JTextField textfield = (JTextField) input;
				String text = textfield.getText();
				boolean b = recursiveCheckType(text);
				if(b) {
					textfield.setBackground(pref.getBGColor());
					if(action != null)
						action.action();
				}
				else
					textfield.setBackground(pref.getErrBGColor());
				//System.out.println("CHECK VERIF: "+text+"/"+b);
				return b;
			}
			return true;
		}

		@Override
		public boolean shouldYieldFocus(JComponent input) {
			return verify(input);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			verifier.verify(JPatternTextField.this);
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			verifier.verify(JPatternTextField.this);
		}
	}

	public static final Pattern PATTERN_COMPLEX_TYPE = Pattern.compile("([_]*[a-zA-Z][a-zA-Z0-9_]*([.][_]*[a-zA-Z][a-zA-Z0-9_]*)*(<(.*)>)?)|@ebd<([_]*[a-zA-Z][a-zA-Z0-9_]*([.][_]*[a-zA-Z][a-zA-Z0-9_]*)*)>", Pattern.CASE_INSENSITIVE);

	public static final Pattern PATTERN_IDENT = Pattern.compile("[_]*[a-zA-Z][a-zA-Z0-9_]*", Pattern.CASE_INSENSITIVE);
	public static final String REGEXP_SIMPLE_TYPE = "([_]*[a-zA-Z][a-zA-Z0-9_]*([.][_]*[a-zA-Z][a-zA-Z0-9_]*)*)"; // 2
	// groupes
	public static final Pattern PATTERN_MODULE = Pattern.compile(
			"([_]*[a-zA-Z][a-zA-Z0-9_]*([.][_]*[a-zA-Z][a-zA-Z0-9_]*)*)(<(.*)>)?\\*?\\z", Pattern.CASE_INSENSITIVE);
	public static final Pattern PATTERN_MODULE_OR_EMPTY = Pattern.compile("([_]*[a-zA-Z][a-zA-Z0-9_]*([.][_]*[a-zA-Z][a-zA-Z0-9_]*)*|)", Pattern.CASE_INSENSITIVE);
}
