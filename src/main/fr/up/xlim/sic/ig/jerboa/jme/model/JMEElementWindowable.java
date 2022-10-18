package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;

public abstract class JMEElementWindowable implements JMEElement {

	protected int posX;
	protected int posY;
	protected int sizeX;
	protected int sizeY;
	protected boolean isopened;
	protected boolean isfullscreen;
	protected boolean isdocked;
	
	protected ArrayList<JMEError> errors;
	
	
	protected JMEElementWindowable() {
		this.posX = 0;
		this.posY = 0;
		this.sizeX = 800;
		this.sizeY = 600;
		this.isopened = true;
		this.isfullscreen = true;
		this.isdocked = true;
		
		
		errors = new ArrayList<>();
	}

	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	

	public void setPosX(int x) {
		posX = x;
	}
	
	public void setPosY(int y) {
		posY = y;
	}

	public int getSizeX() {
		return sizeX;
	}


	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}


	public int getSizeY() {
		return sizeY;
	}


	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}


	public boolean isIsopened() {
		return isopened;
	}


	public void setIsopened(boolean isopened) {
		if(this.isopened != isopened) {
			UndoItemField field = new UndoItemField(this, "isopened", this.isopened, isopened);
			getUndoManager().registerUndo(field);
			this.isopened = isopened;
		}
	}


	public boolean isIsfullscreen() {
		return isfullscreen;
	}


	public void setIsfullscreen(boolean isfullscreen) {
		if(this.isfullscreen != isfullscreen) {
			UndoItemField field = new UndoItemField(this, "fullscreen", this.isfullscreen, isfullscreen);
			getUndoManager().registerUndo(field);
			this.isfullscreen = isfullscreen;
		}
	}


	public boolean isIsdocked() {
		return isdocked;
	}


	public void setIsdocked(boolean isdocked) {
		if(this.isdocked != isdocked) {
			UndoItemField field = new UndoItemField(this, "isdocked", this.isdocked, isdocked);
			getUndoManager().registerUndo(field);
			this.isdocked = isdocked;
		}
	}
	
	
	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch (fitem.field()) {
		case "isdocked":
			this.isdocked = (Boolean)fitem.value();
			break;
		case "fullscreen":
			this.isfullscreen = (Boolean) fitem.value();
			break;
		case "isopened":
			this.isopened = (Boolean) fitem.value();
			break;
		}
		
		getUndoManager().transfertRedo(fitem);
	}

	public int countErrors() {
		return errors.size();
	}
	
	public JMEError getError(int i) {
		return errors.get(i);
	}
	
	public Collection<JMEError> getErrors() {
		return errors;
	}
	
	public abstract Collection<JMEError> getAllErrors();
	
	public void addError(JMEError e) {
		errors.add(e);
		update();
	}
	
	public void addErrors(Collection<JMEError> errors) {
		this.errors.addAll(errors);
		update();
	}
	
	public void setErrors(Collection<JMEError> errors) {
		this.errors = new ArrayList<>(errors);
		update();
	}
	
	public void clearErrors() {
		this.errors.clear();
		update();
	}
}
