package fr.up.xlim.sic.ig.jerboa.jme.script.language.verification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_FreeMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Assignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_AssocParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_AtLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Block;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Break;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Catch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ClearHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Continue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Declare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DeclareMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Delete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_For;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Header;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_HookCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_If;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Map;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Mark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_NOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Print;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Return;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Throw;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Try;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_UnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_While;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_VerificationHasReturn implements JSG_InstVisitor<Boolean, RuntimeException> {
	// on mets a leur hauteur les erreurs potentielles, on les remontes quand on
	// termine le block courant
	private Map<Integer, ArrayList<JSG_Instruction>> depthForPotentialNoReturn;

	private Integer curentDepth;

	public JSG_VerificationHasReturn() {
		curentDepth = 0;
		depthForPotentialNoReturn = new HashMap<Integer, ArrayList<JSG_Instruction>>();
	}

	public ArrayList<JSError> beginVerif(JSG_Instruction s) {
		curentDepth = 0;
		depthForPotentialNoReturn.clear();
		s.visit(this);
		ArrayList<JSError> errList = new ArrayList<>();
		for (ArrayList<JSG_Instruction> l : depthForPotentialNoReturn.values()) {
			for (JSG_Instruction i : l) {
				errList.add(new JSError("potential no return : " + i, i.getLine(), i.getColumn(),
						JSErrorEnumType.CRITICAL));
			}
		}
		return errList;
	}

	@Override
	public Boolean accept(JSG_While jsWhile) throws RuntimeException {
		return false;
	}

	@Override
	public Boolean accept(JSG_Assignment jsAssignment) throws RuntimeException {
		return false;
	}

	@Override
	public Boolean accept(JSG_Block jsBlock) throws RuntimeException {
		curentDepth++;
		boolean result = jsBlock.getBody().visit(this);

		if (depthForPotentialNoReturn.containsKey(curentDepth)) {
			// on remonte les erreurs si on a pas eu de return;
			if (depthForPotentialNoReturn.containsKey(curentDepth - 1)) {
				depthForPotentialNoReturn.put(curentDepth - 1, new ArrayList<>());
			}
			if (curentDepth > 0)
				depthForPotentialNoReturn.get(curentDepth - 1).addAll(depthForPotentialNoReturn.get(curentDepth));
			// depthForPotentialNoReturn.remove(curentDepth);
		}
		curentDepth--;
		return result;
	}

	@Override
	public Boolean accept(JSG_For jsFor) throws RuntimeException {
		return false;
	}

	@Override
	public Boolean accept(JSG_ForEach jsForEach) throws RuntimeException {
		return false;
	}

	@Override
	public Boolean accept(JSG_ForLoop jsForLoop) throws RuntimeException {
		return false;
	}

	@Override
	public Boolean accept(JSG_If jsIf) throws RuntimeException {
		boolean test = false;
		if (jsIf.getAlternant() != null && !(jsIf.getAlternant() instanceof JSG_NOP)) {
			// si pas de else on peut pas savoir
			test = jsIf.getConsequence().visit(this);
			if (!test) {
				if (!depthForPotentialNoReturn.containsKey(curentDepth))
					depthForPotentialNoReturn.put(curentDepth, new ArrayList<>());
				depthForPotentialNoReturn.get(curentDepth).add(jsIf.getConsequence());
				return false;
			}
			test = test && jsIf.getAlternant().visit(this);
			if (!test) {
				System.err.println("if : " + test);
				if (!depthForPotentialNoReturn.containsKey(curentDepth))
					depthForPotentialNoReturn.put(curentDepth, new ArrayList<>());
				depthForPotentialNoReturn.get(curentDepth).add(jsIf.getAlternant());
			}
		}
		return test;
	}

	@Override
	public Boolean accept(JSG_Sequence jsSequence) throws RuntimeException {
		for (JSG_Instruction i : jsSequence) {
			if (i.visit(this))
				return true;
		}
		if (!depthForPotentialNoReturn.containsKey(curentDepth))
			depthForPotentialNoReturn.put(curentDepth, new ArrayList<>());
		depthForPotentialNoReturn.get(curentDepth).add(jsSequence);
		return false;
	}

	@Override
	public Boolean accept(JSG_DoWhile jsDoWhile) throws RuntimeException {
		return false;
	}

	@Override
	public Boolean accept(JSG_ExprInstruction jsExprInstruction) throws RuntimeException {
		return false;
	}

	@Override
	public Boolean accept(JSG_Declare jsDeclare) throws RuntimeException {
		return false;
	}

	@Override
	public Boolean accept(JSG_Map jsMap) {
		return false;
	}

	@Override
	public Boolean accept(JSG_NOP jsEmpty) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Delete jsg_Delete) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Catch jsg_Catch) {
		return jsg_Catch.getBlock().visit(this);
	}

	@Override
	public Boolean accept(JSG_Try jsg_Try) {
		boolean test = false;
		if (jsg_Try.getCatchList() != null && !jsg_Try.getCatchList().isEmpty()) {
			// si pas de catch on peut pas savoir: normalement c'est pas possible...
			if (jsg_Try.getFinallyBlock().visit(this)) // si finally est vrai alors c'est bon
				return true;
			test = jsg_Try.getTryBlock().visit(this);
			if (!test) { // comme on est pas certain que les exceptions seront matchés, on suppose que
							// c'est
				// un back door
				if (!depthForPotentialNoReturn.containsKey(curentDepth))
					depthForPotentialNoReturn.put(curentDepth, new ArrayList<>());
				depthForPotentialNoReturn.get(curentDepth).add(jsg_Try.getTryBlock());
			}
			for (JSG_Catch c : jsg_Try.getCatchList())
				test = test && c.visit(this);
		}
		return test;
	}

	@Override
	public Boolean accept(JSG_DeclareFunction jsg_DeclareFunction) {
		// TODO vérifier que le type retour est matché dans la fonction et qu'il y en
		// a un
		return false;
	}

	@Override
	public Boolean accept(JSG_Print jsg_Print) {
		return false;
	}

	@Override
	public Boolean accept(JSG_HookCall jsg_HookCall) {
		return false;
	}

	@Override
	public Boolean accept(JSG_AssocParam jsg_AssocParam) {
		return false;
	}

	@Override
	public Boolean accept(JSG_ClearHookList jsg_ClearHookList) {
		return false;
	}

	@Override
	public Boolean accept(JSG_AtLang jsg_AtLang) {
		return false;
	}

	@Override
	public Boolean accept(JSG_DeclareMark jsg_DeclareMark) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Break jsg_Break) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Header jsg_Header) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Return jsg_Return) {
		return true;
	}

	@Override
	public Boolean accept(JSG_UnMark jsg_UnMark) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Mark jsg_Mark) {
		return false;
	}

	@Override
	public Boolean accept(JSG_FreeMarker jsg_FreeMarker) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Continue jsg_Continue) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Throw jsg_Throw) {
		return false;
	}

}
