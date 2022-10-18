package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.JMENodeView;
import up.jerboa.core.JerboaOrbit;


public class RuleCompositor {
	private JMERuleAtomic _rule1, _rule2;

	public RuleCompositor(JMERuleAtomic ra, JMERuleAtomic rb){
		_rule1 = ra;
		_rule2 = rb;
	}

	public final JMERuleAtomic getIteration(int iter)  {
		JMERuleAtomic _arule = _rule1;
		String nomBase = _arule.getName()+(_arule.getName().compareTo(_rule2.getName())==0? "":"_O_"+_rule2.getName() )+ "_X";
		// TODO : boucle de composition
		for(int it=0;it<iter;it++) {
			System.err.println("iteration de composition : " +nomBase+ it +" / " + (iter-1));
			JMERuleAtomic rule = (JMERuleAtomic) _arule.copy(_arule.getModeler(), nomBase+it);
			JMEGraph l = rule.getLeft();
			JMEGraph r = rule.getRight();

			System.err.println("création des noeuds : ");
			// enlever si on veut garder les plongements
			for(JMENode n : r.getNodes()) {
				if(n.getExplicitExprs()!=null) {
					List<JMENodeExpression> ll = new ArrayList<>();
					ll.addAll(n.getExplicitExprs());
					for(JMENodeExpression expr: ll) {
						n.removeExpression(expr);
					}
				}
			}
			System.err.println("fini");

			final int rightSize1 = _arule.getRight().getNodes().size();
			final int rightSize2 = _rule2.getRight().getNodes().size();

			List<JMENode> realRightNoder1 = _arule.getRight().getNodes();
			List<JMENode> realRightNoder2 = _rule2.getRight().getNodes();

			int nodeSize = 20;
			for(JMENode n:_rule2.getRight().getNodes()) {
				for(JMEElementView view : n.getViews()) {
					try {
						Rectangle rect = ((JMENodeView)view).bbox();
						if(rect.width> nodeSize)
							nodeSize = rect.width;
						if(rect.height> nodeSize)
							nodeSize = rect.height;
					}catch(Exception e) {

					}
				}
			}

			ArrayList<ArrayList<JMENode>> copyList = new ArrayList<>();
			ArrayList<JMENode> rightNode = new ArrayList<>();
			rightNode.addAll(r.getNodes());
			copyList.add(rightNode);			

			System.err.println("création des copies : ");
			for(int li=1;li<rightSize2;li++) {
				ArrayList<JMENode> copyLine = new ArrayList<>();
				for(JMENode nodeR : rightNode) {
					JMENode copy = r.creatNode(nodeR.getX(), (int) (nodeR.getY()+(it*_rule2.getRight().getNodes().size()+li)*(nodeSize+100)));
					copy.setName(getFreshName(r,nodeR.getName(), realRightNoder2.get(li).getName(), iter));//+"_"+it+"_"+li+"_"+copyLine.size());
					copy.setColor(nodeR.getColor());
					copy.setKind(nodeR.getKind());
					copy.setOrbit(nodeR.getOrbit());
					copyLine.add(copy);
					////////////// TODO: ATTENTION CA MARCHE QUE POUR LES UNILIGNES !
					/*// C'est finalement pas si simple
					if(nodeR.getExplicitExprs()!=null) {
						List<JMENodeExpression> ll = new ArrayList<>();
						ll.addAll(nodeR.getExplicitExprs());
						for(JMENodeExpression expr: ll) {
							String expContent = expr.getExpression();
							// TODO: il faudrait un match entre la gauche et la droite
							String exprLeft = null;
							for(JMENodeExpression exprl: nodeR.getExplicitExprs()) {
								if(expr.getEbdInfo().getName().compareTo(exprl.getEbdInfo().getName())==0) {
									exprLeft = exprl.getExpression();
									break;
								}
							}
							if(exprLeft!=null) {
								for(JMENodeExpression exprl: nodeR.getImplicitExprs()) {
									if(expr.getEbdInfo().getName().compareTo(exprl.getEbdInfo().getName())==0) {
										exprLeft = exprl.getExpression();
										break;
									}
								}
							}
							if(exprLeft!=null && exprLeft.length()>0) {
								exprLeft = exprLeft.replaceAll("return", "");
								exprLeft = exprLeft.substring(0, exprLeft.length()-1); // enleve le ';'
								for(JMENode nodeLeftR2 : _rule2.getLeft().getNodes()) {
									expContent = expContent.replaceAll(nodeLeftR2.getName(), exprLeft);
								}
								copy.addExplicitExpression(new JMENodeExpression(copy, expr.getEbdInfo(), expContent));
							}
						}
					}
					 */
					//////////////////////////////////////////////////////////////////


				}
				copyList.add(copyLine); 
			}
			System.err.println("fait ");

			System.err.println("copie des liaisons : ");
			HashMap<JMENode, Integer> mapNodeToId = new HashMap<JMENode, Integer>();
			for(int i=0;i<rightSize1;i++) {
				mapNodeToId.put(realRightNoder1.get(i), i);
			}
			
			HashMap<JMENode, ArrayList<JMEArc>> mapNodeToItsArcs = new HashMap<JMENode, ArrayList<JMEArc>>();
			
			for(JMENode n: r.getNodes()) {
				mapNodeToItsArcs.put(n, new ArrayList<>());
			}
			for(JMEArc a : r.getArcs()) {
				mapNodeToItsArcs.get(a.getSource()).add(a);
				mapNodeToItsArcs.get(a.getDestination()).add(a);
			}
			

			for(JMEArc a : _arule.getRight().getArcs()) {
				int i = mapNodeToId.get(a.getSource());
				int j = mapNodeToId.get(a.getDestination());
				for(int k=0;k<rightSize2;k++) {// Pour toutes les copies (lignes)
					if(!arcExists(mapNodeToItsArcs.get(copyList.get(k).get(i)), copyList.get(k).get(i), copyList.get(k).get(j), a.getDimension())) {
						if(!(a instanceof JMELoop)) {
							JMEArc arc = r.creatArc(copyList.get(k).get(i), copyList.get(k).get(j), a.getDimension());
							mapNodeToItsArcs.get(arc.getSource()).add(arc);
							mapNodeToItsArcs.get(arc.getDestination()).add(arc);
						}else {
							JMELoop loop = r.creatLoop(copyList.get(k).get(i), a.getDimension());
							mapNodeToItsArcs.get(loop.getSource()).add(loop);
							mapNodeToItsArcs.get(loop.getDestination()).add(loop);
							loop.setAngle(((JMELoop) a).getAngle());
						}
					}
				}				
			}

			/*
			System.err.println("copie des liaisons : ");
			for(JMEArc a : _arule.getRight().getArcs()) {
				boolean found = false;
				// copie des arcs existants (inter copies)
				for(int i=0;i<rightSize1;i++) { // Pour toutes les copies (lignes)
					for(int j=i;j<rightSize1;j++) { // Pour toute les colonne (parcours les noeuds au sein d'une meme copie
						if(a.link(realRightNoder1.get(i), realRightNoder1.get(j)) ) {
							found = true;
							for(int k=0;k<rightSize2;k++) {// Pour toutes les copies (lignes)
								if(!arcExists(r.getArcs(), copyList.get(k).get(i), copyList.get(k).get(j), a.getDimension())) {
									if(!(a instanceof JMELoop)) {
										r.creatArc(copyList.get(k).get(i), copyList.get(k).get(j), a.getDimension());

									}else {
										JMELoop loop = r.creatLoop(copyList.get(k).get(i), a.getDimension());
										loop.setAngle(((JMELoop) a).getAngle());
									}
								}
							}
						}
						if(found)break;
					}
					if(found)break;
				}
			}*/

			System.err.println("fini");

			System.err.println("maj des liaisons : ");
			
			
			HashMap<JMENode, Integer> mapNodeToId2 = new HashMap<JMENode, Integer>();
			for(int i=0;i<rightSize2;i++) {
				mapNodeToId2.put(realRightNoder2.get(i), i);
			}

			

			for(JMEArc a : _rule2.getRight().getArcs()) {
				int i = mapNodeToId2.get(a.getSource());
				int j = mapNodeToId2.get(a.getDestination());
				for(int k=0;k<rightSize1;k++) {// Pour toute les colonne (parcours les noeuds au sein d'une meme copie
					if(!arcExists(mapNodeToItsArcs.get(copyList.get(i).get(k)), copyList.get(i).get(k), copyList.get(j).get(k), a.getDimension())) {
						if(!(a instanceof JMELoop)) {
							JMEArc arc = r.creatArc(copyList.get(i).get(k), copyList.get(j).get(k), a.getDimension());
							mapNodeToItsArcs.get(arc.getSource()).add(arc);
							mapNodeToItsArcs.get(arc.getDestination()).add(arc);
						}else {
							JMELoop loop = r.creatLoop(copyList.get(i).get(k), a.getDimension());
							mapNodeToItsArcs.get(loop.getSource()).add(loop);
							mapNodeToItsArcs.get(loop.getDestination()).add(loop);
							loop.setAngle(((JMELoop) a).getAngle());
						}
					}
				}			
			}
			
			
			/*
			for(JMEArc a : _rule2.getRight().getArcs()) {
				boolean found = false;
				// copie des arcs existants (inter copies)
				for(int i=0;i<rightSize2;i++) { // Pour toutes les copies (lignes)
					for(int j=i;j<rightSize2;j++) { // Pour toutes les copies (lignes)
						if(a.link(realRightNoder2.get(i), realRightNoder2.get(j)) ) {
							found = true;
							for(int k=0;k<rightSize1;k++) {// Pour toute les colonne (parcours les noeuds au sein d'une meme copie
								if(!arcExists(r.getArcs(), copyList.get(i).get(k), copyList.get(j).get(k), a.getDimension())) {
									if(!(a instanceof JMELoop)) {
										r.creatArc(copyList.get(i).get(k), copyList.get(j).get(k), a.getDimension());

									}else {
										JMELoop loop = r.creatLoop(copyList.get(i).get(k), a.getDimension());
										loop.setAngle(((JMELoop) a).getAngle());
									}
								}
							}
						}
						if(found)break;
					}
					if(found)break;
				}
			}*/

			System.err.println("fini");

			System.err.println("renommage des liaisons :");
			
			// renommage des arcs prÃ©existants
			for(int k=0;k<rightSize2;k++) {
				// TODO: Attention !! il faut chercher le bon !
				JMENode hook = _rule2.getLeft().getHooks().get(0);
//				List<JMEArc> arcs = new ArrayList<>();
//				arcs.addAll(r.getArcs());
				JerboaOrbit orbRealK = realRightNoder2.get(k).getOrbit();

				ArrayList<JMENode> copieLine = copyList.get(k); // on prend la kieme copie pour faire les renommages
				for(int i=0;i<rightSize1;i++) { // pour toutes les colonnes (inter copies)
					// orbites des noeuds a changer
					ArrayList <Integer> newOrb = new ArrayList<>();
					for(int di=0;di<copieLine.get(i).getOrbit().size();di++) {
						int d = copieLine.get(i).getOrbit().get(di);
						if(d>=0) {
							int inLeftIndex = hook.getOrbit().indexOf(d);
							if(inLeftIndex>=0)
								newOrb.add(orbRealK.get(inLeftIndex));
							else if(k>0) {
								newOrb.add(-1);	
							}else { // si motif original on ne supprime pas! PAS TOUCHER !!
								newOrb.add(d);	
							}
						}else {
							newOrb.add(d);
						}
					}
					copieLine.get(i).setOrbit(new JerboaOrbit(newOrb));

					// Arcs explicite interCopie a redimensionner
					for(int j=i+1;j<copieLine.size();j++) {
						/**for(JMEArc a: arcs) {**/
						for(JMEArc a : mapNodeToItsArcs.get(copieLine.get(i))) {
							if( a.link(copieLine.get(i),copieLine.get(j)) ) {
								int newO = hook.getOrbit().indexOf(a.getDimension());								
								if(newO>-1) {
									if(orbRealK.get(newO)>=0) {
										a.setDimension(orbRealK.get(newO));
									}else {
										r.removeArc(a);
									}
									break;
								}else if(k>0) {
									r.removeArc(a);	 // si on es tpas sur le motif de base on ne crÃ©e pas les noeuds non filtrÃ© !
								}
							}
						}
					}
				}
			}
			_arule= rule;

			System.err.println("déplacement des noeuds : ");
			int spacement = 150;
			for(int k=0;k<rightSize2;k++) {
				for(int i=0;i<rightSize1;i++) {
					copyList.get(k).get(i).setPosition(i*spacement+100, k*spacement+100);
				}
			}

		}
		String _aruleName = _arule.getName();
		int cpt=0;
		while(_rule1.getModeler().searchRule(_arule.getName())!=null) {
			_arule.setName(_aruleName+"_"+cpt);
			cpt++;
		}
		System.err.println("composition finie");
		return _arule;
	}

	public static boolean arcExists(List<JMEArc> others, JMENode n1, JMENode n2, int dim) {
		for(JMEArc arc : others) {
			if(arc.getDimension()==dim && arc.link(n1,n2)){
				return true;
			}
		}
		return false;
	}


	public static JMERuleAtomic getComposition(final JMERuleAtomic r1, int iter) {
		JMERuleAtomic rule;
		if(iter<=1) {
			rule = (JMERuleAtomic) r1.copy(r1.getModeler(), r1.getName()+(r1.getName().compareTo(r1.getName())==0? "_":"_O_"+r1.getName() )+ "_X"+iter);
		}else {
			rule = getComposition(r1, iter-1);
			rule = (JMERuleAtomic) rule.copy(r1.getModeler(), r1.getName()+(r1.getName().compareTo(r1.getName())==0? "_":"_O_"+r1.getName() )+ "_X"+iter);
		}
		System.err.println(">Composition " + r1.getName()+ " (iter :"+iter+")");
		JMEGraph l = rule.getLeft();
		JMEGraph r = rule.getRight();

		final int rightSize1 = r1.getRight().getNodes().size();
		final int rightSize2 = r1.getRight().getNodes().size();

		final List<JMENode> realRightNoder1 = r1.getRight().getNodes();
		final List<JMENode> realRightNoder2 = r1.getRight().getNodes();

		int nodeSize = 20;
		for(JMENode n: r1.getRight().getNodes()) {
			for(JMEElementView view : n.getViews()) {
				try {
					Rectangle rect = ((JMENodeView)view).bbox();
					if(rect.width> nodeSize)
						nodeSize = rect.width;
					if(rect.height> nodeSize)
						nodeSize = rect.height;
				}catch(Exception e) {

				}
			}
		}

		ArrayList<ArrayList<JMENode>> copyList = new ArrayList<>();
		ArrayList<JMENode> rightNode = new ArrayList<>();
		rightNode.addAll(r.getNodes()); 
		copyList.add(rightNode);			

		for(int li=1;li<rightSize2;li++) {
			ArrayList<JMENode> copyLine = new ArrayList<>();
			for(JMENode nodeR : rightNode) {
				JMENode copy = r.creatNode(nodeR.getX(), (int) (nodeR.getY()+(/*r1.getRight().getNodes().size()+*/li)*(nodeSize+10)));
				copy.setName(nodeR.getName()+"_"+realRightNoder2.get(li).getName()+(iter>0?"_"+iter:""));//+"_"+it+"_"+li+"_"+copyLine.size());
				copy.setName(getFreshName(r,nodeR.getName(), realRightNoder2.get(li).getName(), iter));
				copy.setColor(nodeR.getColor());
				copy.setKind(nodeR.getKind());
				copy.setOrbit(nodeR.getOrbit());
				copyLine.add(copy);
			}
			copyList.add(copyLine); 
		}
		for(JMEArc a : r1.getRight().getArcs()) {
			boolean found = false;
			// copie des arcs existants (inter copies)
			for(int i=0;i<rightSize1;i++) { // Pour toutes les copies (lignes)
				for(int j=i;j<rightSize1;j++) { // Pour toute les colonne (parcours les noeuds au sein d'une meme copie
					if(a.link(realRightNoder1.get(i), realRightNoder1.get(j)) ) {
						found = true;
						for(int k=0;k<rightSize2;k++) {// Pour toutes les copies (lignes)
							if(!arcExists(r.getArcs(), copyList.get(k).get(i), copyList.get(k).get(j), a.getDimension())) {
								if(!(a instanceof JMELoop)) {
									r.creatArc(copyList.get(k).get(i), copyList.get(k).get(j), a.getDimension());

								}else {
									JMELoop loop = r.creatLoop(copyList.get(k).get(i), a.getDimension());
									loop.setAngle(((JMELoop) a).getAngle());
								}
							}
						}
					}
					if(found)break;
				}
				if(found)break;
			}
		}


		for(JMEArc a : r1.getRight().getArcs()) {
			boolean found = false;
			// copie des arcs existants (inter copies)
			for(int i=0;i<rightSize2;i++) { // Pour toutes les copies (lignes)
				for(int j=i;j<rightSize2;j++) { // Pour toutes les copies (lignes)
					if(a.link(realRightNoder2.get(i), realRightNoder2.get(j)) ) {
						found = true;
						for(int k=0;k<rightSize1;k++) {// Pour toute les colonne (parcours les noeuds au sein d'une meme copie
							if(!arcExists(r.getArcs(), copyList.get(i).get(k), copyList.get(j).get(k), a.getDimension())) {
								if(!(a instanceof JMELoop)) {
									r.creatArc(copyList.get(i).get(k), copyList.get(j).get(k), a.getDimension());

								}else {
									JMELoop loop = r.creatLoop(copyList.get(i).get(k), a.getDimension());
									loop.setAngle(((JMELoop) a).getAngle());
								}
							}
						}
					}
					if(found)break;
				}
				if(found)break;
			}
		}

		// renommage des arcs prÃ©existants
		for(int k=0;k<rightSize2;k++) {
			// TODO: Attention !! il faut chercher le bon !
			JMENode hook = r1.getLeft().getHooks().get(0);
			List<JMEArc> arcs = new ArrayList<>();
			arcs.addAll(r.getArcs());
			JerboaOrbit orbRealK = realRightNoder2.get(k).getOrbit();

			ArrayList<JMENode> copieLine = copyList.get(k); // on prend la kieme copie pour faire les renommages
			for(int i=0;i<rightSize1;i++) { // pour toutes les colonnes (inter copies)
				// orbites des noeuds a changer
				ArrayList <Integer> newOrb = new ArrayList<>();
				for(int di=0;di<copieLine.get(i).getOrbit().size();di++) {
					int d = copieLine.get(i).getOrbit().get(di);
					if(d>=0) {
						int inLeftIndex = hook.getOrbit().indexOf(d);
						if(inLeftIndex>=0)
							newOrb.add(orbRealK.get(inLeftIndex));
						else if(k>0) {
							newOrb.add(-1);	
						}else { // si motif original on ne supprime pas! PAS TOUCHER !!
							newOrb.add(d);	
						}
					}else {
						newOrb.add(d);
					}
				}
				copieLine.get(i).setOrbit(new JerboaOrbit(newOrb));

				// Arcs explicite interCopie a redimensionner
				for(int j=i+1;j<copieLine.size();j++) {
					for(JMEArc a: arcs) {
						if( a.link(copieLine.get(i),copieLine.get(j)) ) {
							int newO = hook.getOrbit().indexOf(a.getDimension());								
							if(newO>-1) {
								if(orbRealK.get(newO)>=0) {
									a.setDimension(orbRealK.get(newO));
								}else {
									r.removeArc(a);
								}
								break;
							}else if(k>0) {
								r.removeArc(a);	 // si on es tpas sur le motif de base on ne crÃ©e pas les noeuds non filtrÃ© !
							}
						}
					}
				}
			}
		}
		int spacement = 150;
		for(int k=0;k<rightSize2;k++) {
			for(int i=0;i<rightSize1;i++) {
				copyList.get(k).get(i).setPosition(i*spacement+100, k*spacement+100);
			}
		}
		String _aruleName = rule.getName();
		int cpt=0;
		while(r1.getModeler().searchRule(rule.getName())!=null) {
			rule.setName(_aruleName+"_"+cpt);
			cpt++;
		}
		return rule;
	}

	static String getFreshName(JMEGraph graph, String nameNode1, String nameNode2, int iter) {
		int count=0;
		String base = nameNode1+"_"+nameNode2+"_i"+iter+"_";
		while(graph.getMatchNode(base+count)!=null) {
			count++;
		}
		return base+count;
	}
}
