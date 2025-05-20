/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.awt.Color;
import java.util.List;

import up.jerboa.core.JerboaOrbit;

/**
 * @author Hakim Belhaouari & Romain Pascual
 *
 */
public class JMENode implements JMEElement {

	// topological parameters
	protected String name;
	protected JerboaOrbit orbit;
	private JMENodeKind kind;
	protected String precondition;
	protected JMEParamTopo paramTopo;
	private Color color;


	// editor parameters
	protected JMEGraph graph;


	public JMENode(JMEGraph graph, String name, JMENodeKind k) {
		this.graph = graph;
		this.name = name;

		this.orbit = new JerboaOrbit();
		this.kind = k;
		this.color = new Color(255, 255, 255, 0);

		this.precondition = "";

		paramTopo = new JMEParamTopo(graph.getRule(), this);
	}

	public JMENode(JMENode node, JMEGraph newgraph) {
		this.name = node.name;
		this.orbit = new JerboaOrbit(node.orbit);
		this.kind = JMENodeKind.SIMPLE;
		
		this.graph = newgraph;
		this.precondition = "";
		paramTopo = new JMEParamTopo(newgraph.getRule(), this);
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	public JerboaOrbit getOrbit() {
		return orbit;
	}

	public JMENodeKind getKind() {
		return kind;
	}

	public void setKind(JMENodeKind kind) {
		if (this.kind != kind) {
			this.kind = kind;
			if (kind == JMENodeKind.HOOK)
				graph.getRule().addParamTopo(paramTopo);
			else
				graph.getRule().delParamTopo(paramTopo);
		}
	}

	public void setOrbit(JerboaOrbit jerboaOrbit) {
		if (!orbit.equalsStrict(jerboaOrbit)) {
			this.orbit = jerboaOrbit;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(name);
		sb.append(']');
		return sb.toString();
	}

	public void setName(String text) {
		if (text != null && !text.equals(name)) {
			this.name = text;
		}
	}


	public JMERule getRule() {
		return graph.getRule();
	}

	public Color getColor() {
		return color;
	}

	public boolean isLeftNode() {
		return graph.isLeft();
	}

	public boolean isRightNode() {
		return !graph.isLeft();
	}

	public String getPrecondition() {
		return precondition;
	}

	public void setPrecondition(String n) {
		if(n != null && !n.equals(precondition)) {
			this.precondition = n;
		}
	}

	public int getID() {
		List<JMENode> nodes = graph.getNodes();
		for (int i = 0;i < nodes.size(); ++i) {
			JMENode n = nodes.get(i);
			if(n == this)
				return i;
		}
		return -1;
	}
	
	public JMEGraph getGraph(){
		return graph;
	}
	
	
	public List<JMEArc> alphas() {
		return (graph.getIncidentArcsFromNode(this));
	}
	
	
	public JMENode alpha(int i) {
		List<JMEArc> arcs = alphas();
		for (JMEArc arc : arcs) {
			if(arc.getDimension() == i) {
				if(arc.getSource() == this )
					return arc.getDestination();
				else if(arc.getDestination() == this)
					return arc.getSource();
			}
		}
		return null;
	}

}
