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
	//@ public invariant footprint == \set_union(\singleton(footprint), \singleton(this.nodes.seq), (\infinite_union int i; 0<=i && i<nodes.seq.length; ((JMENode)nodes.seq[i]).footprint), \singleton(this.arcs.seq), (\infinite_union int i; 0<=i && i<arcs.seq.length; ((JMEArc)arcs.seq[i]).footprint), \singleton(this.isleft), \singleton(this.owner));

	 /*@ requires \invariant_for(this);
	   @ accessible nodes,nodes.seq,
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
	  @ accessible arcs,arcs.seq;
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

    /*@ accessible nodes,nodes.seq;
      @ helper model public boolean nodesAreUnique() {
        return !(\exists int a; 0 <= a && a < nodes.seq.length; (\exists int b; a < b && b < nodes.seq.length; nodes.seq[a] == nodes.seq[b]));
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
	  @ requires this.isleft && this.nodesAreUnique();
	  @ ensures (\forall int i; 0 <= i && i < this.nodes.seq.length;
	  @  (((JMENode)this.nodes.seq[i]).kind == JMENodeKind.HOOK)
	  @    ==> (\exists int j; 0 <= j && j < \result.seq.length; \result.seq[j] == this.nodes.seq[i]))
	  @  && (\forall int a; 0 <= a && a < \result.seq.length;
	  @       (\exists int b; 0 <= b && b < this.nodes.seq.length;
	  @         \result.seq[a] == this.nodes.seq[b] && (((JMENode)this.nodes.seq[b]).kind == JMENodeKind.HOOK)))
	  @  && (\forall int a; 0 <= a && a < \result.seq.length; \result.seq[a] instanceof JMENode && \invariant_for((JMENode)\result.seq[a]))
	  @  && !(\exists \bigint i; 0 <= i && i < \result.seq.length;
	  @        (\exists \bigint j; i < j && j < \result.seq.length;
	  @          \result.seq[i] == \result.seq[j]
	  @        )
	  @      )
	  @  && \invariant_for(\result)
	  @  && \fresh(\result)
	  @ ;
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
	  		  @       (\exists int b; 0 <= b && b < i;
	  		  @         listHook.seq[a] == this.nodes.seq[b] && ((JMENode)this.nodes.seq[b]).kind == JMENodeKind.HOOK))
	  		  @  && (\forall int a; 0 <= a && a < listHook.seq.length; listHook.seq[a] instanceof JMENode && \invariant_for((JMENode)listHook.seq[a]))
	  		  @  && !(\exists \bigint a; 0 <= a && a < listHook.seq.length;
	          @        (\exists \bigint b; a < b && b < listHook.seq.length;
	          @          listHook.seq[a] == listHook.seq[b]
	          @        )
	          @     )
	  		  @  && \invariant_for(nodes)
	  		  @  && \invariant_for(this)
	  		  @  && this.nodesAreUnique()
	  		  @  && \invariant_for(listHook)
	  		  @  && \disjoint(this.footprint, \singleton(listHook.seq))
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
      @  && \fresh(\result)
      @  && \invariant_for(\result);
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
          @   && \disjoint(this.footprint, \singleton(incidentArcs.seq))
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
      @ requires
      @  \invariant_for(orbit)
      @  && \invariant_for(stack)
      @  && \invariant_for(visited)
      @  && \invariant_for(this) && this.arcsAreUnique()
      @  && visited.seq != stack.seq
      @  && (\forall int i; 0 <= i && i < stack.seq.length; stack.seq[i] instanceof JMENode)
      @  && (\forall int i; 0 <= i && i < visited.seq.length; visited.seq[i] instanceof JMENode)
      @ ;
      @ ensures
      @  \fresh(\result)
      @  && \result.seq != stack.seq
      @  && \result.seq != visited.seq
      @  && (\forall int i; 0 <= i && i < visited.seq.length; visited.seq[i] instanceof JMENode)
      @  && (\forall \bigint i; 0 <= i && i < \old(visited).seq.length; visited.seq[i] == \old(visited).seq[i])
      @  && (\forall int i; 0 <= i && i < stack.seq.length;
      @      !(\exists int j; 0 <= j && j < \old(visited).seq.length; \old(visited).seq[j] == stack.seq[i])
      @      ==> (\forall int j; 0 <= j && j < nodes.seq.length;
      @            hasArc((JMENode)stack.seq[i], (JMENode)nodes.seq[j], orbit)
      @            ==>
      @            (\exists int k; 0 <= k && k < \result.seq.length; \result.seq[k] == nodes.seq[j])
      @          )
      @          && (\exists int j; 0 <= j && j < visited.seq.length; visited.seq[j] == stack.seq[i])
      @     )
      @ ;
      @ assignable visited.seq;
      @*/
    private List/*<JMENode>*/ orbitIteration(List/*<JMENode>*/ stack, Set/*<JMENode>*/ visited, JerboaOrbit orbit) {
        List/*<JMENode>*/ newStack = new ArrayList();
        int stackSize = stack.size();

        //@ ghost \seq oldVisited = visited.seq;

        /*@ loop_invariant
          @ 0 <= j && j <= stackSize
          @ && stackSize == stack.size()
          @ && (\forall \bigint i; 0 <= i && i < j;
          @      (\exists \bigint k; 0 <= k && k < visited.seq.length;
          @        visited.seq[k] == stack.seq[i]
          @      )
          @ )
          @ && (\forall \bigint i; oldVisited.length <= i && i < visited.seq.length;
          @      (\forall int k; 0 <= k && k < nodes.seq.length;
          @        hasArc((JMENode)visited.seq[i], (JMENode)nodes.seq[k], orbit)
          @         ==>
          @        (\exists int l; 0 <= l && l < newStack.seq.length; newStack.seq[l] == nodes.seq[k])
          @      )
          @      && (\exists \bigint k; 0 <= k && k < stack.seq.length;
          @           stack.seq[k] == visited.seq[i]
          @      )
          @ )
          @ && (\forall int i; 0 <= i && i < visited.seq.length; visited.seq[i] instanceof JMENode)
          @ && (\forall int i; 0 <= i && i < newStack.seq.length; newStack.seq[i] instanceof JMENode)
          @ && \invariant_for(visited)
          @ && \invariant_for(newStack)
          @ && \invariant_for(this)
          @ && visited.seq != newStack.seq
          @ ;
          @ decreases stackSize - j;
          @ assignable visited.seq, newStack.seq;
          @*/
        for (int j = 0; j < stackSize; j++) {
            JMENode cur = (JMENode) stack.get(j);
            if (!visited.contains(cur)) {
                visited.add(cur);
                orbitIterationPart(cur, orbit, newStack);
            } // for all incident arcs
        } // if node is still not visited
        // NEW: remove all already visited nodes from newStack
        removeAll(newStack, visited);
        return newStack;
    }

    /*@ public normal_behavior
      @ requires \invariant_for(this) && this.arcsAreUnique();
      @ requires \invariant_for(orbit);
      @ requires \invariant_for(newStack);
      @ ensures
      @  \invariant_for(newStack)
      @  && (\forall \bigint i; 0 <= i && i < \old(newStack.seq.length); newStack.seq[i] == \old(newStack.seq)[i])
      @  && (\forall \bigint i; 0 <= i && i < nodes.seq.length;
      @       hasArc(cur, (JMENode)nodes.seq[i], orbit)
      @        ==>
      @       (\exists \bigint j; 0 <= j && j < newStack.seq.length; newStack.seq[j] == nodes.seq[i])
      @  )
      @ ;
      @ assignable newStack.seq;
      @*/
    private void orbitIterationPart(JMENode cur, JerboaOrbit orbit, List/*<JMENode>*/ newStack) {
        //@ ghost \seq oldNewStack = newStack.seq;

        List/*<JMEArc>*/ arcsIncident = getIncidentArcsFromNode(cur);
        int arcsIncidentSize = arcsIncident.size();
        /*@ loop_invariant
          @ 0 <= i && i <= arcsIncidentSize
          @ && (\forall \bigint j; 0 <= j && j < oldNewStack.length; newStack.seq[j] == oldNewStack[j])
          @ && (\forall \bigint j; 0 <= j && j < i;
          @      (((JMEArc)arcsIncident.seq[j]).dim >= 0 && orbit.contains(((JMEArc)arcsIncident.seq[j]).dim))
          @       ==>
          @      (
          @       (
          @        ((JMEArc)arcsIncident.seq[j]).a == cur
          @         ==>
          @        (\exists \bigint k; oldNewStack.length <= k && k < newStack.seq.length; newStack.seq[k] == ((JMEArc)arcsIncident.seq[j]).b)
          @       )
          @       && (
          @        ((JMEArc)arcsIncident.seq[j]).b == cur
          @         ==>
          @        (\exists \bigint k; oldNewStack.length <= k && k < newStack.seq.length; newStack.seq[k] == ((JMEArc)arcsIncident.seq[j]).a)
          @       )
          @      )
          @ )
          @ && (\forall \bigint j; oldNewStack.length <= j && j < newStack.seq.length;
          @      (\exists \bigint k; 0 <= k && k < arcsIncident.seq.length;
          @        ((JMEArc)arcsIncident.seq[k]).dim >= 0
          @        && orbit.contains(((JMEArc)arcsIncident.seq[k]).dim)
          @        && (
          @             (((JMEArc)arcsIncident.seq[k]).a == cur && newStack.seq[j] == ((JMEArc)arcsIncident.seq[k]).b)
          @             || (((JMEArc)arcsIncident.seq[k]).b == cur && newStack.seq[j] == ((JMEArc)arcsIncident.seq[k]).a)
          @           )
          @      )
          @ )
          @ && arcsIncidentSize == arcsIncident.size()
          @ && \invariant_for(newStack)
          @ ;
          @ decreases arcsIncidentSize - i;
          @ assignable newStack.seq;
          @*/
        for (int i = 0; i < arcsIncidentSize; i++) {
            JMEArc arc = (JMEArc) arcsIncident.get(i);
            if (arc.dim >= 0 && orbit.contains(arc.dim)) {
                JMENode otherNode = arc.oppositeNode(cur);
                // removed: if (!visited.contains(otherNode))
                newStack.add(otherNode);
            } // end dimension ok
        }
    }

    /**
     * Remove all elements of <code>newStack</code> that are also
     * present in <code>visited</code>.
     *
     * @param newStack list of objects
     * @param visited elements to remove
     */
    /*@ public normal_behavior
      @ requires \invariant_for(newStack)
      @  && \invariant_for(visited)
      @  && newStack != visited
      @  && (\forall \bigint i; 0 <= i && i < newStack.seq.length;
      @       newStack.seq[i] instanceof Object
      @     )
      @  && (\forall \bigint i; 0 <= i && i < visited.seq.length;
      @       visited.seq[i] instanceof Object
      @     )
      @ ;
      @ ensures
      @   (\forall \bigint i; 0 <= i && i < \old(newStack.seq.length);
      @     !(\exists \bigint j; 0 <= j && j < visited.seq.length; visited.seq[j] == \old(newStack.seq[i]))
      @     ==>
      @     (\exists \bigint k; 0 <= k && k < newStack.seq.length; \old(newStack.seq[i]) == newStack.seq[k])
      @   )
      @   && (\forall \bigint i; 0 <= i && i < newStack.seq.length;
      @        (\exists \bigint j; 0 <= j && j < \old(newStack.seq.length); \old(newStack.seq[j]) == newStack.seq[i])
      @        && !(\exists \bigint j; 0 <= j && j < visited.seq.length; visited.seq[j] == newStack.seq[i])
      @      )
      @   && newStack.seq.length <= \old(newStack.seq.length)
      @ ;
      @ assignable newStack.seq;
      @*/
    static void removeAll(List newStack, Collection visited) {
        int newStackSize = newStack.size();

        //@ ghost int originalSize;
        //@ set originalSize = newStackSize;
        //@ ghost int iter;
        //@ set iter = 0;
        //@ ghost int removed;
        //@ set removed = 0;

        /*@ loop_invariant
          @ i >= 0
          @ && i <= newStackSize
          @ && iter >= 0
          @ && iter <= originalSize
          @ && newStackSize == newStack.size()
          @ && newStackSize <= originalSize
          @ && \invariant_for(newStack)
          @ && \invariant_for(visited)
          @ && newStack != visited
          @ && (\forall \bigint j; 0 <= j && j < i;
          @      !(\exists \bigint k; 0 <= k && k < visited.seq.length;
          @         visited.seq[k] == newStack.seq[j]
          @       )
          @      && (\exists \bigint k; 0 <= k && k < iter;
          @           newStack.seq[j] == \pre(newStack.seq[k])
          @         )
          @    )
          @ && (\forall \bigint j; 0 <= j && j < iter;
          @      !(\exists \bigint k; 0 <= k && k < visited.seq.length;
          @         visited.seq[k] == \pre(newStack.seq[j])
          @      )
          @      ==>
          @      (\exists \bigint k; 0 <= k && k < i;
          @         newStack.seq[k] == \pre(newStack.seq[j])
          @      )
          @    )
          @ && 0 <= removed
          @ && removed <= originalSize
          @ && iter - removed == i
          @ && newStack.seq.length == \pre(newStack.seq.length) - removed
          @ && newStack.seq[i .. newStack.seq.length] == \pre(newStack.seq)[iter .. \pre(newStack.seq.length)]
          @ && (\forall \bigint i; 0 <= i && i < newStack.seq.length;
          @      newStack.seq[i] instanceof Object
          @    )
          @ ;
          @ decreases originalSize - iter;
          @ assignable newStackSize,newStack.seq,iter,removed;
          @*/
        for (int i = 0; i < newStackSize; i++) {
            Object node = newStack.get(i);
            if (visited.contains(node)) {
                newStack.remove(i);
                newStackSize -= 1;
                i -= 1;
                //@ set removed = removed + 1;
            }
            //@ set iter = iter + 1;
        }
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
