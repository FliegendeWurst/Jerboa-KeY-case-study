package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools;

import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Block;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;

public class JSGResult<T extends JSG_Entity> {
	private JSG_Sequence presequence;
	private T result;
	
	
	public JSGResult() {
		presequence = new JSG_Sequence(0, 0);
	}
	
	public JSGResult(T e) {
		presequence = new JSG_Sequence(0, 0);
		this.result = e;
	}
	
	public JSG_Sequence getPreSequence() {
		return presequence;
	}
	
	public void add(JSG_Instruction ins) {
		presequence.add(ins);
	}
	
	public void addAll(Collection<JSG_Instruction> is) {
		presequence.addAll(is);
	}
	
	public T getResult() {
		return result;
	}
	
	public void setResult(T r) {
		this.result = r;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(presequence.toString());
		sb.append("\n================\n");
		sb.append(result);
		return sb.toString();
	}
	
	public JSG_Instruction toSequence() {
		if(presequence.isEmpty()) {
			if(result instanceof JSG_Instruction)
				return (JSG_Instruction)result;
			else
				return new JSG_ExprInstruction((JSG_Expression) result);
		}
		else {
			JSG_Sequence seq = new JSG_Sequence(0, 0);
			seq.add(presequence);
			if(result instanceof JSG_Instruction)
				seq.add((JSG_Instruction)result);
			else
				seq.add(new JSG_ExprInstruction((JSG_Expression) result));
			JSG_Block bl = new JSG_Block(seq, true, 0, 0);
			return bl;
		}
	}
}
