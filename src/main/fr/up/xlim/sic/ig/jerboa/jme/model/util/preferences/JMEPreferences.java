package fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import fr.up.xlim.sic.ig.jerboa.jme.util.RuleGraphViewGrid;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEVerif;
import fr.up.xlim.sic.ig.jerboa.jme.verif.impl.JMEVerifEbdExpression;
import fr.up.xlim.sic.ig.jerboa.jme.verif.impl.JMEVerifModeler;
import fr.up.xlim.sic.ig.jerboa.jme.verif.impl.JMEVerifRuleEbdExpression;
import fr.up.xlim.sic.ig.jerboa.jme.verif.impl.JMEVerifTopoClassic;

public class JMEPreferences extends JMEUIPrefPrototype {
	
	@JMEUIPrefItem(name="Default Font: ")
	protected Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	@JMEUIPrefItem(name="Background color: ", group="Graph - Colors" , subGroup="Colors")
	protected Color bGColor;
	@JMEUIPrefItem(name="Foreground color: ", group="Graph - Colors", subGroup="Colors")
	protected Color fGColor;
	@JMEUIPrefItem(name="Meta color: ", group="Graph - Colors", subGroup="Colors")
	protected Color metaColor;
	@JMEUIPrefItem(name="Selection color: ", group="Graph - Colors", subGroup="Colors")
	protected Color selectColor;
	@JMEUIPrefItem(name="Error color: ", group="Graph - Colors", subGroup="Colors")
	protected Color errBGColor;

	@JMEUIPrefItem(name="Default line style: ", group="Graph - Style", subGroup="Style")
	protected Stroke defaultBasicStroke;
	
	@JMEUIPrefItem(name="Default line style: ", group="Graph - Style", subGroup="Style")
	protected Stroke strokeDefaultDim;
	@JMEUIPrefItem(name="\u03B10 line style: ", group="Graph - Style", subGroup="Style")
	protected Stroke strokeDim0;
	
	@JMEUIPrefItem(name="\u03B11 line style: ", group="Graph - Style", subGroup="Style")
	protected Stroke strokeDim1;
	@JMEUIPrefItem(name="\u03B12 line style: ", group="Graph - Style", subGroup="Style")
	protected Stroke strokeDim2;
	@JMEUIPrefItem(name="\u03B12 line style (old): ", group="Graph - Style", subGroup="Style")
	protected Stroke strokeDim2OLD;
	@JMEUIPrefItem(name="\u03B13 line style: ", group="Graph - Style", subGroup="Style")
	protected Stroke strokeDim3;
	
	
	@JMEUIPrefItem(name="Default ebd color: ", group="Graph - Colors", subGroup="Colors")
	protected Color defaultEbdColor;
	
	@JMEUIPrefItem(name="Default explicit embedding expression color: ", group="Graph - Colors", subGroup="Colors")
	protected Color colorExplicitExpr;
	@JMEUIPrefItem(name="Default implicit embedding expression color: ", group="Graph - Colors", subGroup="Colors")
	protected Color colorImplicitExpr;
	
	@JMEUIPrefItem(name="Display alpha character? ", group="Graph", subGroup="Display")
	protected boolean showAlpha;
	@JMEUIPrefItem(name="Arc round rectangle (px): ", group="Graph", subGroup="Display")
	protected int arcRoundRectangle;
	@JMEUIPrefItem(name="Padding size (px): ", group="Graph", subGroup="Display")
	protected int paddingNodeInfo;
	@JMEUIPrefItem(name="Display dimension? ", group="Graph", subGroup="Display")
	protected boolean showDim;
	@JMEUIPrefItem(name="Grid color: ", group="Graph - Colors", subGroup="Colors")
	protected Color gridColor;
	@JMEUIPrefItem(name="Color graph selection: ", group="Graph - Colors", subGroup="Colors")
	protected Color selectGraphColor;
	@JMEUIPrefItem(name="Color graph deselection: ", group="Graph - Colors", subGroup="Colors")
	protected Color deselectGraphColor;
	@JMEUIPrefItem(name="Padding  node <-> alpha link width(px): ", group="Graph", subGroup="Display")
	protected int padNodeAlphaWidth;
	@JMEUIPrefItem(name="Padding node <-> alpha link height (px): ", group="Graph", subGroup="Display")
	protected int padNodeAlphaHeight;
	@JMEUIPrefItem(name="Separation hook double line (px): ", group="Graph", subGroup="Display")
	protected int padHook;
	@JMEUIPrefItem(name="Flatten category rule? ", group="UI", subGroup="Misc.")
	protected boolean showRuleFlatten;
	

	@JMEUIPrefItem(name="Fill color nodes? ", group="Graph", subGroup="Display")
	private boolean isFillNode;
	
	/*
	 * delay in ms
	 */
	@JMEUIPrefItem(name="Delay double-click (ms): ", group="UI", subGroup="Misc.", max=Integer.MAX_VALUE)
	protected long delayClick;
	@JMEUIPrefItem(name="Threshold distance click (px): ", group="UI", subGroup="Misc.")
	protected double thresholdClick;

	@JMEUIPrefItem(name="Last saved modeler path: ", group="UI", subGroup="Misc.")
	protected String lastModelerSavedPath;
	
	static Pattern classPattern = Pattern.compile("[a-zA-Z]+[a-zA-Z\\_0-9]*");
	protected JMEVerif jmeverif;
	
	
	protected DefaultComboBoxModel<String> moduleNames;
	@JMEUIPrefItem(name="Maximize window initially? ", group="UI", subGroup="Misc.")
	protected boolean maximizeWindow;
	
	
	@JMEUIPrefItem(name="Display error? ", group="Graph", subGroup="Display")
	protected boolean showErrors;
	
	
	@JMEUIPrefItem(name="Old loop export in SVG? ", group="Export", subGroup="Parameters")
	private boolean oldLoopExportSVG;
	@JMEUIPrefItem(name="Old alpha2 line style (double-line)? ",group="Graph", subGroup="Display")
	private boolean oldDoubleLineA2;
	

	public void setOldLoopExportSVG(boolean oldLoopExportSVG) {
		this.oldLoopExportSVG = oldLoopExportSVG;
	}

	public JMEPreferences() {
		bGColor = Color.white;
		fGColor = Color.black;
		selectColor = Color.magenta;
		metaColor = Color.green;
		gridColor = Color.lightGray;
		selectGraphColor = new Color(240, 240, 255);
		deselectGraphColor = Color.white;
		errBGColor = Color.pink;
		defaultEbdColor = Color.black;
		
		delayClick= 3000;
		thresholdClick = 8;

		lastModelerSavedPath = "";
		maximizeWindow = false;

		showErrors = true;
		showAlpha = true;
		showDim = true;
		arcRoundRectangle = 16;
		paddingNodeInfo = 4;
		padHook = 4;
		oldLoopExportSVG = false;

		defaultBasicStroke = new BasicStroke(1.0f);
		strokeDim0 = new BasicStroke(2.0f);
		strokeDim2 = new BasicStroke(1.0f);
		strokeDim2OLD = new BasicStroke(1.0f);
		float[] dash = { 5.0f };
		strokeDim1 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);

		dash = new float[] { 2.0f };
		strokeDim3 = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		dash = new float[] { 10.0f };
		strokeDefaultDim = new BasicStroke(4.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		colorExplicitExpr = Color.black;
		colorImplicitExpr = Color.LIGHT_GRAY;

		padNodeAlphaHeight = 8;
		padNodeAlphaWidth = 10;

		showRuleFlatten = false;
	}

	public Font getDefaultFont() {
		return defaultFont;
	}

	public Color getBGColor() {
		return bGColor;
	}
	
	public void setBGColor(Color c) {
		this.bGColor = c;
	}
	
	public Color getErrBGColor() {
		return errBGColor;
	}
	public void setErrBGColor(Color c) {
		this.errBGColor = c;
	}

	public Color getFGColor() {
		return fGColor;
	}
	public void setFGColor(Color c) {
		this.fGColor = c;
	}

	public Color getSelectColor() {
		return selectColor;
	}
	public void setSelectColor(Color c) {
		this.selectColor = c;
	}

	public Color getMetaColor() {
		return metaColor;
	}
	public void setMetaColor(Color c) {
		this.metaColor = c;
	}
	
	public String getLastModelerSavedPath() {
		return lastModelerSavedPath;
	}
	public void setLastModelerSavedPath(String s) {
		lastModelerSavedPath = s;
	}

	public File getLastModelerSavedPathFile() {
		if(lastModelerSavedPath != null) {
			File file = new File(lastModelerSavedPath);
			if(file.exists())
				return file;
		}
		return null;
	}

	
	public static String getSuitableName(String s) {
		// cette fonction est mal calibre... c nul!
		if(s == null)
			return null;
		
		Matcher matcher = classPattern.matcher(s.replaceAll("\\s", "_"));
		if (matcher.find()) {
			System.out.println(matcher.group(0));
			return matcher.group(0);
		}
		return null;
	}
	
	public Stroke getStrokeDim0() {
		return strokeDim0;
	}
	
	public Stroke getStrokeDim1() {
		return strokeDim1;
	}
	
	public Stroke getStrokeDim2() {
		return strokeDim2;
	}

	public Stroke getStrokeDim3() {
		return strokeDim3;
	}
	
	public void setStrokeDim0(Stroke s) {
		this.strokeDim0 = s;
	}
	public void setStrokeDim1(Stroke s) {
		this.strokeDim1 = s;
	}
	public void setStrokeDim2(Stroke s) {
		this.strokeDim2 = s;
	}
	public void setStrokeDim3(Stroke s) {
		this.strokeDim3 = s;
	}
	
	public Stroke getStrokeDefaultDim() {
		return strokeDefaultDim;
	}
	public void setStrokeDefaultDim(Stroke s) {
		this.strokeDefaultDim = s;
	}
	
	public Stroke dimStroke(int dim) {
		switch (dim) {
		case 0:
			return strokeDim0;
		case 1:
			return strokeDim1;
		case 2:
			if(oldDoubleLineA2)
				return strokeDim2OLD;
			else
				return strokeDim2;
		case 3:
			return strokeDim3;
		}
		return strokeDefaultDim;
	}

	public Stroke getDefaultBasicStroke() {
		return defaultBasicStroke;
	}

	public Color dimColor(int dim) {
		switch (dim) {
		case 0:
			return Color.black;
		case 1:
			return new Color(200,0,0);
		case 2:
			return new Color(0,0,200);
		case 3:
			return new Color(0,200,0);
		}
		return Color.darkGray;
	}

	public double getThresholdClick() {
		return thresholdClick;
	}

	public void setThresholdClick(double thresholdClick) {
		this.thresholdClick = thresholdClick;
	}

	public long getDelayClick() {
		return delayClick;
	}
	public void setDelayClick(long l) {
		this.delayClick = l;
	}

	public Color getColorExplicitExpr() {
		return colorExplicitExpr;
	}
	public void setColorExplicitExpr(Color c) {
		this.colorExplicitExpr = c;
	}

	public Color getColorImplicitExpr() {
		return colorImplicitExpr;
	}
	public void setColorImplicitExpr(Color c) {
		this.colorImplicitExpr = c;
	}

	public boolean getShowAlpha() {
		return showAlpha;
	}
	public void setShowAlpha(boolean b) {
		this.showAlpha = b;
	}

	public int getArcRoundRectangle() {
		return arcRoundRectangle;
	}
	public void setArcRoundRectangle(int i) {
		this.arcRoundRectangle = i;
	}

	public int getPaddingNodeInfo() {
		return paddingNodeInfo;
	}
	public void setPaddingNodeInfo(int i) {
		this.paddingNodeInfo = i;
	}

	public boolean getShowDim() {
		return showDim;
	}
	
	public void setShowDim(boolean b) {
		this.showDim = b;
	}

	public Color getGridColor() {
		return gridColor; // couleur discrete pour les lignes et autres
		// informations
	}
	public void setGridColor(Color c) {
		this.gridColor = c;
	}

	public int grainGrid(RuleGraphViewGrid grid) {
		switch (grid) {
		case TINY:
			return 10;
		case SMALL:
			return 20;
		case MEDIUM:
			return 30;
		case LARGE:
			return 40;
		case HUGE:
			return 50;
		}
		return 15;
	}

	public Color getSelectGraphColor() {
		return selectGraphColor;
	}
	public void setSelectGraphColor(Color c) {
		this.selectGraphColor = c;
	}

	public Color getDeselectGraphColor() {
		return deselectGraphColor;
	}
	public void setDeselectGraphColor(Color c) {
		this.deselectGraphColor = c;
	}

	// distance entre le noeud et les aretes alpha qui les collent
	public int getPadNodeAlphaWidth() {
		return padNodeAlphaWidth;
	}
	public void setPadNodeAlphaWidth(int i) {
		this.padNodeAlphaWidth = i;
	}

	public int getPadNodeAlphaHeight() {
		return padNodeAlphaHeight;
	}
	public void setPadNodeAlphaHeight(int i ) {
		this.padNodeAlphaHeight = i;
	}
	

	/**
	 * Renvoie le decalage pour le double trait des hooks.
	 *
	 * @return
	 */
	public int getPadHook() {
		return padHook;
	}
	public void setPadHook(int i) {
		this.padHook = i;
	}

	public boolean getShowRuleFlatten() {
		return showRuleFlatten;
	}
	public void setShowRuleFlatten(boolean b) {
		this.showRuleFlatten = b;
	}

	
	
	public ComboBoxModel<String> getModuleNames() {
		return moduleNames;
	}

	public Color getDefaultEbdColor() {
		return defaultEbdColor;
	}
	public void setDefaultEbdColor(Color c) {
		this.defaultEbdColor = c;
	}

	
	public void setMaximizeWindow(boolean f) {
		maximizeWindow = f;
	}
	
	public boolean getMaximizeWindow() {
		return maximizeWindow;
	}
	
	public JMEVerif getVerif() {
		if(jmeverif == null) {
			jmeverif = new JMEVerif();
			jmeverif.addVerif(new JMEVerifTopoClassic());
			jmeverif.addVerif(new JMEVerifRuleEbdExpression());
			jmeverif.addVerif(new JMEVerifModeler());
			jmeverif.addVerif(new JMEVerifEbdExpression());
		}
		return jmeverif;
	}

	public boolean getShowErrors() {
		return showErrors;
	}
	
	public void setShowErrors(boolean se) {
		this.showErrors = se;
	}

	public boolean getIsFillNode() {
		return isFillNode;
	}
	public void setIsFillNode(boolean b) {
		this.isFillNode = b;
	}

	public boolean getOldLoopExportSVG() {
		return oldLoopExportSVG;
	}
	
	public Stroke getStrokeDim2OLD() {
		return strokeDim2OLD;
	}

	public void setStrokeDim2OLD(Stroke strokeDim2OLD) {
		this.strokeDim2OLD = strokeDim2OLD;
	}

	public boolean getOldDoubleLineA2() {
		return oldDoubleLineA2;
	}

	public void setOldDoubleLineA2(boolean oldDoubleLineA2) {
		this.oldDoubleLineA2 = oldDoubleLineA2;
	}
}
