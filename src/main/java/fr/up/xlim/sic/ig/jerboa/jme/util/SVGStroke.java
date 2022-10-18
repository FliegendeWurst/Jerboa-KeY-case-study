package fr.up.xlim.sic.ig.jerboa.jme.util;

import java.awt.BasicStroke;
import java.awt.Color;

public class SVGStroke {
	
	private Color color;
	private int width; // stroke-width
	private int cap; // stroke-linecap   [butt round square]
	private int[] dasharray; // stroke-dasharray 
	
	public SVGStroke(BasicStroke stroke, Color color) {
		super();
		this.color = color;
		width = (int)stroke.getLineWidth();
		cap = stroke.getEndCap();
		float[] fdasharray = stroke.getDashArray();
		if(fdasharray != null) {
			dasharray = new int[fdasharray.length];
			for (int i = 0;i < dasharray.length; i++) {
				dasharray[i] = (int)fdasharray[i];
			}
		}
	}
	
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("stroke=\"").append("rgb(").append(color.getRed()).append(",").append(color.getGreen()).append(",").append(color.getBlue()).append(")\" ");
		sb.append("stroke-width=\"").append(width).append("\" ");
		switch(cap) {
		case BasicStroke.CAP_BUTT: sb.append("stroke-linecap=\"").append("butt").append("\" "); break;
		case BasicStroke.CAP_ROUND: sb.append("stroke-linecap=\"").append("round").append("\" "); break;
		case BasicStroke.CAP_SQUARE: sb.append("stroke-linecap=\"").append("square").append("\" "); break;
		}
		if(dasharray != null) {
			sb.append("stroke-dasharray=\"");
			for(int i = 0; i < dasharray.length; i++) {
				sb.append(dasharray[i]);
				if(i != dasharray.length - 1) {
					sb.append(",");
				}
			}
			sb.append("\" ");
		}
		return sb.toString();
	}
}
