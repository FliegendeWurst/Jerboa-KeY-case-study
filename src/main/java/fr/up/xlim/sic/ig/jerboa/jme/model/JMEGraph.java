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
	protected ArrayList/*<JMENode>*/ nodes;
	protected ArrayList/*<JMEArc>*/ arcs;
	protected boolean isleft;

	public JMEGraph(JMERule rule, boolean isleft) {
		owner = rule;

		this.isleft = isleft;
		nodes = new ArrayList();
		arcs = new ArrayList();
	}

	public List/*<JMENode>*/ getNodes() {
		return nodes;
	}

	public List/*<JMEArc>*/ getArcs() {
		return arcs;
	}

	public boolean isLeft() {
		return isleft;
	}

	public JMENode getMatchNode(JMENode n) {
        for (int i = 0; i < nodes.size(); i++) {
            JMENode n2 = (JMENode) nodes.get(i);
            if (n.getName().equals(n2.getName()))
                return n2;
        }
		return null;
	}

	public List/*<JMENode>*/ getHooks() {
		List/*<JMENode>*/ listHook = new ArrayList();
		if (isLeft())
            for (int i = 0; i < nodes.size(); i++) {
                JMENode n = (JMENode) nodes.get(i);
                if (n.getKind() == JMENodeKind.HOOK)
                    listHook.add(n);
            }
		return listHook;
	}

	public List/*<JMEArc>*/ getIncidentArcsFromNode(JMENode node) {
		ArrayList/*<JMEArc>*/ incidentArcs = new ArrayList();
        List list = getArcs();
        for (int i = 0; i < list.size(); i++) {
            JMEArc arc = (JMEArc) list.get(i);
            if (arc.getSource() == node || arc.getDestination() == node) { // Undirected graph
                incidentArcs.add(arc);
            }
        }
		return incidentArcs;
	}

	public Set/*<JMENode>*/ orbit(JMENode node, JerboaOrbit orbit) {
		HashSet/*<JMENode>*/ visited = new HashSet();
		Stack/*<JMENode>*/ stack = new Stack();
		stack.push(node);

		while (!stack.isEmpty()) {
			JMENode cur = (JMENode) stack.pop();
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

	public JMERule getRule() {
		return owner;
	}

	@Override
	public String getName() {
		return isleft ? "LeftGraph" : "RightGraph";
	}
}
