package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import up.jerboa.core.JerboaOrbit;

public final class JMEGraph implements JMEElement {

	//@ public invariant \invariant_for(nodes) && \invariant_for(arcs);
	//@ public invariant (\forall int i; 0 <= i && i < nodes.seq.length; nodes.seq[i] instanceof JMENode && \invariant_for((JMENode)nodes.seq[i]));
	//@ public invariant (\forall int j; 0 <= j && j < arcs.seq.length; arcs.seq[j] instanceof JMEArc && \invariant_for((JMEArc)arcs.seq[j]));

	//@ public ghost \locset footprint;
	//@ public accessible \inv : footprint;
	//@ public invariant footprint == \set_union(\singleton(footprint), this.nodes.footprint, (\infinite_union int i; 0<=i && i<nodes.seq.length; ((JMENode)nodes.seq[i]).footprint), this.arcs.footprint, \singleton(this.isleft), \singleton(this.owner));

	 /*@ requires \invariant_for(this);
	   @ accessible nodes,nodes.footprint,
	   @   (\infinite_union int i; 0<=i && i<nodes.seq.length; ((JMENode)nodes.seq[i]).orbit),
	   @   (\infinite_union int i; 0<=i && i<nodes.seq.length; ((JMENode)nodes.seq[i]).orbit.dim[*]);
	   @ helper model public boolean hasCorrectDimensionsNodes(int modDim) {
           return (\forall int i;
             0 <= i && i < nodes.seq.length;
				(\forall int j;
	              0 <= j && j < ((JMENode)nodes.seq[i]).orbit.dim.length;
	              ((JMENode)nodes.seq[i]).orbit.dim[j] >= -1 && ((JMENode)nodes.seq[i]).orbit.dim[j] <= modDim)
             );
         }
       @*/

	/*@ requires \invariant_for(this);
	  @ accessible arcs,arcs.footprint;
	  @ helper model public boolean hasCorrectDimensionsArcs(int modDim) {
        return (\forall int i;
         0 <= i && i < arcs.seq.length;
          ((JMEArc)arcs.seq[i]).dim >= 0 && ((JMEArc)arcs.seq[i]).dim <= modDim);
        }
      @*/

	protected JMERule owner;
	public final List/*<JMENode>*/ nodes;
	public final List/*<JMEArc>*/ arcs;
	protected boolean isleft;

	/*@ public normal_behaviour
	  @ ensures this != null;
	  @*/
	public JMEGraph(JMERule rule, boolean isleft) {
		owner = rule;

		this.isleft = isleft;
		nodes = new ArrayList();
		arcs = new ArrayList();
	}

	/*@ public normal_behavior
	  @ ensures \result == this.isleft;
	  @ ensures \invariant_for(this);
	  @ strictly_pure
	  @*/
	public boolean isLeft() {
		return isleft;
	}

	public JMENode getMatchNode(JMENode n) {
        for (int i = 0; i < nodes.size(); i++) {
            JMENode n2 = (JMENode) nodes.get(i);
			if (n.name.equals(n2.name))
                return n2;
        }
		return null;
	}

	/*@ public normal_behavior
	  @ requires this.isleft;
	  @ ensures (\forall int i; 0 <= i && i < this.nodes.seq.length;
	  @  (((JMENode)this.nodes.seq[i]).kind == JMENodeKind.HOOK)
	  @    ==> (\exists int j; 0 <= j && j < \result.seq.length; \result.seq[j] == this.nodes.seq[i]))
	  @  && (\forall int a; 0 <= a && a < \result.seq.length;
	  @       (\exists int b; 0 <= b && b < this.nodes.seq.length;
	  @         \result.seq[a] == this.nodes.seq[b] && (((JMENode)this.nodes.seq[b]).kind == JMENodeKind.HOOK)))
	  @  && (\forall int a; 0 <= a && a < \result.seq.length; \result.seq[a] instanceof JMENode && \invariant_for((JMENode)\result.seq[a]))
	  @  && \invariant_for(\result) && \fresh(\result);
	  @ assignable \nothing;
	  @*/
	public List/*<JMENode>*/ getHooks() {
		List/*<JMENode>*/ listHook = new ArrayList();
		if (isleft) {
			int nodesSize = nodes.size();
			/*@ loop_invariant
			  @ 0 <= i && i <= nodesSize
			  @  && (\forall int ix; 0 <= ix && ix < i;
	  		  @       (((JMENode)this.nodes.seq[ix]).kind == JMENodeKind.HOOK)
	  		  @         ==> (\exists int j; 0 <= j && j < listHook.seq.length; listHook.seq[j] == this.nodes.seq[ix]))
	  		  @  && (\forall int a; 0 <= a && a < listHook.seq.length;
	  		  @       (\exists int b; 0 <= b && b < this.nodes.seq.length;
	  		  @         listHook.seq[a] == this.nodes.seq[b] && ((JMENode)this.nodes.seq[b]).kind == JMENodeKind.HOOK))
	  		  @  && (\forall int a; 0 <= a && a < listHook.seq.length; listHook.seq[a] instanceof JMENode && \invariant_for((JMENode)listHook.seq[a]))
	  		  @  && \invariant_for(nodes)
	  		  @  && \invariant_for(this)
	  		  @  && nodesSize == nodes.size();
			  @ decreases nodesSize - i;
			  @ assignable listHook.seq;
			  @*/
			for (int i = 0; i < nodesSize; i++) {
				JMENode n = (JMENode) nodes.get(i);
				//@ assert n != null && \invariant_for(n);
				if (n.getKind() == JMENodeKind.HOOK)
					listHook.add(n);
			}
		}
		return listHook;
	}

    /*@ public normal_behavior
      @ requires \invariant_for(this);
      @ ensures (\forall int a; 0 <= a && a < \result.seq.length;
      @           \result.seq[a] instanceof JMEArc
      @           && \invariant_for((JMEArc)\result.seq[a])
      @           && (\exists int j; 0 <= j && j < arcs.seq.length;
      @             \result.seq[a] == arcs.seq[j] && ((JMEArc)arcs.seq[j]).a == node || ((JMEArc)arcs.seq[j]).b == node))
      @  && (\forall int j; 0 <= j && j < arcs.seq.length;
      @       ((JMEArc)arcs.seq[j]).a == node || ((JMEArc)arcs.seq[j]).b == node
      @        ==> (\exists int a; 0 <= a && a < \result.seq.length;
      @             \result.seq[a] == arcs.seq[j]))
      @  && \fresh(\result);
      @ assignable \nothing;
      @*/
	public List/*<JMEArc>*/ getIncidentArcsFromNode(JMENode node) {
		ArrayList/*<JMEArc>*/ incidentArcs = new ArrayList();
        int arcsSize = arcs.size();
        /*@ loop_invariant
          @  i >= 0 && i <= arcsSize
          @   && arcsSize == arcs.size()
          @   && \invariant_for(this)
          @   && \invariant_for(incidentArcs)
          @   && \disjoint(this.footprint,incidentArcs.footprint)
          @   && (\forall int a; 0 <= a && a < incidentArcs.seq.length;
          @           incidentArcs.seq[a] instanceof JMEArc
          @           && \invariant_for((JMEArc)incidentArcs.seq[a])
          @           && (\exists int j; 0 <= j && j < arcs.seq.length;
          @             incidentArcs.seq[a] == arcs.seq[j] && ((JMEArc)arcs.seq[j]).a == node || ((JMEArc)arcs.seq[j]).b == node))
          @  && (\forall int j; 0 <= j && j < i;
          @       ((JMEArc)arcs.seq[j]).a == node || ((JMEArc)arcs.seq[j]).b == node
          @        ==> (\exists int a; 0 <= a && a < incidentArcs.seq.length;
          @             incidentArcs.seq[a] == arcs.seq[j]));
          @ decreases arcsSize - i;
          @ assignable incidentArcs.seq;
          @*/
        for (int i = 0; i < arcsSize; i++) {
            JMEArc arc = (JMEArc) arcs.get(i);
            if (arc.a == node || arc.b == node) { // Undirected graph
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
				List/*<JMEArc>*/ arcs = getIncidentArcsFromNode(cur);
				for (int i = 0; i < arcs.size(); i++) {
					JMEArc arc = (JMEArc) arcs.get(i);
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

	/*@ public normal_behavior
	  @ ensures \result == this.owner;
	  @ strictly_pure
	  @*/
	public JMERule getRule() {
		return owner;
	}

	public String getName() {
		return isleft ? "LeftGraph" : "RightGraph";
	}
}
