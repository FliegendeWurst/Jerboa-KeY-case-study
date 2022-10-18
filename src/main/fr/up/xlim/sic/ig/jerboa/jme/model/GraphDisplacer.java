package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;


public class GraphDisplacer {
	private JMEGraph graph;
	private ArrayList<Float> dists;
	JerboaModelerEditor editor;

	public GraphDisplacer(JMEGraph g, List<Float> distForDim, JerboaModelerEditor edit) {
		graph = g;
		dists = new ArrayList<>();
		dists.addAll(distForDim);
		editor = edit;
	}

	public void go() {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<JMENode> nodes = graph.getNodes();
				HashMap<JMENode, ArrayList<Point>> assocDisplace = new HashMap<>();
				List<JMEArc> arcs = graph.getArcs();

				for(JMENode n: nodes) {
					assocDisplace.put(n, new ArrayList<Point>());
				}
				int cpt = 0;
				int nbDisplaced = 0;
				do {
					cpt++;
					nbDisplaced = 0;
					for(JMEArc a: arcs) {
						if(! (a instanceof JMELoop) ){
							double distance = a.getSource().getPoint().distance(a.getDestination().getPoint());
							double diff = (distance-dists.get(a.getDimension()));
							double x = (a.getSource().getPoint().getX()-a.getDestination().getPoint().getX());
							double y = (a.getSource().getPoint().getY()-a.getDestination().getPoint().getY());
							double vectNorm = Math.sqrt(x*x+y*y);
							x = (diff*0.5*x)/vectNorm;
							y = (diff*0.5*y)/vectNorm;
							
//							Point vect = new Point((int)x,(int)y);
//							Point vect = new Point((int),
//									(int));
//							vect = new Point((int)(diff*vect.getX()/vect.distance(new Point(0,0))),
//									(int)(diff*vect.getY()/vect.distance(new Point(0,0))));
							int coef = (int) (diff/Math.abs(diff));
							if(distance<1) {
								assocDisplace.get(a.getSource()).add(new Point(5,5));
								assocDisplace.get(a.getDestination()).add(new Point(-5,-5));
							}else if(Math.abs(diff)>5) {
								nbDisplaced++;
//								Point newPos = a.getSource().getPoint();
//								newPos.translate(coef*(int)(vect.getX()), coef*(int)(vect.getY()));
//								a.getSource().setPosition((int)newPos.getX(), (int)newPos.getY());
		//
//								Point newPos2 = a.getSource().getPoint();
//								newPos2.translate(coef*(int)(vect.getX()), coef*(int)(vect.getY()));
//								a.getDestination().setPosition((int)newPos2.getX(), (int)newPos2.getY());
								
								assocDisplace.get(a.getSource()).add(new Point(coef*(int)(x),coef*(int)(y)));
								assocDisplace.get(a.getDestination()).add(new Point(-coef*(int)(x),-coef*(int)(y)));
							}
						}
					}
					System.err.println(assocDisplace);
					for(Entry<JMENode, ArrayList<Point>> vals : assocDisplace.entrySet()) {
						Point bary = getBaycenter(vals.getValue());
						Point newPos = vals.getKey().getPoint();
						newPos.translate(bary.x, bary.y);
						System.err.println(bary);
						vals.getKey().setPosition((int)newPos.getX(), (int)newPos.getY());
						vals.getValue().clear();
					}
					System.err.println(cpt+" -------------- ");
					editor.reload();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}while(cpt<50 && nbDisplaced>1);
			}
		});
		t.start();
		
	}

	private Point getBaycenter(List<Point> points) {
		Point p = new Point(0,0);
		for(Point pi : points) {
			p.translate((int)pi.getX(), (int)pi.getY());
		}
//		if(points.size()>0)
//			p = new Point((int)p.getX()/points.size(), (int)p.getY()/points.size());
//		else
//			System.err.println("ERROR : no point found");
		return p;
	}

}
