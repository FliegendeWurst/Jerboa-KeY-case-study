package fr.up.xlim.sic.ig.jerboa.jme.forms.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.serialize.JMELoadModeler;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruletree.RuleTreeNodeLeaf;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruletree.RuleTreeViewerModel;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruletree.RuleTreeViewerRenderer;
import up.jerboa.core.util.Pair;

public class DialogImportRule extends JDialog {
	private static final long serialVersionUID = -8796910201359062122L;
	private JMEModeler baseModeler;
	private JTree ruleTree;
	private RuleTreeViewerModel modelRuleTreeView;
	private List<JMEEmbeddingInfo> ebdList;

	private ArrayList<CoupleEbd> listCoupleEbd;

	private String lastPath;

	private JScrollPane scrollEbdMap;

	private DialogImportRule(final JerboaModelerEditor editor, JMEModeler modeler) {
		super(editor.getWindow());
		baseModeler = modeler;
		listCoupleEbd = new ArrayList<>();
		setTitle("Rule importer");
		setMinimumSize(new Dimension(400, 200));
		setResizable(true);
		Box bGlob = Box.createVerticalBox();
		add(bGlob);
		ebdList = modeler.getEmbeddings();

		lastPath = "";

		JButton butLoad = new JButton("LoadModeler");
		butLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
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

				final int returnVal = chooserOpen.showOpenDialog(editor.getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						File fPath = chooserOpen.getSelectedFile();
						lastPath = fPath.getAbsolutePath();
						Pair<JMEModeler, ArrayList<JMERule>> modeler = JMELoadModeler.loadModeler(fPath);
						load(modeler.l());
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(editor, e.getLocalizedMessage(), "Loading error!",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		bGlob.add(butLoad);
		JButton butReLoad = new JButton("Reload");
		butReLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lastPath.length() != 0) {
					File fPath = new File(lastPath);
					Pair<JMEModeler, ArrayList<JMERule>> modeler = JMELoadModeler.loadModeler(fPath);
					load(modeler.l());
				}
			}
		});
		bGlob.add(butReLoad);

		Box hboxTree = Box.createHorizontalBox();
		bGlob.add(hboxTree);

		JButton butExpand = new JButton("v");
		JButton butRetract = new JButton("^");
		butExpand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				modelRuleTreeView.expandAllNodes();
			}
		});
		butRetract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				modelRuleTreeView.retractAllNodes();
			}
		});

		hboxTree.add(butExpand);
		hboxTree.add(butRetract);

		ruleTree = new JTree();
		modelRuleTreeView = new RuleTreeViewerModel(null);
		ruleTree = new JTree(modelRuleTreeView);
		ruleTree.setRootVisible(false);
		ruleTree.setCellRenderer(new RuleTreeViewerRenderer());

		ruleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		ruleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		ruleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		ruleTree.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					TreePath path = ruleTree.getSelectionPath();
					if (path != null && ruleTree.getModel() instanceof RuleTreeViewerModel) {
						RuleTreeNodeLeaf rule = modelRuleTreeView.getRuleFromPath(path);
						if (rule != null)
							importRule(rule.getRule());
					}
				}
			}
		});

		modelRuleTreeView.setJTree(ruleTree);

		Box boxContent = Box.createHorizontalBox();
		JScrollPane scrollRules = new JScrollPane(ruleTree);
		boxContent.add(scrollRules, BorderLayout.CENTER);

		scrollEbdMap = new JScrollPane(new JPanel());
		boxContent.add(scrollEbdMap);

		bGlob.add(boxContent);
		// TODO: faire le mapping entre les plongement des différents modeurs

		Box hboxGo = Box.createHorizontalBox();
		bGlob.add(hboxGo);
		JButton bntGo = new JButton("Import");
		hboxGo.add(bntGo);
		bntGo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath[] paths = ruleTree.getSelectionPaths();
				System.out.println("Start IMPORT RULES");
				for (TreePath path : paths) {
					if (path != null && ruleTree.getModel() instanceof RuleTreeViewerModel) {
						RuleTreeNodeLeaf rule = modelRuleTreeView.getRuleFromPath(path);
						if (rule != null) {
							System.out.println("IMPORT RULE: "+rule);
							importRule(rule.getRule());
						}
					}	
				}
				System.out.println("STOP IMPORT RULES");
			}
		});
		
		pack();
	}

	public void load(JMEModeler modeler) {
		modelRuleTreeView.setModeler(modeler);
		JPanel panEbdList = new JPanel();
		Box bH = Box.createVerticalBox();
		listCoupleEbd.clear();
		for (JMEEmbeddingInfo ebdi : modeler.getEmbeddings()) {
			CoupleEbd cpe = new CoupleEbd(ebdi, ebdList);
			bH.add(cpe);
			listCoupleEbd.add(cpe);
		}
		panEbdList.add(bH);
		scrollEbdMap.setViewportView(panEbdList);
	}

	public boolean modelerHasRule(JMEModeler m, String ruleName) {
		for (JMERule r : m.getRules()) {
			if (r.getName().compareTo(ruleName) == 0) {
				return true;
			}
		}
		return false;
	}

	public void importRule(JMERule rule) {
		String ruleName = rule.getName();
		while (modelerHasRule(baseModeler, ruleName)) {
			ruleName = ruleName + "BIS";
		}

		for (JMENode n : rule.getLeft().getNodes()) {
			ArrayList<JMENodeExpression> listExprToRemove = new ArrayList<>();
			for (JMENodeExpression e : n.getExplicitExprs()) {
				JMEEmbeddingInfo ebdInfo = getMappedEbdInfo(e.getEbdInfo());
				if (ebdInfo == null) {
					listExprToRemove.add(e);
				} else
					e.setEbdInfo(ebdInfo);
			}
			for (JMENodeExpression e : listExprToRemove) {
				n.removeExpression(e);
			}
		}
		for (JMENode n : rule.getRight().getNodes()) {
			ArrayList<JMENodeExpression> listExprToRemove = new ArrayList<>();
			for (JMENodeExpression e : n.getExplicitExprs()) {
				JMEEmbeddingInfo ebdInfo = getMappedEbdInfo(e.getEbdInfo());
				if (ebdInfo == null) {
					listExprToRemove.add(e);
				} else {
					String expr = e.getExpression();
					for (JMEEmbeddingInfo ei : rule.getModeler().getEmbeddings()) {
						if (getMappedEbdInfo(ei) != null)
							expr = expr.replaceAll("@ebd\\<" + ei.getName() + "\\>",
									"@ebd<" + getMappedEbdInfo(ei).getName() + ">");
						// pour mettre a jour le nom des plongements dans les
						// expressions
					}
					e.setEbdInfo(ebdInfo);
					e.setExpression(expr);
				}
			}
			for (JMENodeExpression e : listExprToRemove) {
				n.removeExpression(e);
			}
			n.setRequiredExpression(new ArrayList<>());
		}
		rule.setName(ruleName);
		rule.getModeler().removeRule(rule);
		baseModeler.addRule(rule);
		rule.setModeler(baseModeler);
		rule.resetModification();
		rule.update();
	}

	private JMEEmbeddingInfo getMappedEbdInfo(JMEEmbeddingInfo ebi) {
		for (CoupleEbd cebd : listCoupleEbd) {
			if (cebd.getEbdName().compareTo(ebi.getName()) == 0) {
				return cebd.getMatchInfo();
			}
		}
		return ebi;
	}

	public static JMEModeler showDialog(final JerboaModelerEditor editor, JMEModeler modeler) {
		DialogImportRule mod = new DialogImportRule(editor, modeler);
		mod.setVisible(true);
		return mod.baseModeler;
	}

	private class CoupleEbd extends JPanel {
		private JComboBox<JMEEmbeddingInfo> comboEbd;
		private JMEEmbeddingInfo ebdToMap;
		private JMEEmbeddingInfo empty;

		public CoupleEbd(JMEEmbeddingInfo ebdi, List<JMEEmbeddingInfo> listBaseEbd) {
			super();
			ebdToMap = ebdi;
			comboEbd = new JComboBox<>();
			empty = new JMEEmbeddingInfo(new JMEModeler("", "", -1), -666, "", null, "", "");
			comboEbd.addItem(empty);
			for (JMEEmbeddingInfo ei : listBaseEbd) {
				comboEbd.addItem(ei);
			}
			Box boxy = Box.createHorizontalBox();
			boxy.add(new JLabel(ebdi.getName()));
			boxy.add(comboEbd);

			for (int i = 0; i < listBaseEbd.size(); i++) {
				if (ebdi.getName().toLowerCase().compareTo(listBaseEbd.get(i).getName().toLowerCase()) == 0) {
					comboEbd.setSelectedIndex(i + 1);
				}
			}

			add(boxy);
		}

		public String getEbdName() {
			return ebdToMap.getName();
		}

		public JMEEmbeddingInfo getMatchInfo() {
			return comboEbd.getSelectedItem().equals(empty) ? null : (JMEEmbeddingInfo) comboEbd.getSelectedItem();
		}

		public Pair<JMEEmbeddingInfo, JMEEmbeddingInfo> getEbdPari() {
			JMEEmbeddingInfo target = comboEbd.getSelectedItem().equals(empty) ? null
					: (JMEEmbeddingInfo) comboEbd.getSelectedItem();
			return new Pair<JMEEmbeddingInfo, JMEEmbeddingInfo>(ebdToMap, target);
		}
	}
}
