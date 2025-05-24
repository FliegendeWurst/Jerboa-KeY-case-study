/**
 * 
 */
package fr.up.xlim.sic.ig.jerboa.jme.model;

/**
 * This class handles the loops in the editor.
 * @author hakim
 *
 */
public final class JMELoop extends JMEArc {

	protected double angle;
	
	public JMELoop(JMEGraph graph, JMENode node, int dim) {
		super(graph, node,node,dim);
	}
}
