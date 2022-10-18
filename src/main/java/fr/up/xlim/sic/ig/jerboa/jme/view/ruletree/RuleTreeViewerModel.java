package fr.up.xlim.sic.ig.jerboa.jme.view.ruletree;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class RuleTreeViewerModel extends DefaultTreeModel implements JMEElementView {

	private static final long serialVersionUID = 3319561793151921570L;
	// private JerboaModelerEditor editor;
	private JMEModeler modeler;
	private DefaultMutableTreeNode racine;
	private JTree tree;
	private boolean isflatten;
	private HashMap<String, RuleTreeNodeInterface> map;
	private ArrayList<JMERule> lastRules;
	private String filter;

	public RuleTreeViewerModel(JMEModeler mod) {
		super(new DefaultMutableTreeNode(""));

		// this.editor = editor;
		if (mod != null) {
			this.modeler = mod;// editor.getModeler();
			this.modeler.addView(this);
			racine = (DefaultMutableTreeNode) root;
			map = new HashMap<>();
			// lastRules = new ArrayList<>();
			isflatten = true;

			lastRules = new ArrayList<JMERule>(modeler.getAllRules());
			/* for (JMERule r : lastRules) {
				r.addView(this);
			} */
			filter = "";
		}
		filter = "";

	}

	public void setModeler(JMEModeler m) {
		if (modeler != null)
			modeler.removeView(this);
		modeler = m;
		this.modeler.addView(this);
		racine = (DefaultMutableTreeNode) root;
		map = new HashMap<>();
		isflatten = true;
		lastRules = new ArrayList<JMERule>(modeler.getRules());
		/*for (JMERule r : lastRules) {
			r.addView(this);
		}*/
		filter = "";
		reload();
	}

	public void setJTree(JTree tree) {
		this.tree = tree;
		tree.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				TreePath tp = ((JTree) e.getSource()).getPathForLocation(e.getX(), e.getY());
				if (tp != null) {
					((JTree) e.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));
					tree.setSelectionPath(tp); // ((JTree)
					// e.getSource()).getComponentAt(e.getX(),
					// e.getY()).setBackground(Color.BLACK);
				} else {
					((JTree) e.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					tree.clearSelection();
				}
			}
		});
	}

	private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
		for (int i = startingIndex; i < rowCount; ++i) {
			tree.expandRow(i);
		}

		if (tree.getRowCount() != rowCount) {
			expandAllNodes(tree, rowCount, tree.getRowCount());
		}
	}

	public void expandAllNodes() {
		expandAllNodes(tree, 0, 0);
	}

	private void retractAllNodes(JTree tree, int startingIndex, int rowCount) {
		for (int i = startingIndex; i < rowCount; ++i) {
			tree.collapseRow(i);
		}

		if (tree.getRowCount() != rowCount) {
			retractAllNodes(tree, rowCount, tree.getRowCount());
		}
	}

	public void retractAllNodes() {
		retractAllNodes(tree, 0, 0);
	}

	@Override
	public void unlink() {
		this.modeler.removeView(this);

		for (JMERule r : lastRules) {
			r.removeView(this);
		}
	}

	private HashMap<TreePath, Boolean> getTreeState(DefaultMutableTreeNode t) {
		HashMap<TreePath, Boolean> map = new HashMap<>();
		if (t.getChildCount() > 0) {
			TreePath tp = new TreePath(t.getPath());
			// System.err.println("# " + tp);
			map.put(tp, tree.isExpanded(tp));
			if (tree.isExpanded(tp)) {
				DefaultMutableTreeNode node = t.getNextNode();
				while (node != null) {
					map.putAll(getTreeState(node));
					node = node.getNextNode();
				}
			}
		}
		return map;
	}

	private void setTreeState(DefaultMutableTreeNode t, HashMap<TreePath, Boolean> state) {
		TreePath tPath = new TreePath(t.getPath());
		String tPathS = tPath.toString();
		for (TreePath path : state.keySet()) {
			if (tPathS.compareTo(path.toString()) == 0 && state.get(path)) {
				tree.expandRow(tree.getRowForPath(tPath));
				DefaultMutableTreeNode node = t.getNextNode();
				while (node != null) {
					setTreeState(node, state);
					node = node.getNextNode();
				}
				break;
			} else if (t.getPath().length == 1) {
				DefaultMutableTreeNode node = t.getNextNode();
				while (node != null) {
					setTreeState(node, state);
					node = node.getNextNode();
				}
				break;
			}
		}
	}

	@Override
	public void reload() {
		HashMap<TreePath, Boolean> treeState = new HashMap<TreePath, Boolean>();
		if (tree != null) {
			treeState = getTreeState(racine);
		}

		for (JMERule r : lastRules) {
			r.removeView(this);
		}
		lastRules = new ArrayList<JMERule>();

		for (JMERule f : modeler.getAllRules()) {
			if (f.getCategory().toLowerCase().contains(filter) || f.getName().toLowerCase().contains(filter)
					|| filter.equals("")) {
				f.addView(this);
				lastRules.add(f);
			}
		}

		racine.removeAllChildren();

		for (RuleTreeNodeInterface irule : map.values()) {
			if(irule instanceof RuleTreeNodeLeaf) {
				RuleTreeNodeLeaf lrule = ((RuleTreeNodeLeaf)irule);
				lrule.getRule().removeView(lrule);
			}
		}
		map.clear();

		TreeSet<JMERule> rules = new TreeSet<JMERule>(lastRules);

		for (JMERule r : rules) {
			String fullname = r.getFullName();
			if (!map.containsKey(fullname)) {
				addRuleTreeNode(r);
			}
		}

		super.reload();
		setTreeState(racine, treeState);
	}

	public void addRuleTreeNode(JMERule rule) {
		String category = rule.getCategory();

		String fullname = rule.getFullName();
		if (!map.containsKey(fullname)) {
			DefaultMutableTreeNode parent = findOrCreateParent(category);
			RuleTreeNodeLeaf rulenode = new RuleTreeNodeLeaf(rule);
			parent.add(rulenode);
			map.put(fullname, rulenode);
		}
	}

	private DefaultMutableTreeNode findOrCreateParent(String category) {
		if (category == null || "".equals(category))
			return racine;
		if (isflatten) {
			if (map.containsKey(category)) {
				return map.get(category).getTreeNode();
			} else {
				RuleTreeNodeCategory cat = new RuleTreeNodeCategory(category);
				map.put(category, cat);
				racine.add(cat);
				return cat;
			}
		} else {
			String[] split = category.split("[.]");
			int i = 0;
			String part = "";
			RuleTreeNodeInterface tmp = null;
			do {
				part = (i != 0 ? part + "." : "") + split[i];
				if (map.containsKey(part)) {
					tmp = map.get(part);
				} else {
					RuleTreeNodeCategory branch = new RuleTreeNodeCategory(split[i]);
					map.put(part, branch);
					if (tmp == null)
						racine.add(branch);
					else
						tmp.getTreeNode().add(branch);
					tmp = branch;
				}
				i++;
			} while (i < split.length);
			return tmp.getTreeNode();
		}
	}

	public void removeRuleTreeNode(JMERule rule) {
		String fullname = rule.getFullName();
		if (map.containsKey(fullname)) {
			RuleTreeNodeInterface rulenode = map.get(fullname);
			DefaultMutableTreeNode current = rulenode.getTreeNode();
			DefaultMutableTreeNode parent;
			while (current != racine) {
				parent = (DefaultMutableTreeNode) current.getParent();
				if (current.getChildCount() == 0)
					parent.remove(current);
				current = parent;

			}
		}
	}

	public void filter(String text) {
		if (text != null) {
			this.filter = text.toLowerCase();
			this.reload();
		}
	}

	public RuleTreeNodeLeaf getRuleFromPath(TreePath path) {
		String fullname = "";
		for (int i = 1; i < path.getPathCount() - 1; ++i) {
			fullname += path.getPathComponent(i) + ".";
		}
		fullname += path.getLastPathComponent();

		if(fullname.endsWith("*")) // cas des regles modifies dont l'affichage indique une etoile
			fullname = fullname.substring(0, fullname.length() - 1);


		RuleTreeNodeInterface node = map.get(fullname);
		if (node instanceof RuleTreeNodeLeaf)
			return (RuleTreeNodeLeaf) map.get(fullname);
		else
			return null;
	}

	public void setFlatView(boolean b) {
		if (this.isflatten != b) {
			this.isflatten = b;
			reload();
		}
	}

	@Override
	public JMEElement getSourceElement() {
		return modeler;
	}
}
