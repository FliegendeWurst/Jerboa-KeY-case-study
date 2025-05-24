package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class JMEModeler implements JMEElement {

	protected boolean modified;
	protected String name;
	protected String module;
	protected int dimension;

	protected ArrayList<JMERule> rules;
	private String comment;


	private HashMap<String, String> properties;

	public JMEModeler(String name, String module, int dim) {
		rules = new ArrayList<>();
		this.name = name;
		this.module = module;
		this.dimension = dim;

		comment = "";
		modified = false;

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

	public JMERule searchRule(String name) {
		for (JMERule rule : rules) {
			if (rule.getName().equals(name))
				return rule;
		}
		return null;
	}

	public void addRule(JMERule rule) {
		if (!rules.contains(rule)) {
			rules.add(rule);
			modified = true;
		}
	}

    public void removeRule(JMERule rule) {
		if (rules.remove(rule)) {
			rule.setModeler(this);
			modified = true;
		}		
	}

	public void setName(String newname) {
		if (newname != null && !name.equals(newname)) {
			name = newname;
			modified = true;
		}
	}

	public void setModule(String newmodule) {
		if (newmodule != null && !module.equals(newmodule)) {
			module = newmodule;
			modified = true;
		}
	}

	public void setDimension(int newdim) {
		if (dimension != newdim && newdim >= 0) {
			dimension = newdim;
			modified = true;
		}
	}


	public List<JMERule> getRules() {
		return rules;
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

	public String getComment() {
		return comment;
	}

	public String get(String prop) {
		return properties.get(prop);
	}

	public boolean existsKey(String prop) {
		return properties.containsKey(prop);
	}
}
