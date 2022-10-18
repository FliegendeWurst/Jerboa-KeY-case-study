package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import fr.up.xlim.sic.ig.jerboa.jme.view.ruletree.RuleTreeViewerRenderer;

public class JClearableTextField extends JPanel {

	private static final long serialVersionUID = -6489659407150769942L;

	private static Icon cross = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/cross.png")).getImage().getScaledInstance(10,
					10, Image.SCALE_SMOOTH));
	private static Icon crossHover = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/crossHover.png"))
			.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH));

	private JButton butClear;
	private JTextField my_textfield;

	public JClearableTextField() {
		this(30);
	}

	public JClearableTextField(int col) {
		super(new FlowLayout());// FlowLayout.CENTER, 0, 0));

		my_textfield = new JTextField("");
		my_textfield.setColumns(col);
		
		// my_textfield.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		// Border lowered_bevelborder = BorderFactory.createLoweredBevelBorder();
		// textfield_with_border.setBorder(lowered_bevelborder);
		//
		// textfield_with_button.add(my_textfield);
		// textfield_with_button.add(my_button);
		// setBorder(lowered_bevelborder);
		// setBackground(Color.RED);

		butClear = new JButton(cross);
		butClear.setBorder(null);
		butClear.setContentAreaFilled(false);
		butClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JClearableTextField.this.my_textfield.setText("");
			}
		});
		butClear.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				butClear.setIcon(cross);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				butClear.setIcon(crossHover);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		add(my_textfield);
		add(butClear);
	}

	public String getText() {
		return my_textfield.getText();
	}

	public Document getDocument() {
		return my_textfield.getDocument();
	}

	@Override
	public void addKeyListener(final KeyListener kl) {
		my_textfield.addKeyListener(kl);
	}
	
	public void addDocumentListener(DocumentListener dl) {
		my_textfield.getDocument().addDocumentListener(dl);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("test");
		Box b = Box.createVerticalBox();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		b.add(new JClearableTextField());
		b.add(Box.createVerticalGlue());
		f.add(b);
		f.setVisible(true);
	}

}
