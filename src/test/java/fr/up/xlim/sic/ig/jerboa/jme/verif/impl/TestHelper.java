package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeKind;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import up.jerboa.core.JerboaOrbit;

public class TestHelper {
	
	private JMEVerifTopoClassic verifier;
	private JMEModeler modeler;
	
	public TestHelper(String modelerName, String modelerModule, int modelerDim) {
		verifier = new JMEVerifTopoClassic();
		modeler = new JMEModeler("test", "module", 2);
	}

	public JMEVerifTopoClassic getVerifier() {
		return verifier;
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	public ArrayList<JMEError> newErrorList() {
		return new ArrayList<JMEError>();
	}
	
	public JMERuleAtomic createRule1() {
		JMERuleAtomic rule1 = new JMERuleAtomic(modeler, "Missing alpha 1");
		JerboaOrbit orbit02 = new JerboaOrbit(0,2);
		JerboaOrbit emptyOrbit = new JerboaOrbit(-1,-1);
		
        // Left
        JMEGraph leftRule1 = rule1.getLeft();
        
        // Nodes
        JMENode n0Left = new JMENode(leftRule1, "n0", 0, 0, JMENodeKind.SIMPLE);
        leftRule1.addNode(n0Left);
        JMENode n1Left = new JMENode(leftRule1, "n1", 0, 0, JMENodeKind.SIMPLE);
        leftRule1.addNode(n1Left);
        JMENode n2Left = new JMENode(leftRule1, "n2", 0, 0, JMENodeKind.SIMPLE);
        leftRule1.addNode(n2Left);
        JMENode n3Left = new JMENode(leftRule1, "n3", 0, 0, JMENodeKind.SIMPLE);
        leftRule1.addNode(n3Left);
        JMENode n4Left = new JMENode(leftRule1, "n4", 0, 0, JMENodeKind.SIMPLE);
        leftRule1.addNode(n4Left);
        JMENode n5Left = new JMENode(leftRule1, "n5", 0, 0, JMENodeKind.HOOK);
        leftRule1.addNode(n5Left);

        // Orbits
        n0Left.setOrbit(orbit02);
        n1Left.setOrbit(emptyOrbit);
        n2Left.setOrbit(emptyOrbit);
        n3Left.setOrbit(emptyOrbit);        
        n4Left.setOrbit(emptyOrbit);
        n5Left.setOrbit(orbit02);

        // Arcs
        leftRule1.creatArc(n0Left, n1Left, 1);
        leftRule1.creatArc(n1Left, n2Left, 2);
        leftRule1.creatArc(n2Left, n3Left, 0);
        leftRule1.creatArc(n3Left, n4Left, 2);
        leftRule1.creatArc(n1Left, n4Left, 0);
        leftRule1.creatArc(n4Left, n5Left, 1);
        
        // Right
        JMEGraph rightRule1 = rule1.getRight();

        // Nodes
        JMENode n0Right = new JMENode(rightRule1, "n0", 0, 0, JMENodeKind.SIMPLE);
        rightRule1.addNode(n0Right);
        JMENode n5Right = new JMENode(rightRule1, "n5", 0, 0, JMENodeKind.SIMPLE);
        rightRule1.addNode(n5Right);
        JMENode n6Right = new JMENode(rightRule1, "n6", 0, 0, JMENodeKind.SIMPLE);
        rightRule1.addNode(n6Right);
        JMENode n7Right = new JMENode(rightRule1, "n7", 0, 0, JMENodeKind.SIMPLE);
        rightRule1.addNode(n7Right);
        JMENode n8Right = new JMENode(rightRule1, "n8", 0, 0, JMENodeKind.SIMPLE);
        rightRule1.addNode(n8Right);
        JMENode n9Right = new JMENode(rightRule1, "n9", 0, 0, JMENodeKind.SIMPLE);
        rightRule1.addNode(n9Right);
        JMENode n10Right = new JMENode(rightRule1, "n10", 0, 0, JMENodeKind.SIMPLE);
        rightRule1.addNode(n10Right);
        JMENode n11Right = new JMENode(rightRule1, "n11", 0, 0, JMENodeKind.SIMPLE);
        rightRule1.addNode(n11Right);
        
        // Orbits
        for (JMENode node : rightRule1.getNodes()) {
        	node.setOrbit(emptyOrbit);
        }

        // Arcs
        rightRule1.creatArc(n0Right, n6Right, 2);
        rightRule1.creatArc(n6Right, n7Right, 0);
        rightRule1.creatArc(n7Right, n8Right, 2);
        rightRule1.creatArc(n8Right, n0Right, 0);
        rightRule1.creatArc(n0Right, n5Right, 1);
        rightRule1.creatArc(n5Right, n9Right, 2);
        rightRule1.creatArc(n9Right, n10Right, 0);
        rightRule1.creatArc(n10Right, n11Right, 2);
        rightRule1.creatArc(n11Right, n5Right, 0);
		
		return rule1;
		
	}

	public JMERuleAtomic createDooSabin() {
		JMERuleAtomic dooSabin = new JMERuleAtomic(modeler, "DooSabin");
        
        // Left
        JMEGraph leftDooSabin = dooSabin.getLeft();
        JMENode n0LeftDooSabin = new JMENode(leftDooSabin, "n0", 0, 0, JMENodeKind.HOOK);
        n0LeftDooSabin.setOrbit(new JerboaOrbit(0,1,2));
        leftDooSabin.addNode(n0LeftDooSabin);

        // Right
        JMEGraph rightDooSabin = dooSabin.getRight();
        JMENode rn0DooSabin = new JMENode(rightDooSabin, "n0", 0, 0, JMENodeKind.SIMPLE);
        JMENode rn1DooSabin = new JMENode(rightDooSabin, "n1", 0, 0, JMENodeKind.SIMPLE);
        JMENode rn2DooSabin = new JMENode(rightDooSabin, "n2", 0, 0, JMENodeKind.SIMPLE);
        JMENode rn3DooSabin = new JMENode(rightDooSabin, "n3", 0, 0, JMENodeKind.SIMPLE);
        rn0DooSabin.setOrbit(new JerboaOrbit(0,1,-1));
        rn1DooSabin.setOrbit(new JerboaOrbit(0,-1,-1));
        rn2DooSabin.setOrbit(new JerboaOrbit(-1,-1,0));
        rn3DooSabin.setOrbit(new JerboaOrbit(-1,1,0));
        rightDooSabin.addNode(rn0DooSabin);
        rightDooSabin.addNode(rn1DooSabin);
        rightDooSabin.addNode(rn2DooSabin);
        rightDooSabin.addNode(rn3DooSabin);
        rightDooSabin.creatArc(rn0DooSabin, rn1DooSabin, 2);
        rightDooSabin.creatArc(rn1DooSabin, rn2DooSabin, 1);
        rightDooSabin.creatArc(rn2DooSabin, rn3DooSabin, 2);
				
		return dooSabin;
	}
	
	public JMERuleAtomic createRemoveEdge() {
		JMERuleAtomic removeEdge = new JMERuleAtomic(modeler, "RemoveEdge");
        
        // Left
        JMEGraph leftRemoveEdge = removeEdge.getLeft();
        JMENode ln0RemoveEdge = new JMENode(leftRemoveEdge, "n0", 0, 0, JMENodeKind.SIMPLE);
        JMENode ln1RemoveEdge = new JMENode(leftRemoveEdge, "n1", 0, 0, JMENodeKind.HOOK);
        ln0RemoveEdge.setOrbit(new JerboaOrbit(-1,-1));
        ln1RemoveEdge.setOrbit(new JerboaOrbit(0,2));
        leftRemoveEdge.addNode(ln0RemoveEdge);
        leftRemoveEdge.addNode(ln1RemoveEdge);
        leftRemoveEdge.creatArc(ln0RemoveEdge, ln1RemoveEdge, 1);

        // Right
        JMEGraph rightRemoveEdge = removeEdge.getRight();
        JMENode rn0RemoveEdge = new JMENode(rightRemoveEdge, "n0", 0, 0, JMENodeKind.SIMPLE);
        rn0RemoveEdge.setOrbit(new JerboaOrbit(-1,1));
        rightRemoveEdge.addNode(rn0RemoveEdge);
				
		return removeEdge;
	}
	
	public JMERuleAtomic createDeleteIsolatedPrism() {
		JMERuleAtomic deleteIsolatedPrism = new JMERuleAtomic(modeler, "DeleteIsolatedPrism");
        
		// Left
		JMEGraph leftDeleteIsolatedPrism = deleteIsolatedPrism.getLeft();
		JMENode ln0DeleteIsolatedPrism = new JMENode(leftDeleteIsolatedPrism, "n0", 0, 0, JMENodeKind.HOOK); 
		ln0DeleteIsolatedPrism.setOrbit(new JerboaOrbit(0,1));
		leftDeleteIsolatedPrism.addNode(ln0DeleteIsolatedPrism);
        JMENode ln1DeleteIsolatedPrism = new JMENode(leftDeleteIsolatedPrism, "n1", 0, 0, JMENodeKind.SIMPLE); 
		ln1DeleteIsolatedPrism.setOrbit(new JerboaOrbit(0,-1));
		leftDeleteIsolatedPrism.addNode(ln1DeleteIsolatedPrism);
        JMENode ln2DeleteIsolatedPrism = new JMENode(leftDeleteIsolatedPrism, "n2", 0, 0, JMENodeKind.SIMPLE); 
		ln2DeleteIsolatedPrism.setOrbit(new JerboaOrbit(-1,2));
		leftDeleteIsolatedPrism.addNode(ln2DeleteIsolatedPrism);
        JMENode ln3DeleteIsolatedPrism = new JMENode(leftDeleteIsolatedPrism, "n3", 0, 0, JMENodeKind.SIMPLE); 
		ln3DeleteIsolatedPrism.setOrbit(new JerboaOrbit(-1,2));
		leftDeleteIsolatedPrism.addNode(ln3DeleteIsolatedPrism);
        JMENode ln4DeleteIsolatedPrism = new JMENode(leftDeleteIsolatedPrism, "n4", 0, 0, JMENodeKind.SIMPLE); 
		ln4DeleteIsolatedPrism.setOrbit(new JerboaOrbit(0,-1));
		leftDeleteIsolatedPrism.addNode(ln4DeleteIsolatedPrism);
        JMENode ln5DeleteIsolatedPrism = new JMENode(leftDeleteIsolatedPrism, "n5", 0, 0, JMENodeKind.SIMPLE); 
		ln5DeleteIsolatedPrism.setOrbit(new JerboaOrbit(0,1));
		leftDeleteIsolatedPrism.addNode(ln5DeleteIsolatedPrism);
		leftDeleteIsolatedPrism.creatArc(ln0DeleteIsolatedPrism, ln1DeleteIsolatedPrism, 2);
		leftDeleteIsolatedPrism.creatArc(ln1DeleteIsolatedPrism, ln2DeleteIsolatedPrism, 1);
		leftDeleteIsolatedPrism.creatArc(ln2DeleteIsolatedPrism, ln3DeleteIsolatedPrism, 0);
		leftDeleteIsolatedPrism.creatArc(ln3DeleteIsolatedPrism, ln4DeleteIsolatedPrism, 1);
		leftDeleteIsolatedPrism.creatArc(ln4DeleteIsolatedPrism, ln5DeleteIsolatedPrism, 2);
				
		return deleteIsolatedPrism;
	}
	
	public JMERuleAtomic createErrorIACondition() {
		JMERuleAtomic errorIACondition = new JMERuleAtomic(modeler, "errorIACondition");
		
		JerboaOrbit orbit0 = new JerboaOrbit(0);
		
		// Left
		JMEGraph leftErrorIACondition = errorIACondition.getLeft();
		JMENode ln0 = new JMENode(leftErrorIACondition, "n0", 0, 0, JMENodeKind.HOOK);
		JMENode ln7 = new JMENode(leftErrorIACondition, "n7", 0, 0, JMENodeKind.SIMPLE);
		JMENode ln1 = new JMENode(leftErrorIACondition, "n1", 0, 0, JMENodeKind.HOOK);
		JMENode ln2 = new JMENode(leftErrorIACondition, "n2", 0, 0, JMENodeKind.SIMPLE);
		leftErrorIACondition.addNode(ln0);
		leftErrorIACondition.addNode(ln1);
		leftErrorIACondition.addNode(ln2);
		leftErrorIACondition.addNode(ln7);
		leftErrorIACondition.creatArc(ln0, ln7, 1);
		leftErrorIACondition.creatArc(ln1, ln2, 2);
		
		for (JMENode node : leftErrorIACondition.getNodes()) {
			node.setOrbit(orbit0);
		}
		
		
		// Right
		JMEGraph righterrorIACondition = errorIACondition.getRight();
		JMENode rn1 = new JMENode(righterrorIACondition, "n1", 0, 0, JMENodeKind.SIMPLE);
		JMENode rn2 = new JMENode(righterrorIACondition, "n2", 0, 0, JMENodeKind.SIMPLE);
		JMENode rn0 = new JMENode(righterrorIACondition, "n0", 0, 0, JMENodeKind.SIMPLE);
		JMENode rn7 = new JMENode(righterrorIACondition, "n7", 0, 0, JMENodeKind.SIMPLE);
		righterrorIACondition.addNode(rn0);
		righterrorIACondition.addNode(rn1);
		righterrorIACondition.addNode(rn2);
		righterrorIACondition.addNode(rn7);
		righterrorIACondition.creatArc(rn7, rn2, 1);
		righterrorIACondition.creatArc(rn1, rn2, 2);
		righterrorIACondition.creatArc(rn1, rn0, 1);
		
		for (JMENode node : righterrorIACondition.getNodes()) {
			node.setOrbit(orbit0);
}
		
		return errorIACondition;
	}
}
