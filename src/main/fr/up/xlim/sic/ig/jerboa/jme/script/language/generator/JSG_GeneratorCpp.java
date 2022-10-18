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
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
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
public class JSG_GeneratorCpp implements Generator_G {

	private boolean oldEngine = false;
	private JMEModeler modeler;

	private GeneratedLanguage genLanguage;
	private LanguageGlue glue;

	private int tab = 0;
	private String errorParseMsg = "error in parsing";

	private boolean lineJumped;
	// private boolean inAssignement;

	private ArrayList<ArrayList<String>> listOfDeclaration;

	private Map<String, JSG_Type> mapVarToType;

	private String hookListName = "_hn";

	private void indent() {
		if (lineJumped)
			for (int i = 0; i < tab; i++) {
				genLanguage.appendContent("   ");
			}
		lineJumped = false;
	}

	@Override
	public GeneratedLanguage getResult() {
		return genLanguage;
	}

	// private String getFreshVariableName(final String base) {
	// String prefix = "_v_";
	// int cpt = 0;
	// while (mapVarToType.containsKey(prefix + base + cpt)) {
	// cpt++;
	// }
	// return prefix + base + cpt;
	// }

	// private void prepareRule(JSG_ApplyRule ap) {
	// for (JSG_HookCall h : ap.getArgs()) {
	// indent();
	// h.visit(this);
	// }
	// if (ap.getArgs().size() > 0)
	// lineJumped = true;
	// }

	public JSG_GeneratorCpp(LanguageGlue _glue, GeneratedLanguage genL, JMEModeler _modeler) {
		tab = 0;
		modeler = _modeler;
		lineJumped = false;
		glue = _glue;
		listOfDeclaration = new ArrayList<>();
		listOfDeclaration.add(new ArrayList<String>());
		mapVarToType = new HashMap<String, JSG_Type>();
		genLanguage = new GeneratedLanguage(genL);
		if (glue.getLangagesState() != LanguageGlue.LanguageState.PRECONDITION
				&& glue.getLangagesState() != LanguageGlue.LanguageState.HEADER
				&& glue.getLangagesState() != LanguageGlue.LanguageState.POSTPROCESS
				&& glue.getLangagesState() != LanguageGlue.LanguageState.PREPROCESS) {
			hookListName = "_hn";
		}
	}

	@Override
	public void beginGeneration(JSG_Sequence js) {
		genLanguage = new GeneratedLanguage();
		if (glue.getLangagesState() != LanguageGlue.LanguageState.PRECONDITION
				&& glue.getLangagesState() != LanguageGlue.LanguageState.HEADER
				&& glue.getLangagesState() != LanguageGlue.LanguageState.POSTPROCESS
				&& glue.getLangagesState() != LanguageGlue.LanguageState.PREPROCESS) {
			if (js.get(0) instanceof JSG_Declare
					&& ((JSG_Declare) js.get(0)).getType() instanceof JSG_TypeJerboaHookList) {
				hookListName = ((JSG_Declare) js.get(0)).getName();
			} else {
				// genLanguage.appendContentln("JerboaHookNode _hn;");
				hookListName = "_hn";
			}
		}
		accept(js);
		listOfDeclaration.clear();
		listOfDeclaration.add(new ArrayList<String>());
		// for (String s : mapResultToRule.keySet()) {
		// genLanguage.appendContentln("delete " + s + ";");
		// }
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
	public Boolean accept(JSG_Variable jsVariable) throws RuntimeException {
		genLanguage.appendContent(jsVariable.getName());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Orbit jsOrbit) throws RuntimeException {
		genLanguage.appendContent("JerboaOrbit(");
		final List<JSG_Expression> dims = jsOrbit.getDimensions();
		if (dims.size() > 0) {
			genLanguage.appendContent(dims.size());
			genLanguage.appendContent(",");
			for (int i = 0; i < dims.size(); i++) {
				final JSG_Expression e = dims.get(i);
				e.visit(this);
				if (i < dims.size() - 1) {
					genLanguage.appendContent(",");
				}
			}
		}
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_InScope jsInScope) throws RuntimeException {
		jsInScope.getLeft().visit(this);
		genLanguage.appendContent(".");
		return jsInScope.getRight().visit(this);
	}

	@Override
	public Boolean accept(JSG_CollectEbd jsCollect) throws RuntimeException {
		if (jsCollect.gmapHasDirectAccess()) {
			genLanguage.appendContent("gmap->");
		} else
			genLanguage.appendContent("_owner->gmap()->");
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
		genLanguage.appendContent("");
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Collect jsCollect) throws RuntimeException {
		if (jsCollect.gmapHasDirectAccess()) {
			genLanguage.appendContent("gmap->");
		} else
			genLanguage.appendContent("_owner->gmap()->");
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
	public Boolean accept(JSG_Index jsIndex) throws RuntimeException {
		jsIndex.getVariable().visit(this);
		genLanguage.appendContent("[");
		jsIndex.getIndex().visit(this);
		genLanguage.appendContent("]");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Alpha jsAlpha) throws RuntimeException {
		jsAlpha.getNode().visit(this);
		genLanguage.appendContent("->alpha(");
		jsAlpha.getDim().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Integer jsInteger) throws RuntimeException {
		genLanguage.appendContent(jsInteger.getValue());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Float jsFloat) throws RuntimeException {
		genLanguage.appendContent(jsFloat.getValue());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Double jsDouble) throws RuntimeException {
		genLanguage.appendContent(jsDouble.getValue());
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Boolean jsBoolean) throws RuntimeException {
		genLanguage.appendContent(jsBoolean.getValue());
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
	public Boolean accept(JSG_String jsString) throws RuntimeException {
		genLanguage.appendContent("\"" + jsString.getValue() + "\"");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_ApplyRule jsApplyRule) throws RuntimeException {
		// if (!inAssignement)
		// prepareRule(jsApplyRule);
		// indent();
		// jsApplyRule.getRule().visit(this);
		// genLanguage.appendContentln("->applyRule(" + hookListName +
		// ",JerboaRuleResultType::"
		// + jsApplyRule.getReturnType() + ");");
		// lineJumped = true;
		// indent();
		// genLanguage.appendContentln(hookListName + ".clear();");
		// lineJumped = true;
		jsApplyRule.getRuleExpr().visit(this);
		genLanguage.appendContent("->applyRule(gmap");

		for (JSG_Expression ra : jsApplyRule.getArgs()) {
			genLanguage.appendContent(", ");
			ra.visit(this);
		}
		genLanguage.appendContent(", JerboaRuleResultType::");
		switch (jsApplyRule.getReturnType()) {
		case NONE:
			genLanguage.appendContent("NONE");
			break;
		case FULL:
			genLanguage.appendContent("FULL");
			break;
		default:
			break;
		}
		genLanguage.appendContent(")");
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
	public Boolean accept(JSG_Not jsNot) throws RuntimeException {
		genLanguage.appendContent("!(");
		jsNot.getExpr().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Return jsReturn) throws RuntimeException {
		genLanguage.appendContent("return ");
		if (jsReturn.getExpression() != null)
			jsReturn.getExpression().visit(this);
		else
			(new JSG_Constructor(new JSG_TypeJerboaRuleResult(jsReturn.getLine(), jsReturn.getColumn()),
					new ArrayList<>())).visit(this);
		genLanguage.appendContentln(";");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_KeywordGmap jsGmapKeyword) throws RuntimeException {
		if (jsGmapKeyword.hasDirectAccess()) {
			genLanguage.appendContent("gmap");
		} else
			genLanguage.appendContent("_owner->gmap()");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Type jsType) {
		if (jsType instanceof JSG_TypeBoolean) {
			genLanguage.appendContent("bool");
		} else if (jsType instanceof JSG_TypeString) {
			genLanguage.appendContent("std::string");
		} else
			genLanguage.appendContent(jsType.getType().replaceAll("\\.", "::"));
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_HookCall jsHookCall) {
		genLanguage.appendContent(hookListName + ".push(");
		jsHookCall.getNodename().visit(this);
		genLanguage.appendContentln(");");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Choice jsAlternativ) {
		indent();
		lineJumped = true;
		for (int i = 0; i < jsAlternativ.getOptions().size(); i++) {
			// JSG_ApplyRule apr = jsAlternativ.getOptions().get(i);
			indent();
			genLanguage.appendContentln("try{");
			tab++;
			lineJumped = true;
			indent();
			// prepareRule(apr);
			if (jsAlternativ.getVarResult() != null) {
				genLanguage.appendContent(jsAlternativ.getVarResult() + " = ");
				lineJumped = false;
			}
			jsAlternativ.getOptions().get(i).visit(this);
			genLanguage.appendContentln("");
			lineJumped = true;
			tab--;
			indent();
			genLanguage.appendContentln("}catch(JerboaException e){");
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
	public Boolean accept(JSG_KeywordHook jsKeywordHook) {
		if (glue.getLangagesState() == LanguageState.CLASSICAL && glue.getCurrentRule() instanceof JMERuleAtomic ) {
			genLanguage.appendContent("curLeftFilter");
		}else {
			genLanguage.appendContent("sels");
		}
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordEbd jsKeywordEbd) {
		// System.err.println("keywordEbd");
		if (jsKeywordEbd.getType() != null)
			genLanguage.appendContent(jsKeywordEbd.getType().replaceAll("\\.", "::"));
		else
			genLanguage.appendContent("JerboaEmbedding*");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_List jsList) {
		genLanguage.appendContent("std::vector<");
		// genLanguage.appendContent("/*");
		// if (jsList.getTypedList() instanceof JSG_UserType) {
		// genLanguage.appendContent("UT: ");
		// if (((JSG_UserType) jsList.getTypedList()).getType() != null)
		// genLanguage.appendContent(((JSG_UserType)
		// jsList.getTypedList()).getType());
		// else
		// genLanguage.appendContent("NULL");
		// }
		// genLanguage.appendContent("*/");
		jsList.getTypedList().visit(this);
		genLanguage.appendContent(">");
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
		genLanguage.appendContent("((" + ruleName + "*)_owner->rule(\"" + ruleName + "\"))");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Print jsPrint) {
		genLanguage.appendContent("std::cout << ");
		final List<JSG_Expression> args = jsPrint.getArguments();
		for (int i = 0; i < args.size(); i++) {
			final JSG_Expression e = args.get(i);
			e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(" << ");
			}
		}
		genLanguage.appendContentln("<< std::flush;");
		// TODO : val : je force le flush car j'en ai besoin, mais en vrai ça
		// se discute
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_KeywordDimension jsKeywordDimension) {
		genLanguage.appendContent("_owner->dimension()");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordModeler jsKeywordModeler) {
		if (jsKeywordModeler.getModeler() != null) {
			genLanguage.appendContent("((");
			genLanguage.appendContent(jsKeywordModeler.getModeler().getModule().replaceAll("\\.", "::"));
			if (jsKeywordModeler.getModeler().getModule().replaceAll("\\s", "").length() > 0) {
				genLanguage.appendContent("::");
			}
			genLanguage.appendContent(jsKeywordModeler.getModeler().getName());
			genLanguage.appendContent("*)");
		}
		genLanguage.appendContent("_owner");
		if (jsKeywordModeler.getModeler() != null) {
			genLanguage.appendContent(")");
		}
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_Size jsSizeSem) {
		genLanguage.appendContent(".size()");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_UserType jsUserTypeSem) {
		if (jsUserTypeSem.getEbdInfo() != null)
			genLanguage.appendContent(jsUserTypeSem.getType().replaceAll("\\.", "::"));
		else
			genLanguage.appendContent("JerboaEmbedding*");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_RuleNode jsRuleNodeSem) {
//		jsRuleNodeSem.getVarExp().visit(this);
//		genLanguage.appendContent(".");
//		genLanguage.appendContent("get(");
		jsRuleNodeSem.getRule().visit(this);
		genLanguage.appendContent("->indexRightRuleNode(\"");
		jsRuleNodeSem.getNodeName().visit(this);
		genLanguage.appendContent("\")");
//		genLanguage.appendContent(", ");
//		jsRuleNodeSem.getExp().visit(this);
//		genLanguage.appendContent(")");

		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeJerboaRuleResult jsTypeJerboaRuleResultSem) {
		// genLanguage.appendContent("JerboaMatrix<JerboaDart*>*");
		genLanguage.appendContent("JerboaRuleResult");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeJerboaDart jsTypeJerboaDartSem) {
		genLanguage.appendContent("JerboaDart*");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_AddInList jsAddInListSem) {
		genLanguage.appendContent("push_back(");
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
	public Boolean accept(JSG_TypeJerboaRule jsTypeJerboaRuleSem) {
		if (jsTypeJerboaRuleSem.getRuleName() != null)
			genLanguage.appendContent(jsTypeJerboaRuleSem.getRuleName() + "*");
		else
			genLanguage.appendContent("JerboaRuleOperation*");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_IndexInRuleResult jsIndexInRuleResultSem) {
		jsIndexInRuleResultSem.getVariable().visit(this);
		if (oldEngine)
			genLanguage.appendContent(".");
		else
			genLanguage.appendContent("->");
		genLanguage.appendContent("get(");
		jsIndexInRuleResultSem.getIndexFirst().visit(this);
		if(jsIndexInRuleResultSem.getIndexSecond()!=null) {
			genLanguage.appendContent(",");
			jsIndexInRuleResultSem.getIndexSecond().visit(this);
		}
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_IndexNodeInGmap jsIndexNodeInGmapSem) {
		genLanguage.appendContent("node(");
		jsIndexNodeInGmapSem.getIndex().visit(this);
		genLanguage.appendContent(")");
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_While jsWhile) throws RuntimeException {
		indent();
		genLanguage.appendContent("while(");
		jsWhile.getCondition().visit(this);
		genLanguage.appendContentln(")");
		lineJumped = true;
		indent();
		jsWhile.getCorps().visit(this);
		genLanguage.appendContentln("");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Assignment jsAssignment) throws RuntimeException {
		// inAssignement = true;

		// final JSG_Expression e = jsAssignment.getValue();
		// if (e != null && e instanceof JSApplyRule)
		// prepareRule((JSG_ApplyRule) e);
		boolean b;
		jsAssignment.getVariable().visit(this);
		genLanguage.appendContent(" = ");
		if (jsAssignment.getVariable() instanceof JSG_Variable) {
			// if (mapVarToType.get(((JSG_Variable)
			// jsAssignment.getVariable()).getName()) instanceof JSG_UserType) {
			// genLanguage.appendContent("new ");
			// mapVarToType.get(((JSG_Variable)jsAssignment.getVariable()).getName()).visit(this);
			// genLanguage.appendContent("(");
			// }
			b = jsAssignment.getValue().visit(this);
			// if
			// (mapVarToType.get(((JSG_Variable)jsAssignment.getVariable()).getName())
			// instanceof JSG_UserType) {
			// genLanguage.appendContent(")");
			// }
		} else {
			return jsAssignment.getValue().visit(this);
		}
		return b;
	}

	@Override
	public Boolean accept(JSG_Block jsBlock) throws RuntimeException {
		// if (jsBlock.hasBracket())
		genLanguage.appendContentln("{");

		lineJumped = true;
		tab++;
		if (!(jsBlock.getBody() instanceof JSG_Sequence))
			indent();
		if (jsBlock.getBody() != null)
			try {
				jsBlock.getBody().visit(this);
			} catch (Exception e) {
				System.err.println(e);
			}
		if (!lineJumped) {
			if (!(jsBlock.getBody() instanceof JSG_Sequence)) {
				genLanguage.appendContentln(";");
			} else
				genLanguage.appendContentln("");
		}
		lineJumped = true;
		tab--;
		indent();
		// if (jsBlock.hasBracket())
		genLanguage.appendContentln("}");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_For jsFor) throws RuntimeException {
		indent();
		genLanguage.appendContent("for(");
		if (jsFor.getType() != null)
			genLanguage.appendContent(jsFor.getType().getType().replaceAll("\\.", "::"));
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
		genLanguage.appendContent("for(");
		jsForEach.getType().visit(this);
		genLanguage.appendContent(" ");
		genLanguage.appendContent(jsForEach.getName());
		genLanguage.appendContent(": ");
		jsForEach.getColl().visit(this);
		genLanguage.appendContent(")");
		if (jsForEach.getBody() != null)
			jsForEach.getBody().visit(this);
		else
			genLanguage.appendContentln("{}");
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
	public Boolean accept(JSG_If jsIf) throws RuntimeException {
		indent();
		genLanguage.appendContent("if(");
		if (jsIf.getCondition() != null)
			jsIf.getCondition().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(") ");
		if (jsIf.getConsequence() != null)
			jsIf.getConsequence().visit(this);
		else {
			genLanguage.appendContentln("{}");
			lineJumped = true;
		}
		if (jsIf.getAlternant() != null && !(jsIf.getAlternant() instanceof JSG_NOP)) {
			indent();
			genLanguage.appendContent("else ");
			jsIf.getAlternant().visit(this);
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_Sequence jsSequence) throws RuntimeException {
		for (final JSG_Instruction inst : jsSequence) {
			indent();
			boolean r = false;
			if (inst != null) {
				r = inst.visit(this);
				if (!r) {
					if (!lineJumped)
						genLanguage.appendContentln(";");
				}
			}
			lineJumped = true;
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_DoWhile jsDoWhile) throws RuntimeException {
		indent();
		genLanguage.appendContent("do");
		jsDoWhile.getBody().visit(this);
		indent();
		genLanguage.appendContent("while(");
		jsDoWhile.getCondition().visit(this);
		genLanguage.appendContentln(");");
		return true;
	}

	@Override
	public Boolean accept(JSG_ExprInstruction jsExprInstruction) throws RuntimeException {
		return jsExprInstruction.getExpr().visit(this);
	}

	@Override
	public Boolean accept(JSG_Declare jsDeclare) throws RuntimeException {
		jsDeclare.getType().visit(this);
		if ((jsDeclare.getValue() != null && jsDeclare.getValue() instanceof JSG_New)
				|| jsDeclare.getType() instanceof JSG_TypeJerboaRuleResult)
			genLanguage.appendContent("*");

		mapVarToType.put(jsDeclare.getName(), jsDeclare.getType());
		genLanguage.appendContent(" " + jsDeclare.getName());
		lineJumped = false;
		if (jsDeclare.getValue() != null
				&& !((jsDeclare.getType() instanceof JSG_List) && (jsDeclare.getValue() instanceof JSG_Constructor))) {
			genLanguage.appendContent(" = ");
			jsDeclare.getValue().visit(this);
		} else if (jsDeclare.getValue() == null && jsDeclare.getType() instanceof JSG_TypeJerboaRuleResult) {
			// genLanguage.appendContent(" = new
			// JerboaMatrix<JerboaDart*>(0,0)");
			if (!oldEngine)
				genLanguage.appendContent(" = new JerboaRuleResult(this)");
			// TODO : faire �a dans la 2e traduction d'arbre car il ne faut pas
			// le faire � chaque fois!
		}

		return false;
	}

	@Override
	public Boolean accept(JSG_Map jsMap) {
		// TODO: implement
		System.err.println("map has not been yet implemented");
		return true;
	}

	@Override
	public Boolean accept(JSG_NOP jsEmpty) {
		genLanguage.appendContent("/* NOP */");
		return true;
	}

	@Override
	public Boolean accept(JSG_AssocParam jsAssocParam) {
		jsAssocParam.getRule().visit(this);
		genLanguage.appendContent("->set");
		genLanguage.appendContent(jsAssocParam.getParamName());
		genLanguage.appendContent("(");
		jsAssocParam.getParamValue().visit(this);
		genLanguage.appendContentln(");");
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Constructor jsg_Constructor) {
		// genLanguage.appendContent("( "); // ### new
		if (jsg_Constructor.getName() instanceof JSG_List
				|| jsg_Constructor.getName() instanceof JSG_TypeJerboaRuleResult)
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
	public Boolean accept(JSG_TypeJerboaHookList jsType) {
		genLanguage.appendContent("JerboaInputHooksGeneric");
		return false;
	}

	@Override
	public Boolean accept(JSG_AddInHookList jsg_AddInHookList) {
		final List<JSG_Expression> args = jsg_AddInHookList.getArgs();
		if (args.size() > 1) { // si on passe un entier ET la valeur c'est pour
			// ajouter à une place spécifique
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
	public Boolean accept(JSG_Indirection jsg_Indirection) {
		genLanguage.appendContent("(*");
		jsg_Indirection.getExp().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_Unreference jsg_Unreference) {
		genLanguage.appendContent("(&");
		jsg_Unreference.getExp().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_Comment jsg_Comment) {
		genLanguage.appendContentln(jsg_Comment.getComment());
		lineJumped = true;
		return true;
	}

	@Override
	public Boolean accept(JSG_Delete jsg_Delete) {
		genLanguage.appendContent("delete ");
		jsg_Delete.getName().visit(this);
		lineJumped = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_New jsg_New) {
		genLanguage.appendContent("new ");
		return jsg_New.getExp().visit(this);
	}

	@Override
	public Boolean accept(JSG_Cast jsg_Cast) {
		genLanguage.appendContent("(");
		genLanguage.appendContent("(");
		jsg_Cast.getType().visit(this);
		if (jsg_Cast.getExpr() instanceof JSG_InScope
				&& ((JSG_InScope) jsg_Cast.getExpr()).getRight() instanceof JSG_GetEbd) {
			genLanguage.appendContent("*");
		}
		genLanguage.appendContent(")");
		jsg_Cast.getExpr().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetEbd jsg_GetEbd) {
		// TODO: améliorer pour stocker les ebd en tant que variable et ne pas
		// les chercher chaque fois

		genLanguage.appendContent("(*((");
		genLanguage.appendContent(jsg_GetEbd.getEbdInfo().getType().replaceAll("\\.", "::"));
		genLanguage.appendContent("*)(");
		jsg_GetEbd.getLeft().visit(this);
		genLanguage.appendContent("->ebd(");
		int cpt = 0;
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if (e.getName().compareToIgnoreCase(jsg_GetEbd.getEbdInfo().getName()) == 0)
				break;
			cpt++;
		}
		genLanguage.appendContent(cpt);
		// (new JSG_KeywordModeler(modeler)).visit(this);
		// genLanguage.appendContent("->get");
		// genLanguage.appendContent(jsg_GetEbd.getEbdInfo().getName());
		// genLanguage.appendContent("()->id()");
		genLanguage.appendContent("))))");
		return false;
	}

	@Override
	public Boolean accept(JSG_Catch jsg_Catch) {
		indent();
		genLanguage.appendContent("catch(");
		if (jsg_Catch.getDeclar() != null) {
			jsg_Catch.getDeclar().visit(this);
		} else {
			genLanguage.appendContent("...");
		}
		genLanguage.appendContent(")");
		if (jsg_Catch.getBlock() != null && !(jsg_Catch.getBlock() instanceof JSG_NOP))
			jsg_Catch.getBlock().visit(this);
		else
			genLanguage.appendContentln("{}");
		return true;
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
			genLanguage.appendContent("catch(...)");
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
	public Boolean accept(JSG_TypeBoolean jsg_TypeBoolean) {
		genLanguage.appendContent("bool");
		return false;
	}

	@Override
	public Boolean accept(JSG_CallRuleResWidth jsg_CallRuleResWidth) {
		genLanguage.appendContent("width()");
		return false;
	}

	@Override
	public Boolean accept(JSG_CallListSize jsg_CallListSize) {
		genLanguage.appendContent("size()");
		return false;
	}

	@Override
	public Boolean accept(JSG_CallRuleResHeight jsg_CallRuleResHeight) {
		genLanguage.appendContent("height()");
		return false;
	}

	@Override
	public Boolean accept(JSG_DeclareFunction jsg_DeclareFunction) {
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
	public Boolean accept(JSG_LeftRuleNode jsg_TopoParam) {
		// dans la pr�condition on est dans une boucle ou on suppose connaitre
		// les instances des noeuds directement
		//		if (glue.getLangagesState() == LanguageState.PRECONDITION) {
		//			genLanguage.appendContent(jsg_TopoParam.getName());
		//			genLanguage.appendContent("()");
		//		} else if (jsg_TopoParam.getRule() instanceof JMERuleAtomic) {
		//			genLanguage.appendContent("parentRule->");
		//			genLanguage.appendContent(jsg_TopoParam.getName());
		//			genLanguage.appendContent("()");
		//		} else {
		//			(new JSG_KeywordHook()).visit(this);
		//			genLanguage.appendContent("[");
		//			genLanguage.appendContent(jsg_TopoParam.getName());
		//			genLanguage.appendContent("()]");
		//		}
		if(glue.getLangagesState() == LanguageState.CLASSICAL) {
			if(glue.getCurrentRule() instanceof JMERuleAtomic) {
				genLanguage.appendContent("(*parentRule->curLeftFilter)[");
			}else if(glue.getCurrentRule() instanceof JMEScript) {
				genLanguage.appendContent("sels[");
			}
		}else if(glue.getLangagesState() == LanguageState.PRECONDITION
				|| glue.getLangagesState() == LanguageState.MIDPROCESS) {
			genLanguage.appendContent("(*(leftfilter[");
		}
		genLanguage.appendContent(jsg_TopoParam.getRule().getLeft().getMatchNode(jsg_TopoParam.getName()).getID());
		if(glue.getLangagesState() == LanguageState.CLASSICAL) {
			genLanguage.appendContent("]");
		}else if(glue.getLangagesState() == LanguageState.PRECONDITION
				|| glue.getLangagesState() == LanguageState.MIDPROCESS) {
			genLanguage.appendContent("]))");			
		}

		return true;
	}

	@Override
	public Boolean accept(JSG_ClearHookList jsg_ClearHookList) {
		genLanguage.appendContent(hookListName);
		genLanguage.appendContent(".clear()");
		return false;
	}

	@Override
	public Boolean accept(JSG_InScopeStatic jsg_InScopeStatic) {
		jsg_InScopeStatic.getLeft().visit(this);
		genLanguage.appendContent("::");
		return jsg_InScopeStatic.getRight().visit(this);
	}

	@Override
	public Boolean accept(JSG_AtLang jsAtLang) {
		if (jsAtLang.getLanguage() == null || "Cpp".equalsIgnoreCase(jsAtLang.getLanguage())
				|| "C++".equalsIgnoreCase(jsAtLang.getLanguage())) {
			genLanguage.appendContentln(jsAtLang.getCode());
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_Null jsg_Null) {
		genLanguage.appendContent("NULL");
		return false;
	}

	@Override
	public Boolean accept(JSG_DeclareMark jsg_DeclareMark) {
		genLanguage.appendContent("JerboaMark ");
		genLanguage.appendContent(jsg_DeclareMark.getName());
		return false;
	}

	@Override
	public Boolean accept(JSG_Break jsg_Break) {
		genLanguage.appendContent("break");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetEbdName jsg_GetEbdName) {
		genLanguage.appendContent("\"" + jsg_GetEbdName.getEbdInfo().getName() + "\"");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetEbdId jsg_GetEbdId) {
		genLanguage.appendContent("_owner->getEmbedding(\"" + jsg_GetEbdId.getEbdInfo().getName() + "\")->id()");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypePrimitive jsg_TypePrimitive) {
		genLanguage.appendContent(jsg_TypePrimitive.getType().replaceAll("\\.", "::"));
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeString jsg_TypePrimitive) {
		genLanguage.appendContent("std::string");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeOrbit jsTypeOrbit) {
		genLanguage.appendContent("JerboaOrbit");
		return false;
	}

	@Override
	public Boolean accept(JSG_TypeMark jsTypeMark) {
		genLanguage.appendContent("JerboaMark");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetNodeId jsg_GetId) {
		genLanguage.appendContent("id()");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetEbdOrbit jsg_GetEbdOrbit) {
		genLanguage.appendContent("_owner->getEmbedding(\"" + jsg_GetEbdOrbit.getEbdInfo().getName() + "\")->orbit()");
		return false;
	}

	@Override
	public Boolean accept(JSG_Header jsg_Header) {
		if (jsg_Header.getLanguage() == null || "Cpp".equalsIgnoreCase(jsg_Header.getLanguage())
				|| "C++".equalsIgnoreCase(jsg_Header.getLanguage())) {
			genLanguage.appendInclude(jsg_Header.getCode());
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_EbdParam jsg_EbdParam) {
		genLanguage.appendContent("parentRule->" + jsg_EbdParam.getName());
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
	public Boolean accept(JSG_FreeMarker jsg_FreeMarker) {
		if (!jsg_FreeMarker.hasDirectAccessToGMap()) {
			genLanguage.appendContent("_owner->gmap()");
		} else
			genLanguage.appendContent("gmap");
		genLanguage.appendContent("->freeMarker(");
		jsg_FreeMarker.getMarker().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_GetMarker jsg_GetFreeMarker) {
		if (!jsg_GetFreeMarker.hasDirectAccessToGMap()) {
			genLanguage.appendContent("_owner->gmap()");
		} else
			genLanguage.appendContent("gmap");
		genLanguage.appendContent("->getFreeMarker()");
		return false;
	}

	@Override
	public Boolean accept(JSG_RuleArg jsg_RuleArg) {
		return jsg_RuleArg.getArgValue().visit(this);
	}

	@Override
	public Boolean accept(JSG_IsMarked jsg_IsMarked) {
		jsg_IsMarked.getLeft().visit(this);
		genLanguage.appendContent("->isMarked(");
		jsg_IsMarked.getMark().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_IsNotMarked jsg_IsNotMarked) {
		jsg_IsNotMarked.getLeft().visit(this);
		genLanguage.appendContent("->isNotMarked(");
		jsg_IsNotMarked.getMark().visit(this);
		genLanguage.appendContent(")");
		return false;
	}

	@Override
	public Boolean accept(JSG_UnMark jsg_UnMark) {
		(new JSG_KeywordGmap(jsg_UnMark.gmapHasDirectAccess(), jsg_UnMark.getLine(), jsg_UnMark.getColumn()))
		.visit(this);
		genLanguage.appendContent("->");
		if (jsg_UnMark.getLeft() instanceof JSG_Collect) {
			genLanguage.appendContent("unmarkOrbit(");
			jsg_UnMark.getMark().visit(this);
			genLanguage.appendContent(", ");
			((JSG_Collect) jsg_UnMark.getLeft()).getNode().visit(this);
			genLanguage.appendContent(",");
			((JSG_Collect) jsg_UnMark.getLeft()).getOrbit().visit(this);
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
	public Boolean accept(JSG_Mark jsg_Mark) {
		(new JSG_KeywordGmap(jsg_Mark.gmapHasDirectAccess(), jsg_Mark.getLine(), jsg_Mark.getColumn())).visit(this);
		genLanguage.appendContent("->");
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
	public Boolean accept(JSG_GMapSize jsg_GMapSize) {
		(new JSG_KeywordGmap(jsg_GMapSize.hasDirectAccess(), jsg_GMapSize.getLine(), jsg_GMapSize.getColumn()))
		.visit(this);
		genLanguage.appendContent("->size()");
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordLeftFilter jsg_KeywordLeftFilter) {
		genLanguage.appendContent("leftfilter");
		return false;
	}

	@Override
	public Boolean accept(JSG_KeywordRightFilter jsg_KeywordLeftFilter) {
		genLanguage.appendContent("rightPattern");
		return false;
	}

	@Override
	public Boolean accept(JSG_IndexRuleNode jsg_IndexRuleNode) {
		(new JSG_Rule(jsg_IndexRuleNode.getRule().getName(), jsg_IndexRuleNode.getRule(), jsg_IndexRuleNode.getLine(),
				jsg_IndexRuleNode.getColumn())).visit(this);
		genLanguage.appendContent(".index");
		switch (jsg_IndexRuleNode.getSide()) {
		case LEFT:
			genLanguage.appendContent("Left");
			break;
		case RIGHT:
			genLanguage.appendContent("Right");
		default:
			break;
		}
		genLanguage.appendContent("RuleNode(\"");
		genLanguage.appendContent(jsg_IndexRuleNode.getNodeName());
		genLanguage.appendContent("\")");
		return false;
	}

	@Override
	public Boolean accept(JSG_PackagedType jsType) {
		jsType.getLeft().visit(this);
		genLanguage.appendContent("::");
		return jsType.getRight().visit(this);
	}

	@Override
	public Boolean accept(JSG_Continue jsg_continue) {
		genLanguage.appendContent("continue");
		return false;
	}

	@Override
	public Boolean accept(JSG_IndexInLeftPattern jsg_IndexInLeftPattern) {
		(new JSG_Index(
				new JSG_Indirection(
						new JSG_Index(new JSG_KeywordLeftFilter(-1, -1), jsg_IndexInLeftPattern.getHookIndex())),
				jsg_IndexInLeftPattern.getIndexInDartList())).visit(this);
		return false;
	}

	@Override
	public Boolean accept(JSG_Throw jsg_Throw) {
		genLanguage.appendContent("throw ");
		jsg_Throw.getExpr().visit(this);
		return false;
	}

	@Override
	public Boolean accept(JSG_GetTopoParam jsg_GetTopoParam) {
		if(glue.getCurrentRule() instanceof JMEScript) {

			genLanguage.appendContent("sels[");
			genLanguage.appendContent(jsg_GetTopoParam.getNode().getID());	
			genLanguage.appendContent("]");
			if(jsg_GetTopoParam.getIndex()!=null) {
				genLanguage.appendContent("[");
				jsg_GetTopoParam.getIndex().visit(this);
				genLanguage.appendContent("]");
			}
		}else if(glue.getLangagesState() == LanguageState.PRECONDITION 
				|| glue.getLangagesState() == LanguageState.MIDPROCESS) {
			genLanguage.appendContent("(*(leftfilter");
			if(jsg_GetTopoParam.getIndex()!=null) {
				genLanguage.appendContent("[");
				jsg_GetTopoParam.getIndex().visit(this);
				genLanguage.appendContent("]))");
				genLanguage.appendContent("["+jsg_GetTopoParam.getNode().getID()+"]");	
			}else {
				genLanguage.appendContent("["+jsg_GetTopoParam.getNode().getID()+"]))");	
			}

		}else {
			genLanguage.appendContent("(*parentRule->curLeftFilter)["+jsg_GetTopoParam.getNode().getID()+"]");
		}
		return false;
	}

}
