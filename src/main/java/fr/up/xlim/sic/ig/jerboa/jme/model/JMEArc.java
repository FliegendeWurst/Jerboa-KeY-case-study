package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import up.jerboa.core.util.Pair;


/**
 *  Cette classe represente un arc dans notre editeur. Un arc est defini par differents
 *  parametres. Ceux correspondant a l'integration dans l'editeur. Ceux correspondant aux parametres topologique.
 *  Enfin, ceux correspondant l'affichage de l'arc au sein de l'editeur. 
 * @author Hakim Ferrier-Belhaouari
 *
 */
public class JMEArc implements JMEElement{

	// extra parameters
	protected boolean modified;
	private boolean orient;
	protected Set<JMEElementView> views;
	protected UndoManager manager;
	
	// topological parameters
	protected JMENode a;
	protected JMENode b;
	protected int dim;
	protected JMEGraph graph;
	
	// geometrical parameters
	protected ArrayList<Point> points;
	private boolean transitoire;
	private Point old;
	
	public JMEArc(JMEGraph graph, JMENode a, JMENode b, int dim) {
		this.a = a;
		this.b = b;
		this.dim = dim;
		this.graph = graph;
		orient = false;
		this.manager = a.getUndoManager();
		views = new HashSet<>();
		points = new ArrayList<>();
		transitoire = false;
		modified = false;
	}

	public boolean sameNoDim(JMEArc arc) {
		return (arc.a == a || arc.a == b) && (arc.b == b || arc.b == a);
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public Point getPoint(int i) {
		return points.get(i);
	}
	
	public void setPoint(int index, Point p) {
		if(!transitoire) {
			manager.registerUndo(new UndoItemField(this,"setpoint",new Pair<Integer, Point>(index, points.get(index)), new Pair<Integer, Point>(index,points.get(index)), !modified ));
			System.out.println("Transitoire...");
			modified = true;
		}
		points.set(index, p);
	}
	
	public void addPoint(Point p) {
		manager.registerUndo(new UndoItemField(this,"addpoint",p,p, !modified));
		points.add(p);
		modified = true; // TODO: a voir
	}
	public void addPoint(int index,Point p) {
		manager.registerUndo(new UndoItemField(this,"addpointi",new Pair<Integer, Point>(index, points.get(index)), new Pair<Integer, Point>(index,p), !modified));
		points.add(index, p);
		modified = true; // TODO: a voir
	}
	
	public int sizePoints() {
		return points.size();
	}
	
	public void setPoints(List<Point> points) {
		ArrayList<Point> newvalue = new ArrayList<>(points);
		manager.registerUndo(new UndoItemField(this, "points", this.points, newvalue, !modified));
		modified = true;
		System.out.println("SETPOINTS: "+points.size());
		this.points = newvalue;
	}
	
	
	public void removePoint(Point p) {
		manager.registerUndo(new UndoItemField(this,"rempoint",p,p, !modified));
		points.remove(p);
		modified = true;
	}
	
	public void removePoint(int i) {
		Point p = (i < points.size()? points.get(i) : null);
		if(p != null) {
			manager.registerUndo(new UndoItemField(this,"rempointi",new Pair<Integer, Point>(i, points.get(i)), new Pair<Integer, Point>(i,p), !modified));
			points.remove(i);
			modified = true;
		}
	}
	
	boolean link(JMENode n1, JMENode n2) {
		return (a.equals(n1) && b.equals(n2))||(b.equals(n1) && a.equals(n2));
	}
	
	public boolean isModified() {
		return modified;
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
			manager.registerUndo(new UndoItemField(this, "dim", dim, d, !modified));
			dim = d;
			modified = true;
			graph.updateAllExprs();
		}
	}
	
	@Override
	public void addView(JMEElementView view) {
		views.add(view);
	}

	@Override
	public void removeView(JMEElementView view) {
		views.remove(view);
	}

	@Override
	public void update() {
		for (JMEElementView view : views) {
			view.reload();
		}
	}

	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch(fitem.field()) {
		case "dim": {
			dim = ((Number) fitem.value()).intValue();
			break;
		}
		case "addpoint":
			points.remove(fitem.value());
			break;
		case "rempoint":
			points.add((Point) fitem.value());
			break;
		case "setpoint": {
			@SuppressWarnings("unchecked")
			Pair<Integer, Point> pair = ((Pair<Integer, Point>) fitem.value());
			points.set(pair.l().intValue(), pair.r());
		}
		case "addpointi": {
			@SuppressWarnings("unchecked")
			Pair<Integer, Point> pair = ((Pair<Integer, Point>) fitem.value());
			points.remove(pair.l().intValue());
		}
		case "rempointi": {
			@SuppressWarnings("unchecked")
			Pair<Integer, Point> pair = ((Pair<Integer, Point>) fitem.value());
			points.add(pair.l().intValue(), pair.r());
		}
		case "points": {
			@SuppressWarnings("unchecked")
			ArrayList<Point> val = (ArrayList<Point>) fitem.value();
			points = val;
		}
		}
		if(fitem.getModifState())
			modified = false;
		manager.transfertRedo(fitem);
		update();
	}


	@Override
	public void redo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch(fitem.field()) {
		case "dim": {
			dim = ((Number) fitem.value()).intValue();
			break;
			
		}
		case "addpoint":
			points.add((Point) fitem.newValue());
			break;
		case "rempoint":
			points.remove(fitem.newValue());
			break;
		case "setpoint": {
			@SuppressWarnings("unchecked")
			Pair<Integer, Point> pair = ((Pair<Integer, Point>) fitem.newValue());
			points.set(pair.l().intValue(), pair.r());
		}
		case "addpointi": {
			@SuppressWarnings("unchecked")
			Pair<Integer, Point> pair = ((Pair<Integer, Point>) fitem.newValue());
			points.remove(pair.l().intValue());
		}
		case "rempointi": {
			@SuppressWarnings("unchecked")
			Pair<Integer, Point> pair = ((Pair<Integer, Point>) fitem.newValue());
			points.add(pair.l().intValue(), pair.r());
		}
		case "points": {
			@SuppressWarnings("unchecked")
			ArrayList<Point> val = (ArrayList<Point>) fitem.newValue();
			points = val;
		}
		}
		if(fitem.getModifState())
			modified = true;   
		manager.transfertUndo(fitem);
		update();
	}

	public void begin(int index) {
		transitoire = true;
		old = new Point(points.get(index));
	}
	
	public void end(int index) {
		transitoire = false;
		manager.registerUndo(new UndoItemField(this, "setpoint", old, points.get(index), !modified));
		old = null;
	}
	
	
	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitArc(this);
	}


	@Override
	public UndoManager getUndoManager() {
		return manager;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	
	@Override
	public void resetModification() {
		modified = false;
	}
	
	public int getNbErrors() {
		Collection<JMEError> errors = graph.getRule().getAllErrors();
		int i = 0;
		for (JMEError e : errors) {
			if(e.getTarget() == this)
				i++;
		}
		return i;
	}

	public JMEArc copy(JMEGraph g) {
		JMENode nodeA = g.searchNodeByName(a.getName());
		JMENode nodeB = g.searchNodeByName(b.getName());
		if(nodeA != null && nodeB != null) {
			if(nodeA == nodeB) {
				JMELoop loop = new JMELoop(g, nodeA, getDimension());
				return loop;
			}
			else {
				JMEArc arc = new JMEArc(g, nodeA, nodeB, getDimension());
				return arc;
			}
		}
		return null;
	}
}
