package fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;

public class InstructionTraduction {
	ArrayList<JSG_Instruction> instrList;

	public InstructionTraduction() {
		// TODO Auto-generated constructor stub
		this.instrList.addAll(instrList);
	}

	public InstructionTraduction(List<JSG_Instruction> instrList) {
		this.instrList = new ArrayList<>();
		this.instrList.addAll(instrList);
	}

	public void addInstruction(JSG_Instruction inst) {
		instrList.add(inst);
	}
}
