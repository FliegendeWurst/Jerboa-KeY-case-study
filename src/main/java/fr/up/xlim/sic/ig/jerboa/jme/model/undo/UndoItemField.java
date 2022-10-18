package fr.up.xlim.sic.ig.jerboa.jme.model.undo;

public class UndoItemField implements UndoItem {
	
	private String field;
	private UndoableObject object;
	private Object value;
	private Object newvalue;
	private boolean modified;
	
	public UndoItemField(UndoableObject obj, String field, Object oldvalue, Object newvalue, boolean ismodified) {
		this.field = field;
		this.object = obj;
		this.value = oldvalue;
		this.newvalue = newvalue;
		this.modified = ismodified;
	}

	public UndoItemField(UndoableObject jmeArc, String string, Object oldvalue, Object newvalue) {
		this(jmeArc,string,oldvalue,newvalue,true); 
	}

	@Override
	public UndoableObject getObject() {
		return object;
	}
	
	public Object newValue() {
		return newvalue;
	}
	
	public Object value() {
		return value;
	}
	
	public String field() {
		return field;
	}
	
	public void setModifState(boolean modif) {
		this.modified = modif;
	}
	
	public boolean getModifState() {
		return modified;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("UI: ").append(object).append(" : ").append(field).append("=").append(value).append("\tnewvalue=").append(newvalue);
		return sb.toString();
	}
}
