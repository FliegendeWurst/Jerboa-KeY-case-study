package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMELoop;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMERuleError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMERuleErrorSeverity;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMERuleErrorType;
import up.jerboa.core.JerboaOrbit;

// Verifications topo
public class JMEVerifTopoClassic {		

	public Collection<JMERuleError> check(JMERule rule) {
		ArrayList<JMERuleError> errors = new ArrayList<>();
		
        try { verifDimension(rule, errors); } catch(Throwable t) { }
        try { verifDuplicateNode(rule, errors);  } catch(Throwable t) { }
        try { verifHooks(rule, errors); } catch(Throwable t) { }
        try { verifNodeOrbitSizes(rule, errors); } catch(Throwable t) { }
        try { verifDuplicateDimension(rule, errors); } catch(Throwable t) { }
        try { verifIncidentArc(rule,errors); } catch(Throwable t) { }
        try { verifCycle(rule, errors); } catch(Throwable t) { }
        
		return errors;
	}

	
	/**
	 * Verifie si les dimensions sont comprises entre :
	 * 		-1 et la dimension du modeleur pour les orbites ;
	 * 		 0 et la dimension du modeleur pour les arcs.
	 * 
	 * On realise pour le graphe gauche et le graphe droit, un parcours des noeuds et, pour chaque noeud, on 
	 * verifie les valeurs des dimensions dans l'orbite. On realise ensuite un parcours des arcs et, pour
	 * chaque arc, on verifie la valeur de sa dimension.
	 * 
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	void verifDimension(JMERule rule, ArrayList<JMERuleError> errors) {
		int modDim = rule.getModeler().getDimension();
		
		// Graphe Gauche
		JMEGraph left = rule.getLeft();
		
		// Orbites
		for (JMENode node : left.getNodes()) {
			for(int i : node.getOrbit().tab()) {
				if(i > modDim || i < -1)
					errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node));	
			}
		}
		
		// Arcs
		for(JMEArc arc : left.getArcs()) {
			if(arc.getDimension() > modDim || arc.getDimension() < 0) {
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, arc));
			}
		}
		

		// Graphe Droit
		JMEGraph right = rule.getRight();
		
		// Orbites
		for (JMENode node : right.getNodes()) {
			for(int i : node.getOrbit().tab()) {
				if(i > modDim || i < -1)
					errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node));	
			}
		}	
		
		// Arc
		for(JMEArc arc : right.getArcs()) {
			if(arc.getDimension() > modDim || arc.getDimension() < 0) {
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, arc));
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
	void verifDuplicateNode(JMERule rule, ArrayList<JMERuleError> errors) {
		
		// Graphe Gauche
		JMEGraph left = rule.getLeft();
		HashMap<String,JMENode> existingNamesLeft = new HashMap<>();
		for (JMENode node : left.getNodes()) {
			
			// Le nom est deja present, on ajoute des erreurs.
			if (existingNamesLeft.containsKey(node.getName())) {
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node));
				JMENode otherNode = existingNamesLeft.get(node.getName());
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, otherNode));
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
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, node));
				JMENode otherNode = existingNamesRight.get(node.getName());
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, otherNode));
			}
			
			// Le nom n'est pas present, on l'ajoute dans la HashMap.
			else
				existingNamesRight.put(node.getName(),node);
		} 
	}
	
	/**
	 * Verifie que
	 * - tous les noeuds du graphe gauche sont relies a un hook
	 * - que chaque hook possede une orbite pleine, c'est-a-dire qu'aucune dimension n'est vide.
	 * - qu'auncun hook n'est relié à un second hook 
	 *
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	void verifHooks(JMERule rule, ArrayList<JMERuleError> errors){
		List<JMENode> hooks = rule.getHooks();
		JMEGraph left = rule.getLeft();
		
		// Verification d'orbite pleine pour les hooks
		for(JMENode hook : hooks){
			if(hook.getOrbit().contains(-1)){
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, hook));
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
					errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, node));
			}
			else if(countHook == 0) {
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, node));
			}
		}
	}
	
	/**
	 * Verifie que les orbites sont tous de meme taille. Un Hook est pris pour reference, si possible.
	 * 
	 * On commence par chercher un noeud de reference (si possible un hook dans le graphe gauche, sinon un noeud
	 * quelconque dans le graphe droit.
	 * 
	 * Pour chaque noeud dans chacun des graphes, on compare ensuite la longueur de l'orbite a la longueur de l'orbite
	 * du noeud reference.
	 * 
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	void verifNodeOrbitSizes(JMERule rule, ArrayList<JMERuleError> errors){
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
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, n));
			
			// Orbite trop grande
			else if(n.getOrbit().size() > length)
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, n));
		}
		// Graphe droit
		for (JMENode n : rule.getRight().getNodes()){
			
			// Orbite trop petite
			if(n.getOrbit().size() < length)
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, n));
			
			// Orbite trop grande
			else if(n.getOrbit().size() > length)
				errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, n));
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
	void verifDuplicateDimension(JMERule rule, ArrayList<JMERuleError> errors) {
		
		// Graphe Gauche
		for (JMENode node : rule.getLeft().getNodes()){
			HashSet<Integer> dims = new HashSet<>();
			
			// Orbite
			for(int i : node.getOrbit()){
				if(!dims.add(i) && i != -1)
					errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node));
			}
			
			// Arcs
			for(JMEArc a : rule.getLeft().getIncidentArcsFromNode(node)){
				if(!dims.add(a.getDimension()))
					errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node));
			}
			
		// Graphe Droit
		} for (JMENode node : rule.getRight().getNodes()){
			HashSet<Integer> dims = new HashSet<>();
			
			// Orbite
			for(int i : node.getOrbit()){
				if(!dims.add(i) && i != -1)
					errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node));
			}
			
			// Arcs
			for(JMEArc a : rule.getRight().getIncidentArcsFromNode(node)){
				if(!dims.add(a.getDimension()))
					errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC, rule, node));
			}
		}
	}
	
	/**
	 * Verifie la contrainte d'arc incident sur la regle :
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
	void verifIncidentArc(JMERule rule, ArrayList<JMERuleError> errors) {
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
							errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, leftNode));
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
						errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, leftNode));
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
						errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, leftNode));
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
						errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, rightNode));
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
						errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, rightNode));
				}
			}
		} for (JMENode rightNode : right.getNodes()){
			JMENode leftNode = left.getMatchNode(rightNode);
			// Noeud ajoute : on verifie qu'il possede toutes les dimensions
			if (leftNode == null){
				List<JMEArc> incidents = right.getIncidentArcsFromNode(rightNode);
				for (int i = 0; i<= modDim; i++){
					if (!rightNode.getOrbit().contains(i)){
						boolean flag = false;
						for (JMEArc a : incidents){
							if(a.getDimension()==i)
								flag = true;
						} if(!flag)
							errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, rightNode));
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
	int hasCycle(JMENode node, int i, int j){
		/*
		 * Dans un premier temps, on determine les aretes i et j sont implicites ou explicites (distinction
		 * arc, boucle)
		 * En fonction, on associe un attribut qui permet de retrouver la configuration.
		 * On traite separement les differentes configurations :
		 * 		- orbite/boucle et boucle/boucle => cycle
		 * 		- arc/orbite => on regarde la position dans l'orbite du noeud adjacent
		 * 		- arc/boucle => on regarde les boucles du noeud adjacent
		 * 		- orbite/orbite =>
		 * 					dans le graphe gauche : cycle
		 * 					dans le graphe droit : on compare avec un noeud du graphe gauche
		 * 		- arc/arc => on regarde si on peut construire un cycle a l'aide d'un troisieme noeud
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
		JMENode otherNode, node_i, node_j, node_cycle;
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
				else if (graph.getRule().getLeft().getHooks().isEmpty())
					return -1;
				else { // Il faut vérifier tous les hooks!
					for (JMENode hook : graph.getRule().getLeft().getHooks()) {
						pos_i = orbit.indexOf(i);
						pos_j = orbit.indexOf(j);
						flag |= hook.getOrbit().get(pos_i) + 2 <= hook.getOrbit().get(pos_j)
								|| hook.getOrbit().get(pos_j) + 2 <= hook.getOrbit().get(pos_i);
					}
				}
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
				
				// Si on ne l'a pas trouve, on s'arrete
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

	/** Verifie la contrainte de cycle sur la regle.
	 * 
	 * On considere des cycles ijij avec i+2 <= j.
	 * 
	 * Dans le cas d'un noeud conserve par la regle,
	 * 		un cycle present doit etre conserve ;
	 * 		si le noeud du graphe gauche ne possede pas de cycle alors le noeud correspondant dans le graphe droit doit
	 * 			soit posseder les dimensions i et j aux memes positions dans son orbite ;
	 * 			soit posseder un arc de dimension i/j si c'etait le cas dans le graphe gauche.
	 * 
	 * Dans le cas d'un noeud ajoute, il faut s'assurer qu'il possede un cycle.
	 * 
	 * TODO: we weakened these conditions in the SCP paper.
	 *  
	 * @param rule Regle a verifier.
	 * @param errors Liste des erreurs deja presentes dans la regle. Cette liste est modifiee lors de la verification.
	 */
	void verifCycle(JMERule rule, ArrayList<JMERuleError> errors) {
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
								errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule,rightNode));
						
						// Pas de cycle
						else if (hasCycle(leftNode,i,j) == 0){
							
							// Dimension i dans l'orbite
							int l_pos_i = leftNode.getOrbit().indexOf(i); // -1 si absent
							int r_pos_i = rightNode.getOrbit().indexOf(i);
							if (l_pos_i != r_pos_i)
								errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, leftNode));
							
							// Dimension j dans l'orbite
							int l_pos_j = leftNode.getOrbit().indexOf(j);
							int r_pos_j = rightNode.getOrbit().indexOf(j);
							if (l_pos_j != r_pos_j)
								errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, leftNode));
							
							// Dimension i manquante dans l'orbite, on regarde les arcs
							if (l_pos_i == -1){
								if (explicitArcHasChanged(i, rule, leftNode)) {
									errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, leftNode));
								}
							} 
							
							// Dimension j manquante dans l'orbite, on regarde les arcs
							if (l_pos_j == -1){
								if (explicitArcHasChanged(j, rule, leftNode)) {
									errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, leftNode));	
								}
							}
						}
					}
				}
					
				// Noeud ajoute dans le graphe droit
				for (JMENode rightNode : right.getNodes()){
					if (left.getNodes().isEmpty() && hasCycle(rightNode, i, j) == -1)
						errors.add(new JMERuleError(JMERuleErrorSeverity.WARNING, JMERuleErrorType.TOPOLOGIC,rule, rightNode));
					if (left.getMatchNode(rightNode) == null && hasCycle(rightNode, i, j) == 0)
						errors.add(new JMERuleError(JMERuleErrorSeverity.CRITIQUE, JMERuleErrorType.TOPOLOGIC,rule, rightNode));
				}
			}
		}
	}


	/**
	 * Check that the explicit incident arc did not change between the LHS and the RHS
	 * @param j : dimension of the arc to be checked, we assume that both the node and its
	 * RHS counterpart has an explicit incident arc of this dimension.
	 * @param rule : rule in which the check happens
	 * @param leftNode: left node to be checked (it must be preserved)
	 * 
	 * @return true if the arc changed
	 */
	private boolean explicitArcHasChanged(int dim, JMERule rule, JMENode leftNode) {
		final int dimI = dim;
		JMENode rightNode = rule.getRight().getMatchNode(leftNode);
		if (leftNode.alphas().stream().anyMatch(arc -> ( dimI == arc.getDimension()))) {
			
			// By the incident arcs constraint, this should always be true.
			// In case it is not, we do not raise an additional error
			if (rightNode.alphas().stream().anyMatch(arc -> ( dimI == arc.getDimension()))) {
				JMENode leftNeighbor = leftNode.alpha(dimI);
				JMENode rightNeigbor = rightNode.alpha(dimI);
				JMENode matchLeftNeighbor = rule.getRight().getMatchNode(leftNeighbor);
				if (matchLeftNeighbor == null)
					return true;
				return (! matchLeftNeighbor.equals(rightNeigbor));
			}
		}
		return false;
	}

}
