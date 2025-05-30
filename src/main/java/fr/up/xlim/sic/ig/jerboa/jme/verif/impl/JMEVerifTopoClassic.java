package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMELoop;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMERuleError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMERuleErrorSeverity;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMERuleErrorType;
import up.jerboa.core.JerboaOrbit;

// Topological verification
public final class JMEVerifTopoClassic {

	public JMERuleError check(JMERule rule) {
        JMERuleError error = verifDimension(rule);
        if (error != null) {
            return error;
        }
        error = verifDuplicateNode(rule);
        if (error != null) {
            return error;
        }
        error = verifHooks(rule);
        if (error != null) {
            return error;
        }
        error = verifNodeOrbitSizes(rule);
        if (error != null) {
            return error;
        }
        error = verifDuplicateDimension(rule);
        if (error != null) {
            return error;
        }
        error = verifIncidentArc(rule);
        if (error != null) {
            return error;
        }
        error = verifCycle(rule);
        if (error != null) {
            return error;
        }
        
		return null;
	}

	
	/**
	 * Checks if the dimensions are between:
	 *      -1 and the modeler's dimension for orbits;
	 *       0 and the modeler's dimension for arcs.
	 * 
	 * For both the left and right graphs, it traverses the nodes and verifies the dimension in the orbit for each node.
	 * Then, it traverses the arcs and verifies the dimension for each arc.
	 * 
	 * @param rule Rule to verify.
	 * @return non-null error if check fails
	 */
    JMERuleError verifDimension(JMERule rule) {
        int modDim = rule.getModeler().getDimension();

        // Left Graph
        JMEGraph left = rule.getLeft();
        JMERuleError error = verifDimensionGraph(rule, modDim, left);
        if (error != null) {
            return error;
        }

        // Right Graph
        JMEGraph right = rule.getRight();
        verifDimensionGraph(rule, modDim, right);
        if (error != null) {
            return error;
        }

        return null;
    }

    /*@ public normal_behavior
      @ requires \invariant_for(graph) && \invariant_for(rule);
      @ requires rule.left == graph || rule.right == graph;
      @ ensures (!graph.verifyDimensionsNodes(modDim)) ==> (\result != null);
      @ ensures (!graph.verifyDimensionsArcs(modDim)) ==> (\result != null);
      @ pure
      @*/
    JMERuleError verifDimensionGraph(JMERule rule, int modDim, JMEGraph graph) {
        // Orbits
        List/*<JMENode>*/ leftNodes = graph.getNodes();
        int leftNodesSize = leftNodes.size();
        /*@ loop_invariant
          @ 0 <= j && j <= leftNodesSize
          @  && leftNodesSize == leftNodes.size()
          @  && (\forall int a; 0 <= a && a < j;
          @        (\forall int b; 0 <= b && b < ((JMENode)(rule.left.nodes).get(a)).orbit.dim.length;
          @           ((JMENode)(rule.left.nodes).get(a)).orbit.dim[b] >= -1 ));
          @ assignable \nothing;
          @ maintaining \invariant_for(graph) && \invariant_for(rule) && graph != null && rule != null
          @  && (rule.left == graph || rule.right == graph);
          @ decreases leftNodesSize - j;
          @*/
        for (int j = 0; j < leftNodesSize; j++) {
            JMENode node = (JMENode) leftNodes.get(j);
            int[] tab = node.getOrbit().tab();
            /*@ loop_invariant
              @ 0 <= j && j < leftNodes.size()
              @  && (\forall int a; 0 <= a && a < j;
              @        (\forall int b; 0 <= b && b < ((JMENode)(rule.left.nodes).get(a)).orbit.dim.length;
              @          ((JMENode)(rule.left.nodes).get(a)).orbit.dim[b] >= -1 ))
              @  && 0 <= k && k <= ((JMENode)(rule.left.nodes).get(j)).orbit.dim.length
              @   && (\forall int c; 0 <= c && c < k;
              @          ((JMENode)(rule.left.nodes).get(j)).orbit.dim[c] >= -1 );
              @ assignable \nothing;
              @ maintaining \invariant_for(graph) && \invariant_for(rule) && graph != null && rule != null
              @  && (rule.left == graph || rule.right == graph);
              @ decreases tab.length - k;
              @*/
            for (int k = 0; k < tab.length; k++) {
                int i = tab[k];
                if (i > modDim || i < -1)
                    return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            }
        }

        // Arcs
        List/*<JMEArc>*/ leftArcs = graph.getArcs();
        for (int i = 0; i < leftArcs.size(); i++) {
            JMEArc arc = (JMEArc) leftArcs.get(i);
            if (arc.getDimension() > modDim || arc.getDimension() < 0) {
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, arc);
            }
        }

        return null;
    }

	/**
	 * Checks that there are no duplicate node names in each graph.
	 * 
	 * A HashMap is used to reference node names. When a name is already present, an error is added for both nodes.
	 * 
	 * @param rule Rule to verify.
	 * @return non-null error if check fails
	 */
    JMERuleError verifDuplicateNode(JMERule rule) {

        // Left Graph
        JMEGraph left = rule.getLeft();
        HashMap/*<String, JMENode>*/ existingNamesLeft = new HashMap();
        List/*<JMENode>*/ leftNodes = left.getNodes();
        for (int i = 0; i < leftNodes.size(); i++) {
            JMENode node = (JMENode) leftNodes.get(i);

            // The name is already present, we add errors.
            if (existingNamesLeft.containsKey(node.getName())) {
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            }

            // The name is not present, we add it to the HashMap.
            else
                existingNamesLeft.put(node.getName(), node);
        }

        // Right Graph
        JMEGraph right = rule.getRight();
        HashMap/*<String, JMENode>*/ existingNamesRight = new HashMap();
        List/*<JMENode>*/ rightNodes = right.getNodes();
        for (int i = 0; i < rightNodes.size(); i++) {
            JMENode node = (JMENode) rightNodes.get(i);

            // The name is already present, we add errors.
            if (existingNamesRight.containsKey(node.getName())) {
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            }

            // The name is not present, we add it to the HashMap.
            else
                existingNamesRight.put(node.getName(), node);
        }

        return null;
    }
	
	/**
	 * Checks that:
	 * - all nodes in the left graph are connected to a hook;
	 * - each hook has a full orbit, meaning no dimension is empty;
	 * - no hook is connected to another hook.
	 * 
	 * @param rule Rule to verify.
	 * @return non-null error if check fails
	 */
    JMERuleError verifHooks(JMERule rule) {
        List/*<JMENode>*/ hooks = rule.getHooks();
        JMEGraph left = rule.getLeft();

        // Verification of full orbit for hooks
        for (int i = 0; i < hooks.size(); i++) {
            JMENode hook = (JMENode) hooks.get(i);
            if (hook.getOrbit().contains(-1)) {
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, hook);
            }
        }

        // Preparation of the connected component orbit for the chosen dimension
        int[] tabOrbitConnexComponent = new int[rule.getModeler().getDimension() + 1];
        for (int i = 0; i < tabOrbitConnexComponent.length; i++)
            tabOrbitConnexComponent[i] = i;
        JerboaOrbit orbitCC = new JerboaOrbit(tabOrbitConnexComponent);

        // Processing each node
        List/*<JMENode>*/ leftNodes = left.getNodes();
        for (int i = 0; i < leftNodes.size(); i++) {
            JMENode node = (JMENode) leftNodes.get(i);
            Set/*<JMENode>*/ nodes = left.orbit(node, orbitCC);
            int countHook = intersectionSize(nodes, hooks);
            if (countHook > 1) {
                if (hooks.contains(node))
                    return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            } else if (countHook == 0) {
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            }
        }

        return null;
    }

    private static int intersectionSize(Set a, List b) {
        int count = 0;
        for (Object x : a) {
            if (b.contains(x)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks that all orbits are of the same size. A hook is used as a reference,
     * if possible.
     * 
     * First, a reference node is found (preferably a hook in the left graph,
     * otherwise any node in the right graph).
     * Then, for each node in both graphs, the orbit length is compared to the
     * reference node's orbit length.
     * 
     * @param rule   Rule to verify.
     * @return non-null error if check fails
     */
    JMERuleError verifNodeOrbitSizes(JMERule rule) {
        int length;
        JMENode node = null;
        if (rule.getHooks().isEmpty()) {
            if (rule.getRight().getNodes().isEmpty())
                return null;
            else
                node = (JMENode) rule.getRight().getNodes().get(0);
        } else
            node = rule.getHooks().get(0);
        length = node.getOrbit().size();

        // Left graph
        List/*<JMENode>*/ leftNodes = rule.getLeft().getNodes();
        for (int i = 0; i < leftNodes.size(); i++) {
            JMENode n = (JMENode) leftNodes.get(i);

            // Orbit too small
            if (n.getOrbit().size() < length)
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, n);

                // Orbit too large
            else if (n.getOrbit().size() > length)
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, n);
        }
        // Right graph
        List/*<JMENode>*/ rightNodes = rule.getRight().getNodes();
        for (int i = 0; i < rightNodes.size(); i++) {
            JMENode n = (JMENode) rightNodes.get(i);

            // Orbit too small
            if (n.getOrbit().size() < length)
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, n);

                // Orbit too large
            else if (n.getOrbit().size() > length)
                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, n);
        }

        return null;
    }

	/**
	 * Checks that a dimension does not appear twice on a node (in the orbit and in the arcs).
	 * 
	 * For each node, a HashSet is used to store encountered dimensions in the orbit and during arc traversal.
	 * If a dimension other than -1 is already present, an error is added to the list.
	 * 
	 * @param rule Rule to verify.
	 * @return non-null error if check fails
	 */
    JMERuleError verifDuplicateDimension(JMERule rule) {

        // Left Graph
        List/*<JMENode>*/ leftNodes = rule.getLeft().getNodes();
        for (int j = 0; j < leftNodes.size(); j++) {
            JMENode node = (JMENode) leftNodes.get(j);
            HashSet/*<Integer>*/ dims = new HashSet();

            // Orbit
            for (int i : node.getOrbit()) {
                if (!dims.add(i) && i != -1)
                    return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            }

            // Arcs
            List/*<JMEArc>*/ incidentArcsFromNode = rule.getLeft().getIncidentArcsFromNode(node);
            for (int i = 0; i < incidentArcsFromNode.size(); i++) {
                JMEArc a = (JMEArc) incidentArcsFromNode.get(i);
                if (!dims.add(a.getDimension()))
                    return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            }
        }

        // Right Graph
        List/*<JMENode>*/ rightNodes = rule.getRight().getNodes();
        for (int j = 0; j < rightNodes.size(); j++) {
            JMENode node = (JMENode) rightNodes.get(j);
            HashSet/*<Integer>*/ dims = new HashSet();

            // Orbit
            for (int i : node.getOrbit()) {
                if (!dims.add(i) && i != -1)
                    return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            }

            // Arcs
            List/*<JMEArc>*/ incidentArcsFromNode = rule.getRight().getIncidentArcsFromNode(node);
            for (int i = 0; i < incidentArcsFromNode.size(); i++) {
                JMEArc a = (JMEArc) incidentArcsFromNode.get(i);
                if (!dims.add(a.getDimension()))
                    return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node);
            }
        }

        return null;
    }
	
	/**
	 * Checks the incident arc constraint on the rule:
	 * - a node removed from the left graph must have all dimensions (in its orbit or arcs);
	 * - a node preserved by the rule must have the same dimensions in the left graph as in the right graph;
	 * - a node added to the right graph must have all dimensions (in its orbit or arcs).
	 * 
	 * For a node added or removed, it verifies that all dimensions between 0 and the modeler's dimension not present
     * in the orbit are present in the arcs.
	 * 
	 * For a node preserved by the rule, it verifies in both directions:
	 * - that a dimension present in the orbit is:
	 *   - either -1;
	 *   - present at the same index in the orbit of the corresponding node in the other graph;
	 *   - present in an arc connected to the corresponding node in the other graph.
	 * 
	 * - that a dimension present in an arc connected to the node is:
	 *   - present in the orbit of the corresponding node in the other graph;
	 *   - present in an arc connected to the corresponding node in the other graph.
	 * 
	 * @param rule Rule to verify.
	 * @return non-null error if check fails
	 */
    JMERuleError verifIncidentArc(JMERule rule) {
        int modDim = rule.getModeler().getDimension();
        JMEGraph left = rule.getLeft();
        JMEGraph right = rule.getRight();
        List/*<JMENode>*/ leftNodes = left.getNodes();
        for (int k = 0; k < leftNodes.size(); k++) {
            JMENode leftNode = (JMENode) leftNodes.get(k);
            JMENode rightNode = right.getMatchNode(leftNode);

            // Node removed: we verify that it has an incident arc for all dimensions
            if (rightNode == null) {
                List/*<JMEArc>*/ incidents = left.getIncidentArcsFromNode(leftNode);
                for (int i = 0; i <= modDim; i++) {
                    if (!leftNode.getOrbit().contains(i)) {
                        boolean flag = false;
                        for (int j = 0; j < incidents.size(); j++) {
                            JMEArc a = (JMEArc) incidents.get(j);
                            flag |= a.getDimension() == i;
                        }
                        if (!flag)
                            return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule,
                                    leftNode);
                    }
                }
            } else {

                // Left -> Right
                // Dimensions in the orbit
                for (int i = 0; i < leftNode.getOrbit().size(); i++) {
                    boolean flag = false;
                    int dim = leftNode.getOrbit().get(i);

                    // Test for equality to -1
                    flag |= dim == -1;

                    // Test for identical position in the orbit
                    flag |= (rightNode.getOrbit().contains(dim))
                            && (dim == rightNode.getOrbit().get(i));

                    // Test for arcs
                    List/*<JMEArc>*/ incidentArcsFromNode = rule.getRight().getIncidentArcsFromNode(rightNode);
                    for (int j = 0; j < incidentArcsFromNode.size(); j++) {
                        JMEArc a = (JMEArc) incidentArcsFromNode.get(j);
                        flag |= (dim == a.getDimension());
                    }
                    if (!flag)
                        return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule,
                                leftNode);
                }

                // Dimensions in the arcs
                List/*<JMEArc>*/ incidentArcsFromNode = rule.getLeft().getIncidentArcsFromNode(leftNode);
                for (int i = 0; i < incidentArcsFromNode.size(); i++) {
                    JMEArc arc = (JMEArc) incidentArcsFromNode.get(i);
                    boolean flag = false;
                    int dim = arc.getDimension();

                    // Test if the dimension is in the orbit
                    flag |= rightNode.getOrbit().contains(dim);

                    // Test the other arcs
                    List/*<JMEArc>*/ rightIncident = rule.getRight().getIncidentArcsFromNode(rightNode);
                    for (int j = 0; j < rightIncident.size(); j++) {
                        JMEArc a = (JMEArc) rightIncident.get(j);
                        flag |= (dim == a.getDimension());
                    }
                    if (!flag)
                        return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule,
                                leftNode);
                }

                // Right -> Left
                // Dimensions in the orbit
                for (int i = 0; i < rightNode.getOrbit().size(); i++) {
                    boolean flag = false;
                    int dim = rightNode.getOrbit().get(i);

                    // Test for equality to -1
                    flag |= dim == -1;

                    // Test for identical position in the orbit
                    flag |= (leftNode.getOrbit().contains(dim))
                            && (dim == leftNode.getOrbit().get(i));

                    // Test des arcs
                    List/*<JMEArc>*/ leftIncident = rule.getLeft().getIncidentArcsFromNode(leftNode);
                    for (int j = 0; j < leftIncident.size(); j++) {
                        JMEArc a = (JMEArc) leftIncident.get(j);
                        flag |= (dim == a.getDimension());
                    }
                    if (!flag)
                        return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule,
                                rightNode);
                }

                // Dimensions in the arcs
                List/*<JMEArc>*/ rightIncident = rule.getRight().getIncidentArcsFromNode(rightNode);
                for (int i = 0; i < rightIncident.size(); i++) {
                    JMEArc arc = (JMEArc) rightIncident.get(i);
                    boolean flag = false;
                    int dim = arc.getDimension();

                    // Test if the dimension is in the orbit
                    flag |= leftNode.getOrbit().contains(dim);

                    // Test the other arcs
                    List/*<JMEArc>*/ leftIncident = rule.getLeft().getIncidentArcsFromNode(leftNode);
                    for (int j = 0; j < leftIncident.size(); j++) {
                        JMEArc a = (JMEArc) leftIncident.get(j);
                        flag |= (dim == a.getDimension());
                    }
                    if (!flag)
                        return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule,
                                rightNode);
                }
            }
        }
        List/*<JMENode>*/ rightNodes = right.getNodes();
        for (int j = 0; j < rightNodes.size(); j++) {
            JMENode rightNode = (JMENode) rightNodes.get(j);
            JMENode leftNode = left.getMatchNode(rightNode);
            // Node added: we verify that it has all dimensions
            if (leftNode == null) {
                List/*<JMEArc>*/ incidents = right.getIncidentArcsFromNode(rightNode);
                for (int i = 0; i <= modDim; i++) {
                    if (!rightNode.getOrbit().contains(i)) {
                        boolean flag = false;
                        for (int k = 0; k < incidents.size(); k++) {
                            JMEArc a = (JMEArc) incidents.get(k);
                            if (a.getDimension() == i)
                                flag = true;
                        }
                        if (!flag)
                            return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule,
                                    rightNode);
                    }
                }
            }
        }

        return null;
    }

	/**
	 * Determines if a node has an ijij cycle.
	 * It is assumed that i+2 <= j.
	 * 
	 * @param node Node in the rule for which to check if an ijij cycle exists.
	 * @param i Smallest dimension of the cycle.
	 * @param j Largest dimension of the cycle.
	 * @return 1 if the node has an ijij cycle, 0 if it does not, and -1 if it is impossible to determine
     *              (i and j in the orbit with an empty left graph).
	 */
	int hasCycle(JMENode node, int i, int j){
		/*
		 * First, we determine whether edges i and j are implicit or explicit (distinction
		 * between arc and loop).
		 * Depending on the case, we associate an attribute that allows us to find the configuration.
		 * We process the different configurations separately:
		 * 		- orbit/loop and loop/loop => cycle
		 * 		- arc/orbit => we look at the position in the orbit of the adjacent node
		 * 		- arc/loop => we look at the loops of the adjacent node
		 * 		- orbit/orbit =>
		 * 					in the left graph: cycle
		 * 					in the right graph: we compare with a node from the left graph
		 * 		- arc/arc => we check if we can construct a cycle using a third node
		 * 
		 * 	|-----------------------------------------------------------------------------------------------------------|
		 * 	|											| Dimension j													|
		 * 	|											|---------------------------------------------------------------|
		 * 	|										    | Absent	| In the orbit	| In a loop		    | In an arc     |
		 * 	|											|---------------------------------------------------------------|
		 * 	|											| 		0	|			1	|				2	|			3	|
		 * 	|-------------------------------------------|---------------------------------------------------------------|
		 * 	| Dimension i 	| Absent			|     0	|		0	|			1	|				2	|			3	|
		 * 	|				|-------------------|-------|-----------|---------------|-------------------|---------------|
		 * 	| 				| In the orbit		|     4	|		4	|			5	|				6	|			7	|
		 * 	|				|-------------------|-------|-----------|---------------|-------------------|---------------|
		 * 	|				| In a loop			|     8	|		8	|			9	|			   10	|		   11	|
		 * 	|				|-------------------|-------|-----------|---------------|-------------------|---------------|
		 * 	|				| In an arc			|    12	|	   12	|		   13	|			   14	|		   15	|
		 * 	|-----------------------------------------------------------------------------------------------------------|
		 */
		
        boolean flag = false;
        int cycleCode = 0;
        JMEGraph graph = node.getGraph();
        JerboaOrbit orbit = node.getOrbit();
        JMEArc arc_i = null;
        JMEArc arc_j = null;
        List/*<JMEArc>*/ nodeIncident = graph.getIncidentArcsFromNode(node);// Case of arc j
        for (int k = 0; k < nodeIncident.size(); k++) {
            JMEArc arc = (JMEArc) nodeIncident.get(k);
            arc_i = (arc.getDimension() == i) ? arc : arc_i;
            arc_j = (arc.getDimension() == j) ? arc : arc_j;

        }
        if (orbit.contains(j)) // orbit
            cycleCode += 1;
        if (arc_j instanceof JMELoop) // loop
            cycleCode += 2;
        else if (arc_j instanceof JMEArc) // arc
            cycleCode += 3;

        // Case of arc i
        if (orbit.contains(i)) // orbit
            cycleCode += 4;
        if (arc_i instanceof JMELoop) // loop
            cycleCode += 8;
        else if (arc_i instanceof JMEArc) // arc
            cycleCode += 12;

        flag |= cycleCode == 6; // loop/orbit
        flag |= cycleCode == 9; // orbit/loop
        flag |= cycleCode == 10; // loop/loop

        // In other cases, we need to compare with another node
        JMENode otherNode, node_i, node_j, node_cycle;
        int pos_i, pos_j;

        switch (cycleCode) {
            // 0,1,2,3,4,8,12 -> i or j missing ; 15 -> i and j arcs

            // j arc and i orbit
            case 7:
                otherNode = (arc_j.getSource() == node) ? arc_j.getDestination() : arc_j.getSource();
                flag |= orbit.indexOf(i) == otherNode.getOrbit().indexOf(i);
                break;

            // i arc and j orbit
            case 13:
                otherNode = (arc_i.getSource() == node) ? arc_i.getDestination() : arc_i.getSource();
                flag |= orbit.indexOf(j) == otherNode.getOrbit().indexOf(j);
                break;

            // i arc and j loop
            case 14:
                otherNode = (arc_i.getSource() == node) ? arc_i.getDestination() : arc_i.getSource();
                List/*<JMEArc>*/ otherIncident = graph.getIncidentArcsFromNode(otherNode);
                for (int k = 0; k < otherIncident.size(); k++) {
                    JMEArc arc = (JMEArc) otherIncident.get(k);
                    flag |= arc instanceof JMELoop && arc.getDimension() == j;
                }
                break;

            // j arc and i loop
            case 11:
                otherNode = (arc_j.getSource() == node) ? arc_j.getDestination() : arc_j.getSource();
                List/*<JMEArc>*/ otherIncident2 = graph.getIncidentArcsFromNode(otherNode);
                for (int k = 0; k < otherIncident2.size(); k++) {
                    JMEArc arc = (JMEArc) otherIncident2.get(k);
                    flag |= arc instanceof JMELoop && arc.getDimension() == i;
                }
                break;

            // i and j orbit
            case 5:
                if (graph.isLeft())
                    return 1;
                else if (graph.getRule().getLeft().getHooks().isEmpty())
                    return -1;
                else { // We need to check all hooks!
                    List/*<JMENode>*/ hooks = graph.getRule().getLeft().getHooks();
                    for (int k = 0; k < hooks.size(); k++) {
                        JMENode hook = (JMENode) hooks.get(k);
                        pos_i = orbit.indexOf(i);
                        pos_j = orbit.indexOf(j);
                        flag |= hook.getOrbit().get(pos_i) + 2 <= hook.getOrbit().get(pos_j)
                                || hook.getOrbit().get(pos_j) + 2 <= hook.getOrbit().get(pos_i);
                    }
                }
                break;

            // i and j arcs
            case 15:
                node_cycle = null;
                node_i = (arc_i.getSource() == node) ? arc_i.getDestination() : arc_i.getSource();
                node_j = (arc_j.getSource() == node) ? arc_j.getDestination() : arc_j.getSource();

                // We search for the fourth node to complete the cycle
                List/*<JMEArc>*/ incidentI = graph.getIncidentArcsFromNode(node_i);
                for (int k = 0; k < incidentI.size(); k++) {
                    JMEArc arc = (JMEArc) incidentI.get(k);
                    if ((!(arc instanceof JMELoop)) && arc.getDimension() == j)
                        node_cycle = (arc.getSource() == node_i) ? arc.getDestination() : arc.getSource();
                }

                // If we did not find it, we stop
                if (node_cycle == null)
                    break;

                // Otherwise, we search for a match
                List/*<JMEArc>*/ incidentJ = graph.getIncidentArcsFromNode(node_j);
                for (int k = 0; k < incidentJ.size(); k++) {
                    JMEArc arc = (JMEArc) incidentJ.get(k);
                    if ((!(arc instanceof JMELoop)) && arc.getDimension() == i) {
                        otherNode = (arc.getSource() == node_j) ? arc.getDestination() : arc.getSource();
                        flag |= otherNode == node_cycle;
                    }
                }
                break;

            default:
                break;

        }
        if (flag)
            return 1;
        return 0;
    }

	/** 
	 * Checks the cycle constraint on the rule.
	 * 
	 * ijij cycles are considered, with i+2 <= j.
	 * 
	 * For a node preserved by the rule:
	 * - a cycle present must be preserved;
	 * - if the node in the left graph does not have a cycle, then the corresponding node in the right graph must:
	 *   - either have dimensions i and j at the same positions in its orbit;
	 *   - or have an arc of dimension i/j if that was the case in the left graph.
	 * 
	 * For a node added, it must be ensured that it has a cycle.
	 * 
	 * Note: We weakened these conditions in the SCP paper.
	 * 
	 * @param rule Rule to verify.
	 * @return non-null error if check fails
	 */
    JMERuleError verifCycle(JMERule rule) {
        int modDim = rule.getModeler().getDimension();
        JMEGraph left = rule.getLeft();
        JMEGraph right = rule.getRight();

        for (int i = 0; i <= modDim - 2; i++) {
            for (int j = i + 2; j <= modDim; j++) {
                List/*<JMENode>*/ leftNodes = left.getNodes();
                for (int k = 0; k < leftNodes.size(); k++) {
                    JMENode leftNode = (JMENode) leftNodes.get(k);
                    JMENode rightNode = right.getMatchNode(leftNode);

                    // Preserved node
                    if (rightNode != null) {

                        // Cycle in the left graph node
                        if (hasCycle(leftNode, i, j) == 1 && hasCycle(rightNode, i, j) == 0)
                            return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule,
                                    rightNode);

                            // No cycle
                        else if (hasCycle(leftNode, i, j) == 0) {

                            // Dimension i in the orbit
                            int l_pos_i = leftNode.getOrbit().indexOf(i); // -1 if absent
                            int r_pos_i = rightNode.getOrbit().indexOf(i);
                            if (l_pos_i != r_pos_i)
                                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,
                                        rule, leftNode);

                            // Dimension j in the orbit
                            int l_pos_j = leftNode.getOrbit().indexOf(j);
                            int r_pos_j = rightNode.getOrbit().indexOf(j);
                            if (l_pos_j != r_pos_j)
                                return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,
                                        rule, leftNode);

                            // Dimension i missing in the orbit, check arcs
                            if (l_pos_i == -1) {
                                if (explicitArcHasChanged(i, rule, leftNode)) {
                                    return new JMERuleError(JMERuleErrorSeverity.CRITIQUE,
                                            JMERuleErrorType.TOPOLOGIC, rule, leftNode);
                                }
                            }

                            // Dimension j missing in the orbit, check arcs
                            if (l_pos_j == -1) {
                                if (explicitArcHasChanged(j, rule, leftNode)) {
                                    return new JMERuleError(JMERuleErrorSeverity.CRITIQUE,
                                            JMERuleErrorType.TOPOLOGIC, rule, leftNode);
                                }
                            }
                        }
                    }
                }

                // Node added in the right graph
                List/*<JMENode>*/ rightNodes = right.getNodes();
                for (int k = 0; k < rightNodes.size(); k++) {
                    JMENode rightNode = (JMENode) rightNodes.get(k);
                    if (left.getNodes().isEmpty() && hasCycle(rightNode, i, j) == -1)
                        return new JMERuleError(JMERuleErrorSeverity.WARNING, JMERuleErrorType.TOPOLOGIC, rule,
                                rightNode);
                    if (left.getMatchNode(rightNode) == null && hasCycle(rightNode, i, j) == 0)
                        return new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule,
                                rightNode);
                }
            }
        }

        return null;
    }

	/**
	 * Check that the explicit incident arc did not change between the LHS and the RHS.
	 * @param dim Dimension of the arc to be checked. It is assumed that both the node and its RHS counterpart have an explicit incident arc of this dimension.
	 * @param rule Rule in which the check happens.
	 * @param leftNode Left node to be checked (it must be preserved).
	 * 
	 * @return true if the arc changed.
	 */
    private boolean explicitArcHasChanged(int dim, JMERule rule, JMENode leftNode) {
        final int dimI = dim;
        JMENode rightNode = rule.getRight().getMatchNode(leftNode);
        if (hasAnyWithDimension(leftNode.alphas(), dimI)) {

            // By the incident arcs constraint, this should always be true.
            // In case it is not, we do not raise an additional error
            if (hasAnyWithDimension(rightNode.alphas(), dimI)) {
                JMENode leftNeighbor = leftNode.alpha(dimI);
                JMENode rightNeigbor = rightNode.alpha(dimI);
                JMENode matchLeftNeighbor = rule.getRight().getMatchNode(leftNeighbor);
                if (matchLeftNeighbor == null)
                    return true;
                return (!matchLeftNeighbor.equals(rightNeigbor));
            }
        }
        return false;
	}

    private static boolean hasAnyWithDimension(List/*<JMEArc>*/ arcs, int dimI) {
        for (int i = 0; i < arcs.size(); i++) {
            JMEArc arc = (JMEArc) arcs.get(i);
            if (arc.getDimension() == dimI) {
                return true;
            }
        }
        return false;
    }
}
