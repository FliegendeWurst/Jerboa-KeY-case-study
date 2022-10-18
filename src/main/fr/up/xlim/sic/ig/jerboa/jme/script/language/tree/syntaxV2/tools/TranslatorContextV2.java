package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Alpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_ApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Collect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_CollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_EbdParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_GetEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_IsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_IsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_JerboaKeyword;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Rule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleNodeId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_InScope;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_Index;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_IndexInJerboaType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Boolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Cast;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Double;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Float;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Indirection;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Integer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Long;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_New;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Not;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Null;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Operator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_String;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Variable;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_Call;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_Constructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetDartId;
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
import up.jerboa.core.util.Pair;

public class TranslatorContextV2 {

	JMEModeler modeler; 

	private HashMap<String, ArrayList<Pair<JSG_2_Type, JSG_2_Entity>>> mapVariableToTypeNValue;

	private Stack<ArrayList<String>> varDeclaredPerBlock;

	private Stack<ArrayList<JSG_2_Entity>> instructionToAddAtBlockEnd;

	private boolean isInScope, gmapHasDirectAccess;
	private JSG_2_Entity currentAssignementVar;


	public TranslatorContextV2(JMEModeler modeler) {
		this.modeler = modeler;
		mapVariableToTypeNValue = new HashMap<>();
		varDeclaredPerBlock = new Stack<ArrayList<String>>();
		varDeclaredPerBlock.push(new ArrayList<>()); // On en mets un par dÃ©faut pour le block courant
		instructionToAddAtBlockEnd = new Stack<ArrayList<JSG_2_Entity>>();

		isInScope = false;
		gmapHasDirectAccess = false;
		currentAssignementVar = null;
	}

	@SuppressWarnings("unchecked")
	public TranslatorContextV2(TranslatorContextV2 context) {
		this.modeler = context.modeler;
		mapVariableToTypeNValue = new HashMap<>();
		for(String k : context.mapVariableToTypeNValue.keySet()) {
			ArrayList<Pair<JSG_2_Type, JSG_2_Entity>> list = new ArrayList<Pair<JSG_2_Type,JSG_2_Entity>>();
			for(Pair<JSG_2_Type, JSG_2_Entity> pair: context.mapVariableToTypeNValue.get(k)) {
				list.add(new Pair<JSG_2_Type, JSG_2_Entity>(pair.l(), pair.r()));
			}
			mapVariableToTypeNValue.put(k, list);
		}

		varDeclaredPerBlock = new Stack<ArrayList<String>>();
		for(ArrayList<String> l : context.varDeclaredPerBlock) {
			varDeclaredPerBlock.add((ArrayList<String>) l.clone());
		}

		instructionToAddAtBlockEnd = new Stack<ArrayList<JSG_2_Entity>>();
		for(ArrayList<JSG_2_Entity> l : context.instructionToAddAtBlockEnd) {
			instructionToAddAtBlockEnd.add((ArrayList<JSG_2_Entity>) l.clone());
		}

		isInScope = context.isInScope;
		gmapHasDirectAccess = context.gmapHasDirectAccess;
		currentAssignementVar = context.currentAssignementVar;		
	}

	@SuppressWarnings("unchecked")
	public void enrichContext(TranslatorContextV2 context) {
		for(String k : context.mapVariableToTypeNValue.keySet()) {
			ArrayList<Pair<JSG_2_Type, JSG_2_Entity>> list = new ArrayList<Pair<JSG_2_Type,JSG_2_Entity>>();
			for(Pair<JSG_2_Type, JSG_2_Entity> pair: context.mapVariableToTypeNValue.get(k)) {
				list.add(new Pair<JSG_2_Type, JSG_2_Entity>(pair.l(), pair.r()));
			}
			mapVariableToTypeNValue.put(k, list);
		}

		for(ArrayList<String> l : context.varDeclaredPerBlock) {
			varDeclaredPerBlock.add((ArrayList<String>) l.clone());
		}

		for(ArrayList<JSG_2_Entity> l : context.instructionToAddAtBlockEnd) {
			instructionToAddAtBlockEnd.add((ArrayList<JSG_2_Entity>) l.clone());
		}
	}

	public ArrayList<Pair<JSG_2_Type, JSG_2_Entity>> var(String name) {
		if (mapVariableToTypeNValue.containsKey(name)) {
			return mapVariableToTypeNValue.get(name);
		}
		return null;
	}

	public Pair<JSG_2_Type, JSG_2_Entity> varLastValue(String name) {
		if (mapVariableToTypeNValue.containsKey(name)) {
			return mapVariableToTypeNValue.get(name).get(mapVariableToTypeNValue.get(name).size() - 1);
		}
		return null;
	}

	public void beginBlock() {
		varDeclaredPerBlock.push(new ArrayList<>());
	}

	public void endBlock(){
		varDeclaredPerBlock.pop();
	}

	public Stack<ArrayList<String>> getAccessibleVariable(){
		Stack<ArrayList<String>> stack = new Stack<>();
		for (ArrayList<String> s : varDeclaredPerBlock) {
			ArrayList<String> tmp = new ArrayList<>();
			tmp.addAll(s);
			stack.push(tmp);
		}
		return stack;
	}

	public void assignVar(String name, JSG_2_Type type, JSG_2_Entity value) {
		varDeclaredPerBlock.peek().add(name);
		if(!mapVariableToTypeNValue.containsKey(name)){
			mapVariableToTypeNValue.put(name, new ArrayList<>());
		}
		mapVariableToTypeNValue.get(name).add(new Pair<JSG_2_Type, JSG_2_Entity>(type, value));
	}

	public boolean varExists(String name) {
		return mapVariableToTypeNValue.containsKey(name);
	}

	public void addBlockEndInstruction(JSG_2_Entity inst) {
		if(instructionToAddAtBlockEnd.size()>0) {
			instructionToAddAtBlockEnd.get(instructionToAddAtBlockEnd.size()-1).add(inst);
		}
	}

	public boolean isInScope() {
		return isInScope;
	}

	public void setInScope(boolean isInScope) {
		this.isInScope = isInScope;
	}

	public boolean isGmapHasDirectAccess() {
		return gmapHasDirectAccess;
	}

	public void setGmapHasDirectAccess(boolean gmapHasDirectAccess) {
		this.gmapHasDirectAccess = gmapHasDirectAccess;
	}

	public JSG_2_Entity getCurrentAssignementVarName() {
		return currentAssignementVar;
	}

	public void setCurrentAssignementVarName(JSG_2_Entity currentAssignementVar) {
		this.currentAssignementVar = currentAssignementVar;
	}

	public JMEModeler getModeler() {
		return modeler;
	}

	public JSG_2_Type getType(JMEEmbeddingInfo e, int l, int c) {
		String t =e.getType();
		if(t.contains("@ebd<")) {
			String ebdName = t.substring(5, t.length()-1);
			JMEEmbeddingInfo embi = modeler.getEmbedding(ebdName);
			if(embi!=null) {
				//				System.out.println("#TranslatorContextV2 @getType : searching ebd name OK : '"+ebdName+"'" );
				return new JSG_2_TypeEmbedding(embi.getName(), l, c);
			}else {
				System.err.println("#TranslatorContextV2 @getType : fail searching ebd name'"+ebdName+"'" );
			}
		}else {
			return new JSG_2_Type(t, l, c);
		}
		return null;
	}

	public JSG_2_Type getType(JSG_2_Entity e, LanguageGlue glue) {
		if(e!=null) {
			int l = e.getLine();
			int c = e.getColumn();

			if(e instanceof JSG_2_Alpha || e instanceof JSG_2_RuleNodeId) {
				return new JSG_2_TypeDart(l, c);

			}else if(e instanceof JSG_2_Type) {
				return (JSG_2_Type) e;

			}else if(e instanceof JSG_2_EbdParam) {
				if(((JSG_2_EbdParam)e).getEbdParam()==null) {
					System.out.println("#TranslatorContextV2 @getType : null EbdParam " );
					return new JSG_2_TypeEmbedding("",l, c);
				}else{
					String t = ((JSG_2_EbdParam)e).getEbdParam().getType();
					if(t.contains("@ebd<")) {
						String ebdName = t.substring(5, t.length()-1);
						JMEEmbeddingInfo einf = modeler.getEmbedding(ebdName);
						if(einf!=null) {
							//							System.out.println("#TranslatorContextV2 @getType : searching ebd name OK : '"+ebdName+"'" );
							return new JSG_2_TypeEmbedding(einf.getName(), l, c);
						}else {
							System.err.println("#TranslatorContextV2 @getType : fail searching ebd name'"+ebdName+"'" );
						}
					}
				}
			}else if(e instanceof JSG_2_Call) {
				if(((JSG_2_Call)e).getName()==null) {
					System.out.println("#TranslatorContextV2 @getType : null function call name " );
					return null;
				}else {
					String t = ((JSG_2_Call)e).getName();
					if(t.contains("@ebd<")) {
						String ebdName = t.substring(5, t.length()-1);
						JMEEmbeddingInfo einf = modeler.getEmbedding(ebdName);
						if(einf!=null) {
							//							System.out.println("#TranslatorContextV2 @getType : searching ebd name OK : '"+ebdName+"'" );
							return new JSG_2_TypeEmbedding(einf.getName(), l, c);
						}
					}else{
						for(JMEEmbeddingInfo einf: modeler.getEmbeddings()) {
							if(t.compareTo(einf.getType())==0) {
								System.out.println("#TranslatorContextV2 @getType : function translated in constructor : '"+t+"'" );
								return new JSG_2_TypeEmbedding(einf.getName(), l, c);
							}
						}
					}
				}
			}else if(e instanceof JSG_2_Constructor) {
				//TODO : a revoir!
				//				System.err.println("ERROR In translatorContext@getType(JSG_2_Constructor)");
				return null;

			}else if(e instanceof JSG_2_Variable) {
				JSG_2_Variable var = ((JSG_2_Variable)e);
				if(varExists(var.getName())) {
					ArrayList<Pair<JSG_2_Type, JSG_2_Entity>> varTypes = var(var.getName());
					if(varTypes.size()>0) {
						return varTypes.get(varTypes.size()-1).l();
					}
				}else {
					System.err.println("#TranslatorContextV2 @getType : no accessible variable named'"+var.getName()+"'" );
				}
			}else if(e instanceof JSG_2_GetEbd) {
				JSG_2_GetEbd var = ((JSG_2_GetEbd)e);
				return new JSG_2_TypeEmbedding(var.getEbdInfo().getName(), var.getLine(), var.getColumn());

			}else if(e instanceof JSG_2_Collect){
				return new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c);

			}else if(e instanceof JSG_2_CollectEbd){
				JMEEmbeddingInfo einf = modeler.getEmbedding(((JSG_2_CollectEbd)e).getEmbedding());
				return new JSG_2_TypeList(new JSG_2_TypeEmbedding(einf.getName(), l, c), l, c);

			}else if(e instanceof JSG_2_Integer
					|| e instanceof JSG_2_RuleNodeId
					|| e instanceof JSG_2_GetDartId){
				return new JSG_2_Type("Integer", l, c);

			}else if(e instanceof JSG_2_Float){
				return new JSG_2_Type("Float", l, c);

			}else if(e instanceof JSG_2_Double){
				return new JSG_2_Type("Double", l, c);

			}else if(e instanceof JSG_2_Long){
				return new JSG_2_Type("Long", l, c);

			}else if(e instanceof JSG_2_JerboaKeyword){
				switch ( ((JSG_2_JerboaKeyword)e).getType()) {
				case DIMENSION:
					return new JSG_2_Type("Integer", l, c);
				case GMAP:
					return new JSG_2_TypeGmap(l, c);
				case HOOK:

					break;
				case LEFTPATTERN:
					return new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c);
				case MODELER:
					return new JSG_2_TypeModeler(l, c);
				case RIGHTPATTERN:
					return new JSG_2_TypeList(new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c), l, c);
				default:
					break;
				}

			}else if(e instanceof JSG_2_Index) {
				JSG_2_Type typeLeft = getType(((JSG_2_Index)e).getVariable(), glue);
				if(typeLeft instanceof JSG_2_TypeList) {
					return ((JSG_2_TypeList)typeLeft).getListContentType();
				}else if(typeLeft instanceof JSG_2_TypeGmap) {
					return new JSG_2_TypeDart(l, c);				
				}else if(typeLeft instanceof JSG_2_TypeRuleResult) {
					return new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c);
				}

			}else if(e instanceof JSG_2_New) {
				return getType(((JSG_2_New)e).getExp(), glue);

			}else if(e instanceof JSG_2_Rule) {
				return new JSG_2_TypeRule(l, c);

			}else if(e instanceof JSG_2_ApplyRule) {
				return new JSG_2_TypeRuleResult(l, c);

			}else if(e instanceof JSG_2_Constructor) {
				return getType(((JSG_2_Constructor)e).getName(), glue);

			}else if(e instanceof JSG_2_IndexInJerboaType) {
				JSG_2_IndexInJerboaType indexer = (JSG_2_IndexInJerboaType) e;

				switch (indexer.getType()) {
				case GMAP:
					return new JSG_2_TypeDart(l, c);
				case LEFT_PATTERN:
				case SCRIPT_HOOK:
					if(glue.getLangageType() == LanguageType.SCRIPT
					|| glue.getLangagesState() == LanguageState.MIDPROCESS 
					|| glue.getLangagesState() == LanguageState.PRECONDITION) {
						if(indexer.getArgList().size()<=1)
							return new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c);
					}
					return new JSG_2_TypeDart(l, c);
				case RIGHT_PATTERN:
					if(indexer.getArgList().size()<=1)
						return new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c);
					return new JSG_2_TypeDart(l, c);
				case RULE_RESULT:
					if(indexer.getArgList().size()>1) {
						return new JSG_2_TypeList(new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c), l, c);
					}else{
						return new JSG_2_TypeDart(l, c);
					}
				default:
					break;
				}

			}else if(e instanceof JSG_2_RuleNodeId) {
				return new JSG_2_Type("Integer", l, c);

			}else if(  e instanceof JSG_2_IsMarked
					|| e instanceof JSG_2_IsNotMarked
					|| e instanceof JSG_2_Not
					|| e instanceof JSG_2_Boolean){
				return new JSG_2_TypeBoolean(l, c);
			}else if(e instanceof JSG_2_Operator) {
				JSG_2_Operator operator = (JSG_2_Operator) e;

				switch (operator.getOperator()) {
				case AND:
				case OR:
				case DIFF:
				case GE:
				case GT:
				case EQUAL:
				case LT:
				case LE:
					return new JSG_2_TypeBoolean(l, c);
				default:
					if(operator.getOperands().size()>0) {
						return getType(operator.getOperands().get(0), glue);
					}else {
						System.err.println("#TranslatorContextV2 @getType : no type found for operator (no operand) : '" + e.getClass().getCanonicalName()+"' " + e);
					}
					break;
				}

			}else if(e instanceof JSG_2_InScope) {
				JSG_2_InScope scoper = (JSG_2_InScope) e;
				if(scoper.getRight() instanceof JSG_2_Variable) {
					JSG_2_Type leftType = getType(scoper.getLeft(), glue);
					System.err.println("#TranslatorContextV2 @getType : no type found for inscope : '" + e.getClass().getCanonicalName()+"' " + e
							+ " with left type : '" + (leftType!=null?leftType.getTypeName():"null") +"'");
				}else {
					// pour obtenir les getGmapSize etc
					// TODO : ce n'est pas parfait il faudrait verifier aussi le type gauche mais ça fait double check avec cette fonction
					return getType(scoper.getRight(), glue);
				}

			}else if(e instanceof JSG_2_Indirection) {
				JSG_2_Indirection eIndirect = (JSG_2_Indirection) e;
				JSG_2_Entity indirectExp = eIndirect.getExp();

				if(indirectExp instanceof JSG_2_TypePointer) {
					return ((JSG_2_TypePointer)indirectExp).getType();
				}else if(indirectExp instanceof JSG_2_Cast) {
					JSG_2_Type typeCaster = getType(indirectExp, glue);
					return getType((new JSG_2_Indirection(typeCaster, l, c)), glue);
					// TODO : c'est bizarre de faire l'indirect sur un type, il faudrait mieux tester .
					// en même temps, si c'est le user qui force le cast et qu'on a pas moyen de connaitre le type
					// c'est peut etre mieux ainsi
				}else {
					System.err.println("#TranslatorContextV2 @getType : no type found for JSG_2_Indirection with expression : '" 
							+ (indirectExp!=null?indirectExp.getClass().getCanonicalName():"null") 
							+ "' " + e);
				}

			}else if(e instanceof JSG_2_Cast) {
				return((JSG_2_Cast)e).getType();
			}else if(e instanceof JSG_2_String) {
				return new JSG_2_TypeString(l, c);
			}else if( !(e instanceof JSG_2_Null) ){
				System.err.println("#TranslatorContextV2 @getType : no type found for element : '" + e.getClass().getCanonicalName()+"' " + e);
			}

		}
		return null;
	}


	public boolean isAPointerType(JSG_2_Entity elt, LanguageGlue glue) {
		boolean isPointer = false;
		if(elt instanceof JSG_2_GetEbd) {
			isPointer = true;
		}else if(elt instanceof JSG_2_New) {
			isPointer = true;
		}else if(elt instanceof JSG_2_Variable) {
			JSG_2_Variable var = (JSG_2_Variable) elt;
			if(varExists(var.getName())) {
				for(Pair<JSG_2_Type, JSG_2_Entity> values : var(var.getName())) {
					isPointer = isPointer 
							|| isAPointerType(values.l(), glue)
							|| values.r() instanceof JSG_2_GetEbd
							|| values.r() instanceof JSG_2_New;
					// VAL : je ne rappelle pas la fonction @isAPointerType sur r()
					// car si la variable est assignee tq : a = a+1; on vas faire une recursion oo
				}
			}else {
				System.err.println("#TranslatorContextV2@isAPointerType : error, no variable declared name '"+var.getName()+"'");
			}


		}else if(elt instanceof JSG_2_Cast){
			JSG_2_Cast caster = (JSG_2_Cast) elt;
			isPointer = isAPointerType(caster.getType(), glue);
		}else {
			JSG_2_Type type = getType(elt, glue);
			isPointer = (type instanceof JSG_2_TypeModeler
					|| type instanceof JSG_2_TypeGmap
					|| type instanceof JSG_2_TypePointer
					|| type instanceof JSG_2_TypeRule
					|| type instanceof JSG_2_TypeDart
					|| type instanceof JSG_2_TypeRuleResult) 
					&& !(elt instanceof JSG_2_Indirection) 
					;
		}


		return isPointer;
	}

	public JSG_2_Type getTypeFromString(String typeName, int l, int c) {
		JSG_2_Type res = null;
		if (typeName.compareToIgnoreCase("JerboaRuleResult") == 0) {
			res = new JSG_2_TypeRuleResult(l, c);
		
		} else if (typeName.compareToIgnoreCase("JerboaDarts") == 0) {
			res = new JSG_2_TypeList(new JSG_2_TypeDart(l, c), l, c);
		
		} else if (typeName.compareToIgnoreCase("JerboaHooks") == 0
				|| typeName.compareToIgnoreCase("JerboaHookList") == 0) {
			res = new JSG_2_TypeHookList(null, l, c); // TODO: attention a la regle null
		
		} else if (typeName.compareToIgnoreCase("JerboaDart") == 0) {
			res = new JSG_2_TypeDart(l, c);
		
		} else if (typeName.compareToIgnoreCase("JerboaRule") == 0) {
			res = new JSG_2_TypeRule(l, c);
		
		} else if (typeName.compareToIgnoreCase("boolean") == 0
				|| typeName.compareToIgnoreCase("bool") == 0) {
			res = new JSG_2_TypeBoolean(l, c);
		
		} else if (typeName.compareToIgnoreCase("string") == 0) {
			res = new JSG_2_TypeString(l, c);
		
		} else if (typeName.compareToIgnoreCase("JerboaOrbit") == 0) {
			res = new JSG_2_TypeOrbit(l, c);
		
		} else if (typeName.compareToIgnoreCase("JerboaMark") == 0) {
			res = new JSG_2_TypeMark(l, c);
		
		} else if (typeName.compareToIgnoreCase("JerboaList") == 0) {
			res = new JSG_2_TypeList(null, l, c);
		
		} else if(typeName.contains("@ebd<")) {
			String ebdName = typeName.substring(5, typeName.length()-1);
			JMEEmbeddingInfo embi = modeler.getEmbedding(ebdName);
			if(embi!=null) {
				return new JSG_2_TypeEmbedding(embi.getName(), l, c);
			}else {
				System.err.println("#TranslatorContextV2 @getType : fail searching ebd name'"+ebdName+"'" );
			}
		}else if (typeName.compareTo("ArrayList") == 0) {
			// TODO : Il faudrait tester en fonction du langage ! pour éviter les conflicts de type !
			res = new JSG_2_TypeList(null, l, c);
		
		}else
			res = new JSG_2_Type(typeName, l, c);

		return res;
	}

	public static void main(String[] s) {

		// test pour voir si la copie Ã©tait inversÃ©e
		TranslatorContextV2 tc = new TranslatorContextV2(new JMEModeler("test", "", 3));
		tc.beginBlock();
		tc.assignVar("test1", null, null);
		tc.assignVar("test2", null, null);
		tc.beginBlock();
		tc.assignVar("test3", null, null);
		tc.assignVar("test4", null, null);
		System.err.println("## " + tc.getAccessibleVariable());
		System.err.println("#> " + tc.varDeclaredPerBlock);

		tc.endBlock();
		System.err.println("## " + tc.getAccessibleVariable());
		System.err.println("#> " + tc.varDeclaredPerBlock);
		tc.endBlock();
	}

	public boolean isPrimitvType(JSG_2_Type varType) {
		String typeName = varType.getTypeName();
		return varType instanceof JSG_2_TypeBoolean
				|| varType instanceof JSG_2_TypeString
				|| typeName.compareTo(JSG_2_Type.DOUBLE)== 0
				|| typeName.compareTo(JSG_2_Type.FLOAT) == 0
				|| typeName.compareTo(JSG_2_Type.INT) 	== 0
				|| typeName.compareTo(JSG_2_Type.LONG) 	== 0;
	}

}
