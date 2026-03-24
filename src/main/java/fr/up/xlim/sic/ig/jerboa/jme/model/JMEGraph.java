package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.*;

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

    /*@ accessible arcs,arcs.seq,nodes,nodes.seq;
      @ helper model public boolean arcsAreInGraph() {
        return (\forall \bigint a; 0 <= a && a < arcs.seq.length;
                 (\exists int b; 0 < b && b < nodes.seq.length; ((JMEArc)arcs.seq[a]).a == nodes.seq[b])
                 && (\exists int b; 0 < b && b < nodes.seq.length; ((JMEArc)arcs.seq[a]).b == nodes.seq[b])
               );
      }
      @*/

	protected JMERule owner;
	public final List/*<JMENode>*/ nodes;
	public final List/*<JMEArc>*/ arcs;
	protected boolean isleft;

	/*@ public normal_behavior
	  @ ensures this != null;
	  @ assignable owner,isleft,nodes,arcs;
	  @*/
	public JMEGraph(JMERule rule, boolean isleft) {
		owner = rule;

		this.isleft = isleft;
		nodes = new ArrayList();
		arcs = new ArrayList();
        //@ set footprint = \set_union(\singleton(footprint), \singleton(this.nodes.seq), (\infinite_union int i; 0<=i && i<nodes.seq.length; ((JMENode)nodes.seq[i]).footprint), \singleton(this.arcs.seq), (\infinite_union int i; 0<=i && i<arcs.seq.length; ((JMEArc)arcs.seq[i]).footprint), \singleton(this.isleft), \singleton(this.owner));
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

    /*@ requires \invariant_for(orbit);
      @ accessible p,arcs.seq,nodes.seq,node,orbit,orbit.dim[*];
      @ helper model public boolean isValidPath(\seq p, JMENode node, JerboaOrbit orbit) {
          return p.length >= 1
                && p.length <= this.nodes.seq.length
                && (\forall int i; i >= 0 && i < p.length;
                     p[i] instanceof JMENode
                     && (\exists \bigint j; 0 <= j && j < nodes.seq.length;
                          nodes.seq[j] == p[i]
                        )
                   )
                && (p[0] == node)
                && (\forall int i; i >= 0 && i < p.length - 1;
                     hasArc((JMENode)p[i], (JMENode)p[i+1], orbit)
                   )
                && (\forall int i; i >= 0 && i < p.length;
                     (\forall int j; 0 <= j && j < p.length;
                       i == j || p[i] != p[j]
                     )
                   )
        ;
      }
      @*/

    /*@ requires \invariant_for(orbit);
      @ accessible arcs.seq,orbit.dim[*];
      @ helper model public boolean hasArc(JMENode a, JMENode b, JerboaOrbit orbit) {
          return (\exists int k; 0 <= k && k < arcs.seq.length;
                    ((((JMEArc)arcs.seq[k]).a == a) && (((JMEArc)arcs.seq[k]).b == b)
                     || (((JMEArc)arcs.seq[k]).b == a) && (((JMEArc)arcs.seq[k]).a == b))
                    && ((JMEArc)arcs.seq[k]).dim >= 0 && orbit.contains(((JMEArc)arcs.seq[k]).dim)
                 );
      }
      @*/

    /*@ requires \invariant_for(orbit);
      @ accessible arcs.seq,nodes.seq,node,target,orbit,orbit.dim[*];
      @ helper model public boolean existsValidPath(JMENode node, JMENode target, JerboaOrbit orbit) {
          return (\exists \seq p;
                   isValidPath(p, node, orbit)
                   && p[p.length - 1] == target
          );
      }
      @*/

    /**
     * Given a node, calculate all the nodes reachable using arcs
     * with dimension included in the provided orbit.
     * @param node the start node
     * @param orbit orbit with allowed dimensions
     * @return set of reachable nodes
     */
    /*@ public normal_behavior
      @ requires \invariant_for(this) && this.arcsAreUnique() && this.arcsAreInGraph();
      @ requires \invariant_for(node);
      @ requires \invariant_for(orbit);
      @ requires (\exists \bigint i; 0 <= i && i < nodes.seq.length; nodes.seq[i] == node);
      @ ensures \fresh(\result) && \invariant_for(\result);
      @ ensures (\forall \bigint i; 0 <= i && i < \result.seq.length;
      @           \result.seq[i] instanceof JMENode
      @           && ((JMENode)\result.seq[i]) != null
      @         );
      @ ensures
      @   (\forall \seq p;
      @      isValidPath(p, node, orbit)
      @     ==>
      @      (\exists \bigint i; 0 <= i && i < \result.seq.length; \result.seq[i] == p[p.length - 1])
      @   )
      @   && (\forall \bigint i; 0 <= i && i < \result.seq.length;
      @     (\exists \seq p;
      @       isValidPath(p, node, orbit) && p[p.length - 1] == \result.seq[i]
      @     )
      @   )
      @ ;
      @ assignable \nothing;
      @*/
	public Set/*<JMENode>*/ orbit(JMENode node, JerboaOrbit orbit) {
		HashSet/*<JMENode>*/ visited = new HashSet();
		List/*<JMENode>*/ stack = new ArrayList();
		stack.add(node);

        int iteration = 0;
        int nodesSize = nodes.size();

        /* Loop invariant in plain English:
         * 0. Basic variable conditions
         * 1. If there is a valid path (length <= `iteration`), there is an entry in `visited`.
         * 2. If there is an entry in `visited`, there is a valid path (length <= `iteration`).
         * 3. If there is a valid path (length <= `iteration` + 1), and the target is not in `visited`,
         *    there is an entry in `stack`.
         * 4. If there is an entry in `stack`, there is a valid path (length <= `iteration` + 1),
         *    and the target is not in `visited`.
         */

        /*@ loop_invariant
          @  iteration >= 0
          @  && iteration <= nodes.seq.length
          @  && visited != stack
          @  && \invariant_for(visited)
          @  && stack != null
          @  && \disjoint(this.footprint, \set_union(\singleton(visited.seq), \singleton(stack.seq)))
          @  && \invariant_for(stack)
          @  && \invariant_for(orbit)
          @  && \invariant_for(this)
          @  && \dl_created(stack)
          @  && (\forall \bigint i; 0 <= i && i < visited.seq.length; visited.seq[i] instanceof JMENode && (JMENode)visited.seq[i] != null)
          @  && (\forall \bigint i; 0 <= i && i < stack.seq.length; stack.seq[i] instanceof JMENode && (JMENode)stack.seq[i] != null)
          @  && (\forall \seq p;
          @       (isValidPath(p, node, orbit)
          @        && p.length <= iteration
          @       ) ==> (\exists int i; 0 <= i && i < visited.seq.length; visited.seq[i] == p[p.length - 1])
          @  )
          @  && (\forall \bigint i; 0 <= i && i < visited.seq.length;
          @    (\exists \seq p;
          @      isValidPath(p, node, orbit)
          @      && p.length <= iteration
          @      && p[p.length - 1] == visited.seq[i]
          @    )
          @  )
          @  && (\forall \seq p;
          @       (isValidPath(p, node, orbit)
          @        && p.length <= iteration + 1
          @        && !(\exists int i; 0 <= i && i < visited.seq.length; visited.seq[i] == p[p.length - 1])
          @       ) ==> (
          @         (\exists int i; 0 <= i && i < stack.seq.length; stack.seq[i] == p[p.length - 1])
          @       )
          @  )
          @  && (\forall \bigint i; 0 <= i && i < stack.seq.length;
          @    (\exists \seq p;
          @      isValidPath(p, node, orbit)
          @      && p.length <= iteration + 1
          @      && p[p.length - 1] == stack.seq[i]
          @    )
          @    && !(\exists int j; 0 <= j && j < visited.seq.length; visited.seq[j] == stack.seq[i])
          @  )
          @  ;
          @ decreases this.nodes.seq.length - iteration;
          @ assignable stack,visited.seq,iteration;
          @*/
		while (!stack.isEmpty() || iteration < nodesSize) {
            List/*<JMENode>*/ newStack = orbitIteration(stack, visited, orbit);
            stack = newStack;
            iteration++;
		} // while the search continue

        //@ assert (\forall \bigint i; 0 <= i && i < visited.seq.length;
        //@     (\exists \seq p;
        //@       isValidPath(p, node, orbit) && p[p.length - 1] == visited.seq[i]
        //@     )
        //@   );

		return visited;
	}

    /*@ public normal_behavior
      @ requires
      @  \invariant_for(orbit)
      @  && \invariant_for(stack)
      @  && \invariant_for(visited)
      @  && \invariant_for(this) && this.arcsAreUnique() && this.arcsAreInGraph()
      @  && visited != stack
      @  && \disjoint(this.footprint, \set_union(\singleton(visited.seq), \singleton(stack.seq)))
      @  && (\forall int i; 0 <= i && i < stack.seq.length; stack.seq[i] instanceof JMENode && (JMENode)stack.seq[i] != null)
      @  && (\forall int i; 0 <= i && i < visited.seq.length; visited.seq[i] instanceof JMENode && (JMENode)visited.seq[i] != null)
      @  && (\forall int i; 0 <= i && i < stack.seq.length; !(\exists int j; 0 <= j && j < visited.seq.length; stack.seq[i] == visited.seq[j]))
      @ ;
      @ ensures
      @  \fresh(\result)
      @  && (\forall int i; 0 <= i && i < \result.seq.length; \result.seq[i] instanceof JMENode && (JMENode)\result.seq[i] != null)
      @  && \result != stack
      @  && \result != visited
      @  && \result != nodes
      @  && \invariant_for(visited)
      @  && \invariant_for(\result)
      @  && \invariant_for(this) && this.arcsAreUnique() && this.arcsAreInGraph()
      @  && (\forall int i; 0 <= i && i < visited.seq.length; visited.seq[i] instanceof JMENode && (JMENode)visited.seq[i] != null)
      @  && (\forall \bigint i; 0 <= i && i < \old(visited.seq.length); visited.seq[i] == \old(visited.seq[i]))
      @  && (\forall int i; 0 <= i && i < stack.seq.length;
      @          (\forall int j; 0 <= j && j < nodes.seq.length;
      @            (
      @              hasArc((JMENode)stack.seq[i], (JMENode)nodes.seq[j], orbit)
      @              && !(\exists \bigint k; 0 <= k && k < visited.seq.length; visited.seq[k] == nodes.seq[j])
      @            )
      @            ==>
      @            (\exists int k; 0 <= k && k < \result.seq.length; \result.seq[k] == nodes.seq[j])
      @          )
      @          && (\exists int j; \old(visited.seq.length) <= j && j < visited.seq.length; visited.seq[j] == stack.seq[i])
      @     )
      @  && (\forall \bigint j; \old(visited.seq.length) <= j && j < visited.seq.length;
      @       (\exists \bigint i; 0 <= i && i < stack.seq.length;
      @         visited.seq[j] == stack.seq[i]
      @       )
      @     )
      @  && (\forall \bigint l; 0 <= l && l < \result.seq.length;
      @      !(\exists \bigint i; 0 <= i && i < visited.seq.length; visited.seq[i] == \result.seq[l])
      @      &&
      @      (\exists int i; \old(visited.seq.length) <= i && i < visited.seq.length;
      @        (\exists int k; 0 <= k && k < nodes.seq.length;
      @          \result.seq[l] == nodes.seq[k]
      @          &&
      @          hasArc((JMENode)visited.seq[i], (JMENode)nodes.seq[k], orbit)
      @        )
      @      )
      @    )
      @  && visited.seq.length >= \old(visited.seq.length)
      @ ;
      @ assignable visited.seq;
      @*/
    private List/*<JMENode>*/ orbitIteration(List/*<JMENode>*/ stack, Set/*<JMENode>*/ visited, JerboaOrbit orbit) {
        List/*<JMENode>*/ newStack = new ArrayList();
        int stackSize = stack.size();

        /*@ loop_invariant
          @ 0 <= j && j <= stackSize
          @ && stackSize == stack.size()
          @ && (\forall \bigint i; 0 <= i && i < \pre(visited.seq.length); visited.seq[i] == \pre(visited.seq[i]))
          @ && (\forall \bigint i; 0 <= i && i < j;
          @      (\exists \bigint k; \pre(visited.seq.length) <= k && k < visited.seq.length;
          @        visited.seq[k] == stack.seq[i]
          @      )
          @ )
          @ && (\forall \bigint i; \pre(visited.seq.length) <= i && i < visited.seq.length;
          @      (\forall int k; 0 <= k && k < nodes.seq.length;
          @        hasArc((JMENode)visited.seq[i], (JMENode)nodes.seq[k], orbit)
          @         ==>
          @        (\exists int l; 0 <= l && l < newStack.seq.length; newStack.seq[l] == nodes.seq[k])
          @      )
          @      && (\exists \bigint k; 0 <= k && k < stack.seq.length;
          @           stack.seq[k] == visited.seq[i]
          @      )
          @ )
          @ && (\forall \bigint l; 0 <= l && l < newStack.seq.length;
          @      (\exists int i; \pre(visited.seq.length) <= i && i < visited.seq.length;
          @        (\exists int k; 0 <= k && k < nodes.seq.length;
          @          newStack.seq[l] == nodes.seq[k]
          @          &&
          @          hasArc((JMENode)visited.seq[i], (JMENode)nodes.seq[k], orbit)
          @        )
          @      )
          @    )
          @ && (\forall int i; 0 <= i && i < visited.seq.length; visited.seq[i] instanceof JMENode && (JMENode)visited.seq[i] != null)
          @ && (\forall int i; 0 <= i && i < newStack.seq.length; newStack.seq[i] instanceof JMENode && (JMENode)newStack.seq[i] != null)
          @ && \invariant_for(visited)
          @ && \invariant_for(newStack)
          @ && \invariant_for(this)
          @ && \invariant_for(stack)
          @ && visited != newStack
          @ && visited != stack
          @ && newStack != nodes
          @ && visited != nodes
          @ && \disjoint(this.footprint, \set_union(\singleton(stack.seq), \set_union(\singleton(visited.seq), \singleton(newStack.seq))))
          @ && visited.seq.length >= \pre(visited.seq.length)
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
      @ requires \invariant_for(this) && this.arcsAreUnique() && this.arcsAreInGraph();
      @ requires \invariant_for(orbit);
      @ requires \invariant_for(newStack);
      @ requires \disjoint(this.footprint, \singleton(newStack.seq));
      @ requires (\forall int i; 0 <= i && i < newStack.seq.length; newStack.seq[i] instanceof JMENode && (JMENode)newStack.seq[i] != null);
      @ ensures
      @  \invariant_for(newStack)
      @  && \disjoint(this.footprint, \singleton(newStack.seq))
      @  && (\forall \bigint i; 0 <= i && i < newStack.seq.length; newStack.seq[i] instanceof JMENode && (JMENode)newStack.seq[i] != null)
      @  && (\forall \bigint i; 0 <= i && i < \old(newStack.seq.length); newStack.seq[i] == \old(newStack.seq[i]))
      @  && (\forall \bigint i; 0 <= i && i < nodes.seq.length;
      @       hasArc(cur, (JMENode)nodes.seq[i], orbit)
      @        ==>
      @       (\exists \bigint j; \old(newStack.seq.length) <= j && j < newStack.seq.length; newStack.seq[j] == nodes.seq[i])
      @  )
      @  && (\forall \bigint j; \old(newStack.seq.length) <= j && j < newStack.seq.length;
      @       (\exists \bigint i; 0 <= i && i < nodes.seq.length;
      @         newStack.seq[j] == nodes.seq[i]
      @         && hasArc(cur, (JMENode)nodes.seq[i], orbit)
      @       )
      @     )
      @  && newStack.seq.length >= \old(newStack.seq.length)
      @ ;
      @ assignable newStack.seq;
      @*/
    private void orbitIterationPart(JMENode cur, JerboaOrbit orbit, List/*<JMENode>*/ newStack) {
        //@ ghost \seq oldNewStack = newStack.seq;

        List/*<JMEArc>*/ arcsIncident = getIncidentArcsFromNode(cur);
        int arcsIncidentSize = arcsIncident.size();
        /*@ loop_invariant
          @ 0 <= i && i <= arcsIncidentSize
          @ && \disjoint(this.footprint, \singleton(newStack.seq))
          @ && (\forall int i; 0 <= i && i < newStack.seq.length; newStack.seq[i] instanceof JMENode && (JMENode)newStack.seq[i] != null)
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
          @       (\exists \bigint i; 0 <= i && i < nodes.seq.length;
          @         newStack.seq[j] == nodes.seq[i]
          @         && hasArc(cur, (JMENode)nodes.seq[i], orbit)
          @       )
          @     )
          @ && arcsIncidentSize == arcsIncident.size()
          @ && \invariant_for(newStack)
          @ && newStack.seq.length >= oldNewStack.length
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
      @   && \invariant_for(newStack)
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
