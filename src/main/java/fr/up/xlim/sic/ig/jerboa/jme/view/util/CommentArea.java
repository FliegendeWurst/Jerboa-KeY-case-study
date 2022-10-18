package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashSet;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

public class CommentArea extends JPanel implements ActionListener, FocusListener {

	private static final long serialVersionUID = -4234249435278372239L;
	private JEditorPane area;
	private JToggleButton edit;
	private String lastText;

	private static final String header = "<html><body>";
	private static final String footer = "</body></html>";

	private HashSet<ModifyListener> listeners;

	public CommentArea() {
		setLayout(new BorderLayout());
		listeners = new HashSet<>();

		area = new JEditorPane();
		edit = new JToggleButton("EDIT");
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(area);

		lastText = "";

		add(edit, BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);

		edit.addActionListener(this);
		area.addFocusListener(this);
	}

	public void addModifyListener(ModifyListener listener) {
		listeners.add(listener);
	}

	public void removeModifyListener(ModifyListener listener) {
		listeners.remove(listener);
	}

	public JEditorPane getEditorPane() {
		return area;
	}

	public JToggleButton getEditButton() {
		return edit;
	}

	public String getText() {
		if (edit.isSelected()) {
			return lastText;
		} else
			return area.getText();

	}

	public void setText(String text) {
		if(!lastText.equals(text)) {
			lastText = text;
			
			if(edit.isSelected())
				viewMode();
			else
				editMode();
			/*boolean reclick = false;
			if (edit.isSelected()) {
				edit.doClick();
				reclick = true;
			}
			area.setText(text);
			lastText = text;

			if (reclick)
				edit.doClick();
				*/
		}
	}
	
	private void viewMode() {
		StringBuilder sb = new StringBuilder(header);
		sb.append(lastText).append(footer);
		area.setContentType("text/html");
		area.setText(sb.toString());
		area.setEditable(false);
	}
	
	private void editMode() {
		area.setContentType("text/plain");
		area.setText(lastText);
		area.setEditable(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (edit.isSelected()) {
			lastText = area.getText();
			StringBuilder sb = new StringBuilder(header);
			sb.append(lastText).append(footer);
			area.setContentType("text/html");
			area.setText(sb.toString());
			area.setEditable(false);
		} else {
			area.setContentType("text/plain");
			area.setText(lastText);
			area.setEditable(true);
		}
		//area.requestFocus();
		update();
	}

	private void update() {
		for (ModifyListener l : listeners) {
			l.action();
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {

	}

	@Override
	public void focusLost(FocusEvent arg0) {
		update();
	}

}
