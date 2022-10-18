package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public interface JSG_Expression extends JSG_Entity {

	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E;

}