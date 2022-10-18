package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.verification;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Alpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_ApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_AssocParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Choice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Collect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_CollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_EbdParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_FreeMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_GetEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_GetMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_IsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_IsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_JerboaKeyword;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Mark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Rule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleArg;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleNodeId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleNodeId.GraphSide;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_UnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_InScope;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_Index;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_IndexInJerboaType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_AtLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Block;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Boolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Break;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Cast;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Catch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Comment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Continue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Delete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Double;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Float;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Header;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_If;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Indirection;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Integer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Literal;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Long;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Map;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_NOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_New;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Not;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Null;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Operator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Orbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Print;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Return;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_String;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Throw;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Try;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Unreference;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Variable;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration.JSG_2_Assignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration.JSG_2_Declare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration.JSG_2_DeclareMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_AddInHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_AddInList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_Call;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_CallListSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_CallRuleResHeight;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_CallRuleResWidth;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_Constructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_DeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GMapSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetDartId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetEbdId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetEbdName;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetEbdOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_PackagedType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeBoolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeDart;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeEmbedding;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeGmap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypePointer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeString;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeTemplate;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_DoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_For;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_ForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_ForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_While;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSError;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSErrorEnumType;

public class Verification_V2 implements JSG_2_EntityVisitor<JSG_2_Type, RuntimeException> {

	private List<JSError> errorList;
	private TranslatorContextV2 context;
	private LanguageGlue glue;
	
	public Verification_V2(TranslatorContextV2 context, LanguageGlue glue) {
		this.context = context;
		this.glue = glue;
		this.errorList = new ArrayList<JSError>();
	}

	public List<JSError> getErrors(){
		return errorList;
	}
	

	public void clearError(){
		errorList.clear();;
	}
	
	public boolean isDartList(JSG_2_Entity t) {
		return t instanceof JSG_2_TypeList && ((JSG_2_TypeList)t).getListContentType() instanceof JSG_2_TypeDart;
	}
	
	public String printType(JSG_2_Type t) {
		return t!=null?t.toString():"unknown";
	}
	
	public boolean sameType(JSG_2_Type a, JSG_2_Type b) {
		boolean test = (a==null && b==null)
				|| (a!=null && b!=null 
				&& ( //a.getClass().isInstance(b.getClass()) 
					//|| 
					a.getTypeName().compareTo(b.getTypeName())==0)
				);

		if(a!=null && b!=null) {
			String aT = a.getTypeName();
			String bT = b.getTypeName();
			if(a instanceof JSG_2_TypeEmbedding) {
				JMEEmbeddingInfo einf = context.getModeler().getEmbedding(((JSG_2_TypeEmbedding)a).getEbdName());
				if(einf!=null) {
					aT = einf.getType();
				}
			}
			if(b instanceof JSG_2_TypeEmbedding) {
				JMEEmbeddingInfo einf = context.getModeler().getEmbedding(((JSG_2_TypeEmbedding)b).getEbdName());
				if(einf!=null) {
					bT = context.getType(einf, b.getLine(), b.getColumn()).getTypeName();
				}
			}
			if(aT.length()>0 && bT.length()>0) {
				test = test || (aT.compareTo(bT)==0);
			}
			
		}
		return test;
	}

	@Override
	public JSG_2_Type accept(JSG_2_AddInHookList jsg_2_AddInHookList) {
		int l = jsg_2_AddInHookList.getLine();
		int c = jsg_2_AddInHookList.getColumn();
		for(JSG_2_Entity e : jsg_2_AddInHookList.getArgs()) {
			JSG_2_Type t = e.visit(this);
			if(!(t instanceof JSG_2_TypeDart) && !(isDartList(t))) {
				errorList.add(new JSError("Wrong type : trying to add element of type '"+printType(t)+"' in a hook list",l,c,JSErrorEnumType.CRITICAL));
			}
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_AddInList jsg_2_AddInList) {
		int l = jsg_2_AddInList.getLine();
		int c = jsg_2_AddInList.getColumn();
		
		JSG_2_Type listType = jsg_2_AddInList.getList().visit(this);
		if(listType!=null) {
			if(listType instanceof JSG_2_TypeList) {
				for(JSG_2_Entity e : jsg_2_AddInList.getArgs()) {
					JSG_2_Type t = e.visit(this);
					if( !( sameType(t, listType)) ) {
						errorList.add(new JSError("Wrong type : trying to add element of type '"+printType(t)+"' in a list of type " + listType, l, c, JSErrorEnumType.INFO));
					}
				}
			}else{
				errorList.add(new JSError("Wrong type : left element may be not a list but type '"+printType(listType)+"' in a hook list", l, c, JSErrorEnumType.INFO));				
			}
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Alpha jsg_2_Alpha) {
		int l = jsg_2_Alpha.getLine();
		int c = jsg_2_Alpha.getColumn();
		JSG_2_Type leftType = jsg_2_Alpha.getNode().visit(this);
		if(! (leftType instanceof JSG_2_TypeDart) ) {
			errorList.add(new JSError("Wrong type : trying access to topological neightbor on element of type '"+printType(leftType)+"' but expected type JerboaDart", l, c, JSErrorEnumType.INFO));
		}
		return new JSG_2_TypeDart(l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_ApplyRule jsg_2_ApplyRule) {
		int l = jsg_2_ApplyRule.getLine();
		int c = jsg_2_ApplyRule.getColumn();
		for(JSG_2_Entity e : jsg_2_ApplyRule.getArgs()) {
//			JSG_2_Type et = 
			e.visit(this);
			//TODO : faire un test ici ?
		}
		return new JSG_2_TypeRuleResult(l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_Assignment jsg_2_Assignment) {
		int l = jsg_2_Assignment.getLine();
		int c = jsg_2_Assignment.getColumn();
		JSG_2_Type left = jsg_2_Assignment.getVariable().visit(this);
		JSG_2_Type right = jsg_2_Assignment.getValue().visit(this);
		if(!sameType(left, right)) {
			JSErrorEnumType criticality = JSErrorEnumType.CRITICAL;
			if(left==null || right == null) {
				criticality = JSErrorEnumType.INFO;
			}
			errorList.add(new JSError("Wrong type : assignement of type '"+printType(right)+"'in variable of type '"+printType(right)+"'", l, c, criticality));
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_AssocParam jsg_2_AssocParam) {
		int l = jsg_2_AssocParam.getLine();
		int c = jsg_2_AssocParam.getColumn();
		JSG_2_Rule rule = jsg_2_AssocParam.getRule();
		
		JSG_2_Type typeValue = jsg_2_AssocParam.getParamValue().visit(this);
		
		if(rule!=null) {
			JMERule jmeRule = rule.getRule();
			if(jmeRule!=null) {
				boolean found = false;
				for(JMEParamEbd e : jmeRule.getParamsEbd()) {
					if(e.getName().compareTo(jsg_2_AssocParam.getParamName())==0) {
						found = true;
						JSG_2_Type paramType = context.getType(new JSG_2_EbdParam(e, e.getName(),l,c), glue);
						if(!sameType(paramType, typeValue)) {
							JSErrorEnumType criticality = JSErrorEnumType.CRITICAL;
							if(paramType==null || typeValue == null) {
								criticality = JSErrorEnumType.INFO;
							}
							errorList.add(new JSError("Wrong type : assignement of type '"+printType(typeValue)+"' for rule parameter named '"+e.getName()+"', expected type was '"+printType(paramType)+"'", l, c, criticality));
						}
					}
				}
				if(!found) {
					errorList.add(new JSError("Rule parmeter name "+ jsg_2_AssocParam.getParamName() +" not found", l, c, JSErrorEnumType.CRITICAL));
				}
			}
		}
		
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_AtLang jsg_2_AtLang) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Block jsg_2_Block) {
		context.beginBlock();
		jsg_2_Block.getBody().visit(this);
		context.endBlock();
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Boolean jsg_2_Boolean) {
		return new JSG_2_TypeBoolean(jsg_2_Boolean.getLine(), jsg_2_Boolean.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Break jsg_2_Break) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Call jsg_2_Call) {
		// TODO : tester si la fonction est def dans le modeleur
		return context.getType(jsg_2_Call, glue);
	}

	@Override
	public JSG_2_Type accept(JSG_2_CallListSize jsg_2_CallListSize) {
		return new JSG_2_Type(JSG_2_Type.INT,jsg_2_CallListSize.getLine(), jsg_2_CallListSize.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_CallRuleResHeight jsg_2_CallRuleResHeight) {
		return new JSG_2_Type(JSG_2_Type.INT,jsg_2_CallRuleResHeight.getLine(), jsg_2_CallRuleResHeight.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_CallRuleResWidth jsg_2_CallRuleResWidth) {
		return new JSG_2_Type(JSG_2_Type.INT,jsg_2_CallRuleResWidth.getLine(), jsg_2_CallRuleResWidth.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Cast jsg_2_Cast) {
		jsg_2_Cast.getExpr().visit(this);
		return jsg_2_Cast.getType();
	}

	@Override
	public JSG_2_Type accept(JSG_2_Catch jsg_2_Catch) {
		jsg_2_Catch.getBlock().visit(this);
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Choice jsg_2_Choice) {
		for(JSG_2_Entity e : jsg_2_Choice.getOptions()) {
			e.visit(this);
		}
		return new JSG_2_TypeRuleResult(jsg_2_Choice.getLine(), jsg_2_Choice.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Collect jsg_2_Collect) {
		int l = jsg_2_Collect.getLine();
		int c = jsg_2_Collect.getColumn();
		return new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_CollectEbd jsg_2_CollectEbd) {
		int l = jsg_2_CollectEbd.getLine();
		int c = jsg_2_CollectEbd.getColumn();
		JMEEmbeddingInfo einf = context.getModeler().getEmbedding(jsg_2_CollectEbd.getEmbedding());
		if(einf !=null) {
			return new JSG_2_TypeList(context.getType(einf,l,c), l, c);
		}else {
			return new JSG_2_TypeList(null, l, c);
		}
	}

	@Override
	public JSG_2_Type accept(JSG_2_Comment jsg_2_Comment) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Constructor jsg_2_Constructor) {
		for(JSG_2_Entity e : jsg_2_Constructor.getArguments()) {
			e.visit(this);
		}
//		if(jsg_2_Constructor.getName() instanceof JSG_2_Type) {
//			return (JSG_2_Type)jsg_2_Constructor.getName();
//		}else {
			return context.getType(jsg_2_Constructor, glue);
			//return new JSG_2_Type(jsg_2_Constructor.getName().toString(),jsg_2_Constructor.getLine(), jsg_2_Constructor.getColumn());
//		}
	}

	@Override
	public JSG_2_Type accept(JSG_2_Continue jsg_2_Continue) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Declare jsg_2_Declare) {
		int l = jsg_2_Declare.getLine();
		int c = jsg_2_Declare.getColumn();
		if(jsg_2_Declare.getValue()!=null) {
			JSG_2_Type valueType = jsg_2_Declare.getValue().visit(this);
			if(!sameType(valueType, jsg_2_Declare.getType())) {
				JSErrorEnumType criticality = JSErrorEnumType.CRITICAL;
				if(valueType==null || jsg_2_Declare.getType() == null) {
					criticality = JSErrorEnumType.INFO;
				}
				errorList.add(new JSError("Wrong type : assignement of type '"+printType(valueType)+"' in variable of type '"+printType(jsg_2_Declare.getType())+"' val is " + jsg_2_Declare.getValue(), l, c, criticality));	
			}
		}
		if(context.varExists(jsg_2_Declare.getName())) {
			errorList.add(new JSError("Conflict name : declaration of variable '"+jsg_2_Declare.getName()+"' with name allready used" , l, c, JSErrorEnumType.CRITICAL));	
		}else {
			context.assignVar(jsg_2_Declare.getName(), jsg_2_Declare.getType(), jsg_2_Declare.getValue());
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_DeclareFunction jsg_2_DeclareFunction) {
		for(JSG_2_Entity e : jsg_2_DeclareFunction.getArguments()) {
			e.visit(this);
		}
		jsg_2_DeclareFunction.getBlock().visit(this);
		// TODO : tester return type
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_DeclareMark jsg_2_DeclareMark) {
		context.assignVar(jsg_2_DeclareMark.getName(), new JSG_2_TypeMark(jsg_2_DeclareMark.getLine(), jsg_2_DeclareMark.getColumn()), null);		
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Delete jsg_2_Delete) {
		int l = jsg_2_Delete.getLine();
		int c = jsg_2_Delete.getColumn();
		JSG_2_Type valueType = jsg_2_Delete.getName().visit(this);
		if(! context.isAPointerType(valueType, glue) && !(valueType instanceof JSG_2_TypeMark) ) {
			errorList.add(new JSError("Non deletable object : '"+jsg_2_Delete.getName()+"' with type '" + printType(valueType)+"'", l, c, JSErrorEnumType.CRITICAL));	
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Double jsg_2_Double) {
		return new JSG_2_Type(JSG_2_Type.DOUBLE, jsg_2_Double.getLine(), jsg_2_Double.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_DoWhile jsg_2_DoWhile) {
		jsg_2_DoWhile.getBody().visit(this);
		jsg_2_DoWhile.getCondition().visit(this);
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_EbdParam jsg_2_Variable) {
		return context.getType(jsg_2_Variable, glue);
	}

	@Override
	public JSG_2_Type accept(JSG_2_Float jsg_2_Float) {
		return new JSG_2_Type(JSG_2_Type.FLOAT, jsg_2_Float.getLine(), jsg_2_Float.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_For jsg_2_For) {
		int l = jsg_2_For.getLine();
		int c = jsg_2_For.getColumn();
		context.beginBlock();
		if(context.varExists(jsg_2_For.getVariable())) {
			errorList.add(new JSError("Conflict name : declaration of variable '"+jsg_2_For.getVariable()+"' with name allready used" , l, c, JSErrorEnumType.INFO));	
		}else{
			context.assignVar(jsg_2_For.getVariable(), jsg_2_For.getType(), null);
		}
		jsg_2_For.getStart().visit(this);
		jsg_2_For.getStep().visit(this);
		jsg_2_For.getEnd().visit(this);
		jsg_2_For.getBody().visit(this);
		context.endBlock();
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_ForEach jsg_2_ForEach) {
		int l = jsg_2_ForEach.getLine();
		int c = jsg_2_ForEach.getColumn();
		context.beginBlock();
		if(context.varExists(jsg_2_ForEach.getName())) {
			errorList.add(new JSError("Conflict name : declaration of variable '"+jsg_2_ForEach.getName()+"' with name allready used" , l, c, JSErrorEnumType.INFO));	
		}else{
			context.assignVar(jsg_2_ForEach.getName(), jsg_2_ForEach.getType(), null);
			System.out.println("declaring var " + jsg_2_ForEach.getName());
		}
		jsg_2_ForEach.getColl().visit(this);
		jsg_2_ForEach.getBody().visit(this);
		context.endBlock();
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_ForLoop jsg_2_ForLoop) {
//		int l = jsg_2_ForLoop.getLine();
//		int c = jsg_2_ForLoop.getColumn();
//		if(context.varExists(jsg_2_ForLoop.getVariable())) {
//			errorList.add(new JSError("Conflict name : declaration of variable '"+jsg_2_ForLoop.getVariable()+"' with name allready used" , l, c, JSErrorEnumType.INFO));	
//		}else{
//			context.assignVar(jsg_2_ForLoop.getVariable(), jsg_2_ForLoop.getType(), null);
//		}
		context.beginBlock();
		jsg_2_ForLoop.getInit().visit(this);
		jsg_2_ForLoop.getStep().visit(this);
		jsg_2_ForLoop.getCond().visit(this);
		jsg_2_ForLoop.getBody().visit(this);
		context.endBlock();
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_FreeMarker jsg_2_FreeMarker) {
		int l = jsg_2_FreeMarker.getLine();
		int c = jsg_2_FreeMarker.getColumn();
		JSG_2_Type markType = jsg_2_FreeMarker.getMarker().visit(this);
		if( !(markType instanceof JSG_2_TypeMark) ) {
			errorList.add(new JSError("Undeclared mark : trying to free a mark but found element of type '"+printType(markType)+"'" , l, c, JSErrorEnumType.INFO));	
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_GetDartId jsg_2_GetNodeId) {
		int l = jsg_2_GetNodeId.getLine();
		int c = jsg_2_GetNodeId.getColumn();
		return new JSG_2_Type(JSG_2_Type.INT, l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_GetEbd jsg_2_GetEbd) {
		int l = jsg_2_GetEbd.getLine();
		int c = jsg_2_GetEbd.getColumn();
		JSG_2_Type varType = jsg_2_GetEbd.getLeft().visit(this);
		if( !(varType instanceof JSG_2_TypeDart) ){
			errorList.add(new JSError("Wrong type : trying to access embedding value on non dart element. Type found is '"+printType(varType)+"' value is " + jsg_2_GetEbd.getLeft() , l, c, JSErrorEnumType.CRITICAL));				
		}
		return context.getType(jsg_2_GetEbd.getEbdInfo(), l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_GetEbdId jsg_2_GetEbdId) {
		int l = jsg_2_GetEbdId.getLine();
		int c = jsg_2_GetEbdId.getColumn();
		if(jsg_2_GetEbdId.getEbdInfo() == null) {
			errorList.add(new JSError("Unable to get embedding id on unknown embedding" , l, c, JSErrorEnumType.CRITICAL));	
		}
		return new JSG_2_Type(JSG_2_Type.INT, l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_GetEbdName jsg_2_GetEbdName) {
		int l = jsg_2_GetEbdName.getLine();
		int c = jsg_2_GetEbdName.getColumn();
		if(jsg_2_GetEbdName.getEbdInfo() == null) {
			errorList.add(new JSError("Unable to get embedding name on unknown embedding" , l, c, JSErrorEnumType.CRITICAL));	
		}
		return new JSG_2_TypeString(l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_GetEbdOrbit jsg_2_GetEbdOrbit) {
		int l = jsg_2_GetEbdOrbit.getLine();
		int c = jsg_2_GetEbdOrbit.getColumn();
		if(jsg_2_GetEbdOrbit.getEbdInfo() == null) {
			errorList.add(new JSError("Unable to get embedding orbit on unknown embedding" , l, c, JSErrorEnumType.CRITICAL));	
		}
		return new JSG_2_TypeOrbit(l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_GetMarker jsg_2_GetMarker) {
		return new JSG_2_TypeMark(jsg_2_GetMarker.getLine(), jsg_2_GetMarker.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_GMapSize jsg_2_GMapSize) {
		return new JSG_2_Type(JSG_2_Type.INT, jsg_2_GMapSize.getLine(), jsg_2_GMapSize.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Header jsg_2_Header) {
		// TODO : pas de verif ici
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_If jsg_2_If) {
		jsg_2_If.getCondition().visit(this);
		jsg_2_If.getConsequence().visit(this);
		if(jsg_2_If.getAlternant()!=null)
			jsg_2_If.getAlternant().visit(this);
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Index jsg_2_Index) {
		// TODO : verifier l'indexabilite de l'elt
		JSG_2_Type varType = jsg_2_Index.getVariable().visit(this);
		jsg_2_Index.getIndex().visit(this);
		if(varType instanceof JSG_2_TypeList) {
			return ((JSG_2_TypeList)varType).getListContentType();
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_IndexInJerboaType jsg_2_IndexInJerboaType) {
		return context.getType(jsg_2_IndexInJerboaType, glue);
	}

	@Override
	public JSG_2_Type accept(JSG_2_Indirection jsg_2_Indirection) {
		JSG_2_Type varType = jsg_2_Indirection.getExp().visit(this);
		if(varType instanceof JSG_2_TypePointer) {
			return ((JSG_2_TypePointer)varType).getType();
		}
		return varType;
	}

	@Override
	public JSG_2_Type accept(JSG_2_InScope jsg_2_InScope) {
		// TODO : c'est pas ca mais bon
		int l = jsg_2_InScope.getLeft().getLine();
		int c = jsg_2_InScope.getLeft().getColumn();
		
//		JSG_2_Type lType = 
		jsg_2_InScope.getLeft().visit(this);
//		JSG_2_Type rType = 
		jsg_2_InScope.getRight().visit(this);
		if(jsg_2_InScope.getRight() instanceof JSG_2_Variable) {
			JSG_2_Variable var = (JSG_2_Variable) jsg_2_InScope.getRight();
			for(JMEEmbeddingInfo einf : context.getModeler().getEmbeddings()) {
				if(einf.getName().compareTo(var.getName())==0) {				
					errorList.add(new JSError("Dot expression done with a name that match an embedding. "
							+ "As default this expression is translated in an embedding getting on a dart type."
							+ "If it is not an embedding getting, please cast the left part of the dot in the specific type." , 
							l, c, JSErrorEnumType.WARNING));	
				}
			}
		}
		
		
		return context.getType(jsg_2_InScope, glue);
	}

	@Override
	public JSG_2_Type accept(JSG_2_Integer jsg_2_Integer) {
		return new JSG_2_Type(JSG_2_Type.INT, jsg_2_Integer.getLine(), jsg_2_Integer.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_IsMarked jsg_2_IsMarked) {
		return new JSG_2_TypeBoolean(jsg_2_IsMarked.getLine(), jsg_2_IsMarked.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_IsNotMarked jsg_2_IsNotMarked) {
		return new JSG_2_TypeBoolean(jsg_2_IsNotMarked.getLine(), jsg_2_IsNotMarked.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_JerboaKeyword jsg_2_JerboaKeyword) {
		int l = jsg_2_JerboaKeyword.getLine();
		int c = jsg_2_JerboaKeyword.getColumn();
		switch (jsg_2_JerboaKeyword.getType()) {
		case DIMENSION:
			return new JSG_2_Type(JSG_2_Type.INT, l, c);
		case GMAP:
			return new JSG_2_TypeGmap(l, c);
		case RIGHTPATTERN:
		case LEFTPATTERN:
			return new JSG_2_TypeList(new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c), l, c);
		case MODELER:
			return new JSG_2_TypeModeler(l, c);
		case HOOK: 
			return new JSG_2_TypeHookList(null, l, c);
		default:
			break;
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(@SuppressWarnings("rawtypes") JSG_2_Literal jsg_2_Literal) {
		// TODO : faire un getType sur n'importe quel objet
		return new JSG_2_Type(jsg_2_Literal.getClass().getCanonicalName(), jsg_2_Literal.getLine(), jsg_2_Literal.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Long jsg_2_Long) {
		return new JSG_2_Type(JSG_2_Type.LONG, jsg_2_Long.getLine(), jsg_2_Long.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Map jsg_2_Map) {
		jsg_2_Map.getExpr().visit(this);
		jsg_2_Map.getBody().visit(this);
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Mark jsg_2_Mark) {
		int l = jsg_2_Mark.getLine();
		int c = jsg_2_Mark.getColumn();
		if (jsg_2_Mark.getLeft() instanceof JSG_2_Collect) {
			((JSG_2_Collect)jsg_2_Mark.getLeft()).visit(this);
		}else {
			JSG_2_Type t = jsg_2_Mark.getLeft().visit(this);
			if(!(t instanceof JSG_2_TypeDart)) {
				errorList.add(new JSError("Collect done of non dart element but on '"+printType(t)+"' " , l, c, JSErrorEnumType.CRITICAL));	
			}
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_New jsg_2_New) {
		return jsg_2_New.getExp().visit(this);
	}

	@Override
	public JSG_2_Type accept(JSG_2_NOP jsg_2_NOP) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Not jsg_2_Not) {
		jsg_2_Not.getExpr().visit(this);
		return new JSG_2_TypeBoolean(jsg_2_Not.getLine(), jsg_2_Not.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Null jsg_2_Null) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Operator jsg_2_Operator) {
		for(JSG_2_Entity e : jsg_2_Operator.getOperands()) {
			if(e!=null)
				e.visit(this);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public JSG_2_Type accept(JSG_2_Orbit jsg_2_Orbit) {
		int l = jsg_2_Orbit.getLine();
		int c = jsg_2_Orbit.getColumn();
		for(JSG_2_Entity e : jsg_2_Orbit.getDimensions()){
			int value = 0;
			if(e instanceof JSG_2_Literal) {
				Object dim = ((JSG_2_Literal)e).getValue();
				if(dim instanceof Integer) {
					value = ((Integer)dim).intValue();
				}else if(dim instanceof Long) {
					value = ((Long)dim).intValue();
				}else if(dim instanceof Float) {
					value = ((Float)dim).intValue();
				}else if(dim instanceof Double) {
					value = ((Float)dim).intValue();
				}
				
			}else if(e instanceof JSG_2_Integer){
				value = ((JSG_2_Integer)e).getValue();
			}else if(e instanceof JSG_2_Long){
				value = ((JSG_2_Integer)e).getValue();
			}else if(e instanceof JSG_2_Double){
				value = ((JSG_2_Integer)e).getValue();
			}else if(e instanceof JSG_2_Float){
				value = ((JSG_2_Integer)e).getValue();
			}else {
				e.visit(this);
			}
			if(value<0) {
				errorList.add(new JSError("Orbit dimension is below 0 '"+e+"' " , l, c, JSErrorEnumType.CRITICAL));	
			}else if(value>context.getModeler().getDimension()) {
				errorList.add(new JSError("Orbit dimension is higher than modeler dimension '"+e+"' " , l, c, JSErrorEnumType.CRITICAL));	
			}
		}
		return new JSG_2_TypeOrbit(l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_PackagedType jsg_2_PackagedType) {
		return jsg_2_PackagedType;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Print jsg_2_Print) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Return jsg_2_Return) {
		return jsg_2_Return.getExpression().visit(this);
	}

	@Override
	public JSG_2_Type accept(JSG_2_Rule jsg_2_Rule) {
		return new JSG_2_TypeRule(jsg_2_Rule.getLine(), jsg_2_Rule.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_RuleArg jsg_2_RuleArg) {
		jsg_2_RuleArg.getArgValue().visit(this); // TODO : on a pas la regle pour tester
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_RuleNodeId jsg_2_RuleNode) {
		int l = jsg_2_RuleNode.getLine();
		int c = jsg_2_RuleNode.getColumn();
		JMERule rule = jsg_2_RuleNode.getRule();
		JMEGraph graph = jsg_2_RuleNode.getGraphside() == GraphSide.LEFT ? rule.getLeft() : rule.getRight();
		if(graph.searchNodeByName(jsg_2_RuleNode.getNodeName())==null) {
			errorList.add(new JSError("Non existing node named "+ jsg_2_RuleNode.getNodeName() + " in rule '"+rule.getName()+"' " , l, c, JSErrorEnumType.CRITICAL));	
		}
		return new JSG_2_TypeDart(l, c);
	}

	@Override
	public JSG_2_Type accept(JSG_2_Sequence jsg_2_Sequence) {
		for(JSG_2_Entity e : jsg_2_Sequence) {
			e.visit(this);
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_String jsg_2_String) {
		return new JSG_2_TypeString(jsg_2_String.getLine(), jsg_2_String.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Throw jsg_2_Throw) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Try jsg_2_Try) {
		jsg_2_Try.getTryBlock().visit(this);
		for(JSG_2_Entity e : jsg_2_Try.getCatchList()) {
			e.visit(this);
		}
		if(jsg_2_Try.getFinallyBlock()!=null) {
			jsg_2_Try.getFinallyBlock().visit(this);
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Type jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeBoolean jsg_2_TypeBoolean) {
		return jsg_2_TypeBoolean;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeDart jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeEmbedding jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeGmap jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeHookList jsg_2_TypeHookList) {
		return jsg_2_TypeHookList;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeList jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeMark jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeModeler jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeOrbit jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypePointer jsg_2_TypePointer) {
		return jsg_2_TypePointer;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeRule jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeRuleResult jsg_2_Type) {
		return jsg_2_Type;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeString jsg_2_TypeString) {
		return jsg_2_TypeString;
	}

	@Override
	public JSG_2_Type accept(JSG_2_TypeTemplate jsg_2_Type) {
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_UnMark jsg_2_UnMark) {
		int l = jsg_2_UnMark.getLine();
		int c = jsg_2_UnMark.getColumn();
		if (jsg_2_UnMark.getLeft() instanceof JSG_2_Collect) {
			((JSG_2_Collect)jsg_2_UnMark.getLeft()).visit(this);
		}else {
			JSG_2_Type t = jsg_2_UnMark.getLeft().visit(this);
			if(!(t instanceof JSG_2_TypeDart)) {
				errorList.add(new JSError("Collect done of non dart element but on '"+printType(t)+"' " , l, c, JSErrorEnumType.CRITICAL));	
			}
		}
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_Unreference jsg_2_Unreference) {
		return new JSG_2_TypePointer(jsg_2_Unreference.getExp().visit(this), jsg_2_Unreference.getLine(), jsg_2_Unreference.getColumn());
	}

	@Override
	public JSG_2_Type accept(JSG_2_Variable jsg_2_Variable) {
		int l = jsg_2_Variable.getLine();
		int c = jsg_2_Variable.getColumn();
		JMERule rule = glue.getCurrentRule();
		if(rule!=null) {
			for(JMENode hook : rule.getLeft().getHooks()) {
				if(hook.getName().compareTo(jsg_2_Variable.getName())==0) {
					if(glue.getLangagesState()== LanguageState.CLASSICAL && glue.getLangageType()==LanguageType.SCRIPT) {
						return new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c);
					}else {
						return new JSG_2_TypeDart(l, c);
					}
				}
			}
			for(JMEParamEbd ebd: rule.getParamsEbd()) {
				if(ebd.getName().compareTo(jsg_2_Variable.getName())==0) {
					return context.getType(new JSG_2_EbdParam(ebd, ebd.getName(), l, c) , glue);
				}
			}
		}
		if(context.varExists(jsg_2_Variable.getName())) {			
			return context.var(jsg_2_Variable.getName()).get(0).l();
		}
		errorList.add(new JSError("Non existing variable '"+jsg_2_Variable.getName()+"' " , l, c, JSErrorEnumType.CRITICAL));	
		return null;
	}

	@Override
	public JSG_2_Type accept(JSG_2_While jsg_2_While) {
		jsg_2_While.getCondition().visit(this);
		jsg_2_While.getBody().visit(this);
		return null;
	}

	
}
