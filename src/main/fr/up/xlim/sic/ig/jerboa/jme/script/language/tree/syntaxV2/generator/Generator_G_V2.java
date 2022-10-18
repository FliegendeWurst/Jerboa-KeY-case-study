package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.generator;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.GeneratedLanguage;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public interface Generator_G_V2
extends JSG_2_EntityVisitor<Boolean, RuntimeException> {

	public GeneratedLanguage getResult();

	public void beginGeneration(JSG_2_Entity js);
}
