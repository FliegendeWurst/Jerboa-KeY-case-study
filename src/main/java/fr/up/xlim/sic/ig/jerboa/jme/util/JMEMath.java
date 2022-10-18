/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Hakim Belhaouari
 *
 */
public final class JMEMath {

	private JMEMath() {
		// TODO Auto-generated constructor stub
	}

	public static Point intersectionPaveDroiteNearest(Point leftup, Point rightdown, Point a, Point b, Point c) {
		List<Point> pts = intersectionPaveDroite(leftup, rightdown, a, b);
		Point res = null;
		double d = Double.MAX_VALUE;
		for (Point p : pts) {
			double dist = p.distance(c);
			if (dist <= d) {
				d = dist;
				res = p;
			}
		}
		return res;
	}

	/**
	 * Permet de calculer l'angle de 3 points BAC. En particulier entre AB et
	 * AC.
	 *
	 * @param a:
	 *            premier point geometrique/
	 * @param b:
	 *            second point
	 * @param c:
	 *            troisieme point
	 * @return renvoie l'angle en radian.
	 */
	public static double angle(Point a, Point b, Point c) {
		Point ab = new Point(b.x - a.x, b.y - a.y);
		Point ac = new Point(c.x - a.x, c.y - a.y);
		return angle(ab, ac);
	}

	/**
	 * Renvoie l'angle entre deux vecteurs autour de l'axe Z (formant un repere
	 * orthogonal direct)
	 *
	 * @param u:
	 *            premier vecteur
	 * @param v:
	 *            second vecteur
	 * @return Renvoie l'angle en radian.
	 */
	public static double angle(Point u, Point v) {
		double un = Math.sqrt(u.x * u.x + u.y * u.y);
		double vn = Math.sqrt(v.x * v.x + v.y * v.y);

		double scal = u.x * v.x + u.y * v.y;
		double cos = scal / (un * vn);

		double det = u.x * v.y - u.y * v.x;
		double sin = det / (un * vn);

		double t1 = Math.acos(cos);
		double t2 = Math.asin(sin);

		if (t2 >= 0)
			return t1;
		else
			return -t1;
	}

	public static Point rotationZ(double angle, Point q) {
		double px = q.getX() * Math.cos(angle) - q.getY() * Math.sin(angle);
		double py = q.getX() * Math.sin(angle) + q.getY() * Math.cos(angle);

		return new Point((int) px, (int) py);
	}

	/*
	 * Perpendicularite u.v = 0 u.x * v.x + u.y*v.y = 0 donc si u( a b) alors
	 * est v(-b a)
	 *
	 */

	/**
	 * Renvoie la distance d'un point par rapport a une droite.
	 *
	 * @param a:
	 *            premier point de la droite
	 * @param b:
	 *            autre point de la droite
	 * @param p:
	 *            point exterieur a la droite.
	 * @return renvoie la distance de p a la droite (ab).
	 */
	public static double distanceDroiteAB(Point a, Point b, Point p) {
		Point ap = new Point(p.x - a.x, p.y - a.y);
		Point ab = new Point(b.x - a.x, b.y - a.y);

		double sinus = Math.abs(Math.sin(angle(ap, ab)));
		double dist = a.distance(p);
		return dist * sinus;

		// double coef = (b.x == a.x ? 0 : (b.y - a.y) / (b.x - a.x));
		// if (coef == 0) {
		// return Math.abs(p.x - a.x);
		// }
		// double ord = a.y - coef * a.x;
		//
		// double dist = Math.abs(coef * p.x - p.y + ord) / Math.sqrt(coef *
		// coef + 1);
		// return dist;

	}

	/**
	 * Calcul le vecteur de la droite perpendiculaire
	 *
	 * @param a:
	 *            un point de la droite
	 * @param b:
	 *            autre point de la droite
	 * @return renvoie le vecteur de la droite perpendiculaire.
	 */
	public static Point perpendicularity(Point a, Point b) {
		Point ab = new Point(b.x - a.x, b.y - a.y);
		return new Point(-ab.y, ab.x);
	}

	public static boolean insideSegment(Point a, Point b, Point c) {
		double abx = b.x - a.x;
		double aby = b.y - a.y;

		double acx = c.x - a.x;
		double acy = c.y - a.y;

		double cbx = b.x - c.x;
		double cby = b.y - c.y;

		double scalABAC = abx * acx + aby * acy;
		double scalABCB = abx * cbx + aby * cby;

		if (scalABAC * scalABCB < 0)
			return false;
		else
			return true;
	}

	public static Point[] intersectionCercleDroite(Point center, double rayon, Point p, Point q) {
		double a = (q.x - p.x) * (q.x - p.x) + (q.y - p.y) * (q.y - p.y);
		double b = 2 * ((q.x - p.x) * (p.x - center.x) + (q.y - p.y) * (p.y - center.y));
		double c = center.x * center.x + center.y * center.y + p.x * p.x + p.y * p.y
				- 2 * (center.x * p.x + center.y * p.y) - rayon * rayon;

		double delta = b * b - 4 * a * c;
		if (delta < 0) {
			return null;
		} else {
			double k1 = (-b + Math.sqrt(delta)) / (2 * a);
			double k2 = (-b - Math.sqrt(delta)) / (2 * a);

			Point p1 = new Point((int) (p.x + k1 * (q.x - p.x)), (int) (p.y + k1 * (q.y - p.y)));
			Point p2 = new Point((int) (p.x + k2 * (q.x - p.x)), (int) (p.y + k2 * (q.y - p.y)));

			return new Point[] { p1, p2 };
		}
	}

	public static Point intersectionCercleDroiteNearest(Point center, double rayon, Point p, Point q, Point c) {
		Point[] res = intersectionCercleDroite(center, rayon, p, q);
		double d1 = res[0].distance(c);
		double d2 = res[1].distance(c);

		if (d1 < d2)
			return res[0];
		else
			return res[1];
	}

	public static Point intersectionDroiteDroite(Point p, Point q, Point r, Point s) {
		double x = 0, y = 0;

		int dx1 = q.x - p.x;
		int dy1 = q.y - p.y;

		int dx2 = s.x - r.x;
		int dy2 = s.y - r.y;

		int n1 = dx1 * dy2;
		int n2 = dx2 * dy1;
		// int delta = Math.abs(n1 - n2);
		if (n1 == n2) {
			// colineaire
			return null;
		}

		double a = (p.x == q.x ? 0 : ((double) dy1 / (double) dx1));
		double b = p.y - (a * p.x);

		double c = r.x == s.x ? 0 : ((double) dy2 / (double) dx2);
		double d = r.y - (c * r.x);

		if (p.x == q.x) {
			x = p.x;
			y = c * x + d;
		} else if (r.x == s.x) {
			x = r.x;
			y = a * x + b;
		} else {
			x = (d - b) / (a - c);
			y = (a * x + b);
		}

		return new Point((int) x, (int) y);
	}

	/*
	 * (x - c.x)^2/r1^2 + (y - c.y)^2/r2^2 = 1 y = ax + b
	 *
	 *
	 * (x - c.x)^2/ r1^2 + (y - c.y)^2 / r2^2 = 1
	 *
	 * (x^2 - 2*x*c.x + c.x^2)/r1^2 + (y^2 - 2*y*c.y + c.y^2)/ r2^2 = 1 (x^2 -
	 * 2*x*c.x + c.x^2)/r1^2 + ((a*x + b)^2 - 2*(a*x + b)*c.y + c.y^2)/ r2^2 = 1
	 * (x^2 - 2*x*c.x + c.x^2)/r1^2 + (a*a*x^2 + 2ab*x + b^2 - 2a*c.y*x -
	 * 2*b*c.y + c.y^2) /r2^2 = 1 r2^2*x^2 - 2*c.x*r2^2*x +r2^2*c.x^2 +
	 * a^2*r1^2*x^2 + 2*a*b*r1^2*x+ r1^2*b^2 - 2*a*c.y*r1^2*x - 2*b*c.y*r1^2 +
	 * r1^2*c.y^2 = r1^2*r2^2
	 *
	 * Soit: A = r2^2 + a^2*r1^2 B = -2*c.x*r2^2 + 2*a*b*r1^2 - 2*a*c.y*r1^2 C =
	 * r2^2*c.x^2 + r1^2*b^2 - 2*b*c.y*r1^2 + r1^2*c.y^2 - r1^2*r2^2 Donc Ax^2 +
	 * B x + C = 0 delta = B*B - 4*A*C
	 *
	 */
	public static List<Point> intersectionEllipseDroite(Point c, double r1, double r2, Point p, Point q) {
		int dx = q.x - p.x;
		int dy = q.y - p.y;
		double a = (dx == 0 ? 0 : ((double) dy / (double) dx));
		double b = p.y - a * p.x;

		double r = r2 * r2 + a * a * r1 * r1;
		double s = 2 * r1 * r1 * a * b - 2 * c.x * r2 * r2 - 2 * c.y * r1 * r1 * a;
		double t = r2 * r2 * c.x * c.x + r1 * r1 * b * b - 2 * c.y * r1 * r1 * b + r1 * r1 * c.y * c.y
				- r1 * r1 * r2 * r2;

		double delta = s * s - 4 * r * t;
		if (delta >= 0) {
			double x1 = (-s + Math.sqrt(delta)) / (2 * r);
			double x2 = (-s - Math.sqrt(delta)) / (2 * r);

			double y1 = a * x1 + b;
			double y2 = a * x2 + b;

			ArrayList<Point> res = new ArrayList<>();
			res.add(new Point((int) x1, (int) y1));
			res.add(new Point((int) x2, (int) y2));

			return res;
		} else
			return null;
	}
	/*
	 * double dx = q.x - p.x; double dy = q.y - p.y; double a = (dx == 0? 0 :
	 * dy/dx); double b = p.y - a*p.x;
	 *
	 *
	 * double r = r2*r2 + a*r1*r1; double s = 2*r1*r1*a*b - 2*c.x*r2*r2 -
	 * 2*c.y*r1*r1*a; double t = r2*r2*c.x*c.x + r1*r1*b*b - 2*c.y*r1*r1*b +
	 * r1*r1*c.y*c.y - 1;
	 *
	 *
	 * double delta = s*s - 4*r*t; if(delta >= 0) { double x1 = (-s +
	 * Math.sqrt(delta)) / (2*r); double x2 = (-s - Math.sqrt(delta)) / (2*r);
	 *
	 * double y1 = a*x1 + b; double y2 = a*x2 + b;
	 *
	 * return new Point[] { new Point((int)x1,(int)y1), new
	 * Point((int)x2,(int)y2) }; } else return null; }
	 */

	public static Point intersectionEllipseDroiteNearest(Point c, double r1, double r2, Point p, Point q, Point a) {
		List<Point> pts = intersectionEllipseDroite(c, r1, r2, p, q);
		double d1 = a.distance(pts.get(0));
		double d2 = a.distance(pts.get(1));
		if (d1 < d2)
			return pts.get(0);
		else
			return pts.get(1);
	}

	public static List<Point> intersectionPaveDroite(Point leftup, Point rightdown, Point a, Point b) {

		int dx = rightdown.x - leftup.x;
		int dy = rightdown.y - leftup.y;
		if (dx == dy && dx == 0)
			return null;

		Point p1 = new Point(leftup);
		Point p2 = new Point(rightdown.x, leftup.y);
		Point p3 = new Point(rightdown);
		Point p4 = new Point(leftup.x, rightdown.y);

		Point r1 = intersectionDroiteDroite(a, b, p1, p2);
		Point r2 = intersectionDroiteDroite(a, b, p2, p3);
		Point r3 = intersectionDroiteDroite(a, b, p3, p4);
		Point r4 = intersectionDroiteDroite(a, b, p4, p1);

		ArrayList<Point> res = new ArrayList<>();
		if (r1 != null && insideSegment(p1, p2, r1))
			res.add(r1);
		if (r2 != null && insideSegment(p2, p3, r2))
			res.add(r2);
		if (r3 != null && insideSegment(p3, p4, r3))
			res.add(r3);
		if (r4 != null && insideSegment(p4, p1, r4))
			res.add(r4);

		return res;

	}

	public class TestPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Point a, b;

		public TestPanel(Point _a, Point _b) {
			a = _a;
			b = _b;
		}

		@Override
		public void paint(Graphics g) {
			for (int i = 0; i < getWidth(); i++) {
				for (int j = 0; j < getHeight(); j++) {
					int res = (int) distanceDroiteAB(a, b, new Point(i, j));
					if (res == 8) {
						g.setColor(Color.RED);
					} else
						g.setColor(new Color(res, false));

					g.fillRect(i, j, 1, 1);
				}
			}

			g.setColor(Color.WHITE);
			g.drawLine(a.x, a.y, b.x, b.y);
		}
	}

	public static void main(String[] args) {
		Point l = new Point(77, 173);
		Point r = new Point(77 + 24, 173 + 19);

		List<Point> res;

		// ARC: A((n1:280;129]--0--n0:63;103]))
		Point a = new Point(89, 182);
		Point b = new Point(296, 207);
		System.out.println("A: " + a + "\t\tB: " + b);
		res = JMEMath.intersectionPaveDroite(l, r, a, b);
		System.out.println("RES: " + res);
		System.out.println();

		Point center = new Point(3, 1);
		double r1 = 1;
		double r2 = 1.41;

		Point d = new Point(1, 4);
		Point e = new Point(5, -2);
		List<Point> pts = JMEMath.intersectionEllipseDroite(center, r1, r2, d, e);
		System.out.println("POINTS: " + pts);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMEMath m = new JMEMath();
		JPanel pan = m.new TestPanel(new Point(400, 0), new Point(400, 800));
		f.setMinimumSize(new Dimension(600, 600));
		f.add(pan, BorderLayout.CENTER);
		f.setVisible(true);
	}

	public static Point vector(Point q1, Point q2) {
		int x = q2.x - q1.x;
		int y = q2.y - q1.y;
		return new Point(x, y);
	}

}
