package fr.up.xlim.sic.ig.jerboa.jme.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModelerGenerationType;
import fr.up.xlim.sic.ig.jerboa.jme.view.errorstree.ErrorsPanel;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.CommentArea;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JPatternTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ModifyListener;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.DockablePanelAdapter;
import net.miginfocom.swing.MigLayout;

public class ModelerDetailsPanel extends DockablePanelAdapter implements JMEElementView {
	private static final long serialVersionUID = -2853392592157121334L;
	private JTextField textPackage;
	private JTextField textName;
	private JSpinner dimension;
	private ModelerExpressionHeaderPanel panelHeader;
	private JMEModeler modeler;
	private JTextField textSaveFile;
	private JTextField textDestDir;
//	private final ButtonGroup buttonGroup = new ButtonGroup();
//	private JRadioButton radioAllSupportedLanguage;
//	private JRadioButton radioCppGeneration;
//	private JRadioButton radioJavaGeneration;
	private JButton bntSelectDestDir;
	private JButton bntSelectSaveFile;
	private JerboaModelerEditor owner;
	private CommentArea commentArea;
	private JLabel lblProjectDirectory;
	private JTextField textProjectDir;
	private JButton bntProjectDir;
	private ErrorsPanel errorsPanel;

	private boolean startCheck = false;
	private JTabbedPane tabbedPane;
	private JComboBox languageSelector;

	public ModelerDetailsPanel(JerboaModelerEditor editor) {
		super("Modeler Advanced parameter");
		this.owner = editor;
		this.modeler = owner.getModeler();
		setLayout(new MigLayout("", "[left][297px,grow,fill]", "[11.00px,center][center][center][grow]"));

		if (modeler != null) {
			this.modeler.addView(this);
		}

		JLabel lblName = new JLabel("Name:");
		add(lblName, "cell 0 0,alignx trailing");

		textName = new JPatternTextField(editor.getPreferences(), JPatternTextField.PATTERN_IDENT,
				new ModifyListener() {

			@Override
			public void action() {
				ModelerDetailsPanel.this.modeler.setName(textName.getText());
				check();
			}
		});
		if (modeler != null)
			textName.setText(modeler.getName());

		lblName.setLabelFor(textName);
		textName.setColumns(30);
		add(textName, "cell 1 0,growx");

		JLabel lblPackage = new JLabel("Package:");
		add(lblPackage, "cell 0 1,alignx trailing");

		textPackage = new JPatternTextField(editor.getPreferences(), JPatternTextField.PATTERN_MODULE,
				new ModifyListener() {

			@Override
			public void action() {
				ModelerDetailsPanel.this.modeler.setModule(textPackage.getText());
				check();
			}
		});
		if (modeler != null)
			textPackage.setText(modeler.getModule());
		lblPackage.setLabelFor(textPackage);
		add(textPackage, "cell 1 1,growx");
		textPackage.setColumns(10);

		JLabel lblDimension = new JLabel("Dimension:");
		add(lblDimension, "cell 0 2");

		dimension = new JSpinner();
		dimension.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				modeler.setDimension(((Number) dimension.getValue()).intValue());
				check();
			}
		});
		lblDimension.setLabelFor(dimension);
		dimension.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		add(dimension, "cell 1 2");
		if (modeler != null)
			dimension.setValue(modeler.getDimension());

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, "cell 0 3 2 1,grow");

		commentArea = new CommentArea();
		tabbedPane.addTab("Comment", null, commentArea, null);

		commentArea.addModifyListener(new ModifyListener() {

			@Override
			public void action() {
				if (ModelerDetailsPanel.this.modeler != null) {
					ModelerDetailsPanel.this.modeler.setComment(commentArea.getText());
				}

			}
		});

		// JScrollPane scrollPane2 = new JScrollPane();

		panelHeader = new ModelerExpressionHeaderPanel(this);
		// scrollPane2.setViewportView(panelHeader);
		tabbedPane.addTab("Header", null, panelHeader, null);

		JPanel panelGeneration = new JPanel();
		tabbedPane.addTab("Generation/Misc", null, panelGeneration, null);
		panelGeneration.setLayout(new MigLayout("", "[][grow][]", "[][][][][][][]"));

		errorsPanel = new ErrorsPanel(modeler);
		tabbedPane.addTab("Errors", errorsPanel);

		JLabel lblSaveFile = new JLabel("Save file:");
		panelGeneration.add(lblSaveFile, "cell 0 0,alignx trailing");

		textSaveFile = new JTextField();
		panelGeneration.add(textSaveFile, "flowx,cell 1 0,growx");
		textSaveFile.setColumns(10);

		bntSelectSaveFile = new JButton("...");
		bntSelectSaveFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectFileSave();
			}
		});
		panelGeneration.add(bntSelectSaveFile, "cell 2 0");
		if (modeler != null) {
			textSaveFile.setText(modeler.getFileJME());
		}

		JLabel lblNewLabel = new JLabel("Destination directory:");
		panelGeneration.add(lblNewLabel, "cell 0 1,alignx trailing");

		textDestDir = new JTextField();
		panelGeneration.add(textDestDir, "cell 1 1,growx");
		textDestDir.setColumns(10);

		bntSelectDestDir = new JButton("...");
		bntSelectDestDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectDestDir();
			}
		});
		panelGeneration.add(bntSelectDestDir, "cell 2 1");
		if (modeler != null) {
			textDestDir.setText(modeler.getDestDir());
		}

		lblProjectDirectory = new JLabel("Project directory:");
		panelGeneration.add(lblProjectDirectory, "cell 0 2,alignx trailing");

		textProjectDir = new JTextField();
		panelGeneration.add(textProjectDir, "cell 1 2,growx");
		textProjectDir.setColumns(10);

		bntProjectDir = new JButton("...");
		bntProjectDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectProjectDir();
			}
		});
		panelGeneration.add(bntProjectDir, "cell 2 2");

		JLabel lblLanguageOutput = new JLabel("Language output:");
		panelGeneration.add(lblLanguageOutput, "cell 0 3,alignx trailing");

//		radioJavaGeneration = new JRadioButton("Java generation");
//		radioJavaGeneration.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				ModelerDetailsPanel.this.modeler.setGenerationType(JMEModelerGenerationType.JAVA);
//			}
//		});
//		buttonGroup.add(radioJavaGeneration);
//		panelGeneration.add(radioJavaGeneration, "cell 1 3");
//
//		radioCppGeneration = new JRadioButton("C++ generation");
//		radioCppGeneration.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				ModelerDetailsPanel.this.modeler.setGenerationType(JMEModelerGenerationType.CPP);
//			}
//		});
//
//		buttonGroup.add(radioCppGeneration);
//		panelGeneration.add(radioCppGeneration, "cell 1 4");

//		radioAllSupportedLanguage = new JRadioButton("All supported language");
//		radioAllSupportedLanguage.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				ModelerDetailsPanel.this.modeler.setGenerationType(JMEModelerGenerationType.ALL);
//			}
//		});
//		buttonGroup.add(radioAllSupportedLanguage);
//		panelGeneration.add(radioAllSupportedLanguage, "cell 1 5");
		
		//////// Val : J'ajoute ça comme ça, au cas ou on voudrais faire d'autres langage
		languageSelector = new JComboBox(JMEModelerGenerationType.values());
		panelGeneration.add(languageSelector, "cell 1 3");
		//////// Val : J'ajoute ça comme ça, au cas ou on voudrais faire d'autres langage
		if (modeler != null) {
			languageSelector.setSelectedItem(modeler.getGenerationType());
//			if (modeler.getGenerationType() == JMEModelerGenerationType.CPP)
//				radioCppGeneration.setSelected(true);
//			else if (modeler.getGenerationType() == JMEModelerGenerationType.JAVA)
//				radioJavaGeneration.setSelected(true);
		}

		JPanel panel = new JPanel();
		panelGeneration.add(panel, "cell 0 6 3 1,grow");

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		panel.add(btnApply);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		panel.add(btnRefresh);

		if (modeler != null) {
			reload();
			startCheck = true;
		}
	}

	protected void selectFileSave() {
		JFileChooser fileChooser = new JFileChooser(modeler.getDestDir());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle("Select JME file");
		fileChooser.setAutoscrolls(true);
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		FileFilter jme = new FileNameExtensionFilter("Jerboa Modeler Editor (*.jme)", "jme");
		fileChooser.setFileFilter(jme);
		int res = fileChooser.showSaveDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			String filexml = fileChooser.getSelectedFile().getAbsolutePath();
			if (!filexml.endsWith(".jme"))
				filexml += ".jme";
			modeler.setFileJME(filexml);
			textSaveFile.setText(modeler.getFileJME());
		}
	}

	protected void selectDestDir() {
		JFileChooser dirChooser = new JFileChooser(modeler.getDestDir());
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirChooser.setDialogTitle("Select directory destination");
		dirChooser.setApproveButtonText("Select");
		dirChooser.setAutoscrolls(true);
		dirChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		int res = dirChooser.showSaveDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			String dirchooser = dirChooser.getSelectedFile().getAbsolutePath();
			modeler.setDestDir(dirchooser);
			textDestDir.setText(dirchooser);
		}
	}

	protected void selectProjectDir() {
		JFileChooser dirChooser = new JFileChooser(modeler.getDestDir());
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirChooser.setDialogTitle("Select Project directory");
		dirChooser.setApproveButtonText("Select");
		dirChooser.setAutoscrolls(true);
		dirChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		int res = dirChooser.showSaveDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			String dirchooser = dirChooser.getSelectedFile().getAbsolutePath();
			modeler.setProjectDir(dirchooser);
			textProjectDir.setText(dirchooser);
		}
	}

	@Override
	public void reload() {
		textName.setText(modeler.getName());
		textPackage.setText(modeler.getModule());
		dimension.setValue(modeler.getDimension());

		commentArea.setText(modeler.getComment());

		panelHeader.reload();

		textDestDir.setText(modeler.getDestDir());
		textSaveFile.setText(modeler.getFileJME());
		textProjectDir.setText(modeler.getProjectDir());

//		radioAllSupportedLanguage.setSelected(modeler.getGenerationType() == JMEModelerGenerationType.ALL);
//		radioJavaGeneration.setSelected(modeler.getGenerationType() == JMEModelerGenerationType.JAVA);
//		radioCppGeneration.setSelected(modeler.getGenerationType() == JMEModelerGenerationType.CPP);
		languageSelector.setSelectedItem(modeler.getGenerationType());

		reloadTitle();
	}

	public void save() {
		modeler.setName(textName.getText());
		modeler.setModule(textPackage.getText());
		modeler.setDimension(((Number) dimension.getValue()).intValue());
		modeler.setComment(commentArea.getText());

		modeler.setDestDir(textDestDir.getText());
		modeler.setFileJME(textSaveFile.getText());
		modeler.setProjectDir(textProjectDir.getText());
//
//		final boolean isall = radioAllSupportedLanguage.isSelected();
//		final boolean isjava = radioJavaGeneration.isSelected();
//		final boolean iscpp = radioCppGeneration.isSelected();
//
//		if (isall)
//			modeler.setGenerationType(JMEModelerGenerationType.ALL);
//		else if (isjava)
//			modeler.setGenerationType(JMEModelerGenerationType.JAVA);
//		else if (iscpp)
//			modeler.setGenerationType(JMEModelerGenerationType.CPP);
		
		modeler.setGenerationType((JMEModelerGenerationType) languageSelector.getSelectedItem());

		panelHeader.save();
		check();
	}

	@Override
	public void unlink() {
		modeler.removeView(this);
		panelHeader.unlink();
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	@Override
	public void check() {
		if(startCheck) {
			owner.check();
			// if(modeler.getAllErrors().size() > 0) {
			// tabbedPane.setSelectedComponent(errorsPanel);
			// errorsPanel.requestFocusInWindow();
			// }
			// Val: j'enleve cette ligne c'est trop Ã©nervant que Ã§a s'ouvre sans prÃ©venir
			reloadTitle();
		}
	}

	@Override
	public void reloadTitle() {
		super.reloadTitle();
		if(getWindowContainer() != null) {
			getWindowContainer().setTitle("Modeler Advanced parameter"  + (modeler.isModified()? "*" : ""));
		}
	}

	public void openErrorPanel() {
		tabbedPane.setSelectedComponent(errorsPanel);
	}
}
