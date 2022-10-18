/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.awt.Color;
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
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorType;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import up.jerboa.core.JerboaOrbit;
import up.jerboa.core.util.Pair;

/**
 * @author Hakim Belhaouari
 *
 */
public class JMENode implements JMEElement {

	// topological parameters
	protected String name;
	protected JerboaOrbit orbit;
	protected JMENodeMultiplicity multiplicity;
	private JMENodeKind kind;
	protected ArrayList<JMENodeExpression> explicits;
	protected ArrayList<JMENodeExpression> implicits;
	protected ArrayList<JMENodeExpression> required;
	protected String precondition;
	protected JMEParamTopo paramTopo;
	private Color color;


	// editor parameters
	protected JMEGraph graph;
	protected int topoErrors;
	protected int ebdsErrors;

	// geometrical parameters
	protected int x;
	protected int y;
	protected boolean modified;

	protected Set<JMEElementView> views;

	protected UndoManager manager;
	protected boolean transitoire;

	private int oldX;
	private int oldY;

	private transient float speedX;
	private transient float speedY;

	public JMENode(JMEGraph graph, String name, int x, int y, JMENodeKind k) {
		this.graph = graph;
		this.x = x;
		this.y = y;
		this.name = name;
		views = new HashSet<>();
		transitoire = false;
		modified = false;
		multiplicity = new JMENodeMultiplicity(1, 1);

		this.manager = graph.getUndoManager();
		this.orbit = new JerboaOrbit();
		this.kind = k;
		this.explicits = new ArrayList<>();
		this.implicits = new ArrayList<>();
		this.required = new ArrayList<>();
		this.color = new Color(255, 255, 255, 0);

		this.precondition = "";

		topoErrors = 0;
		ebdsErrors = 0;

		paramTopo = new JMEParamTopo(graph.getRule(), this);
	}

	public JMENode(JMENode node, JMEGraph newgraph) {
		this.name = node.name;
		this.orbit = new JerboaOrbit(node.orbit);
		this.kind = JMENodeKind.SIMPLE;
		this.explicits =new ArrayList<>();
		this.implicits = new ArrayList<>();
		this.required = new ArrayList<>();
		this.color = new Color(node.color.getRGB());
		this.manager = node.getUndoManager();
		
		this.x = node.x;
		this.y = node.y;
		this.graph = newgraph;
		views = new HashSet<>();
		transitoire = false;
		modified = false;
		multiplicity =new JMENodeMultiplicity(node.multiplicity.getMin(), node.multiplicity.getMax());
		this.precondition = "";
		topoErrors = 0;
		ebdsErrors = 0;
		paramTopo = new JMEParamTopo(newgraph.getRule(), this);
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	public void beginTrans() {
		transitoire = true;
		oldX = x;
		oldY = y;
	}

	public void endTrans() {
		transitoire = false;
		manager.registerUndo(new UndoItemField(this, "position", new Point(oldX, oldY), new Point(x, y), !modified));
		modified = true; // TODO a voir
		update();
	}

	@Override
	public boolean isModified() {
		for (JMENodeExpression expr : explicits) {
			if (expr.isModified())
				return true;
		}
		return modified;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point getPoint() {
		return new Point(x, y);
	}

	public JerboaOrbit getOrbit() {
		return orbit;
	}

	public List<JMENodeExpression> getExplicitExprs() {
		return explicits;
	}

	public List<JMENodeExpression> getImplicitExprs() {
		// return graph.computeImplicitExprs(this);
		return implicits;
	}

	public List<JMENodeExpression> getRequiredExprs() {
		return required;
	}
	
	// on suppose que les expr, impl et req sont correctes
	public List<JMENodeExpression> getDefaultExprs() {
		ArrayList<JMENodeExpression> list = new ArrayList<>();
		for (JMENodeExpression expr : required) {
			final JMEEmbeddingInfo ebdinfo = expr.getEbdInfo();
			if(ebdinfo != null && ebdinfo.hasDefaultCode()) {
				Set<JMENode> nodes = getGraph().orbit(this, ebdinfo.getOrbit());
				JMENode lowestDart = this;
				int lowestID = this.getID();
				for (JMENode node : nodes) {
					if(node.getID() < lowestID) {
						lowestDart = node;
						lowestID = node.getID();
					}
				}
				if(lowestDart == this) {
					list.add(new JMENodeExpression(this, ebdinfo, ebdinfo.getDefaultCode())); // HAK on peut adapter le code potentiellement?
				}
			}
		}
		
		return list;
	}

	public JMENodeKind getKind() {
		return kind;
	}

	public void setKind(JMENodeKind kind) {
		if (this.kind != kind) {
			if (!transitoire) {
				manager.registerUndo(new UndoItemField(this, "kind", this.kind, kind, !modified));
				modified = true;
			}
			this.kind = kind;
			if (kind == JMENodeKind.HOOK)
				graph.getRule().addParamTopo(paramTopo);
			else
				graph.getRule().delParamTopo(paramTopo);
		}
	}

	public void addExplicitExpression(JMENodeExpression exp) {
		if (!explicits.contains(exp)) {
			explicits.add(exp);
			graph.updateAllExprs();
		}
	}

	public void setImplicitExpression(List<JMENodeExpression> exprs) {
		this.implicits = new ArrayList<>(exprs);
	}

	public void setRequiredExpression(List<JMENodeExpression> exprs) {
		this.required.clear();
		this.required = new ArrayList<>(exprs);
	}

	public int getTopoErrors() {
		Collection<JMEError> errors = getRule().getAllErrors();
		int i = 0;
		for (JMEError e : errors) {
			if(e.getTarget() == this && e.getType() == JMEErrorType.TOPOLOGIC)
				i++;
		}
		return i;
	}

	public int getEbdsErrors() {
		Collection<JMEError> errors = getRule().getAllErrors();
		int i = 0;
		for (JMEError e : errors) {
			if(e.getTarget() == this && e.getType() == JMEErrorType.EMBEDDING)
				i++;
		}
		return i;
	}


	public void removeExpression(JMENodeExpression expr) {
		for (JMENodeExpression e : explicits) {
			if (e.getEbdInfo().getName().compareTo(expr.getEbdInfo().getName()) == 0) {
				explicits.remove(e);
				graph.updateAllExprs();
				break;
			}
		}
	}

	public void setOrbit(JerboaOrbit jerboaOrbit) {
		if (!orbit.equalsStrict(jerboaOrbit)) {
			if (!transitoire) {
				manager.registerUndo(new UndoItemField(this, "orbit", this.orbit, orbit, !modified));
				modified = true;
			}
			this.orbit = jerboaOrbit;
			update();
		}
	}

	public void setPosition(int dx, int dy) {
		if (x != dx || y != dy) {
			x = dx;
			y = dy;
			if (!transitoire) {
				manager.registerUndo(new UndoItemField(this, "position", new Point(x, y), new Point(dx, dy), !modified));
				modified = true;
			}
			update();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(name).append(":").append(x).append(';').append(y);
		sb.append(']');
		return sb.toString();
	}

	public void setName(String text) {
		if (text != null && !text.equals(name)) {
			if (!transitoire) {
				manager.registerUndo(new UndoItemField(this, "name", name, text, !modified));
				modified = true;
			}
			this.name = text;
			update();
		}
	}

	public void setColor(Color color) {
		if(color != null && !color.equals(this.color)) {
			this.color = color;
			modified = true;
			update();
		}
	}

	public void setMultiplicity(JMENodeMultiplicity multiplicity) {
		if (multiplicity != null && !multiplicity.equals(this.multiplicity)) {
			if (!transitoire) {
				manager.registerUndo(new UndoItemField(this, "multiplicity", this.multiplicity, multiplicity, !modified));
				modified = true;
			}
			this.multiplicity = multiplicity;
			update();
		}
	}

	public JMENodeMultiplicity getMultiplicity() {
		return multiplicity;
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
	
	public Set<JMEElementView> getViews() {
		return views;
	}

	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		manager.transfertRedo(fitem);
		switch (fitem.field()) {
		case "name":
			name = (String) fitem.value();
			break;
		case "multiplicity":
			multiplicity = (JMENodeMultiplicity) fitem.value();
			break;
		case "orbit":
			orbit = (JerboaOrbit) fitem.value();
			break;
		case "kind":
			kind = (JMENodeKind) fitem.value();
			break;
		case "position":
			Point pos = (Point) fitem.value();
			x = pos.x;
			y = pos.y;
			break;
		}
		if (fitem.getModifState())
			modified = false;

		update();
	}

	@Override
	public void redo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		manager.transfertUndo(fitem);
		switch (fitem.field()) {
		case "name":
			name = (String) fitem.newValue();
			break;
		case "multiplicity":
			multiplicity = (JMENodeMultiplicity) fitem.newValue();
			break;
		case "orbit":
			orbit = (JerboaOrbit) fitem.newValue();
			break;
		case "kind":
			kind = (JMENodeKind) fitem.newValue();
			break;
		case "position":
			Point pos = (Point) fitem.newValue();
			x = pos.x;
			y = pos.y;
			break;
		}
		if (fitem.getModifState())
			modified = true;
		update();
	}

	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitNode(this);
	}

	public JMERule getRule() {
		return graph.getRule();
	}

	@Override
	public UndoManager getUndoManager() {
		return manager;
	}

	public JMENodeExpression searchExpression(JMEEmbeddingInfo ebdinfo) {
		for (JMENodeExpression jmeNodeExpression : explicits) {
			if (jmeNodeExpression.getEbdInfo() == ebdinfo) {
				return jmeNodeExpression;
			}
		}

		List<JMENodeExpression> implicits = getImplicitExprs();
		for (JMENodeExpression jmeNodeExpression : implicits) {
			if (jmeNodeExpression.getEbdInfo() == ebdinfo) {
				return jmeNodeExpression;
			}
		}

		// il faut le creer
		// HAK: non j'aurais dit qu'il faut lever l'exception mais bon on verra
		// a l'usage.
		JMENodeExpression nodeexpr = new JMENodeExpression(this, ebdinfo, "");
		explicits.add(nodeexpr);
		return nodeexpr;
	}


	@Override
	public void resetModification() {
		modified = false;
	}

	public Color getColor() {
		return color;
	}

	public boolean isLeftNode() {
		return graph.isLeft();
	}

	public boolean isRightNode() {
		return !graph.isLeft();
	}


	public boolean existExpression(JMENodeExpression mene) {
		for (JMENodeExpression jmeNodeExpression : getExplicitExprs()) {
			if (jmeNodeExpression.equalsEbd(mene)) {
				return true;
			}
		}

		for (JMENodeExpression jmeNodeExpression : getImplicitExprs()) {
			if (mene.equalsEbd(jmeNodeExpression)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean existExpression(JMEEmbeddingInfo ebdinfo) {
		for (JMENodeExpression jmeNodeExpression : getExplicitExprs()) {
			if (jmeNodeExpression.equalsEbd(ebdinfo)) {
				return true;
			}
		}

		for (JMENodeExpression jmeNodeExpression : getImplicitExprs()) {
			if (jmeNodeExpression.equalsEbd(ebdinfo)) {
				return true;
			}
		}
		return false;
	}

	public String getPrecondition() {
		return precondition;
	}

	public void setPrecondition(String n) {
		if(n != null && !n.equals(precondition)) {
			this.precondition = n;
			modified = true;
			update();
		}
	}

	public int getID() {
		List<JMENode> nodes = graph.getNodes();
		for (int i = 0;i < nodes.size(); ++i) {
			JMENode n = nodes.get(i);
			if(n == this)
				return i;
		}
		return -1;
	}

	public JMENode copy(JMEGraph graph) {
		JMENode node = new JMENode(graph, name, x, y, kind);
		node.color = color;
		node.kind = kind;
		node.multiplicity = multiplicity;
		node.orbit = new JerboaOrbit(orbit.tab());
		
		node.precondition = precondition;
		JMEModeler modeler = node.getRule().getModeler();
		for (JMENodeExpression explicit : explicits) {
			JMEEmbeddingInfo info = modeler.search(explicit.getEbdInfo().getName());
			if(info != null) {
				node.addExplicitExpression(new JMENodeExpression(node, info, explicit.getExpression()));
			}
		}
		
		return node;
	}
	
	public JMEGraph getGraph(){
		return graph;
	}
	
	
	public List<JMEArc> alphas() {
		return (graph.getIncidentArcsFromNode(this));
	}
	
	
	public JMENode alpha(int i) {
		List<JMEArc> arcs = alphas();
		for (JMEArc arc : arcs) {
			if(arc.getDimension() == i) {
				if(arc.getSource() == this )
					return arc.getDestination();
				else if(arc.getDestination() == this)
					return arc.getSource();
			}
		}
		return null;
	}

}
