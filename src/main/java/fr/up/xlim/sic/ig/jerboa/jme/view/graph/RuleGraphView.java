/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.view.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMELoop;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeKind;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMENodeShape;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.util.JMEDrawable;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleView;
import up.jerboa.core.JerboaOrbit;

/**
 *
 * @author Hakim Belhaouari
 *
 */
public class RuleGraphView extends JPanel
implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, JMEElementView {
	//TODO : Val : désolé mais je n'en peux plus de cette option
	public static boolean useRange = false;

	public static final String ALPHA = "\u03B1";
	private static final long serialVersionUID = -7547749360006990078L;

	private int defaultWidth, defaultHeight;
	private double scaleZoom;

	private HashMap<JMEArc, JMEArcView> arcs;
	private HashMap<JMENode, JMENodeView> nodes;
	private HashMap<JMEElement, JMEDrawable> objects;
	private ArrayList<JMEDrawable> selections;
	private Color backgroundcolor;

	private Point selectionRectangleOrigin;
	private Point selectionRectangleTmp;

	private RuleView owner;
	private JMEGraph graph;
	private JMERule rule;

	private Point lastMousePress;
	private Point posMouseDrag;

	private long whenLastMousePress;
	private Rectangle range;

	private JMEDrawable elementHoveredToMove;
	private transient boolean mousePressWasOnElement;

	public RuleGraphView(RuleView rv, JMERule rule, JMEGraph graph, boolean editable) {
		super(new BorderLayout(), true);
		this.owner = rv;
		this.rule = rule;
		this.graph = graph;
		backgroundcolor = Color.white;

		selectionRectangleOrigin = null;

		graph.addView(this);
		scaleZoom = 1;

		elementHoveredToMove = null;

		setBorder(new LineBorder(new Color(0, 0, 0), 0));

		objects = new HashMap<>();
		selections = new ArrayList<>();
		arcs = new HashMap<>();
		nodes = new HashMap<>();

		if (editable) {
			// a faire en dernier pour eviter les appels debiles
			addKeyListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
		}


		range = new Rectangle(0, 0, 400, 300);
		setPreferredSize(new Dimension(400, 300));
		defaultWidth = getWidth();
		defaultHeight = getHeight();

		reload();
	}

	@Override
	public void paint(Graphics og) {
		super.paint(og);
		try {
			if (og instanceof Graphics2D && graph != null) {
				Graphics2D g = (Graphics2D) og;
				setBackground(backgroundcolor);
				g.setBackground(backgroundcolor);
				g.clearRect(0, 0, getWidth(), getHeight());
				if (rule.isShowGrid()) {
					Color discolor = getPreferences().getGridColor();
					g.setColor(discolor);
					int step = (int) (getPreferences().grainGrid(rule.getGridsize()) * scaleZoom);
					for (int i = 0; i < getWidth(); i += step) {
						g.drawLine(i, 0, i, getHeight());
					}
					for (int j = 0; j < getHeight(); j += step) {
						g.drawLine(0, j, getWidth(), j);
					}
				}
				Font f = g.getFont();
				if(owner!=null)
					f = getPreferences().getDefaultFont();

				f = new Font(f.getName(), f.getStyle(), (int) (f.getSize() * scaleZoom));
				g.setFont(f);
				if (owner != null)
					g.setStroke(getPreferences().getDefaultBasicStroke());
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				Color fgcolor = Color.BLACK;
				if (owner != null)
					fgcolor = getPreferences().getFGColor();
				g.setColor(fgcolor);
				for (Entry<JMENode, JMENodeView> o : nodes.entrySet()) {
					if (!selections.contains(o.getValue()))
						o.getValue().draw(g, useRange?range:getVisibleRect(), false, scaleZoom);
				}

				for (Entry<JMEArc, JMEArcView> o : arcs.entrySet()) {
					if (!selections.contains(o.getValue()))
						o.getValue().draw(g, useRange?range:getVisibleRect(), false, scaleZoom);
				}

				Color selcolor = Color.cyan;
				if (owner != null)
					selcolor = getPreferences().getSelectColor();
				g.setColor(selcolor);
				for (JMEDrawable o : selections) {
					o.draw(g, useRange?range:getVisibleRect(), true, scaleZoom);
				}

				g.setXORMode(backgroundcolor);
				g.setColor(selcolor);
				if (posMouseDrag != null && lastMousePress != null) {
					g.drawLine(lastMousePress.x, lastMousePress.y, posMouseDrag.x, posMouseDrag.y);
				}
				if (selectionRectangleOrigin != null && selectionRectangleTmp != null) {
					Color col = getGraphics().getColor();
					g.setColor(new Color(0.8f, 0.9f, 0.8f, 0.5f));
					g.fillRect(Math.min(selectionRectangleTmp.x, selectionRectangleOrigin.x),
							Math.min(selectionRectangleTmp.y, selectionRectangleOrigin.y),
							Math.abs(selectionRectangleTmp.x - selectionRectangleOrigin.x),
							Math.abs(selectionRectangleTmp.y - selectionRectangleOrigin.y));
					g.setColor(col);
				}
			}
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}

	public Rectangle updateRangeDimension() {
		int xmin, xmax, ymin, ymax;
		xmin = ymin = 0;
		xmax = ymax = 50;
		//TODO: VAL : correction du bug de range et des noeuds qui se collent en haut a gauche
//		xmin = ymin = Integer.MAX_VALUE;
//		xmax = ymax = Integer.MIN_VALUE;
		for (JMEDrawable o : objects.values()) {
			Rectangle p = o.bbox();
			xmin = Math.min((int) ((p.x) * scaleZoom), xmin);
			xmax = Math.max((int) ((p.x + p.width) * scaleZoom), xmax);
			ymin = Math.min((int) ((p.y) * scaleZoom), ymin);
			ymax = Math.max((int) ((p.y + p.height) * scaleZoom), ymax);
		}
		range = new Rectangle(magnetic(xmin), magnetic(ymin), magnetic(xmax - xmin), magnetic(ymax - ymin));
		this.setPreferredSize(new Dimension(range.width - range.x, range.height - range.y));
		this.revalidate(); // on force le redimentionnement
		repaint(); // on force le dessin en cas de pepin
		return range;
	}

	public JMEGraph getGraph() {
		return graph;
	}

	public JMERule getRule() {
		return rule;
	}

	public String genOrbitStr(JMENode node) {
		StringBuilder sb = new StringBuilder("<");
		JerboaOrbit orbit = node.getOrbit();
		if (orbit.size() > 0) {

			int val = orbit.get(0);
			if (val < 0)
				sb.append("_");
			else {
				if (owner == null || getPreferences().getShowAlpha())
					sb.append(ALPHA);
				sb.append(val);
			}
		}

		for (int i = 1; i < orbit.size(); i++) {
			sb.append(", ");
			int val = orbit.get(i);
			if (val < 0)
				sb.append("_");
			else {
				if (owner == null || getPreferences().getShowAlpha())
					sb.append(ALPHA);
				sb.append(val);
			}
		}
		sb.append(">");
		return sb.toString();
	}

	public JMEPreferences getPreferences() {
		if (owner != null)
			return owner.getPreferences();
		return null;
	}

	/* DEBUT DE LA LISTE DES EVENEMENTS DEVANT ETRE GERE */

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	private void clearSelection() {
		if (owner != null)
			for (JMEDrawable s : selections) {
				owner.remSelect(this, s.getModel());
			}
		selections.clear();
	}

	public void addSelection(JMEDrawable d) {
		if (!selections.contains(d)) {
			selections.add(d);
			if (owner != null)
				owner.addSelect(this, d.getModel());
		}
	}

	public void removeSelection(JMEDrawable d) {
		if (selections.contains(d)) {
			owner.remSelect(this, d.getModel());
			selections.remove(d);
		}
	}

	public void deleteArc(JMEArc av){
		graph.removeArc(av);
		arcs.remove(av);
		objects.remove(av);
	}

	public void deleteNode(JMENodeView nv){
		JMENode node = nv.getNode();
		HashSet<JMEArc> arcList = new HashSet<>();
		for(JMEArc a : arcs.keySet()){
			arcList.add(a);
		}
		for(JMEArc a : arcList){
			if(a.getDestination() == node
					|| a.getSource()==node) {
				deleteArc(a);
			}
		}
		graph.removeNode(node);
		nodes.remove(node);
		objects.remove(node);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) { // ctrl + a
			if (selections.size() != nodes.size()) {
				clearSelection();
				for (JMENodeView n : nodes.values()) {
					addSelection(n);
				}
				
			} else {
				clearSelection();
			}
		}
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W)
			owner.close(); // TODO: ce n'est pas le bon endroit mais dans les
		// internalFrame ï¿½a marche pas
		else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) { // ctrl + c
			List<JMEDrawable> elList = new ArrayList<>();
			for (JMEDrawable esel : selections) {
				elList.add(esel);
			}
			owner.getEditor().copy(elList);
		} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) { // ctrl + v
			paste();
		} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) { // ctrl + z
			if (e.isShiftDown())
				graph.getUndoManager().redo();
			else
				graph.getUndoManager().undo();
		} else if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) { // si suppression
			ArrayList<JMEDrawable> dlist = new ArrayList<JMEDrawable> (selections);
			for (JMEDrawable n : dlist) {
				// System.err.println(n);
				removeSelection(n);
				if (n instanceof JMEArcView) {
					deleteArc(((JMEArcView) n).getArc());
				} else if (n instanceof JMENodeView) {
					deleteNode((JMENodeView)n);
				}
			}
			clearSelection();
			owner.reload();
			check();
		} else {
			ArrayList<JMEDrawable> dlist = new ArrayList<JMEDrawable>(selections);
			for (JMEDrawable d : dlist) {
				if (d instanceof JMEArcView) {
					JMEArcView av = (JMEArcView) d;
					if (e.getKeyCode() == KeyEvent.VK_NUMPAD0 || e.getKeyCode() == KeyEvent.VK_0) {
						// the number 0 (pad or keyboard)
						av.getArc().setDimension(0);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_1) { // KeyEvent.VK_NUMPAD0
						av.getArc().setDimension(1);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_2) {
						av.getArc().setDimension(2);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyCode() == KeyEvent.VK_3) {
						av.getArc().setDimension(3);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_4) {
						av.getArc().setDimension(4);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD5 || e.getKeyCode() == KeyEvent.VK_5) {
						av.getArc().setDimension(5);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_6) {
						av.getArc().setDimension(6);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD7 || e.getKeyCode() == KeyEvent.VK_7) {
						av.getArc().setDimension(7);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_8) {
						av.getArc().setDimension(8);
						check();
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD9 || e.getKeyCode() == KeyEvent.VK_9) {
						av.getArc().setDimension(9);
						check();
					}
				} else if (d instanceof JMENodeView) {
					JMENodeView nv = (JMENodeView) d;
					JerboaOrbit orb = nv.getNode().getOrbit();
					if (e.getKeyCode() == KeyEvent.VK_NUMPAD0 || e.getKeyCode() == KeyEvent.VK_0) {
						if (orb.contains(0)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 0));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 0));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_1) {
						if (orb.contains(1)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 1));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 1));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_2) {
						if (orb.contains(2)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 2));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 2));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyCode() == KeyEvent.VK_3) {
						if (orb.contains(3)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 3));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 3));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_4) {
						if (orb.contains(4)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 4));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 4));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD5 || e.getKeyCode() == KeyEvent.VK_5) {
						if (orb.contains(5)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 5));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 5));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_6) {
						if (orb.contains(6)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 6));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 6));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD7 || e.getKeyCode() == KeyEvent.VK_7) {
						if (orb.contains(7)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 7));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 7));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_8) {
						if (orb.contains(8)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 8));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 8));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD9 || e.getKeyCode() == KeyEvent.VK_9) {
						if (orb.contains(9)) {
							nv.getNode().setOrbit(removeDimFromOrbit(orb, 9));
						} else {
							nv.getNode().setOrbit(addDimToOrbit(orb, 9));
						}
					} else if (e.getKeyCode() == KeyEvent.VK_SUBTRACT || e.getKeyCode() == KeyEvent.VK_MINUS) {
						if (e.isShiftDown() || e.isControlDown())
							nv.getNode().setOrbit(removeDimFromOrbit(orb, -1));
						else
							nv.getNode().setOrbit(addDimToOrbit(orb, -1));
					} else if (e.getKeyCode() == KeyEvent.VK_H) {
						if (graph.isLeft()) {
							if (nv.getNode().getKind() != JMENodeKind.HOOK)
								nv.getNode().setKind(JMENodeKind.HOOK);
							else
								nv.getNode().setKind(JMENodeKind.SIMPLE);
						}
					}
					check();
				}
			}
		}
		repaint();
	}

	private JerboaOrbit removeDimFromOrbit(JerboaOrbit o, int dim) {
		Collection<Integer> res = new ArrayList<>();
		if (dim >= 0)
			for (int i = 0; i < o.size(); i++) {
				if (o.get(i) != dim) {
					res.add(o.get(i));
				}
			}
		else {
			ArrayList<Integer> resReversed = new ArrayList<>();
			boolean done = false;
			for (int i = o.size() - 1; i >= 0; i--) {
				if (done || o.get(i) != dim) {
					resReversed.add(o.get(i));
				} else
					done = true;
			}
			for (int i = resReversed.size() - 1; i >= 0; i--) {
				res.add(resReversed.get(i));
			}
		}
		return new JerboaOrbit(res);
	}

	private JerboaOrbit addDimToOrbit(JerboaOrbit o, int dim) {
		if (dim < 0 || !o.contains(dim)) {
			List<Integer> res = new ArrayList<>();
			for (int i = 0; i < o.size(); i++) {
				res.add(o.get(i));
			}
			res.add(new Integer(dim));
			return new JerboaOrbit(res);
		}
		return o;
	}

	public void paste() {
		int nbPaste = 0;
		nbPaste++;
		// unselectAll();
		clearSelection();

		ArrayList<JMENode> old_Node = new ArrayList<JMENode>();
		ArrayList<JMENode> new_Node = new ArrayList<JMENode>();

		for (JMEDrawable dr : owner.getEditor().getCopyCache()) {
			if (dr instanceof JMENodeView) {
				JMENode dr_n = ((JMENodeView) dr).getNode();
				JMENode n = graph.creatNode(dr_n.getX(), dr_n.getY());
				if (graph.isLeft())
					n.setKind(dr_n.getKind());
				n.setOrbit(new JerboaOrbit(dr_n.getOrbit().tab()));

				if (!graph.isLeft())
					for (JMENodeExpression expr : dr_n.getExplicitExprs()) {
						JMENodeExpression exptmp = new JMENodeExpression(n, expr.getEbdInfo(), expr.getExpression());
						n.addExplicitExpression(exptmp);
					}

				old_Node.add(dr_n);
				new_Node.add(n);
			}
		}

		for (JMEDrawable dr : owner.getEditor().getCopyCache()) {
			if (dr instanceof JMEArcView) {
				int i_source = old_Node.indexOf(((JMEArcView) dr).getArc().getSource());
				int i_target = old_Node.indexOf(((JMEArcView) dr).getArc().getDestination());

				if (i_source != -1 && i_target != -1) {
					JMEArc myArc = graph.creatArc(new_Node.get(i_source), new_Node.get(i_target),
							((JMEArcView) dr).getArc().getDimension());
					myArc.setPoints(((JMEArcView) dr).getArc().getPoints());
				}
			}
		}
		reload();
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) > 0) {
			int dx = (e.getX());
			int dy = (e.getY());
			int movex = (int) ((dx - lastMousePress.x) / scaleZoom);
			int movey = (int) ((dy - lastMousePress.y) / scaleZoom);
			if (selectionRectangleOrigin != null) {
				if (selectionRectangleOrigin != null) {
					selectionRectangleTmp = e.getPoint();
				}
			} else if (elementHoveredToMove != null && !selections.contains(elementHoveredToMove)) {
				if (getPreferences() != null
						&& (rule.isMagnetic() || (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) > 0))
					elementHoveredToMove.tryMove(movex, movey, getPreferences().grainGrid(rule.getGridsize()));
				else
					elementHoveredToMove.tryMove(movex, movey);
			} else {
				for (JMEDrawable o : selections) {
					if (getPreferences() != null
							&& (rule.isMagnetic() || (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) > 0))
						o.tryMove(movex, movey, getPreferences().grainGrid(rule.getGridsize()));
					else
						o.tryMove(movex, movey);
				}
			}
			updateRangeDimension();
		}
		if (SwingUtilities.isMiddleMouseButton(e)) {
			for (JMEDrawable o : objects.values()) {
				int dx = (e.getX());
				int dy = (e.getY());
				int movex = (int) ((dx - lastMousePress.x) / scaleZoom);
				int movey = (int) ((dy - lastMousePress.y) / scaleZoom);
				if (o instanceof JMENodeView) {
					if (getPreferences() != null
							&& (rule.isMagnetic() || (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) > 0))
						o.tryMove(movex, movey, getPreferences().grainGrid(rule.getGridsize()));
					else
						o.tryMove(movex, movey);
				}
			}
			updateRangeDimension();
		}
		if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) > 0 && e.getPoint().distance(lastMousePress) > 2) {
			posMouseDrag = e.getPoint();
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// System.out.println("mouseMoved: "+e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getWhen() - whenLastMousePress < (getPreferences() != null ? getPreferences().getDelayClick() : 0.01)) {
			// System.out.println("CLICK");
			requestFocus();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("PRESS");
		if (owner != null)
			owner.setCurrentGraph(this);

		lastMousePress = new Point(e.getPoint());
		whenLastMousePress = e.getWhen();

		elementHoveredToMove = pick(e.getPoint());
		// System.err.println("PICK pressed : pos " + e.getPoint());


		if (SwingUtilities.isMiddleMouseButton(e)) {
			for (JMEDrawable o : objects.values()) {
				if (o instanceof JMENodeView)
					o.beginMove(e.getX() + (useRange?range:getVisibleRect()).x, e.getY() + (useRange?range:getVisibleRect()).y);
			}
		} else if(e.getButton() == MouseEvent.BUTTON1) {
			if (elementHoveredToMove == null) {// && selections.size() == 0) {
				// rectangle de selection
				if (selectionRectangleOrigin == null) {
					selectionRectangleOrigin = e.getPoint();
				}

			} else if (elementHoveredToMove == null || selections.contains(elementHoveredToMove)) {
				for (JMEDrawable o : selections) {
					o.beginMove(e.getX() + (useRange?range:getVisibleRect()).x, e.getY() + (useRange?range:getVisibleRect()).y);
				}
			}else {
				mousePressWasOnElement = true;
				elementHoveredToMove.beginMove(e.getX() + (useRange?range:getVisibleRect()).x, e.getY() + (useRange?range:getVisibleRect()).y);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressWasOnElement = false;
		double distance = e.getPoint().distance(lastMousePress);

		if (e.getButton() == MouseEvent.BUTTON3) {
			createNodeArc(e);
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			int x = magnetic(e.getX() + (useRange?range:getVisibleRect()).x);
			int y = magnetic(e.getY() + (useRange?range:getVisibleRect()).y);

			if (elementHoveredToMove == null) {// && selections.size() == 0) {
				if (selectionRectangleOrigin != null && selectionRectangleTmp != null) {
					if (!e.isControlDown() && !e.isShiftDown())
						clearSelection();
					Rectangle selRect = new Rectangle(
							(int) ((Math.min(selectionRectangleTmp.x, selectionRectangleOrigin.x) - (useRange?range:getVisibleRect()).x)
									/ scaleZoom),
							(int) ((Math.min(selectionRectangleTmp.y, selectionRectangleOrigin.y) - (useRange?range:getVisibleRect()).y)
									/ scaleZoom),
							(int) ((Math.abs(selectionRectangleTmp.x - selectionRectangleOrigin.x)) / scaleZoom),
							(int) ((Math.abs(selectionRectangleTmp.y - selectionRectangleOrigin.y)) / scaleZoom));
					int cptSelect = 0;
					for (JMEDrawable jd : objects.values()) {
						if (selRect.contains(jd.bbox())) {
							addSelection(jd);
							cptSelect++;
						}
					}
					if (cptSelect <= 0)
						clearSelection();
				} else {
					clearSelection();
				}
			} else if (elementHoveredToMove != null && !selections.contains(elementHoveredToMove)) {
				elementHoveredToMove.endMove(x, y);
				if (e.getWhen()
						- whenLastMousePress < (getPreferences() != null ? getPreferences().getDelayClick() : 0.01)
						&& distance < getPreferences().getThresholdClick())
				{
					if(e.isShiftDown() || e.isControlDown()){
						if(selections.contains(elementHoveredToMove))
							removeSelection(elementHoveredToMove);
						else
							addSelection(elementHoveredToMove);
					} else {
						clearSelection();
						addSelection(elementHoveredToMove);
					}
				}
			} else {
				for (JMEDrawable o : selections) {
					o.endMove(x, y);
				}
				if (e.getWhen()
						- whenLastMousePress < (getPreferences() != null ? getPreferences().getDelayClick() : 0.01)
						&& distance < getPreferences().getThresholdClick())
				{
					clearSelection();
				}
			}

		} else if (SwingUtilities.isMiddleMouseButton(e)) {
			if(elementHoveredToMove != null && elementHoveredToMove instanceof JMENodeView) {
				JMENodeView nodeview = ((JMENodeView) elementHoveredToMove);
				if(graph.isLeft()) {
					boolean insideLabel = nodeview.insideLabel(e.getX(), e.getY());
					boolean insideOrbit = nodeview.insideOrbit(e.getX(), e.getY());

					System.out.println("INSIDE LABEL: "+insideLabel+"\t\t  ORBIT: "+insideOrbit);
					if(insideLabel || insideOrbit)
						if (owner != null)
							owner.openNodePrecondition(nodeview.getNode());
						else if(nodeview.inside(e.getX(), e.getY())) {
							Color col = JColorChooser.showDialog(this, "Select fill color of "+nodeview.getNode().getName(), nodeview.getNode().getColor());
							if(col != null) {
								nodeview.getNode().setColor(col);
							}
						}
				}
				else {
					JMEEmbeddingInfo ebdinfo = nodeview.getEbdAtPos(e.getX() - (useRange?range:getVisibleRect()).x, e.getY() - (useRange?range:getVisibleRect()).y);
					if(ebdinfo != null) {
						System.out.println(" EDITION DE: "+ebdinfo);
						if (owner != null)
							owner.openNodeExpression(nodeview.getNode(), ebdinfo);
					}
					else if(nodeview.inside(e.getX(), e.getY())) {
						Color col = JColorChooser.showDialog(this, "Select fill color of "+nodeview.getNode().getName(), nodeview.getNode().getColor());
						if(col != null) {
							nodeview.getNode().setColor(col);
						}
					}
				}

			}
			else {
				for (JMEDrawable o : objects.values()) {
					int x = magnetic(e.getX() + (useRange?range:getVisibleRect()).x);
					int y = magnetic(e.getY() + (useRange?range:getVisibleRect()).y);
					if (o instanceof JMENodeView) {
						o.endMove(x, y);
					}
				}
			}
		}
		posMouseDrag = null;

		selectionRectangleOrigin = null;
		selectionRectangleTmp = null;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isControlDown()) {
			// System.err.println("Scale !");
			double factor = 0.01;
			if (e.isShiftDown())
				factor *= 4;
			if (e.getWheelRotation() < 0)
				zoomFactor(scaleZoom + factor);
			else
				zoomFactor(scaleZoom - factor);
			repaint();
			updateRangeDimension();
		} else {
			for (JMEDrawable d : selections) {
				if (d instanceof JMELoopView) {
					((JMELoopView) d).rotate(e.getWheelRotation() * Math.PI / 30);
				}
			}
			// repaint();
		}

	}

	/* FIN DE LA LISTE DES EVENEMENTS DEVANT ETRE GEREE */

	private void createNodeArc(MouseEvent e) {
		JMENodeView a = searchNodeView(lastMousePress.x + (useRange?range:getVisibleRect()).x, lastMousePress.y + (useRange?range:getVisibleRect()).y);
		JMENodeView b = searchNodeView(e.getX() + (useRange?range:getVisibleRect()).x, e.getY() + (useRange?range:getVisibleRect()).y);

		if (a != null && b != null) {
			JMENode na = a.getNode();
			JMENode nb = b.getNode();
			int dim = guessDim(na, nb);
			if (na == nb) {
				JMELoop loop = graph.creatLoop(na, dim);
				JMELoopView view = new JMELoopView(this, loop);
				loop.setAngle(guessAngle(loop));
				objects.put(loop, view);
				arcs.put(loop, view);
			} else // GROSSE ERREUR: if (getLinkBetween(a.getNode(),
				// b.getNode()) == null)
			{
				JMEArc arc = graph.creatArc(na, nb, dim);
				JMEArcView view = new JMEArcView(this, arc);
				objects.put(arc, view);
				arcs.put(arc, view);
			}
		}
		if (a == null && b == null) {
			createNode(e);
		}

		check();
	}

	private double guessAngle(JMELoop loop) {
		ArrayList<Double> loops = new ArrayList<>();
		for (JMEArc a : graph.getArcs()) {
			if(loop.sameNoDim(a)) {
				loops.add(((JMELoop)a).getAngle());
			}
		}
		int i = 0;
		double dixDegree = Math.random()*Math.PI/6;
		while(i < 10) {
			double tangle = (Math.random()* 6*Math.PI) % (2*Math.PI);
			for (Double d : loops) {
				double delta = d % (2*Math.PI);
				if( Math.abs(delta - tangle) >= dixDegree)
					return tangle;
			}
			i++;
		}
		return 0;
	}

	private int guessDim(JMENode na, JMENode nb) {
		Integer d = 0;
		HashSet<Integer> allreadyUsed = new HashSet<>();
		for (JMEArc a : graph.getArcs()) {
			if (a.getSource() == na || a.getDestination() == nb
					|| a.getSource() == nb || a.getDestination() == na) {
				allreadyUsed.add(a.getDimension());
			}
		}
		for (int i = 0; i < na.getOrbit().size(); i++) {
			if (na.getOrbit().get(0) >= 0)
				allreadyUsed.add(na.getOrbit().get(i));
		}
		for (int i = 0; i < nb.getOrbit().size(); i++) {
			if (nb.getOrbit().get(0) >= 0)
				allreadyUsed.add(nb.getOrbit().get(i));
		}
		for (int i = 0; i <= na.getRule().getModeler().getDimension(); i++) {
			if (!allreadyUsed.contains(i)) {
				d = i;
				break;
			}
		}
		// System.err.println(allreadyUsed);
		return d;
	}

	/*
	 * x et y sont dans les coord du canvas et non de l'affichage...
	 */
	private JMENodeView searchNodeView(int x, int y) {
		for (JMEDrawable o : objects.values()) {
			if (o instanceof JMENodeView) {
				JMENodeView v = (JMENodeView) o;
				if (v.inside(x, y))
					return v;
			}
		}
		return null;
	}

	private void createNode(MouseEvent e) {
		int x = magnetic(e.getX() + (useRange?range:getVisibleRect()).x);
		int y = magnetic(e.getY() + (useRange?range:getVisibleRect()).y);

		JMENode node = this.graph.creatNode(x, y);
		JMENodeView nodeview = new JMENodeView(this, node);
		objects.put(node, nodeview);
		nodes.put(node, nodeview);
	}

	public void zoomFactor(double d) {
		this.scaleZoom = d;
		// invalidate();
	}

	private int magnetic(int x) {
		if (getPreferences() != null && rule.isMagnetic()) {
			int step = getPreferences().grainGrid(rule.getGridsize());
			int coef = x / step;
			return (coef * step);
		} else
			return x;
	}

	/**
	 *
	 * @param p
	 *            point dans le repere ecran et non du canevas
	 * @return
	 */
	public JMEDrawable pick(Point p) {
		ArrayList<JMEDrawable> dispo = new ArrayList<>();
		// int px = magnetic(p.x + (useRange?range:getVisibleRect()).x);
		// int py = magnetic(p.y + (useRange?range:getVisibleRect()).y);
		// Val : j'enleve le magnetic sur le click souris car c'est insupportable
		int px = (int) ((p.x + (useRange?range:getVisibleRect()).x) / scaleZoom);
		int py = (int) ((p.y + (useRange?range:getVisibleRect()).y) / scaleZoom);

		for (JMENodeView node : nodes.values()) {
			if (node.inside(px, py)) {
				dispo.add(node);
			}
		}
		getGraphics().drawOval((int) (px * scaleZoom) - (useRange?range:getVisibleRect()).x - 5, (int) (py * scaleZoom) - (useRange?range:getVisibleRect()).y - 5, 10, 10);
		for (JMEArcView arc : arcs.values()) {
			if (arc.inside(px , py )) {
				dispo.add(arc);
			}
		}

		if (dispo.size() > 0)
			return dispo.get(0);
		else
			return null;
	}

	public List<JMENodeView> searchNodes(Point p) {
		ArrayList<JMENodeView> dispo = new ArrayList<>();
		for (JMENodeView node : nodes.values()) {
			if (node.inside(p.x, p.y)) {
				dispo.add(node);
			}
		}
		return dispo;
	}

	public List<JMEArcView> searchArcs(Point p) {
		ArrayList<JMEArcView> dispo = new ArrayList<>();
		for (JMEArcView node : arcs.values()) {
			if (node.inside(p.x, p.y)) {
				dispo.add(node);
			}
		}
		return dispo;
	}

	public JMENodeView getNodeView(JMENode source) {
		for (JMEDrawable o : objects.values()) {
			if (o instanceof JMENodeView) {
				JMENodeView v = (JMENodeView) o;
				if (v.getNode() == source)
					return v;
			}
		}
		return null;
	}

	private void reloadInThreadUI() {

		{
			List<JMENode> allnodes = graph.getNodes();
			for (JMENode n : allnodes) {
				if (!nodes.containsKey(n)) {
					JMENodeView view = new JMENodeView(this, n);
					nodes.put(n, view);
					objects.put(n, view);
				}
			}
		}

		{
			List<JMEArc> allarcs = graph.getArcs();
			for (JMEArc a : allarcs) {
				if (!arcs.containsKey(a)) {
					JMEArcView view = null;
					if (a instanceof JMELoop) {
						view = new JMELoopView(this, (JMELoop) a);
					} else {
						view = new JMEArcView(this, a);
					}
					arcs.put(a, view);
					objects.put(a, view);
				}
			}
		}

		for (JMEDrawable e : objects.values()) {
			e.reload();
		}
		repaint();
	}

	@Override
	public void reload() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				reloadInThreadUI();
			}
		});
	}

	@Override
	public void unlink() {
		graph.removeView(this);
	}

	public void simuLostFocus() {
		backgroundcolor = getPreferences().getDeselectGraphColor();
		repaint();
	}

	public void simuGainFocus() {
		backgroundcolor = getPreferences().getSelectGraphColor();
		repaint();
	}

	public void setBackgroundColor(Color color) {
		backgroundcolor = color;
		repaint();
	}

	public Rectangle getRange() {
		return range;
	}

	public HashMap<JMEArc, JMEArcView> getArcs() {
		return arcs;
	}

	public HashMap<JMENode, JMENodeView> getNodes() {
		return nodes;
	}

	public JMENodeShape getShape() {
		return owner.getRule().getShape();
	}

	void check() {
		System.out.println("CHECK");
		owner.check();
	}

	@Override
	public JMEElement getSourceElement() {
		return rule;
	}
	
	public void layout() {
		
	}
	
	public void resetLayout() {
		
	}
}
