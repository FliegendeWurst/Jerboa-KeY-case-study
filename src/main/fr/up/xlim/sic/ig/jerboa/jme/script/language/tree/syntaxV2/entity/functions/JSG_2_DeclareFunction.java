/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Block;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

/**
 * @author Valentin
 *
 */
public class JSG_2_DeclareFunction extends JSG_2_Entity {

	private String name;
	private ArrayList<JSG_2_Entity> arguments;
	private JSG_2_Block block;
	private JSG_2_Type returnType;

	int line, column;

	public JSG_2_DeclareFunction(JSG_2_Type _returnType, String name, Collection<JSG_2_Entity> args, JSG_2_Block _block,
			int l, int col) {
		super(l, col);
		returnType = _returnType;
		this.name = name;
		this.arguments = new ArrayList<>(args);
		this.block = _block;
	}

	public String getName() {
		return name;
	}

	public ArrayList<JSG_2_Entity> getArguments() {
		return arguments;
	}

	public JSG_2_Block getBlock() {
		return block;
	}

	public JSG_2_Type getReturnType() {
		return returnType;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

}
