package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.IntStream;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import up.jerboa.core.JerboaOrbit;

public class JMEGraph implements JMEElement {

	protected JMERule owner;
	protected ArrayList<JMENode> nodes;
	protected ArrayList<JMEArc> arcs;
	protected boolean isleft;
	protected boolean modified;
	protected boolean selected;
	protected Set<JMEElementView> views;
	protected UndoManager manager;
	protected boolean updateExprs = true;

	public JMEGraph(JMERule rule, boolean isleft) {
		owner = rule;

		this.isleft = isleft;
		nodes = new ArrayList<>();
		arcs = new ArrayList<>();
		modified = false;
		views = new HashSet<>();
		this.manager = new UndoManager();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (isleft)
			sb.append("L");
		else
			sb.append("R");
		sb.append("Graph ").append(owner.getName());
		sb.append(" NODES : ");
		for (JMENode n : nodes) {
			sb.append(n);
		}
		return sb.toString();
	}

	public List<JMENode> getNodes() {
		return nodes;
	}

	public List<JMEArc> getArcs() {
		return arcs;
	}

	public boolean isLeft() {
		return isleft;
	}
	
	public boolean isUpdateExprs() {
		return updateExprs;
	}

	public void setUpdateExprs(boolean updateExprs) {
		this.updateExprs = updateExprs;
	}

	public JMENode creatNode(int x, int y) {
		String newnode = genIDName();
		JMENode node = new JMENode(this, newnode, x, y, JMENodeKind.SIMPLE);
		nodes.add(node);
		manager.registerUndo(new UndoItemField(this, "addnode", node, null, !modified));
		modified = true;
		return node;
	}

	public JMENode addNode(JMENode node) {
		nodes.add(node);
		manager.registerUndo(new UndoItemField(this, "addnode", node, null, !modified));
		modified = true;
		return node;
	}

	public void removeNode(JMENode node) {
		if (nodes.remove(node)) {
			manager.registerUndo(new UndoItemField(this, "delnode", node, null, !modified));
			if (node.getKind() == JMENodeKind.HOOK) {
				owner.delParamTopo(node);
			}
			modified = true;
		}
	}

	public JMEArc creatArc(JMENode a, JMENode b, int dim) {
		JMEArc arc = new JMEArc(this, a, b, dim);
		arcs.add(arc);
		manager.registerUndo(new UndoItemField(this, "addarc", arc, null, !modified));
		modified = true;
		updateAllExprs();
		return arc;
	}

	public JMELoop creatLoop(JMENode na, int dim) {
		JMELoop loop = new JMELoop(this, na, dim);
		arcs.add(loop);
		manager.registerUndo(new UndoItemField(this, "addarc", loop, null, !modified));
		modified = true;
		updateAllExprs();
		return loop;
	}

	public void removeArc(JMEArc arc) {
		if (arcs.remove(arc)) {
			manager.registerUndo(new UndoItemField(this, "delarc", arc, null, !modified));
			modified = true;

			updateAllExprs();
		}
	}

	private String genIDName() {
		int i = 0;
		while (existNodeName(i)) {
			i++;
		}
		return "n" + i;
	}

	private boolean existNodeName(int i) {
		for (JMENode node : nodes) {
			if (node.getName().equals("n" + i))
				return true;
		}

		return false;
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean isModified() {
		for (JMEArc arc : arcs) {
			if (arc.isModified())
				return true;
		}

		for (JMENode node : nodes) {
			if (node.isModified())
				return true;
		}
		return modified;
	}

	public JMENode getMatchNode(JMENode n) {
		for (JMENode n2 : nodes) {
			if (n.getName().equals(n2.getName()))
				return n2;
		}
		return null;
	}

	public JMENode getMatchNode(String nodeName) {
		for (JMENode n2 : nodes) {
			if (nodeName.equals(n2.getName()))
				return n2;
		}
		return null;
	}
	
	public JMENode searchNodeByName(String name) {
		for (JMENode node : nodes) {
			if (node.getName().equals(name))
				return node;
		}
		return null;
	}

	@Override
	public void addView(JMEElementView view) {
		views.add(view);
	}

	@Override
	public void removeView(JMEElementView view) {
		views.remove(view);
	}

	@Override
	public void update() {
		updateAllExprs();
		ArrayList<JMEElementView> aviews = new ArrayList<>(views);
		for (JMEElementView view : aviews) {
			view.reload();
		}
	}

	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		manager.transfertRedo(fitem);
		switch (fitem.field()) {
		case "addnode":
			nodes.remove(fitem.value());
			break;
		case "delnode":
			nodes.add((JMENode) fitem.value());
			break;
		case "addarc":
			arcs.remove(fitem.value());
			break;
		case "delarc":
			arcs.add((JMEArc) fitem.value());
			break;
		}
		if (fitem.getModifState())
			modified = false;
		update();
	}

	@Override
	public void redo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		manager.transfertUndo(fitem);
		switch (fitem.field()) {
		case "addnode":
			nodes.add((JMENode) fitem.value());
			break;
		case "delnode":
			nodes.remove(fitem.value());
			break;
		case "addarc":
			arcs.add((JMEArc) fitem.value());
			break;
		case "delarc":
			arcs.remove(fitem.value());
			break;
		}
		if (fitem.getModifState())
			modified = true;
		update();
	}

	public List<JMENode> getHooks() {
		List<JMENode> listHook = new ArrayList<>();
		if (isLeft())
			for (JMENode n : nodes) {
				if (n.getKind() == JMENodeKind.HOOK)
					listHook.add(n);
			}
		return listHook;
	}

	public List<JMEArc> getIncidentArcsFromNode(JMENode node) {
		ArrayList<JMEArc> incidentArcs = new ArrayList<>();
		for (JMEArc arc : getArcs()) {
			if (arc.getSource() == node || arc.getDestination() == node) { // Graph
																			// non
																			// oriente
				incidentArcs.add(arc);
			}
		}
		return incidentArcs;
	}

	public HashSet<JMENode> addConnectedNodes(JMENode node, HashSet<JMENode> connectedNodes) {
		connectedNodes.add(node);
		for (JMEArc arc : getIncidentArcsFromNode(node)) {
			JMENode otherNode = null;
			if (node == arc.getSource())
				otherNode = arc.getDestination();
			else if (node == arc.getDestination())
				otherNode = arc.getSource();
			try {
				if (!connectedNodes.contains(otherNode))
					connectedNodes = addConnectedNodes(otherNode, connectedNodes);
			} catch (NullPointerException e) {
				;
			}
		}
		return connectedNodes;
	}

	public Set<JMENode> orbit(JMENode node, JerboaOrbit orbit) {
		HashSet<JMENode> visited = new HashSet<>();
		Stack<JMENode> stack = new Stack<>();
		stack.push(node);

		while (!stack.isEmpty()) {
			JMENode cur = stack.pop();
			if (!visited.contains(cur)) {
				visited.add(cur);
				List<JMEArc> arcs = getIncidentArcsFromNode(cur);
				for (JMEArc arc : arcs) {
					if (arc.getDimension() >= 0 && orbit.contains(arc.getDimension())) {
						JMENode otherNode = null;
						if (arc.getSource() == cur)
							otherNode = arc.getDestination();
						else if (arc.getDestination() == cur)
							otherNode = arc.getSource();
						if (otherNode != null && !visited.contains(otherNode))
							stack.push(otherNode);
					} // end dimension ok
				} // for all incident arcs
			} // if node is still not visited
		} // while the search continue

		return visited;
	}
	
	/**
	 * Quotient the graph by the orbit and return one node per orbit 
	 * @param orbit used for the quotient
	 * @return set of nodes, one per orbit in the graph.
	 */
	public Set<JMENode> oneNodePerOrbit(JerboaOrbit orbit) {
		HashSet<JMENode> res = new HashSet<>();
		HashSet<JMENode> visited = new HashSet<>();
		for (JMENode node : nodes) {
			if (!visited.contains(node)) {
				visited.addAll(orbit(node, orbit));
				res.add(node);
			}
		}
		return res;	
	}

	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitGraph(this);
	}

	@Override
	public UndoManager getUndoManager() {
		return manager;
	}

	public JMERule getRule() {
		return owner;
	}

	public void updateAllExprs() {
		if (!updateExprs)
			return;
		
		nodes.stream().forEach(node -> {
			List<JMENodeExpression> e = computeImplicitExprs(node);
			node.setImplicitExpression(e);
		});

		nodes.stream().forEach(node -> {
			List<JMENodeExpression> e = computeRequiredExprs(node);
			node.setRequiredExpression(e);
		});

	}

	private List<JMENodeExpression> computeImplicitExprs(JMENode start) {
		ArrayList<JMENodeExpression> list = new ArrayList<>();
		for (JMEEmbeddingInfo e : owner.getModeler().getEmbeddings()) {
			if (hasExplicitExpression(start, e) == null) {
				HashSet<JMENode> visited = new HashSet<>();
				Stack<JMENode> stack = new Stack<>();
				JerboaOrbit orbit = e.getOrbit();
				stack.push(start);
				while (!stack.isEmpty()) {
					JMENode n = stack.pop();
					if (!visited.contains(n)) {
						visited.add(n);
						JMENodeExpression exp = hasExplicitExpression(n, e);
						if (exp != null) {
							list.add(exp);
							break;
						}
						for (JMEArc ai : arcs) {
							if (orbit.contains(ai.getDimension())) {
								JMENode neighbor = null;
								if (ai.getSource() == n) {
									neighbor = ai.getDestination();
								} else if (ai.getDestination() == n) {
									neighbor = ai.getSource();
								}

								stack.push(neighbor);
							}
						}
					}
				} // end wile loop
			} // end has Explicit
		} // end for all embedding
		return list;
	}

	// hyp: les explicits et implicits sont corrects
	private List<JMENodeExpression> computeRequiredExprs(JMENode start) {
		ArrayList<JMENodeExpression> list = new ArrayList<>();
		JMEGraph left = owner.getLeft();
		if (!isleft && left.countRuleNodeWithName(start.getName()) == 0) {
			for (JMEEmbeddingInfo e : owner.getModeler().getEmbeddings()) {
				boolean find = false;
				Set<JMENode> nodes = orbit(start, e.getOrbit());
				for (JMENode n : nodes) {
					if (left.countRuleNodeWithName(n.getName()) > 0) {
						find = true;
					}
				}
				JMENodeExpression mene = new JMENodeExpression(start, e, "");
				if (!find && !start.existExpression(mene)) {
					list.add(mene);
				}
			}
		}
		return list;
	}

	private JMENodeExpression hasExplicitExpression(JMENode n, JMEEmbeddingInfo ref) {
		if (n != null && n.getExplicitExprs() != null)
			for (JMENodeExpression expr : n.getExplicitExprs()) {
				if (expr.getEbdInfo() == ref) {
					return expr;
				}
			}
		return null;
	}

	@Override
	public void resetModification() {
		modified = false;
		for (JMEArc jmeArc : arcs) {
			jmeArc.resetModification();
		}

		for (JMENode jmeNode : nodes) {
			jmeNode.resetModification();
		}
	}

	public int countRuleNodeWithName(String name) {
		int res = 0;
		for (JMENode node : nodes) {
			if (node.getName().equals(name)) {
				res++;
			}
		}
		return res;
	}

	@Override
	public String getName() {
		return isleft ? "LeftGraph" : "RightGraph";
	}

	public void copy(JMEGraph graph) {
		for (JMENode node : nodes) {
			graph.addNode(node.copy(graph));
		}
		isleft = graph.isleft;
		for (JMEArc arc : arcs) {
			graph.arcs.add(arc.copy(graph));
		}
		graph.selected = selected;

	}


	
	/**
	 * Try to add a hook to connected components without one (take any node with
	 * full orbit).
	 * 
	 * @author romain
	 */
	public void enforceHooks() {
		if (!isLeft())
			return;

		List<JMENode> hooks = getHooks();
		Set<JMENode> seen = new HashSet<JMENode>();

		JerboaOrbit orbitForCC = JerboaOrbit.orbit(IntStream
				.rangeClosed(0, owner.getModeler().getDimension()).toArray());

		for (JMENode node : getNodes()) {
			if (!seen.contains(node)) {
				Set<JMENode> nodesInCC = orbit(node, orbitForCC);
				int countHook = nodesInCC.stream()
						.mapToInt(n -> (hooks.contains(n) ? 1 : 0)).sum();
				seen.addAll(nodesInCC);

				if (countHook > 0)
					continue;

				for (JMENode ccNode : nodesInCC) {
					// declare a node in the CC to be the hook (if possible)
					if (!ccNode.getOrbit().contains(-1)) {
						ccNode.setKind(JMENodeKind.HOOK);
						break;
					}
				}
			}
		}
	}
}
