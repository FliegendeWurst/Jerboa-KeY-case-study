package fr.up.xlim.sic.ig.jerboa.jme.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.util.JMEComplexDrawable;
import fr.up.xlim.sic.ig.jerboa.jme.util.JMEMath;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class JMEArcView implements JMEComplexDrawable, JMEElementView {

	protected JMEArc arc;
	protected boolean dirty;
	protected RuleGraphView owner;

	protected int labelX, labelY;
	protected String labelName;
	protected int far;
	protected int index;
	protected Point movePoint;
	protected ArrayList<Point> points;
	protected boolean transitoire;

	public static final int DELTA = 2;
	public static final double THRESHOLDANGLE = Math.PI / 25.0; // 5 degrees

	public JMEArcView(RuleGraphView owner, JMEArc arc) {
		this.arc = arc;
		points = new ArrayList<>();
		dirty = true;
		this.owner = owner;
		arc.addView(this);
		far = 15;
		transitoire = false;
	}

	@Override
	public void draw(Graphics2D g, Rectangle range, boolean highlight, double scaleFactor) {
		if (dirty) {
			update(g, range, highlight);
		}
		final JMEPreferences pref = owner.getPreferences();
		Color old = g.getColor();
		int size = points.size();
		int dim = arc.getDimension();
		Color line = Color.black;
		if (pref != null)
			line = pref.dimColor(dim);
		else {
			if (dim == 1)
				line = new Color(200,0,0);
			else if (dim == 2)
				line = new Color(0,0,200);
			else if (dim == 3)
				line = new Color(0,200,0);
		}

		g.setColor(line);
		if (pref != null)
			g.setStroke(pref.getDefaultBasicStroke());
		else {
			g.setStroke(new BasicStroke(1.0f));
		}

		// dessin des traits de construction
		for (int i = 0; i < (size - 1); i++) {
			g.setStroke((pref != null ? pref.dimStroke(dim) : new BasicStroke(1.0f)));
			Point a = points.get(i);
			Point b = points.get(i + 1);
			if (dim == 2 && pref.getOldDoubleLineA2()) {
				Point perpAB = JMEMath.perpendicularity(a, b);
				double norm = Math.sqrt(perpAB.getX() * perpAB.getX() + perpAB.getY() * perpAB.getY());
				Point a1 = new Point((int) (a.x + perpAB.getX() * 2 / norm), (int) (a.y + perpAB.getY() * 2 / norm));
				Point a2 = new Point((int) (a.x - perpAB.getX() * 2 / norm), (int) (a.y - perpAB.getY() * 2 / norm));
				Point b1 = new Point((int) (b.x + perpAB.getX() * 2 / norm), (int) (b.y + perpAB.getY() * 2 / norm));
				Point b2 = new Point((int) (b.x - perpAB.getX() * 2 / norm), (int) (b.y - perpAB.getY() * 2 / norm));

				g.drawLine((int) ((a1.x - range.x) * scaleFactor), (int) ((a1.y - range.y) * scaleFactor),
						(int) ((b1.x - range.x) * scaleFactor), (int) ((b1.y - range.y) * scaleFactor));
				g.drawLine((int) ((a2.x - range.x) * scaleFactor), (int) ((a2.y - range.y) * scaleFactor),
						(int) ((b2.x - range.x) * scaleFactor), (int) ((b2.y - range.y) * scaleFactor));
			} else {
				g.drawLine((int) ((a.x - range.x) * scaleFactor), (int) ((a.y - range.y) * scaleFactor),
						(int) ((b.x - range.x) * scaleFactor), (int) ((b.y - range.y) * scaleFactor));
			}

		}

		// remise a default des traits
		g.setStroke(pref != null ? pref.getDefaultBasicStroke() : new BasicStroke(1.0f));

		// selection des points. MERDE VALENTIN A ENCORE CHANGE CE CODE
		if (highlight) {
			line = pref != null ? pref.getSelectColor() : Color.CYAN;
			g.setColor(line);
			Point s = points.get(0);
			g.fillRect((int) ((s.x - range.x - DELTA * 2) * scaleFactor),
					(int) ((s.y - range.y - DELTA * 2) * scaleFactor), (int) ((DELTA * 4) * scaleFactor),
					(int) ((DELTA * 4) * scaleFactor));
			Point f = points.get(points.size() - 1);
			g.fillRect((int) ((f.x - range.x - DELTA * 2) * scaleFactor),
					(int) ((f.y - range.y - DELTA * 2) * scaleFactor), (int) ((DELTA * 4) * scaleFactor),
					(int) ((DELTA * 4) * scaleFactor));
		}
		{
			for (int i = 1; i < (size - 1); i++) {
				Point a = points.get(i);
				g.fillOval((int) ((a.x - range.x - DELTA * 2) * scaleFactor),
						(int) ((a.y - range.y - DELTA * 2) * scaleFactor), (int) ((DELTA * 4) * scaleFactor),
						(int) ((DELTA * 4) * scaleFactor));
			}

		}

		if ((arc.getNbErrors() > 0) && owner.getPreferences() != null && owner.getPreferences().getShowErrors())
		{
			for (int i = 0; i < (size - 1); i++) {
				Point a = points.get(i);
				Point b = points.get(i + 1);

				int cx = (a.x + b.x) / 2;
				int cy = (a.y + b.y) / 2;
				Color backup = g.getColor();
				drawArcInError(g, backup, (int) ((cx - range.x) * scaleFactor), (int) ((cy - range.y) * scaleFactor));
				g.setColor(backup);
			}
		}


		if (labelName != null && !labelName.isEmpty()) {
			final int lx = labelX;
			final int ly = labelY;
			g.drawString(labelName, (int) ((lx - range.x) * scaleFactor), (int) ((ly - range.y) * scaleFactor));
		}
		g.setColor(old);
	}


	public void drawArcInError(Graphics2D g, Color def, int cx, int cy) {
		try {
			FontMetrics metrics = g.getFontMetrics();
			int fontheight = metrics.getHeight();
			// int descent = metrics.getDescent();
			int lheight = (int) (1.5 * fontheight); // largeur du carre aussi
			final int lwidth = metrics.stringWidth("!");

			Color errColor = Color.yellow;
			Polygon triangle = new Polygon();
			triangle.addPoint(cx - (lheight/2), cy + (lheight/2));
			triangle.addPoint(cx + (lheight/2),  cy + (lheight/2));
			triangle.addPoint(cx, cy - (lheight/2));
			g.setColor(errColor);
			g.fillPolygon(triangle);
			g.setColor(def);
			g.drawPolygon(triangle);
			g.drawString("!", cx - (lwidth/2) , cy + (fontheight/2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Point searchLabelLoc() {
		if (points.size() % 2 == 1) {
			int mi = points.size() / 2;
			Point o = points.get(mi - 1);
			Point p = points.get(mi);
			Point q = points.get(mi + 1);
			Point op = new Point(p.x - o.x, p.y - o.y);
			Point qp = new Point(p.x - q.x, p.y - q.y);
			Point pp = new Point(op.x + qp.x, op.y + qp.x);
			int dist = (int) Math.sqrt(pp.x * pp.x + pp.y * pp.y);
			dist = Math.max(dist, 1);
			Point pn = new Point(pp.x / dist, pp.y / dist);
			return new Point(p.x + pn.x * far, p.y + pn.y * far);
		} else {
			int mi = points.size() / 2;
			Point p = points.get(mi - 1);
			Point q = points.get(mi);

			Point m = new Point((p.x + q.x) / 2, (p.y + q.y) / 2);
			Point v = JMEMath.perpendicularity(p, q);
			int dist = (int) Math.sqrt(v.x * v.x + v.y * v.y);
			dist = Math.max(dist, 1);
			return new Point(m.x + (v.x / dist) * far, m.y + (v.y / dist) * far);
		}
	}

	@Override
	public void update(Graphics2D g, Rectangle range, boolean highlight) {
		// points.clear();

		JMENode msrc = arc.getSource();
		JMENode mdest = arc.getDestination();

		JMENodeView src = owner.getNodeView(msrc);
		JMENodeView dest = owner.getNodeView(mdest);

		Point p;
		Point q;
		if (!transitoire)
			points = new ArrayList<>(arc.getPoints());

		if (points.size() > 0) {
			Point pp = points.get(1);
			Point pq = points.get(points.size() - 2);
			p = src.coord(pp.x, pp.y);
			q = dest.coord(pq.x, pq.y);
			if (p != null)
				points.set(0, p);
			if (q != null)
				points.set(points.size() - 1, q);
		} else {
			p = src.coord(mdest.getX(), mdest.getY());
			q = dest.coord(msrc.getX(), msrc.getY());
			if (p != null)
				points.add(p);
			if (q != null)
				points.add(q);
		}

		// je ne sais pas pourquoi mais p ou q peuvent etre null???
		if (p != null && q != null && p.distance(q) >= 2) {
			if ((owner.getPreferences() != null ? owner.getPreferences().getShowDim() : false)) {
				if (owner.getPreferences().getShowAlpha())
					labelName = RuleGraphView.ALPHA + arc.getDimension();
				else
					labelName = "" + arc.getDimension();
			} else
				labelName = "";

			FontMetrics metrics = g.getFontMetrics();
			int lwidth = metrics.stringWidth(labelName);
			int lheight = metrics.getHeight();
			// int ldescent = metrics.getDescent();
			Point label = searchLabelLoc();
			labelX = label.x - (lwidth / 2);
			labelY = label.y - (lheight / 2) + metrics.getAscent(); // -
			// ldescent;
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/*
	 * Perpendicularite u.v = 0 u.x * v.x + u.y*v.y = 0 donc si u( a b) alors
	 * est v(-b a)
	 *
	 */

	@Override
	public boolean inside(int x, int y) {
		int size = points.size();
		for (int i = 0; i < (size - 1); i++) {
			Point p = points.get(i);
			Point q = points.get(i + 1);
			Point c = new Point(x, y);
			double d = JMEMath.distanceDroiteAB(p, q, c);
			boolean isinside = JMEMath.insideSegment(p, q, c);
			boolean step = (d < DELTA * 4) && isinside;
			if (step)
				return true;
		}
		return false;
	}

	public void simplify() {
		for (int i = 0; i < points.size() - 2; i++) {
			Point a = points.get(i);
			Point b = points.get(i + 1);
			Point c = points.get(i + 2);
			double angle = JMEMath.angle(a, b, c);
			if (Math.abs(angle) <= THRESHOLDANGLE) {
				points.remove(i + 1);
			}
		}
		if (points.size() == 2) {
			points.clear();
		}
	}

	@Override
	public void move(int dx, int dy) {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("A(");
		sb.append(arc.toString());
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int centerX() {
		Point p = points.get(0);
		Point q = points.get(1);
		int x = (q.x + p.x) / 2;
		return x;
	}

	@Override
	public int centerY() {
		Point p = points.get(0);
		Point q = points.get(1);
		int y = (q.y + p.y) / 2;
		return y;
	}

	@Override
	public void tryMove(int dx, int dy) {
		if (index > 0) {
			points.set(index, new Point(movePoint.x + dx, movePoint.y + dy));
		}
	}

	/**
	 * Le grainGrid ne sert a rien pour les arcs
	 */
	@Deprecated
	@Override
	public void tryMove(int dx, int dy, int grainGrid) {
		if (index > 0) {
			points.set(index, new Point(movePoint.x + dx, movePoint.y + dy));
		}
	}

	@Override
	public void endMove(int x, int y) {
		if (index > 0) {
			points.set(index, new Point(x, y));
			simplify();
			arc.setPoints(points);
		}
		transitoire = false;
	}

	@Override
	public void beginMove(int x, int y) {
		index = -1;
		transitoire = true;
		int size = points.size();
		for (int i = 0; i < (size - 1); i++) {
			Point p = points.get(i);
			Point q = points.get(i + 1);
			Point c = new Point(x, y);
			if (near(p, c) && i != 0) {
				index = i;
				movePoint = new Point(p);
				return;
			}
			if (near(q, c) && (i + 1) != size - 1) {
				index = i + 1;
				movePoint = new Point(q);
				return;
			}
			double d = JMEMath.distanceDroiteAB(p, q, c);
			boolean step = (d < DELTA * 4) && JMEMath.insideSegment(p, q, c);
			if (step) {
				index = i + 1;
				break;
			}
		}
		if (index > 0) {
			Point p = new Point(x, y);
			movePoint = new Point(p);
			points.add(index, p);
			dirty = true;
		}
	}

	protected boolean near(Point p, Point c) {
		return p.distance(c) < DELTA * 4;
	}

	@Override
	public Rectangle bbox() {
		int xmin, xmax, ymin, ymax;
		xmin = ymin = Integer.MAX_VALUE;
		xmax = ymax = Integer.MIN_VALUE;
		for (Point p : points) {
			xmin = Math.min(p.x, xmin);
			xmax = Math.max(p.x, xmax);
			ymin = Math.min(p.y, ymin);
			ymax = Math.max(p.y, ymax);

		}
		return new Rectangle(xmin, ymin, xmax - xmin, ymax - ymin);
	}

	public JMEArc getArc() {
		return arc;
	}

	@Override
	public JMEElement getModel() {
		return arc;
	}

	public int getLabelX() {
		return labelX;
	}

	public int getLabelY() {
		return labelY;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public String getLabel() {
		return labelName;
	}

	@Override
	public void reload() {
		dirty = true;
		owner.repaint();
	}

	@Override
	public void unlink() {
		arc.removeView(this);
	}

	@Override
	public JMEElement getSourceElement() {
		return arc;
	}
}
