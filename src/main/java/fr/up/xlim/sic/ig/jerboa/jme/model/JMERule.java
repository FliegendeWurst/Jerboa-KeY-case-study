package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMENodeShape;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.util.RuleGraphViewGrid;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorSeverity;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorType;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementWindowableView;

public abstract class JMERule extends JMEElementWindowable implements JMEElement, Cloneable, Comparable<JMERule> {

	protected JMEModeler modeler;
	protected String name;
	protected String comment;
	protected String category;

	protected JMEGraph left;
	protected JMEGraph right;

	protected boolean modified;
	protected String header;
	protected String precondition;
	protected String preprocess;
	protected String postprocess;
	protected Set<JMEElementView> views;
	protected UndoManager manager;

	protected ArrayList<JMEParamEbd> paramsebd;
	protected ArrayList<JMEParamTopo> paramstopo;

	protected JMENodeShape shape;
	protected boolean magnetic;
	protected RuleGraphViewGrid gridsize;
	protected boolean showgrid;
	protected String midprocess;

	protected JMERule(JMEModeler modeler, String name) {
		this.modeler = modeler;
		this.name = name;

		this.comment = "";

		modified = false;
		views = new HashSet<>();
		this.manager = new UndoManager();

		this.shape = JMENodeShape.CIRCLE;
		this.magnetic = false;
		this.gridsize = RuleGraphViewGrid.MEDIUM;
		this.showgrid = false;

		paramsebd = new ArrayList<>();
		paramstopo = new ArrayList<>();

		this.header = "";
		category = "";
		precondition = "";
		preprocess = "";
		postprocess = "";
		midprocess = "";

		isdocked = false;
		isopened = false;
		sizeX = 800;
		sizeY = 600;

		// attention a l'ordre
		left = new JMEGraph(this, true);
		right = new JMEGraph(this, false);
	}

	public String getFullName() {
		String res = "";
		if (category == null || "".equals(category))
			res = getName();
		else
			res = getCategory() + "." + getName();
		return res;
	}

	/**
	 * Cette fonction renvoie true lorsque la regle a ete verifiee avec succes et qu'aucune erreur critique reste sur la regle.
	 * Dans un premier temps, seul les erreurs topologiques sont
	 * @return
	 */
	public boolean check() {
		// return !hasCriticalErrors();
		return !hasTopoErrors();
	}

	@Override
	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public JMEGraph getLeft() {
		return left;
	}

	public JMEGraph getRight() {
		return right;
	}

	public void setModeler(JMEModeler _modeler) {
		modeler = _modeler;
	}

	public void setName(String newname) {
		if (newname != null && !newname.equals(name)) {
			manager.registerUndo(new UndoItemField(this, "name", name, newname, !modified));
			modified = true;
			name = newname;
			update();
		}
	}

	public void setComment(String newcomment) {
		if (newcomment != null && !newcomment.equals(comment)) {
			manager.registerUndo(new UndoItemField(this, "comment", comment, newcomment, !modified));
			modified = true;
			comment = newcomment;
			update();
		}
	}

	public void setPrecondition(String text) {
		if (text != null && !text.equals(precondition)) {
			manager.registerUndo(new UndoItemField(this, "precond", precondition, text, !modified));
			precondition = text;
			modified = true;
			update();
		}
	}

	public String getPrecondition() {
		return precondition;
	}

	public void setHeader(String text) {
		if (text != null && !text.equals(header)) {
			manager.registerUndo(new UndoItemField(this, "header", header, text, !modified));
			header = text;
			modified = true;
			update();
		}
	}

	public String getHeader() {
		return header;
	}

	public void setLeft(JMEGraph g) {
		left = g;
	}

	public void setRight(JMEGraph g) {
		right = g;
	}

	/**
	 * On retourne le premier hook portant le nom explicitÃ© en paramÃ¨tre.
	 * Attention si il existe des doublons.
	 *
	 * @param name
	 * @return
	 */
	public JMENode getHookNode(String name) {
		for (JMENode n : left.getHooks()) {
			if (n.getName().compareTo(name) == 0) {
				return n;
			}
		}
		return null;
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
		ArrayList<JMEElementView> bviews = new ArrayList<>(views);
		for (JMEElementView view : bviews) {
			view.reload();
		}

		left.update();
		right.update();

		for (JMEParamTopo topo : paramstopo) {
			topo.update();
		}

		synchronized (paramsebd) {
			for (JMEParamEbd ebd : paramsebd) {
				ebd.update();
			}
		}
	}

	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch (fitem.field()) {
		case "name":
			name = (String) fitem.value();
			break;
		case "comment":
			comment = (String) fitem.value();
			break;
		case "precond":
			precondition = (String) fitem.value();
			break;
		case "addparamtopo": {
			JMEParamTopo t = (JMEParamTopo) fitem.newValue();
			paramstopo.remove(t);
			break;
		}
		case "addparamebd": {
			JMEParamEbd e = (JMEParamEbd) fitem.newValue();
			paramsebd.remove(e);
			break;
		}
		case "delparamtopo": {
			JMEParamTopo t = (JMEParamTopo) fitem.value();
			paramstopo.add(t);
			break;
		}
		case "delparamebd": {
			JMEParamEbd e = (JMEParamEbd) fitem.value();
			paramsebd.add(e);
			break;
		}
		}
		if (fitem.getModifState())
			modified = false;
		manager.transfertRedo(fitem);
		update();
	}

	@Override
	public void redo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch (fitem.field()) {
		case "name":
			name = (String) fitem.newValue();
			break;
		case "comment":
			comment = (String) fitem.newValue();
			break;
		case "precond":
			precondition = (String) fitem.newValue();
			break;
		case "addparamtopo": {
			JMEParamTopo t = (JMEParamTopo) fitem.newValue();
			paramstopo.add(t);
			break;
		}
		case "addparamebd": {
			JMEParamEbd e = (JMEParamEbd) fitem.newValue();
			paramsebd.add(e);
			break;
		}
		case "delparamtopo": {
			JMEParamTopo t = (JMEParamTopo) fitem.value();
			paramstopo.remove(t);
			break;
		}
		case "delparamebd": {
			JMEParamEbd e = (JMEParamEbd) fitem.value();
			paramsebd.remove(e);
			break;
		}
		}
		if (fitem.getModifState())
			modified = true;
		manager.transfertUndo(fitem);
		update();
	}

	@Override
	public String toString() {
		return getName();
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	public void setPreProcess(String text) {
		if (text != null && !text.equals(preprocess)) {
			preprocess = text;
			modified = true;
			update();
		}
	}

	public String getPreProcess() {
		return preprocess;
	}

	public void setPostProcess(String text) {
		if (text != null && !text.equals(postprocess)) {
			postprocess = text;
			modified = true;
			update();
		}
	}

	public String getPostProcess() {
		return postprocess;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String text) {
		if (text != null && !text.equals(category)) {
			this.category = text;
			modified = true;
			update();
		}
	}

	public List<JMEParamEbd> getParamsEbd() {
		return paramsebd;
	}

	public List<JMEParamTopo> getParamsTopo() {
		return paramstopo;
	}

	public void delParamTopo(JMEParamTopo topo) {
		if (topo != null && paramstopo.contains(topo)) {
			UndoItemField field = new UndoItemField(this, "delparamtopo", topo, null);
			manager.registerUndo(field);
			topo.getNode().setKind(JMENodeKind.SIMPLE);
			modified = true;
			paramstopo.remove(topo);
			update();
		}
	}

	public void delParamEbd(JMEParamEbd ebd) {
		if (ebd != null && paramsebd.contains(ebd)) {
			UndoItemField field = new UndoItemField(this, "delparamebd", ebd, null);
			manager.registerUndo(field);
			modified = true;
			paramsebd.remove(ebd);
			update();
		}
	}

	public void addParamTopo(JMEParamTopo topo) {
		if (topo != null && !paramstopo.contains(topo)) {
			UndoItemField field = new UndoItemField(this, "addparamtopo", null, topo);
			manager.registerUndo(field);
			modified = true;
			paramstopo.add(topo);
			update();
		}
	}

	public void addParamEbd(JMEParamEbd ebd) {

		if (ebd != null && !paramsebd.contains(ebd)) {
			UndoItemField field = new UndoItemField(this, "addparamebd", null, ebd);
			manager.registerUndo(field);
			modified = true;
			paramsebd.add(ebd);
			update();
		}

	}

	public JMEParamEbd addNewParamEbd() {
		JMEParamEbd newebd = new JMEParamEbd(this, "", "", "", paramsebd.size());
		synchronized (paramsebd) {
			paramsebd.add(newebd);
			modified = true;
			update();
		}
		return newebd;
	}

	@Override
	public UndoManager getUndoManager() {
		return manager;
	}

	public void insertParamTopo(JMEParamTopo p, int order) {
		if (p != null && paramstopo.contains(p)) {
			int oldorder = paramstopo.indexOf(p);
			paramstopo.add(order, p);

			if (oldorder >= order)
				oldorder++;
			modified = true;
			paramstopo.remove(oldorder);
			// p.getNode().setKind(JMENodeKind.SIMPLE);
			paramstopo.stream().forEach(f -> f.update());
		}
	}

	@Override
	public int compareTo(JMERule o) {

		int cmpcat = -1 * getCategory().compareTo(o.getCategory());

		String ofullname = (o.getCategory() == null ? "" : o.getCategory()) + "." + o.getName();
		String fullname = (getCategory() == null ? "" : getCategory()) + "." + getName();

		int cmpfull = fullname.compareTo(ofullname);
		if (cmpcat == 0)
			return cmpfull;
		else {
			if ("".equals(getCategory()))
				return 1;
			else if ("".equals(o.getCategory()))
				return -1;
			return cmpfull;
		}
	}

	public List<JMENode> getHooks() {
		if (left != null)
			return left.getHooks();
		return new ArrayList<JMENode>();
	}

	public void delParamTopo(JMENode node) {
		ArrayList<JMEParamTopo> newParam = new ArrayList<>();
		for (JMEParamTopo topo : paramstopo) {
			if (topo.getNode() != node) {
				newParam.add(topo);
			}
		}
		paramstopo.clear();
		paramstopo.addAll(newParam);
	}
	
	/**
	 * Try to add a hook to connected components without one (take any node
	 * with full orbit).
	 * @author romain
	 */
	public void enforceHooks() {
		if (left != null)
			left.enforceHooks();
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	public static boolean isValidName(JMEPreferences pref, String text) {
		Matcher matcher = PATTERN_MODULE.matcher(text);
		if(matcher.matches()) {
			boolean s = true;
				String templates = matcher.group(4);
				if(templates != null) {
					String[] splited =  templates.split(",");
					for (String subtype : splited) {
						s = s && isValidName(pref,subtype.trim());
					}
				}
			
			return s;
		}
		return false;
	}
	
	public JMENodeShape getShape() {
		return shape;
	}

	public void setShape(JMENodeShape shape) {
		this.shape = shape;
	}

	public boolean isMagnetic() {
		return magnetic;
	}

	public void setMagnetic(boolean magnetic) {
		this.magnetic = magnetic;
	}

	public RuleGraphViewGrid getGridsize() {
		return gridsize;
	}

	public void setGridsize(RuleGraphViewGrid gridsize) {
		// System.out.println("PRE GRIDSIZE: " + this.gridsize + " -> " + gridsize);
		this.gridsize = gridsize;
		// System.out.println("POST GRIDSIZE: " + this.gridsize);
	}

	public void setShowGrid(boolean selected) {
		this.showgrid = selected;
	}

	public boolean isShowGrid() {
		return showgrid;
	}

	@Override
	public Collection<JMEError> getAllErrors() {
		return getErrors();
	}


	@Override
	public boolean isModified() {
		return left.isModified() || right.isModified() || modified;
	}

	@Override
	public void resetModification() {
		modified = false;
		left.resetModification();
		right.resetModification();
		for (JMEParamEbd jmeParamEbd : paramsebd) {
			jmeParamEbd.resetModification();
		}

		for (JMEParamTopo jmeParamTopo : paramstopo) {
			jmeParamTopo.resetModification();
		}

		for (JMEElementView view : views) {
			if(view instanceof JMEElementWindowableView) {
				((JMEElementWindowableView)view).reloadTitle();
			}
		}
	}

	public String getMidProcess() {
		return midprocess;
	}

	public void setMidProcess(String p) {
		if (p != null && !p.equals(midprocess)) {
			modified = true;
			this.midprocess = p;
			update();
		}
	}

	public abstract JMERule copy(JMEModeler modeler, String rulename);

	public boolean hasTopoErrors() {
		for (JMEError err : errors) {
			if(err.getType() == JMEErrorType.TOPOLOGIC)
				return true;
		}
		return false;
	}

	public boolean hasCriticalErrors() {
		for (JMEError err : errors) {
			if (err.getSeverity() == JMEErrorSeverity.CRITIQUE)
				return true;
		}
		return false;
	}
	
	public static final Pattern PATTERN_MODULE = Pattern.compile(
			"([_]*[a-zA-Z][a-zA-Z0-9_]*([.][_]*[a-zA-Z][a-zA-Z0-9_]*)*)(<(.*)>)?\\*?\\z", Pattern.CASE_INSENSITIVE);
	
}
