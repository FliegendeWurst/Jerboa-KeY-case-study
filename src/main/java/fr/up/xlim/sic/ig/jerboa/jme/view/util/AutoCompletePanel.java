package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public class AutoCompletePanel extends JPopupMenu {

	private static final long serialVersionUID = -2137442944730133529L;

	private JTextComponent textComp;
	private Collection<String> wordList;

	public AutoCompletePanel(JTextComponent _textComp) {
		super();
		this.textComp = _textComp;
		wordList = new ArrayList<>();
	}

	public void updateWords(Collection<String> words) {
		wordList.clear();
		wordList.addAll(words);
	}

	public void showProposition() {
		removeAll();
		int carPos = textComp.getCaretPosition();
		String reversedString = new StringBuilder(textComp.getText().substring(0, carPos)).reverse().toString();
		int curWordSize = 0;
		String currentWord = "";
		Pattern pat = Pattern.compile("[a-zA-Z]+@?");
		Matcher m = pat.matcher(reversedString);
		if (m.find()) {
			curWordSize = m.end();
			try {
				currentWord = textComp.getText(carPos - curWordSize, curWordSize);
				System.err.println("pattern found " + currentWord);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else
			System.err.println("pattern not found");

		final String curw = currentWord;
		if (currentWord != null && currentWord.length() > 0) {
			// System.err.println("current word : " + currentWord);
			for (String s : wordList) {
				if (s.toLowerCase().startsWith(currentWord.toLowerCase())) {
					JMenuItem item = add(s);
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							final String chaine = s;// .substring(curw.length());
							try {
								textComp.getDocument().insertString(carPos - curw.length(), chaine, null);
								textComp.getDocument().remove(carPos + chaine.length() - curw.length(),
										curw.length());
							} catch (BadLocationException e1) {
							}
						}
					});
				}
			}
			if (getComponents().length > 0) {
				getSelectionModel().setSelectedIndex(1);

				Point p = textComp.getCaret().getMagicCaretPosition();
				FontMetrics fm = textComp.getFontMetrics(textComp.getFont());
				setSize(100, 100);
				if (p != null)
					show(textComp, p.x + fm.getWidths()[64], p.y + fm.getHeight());
				// textComp.requestFocus();
			}
		}
	}

	public void addWord(String w) {
		wordList.add(w);
	}

	public void addWord(Collection<String> _words) {
		wordList.addAll(_words);
	}

}
