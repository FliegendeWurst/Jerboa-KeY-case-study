package fr.up.xlim.sic.ig.jerboa.jme.forms.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.CommentArea;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JOrbitComponent;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JPatternTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ModifyListener;

@Deprecated
public class DialogEmbedding extends JDialog {
	private static final long serialVersionUID = -8359462776933320908L;

	private JMEModeler modeler;

	private JMEEmbeddingInfo result;
	private JTextField textName;
	private JOrbitComponent textOrbit;
	private JTextField textType;
	private JButton bntSearchType;

	private CommentArea textComment;

	private UndoManager manager;

	private JLabel allreadyUsedName;

	private JTextField textHeader;

	private JButton bntSearchHeader;

	public DialogEmbedding(JerboaModelerEditor parent, JMEEmbeddingInfo ebdInfo) {
		super(parent.getWindow(), "Embedding form");
		Dimension dim = new Dimension(450, 300);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setType(Type.UTILITY);
		this.modeler = parent.getModeler();
		this.manager = modeler.getUndoManager();
		setModalityType(ModalityType.APPLICATION_MODAL);

		JPanel panelCmd = new JPanel();
		getContentPane().add(panelCmd, BorderLayout.SOUTH);

		textHeader = new JTextField();

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hasValidName(ebdInfo, textName.getText())) {
					if (ebdInfo == null) {
						result = new JMEEmbeddingInfo(modeler, DialogEmbedding.this.modeler.sizeEmbeddings(),
								textName.getText(), textOrbit.getOrbit(), textType.getText(), textComment.getText());
						result.setFileHeader(textHeader.getText());
					} else {
						ebdInfo.setName(textName.getText());
						ebdInfo.setOrbit(textOrbit.getOrbit());
						ebdInfo.setComment(textComment.getText());
						ebdInfo.setType(textType.getText());
						ebdInfo.setFileHeader(textHeader.getText());
						result = ebdInfo;
					}
					result.setComment(textComment.getText());
					setVisible(false);
				} else {

				}
			}
		});
		panelCmd.add(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				result = null;
				setVisible(false);
			}
		});
		panelCmd.add(btnCancel);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 45 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		panel.setLayout(gbl_panel);

		JLabel lblName = new JLabel("Name:");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		panel.add(lblName, gbc_lblName);

		textName = new JPatternTextField(parent.getPreferences(), JPatternTextField.PATTERN_IDENT, new ModifyListener() {
			@Override
			public void action() {
				if(ebdInfo != null)
					ebdInfo.setName(textName.getText());
				
			}
		});
		if (ebdInfo != null)
			textName.setText(ebdInfo.getName());
		GridBagConstraints gbc_textName = new GridBagConstraints();
		gbc_textName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textName.insets = new Insets(0, 0, 5, 5);
		gbc_textName.gridx = 1;
		gbc_textName.gridy = 0;
		panel.add(textName, gbc_textName);
		textName.setColumns(20);

		textName.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!hasValidName(ebdInfo, DialogEmbedding.this.textName.getText())) {
					DialogEmbedding.this.allreadyUsedName.setVisible(true);
				} else
					DialogEmbedding.this.allreadyUsedName.setVisible(false);
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		allreadyUsedName = new JLabel("Name allready used");
		GridBagConstraints gbc_textNameError = new GridBagConstraints();
		gbc_textNameError.fill = GridBagConstraints.HORIZONTAL;
		gbc_textNameError.insets = new Insets(0, 0, 5, 0);
		gbc_textNameError.gridx = 2;
		gbc_textNameError.gridy = 0;
		allreadyUsedName.setForeground(new Color(200, 0, 0));
		panel.add(allreadyUsedName, gbc_textNameError);
		allreadyUsedName.setVisible(false);

		JLabel lblOrbit = new JLabel("Orbit:");
		GridBagConstraints gbc_lblOrbit = new GridBagConstraints();
		gbc_lblOrbit.anchor = GridBagConstraints.EAST;
		gbc_lblOrbit.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrbit.gridx = 0;
		gbc_lblOrbit.gridy = 1;
		panel.add(lblOrbit, gbc_lblOrbit);

		textOrbit = new JOrbitComponent();
		if (ebdInfo != null)
			textOrbit.setText(ebdInfo.getOrbit());
		GridBagConstraints gbc_textOrbit = new GridBagConstraints();
		gbc_textOrbit.fill = GridBagConstraints.HORIZONTAL;
		gbc_textOrbit.insets = new Insets(0, 0, 5, 5);
		gbc_textOrbit.gridx = 1;
		gbc_textOrbit.gridy = 1;
		panel.add(textOrbit, gbc_textOrbit);
		textOrbit.setColumns(10);

		JLabel lblType = new JLabel("Type:");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.EAST;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 2;
		panel.add(lblType, gbc_lblType);

		textType = new JPatternTextField(parent.getPreferences(), JPatternTextField.PATTERN_MODULE, new ModifyListener() {
			@Override
			public void action() {
				if(ebdInfo != null)
					ebdInfo.setType(textType.getText());
				
			}
		});
		if (ebdInfo != null)
			textType.setText(ebdInfo.getType());
		GridBagConstraints gbc_textType = new GridBagConstraints();
		gbc_textType.fill = GridBagConstraints.HORIZONTAL;
		gbc_textType.insets = new Insets(0, 0, 5, 5);
		gbc_textType.gridx = 1;
		gbc_textType.gridy = 2;
		panel.add(textType, gbc_textType);
		textType.setColumns(10);

		bntSearchType = new JButton("...");
		bntSearchType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(DialogEmbedding.this, "Feature still not supported");
			}
		});
		GridBagConstraints gbc_bntSearchType = new GridBagConstraints();
		gbc_bntSearchType.insets = new Insets(0, 0, 5, 0);
		gbc_bntSearchType.gridx = 2;
		gbc_bntSearchType.gridy = 2;
		panel.add(bntSearchType, gbc_bntSearchType);

		/** File header **/

		JLabel lblHeader = new JLabel("Header File (C++ only) :");
		GridBagConstraints gbc_lblHeader = new GridBagConstraints();
		gbc_lblHeader.anchor = GridBagConstraints.EAST;
		gbc_lblHeader.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeader.gridx = 0;
		gbc_lblHeader.gridy = 3;
		panel.add(lblHeader, gbc_lblHeader);

		if (ebdInfo != null)
			textHeader.setText(ebdInfo.getFileHeader());
		GridBagConstraints gbc_textHeader = new GridBagConstraints();
		gbc_textHeader.fill = GridBagConstraints.HORIZONTAL;
		gbc_textHeader.insets = new Insets(0, 0, 5, 5);
		gbc_textHeader.gridx = 1;
		gbc_textHeader.gridy = 3;
		panel.add(textHeader, gbc_textHeader);
		textHeader.setColumns(10);

		bntSearchHeader = new JButton("...");
		bntSearchHeader.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser chooserSave = new JFileChooser(new File(DialogEmbedding.this.modeler.getDestDir()));
				chooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
				chooserSave.setAcceptAllFileFilterUsed(false);
				chooserSave.setDialogTitle("Embedding header file");
				final int returnVal = chooserSave.showSaveDialog(chooserSave);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					DialogEmbedding.this.textHeader.setText(DialogEmbedding.this.modeler.getRelativPathToDestDirForFile(
							chooserSave.getSelectedFile().getAbsolutePath().replace("\\", "/")));
					if (ebdInfo == null) {
						DialogEmbedding.this.result.setFileHeader(DialogEmbedding.this.textHeader.getText());
					} else {
						ebdInfo.setFileHeader(DialogEmbedding.this.textHeader.getText());
					}
				}
			}
		});
		GridBagConstraints gbc_bntSearchHeader = new GridBagConstraints();
		gbc_bntSearchHeader.insets = new Insets(0, 0, 5, 0);
		gbc_bntSearchHeader.gridx = 2;
		gbc_bntSearchHeader.gridy = 3;
		panel.add(bntSearchHeader, gbc_bntSearchHeader);
	
		
				JLabel lblComment = new JLabel("Comment:");
				lblComment.setHorizontalAlignment(SwingConstants.RIGHT);
				lblComment.setVerticalAlignment(SwingConstants.TOP);
				GridBagConstraints gbc_lblComment = new GridBagConstraints();
				gbc_lblComment.anchor = GridBagConstraints.EAST;
				gbc_lblComment.fill = GridBagConstraints.VERTICAL;
				gbc_lblComment.insets = new Insets(0, 0, 0, 5);
				gbc_lblComment.gridx = 0;
				gbc_lblComment.gridy = 5;
				panel.add(lblComment, gbc_lblComment);
		
				textComment = new CommentArea();
				lblComment.setLabelFor(textComment);
				GridBagConstraints gbc_textComment = new GridBagConstraints();
				gbc_textComment.fill = GridBagConstraints.BOTH;
				gbc_textComment.insets = new Insets(0, 0, 0, 5);
				gbc_textComment.gridx = 1;
				gbc_textComment.gridy = 5;
				panel.add(textComment, gbc_textComment);
		if (ebdInfo != null)
			textComment.setText(ebdInfo.getComment());
	}

	private boolean hasValidName(JMEEmbeddingInfo ebdToModify, String name) {
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if ((ebdToModify == null || !ebdToModify.equals(e)) && e.getName().compareTo(name) == 0)
				return false;
		}
		return true;
	}

	public UndoManager getManager() {
		return manager;
	}

	public static JMEEmbeddingInfo showDialog(JerboaModelerEditor editor, JMEEmbeddingInfo ebdInfo) {
		DialogEmbedding mod = new DialogEmbedding(editor, ebdInfo);
		mod.setVisible(true);
		return mod.result;
	}

}
