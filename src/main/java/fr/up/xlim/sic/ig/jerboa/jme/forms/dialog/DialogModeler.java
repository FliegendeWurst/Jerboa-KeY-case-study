package fr.up.xlim.sic.ig.jerboa.jme.forms.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.CommentArea;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JPatternTextField;

public class DialogModeler extends JDialog {
	private static final long serialVersionUID = -8796910201359062122L;
	private JTextField textName;
	private JTextField textPack;
	private JSpinner spinDim;

	private JMEModeler result;
	private CommentArea textComment;
	private JButton btnOk;

	private DialogModeler(final JerboaModelerEditor editor) {
		super(editor.getWindow());
		// setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		setTitle("Creat Modeler");
		setType(Type.UTILITY);
		setLocationRelativeTo(editor.getWindow());
		// setModalityType(ModalityType.TOOLKIT_MODAL);
		setModal(true);
		// BorderLayout borderLayout = (BorderLayout)
		// getContentPane().getLayout();

		JPanel panelCmd = new JPanel();
		getContentPane().add(panelCmd, BorderLayout.SOUTH);

		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = new JMEModeler(textName.getText(), textPack.getText(),
						((Integer) spinDim.getValue()).intValue());
				result.setComment(textComment.getText());
				setVisible(false);
				dispose();
			}
		});
		panelCmd.add(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = null;
				setVisible(false);
				dispose();

			}
		});
		panelCmd.add(btnCancel);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		panel_1.add(lblName, gbc_lblName);

		textName = new JPatternTextField(editor.getPreferences(), JPatternTextField.PATTERN_IDENT, null);
		GridBagConstraints gbc_textName = new GridBagConstraints();
		gbc_textName.insets = new Insets(0, 0, 5, 0);
		gbc_textName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textName.gridx = 1;
		gbc_textName.gridy = 0;
		panel_1.add(textName, gbc_textName);
		textName.setColumns(20);

		JLabel lblPackage = new JLabel("Package:");
		GridBagConstraints gbc_lblPackage = new GridBagConstraints();
		gbc_lblPackage.anchor = GridBagConstraints.EAST;
		gbc_lblPackage.insets = new Insets(0, 0, 5, 5);
		gbc_lblPackage.gridx = 0;
		gbc_lblPackage.gridy = 1;
		panel_1.add(lblPackage, gbc_lblPackage);

		textPack = new JPatternTextField(editor.getPreferences(), JPatternTextField.PATTERN_MODULE, null);
		GridBagConstraints gbc_textPack = new GridBagConstraints();
		gbc_textPack.insets = new Insets(0, 0, 5, 0);
		gbc_textPack.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPack.gridx = 1;
		gbc_textPack.gridy = 1;
		panel_1.add(textPack, gbc_textPack);
		textPack.setColumns(20);

		JLabel lblDimension = new JLabel("Dimension:");
		GridBagConstraints gbc_lblDimension = new GridBagConstraints();
		gbc_lblDimension.insets = new Insets(0, 0, 5, 5);
		gbc_lblDimension.gridx = 0;
		gbc_lblDimension.gridy = 2;
		panel_1.add(lblDimension, gbc_lblDimension);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		panel_1.add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));

		spinDim = new JSpinner();
		panel.add(spinDim);
		spinDim.setModel(new SpinnerNumberModel(new Integer(3), new Integer(0), null, new Integer(1)));

		JLabel lblComment = new JLabel("Comment:");
		lblComment.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.NORTH;
		gbc_lblComment.insets = new Insets(0, 0, 0, 5);
		gbc_lblComment.gridx = 0;
		gbc_lblComment.gridy = 3;
		panel_1.add(lblComment, gbc_lblComment);

		textComment = new CommentArea();
		GridBagConstraints gbc_textComment = new GridBagConstraints();
		gbc_textComment.anchor = GridBagConstraints.NORTH;
		gbc_textComment.gridx = 1;
		gbc_textComment.gridy = 3;
		panel_1.add(textComment, gbc_textComment);
		textComment.setPreferredSize(new Dimension(250, 200));
		pack();
	}

	public static JMEModeler showDialog(final JerboaModelerEditor editor) {
		DialogModeler mod = new DialogModeler(editor);
		mod.setVisible(true);
		return mod.result;
	}
}
