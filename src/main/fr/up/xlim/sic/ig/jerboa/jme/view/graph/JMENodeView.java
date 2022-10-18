/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.view.graph;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeKind;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.util.JMEComplexDrawable;
import fr.up.xlim.sic.ig.jerboa.jme.util.JMEMath;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import up.jerboa.util.Triplet;

/**
 *
 *
 * @author hakim
 *
 */
public class JMENodeView implements JMEComplexDrawable, JMEElementView {
	protected JMENode node;
	protected ArrayList<Triplet<Rectangle, String, Color>> contents;
	protected int x, y, width, height;
	protected RuleGraphView owner;
	protected boolean dirty;
	protected double theta;

	protected transient int lastX;
	protected transient int lastY;
	protected transient int deltaX;
	protected transient int deltaY;

	protected transient Rectangle errorsArea;
	protected transient Rectangle labelArea;

	public JMENodeView(RuleGraphView view, JMENode node) {
		this.node = node;
		this.owner = view;
		contents = new ArrayList<>();
		dirty = true;
		theta = 1.5707963267948966192313216916398; // -90 degree
		node.addView(this);
	}

	/**
	 *
	 * @see fr.up.xlim.sic.ig.jerboa.jme.util.JMEDrawable#draw(java.awt.Graphics2D,
	 *      Rectangle, boolean)
	 */
	@Override
	public void draw(Graphics2D g, Rectangle range, boolean highlight, double scaleFactor) {
		if (dirty) {
			update(g, range, highlight);
		}
		FontMetrics metrics = g.getFontMetrics();
		int descent = metrics.getDescent();

		final JMEPreferences pref = getPreferences();
		int padhook = 3;
		if (pref != null)
			padhook = pref.getPadHook();
		final int padhalfhook = padhook / 2;

		Color backup = g.getColor();

		if (pref != null && pref.getIsFillNode()) {
			g.setColor(node.getColor());
			// DESSIN DU FOND DE COULEUR
			switch (owner.getRule().getShape()) {
			case CIRCLE:
				if (node.getKind() == JMENodeKind.HOOK)
					g.fillOval((int) ((x - range.x - padhalfhook) * scaleFactor),
							(int) ((y - range.y - padhalfhook) * scaleFactor), (int) ((width + padhook) * scaleFactor),
							(int) ((height + padhook) * scaleFactor));
				else
					g.fillOval((int) ((x - range.x) * scaleFactor), (int) ((y - range.y) * scaleFactor),
							(int) ((width) * scaleFactor), (int) ((height) * scaleFactor));
				break;
			case RECTANGLE:
				if (node.getKind() == JMENodeKind.HOOK) {
					g.fillRect((int) ((x - range.x - padhalfhook) * scaleFactor),
							(int) ((y - range.y - padhalfhook) * scaleFactor), (int) ((width + padhook) * scaleFactor),
							(int) ((height + padhook) * scaleFactor));
				}
				else
					g.fillRect((int) ((x - range.x) * scaleFactor), (int) ((y - range.y) * scaleFactor),
							(int) ((width) * scaleFactor), (int) ((height) * scaleFactor));
				break;
			case ROUNDRECTANGLE: {
				int arc = pref.getArcRoundRectangle();
				if (node.getKind() == JMENodeKind.HOOK)
					g.fillRoundRect((int) ((x - range.x - padhalfhook) * scaleFactor),
							(int) ((y - range.y - padhalfhook) * scaleFactor), (int) ((width + padhook) * scaleFactor),
							(int) ((height + padhook) * scaleFactor), (int) ((arc) * scaleFactor),
							(int) ((arc) * scaleFactor));
				else
					g.fillRoundRect((int) ((x - range.x) * scaleFactor), (int) ((y - range.y) * scaleFactor),
							(int) ((width) * scaleFactor), (int) ((height) * scaleFactor), (int) ((arc) * scaleFactor),
							(int) ((arc) * scaleFactor));

				break;
			}
			case ELLIPSE:
				if (node.getKind() == JMENodeKind.HOOK) {
					g.fillOval((int) ((x - range.x - padhalfhook) * scaleFactor),
							(int) ((y - range.y - padhalfhook) * scaleFactor), (int) ((width + padhook) * scaleFactor),
							(int) ((height + padhook) * scaleFactor));
				}
				else
					g.fillOval((int) ((x - range.x) * scaleFactor), (int) ((y - range.y) * scaleFactor),
							(int) ((width) * scaleFactor), (int) ((height) * scaleFactor));
				break;
			default:
				break;
			}
			g.setColor(backup);
		}
		// DESSIN DU BORD
		int padHookScaled     = Math.max((int) (padhook * scaleFactor), padhook);
		int padHalfHookScaled = padHookScaled/2;
		switch (owner.getRule().getShape()) {
		case CIRCLE:
			g.drawOval((int) ((x - range.x) * scaleFactor), (int) ((y - range.y) * scaleFactor),
					(int) ((width) * scaleFactor), (int) ((height) * scaleFactor));
			if (node.getKind() == JMENodeKind.HOOK) {
				g.drawOval((int) ((x - range.x) * scaleFactor) - padHalfHookScaled,
						(int) ((y - range.y) * scaleFactor) - padHalfHookScaled,
						(int) (width * scaleFactor + padHookScaled), (int) (height * scaleFactor) + padHookScaled);
			}
			break;
		case RECTANGLE:
			g.drawRect((int) ((x - range.x) * scaleFactor), (int) ((y - range.y) * scaleFactor),
					(int) ((width) * scaleFactor), (int) ((height) * scaleFactor));
			if (node.getKind() == JMENodeKind.HOOK) {
				g.drawRect((int) ((x - range.x) * scaleFactor) - padHalfHookScaled,
						(int) ((y - range.y) * scaleFactor) - padHalfHookScaled,
						(int) (width * scaleFactor) + padHookScaled, (int) (height * scaleFactor) + padHookScaled);
			}
			break;
		case ROUNDRECTANGLE: {
			int arc = 3;
			if (pref != null)
				arc = pref.getArcRoundRectangle();
			g.drawRoundRect((int) ((x - range.x) * scaleFactor), (int) ((y - range.y) * scaleFactor), (int) ((width) * scaleFactor), (int) ((height) * scaleFactor), (int) ((arc) * scaleFactor), (int) ((arc) * scaleFactor));
			if (node.getKind() == JMENodeKind.HOOK) {
				g.drawRoundRect((int) ((x - range.x) * scaleFactor - padHalfHookScaled),
						(int) ((y - range.y) * scaleFactor - padHalfHookScaled),
						(int) ((width) * scaleFactor) + padHookScaled, (int) ((height) * scaleFactor) + padHookScaled,
						(int) ((arc) * scaleFactor), (int) ((arc) * scaleFactor));
			}
			break;
		}
		case ELLIPSE:
			g.drawOval((int) ((x - range.x) * scaleFactor), (int) ((y - range.y) * scaleFactor),
					(int) ((width) * scaleFactor), (int) ((height) * scaleFactor));
			if (node.getKind() == JMENodeKind.HOOK) {
				g.drawOval((int) ((x - range.x) * scaleFactor) - padHalfHookScaled,
						(int) ((y - range.y) * scaleFactor) - padHalfHookScaled,
						(int) ((width) * scaleFactor) + padHookScaled, (int) ((height) * scaleFactor) + padHookScaled);
			}
			break;
		default:
			break;
		}

		// DESSIN DES TEXTES (plongements etc...)
		for (Triplet<Rectangle, String, Color> pair : contents) {
			Rectangle rect = pair.l();
			String msg = pair.m();
			int lwidth = metrics.stringWidth(msg) + 1;
			if(msg != null) {
				Color c = pair.r();
				if(c == null)
					g.setColor(backup);
				else
					g.setColor(c);
				g.drawString(msg, (int) ((x + (width / 2) - range.x) * scaleFactor) - (lwidth / 2),
						(int) ((rect.y + rect.height - descent - range.y) * scaleFactor));
			}
		}

		// DESSIN DU NOM SUR LE BORD attention le background
		g.setColor(backup);
		int lwidth = metrics.stringWidth(node.getName()) + 1;
		int lheight = metrics.getHeight();
		int lx = (int) ((-range.x + x + (width / 2)) * scaleFactor) - (lwidth / 2);
		int ly = (int) ((-range.y + y + height + descent) * scaleFactor);
		g.clearRect(lx, ly - lheight, lwidth, lheight);
		if (pref != null && pref.getIsFillNode()) {
			g.setColor(node.getColor());
			g.fillRect(lx, ly - lheight, lwidth, lheight);
			g.setColor(backup);
		}

		g.drawString(node.getName(), lx + 1, ly);

		if (errorsArea != null) {
			int cx = errorsArea.x + errorsArea.width/2;
			int cy = errorsArea.y + errorsArea.height/2 - (lheight/2);
			drawNodeInError(g, backup, cx - range.x, cy - range.y, scaleFactor);
		}
	}


	public void drawNodeInError(Graphics2D g, Color def, int cxx, int cyy, double scaleFactor) {
		try {
			int cx = (int) (cxx * scaleFactor);
			int cy = (int) (cyy * scaleFactor);
			Color errColor = Color.yellow;
			if (node.getTopoErrors() > 0 && node.getEbdsErrors() > 0) {
				// couleur = "Red";
				errColor = Color.RED;
			}
			else if (node.getTopoErrors() > 0) {
				errColor  = Color.yellow;
			}
			else {
				errColor = Color.green;
			}

			FontMetrics metrics = g.getFontMetrics();
			int fontheight = metrics.getHeight();
			// int descent = metrics.getDescent();
			int lheight = (int) (1.5 * fontheight * scaleFactor); // largeur du carre aussi
			final int lwidth = (int) (metrics.stringWidth("!") * scaleFactor);

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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.up.xlim.sic.ig.jerboa.jme.util.JMEDrawable#update(java.awt.Graphics2D)
	 */
	@Override
	public void update(Graphics2D g, Rectangle range, boolean highlight) {
		FontMetrics metrics = g.getFontMetrics();
		int width = 0;
		int height = 0;
		ArrayList<Triplet<Integer, String, Color>> lines = new ArrayList<>();

		int fontheight = metrics.getHeight();
		// Preparation des erreurs
		if (((node.getEbdsErrors() > 0) || (node.getTopoErrors() > 0)) && getPreferences().getShowErrors()) {
			int dl = (int) (1.5 * fontheight);
			height += dl + dl;
			lines.add(new Triplet<Integer, String, Color>(dl, null, null));
			lines.add(new Triplet<Integer, String, Color>(dl, null, null));
		} else {
			errorsArea = null;
		}

		if(owner.getRule() instanceof JMEScript) {
			// Preparation de la multiplicite
			String smulti = node.getMultiplicity().toString();
			int lmulti = metrics.stringWidth(smulti);
			width = Math.max(width, lmulti);
			lines.add(new Triplet<Integer, String, Color>(lmulti, smulti, null));
			height += fontheight;
		}


		// Preparation de l'orbit
		String sorbit = owner.genOrbitStr(node);
		int l = metrics.stringWidth(sorbit);
		width = Math.max(width, l);
		lines.add(new Triplet<Integer, String, Color>(l, sorbit, null));
		height += fontheight;


		if(node.isRightNode()) {
			// Preparation des expressions explicits
			List<JMENodeExpression> explicits = node.getExplicitExprs();
			for (JMENodeExpression ne : explicits) {
				JMEEmbeddingInfo info = ne.getEbdInfo();
				if(info.isVisible()) {
					String name = info.getName();
					int w = metrics.stringWidth(name);
					Color c = info.getColor();
					if(highlight)
						c = null;
					lines.add(new Triplet<Integer, String, Color>(w, name, c));
					width = Math.max(w, width);
					height += fontheight;
				}
			}

			// Preparation des expressions implicits
			List<JMENodeExpression> implicits = node.getImplicitExprs();
			for (JMENodeExpression ne : implicits) {
				JMEEmbeddingInfo info = ne.getEbdInfo();
				if(info.isVisible()) {
					String name = info.getName();
					int w = metrics.stringWidth(name);
					Color c = info.getColor();
					if(highlight)
						c = null;
					else if(c != null) {
						Color bis = c.brighter();
						if(bis.equals(c))
							c = c.darker();
						else
							c = bis;
					}
					else
						c = getPreferences() != null ? getPreferences().getColorImplicitExpr() : Color.GRAY;
						lines.add(new Triplet<Integer, String, Color>(w, name, c)); // lines.add(new Pair<Integer, String>(w, name));
						width = Math.max(w, width);
						height += fontheight;
				}
			}

			// Preparation des expression required
			List<JMENodeExpression> requireds = node.getRequiredExprs();
			for (JMENodeExpression ne : requireds) {
				JMEEmbeddingInfo info = ne.getEbdInfo();
				if(info.isVisible()) {
					String name = info.getName();
					if(info.getDefaultCode().isEmpty())
						name = "!"+name+"!";
					else
						name = "("+name+")";
					int w = metrics.stringWidth(name);
					Color c = info.getColor();
					if(highlight)
						c = null;
					else if(c != null) {
						Color bis = c.brighter();
						if(bis.equals(c))
							c = c.darker();
						else
							c = bis;
					}
					else
						c = getPreferences().getColorImplicitExpr();
					lines.add(new Triplet<Integer, String, Color>(w, name, c)); // lines.add(new Pair<Integer, String>(w, name));
					width = Math.max(w, width);
					height += fontheight;
				}
			}
		} // end if right node

		if(node.isLeftNode()) {
			if(node.getPrecondition() != null && !node.getPrecondition().isEmpty()) {
				String m = "pre";
				int w = metrics.stringWidth(m);
				lines.add(new Triplet<Integer, String, Color>(w, m, null));
			}
		}

		// Calcul de la dimension du noeud affichable

		// preparation
		height = height + 5;
		int padding = 5;
		if (owner != null && owner.getPreferences() != null)
			padding = getPreferences().getPaddingNodeInfo();

		int textheight = height; // version pour ne pas fausser la position dans
		// le cadre.

		//		width = Math.max(width, (int) (50 * scale));
		//		height = Math.max(height, (int) (35 * scale));
		width = Math.max(width, 50);
		height = Math.max(height, 35);

		final int nodeX = node.getX();
		final int nodeY = node.getY();

		switch (owner.getRule().getShape()) {
		case CIRCLE: {
			int w = (width + padding) / 2;
			int h = (height + padding) / 2;
			int hypo = (int) Math.sqrt(w * w + h * h);
			x = nodeX - hypo;
			y = nodeY - hypo;
			this.width = hypo * 2;
			this.height = hypo * 2;
			break;
		}
		case RECTANGLE: {
			this.width = width + padding;
			this.height = height + padding; // TODO? + fontheight;
			x = nodeX - (this.width / 2);
			y = nodeY - (this.height / 2);
			break;
		}
		case ROUNDRECTANGLE: {
			int arc = 3;
			if (owner != null && owner.getPreferences() != null)
				arc = getPreferences().getArcRoundRectangle();
			this.width = width + arc;
			this.height = height + arc; // TODO? + fontheight;
			x = nodeX - (this.width / 2);
			y = nodeY - (this.height / 2);
			break;
		}
		case ELLIPSE: {
			x = nodeX - ((width + padding) / 2);
			y = nodeY - ((height + padding) / 2);
			this.width = width + padding;
			this.height = height + padding; // TODO? + fontheight;
			break;
		}
		default:
			x = nodeX - (width / 2);
			y = nodeY - (height / 2);
		}

		this.contents.clear();
		for (int i = 0; i < lines.size(); i++) {
			Triplet<Integer, String, Color> pair = lines.get(i);
			int top = nodeY - (textheight / 2) + fontheight + (i * fontheight);
			int gauche = nodeX - (pair.l().intValue() / 2);
			Rectangle bbox = new Rectangle(gauche, top - fontheight, pair.l().intValue(), fontheight);
			if (pair.m() == null) {
				// erreur
				errorsArea = bbox;
			} else
				this.contents.add(new Triplet<Rectangle, String, Color>(bbox, pair.m(), pair.r()));
		}


		// update labelArea
		int lwidth = metrics.stringWidth(node.getName()) + 1;
		int lheight = metrics.getHeight();
		int lx = -range.x + x + (width / 2) - (lwidth / 2);
		int ly = -range.y + y + height + metrics.getDescent();
		labelArea = new Rectangle(lx, ly - lheight, lwidth, lheight);
	}


	public boolean insideOrbit(int x, int y) {
		for (Triplet<Rectangle, String, Color> triplet : contents) {
			if(triplet.getLeft().contains(x, y)) {
				String ebdname = triplet.getMiddle();
				if(ebdname.startsWith("<") && ebdname.endsWith(">"))
					return true;
			}
		}
		return false;
	}

	public JMEEmbeddingInfo getEbdAtPos(int x, int y) {
		System.out.println("EBD AT POS: "+x+" "+y);

		for (Triplet<Rectangle, String, Color> triplet : contents) {
			if(triplet.getLeft().contains(x, y)) {
				System.out.println("    EBD FOUND: "+triplet);
				String ebdname = triplet.getMiddle();
				if(ebdname.startsWith("!") && ebdname.endsWith("!"))
					ebdname = ebdname.substring(1, ebdname.length() - 1);
				if(ebdname.startsWith("(") && ebdname.endsWith(")"))
					ebdname = ebdname.substring(1, ebdname.length() - 1);
				return node.getRule().getModeler().search(ebdname);
			}
		}

		return null;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	private JMEPreferences getPreferences() {
		return owner.getPreferences();
	}

	@Override
	public boolean inside(int x, int y) {
		return (this.x <= x && x <= this.x + this.width) && (this.y <= y && y <= this.y + this.height);
	}

	public JMENode getNode() {
		return node;
	}

	/**
	 * Calcule le point dans l'alignement avec les coords (x2,y2).
	 *
	 * @param x2
	 * @param y2
	 * @return
	 */
	public Point coord(int x2, int y2) {

		Point center = new Point(node.getX(), node.getY());
		Point pos = new Point(x2, y2);
		// Point center = new Point(node.getX(), node.getY());
		// Point pos = new Point(x2 - range.x, y2 - range.y);
		final int padhook = (node.getKind() == JMENodeKind.HOOK
				? (getPreferences() != null ? getPreferences().getPadHook() : 0) : 0);
		int width = this.width + (getPreferences() != null ? getPreferences().getPadNodeAlphaWidth() : 5);
		int height = this.height + (getPreferences() != null ? getPreferences().getPadNodeAlphaHeight() : 5);

		switch (owner.getRule().getShape()) {
		case CIRCLE: {
			width +=  padhook;
			height += padhook;
			return JMEMath.intersectionCercleDroiteNearest(center, width / 2, pos, center, pos);
		}
		case ELLIPSE: {
			width +=  padhook;
			height += padhook;
			Point pts = JMEMath.intersectionEllipseDroiteNearest(center, width / 2, height / 2, center, pos, pos);
			return pts;
		}
		case RECTANGLE:
		case ROUNDRECTANGLE: {
			// Point p1 = new Point(x-10 , y-10);
			// Point p3 = new Point(x+width-10, y+height-10);
			int halfpadhook = padhook/2;
			final int halfpadW = ((getPreferences() != null ? getPreferences().getPadNodeAlphaWidth() : 5)) / 2
					+ halfpadhook;
			final int halfpadH = ((getPreferences() != null ? getPreferences().getPadNodeAlphaHeight() : 5)) / 2
					+ halfpadhook;

			Point p1 = new Point(x - halfpadW, y - halfpadH);
			Point p3 = new Point(x + width + halfpadhook, y + height + halfpadhook);

			Point res = JMEMath.intersectionPaveDroiteNearest(p1, p3, center, pos, pos);
			return res;
		}
		}
		throw new RuntimeException("Unsupported SHAPE in objects");
	}

	@Override
	public void move(int dx, int dy) {
		node.setPosition(dx, dy);
	}

	@Override
	public void tryMove(int dx, int dy) {
		deltaX = dx;
		deltaY = dy;
		node.setPosition(lastX + deltaX, lastY + deltaY);
	}

	@Override
	public void tryMove(int dx, int dy, int grainGrid) {
		deltaX = dx;
		deltaY = dy;
		int nx = lastX + deltaX;
		int ny = lastY + deltaY;
		nx = (nx / grainGrid) * grainGrid;
		ny = (ny / grainGrid) * grainGrid;
		node.setPosition(nx, ny);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(node.toString()).append("V(").append(x).append("x").append(y).append(":").append(width).append("x")
		.append(height).append(")");
		return sb.toString();
	}

	@Override
	public int centerX() {
		return node.getX();
	}

	@Override
	public int centerY() {
		return node.getY();
	}

	@Override
	public void endMove(int x, int y) {
		node.endTrans();
	}

	@Override
	public void beginMove(int x, int y) {
		lastX = node.getX();
		lastY = node.getY();
		deltaX = 0;
		deltaY = 0;
		node.beginTrans();
	}

	@Override
	public Rectangle bbox() {
		return new Rectangle(x, y, width, height);
	}

	@Override
	public JMEElement getModel() {
		return node;
	}

	@Override
	public void reload() {
		dirty = true;
		owner.repaint();
	}

	@Override
	public void unlink() {
		node.removeView(this);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ArrayList<Triplet<Rectangle, String, Color>> getContent() {
		return contents;
	}

	public boolean insideLabel(int x, int y) {
		if(labelArea != null) {
			return labelArea.contains(x, y);
		}
		return false;
	}

	@Override
	public JMEElement getSourceElement() {
		return node;
	}

}
