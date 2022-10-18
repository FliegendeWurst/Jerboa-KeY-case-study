package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class JMECategoryComboBoxModel implements ComboBoxModel<String>, JMEElementView {
	
	private HashSet<ListDataListener> dataListener;
	private String current;
	private ArrayList<String> list;
	private JMEModeler modeler;
	private ArrayList<JMERule> lastRules;
	
	public JMECategoryComboBoxModel(JerboaModelerEditor editor) {
		super();
		dataListener = new HashSet<>();
		current = "";
		list = new ArrayList<>();
		modeler = editor.getModeler();
		modeler.addView(this);
		reload();
	}
	
	public boolean add(String e) {
		if(!list.contains(e)) {
			boolean o = list.add(e);
			for (ListDataListener l : dataListener) {
				l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, list.size()-1, list.size()-1));
			}
			return o;
		}
		return false;
	}
	
	@Override
	public void addListDataListener(ListDataListener l) {
		System.out.println();
		dataListener.add(l);
	}

	@Override
	public String getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		dataListener.remove(l);
	}

	@Override
	public Object getSelectedItem() {
		return current;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		//System.out.println("SET SELECTED ITEM: "+anItem);
		this.current = (String)anItem;
	}

	@Override
	public void reload() {
		list.clear();
		if(lastRules != null) {
			for (JMERule jmeRule : lastRules) {
				jmeRule.removeView(this);
			}
		}
		
		List<JMERule> rules = modeler.getRules();
		lastRules = new ArrayList<JMERule>();
		for (JMERule r : rules) {
			lastRules.add(r);
			r.addView(this);
			if(!list.contains(r.getCategory())) {
				list.add(r.getCategory());
			}
		}
		
		Collections.sort(list);
		
		for (ListDataListener l : dataListener) {
			l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, list.size()-1));
		}
	}

	@Override
	public void unlink() {
		modeler.removeView(this);
		if(lastRules != null) {
			for (JMERule jmeRule : lastRules) {
				jmeRule.removeView(this);
			}
		}
	}

	@Override
	public JMEElement getSourceElement() {
		return modeler;
	}
}