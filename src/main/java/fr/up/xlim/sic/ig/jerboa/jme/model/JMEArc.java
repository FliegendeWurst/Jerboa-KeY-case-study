package fr.up.xlim.sic.ig.jerboa.jme.model;


/**
 *  This class represents an arc in our editor. An arc is defined by different
 *  parameters. Those corresponding to the integration in the editor. Those corresponding to topological parameters.
 *  Finally, those corresponding to the display of the arc within the editor. 
 *  @author Hakim Ferrier-Belhaouari
 *
 */
public class JMEArc implements JMEElement{
    //@ public ghost \locset footprint;
    //@ public accessible \inv : footprint;
    //@ public invariant footprint == \set_union(\singleton(footprint), a.footprint, b.footprint);
    //@ public invariant \invariant_for(a);
    //@ public invariant \invariant_for(b);

    // topological parameters
    public final JMENode a;
    public final JMENode b;
    public final int dim;

    /*@ public normal_behavior
      @ requires \invariant_for(a) && \invariant_for(b);
      @ assignable this.a,this.b,this.dim;
      @*/
    public JMEArc(JMENode a, JMENode b, int dim) {
        this.a = a;
        this.b = b;
        this.dim = dim;
        //@ set footprint = \set_union(\singleton(footprint), a.footprint, b.footprint);
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
