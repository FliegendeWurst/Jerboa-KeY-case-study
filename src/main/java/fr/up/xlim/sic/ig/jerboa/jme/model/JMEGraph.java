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
	//@ public invariant footprint == \set_union(\singleton(footprint), this.nodes.footprint, (\infinite_union int i; 0<=i && i<nodes.seq.length; ((JMENode)nodes.seq[i]).footprint), this.arcs.footprint, (\infinite_union int i; 0<=i && i<arcs.seq.length; ((JMEArc)arcs.seq[i]).footprint), \singleton(this.isleft), \singleton(this.owner));

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

    /*@ accessible arcs,arcs.seq;
      @ helper model public boolean arcsAreUnique() {
        return !(\exists int a; 0 <= a && a < arcs.seq.length; (\exists int b; a < b && b < arcs.seq.length; arcs.seq[a] == arcs.seq[b]));
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
	  		  @  && \disjoint(nodes.footprint, listHook.footprint)
	  		  @  && \disjoint(this.footprint, listHook.footprint)
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
      @ requires \invariant_for(this) && this.arcsAreUnique();
      @ ensures (\forall int a; 0 <= a && a < \result.seq.length;
      @           \result.seq[a] instanceof JMEArc
      @           && \invariant_for((JMEArc)\result.seq[a])
      @           && (\exists int j; 0 <= j && j < arcs.seq.length;
      @             \result.seq[a] == arcs.seq[j] && (((JMEArc)arcs.seq[j]).a == node || ((JMEArc)arcs.seq[j]).b == node)))
      @  && (\forall int j; 0 <= j && j < arcs.seq.length;
      @       ((JMEArc)arcs.seq[j]).a == node || ((JMEArc)arcs.seq[j]).b == node
      @        ==> (\exists int a; 0 <= a && a < \result.seq.length;
      @             \result.seq[a] == arcs.seq[j]))
      @  && !(\exists int a; 0 <= a && a < \result.seq.length; (\exists int b; a < b && b < \result.seq.length; \result.seq[a] == \result.seq[b]))
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
          @           && (\exists int j; 0 <= j && j < i;
          @             incidentArcs.seq[a] == arcs.seq[j] && (((JMEArc)arcs.seq[j]).a == node || ((JMEArc)arcs.seq[j]).b == node)))
          @  && (\forall int j; 0 <= j && j < i;
          @       ((JMEArc)arcs.seq[j]).a == node || ((JMEArc)arcs.seq[j]).b == node
          @        ==> (\exists int a; 0 <= a && a < incidentArcs.seq.length;
          @             incidentArcs.seq[a] == arcs.seq[j]))
          @  && !(\exists int a; 0 <= a && a < incidentArcs.seq.length; (\exists int b; a < b && b < incidentArcs.seq.length; incidentArcs.seq[a] == incidentArcs.seq[b]))
          @  ;
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

    /**
     * Given a node, calculate all the nodes reachable using arcs
     * with dimension included in the provided orbit.
     * @param node the start node
     * @param orbit orbit with allowed dimensions
     * @return set of reachable nodes
     */
    /*@ public normal_behavior
      @ requires \invariant_for(this);
      @ requires \invariant_for(node);
      @ requires \invariant_for(orbit);
      @ ensures \fresh(\result);
      @*/
	public Set/*<JMENode>*/ orbit(JMENode node, JerboaOrbit orbit) {
		HashSet/*<JMENode>*/ visited = new HashSet();
		List/*<JMENode>*/ stack = new ArrayList();
		stack.add(node);
        //@ ghost int iteration;
        //@ set iteration = 0;

        /* loop_invariant
          @  (\forall \seq p;
          @     (\dl_seqLen(p) <= iteration
          @      && (\forall int i; i >= 0 && i < \dl_seqLen(p); p[i] instanceof JMENode)
          @      && (\forall int i; i >= 0 && i < \dl_seqLen(p) - 1;
          @           (\exists int j; ((JMEArc)arcs.seq[i]).a == p[i] && ((JMEArc)arcs.seq[i]).b == p[i+1])
          @                            || ((JMEArc)arcs.seq[i]).b == p[i] && ((JMEArc)arcs.seq[i]).a == p[i+1])
          @            )
          @     ) ==> true
          @  );
          @ decreases this.nodes.seq.length - iteration;
          @*/
		while (!stack.isEmpty()) {
            //@ set iteration = iteration + 1;
            List/*<JMENode>*/ newStack = new ArrayList();
            int stackSize = stack.size();
            for (int j = 0; j < stackSize; j++) {
                JMENode cur = (JMENode) stack.get(j);
                if (!visited.contains(cur)) {
                    visited.add(cur);
                    // TODO: move below in separate function
                    List/*<JMEArc>*/ arcsIncident = getIncidentArcsFromNode(cur);
                    for (int i = 0; i < arcsIncident.size(); i++) {
                        JMEArc arc = (JMEArc) arcsIncident.get(i);
                        if (arc.dim >= 0 && orbit.contains(arc.dim)) {
                            JMENode otherNode = null;
                            if (arc.getSource() == cur)
                                otherNode = arc.getDestination();
                            else if (arc.getDestination() == cur)
                                otherNode = arc.getSource();
                            if (otherNode != null && !visited.contains(otherNode))
                                newStack.add(otherNode);
                        } // end dimension ok
                    }
                } // for all incident arcs
			} // if node is still not visited
            stack = newStack;
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
