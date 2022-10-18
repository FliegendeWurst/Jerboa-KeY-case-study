/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSAssocParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSAssignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSAtLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSBlock;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSBreak;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSCatch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSContinue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDeclare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDelete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSFor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSHeader;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSHookCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSIf;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSMap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSNOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSPrint;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSReturn;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSSequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSThrow;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSTry;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSUnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSWhile;

/**
 * @author Hakim
 *
 */
public interface JSInstVisitor<T, E extends Exception> {

	T accept(JSWhile jsWhile) throws E;

	T accept(JSAssignment jsAssignment) throws E;

	T accept(JSBlock jsBlock) throws E;

	T accept(JSFor jsFor) throws E;

	T accept(JSForEach jsForEach) throws E;

	T accept(JSForLoop jsForLoop) throws E;

	T accept(JSIf jsIf) throws E;

	T accept(JSSequence jsSequence) throws E;

	T accept(JSDoWhile jsDoWhile) throws E;

	T accept(JSExprInstruction jsExprInstruction) throws E;

	T accept(JSDeclare jsDeclare) throws E;

	T accept(JSMap jsMap);

	T accept(JSNOP jsEmpty);

	T accept(JSAssocParam jsAssocParam);

	T accept(JSDelete jsDelete);

	T accept(JSCatch jsCatch);

	T accept(JSTry jsTry);

	T accept(JSDeclareFunction jsDeclareFunction);

	T accept(JSPrint jsPrint);

	T accept(JSHookCall jsHookCall);

	T accept(JSAtLang jsAtLang);

	T accept(JSBreak jsBreak);

	T accept(JSHeader jsHeader);

	T accept(JSReturn jsReturn);

	T accept(JSMark jsMark);

	T accept(JSUnMark jsUnMark);

	T accept(JSContinue jsContinue);

	T accept(JSThrow jsThrow);

}
