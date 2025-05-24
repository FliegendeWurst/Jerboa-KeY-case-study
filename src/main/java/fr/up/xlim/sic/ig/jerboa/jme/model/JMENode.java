/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.List;

import up.jerboa.core.JerboaOrbit;

/**
 * @author Hakim Belhaouari & Romain Pascual
 *
 */
public final class JMENode implements JMEElement {

	// topological parameters
	protected String name;
	protected JerboaOrbit orbit;
	private JMENodeKind kind;


	// editor parameters
	protected JMEGraph graph;


	public JMENode(JMEGraph graph, String name, JMENodeKind k) {
		this.graph = graph;
		this.name = name;

		this.orbit = new JerboaOrbit();
		this.kind = k;
	}

	public String getName() {
		return name;
	}

	public JerboaOrbit getOrbit() {
		return orbit;
	}

	public JMENodeKind getKind() {
		return kind;
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
