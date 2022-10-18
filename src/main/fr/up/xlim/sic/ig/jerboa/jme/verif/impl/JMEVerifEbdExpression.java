package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorSeverity;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorType;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEVerifIterator;
import up.jerboa.core.JerboaOrbit;
import up.jerboa.core.util.Pair;

/**
 * @author Hakim
 *
 */
public class JMEVerifEbdExpression implements JMEVerifIterator {

	public JMEVerifEbdExpression() {

	}

	@Override
	public Collection<JMEError> check(JMEElementWindowable element) {
		// System.out.println("check Verif EBD: " + element);
		Collection<JMEError> errList = new ArrayList<>();
		if (element instanceof JMERule) {
			JMERule r = ((JMERule) element);
			checkOmitExpr(r,errList);
			checkDoubleExpr(r,errList);
		}
		return errList;
	}



	private void checkDoubleExpr(JMERule r, Collection<JMEError> errList) {
		
		JMEGraph left = r.getLeft();
		JMEGraph right = r.getRight();
		JMEModeler modeler = r.getModeler();
		
		List<JMEEmbeddingInfo> ebds = modeler.getEmbeddings();
		
		// for(JMEEmbeddingInfo ebd : ebds) 
		ebds.parallelStream().forEach(ebd -> 
			{
				JerboaOrbit orbit = ebd.getOrbit();
				
				for (JMENode n : right.getNodes()) {
					// si le n est un noeud conserve alors
					// 	si le n est relie a un noeud existant
					//	 ils doivent etre sur la meme orbite de ebd
					// 	sinon doublon
					// si c nouveau neoud alors
					// on verifie si connecte a 
					JMENode  lnode = left.getMatchNode(n);
					if(lnode != null) {
						Set<JMENode> nodes = right.orbit(n, orbit);
						Set<JMENode> lnodes = nodes.stream().map(node -> left.getMatchNode(node)).filter(o -> o != null).collect(Collectors.toSet());
						List<JMEError> errors = lnodes.stream().map(ln -> new Pair<>( left.orbit(ln, orbit) , ln) ).filter(pair -> !pair.l().contains(lnode))
							.map(pair -> new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.EMBEDDING, r, n,  "expression needed for ebd: " + ebd.getName() ))
							.collect(Collectors.toList());
						errList.addAll(errors);
					}
				}
			}
		);
	}

	private void checkOmitExpr(JMERule r, Collection<JMEError> errList) {
		for (JMENode n : r.getRight().getNodes()) {
			
			for (JMENodeExpression e : n.getRequiredExprs()) {
				JMEError err = new JMEError(JMEErrorSeverity.WARNING, JMEErrorType.EMBEDDING, r, n, "Missed required expression on "+n.getName()+" for embedding: " + e.getEbdInfo().getName());
				errList.add(err);
			}
		}
	}

}
