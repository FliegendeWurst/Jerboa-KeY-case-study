package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

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
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorSeverity;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorType;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEVerifIterator;
import up.jerboa.core.JerboaOrbit;

// Verifications topo
public class JMEVerifTopoClassic implements JMEVerifIterator {		

	@Override
	public Collection<JMEError> check(JMEElementWindowable element) {
		ArrayList<JMEError> errors = new ArrayList<>();
		if(element instanceof JMERule) {
			JMERule rule = (JMERule)element;
			try { verifDimension(rule, errors); } catch(Throwable t) { }
			try { verifDuplicateNode(rule, errors);  } catch(Throwable t) { }
			try { verifHooks(rule, errors); } catch(Throwable t) { }
			try { verifNodeNumberOrbits(rule, errors); } catch(Throwable t) { }
			try { verifDuplicateDimension(rule, errors); } catch(Throwable t) { }
			//try { verifIncidentArc(rule,errors); } catch(Throwable t) { }
			try { verifCycle(rule, errors); } catch(Throwable t) { }
		}
		return errors;

	}

	
	/**
	 * Verifie si les dimensions sont comprises entre :
	 * 		-1 et la dimension du modeleur pour les orbites ;
	 * 		 0 et la dimension du modeleur pour les arcs.
	 * 
	 * On realise pour le graphe gauche et le graphe droit, un parcours des noeuds et, pour chaque noeud, on 
	 * verifie les valeurs des dimensions dans l'orbite. On realise ensuite un parcours des arcs et, pour
	 * chaque arc, on verifie la valeur de sa dimension
	 * 
	 * A noter qu'une erreur de dimension sur un arc sera reportee sur les deux noeuds.
	 * 
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	private void verifDimension(JMERule rule, ArrayList<JMEError> errors) {
		int modDim = rule.getModeler().getDimension();
		
		// Graphe Gauche
		JMEGraph left = rule.getLeft();
		
		// Orbites
		for (JMENode node : left.getNodes()) {
			for(int i : node.getOrbit().tab()) {
				if(i > modDim || i < -1)
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, node,
							node.getName() + " has orbit variable with wrong dimension in the left graph: " + i));	
			}
		}
		
		// Arcs
		for(JMEArc arc : left.getArcs()) {
			if(arc.getDimension() > modDim || arc.getDimension() < 0) {
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, arc,
						arc.toString() + " has wrong dimension in the left graph: " + arc.getDimension()));
			}
		}
		

		// Graphe Droit
		JMEGraph right = rule.getRight();
		
		// Orbites
		for (JMENode node : right.getNodes()) {
			for(int i : node.getOrbit().tab()) {
				if(i > modDim || i < -1)
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, node,
							node.getName() + " has orbit variable with wrong dimension in the right graph: " + i));	
			}
		}	
		
		// Arc
		for(JMEArc arc : right.getArcs()) {
			if(arc.getDimension() > modDim || arc.getDimension() < 0) {
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, arc,
						arc.toString() + " has wrong dimension in the right graph: " + arc.getDimension()));
			}
		}
	}

	/**
	 * Verifie qu'il n'existe pas de doublon dans les noms des noeuds dans chacun des graphe.
	 * 
	 * On utilise une HashMap qui reference les noms des noeuds. Lorsqu'un nom est deja present, une erreur est
	 * ajoutee sur chacun des deux noeuds.

	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	private void verifDuplicateNode(JMERule rule, ArrayList<JMEError> errors) {
		
		// Graphe Gauche
		JMEGraph left = rule.getLeft();
		HashMap<String,JMENode> existingNamesLeft = new HashMap<>();
		for (JMENode node : left.getNodes()) {
			
			// Le nom est deja present, on ajoute des erreurs.
			if (existingNamesLeft.containsKey(node.getName())) {
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, node,
						"Duplicate nodes in the left graph: " + node.getName()));
				JMENode otherNode = existingNamesLeft.get(node.getName());
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, otherNode,
						"Duplicate nodes in the left graph: " + node.getName()));
			}
			
			// Le nom n'est pas present, on l'ajoute dans la HashMap.
			else
				existingNamesLeft.put(node.getName(),node);
		}
		
		// Graphe Droit
		JMEGraph right = rule.getRight();
		HashMap<String,JMENode> existingNamesRight = new HashMap<>();
		for (JMENode node : right.getNodes()) {
			
			// Le nom est deja present, on ajoute des erreurs.
			if (existingNamesRight.containsKey(node.getName())) {
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, node,
						"Duplicate nodes in the right graph: " + node.getName()));
				JMENode otherNode = existingNamesRight.get(node.getName());
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, otherNode,
						"Duplicate nodes in the right graph: " + node.getName()));
			}
			
			// Le nom n'est pas present, on l'ajoute dans la HashMap.
			else
				existingNamesRight.put(node.getName(),node);
		} 
	}
	
	/**
	 * Verifie que tous les noeuds du graphe gauche sont relies à un unique hook et que chaque hook possede une orbite
	 * pleine, c'est-a-dire qu'aucune dimension n'est vide.
	 * 
	 * On realise tout d'abord un parcours des hooks pour verifier que -1 n'est pas dans leur orbite.
	 * 
	 * Pour verifier que chaque noeud est relie a un unique hook, on construit sa composante connexe puis,
	 * a l'aide d'un stream, on compte le nombre de hook dans la composante connexe et on en deduit les eventuelles
	 * erreurs.

	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	private void verifHooks(JMERule rule, ArrayList<JMEError> errors){
		List<JMENode> hooks = rule.getHooks();
		JMEGraph left = rule.getLeft();
		
		// Verification d'orbite pleine pour les hooks
		for(JMENode hook : hooks){
			if(hook.getOrbit().contains(-1)){
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, hook,
						"A hook '"+hook.getName()+"' must have a full orbit."));
			}
		} 
		
		// preparation de l'orbite composante connexe pour la dimension choisie
		int[] tabOrbitConnexComponent = new int[rule.getModeler().getDimension() + 1];
		for(int i = 0;i < tabOrbitConnexComponent.length; i++)
			tabOrbitConnexComponent[i] = i;
		JerboaOrbit orbitCC = JerboaOrbit.orbit(tabOrbitConnexComponent);
		
		// Traitement de chaque noeud
		for (JMENode node : left.getNodes()) {
			Set<JMENode> nodes = left.orbit(node, orbitCC);
			int countHook = nodes.stream().mapToInt(f -> (hooks.contains(f)? 1 : 0)).sum();
			if(countHook > 1) {
				if(hooks.contains(node))
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, node,
							"Hook '"+node.getName()+"' is connected to another hook."));
				else
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, node,
							"Node '"+node.getName()+"' is connected to "+countHook+" hooks"));
			}
			else if(countHook == 0) {
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, node,
						"Node '"+node.getName()+"' must be connected to exactly one hook"));
			}
		}
	}
	
	/**
	 * Verifie que les orbites sont tous de même taille. Un Hook est pris pour référence, si possible.
	 * 
	 * On commence par chercher un noeud de référence (si possible un hook dans le graphe gauche, sinon un noeud
	 * quelconque dans le graphe droit.
	 * 
	 * Pour chaque noeud dans chacun des graphes, on compare ensuite la longueur de l'orbite a la longueur de l'orbite
	 * du noeud reference.
	 * 
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	private void verifNodeNumberOrbits(JMERule rule, ArrayList<JMEError> errors){
		int length;
		JMENode node = null;
		String graph = "";
		if(rule.getHooks().isEmpty()){
			if(rule.getRight().getNodes().isEmpty())
				return ;
			else 
				node = rule.getRight().getNodes().get(0);
				graph = "right";
		} else
			node = rule.getHooks().get(0);
			graph = "left";
		length = node.getOrbit().size();
		
		// Graphe gauche
		for (JMENode n : rule.getLeft().getNodes()){
			
			// Orbite trop petite
			if(n.getOrbit().size() < length)
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, n,
						"Node in left graph is missing dimensions in orbit: " + n.getName()
						+ " (with respect to node: " + node.getName() + " in " + graph + " graph)"));
			
			// Orbite trop grande
			else if(n.getOrbit().size() > length)
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, n,
						"Node in left graph has too many dimensions in orbit: " + n.getName()
						+ " (with respect to node: " + node.getName() + " in " + graph + " graph)"));
		}
		// Graphe droit
		for (JMENode n : rule.getRight().getNodes()){
			
			// Orbite trop petite
			if(n.getOrbit().size() < length)
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, n,
						"Node in right graph is missing dimensions in orbit: " + n.getName()
						+ " (with respect to node: " + node.getName() + " in " + graph + " graph)"));
			
			// Orbite trop grande
			else if(n.getOrbit().size() > length)
				errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, n,
						"Node in right graph has too many dimensions in orbit: " + n.getName()
						+ " (with respect to node: " + node.getName() + " in " + graph + " graph)"));
		}
		
	}

	/**
	 * Verifie qu'une dimension n'apparait pas deux fois sur un noeud (dans l'orbite et dans les arcs)
	 * 
	 * Pour chaque noeud, on utilise un HashSet dans lequel on ajoute les dimensions rencontrees dans l'orbite et
	 * lors du parcours des arcs. Si une dimension differente de -1 est deja presente, on ajoute une erreur dans la
	 * liste.
	 * 
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	private void verifDuplicateDimension(JMERule rule, ArrayList<JMEError> errors) {
		
		// Graphe Gauche
		for (JMENode node : rule.getLeft().getNodes()){
			HashSet<Integer> dims = new HashSet<>();
			
			// Orbite
			for(int i : node.getOrbit()){
				if(!dims.add(i) && i != -1)
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, node,
							node.getName() + " has multiple " + i + "-edge in left graph"));
			}
			
			// Arcs
			for(JMEArc a : rule.getLeft().getIncidentArcsFromNode(node)){
				if(!dims.add(a.getDimension()))
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, node,
							node.getName() + " has multiple " + a.getDimension() + "-edge in left graph"));
			}
			
		// Graphe Droit
		} for (JMENode node : rule.getRight().getNodes()){
			HashSet<Integer> dims = new HashSet<>();
			
			// Orbite
			for(int i : node.getOrbit()){
				if(!dims.add(i) && i != -1)
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, node,
							node.getName() + " has multiple " + i + "-edge in right graph"));
			}
			
			// Arcs
			for(JMEArc a : rule.getRight().getIncidentArcsFromNode(node)){
				if(!dims.add(a.getDimension()))
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC, rule, node,
						node.getName() + " has multiple " + a.getDimension() + "-edge in right graph"));
			}
		}
	}
	
	/**
	 * Verifie la contrainte d'arc incident sur la règle :
	 * 		un noeud supprime du graphe gauche doit posseder toutes les dimensions (dans son orbite ou dans ses arcs)
	 * 		un noeud conserve dans par la regle doit posseder les memes dimensions dans le graphe gauche que dans le
	 * 			grape droit
	 * 		un noeud ajoute du graphe droit doit posseder toutes les dimensions (dans son orbite ou dans ses arcs)
	 * 
	 * Dans le cas d'un noeud ajoute ou supprime, on verifie que toutes le dimensions comprises entre 0 et la dimension
	 * du modeleur non presentes dans l'orbite le sont dans les arcs.
	 * 
	 * Dans le cas d'un noeud conserve par la regle, on verifie dans chaque sens : 
	 * 		qu'une dimension presente dans l'orbite est
	 * 			soit -1 ;
	 * 			soit presente au meme indice dans l'orbite du noeud correspondant dans l'autre graphe ;
	 * 			soit presente dans un arc relie au noeud correpondant dans l'autre graphe.
	 * 
	 *		qu'une dimension presente dans un arc relie au noeud est
	 *			soit presente dans l'orbite du noeud correspondant dans l'autre graphe ;
	 * 			soit presente dans un arc relie au noeud correpondant dans l'autre graphe.
	 * 
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	private void verifIncidentArc(JMERule rule, ArrayList<JMEError> errors) {
		int modDim = rule.getModeler().getDimension();
		JMEGraph left = rule.getLeft();
		JMEGraph right = rule.getRight();
		for (JMENode leftNode : left.getNodes()){
			JMENode rightNode = right.getMatchNode(leftNode);
			
			// Noeud supprime : on verifie qu'il possede toutes les dimensions
			if (rightNode == null){
				List<JMEArc> incidents = left.getIncidentArcsFromNode(leftNode);
				for (int i = 0; i<= modDim; i++){
					if (!leftNode.getOrbit().contains(i)){
						boolean flag = false;
						for (JMEArc a : incidents)
							flag |= a.getDimension()==i;
						if(!flag)
							errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, leftNode,
									" Left node deleted without " + i + "-edge: " + leftNode.getName()));
					}
				}
			}else {
				
				// Left -> Right
				// Dimensions presentes dans l'orbite
				for(int i = 0; i < leftNode.getOrbit().size(); i++){
					boolean flag = false;
					int dim = leftNode.getOrbit().get(i);
					
					// Test d'egalite a -1
					flag |= dim == -1;
					
					// Test de position identique dans l'orbite
					flag |= (rightNode.getOrbit().contains(dim))
							&& (dim == rightNode.getOrbit().get(i));
					
					// Test des arcs
					for(JMEArc a : rule.getRight().getIncidentArcsFromNode(rightNode)){
						flag |= (dim == a.getDimension());
					} if (!flag)
						errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, leftNode,
								dim + "-edge deleted from left to right: " + leftNode.getName()));
				}
				
				// Dimensions presentes dans les arcs
				for(JMEArc arc : rule.getLeft().getIncidentArcsFromNode(leftNode)){
					boolean flag = false;
					int dim = arc.getDimension();
					
					// Test d'appartenance dans l'orbite
					flag |= rightNode.getOrbit().contains(dim);
					
					// Test des arcs
					for(JMEArc a : rule.getRight().getIncidentArcsFromNode(rightNode)){
						flag |= (dim == a.getDimension());
					} if (!flag)
						errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, leftNode,
								dim + "-edge deleted from left to right: " + leftNode.getName()));
				}
				
				// Right -> Left
				// Dimensions presentes dans l'orbite
				for(int i = 0; i < rightNode.getOrbit().size(); i++){
					boolean flag = false;
					int dim = rightNode.getOrbit().get(i);
					
					// Test d'egalite a -1
					flag |= dim == -1;
					
					// Test de position identique dans l'orbite
					flag |= (leftNode.getOrbit().contains(dim))
							&& (dim == leftNode.getOrbit().get(i));
					
					// Test des arcs
					for(JMEArc a : rule.getLeft().getIncidentArcsFromNode(leftNode)){
						flag |= (dim == a.getDimension());
					} if (!flag)
						errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, rightNode,
								dim + "-edge added from left to right: " + rightNode.getName()));
				}
				
				// Dimensions presentes dans les arcs
				for(JMEArc arc : rule.getRight().getIncidentArcsFromNode(rightNode)){
					boolean flag = false;
					int dim = arc.getDimension();
					
					// Test d'appartenance dans l'orbite
					flag |= leftNode.getOrbit().contains(dim);
					
					// Test des arcs
					for(JMEArc a : rule.getLeft().getIncidentArcsFromNode(leftNode)){
						flag |= (dim == a.getDimension());
					} if (!flag)
						errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, rightNode,
								dim + "-edge added from left to right: " + rightNode.getName()));
				}
			}
		} for (JMENode rightNode : right.getNodes()){
			JMENode leftNode = left.getMatchNode(rightNode);
			// Noeud ajoute : on vérifie qu'il possede toutes les dimensions
			if (leftNode == null){
				List<JMEArc> incidents = right.getIncidentArcsFromNode(rightNode);
				for (int i = 0; i<= modDim; i++){
					if (!rightNode.getOrbit().contains(i)){
						boolean flag = false;
						for (JMEArc a : incidents){
							if(a.getDimension()==i)
								flag = true;
						} if(!flag)
							errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, rightNode,
									" Right node added without " + i + "-edge: " + rightNode.getName()));
					}
				}
			}
		}
		
	}
	
	/**
	 * Permet de determiner si un noeud possede un cycle ijij.
	 * On suppose que i+2 <= j
	 * 
	 * @param node Noeud de la regle pour lequel on cherche s'il existe un cycle ijij
	 * @param i plus petite dimension du cycle.
	 * @param j plus grande dimension du cycle
	 * @return 1 si le noeud possede un cycle ijij, 0 s'il n'en possede pas et -1 s'il est impossible 
	 * 		de determiner si le noeud possede un cycle (i et j dans l'orbite avec un graphe gauche vide)
	 */
	private int hasCycle(JMENode node, int i, int j){
		/*
		 * Dans un premier temps, on détermine les arêtes i et j sont implicites ou explicites (distinction
		 * arc, boucle)
		 * En fonction, on associe un attribut qui permet de retrouver la configuration.
		 * On traite séparement les différentes configurations :
		 * 		- orbite/boucle et boucle/boucle => cycle
		 * 		- arc/orbite => on regarde la position dans l'orbite du noeud adjacent
		 * 		- arc/boucle => on regarde les boucles du noeud adjacent
		 * 		- orbite/orbite =>
		 * 					dans le graphe gauche : cycle
		 * 					dans le graphe droit : on compare avec un noeud du graphe gauche
		 * 		- arc/arc => on regarde si on peut construire un cycle à l'aide d'un troisième noeud
		 * 
		 * 	|-----------------------------------------------------------------------------------------------------------|
		 * 	|											| Dimension j													|
		 * 	|											|---------------------------------------------------------------|
		 * 	|											| Absente	| Dans l'orbite	| Dans une boucle	| Dans un arc	|
		 * 	|											|---------------------------------------------------------------|
		 * 	|											| 		0	|			1	|				2	|			3	|
		 * 	|-------------------------------------------|---------------------------------------------------------------|
		 * 	| Dimension i 	| Absente			|     0	|		0	|			1	|				2	|			3	|
		 * 	|				|-------------------|-------|-----------|---------------|-------------------|---------------|
		 * 	| 				| Dans l'orbite		|     4	|		4	|			5	|				6	|			7	|
		 * 	|				|-------------------|-------|-----------|---------------|-------------------|---------------|
		 * 	|				| Dans une boucle	|     8	|		8	|			9	|			   10	|		   11	|
		 * 	|				|-------------------|-------|-----------|---------------|-------------------|---------------|
		 * 	|				| Dans un arc		|    12	|	   12	|		   13	|			   14	|		   15	|
		 * 	|-----------------------------------------------------------------------------------------------------------|
		 */
		
		boolean flag = false;
		int cycleCode = 0;
		JMEGraph graph = node.getGraph();
		JerboaOrbit orbit = node.getOrbit();
		JMEArc arc_i = null;
		JMEArc arc_j = null;
		for(JMEArc arc : graph.getIncidentArcsFromNode(node)){
			arc_i = (arc.getDimension() == i) ? arc : arc_i;
			arc_j = (arc.getDimension() == j) ? arc : arc_j;
			
		} // Cas de l'arc j
		if (orbit.contains(j)) // orbite
			cycleCode += 1;
		if (arc_j instanceof JMELoop) // boucle 
			cycleCode += 2;
		else if (arc_j instanceof JMEArc) // arc
			cycleCode += 3;
		
		// Cas de l'arc i
		if (orbit.contains(i)) // orbite
			cycleCode += 4;	
		if (arc_i instanceof JMELoop) // boucle
			cycleCode += 8;
		else if (arc_i instanceof JMEArc) // arc
			cycleCode += 12;
		
		flag |= cycleCode == 6; // boucle/orbite
		flag |= cycleCode == 9; // orbite/boucle
		flag |= cycleCode == 10; // boucle/boucle

		// Dans les autres cas, on a besoin de realiser une comparation avec un autre noeud
		JMENode otherNode, hook, node_i, node_j, node_cycle;
		int pos_i, pos_j ;
		
		switch (cycleCode){
			// 0,1,2,3,4,8,12 -> i ou j manquant ; 15 -> i et j arcs
		
			// j arc et i orbite
			case 7 : 
				otherNode  = (arc_j.getSource() == node) ? arc_j.getDestination() : arc_j.getSource();
				flag |= orbit.indexOf(i) == otherNode.getOrbit().indexOf(i);
				break ;
			
			// i arc et j orbite
			case 13 : 
				otherNode = (arc_i.getSource() == node) ? arc_i.getDestination() : arc_i.getSource();
				flag |= orbit.indexOf(j) == otherNode.getOrbit().indexOf(j);
				break;
				
			// i arc et j boucle
			case 14 : 
				otherNode = (arc_i.getSource() == node) ? arc_i.getDestination() : arc_i.getSource();
				for (JMEArc arc : graph.getIncidentArcsFromNode(otherNode))
					flag |= arc instanceof JMELoop && arc.getDimension()==j ;
				break;
			
			// j arc et i boucle
			case 11 : 
				otherNode  = (arc_j.getSource() == node) ? arc_j.getDestination() : arc_j.getSource();
				for (JMEArc arc : graph.getIncidentArcsFromNode(otherNode))
					flag |= arc instanceof JMELoop && arc.getDimension()==i ;
				break;

			// i et j orbite
			case 5 :
				if(graph.isLeft())
					return 1;
				try { // on test i+2 <= j en comparant dans le graphe gauche
					hook = graph.getRule().getLeft().getHooks().get(0);
				} catch(IndexOutOfBoundsException e){
					return -1;
				}
				pos_i = orbit.indexOf(i);
				pos_j = orbit.indexOf(j);
				flag |= hook.getOrbit().get(pos_i) + 2 <= hook.getOrbit().get(pos_j)
						|| hook.getOrbit().get(pos_j) + 2 <= hook.getOrbit().get(pos_i);
				break;
				
			// i et j arcs
			case 15 :
				node_cycle = null ;
				node_i = (arc_i.getSource() == node) ? arc_i.getDestination() : arc_i.getSource();
				node_j = (arc_j.getSource() == node) ? arc_j.getDestination() : arc_j.getSource();
				
				// on cherche le quatrieme noeud pour faire le cycle
				for (JMEArc arc : graph.getIncidentArcsFromNode(node_i)){
					if ( (!(arc instanceof JMELoop)) && arc.getDimension() == j)
						node_cycle = (arc.getSource() == node_i) ? arc.getDestination() : arc.getSource();		
				}
				
				// Si on ne l'a pas trouvé, on s'arrete
				if (node_cycle == null)
					break;
				
				// Sinon on cherche une correspondance
				for (JMEArc arc : graph.getIncidentArcsFromNode(node_j)){
					if ( (!(arc instanceof JMELoop)) && arc.getDimension() == i){
						otherNode = (arc.getSource() == node_j) ? arc.getDestination() : arc.getSource();
						flag |= otherNode == node_cycle;
					}
				} break;
				
			default :
				break;
				
		}if(flag)
			return 1;
		return 0;
	}

	/** Verifie la contrainte de cycle sur la règle.
	 * 
	 * On considère des cycles ijij avec i+2 <= j.
	 * 
	 * Dans le cas d'un noeud conserve par la regle,
	 * 		un cycle present doit etre conserve ;
	 * 		si le noeud du graphe gauche ne possede pas de cycle alors le noeud correspondant dans le graphe droit doit
	 * 			soit posseder les dimensions i et j aux memes positions dans son orbite ;
	 * 			soit posseder un arc de dimension i/j si c'etait le cas dans le graphe gauche.
	 * 
	 * Dans le cas d'un noeud ajoute, il faut s'assurer qu'il possede un cycle.
	 *  
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	private void verifCycle(JMERule rule, ArrayList<JMEError> errors) {
		int modDim = rule.getModeler().getDimension();
		JMEGraph left = rule.getLeft();
		JMEGraph right = rule.getRight();
		
		for(int i = 0; i <= modDim -2; i++){
			for (int j = i+2; j <= modDim; j ++){
				for (JMENode leftNode : left.getNodes()){
					JMENode rightNode = right.getMatchNode(leftNode);
					
					// Noeud preserve
					if (rightNode != null){
						
						// Cycle dans le noeud du graphe gauche
						if (hasCycle(leftNode,i,j)==1 && hasCycle(rightNode,i,j)==0)
								errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule,rightNode,
										i+""+j+""+i+""+j+ "-cycle not preserved from left to right: " + rightNode.getName()));
						
						// Pas de cycle
						else if (hasCycle(leftNode,i,j) == 0){
							
							// Dimension i dans l'orbite
							int l_pos_i = leftNode.getOrbit().indexOf(i); // -1 si absent
							int r_pos_i = rightNode.getOrbit().indexOf(i);
							if (l_pos_i != r_pos_i)
								errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, leftNode,
										"Left node has no " + i+""+j+""+i+""+j+"-cycle but arcs of dimension " + i +
										"are modified on right node: " + leftNode.getName()));
							
							// Dimension j dans l'orbite
							int l_pos_j = leftNode.getOrbit().indexOf(j);
							int r_pos_j = rightNode.getOrbit().indexOf(j);
							if (l_pos_j != r_pos_j)
								errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, leftNode,
										"Left node has no " + i+""+j+""+i+""+j+"-cycle but arcs of dimension " + j +
										"are modified on right node: " + leftNode.getName()));
							
							// Dimension i manquante dans l'orbite, on regarde les arcs
							if (l_pos_i == -1){
								for(JMEArc l_arc  : left.getIncidentArcsFromNode(leftNode)){
									if (l_arc.getDimension() == i){
										boolean flag = false;
										for(JMEArc r_arc : right.getIncidentArcsFromNode(rightNode)){
											flag |= r_arc.getDimension() == i;
										} if (!flag)
											errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, leftNode,
													"Left node has no " + i+""+j+""+i+""+j+"-cycle but arcs of dimension " + i +
													"are modified on right node: " + leftNode.getName()));
											
									}
								}
							} 
							
							// Dimension j manquante dans l'orbite, on regarde les arcs
							if (l_pos_j == -1){
								for(JMEArc l_arc  : left.getIncidentArcsFromNode(leftNode)){
									if (l_arc.getDimension() == j){
										boolean flag = false;
										for(JMEArc r_arc : right.getIncidentArcsFromNode(rightNode)){
											flag |= r_arc.getDimension() == j;
										} if (!flag)
											errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, leftNode,
													"Left node has no " + i+""+j+""+i+""+j+"cycle but arcs of dimension " + j +
													"are modified on right node:" + leftNode.getName()));
											
									}
								}
							}
						}
					}
				}
					
				// Noeud ajoute dans le graphe droit
				for (JMENode rightNode : right.getNodes()){
					if (left.getNodes().isEmpty() && hasCycle(rightNode, i, j) == -1)
						errors.add(new JMEError(JMEErrorSeverity.WARNING, JMEErrorType.TOPOLOGIC,rule, rightNode,
								"No Left graph, right node may be added without " + i+""+j+""+i+""+j + " cycle: " + rightNode.getName()));
					if (left.getMatchNode(rightNode) == null && hasCycle(rightNode, i, j) == 0)
						errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.TOPOLOGIC,rule, rightNode,
								"Right node added without " + i+""+j+""+i+""+j + " cycle: " + rightNode.getName()));
				}
			}
		}
	}

}
