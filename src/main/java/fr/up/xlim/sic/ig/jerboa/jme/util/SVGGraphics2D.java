package fr.up.xlim.sic.ig.jerboa.jme.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import fr.up.xlim.sic.ig.jerboa.jme.view.graph.RuleGraphView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleView;

public class SVGGraphics2D extends Graphics2D {
	
	private StringBuilder sb;
	private Color fg;
	private Color bg;
	private BasicStroke bs;
	
	private Font font;
	private RuleView rv;
	
	private String idpart;
	private int num;
	
	private Rectangle bbox;
	
	public SVGGraphics2D(RuleView ruleview) {
		fg = Color.black;
		bg = Color.white;
		
		sb = new StringBuilder();
		this.font = ruleview.getFont();
		this.rv = ruleview;
		this.bs = (BasicStroke) ruleview.getPreferences().getDefaultBasicStroke();
		
		idpart ="";
		num = 0;
		
		resetBBox();
	}
	
	
	public void resetBBox() {
		bbox = null;
	}


	public StringBuilder getGraph() {
		return sb;
	}
	

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clip(Shape s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Shape s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawString(String s, int x, int y) {
		String str = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(RuleGraphView.ALPHA, "&#945;");
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<text "+locid+" x=\"").append(x).append("\" y=\"").append(y).append("\" fill=\"").append(colorToString(fg)).append("\" >").append(str).append("</text>\n");
		
		// update metrics
		FontMetrics metrics = getFontMetrics();
		int width = metrics.stringWidth(s);
		Rectangle r = new Rectangle(x, y, width, metrics.getHeight());
		// System.out.println("drawString: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void drawString(String s, float x, float y) {
		String str = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(RuleGraphView.ALPHA, "&#945;");
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<text "+locid+" x=\"").append(x).append("\" y=\"").append(y).append("\" fill=\"").append(colorToString(fg)).append("\" >").append(str).append("</text>\n");
		
		// update metrics
		FontMetrics metrics = getFontMetrics();
		int width = metrics.stringWidth(s);
		Rectangle r = new Rectangle((int)x, (int)y, width, metrics.getHeight());
		// System.out.println("drawString2: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fill(Shape s) {
		// TODO Auto-generated method stub

	}

	@Override
	public Color getBackground() {
		return bg;
	}

	@Override
	public Composite getComposite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Paint getPaint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRenderingHint(Key hintKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RenderingHints getRenderingHints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stroke getStroke() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AffineTransform getTransform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void rotate(double theta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotate(double theta, double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scale(double sx, double sy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBackground(Color color) {
		this.bg = color;
	}

	@Override
	public void setComposite(Composite comp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPaint(Paint paint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRenderingHint(Key hintKey, Object hintValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStroke(Stroke s) {
		this.bs = (BasicStroke) s;
	}

	@Override
	public void setTransform(AffineTransform Tx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shear(double shx, double shy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void transform(AffineTransform Tx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void translate(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void translate(double tx, double ty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearRect(int x, int y, int w, int h) {
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<rect "+locid+" x=\"").append(x).append("\" y=\"").append(y).append("\" width=\"").append(w).append("\" height=\"").append(h).
		append("\" fill=\""+colorToString(bg)+"\" />\n");
		
		// update metrics
		Rectangle r = new Rectangle(x, y, w,h);
		// System.out.println("clearRect: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void clipRect(int arg0, int arg1, int arg2, int arg3) {
		// System.out.println("unsupported feature: clipRect");
	}

	@Override
	public void copyArea(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	@Override
	public Graphics create() {
		return new SVGGraphics2D(rv);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawArc(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// System.out.println("unsupported feature: drawArc");
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, ImageObserver arg3) {
		// System.out.println("unsupported feature: drawImage");
		return false;
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, Color arg3, ImageObserver arg4) {
		// System.out.println("unsupported feature: drawImage0");
		return false;
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, ImageObserver arg5) {
		// System.out.println("unsupported feature: drawImage1");
		return false;
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, Color arg5, ImageObserver arg6) {
		// System.out.println("unsupported feature: drawImage2");
		return false;
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8,
			ImageObserver arg9) {
		// System.out.println("unsupported feature: drawImage3");
		return false;
	}

	@Override
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8,
			Color arg9, ImageObserver arg10) {

		// System.out.println("unsupported feature: drawImage4");
		return false;
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		String stroke = strokeToString(bs);
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<line "+locid+" x1=\"").append(x1).append("\" y1=\"").append(y1).append("\" x2=\"").append(x2).append("\" y2=\"").append(y2).append("\" "+stroke+"/>\n");
		
		// update metrics
		int xmin = Math.min(x1, x2);
		int xmax = Math.max(x1, x2);
		int ymin = Math.min(y1, y2);
		int ymax = Math.max(y1, y2);
		Rectangle r = new Rectangle(xmin, ymin, xmax - xmin, ymax - ymin);
		// System.out.println("drawLine: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void drawOval(int x, int y, int w, int h) {
		String stroke = strokeToString(bs);
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		if(w != h) {
			sb.append("<ellipse "+locid+" cx=\"").append(x+w/2).append("\" cy=\"").append(y+h/2).append("\" rx=\"").append(w/2).append("\" ry=\"").append(h/2).append("\" fill=\"none\" "+stroke+" />\n");
		}
		else {
			sb.append("<circle "+locid+" cx=\"").append(x+w/2).append("\" cy=\"").append(y+w/2).append("\" r=\"").append(w/2).append("\" fill=\"none\" "+stroke+" />\n");
		}
		
		// update metrics
		Rectangle r = new Rectangle(x, y, w,h);
		// System.out.println("drawOval: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void drawPolygon(int[] xs, int[] ys, int n) {
		String stroke = strokeToString(bs);
		String locid = "";
		int xmin = Integer.MAX_VALUE, xmax = Integer.MIN_VALUE, ymin = Integer.MAX_VALUE, ymax = Integer.MIN_VALUE;
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<polygon "+locid+" points=\"");
		for(int i = 0;i < n; i++) {
			sb.append(xs[i]).append(",").append(ys[i]).append(" ");
			xmin = Math.min(xs[i], xmin);
			xmax = Math.max(xs[i], xmax);
			ymin = Math.min(ys[i], ymin);
			ymax = Math.max(ys[i], ymax);
		}
		sb.append("\" fill=\"none\" "+stroke+" />\n");
		
		// update metrics
		Rectangle r = new Rectangle(xmin, ymin, xmax-xmin, ymax-ymin);
		// System.out.println("drawPolygon: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void drawPolyline(int[] xs, int[] ys, int n) {
		String stroke = strokeToString(bs);
		String locid = "";
		int xmin = Integer.MAX_VALUE, xmax = Integer.MIN_VALUE, ymin = Integer.MAX_VALUE, ymax = Integer.MIN_VALUE;
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<path "+locid+" d=\"");
		for(int i = 0;i < n; i++) {
			if(i == 0)
				sb.append("M");
			else
				sb.append("L");
			sb.append(xs[i]).append(" ").append(ys[i]).append(" ");
			xmin = Math.min(xs[i], xmin);
			xmax = Math.max(xs[i], xmax);
			ymin = Math.min(ys[i], ymin);
			ymax = Math.max(ys[i], ymax);
		}
		sb.append("\" fill=\"none\" "+stroke+" />\n");
		
		// update metrics
		Rectangle r = new Rectangle(xmin, ymin, xmax-xmin, ymax-ymin);
		// System.out.println("drawPolyline: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void drawRoundRect(int x, int y, int w, int h, int arcW, int arcH) {
		String stroke = strokeToString(bs);
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		if(arcW == arcH && arcW == 0) {
			sb.append("<rect "+locid+" x=\"").append(x).append("\" y=\"").append(y).append("\" width=\"").append(w).append("\" height=\"").append(h).
				append("\" fill=\"none\" "+stroke+"  />\n");
		}
		else {
			sb.append("<rect "+locid+" x=\"").append(x).append("\" y=\"").append(y).append("\" width=\"").append(w).append("\" height=\"").append(h).
				append("\" rx=\"").append(arcW).append("\" ry=\"").append(arcH).append("\" fill=\"none\" "+stroke+" />\n");	
		}
		
		// update metrics
		Rectangle r = new Rectangle(x, y, w,h);
		// System.out.println("drawRoundRect: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void fillArc(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {

		// System.out.println("unsupported feature: fillArc");
	}

	@Override
	public void fillOval(int x, int y, int w, int h) {
		String stroke = strokeToString(bs);
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		if(w != h) {
			sb.append("<ellipse "+locid+" cx=\"").append(x+w/2).append("\" cy=\"").append(y+h/2).append("\" rx=\"").append(w/2).append("\" ry=\"").append(h/2).append("\" ")
				.append("fill=\"").append(colorToString(fg)).append("\" ").append(stroke+" />\n");
		}
		else {
			sb.append("<circle "+locid+" cx=\"").append(x+w/2).append("\" cy=\"").append(y+h/2).append("\" r=\"").append(w/2).append("\"  ")
				.append("fill=\"").append(colorToString(fg)).append("\" ").append(stroke+" />\n");
		}
		
		// update metrics
		Rectangle r = new Rectangle(x, y, w,h);
		// System.out.println("fillOval: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void fillPolygon(int[] xs, int[] ys, int n) {
		String stroke = strokeToString(bs);
		String locid = "";
		int xmin = Integer.MAX_VALUE, xmax = Integer.MIN_VALUE, ymin = Integer.MAX_VALUE, ymax = Integer.MIN_VALUE;
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<polygon "+locid+" points=\"");
		for(int i = 0;i < n; i++) {
			sb.append(xs[i]).append(",").append(ys[i]).append(" ");
			xmin = Math.min(xs[i], xmin);
			xmax = Math.max(xs[i], xmax);
			ymin = Math.min(ys[i], ymin);
			ymax = Math.max(ys[i], ymax);
		}
		sb.append("\" "+stroke+" fill=\""+colorToString(fg)+"\" />\n");
		
		// update metrics
		Rectangle r = new Rectangle(xmin, ymin, xmax-xmin, ymax-ymin);
		// System.out.println("fillPolygon: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void fillRect(int x, int y, int w, int h) {
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<rect "+locid+" x=\"").append(x).append("\" y=\"").append(y).append("\" width=\"").append(w).append("\" height=\"").append(h).append("\" ")
			.append(strokeToString(bs)).append(" fill=\""+colorToString(fg)+"\" ").append("/>\n");
		// update metrics
		Rectangle r = new Rectangle(x, y, w,h);
		// System.out.println("fillRect: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public void fillRoundRect(int x, int y, int w, int h, int arcW, int arcH) {
		String locid = "";
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<rect "+locid+" x=\"").append(x).append("\" y=\"").append(y).append("\" width=\"").append(w).append("\" height=\"").append(h).
			append("\" rx=\"").append(arcW).append("\" ry=\"").append(arcH).append("\" ")
			.append(strokeToString(bs)).append(" fill=\""+colorToString(fg)+"\" ").append("/>\n");
		
		// update metrics
		Rectangle r = new Rectangle(x, y, w,h);
		// System.out.println("fillRoundRect: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}

	@Override
	public Shape getClip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle getClipBounds() {
		return bbox;
	}

	@Override
	public Color getColor() {
		return fg;
	}

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public FontMetrics getFontMetrics(Font arg0) {
		return rv.getFontMetrics(arg0);
	}

	@Override
	public void setClip(Shape arg0) {
		// System.out.println("unsupported feature: setClip2");
	}

	@Override
	public void setClip(int arg0, int arg1, int arg2, int arg3) {
		// System.out.println("unsupported feature: setClip");
	}

	@Override
	public void setColor(Color color) {
		fg = color;
	}

	@Override
	public void setFont(Font font) {
		this.font = font;
	}

	@Override
	public void setPaintMode() {
		// System.out.println("unsupported feature: setPaintMode");
	}

	@Override
	public void setXORMode(Color arg0) {
		// TODO Auto-generated method stub
		// System.out.println("unsupported feature: setXORMode");
	}

	
	public String strokeToString(Stroke c) {
		SVGStroke ss = new SVGStroke((BasicStroke) c, fg);
		return ss.toString();
	}
	
	public static String colorToString(Color c) {
		return "rgb("+c.getRed()+","+c.getGreen()+","+c.getBlue()+")";
	}


	public String getIdpart() {
		return idpart;
	}


	public void setIdpart(String idpart) {
		this.idpart = idpart;
	}

	public Rectangle getBBox() {
		return bbox;
	}


	public void drawBezierDegree5(Point point, Point point2, Point point3, Point point4, Point point5, Point point6) {
		String stroke = strokeToString(bs);
		String locid = "";
		int xmin = Integer.MAX_VALUE, xmax = Integer.MIN_VALUE, ymin = Integer.MAX_VALUE, ymax = Integer.MIN_VALUE;
		if(idpart != null || !idpart.isEmpty())
			locid = "id=\""+idpart+(num++)+"\"";
		sb.append("<path "+locid+" d=\"M");
		sb.append(point.x).append(" ").append(point.y);
		sb.append(" L ").append(point2.x).append(" ").append(point2.y).append(" C ")
			.append(point3.x).append(" ").append(point3.y).append(", ")
			.append(point4.x).append(" ").append(point4.y).append(" ");
		sb.append(" , ").append(point5.x).append(" ").append(point5.y).append(" ");
		sb.append(" L ").append(point6.x).append(" ").append(point6.y).append(" ");
		
		
		xmin = Math.min(point.x, xmin);
		xmax = Math.max(point.x, xmax);
		ymin = Math.min(point.y, ymin);
		ymax = Math.max(point.y, ymax);

		xmin = Math.min(point2.x, xmin);
		xmax = Math.max(point2.x, xmax);
		ymin = Math.min(point2.y, ymin);
		ymax = Math.max(point2.y, ymax);
		
		xmin = Math.min(point3.x, xmin);
		xmax = Math.max(point3.x, xmax);
		ymin = Math.min(point3.y, ymin);
		ymax = Math.max(point3.y, ymax);
		
		xmin = Math.min(point4.x, xmin);
		xmax = Math.max(point4.x, xmax);
		ymin = Math.min(point4.y, ymin);
		ymax = Math.max(point4.y, ymax);
		
		xmin = Math.min(point5.x, xmin);
		xmax = Math.max(point5.x, xmax);
		ymin = Math.min(point5.y, ymin);
		ymax = Math.max(point5.y, ymax);
		
		xmin = Math.min(point6.x, xmin);
		xmax = Math.max(point6.x, xmax);
		ymin = Math.min(point6.y, ymin);
		ymax = Math.max(point6.y, ymax);
		
		sb.append("\" fill=\"none\" "+stroke+" />\n");
		
		// update metrics
		Rectangle r = new Rectangle(xmin, ymin, xmax - xmin, ymax - ymin);
		// System.out.println("drawBezier: "+r);
		if(bbox == null)
			bbox = r;
		else
			bbox = r.union(bbox);
	}
	
}
