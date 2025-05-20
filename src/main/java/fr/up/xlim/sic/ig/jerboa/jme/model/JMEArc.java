package fr.up.xlim.sic.ig.jerboa.jme.model;


/**
 *  This class represents an arc in our editor. An arc is defined by different
 *  parameters. Those corresponding to the integration in the editor. Those corresponding to topological parameters.
 *  Finally, those corresponding to the display of the arc within the editor. 
 *  @author Hakim Ferrier-Belhaouari
 *
 */
public class JMEArc implements JMEElement{

    // extra parameters
    private boolean orient;
    
    // topological parameters
    protected JMENode a;
    protected JMENode b;
    protected int dim;
    protected JMEGraph graph;
    
    public JMEArc(JMEGraph graph, JMENode a, JMENode b, int dim) {
        this.a = a;
        this.b = b;
        this.dim = dim;
        this.graph = graph;
        orient = false;
    }

    public boolean sameNoDim(JMEArc arc) {
        return (arc.a == a || arc.a == b) && (arc.b == b || arc.b == a);
    }
    
    boolean link(JMENode n1, JMENode n2) {
        return (a.equals(n1) && b.equals(n2))||(b.equals(n1) && a.equals(n2));
    }
    
    public int getDimension() {
        return dim;
    }

    public boolean isOriented() {
        return orient;
    }

    public JMENode getSource() {
        return a;
    }
    
    public JMENode getDestination() {
        return b;
    }
    
    public boolean isLoop() {
        return (a == b);
    }
    
    
    @Override
    public String toString() {
        return "("+a.toString()+"--"+dim+"--"+b.toString()+")";
    }


    public void setDimension(int d) {
        if(d != dim) {
            dim = d;
        }
    }
    
    @Override
    public String getName() {
        return toString();
    }
    

}
