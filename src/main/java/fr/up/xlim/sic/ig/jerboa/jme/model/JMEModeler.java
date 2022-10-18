package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.ruleview.RuleAtomicView;

public class JMEModeler extends JMEElementWindowable implements JMEElement {

	protected boolean modified;
	protected String name;
	protected String module;
	protected int dimension;

	protected ArrayList<JMEEmbeddingInfo> embeddings;
	protected ArrayList<JMERule> rules;
	protected ArrayList<JMERule> inferredrules;
	protected Set<JMEElementView> views;
	private String comment;
	private UndoManager manager;

	private String fileJME;
	private String destDir;
	private String projectDir;

	private HashMap<String, String> properties;
	private String header;
	private JMEModelerGenerationType type;
	// NON PAS DE VUE DANS LE MODEL!!!

	public JMEModeler(String name, String module, int dim) {
		embeddings = new ArrayList<>();
		rules = new ArrayList<>();
		inferredrules = new ArrayList<>();
		views = new HashSet<>();
		this.name = name;
		this.module = module;
		this.dimension = dim;
		this.manager = new UndoManager();

		comment = "";
		modified = false;
		type = JMEModelerGenerationType.JAVA;

		destDir = ".";
		fileJME = "default.jme";
		header = "";
		projectDir = ".";
		properties = new HashMap<>();
	}

	@Override
	public String getName() {
		return name;
	}

	public String getModule() {
		return module;
	}

	public int getDimension() {
		return dimension;
	}

	public Set<JMEElementView> getViews() {
		return views;
	}

	@Override
	public boolean isModified() {
		for (JMEEmbeddingInfo ebd : embeddings) {
			if (ebd.isModified())
				return true;
		}
		for (JMERule rule : rules) {
			if (rule.isModified())
				return true;
		}

		return modified;
	}

	public JMEEmbeddingInfo search(String name) {
		for (JMEEmbeddingInfo info : embeddings) {
			if (info.getName().equals(name))
				return info;
		}
		return null;
	}

	public JMERule searchRule(String name) {
		for (JMERule rule : rules) {
			if (rule.getName().equals(name))
				return rule;
		}
		for (JMERule rule : inferredrules) {
			if (rule.getName().equals(name))
				return rule;
		}
		return null;
	}

	public void addEmbedding(JMEEmbeddingInfo info) {
		if (!embeddings.contains(info)) {
			manager.registerUndo(new UndoItemField(this, "addebd", info, null, !modified));
			embeddings.add(info);
			modified = true;
			update();
		}
	}

	public void addRule(JMERule rule) {
		if (!rules.contains(rule)) {
			manager.registerUndo(new UndoItemField(this, "addrule", rule, null, !modified));
			rules.add(rule);
			modified = true;
			update();
		}
	}
	
	public void addInferRule(JMERule ruleed) {
		if (!inferredrules.contains(ruleed)) {
			inferredrules.add(ruleed);
			// update();
		}
	}


	public void addRuleView(RuleAtomicView rv) {
		if (!rules.contains(rv.getRule())) {
			views.add(rv);
			rules.add(rv.getRule());
			update();
		}
	}

	public void removeEmbedding(JMEEmbeddingInfo info) {
		if (embeddings.remove(info)) {
			manager.registerUndo(new UndoItemField(this, "delebd", info, null, !modified));
			modified = true;
			for(JMERule r : rules) {
				for(JMENode n : r.getRight().getNodes()) {
					List<JMENodeExpression>listNodeExpr = new ArrayList<>();
					listNodeExpr.addAll(n.getExplicitExprs());
					for(JMENodeExpression e : listNodeExpr) {
						if(e.getEbdInfo().getName().compareTo(info.getName())==0) {
							n.removeExpression(e);
						}
					}
				}
			}
			update();
		}
	}

	public void removeRule(JMERule rule) {
		if (rules.remove(rule)) {
			manager.registerUndo(new UndoItemField(this, "delrule", rule, null, !modified));
			rule.setModeler(this);
			modified = true;
			update();
		}
		if(inferredrules.remove(rule)) {
			System.out.println("DELETE infered rule: " + rule.getFullName());
			update();
		}
		
	}

	public void setName(String newname) {
		if (newname != null && !name.equals(newname)) {
			manager.registerUndo(new UndoItemField(this, "name", name, newname, !modified));
			name = newname;
			modified = true;
			update();
		}
	}

	public void setModule(String newmodule) {
		if (newmodule != null && !module.equals(newmodule)) {
			manager.registerUndo(new UndoItemField(this, "module", module, newmodule, !modified));
			module = newmodule;
			modified = true;
			update();
		}
	}

	public void setDimension(int newdim) {
		if (dimension != newdim && newdim >= 0) {
			manager.registerUndo(new UndoItemField(this, "dimension", dimension, newdim, !modified));
			dimension = newdim;
			modified = true;
			update();
		}
	}

	public List<JMEEmbeddingInfo> getEmbeddings() {
		return embeddings;
	}

	public JMEEmbeddingInfo getEmbedding(String ebdName) {
		for (JMEEmbeddingInfo einfo : embeddings)
			if (einfo.getName().compareTo(ebdName) == 0)
				return einfo;
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
		for (JMEElementView view : views) {
			try {
				view.reload();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (JMEEmbeddingInfo e : embeddings) {
			e.update();
		}

		for (JMERule rule : getAllRules()) {
			rule.update();
		}
	}

	public void setComment(String text) {
		if (text != null && !text.equals(comment)) {
			manager.registerUndo(new UndoItemField(this, "comment", comment, text, !modified));
			comment = text;
			modified = true;
			update();
		}
	}

	public List<JMERule> getRules() {
		return rules;
	}
	
	public List<JMERule> getAllRules() {
		ArrayList<JMERule> res = new ArrayList<>(rules);
		res.addAll(inferredrules);
		return res;
	}
	
	public List<JMERule> getInferedRules() {
		return inferredrules;
	}

	/*
	 * H: Euh c'est une blague??? CETTE FONCTION N'A PAS SA PLACE DANS LE MODELEUR!!! 
	 */
	public String getRelativPathToDestDirForFile(String s) {
		String path = "";
		String[] destDirList = destDir.split(File.separator.compareTo("\\") == 0 ? "\\\\" : "/");

		String[] fileDirList = s.split("/");
		int i = 0;
		System.err.println(s + " -- " + destDir);
		for (i = 0; i < Math.min(fileDirList.length, destDirList.length); i++) {
			if (destDirList[i].compareTo(fileDirList[i]) != 0) {
				// fin = i;
				break;
			}
		}
		for (int j = i; j < destDirList.length; j++) {
			path += "../";
		}
		for (int j = i; j < fileDirList.length - 1; j++) {
			path += fileDirList[j] + "/";
		}
		System.err.println(path);
		path += fileDirList[fileDirList.length - 1];
		return path;
	}

	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch (fitem.field()) {
		case "name":
			name = (String) fitem.value();
			break;
		case "module":
			module = (String) fitem.value();
			break;
		case "dimension":
			dimension = ((Integer) fitem.value()).intValue();
			break;
		case "addebd":
			embeddings.remove(fitem.value());
			break;
		case "delebd":
			embeddings.add((JMEEmbeddingInfo) fitem.value());
			break;
		case "addrule":
			rules.remove(fitem.value());
			break;
		case "delrule":
			rules.add((JMERule) fitem.value());
			break;
		case "comment":
			comment = (String) fitem.value();
		default:
			break;
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
		case "module":
			module = (String) fitem.newValue();
			break;
		case "dimension":
			dimension = ((Integer) fitem.newValue()).intValue();
			break;
		case "addebd":
			embeddings.add((JMEEmbeddingInfo) fitem.value());
			break;
		case "delebd":
			embeddings.remove(fitem.value());
			break;
		case "addrule":
			rules.add((JMERule) fitem.value());
			break;
		case "delrule":
			rules.remove(fitem.value());
			break;
		case "comment":
			comment = (String) fitem.newValue();
		default:
			break;
		}
		if (fitem.getModifState())
			modified = true;
		manager.transfertUndo(fitem);
		update();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		return sb.toString();
	}

	public int sizeRules() {
		return rules.size();
	}

	public int sizeEmbeddings() {
		return embeddings.size();
	}

	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitModeler(this);
	}

	public String getComment() {
		return comment;
	}

	public String getDestDir() {
		return destDir;
	}

	public void setDestDir(String dir) {
		if (dir != null && !destDir.equals(dir)) {
			destDir = dir;
			modified = true;
			update();
		}
	}

	public String get(String prop) {
		return properties.get(prop);
	}

	public boolean existsKey(String prop) {
		return properties.containsKey(prop);
	}

	@Override
	public UndoManager getUndoManager() {
		return manager;
	}

	public void setFileJME(String filexml) {
		if (filexml != null && !this.fileJME.equals(filexml)) {
			this.fileJME = filexml;
			// Attention pas de modified = true ici du moins il faut etre raccord apres un chargement de modeleur pour l'instant
			// je me focalise pour finir un truc qui fonctionne un peu
			update();
		}
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String h) {
		if (h != null && !this.header.equals(h)) {
			this.header = h;
			modified = true;
			update();
		}
	}

	public String getFileJME() {
		return fileJME;
	}

	public String getProjectDir() {
		return projectDir;
	}

	public void setProjectDir(String projectDir) {
		this.projectDir = projectDir;
	}

	public JMEModelerGenerationType getGenerationType() {
		return type;
	}

	public void setGenerationType(JMEModelerGenerationType t) {
		if (this.type != t) {
			this.type = t;
			modified = true;
			update();
		}
	}

	@Override
	public Collection<JMEError> getAllErrors() {
		ArrayList<JMEError> result = new ArrayList<>(errors);

		for (JMEEmbeddingInfo ebdinfo : embeddings) {
			result.addAll(ebdinfo.getAllErrors());
		}

		for (JMERule jmeRule : getAllRules()) {
			result.addAll(jmeRule.getAllErrors());
		}

		return result;
	}
	
	public void copyRule(JMERule oldrule, String newname) {
		JMERule newrule = oldrule.copy(this, newname);
		addRule(newrule);
	}


	@Override
	public void resetModification() {
		// TODO: faut-il reset le undomanager
		modified = false;
		for (JMEEmbeddingInfo jmeEmbeddingInfo : embeddings) {
			jmeEmbeddingInfo.resetModification();
		}

		for (JMERule jmeRule : getAllRules()) {
			jmeRule.resetModification();
		}
		update();
	}

}
