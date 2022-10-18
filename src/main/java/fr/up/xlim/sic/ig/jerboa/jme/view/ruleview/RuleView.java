package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import fr.up.xlim.sic.ig.jerboa.jme.export.CPPExport_New_Engine;
import fr.up.xlim.sic.ig.jerboa.jme.export.CPPExport_V2;
import fr.up.xlim.sic.ig.jerboa.jme.export.JavaExport;
import fr.up.xlim.sic.ig.jerboa.jme.export.JavaExport_V2;
import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
//import fr.up.xlim.sic.ig.jerboa.jme.model.GraphDisplacer;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModelerGenerationType;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.RuleCompositor;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMENodeShape;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.util.RuleGraphViewGrid;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEVerif;
import fr.up.xlim.sic.ig.jerboa.jme.view.errorstree.ErrorsPanelTabDescription;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.ArcInfo;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.JPanelElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.LeftRuleNodeInfo;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.RightRuleNodeInfo;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.RuleGraphView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.tab.JMETabbedPane;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.DockablePanelDefault;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.WindowContainerInterface;

public abstract class RuleView extends DockablePanelDefault {
	private static final long serialVersionUID = 7412843793588397180L;

	final static Icon refreshIcon = new ImageIcon(new ImageIcon(RuleView.class.getResource("/image/refresh_icon.png"))
			.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

	final static Icon exportPictureIcon = new ImageIcon(new ImageIcon(RuleView.class.getResource("/image/picture.png"))
			.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

	private boolean startCheck = false;
	private JMERule rule;
	protected WindowContainerInterface container;

	protected JerboaModelerEditor editor;
	protected RuleGraphView current;
	protected RuleGraphView left;
	protected RuleGraphView right;
	protected HashMap<JMEElement, JPanelElementView> leftselect;
	protected HashMap<JMEElement, JPanelElementView> rightselect;
	protected RuleDetailTab panelSettings;
	protected RulePrecondTab panelPrecond;
	protected RulePreProcessTab panelPreprocess;
	protected RulePostProcessTab panelPostprocess;
	protected RuleMidProcessTab panelMidprocess;
	// protected RuleDetailInitialization panelInit;

	protected RuleParamsPanelEbdPart panelParamEbdPart;
	protected RuleParamsPanelTopoPart panelParamTopoPart;

	protected JSplitPane splitPaneGraphs;
	protected JScrollPane scrollLeft;
	protected JScrollPane scrollRight;
	protected JToolBar toolBar;
	protected JPanel status;
	protected JLabel lblInformations;
	protected JButton check;
	protected JToggleButton grid;
	protected JToggleButton magnetic;
	protected JComboBox<RuleGraphViewGrid> gridList;
	protected JComboBox<JMENodeShape> shape;
	protected JPanel panel;
	protected JSplitPane splitEditor;
	protected JPanel panelEditor;
	protected JPanel panelGraphs;
	protected JMETabbedPane tabbedPane;
	protected JPanel panelLeftInfo;
	protected JScrollPane scrollPaneLeftInfos;
	protected Box vLeftRuleNodesInfos;
	protected JPanel panelRightInfo;
	protected JScrollPane scrollPaneRightInfos;
	protected Box vRightRuleNodesInfos;
	protected JPanel panelParameters;
	protected RuleDetailHeader panelHeader;
	protected HashMap<String, RuleExpressionPanel> exprmap = new HashMap<>();
	protected HashMap<JMENode, RulePrecondNodeTab> precondmap = new HashMap<>();
	protected JButton bntExportImage;

	protected JMenuBar menuBar;
	protected JMenu mnViews;
	protected JMenuItem mntmHeader;
	protected JMenuItem mntmPrecondition;
	protected JMenuItem mntmPreprocess;
	protected JMenuItem mntmPostprocess;
	protected JMenu mnFile;
	protected JMenuItem mntmExportImage;
	protected JMenuItem mntmExportGraphML;
	protected JMenuItem mntmExportD3JS;
	protected JSeparator separator;
	protected JMenuItem mntmDetails;
	protected JMenuItem mntmParameters;
	protected JMenuItem mntmErrorDescription;
	protected JSeparator separator_1;
	private JMenuItem mntmRefresh;

	private ErrorsPanelTabDescription panelErrors;

	private JButton exportCurrentRule;
	private JMenuItem mntmTopoParam;

	private JMenuItem mntmMidprocess;
	private JToggleButton fillColor;
	private JToggleButton drawAlpha;
	private JToggleButton displayError;

	private JButton resetScaleFactor;

	private RulePreview preview = null;
	private JButton fillDefCode;

	public RuleView(JerboaModelerEditor editor, JMERule rule) {
		super(rule.getName(), rule);

		this.rule = rule;
		this.editor = editor;

		leftselect = new HashMap<>();
		rightselect = new HashMap<>();

		setLayout(new BorderLayout(0, 0));

		JPanel panelCenter = new JPanel(new BorderLayout());
		add(panelCenter, BorderLayout.CENTER);

		panel = new JPanel();
		panelCenter.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		splitEditor = new JSplitPane();
		splitEditor.setResizeWeight(1.0);
		splitEditor.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitEditor.setOneTouchExpandable(true);
		panel.add(splitEditor, BorderLayout.CENTER);

		panelEditor = new JPanel();
		splitEditor.setRightComponent(panelEditor);
		panelEditor.setLayout(new BorderLayout(0, 0));

		tabbedPane = new JMETabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		panelEditor.add(tabbedPane, BorderLayout.CENTER);

		panelSettings = new RuleDetailTab(this, this.rule);
		panelSettings.reload();
		tabbedPane.addTab("Details", panelSettings);

		panelParameters = new JPanel();
		panelParameters.setLayout(new GridLayout(1, 0));
		panelParamEbdPart = new RuleParamsPanelEbdPart(this, this.rule);
		panelParamTopoPart = new RuleParamsPanelTopoPart(this, this.rule);
		// panelParameters.add(panelParamEbdPart);
		// panelParameters.add(panelParamTopoPart);
		panelParamEbdPart.reload();
		panelParamTopoPart.reload();
		tabbedPane.addTab("Topo. Param.", panelParamTopoPart);
		tabbedPane.addTab("Ebd. Param.", panelParamEbdPart);

		panelHeader = new RuleDetailHeader(this);
		panelHeader.reload();

		panelPrecond = new RulePrecondTab(this);
		panelPrecond.reload();

		// panelInit = new RuleDetailInitialization(this);
		// panelInit.reload();

		panelPreprocess = new RulePreProcessTab(this);
		panelPreprocess.reload();

		panelPostprocess = new RulePostProcessTab(this);
		panelPostprocess.reload();

		panelMidprocess = new RuleMidProcessTab(this);
		panelMidprocess.reload();

		tabbedPane.setSelectedIndex(0);

		panelGraphs = new JPanel();
		panelGraphs.setMinimumSize(new Dimension(10, 100));
		panelGraphs.setPreferredSize(new Dimension(10, 200));
		splitEditor.setLeftComponent(panelGraphs);
		panelGraphs.setLayout(new BorderLayout(0, 0));
		if (this.rule == null) {
			left = new RuleGraphView(this, rule, null, true);
			right = new RuleGraphView(this, rule, null, true);
		} else {
			left = new RuleGraphView(this, rule, this.rule.getLeft(), true);
			right = new RuleGraphView(this, rule, this.rule.getRight(), true);
		}
		splitPaneGraphs = new JSplitPane();
		panelGraphs.add(splitPaneGraphs);
		splitPaneGraphs.setResizeWeight(0.5);
		splitPaneGraphs.setOneTouchExpandable(true);

		scrollLeft = new JScrollPane();
		scrollLeft.setPreferredSize(new Dimension(300, 300));

		JPanel panelLeft = new JPanel();
		panelLeft.setLayout(new BorderLayout());
		panelLeft.add(scrollLeft, BorderLayout.CENTER);
		splitPaneGraphs.setLeftComponent(panelLeft);

		scrollLeft.setViewportView(left);

		panelLeftInfo = new JPanel();
		panelLeftInfo.setPreferredSize(new Dimension(10, 70));
		panelLeft.add(panelLeftInfo, BorderLayout.NORTH);
		panelLeftInfo.setLayout(new BorderLayout(0, 0));

		scrollPaneLeftInfos = new JScrollPane();
		panelLeftInfo.add(scrollPaneLeftInfos);

		vLeftRuleNodesInfos = Box.createVerticalBox();
		scrollPaneLeftInfos.setViewportView(vLeftRuleNodesInfos);

		scrollRight = new JScrollPane();
		scrollRight.setPreferredSize(new Dimension(300, 300));

		JPanel panelRight = new JPanel();
		panelRight.setLayout(new BorderLayout());
		panelRight.add(scrollRight, BorderLayout.CENTER);
		splitPaneGraphs.setRightComponent(panelRight);

		scrollRight.setViewportView(right);

		panelRightInfo = new JPanel();
		panelRightInfo.setPreferredSize(new Dimension(10, 70));
		panelRight.add(panelRightInfo, BorderLayout.NORTH);
		panelRightInfo.setLayout(new BorderLayout(0, 0));

		scrollPaneRightInfos = new JScrollPane();
		panelRightInfo.add(scrollPaneRightInfos, BorderLayout.CENTER);

		vRightRuleNodesInfos = Box.createVerticalBox();
		scrollPaneRightInfos.setViewportView(vRightRuleNodesInfos);
		// splitEditor.setDividerLocation(0.4);

		toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setAutoscrolls(true);
		panelCenter.add(toolBar, BorderLayout.NORTH);

		exportCurrentRule = new JButton(new ImageIcon(RuleView.class.getResource("/image/export.png")));
		exportCurrentRule.setToolTipText("Export this rule to code");
		exportCurrentRule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rule.getModeler().getGenerationType() == JMEModelerGenerationType.ALL
						|| rule.getModeler().getGenerationType() == JMEModelerGenerationType.CPP) {
					CPPExport_New_Engine.exportModelerOnly(rule.getModeler());
					CPPExport_New_Engine.exportRule(rule);
				}else if (rule.getModeler().getGenerationType() == JMEModelerGenerationType.ALL
						|| rule.getModeler().getGenerationType() == JMEModelerGenerationType.CPP_V2) {
					CPPExport_V2.exportModelerOnly(rule.getModeler());
					CPPExport_V2.exportRule(rule);
				}else if (rule.getModeler().getGenerationType() == JMEModelerGenerationType.ALL
						|| rule.getModeler().getGenerationType() == JMEModelerGenerationType.JAVA) {
					JavaExport.exportModelerOnly(rule.getModeler());
					JavaExport.exportRule(rule);
				}else if (rule.getModeler().getGenerationType() == JMEModelerGenerationType.ALL
						|| rule.getModeler().getGenerationType() == JMEModelerGenerationType.JAVA_V2) {
					JavaExport_V2.exportModelerOnly(rule.getModeler());
					JavaExport_V2.exportRule(rule);
				}
			}
		});
		toolBar.add(exportCurrentRule);

		check = new JButton(new ImageIcon(RuleView.class.getResource("/image/check.png")));
		check.setToolTipText("Check the rule");
		check.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMEVerif verif = getPreferences().getVerif();

				Collection<JMEError> errors = verif.run(rule);
				for (JMEError jmeError : errors) {
					System.out.println(jmeError);
				}
				RuleView.this.rule.setErrors(errors);
				if(RuleView.this instanceof RuleScriptView) {
					((RuleScriptView)RuleView.this).getScriptPanel().verif();
				}
				
			}
		});
		toolBar.add(check);

		bntExportImage = new JButton(new ImageIcon(RuleView.class.getResource("/image/picture.png")));
		bntExportImage.setToolTipText("Export as SVG picture...");
		bntExportImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exportSVG();
			}
		});
		toolBar.add(bntExportImage);

		shape = new JComboBox<JMENodeShape>();
		shape.setToolTipText("Shape of node inside the graph");
		shape.setModel(new DefaultComboBoxModel<JMENodeShape>(JMENodeShape.values()));
		shape.setSelectedIndex(rule.getShape().ordinal());
		shape.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				rule.setShape((JMENodeShape) e.getItem());
				left.repaint();
				right.repaint();
			}
		});

		magnetic = new JToggleButton(new ImageIcon(RuleView.class.getResource("/image/magnet.png")));
		magnetic.setToolTipText("Magnetic");
		magnetic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rule.setMagnetic(magnetic.isSelected());
				left.repaint();
				right.repaint();
			}
		});
		toolBar.add(magnetic);

		grid = new JToggleButton(new ImageIcon(RuleView.class.getResource("/image/grid.png")));
		grid.setToolTipText("Display the grid");
		grid.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				rule.setShowGrid(grid.isSelected());
				left.repaint();
				right.repaint();
			}
		});
		toolBar.add(grid);

		resetScaleFactor = new JButton("Scale x1");
		// new ImageIcon(RuleView.class.getResource("/image/grid.png")));
		resetScaleFactor.setToolTipText("Reset scale factor");
		resetScaleFactor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				left.zoomFactor(1);
				right.zoomFactor(1);
				left.repaint();
				right.repaint();
			}
		});
		toolBar.add(resetScaleFactor);

		JButton previewBut = new JButton("Preview");
		// new ImageIcon(RuleView.class.getResource("/image/grid.png")));
		previewBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				preview = new RulePreview(rule, RuleView.this);
				preview.setVisible(true);
				preview.requestFocus();
				System.err.println("Preview!");
			}
		});

		JButton recurseBut = new JButton("Composition");
		// new ImageIcon(RuleView.class.getResource("/image/grid.png")));
		recurseBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rule instanceof JMERuleAtomic) {
					String res = JOptionPane.showInputDialog("Enter an iteration number",2);
					String op2 = JOptionPane.showInputDialog("Enter The other rule Name tocompose",rule.getName());

					try {
						JMERule r2 = getModeler().searchRule(op2);
						if(r2 != null && r2 instanceof JMERuleAtomic) {
							RuleCompositor rc = new RuleCompositor((JMERuleAtomic)rule, (JMERuleAtomic)r2);
							getModeler().addRule(rc.getIteration(Integer.parseInt(res)));
							editor.reload();
							System.err.println("Composition!");
						}
					}catch (Exception err) {
						err.printStackTrace();
					}

				}
			}
		});
		toolBar.add(recurseBut); 
		
		JButton recurseSelfBut = new JButton("CompositionSelf");
		// new ImageIcon(RuleView.class.getResource("/image/grid.png")));
		recurseSelfBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rule instanceof JMERuleAtomic) {
					String res = JOptionPane.showInputDialog("Enter an iteration number",2);
					try {
						getModeler().addRule(RuleCompositor.getComposition((JMERuleAtomic)rule, Integer.parseInt(res)));
						editor.reload();
						System.err.println("Composition!");
					}catch (Exception err) {
						err.printStackTrace();
					}

				}
			}
		});
		toolBar.add(recurseSelfBut); 
				
//		
//		JButton displaceBut = new JButton("GraphDisplace");
//		// new ImageIcon(RuleView.class.getResource("/image/grid.png")));
//		displaceBut.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(rule instanceof JMERuleAtomic) {
//					ArrayList<Float> dispDim = new ArrayList<>( );
//					dispDim.add(150.f);
//					dispDim.add(100.f);
//					dispDim.add(100.f);
//					dispDim.add(5.f);
//					GraphDisplacer gd = new GraphDisplacer(rule.getRight(), dispDim,editor);
//					gd.go();
//					editor.reload();
//				}
//			}
//		});
//		toolBar.add(displaceBut); 


		fillColor = new JToggleButton(new ImageIcon(RuleView.class.getResource("/image/fillColor.png")));
		fillColor.setToolTipText("Fill color node");
		fillColor.setSelected(getPreferences().getIsFillNode());
		fillColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getPreferences().getIsFillNode() != fillColor.isSelected()) {
					getPreferences().setIsFillNode(fillColor.isSelected());
					reload();
				}
			}
		});

		toolBar.add(fillColor);

		drawAlpha = new JToggleButton(new ImageIcon(RuleView.class.getResource("/image/notalpha.png")));
		drawAlpha.setSelectedIcon(new ImageIcon(RuleView.class.getResource("/image/alpha.png")));
		drawAlpha.setToolTipText("Display alpha character");
		drawAlpha.setSelected(getPreferences().getShowAlpha());
		drawAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getPreferences().getShowAlpha() != drawAlpha.isSelected()) {
					getPreferences().setShowAlpha(drawAlpha.isSelected());
					reload();
				}
			}
		});

		toolBar.add(drawAlpha);

		displayError = new JToggleButton(new ImageIcon(RuleView.class.getResource("/image/noerror.png")));
		displayError.setSelectedIcon(new ImageIcon(RuleView.class.getResource("/image/error.png")));
		displayError.setToolTipText("Display/Hide the errors");
		displayError.setSelected(getPreferences().getShowErrors());
		displayError.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getPreferences().getShowErrors() != displayError.isSelected()) {
					getPreferences().setShowErrors(displayError.isSelected());
					reload();
				}
			}
		});

		toolBar.add(displayError);
		toolBar.addSeparator();
		toolBar.add(new JLabel("Shape:"));
		toolBar.add(shape);
		toolBar.addSeparator();

		toolBar.add(new JLabel("Grid size:"));

		gridList = new JComboBox<RuleGraphViewGrid>();
		gridList.setToolTipText("Gridsize of graphs");
		gridList.setModel(new DefaultComboBoxModel<RuleGraphViewGrid>(RuleGraphViewGrid.values()));
		gridList.setSelectedIndex(rule.getGridsize().ordinal());
		gridList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				rule.setGridsize((RuleGraphViewGrid) e.getItem());
				left.repaint();
				right.repaint();
			}
		});
		toolBar.add(gridList);
		
		fillDefCode = new JButton("");
		fillDefCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(RuleView.this.rule instanceof JMERuleAtomic) {
					// on fait des copies pour les modifs en cascade qui risque de perturber les listes (concurrentmodif)
					List<JMENode> rightnodes = new ArrayList<>(RuleView.this.rule.getRight().getNodes()); 
					for (JMENode rightnode : rightnodes) {
						List<JMENodeExpression> exprs = new ArrayList<>(rightnode.getRequiredExprs());
						for (JMENodeExpression expression : exprs) {
							JMEEmbeddingInfo ebdinfo = expression.getEbdInfo();
							if(ebdinfo != null && ebdinfo.getDefaultCode() != null && ebdinfo.getDefaultCode().trim().length() > 0) {
								rightnode.addExplicitExpression(new JMENodeExpression(rightnode, ebdinfo, ebdinfo.getDefaultCode()));
							}
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(RuleView.this, "It seems your rule is not an atomic rule!", "Spread default code for embeddings", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		fillDefCode.setIcon(new ImageIcon(RuleView.class.getResource("/image/crosshair_20x20.png")));
		fillDefCode.setToolTipText("Fill undefined embedding with their default code, if it exists");
		toolBar.add(fillDefCode);

		status = new JPanel();
		FlowLayout flowLayout = (FlowLayout) status.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		status.setPreferredSize(new Dimension(10, (int) (getFont().getSize() * 1.5)));
		add(status, BorderLayout.SOUTH);

		lblInformations = new JLabel("Informations:");
		status.add(lblInformations);

		menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmExportImage = new JMenuItem("Export Image");
		mntmExportImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		mntmExportImage.setIcon(exportPictureIcon);
		mntmExportImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportSVG();
			}
		});
		
		mntmExportGraphML = new JMenuItem("Export GraphML");
		mntmExportGraphML.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		mntmExportGraphML.setIcon(exportPictureIcon);
		mntmExportGraphML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportGraphML();
			}
		});
		
		
		mntmExportD3JS = new JMenuItem("Export D3.js");
		mntmExportD3JS.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		mntmExportD3JS.setIcon(exportPictureIcon);
		mntmExportD3JS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportD3JS_p();
			}
		});

		mntmRefresh = new JMenuItem("Refresh");
		mntmRefresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmRefresh.setIcon(refreshIcon);
		mntmRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		mnFile.add(mntmRefresh);
		mnFile.add(mntmExportImage);
		mnFile.add(mntmExportGraphML);
		mnFile.add(mntmExportD3JS);

		mnViews = new JMenu("Views");
		menuBar.add(mnViews);

		mntmHeader = new JMenuItem("Header");
		mntmHeader.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_MASK));
		mntmHeader.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = tabbedPane.indexOfComponent(panelHeader);
				if (i == -1)
					tabbedPane.addTabDescription(panelHeader);
				else
					tabbedPane.setSelectedIndex(i);
			}
		});

		mntmDetails = new JMenuItem("Details");
		mntmDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_MASK));
		mntmDetails.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
			}
		});
		mnViews.add(mntmDetails);

		mntmParameters = new JMenuItem("Embedding Parameters");
		mntmParameters.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK));
		mntmParameters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		mnViews.add(mntmParameters);

		mntmTopoParam = new JMenuItem("Topological Parameters");
		mntmTopoParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tabbedPane.setSelectedIndex(2);
			}
		});
		mntmTopoParam.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_MASK));
		mnViews.add(mntmTopoParam);

		separator = new JSeparator();
		mnViews.add(separator);
		mnViews.add(mntmHeader);

		mntmPreprocess = new JMenuItem("PreProcess");
		mntmPreprocess.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_MASK));
		mntmPreprocess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = tabbedPane.indexOfComponent(panelPreprocess);
				if (i == -1)
					tabbedPane.addTabDescription(panelPreprocess);
				else
					tabbedPane.setSelectedIndex(i);
			}
		});
		mnViews.add(mntmPreprocess);

		mntmPrecondition = new JMenuItem("Precondition");
		mntmPrecondition.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_MASK));
		mntmPrecondition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = tabbedPane.indexOfComponent(panelPrecond);
				if (i == -1)
					tabbedPane.addTabDescription(panelPrecond);
				else
					tabbedPane.setSelectedIndex(i);
			}
		});
		mnViews.add(mntmPrecondition);

		mntmMidprocess = new JMenuItem("MidProcess");
		mntmMidprocess.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_MASK));
		mntmMidprocess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = tabbedPane.indexOfComponent(panelMidprocess);
				if (i == -1)
					tabbedPane.addTabDescription(panelMidprocess);
				else
					tabbedPane.setSelectedIndex(i);
			}
		});
		mnViews.add(mntmMidprocess);


		mntmPostprocess = new JMenuItem("PostProcess");
		mntmPostprocess.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_MASK));
		mnViews.add(mntmPostprocess);

		separator_1 = new JSeparator();
		mnViews.add(separator_1);

		panelErrors = new ErrorsPanelTabDescription(this);

		mntmErrorDescription = new JMenuItem("Error description");
		mntmErrorDescription.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
		mntmErrorDescription.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openPanelError();
			}
		});
		mnViews.add(mntmErrorDescription);

		mntmPostprocess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = tabbedPane.indexOfComponent(panelPostprocess);
				if (i == -1)
					tabbedPane.addTabDescription(panelPostprocess);
				else
					tabbedPane.setSelectedIndex(i);
			}
		});

		// fin

		/*
		 * shape.setSelectedIndex(rule.getShape().ordinal());
		 * if(rule.isMagnetic() ) { if(!magnetic.isSelected())
		 * magnetic.doClick(); } else { if(magnetic.isSelected())
		 * magnetic.doClick(); }
		 *
		 * gridList.setSelectedIndex(rule.getGridsize().ordinal());
		 * if(rule.isShowGrid()) { if(!grid.isSelected()) grid.doClick(); } else
		 * { if(grid.isSelected()) grid.doClick(); }
		 */

		// on affiche les erreurs et sinon on ouvre le panneau des erreurs.
		tabbedPane.addTabDescription(panelErrors);
		if(rule.countErrors() > 0)
			openPanelError();

		reload();

		rule.addView(this);
		startCheck = true;
	}

	public void showPreview() {
		preview = new RulePreview(rule, this);
		preview.setVisible(true);
	}

	protected void openPanelError() {
		int i = tabbedPane.indexOfComponent(panelErrors);
		if (i == -1)
			tabbedPane.addTabDescription(panelErrors);
		else
			tabbedPane.setSelectedIndex(i);
	}

	public void close() {
		container.close();
	}

	public abstract void exportToSVG(String path);

	public JMEPreferences getPreferences() {
		return editor.getPreferences();
	}

	public JerboaModelerEditor getEditor() {
		return editor;
	}

	public void refresh() {
		for (JPanelElementView rni : leftselect.values()) {
			rni.reload();
			rni.repaint();
		}
		for (JPanelElementView rni : rightselect.values()) {
			rni.reload();
			rni.repaint();
		}
		revalidate();
		repaint();
	}
	
	protected void exportD3JS_p() {
		File filejs = new File("");

		final JFileChooser chooserSave = new JFileChooser(filejs);
		chooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
		chooserSave.setAcceptAllFileFilterUsed(false);
		chooserSave.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(final File f) {
				if (f.isDirectory())
					return true;
				final String nomFichier = f.getName().toLowerCase();

				return nomFichier.endsWith(".json");
			}

			@Override
			public String getDescription() {
				return "GraphML Files";
			}
		});
		chooserSave.setDialogTitle("Export D3.js");
		chooserSave.setSelectedFile(filejs);
		final int returnVal = chooserSave.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fPath = chooserSave.getSelectedFile().getAbsolutePath();
			if (!fPath.endsWith(".json"))
				fPath += ".json";
			exportToD3JS(fPath);
		}
	}

	private void exportToD3JS(String fPath) {
		File file = new File(fPath);
		
		try(PrintStream ps = new PrintStream(file)) {
			JMEGraph graph = current.getGraph();
			
			ps.println("var nodes =  [");
			List<JMENode> nodes = graph.getNodes();
			for (JMENode node : nodes) {
				ps.println("{ \"id\" : \"" + node.getName() + "\", \"orbit\" : \"" + node.getOrbit().toString() + "\" }, ");
			}
			ps.println("]; ");
			
			
			ps.println("var links =  [");
			List<JMEArc> arcs = graph.getArcs();
			for (JMEArc arc : arcs) {
				ps.println("{  \"source\" : \""+arc.getSource().getName() 
						+ "\", \"target\" : \""+arc.getDestination().getName()
						+ "\", \"dim\" : " + arc.getDimension() +" },");
			}
			ps.println("]; ");
			ps.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void exportGraphML() {
		File filegml = new File("");

		final JFileChooser chooserSave = new JFileChooser(filegml);
		chooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
		chooserSave.setAcceptAllFileFilterUsed(false);
		chooserSave.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(final File f) {
				if (f.isDirectory())
					return true;
				final String nomFichier = f.getName().toLowerCase();

				return nomFichier.endsWith(".graphml");
			}

			@Override
			public String getDescription() {
				return "GraphML Files";
			}
		});
		chooserSave.setDialogTitle("Export GraphML");
		chooserSave.setSelectedFile(filegml);
		final int returnVal = chooserSave.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fPath = chooserSave.getSelectedFile().getAbsolutePath();
			if (!fPath.endsWith(".graphml"))
				fPath += ".graphml";
			exportToGML(fPath);
		}
	}


	public void exportToGML(String fPath) {
		File file = new File(fPath);
		
		try(PrintStream ps = new PrintStream(file)) {
			// ecriture en-tete
			ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
					+ "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"  \r\n"
					+ "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
					+ "    xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns \r\n"
					+ "     http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
			
			ps.println("<graph id=\"G\" edgedefault=\"undirected\">");
			JMEGraph graph = current.getGraph();
			
			ps.println("<key id=\"dim\" for=\"edge\" attr.name=\"color\" attr.type=\"string\" />\r\n"
					+ "<key id=\"color\" for=\"node\" attr.name=\"color\" attr.type=\"string\">\r\n"
					+ "    <default>yellow</default>\r\n"
					+ "  </key>"
					+ "  <key id=\"orbit\" for=\"node\" attr.name=\"orbit\" attr.type=\"string\" />"
					+ "  <key id=\"idname\" for=\"node\" attr.name=\"name\" attr.type=\"string\" />");
			
			List<JMENode> nodes = graph.getNodes();
			for (JMENode node : nodes) {
				ps.println("<node id=\"" + node.getName()+"\" >");
				ps.println("<data key=\"orbit\">" + node.getOrbit().toString().replace('<', ' ').replace('>', ' ') + "</data>");
				ps.println("<data key=\"idname\">" + node.getName() + "</data>");
				ps.println("</node>");
			}
			
			List<JMEArc> arcs = graph.getArcs();
			for (JMEArc arc : arcs) {
				ps.println("<edge source=\""+arc.getSource().getName() + "\" target=\""+arc.getDestination().getName()+"\">");
				String color = "yellow";
				switch(arc.getDimension()) {
				case 0: color = "black"; break;
				case 1: color ="red"; break;
				case 2: color ="blue"; break;
				case 3: color ="green"; break;
				default: color = "yellow"; break;
				}
				ps.println("<data key=\"dim\">" + color + "</data>");
				ps.println("</edge>");
			}
			ps.println("</graph>");
			ps.println("</graphml>");
			ps.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void exportSVG() {
		File filesvg = new File("");

		final JFileChooser chooserSave = new JFileChooser(filesvg);
		chooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
		chooserSave.setAcceptAllFileFilterUsed(false);
		chooserSave.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(final File f) {
				if (f.isDirectory())
					return true;
				final String nomFichier = f.getName().toLowerCase();

				return nomFichier.endsWith(".svg");
			}

			@Override
			public String getDescription() {
				return "SVG Files";
			}
		});
		chooserSave.setDialogTitle("Export image");
		chooserSave.setSelectedFile(filesvg);
		final int returnVal = chooserSave.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fPath = chooserSave.getSelectedFile().getAbsolutePath();
			if (!fPath.endsWith(".svg"))
				fPath += ".svg";
			exportToSVG(fPath);
		}
	}

	public void addSelect(RuleGraphView ruleGraphView, JMEElement p) {
		if (ruleGraphView == left) {
			JMEElement elem = p;
			if (elem instanceof JMENode) {
				if (!leftselect.containsKey(elem)) {
					LeftRuleNodeInfo info = new LeftRuleNodeInfo(this, (JMENode) elem);
					leftselect.put(elem, info);
					vLeftRuleNodesInfos.add(info);
				}
			} else if (elem instanceof JMEArc) {
				if (!leftselect.containsKey(elem)) {
					ArcInfo info = new ArcInfo(this, (JMEArc) elem);
					leftselect.put(elem, info);
					vLeftRuleNodesInfos.add(info);
				}
			}
		} else {
			JMEElement elem = p;
			if (elem instanceof JMENode) {
				if (!rightselect.containsKey(elem)) {
					RightRuleNodeInfo info = new RightRuleNodeInfo(this, (JMENode) elem);
					rightselect.put(elem, info);
					vRightRuleNodesInfos.add(info);
				}
			} else if (elem instanceof JMEArc) {
				if (!rightselect.containsKey(elem)) {
					ArcInfo info = new ArcInfo(this, (JMEArc) elem);
					rightselect.put(elem, info);
					vRightRuleNodesInfos.add(info);
				}
			}
		}
		revalidate();
		repaint();
	}

	public void remSelect(RuleGraphView ruleGraphView, JMEElement p) {
		if (ruleGraphView == left) {// TODO ici la selection...
			JMEElement elem = p;
			if (leftselect.containsKey(elem)) {
				JPanelElementView info = leftselect.remove(elem);
				elem.removeView(info);
				vLeftRuleNodesInfos.remove(info);
			}
		} else {
			JMEElement elem = p;
			if (rightselect.containsKey(elem)) {
				JPanelElementView info = rightselect.remove(elem);
				elem.removeView(info);
				vRightRuleNodesInfos.remove(info);
			}
		}
		revalidate();
		repaint();
	}



	@Override
	public void reload() {
		shape.setSelectedIndex(rule.getShape().ordinal());
		if (rule.isMagnetic()) {
			if (!magnetic.isSelected())
				magnetic.doClick();
		} else {
			if (magnetic.isSelected())
				magnetic.doClick();
		}

		gridList.setSelectedIndex(rule.getGridsize().ordinal());
		if (rule.isShowGrid()) {
			if (!grid.isSelected())
				grid.doClick();
		} else {
			if (grid.isSelected())
				grid.doClick();
		}

		left.reload();
		right.reload();
		panelSettings.reload();

		panelHeader.reload();
		// panelInit.reload();
		panelPostprocess.reload();
		panelPreprocess.reload();
		panelPrecond.reload();

		panelParamEbdPart.reload();
		panelParamTopoPart.reload();
		reloadTitle();
	}

	public JMEModeler getModeler() {
		return editor.getModeler();
	}

	public void setLeftRightGraph(boolean left) {
		if (current != null)
			current.simuLostFocus();
		if (left) {
			current = this.left;
		} else {
			current = this.right;
		}
		current.simuGainFocus();
	}

	public RuleGraphView getLeft() {
		return left;
	}

	public RuleGraphView getRight() {
		return right;
	}

	@Override
	public void unlink() {
		rule.removeView(this);
		left.unlink();
		right.unlink();

		panelSettings.unlink();
		panelPrecond.unlink();
		panelPreprocess.unlink();
		panelPostprocess.unlink();
		// panelInit.unlink();

		panelParamEbdPart.unlink();
		panelParamTopoPart.unlink();
	}

	public void setCurrentGraph(RuleGraphView graph) {
		if (current != graph) {
			if (current != null)
				current.simuLostFocus();
			current = graph;
			current.simuGainFocus();
		}
	}

	public JMERule getRule() {
		return rule;
	}

	@Override
	public Component getRootComponent() {
		return this;
	}

	@Override
	public String getTitle() {
		return rule.getName() + (rule.isModified() ? "*" : "");
	}

	@Override
	public void setWindowContainer(WindowContainerInterface windowContainerDialog) {
		container = windowContainerDialog;
	}

	@Override
	public WindowContainerInterface getWindowContainer() {
		return container;
	}

	@Override
	public int getSizeX() {
		return rule.getSizeX();
	}

	@Override
	public int getSizeY() {
		return rule.getSizeY();
	}

	@Override
	public boolean isMaximized() {
		return false;
	}

	public JMETabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void openNodeExpression(JMENode node, JMEEmbeddingInfo ebdinfo) {
		JMENodeExpression nodeexpr = node.searchExpression(ebdinfo);
		JMENode nodeREF = nodeexpr.getNode();
		String title = nodeREF.getName() + "#" + ebdinfo.getName();
		RuleExpressionPanel panel = null;
		if (exprmap.containsKey(title)) {
			panel = exprmap.get(title);
		} else {
			panel = new RuleExpressionPanel(this, nodeexpr, title);
			exprmap.put(title, panel);
		}
		tabbedPane.addTabDescription(panel);
		panel.fixFocus();
	}

	public void openNodePrecondition(JMENode node) {
		if(precondmap.containsKey(node)) {
			RulePrecondNodeTab tab = precondmap.get(node);
			tabbedPane.setSelectedComponent(tab);
		}
		else {
			RulePrecondNodeTab tab = new RulePrecondNodeTab(this, node);
			precondmap.put(node, tab);
			tabbedPane.addTabDescription(tab);
		}
	}

	@Override
	public void OnResize(int width, int height) {
		this.rule.setSizeX(width);
		this.rule.setSizeY(height);
	}

	@Override
	public void OnClose() {
		// TODO faire une sauvegarde?
		// bon on referme au cas ou le signal viendrai de la fenetre 
		editor.closeRule(this.rule);
	}


	@Override
	public void OnFocus(boolean temporary) {
		if(!temporary)
		{
			System.out.println("focus");
			drawAlpha.setSelected(getPreferences().getShowAlpha());
			fillColor.setSelected(getPreferences().getIsFillNode());
			displayError.setSelected(getPreferences().getShowErrors());
		}
	}

	@Override
	public void OnFocusLost(boolean temporary) {
		if(!temporary) {

		}
	}

	/*
	 * public void addTab(String title) { tabbedPane.add(title,new
	 * JLabel(title)); int index = tabbedPane.indexOfTab(title);
	 * tabbedPane.setTabComponentAt(index, new HeaderTabComponent(tabbedPane));
	 * }
	 */


	@Override
	public void check() {
		if(startCheck) {
			reloadTitle();
			/*System.out.println("CHECK: "+rule);
			try {
				throw new Exception("debug");
			}
			catch(Exception e) {
				e.printStackTrace(System.out);
			}*/

			Collection<JMEError> errors = getPreferences().getVerif().run(rule);
			// if(errors.size() > 1)
			// openPanelError();
			// Val: j'enleve cette ligne c'est trop Ã©nervant que Ã§a s'ouvre sans prÃ©venir
			rule.setErrors(errors);

			editor.check();
		}
	}

	public String getStateName() {
		return rule.getName() + (rule.isModified()? "*" : "");
	}

	@Override
	public void reloadTitle() {
		if(container != null) {
			container.setTitle(getStateName());
		}
	}
}
