/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageType;
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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaDart;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeObject;
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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSOperatorKind;

/**
 * @author Valentin
 *
 */
public class JSG_GeneratorJava implements Generator_G {

	private static String nameToProperty(String name) {
		String s = name.substring(0, 1).toUpperCase() + name.substring(1);
		return s;
	}

	private GeneratedLanguage genLanguage;

	private JMEModeler modeler;

	private LanguageGlue glue;
	private int tab = 0;

	private String errorParseMsg = "error in parsing";

	private boolean lineJumped;

	private String hookListName;

	private ArrayList<ArrayList<String>> listOfDeclaration;
	
	private Map<String, JSG_Type> mapVarToType;

	private ArrayList<JMERule> importedRules;

	public JSG_GeneratorJava(LanguageGlue _glue, GeneratedLanguage genL, JMEModeler _mod) {
		tab = 0;
		modeler = _mod;
		lineJumped = true;
		glue = _glue;
		listOfDeclaration = new ArrayList<>();
		listOfDeclaration.add(new ArrayList<String>());
		mapVarToType = new HashMap<String, JSG_Type>();
		genLanguage = new GeneratedLanguage(genL);
		if (glue.getLangagesState() != LanguageGlue.LanguageState.PRECONDITION
				&& glue.getLangagesState() != LanguageGlue.LanguageState.HEADER
				&& glue.getLangagesState() != LanguageGlue.LanguageState.POSTPROCESS
				&& glue.getLangagesState() != LanguageGlue.LanguageState.PREPROCESS
				&& glue.getLangageType() != LanguageType.EMBEDDING) {
			hookListName = "_hn";
		}
		importedRules = new ArrayList<>();
	}

	@Override
	public Boolean accept(JSG_AddInHookList jsg_AddInHookList) {
		final List<JSG_Expression> args = jsg_AddInHookList.getArgs();
		if (args.size() > 1) { // si on passe un entier ET la valeur c'est pour
								// ajouter Ã  une place spÃ©cifique
			genLanguage.appendContent("addRow(");
		} else // sinon ajout normal
			genLanguage.appendContent("addCol(");

		for (int i = 0; i < args.size(); i++) {
			final JSG_Expression e = args.get(i);
			e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_AddInList jsAddInListSem) {
		// HAK: ????
		genLanguage.appendContent("add(");
		final List<JSG_Expression> args = jsAddInListSem.getArgs();
		for (int i = 0; i < args.size(); i++) {
			final JSG_Expression e = args.get(i);
			e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Alpha jsAlpha) throws RuntimeException {
		jsAlpha.getNode().visit(this);
		genLanguage.appendContent(".alpha(");
		jsAlpha.getDim().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_ApplyRule jsApplyRule) throws RuntimeException {
		// TODO: VAL : j'ai fais la modif pour utiliser la nouvelle traduction,
		// a tester
		/*
		 * int curVar = fresh++;
		 * genLanguage.appendContentln("JerboaInputHooksAtomic var" + curVar +
		 * " = new JerboaInputHooksAtomic();"); for (JSG_Expression ra :
		 * jsApplyRule.getArgs()) { genLanguage.appendContent("var" + curVar +
		 * ".addCol("); ra.visit(this); genLanguage.appendContentln(");"); }
		 *
		 * jsApplyRule.getRuleExpr().visit(this);
		 * genLanguage.appendContent(".applyRule(gmap, var" + curVar +
		 * ", JerboaRuleResultKind.");
		 */
		jsApplyRule.getRuleExpr().visit(this);
		JMERule rule = jsApplyRule.getRule();
		if(!importedRules.contains(rule)) {
			importedRules.add(rule);
			String pkg =  rule.getModeler().getModule();
			if(rule.getCategory()!=null && rule.getCategory().length()>0) {
				pkg += "." + rule.getCategory();
			}
			genLanguage.appendInclude("import " + pkg + "." + rule.getName() + ";\n");
		}
		genLanguage.appendContent(".applyRule(gmap");
		for (JSG_Expression ra : jsApplyRule.getArgs()) {
			genLanguage.appendContent(", ");
			ra.visit(this);
		}
		/*
		 * genLanguage.appendContent(", JerboaRuleResultKind."); switch
		 * (jsApplyRule.getReturnType()) { case NONE: break; case COLUMN:
		 * 
		 * break; case ROW: genLanguage.appendContent("ROW");
		 * System.err.println("ROW RULECALL IS NO MORE SUPPORTED!!!! (" +
		 * jsApplyRule.getReturnType() + ")"); throw new
		 * RuntimeException("APPLY RULE RESULT MUST BE COLUMN OR NONE BUT NO MORE ROW!!!!!"
		 * ); default: genLanguage.appendContent(jsApplyRule.getReturnType());
		 * System.err.println("ROW RULECALL IS NO MORE SUPPORTED!!!!"); }
		 */

		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_Assignment jsAssignment) throws RuntimeException {

		jsAssignment.getVariable().visit(this);
		genLanguage.appendContent(" = ");
		// HAK: je ne comprends pas trop ici pourquoi tout ca????
		// TODO: vÃ©rifier ici si on teste le #PrimitivTtpe
		// Val : j'elève pour éviter de créer des intance sur des paramètres null 
//		if (mapVarToType.get(((JSG_Variable) jsAssignment.getVariable()).getName()) instanceof JSG_UserType) {
//			genLanguage.appendContent("new ");
//			mapVarToType.get(((JSG_Variable) jsAssignment.getVariable()).getName()).visit(this);
//			genLanguage.appendContent("(");
//		}
		jsAssignment.getValue().visit(this);
//		if (mapVarToType.get(((JSG_Variable) jsAssignment.getVariable()).getName()) instanceof JSG_UserType) {
//			genLanguage.appendContent(")");
//		}
		// genLanguage.appendContentln(";");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_AssocParam jsAssocParam) {
		jsAssocParam.getRule().visit(this);
		genLanguage.appendContent(".set");
		genLanguage.appendContent(nameToProperty(jsAssocParam.getParamName()));
		genLanguage.appendContent("(");
		jsAssocParam.getParamValue().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_AtLang jsAtLang) {
		if (jsAtLang.getLanguage() == null || "java".equalsIgnoreCase(jsAtLang.getLanguage())) {
			genLanguage.appendContentln("// BEGIN LANGUAGE INPUT CODE");
			indent();
			genLanguage.appendContentln(jsAtLang.getCode());
			indent();
			genLanguage.appendContentln("// END LANGUAGE INPUT CODE");

		}
		return true;
	}

	@Override
	public Boolean accept(JSG_Block jsBlock) throws RuntimeException {
//		if (jsBlock.hasBracket())
			genLanguage.appendContentln("{");
		lineJumped = true;
		tab++;
		if (!(jsBlock.getBody() instanceof JSG_Sequence))
			indent();
		if (jsBlock.getBody() != null)
			jsBlock.getBody().visit(this);
		if (!lineJumped) {
			if (!(jsBlock.getBody() instanceof JSG_Sequence) && !(jsBlock.getBody() instanceof JSG_NOP)) {
				genLanguage.appendContentln(";");
			} else
				genLanguage.appendContentln("");
		}
		lineJumped = true;
		tab--;
		indent();
//		if (jsBlock.hasBracket())
			genLanguage.appendContentln("}");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Boolean jsBoolean) throws RuntimeException {
		genLanguage.appendContent(jsBoolean.getValue());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Break jsg_Break) {
		genLanguage.appendContent("break");
		return false;
	}

	@Override
	public Boolean accept(JSG_Call jsCall) throws RuntimeException {
		genLanguage.appendContent(jsCall.getName());
		genLanguage.appendContent("(");
		final List<JSG_Expression> args = jsCall.getArguments();
		for (int i = 0; i < args.size(); i++) {
			final JSG_Expression e = args.get(i);
			e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_CallListSize jsg_CallListSize) {
		genLanguage.appendContent("size()");
		return false;
	}

	@Override
	public Boolean accept(JSG_CallRuleResHeight jsg_CallRuleResHeight) {
		genLanguage.appendContent("getHeight()");
		return false;
	}

	@Override
	public Boolean accept(JSG_CallRuleResWidth jsg_CallRuleResWidth) {
		// HAK: bon ce ne n'est pas ca mais .size()???
		genLanguage.appendContent("getWidth()");
		return false;
	}

	@Override
	public Boolean accept(JSG_Cast jsg_Cast) {
		genLanguage.appendContent("(");
		genLanguage.appendContent("(");
		jsg_Cast.getType().visit(this);
		genLanguage.appendContent(")");
		jsg_Cast.getExpr().visit(this);
		genLanguage.appendContent(")");
		return null;
	}

	@Override
	public Boolean accept(JSG_Catch jsg_Catch) {
		// HAK: faute typo pour getDeclare
		indent();
		genLanguage.appendContent("catch(");
		if (jsg_Catch.getDeclar() != null) {
			jsg_Catch.getDeclar().visit(this);
		} else {
			genLanguage.appendContent("Exception __excp");
		}
		genLanguage.appendContent(")");
		jsg_Catch.getBlock().visit(this);
		return true;
	}

	@Override
	public Boolean accept(JSG_Choice jsAlternativ) {
		indent();
		lineJumped = true;
		for (int i = 0; i < jsAlternativ.getOptions().size(); i++) {
			JSG_Expression apr = jsAlternativ.getOptions().get(i);
			indent();
			genLanguage.appendContentln("try{");
			tab++;
			lineJumped = true;
			indent();
			// if (jsAlternativ.getVarResult() != null) {
			// genLanguage.appendContent(jsAlternativ.getVarResult() + " = ");
			// lineJumped = false;
			// }
			apr.visit(this);
			genLanguage.appendContentln(";");
			lineJumped = true;
			tab--;
			indent();
			genLanguage.appendContentln("}catch(JerboaException e" + i + "){");
			lineJumped = true;
			tab++;
		}
		indent();
		genLanguage.appendContentln("throw e;");
		lineJumped = true;
		for (int i = 0; i < jsAlternativ.getOptions().size(); i++) {
			tab--;
			indent();
			genLanguage.appendContentln("}");
			lineJumped = true;

		}
		if (jsAlternativ.getOptions().size() <= 0) {
			genLanguage.appendContentln("}");
			lineJumped = true;
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_ClearHookList jsg_ClearHookList) {
		// HAK: c'est quasi definitif je hais ton truc...
		genLanguage.appendContent(hookListName);
		genLanguage.appendContent(".clear()");
		return false;
	}

	@Override
	public Boolean accept(JSG_Collect jsCollect) throws RuntimeException {
		// genLanguage.appendContent("map.");
		// TODO: VAL : ID que pour collect d'ebd
		genLanguage.appendContent("gmap.");
		genLanguage.appendContent("collect(");
		jsCollect.getNode().visit(this);
		genLanguage.appendContent(",");
		jsCollect.getOrbit().visit(this);
		genLanguage.appendContent(",");
		jsCollect.getSubOrbit().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_CollectEbd jsCollect) throws RuntimeException {
		// genLanguage.appendContent("map.");
		// genLanguage.appendContent("<");
		// genLanguage.appendContent(jsCollect.getEbdType());
		// genLanguage.appendContent(">");
		// TODO: VAL : j'ai mis le "gmap." car je faisais une mauvaise
		// traduction que j'ai enlevÃ©
		genLanguage.appendContent("gmap.");
		// HAK: OPT le plongement par sa valeur?
		genLanguage.appendContent("<");
		genLanguage.appendContent(modeler.getEmbedding(jsCollect.getEmbedding()).getType());
		genLanguage.appendContent(">");
		genLanguage.appendContent("collect(");
		jsCollect.getNode().visit(this);
		genLanguage.appendContent(",");
		jsCollect.getOrbit().visit(this);
		genLanguage.appendContent(",");
		int cpt = 0;
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if (e.getName().compareToIgnoreCase(jsCollect.getEmbedding()) == 0)
				break;
			cpt++;
		}
		genLanguage.appendContent(cpt);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Comment jsg_Comment) {
		genLanguage.appendContentln(jsg_Comment.getComment());
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Constructor jsg_Constructor) {
		// // genLanguage.appendContent("( "); // ### new
		// if (jsg_Constructor.getName() instanceof JSG_List)
		genLanguage.appendContent("new ");
		jsg_Constructor.getName().visit(this);
		genLanguage.appendContent("(");
		final List<JSG_Expression> args = jsg_Constructor.getArguments();
		for (int i = 0; i < args.size(); i++) {
			final JSG_Expression e = args.get(i);
			e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Continue jsg_continue) {
		genLanguage.appendContent("continue");
		return false;
	}

	@Override
	public Boolean accept(JSG_Declare jsDeclare) throws RuntimeException {
		jsDeclare.getType().visit(this);

		mapVarToType.put(jsDeclare.getName(), jsDeclare.getType());
		genLanguage.appendContent(" " + jsDeclare.getName());
		lineJumped = false;
		if (jsDeclare.getValue() != null
				&& !((jsDeclare.getType() instanceof JSG_List) && (jsDeclare.getValue() instanceof JSG_Constructor))) {
			genLanguage.appendContent(" = ");
			/*if (!(jsDeclare.getValue() instanceof JSG_Null) && mapVarToType.get(jsDeclare.getName()) instanceof JSG_UserType) {
				genLanguage.appendContent("new ");
				mapVarToType.get(jsDeclare.getName()).visit(this);
				genLanguage.appendContent("(");
			}*/
			jsDeclare.getValue().visit(this);
			/*if (!(jsDeclare.getValue() instanceof JSG_Null) && mapVarToType.get(jsDeclare.getName()) instanceof JSG_UserType) {
				genLanguage.appendContent(")");
			}*/
		} else if (jsDeclare.getValue() == null && jsDeclare.getType() instanceof JSG_List) {
			genLanguage.appendContent(" = new ArrayList<");
			((JSG_List) jsDeclare.getType()).getTypedList().visit(this);
			genLanguage.appendContent(">()");
		} else if (jsDeclare.getValue() == null && jsDeclare.getType() instanceof JSG_TypeObject)
			genLanguage.appendContent(" = null");

		// if (!lineJumped)
		// genLanguage.appendContentln(";");
		// lineJumped = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_DeclareFunction jsg_DeclareFunction) {
		// HAK: euh tu es sur que c'est le bon endroit pour declarer une
		// fonction
		// on pourrait la decaler??

		jsg_DeclareFunction.getReturnType().visit(this);
		genLanguage.appendContent(" ");
		genLanguage.appendContent(jsg_DeclareFunction.getName());
		genLanguage.appendContent("(");
		final List<JSG_Instruction> args = jsg_DeclareFunction.getArguments();
		for (int i = 0; i < args.size(); i++) {
			args.get(i).visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		genLanguage.appendContent(")");
		jsg_DeclareFunction.getBlock().visit(this);
		genLanguage.appendContentln("");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_DeclareMark jsg_DeclareMark) {
		genLanguage.appendContent("int ");
		genLanguage.appendContent(jsg_DeclareMark.getName());
		genLanguage.appendContent(" = gmap.getFreeMarker()");
		return false;
	}

	@Override
	public Boolean accept(JSG_Delete jsg_Delete) {
		// Nothing to do
		genLanguage.appendContentln("// No need anymore: " + jsg_Delete.getName());
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Double jsDouble) throws RuntimeException {
		genLanguage.appendContent(jsDouble.getValue());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_DoWhile jsDoWhile) throws RuntimeException {
		indent();
		genLanguage.appendContent("do");
		jsDoWhile.getBody().visit(this);
		indent();
		genLanguage.appendContent("while(");
		jsDoWhile.getCondition().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_EbdParam jsg_EbdParam) {
		genLanguage.appendContent(jsg_EbdParam.getName());
		return false;
	}

	@Override
	public Boolean accept(JSG_ExprInstruction jsExprInstruction) throws RuntimeException {
		return jsExprInstruction.getExpr().visit(this);
		/*
		 * genLanguage.appendContentln(";"); return true;
		 */
	}

	@Override
	public Boolean accept(JSG_Float jsFloat) throws RuntimeException {
		genLanguage.appendContent(jsFloat.getValue()+"f");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_For jsFor) throws RuntimeException {
		// HAK: c'est bizarre de lever des erreurs ici normalement surtout
		// que toi tu souleves des trucs que normalement on savait deja, non????
		indent();
		genLanguage.appendContent("for(");
		if (jsFor.getType() != null)
			genLanguage.appendContent(jsFor.getType().getType());
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(" ");
		if (jsFor.getVariable() != null)
			genLanguage.appendContent(jsFor.getVariable());
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent("=");
		if (jsFor.getStart() != null)
			jsFor.getStart().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(";");
		genLanguage.appendContent(jsFor.getVariable());
		genLanguage.appendContent("<=");
		if (jsFor.getEnd() != null)
			jsFor.getEnd().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(";");
		if (jsFor.getVariable() != null)
			genLanguage.appendContent(jsFor.getVariable());
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent("+=");
		if (jsFor.getStep() != null)
			jsFor.getStep().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(")");
		lineJumped = false;

		if (jsFor.getBody() != null)
			jsFor.getBody().visit(this);
		return true;
	}

	@Override
	public Boolean accept(JSG_ForEach jsForEach) throws RuntimeException {
		indent();
		genLanguage.appendContent("for(");
		jsForEach.getType().visit(this);
		genLanguage.appendContent(" ");
		genLanguage.appendContent(jsForEach.getName());
		genLanguage.appendContent(" : ");
		jsForEach.getColl().visit(this);
		// HAK: pourquoi des incollades ici alors que pas dans les autres for???
		genLanguage.appendContent(")");
		jsForEach.getBody().visit(this);
		indent();
		genLanguage.appendContent("\n");
		return true;
	}

	@Override
	public Boolean accept(JSG_ForLoop jsForLoop) throws RuntimeException {
		indent();
		genLanguage.appendContent("for(");
		jsForLoop.getInit().visit(this);
		genLanguage.appendContent("; ");
		jsForLoop.getCond().visit(this);
		genLanguage.appendContent("; ");
		jsForLoop.getStep().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;

		jsForLoop.getBody().visit(this);
		return true;
	}

	@Override
	public Boolean accept(JSG_FreeMarker jsg_FreeMarker) {
		genLanguage.appendContent("gmap");
		genLanguage.appendContent(".freeMarker(");
		jsg_FreeMarker.getMarker().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetEbd jsg_GetEbd) {
		// HAK: pensez a l'OPTimisation
		jsg_GetEbd.getLeft().visit(this);
		genLanguage.appendContent(".<");
		genLanguage.appendContent(jsg_GetEbd.getEbdInfo().getType());
		genLanguage.appendContent(">ebd(");
		int cpt = 0;
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if (e.getName().compareToIgnoreCase(jsg_GetEbd.getEbdInfo().getName()) == 0)
				break;
			cpt++;
		}
		genLanguage.appendContent(cpt+")");
//		
//		
//		(new JSG_KeywordModeler(modeler)).visit(this);
//		genLanguage.appendContent(".get");
//		String ebdName = jsg_GetEbd.getEbdInfo().getName();
//		if (ebdName.length() > 0) {
//			String first = ebdName.charAt(0) + "";
//			first = first.toUpperCase();
//			ebdName = first + ebdName.substring(1);
//		}
//		genLanguage.appendContent(ebdName);
//		genLanguage.appendContent("().getID()"
//				+ ")");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetEbdId jsg_GetEbdId) {
		// HAK: pensez a l'OPTimisation
		genLanguage.appendContent("modeler.getEmbedding(\"" + jsg_GetEbdId.getEbdInfo().getName() + "\").getID()");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetEbdName jsg_GetEbdName) {
		// HAK: ??????
		genLanguage.appendContent("\"" + jsg_GetEbdName.getEbdInfo().getName() + "\"");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetEbdOrbit jsg_GetEbdOrbit) {
		// HAK: OPTimisation
		genLanguage
				.appendContent("modeler.getEmbedding(\"" + jsg_GetEbdOrbit.getEbdInfo().getName() + "\").getOrbit()");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetMarker jsg_GetFreeMarker) {
		genLanguage.appendContent("gmap");
		genLanguage.appendContent(".creatFreeMarker()");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetNodeId jsg_GetId) {
		genLanguage.appendContent("getID()");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetTopoParam jsg_GetTopoParam) {
		if(glue.getLangagesState() == LanguageState.PRECONDITION) {
			/*
			 * Val : on a pas du mettre les filtre dans le même sens car en C++ c'est plus simple à traduire
			genLanguage.appendContent("leftPattern.get(");
			genLanguage.appendContent(jsg_GetTopoParam.getNode().getID());	
			genLanguage.appendContent(")");
			if(jsg_GetTopoParam.getIndex()!=null) {
				genLanguage.appendContent(".get(");
				jsg_GetTopoParam.getIndex().visit(this);				
				genLanguage.appendContent(")");
			}
			*/

			if(jsg_GetTopoParam.getIndex()!=null) {
				genLanguage.appendContent("leftPattern.get(");
				jsg_GetTopoParam.getIndex().visit(this);
				genLanguage.appendContent(")");
				genLanguage.appendContent(".get(");	
				genLanguage.appendContent(jsg_GetTopoParam.getNode().getID());				
				genLanguage.appendContent(")");
			}else {
				genLanguage.appendContent("/* error in traduction in function 'Boolean accept(JSG_GetTopoParam jsg_GetTopoParam)' in JSG_GeneratorJava file */"); 
			}
		}else if(glue.getCurrentRule() instanceof JMEScript) {
			genLanguage.appendContent("hooks.dart(");
			genLanguage.appendContent(jsg_GetTopoParam.getNode().getID());	
			if(jsg_GetTopoParam.getIndex()!=null) {
				genLanguage.appendContent(", ");
				jsg_GetTopoParam.getIndex().visit(this);
			}
			genLanguage.appendContent(")");
		}else if( glue.getLangagesState() == LanguageState.MIDPROCESS) {
			genLanguage.appendContent("leftPattern.get(");
			genLanguage.appendContent(jsg_GetTopoParam.getNode().getID()+")");	
			if(jsg_GetTopoParam.getIndex()!=null) {
				genLanguage.appendContent(".getNode(");
				jsg_GetTopoParam.getIndex().visit(this);
				genLanguage.appendContent(")");
			}
		}else {
			genLanguage.appendContent("curleftPattern.getNode("+jsg_GetTopoParam.getNode().getID()+")");
		}
		return false;
	}

	@Override
	public Boolean accept(JSG_GMapSize jsg_GMapSize) {
		genLanguage.appendContent("gmap.size()");
		return false;
	}

	@Override
	public Boolean accept(JSG_Header jsg_Header) {
		if (jsg_Header.getLanguage() == null || "java".equalsIgnoreCase(jsg_Header.getLanguage())) {
			// headerCode += jsg_Header.getCode() + "\n";
			genLanguage.appendInclude(jsg_Header.getCode());
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_HookCall jsHookCall) {
		// HAK: JE HAIS TON TRUC!
		genLanguage.appendContent(hookListName + ".addCol(");
		jsHookCall.getNodename().visit(this);
		genLanguage.appendContentln(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_If jsIf) throws RuntimeException {
		indent();
		genLanguage.appendContent("if(");
		if (jsIf.getCondition() != null)
			jsIf.getCondition().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(") ");
		if (jsIf.getConsequence() != null && !(jsIf.getConsequence() instanceof JSG_NOP))
			jsIf.getConsequence().visit(this);
		else {
			genLanguage.appendContentln("{}");
			lineJumped = true;
		}
		// on ne peut pas recup le type de l'expr/inst?
		if (jsIf.getAlternant() != null && !(jsIf.getAlternant() instanceof JSG_NOP)) {
			indent();
			genLanguage.appendContent("else ");
			jsIf.getAlternant().visit(this);
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_Index jsIndex) throws RuntimeException {
		// HAK: j'ai du mal a comprendre cet item
		jsIndex.getVariable().visit(this);
		genLanguage.appendContent(".get(");
		jsIndex.getIndex().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_IndexInLeftPattern jsg_IndexInLeftPattern) {
		(new JSG_Index(new JSG_Index(new JSG_KeywordLeftFilter(-1, -1), jsg_IndexInLeftPattern.getIndexInDartList()),
				jsg_IndexInLeftPattern.getHookIndex())).visit(this);
		return false;
	}

	@Override
	public Boolean accept(JSG_IndexInRuleResult jsIndexInRuleResultSem) {
		jsIndexInRuleResultSem.getVariable().visit(this);
		genLanguage.appendContent(".get(");
		jsIndexInRuleResultSem.getIndexFirst().visit(this);
		if(jsIndexInRuleResultSem.getIndexSecond()!=null) {
			genLanguage.appendContent(").get(");
			jsIndexInRuleResultSem.getIndexSecond().visit(this);
		}
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_IndexNodeInGmap jsIndexNodeInGmapSem) {
		// HAK: ????
		genLanguage.appendContent("getNode(");
		jsIndexNodeInGmapSem.getIndex().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_IndexRuleNode jsg_IndexRuleNode) {
		(new JSG_Rule(jsg_IndexRuleNode.getRule().getName(), jsg_IndexRuleNode.getRule(), jsg_IndexRuleNode.getLine(),
				jsg_IndexRuleNode.getColumn())).visit(this);
		genLanguage.appendContent(".get");
		switch (jsg_IndexRuleNode.getSide()) {
		case LEFT:
			genLanguage.appendContent("Left");
			break;
		case RIGHT:
			genLanguage.appendContent("Right");
		default:
			break;
		}
		genLanguage.appendContent("IndexRuleNode(\"");
		genLanguage.appendContent(jsg_IndexRuleNode.getNodeName());
		genLanguage.appendContent("\")");
		return false;
	}

	@Override
	public Boolean accept(JSG_Indirection jsg_Indirection) {
		jsg_Indirection.getExp().visit(this);
		return false;
	}

	@Override
	public Boolean accept(JSG_InScope jsInScope) throws RuntimeException {
		jsInScope.getLeft().visit(this);
		genLanguage.appendContent(".");
		return jsInScope.getRight().visit(this);
	}

	@Override
	public Boolean accept(JSG_InScopeStatic jsg_InScopeStatic) {
		jsg_InScopeStatic.getLeft().visit(this);
		genLanguage.appendContent(".");
		return jsg_InScopeStatic.getRight().visit(this);
	}

	@Override
	public Boolean accept(JSG_Integer jsInteger) throws RuntimeException {
		genLanguage.appendContent(jsInteger.getValue());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_IsMarked jsg_IsMarked) {
		jsg_IsMarked.getLeft().visit(this);
		genLanguage.appendContent(".isMarked(");
		jsg_IsMarked.getMark().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_IsNotMarked jsg_IsNotMarked) {
		jsg_IsNotMarked.getLeft().visit(this);
		genLanguage.appendContent(".isNotMarked(");
		jsg_IsNotMarked.getMark().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordDimension jsKeywordDimension) {
		genLanguage.appendContent("modeler.getDimension()");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordEbd jsKeywordEbd) {
		// System.err.println("keywordEbd");
		if (jsKeywordEbd.getType() != null)
			genLanguage.appendContent(jsKeywordEbd.getType());
		else
			genLanguage.appendContent("Object");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordGmap jsGmapKeyword) throws RuntimeException {
		// HAK: est-ce utile? toujours applicable? bon a voir
		genLanguage.appendContent("gmap");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordHook jsKeywordHook) {
		genLanguage.appendContent("hooks");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordLeftFilter jsg_KeywordLeftFilter) {
		genLanguage.appendContent("leftPattern");
		return null;
	}

	@Override
	public Boolean accept(JSG_KeywordModeler jsKeywordModeler) {
		if (jsKeywordModeler.getModeler() != null) {
			genLanguage.appendContent("((");
			genLanguage.appendContent(jsKeywordModeler.getModeler().getModule());
			if (jsKeywordModeler.getModeler().getModule().replaceAll("\\s", "").length() > 0) {
				genLanguage.appendContent(".");
			}
			genLanguage.appendContent(jsKeywordModeler.getModeler().getName());
			genLanguage.appendContent(")");
		}
		genLanguage.appendContent("modeler");
		if (jsKeywordModeler.getModeler() != null) {
			genLanguage.appendContent(")");
		}
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordRightFilter jsg_KeywordLeftFilter) {
		genLanguage.appendContent("rightPattern");
		return null;
	}

	@Override
	public Boolean accept(JSG_LeftRuleNode jsg_TopoParam) {
//		if (jsg_TopoParam.getRule() instanceof JMERuleAtomic) {
//			genLanguage.appendContent(jsg_TopoParam.getName());
//			genLanguage.appendContent("()");
//		} else {
//			(new JSG_KeywordHook()).visit(this);
//			genLanguage.appendContent("[");
//			genLanguage.appendContent(jsg_TopoParam.getName());
//			genLanguage.appendContent("()]");
//		}
/*
		if(glue.getLangagesState() == LanguageState.CLASSICAL) {
			if(glue.getCurrentRule() instanceof JMERuleAtomic) {
				genLanguage.appendContent("curleftPattern.getNode(");
			}else if(glue.getCurrentRule() instanceof JMEScript 
//					&& glue.getCurrentRule().getName().compareTo(jsg_TopoParam.getName())==0
					) {
				genLanguage.appendContent("((JerboaInputHooksGeneric)hooks).getCol(");
			}
		}else if(   glue.getLangagesState() == LanguageState.PRECONDITION 
				 || glue.getLangagesState() == LanguageState.MIDPROCESS) {
			genLanguage.appendContent("curleftPattern.getNode(");
		}
		genLanguage.appendContent(jsg_TopoParam.getRule().getLeft().getMatchNode(jsg_TopoParam.getName()).getID());
		if(glue.getLangagesState() == LanguageState.CLASSICAL
				|| glue.getLangagesState() == LanguageState.PRECONDITION
				|| glue.getLangagesState() == LanguageState.MIDPROCESS) {
			genLanguage.appendContent(")");
		}
		*/
		
		
		if(glue.getLangagesState() == LanguageState.CLASSICAL) {
			if(glue.getCurrentRule() instanceof JMERuleAtomic) {
				genLanguage.appendContent("curleftPattern.getNode(");
			}else if(glue.getCurrentRule() instanceof JMEScript) {
				genLanguage.appendContent("((JerboaInputHooksGeneric)hooks).getCol(");
			}
		}else if(glue.getLangagesState() == LanguageState.PRECONDITION
				|| glue.getLangagesState() == LanguageState.MIDPROCESS) {
			genLanguage.appendContent("leftPattern.get(");
		}
		genLanguage.appendContent(jsg_TopoParam.getRule().getLeft().getMatchNode(jsg_TopoParam.getName()).getID());
		if(glue.getLangagesState() == LanguageState.CLASSICAL) {
			genLanguage.appendContent(")");
		}else if(glue.getLangagesState() == LanguageState.PRECONDITION
				|| glue.getLangagesState() == LanguageState.MIDPROCESS) {
			genLanguage.appendContent(")");			
		}

		return true;
	}

	@Override
	public Boolean accept(JSG_List jsList) {
		genLanguage.appendContent("java.util.List<");
		jsList.getTypedList().visit(this);
		genLanguage.appendContent(">");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Long jsLong) throws RuntimeException {
		genLanguage.appendContent(jsLong.getValue());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Map jsMap) {
		// TODO: implement
		System.err.println("map has not been yet implemented");
		return null;
	}

	// TODO: Val : il faut corriger les mark et unmark je ne sais plus quelle
	// est la syntaxe exacte
	@Override
	public Boolean accept(JSG_Mark jsg_Mark) {
		genLanguage.appendContent("gmap.");
		if (jsg_Mark.getLeft() instanceof JSG_Collect) {
			genLanguage.appendContent("markOrbit(");
			((JSG_Collect) jsg_Mark.getLeft()).getNode().visit(this);
			genLanguage.appendContent(",");
			((JSG_Collect) jsg_Mark.getLeft()).getOrbit().visit(this);
			genLanguage.appendContent(", ");
			jsg_Mark.getMark().visit(this);
		} else { // TODO: Val: en C++ la mark est un coup en 1er un coup en 2e
			// c'est n'importe quoi ...
			genLanguage.appendContent("mark(");
			jsg_Mark.getMark().visit(this);
			genLanguage.appendContent(", ");
			jsg_Mark.getLeft().visit(this);
		}
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_New jsg_New) {
		if (!(jsg_New.getExp() instanceof JSG_Constructor))
			genLanguage.appendContent("new ");
		// HAK: je ne comprends pas trop?
		jsg_New.getExp().visit(this);
		return false;
	}

	@Override
	public Boolean accept(JSG_NOP jsEmpty) {
		return false;
	}

	@Override
	public Boolean accept(JSG_Not jsNot) throws RuntimeException {
		genLanguage.appendContent("!(");
		jsNot.getExpr().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Null jsg_Null) {
		genLanguage.appendContent("null");
		return false;
	}

	@Override
	public Boolean accept(JSG_Operator jsOperator) throws RuntimeException {
		if (!(jsOperator.getOperator() == JSOperatorKind.INC || jsOperator.getOperator() == JSOperatorKind.DEC)) {
			genLanguage.appendContent("(");
		}
		boolean first = true;
		for (final JSG_Expression e : jsOperator) {
			if (first) {
				first = false;
			} else {
				genLanguage.appendContent(" " + jsOperator.getOperator().toCode() + " ");
			}
			if (e != null)
				e.visit(this);
		}
		if (!(jsOperator.getOperator() == JSOperatorKind.INC || jsOperator.getOperator() == JSOperatorKind.DEC)) {
			genLanguage.appendContent(")");
		}
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Orbit jsOrbit) throws RuntimeException {
		genLanguage.appendContent("JerboaOrbit.orbit(");
		final List<JSG_Expression> dims = jsOrbit.getDimensions();
		// genLanguage.appendContent(dims.size() + ",");
		for (int i = 0; i < dims.size(); i++) {
			final JSG_Expression e = dims.get(i);
			e.visit(this);
			if (i < dims.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_PackagedType jsType) {
		jsType.getLeft().visit(this);
		genLanguage.appendContent(".");
		return jsType.getRight().visit(this);
	}

	@Override
	public Boolean accept(JSG_Print jsPrint) {

		final List<JSG_Expression> args = jsPrint.getArguments();
		for (int i = 0; i < args.size(); i++) {
			final JSG_Expression e = args.get(i);
			indent();
			if (i == args.size() - 1)
				genLanguage.appendContent("System.out.println(");
			else
				genLanguage.appendContent("System.out.print(");
			e.visit(this);
			genLanguage.appendContentln(");");
			lineJumped = true;
			// if (i < args.size() - 1) {
			// genLanguage.appendContent(" + ");
			// }
		}
		// genLanguage.appendContentln(");");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Return jsReturn) throws RuntimeException {
		genLanguage.appendContent("return ");
		if (jsReturn.getExpression() != null)
			jsReturn.getExpression().visit(this);
		else
			genLanguage.appendContent("null");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Rule jsRule) {
		String ruleName = "";
		if(jsRule.getName()==null) {
			ruleName = glue.getCurrentRule().getName().replaceAll(" ", "_");
		}else {
			ruleName = jsRule.getName().replaceAll(" ", "_");
		}
		ruleName = ruleName.substring(0, 1).toUpperCase() + ruleName.substring(1);
		genLanguage.appendContent("((" + ruleName + ")modeler.getRule(\"" + jsRule.getName() + "\"))");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_RuleArg jsg_RuleArg) {
		return jsg_RuleArg.getArgValue().visit(this);
	}

	@Override
	public Boolean accept(JSG_RuleNode jsRuleNodeSem) {
		// System.err.println("coucou");
//		jsRuleNodeSem.getVarExp().visit(this);
//		genLanguage.appendContent(".get(");
		jsRuleNodeSem.getRule().visit(this);
		genLanguage.appendContent(".getRightIndexRuleNode(\"");
		jsRuleNodeSem.getNodeName().visit(this);
		genLanguage.appendContent("\")");
//		genLanguage.appendContent(").get(");
//		jsRuleNodeSem.getExp().visit(this);
//		genLanguage.appendContent(")");

		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Sequence jsSequence) throws RuntimeException {
		for (final JSG_Instruction inst : jsSequence) {
			indent();
			Boolean r = false;
			if (inst != null) {
				r = inst.visit(this);
				if (!r) {
					if (!lineJumped && !(inst instanceof JSG_NOP))
						genLanguage.appendContentln(";");
				}
			}
			lineJumped = true;
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_Size jsSizeSem) {
		// HAK: QUOI??????
		// TODO: enlever ?
		genLanguage.appendContent(".size()");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_String jsString) throws RuntimeException {
		genLanguage.appendContent("\"" + jsString.getValue() + "\"");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Throw jsg_Throw) {
		genLanguage.appendContent("throw ");
		jsg_Throw.getExpr().visit(this);
		return false;
	}

	@Override
	public Boolean accept(JSG_Try jsg_Try) {
		indent();
		genLanguage.appendContent("try");
		jsg_Try.getTryBlock().visit(this);
		if (jsg_Try.getCatchList() != null) {
			for (JSG_Catch c : jsg_Try.getCatchList()) {
				c.visit(this);
			}
		}
		if (jsg_Try.getFinallyBlock() != null) {
			indent();
			genLanguage.appendContent("finally");
			if (!(jsg_Try.getFinallyBlock() instanceof JSG_NOP))
				jsg_Try.getFinallyBlock().visit(this);
			else
				genLanguage.appendContent("{}");
		}
		genLanguage.appendContentln("");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Type jsType) {
		if (jsType.getType().compareToIgnoreCase("string") == 0)
			// TODO: faire un type_String ? HAK: oui pourquoi pas mais qui doit
			// se traduire dans un type du langage cible
			genLanguage.appendContent("String");
		else
			genLanguage.appendContent(jsType.getType());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeBoolean jsg_TypeBoolean) {
		genLanguage.appendContent("boolean");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeJerboaDart jsTypeJerboaNodeSem) {
		genLanguage.appendContent("JerboaDart");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeJerboaHookList jsHookList) throws RuntimeException {
		genLanguage.appendContent("JerboaInputHooksGeneric");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeJerboaRule jsTypeJerboaRuleSem) {
		if (jsTypeJerboaRuleSem.getRuleName() != null)
			genLanguage.appendContent(jsTypeJerboaRuleSem.getRuleName());
		else
			genLanguage.appendContent("JerboaRuleGenerated");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeJerboaRuleResult jsTypeJerboaRuleResultSem) {
		genLanguage.appendContent("JerboaRuleResult");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeMark jsTypeMark) {
		genLanguage.appendContent("JerboaMark");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeOrbit jsTypeOrbit) {
		genLanguage.appendContent("JerboaOrbit");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypePrimitive jsg_TypePrimitive) {
		if (jsg_TypePrimitive instanceof JSG_TypeString)
			genLanguage.appendContent("String");
		else
			genLanguage.appendContent(jsg_TypePrimitive.getType());
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeString jsg_TypePrimitive) {
		genLanguage.appendContent("String");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeTemplate jsTypeTemplate) {
		jsTypeTemplate.getBaseType().visit(this);
		genLanguage.appendContent("<");
		for (int i = 0; i < jsTypeTemplate.getTypes().size(); i++) {
			jsTypeTemplate.getTypes().get(i).visit(this);
			if (i < jsTypeTemplate.getTypes().size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		genLanguage.appendContent(">");
		return false;
	}

	@Override
	public Boolean accept(JSG_UnMark jsg_UnMark) {
		genLanguage.appendContent("gmap.");
		if (jsg_UnMark.getLeft() instanceof JSG_Collect) {
			genLanguage.appendContent("unmarkOrbit(");
			((JSG_Collect) jsg_UnMark.getLeft()).getNode().visit(this);
			genLanguage.appendContent(",");
			((JSG_Collect) jsg_UnMark.getLeft()).getOrbit().visit(this);
			genLanguage.appendContent(", ");
			jsg_UnMark.getMark().visit(this);
		} else {
			genLanguage.appendContent("unmark(");
			jsg_UnMark.getMark().visit(this);
			genLanguage.appendContent(", ");
			jsg_UnMark.getLeft().visit(this);
		}
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_Unreference jsg_Unreference) {
		jsg_Unreference.getExp().visit(this);
		return false;
	}

	@Override
	public Boolean accept(JSG_UserType jsUserTypeSem) {
		if (jsUserTypeSem.getEbdInfo() != null)
			genLanguage.appendContent(jsUserTypeSem.getType());
		else
			genLanguage.appendContent("Object");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Variable jsVariable) throws RuntimeException {
		genLanguage.appendContent(jsVariable.getName());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_While jsWhile) throws RuntimeException {
		indent();
		genLanguage.appendContent("while(");
		jsWhile.getCondition().visit(this);
		genLanguage.appendContent(")");
		lineJumped = true;
		jsWhile.getCorps().visit(this);
		genLanguage.appendContentln("");
		lineJumped = true;
		return true;
	}

	@Override
	public void beginGeneration(JSG_Sequence js) {
		if (glue.getLangagesState() != LanguageGlue.LanguageState.PRECONDITION
				&& glue.getLangagesState() != LanguageGlue.LanguageState.HEADER
				&& glue.getLangagesState() != LanguageGlue.LanguageState.POSTPROCESS
				&& glue.getLangagesState() != LanguageGlue.LanguageState.PREPROCESS
				&& glue.getLangageType() != LanguageType.EMBEDDING) {
			if (js.get(0) instanceof JSG_Declare
					&& ((JSG_Declare) js.get(0)).getType() instanceof JSG_TypeJerboaHookList) {
				System.err.println("HAK: je ne comprends pas ici");
				hookListName = ((JSG_Declare) js.get(0)).getName();
			} else {
				// genLanguage.appendContentln("List<JerboaDart> _hn = new
				// ArrayList<>();");
				hookListName = "_hn";
			}
		}
		accept(js);
		listOfDeclaration.clear();
		listOfDeclaration.add(new ArrayList<String>());
		importedRules = new ArrayList<>();

		// for (String s : mapResultToRule.keySet()) {
		// genLanguage.appendContentln("delete " + s + ";");
		// }
	}

	@Override
	public GeneratedLanguage getResult() {
		return genLanguage;
	}

	private void indent() {
		if (lineJumped)
			for (int i = 0; i < tab; i++) {
				genLanguage.appendContent("   ");
			}
		lineJumped = false;
	}
}
