package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.IntStream;

import up.jerboa.core.JerboaOrbit;

public final class JMEGraph implements JMEElement {

	protected JMERule owner;
	protected ArrayList<JMENode> nodes;
	protected ArrayList<JMEArc> arcs;
	protected boolean isleft;
	protected boolean selected;
	protected boolean updateExprs = true;

	public JMEGraph(JMERule rule, boolean isleft) {
		owner = rule;

		this.isleft = isleft;
		nodes = new ArrayList<>();
		arcs = new ArrayList<>();
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

	public JMENode creatNode() {
		String newnode = genIDName();
		JMENode node = new JMENode(this, newnode, JMENodeKind.SIMPLE);
		nodes.add(node);
		return node;
	}

	public JMENode addNode(JMENode node) {
		nodes.add(node);
		return node;
	}

	public void removeNode(JMENode node) {
		if (nodes.remove(node)) {
			if (node.getKind() == JMENodeKind.HOOK) {
				owner.delParamTopo(node);
			}
		}
	}

	public JMEArc creatArc(JMENode a, JMENode b, int dim) {
		JMEArc arc = new JMEArc(this, a, b, dim);
		arcs.add(arc);
		return arc;
	}

	public JMELoop creatLoop(JMENode na, int dim) {
		JMELoop loop = new JMELoop(this, na, dim);
		arcs.add(loop);
		return loop;
	}

	public void removeArc(JMEArc arc) {
		arcs.remove(arc);
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
			if (arc.getSource() == node || arc.getDestination() == node) { // Undirected graph
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

	public JMERule getRule() {
		return owner;
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

		JerboaOrbit orbitForCC = new JerboaOrbit(IntStream
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
