package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.translator;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSAlpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSAssocParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSBoolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCast;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSChoice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCollect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSComment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSConstructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSDouble;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSFloat;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSGMapSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSInScope;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSInScopeStatic;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIndex;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIndexInLeftPattern;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIndirection;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSInteger;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordDimension;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordGmap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordLeftFilter;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordRightFilter;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSLong;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSNew;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSNot;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSNull;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSOperator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSPackagedType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSRuleArg;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSString;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSTypeTemplate;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSUnreference;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSVariable;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSAssignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSAtLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSBlock;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSBreak;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSCatch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSContinue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDeclare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDelete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSFor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSHeader;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSHookCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSIf;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSMap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSNOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSPrint;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSReturn;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSSequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSThrow;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSTry;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSUnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Alpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_ApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_ApplyRule.JSG_2_RuleReturnType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_AssocParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Choice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Collect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_CollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_EbdParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_GetEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_IsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_IsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_JerboaKeyword;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_JerboaKeyword.KeywordType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Mark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Rule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleArg;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleNodeId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleNodeId.GraphSide;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_UnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_InScope;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_Index;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_IndexInJerboaType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_IndexInJerboaType.JerboaTypeIndexable;
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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_AddInList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_Call;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_Constructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_DeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GMapSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetDartId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_PackagedType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeDart;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeEmbedding;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeTemplate;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_DoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_For;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_ForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_ForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_While;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;
import up.jerboa.core.util.Pair;

public class JSSyntaxToSemanticV2_common 
implements JSExprVisitor<JSG_2_Entity, RuntimeException>, JSInstVisitor<JSG_2_Entity, RuntimeException>{

	protected TranslatorContextV2 context;
	protected LanguageGlue glue;


	public JSSyntaxToSemanticV2_common(TranslatorContextV2 basicContext, LanguageGlue glue) {
		context = new TranslatorContextV2(basicContext.getModeler());
		context.enrichContext(basicContext);
		this.glue = glue;
	}

	@Override
	public JSG_2_Entity accept(JSAlpha jsAlpha) throws RuntimeException {
		return new JSG_2_Alpha(jsAlpha.getNode().visit(this), jsAlpha.getDim().visit(this), jsAlpha.getLine(), jsAlpha.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSApplyRule jsApplyRule) throws RuntimeException {
		ArrayList<JSG_2_Entity> ruleArgs = new ArrayList<>();

		for (JSRuleArg ra : jsApplyRule.getArgs()) {
			ruleArgs.add(ra.visit(this));
		}
		JMERule rule = null;
		for (JMERule r : glue.getModeler().getRules()) {
			if (r.getName().compareTo(jsApplyRule.getRuleName()) == 0) {
				rule = r;
				break;
			}
		}

		JSG_2_Entity expression = jsApplyRule.getRule().visit(this);

		JSG_2_ApplyRule applyResult = null;

		if (context.getCurrentAssignementVarName()!=null) {
			// si on veut mettre le resultat dans un retour de regle
			applyResult = new JSG_2_ApplyRule(rule, expression, ruleArgs, JSG_2_RuleReturnType.FULL, jsApplyRule.getLine(), jsApplyRule.getColumn());
//			if(context.getCurrentAssignementVarName() instanceof JSG_2_Variable) {
//				context.assignVar(((JSG_2_Variable)context.getCurrentAssignementVarName()).getName(), 
//						context.getType(expression, glue), 
//						applyResult);
//			}
		}else {
			applyResult = new JSG_2_ApplyRule(rule, expression, ruleArgs, JSG_2_RuleReturnType.NONE, jsApplyRule.getLine(), jsApplyRule.getColumn());

		}

		return applyResult;
	}


	@Override
	public JSG_2_Entity accept(JSAssignment jsAssignment) throws RuntimeException {
		JSG_2_Entity variable = jsAssignment.getVariable().visit(this);


		context.setCurrentAssignementVarName(variable);
		JSG_2_Entity value = jsAssignment.getValue().visit(this);
		context.setCurrentAssignementVarName(null);


		if(variable instanceof JSG_2_Variable) {
			JSG_2_Type varType = context.getType(variable, glue);
			context.assignVar(((JSG_2_Variable)variable).getName(), varType, value);
		}else{
			// si autre chose qu'une variable on ne met pas a jour le contexte
		}

		return new JSG_2_Assignment(variable, value, jsAssignment.getLine(), jsAssignment.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSAssocParam jsAssocParam) {
		int l = jsAssocParam.getLine();
		int c = jsAssocParam.getColumn();
		
		JSG_2_Entity value = null;
		if(jsAssocParam.getParamValue() != null) {
			value = jsAssocParam.getParamValue().visit(this);
		}
		
		return new JSG_2_AssocParam((JSG_2_Rule) jsAssocParam.getRule().visit(this), value, jsAssocParam.getParamName(), l, c);
	}

	@Override
	public JSG_2_Entity accept(JSAtLang jsAtLang) {
		return new JSG_2_AtLang(jsAtLang.getLanguage(), jsAtLang.getCode(), jsAtLang.getLine(), jsAtLang.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSBlock jsBlock) throws RuntimeException {
		context.beginBlock();
		JSG_2_Entity body = jsBlock.getBody().visit(this);
		context.endBlock();
		return new JSG_2_Block(body, jsBlock.hasBracket(), jsBlock.getLine(), jsBlock.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSBoolean jsBoolean) throws RuntimeException {
		return new JSG_2_Boolean(jsBoolean.getValue(), jsBoolean.getLine(), jsBoolean.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSBreak jsBreak) {
		return new JSG_2_Break(jsBreak.getLine(), jsBreak.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSCall jsCall) throws RuntimeException {
//		if(jsCall.getName().compareToIgnoreCase("alpha")==0) {
//			System.err.println("#### call alpha");
//		}
		List<JSG_2_Entity> argList = new ArrayList<>();
		for (JSExpression expr : jsCall.getArguments()) {
			argList.add(expr.visit(this));
		}
		return new JSG_2_Call(jsCall.getName(), argList, jsCall.getLine(), jsCall.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSCast jsCast) {
		return new JSG_2_Cast((JSG_2_Type)jsCast.getType().visit(this), jsCast.getExpr().visit(this), 
				jsCast.getLine(), jsCast.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSCatch jsCatch) {
		JSG_2_Entity excepDecl = new JSG_2_Declare((JSG_2_Type) jsCatch.getTypeExep().visit(this),
				jsCatch.getNameExep(), null, jsCatch.getLine(), jsCatch.getColumn());

		return new JSG_2_Catch(
				(jsCatch.getBlock()==null?null:jsCatch.getBlock().visit(this)), 
				excepDecl,
				jsCatch.getLine(), jsCatch.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSChoice jsChoice) {
		List<JSG_2_Entity> listAPR = new ArrayList<JSG_2_Entity>();
		for(JSApplyRule ap : jsChoice.getRules()){
			listAPR.add(ap.visit(this));
		}

		return new JSG_2_Choice(listAPR,
				(String) ((context.getCurrentAssignementVarName()!=null) ? context.getCurrentAssignementVarName() : ""),
				jsChoice.getLine(), jsChoice.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSCollect jsCollect) throws RuntimeException {
		return new JSG_2_Collect((JSG_2_Orbit)jsCollect.getOrbit().visit(this), 
				(JSG_2_Orbit)jsCollect.getSubOrbit().visit(this),
				jsCollect.getNode().visit(this), context.isGmapHasDirectAccess(),
				jsCollect.getLine(), jsCollect.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSCollectEbd jsCollect) throws RuntimeException {
		return new JSG_2_CollectEbd(
				(JSG_2_Orbit) (jsCollect.getOrbit()==null?null:jsCollect.getOrbit().visit(this)), 
				jsCollect.getEmbedding(),
				(jsCollect.getNode()==null?null:jsCollect.getNode().visit(this)), 
				context.isGmapHasDirectAccess(),
				jsCollect.getLine(), jsCollect.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSComment jsComment) {
		return new JSG_2_Comment(jsComment.getComment(), jsComment.getLine(), jsComment.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSConstructor jsConstructor) {
		ArrayList<JSG_2_Entity> argList = new ArrayList<>();
		for (JSExpression e : jsConstructor.getArguments()) {
			argList.add(e.visit(this));
		}
		if(context.varExists(jsConstructor.getName().getType())) {
			Pair<JSG_2_Type, JSG_2_Entity> varVal = context.varLastValue(jsConstructor.getName().getType());
			if(varVal != null && varVal.r() instanceof JSG_2_Rule) {
				JSG_2_Rule js2rule = (JSG_2_Rule) varVal.r();
				return new JSG_2_ApplyRule(js2rule.getRule(), js2rule, argList, 
						context.getCurrentAssignementVarName()!=null? JSG_2_RuleReturnType.FULL : JSG_2_RuleReturnType.NONE,
								jsConstructor.getLine(), jsConstructor.getColumn());
			}
		}else {
			// TODO : tester si ce n'est pas plutot un appel de fonction ?
		}

		return new JSG_2_Constructor(jsConstructor.getName().visit(this), argList, jsConstructor.getLine(), jsConstructor.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSContinue jsContinue) {
		return new JSG_2_Continue(jsContinue.getLine(), jsContinue.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSDeclare jsDeclare) throws RuntimeException {
		JSG_2_Entity type = jsDeclare.getType().visit(this);
		JSG_2_Entity value = null;
		if(jsDeclare.getValue()!=null) {
			context.setCurrentAssignementVarName(new JSG_2_Variable(jsDeclare.getName(), jsDeclare.getLine(), jsDeclare.getColumn()));
			value = jsDeclare.getValue().visit(this);
			context.setCurrentAssignementVarName(null);
		}
		context.assignVar(jsDeclare.getName(), context.getType(type, glue), value);
		return new JSG_2_Declare(context.getType(type, glue), jsDeclare.getName(), value, jsDeclare.getLine(), jsDeclare.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSDeclareFunction jsDeclareFunction) {
		List<JSG_2_Entity> params = new ArrayList<>();
		for(JSDeclare d : jsDeclareFunction.getArguments()) {
			params.add(d.visit(this));
		}
		JSG_2_Block block = (JSG_2_Block) jsDeclareFunction.getBlock().visit(this);
		
		JSType returnTypeSyntax = jsDeclareFunction.getReturnType();
		JSG_2_Type returnType = null;
		if(returnTypeSyntax != null) {
			returnType = (JSG_2_Type) returnTypeSyntax.visit(this);
		}
		return new JSG_2_DeclareFunction(returnType,
				jsDeclareFunction.getName(), params, block, 
				jsDeclareFunction.getLine(), jsDeclareFunction.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSDelete jsDelete) {
		return new JSG_2_Delete(jsDelete.getName().visit(this), jsDelete.getLine(), jsDelete.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSDouble jsDouble) throws RuntimeException {
		return new JSG_2_Double(jsDouble.getValue(), jsDouble.getLine(), jsDouble.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSDoWhile jsDoWhile) throws RuntimeException {
		return new JSG_2_DoWhile( 
				(jsDoWhile.getCondition()==null?null:jsDoWhile.getCondition().visit(this)),
				(jsDoWhile.getBody()==null?null:jsDoWhile.getBody().visit(this)), 
				jsDoWhile.getLine(), jsDoWhile.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSExprInstruction jsExprInstruction) throws RuntimeException {
		if(jsExprInstruction!=null) {
			return jsExprInstruction.getExpr().visit(this);
		}
		return new JSG_2_NOP(-1,-1);
	}

	@Override
	public JSG_2_Entity accept(JSFloat jsFloat) throws RuntimeException {
		return new JSG_2_Float(jsFloat.getValue(), jsFloat.getLine(), jsFloat.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSFor jsFor) throws RuntimeException {
		context.beginBlock();
		JSG_2_Entity start = (jsFor.getStart()==null?null:jsFor.getStart().visit(this));
		JSG_2_Type type = (JSG_2_Type)(jsFor.getType()==null?null:jsFor.getType().visit(this));
		context.assignVar(jsFor.getVariable(), type, start);		
		JSG_2_For result = new JSG_2_For(
				type, 
				jsFor.getVariable(), 
				start, 
				(jsFor.getEnd()==null?null:jsFor.getEnd().visit(this)), 
				(jsFor.getStep()==null?null:jsFor.getStep().visit(this)), 
				(jsFor.getBody()==null?null:jsFor.getBody().visit(this)), 
				jsFor.getLine(), jsFor.getColumn());
		context.endBlock();
		return result;
	}

	@Override
	public JSG_2_Entity accept(JSForEach jsFor) throws RuntimeException {
		JSG_2_Type type = (JSG_2_Type)(jsFor.getType()==null?null:jsFor.getType().visit(this));
		context.beginBlock();
		context.assignVar(jsFor.getName(), type, null);	
		JSG_2_ForEach foreach = new JSG_2_ForEach(
				type, 
				jsFor.getName(), 
				(jsFor.getColl()==null?null:jsFor.getColl().visit(this)), 
				(jsFor.getBody()==null?null:jsFor.getBody().visit(this)), 
				jsFor.getLine(), jsFor.getColumn());
		context.endBlock();
		return foreach;
	}

	@Override
	public JSG_2_Entity accept(JSForLoop jsFor) throws RuntimeException {
		context.beginBlock();
		JSG_2_ForLoop foreach = new JSG_2_ForLoop(
				(jsFor.getInit()==null?null:jsFor.getInit().visit(this)), 
				(jsFor.getCond()==null?null:jsFor.getCond().visit(this)), 
				(jsFor.getStep()==null?null:jsFor.getStep().visit(this)), 
				(jsFor.getBody()==null?null:jsFor.getBody().visit(this)), 
				jsFor.getLine(), jsFor.getColumn());
		context.endBlock();
		return foreach;
	}

	@Override
	public JSG_2_Entity accept(JSGMapSize jsgMapSize) {
		return new JSG_2_GMapSize(context.isGmapHasDirectAccess(), jsgMapSize.getLine(), jsgMapSize.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSHeader jsHeader) {
		return new JSG_2_Header(jsHeader.getLanguage(), jsHeader.getCode(), jsHeader.getLine(), jsHeader.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSHookCall jsHookCall) {
		return new JSG_2_Comment("HookCall", jsHookCall.getLine(), jsHookCall.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSIf jsIf) throws RuntimeException {
		return new JSG_2_If(
				(jsIf.getCondition()==null?null:jsIf.getCondition().visit(this)), 
				(jsIf.getConsequence()==null?null:jsIf.getConsequence().visit(this)), 
				(jsIf.getAlternant()==null?null:jsIf.getAlternant().visit(this)),
				jsIf.getLine(), jsIf.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSIndex jsIndex) throws RuntimeException {
		int l = jsIndex.getLine();
		int c = jsIndex.getColumn();
		JSG_2_Entity translation = null;

		JSG_2_Entity left  = jsIndex.getVariable().visit(this);
		JSG_2_Entity right = jsIndex.getIndex().visit(this);



		if(jsIndex.getVariable() instanceof JSKeywordGmap) {
			List<JSG_2_Entity> argList = new ArrayList<>();
			argList.add(right);
			translation = new JSG_2_IndexInJerboaType(JerboaTypeIndexable.GMAP, null, argList, l, c);
		}else if(jsIndex.getVariable() instanceof JSKeywordLeftFilter) {
			List<JSG_2_Entity> argList = new ArrayList<>();
			argList.add(right);
			translation = new JSG_2_IndexInJerboaType(JerboaTypeIndexable.LEFT_PATTERN, null, argList, l, c);
		}else if(jsIndex.getVariable() instanceof JSKeywordRightFilter) {
			List<JSG_2_Entity> argList = new ArrayList<>();
			argList.add(right);
			translation = new JSG_2_IndexInJerboaType(JerboaTypeIndexable.RIGHT_PATTERN, null, argList, l, c);
		}else if(left instanceof JSG_2_IndexInJerboaType) {
			JSG_2_IndexInJerboaType leftIndexJerboaType = (JSG_2_IndexInJerboaType) left;
			
			List<JSG_2_Entity> argList = new ArrayList<>();
			argList.addAll(((JSG_2_IndexInJerboaType)left).getArgList());
			if(glue.getCurrentRule()!=null && right instanceof JSG_2_Variable) {
				// On cherche si un hook est dans les parametres
				if(leftIndexJerboaType.getLeft() instanceof JSG_2_Variable) {
					right = getRightRuleNodeFromResult((JSG_2_Variable)leftIndexJerboaType.getLeft(), ((JSG_2_Variable)right));
				}
			}

			argList.add(right);
			translation = new JSG_2_IndexInJerboaType(((JSG_2_IndexInJerboaType)left).getType(), ((JSG_2_IndexInJerboaType)left).getLeft(), argList, l, c);
		}else{
			if(left instanceof JSG_2_Variable) {
				JSG_2_Variable var = (JSG_2_Variable) left;
				if(context.varExists(var.getName())) {
					if( context.varLastValue(var.getName()).l()!=null 
						&& context.varLastValue(var.getName()).l() instanceof JSG_2_TypeRuleResult) {
						// un retour de regle
						List<JSG_2_Entity> argList = new ArrayList<>();
						if(glue.getCurrentRule()!=null && right instanceof JSG_2_Variable) {
							// On cherche si un hook est dans les parametres
							right = getRightRuleNodeFromResult(var, ((JSG_2_Variable)right));
						}
						argList.add(right);
						translation = new JSG_2_IndexInJerboaType(JerboaTypeIndexable.RULE_RESULT, left, argList, l, c);
					}
				}else {
					System.err.println("Undeclared var : " + var.getName());
				}
			}else if(left instanceof JSG_2_InScope) {
				JSG_2_InScope inscope = (JSG_2_InScope) left;
//				System.err.println("############################ " +left +"(" 
//				+ context.getType(inscope.getLeft(), glue) + " . " 
//				+ context.getType(inscope.getRight(), glue) + ") "
//				+ " - " + right  );
				if(inscope.getLeft() instanceof JSG_2_Variable 
						&& context.varExists(((JSG_2_Variable) inscope.getLeft()).getName()) 
						&& context.varLastValue(((JSG_2_Variable) inscope.getLeft()).getName()).l()!=null 
						&& context.varLastValue(((JSG_2_Variable) inscope.getLeft()).getName()).l() instanceof JSG_2_TypeRuleResult) {
					List<JSG_2_Entity> argList = new ArrayList<>();
					argList.add(right);
					argList.add(right);
					translation = new JSG_2_IndexInJerboaType(JerboaTypeIndexable.RULE_RESULT, left, argList, l, c);
					
				}
				
			}

			if(translation == null) {// si aucune traduction specifique trouve 
				translation = new JSG_2_Index(left,  right, l, c);
			}
		}

		return translation;
	}

	@Override
	public JSG_2_Entity accept(JSIndexInLeftPattern jsIndexInLeftPattern) {
		List<JSG_2_Entity> argList = new ArrayList<>();
		if(jsIndexInLeftPattern.getHookIndex() != null) {
			argList.add(jsIndexInLeftPattern.getHookIndex().visit(this));
		}
		if(jsIndexInLeftPattern.getIndexInDartList() != null) {
			argList.add(jsIndexInLeftPattern.getIndexInDartList().visit(this));
		}

		return new JSG_2_IndexInJerboaType(JerboaTypeIndexable.LEFT_PATTERN, null, argList, 
				jsIndexInLeftPattern.getLine(), jsIndexInLeftPattern.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSIndirection jsIndirection) {
		return new JSG_2_Indirection(jsIndirection.getExp().visit(this), jsIndirection.getLine(), jsIndirection.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSInScope jsInScope) throws RuntimeException {
		int l = jsInScope.getLine();
		int c = jsInScope.getColumn();

		JSG_2_Entity left = jsInScope.getLeft().visit(this);
		JSG_2_Entity right = jsInScope.getRight().visit(this);

		// TODO: tester si a droite c'est une variable dont le nom est 'id', 'name', 'orbit', 'size'
		JSG_2_Type leftType = context.getType(left, glue);
		//		JSG_2_Type rightType = context.getType(right);

		if(leftType instanceof JSG_2_TypeDart) {
			if(right instanceof JSG_2_Variable) {
				JSG_2_Variable varRight = (JSG_2_Variable)right;
				if( glue.ebdExist(varRight.getName()) ) {
					// on accede a un plongement
					JMEEmbeddingInfo einf = glue.getModeler().getEmbedding(varRight.getName());
					JSG_2_GetEbd getEbdEntity = new JSG_2_GetEbd(left, einf, l, c);
					//					System.out.println("#JSSyntaxToSemanticV2_common@JSInScope  ebd getting : '"  + getEbdEntity.getLeft() +"." + einf.getName() +"'");

					return getEbdEntity;
					// TODO : val on deporte les cast dans le parcours d'arbre suivant
					//					JSG_2_TypeEmbedding typeEbd = new JSG_2_TypeEmbedding(einf.getName(), l, c);
					//					return new JSG_2_Cast(new JSG_2_TypePointer(typeEbd, l, c),
					//							getEbdEntity, l, c);
				}else if(varRight.getName().compareToIgnoreCase("id")==0) {
					return new JSG_2_InScope(left, new JSG_2_GetDartId(varRight.getLine(), varRight.getColumn()),
							false, l, c);
				}else {
					System.err.println("#JSSyntaxToSemanticV2_common@JSInScope  ebd doesn't exist : '"  + varRight.getName() +"'");
				}
			}else if(right instanceof JSG_2_Call) {
				JSG_2_Call rightCall = (JSG_2_Call) right;
				if(rightCall.getName().compareToIgnoreCase("alpha")==0) {
					JSG_2_Entity dim = new JSG_2_Integer(-1, l, c);
					if(rightCall.getArguments().size()>0) {
						dim = rightCall.getArguments().get(0);
					}
					return new JSG_2_Alpha(left, dim, rightCall.getLine(), rightCall.getColumn());
				}
			}
		}else if(leftType instanceof JSG_2_TypeRuleResult) {
			if(left instanceof JSG_2_Variable) {
				JSG_2_Variable varRuleRes = (JSG_2_Variable) left;
				if(right instanceof JSG_2_Variable) {
					// si on a : "ruleRes.n0"

					JSG_2_Variable varRight = (JSG_2_Variable)right;
					List<JSG_2_Entity> argList = new ArrayList<>();
					JSG_2_Entity rightTranslation = getRightRuleNodeFromResult(varRuleRes, varRight);

					if(rightTranslation instanceof JSG_2_RuleNodeId) {
						argList.add(rightTranslation);						
					}else if(varRight.getName().compareToIgnoreCase("id")==0) {
						// on cherche après pour les hauteur/ etc car il se peut que les noeud de regle portent ces noms

					}else {
						argList.add(varRight);
					}
					return new JSG_2_IndexInJerboaType(JerboaTypeIndexable.RULE_RESULT, left, argList, l, c);
				}else if(right instanceof JSG_2_Index) {
					System.err.println("RR scope : " +left + " -- "+ right);
					// si on a : "ruleRes.n0#i" 
					// ==> en pratique ne doit pas arriver car on a plutot : (ruleRes.n0)#0 
					// |-> donc il faut checker dans JSIndex

					JSG_2_Index indexRight = (JSG_2_Index)right;

					List<JSG_2_Entity> argList = new ArrayList<>();
/*
					JSG_2_Index indexInRR = (JSG_2_Index) rightTranslation;
					// TODO: Attention ! ici je traduit le nom de noeud en string car je ne cherche pas quelle règle a été appliquée
					// 		dans le ruleResult : on pourrait le savoir normalement, sauf si on fait un JSChoice donc c'est plus générique
					//		de faire comme ça. on laisse le modeler chercher dynamiquement l'index du noeud.
					if(indexInRR.getVariable() instanceof JSG_2_Variable) {
						argList.add(new JSG_2_String(((JSG_2_Variable) indexInRR.getVariable()).getName(), l, c));
						argList.add(indexInRR.getIndex());
					}else if(indexInRR.getIndex() instanceof JSG_2_Variable){
						argList.add(new JSG_2_String(((JSG_2_Variable) indexInRR.getIndex()).getName(), l, c));
						argList.add(indexInRR.getVariable());
					}else {
						argList.add(indexInRR.getVariable());
						argList.add(indexInRR.getIndex());
					}*/
					if(indexRight.getVariable() instanceof JSG_2_Variable) {
						// ruleRes.n0#XX
						JSG_2_Variable varRight = (JSG_2_Variable) indexRight.getVariable();

						argList.add(getRightRuleNodeFromResult(varRuleRes, varRight));
						argList.add(indexRight.getIndex());

					}else if(indexRight.getIndex() instanceof JSG_2_Variable){
						// ruleRes.XX#n0
						JSG_2_Variable varRight = (JSG_2_Variable) indexRight.getVariable();

						argList.add(getRightRuleNodeFromResult(varRuleRes, varRight));
						argList.add(indexRight.getVariable());

					}else{
						argList.add(indexRight.getVariable());
						argList.add(indexRight.getIndex());
						//						System.err.println("#TranslatorContextV2 @JSInScope : right is an index but no translation found ");
					}
					return new JSG_2_IndexInJerboaType(JerboaTypeIndexable.RULE_RESULT, left, argList, l, c) ;
				}else if(right instanceof JSG_2_InScope){
					JSG_2_InScope inScopeRight = (JSG_2_InScope)right;

					List<JSG_2_Entity> argList = new ArrayList<>();


					if(inScopeRight.getLeft() instanceof JSG_2_Variable) {
						// ruleRes.n0#XX
						JSG_2_Variable varRight = (JSG_2_Variable) inScopeRight.getLeft();

						argList.add(getRightRuleNodeFromResult(varRuleRes, varRight));

						if(inScopeRight.getRight() instanceof JSG_2_Variable) {
							JSG_2_Variable rightScopeAccess = (JSG_2_Variable)inScopeRight.getRight();
							if(rightScopeAccess.getName().compareToIgnoreCase("size") == 0
									|| rightScopeAccess.getName().compareToIgnoreCase("length") == 0) {
								//								return new JSG_2_InScope(,
								//										new JSG_2_, 
								//l, c);
								// TODO : call sur un size de liste sur le inscopt de res.n0
							}
						}else{
							argList.add(inScopeRight.getRight());
						}

					}else{
						//						argList.add(right);
						System.err.println("#TranslatorContextV2 @JSInScope : right is an inscope but no translation found ");
					}

				}
			}else {
				System.err.println("#TranslatorContextV2 @JSInScope : left is Rule result but not a variable, it is '" 
						+ left.getClass().getCanonicalName() + "'");
			}
		}else if(leftType instanceof JSG_2_TypeList) {
			if(right instanceof JSG_2_Call) {
				JSG_2_Call caller = (JSG_2_Call) right;
				if(caller.getName().compareToIgnoreCase("add") == 0
						|| caller.getName().compareToIgnoreCase("push_back") == 0
						|| caller.getName().compareToIgnoreCase("push") == 0
						|| caller.getName().compareToIgnoreCase("put") == 0) {
					right = new JSG_2_AddInList(caller.getArguments(), 
							new JSG_2_Type(((JSG_2_TypeList)leftType).getTypeName(),
									caller.getLine(), caller.getColumn()), 
							l, c);
				}
			}
		}

		return new JSG_2_InScope(left, right, false, l, c);
	}

	@Override
	public JSG_2_Entity accept(JSInScopeStatic jsInScopeStatic) {
		return new JSG_2_InScope(jsInScopeStatic.getLeft().visit(this),
				jsInScopeStatic.getRight().visit(this), true, 
				jsInScopeStatic.getLine(), jsInScopeStatic.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSInteger jsInteger) throws RuntimeException {
		return new JSG_2_Integer(jsInteger.getValue(), jsInteger.getLine(), jsInteger.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSIsMarked jsIsMarked) {
		return new JSG_2_IsMarked(jsIsMarked.getLeft().visit(this), jsIsMarked.getMark().visit(this), 
				jsIsMarked.getLine(), jsIsMarked.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSIsNotMarked jsIsNotMarked) {
		return new JSG_2_IsNotMarked(jsIsNotMarked.getLeft().visit(this), jsIsNotMarked.getMark().visit(this), 
				jsIsNotMarked.getLine(), jsIsNotMarked.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSKeywordDimension jsKeywordDimension) {
		return new JSG_2_JerboaKeyword(KeywordType.DIMENSION, jsKeywordDimension.getLine(), jsKeywordDimension.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSKeywordEbd jsKeywordEbd) {
		// TODO : ATTENTION il faudra tenir compte des REQUEST !!
		return new JSG_2_TypeEmbedding(jsKeywordEbd.getEbd(), jsKeywordEbd.getLine(), jsKeywordEbd.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSKeywordGmap jsGmapKeyword) throws RuntimeException {
		return new JSG_2_JerboaKeyword(KeywordType.GMAP, jsGmapKeyword.getLine(), jsGmapKeyword.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSKeywordLeftFilter jsKeywordLeftFilter) {
		return new JSG_2_JerboaKeyword(KeywordType.LEFTPATTERN, jsKeywordLeftFilter.getLine(), jsKeywordLeftFilter.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSKeywordModeler jsKeywordModeler) {
		return new JSG_2_JerboaKeyword(KeywordType.MODELER, jsKeywordModeler.getLine(), jsKeywordModeler.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSKeywordRightFilter jsKeywordRightFilter) {
		return new JSG_2_JerboaKeyword(KeywordType.RIGHTPATTERN, jsKeywordRightFilter.getLine(), jsKeywordRightFilter.getColumn());
	}


	@Override
	public JSG_2_Entity accept(JSList jsList) {
		return new JSG_2_TypeList(context.getType(jsList.getTypedList().visit(this), glue), jsList.getLine(), jsList.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSLong jsLong) throws RuntimeException {
		return new JSG_2_Long(jsLong.getValue(), jsLong.getLine(), jsLong.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSMap jsMap) {
		return new JSG_2_Map(
				(JSG_2_Type) (jsMap.getType()==null?null:jsMap.getType().visit(this)), 
				jsMap.getVar(),
				(jsMap.getBody()==null?null:jsMap.getBody().visit(this)), 
				(jsMap.getExpr()==null?null:jsMap.getExpr().visit(this)),
				jsMap.getLine(), jsMap.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSMark jsMark) {
		return new JSG_2_Mark(jsMark.getLeft().visit(this), jsMark.getMark().visit(this), jsMark.getLine(), jsMark.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSNew jsNew) {
		return new JSG_2_New(jsNew.getExp().visit(this), jsNew.getLine(), jsNew.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSNOP jsEmpty) {
		return new JSG_2_NOP(jsEmpty.getLine(), jsEmpty.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSNot jsNot) throws RuntimeException {
		return new JSG_2_Not(jsNot.getExpr().visit(this), jsNot.getLine(), jsNot.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSNull jsNull) {
		return new JSG_2_Null(jsNull.getLine(), jsNull.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSOperator jsOperator) throws RuntimeException {
		List<JSG_2_Entity> operandes = new ArrayList<>();
		for(JSExpression e : jsOperator.getOperands()) {
			if(e!=null) {
				operandes.add(e.visit(this));
			}else {
				operandes.add(null);
			}
		}
		return new JSG_2_Operator(jsOperator.getOperator(), jsOperator.getLine(), jsOperator.getColumn(), operandes);
	}

	@Override
	public JSG_2_Entity accept(JSOrbit jsOrbit) throws RuntimeException {
		List<JSG_2_Entity> listDim = new ArrayList<>();
		for(JSExpression e : jsOrbit.getDimensions()) {
			listDim.add(e.visit(this));
		}
		return new JSG_2_Orbit(listDim, jsOrbit.getLine(), jsOrbit.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSPackagedType jsType) {
		return new JSG_2_PackagedType((JSG_2_Type)jsType.getLeft().visit(this), 
				(JSG_2_Type) jsType.getRight().visit(this), 
				jsType.getLine(), jsType.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSPrint jsPrint) {
		List<JSG_2_Entity> args = new ArrayList<>();
		for(JSExpression e : jsPrint.getArguments()) {
			args.add(e.visit(this));
		}
		return new JSG_2_Print(args, jsPrint.getLine(), jsPrint.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSReturn jsReturn) {
		return new JSG_2_Return(jsReturn.getExpression().visit(this), jsReturn.getLine(), jsReturn.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSRule jsRule) {
		JMERule rule = null;
		if (glue.getModeler() != null) {
			for (JMERule mrule : glue.getModeler().getRules()) {
				if(jsRule.getName() == null) {
					rule = glue.getCurrentRule();
				}else if (mrule.getName().compareTo(jsRule.getName()) == 0) {
					rule = mrule;
					break;
				}
			}
		}
		return new JSG_2_Rule(rule.getName(), rule, jsRule.getLine(), jsRule.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSRuleArg jsRuleArg) {
		return new JSG_2_RuleArg(jsRuleArg.getArgName(), jsRuleArg.getArgValue().visit(this));
//		return new JSG_2_Comment("JSRuleArg", jsRuleArg.getLine(), jsRuleArg.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSSequence jsSequence) throws RuntimeException {
		List<JSG_2_Entity> instructions = new ArrayList<JSG_2_Entity>();
		for(JSInstruction i : jsSequence) {
			instructions.add((i!=null) ? i.visit(this) : new JSG_2_NOP(-1,-1));
		}
		return new JSG_2_Sequence(instructions, jsSequence.getLine(), jsSequence.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSString jsString) throws RuntimeException {
		return new JSG_2_String(jsString.getValue().replaceAll("\\\\", "\\\\\\\\"), jsString.getLine(), jsString.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSThrow jsThrow) {
		return new JSG_2_Throw(jsThrow.getExpr().visit(this), jsThrow.getLine(), jsThrow.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSTry jsTry) {
		List<JSG_2_Catch> catchList = new ArrayList<>();
		for(JSCatch c : jsTry.getCatchList()) {
			catchList.add((JSG_2_Catch) c.visit(this));
		}
		return new JSG_2_Try(
				(jsTry.getTryBlock()==null?new JSG_2_Block(jsTry.getLine(), jsTry.getColumn()):jsTry.getTryBlock().visit(this)), 
				catchList,
				(jsTry.getFinallyBlock()==null?null:jsTry.getFinallyBlock().visit(this)),
				jsTry.getLine(), jsTry.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSType jsType) {
		int l = jsType.getLine();
		int c = jsType.getColumn();
		if (jsType instanceof JSKeywordEbd) {
			return  new JSG_2_TypeEmbedding( ((JSKeywordEbd) jsType).getEbd(), l, c);
		} else {
			return context.getTypeFromString(jsType.getType(), l, c);
		}
	}

	@Override
	public JSG_2_Entity accept(JSTypeTemplate jsTypeTemplate) {
		int l = jsTypeTemplate.getLine();
		int c = jsTypeTemplate.getColumn();
		
		List<JSG_2_Type> listTemplate = new ArrayList<>();
		for(JSType t : jsTypeTemplate.getTypes()) {
			listTemplate.add((JSG_2_Type) t.visit(this));
		}		
		
		JSG_2_Type typeTranslated = context.getTypeFromString(jsTypeTemplate.getType(), l, c);
		if(typeTranslated instanceof JSG_2_TypeList && listTemplate.size()<=1) {
			JSG_2_Type e = listTemplate.size()==1 ? listTemplate.get(0) : null;
			return new JSG_2_TypeList(e, jsTypeTemplate.getLine(), jsTypeTemplate.getColumn());
		}else {
			System.err.println("### typetemplate : " + typeTranslated);
		}
		
		return new JSG_2_TypeTemplate(context.getTypeFromString(jsTypeTemplate.getType(), l, c), listTemplate, l, c);
	}

	@Override
	public JSG_2_Entity accept(JSUnMark jsUnMark) {
		return new JSG_2_UnMark(jsUnMark.getLeft().visit(this), jsUnMark.getMark().visit(this), jsUnMark.getLine(), jsUnMark.getColumn());

	}

	@Override
	public JSG_2_Entity accept(JSUnreference jsUnreference) {
		return new JSG_2_Unreference(jsUnreference.getExp().visit(this), jsUnreference.getLine(), jsUnreference.getColumn());
	}

	@Override
	public JSG_2_Entity accept(JSVariable jsVariable) throws RuntimeException {
		int l = jsVariable.getLine();
		int c = jsVariable.getColumn();

		JMERule rule = glue.getCurrentRule();
		if(rule != null) { // on traduit un process, un script ou un plongement
			switch (glue.getLangageType()) {
			case EMBEDDING:
			case RULE:				
			case EDBDEFCODE:
			case SCRIPT:
				for(JMENode leftNode : rule.getLeft().getHooks()) {
					
					// /!\ Je ne traduit pas directement en integer pour pouvoir faire la verif de nom du noeud a l'issue de cette traduction !

					
					if(jsVariable.getName().compareTo(leftNode.getName())==0) {
						switch (glue.getLangagesState()) {
						case PREPROCESS:
						case PRECONDITION:
						case MIDPROCESS:
							List<JSG_2_Entity> indices = new ArrayList<>();
							indices.add(new JSG_2_RuleNodeId(rule, jsVariable.getName(), GraphSide.LEFT, l, c));
							return new JSG_2_IndexInJerboaType(JerboaTypeIndexable.LEFT_PATTERN, null, indices, l, c);
						default:
							switch (glue.getLangageType()) {
							case SCRIPT:
								List<JSG_2_Entity> indicesScript = new ArrayList<>();
								indicesScript.add(new JSG_2_RuleNodeId(rule, jsVariable.getName(), GraphSide.LEFT, l, c));
								return new JSG_2_IndexInJerboaType(JerboaTypeIndexable.SCRIPT_HOOK, null, indicesScript, l, c);
							case RULE : 
								List<JSG_2_Entity> indicesRule = new ArrayList<>();
								indicesRule.add(new JSG_2_RuleNodeId(rule, jsVariable.getName(), GraphSide.LEFT, l, c));
								return new JSG_2_IndexInJerboaType(JerboaTypeIndexable.LEFT_PATTERN, null, indicesRule, l, c);
							default:
								break;
							}
						}
					}
				}
				for(String nodeName : glue.getRuleLeftNodesParam(rule.getName())) {
					if(jsVariable.getName().compareTo(nodeName)==0) {
						List<JSG_2_Entity> indices = new ArrayList<>();
						indices.add(new JSG_2_RuleNodeId(rule, jsVariable.getName(), GraphSide.LEFT, l,c));
						return new JSG_2_IndexInJerboaType(JerboaTypeIndexable.LEFT_PATTERN, null, indices, l, c);
					}
				}
//				System.err.println("no node found for var " + jsVariable.getName());
				// Ici je considère que le nom de noeud masque un éventuel paramètre de plongement qui porte le même nom
//				for(String nodeName : glue.getRuleLeftNodesParam(rule.getName())) {
//					if(jsVariable.getName().compareTo(nodeName)==0) {
//						return new JSG_2_RuleNodeId(rule, jsVariable.getName(), GraphSide.LEFT, l, c);
//					}
//				}
				for (String e : glue.getEbdParams()) {
					if (e.compareTo(jsVariable.getName()) == 0) {
						JMEParamEbd param = null;
						if (rule != null) {
							for (JMEParamEbd pebd : rule.getParamsEbd()) {
//								System.out.println("Checking rule '" + rule.getName() +"' parameter : " +  pebd.getName());
								if (pebd.getName().compareTo(jsVariable.getName()) == 0) {
									param = pebd;
//									System.out.println(" parameter : " +  pebd.getName() + " is chosen");
									break;
								}
							}
						}
						if (param != null) {
							return new JSG_2_EbdParam(param, jsVariable.getName(), l, c);
						}
					}
				}
			case MODELER:
				break;
			default:
				break;
			}



		}else {
			// on traduit un modeler
		}

		return new JSG_2_Variable(jsVariable.getName(), l, c);
	}

	/**---------------------------------------------**/


	@Override
	public JSG_2_Entity accept(JSWhile jsWhile) throws RuntimeException {
		JSG_2_Entity cond = jsWhile.getCondition().visit(this);
		JSG_2_Entity corps = jsWhile.getCorps().visit(this);

		return new JSG_2_While(cond, corps, jsWhile.getLine(), jsWhile.getColumn());
	}

	private JSG_2_Entity getRightRuleNodeFromResult(JSG_2_Variable ruleRes, JSG_2_Variable nodeName) {
		ArrayList<Pair<JSG_2_Type, JSG_2_Entity>> leftVarValues = context.var(ruleRes.getName());
		JMERule ruleInsideResult = null;

		// on part de la fin pour chopper la derniere assignation possible mais il 
		// se peut que ca ne soit pas la bonne regle!
		// on teste donc la dernire règle qui contient une variable a droite dont le nom
		// match avec celle demandée
		for(int vi = leftVarValues.size()-1; vi>=0; vi--) {
			Pair<JSG_2_Type, JSG_2_Entity> p = leftVarValues.get(vi);
			if(p.r() instanceof JSG_2_ApplyRule) {
				JSG_2_ApplyRule applyR = (JSG_2_ApplyRule) p.r();
				if(applyR.getRule()!=null) {
					JMENode node = applyR.getRule().getRight().searchNodeByName(nodeName.getName());
					if(node!=null) {
						ruleInsideResult = applyR.getRule();
					}
				}
			}else {
				System.err.println("#TranslatorContextV2 @JSInScope : fail searching rule result assignement rule matching ");
				if(p.r()!=null) {
					System.err.println("class name as : '"  + p.r().getClass().getCanonicalName() + "'");
				}
			}
		}

		if(ruleInsideResult!=null) {
			return new JSG_2_RuleNodeId(ruleInsideResult, nodeName.getName(), GraphSide.RIGHT,
					nodeName.getLine(), nodeName.getColumn());							
		}else {
			return nodeName;
		}
	}
	
	

}
