package fr.up.xlim.sic.ig.jerboa.jme.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.TreePath;

import fr.up.xlim.sic.ig.jerboa.jme.export.CPPExport_New_Engine;
import fr.up.xlim.sic.ig.jerboa.jme.export.CPPExport_V2;
import fr.up.xlim.sic.ig.jerboa.jme.export.JavaExport;
import fr.up.xlim.sic.ig.jerboa.jme.export.JavaExport2;
import fr.up.xlim.sic.ig.jerboa.jme.export.JavaExport_V2;
import fr.up.xlim.sic.ig.jerboa.jme.forms.dialog.DialogImportRule;
import fr.up.xlim.sic.ig.jerboa.jme.forms.dialog.DialogModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModelerGenerationType;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.model.serialize.JMELoadModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.serialize.JMESaveModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMENodeShape;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.util.FrameJMenuItem;
import fr.up.xlim.sic.ig.jerboa.jme.util.JMEDrawable;
import fr.up.xlim.sic.ig.jerboa.jme.util.OpenedDockItem;
import fr.up.xlim.sic.ig.jerboa.jme.util.RuleGraphViewGrid;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.view.DetailsEmbedding;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementWindowableView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ModelerDetailsPanel;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruletree.RuleTreeNodeLeaf;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruletree.RuleTreeViewerModel;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruletree.RuleTreeViewerRenderer;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleAtomicView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RulePreview;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleScriptView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ClosedListener;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ConsolePanel;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JClearableTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JMEEbdListView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JPatternTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ModifyListener;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.DockablePanel;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.WindowContainerInterface;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.WindowContainerInternalFrame;
import up.jerboa.core.JerboaOrbit;
import up.jerboa.core.util.Pair;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;

public class JerboaModelerEditor extends JPanel implements JMEElementWindowableView {

	private static final long serialVersionUID = 7955567233184653562L;

	private final static Icon atomic = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/atomic_16x16_ps.png")).getImage()
			.getScaledInstance(17, 17, Image.SCALE_SMOOTH));
	private final static Icon script = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/script_16x16_ps.png")).getImage()
			.getScaledInstance(17, 17, Image.SCALE_SMOOTH));
	private JPanel status;
	protected JMEModeler modeler;
	private JMEPreferences preferences;

	private JDesktopPane desktopPane;
	private JTextField textModName;
	private JTextField textModPack;
	private JSpinner spinDim;

	private JTextField userNameLabel;

	private JMenuItem mnUndo;
	private JMenuItem mnRedo;
	private JMenuItem mnCopier;
	private JMenuItem mnCouper;
	private JMenuItem mnColler;
	private JMenuItem mntmNewModeler;
	private JMenuItem mntmNewEbd;
	private JMenuItem mntmNewRule;
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;
	private JMenuItem mntmLoad;
	private JMenuItem mntmQuit;
	private JMenu mnFile;
	private JMEEbdListView listEbds;
	private JButton bNewEbd;
	private JButton btnNewRule;
	private JLabel labelNbRules;

	private HashMap<JMERule, RuleView> frames;
	private HashMap<JMEEmbeddingInfo, DockablePanel> framesEbds;
	private JMenuItem mntmOpenRule;
	private JMenuItem mntmDeleteRule;
	private JMenu mnOpenedWindows;
	private JTree treeRules;

	private JerboaModelerEditorParent owner;
	private RuleTreeViewerModel modelRuleTreeView;
	private JClearableTextField filterRules;
	private JMenu mnLooknFeel;
	private String lookNFeelId;

	private ConsolePanel consolepanel;
	private JCheckBoxMenuItem chboxConsole;
	private ModelerDetailsPanel modelerAdvPanel;
	private JScrollPane scrollRules;
	private UndoManager manager;

	private List<JMEDrawable> copyCache;
	private JMenuItem mntmArrange;
	private JMenuItem mntmAdvModelerParam;
	private JButton bntEditAdv;

	private JMenuItem mntmNewScriptRule_1;
	private JMenuItem mnCloseAllWindows;
	private JMenuItem mnCloseCurrentWindows;
	private JToolBar toolBar;
	private JMenuItem mntmSettings;

	private JMenuItem mntmImportRule;
	private JToolBar listOpenRules;
	private JLabel lblEmbeddings;
	
	public JMenuBar menuBar;

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	public JerboaModelerEditor(final JerboaModelerEditorParent parent, final JMEModeler m_modeler) {
		super(true);
		this.owner = parent;
		this.modeler = m_modeler;
		framesEbds = new HashMap<>();

		// Loading preference file
		preferences = parent.getPreferences();


		frames = new HashMap<>();
		setLayout(new BorderLayout(0, 0));
		setPreferredSize(new Dimension(800, 600));

		menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		final Icon newModelerIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/modeler_adding.png")).getImage());
		mntmNewModeler = new JMenuItem("New Modeler");
		mntmNewModeler.setIcon(newModelerIcon);
		mntmNewModeler.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMEModeler newmod = DialogModeler.showDialog(JerboaModelerEditor.this);
				if (newmod != null) {
					JerboaModelerEditor newjme = new JerboaModelerEditor(parent, newmod);
					parent.newWindow(newjme);
				}
			}
		});
		mnFile.add(mntmNewModeler);

		mntmImportRule = new JMenuItem("Import rule");
		mntmImportRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogImportRule.showDialog(JerboaModelerEditor.this, modeler);
			}
		});
		mnFile.add(mntmImportRule);

		final Icon newEbdIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/embedding_adding.png")).getImage());
		mntmNewEbd = new JMenuItem("New Embedding");
		mntmNewEbd.setIcon(newEbdIcon);
		mntmNewEbd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewEmbeddingInfo();
			}
		});

//		mntmNewEbd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewEbd);

		final Icon newAtomicIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/atomic_adding_ic.png")).getImage()
				.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		mntmNewRule = new JMenuItem("New Atomic Rule");
		mntmNewRule.setIcon(newAtomicIcon);
		mntmNewRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewAtomicRule();
			}
		});
		mntmNewRule.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewRule);

		final Icon newScriptIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/script_adding_ic.png")).getImage()
				.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		mntmNewScriptRule_1 = new JMenuItem("New Script Rule");
		mntmNewScriptRule_1.setIcon(newScriptIcon);
		mntmNewScriptRule_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewScriptRule();
			}
		});
		mnFile.add(mntmNewScriptRule_1);
		mntmNewScriptRule_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		mntmSave = new JMenuItem("Save");
		mntmSave.setIcon(new ImageIcon(JerboaModelerEditor.class.getResource("/image/saveIcon.png")));
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveModeler(true);
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);

		mntmSaveAs = new JMenuItem("Save as...");
		mntmSaveAs.setIcon(new ImageIcon(JerboaModelerEditor.class.getResource("/image/saveAsIcon.png")));
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.SHIFT_DOWN_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveModeler(false);
			}
		});
		mnFile.add(mntmSaveAs);

		mntmLoad = new JMenuItem("Load");
		mntmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmLoad);
		mntmLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadModeler();
			}
		});

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmExport = new JMenuItem("Export Modeler");
		mntmExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmExport);
		mntmExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (JerboaModelerEditor.this.modeler.getGenerationType()) {
				case CPP:
					CPPExport_New_Engine.exportModeler(JerboaModelerEditor.this.modeler);
					break;
				case CPP_V2:
					CPPExport_V2.exportModeler(JerboaModelerEditor.this.modeler);
					break;
				case JAVA:
					JavaExport.exportModeler(JerboaModelerEditor.this.modeler);
					break;
				case JAVA_V2:
					JavaExport_V2.exportModeler(JerboaModelerEditor.this.modeler);
					break;

				default:
					System.err.println("Export : error, export type not supported '" + JerboaModelerEditor.this.modeler.getGenerationType() + "'");
					break;
				}
			}
		});


		JMenuItem mntmExportJava = new JMenuItem("Export (force Java)");
		mnFile.add(mntmExportJava);
		mntmExportJava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				JavaExport.exportModeler(JerboaModelerEditor.this.modeler);
				JavaExport_V2.exportModeler(JerboaModelerEditor.this.modeler);
			}
		});

		JMenuItem mntmExportCPP = new JMenuItem("Export (force C++)");
		mnFile.add(mntmExportCPP);
		mntmExportCPP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO : attention a l'export utilisé ici
//				CPPExport_New_Engine.exportModeler(JerboaModelerEditor.this.modeler);
				CPPExport_V2.exportModeler(JerboaModelerEditor.this.modeler);
			}
		});
		
		JMenuItem mntmExportJavaRebuilt = new JMenuItem("Export ModelerRebuilt version");
		mnFile.add(mntmExportJavaRebuilt);
		mntmExportJavaRebuilt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				JavaExport.exportModeler(JerboaModelerEditor.this.modeler);
				JavaExport2.isRebuildFramework = true;
				try {
					JavaExport2.exportModeler(JerboaModelerEditor.this.modeler);
				}
				finally {
					JavaExport2.isRebuildFramework = false;
				}
			}
		});

		JSeparator separator_5 = new JSeparator();
		mnFile.add(separator_5);

		mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quitNow();
			}
		});
		mntmQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmQuit);

		JMenu mnEdition = new JMenu("Edition");
		menuBar.add(mnEdition);

		mnUndo = new JMenuItem("Undo");
		mnUndo.setEnabled(false);
		mnUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		mnEdition.add(mnUndo);

		mnRedo = new JMenuItem("Redo");
		mnEdition.add(mnRedo);

		JSeparator separator_2 = new JSeparator();
		mnEdition.add(separator_2);

		final Icon cutIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/cisors.png")).getImage());
		mnCouper = new JMenuItem("Cut");
		mnCouper.setIcon(cutIcon);
		mnCouper.setEnabled(false);
		mnCouper.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnEdition.add(mnCouper);

		final Icon copyIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/copyIcon.png")).getImage());
		mnCopier = new JMenuItem("Copy");
		mnCopier.setIcon(copyIcon);
		mnCopier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnEdition.add(mnCopier);

		mnColler = new JMenuItem("Paste");
		mnColler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnEdition.add(mnColler);

		JSeparator separator_3 = new JSeparator();
		mnEdition.add(separator_3);

		JMenuItem mntmForceApply = new JMenuItem("Force apply");
		mntmForceApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		mnEdition.add(mntmForceApply);

		JMenu mnWindow = new JMenu("Window");
		menuBar.add(mnWindow);

		final Icon consoleIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/console_icon.png")).getImage());
		chboxConsole = new JCheckBoxMenuItem("Console");
		chboxConsole.setIcon(consoleIcon);
		chboxConsole.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		chboxConsole.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (chboxConsole.getState()) {
					if (consolepanel.getWindowContainer() == null) {
						WindowContainerInterface consoleWindow = new WindowContainerInternalFrame(
								JerboaModelerEditor.this, consolepanel);
						consoleWindow.activate();
					} else {
						consolepanel.getWindowContainer().activate();
					}
				} else {
					if (consolepanel.getWindowContainer() != null) {
						consolepanel.getWindowContainer().close();
						// consoleWindow = null; // ATTENTION APRES UN CLOSE IL
						// FAUT RECREER UNE CLASSE
					}
				}
			}
		});

		mnWindow.add(chboxConsole);

		mntmAdvModelerParam = new JMenuItem("Adv. modeler Param");
		mntmAdvModelerParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bntEditAdv.doClick();
			}
		});
		mntmAdvModelerParam.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		mnWindow.add(mntmAdvModelerParam);
		mnWindow.addSeparator();

		mntmArrange = new JMenuItem("Arrange");
		mntmArrange.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmArrange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				arrangeWindows();
			}
		});
		mnWindow.add(mntmArrange);

		JSeparator separator_4 = new JSeparator();
		mnWindow.add(separator_4);

		mnOpenedWindows = new JMenu("Opened windows:");
		mnWindow.add(mnOpenedWindows);

		mnCloseCurrentWindows = new JMenuItem("Close current window");
		mnWindow.add(mnCloseCurrentWindows);
		mnCloseCurrentWindows.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		mnCloseCurrentWindows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeCurrentWindow();
			}
		});

		mnCloseAllWindows = new JMenuItem("Close all windows");
		mnWindow.add(mnCloseAllWindows);
		mnCloseAllWindows.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				KeyEvent.SHIFT_DOWN_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mnCloseAllWindows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeAllWindow();
			}
		});

		mnWindow.add(new JSeparator(SwingConstants.HORIZONTAL));

		final ButtonGroup groupLookAndFeel = new ButtonGroup();
		mnLooknFeel = new JMenu("Look and Feel");
		mnWindow.add(mnLooknFeel);
		for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			final JRadioButtonMenuItem item = new JRadioButtonMenuItem(info.getName());
			if ("Windows".equals(info.getName())) {
				lookNFeelId = "Windows";
				updateLookAndFeel(lookNFeelId);
			} else if ("Nimbus".equals(info.getName())) {
				lookNFeelId = "Nimbus";
				updateLookAndFeel(lookNFeelId);
			}
			groupLookAndFeel.add(item);
			item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					updateLookAndFeel(e.getActionCommand());
					lookNFeelId = e.getActionCommand();
				}
			});
			mnLooknFeel.add(item);
		}

		JScrollPane scrollPane = new JScrollPane();
		// panelRight.add(scrollPane, BorderLayout.CENTER);

		desktopPane = new JDesktopPane();
		desktopPane.setAutoscrolls(true);
		desktopPane.setBackground(SystemColor.control);
		scrollPane.setViewportView(desktopPane);

		status = new JPanel();
		status.setPreferredSize(new Dimension(10, 20));
		add(status, BorderLayout.SOUTH);
		
		JPanel mainpanel = new JPanel(new BorderLayout(), true);
		add(mainpanel, BorderLayout.CENTER);
		
		listOpenRules = new JToolBar("Opened Rules:");
		this.listOpenRules.setRollover(true);
		mainpanel.add(listOpenRules, BorderLayout.NORTH);
		
		JSplitPane splitDesktop = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitDesktop.setOneTouchExpandable(true);
		splitDesktop.setContinuousLayout(true);
		mainpanel.add(splitDesktop, BorderLayout.CENTER);

		JPanel sidePanel = new JPanel();
		splitDesktop.setLeftComponent(sidePanel);
		splitDesktop.setRightComponent(scrollPane);
		sidePanel.setPreferredSize(new Dimension(150, 100));
		sidePanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		sidePanel.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panelModName = new JPanel();
		panel.add(panelModName, BorderLayout.NORTH);
		panelModName.setLayout(new BorderLayout(0, 0));

		Box verticalBox = Box.createVerticalBox();
		panelModName.add(verticalBox);


		Box plModUserName = Box.createHorizontalBox();
		verticalBox.add(plModUserName);
		Properties p = System.getProperties();
		userNameLabel = new JTextField(p.getProperty("user.name"));
		userNameLabel.setEnabled(false);
		plModUserName.add(new JLabel("User name"));
		plModUserName.add(userNameLabel);

		Box plmodname = Box.createHorizontalBox();
		verticalBox.add(plmodname);

		JLabel lblModelersName = new JLabel("Modeler's Name:");
		plmodname.add(lblModelersName);

		Component horizontalGlue_2 = Box.createHorizontalGlue();
		plmodname.add(horizontalGlue_2);

		// textModName = new JTextField();
		textModName = new JPatternTextField(preferences, JPatternTextField.PATTERN_IDENT, new ModifyListener() {
			@Override
			public void action() {
				modeler.setName(textModName.getText());
			}
		});
		verticalBox.add(textModName);
		textModName.setColumns(10);


		Box plModPack = Box.createHorizontalBox();
		verticalBox.add(plModPack);

		JLabel lblModelersPackage = new JLabel("Modeler's package:");
		plModPack.add(lblModelersPackage);

		Component horizontalGlue_3 = Box.createHorizontalGlue();
		plModPack.add(horizontalGlue_3);

		textModPack = new JPatternTextField(preferences, JPatternTextField.PATTERN_MODULE, new ModifyListener() {

			@Override
			public void action() {
				modeler.setModule(textModPack.getText());
			}
		});
		verticalBox.add(textModPack);
		textModPack.setColumns(10);

		Box dimTitle = Box.createHorizontalBox();
		verticalBox.add(dimTitle);

		JLabel lblDimension = new JLabel("Dimension:");
		dimTitle.add(lblDimension);

		spinDim = new JSpinner();
		spinDim.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				modeler.setDimension(((Number) spinDim.getValue()).intValue());
				checkAll();
			}
		});
		spinDim.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		dimTitle.add(spinDim);

		modelerAdvPanel = new ModelerDetailsPanel(this);

		Box dimAdv = Box.createHorizontalBox();
		bntEditAdv = new JButton("Adv. param.");
		bntEditAdv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (modelerAdvPanel.getWindowContainer() == null) {
					WindowContainerInterface modelerAdvPanelWindow = new WindowContainerInternalFrame(
							JerboaModelerEditor.this, modelerAdvPanel);
					modelerAdvPanelWindow.activate();
				} else {
					modelerAdvPanel.getWindowContainer().activate();
				}
			}
		});
		dimAdv.add(bntEditAdv);
		verticalBox.add(dimAdv);

		JButton btnCheckAll = new JButton("Check all");
		btnCheckAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkAll();
			}
		});
		dimAdv.add(btnCheckAll);

		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		verticalBox.add(sep);

		Box ebdTitle = Box.createHorizontalBox();
		verticalBox.add(ebdTitle);

		lblEmbeddings = new JLabel("Embeddings:");
		ebdTitle.add(lblEmbeddings);
		JCheckBox checkPrintAllEbds = new JCheckBox(""); // un msg ?
		checkPrintAllEbds.setSelected(true);
		ebdTitle.add(checkPrintAllEbds);
		checkPrintAllEbds.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JMEEmbeddingInfo ei : modeler.getEmbeddings()) {
					ei.setIsVisible(checkPrintAllEbds.isSelected());
				}
				repaint();
			}
		});

		Component horizontalGlue = Box.createHorizontalGlue();
		ebdTitle.add(horizontalGlue);

		bNewEbd = new JButton(newEbdIcon);
		bNewEbd.setContentAreaFilled(false);
		bNewEbd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (modeler != null) { // HAK useless mais osef
					createNewEmbeddingInfo();
				}
			}
		});
		ebdTitle.add(bNewEbd);

		JScrollPane ebdList = new JScrollPane();
		verticalBox.add(ebdList);

		JPanel panel_1 = new JPanel();
		ebdList.setViewportView(panel_1);
		panel_1.setPreferredSize(new Dimension(10, 150));
		panel_1.setLayout(new BorderLayout(0, 0));

		listEbds = new JMEEbdListView(this); // JList<JMEEmbeddingInfo>(new
		// DefaultListModel<JMEEmbeddingInfo>());
		listEbds.setBackground(Color.WHITE);
		// listEbds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel_1.add(listEbds);

		JPanel panelRootRules = new JPanel();
		sidePanel.add(panelRootRules, BorderLayout.CENTER);
		panelRootRules.setLayout(new BorderLayout(0, 0));

		Box horizontalBox_1 = Box.createHorizontalBox();
		panelRootRules.add(horizontalBox_1, BorderLayout.NORTH);

		Box boxFilterRules = Box.createHorizontalBox();
		boxFilterRules.add(new JLabel("Filter:"));

		filterRules = new JClearableTextField(10);
		filterRules.addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				modelRuleTreeView.filter(filterRules.getText());
			}			
			@Override
			public void insertUpdate(DocumentEvent e) {
				modelRuleTreeView.filter(filterRules.getText());
			}			
			@Override
			public void changedUpdate(DocumentEvent e) {
				modelRuleTreeView.filter(filterRules.getText());
			}
		});
		filterRules.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				modelRuleTreeView.filter(filterRules.getText());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				modelRuleTreeView.filter(filterRules.getText());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				modelRuleTreeView.filter(filterRules.getText());
			}
		});
		/*filterRules.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				modelRuleTreeView.filter(filterRules.getText());

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				modelRuleTreeView.filter(filterRules.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				modelRuleTreeView.filter(filterRules.getText());

			}
		});*/
		
		boxFilterRules.add(filterRules);
		panelRootRules.add(boxFilterRules, BorderLayout.SOUTH);

		JLabel lblRules = new JLabel("Rules:");
		horizontalBox_1.add(lblRules);

		labelNbRules = new JLabel("0");
		horizontalBox_1.add(labelNbRules);

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue_1);

		final Icon addAtomicIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/atomic_adding_ic.png")).getImage()
				.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		btnNewRule = new JButton(addAtomicIcon);
		btnNewRule.setBorder(null);
		btnNewRule.setContentAreaFilled(false);
		horizontalBox_1.add(Box.createHorizontalStrut(10));
		horizontalBox_1.add(btnNewRule);
		horizontalBox_1.add(Box.createHorizontalStrut(10));
		btnNewRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewAtomicRule();
			}
		});
		final Icon addScriptIcon = new ImageIcon(
				new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/script_adding_ic.png")).getImage()
				.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		JButton btnNewScript = new JButton(addScriptIcon);
		btnNewScript.setBorder(null);
		btnNewScript.setContentAreaFilled(false);
		horizontalBox_1.add(btnNewScript);
		btnNewScript.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewScriptRule();
			}
		});

		toolBar = new JToolBar();
		toolBar.setToolTipText("Operation on rules");
		horizontalBox_1.add(toolBar);
		toolBar.setFloatable(false);
		JButton btnExpandAll = new JButton("v");
		toolBar.add(btnExpandAll);
		JButton btnRetractAll = new JButton("^");
		toolBar.add(btnRetractAll);
		btnRetractAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JerboaModelerEditor.this.modelRuleTreeView.retractAllNodes();
			}
		});
		btnExpandAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JerboaModelerEditor.this.modelRuleTreeView.expandAllNodes();
			}
		});

		consolepanel = new ConsolePanel();
		System.setOut(consolepanel.getOut());
		System.setErr(consolepanel.getErr());

		consolepanel.addClosedListener(new ClosedListener() {
			@Override
			public void closeActionPerformed(DockablePanel window) {
				chboxConsole.setSelected(false);
			}
		});

		////////////// LA PARTIE A RELAISSE ICI
		mntmOpenRule = new JMenuItem("Open");
		mntmOpenRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath path = treeRules.getSelectionPath();
				if (path != null) {
					RuleTreeNodeLeaf rule = JerboaModelerEditor.this.modelRuleTreeView.getRuleFromPath(path);
					if (rule != null)
						openRule(rule.getRule());
				}
			}
		});


		manager = JerboaModelerEditor.this.modeler.getUndoManager();
		mnRedo.setEnabled(false);
		mnRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JerboaModelerEditor.this.manager.redo();

			}
		});
		mnRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));

		JMenuItem mntmForceUpdate = new JMenuItem("Force update");
		mntmForceUpdate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mntmForceUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeler.update();
			}
		});
		mnEdition.add(mntmForceUpdate);

		JSeparator separator__3 = new JSeparator();
		mnEdition.add(separator__3);

		mntmSettings = new JMenuItem("Settings");
		mntmSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
		mntmSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getPreferences().display(JerboaModelerEditor.this, "Settings");
			}
		});
		mnEdition.add(mntmSettings);
		modelRuleTreeView = new RuleTreeViewerModel(this.modeler);
		treeRules = new JTree(modelRuleTreeView);
		treeRules.setRootVisible(false);
		treeRules.setCellRenderer(new RuleTreeViewerRenderer());

		modelRuleTreeView.setJTree(treeRules);

		scrollRules = new JScrollPane(treeRules);
		panelRootRules.add(scrollRules, BorderLayout.CENTER);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(treeRules, popupMenu, this);

		mntmOpenRule = new JMenuItem("Open");
		mntmOpenRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath path = treeRules.getSelectionPath();
				if (path != null) {
					RuleTreeNodeLeaf rule = modelRuleTreeView.getRuleFromPath(path);
					if (rule != null)
						openRule(rule.getRule());
				}
			}
		});

		JMenuItem mntmNewAtomicRule = new JMenuItem("New Atomic rule");
		mntmNewAtomicRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewAtomicRule();
			}
		});
		popupMenu.add(mntmNewAtomicRule);

		JMenuItem mntmNewScriptRule = new JMenuItem("New Script rule");
		mntmNewScriptRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewScriptRule();
			}
		});
		popupMenu.add(mntmNewScriptRule);

		JSeparator separator_7 = new JSeparator();
		popupMenu.add(separator_7);

		popupMenu.add(mntmOpenRule);
		JMenuItem mntmDuplicateRule = new JMenuItem("Duplicate");
		mntmDuplicateRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath path = treeRules.getSelectionPath();
				if (path != null) {
					RuleTreeNodeLeaf ruleLeaf = modelRuleTreeView.getRuleFromPath(path);
					JMERule rule = (ruleLeaf.getRule());
					int i = 2;
					while(modeler.searchRule(rule.getName()+i) != null)
						i++;
					modeler.copyRule(rule, rule.getName()+i);
				}
			}
		});
		popupMenu.add(mntmDuplicateRule);

		mntmDeleteRule = new JMenuItem("Delete");
		mntmDeleteRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath path = treeRules.getSelectionPath();
				if (path != null) {
					RuleTreeNodeLeaf rule = modelRuleTreeView.getRuleFromPath(path);
					closeRule(rule.getRule());
					modeler.removeRule(rule.getRule());
				}
			}
		});
		popupMenu.add(mntmDeleteRule);

		final JCheckBoxMenuItem chboxFlatView = new JCheckBoxMenuItem("Flat view");
		chboxFlatView.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				modelRuleTreeView.setFlatView(chboxFlatView.isSelected());
			}
		});

		JSeparator separator_6 = new JSeparator();
		popupMenu.add(separator_6);
		chboxFlatView.setSelected(true);
		popupMenu.add(chboxFlatView);

		JMenuItem mntmExpandAll = new JMenuItem("Expand all");
		mntmExpandAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JerboaModelerEditor.this.modelRuleTreeView.expandAllNodes();
			}
		});
		popupMenu.add(mntmExpandAll);

		JMenuItem mntmRetractAll = new JMenuItem("Retract all");
		mntmRetractAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JerboaModelerEditor.this.modelRuleTreeView.retractAllNodes();
			}
		});
		popupMenu.add(mntmRetractAll);

		// derniere partie a faire en dernier
		copyCache = new ArrayList<>();
		if (modeler instanceof JMEModeler && modeler != null) {
			this.modeler.addView(this);
			reload();
		}
	}

	protected void closeAllWindow() {
		HashMap<JMERule, RuleView> frameCopy = new HashMap<>(frames);
		for (JMERule rv : frameCopy.keySet()) {
			frames.get(rv).close();
			frames.remove(rv);
		}
	}

	protected void closeCurrentWindow() {
		HashMap<JMERule, RuleView> frameCopy = new HashMap<>(frames);
		for (JMERule rv : frameCopy.keySet()) {
			if (frames.get(rv).hasFocus()) { // TODO: ça, ça marche pas, il
				// faut
				// trouver une autre solution
				frames.get(rv).close();
				frames.remove(rv);
			}
		}
	}

	public void arrangeWindows() {
		JInternalFrame[] allframes = desktopPane.getAllFrames();
		int count = allframes.length;
		if (count == 0)
			return;

		// Determine the necessary grid size
		int sqrt = (int) Math.sqrt(count);
		int rows = sqrt;
		int cols = sqrt;
		if (rows * cols < count) {
			cols++;
			if (rows * cols < count) {
				rows++;
			}
		}
		/*
		 * for (JInternalFrame i : allframes) {
		 * System.out.println("\t"+i.getTitle()); }
		 */

		// Define some initial values for size & location.
		Dimension size = desktopPane.getSize();

		int w = size.width / cols;
		int h = size.height / rows;
		int x = 0;
		int y = 0;

		// Iterate over the frames, deiconifying any iconified frames and then
		// relocating & resizing each.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols && ((i * cols) + j < count); j++) {
				JInternalFrame f = allframes[(i * cols) + j];

				if (!f.isClosed() && f.isIcon()) {
					try {
						f.setIcon(false);
					} catch (PropertyVetoException ignored) {
					}
				}

				if (!f.isClosed() && f.isMaximum()) {
					try {
						f.setMaximum(false);
					} catch (PropertyVetoException e1) {
					}
				}

				desktopPane.getDesktopManager().resizeFrame(f, x, y, w, h);
				x += w;
			}
			y += h; // start the next row
			x = 0;
		}
	}

	public void copy(List<JMEDrawable> views) {
		copyCache.clear();
		for (JMEDrawable ev : views)
			copyCache.add(ev);
		System.err.println(copyCache);
	}

	public List<JMEDrawable> getCopyCache() {
		List<JMEDrawable> listCopy = new ArrayList<>();
		for (JMEDrawable ev : copyCache) {
			listCopy.add(ev);
		}
		return listCopy;
	}
	
	public static JMEModeler loadModeler(File fPath) {
		try {
			Pair<JMEModeler, ArrayList<JMERule>> modelerLoaded = JMELoadModeler.loadModeler(fPath);
			return modelerLoaded.l();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Loading error!",
					JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public void loadModeler() {
		final JFileChooser chooserOpen = new JFileChooser();
		chooserOpen.setDialogType(JFileChooser.OPEN_DIALOG);
		chooserOpen.setAcceptAllFileFilterUsed(false);
		chooserOpen.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(final File f) {
				if (f.isDirectory())
					return true;
				final String nomFichier = f.getName().toLowerCase();

				return nomFichier.endsWith(".jme");
			}

			@Override
			public String getDescription() {
				return "JME Files (*.jme)";
			}
		});
		chooserOpen.setDialogTitle("Load Jerboa Modeler");

		final int returnVal = chooserOpen.showOpenDialog(owner.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File fPath = chooserOpen.getSelectedFile();
				Pair<JMEModeler, ArrayList<JMERule>> modelerLoaded = JMELoadModeler.loadModeler(fPath);
				JerboaModelerEditor editor = new JerboaModelerEditor(owner, modelerLoaded.l());
				owner.change(editor);
				editor.preferences.setLastModelerSavedPath(fPath.getAbsolutePath());
				editor.preferences.save();
				for(JMERule r : modelerLoaded.r()) {
					editor.openRule(r);
				}
				editor.reload();
				editor.repaint();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Loading error!",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// HAK: attention j'ai change la semantique du boolean
	// il indique true si on clique sur save,
	// et faux si on fait un save as...
	// mais je n'ai pas update le code de la fonction
	public void saveModeler(boolean simpleSave) {
		if (modeler == null)
			return;

		String sfilejme = modeler.getFileJME();
		if (sfilejme == null) {
			sfilejme = modeler.getName() + ".jme";
			simpleSave = false; // on fait un saveAS en premier lieu
		}

		if (simpleSave) {
			try {
				File filejme = new File(sfilejme);
				if (filejme.exists()) {
					filejme.renameTo(new File(sfilejme + ".bak"));
					JMESaveModeler mod = new JMESaveModeler(new FileOutputStream(filejme));
					System.out.println("Modeler save at : " + sfilejme);
					mod.save(getModeler(), JerboaModelerEditor.this);
					return;
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "An error occur: " + e1.getLocalizedMessage(),
						"Error file not found", JOptionPane.ERROR_MESSAGE);
			}
		}

		{
			File filejme = new File(sfilejme);

			final JFileChooser chooserSave = new JFileChooser(filejme);
			chooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
			chooserSave.setAcceptAllFileFilterUsed(false);
			chooserSave.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(final File f) {
					if (f.isDirectory())
						return true;
					final String nomFichier = f.getName().toLowerCase();

					return nomFichier.endsWith(".jme") || nomFichier.endsWith(".xml");
				}

				@Override
				public String getDescription() {
					return "JME Files";
				}
			});
			chooserSave.setDialogTitle("Save");
			chooserSave.setSelectedFile(filejme);

			final int returnVal = chooserSave.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// here the code to import, and we have to put some filters
				String fPath = chooserSave.getSelectedFile().getAbsolutePath();
				if (!fPath.endsWith(".jme") && !fPath.endsWith(".xml"))
					fPath += ".jme";
				try {
					modeler.setFileJME(fPath);
					JMESaveModeler mod = new JMESaveModeler(new FileOutputStream(fPath));
					mod.save(getModeler(), JerboaModelerEditor.this);
					System.out.println("Modeler save at : " + fPath);
					preferences.setLastModelerSavedPath(fPath);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	public void updateLookAndFeel(final String LOOKANDFEEL) {
		try {

			for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (LOOKANDFEEL.equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}

		SwingUtilities.updateComponentTreeUI(this);
		// this.pack();

	}

	@Override
	public void reload() {
		textModName.setText(modeler.getName());
		textModPack.setText(modeler.getModule());
		spinDim.setValue(modeler.getDimension());

		labelNbRules.setText("" + modeler.sizeRules());
		lblEmbeddings.setText("Embeddings("+modeler.sizeEmbeddings()+"):");

		mntmOpenRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath path = treeRules.getSelectionPath();
				if (path != null) {
					RuleTreeNodeLeaf rule = modelRuleTreeView.getRuleFromPath(path);
					if (rule != null)
						openRule(rule.getRule());
				}
			}
		});
		modelRuleTreeView.reload();

		updateOpenWindowsList();
	}

	// Create a new internal frame.
	protected void createRule(String rulename) {
		JMERuleAtomic rule = new JMERuleAtomic(modeler, rulename);
		modeler.addRule(rule);
		rule.addView(this);
		openRule(rule);
		check();
	}

	protected void deleteRule(JMERule r) {
		int confirm = JOptionPane.showConfirmDialog(this, "Are-you sure to delete the rule '" + r.getFullName() + "'?",
				"Delete", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			closeRule(r);
			modeler.removeRule(r);
		}
	}

	// Create a new internal frame.
	protected void createScriptRule(String rulename) {
		JMERule rule = new JMEScript(modeler, rulename);
		modeler.addRule(rule);
		rule.addView(this);
		openRule(rule);
		check();
	}

	public static void main(String[] args) {

		// HAK: pff qu'est ce que c'est nul j'ai pass� 4 heures
		// a debugguer pleins d'erreurs dans different fichier
		// a cause de tes modifs dans cette fonction!!!

		JerboaModelerEditorParentDefault frame = new JerboaModelerEditorParentDefault();
		JMEPreferences pref = frame.getPreferences();
		
		File fileLastModeler = pref.getLastModelerSavedPathFile();
		
		Pair<JMEModeler, ArrayList<JMERule>> loadedModeler = null;
		
		if(args.length > 0) {
			File fileinput = new File(args[0]);
			if(fileinput.exists()) {
				fileLastModeler = fileinput;
			}
		}
		
		if (fileLastModeler != null) {
			loadedModeler = JMELoadModeler.loadModeler(fileLastModeler);
		}else {
			
			JMEModeler modeler = new JMEModeler("Demo", "demomodeler.jerboa", 3);
			JMEEmbeddingInfo point = new JMEEmbeddingInfo(modeler, 0, "point", JerboaOrbit.orbit(1, 2, 3), "Point3",
					"");
			JMEEmbeddingInfo color = new JMEEmbeddingInfo(modeler, 1, "color", JerboaOrbit.orbit(0, 1), "Color3",
					"<h1>C'est une couleur de <b>face</b></h1>");
			modeler.addEmbedding(point);
			modeler.addEmbedding(color);

			JMERuleAtomic rule1 = new JMERuleAtomic(modeler, "DemoRule");
			modeler.addRule(rule1);
			JMENode n0 = rule1.getRight().creatNode(20, 50);
			JMENode n1 = rule1.getRight().creatNode(200, 50);
			rule1.getRight().creatArc(n0, n1, 0);
			rule1.setShowGrid(true);
			rule1.setShape(JMENodeShape.ROUNDRECTANGLE);
			rule1.setMagnetic(true);
			rule1.setGridsize(RuleGraphViewGrid.HUGE);
			modeler.resetModification();
			loadedModeler = new Pair<JMEModeler, ArrayList<JMERule>>(modeler, new ArrayList<>());
		}

		JerboaModelerEditor jme = new JerboaModelerEditor(frame, loadedModeler.l());
		// VAL : je trouve �a null qu'on ne puisse pas lancer l'�diteur sans modeleur...		
		frame.change(jme);
		jme.checkAll();
		
		for(JMERule r : loadedModeler.r()) {
			jme.openRule(r);
		}
		// frame.getParent().setLocationRelativeTo(null);
	}

	public JMEPreferences getPreferences() {
		return preferences;
	}

	public void setModeler(JMEModeler mod) {
		modeler = mod;
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	private static void addPopup(final Component component, final JPopupMenu popup,
			final JerboaModelerEditor modelerEditor) {
		// String userName = System.getProperties().getProperty("user.name");
		component.addMouseMotionListener(new MouseMotionListener() {
			private RulePreview preview;
			@Override
			public void mouseMoved(MouseEvent e) {
				/*if (component instanceof JTree) {
					TreePath path = ((JTree) component).getSelectionPath();
					if (path != null && ((JTree) component).getModel() instanceof RuleTreeViewerModel) {
						RuleTreeNodeLeaf rule = modelerEditor.modelRuleTreeView.getRuleFromPath(path);
						boolean otherRule = rule != null && preview != null
								&& preview.getRule().getName().compareTo(rule.getRule().getName()) != 0;
						if (rule != null && !(rule.getRule() instanceof JMEScript) && (preview == null || otherRule)) {
							if (otherRule) {
								preview.setVisible(false);
								preview = null;
							}
							preview = new RulePreview(rule.getRule(), null);
							preview.setLocation(new Point(e.getXOnScreen() + 100, e.getYOnScreen() - 40));
							preview.setVisible(true);
						} else if (preview != null
								&& (rule == null || !preview.isVisible() || rule.getRule() instanceof JMEScript)) {
							preview.setVisible(false);
							preview = null;
						}
					}
				}*/
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		component.addMouseListener(new MouseListener() {
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (component instanceof JTree) {
						TreePath path = ((JTree) component).getSelectionPath();
						if (path != null && ((JTree) component).getModel() instanceof RuleTreeViewerModel) {
							RuleTreeNodeLeaf rule = modelerEditor.modelRuleTreeView.getRuleFromPath(path);
							if (rule != null)
								modelerEditor.openRule(rule.getRule());
						}

					}
				}
			}
		});
	}

	public Window getWindow() {
		return owner.getParent();
	}

	@Override
	public void unlink() {
		modeler.removeView(this);
	}

	public void openRule(JMERule rule) {
		if (frames.containsKey(rule)) {
			RuleView ruleView = frames.get(rule);
			if (ruleView.getWindowContainer() != null) {
				ruleView.getWindowContainer().activate();
			} else {
				WindowContainerInternalFrame iframe = new WindowContainerInternalFrame(this, frames.get(rule));
				iframe.activate();
			}
		} else {
			if (rule instanceof JMERuleAtomic) {
				RuleAtomicView rav = new RuleAtomicView(this, (JMERuleAtomic) rule);
				WindowContainerInternalFrame iframe = new WindowContainerInternalFrame(this, rav);
				iframe.setFrameIcon(atomic);
				try {
					iframe.setMaximum(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
				iframe.activate();
				frames.put(rule, rav);
			} else if (rule instanceof JMEScript) {
				RuleScriptView rsv = new RuleScriptView(this, (JMEScript) rule);
				WindowContainerInternalFrame iframe = new WindowContainerInternalFrame(this, rsv);
				iframe.setFrameIcon(script);
				try {
					iframe.setMaximum(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
				iframe.activate();
				frames.put(rule, rsv);
			}
		}
		updateOpenWindowsList();
	}

	public void closeRule(JMERule rule) {
		if (frames.containsKey(rule)) {
			DockablePanel ruleView = frames.remove(rule);
			if (ruleView.getWindowContainer() != null)
				ruleView.getWindowContainer().close();
		}
		updateOpenWindowsList();
	}

	public void updateOpenWindowsList() {
		int count = mnOpenedWindows.getItemCount();
		for (int i = 0; i < count; i++) {
			FrameJMenuItem item = (FrameJMenuItem) mnOpenedWindows.getItem(i);
			item.unlink();
		}
		mnOpenedWindows.removeAll();
		List<JMERule> ordered = new ArrayList<>(frames.keySet());
		Collections.sort(ordered, new Comparator<JMERule>() {

			@Override
			public int compare(JMERule o1, JMERule o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		for (JMERule r : ordered) {
			FrameJMenuItem item = new FrameJMenuItem(r, this); 
			mnOpenedWindows.add(item);
		}
		// -------------------------------------------------------------
		count = listOpenRules.getComponentCount();
		for(int i = 0;i < count; i++) {
			if (listOpenRules.getComponent(i) instanceof OpenedDockItem) {
				OpenedDockItem ncomp = (OpenedDockItem) listOpenRules.getComponent(i);
				ncomp.unlink();
			}
		}
		listOpenRules.removeAll();
		listOpenRules.addSeparator();
		for (JMERule r : ordered) {
			OpenedDockItem item = new OpenedDockItem(this, frames.get(r)); 
			//item.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			listOpenRules.add(item);
			listOpenRules.addSeparator();
		}
		listOpenRules.invalidate();
		listOpenRules.repaint(500);
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public void createNewEmbeddingInfo() {
		JMEEmbeddingInfo ebdinfo = new JMEEmbeddingInfo(modeler, modeler.sizeEmbeddings(),
				"Ebd" + modeler.sizeEmbeddings(), JerboaOrbit.orbit(), "", ""); // DialogEmbedding.showDialog(this,
		// null);
		if (ebdinfo != null) {
			modeler.addEmbedding(ebdinfo);
			openEbd(ebdinfo);
			check();
		}
	}

	public void createNewAtomicRule() {
		if (modeler != null) {
			String rulename = null;
			// HAK: non mais serieux mon code est nettement mieux maintenant on
			// ne peut plus annuler une demande de creation
			// ca sert a quoi ton truc... pas compris...
			/*
			 * while (rulename == null) {
			 *
			 * rulename =
			 * JMEPreferences.getSuitableName(JOptionPane.showInputDialog(
			 * JerboaModelerEditor.this, "Atomic Rule's name: ", "Rule" +
			 * modeler.sizeRules()));
			 *
			 * if (rulename == null)
			 * JOptionPane.showMessageDialog(JerboaModelerEditor.this,
			 * "Wrong rule name, please retry (must begin with a letter)"); } if
			 * (rulename != null && !"".equals(rulename)) {
			 * createRule(rulename); }
			 */
			rulename = JOptionPane.showInputDialog(JerboaModelerEditor.this, "Atomic Rule's name: ",
					"Rule" + modeler.sizeRules());
			while(!JMERule.isValidName(preferences, rulename)) {
				JOptionPane.showMessageDialog(this, "Wrong name (must begin by a letter and not contain special characters)");
				rulename = JOptionPane.showInputDialog(JerboaModelerEditor.this, "Atomic Rule's name: ",
						"Rule" + modeler.sizeRules());
			}
			if (rulename != null && !"".equals(rulename)) {
				createRule(rulename);
			}
		}
	}

	public void createNewScriptRule() {
		if (JerboaModelerEditor.this.modeler != null) {
			/*
			 * Idem cf fct precedente String rulename =
			 * JMEPreferences.getSuitableName(JOptionPane.showInputDialog(
			 * JerboaModelerEditor.this, "Script Rule's name: ", "SRule" +
			 * JerboaModelerEditor.this.modeler.sizeRules()));
			 */

			String rulename = JOptionPane.showInputDialog(JerboaModelerEditor.this, "Script Rule's name: ",
					"SRule" + JerboaModelerEditor.this.modeler.sizeRules());
			if (rulename != null && !"".equals(rulename)) {
				createScriptRule(rulename);
			}
		}
	}

	public void quitNow() {
		getPreferences().save();
		if (modeler.isModified()) {
			final int option = JOptionPane.showConfirmDialog(null, "Do you want to save your modeler ?",
					"Save Security", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				saveModeler(true);
			} else if (option == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		owner.close();
	}

	public void openEbd(JMEEmbeddingInfo ebd) {
		if (ebd == null) {
			return;
		}

		if (framesEbds.containsKey(ebd)) {
			DockablePanel panel = framesEbds.get(ebd);
			if (panel.getWindowContainer() != null)
				panel.getWindowContainer().activate();
			else {
				WindowContainerInternalFrame iframe = new WindowContainerInternalFrame(this, panel);
				iframe.activate();
			}
		} else {
			DetailsEmbedding de = new DetailsEmbedding(this, ebd);
			WindowContainerInternalFrame iframe = new WindowContainerInternalFrame(this, de);
			try {
				iframe.setMaximum(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			iframe.activate();
			framesEbds.put(ebd, de);
		}
		// TODO: update liste des fenetre ouvertes des plongements
	}

	public void closeEbd(JMEEmbeddingInfo ebd) {
		if (ebd == null)
			return;

		if (framesEbds.containsKey(ebd)) {
			DockablePanel panel = framesEbds.remove(ebd);
			if (panel.getWindowContainer() != null)
				panel.getWindowContainer().close();
		}
	}
	
	public HashMap<JMERule, RuleView> getOpenRules(){
		return frames;
	}

	@Override
	public void check() {
		Collection<JMEError> errors = getPreferences().getVerif().run(modeler);
		if (errors.size() > 0) {
			modeler.setErrors(errors);
			bntEditAdv.doClick();
			// modelerAdvPanel.openErrorPanel();
			// Val: j'enleve cette ligne c'est trop énervant que ça s'ouvre sans prévenir
			reloadTitle();
		}
	}

	public void checkAll() {
		check();
		for (JMEEmbeddingInfo ebdinfo : modeler.getEmbeddings()) {
			Collection<JMEError> errors = getPreferences().getVerif().run(ebdinfo);
			ebdinfo.setErrors(errors);
		}
		for (JMERule rule : modeler.getRules()) {
			Collection<JMEError> errors = getPreferences().getVerif().run(rule);
			rule.setErrors(errors);
		}
	}

	@Override
	public void reloadTitle() {
		// C'est automatique ici pour le parent si c'est compatible avec son
		// environnement

		// mais j'ai besoin quand meme de lancer sur la fenetre de parametrage
		if (modelerAdvPanel != null)
			modelerAdvPanel.reloadTitle();
	}

	@Override
	public JMEElement getSourceElement() {
		return modeler;
	}
}
