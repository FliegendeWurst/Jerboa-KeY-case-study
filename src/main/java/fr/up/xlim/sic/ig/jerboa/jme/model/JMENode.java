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

	//@ public ghost \locset footprint;
	//@ public accessible \inv : footprint;
	//@ public invariant footprint == \set_union(\singleton(footprint), orbit.footprint);

	//@ public invariant \invariant_for(this.orbit);

	// topological parameters
	public final String name;
	public final JerboaOrbit orbit;
	private /*@ spec_public */ final JMENodeKind kind;


	// editor parameters
	protected /*@ spec_public */ final JMEGraph graph;


	/*@ public normal_behaviour
	  @ ensures this != null;
	  @*/
	public JMENode(JMEGraph graph, String name, JMENodeKind k) {
		this.graph = graph;
		this.name = name;

		this.orbit = new JerboaOrbit(new int[0]);
		this.kind = k;
	}

	/*@ public normal_behavior
	  @ ensures \result == this.name;
	  @ strictly_pure
	  @*/
	public String getName() {
		return name;
	}

	/*@ public normal_behavior
	  @ ensures \result == this.kind;
	  @ strictly_pure
	  @*/
	public JMENodeKind getKind() {
		return kind;
	}

	/*@ public normal_behavior
	  @ ensures \result == this.graph;
	  @ strictly_pure
	  @*/
	public JMEGraph getGraph(){
		return graph;
	}

	/*@ public normal_behavior
	  @ ensures (\forall int i; 0 <= i && i < \result.size(); \result.get(i) instanceof JMEArc);
	  @ assignable \nothing;
	  @*/
	public List/*<JMEArc>*/ alphas() {
		return (graph.getIncidentArcsFromNode(this));
	}
	
	
	public JMENode alpha(int i) {
		List/*<JMEArc>*/ arcs = alphas();
		for (int j = 0; j < arcs.size(); j++) {
			JMEArc arc = (JMEArc) arcs.get(j);
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
