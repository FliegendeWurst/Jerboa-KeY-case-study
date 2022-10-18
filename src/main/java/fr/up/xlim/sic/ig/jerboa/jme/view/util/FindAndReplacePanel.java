package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;

public class FindAndReplacePanel extends JFrame implements KeyListener {
	private static final long serialVersionUID = 4110015720065061077L;
	private ExpressionPanel expPan;
	private JTextField textFind;
	private JTextField textReplace;

	private JCheckBox checkIgnoreCase;

	private JLabel notFoundLabel;

	private JButton butSearch, butReplace, butReplaceFind, butReplaceAll;

	public FindAndReplacePanel(ExpressionPanel exp, String findString) {
		this(exp);
		textFind.setText(findString);
	}

	public FindAndReplacePanel(ExpressionPanel exp) {
		super();
		expPan = exp;

		textFind = new JTextField(30);
		textFind.addKeyListener(this);
		textReplace = new JTextField(30);
		textReplace.addKeyListener(this);

		butSearch = new JButton("Search");
		butReplace = new JButton("Replace");
		butReplaceFind = new JButton("Replace find");
		butReplaceAll = new JButton("Replace all");
		notFoundLabel = new JLabel("String not found");
		notFoundLabel.setVisible(false);
		checkIgnoreCase = new JCheckBox("Ignore case");

		butSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String textToFind = textFind.getText();
				if (checkIgnoreCase.isSelected()) {
					textToFind = textToFind.toLowerCase();
				}
				String text = expPan.getText();
				if (checkIgnoreCase.isSelected()) {
					text = text.toLowerCase();
				}
				final int carPos = expPan.getCaretPos();

				int foundIndex = text.indexOf(textToFind, carPos);
				if (foundIndex >= 0) {
					expPan.getContentPanel().select(foundIndex, foundIndex + textToFind.length());
					notFoundLabel.setVisible(false);
				} else {
					foundIndex = text.indexOf(textToFind);
					if (foundIndex >= 0) {
						expPan.getContentPanel().select(foundIndex, foundIndex + textToFind.length());
						notFoundLabel.setVisible(false);
					} else {
						notFoundLabel.setVisible(true);
					}
				}
			}
		});

		butReplace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean found = false;
				if (expPan.getContentPanel().getSelectedText() != null)
					if (checkIgnoreCase.isSelected()) {
						if (expPan.getContentPanel().getSelectedText().compareToIgnoreCase(textFind.getText()) == 0) {
							found = true;
						}
					} else {
						if (expPan.getContentPanel().getSelectedText().compareTo(textFind.getText()) == 0) {
							found = true;
						}
					}
				if (found) {
					expPan.getContentPanel().replaceSelection(textReplace.getText());
				}
			}
		});

		butReplaceFind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				butReplace.doClick();
				butSearch.doClick();
			}
		});

		JLabel labSearch = new JLabel("Find ");
		JLabel labReplace = new JLabel("Replace with ");

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gbl_panel);

		GridBagConstraints gbc_search = new GridBagConstraints();
		gbc_search.insets = new Insets(0, 0, 5, 0);
		gbc_search.fill = GridBagConstraints.HORIZONTAL;
		gbc_search.gridx = 0;
		gbc_search.gridy = 0;
		add(labSearch, gbc_search);

		GridBagConstraints gbc_replace = new GridBagConstraints();
		gbc_replace.insets = new Insets(0, 0, 5, 0);
		gbc_replace.fill = GridBagConstraints.HORIZONTAL;
		gbc_replace.gridx = 0;
		gbc_replace.gridy = 1;
		add(labReplace, gbc_replace);

		GridBagConstraints gbc_textSearch = new GridBagConstraints();
		gbc_textSearch.insets = new Insets(0, 0, 5, 0);
		gbc_textSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_textSearch.gridx = 1;
		gbc_textSearch.gridy = 0;
		add(textFind, gbc_textSearch);

		GridBagConstraints gbc_textReplace = new GridBagConstraints();
		gbc_textReplace.insets = new Insets(0, 0, 5, 0);
		gbc_textReplace.fill = GridBagConstraints.HORIZONTAL;
		gbc_textReplace.gridx = 1;
		gbc_textReplace.gridy = 1;
		add(textReplace, gbc_textReplace);

		Box boxSearch = Box.createHorizontalBox();
		boxSearch.add(Box.createHorizontalGlue());
		boxSearch.add(butSearch);
		boxSearch.add(Box.createHorizontalGlue());

		Box boxReplace = Box.createHorizontalBox();
		boxReplace.add(butReplace);
		boxReplace.add(Box.createHorizontalGlue());

		GridBagConstraints gbc_butSearch = new GridBagConstraints();
		gbc_butSearch.insets = new Insets(0, 0, 5, 0);
		gbc_butSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_butSearch.gridx = 0;
		gbc_butSearch.gridy = 2;
		add(boxSearch, gbc_butSearch);

		GridBagConstraints gbc_butReplace = new GridBagConstraints();
		gbc_butReplace.insets = new Insets(0, 0, 5, 0);
		gbc_butReplace.fill = GridBagConstraints.HORIZONTAL;
		gbc_butReplace.gridx = 1;
		gbc_butReplace.gridy = 2;
		add(boxReplace, gbc_butReplace);

		Box boxReplaceFind = Box.createHorizontalBox();
		boxReplaceFind.add(Box.createHorizontalGlue());
		boxReplaceFind.add(butReplaceFind);
		boxReplaceFind.add(Box.createHorizontalGlue());

		Box boxReplaceAll = Box.createHorizontalBox();
		boxReplaceAll.add(butReplaceAll);
		boxReplaceAll.add(Box.createHorizontalGlue());

		GridBagConstraints gbc_butReplaceFind = new GridBagConstraints();
		gbc_butReplaceFind.insets = new Insets(0, 0, 5, 0);
		gbc_butReplaceFind.fill = GridBagConstraints.HORIZONTAL;
		gbc_butReplaceFind.gridx = 0;
		gbc_butReplaceFind.gridy = 3;
		add(boxReplaceFind, gbc_butReplaceFind);

		GridBagConstraints gbc_butReplaceAll = new GridBagConstraints();
		gbc_butReplaceAll.insets = new Insets(0, 0, 5, 0);
		gbc_butReplaceAll.fill = GridBagConstraints.HORIZONTAL;
		gbc_butReplaceAll.gridx = 1;
		gbc_butReplaceAll.gridy = 3;
		add(boxReplaceAll, gbc_butReplaceAll);

		GridBagConstraints gbc_checkIgnoreCase = new GridBagConstraints();
		gbc_checkIgnoreCase.insets = new Insets(0, 0, 5, 0);
		gbc_checkIgnoreCase.fill = GridBagConstraints.HORIZONTAL;
		gbc_checkIgnoreCase.gridx = 0;
		gbc_checkIgnoreCase.gridy = 4;
		add(checkIgnoreCase, gbc_checkIgnoreCase);

		GridBagConstraints gbc_labNotFound = new GridBagConstraints();
		gbc_labNotFound.insets = new Insets(0, 0, 5, 0);
		gbc_labNotFound.fill = GridBagConstraints.HORIZONTAL;
		gbc_labNotFound.gridx = 1;
		gbc_labNotFound.gridy = 5;
		add(notFoundLabel, gbc_labNotFound);

		pack();
	}

	public void setTextToFind(String text) {
		textFind.setText(text);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		JDialog dial = new JDialog(f, "test", true);
		dial.setMinimumSize(new Dimension(100, 100));
		dial.add(new JLabel("test"));
		ExpressionPanel pan = new ExpressionPanel("", new JMEModeler("test", "", 3), null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		f.setMinimumSize(new Dimension(600, 600));
		f.add(pan, BorderLayout.CENTER);
		f.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// System.err.println(e.getKeyCode() + " - " + e.getKeyChar());
		if (e.getKeyCode() == 10) {// enter pressed
			butSearch.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
