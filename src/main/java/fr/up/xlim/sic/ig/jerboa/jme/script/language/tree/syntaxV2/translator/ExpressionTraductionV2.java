package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.translator;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;

public class ExpressionTraductionV2 {
	ArrayList<JSG_2_Entity> instrList;

	JSG_2_Entity expr;

	public ExpressionTraductionV2(JSG_2_Entity expression) {
		instrList = new ArrayList<>();
		this.expr = expression;
	}

	public ExpressionTraductionV2(List<JSG_2_Entity> instrList, JSG_2_Entity expr) {
		this.instrList = new ArrayList<>();
		this.instrList.addAll(instrList);
		this.expr = expr;
	}

	public ExpressionTraductionV2(JSG_2_Entity instr, JSG_2_Entity expr) {
		this.instrList = new ArrayList<>();
		this.instrList.add(instr);
		this.expr = expr;
	}

	public void addInstruction(JSG_2_Entity inst) {
		instrList.add(inst);
	}

	public void addInstruction(List<JSG_2_Entity> inst) {
		instrList.addAll(inst);
	}

	public ArrayList<JSG_2_Entity> getInstructions() {
		return instrList;
	}

	public JSG_2_Entity getExpression() {
		return expr;
	}

	public void setExpression(JSG_2_Entity e) {
		expr = e;
	}
}
