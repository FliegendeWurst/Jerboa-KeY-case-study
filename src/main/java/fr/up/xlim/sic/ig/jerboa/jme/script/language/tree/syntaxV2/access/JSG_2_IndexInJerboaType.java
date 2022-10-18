package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_JerboaKeyword;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_JerboaKeyword.KeywordType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_IndexInJerboaType extends JSG_2_Entity {

	public enum JerboaTypeIndexable{LEFT_PATTERN, RIGHT_PATTERN, GMAP, RULE_RESULT, SCRIPT_HOOK};

	private JerboaTypeIndexable type;
	private JSG_2_Entity left;
	private List<JSG_2_Entity> argList;

	public JSG_2_IndexInJerboaType(JerboaTypeIndexable type, JSG_2_Entity left, List<JSG_2_Entity> argList, int l, int c) {
		super(l, c);
		this.type = type;
		switch (type) {
		case GMAP:
			this.left = new JSG_2_JerboaKeyword(KeywordType.GMAP, l, c);
			break;
		case LEFT_PATTERN:
			this.left = new JSG_2_JerboaKeyword(KeywordType.LEFTPATTERN, l, c);
			break;
		case RIGHT_PATTERN:
			this.left = new JSG_2_JerboaKeyword(KeywordType.RIGHTPATTERN, l, c);
			break;
		case SCRIPT_HOOK:
			this.left = new JSG_2_JerboaKeyword(KeywordType.HOOK, l, c);
			break;
		case RULE_RESULT:
		default:
			this.left = left;
			break;
		}
		this.argList = new ArrayList<JSG_2_Entity>();
		this.argList.addAll(argList);
	}

	public JSG_2_Type returnType() {
		System.err.println("TODO : faire le retour de type dans IndexInJerboatype");
		return null;
		//		if (indexInDartList != null)
		//			return new JSG_2_TypeDart(line, col);
		//		return new JSG_2_List(new JSG_TypeJerboaDart(line, column), line, column);
		// TODO: ici ça peut être intéressant d'avoir une liste de brin
		// retourné, mais en C++ en tout cas c'est pas prévu je crois.
	}

	public JSG_2_Entity getLeft() {
		return left;
	}
	
	public JerboaTypeIndexable getType() {
		return type;
	}

	public List<JSG_2_Entity> getArgList() {
		return argList;
	}

	public String toString() {
		String res = "JSG_2_IndexInJerboaType {";
		switch (type) {
		case LEFT_PATTERN: res += "@LeftPattern";
			break;
		case RIGHT_PATTERN: res += "@RightPattern";
				break;
		case GMAP: res += "@LeftPattern";
				break;
		case RULE_RESULT: res += "@ruleResult";
				break;
		default: res += "@unknown";
				break;
		}
		res+="[";
		for(int i=0; i<argList.size();i++) {
			res+=argList.get(i);
			if(i<argList.size()-1) {
				res+=", ";
			}
		}
		res+="] }";
		return res;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}


}
