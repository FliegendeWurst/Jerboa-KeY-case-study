package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_TypeTemplate extends JSG_2_Type {
	protected JSG_2_Type typeTemplate;
	protected List<JSG_2_Type> templateTypeList;

	public JSG_2_TypeTemplate(JSG_2_Type type, List<JSG_2_Type> tlist, int l, int c) {
		super(type.toString(), l,c);
		typeTemplate = type;
		templateTypeList = new ArrayList<JSG_2_Type>();
		templateTypeList.addAll(tlist);
	}

	public List<JSG_2_Type> getTemplateTypeList() {
		return templateTypeList;
	}

//	@Override
//	public String getTypeName() {
////		String entireName = typeName + "<" ;
////		for(int i=0;i<templateTypeList.size();i++) {
////			entireName+=templateTypeList.get(i).getTypeName();
////			if(i<templateTypeList.size()-1) {
////				entireName+=", ";
////			}
////		}
////		typeName += ">";
//		return entireName;
//	}	
	
	public JSG_2_Type getTypeTemplate() {
		return typeTemplate;
	}

	public static <T1,T2>  boolean compare(Class<T1> t_1, Class<T2> t_2) {
		return t_1.equals(t_2); // ça ne check pas en profondeur les types templatés !
	}


	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
	
	@Override
	public String toString() {
		String s = typeName+"<";
		for(int i=0; i<templateTypeList.size(); i++) {
			s += templateTypeList.get(i);
			if(i<templateTypeList.size()-1) {
				s+=", ";
			}
		}
		s+=">";
		return s;
	}
}


