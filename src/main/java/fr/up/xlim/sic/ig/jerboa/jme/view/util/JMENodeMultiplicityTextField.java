package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeMultiplicity;

public class JMENodeMultiplicityTextField extends JTextField {

	private static final long serialVersionUID = 8769753908538355251L;
	
	public JMENodeMultiplicityTextField() {
		super("{1}");
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				setText(parseMultiplicity());
			}
		});
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setText(parseMultiplicity());
			}
		});
	}

	private JMENodeMultiplicity parseMultiplicity() {
		String multi = super.getText();
		String spattern = "(\\d+|[*]|[nm])";
		Pattern pattern = Pattern.compile(spattern);
		Matcher matcher = pattern.matcher(multi);
		ArrayList<Integer> values = new ArrayList<>(2);
		while(matcher.find()) {
			for(int i = 0;i < matcher.groupCount(); i++) {
				String pat = matcher.group(i);
				if(pat.equals("*") || pat.equals("n") || pat.equals("m"))
					values.add(Integer.MAX_VALUE);
				else
					values.add(Integer.parseInt(pat));
			}
		}
		
		JMENodeMultiplicity res;
		switch(values.size()) {
		case 0:
			res = new JMENodeMultiplicity(1, 1);
			break;
		case 1:
			res = new JMENodeMultiplicity(values.get(0), values.get(0));
			break;
		default:
			res = new JMENodeMultiplicity(values.get(0), values.get(1));
			break;
		}
		return res;
	}

	public void setText(JMENodeMultiplicity multiplicity) {
		setText(multiplicity.toString());
	}

	public JMENodeMultiplicity getMultiplicity() {
		return parseMultiplicity();
	}
	
}
