/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction;

import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSChoice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSSequence;

/**
 *
 */
public class JSSyntaxToSemantic_ExpressionLang extends JSSyntaxToSemantic_common {
	private String curentRuleName;

	public JSSyntaxToSemantic_ExpressionLang(LanguageGlue _glue, JMEModeler modeler) {
		super(_glue, modeler);
		gmapHasDirectAccess = true;
	}

	@Override
	public JSG_Sequence accept(JSSequence jsSequence) throws RuntimeException {
		ArrayList<JSG_Instruction> list = new ArrayList<>();
		for (final JSInstruction inst : jsSequence) {
			if (!(inst instanceof JSExprInstruction && (((JSExprInstruction) inst).getExpr() instanceof JSApplyRule
					|| ((JSExprInstruction) inst).getExpr() instanceof JSChoice))) {
				JSG_Instruction i = inst.visit(this);
				if (i != null)
					list.add(i);
			}
		}
		return new JSG_Sequence(jsSequence.getLine(), jsSequence.getColumn(), list);
	}

}
