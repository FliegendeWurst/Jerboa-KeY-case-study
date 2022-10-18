package fr.up.xlim.sic.ig.jerboa.jme.script.language.verification;

public class JSError {

	private String msg;
	private int line_beg, line_end;
	private int column_beg;

	private JSErrorEnumType criticality;

	public JSError(String m, int l, int c, JSErrorEnumType crit) {
		msg = m;
		line_beg = l;
		column_beg = c;
		line_end = l;
		criticality = crit;
	}

	public JSError(String m, int lbeg, int cbeg, int lend, JSErrorEnumType crit) {
		msg = m;
		line_beg = lbeg;
		column_beg = cbeg;
		line_end = lend;
		criticality = crit;
	}

	public JSError(JSError e) {
		msg = e.msg;
		line_beg = e.line_beg;
		column_beg = e.column_beg;
		line_end = e.line_end;
		criticality = e.criticality;
	}

	@Override
	public String toString() {
		if (line_beg == line_end)
			return "[" + criticalityMsg(criticality) + "]" + msg + " at line : " + line_beg + " col " + column_beg;
		else
			return "[" + criticalityMsg(criticality) + "]" + msg + " between line : " + line_beg + " and  " + line_end;
	}

	public static String criticalityMsg(JSErrorEnumType crit) {
		switch (crit) {
		case INFO:
			return "Information";
		case CRITICAL:
			return "Error";
		case WARNING:
			return "Warning";
		case DEADCODE:
			return "Dead code";
		default:
			return "undefined";
		}
	}

	public JSErrorEnumType getCriticality() {
		return criticality;
	}

	public int getLine() {
		return line_beg;
	}

	public int getLineEnd() {
		return line_end;
	}

	public int getColumn() {
		return column_beg;
	}
}
