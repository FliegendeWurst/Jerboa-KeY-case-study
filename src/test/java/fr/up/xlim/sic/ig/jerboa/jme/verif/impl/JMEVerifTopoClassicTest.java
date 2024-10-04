package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Verifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMELoop;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeKind;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorSeverity;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorType;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEVerifIterator;
import up.jerboa.core.JerboaOrbit;

public class JMEVerifTopoClassicTest {		
	
	private JMEVerifTopoClassic verifier;
	private ArrayList<JMEError> errorList;
	private JMERuleAtomic rule1;

	@Before
    public void setUp() {
		verifier = new JMEVerifTopoClassic();
		
		JMEModeler modeler = new JMEModeler("test", "module", 2);
		
		JerboaOrbit orbit02 = new JerboaOrbit(0,2);
		JerboaOrbit emptyOrbit = new JerboaOrbit(-1,-1);
		
		// Rule 1		
        rule1 = new JMERuleAtomic(modeler, "Missing alpha 1");
        
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
        
        errorList = new ArrayList<JMEError>(); 
    }

	
	/**
	 * {@verifDimension} checks the dimensions in the nodes and the arcs.
	 * For nodes, dimension should be between -1 and the dimension of the modeler
	 * For arcs, dimensions should be between 0 and the dimension of the modeler
	 */
	
	@Test
	public void testVerifDimensionValid() {
		// Arrange
		int initialErrorCount = errorList.size();
		
		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(), initialErrorCount);
	}
	
	@Test
	public void testVerifDimensionLeftNodeNegativeDimension() {
		// Arrange: modify a node's orbit to have an invalid dimension
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode n2Left = leftRule1.getMatchNode("n2");
		n2Left.setOrbit(new JerboaOrbit(-2, -1)); // Invalid orbit dimension

		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(), initialErrorCount+1);
	}
	
	@Test
	public void testVerifDimensionLeftNodeTooLargeDimension() {
		// Arrange: modify a node's orbit to have an invalid dimension
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode n2Left = leftRule1.getMatchNode("n2");
		n2Left.setOrbit(new JerboaOrbit(0, -3)); // Invalid orbit dimension

		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(), initialErrorCount+1);
	}
	
	@Test
	public void testVerifDimensionRightNodeNegativeDimension() {
		// Arrange: modify a node's orbit to have an invalid dimension
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode n10Right = rightRule1.getMatchNode("n10");
		n10Right.setOrbit(new JerboaOrbit(-2, -1)); // Invalid orbit dimension

		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(), initialErrorCount+1);
	}
	
	@Test
	public void testVerifDimensionRightNodeTooLargeDimension() {
		// Arrange: modify a node's orbit to have an invalid dimension
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode n10Right = rightRule1.getMatchNode("n10");
		n10Right.setOrbit(new JerboaOrbit(1, 5)); // Invalid orbit dimension

		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(), initialErrorCount+1);
	}
	
	@Test
	public void testVerifDimensionLeftArcNegativeDimension() {
		// Arrange: modify an arc's dimension to have an invalid dimension
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMEArc arc = leftRule1.getArcs().get(2);
		arc.setDimension(-1); // Invalid dimension

		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(), initialErrorCount+1);
	}
	
	@Test
	public void testVerifDimensionLeftArcTooLargeDimension() {
		// Arrange: modify an arc's dimension to have an invalid dimension
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMEArc arc = leftRule1.getArcs().get(3);
		arc.setDimension(-1); // Invalid dimension

		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(),initialErrorCount+1);
	}
	
	@Test
	public void testVerifDimensionRightArcNegativeDimension() {
		// Arrange: modify an arc's dimension to have an invalid dimension
		int initialErrorCount = errorList.size();
		JMEGraph righRule1 = rule1.getRight();
		JMEArc arc = righRule1.getArcs().get(2);
		arc.setDimension(-1); // Invalid dimension

		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(), initialErrorCount+1);
	}
	
	@Test
	public void testVerifDimensionRightArcTooLargeDimension() {
		// Arrange: modify an arc's dimension to have an invalid dimension
		int initialErrorCount = errorList.size();
		JMEGraph righRule1 = rule1.getRight();
		JMEArc arc = righRule1.getArcs().get(2);
		arc.setDimension(3); // Invalid dimension

		// Act
		verifier.verifDimension(rule1, errorList);
		
		// Assert
		assertEquals(errorList.size(), initialErrorCount+1);
	}

	@Test
	public void testVerifDuplicateNodeNoDuplicates() {
	    // Arrange
	    int initialErrorCount = errorList.size();

	    // Act
	    verifier.verifDuplicateNode(rule1, errorList);

	    // Assert: no errors should be added
	    assertEquals(initialErrorCount, errorList.size());
	}
	
	
	/**
	 * {@verifDuplicateNode} checks the absence of duplicate nodes
	 * in both parts of the rule.
	 */
	
	@Test
	public void testVerifDuplicateNodeDuplicateInLeftGraph() {
	    // Arrange: add a duplicate node to the left graph
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode n1Left = leftRule1.getMatchNode("n1");
	    JMENode duplicateNode = new JMENode(n1Left, leftRule1);
	    leftRule1.addNode(duplicateNode);

	    // Act
	    verifier.verifDuplicateNode(rule1, errorList);

	    // Assert: two new errors should be added (one for each duplicate node)
	    assertEquals(initialErrorCount + 2, errorList.size());

	    // Verify that the error message corresponds to the duplicate node in the left graph
	    assertTrue(errorList.get(initialErrorCount).getMessage().contains("Duplicate nodes in the left graph"));
	    assertTrue(errorList.get(initialErrorCount + 1).getMessage().contains("Duplicate nodes in the left graph"));
	}
	
	@Test
	public void testVerifDuplicateNodeDuplicateInRightGraph() {
	    // Arrange: add a duplicate node to the right graph
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode n9Right = rightRule1.getMatchNode("n9");
	    JMENode duplicateNode = new JMENode(n9Right, rightRule1);
	    rightRule1.addNode(duplicateNode);

	    // Act
	    verifier.verifDuplicateNode(rule1, errorList);

	    // Assert: two new errors should be added (one for each duplicate node)
	    assertEquals(initialErrorCount + 2, errorList.size());

	    // Verify that the error message corresponds to the duplicate node in the left graph
	    assertTrue(errorList.get(initialErrorCount).getMessage().contains("Duplicate nodes in the right graph"));
	    assertTrue(errorList.get(initialErrorCount + 1).getMessage().contains("Duplicate nodes in the right graph"));
	}
	
	@Test
	public void testVerifDuplicateNodeInBothGraphs() {
	    // Arrange: add duplicate nodes to both left and right graphs
	    JMENode duplicateLeftNode = new JMENode(rule1.getLeft(), "n1", 0, 0, JMENodeKind.SIMPLE);
	    rule1.getLeft().addNode(duplicateLeftNode);
	    JMENode duplicateRightNode = new JMENode(rule1.getRight(), "n9", 0, 0, JMENodeKind.SIMPLE);
	    rule1.getRight().addNode(duplicateRightNode);
	    int initialErrorCount = errorList.size();

	    // Act
	    verifier.verifDuplicateNode(rule1, errorList);

	    // Assert: four new errors should be added (two for the left graph, two for the right graph)
	    assertEquals(initialErrorCount + 4, errorList.size());
	}
	
	
	/**
	 * {@verifHooks} checks that there is exactly one hook per connected
	 * component and that hooks have a full orbit.
	 */
	
	@Test
	public void testVerifHooksValid(){
		// Arrange
	    int initialErrorCount = errorList.size();

	    // Act
	    verifier.verifHooks(rule1, errorList);

	    // Assert: no errors should be added
	    assertEquals(initialErrorCount, errorList.size());
	}
	
	@Test
	public void testVerifHooksNonFullOrbit(){
		// Arrange: change the orbit of the hook
	    int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		leftRule1.getMatchNode("n5").setOrbit(new JerboaOrbit(-1,2));
	    
	    // Act
	    verifier.verifHooks(rule1, errorList);

	    // Assert: one new error for the hook
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifHooksTwoHooks(){
		// Arrange: change a left node to a hook
	    int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		leftRule1.getMatchNode("n0").setKind(JMENodeKind.HOOK);
	    
	    // Act
	    verifier.verifHooks(rule1, errorList);

	    // Assert: two new errors should be added (one for each hook)
	    assertEquals(initialErrorCount+2, errorList.size());
	}
	
	@Test
	public void testVerifHooksTwoComponents(){
		// Arrange: Add a disconnected node which is also a hook
	    int initialErrorCount = errorList.size();
	    JMENode hookNode = new JMENode(rule1.getLeft(), "hook", 0, 0, JMENodeKind.HOOK);
	    rule1.getLeft().addNode(hookNode);
	    
	    // Act
	    verifier.verifHooks(rule1, errorList);

	    // Assert: no now error
	    assertEquals(initialErrorCount, errorList.size());
	}
	
	
	/**
	 * {@verifNodeOrbitSizes} checks that all nodes have the same
	 * orbit size.
	 */
	
	@Test
	public void testVerifNodeOrbitSizesValid(){
		// Arrange
	    int initialErrorCount = errorList.size();

	    // Act
	    verifier.verifNodeOrbitSizes(rule1, errorList);

	    // Assert: no errors should be added
	    assertEquals(initialErrorCount, errorList.size());
	}
	
	@Test
	public void testVerifNodeOrbitSizesTooSmallLeft(){
		// Arrange
	    int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		leftRule1.getMatchNode("n1").setOrbit(new JerboaOrbit(-1));

	    // Act
	    verifier.verifNodeOrbitSizes(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifNodeOrbitSizesTooLargeLeft(){
		// Arrange
	    int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		leftRule1.getMatchNode("n1").setOrbit(new JerboaOrbit(0,1,2));

	    // Act
	    verifier.verifNodeOrbitSizes(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifNodeOrbitSizesTooSmallRight(){
		// Arrange
	    int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		rightRule1.getMatchNode("n10").setOrbit(new JerboaOrbit(0));

	    // Act
	    verifier.verifNodeOrbitSizes(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifNodeOrbitSizesTooLargeRight(){
		// Arrange
	    int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		rightRule1.getMatchNode("n7").setOrbit(new JerboaOrbit(0,1,-1));

	    // Act
	    verifier.verifNodeOrbitSizes(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}

	/**
	 * {@verifDuplicateDimension} checks that no node have a duplicated
	 * dimension in its orbit and arcs.
	 */
	
	@Test
	public void testVerifDuplicateDimensionValid() {
		// Arrange
	    int initialErrorCount = errorList.size();

	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert: no errors should be added
	    assertEquals(initialErrorCount, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionLeftOrbit() {
		// Arrange : add a left node with duplicate dimension in orbit
	    int initialErrorCount = errorList.size();
	    JMENode hookNode = new JMENode(rule1.getLeft(), "hook", 0, 0, JMENodeKind.HOOK);
	    hookNode.setOrbit(new JerboaOrbit(0,0));
	    rule1.getLeft().addNode(hookNode);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionLeftLoops() {
		// Arrange : add a left node with two loops of the same dimension
	    int initialErrorCount = errorList.size();
	    JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
	    hookNode.setOrbit(new JerboaOrbit(1,2));
	    leftRule1.addNode(hookNode);
	    leftRule1.creatLoop(hookNode, 0);
	    leftRule1.creatLoop(hookNode, 0);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionLeftLoopAndOrbit() {
		// Arrange : add a left node with a loop of a dimension present in the orbit
	    int initialErrorCount = errorList.size();
	    JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
	    hookNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(hookNode);
	    leftRule1.creatLoop(hookNode, 0);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionLeftLoopAndArc() {
		// Arrange : add a left node with a loop and an arc of the same dimension
	    int initialErrorCount = errorList.size();
	    JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
	    hookNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(hookNode);
	    JMENode otherNode = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
	    otherNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(otherNode);
	    leftRule1.creatLoop(hookNode, 1);
	    leftRule1.creatArc(hookNode, otherNode, 1);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionLeftArcAndArcSameNode() {
		// Arrange : add two left nodes with parallel arcs of the same dimension
	    int initialErrorCount = errorList.size();
	    JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
	    hookNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(hookNode);
	    JMENode otherNode = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
	    otherNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(otherNode);
	    leftRule1.creatArc(hookNode, otherNode, 1);
	    leftRule1.creatArc(hookNode, otherNode, 1);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert: one error for each node
	    assertEquals(initialErrorCount+2, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionLeftArcAndArcDifferrentNode() {
		// Arrange : add three left nodes with a path of arcs of the same dimension
	    int initialErrorCount = errorList.size();
	    JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
	    hookNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(hookNode);
	    JMENode otherNode = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
	    otherNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(otherNode);
	    JMENode thirdNode = new JMENode(leftRule1, "third", 0, 0, JMENodeKind.SIMPLE);
	    thirdNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(thirdNode);
	    leftRule1.creatArc(hookNode, otherNode, 1);
	    leftRule1.creatArc(hookNode, thirdNode, 1);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	

	@Test
	public void testVerifDuplicateDimensionLeftArcAndOrbit() {
		// Arrange : add a left node with an arc of a dimension present in the orbit
	    int initialErrorCount = errorList.size();
	    JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
	    hookNode.setOrbit(new JerboaOrbit(1,2));
	    leftRule1.addNode(hookNode);
	    JMENode otherNode = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
	    otherNode.setOrbit(new JerboaOrbit(0,2));
	    leftRule1.addNode(otherNode);
	    leftRule1.creatArc(hookNode, otherNode, 1);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert: one error for each node
	    assertEquals(initialErrorCount+1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightOrbit() {
		// Arrange : add a right node with duplicate dimension in orbit
	    int initialErrorCount = errorList.size();
	    JMENode newNode = new JMENode(rule1.getRight(), "new", 0, 0, JMENodeKind.SIMPLE);
	    newNode.setOrbit(new JerboaOrbit(0,0));
	    rule1.getRight().addNode(newNode);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionRightLoops() {
		// Arrange : add a right node with two loops of the same dimension
	    int initialErrorCount = errorList.size();
	    JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
	    newNode.setOrbit(new JerboaOrbit(1,2));
	    rightRule1.addNode(newNode);
	    rightRule1.creatLoop(newNode, 0);
	    rightRule1.creatLoop(newNode, 0);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionRightLoopAndOrbit() {
		// Arrange : add a right node with a loop of a dimension present in the orbit
	    int initialErrorCount = errorList.size();
	    JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
	    newNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(newNode);
	    rightRule1.creatLoop(newNode, 0);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionRightLoopAndArc() {
		// Arrange : add a right node with a loop and an arc of the same dimension
	    int initialErrorCount = errorList.size();
	    JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
	    newNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(newNode);
	    JMENode otherNode = new JMENode(rightRule1, "other", 0, 0, JMENodeKind.SIMPLE);
	    otherNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(otherNode);
	    rightRule1.creatLoop(newNode, 1);
	    rightRule1.creatArc(newNode, otherNode, 1);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionRightArcAndArcSameNode() {
		// Arrange : add two right nodes with parallel arcs of the same dimension
	    int initialErrorCount = errorList.size();
	    JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
	    newNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(newNode);
	    JMENode otherNode = new JMENode(rightRule1, "other", 0, 0, JMENodeKind.SIMPLE);
	    otherNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(otherNode);
	    rightRule1.creatArc(newNode, otherNode, 1);
	    rightRule1.creatArc(newNode, otherNode, 1);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert: one error for each node
	    assertEquals(initialErrorCount+2, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightArcAndArcDifferrentNode() {
		// Arrange : add three right nodes with a path of arcs of the same dimension
	    int initialErrorCount = errorList.size();
	    JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
	    newNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(newNode);
	    JMENode otherNode = new JMENode(rightRule1, "other", 0, 0, JMENodeKind.SIMPLE);
	    otherNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(otherNode);
	    JMENode thirdNode = new JMENode(rightRule1, "third", 0, 0, JMENodeKind.SIMPLE);
	    thirdNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(thirdNode);
	    rightRule1.creatArc(newNode, otherNode, 1);
	    rightRule1.creatArc(newNode, thirdNode, 1);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifDuplicateDimensionRightArcAndOrbit() {
		// Arrange : add a right node with an arc of a dimension present in the orbit
	    int initialErrorCount = errorList.size();
	    JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
	    newNode.setOrbit(new JerboaOrbit(1,2));
	    rightRule1.addNode(newNode);
	    JMENode otherNode = new JMENode(rightRule1, "other", 0, 0, JMENodeKind.SIMPLE);
	    otherNode.setOrbit(new JerboaOrbit(0,2));
	    rightRule1.addNode(otherNode);
	    rightRule1.creatArc(newNode, otherNode, 1);
	    
	    // Act
	    verifier.verifDuplicateDimension(rule1, errorList);

	    // Assert: one error for each node
	    assertEquals(initialErrorCount+1, errorList.size());
	}
	
	@Test
	public void testVerifIncidentArc() {
	}
	
	@Test
	public void testHasCycle(){
	}

	@Test
	public void testVerifCycle() {
	}

}
