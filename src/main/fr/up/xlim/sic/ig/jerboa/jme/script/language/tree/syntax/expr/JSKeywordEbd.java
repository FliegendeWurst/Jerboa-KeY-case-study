/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;

/**
 * @author Valentin
 *
 *         TODO: Bon il faut revoir cette classe. Ce n'est plus vraiment un type
 *         quand on utilse NAME ou ID... soit on sépare mais c'est pas terrible,
 *         soit on enlève sa relation avec JSType mais c'est pénible aussi.
 */
public class JSKeywordEbd extends JSType {

	private String ebdName;
	private EBDRequest req;

	public JSKeywordEbd(String _ebdname, EBDRequest _req, int l, int col) {
		super("@ebd<" + _ebdname + ">", l, col);
		ebdName = _ebdname;
		req = _req;
	}

	public String getEbd() {
		return ebdName;
	}

	public EBDRequest getRequest() {
		return req;
	}

	@Override
	public <T, E extends Exception> T visit(JSExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
