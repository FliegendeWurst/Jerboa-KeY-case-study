package fr.up.xlim.sic.ig.jerboa.jme.view.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMELoop;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.util.JMEMath;
import fr.up.xlim.sic.ig.jerboa.jme.util.SVGGraphics2D;

public class JMELoopView extends JMEArcView {

	// protected double angle;
	protected ArrayList<Point> effpoints; // points effectifs
	private int refX;
	private int refY;
	private double refAngle;

	private Point rotLabel;

	private JMELoop loop;
	private double viewAngle;

	public static int COUNTSEGMENT = 100;
	public static double STEP = 1. / COUNTSEGMENT;

	public JMELoopView(RuleGraphView owner, JMELoop arc) {
		super(owner, arc);
		this.loop = arc;
		// M 0 3 L 10 10 L 40 20 L 40 -20 L 10 -10 L 0 -3
		/*
		 *  <path d="M 0 2 L 10 7 C 38 20 , 38 -20 , 10 -7 L 0 -2"  fill="none" stroke="red" />
   <path d="M 0 4 L 10 9 C 42 22 , 42 -22 , 10 -9 L 0 -4"  fill="none" stroke="green" />
		 */
		points.add(new Point(0, 3));
		points.add(new Point(10, 10));
		points.add(new Point(40, 20));
		points.add(new Point(40, -20));
		points.add(new Point(10, -10));
		points.add(new Point(0, -3));

		points.add(new Point(0, 4));
		points.add(new Point(10, 9));
		points.add(new Point(42, 22));
		points.add(new Point(42, -22));
		points.add(new Point(10, -9));
		points.add(new Point(0, -4));

		points.add(new Point(0, 2));
		points.add(new Point(10, 7));
		points.add(new Point(38, 20));
		points.add(new Point(38, -20));
		points.add(new Point(10, -7));
		points.add(new Point(0, -2));

		labelX = 42;
		labelY = 0;
		rotLabel = new Point(45, 0);

		effpoints = new ArrayList<>();
	}

	public static void fillBezierDegree5(int[] allX, int[] allY, final Rectangle range, Point p0, Point p1, Point p2,
			Point p3, Point p4, Point p5, double scaleFactor) {
		int pos = 0;
		for(double t = 0; t <= 1 && pos < COUNTSEGMENT; t+= STEP) {
			allX[pos] = (int) (((1 * coefBezier(t, 0, 5) * p0.x
					+  5*coefBezier(t, 1, 5)*p1.x
					+ 10*coefBezier(t, 2, 5)*p2.x
					+ 10*coefBezier(t, 3, 5)*p3.x
					+  5*coefBezier(t, 4, 5)*p4.x
					+ 1 * coefBezier(t, 5, 5) * p5.x) - range.x) * scaleFactor);
			allY[pos] = (int) (((1 * coefBezier(t, 0, 5) * p0.y
					+  5*coefBezier(t, 1, 5)*p1.y
					+ 10*coefBezier(t, 2, 5)*p2.y
					+ 10*coefBezier(t, 3, 5)*p3.y
					+  5*coefBezier(t, 4, 5)*p4.y
					+ 1 * coefBezier(t, 5, 5) * p5.y) - range.y) * scaleFactor);
			pos++;
		}
	}

	@Override
	public void draw(Graphics2D g, Rectangle range, boolean highlight, double scaleFactor) {
		if (dirty) {
			update(g, range, highlight);
		}
		JMEPreferences pref = owner.getPreferences();
		Color old = g.getColor();
		final int size = 6; // effpoints.size();
		final int dim = arc.getDimension();
		Color line = Color.black;
		if (pref != null)
			line = pref.dimColor(dim);
		else {
			if(dim == 0)
				line = Color.BLACK;
			else if (dim == 1)
				line = new Color(200,0,0);
			else if (dim == 2)
				line = new Color(0,0,200);
			else if (dim == 3)
				line = new Color(0,200,0);
		}
		g.setColor(line);
		if (pref != null)
			g.setStroke(pref.dimStroke(dim));
		else {
			g.setStroke(new BasicStroke(1.0f));
		}

		int[] allX = new int[COUNTSEGMENT];
		int[] allY = new int[COUNTSEGMENT];
		if(g instanceof SVGGraphics2D &&  !pref.getOldLoopExportSVG()) {
			SVGGraphics2D gsvg = (SVGGraphics2D)g;
			if(dim == 2) {
				Point p0 = p(effpoints.get(6), range, scaleFactor);
				Point p1 = p(effpoints.get(7), range, scaleFactor);
				Point p2 = p(effpoints.get(8), range, scaleFactor);
				Point p3 = p(effpoints.get(9), range, scaleFactor);
				Point p4 = p(effpoints.get(10), range, scaleFactor);
				Point p5 = p(effpoints.get(11), range, scaleFactor);
				gsvg.drawBezierDegree5(p0,p1,p2,p3,p4,p5);
				p0 = p(effpoints.get(12), range, scaleFactor);
				p1 = p(effpoints.get(13), range, scaleFactor);
				p2 = p(effpoints.get(14), range, scaleFactor);
				p3 = p(effpoints.get(15), range, scaleFactor);
				p4 = p(effpoints.get(16), range, scaleFactor);
				p5 = p(effpoints.get(17), range, scaleFactor);
				gsvg.drawBezierDegree5(p0,p1,p2,p3,p4,p5);
				// gsvg.drawBezierDegree5(effpoints.get(12), effpoints.get(13), effpoints.get(14), effpoints.get(15), effpoints.get(16), effpoints.get(17));
			}
			else {
				Point p0 = p(effpoints.get(6), range, scaleFactor);
				Point p1 = p(effpoints.get(7), range, scaleFactor);
				Point p2 = p(effpoints.get(8), range, scaleFactor);
				Point p3 = p(effpoints.get(9), range, scaleFactor);
				Point p4 = p(effpoints.get(10), range, scaleFactor);
				Point p5 = p(effpoints.get(11), range, scaleFactor);
				gsvg.drawBezierDegree5(p0,p1,p2,p3,p4,p5);
				//gsvg.drawBezierDegree5(effpoints.get(0), effpoints.get(1), effpoints.get(2), effpoints.get(3), effpoints.get(4), effpoints.get(5));
			}
		}
		else {
			if(dim == 2 && pref.getOldDoubleLineA2()) {
				fillBezierDegree5(allX, allY, range, effpoints.get(6), effpoints.get(7), effpoints.get(8),
						effpoints.get(9), effpoints.get(10), effpoints.get(11), scaleFactor);
				g.drawPolyline(allX, allY, allX.length);
				fillBezierDegree5(allX, allY, range, effpoints.get(12), effpoints.get(13), effpoints.get(14),
						effpoints.get(15), effpoints.get(16), effpoints.get(17), scaleFactor);
				g.drawPolyline(allX, allY, allX.length);
			}
			else {
				fillBezierDegree5(allX, allY, range, effpoints.get(0), effpoints.get(1), effpoints.get(2),
						effpoints.get(3), effpoints.get(4), effpoints.get(5), scaleFactor);
				g.drawPolyline(allX, allY, allX.length);
			}

			if(highlight){
				for(int i = 0;i < (size-1); i++) {
					Point a = effpoints.get(i);
					Point b = effpoints.get(i+1);
					if(dim == 2 && pref.getOldDoubleLineA2()) {
						Point perpAB = JMEMath.perpendicularity(a, b);
						double norm = Math.sqrt(perpAB.getX()*perpAB.getX() + perpAB.getY()*perpAB.getY());
						Point a1 = new Point((int)(a.x + perpAB.getX()*2/norm),(int) (a.y + perpAB.getY()*2/norm));
						Point a2 = new Point((int)(a.x - perpAB.getX()*2/norm),(int) (a.y - perpAB.getY()*2/norm));
						Point b1 = new Point((int)(b.x + perpAB.getX()*2/norm),(int) (b.y + perpAB.getY()*2/norm));
						Point b2 = new Point((int)(b.x - perpAB.getX()*2/norm),(int) (b.y - perpAB.getY()*2/norm));

						g.drawLine((int) ((a1.x - range.x) * scaleFactor), (int) ((a1.y - range.y) * scaleFactor),
								(int) ((b1.x - range.x) * scaleFactor), (int) ((b1.y - range.y) * scaleFactor));
						g.drawLine((int) ((a2.x - range.x) * scaleFactor), (int) ((a2.y - range.y) * scaleFactor),
								(int) ((b2.x - range.x) * scaleFactor), (int) ((b2.y - range.y) * scaleFactor));
					}
					else
						g.drawLine((int) ((a.x - range.x) * scaleFactor), (int) ((a.y - range.y) * scaleFactor),
								(int) ((b.x - range.x) * scaleFactor), (int) ((b.y - range.y) * scaleFactor));
				}
				g.setStroke(pref.getDefaultBasicStroke());

				line = pref.getSelectColor();
				g.setColor(line);
				Point s = effpoints.get(0);
				g.fillRect((int) ((s.x - range.x - DELTA * 2) * scaleFactor),
						(int) ((s.y - range.y - DELTA * 2) * scaleFactor), (int) ((DELTA * 4) * scaleFactor),
						(int) ((DELTA * 4) * scaleFactor));
				Point f = effpoints.get(effpoints.size() - 1);
				g.fillRect((int) ((f.x - range.x - DELTA * 2) * scaleFactor),
						(int) ((f.y - range.y - DELTA * 2) * scaleFactor), (int) ((DELTA * 4) * scaleFactor),
						(int) ((DELTA * 4) * scaleFactor));

				for (int i = 0; i < (size - 1); i++) {
					Point a = effpoints.get(i);
					g.fillOval((int) ((a.x - range.x - DELTA * 2) * scaleFactor),
							(int) ((a.y - range.y - DELTA * 2) * scaleFactor), (int) ((DELTA * 4) * scaleFactor),
							(int) ((DELTA * 4) * scaleFactor));
				}

			}
		}
		if (owner != null && owner.getPreferences() != null)
			g.setStroke(owner.getPreferences().getDefaultBasicStroke());
		else
			g.setStroke(new BasicStroke(1.0f));


		if ((arc.getNbErrors() > 0) && owner != null && owner.getPreferences() != null
				&& owner.getPreferences().getShowErrors())
		{
			int cx = 0;
			int cy = 0;

			for (Point point : effpoints) {
				cx += point.x;
				cy += point.y;
			}
			cx /= effpoints.size();
			cy /= effpoints.size();
			Color backup = g.getColor();
			drawArcInError(g, backup, cx - range.x, cy - range.y);
			g.setColor(backup);
		}

		if (labelName != null && !labelName.isEmpty()) {
			g.drawString(labelName, (int) ((labelX - range.x) * scaleFactor), (int) ((labelY - range.y) * scaleFactor));
		}
		g.setColor(old);
	}

	private Point p(Point point, Rectangle range, double scaleFactor) {
		return new Point((int) ((point.x - range.x) * scaleFactor), (int) ((point.y - range.y) * scaleFactor));
	}

	public static double coefBezier(double t, int i, int n) {
		return Math.pow(t, i) * Math.pow(1.0 - t, n - i);
	}

	@Override
	public void update(Graphics2D g, Rectangle range, boolean highlight) {
		// points.clear();
		effpoints.clear();
		JMENode msrc = arc.getSource();
		JMENodeView src = owner.getNodeView(msrc);

		double angle = viewAngle;
		Point center = new Point(msrc.getX(), msrc.getY());
		Point p = new Point(100, 0);
		Point q = JMEMath.rotationZ(angle, p);

		q = src.coord(q.x + center.x, q.y + center.y);

		for (Point point : points) {
			Point a = JMEMath.rotationZ(angle, point);
			a.x += q.x;
			a.y += q.y;
			effpoints.add(a);
		}

		Point newRotLab = JMEMath.rotationZ(angle, rotLabel);
		labelX = newRotLab.x + q.x;
		labelY = newRotLab.y + q.y;

		if (owner.getPreferences() != null && owner.getPreferences().getShowDim()) {
			if (owner.getPreferences().getShowAlpha())
				labelName = RuleGraphView.ALPHA + arc.getDimension();
			else
				labelName = "" + arc.getDimension();
		} else
			labelName = "";

		FontMetrics metrics = g.getFontMetrics();
		int lwidth = metrics.stringWidth(labelName);
		int lheight = metrics.getHeight();
		labelX = labelX - (lwidth / 2);
		labelY = labelY - (lheight / 2) + metrics.getAscent();// - ldescent;
	}

	/* Perpendicularite
	 * u.v = 0
	 * u.x * v.x + u.y*v.y = 0
	 * donc si u( a b) alors est v(-b a)
	 *
	 */

	@Override
	public boolean inside(int x, int y) {
		int size = effpoints.size();
		for (int i = 0; i < (size - 1); i++) {
			Point p = effpoints.get(i);
			Point q = effpoints.get(i + 1);
			Point c = new Point(x, y);
			double d = JMEMath.distanceDroiteAB(p, q, c);
			boolean isinside = JMEMath.insideSegment(p, q, c);
			boolean step = (d < DELTA * 4) && isinside;
			// System.out.println("DIST: "+d+" ISINSIDE:
			// "+JMEMath.insideSegment(p, q, c));
			if (step)
				return true;
		}
		return false;
	}

	@Override
	public void beginMove(int x, int y) {
		this.refX = x;
		this.refY = y;
		this.refAngle = loop.getAngle();
		this.viewAngle = loop.getAngle();
		transitoire = true;
		super.beginMove(x, y);
	}

	@Override
	public void endMove(int x, int y) {
		super.endMove(x, y);
		loop.setAngle(this.viewAngle);
		transitoire = false;
		//System.out.println("ENDMOVE: ANGLE: " + viewAngle);
	}

	@Override
	public void move(int dx, int dy) {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("L(");
		sb.append(arc.toString());
		sb.append(")");
		return sb.toString();
	}

	@Override
	public void tryMove(int dx, int dy) {

		JMENode msrc = arc.getSource();
		JMENodeView src = owner.getNodeView(msrc);

		Point q = src.coord(refX + dx, refY + dy);

		Point center = new Point(msrc.getX(), msrc.getY());
		Point ref = new Point(refX - center.x, refY - center.y);
		Point ray = new Point(q.x - center.x, q.y - center.y);

		double deltaAngle = JMEMath.angle(ref, ray);
		viewAngle = refAngle + deltaAngle;
		dirty = true;
	}

	public void rotate(double angle) {
		viewAngle += angle;
		loop.setAngle(viewAngle);
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

	@Override
	public void reload() {
		viewAngle = loop.getAngle();
		super.reload();
	}

	@Override
	public ArrayList<Point> getPoints() {
		return effpoints;
	}
}
