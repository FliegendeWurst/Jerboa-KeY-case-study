/**
 * 
 */
package fr.up.xlim.sic.ig.jerboa.jme.model;

/**
 * Cette classe specialise les arcs classiques pour les boucles.
 * @author hakim
 *
 */
public class JMELoop extends JMEArc {

	protected double angle;
	
	public JMELoop(JMEGraph graph, JMENode node, int dim) {
		super(graph, node,node,dim);
	}
}
