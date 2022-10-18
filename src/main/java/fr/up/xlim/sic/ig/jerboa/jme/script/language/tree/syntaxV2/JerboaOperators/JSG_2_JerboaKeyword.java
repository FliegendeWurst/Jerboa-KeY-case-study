/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_JerboaKeyword extends JSG_2_Entity {
	public enum KeywordType{MODELER, GMAP, LEFTPATTERN, RIGHTPATTERN, DIMENSION, HOOK,};
	
	private KeywordType type; 
	
	public JSG_2_JerboaKeyword(KeywordType _keyType, int l, int c) {
		super(l, c);
		this.type = _keyType;
	}

	public KeywordType getType() {
		return type;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
