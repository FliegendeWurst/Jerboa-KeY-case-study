/**
 * 
 */
package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import up.jerboa.core.JerboaOrbit;

/**
 * @author Hakim Belhaouari
 *
 */
public class JOrbitComponent extends JTextField implements ActionListener,
		FocusListener {

	private static final long serialVersionUID = -2522938570233995370L;
	public static final char ALPHA = '\u03B1';
	private static final boolean HASALPHA = true;

	public JOrbitComponent() {
		super("<>");
		addActionListener(this);
		addFocusListener(this);
	}

	/**
	 * @param text
	 */
	public JOrbitComponent(String text) {
		super(text);
	}

	/**
	 * @param columns
	 */
	public JOrbitComponent(int columns) {
		super(null, "<>", columns);
	}

	public static void main(String[] args) {
		/*
		 * JFrame f = new JFrame("test component");
		 * f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 * f.getContentPane().add(new JOrbitComponent()); f.setSize(100, 30);
		 * f.setVisible(true);
		 */

		JOrbitComponent joc = new JOrbitComponent();
		joc.setText("<0 2 3fdsfd-5sfsdf4 _fdsfsd_fsd");
		joc.parseOrbit();
		System.out.println("SORTIE: " + joc.getText());

		joc.setText("<_ 1 >");
		joc.parseOrbit();
		System.out.println("SORTIE: " + joc.getText());

		joc.setText("0 _");
		joc.setText("0 -");
		joc.parseOrbit();
		System.out.println("SORTIE: " + joc.getText());
	}

	private void parseOrbit() {
		String orbits = super.getText();
		ArrayList<String> list = new ArrayList<>();
		String spattern = "(_|-1|\\d+)";
		Pattern pattern = Pattern.compile(spattern);
		Matcher matcher = pattern.matcher(orbits);
		while (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) {
				String pat = matcher.group(i);
				if (pat.startsWith("-") || pat.startsWith("_"))
					list.add("_");
				else
					list.add(pat);
			}
		}

		orbits = "<";
		for (int i = 0; i < list.size(); i++) {
			String part = list.get(i);
			if (HASALPHA && !"_".equals(part)) {
				orbits += ALPHA;
			}
			orbits += part;
			if (i != list.size() - 1)
				orbits += ", ";
		}
		orbits += ">";

		super.setText(orbits);
	}

	public JerboaOrbit getOrbit() {
		String orbits = getText();
		ArrayList<Integer> list = new ArrayList<>();
		String spattern = "(_|-1|\\d+)";
		Pattern pattern = Pattern.compile(spattern);
		Matcher matcher = pattern.matcher(orbits);
		while (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) {
				String pat = matcher.group(i);
				System.out.println("PATTERN: " + pat);
				if (pat.startsWith("-") || pat.startsWith("_"))
					list.add(-1);
				else
					list.add(Integer.parseInt(pat));
			}
		}
		return new JerboaOrbit(list);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		parseOrbit();
	}

	@Override
	public void setText(String t) {
		super.setText(t);
		parseOrbit();
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		parseOrbit();
	}

	public void setText(JerboaOrbit orbit) {
		setText(orbit.toString());
	}

}
