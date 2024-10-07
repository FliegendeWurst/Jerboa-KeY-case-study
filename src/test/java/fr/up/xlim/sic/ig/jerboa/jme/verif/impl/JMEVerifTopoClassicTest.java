package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeKind;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import up.jerboa.core.JerboaOrbit;

public class JMEVerifTopoClassicTest {

	private static TestHelper helper;
	private static JMEVerifTopoClassic verifier;

	private ArrayList<JMEError> errorList;
	private JMERuleAtomic rule1;
	private JMERuleAtomic dooSabin;
	private JMERuleAtomic removeEdge;
	private JMERuleAtomic deleteIsolatedPrism;
	private JMERuleAtomic errorIACondition;
	private JMERuleAtomic errorCycleCondition;

	@BeforeClass
	public static void setUpClass() {
		helper = new TestHelper();
		verifier = helper.getVerifier();
	}

	@Before
	public void setUp() {
		errorList = helper.newErrorList();
		rule1 = helper.createRule1();
		dooSabin = helper.createDooSabin();
		removeEdge = helper.createRemoveEdge();
		deleteIsolatedPrism = helper.createDeleteIsolatedPrism();
		errorCycleCondition = helper.createErrorCycleCondition();

		// Incorrect rule for incident arcs constraint
		errorIACondition = helper.createErrorIACondition();
	}

	/**
	 * @verifDimension checks the dimensions in the nodes and the arcs. For nodes,
	 *                 dimension should be between -1 and the dimension of the
	 *                 modeler For arcs, dimensions should be between 0 and the
	 *                 dimension of the modeler
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
		assertEquals(errorList.size(), initialErrorCount + 1);
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
		assertEquals(errorList.size(), initialErrorCount + 1);
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
		assertEquals(errorList.size(), initialErrorCount + 1);
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
		assertEquals(errorList.size(), initialErrorCount + 1);
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
		assertEquals(errorList.size(), initialErrorCount + 1);
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
		assertEquals(errorList.size(), initialErrorCount + 1);
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
		assertEquals(errorList.size(), initialErrorCount + 1);
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
		assertEquals(errorList.size(), initialErrorCount + 1);
	}

	@Test
	public void testVerifDuplicateNodeNoDuplicates() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifDuplicateNode(rule1, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	/**
	 * @verifDuplicateNode checks the absence of duplicate nodes in both parts of
	 *                     the rule.
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

		// Verify that the error message corresponds to the duplicate node in the left
		// graph
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

		// Verify that the error message corresponds to the duplicate node in the left
		// graph
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

		// Assert: four new errors should be added (two for the left graph, two for the
		// right graph)
		assertEquals(initialErrorCount + 4, errorList.size());
	}

	/**
	 * @verifHooks checks that there is exactly one hook per connected component and
	 *             that hooks have a full orbit.
	 */

	@Test
	public void testVerifHooksValid() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifHooks(rule1, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifHooksNonFullOrbit() {
		// Arrange: change the orbit of the hook
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		leftRule1.getMatchNode("n5").setOrbit(new JerboaOrbit(-1, 2));

		// Act
		verifier.verifHooks(rule1, errorList);

		// Assert: one new error for the hook
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifHooksTwoHooks() {
		// Arrange: change a left node to a hook
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		leftRule1.getMatchNode("n0").setKind(JMENodeKind.HOOK);

		// Act
		verifier.verifHooks(rule1, errorList);

		// Assert: two new errors should be added (one for each hook)
		assertEquals(initialErrorCount + 2, errorList.size());
	}

	@Test
	public void testVerifHooksTwoComponents() {
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
	 * @verifNodeOrbitSizes checks that all nodes have the same orbit size.
	 */

	@Test
	public void testVerifNodeOrbitSizesValid() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifNodeOrbitSizes(rule1, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifNodeOrbitSizesTooSmallLeft() {
		// Arrange
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		leftRule1.getMatchNode("n1").setOrbit(new JerboaOrbit(-1));

		// Act
		verifier.verifNodeOrbitSizes(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifNodeOrbitSizesTooLargeLeft() {
		// Arrange
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		leftRule1.getMatchNode("n1").setOrbit(new JerboaOrbit(0, 1, 2));

		// Act
		verifier.verifNodeOrbitSizes(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifNodeOrbitSizesTooSmallRight() {
		// Arrange
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		rightRule1.getMatchNode("n10").setOrbit(new JerboaOrbit(0));

		// Act
		verifier.verifNodeOrbitSizes(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifNodeOrbitSizesTooLargeRight() {
		// Arrange
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		rightRule1.getMatchNode("n7").setOrbit(new JerboaOrbit(0, 1, -1));

		// Act
		verifier.verifNodeOrbitSizes(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	/**
	 * @verifDuplicateDimension checks that no node have a duplicated dimension in
	 *                          its orbit and arcs.
	 */

	@Test
	public void testVerifDuplicateDimensionValid() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionLeftOrbit() {
		// Arrange : add a left node with duplicate dimension in orbit
		int initialErrorCount = errorList.size();
		JMENode hookNode = new JMENode(rule1.getLeft(), "hook", 0, 0, JMENodeKind.HOOK);
		hookNode.setOrbit(new JerboaOrbit(0, 0));
		rule1.getLeft().addNode(hookNode);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionLeftLoops() {
		// Arrange : add a left node with two loops of the same dimension
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
		hookNode.setOrbit(new JerboaOrbit(1, 2));
		leftRule1.addNode(hookNode);
		leftRule1.creatLoop(hookNode, 0);
		leftRule1.creatLoop(hookNode, 0);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionLeftLoopAndOrbit() {
		// Arrange : add a left node with a loop of a dimension present in the orbit
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
		hookNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(hookNode);
		leftRule1.creatLoop(hookNode, 0);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionLeftLoopAndArc() {
		// Arrange : add a left node with a loop and an arc of the same dimension
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
		hookNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(hookNode);
		JMENode otherNode = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		otherNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(otherNode);
		leftRule1.creatLoop(hookNode, 1);
		leftRule1.creatArc(hookNode, otherNode, 1);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionLeftArcAndArcSameNode() {
		// Arrange : add two left nodes with parallel arcs of the same dimension
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
		hookNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(hookNode);
		JMENode otherNode = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		otherNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(otherNode);
		leftRule1.creatArc(hookNode, otherNode, 1);
		leftRule1.creatArc(hookNode, otherNode, 1);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert: one error for each node
		assertEquals(initialErrorCount + 2, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionLeftArcAndArcDifferrentNode() {
		// Arrange : add three left nodes with a path of arcs of the same dimension
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
		hookNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(hookNode);
		JMENode otherNode = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		otherNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(otherNode);
		JMENode thirdNode = new JMENode(leftRule1, "third", 0, 0, JMENodeKind.SIMPLE);
		thirdNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(thirdNode);
		leftRule1.creatArc(hookNode, otherNode, 1);
		leftRule1.creatArc(hookNode, thirdNode, 1);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionLeftArcAndOrbit() {
		// Arrange : add a left node with an arc of a dimension present in the orbit
		int initialErrorCount = errorList.size();
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode hookNode = new JMENode(leftRule1, "hook", 0, 0, JMENodeKind.HOOK);
		hookNode.setOrbit(new JerboaOrbit(1, 2));
		leftRule1.addNode(hookNode);
		JMENode otherNode = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		otherNode.setOrbit(new JerboaOrbit(0, 2));
		leftRule1.addNode(otherNode);
		leftRule1.creatArc(hookNode, otherNode, 1);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert: one error for each node
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightOrbit() {
		// Arrange : add a right node with duplicate dimension in orbit
		int initialErrorCount = errorList.size();
		JMENode newNode = new JMENode(rule1.getRight(), "new", 0, 0, JMENodeKind.SIMPLE);
		newNode.setOrbit(new JerboaOrbit(0, 0));
		rule1.getRight().addNode(newNode);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightLoops() {
		// Arrange : add a right node with two loops of the same dimension
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
		newNode.setOrbit(new JerboaOrbit(1, 2));
		rightRule1.addNode(newNode);
		rightRule1.creatLoop(newNode, 0);
		rightRule1.creatLoop(newNode, 0);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightLoopAndOrbit() {
		// Arrange : add a right node with a loop of a dimension present in the orbit
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
		newNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(newNode);
		rightRule1.creatLoop(newNode, 0);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightLoopAndArc() {
		// Arrange : add a right node with a loop and an arc of the same dimension
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
		newNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(newNode);
		JMENode otherNode = new JMENode(rightRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		otherNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(otherNode);
		rightRule1.creatLoop(newNode, 1);
		rightRule1.creatArc(newNode, otherNode, 1);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightArcAndArcSameNode() {
		// Arrange : add two right nodes with parallel arcs of the same dimension
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
		newNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(newNode);
		JMENode otherNode = new JMENode(rightRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		otherNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(otherNode);
		rightRule1.creatArc(newNode, otherNode, 1);
		rightRule1.creatArc(newNode, otherNode, 1);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert: one error for each node
		assertEquals(initialErrorCount + 2, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightArcAndArcDifferrentNode() {
		// Arrange : add three right nodes with a path of arcs of the same dimension
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
		newNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(newNode);
		JMENode otherNode = new JMENode(rightRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		otherNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(otherNode);
		JMENode thirdNode = new JMENode(rightRule1, "third", 0, 0, JMENodeKind.SIMPLE);
		thirdNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(thirdNode);
		rightRule1.creatArc(newNode, otherNode, 1);
		rightRule1.creatArc(newNode, thirdNode, 1);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifDuplicateDimensionRightArcAndOrbit() {
		// Arrange : add a right node with an arc of a dimension present in the orbit
		int initialErrorCount = errorList.size();
		JMEGraph rightRule1 = rule1.getRight();
		JMENode newNode = new JMENode(rightRule1, "new", 0, 0, JMENodeKind.SIMPLE);
		newNode.setOrbit(new JerboaOrbit(1, 2));
		rightRule1.addNode(newNode);
		JMENode otherNode = new JMENode(rightRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		otherNode.setOrbit(new JerboaOrbit(0, 2));
		rightRule1.addNode(otherNode);
		rightRule1.creatArc(newNode, otherNode, 1);

		// Act
		verifier.verifDuplicateDimension(rule1, errorList);

		// Assert: one error for each node
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	/**
	 * @verifIncidentArc checks the incident arcs condition. In this context an
	 *                   incident arc is both an implicit arc (from the orbit) or an
	 *                   explicit arc (from the graph).
	 * 
	 *                   - A deleted node (from the left graph) must have all
	 *                   incident arcs. - A node preserved by the rule must have the
	 *                   same incident arcs. - A created node (in the right graph)
	 *                   must have all incident arcs.
	 * 
	 *                   Here we just check for the existence of the dimensions,
	 *                   possible duplicates are checked
	 *                   by @verifDuplicateDimension.
	 */

	@Test
	public void testVerifIncidentArcRule1() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifIncidentArc(rule1, errorList);

		// Assert: 8 errors should be added
		assertEquals(initialErrorCount + 8, errorList.size());
	}

	@Test
	public void testVerifIncidentArcDooSabin() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifIncidentArc(dooSabin, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifIncidentArcRemoveEdge() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifIncidentArc(removeEdge, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifIncidentArcDeleteIsolatedPrism() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifIncidentArc(deleteIsolatedPrism, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifIncidentErrorIACondition() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifIncidentArc(errorIACondition, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount + 2, errorList.size());
	}

	@Test
	public void testVerifIncidentArcPreservedRightNodeMissingIncidentExplicitArc() {
		// Arrange: missing explicit incident arc on right preserved node
		int initialErrorCount = errorList.size();
		JMEGraph rightDooSabin = dooSabin.getRight();
		JMENode n0Right = rightDooSabin.getMatchNode("n0");
		JMENode n1Right = rightDooSabin.getMatchNode("n1");
		JMEArc incidentArc = n0Right.alphas().get(0);
		int dimensionIncidentArc = incidentArc.getDimension();
		rightDooSabin.removeArc(incidentArc);
		// restore arc on the other node
		rightDooSabin.creatLoop(n1Right, dimensionIncidentArc);

		// Act
		verifier.verifIncidentArc(dooSabin, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcPreservedRightNodeMissingIncidentImplicitArc() {
		// Arrange: missing implicit incident arc on right preserved node
		int initialErrorCount = errorList.size();
		JMEGraph rightDooSabin = dooSabin.getRight();
		JMENode n0Right = rightDooSabin.getMatchNode("n0");
		n0Right.setOrbit(new JerboaOrbit(0, -1, -1));

		// Act
		verifier.verifIncidentArc(dooSabin, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcPreservedRightNodeMissingTwoIncidentImplicitArcs() {
		// Arrange: missing two implicit incident arcs on right preserved node
		int initialErrorCount = errorList.size();
		JMEGraph rightDooSabin = dooSabin.getRight();
		JMENode n0Right = rightDooSabin.getMatchNode("n0");
		n0Right.setOrbit(new JerboaOrbit(-1, -1, -1));

		// Act
		verifier.verifIncidentArc(dooSabin, errorList);

		// Assert
		assertEquals(initialErrorCount + 2, errorList.size());
	}

	@Test
	public void testVerifIncidentArcCreatedNodeMissingIncidentExplicitArc() {
		// Arrange: missing explicit incident arcs on right added node
		int initialErrorCount = errorList.size();
		JMEGraph rightDooSabin = dooSabin.getRight();
		JMENode n0Right = rightDooSabin.getMatchNode("n0");
		JMEArc incidentArc = n0Right.alphas().get(0);
		int dimensionIncidentArc = incidentArc.getDimension();
		rightDooSabin.removeArc(incidentArc);
		// restore arc on the other node
		rightDooSabin.creatLoop(n0Right, dimensionIncidentArc);

		// Act
		verifier.verifIncidentArc(dooSabin, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcCreatedNodeMissingIncidentImplicitArc() {
		// Arrange: missing implicit incident arcs on right added node
		int initialErrorCount = errorList.size();
		JMEGraph rightDooSabin = dooSabin.getRight();
		JMENode n2Right = rightDooSabin.getMatchNode("n2");
		n2Right.setOrbit(new JerboaOrbit(-1, -1, -1));

		// Act
		verifier.verifIncidentArc(dooSabin, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcPreservedLeftNodeMissingIncidentExplicitArc() {
		// Arrange: missing explicit incident arc on left preserved node
		int initialErrorCount = errorList.size();
		JMEGraph leftRemoveEdge = removeEdge.getLeft();
		JMENode n1Left = leftRemoveEdge.getMatchNode("n1");
		JMEArc incidentArc = n1Left.alphas().get(0);
		int dimensionIncidentArc = incidentArc.getDimension();
		leftRemoveEdge.removeArc(incidentArc);
		// restore arc on the other node
		leftRemoveEdge.creatLoop(n1Left, dimensionIncidentArc);

		// Act
		verifier.verifIncidentArc(removeEdge, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcDeletedHookMissingIncidentExplicitArc() {
		// Arrange: missing explicit incident arcs on left deleted node
		int initialErrorCount = errorList.size();
		JMEGraph leftRemoveEdge = removeEdge.getLeft();
		JMENode n0Left = leftRemoveEdge.getMatchNode("n0");
		JMEArc incidentArc = n0Left.alphas().get(0);
		int dimensionIncidentArc = incidentArc.getDimension();
		leftRemoveEdge.removeArc(incidentArc);
		// restore arc on the other node
		leftRemoveEdge.creatLoop(n0Left, dimensionIncidentArc);

		// Act
		verifier.verifIncidentArc(removeEdge, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcDeletedHookMissingIncidentImplicitArc() {
		// Arrange: missing implicit incident arcs on left deleted node
		int initialErrorCount = errorList.size();
		JMEGraph leftRemoveEdge = removeEdge.getLeft();
		JMENode n1Left = leftRemoveEdge.getMatchNode("n1");
		n1Left.setOrbit(new JerboaOrbit(-1, 2));
		n1Left.setKind(JMENodeKind.HOOK);

		// Act
		verifier.verifIncidentArc(removeEdge, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcDeletedNodeMissingIncidentExplicitArc() {
		// Arrange: missing explicit incident arcs on left deleted node
		int initialErrorCount = errorList.size();
		JMEGraph leftDeleteIsolatedPrism = deleteIsolatedPrism.getLeft();
		JMENode n5Left = leftDeleteIsolatedPrism.getMatchNode("n5");
		JMEArc incidentArc = n5Left.alphas().get(0);
		int dimensionIncidentArc = incidentArc.getDimension();
		leftDeleteIsolatedPrism.removeArc(incidentArc);
		// restore arc on the other node
		leftDeleteIsolatedPrism.creatLoop(n5Left, dimensionIncidentArc);
		// add hook
		n5Left.setKind(JMENodeKind.HOOK);

		// Act
		verifier.verifIncidentArc(deleteIsolatedPrism, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcDeletedNodeMissingIncidentImplicitArc() {
		// Arrange: missing implicit incident arcs on left deleted node
		int initialErrorCount = errorList.size();
		JMEGraph leftDeleteIsolatedPrism = deleteIsolatedPrism.getLeft();
		JMENode n5Left = leftDeleteIsolatedPrism.getMatchNode("n5");
		n5Left.setOrbit(new JerboaOrbit(-1, 1));

		// Act
		verifier.verifIncidentArc(deleteIsolatedPrism, errorList);

		// Assert
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcPreservedLeftNodeMissingIncidentImplicitArc() {
		// Arrange
		verifier.verifIncidentArc(errorIACondition, errorList);
		int initialErrorCount = errorList.size();
		JMEGraph left = errorIACondition.getLeft();
		JMENode node7 = left.getMatchNode("n7");
		node7.setOrbit(new JerboaOrbit(-1));

		// Act
		verifier.verifIncidentArc(errorIACondition, errorList);

		// Assert: no error should be added
		assertEquals(2 * initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifIncidentArcPreservedLeftNodeMissingIncidentImplicitArcDeletion() {
		// Arrange
		verifier.verifIncidentArc(errorIACondition, errorList);
		int initialErrorCount = errorList.size();
		errorIACondition.getLeft().getMatchNode("n7").setOrbit(new JerboaOrbit());

		// Act
		verifier.verifIncidentArc(errorIACondition, errorList);

		// Assert: no error should be added
		assertEquals(2 * initialErrorCount + 1, errorList.size());
	}

	/**
	 * @verifIncidentArc determines whether a node has a cycle. The arcs are both
	 *                   considered as implicit and explicit arcs.
	 * 
	 *                   A cycle is valid if it is: - made only of explicit arcs -
	 *                   made of both implicit and explicit arcs if the implicit
	 *                   arcs are at the same position in the orbit - made only of
	 *                   implicit arcs in a left node - made only of implicit arcs
	 *                   in a right node with position describing cycles in
	 *
	 */

	@Test
	public void testNoArcNoOrbitNoCycle() {
		// Arrange: node without arc and orbit
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testIJInLeftOrbit() {
		// Arrange: i and j in left orbit
		JMENode node = rule1.getLeft().getMatchNode("n0");

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testIOrbitJArcIInAdjacentOrbit() {
		// Arrange: i in orbit and j arc with i in the adjacent orbit
		JMENode node = deleteIsolatedPrism.getLeft().getMatchNode("n0");

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testIOrbitJArcIInAdjacentOrbitWrongPosition() {
		// Arrange: i in left orbit and j arc with i in the adjacent orbit
		JMENode node = deleteIsolatedPrism.getLeft().getMatchNode("n0");
		JMENode node1 = deleteIsolatedPrism.getLeft().getMatchNode("n1");
		node1.setOrbit(new JerboaOrbit(-1, 0));

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testIOrbitJArcINotInAdjacentOrbit() {
		// Arrange: i in left orbit and j arc with i in the adjacent orbit
		JMENode node = deleteIsolatedPrism.getLeft().getMatchNode("n0");
		JMENode node1 = deleteIsolatedPrism.getLeft().getMatchNode("n1");
		node1.setOrbit(new JerboaOrbit(-1, -1));

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testJOrbitIArcJInAdjacentOrbit() {
		// Arrange: i in left orbit and j arc with i in the adjacent orbit
		JMENode node = deleteIsolatedPrism.getLeft().getMatchNode("n2");

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testJOrbitIArcJInAdjacentOrbitWrongPosition() {
		// Arrange: i in left orbit and j arc with i in the adjacent orbit
		JMENode node = deleteIsolatedPrism.getLeft().getMatchNode("n2");
		JMENode node3 = deleteIsolatedPrism.getLeft().getMatchNode("n3");
		node3.setOrbit(new JerboaOrbit(2, -1));

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testJOrbitIArcJNotInAdjacentOrbit() {
		// Arrange: i in left orbit and j arc with i in the adjacent orbit
		JMENode node = deleteIsolatedPrism.getLeft().getMatchNode("n2");
		JMENode node3 = deleteIsolatedPrism.getLeft().getMatchNode("n3");
		node3.setOrbit(new JerboaOrbit(-1, -1));

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testBothArcsWithCycle() {
		// Arrange: i and j as left arcs
		JMENode node = rule1.getLeft().getMatchNode("n1");

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testLoopsWithCycle() {
		// Arrange
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		leftRule1.creatLoop(node, 0);
		leftRule1.creatLoop(node, 2);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testLoopsWithoutCycle() {
		// Arrange
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		leftRule1.creatLoop(node, 0);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testIArcJLoopCycle() {
		// Arrange:
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		leftRule1.creatLoop(node, 2);
		JMENode adjacent = new JMENode(leftRule1, "adjacent", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(adjacent);
		leftRule1.creatLoop(adjacent, 2);
		leftRule1.creatArc(node, adjacent, 0);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testIArcJLoopNoCycle() {
		// Arrange:
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		leftRule1.creatLoop(node, 2);
		JMENode adjacent = new JMENode(leftRule1, "adjacent", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(adjacent);
		leftRule1.creatArc(node, adjacent, 0);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testJArcILoopCycle() {
		// Arrange:
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		leftRule1.creatLoop(node, 0);
		JMENode adjacent = new JMENode(leftRule1, "adjacent", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(adjacent);
		leftRule1.creatLoop(adjacent, 0);
		leftRule1.creatArc(node, adjacent, 2);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testJArcILoopNoCycle() {
		// Arrange:
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		leftRule1.creatLoop(node, 0);
		JMENode adjacent = new JMENode(leftRule1, "adjacent", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(adjacent);
		leftRule1.creatArc(node, adjacent, 2);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testILoopJOrbitWithCycle() {
		// Arrange
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		node.setOrbit(new JerboaOrbit(-1, 2));
		leftRule1.addNode(node);
		leftRule1.creatLoop(node, 0);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testJLoopIOrbitWithCycle() {
		// Arrange
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		node.setOrbit(new JerboaOrbit(-1, 0));
		leftRule1.addNode(node);
		leftRule1.creatLoop(node, 2);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testIJArcsNoCycle() {
		// Arrange:
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		JMENode adjacent = new JMENode(leftRule1, "adjacent", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(adjacent);
		JMENode other = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(other);
		leftRule1.creatArc(node, adjacent, 0);
		leftRule1.creatArc(node, other, 2);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testIJArcsNoCyclePath3IJI() {
		// Arrange:
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		JMENode adjacent = new JMENode(leftRule1, "adjacent", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(adjacent);
		JMENode other = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(other);
		JMENode opposite = new JMENode(leftRule1, "opposite", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(opposite);
		leftRule1.creatArc(node, adjacent, 0);
		leftRule1.creatArc(node, other, 2);
		leftRule1.creatArc(other, opposite, 0);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testIJArcsNoCyclePath3JIJ() {
		// Arrange
		JMEGraph leftRule1 = rule1.getLeft();
		JMENode node = new JMENode(leftRule1, "node", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(node);
		JMENode adjacent = new JMENode(leftRule1, "adjacent", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(adjacent);
		JMENode other = new JMENode(leftRule1, "other", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(other);
		JMENode opposite = new JMENode(leftRule1, "opposite", 0, 0, JMENodeKind.SIMPLE);
		leftRule1.addNode(opposite);
		leftRule1.creatArc(node, adjacent, 2);
		leftRule1.creatArc(node, other, 0);
		leftRule1.creatArc(other, opposite, 2);

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);
	}

	@Test
	public void testImpossibleToDetermine() {
		// Arrange
		JMERuleAtomic testRule = new JMERuleAtomic(helper.getModeler(), "Tester");
		JMENode node = new JMENode(testRule.getRight(), "node", 0, 0, JMENodeKind.SIMPLE);
		testRule.getRight().addNode(node);
		node.setOrbit(new JerboaOrbit(0, 2));

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(-1, result);
	}

	@Test
	public void testIJOrbitRightHasCycle() {
		// Arrange
		JMENode node = new JMENode(dooSabin.getRight(), "node", 0, 0, JMENodeKind.SIMPLE);
		dooSabin.getRight().addNode(node);
		node.setOrbit(new JerboaOrbit(0, -1, 2));

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(1, result);
	}

	@Test
	public void testIJOrbitRightNoCycle() {
		// Arrange
		JMENode node = new JMENode(dooSabin.getRight(), "node", 0, 0, JMENodeKind.SIMPLE);
		dooSabin.getRight().addNode(node);
		node.setOrbit(new JerboaOrbit(0, 2, -1));

		// Act
		int result = verifier.hasCycle(node, 0, 2);

		// Assert
		assertEquals(0, result);

	}

	/**
	 * verifCycle checks the cycle condition. In this context an incident arc is
	 * both an implicit arc (from the orbit) or an explicit arc (from the graph).
	 * 
	 * - A preserved node source of a cycle in the left graph must be the source of
	 * a cycle in the right graph. - A preserved node not source of a cycle must
	 * preserve its incident arcs. - A created node (in the right graph) must be
	 * incident to a cycle.
	 * 
	 * Here we just check for the existence of the dimensions, possible duplicates
	 * are checked by @verifDuplicateDimension.
	 */

	@Test
	public void testVerifCycleRule1() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifCycle(rule1, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifCycleDooSabin() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifCycle(dooSabin, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifICycleRemoveEdge() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifIncidentArc(removeEdge, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifCycleIsolatedPrism() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifIncidentArc(deleteIsolatedPrism, errorList);

		// Assert: no error should be added
		assertEquals(initialErrorCount, errorList.size());
	}

	@Test
	public void testVerifCycleNoCycleCreatedNode() {
		// Arrange: Add an isolated node (no cycle)
		int initialErrorCount = errorList.size();
		JMENode node = new JMENode(dooSabin.getRight(), "node", 0, 0, JMENodeKind.SIMPLE);
		dooSabin.getRight().addNode(node);

		// Act
		verifier.verifCycle(dooSabin, errorList);

		// Assert: one error should be added
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifCyclePreservedNodeModifOrbit() {
		// Arrange: preserved node without cycle and with modified orbit
		int initialErrorCount = errorList.size();
		removeEdge.getLeft().getMatchNode("n0").setOrbit(new JerboaOrbit(-1, 2));
		removeEdge.getRight().getMatchNode("n0").setOrbit(new JerboaOrbit(2, -1));

		// Act
		verifier.verifCycle(removeEdge, errorList);

		// Assert: one error should be added
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifCyclePreservedNodeModifIncident() {
		// Arrange: preserved node without cycle and with modified arcs
		int initialErrorCount = errorList.size();
		JMENode n0l = removeEdge.getLeft().getMatchNode("n0");
		JMENode n2l = new JMENode(removeEdge.getLeft(), "added", 0, 0, JMENodeKind.SIMPLE);
		removeEdge.getLeft().addNode(n2l);
		removeEdge.getLeft().creatArc(n0l, n2l, 2);
		JMENode n0r = removeEdge.getRight().getMatchNode("n0");
		removeEdge.getRight().creatLoop(n0r, 2);

		// Act
		verifier.verifCycle(removeEdge, errorList);

		// Assert: one error should be added
		assertEquals(initialErrorCount + 1, errorList.size());
	}

	@Test
	public void testVerifCycleErrorRule() {
		// Arrange
		int initialErrorCount = errorList.size();

		// Act
		verifier.verifCycle(errorCycleCondition, errorList);

		// Assert: one error should be added
		assertEquals(initialErrorCount + 4, errorList.size());
	}

}
