package fr.up.xlim.sic.ig.jerboa.jme.script.language.verification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction.JSSyntaxToSemantic_common;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_AddInHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_AddInList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Alpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_ApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Boolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Call;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallListSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallRuleResHeight;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallRuleResWidth;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Cast;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Choice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Collect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Comment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Constructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Double;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_EbdParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Float;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_FreeMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GMapSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetEbdId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetEbdName;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetEbdOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetNodeId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetTopoParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_InScope;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_InScopeStatic;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Index;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexInLeftPattern;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexInRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexNodeInGmap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexRuleNode;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Indirection;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Integer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordDimension;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordGmap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordHook;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordLeftFilter;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordRightFilter;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_LeftRuleNode;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_List;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Long;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_New;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Not;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Null;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Operator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Orbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_PackagedType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Rule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_RuleArg;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_RuleNode;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Size;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_String;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeBoolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeGMap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaDart;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypePrimitive;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeString;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeTemplate;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Unreference;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_UserType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Variable;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Assignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_AssocParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_AtLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Block;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Break;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Catch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ClearHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Continue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Declare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DeclareMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Delete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_For;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Header;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_HookCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_If;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Map;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Mark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_NOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Print;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Return;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Throw;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Try;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_UnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_While;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSOperatorKind;
import up.jerboa.core.util.Pair;

public class JSG_Verification
		implements JSG_ExprVisitor<JSG_Type, RuntimeException>, JSG_InstVisitor<JSG_Type, RuntimeException> {

	private ArrayList<JSError> errorList;

	private ArrayList<ArrayList<Pair<String, JSG_Type>>> variableList;

	private LanguageGlue glue;

	/**
	 * Stock a list of pair : <blockDepth ; TheReturn>
	 */
	private Map<Integer, JSG_Return> listReturnWithDepth = new HashMap<>();

	private JMEModeler modeler;

	// detecter le code mort en regardant si un return/break a dÃ©jÃ  Ã©tÃ©
	// appellÃ©
	// dans le block courant.

	private JSG_Type variableType(String v) {
		for (ArrayList<Pair<String, JSG_Type>> l : variableList) {
			for (Pair<String, JSG_Type> p : l) {
				//System.err.println(p + " -- " + p.l() + " -- " + p.r() + " >>> " + v);
				if (p != null && p.l().compareTo(v) == 0) {
					return p.r();
				}
			}
		}
		for (String vt : glue.getEbdParams()) {
			// System.err.println(vt + " - " + v);
			if (vt.compareTo(v) == 0) {
				JSSyntaxToSemantic_common translator = new JSSyntaxToSemantic_common(glue, modeler);
				return translator.accept(new JSType(glue.getEbdParamType(v), -1, -1));
			}
		}
		if (glue.getCurrentRule() != null) {
			for (JMEParamTopo tp : glue.getCurrentRule().getParamsTopo()) {
				if (tp.getName().compareTo(v) == 0) {
					if (glue.getCurrentRule() instanceof JMERuleAtomic) {
						return new JSG_TypeJerboaDart(-1, -1);
					} else
						return new JSG_TypeJerboaHookList(glue.getCurrentRule(), -1, -1);
				}
			}
		}
		return null;
	}

	public JSG_Verification(LanguageGlue _glue, JMEModeler mod) {
		glue = _glue;
		modeler = mod;
		errorList = new ArrayList<>();
		variableList = new ArrayList<>();
		variableList.add(new ArrayList<Pair<String, JSG_Type>>());
		variableList.get(0).add(new Pair<String, JSG_Type>("this", new JSG_TypeJerboaRule(glue.getCurrentRule().getName(), -1,-1)));
		listReturnWithDepth.clear();
	}

	public ArrayList<JSError> beginGeneration(JSG_Sequence js) {
		errorList.clear();
		variableList.clear();
		variableList.add(new ArrayList<Pair<String, JSG_Type>>());
		
		variableList.get(0).add(new Pair<String, JSG_Type>("this", new JSG_TypeJerboaRule(glue.getCurrentRule().getName(), -1,-1)));
		listReturnWithDepth.clear();
		errorList = new ArrayList<>();
		accept(js);
		return errorList;
	}

	public ArrayList<JSError> getErrors() {
		return errorList;
	}

	public boolean declareVariable(String name, JSG_Type type, JSEntity caller) {
		if (variableType(name) != null) {
			errorList.add(new JSError("conflict variable name: ", caller.getLine(), caller.getColumn(),
					JSErrorEnumType.CRITICAL));
			return false;
		} else {
			variableList.get(variableList.size() - 1).add(new Pair<String, JSG_Type>(name, type));
			return true;
		}
	}

	private boolean sameType(Object a, Object b) {
		if(a instanceof JMEParamEbd) {
			return sameType(((JMEParamEbd)a).getType(), b);
		}
		if(b instanceof JMEParamEbd) {
			return sameType(a, ((JMEParamEbd)b).getType());
		}
		if(a instanceof JSG_KeywordEbd ) {
			return sameType(((JSG_KeywordEbd)a).getType(), b);
		}
		if(b instanceof JSG_KeywordEbd) {
			return sameType(a, ((JSG_KeywordEbd)b).getType());
		}
		if(a instanceof JSG_UserType ) {
			return sameType(((JSG_UserType)a).getType(), b);
		}
		if(b instanceof JSG_UserType) {
			return sameType(a, ((JSG_UserType)b).getType());
		}
		if(a instanceof JSG_TypePrimitive ) {
			return sameType(((JSG_TypePrimitive)a).getType(), b);
		}
		if(b instanceof JSG_TypePrimitive) {
			return sameType(a, ((JSG_TypePrimitive)b).getType());
		}
		
		// Je sÃ©pare en plusieurs if mÃªme pour un mÃªme retour mais c'est plus lisible
		if (a == null && b == null)
			return true;
		else if(a == null || b == null) {
			return false;
		}else if(a instanceof String && b instanceof String) {
			return ((String)a).compareTo((String)b) == 0 ||
					( 
							(((String)a).compareToIgnoreCase("bool") == 0 || ((String)a).compareToIgnoreCase("boolean") == 0 )
							&& 
							(((String)b).compareToIgnoreCase("bool") == 0 || ((String)b).compareTo("boolean") == 0 )
					)||
					( 
							(((String)a).compareToIgnoreCase("int") == 0 || ((String)a).compareToIgnoreCase("float") == 0 
							|| ((String)a).compareToIgnoreCase("integer") == 0 
							|| ((String)a).compareToIgnoreCase("double") == 0 || ((String)a).compareToIgnoreCase("unsigned") == 0
							|| ((String)a).compareToIgnoreCase("unsigned int") == 0 || ((String)a).compareToIgnoreCase("unit") == 0 )
							&& 
							(((String)b).compareToIgnoreCase("int") == 0 || ((String)b).compareToIgnoreCase("float") == 0 
							|| ((String)b).compareToIgnoreCase("integer") == 0 
							|| ((String)b).compareToIgnoreCase("double") == 0 || ((String)b).compareToIgnoreCase("unsigned") == 0
							|| ((String)b).compareToIgnoreCase("unsigned int") == 0 || ((String)b).compareToIgnoreCase("unit") == 0 )
					);
		//}
//		else if((a instanceof JSG_UserType && ((JSG_UserType) a).getType().compareTo(((JSG_UserType) b).getType()) == 0)
//				|| (a instanceof JSG_TypePrimitive
//						&& ((JSG_TypePrimitive) a).getType().compareTo(((JSG_TypePrimitive) b).getType()) == 0)){
//			// Si on a un type primitif avec un type utilisateur
//			return true;
//		}else if(a.getClass().equals(b.getClass())) {
//			return true;
		}else if((a instanceof JMEParamTopo && (b instanceof JSG_TypeJerboaDart 
				|| (b instanceof JSG_List  && ((JSG_List)b).getTypedList() instanceof JSG_TypeJerboaDart )))
				||
				(b instanceof JMEParamTopo && (a instanceof JSG_TypeJerboaDart 
						|| (a instanceof JSG_List  && ((JSG_List)a).getTypedList() instanceof JSG_TypeJerboaDart )))
				) {
			// Si sont de param topo
			return true;
		}

		return a.getClass().equals(b.getClass());
	}

	@Override
	public JSG_Type accept(JSG_While jsWhile) throws RuntimeException {
		if (jsWhile.getCondition() != null)
			jsWhile.getCondition().visit(this);
		if (jsWhile.getCorps() != null)
			jsWhile.getCorps().visit(this);

		return null;
	}

	@Override
	public JSG_Type accept(JSG_Assignment jsAssignment) throws RuntimeException {
		JSG_Type typeVar = jsAssignment.getVariable().visit(this);
		if (typeVar != null) {
			JSG_Type typeAssign = jsAssignment.getValue().visit(this);
			if (jsAssignment.getValue() instanceof JSG_Null) {
			} else if (typeAssign == null) {
				errorList.add(new JSError("unknown assignement type : ", jsAssignment.getLine(),
						jsAssignment.getColumn(), JSErrorEnumType.WARNING));
			} else if (!sameType(typeVar, typeAssign)) {
				// TODO: si un truc null Ã§a passe pas !
				errorList.add(new JSError(
						"wrong type assignement : expected '" + typeVar.toString() + "' but found '"
								+ typeAssign.toString() + "'",
						jsAssignment.getLine(), jsAssignment.getColumn(), JSErrorEnumType.CRITICAL));
			}
		} else {
			if (jsAssignment.getVariable() instanceof JSG_Variable)
				errorList.add(new JSError(
						"variable '" + ((JSG_Variable) jsAssignment.getVariable()).getName() + "' not declared",
						jsAssignment.getLine(), jsAssignment.getColumn(), JSErrorEnumType.CRITICAL));
			else
				errorList.add(new JSError(jsAssignment + "' not declared", jsAssignment.getLine(),
						jsAssignment.getColumn(), JSErrorEnumType.CRITICAL));

		}
		return typeVar;
	}

	@Override
	public JSG_Type accept(JSG_Block jsBlock) throws RuntimeException {
		variableList.add(new ArrayList<Pair<String, JSG_Type>>());
		if (jsBlock.getBody() != null)
			jsBlock.getBody().visit(this);
		if (listReturnWithDepth.containsKey(variableList.size())
				&& jsBlock.getLine() - 1 > listReturnWithDepth.get(variableList.size()).getLine()) {
			errorList.add(new JSError("Dead code block", listReturnWithDepth.get(variableList.size()).getLine(),
					listReturnWithDepth.get(variableList.size()).getColumn(), jsBlock.getBody().getLine(),
					JSErrorEnumType.DEADCODE));
			listReturnWithDepth.remove(variableList.size());
		}
		variableList.remove(variableList.size() - 1);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_For jsFor) throws RuntimeException {
		variableList.add(new ArrayList<Pair<String, JSG_Type>>());
		// jsFor.getVariable().visit(this);
		declareVariable(jsFor.getVariable(), jsFor.getType(), jsFor);
		jsFor.getStart().visit(this);
		jsFor.getEnd().visit(this);
		jsFor.getBody().visit(this);
		variableList.remove(variableList.size() - 1);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_ForEach jsForEach) throws RuntimeException {
		variableList.add(new ArrayList<Pair<String, JSG_Type>>());
		if (jsForEach.getType() != null)
			jsForEach.getType().visit(this);
		if (jsForEach.getName() != null)
			declareVariable(jsForEach.getName(), jsForEach.getType(), jsForEach);

		JSG_Type typeList = null;
		if (jsForEach.getColl() != null) {
			typeList = jsForEach.getColl().visit(this);
			if (typeList instanceof JSG_TypeGMap)
				typeList = new JSG_List(
						new JSG_TypeJerboaDart(jsForEach.getType().getLine(), jsForEach.getType().getColumn()),
						jsForEach.getType().getLine(), jsForEach.getType().getColumn());
			if (typeList instanceof JSG_TypeJerboaHookList) {
				if (!sameType(jsForEach.getType(), new JSG_TypeJerboaDart(-1, -1))) {
					errorList.add(new JSError("found type '" + jsForEach.getType() + "' but expected type JerboaDart ",
							jsForEach.getType().getLine(), jsForEach.getType().getColumn(), JSErrorEnumType.CRITICAL));
				}
			} else if (!(typeList instanceof JSG_List || typeList instanceof JSG_TypeJerboaHookList)) {
				errorList.add(new JSError("type " + typeList + " may not be iterable " + jsForEach.getColl(),
						jsForEach.getType().getLine(), jsForEach.getType().getColumn(), JSErrorEnumType.WARNING));
			} else if (!sameType(((JSG_List) typeList).getTypedList(), jsForEach.getType())) {
				errorList.add(new JSError(
						"wrong type : expected '" + ((JSG_List) typeList).getTypedList() + "' but found '"
								+ jsForEach.getType() + "'",
						jsForEach.getLine(), jsForEach.getColumn(), JSErrorEnumType.CRITICAL));
			}
		}
		if (jsForEach.getBody() != null)
			jsForEach.getBody().visit(this);
		variableList.remove(variableList.size() - 1);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_ForLoop jsForLoop) throws RuntimeException {
		variableList.add(new ArrayList<Pair<String, JSG_Type>>());
		if (jsForLoop.getInit() != null)
			jsForLoop.getInit().visit(this);

		if (jsForLoop.getCond() != null)
			jsForLoop.getCond().visit(this);
		else
			errorList.add(new JSError("warning : no condition for loop ", jsForLoop.getLine(), jsForLoop.getColumn(),
					JSErrorEnumType.WARNING));

		if (jsForLoop.getStep() != null)
			jsForLoop.getStep().visit(this);
		else
			errorList.add(new JSError("warning : no step specified for loop", jsForLoop.getLine(),
					jsForLoop.getColumn(), JSErrorEnumType.WARNING));

		if (jsForLoop.getBody() != null)
			jsForLoop.getBody().visit(this);
		else
			errorList.add(new JSError("warning : nothing done in for loop ", jsForLoop.getLine(), jsForLoop.getColumn(),
					JSErrorEnumType.WARNING));

		variableList.remove(variableList.size() - 1);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_If jsIf) throws RuntimeException {
		if (jsIf.getCondition() != null)
			jsIf.getCondition().visit(this);
		if (jsIf.getConsequence() != null) {
			jsIf.getConsequence().visit(this);
		}
		if (jsIf.getAlternant() != null) {
			jsIf.getAlternant().visit(this);
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Sequence jsSequence) throws RuntimeException {
		int lineBeginDeadCode = 0, lineEndDeadCode = 0;
		for (JSG_Instruction i : jsSequence) {
			i.visit(this);
			if (i instanceof JSG_Return) {
				if (!listReturnWithDepth.containsKey(variableList.size())) {
					listReturnWithDepth.put(variableList.size(), (JSG_Return) i);
					lineBeginDeadCode = i.getLine();
				}
			} else if (listReturnWithDepth.containsKey(variableList.size())) {
				lineEndDeadCode = i.getLine();
			}
		}
		if (variableList.size() == 1 && lineBeginDeadCode != 0 && lineEndDeadCode != 0) {
			// si c'est la sÃ©quence principale on est pas dans un block
			// donc on test l'erreur ici.
			if (listReturnWithDepth.containsKey(variableList.size())) {
				errorList.add(new JSError("Dead code seq", lineBeginDeadCode,
						listReturnWithDepth.get(variableList.size()).getColumn(), lineEndDeadCode,
						JSErrorEnumType.DEADCODE));
				listReturnWithDepth.remove(variableList.size());
			}
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_DoWhile jsDoWhile) throws RuntimeException {
		if (jsDoWhile.getCondition() != null)
			jsDoWhile.getCondition().visit(this);
		if (jsDoWhile.getBody() != null)
			jsDoWhile.getBody().visit(this);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_ExprInstruction jsExprInstruction) throws RuntimeException {
		if (jsExprInstruction.getExpr() != null)
			return jsExprInstruction.getExpr().visit(this);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Declare jsDeclare) throws RuntimeException {
		JSG_Type typeVar = jsDeclare.getType().visit(this);
		if (jsDeclare.getName() != null && typeVar != null ) {
			// if not declared, do not check the assignement ?
			if (jsDeclare.getValue() != null) {
				JSG_Type t = jsDeclare.getValue().visit(this);
				if (jsDeclare.getValue() instanceof JSG_Null) {
				} else if (t == null)
					errorList.add(new JSError("undefined type", jsDeclare.getLine(), jsDeclare.getColumn(),
							JSErrorEnumType.INFO));
				else if (!sameType(typeVar, t)) {
					errorList
							.add(new JSError(
									"wrong assignement type : expecting '" + typeVar.getType() + "' but found '"
											+ t.getType() + "'",
									jsDeclare.getLine(), jsDeclare.getColumn(), JSErrorEnumType.CRITICAL));
				}
			}
		}
		declareVariable(jsDeclare.getName(), typeVar, jsDeclare);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Map jsMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSG_Type accept(JSG_NOP jsEmpty) {
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Delete jsg_Delete) {
		JSG_Type typeVar = jsg_Delete.getName().visit(this);
		if (typeVar == null) {
			errorList.add(new JSError("delete on unknown variable ", jsg_Delete.getLine(), jsg_Delete.getColumn(),
					JSErrorEnumType.CRITICAL));
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Catch jsg_Catch) {
		variableList.add(new ArrayList<Pair<String, JSG_Type>>());
		if (jsg_Catch.getDeclar() != null)
			jsg_Catch.getDeclar().visit(this);
		if (jsg_Catch.getBlock() != null)
			jsg_Catch.getBlock().visit(this);
		variableList.remove(variableList.size() - 1);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Try jsg_Try) {
		if (jsg_Try.getTryBlock() != null)
			jsg_Try.getTryBlock().visit(this);
		if (jsg_Try.getFinallyBlock() != null)
			jsg_Try.getFinallyBlock().visit(this);
		for (JSG_Catch c : jsg_Try.getCatchList()) {
			c.visit(this);
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_DeclareFunction jsg_DeclareFunction) {
		jsg_DeclareFunction.getReturnType().visit(this);
		// TODO: tester les types de retour !
		for (JSG_Instruction e : jsg_DeclareFunction.getArguments()) {
			e.visit(this);
		}
		jsg_DeclareFunction.getBlock().visit(this);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Print jsg_Print) {
		for (JSG_Expression e : jsg_Print.getArguments()) {
			if(e!=null)
				e.visit(this);
			else {
				errorList.add(new JSError(
						"unrecognized element in @print ", jsg_Print.getLine(), jsg_Print.getColumn(), JSErrorEnumType.CRITICAL));
			}
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_HookCall jsg_HookCall) {
		jsg_HookCall.getHookname(); // TODO: tester si le hook existe dans la
		// rÃ¨gle
		JSG_Type t = jsg_HookCall.getNodename().visit(this);
		if (t == null)
			errorList.add(new JSError("undefined type", jsg_HookCall.getLine(), jsg_HookCall.getColumn(),
					JSErrorEnumType.INFO));
		else if (!sameType(t, new JSG_TypeJerboaDart(-1, -1))) {
			errorList.add(new JSError(
					"wrong hook type, expecting " + (new JSG_TypeJerboaDart(-1, -1)).getType() + " but found '"
							+ t.getType() + "'",
					jsg_HookCall.getLine(), jsg_HookCall.getColumn(), JSErrorEnumType.CRITICAL));
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_AssocParam jsg_AssocParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSG_Type accept(JSG_ClearHookList jsg_ClearHookList) {
		return null;
	}

	@Override
	public JSG_Type accept(JSG_AtLang jsg_AtLang) {
		return null;
	}

	@Override
	public JSG_Type accept(JSG_DeclareMark jsg_DeclareMark) {
		variableType(jsg_DeclareMark.getName());

		// Faire un: declareVar(String varName, JSentity e); qui ajoute l'erreur
		// si besoin et crÃ© la variable.
		declareVariable(jsg_DeclareMark.getName(),
				new JSG_TypeMark(jsg_DeclareMark.getLine(), jsg_DeclareMark.getColumn()), jsg_DeclareMark);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Break jsg_Break) {
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Call jsCall) throws RuntimeException {
		// on ne fait pas de vÃ©rif sur le nom de la fonction car elle peut
		// venir
		// de n'importe oÃ¹
		int argCpt = 0;
		for (JSG_Expression e : jsCall.getArguments()) {
			argCpt++;
			if(e != null) {
				e.visit(this);
			}else {
				errorList.add(new JSError("null argument in function call for function : '" + jsCall.getName() + "' at argument number : " + argCpt ,
						jsCall.getLine(),
						jsCall.getColumn(), JSErrorEnumType.CRITICAL));
			}
		}
		return null; // le retour est null car on a pas la signature de la
		// fonction
	}

	@Override
	public JSG_Type accept(JSG_Variable jsVariable) throws RuntimeException {
		if (variableType(jsVariable.getName()) == null)
			errorList.add(new JSError("unknown variable : '" + jsVariable.getName() + "'", jsVariable.getLine(),
					jsVariable.getColumn(), JSErrorEnumType.CRITICAL));
		return variableType(jsVariable.getName());
	}

	@Override
	public JSG_Type accept(JSG_Orbit jsOrbit) throws RuntimeException {
		Set<Integer> set = new HashSet<>();
		for (JSG_Expression ei : jsOrbit.getDimensions()) {
			if (ei instanceof JSG_Integer) {
				JSG_Integer e = (JSG_Integer) ei;
				if (!set.add(e.getValue())) {
					errorList.add(new JSError("wrong orbit value (multiple occurrence) :'" + e.getValue() + "'",
							e.getLine(), e.getColumn(), JSErrorEnumType.CRITICAL));
				}
				if (e.getValue() < 0) {
					errorList.add(new JSError("wrong orbit value (must be >0) :'" + e.getValue() + "'", e.getLine(),
							e.getColumn(), JSErrorEnumType.CRITICAL));
				} else if (e.getValue() > glue.getModelerDimension()) {
					errorList.add(new JSError(
							"wrong orbit value (must be less than modeler dimension) :'" + e.getValue() + "'",
							e.getLine(), e.getColumn(), JSErrorEnumType.CRITICAL));
				}
			}
		}
		return new JSG_TypeOrbit(jsOrbit.getLine(), jsOrbit.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_InScope jsInScope) throws RuntimeException {
		if(jsInScope.getLeft() != null)
			jsInScope.getLeft().visit(this);

		if(jsInScope.getRight() != null)
			jsInScope.getRight().visit(this);
		return null;
	}

	@Override
	public JSG_Type accept(JSG_CollectEbd jsCollect) throws RuntimeException {
		jsCollect.getEmbedding(); // TODO: tester si existe !
		jsCollect.getEbdType(); // TODO: et tester le type du plongement!
		jsCollect.getNode().visit(this);
		jsCollect.getOrbit().visit(this);
		return new JSG_List(new JSG_UserType(modeler.getEmbedding(jsCollect.getEbdType()), jsCollect.getLine(),
				jsCollect.getColumn()), jsCollect.getLine(), jsCollect.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Collect jsCollect) throws RuntimeException {
		jsCollect.getOrbit().visit(this);
		jsCollect.getNode().visit(this);
		return new JSG_List(new JSG_TypeJerboaDart(jsCollect.getLine(), jsCollect.getColumn()), jsCollect.getLine(),
				jsCollect.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Index jsIndex) throws RuntimeException {
		JSG_Type t = jsIndex.getVariable().visit(this);
		jsIndex.getIndex().visit(this);
		if (t != null) {
			if (t instanceof JSG_List)
				return ((JSG_List) t).getTypedList();
			else if (t instanceof JSG_TypeJerboaRuleResult)
				return new JSG_TypeJerboaDart(1, -1);
		}
		// TODO: vÃ©rifier si d'autre type pour les index

		return null;
	}

	@Override
	public JSG_Type accept(JSG_Alpha jsAlpha) throws RuntimeException {
		JSG_Type typeVar = jsAlpha.getNode().visit(this);
		if (typeVar == null) {
			errorList.add(new JSError("alpha operator called an undefined type : ", jsAlpha.getLine(),
					jsAlpha.getColumn(), JSErrorEnumType.INFO));
		} else if (!(typeVar instanceof JSG_TypeJerboaDart)) {
			errorList.add(new JSError("alpha operator called on something that is not a 'JerboaNode' : ",
					jsAlpha.getLine(), jsAlpha.getColumn(), JSErrorEnumType.CRITICAL));
		}
		if (jsAlpha.getDim() instanceof JSG_Integer && ((JSG_Integer) jsAlpha.getDim()).getValue() < 0) {
			errorList.add(new JSError("wrong alpha dimension, must be >= 0 ", jsAlpha.getLine(), jsAlpha.getColumn(),
					JSErrorEnumType.CRITICAL));
		}
		return new JSG_TypeJerboaDart(jsAlpha.getLine(), jsAlpha.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Integer jsInteger) throws RuntimeException {
		return new JSG_TypePrimitive("int", jsInteger.getLine(), jsInteger.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Float jsFloat) throws RuntimeException {
		return new JSG_TypePrimitive("float", jsFloat.getLine(), jsFloat.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Double jsDouble) throws RuntimeException {
		return new JSG_TypePrimitive("double", jsDouble.getLine(), jsDouble.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Boolean jsBoolean) throws RuntimeException {
		return new JSG_TypeBoolean(jsBoolean.getLine(), jsBoolean.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Long jsLong) throws RuntimeException {
		return new JSG_TypePrimitive("long", jsLong.getLine(), jsLong.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_String jsString) throws RuntimeException {
		return new JSG_TypeString(jsString.getLine(), jsString.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_ApplyRule jsApplyRule) throws RuntimeException {
		if (jsApplyRule.getRule() != null) {
			JMERule rule = jsApplyRule.getRule();
			if (!glue.ruleExist(rule.getName())) {
				errorList.add(new JSError("undefined rule : " + rule.getName(), jsApplyRule.getLine(),
						jsApplyRule.getColumn(), JSErrorEnumType.CRITICAL));
			}
			if (glue.getLangageType() == LanguageType.SCRIPT && glue.getOwnerName().compareTo(rule.getName()) == 0) {
				errorList.add(new JSError("rule recursivity " + rule.getName(), jsApplyRule.getLine(),
						jsApplyRule.getColumn(), JSErrorEnumType.WARNING));
			}
			if(rule.getLeft().getHooks().size()>jsApplyRule.getArgs().size()) {
				errorList.add(new JSError("too few argument for rule : " + rule.getName(), jsApplyRule.getLine(),
						jsApplyRule.getColumn(), JSErrorEnumType.CRITICAL));
			}else {
				int i=0;
				for(;i<Math.min(jsApplyRule.getArgs().size(),rule.getParamsTopo().size());i++) {
					if(!sameType(rule.getParamsTopo().get(i),jsApplyRule.getArgs().get(i).visit(this))) {
						String errFoundParam = "";
						if(jsApplyRule.getArgs() != null) {
							JSG_Expression exprArg = jsApplyRule.getArgs().get(i);
							if(exprArg != null) {
								JSG_Type type = exprArg.visit(this);
								if(type!=null) {
									errFoundParam = type.getType();
								}else {
									errFoundParam = "unknown type";
								}
							}else {
								errFoundParam = "null argument";
							}
						} else {
							errFoundParam = "no argument parsed";
						}
						errorList.add(new JSError("wrong type in rule application for argument num " + i + " named '" 
								+ rule.getParamsTopo().get(i).getName() + "' : " 
								+ rule.getName() +" ==> Found '" 
								+ errFoundParam
								+ "' instead of '" + (rule instanceof JMEScript? " list of JerboaDarts" : "JerboaDarts") +"'", jsApplyRule.getLine(),
								jsApplyRule.getColumn(), JSErrorEnumType.WARNING));
					}
				}
				for(int j=0;j<rule.getParamsEbd().size();j++) {
					if(i>=jsApplyRule.getArgs().size())
						break;
					JSG_Type argExp = jsApplyRule.getArgs().get(i).visit(this);
					if(!sameType(rule.getParamsEbd().get(j),argExp)) {
						errorList.add(new JSError("wrong type in rule application for argument num " + i + " named '" 
								+ rule.getParamsEbd().get(j).getName() + "' : " 
								+ rule.getName() +" ==> Found '" + (argExp!=null?argExp.getType() : "null")
								+ "' instead of '" + rule.getParamsEbd().get(j).getType()+"'", jsApplyRule.getLine(),
								jsApplyRule.getColumn(), JSErrorEnumType.WARNING));
					}
					i++;
				}
			}
		}
		return new JSG_TypeJerboaRuleResult(jsApplyRule.getLine(), jsApplyRule.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Operator jsOperator) throws RuntimeException {
		JSG_Type t = null;
		for (JSG_Expression e : jsOperator) {
			if (e != null) {
				JSG_Type te = e.visit(this);
				if (t == null)
					t = te;
				else if (!sameType(t, te)) {
					if (e instanceof JSG_Null) {
					} else if (te == null)
						errorList.add(new JSError("undefined type", jsOperator.getLine(), jsOperator.getColumn(),
								JSErrorEnumType.INFO));
					else if (t instanceof JSG_UserType || te instanceof JSG_UserType) {
						errorList.add(new JSError(
								"wrong operand type for operator : '" + jsOperator.getOperator().toCode()
										+ "' expecting type '" + t.getType() + "' but found '" + te.getType() + "'",
								jsOperator.getLine(), jsOperator.getColumn(), JSErrorEnumType.WARNING));
					} else if (!(t instanceof JSG_TypePrimitive && te instanceof JSG_TypePrimitive)) {
						errorList.add(new JSError(
								"wrong operand type for operator : '" + jsOperator.getOperator().toCode()
										+ "' expecting type '" + t.getType() + "' but found '" + te.getType() + "'",
								jsOperator.getLine(), jsOperator.getColumn(), JSErrorEnumType.WARNING));
					}
				}
			}
		}
		if (jsOperator.getOperator() == JSOperatorKind.DIFF || jsOperator.getOperator() == JSOperatorKind.EQUAL
				|| jsOperator.getOperator() == JSOperatorKind.GE || jsOperator.getOperator() == JSOperatorKind.GT
				|| jsOperator.getOperator() == JSOperatorKind.LE || jsOperator.getOperator() == JSOperatorKind.LT// ||
																													// jsOperator.getOperator()
																													// ==
																													// JSOperatorKind.XOR
				|| jsOperator.getOperator() == JSOperatorKind.AND || jsOperator.getOperator() == JSOperatorKind.OR) {
			return new JSG_TypeBoolean(jsOperator.getLine(), jsOperator.getColumn());
		}
		return t;
	}

	@Override
	public JSG_Type accept(JSG_Not jsNot) throws RuntimeException {
		JSG_Type typeVar = jsNot.getExpr().visit(this);
		if (typeVar == null) {
			errorList.add(new JSError("undefined operand type for operator NOT", jsNot.getLine(), jsNot.getColumn(),
					JSErrorEnumType.INFO));
		} else if (!(typeVar instanceof JSG_UserType)
				&& !sameType(typeVar, new JSG_TypeBoolean(jsNot.getLine(), jsNot.getColumn()))) {
			errorList.add(new JSError("wrong operand type for operator NOT", jsNot.getLine(), jsNot.getColumn(),
					JSErrorEnumType.CRITICAL));
		}
		return typeVar;
	}

	@Override
	public JSG_Type accept(JSG_Return jsReturn) throws RuntimeException {
		JSG_Type t = null;
		if (jsReturn.getExpression() != null)
			t = jsReturn.getExpression().visit(this);
		return t;
	}

	@Override
	public JSG_Type accept(JSG_KeywordGmap jsGmapKeyword) throws RuntimeException {
		return new JSG_TypeGMap(jsGmapKeyword.getLine(), jsGmapKeyword.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_Type jsType) {
		return jsType;
	}

	@Override
	public JSG_Type accept(JSG_Choice jsAlternativ) {
		for (JSG_Expression e : jsAlternativ.getOptions()) {
			e.visit(this);
		}
		return new JSG_TypeJerboaRuleResult(jsAlternativ.getLine(), jsAlternativ.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_KeywordHook jsKeywordHook) {
		if (glue.getCurrentRule() instanceof JMERuleAtomic)
			return new JSG_TypeJerboaDart(1, -1);
		else if (glue.getCurrentRule() instanceof JMERuleAtomic) {
			return new JSG_List(new JSG_TypeJerboaDart(-1, -1), -1, -1);
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_KeywordEbd jsKeywordEbd) {
		return new JSG_UserType(null, jsKeywordEbd.getLine(), jsKeywordEbd.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_List jsList) {
		jsList.getTypedList().visit(this);
		return jsList;
	}

	@Override
	public JSG_Type accept(JSG_Rule jsRule) {
		if (jsRule.getRule() == null) {
			errorList.add(new JSError("undefined rule called " + jsRule.getName(), jsRule.getLine(), jsRule.getColumn(),
					JSErrorEnumType.CRITICAL));
		}
		return new JSG_TypeJerboaRule(jsRule.getName(), -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_KeywordDimension jsKeywordDimension) {
		return new JSG_TypePrimitive("int", -1, 1);
	}

	@Override
	public JSG_Type accept(JSG_KeywordModeler jsKeywordModeler) {
		return null;// new JSG_TypeModeler(jsKeywordModeler., col); // retourner
		// un JSG_TypeModeler ?
	}

	@Override
	public JSG_Type accept(JSG_Size jsSizeSem) {
		return new JSG_TypePrimitive("int", 1, -1);
	}

	@Override
	public JSG_Type accept(JSG_UserType jsUserTypeSem) {
		return jsUserTypeSem;
	}

	@Override
	public JSG_Type accept(JSG_RuleNode jsRuleNodeSem) {

		jsRuleNodeSem.getRule().visit(this); // tester si la regle existe ? ou
		// c'est plutÃ´t fait dans la
		// rÃ¨gle

		if (jsRuleNodeSem.getNodeName() instanceof JSG_Variable && jsRuleNodeSem.getRule() != null) {
			JMENode nodeToCatch = jsRuleNodeSem.getRule().getRule().getRight().getMatchNode(((JSG_Variable)jsRuleNodeSem.getNodeName()).getName());
			if (nodeToCatch==null) {
				errorList.add(new JSError(
						"Topological variable : '" + ((JSG_Variable) jsRuleNodeSem.getNodeName()).getName()
								+ "' does not exist in right graph of rule '" + jsRuleNodeSem.getRule().getName() + "'",
						jsRuleNodeSem.getLine(), jsRuleNodeSem.getColumn(), JSErrorEnumType.INFO));
			}
		}
//		jsRuleNodeSem.getVarExp().visit(this);
		return new JSG_TypeJerboaDart(1, -1);
	}

	@Override
	public JSG_Type accept(JSG_TypeJerboaRuleResult jsTypeJerboaRuleResultSem) {
		return jsTypeJerboaRuleResultSem;
	}

	@Override
	public JSG_Type accept(JSG_TypeJerboaDart jsTypeJerboaNodeSem) {
		return jsTypeJerboaNodeSem;
	}

	@Override
	public JSG_Type accept(JSG_AddInList jsAddInListSem) {
		for (JSG_Expression e : jsAddInListSem.getArgs()) {
			JSG_Type type = e.visit(this); 
			if(!sameType(type, jsAddInListSem.getList()!=null ? jsAddInListSem.getList().getTypedList() : null)) {
				errorList.add(new JSError("wrong type for element adding in list. Found "
						+ type + " instead of " + jsAddInListSem.getList(), 
						jsAddInListSem.getList().getLine(),
						jsAddInListSem.getList().getColumn(), JSErrorEnumType.WARNING));
			}
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_TypeJerboaRule jsTypeJerboaRuleSem) {
		return jsTypeJerboaRuleSem;
	}

	@Override
	public JSG_Type accept(JSG_TypeJerboaHookList jsType) {
		return jsType;
	}

	@Override
	public JSG_Type accept(JSG_IndexInRuleResult jsIndexInRuleResultSem) {
		if(jsIndexInRuleResultSem.getIndexSecond() != null) {
			return new JSG_TypeJerboaDart(-1, -1);
		}else {
			return new JSG_List(new JSG_TypeJerboaDart(-1, -1), -1, -1);
		}
	}

	@Override
	public JSG_Type accept(JSG_IndexNodeInGmap jsIndexNodeInGmapSem) {
		return new JSG_TypePrimitive("int", -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_Constructor jsg_Constructor) {
		for (JSG_Expression e : jsg_Constructor.getArguments()) {
			e.visit(this);
		}
		return jsg_Constructor.getName().visit(this);
	}

	@Override
	public JSG_Type accept(JSG_AddInHookList jsg_AddInHookList) {
		for (JSG_Expression e : jsg_AddInHookList.getArgs()) {
			e.visit(this);
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Indirection jsg_Indirection) {
		return jsg_Indirection.getExp().visit(this);
	}

	@Override
	public JSG_Type accept(JSG_Unreference jsg_Unreference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Comment jsg_Comment) {
		return null;
	}

	@Override
	public JSG_Type accept(JSG_New jsg_New) {
		jsg_New.getExp().visit(this);
		// TODO: ajouter un pointeur dans les variables ?
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Cast jsg_Cast) {
		jsg_Cast.getType(); // Aurait du Ãªtre un JSG_Type !
		// TODO: refaire cette partie
		return null;
	}

	@Override
	public JSG_Type accept(JSG_GetEbd jsg_GetEbd) {
		return new JSG_UserType(jsg_GetEbd.getEbdInfo(), -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_TypeBoolean jsg_TypeBoolean) {
		return jsg_TypeBoolean;
	}

	@Override
	public JSG_Type accept(JSG_CallRuleResWidth jsg_CallRuleResWidth) {
		return new JSG_TypePrimitive("int", -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_CallListSize jsg_CallListSize) {
		return new JSG_TypePrimitive("int", -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_CallRuleResHeight jsg_CallRuleResHeight) {
		return new JSG_TypePrimitive("int", -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_LeftRuleNode jsg_TopoParam) {
		if (glue.getLangageType() == LanguageType.SCRIPT) {
			return new JSG_TypeJerboaHookList(jsg_TopoParam.getRule(), jsg_TopoParam.getLine(),
					jsg_TopoParam.getColumn());
		}
		return new JSG_TypeJerboaDart(jsg_TopoParam.getLine(), jsg_TopoParam.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_InScopeStatic jsg_InScopeStatic) {
		jsg_InScopeStatic.getLeft().visit(this);
		// jsg_InScopeStatic.getRight().visit(this);
		// on ne visite pas a droite car on ne connait pas les spÃ©cificitÃ©s de
		// la classe
		// on ne sais pas trop quel type est retournÃ© ici.
		// EDIT : en vrai il faudrait tester si c'est une fonction par exemple
		// pour vÃ©rifier que les paramÃ¨tres
		// existent
		if (!(jsg_InScopeStatic.getRight() instanceof JSG_Variable)) {
			jsg_InScopeStatic.getRight().visit(this);
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Null jsg_Null) {
		return null;
	}

	@Override
	public JSG_Type accept(JSG_GetEbdName jsg_GetEbdName) {
		// TODO: tester si le plongement existe
		return new JSG_TypeString(1, -1);
	}

	@Override
	public JSG_Type accept(JSG_GetEbdId jsg_GetEbdId) {
		return new JSG_TypePrimitive("int", -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_TypePrimitive jsg_TypePrimitive) {
		return jsg_TypePrimitive;
	}

	@Override
	public JSG_Type accept(JSG_TypeOrbit jsTypeOrbit) {
		return jsTypeOrbit;
	}

	@Override
	public JSG_Type accept(JSG_TypeMark jsTypeMark) {
		return jsTypeMark;
	}

	@Override
	public JSG_Type accept(JSG_GetNodeId jsg_GetId) {
		return new JSG_TypePrimitive("int", -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_GetEbdOrbit jsg_GetEbdOrbit) {
		// TODO: tester si le plongement existe
		return new JSG_TypeOrbit(-1, -1);
	}

	@Override
	public JSG_Type accept(JSG_Header jsg_Header) {
		// on ne fait rien ici
		return null;
	}

	@Override
	public JSG_Type accept(JSG_EbdParam jsg_EbdParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSG_Type accept(JSG_TypeTemplate jsTypeTemplate) {
		jsTypeTemplate.getBaseType().visit(this);
		for (JSG_Type t : jsTypeTemplate.getTypes()) {
			t.visit(this);
		}
		return jsTypeTemplate;
	}

	@Override
	public JSG_Type accept(JSG_FreeMarker jsg_FreeMarker) {
		return new JSG_TypeMark(1, -1);
	}

	@Override
	public JSG_Type accept(JSG_GetMarker jsg_GetFreeMarker) {
		return new JSG_TypeMark(1, -1);
	}

	@Override
	public JSG_Type accept(JSG_RuleArg jsg_RuleArg) {
		if(jsg_RuleArg!=null && jsg_RuleArg.getArgValue() !=null) {
			return jsg_RuleArg.getArgValue().visit(this);
		}else
			System.err.println(jsg_RuleArg.getArgName()+" undefined ");
		return null;
	}

	@Override
	public JSG_Type accept(JSG_IsMarked jsg_IsMarked) {
		JSG_Type typeDart = jsg_IsMarked.getLeft().visit(this);
		JSG_Type typeMark = jsg_IsMarked.getMark().visit(this);
		if (typeDart == null) {
			errorList.add(new JSError("unknown type for dart at mark test ", jsg_IsMarked.getLine(),
					jsg_IsMarked.getColumn(), JSErrorEnumType.WARNING));
		} else if (!sameType(typeDart, new JSG_TypeJerboaDart(-1, -1))) {
			errorList.add(new JSError(
					"wrong type for dart at mark test, found :" + typeDart + " but expected type "
							+ new JSG_TypeJerboaDart(-1, -1),
					jsg_IsMarked.getLine(), jsg_IsMarked.getColumn(), JSErrorEnumType.CRITICAL));
		}
		if (typeMark == null) {
			errorList.add(new JSError("unknown type for dart at mark test ", jsg_IsMarked.getLine(),
					jsg_IsMarked.getColumn(), JSErrorEnumType.WARNING));
		} else if (!sameType(typeMark, new JSG_TypeMark(-1, -1))) {
			errorList.add(new JSError(
					"wrong type for mark at mark test, found :" + typeMark + " but expected type "
							+ new JSG_TypeMark(-1, -1),
					jsg_IsMarked.getLine(), jsg_IsMarked.getColumn(), JSErrorEnumType.CRITICAL));
		}

		return new JSG_TypeBoolean(jsg_IsMarked.getLine(), jsg_IsMarked.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_IsNotMarked jsg_IsNotMarked) {
		JSG_Type typeDart = jsg_IsNotMarked.getLeft().visit(this);
		JSG_Type typeMark = jsg_IsNotMarked.getMark().visit(this);
		if (typeDart == null) {
			errorList.add(new JSError("unknown type for dart at mark test ", jsg_IsNotMarked.getLine(),
					jsg_IsNotMarked.getColumn(), JSErrorEnumType.WARNING));
		} else if (!sameType(typeDart, new JSG_TypeJerboaDart(-1, -1))) {
			errorList.add(new JSError(
					"wrong type for dart at mark test, found :" + typeDart + " but expected type "
							+ new JSG_TypeJerboaDart(-1, -1),
					jsg_IsNotMarked.getLine(), jsg_IsNotMarked.getColumn(), JSErrorEnumType.CRITICAL));
		}
		if (typeMark == null) {
			errorList.add(new JSError("unknown type for dart at mark test ", jsg_IsNotMarked.getLine(),
					jsg_IsNotMarked.getColumn(), JSErrorEnumType.WARNING));
		} else if (!sameType(typeMark, new JSG_TypeMark(-1, -1))) {
			errorList.add(new JSError(
					"wrong type for mark at mark test, found :" + typeMark + " but expected type "
							+ new JSG_TypeMark(-1, -1),
					jsg_IsNotMarked.getLine(), jsg_IsNotMarked.getColumn(), JSErrorEnumType.CRITICAL));
		}

		return new JSG_TypeBoolean(jsg_IsNotMarked.getLine(), jsg_IsNotMarked.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_UnMark jsg_UnMark) {
		JSG_Type typeLeft = jsg_UnMark.getLeft().visit(this);
		JSG_Type typeMark = jsg_UnMark.getMark().visit(this);
		if (!sameType(typeLeft, new JSG_List(new JSG_TypeJerboaDart(-1, -1), -1, -1))
				&& !sameType(typeLeft, new JSG_TypeJerboaDart(-1, -1))
				&& !sameType(typeLeft, new JSG_TypeJerboaDart(-1, -1))) {
			errorList.add(new JSError(
					"wrong type for 1st argument of marking function, expected list of JerboaDart or JerboaDart but found "
							+ typeLeft,
					jsg_UnMark.getLine(), jsg_UnMark.getColumn(), JSErrorEnumType.CRITICAL));
		}
		if (!sameType(typeMark, new JSG_TypeMark(-1, -1))) {
			errorList.add(
					new JSError("wrong type for 2nd argument of marking function, expected mark but found " + typeMark,
							jsg_UnMark.getLine(), jsg_UnMark.getColumn(), JSErrorEnumType.CRITICAL));
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_Mark jsg_Mark) {
		JSG_Type typeLeft = jsg_Mark.getLeft().visit(this);
		JSG_Type typeMark = jsg_Mark.getMark().visit(this);
		if (!sameType(typeLeft, new JSG_List(new JSG_TypeJerboaDart(-1, -1), -1, -1))
				&& !sameType(typeLeft, new JSG_TypeJerboaDart(-1, -1))
				&& !sameType(typeLeft, new JSG_TypeJerboaDart(-1, -1))) {
			errorList.add(new JSError(
					"wrong type for 1st argument of marking function, expected list of JerboaDart or JerboaDart but found "
							+ typeLeft,
					jsg_Mark.getLine(), jsg_Mark.getColumn(), JSErrorEnumType.CRITICAL));
		}
		if (!sameType(typeMark, new JSG_TypeMark(-1, -1))) {
			errorList.add(
					new JSError("wrong type for 2nd argument of marking function, expected mark but found " + typeMark,
							jsg_Mark.getLine(), jsg_Mark.getColumn(), JSErrorEnumType.CRITICAL));
		}
		return null;
	}

	@Override
	public JSG_Type accept(JSG_GMapSize jsg_GMapSize) {
		return new JSG_TypePrimitive("int", jsg_GMapSize.getLine(), jsg_GMapSize.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_KeywordLeftFilter jsg_KeywordLeftFilter) {
		// TODO: ajouter un type JerboaFilterRowMatrix ?
		return new JSG_TypeJerboaRuleResult(jsg_KeywordLeftFilter.getLine(), jsg_KeywordLeftFilter.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_KeywordRightFilter jsg_KeywordRightFilter) {
		// TODO: ajouter un type JerboaFilterRowMatrix ?
		return new JSG_TypeJerboaRuleResult(jsg_KeywordRightFilter.getLine(), jsg_KeywordRightFilter.getColumn());
	}

	@Override
	public JSG_Type accept(JSG_IndexRuleNode jsg_IndexRuleNode) {
		// TODO: vÃ©rif sur la rÃ¨gle qu'elle a bien le noeud spÃ©cifiÃ©

		if(jsg_IndexRuleNode.getRule().getRight().getMatchNode(jsg_IndexRuleNode.getNodeName()) == null) {
			errorList.add(
					new JSError("no right node name " + jsg_IndexRuleNode.getNodeName() 
					+ " in rule '" + jsg_IndexRuleNode.getRule().getName() +"'",
							jsg_IndexRuleNode.getLine(), jsg_IndexRuleNode.getColumn(), JSErrorEnumType.CRITICAL));
		}
		return new JSG_TypePrimitive("int", -1, -1);
	}

	@Override
	public JSG_Type accept(JSG_PackagedType jsType) {
		return jsType.getRight().visit(this);
	}

	@Override
	public JSG_Type accept(JSG_TypeString jsg_TypeString) {
		return jsg_TypeString;
	}

	@Override
	public JSG_Type accept(JSG_Continue jsg_Continue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSG_Type accept(JSG_IndexInLeftPattern jsg_IndexInLeftPattern) {
		if (jsg_IndexInLeftPattern.getHookIndex() != null)
			jsg_IndexInLeftPattern.getIndexInDartList().visit(this);
		if (jsg_IndexInLeftPattern.getHookIndex() != null)
			jsg_IndexInLeftPattern.getHookIndex().visit(this);
		return jsg_IndexInLeftPattern.returnType();
	}

	@Override
	public JSG_Type accept(JSG_Throw jsg_Throw) {
		return jsg_Throw.getExpr().visit(this);
	}

	@Override
	public JSG_Type accept(JSG_GetTopoParam jsg_GetTopoParam) {
		if(jsg_GetTopoParam.getIndex()==null) {
			return new JSG_List(new JSG_TypeJerboaDart(-1, -1), -1,-1);
		}
		return new JSG_TypeJerboaDart(-1, -1);
	}

}
