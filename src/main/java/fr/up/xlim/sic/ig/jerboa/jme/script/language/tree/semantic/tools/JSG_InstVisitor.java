/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools;

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

/**
 * @author Hakim
 *
 */
public interface JSG_InstVisitor<T, E extends Exception> {

	T accept(JSG_While jsWhile) throws E;

	T accept(JSG_Assignment jsAssignment) throws E;

	T accept(JSG_Block jsBlock) throws E;

	T accept(JSG_For jsFor) throws E;

	T accept(JSG_ForEach jsForEach) throws E;

	T accept(JSG_ForLoop jsForLoop) throws E;

	T accept(JSG_If jsIf) throws E;

	T accept(JSG_Sequence jsSequence) throws E;

	T accept(JSG_DoWhile jsDoWhile) throws E;

	T accept(JSG_ExprInstruction jsExprInstruction) throws E;

	T accept(JSG_Declare jsDeclare) throws E;

	T accept(JSG_Map jsMap);

	T accept(JSG_NOP jsEmpty);

	T accept(JSG_Delete jsg_Delete);

	T accept(JSG_Catch jsg_Catch);

	T accept(JSG_Try jsg_Try);

	T accept(JSG_DeclareFunction jsg_DeclareFunction);

	T accept(JSG_Print jsg_Print);

	T accept(JSG_HookCall jsg_HookCall);

	T accept(JSG_AssocParam jsg_AssocParam);

	T accept(JSG_ClearHookList jsg_ClearHookList);

	T accept(JSG_AtLang jsg_AtLang);

	T accept(JSG_DeclareMark jsg_DeclareMark);

	T accept(JSG_Break jsg_Break);

	T accept(JSG_Header jsg_Header);

	T accept(JSG_Return jsg_Return);

	T accept(JSG_UnMark jsg_UnMark);

	T accept(JSG_Mark jsg_Mark);

	T accept(JSG_FreeMarker jsg_FreeMarker);

	T accept(JSG_Continue jsg_Continue);

	T accept(JSG_Throw jsg_Throw);
}
