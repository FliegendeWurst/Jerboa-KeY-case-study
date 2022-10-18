package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoManager;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementWindowableView;
import up.jerboa.core.JerboaOrbit;

public class JMEEmbeddingInfo extends JMEElementWindowable implements JMEElement {
	// intrinsic parameters
	protected String name;
	protected JerboaOrbit orbit;
	protected String type;
	protected Color color;

	// editors parameters
	protected boolean modified;
	protected Set<JMEElementView> views;
	protected String comment;
	protected UndoManager manager;
	private String headerFile;
	private JMEModeler modeler;
	private boolean visible;
	private String defaultCode;

	public JMEEmbeddingInfo(JMEModeler modeler, int id, String name,
			JerboaOrbit orbit, String type, String com) {
		this.name = name;
		this.type = type;
		this.orbit = orbit;
		
		this.modified = false;
		this.headerFile = "";
		comment = com;
		this.views = new HashSet<>();
		this.modeler = modeler;
		this.manager = modeler.getUndoManager();
		visible = true;
		
		color = null;
		defaultCode = "";
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		if(this.color != color) {
			this.color = color;
			modified = true;
			update();
		}
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public boolean isModified() {
		return modified;
	}

	public JerboaOrbit getOrbit() {
		return orbit;
	}

	public void setOrbit(JerboaOrbit orbit) {
		if (orbit != null && !orbit.equalsStrict(this.orbit)) {
			manager.registerUndo(new UndoItemField(this, "orbit", this.orbit, orbit, !modified));
			this.orbit = orbit;
			modified = true;
			update();
		}
	}
	
	public String getDefaultCode() {
		return defaultCode;
	}
	
	public void setDefaultCode(String defcode) {
		if(defcode != null && !defcode.equals(defaultCode)){
			manager.registerUndo(new UndoItemField(this, "defaultCode", this.defaultCode, defcode, !modified));
			this.defaultCode = defcode;
			modified = true;
			update();
		}
	}
	
	public boolean hasDefaultCode() {
		return defaultCode != null && !defaultCode.trim().isEmpty();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type != null && !type.equals(this.type)) {
			manager.registerUndo(new UndoItemField(this, "type", this.type, type, !modified));
			this.type = type;
			modified = true;
			update();
		}
	}

	public void setName(String name) {
		if (!name.equals(this.name)) {
			manager.registerUndo(new UndoItemField(this, "name", this.name,
					name, !modified));
			this.name = name;
			modified = true;
			update();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(":").append(orbit); // .append(" ->
													// ").append(type);
		return sb.toString();
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
	}

	public void setComment(String text) {
		if (text != null && !text.equals(comment)) {
			comment = text;
			modified = true;
			update();
		}
	}

	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch (fitem.field()) {
		case "name": {
			name = (String) fitem.value();
			break;
		}
		case "orbit": {
			orbit = (JerboaOrbit) fitem.value();
			break;
		}
		case "type": {
			type = (String) fitem.value();
			break;
		}
		case "comment": {
			comment = (String) fitem.value();
			break;
		}
		}
		manager.transfertRedo(fitem);
		update();
	}

	public void setFileHeader(String headerF) {
		if(headerF != null && !headerF.equals(headerFile)) {
			headerFile = headerF;
			modified = true;
			update();
		}
	}

	public String getFileHeader() {
		return headerFile;
	}

	@Override
	public void redo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch (fitem.field()) {
		case "name": {
			name = (String) fitem.newValue();
			break;
		}
		case "orbit": {
			orbit = (JerboaOrbit) fitem.newValue();
			break;
		}
		case "type": {
			type = (String) fitem.newValue();
			break;
		}
		case "comment": {
			comment = (String) fitem.newValue();
			break;
		}
		}
		manager.transfertUndo(fitem);
		update();
	}

	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitEmbeddingInfo(this);
	}

	@Override
	public UndoManager getUndoManager() {
		return manager;
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setIsVisible(boolean visible) {
		if(visible != this.visible) {
			this.visible = visible;
			modified = true;
			update();
		}
	}
	
	public Collection<JMEError> getAllErrors() {
		return getErrors();
	}

	@Override
	public void resetModification() {
		modified = false;
		
		for (JMEElementView view : views) {
			if(view instanceof JMEElementWindowableView) {
				((JMEElementWindowableView)view).reloadTitle();
			}
		}
	}
}
