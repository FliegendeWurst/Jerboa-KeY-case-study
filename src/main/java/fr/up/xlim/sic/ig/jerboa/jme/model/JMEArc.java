package fr.up.xlim.sic.ig.jerboa.jme.model;


/**
 *  This class represents an arc in our editor. An arc is defined by different
 *  parameters. Those corresponding to the integration in the editor. Those corresponding to topological parameters.
 *  Finally, those corresponding to the display of the arc within the editor. 
 *  @author Hakim Ferrier-Belhaouari
 *
 */
public class JMEArc implements JMEElement{
    // topological parameters
    protected JMENode a;
    protected JMENode b;
    protected /*@ spec_public */ int dim;
    protected JMEGraph graph;
    
    public JMEArc(JMEGraph graph, JMENode a, JMENode b, int dim) {
        this.a = a;
        this.b = b;
        this.dim = dim;
        this.graph = graph;
    }

    public int getDimension() {
        return dim;
    }

    public JMENode getSource() {
        return a;
    }
    
    public JMENode getDestination() {
        return b;
    }

    public String toString() {
        return "("+a.toString()+"--"+dim+"--"+b.toString()+")";
    }

    public String getName() {
        return toString();
    }
    

}
