/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Constructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Return;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSSequence;

/**
 *
 */
public class JSSyntaxToSemantic_Script extends JSSyntaxToSemantic_common {

	public JSSyntaxToSemantic_Script(LanguageGlue _glue, JMEModeler modeler) {
		super(_glue, modeler);
		gmapHasDirectAccess = false; // appelÃ© par owner->gmap en C++
	}
	// TODO: faire un beginGeneration pour savoir si un return a Ã©tÃ© appelÃ©
	// dans
	// le flux principal

	public JSG_Sequence beginGeneration(JSSequence s) {
		JSG_Sequence seqRes = (JSG_Sequence) s.visit(this);
		if (!mainStreamHasReturn) {
			seqRes.add(new JSG_Return(-1, -1, new JSG_Constructor(new JSG_TypeJerboaRuleResult(-1, -1), null)));
		}
		gmapHasDirectAccess = false;
		return seqRes;
	}

	@Override
	public JSG_DeclareFunction accept(JSDeclareFunction jsDeclareFunction) {
		return null;
	}

}
