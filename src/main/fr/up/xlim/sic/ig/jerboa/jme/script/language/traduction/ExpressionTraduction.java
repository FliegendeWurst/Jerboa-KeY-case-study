package fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;

public class ExpressionTraduction {
	ArrayList<JSG_Instruction> instrList;

	JSG_Expression expr;

	public ExpressionTraduction(JSG_Expression expression) {
		// TODO Auto-generated constructor stub
		instrList = new ArrayList<>();
		this.expr = expression;
	}

	public ExpressionTraduction(List<JSG_Instruction> instrList, JSG_Expression expr) {
		this.instrList = new ArrayList<>();
		this.instrList.addAll(instrList);
		this.expr = expr;
	}

	public ExpressionTraduction(JSG_Instruction instr, JSG_Expression expr) {
		this.instrList = new ArrayList<>();
		this.instrList.add(instr);
		this.expr = expr;
	}

	public void addInstruction(JSG_Instruction inst) {
		instrList.add(inst);
	}

	public void addInstruction(List<JSG_Instruction> inst) {
		instrList.addAll(inst);
	}

	public ArrayList<JSG_Instruction> getInstructions() {
		return instrList;
	}

	public JSG_Expression getExpression() {
		return expr;
	}

	public void setExpression(JSG_Expression e) {
		expr = e;
	}
}
