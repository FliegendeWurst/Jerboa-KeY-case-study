package fr.up.xlim.sic.ig.jerboa.jme.script.language.generator;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public interface Generator_G
extends JSG_ExprVisitor<Boolean, RuntimeException>, JSG_InstVisitor<Boolean, RuntimeException> {

	public GeneratedLanguage getResult();

	public void beginGeneration(JSG_Sequence js);
}
