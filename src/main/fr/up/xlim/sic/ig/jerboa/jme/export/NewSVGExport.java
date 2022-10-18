package fr.up.xlim.sic.ig.jerboa.jme.export;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.util.SVGGraphics2D;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.JMEArcView;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.JMENodeView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleView;

public class NewSVGExport {

	private RuleView ruleview;
	private StringBuilder leftgraph;
	private StringBuilder rightgraph;

	private Font font;
	private Rectangle leftRange;
	private Rectangle rightRange;
	private Point centerLineLeftGraph;
	private Point centerLineRightGraph;

	private int arrowWidth;
	private int arrowHeight;

	private int padLeft;
	private int padRight;

	private static String NEWLINE = "\n";

	public NewSVGExport(RuleView ruleview) {
		this.ruleview = ruleview;
		leftgraph = new StringBuilder();
		rightgraph = new StringBuilder();
		font = ruleview.getFont();
		arrowWidth = 50;
		arrowHeight = 25;

		padLeft = 5;
		padRight = 5;
	}

	public String generate() {
		Point ori = new Point(0, 0);
		// ON DETERMINE LA TAILLE DES GRAPHES INDEPENDEMMENT DE LEUR POSITION
		prepareLeftGraph(ori);
		prepareRightGraph(ori);

		if(leftRange == null)
			leftRange = new Rectangle();
		if(rightRange == null)
			rightRange = new Rectangle();

		// ON EN DEDUIT LE PARAMETRAGE D'AFFICHAGE
		int width = padLeft + leftRange.width + padRight + arrowWidth + padLeft + rightRange.width + padRight;
		int height = Math.max(leftRange.height, Math.max(rightRange.height, arrowHeight));

		int dcYRightGraph = Math.abs(centerLineRightGraph.y - rightRange.y);
		int dcYLeftGraph = Math.abs(centerLineLeftGraph.y - leftRange.y);

		int topLeftGraph,topRightGraph, topArrow;
		if(dcYRightGraph > dcYLeftGraph) {
			topLeftGraph = -leftRange.y + dcYRightGraph - (centerLineLeftGraph.y - leftRange.y); //(height - leftRange.height) / 2;
			topRightGraph = -rightRange.y; // (height - rightRange.height) / 2;
			topArrow = dcYRightGraph - (arrowHeight/2); // (height - arrowHeight) / 2;
		}
		else {
			topLeftGraph = -leftRange.y; //(height - leftRange.height) / 2;
			topRightGraph = -rightRange.y + dcYLeftGraph - (centerLineRightGraph.y - rightRange.y); // (height - rightRange.height) / 2;
			topArrow = dcYLeftGraph - (arrowHeight/2); // (height - arrowHeight) / 2;
		}

		int leftLeftGraph = -leftRange.x; // padLeft - leftRange.x;
		int leftArrow = padLeft + leftRange.width + padRight;
		int leftRightGraph = -rightRange.x + leftArrow + arrowWidth + padLeft; // padLeft + leftRange.width + arrowWidth - rightRange.x;

		// MAINTENANT ON DESSINE TOUT A SA PLACE
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" standalone=\"no\"?> \n");
		sb.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \n");
		sb.append("\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"> \n");
		sb.append("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" font=\"").append(font.getFontName()).append("\" font-size=\"").append(font.getSize()).append("\" ");
		sb.append("width=\"").append(width).append("\" height=\"").append(height).append("\" ");
		sb.append("> \n");

		prepareLeftGraph(new Point(leftLeftGraph, topLeftGraph));
		sb.append("<g>").append(NEWLINE);
		sb.append(leftgraph.toString());
		sb.append("</g>").append(NEWLINE);
		System.out.println(leftRange);

		sb.append("<g>").append(NEWLINE);
		sb.append(prepareArrow(leftArrow, topArrow, arrowWidth, arrowHeight, Color.BLACK));
		sb.append("</g>").append(NEWLINE);

		prepareRightGraph(new Point(leftRightGraph, topRightGraph));
		sb.append("<g>").append(NEWLINE);
		sb.append(rightgraph.toString());
		sb.append("</g>").append(NEWLINE);
		System.out.println(rightRange);

		sb.append("</svg>");
		return sb.toString();
	}

	private boolean prepareLeftGraph(Point leftTop) {
		boolean res = true;
		HashMap<JMEArc, JMEArcView> mapArcs = ruleview.getLeft().getArcs();
		HashMap<JMENode, JMENodeView> mapNodes = ruleview.getLeft().getNodes();
		leftRange = new Rectangle(-leftTop.x, -leftTop.y,0, 10);

		SVGGraphics2D g = new SVGGraphics2D(ruleview);
		int cx = Integer.MAX_VALUE;
		int cy = Integer.MAX_VALUE;

		for (Entry<JMENode, JMENodeView> o : mapNodes.entrySet()) {
			g.setIdpart("LG"+o.getKey().getName()+"_");
			JMENodeView view = o.getValue();
			view.draw(g, leftRange, false, 1);

			int tmp = Math.min(cx, view.getNode().getX());
			if(cx ==  view.getNode().getX()) {
				cx = tmp;
				cy = Math.min(cy, view.getNode().getY());
			}
			else if(tmp == view.getNode().getX()) {
				cx = tmp;
				cy = view.getNode().getY();
			}
		}

		centerLineLeftGraph = new Point(cx == Integer.MAX_VALUE? 0 : cx, cy == Integer.MAX_VALUE? 0 : cy);

		for (Entry<JMEArc, JMEArcView> o : mapArcs.entrySet()) {
			g.setIdpart("LG"+o.getKey().getSource().getName()+"_"+o.getKey().getDimension()+"_"+o.getKey().getDestination().getName()+"_");
			JMEArcView view = o.getValue();
			view.draw(g, leftRange, false, 1);
		}
		leftgraph = g.getGraph();
		leftRange = g.getBBox();
		System.out.println("LG BBOX: "+leftRange);
		return res;
	}

	private boolean prepareRightGraph(Point leftTop) {
		boolean res = true;
		HashMap<JMEArc, JMEArcView> mapArcs = ruleview.getRight().getArcs();
		HashMap<JMENode, JMENodeView> mapNodes = ruleview.getRight().getNodes();
		rightRange = new Rectangle(-leftTop.x, -leftTop.y,0,  10);

		SVGGraphics2D g = new SVGGraphics2D(ruleview);
		int cx = Integer.MAX_VALUE;
		int cy = Integer.MAX_VALUE;

		for (Entry<JMENode, JMENodeView> o : mapNodes.entrySet()) {
			g.setIdpart("RG"+o.getKey().getName()+"_");
			JMENodeView view = o.getValue();
			view.draw(g, rightRange, false, 1);

			int tmp = Math.min(cx, view.getNode().getX());
			if(cx ==  view.getNode().getX()) {
				cx = tmp;
				cy = Math.min(cy, view.getNode().getY());
			}
			else if(tmp == view.getNode().getX()) {
				cx = tmp;
				cy = view.getNode().getY();
			}
		}

		centerLineRightGraph = new Point(cx == Integer.MAX_VALUE? 0 : cx, cy == Integer.MAX_VALUE? 0 : cy);

		for (Entry<JMEArc, JMEArcView> o : mapArcs.entrySet()) {
			g.setIdpart("RG"+o.getKey().getSource().getName()+"_"+o.getKey().getDimension()+"_"+o.getKey().getDestination().getName()+"_");
			JMEArcView view = o.getValue();
			view.draw(g, rightRange, false, 1);
		}
		rightgraph = g.getGraph();
		rightRange = g.getBBox();
		System.out.println("RG BBOX: "+rightRange);
		return res;
	}


	/**
	 * @param x
	 *            {@link Integer}
	 * @param y
	 *            {@link Integer}
	 * @param width
	 *            {@link Integer} the arrow width
	 * @param color
	 *            {@link String} the arrow color
	 * @return {@link StringBuilder} an arrow draw in SVG expression
	 */
	private StringBuilder prepareArrow(final int x, final int y, final int width, final int height,
			final Color color) {
		SVGGraphics2D g = new SVGGraphics2D(ruleview);
		g.setColor(color);

		int[] xpos = new int[] { x               , x + (5 * width / 8), x + (5 * width / 8), x + width     , x + (5 * width / 8), x + (5 * width / 8), x                };
		int[] ypos = new int[] { y + (height / 4), y + (height / 4)   , y                  , y + (height/2), y + height         , y + (3*height/4)   , y + (3*height/4) };

		g.fillPolygon(new Polygon(xpos, ypos, xpos.length));

		return g.getGraph();
	}
}
