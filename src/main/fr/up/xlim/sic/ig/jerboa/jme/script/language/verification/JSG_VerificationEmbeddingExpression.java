package fr.up.xlim.sic.ig.jerboa.jme.script.language.verification;

import java.util.ArrayList;
import java.util.HashMap;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
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
import up.jerboa.core.JerboaOrbit;

public class JSG_VerificationEmbeddingExpression
		implements JSG_ExprVisitor<JSG_Expression, RuntimeException>, JSG_InstVisitor<Boolean, RuntimeException> {

	private ArrayList<JSError> errorList;

	private LanguageGlue glue;

	private HashMap<String, JSG_Expression> mapVariableToValue;

	JerboaOrbit orbiteCache, nodeOrbit, ebdOrbit;

	public JSG_VerificationEmbeddingExpression(LanguageGlue _glue) {
		glue = _glue;
		errorList = new ArrayList<>();
		mapVariableToValue = new HashMap<>();
		orbiteCache = null;
		nodeOrbit = new JerboaOrbit();
		ebdOrbit = new JerboaOrbit();
		if (glue.getLangageType() != LanguageGlue.LanguageType.EMBEDDING) {
			System.err.println(
					"This verification must be used for embedding expressions and not for " + glue.getLangageType());
		} else {
			ebdOrbit = glue.getEbdOrbit(glue.getEbdName());
			nodeOrbit = glue.getRightNodeOrbit(glue.getRuleName(), glue.getOwnerName());
			orbiteCache = nodeOrbit;
		}
	}

	public boolean currentRuleHasLeftNodeNamedAs(JSG_Variable var) {
		return glue.getCurrentRule().getLeft().getMatchNode(var.getName()) != null;
	}

	public ArrayList<JSError> getErrors() {
		return errorList;
	}

	public ArrayList<JSError> beginGeneration(JSG_Sequence js) {
		errorList.clear();
		mapVariableToValue = new HashMap<>();
		nodeOrbit = new JerboaOrbit();
		orbiteCache = null;
		ebdOrbit = new JerboaOrbit();
		if (glue.getLangageType() == LanguageType.EMBEDDING) {
			ebdOrbit = glue.getEbdOrbit(glue.getEbdName());
			nodeOrbit = glue.getRightNodeOrbit(glue.getRuleName(), glue.getOwnerName());
		}
		orbiteCache = nodeOrbit;
		accept(js);
		return errorList;
	}

	// JerboaOrbit getOrbitIntersection

	@Override
	public Boolean accept(JSG_While jsWhile) throws RuntimeException {
		boolean valid = true;
		jsWhile.getCondition().visit(this);
		valid = valid && jsWhile.getCorps().visit(this);
		return valid;
	}

	@Override
	public Boolean accept(JSG_Assignment jsAssignment) throws RuntimeException {
		mapVariableToValue.put(((JSG_Variable) jsAssignment.getVariable()).getName(),
				jsAssignment.getValue().visit(this));
		return true;
	}

	@Override
	public Boolean accept(JSG_Block jsBlock) throws RuntimeException {
		return jsBlock.getBody().visit(this);
	}

	@Override
	public Boolean accept(JSG_For jsFor) throws RuntimeException {
		boolean valid = true;
		// on teste avec la valeur de début
		mapVariableToValue.put(jsFor.getVariable(), jsFor.getStart());
		valid = valid && jsFor.getStep().visit(this) && jsFor.getBody().visit(this);

		// on teste avec la valeur de fin
		mapVariableToValue.put(jsFor.getVariable(), jsFor.getEnd());
		valid = valid && jsFor.getStep().visit(this) && jsFor.getBody().visit(this);

		mapVariableToValue.remove(jsFor.getVariable());
		return valid;
	}

	@Override
	public Boolean accept(JSG_ForEach jsForEach) throws RuntimeException {
		boolean valid = true;
		valid = valid && jsForEach.getBody().visit(this);
		// Val : je ne sais pas si on test l'assignement ici.. je ne pense pas.
		// de toute façon dès qu'on itère sur un truc les vérifs sont moins
		// évidentes
		return valid;
	}

	@Override
	public Boolean accept(JSG_ForLoop jsForLoop) throws RuntimeException {
		boolean valid = true;
		valid = valid && jsForLoop.getInit().visit(this);
		jsForLoop.getCond().visit(this);
		valid = valid && jsForLoop.getStep().visit(this);
		valid = valid && jsForLoop.getBody().visit(this);
		return valid;
	}

	@Override
	public Boolean accept(JSG_If jsIf) throws RuntimeException {
		jsIf.getCondition().visit(this);
		// TODO: pour évaluer correctement, il faudrait que l'on évalue toute
		// la suite du block pour chaque
		// consequences de chaque if/else.
		// Les variables peuvent avoir des valeurs différentes et donc il faut
		// passer pls fois dans l'arbre,
		// autant de fois qu'il y a de possibilité.
		return jsIf.getConsequence().visit(this) && jsIf.getAlternant().visit(this);
	}

	@Override
	public Boolean accept(JSG_Sequence jsSequence) throws RuntimeException {
		boolean valid = true;
		for (int i = 0; i < jsSequence.size(); i++) {
			valid = valid && jsSequence.get(i).visit(this);
		}
		return valid;
	}

	@Override
	public Boolean accept(JSG_DoWhile jsDoWhile) throws RuntimeException {
		boolean valid = true;
		jsDoWhile.getCondition().visit(this);
		valid = valid && jsDoWhile.getBody().visit(this);
		return valid;
	}

	@Override
	public Boolean accept(JSG_ExprInstruction jsExprInstruction) throws RuntimeException {
		jsExprInstruction.getExpr().visit(this);
		return true;
	}

	@Override
	public Boolean accept(JSG_Declare jsDeclare) throws RuntimeException {
		if (jsDeclare.getValue() != null) {
			mapVariableToValue.put(jsDeclare.getName(), jsDeclare.getValue().visit(this));
			jsDeclare.getValue().visit(this);
		}
		return true;
	}

	@Override
	public Boolean accept(JSG_Map jsMap) {
		jsMap.getExpr().visit(this);
		jsMap.getBody().visit(this);
		return true;
	}

	@Override
	public Boolean accept(JSG_NOP jsEmpty) {
		return true;
	}

	@Override
	public Boolean accept(JSG_Delete jsg_Delete) {
		jsg_Delete.getName().visit(this);
		return true;
	}

	@Override
	public Boolean accept(JSG_Catch jsg_Catch) {
		return jsg_Catch.getBlock().visit(this);
	}

	@Override
	public Boolean accept(JSG_Try jsg_Try) {
		boolean valid = true;
		for (JSG_Catch c : jsg_Try.getCatchList()) {
			valid = valid && c.visit(this);
		}
		valid = valid && jsg_Try.getTryBlock().visit(this);
		valid = valid && jsg_Try.getFinallyBlock().visit(this);
		return valid;
	}

	@Override
	public Boolean accept(JSG_DeclareFunction jsg_DeclareFunction) {
		// on ne teste pas les paramètres ça n'a pas d'intérêt
		return jsg_DeclareFunction.getBlock().visit(this);
	}

	@Override
	public Boolean accept(JSG_Print jsg_Print) {
		return true;
	}

	@Override
	public Boolean accept(JSG_HookCall jsg_HookCall) {
		return true; // dans les plongements on appelle pas de règle.
	}

	@Override
	public Boolean accept(JSG_AssocParam jsg_AssocParam) {
		return true;
	}

	@Override
	public Boolean accept(JSG_ClearHookList jsg_ClearHookList) {
		return true;
	}

	@Override
	public Boolean accept(JSG_AtLang jsg_AtLang) {
		return true;
	}

	@Override
	public Boolean accept(JSG_DeclareMark jsg_DeclareMark) {
		return true;
	}

	@Override
	public Boolean accept(JSG_Break jsg_Break) {
		return true;
	}

	@Override
	public Boolean accept(JSG_Header jsg_Header) {
		return true;
	}

	@Override
	public Boolean accept(JSG_Return jsg_Return) {
		if (jsg_Return.getExpression() != null)
			jsg_Return.getExpression().visit(this);
		return true;
	}

	@Override
	public JSG_Expression accept(JSG_AddInHookList jsg_AddInHookList) {
		return jsg_AddInHookList;
	}

	@Override
	public JSG_Expression accept(JSG_AddInList jsAddInListSem) {
		for (JSG_Expression e : jsAddInListSem.getArgs())
			e.visit(this);
		return jsAddInListSem;
	}

	@Override
	public JSG_Expression accept(JSG_Alpha jsAlpha) throws RuntimeException {
		if (glue.getLangageType() == LanguageType.EMBEDDING) {
			if (jsAlpha.getNode() instanceof JSG_Variable) {
				// String nodeName = ((JSG_Variable)
				// jsAlpha.getNode()).getName();!
				// if (jsAlpha.getDim() instanceof JSG_Integer) {
				// int dim = ((JSG_Integer) jsAlpha.getDim()).getValue();
				// if (!orbiteCache.contains(dim)) {
				// errorList.add(new JSError(
				// "Unconsistancy found. Trying to get a neibor outside of the
				// embedding orbit",
				// jsAlpha.getLine(), jsAlpha.getColumn(),
				// JSErrorEnumType.CRITICAL));
				// }
				// } else {
				// errorList.add(new JSError("Possible unconsistancy here :
				// alpha getting with variable dimension",
				// jsAlpha.getLine(), jsAlpha.getColumn(),
				// JSErrorEnumType.WARNING));
				// }
			} else {
				// TODO : Val: si c'est pas une variable a gauche, c'est un dart
				// d'une liste au d'un retour
				// de fonction ou autre donc on ne peut présager de rien : on
				// lève un warning.
				// errorList.add(new JSError("Possible unconsistancy here :
				// alpha getting on undefined dart",
				// jsAlpha.getLine(), jsAlpha.getColumn(),
				// JSErrorEnumType.WARNING));
			}
		}
		// TODO : c'est ici qu'il faudra faire un test : VAL: en fait non rien a
		// faire
		return null;
	}

	@Override
	public JSG_Expression accept(JSG_Choice jsAlternativ) {
		return jsAlternativ;
	}

	@Override
	public JSG_Expression accept(JSG_ApplyRule jsApplyRule) throws RuntimeException {
		return jsApplyRule;
	}

	@Override
	public JSG_Expression accept(JSG_Boolean jsBoolean) throws RuntimeException {
		return jsBoolean;
	}

	@Override
	public JSG_Expression accept(JSG_Call jsCall) throws RuntimeException {
		for (JSG_Expression e : jsCall.getArguments())
			e.visit(this);
		return jsCall;
	}

	@Override
	public JSG_Expression accept(JSG_CallListSize jsg_CallListSize) {
		return jsg_CallListSize;
	}

	@Override
	public JSG_Expression accept(JSG_CallRuleResHeight jsg_CallRuleResHeight) {
		return jsg_CallRuleResHeight;
	}

	@Override
	public JSG_Expression accept(JSG_CallRuleResWidth jsg_CallRuleResWidth) {
		return jsg_CallRuleResWidth;
	}

	@Override
	public JSG_Expression accept(JSG_Cast jsg_Cast) {
		return jsg_Cast.getExpr().visit(this);
	}

	@Override
	public JSG_Expression accept(JSG_Collect jsCollect) throws RuntimeException {
		JerboaOrbit tmp = orbiteCache;
		orbiteCache = getOrbit(jsCollect.getOrbit());
		jsCollect.getNode().visit(this);
		// if(!tmp.contains(orbiteCache)){ // si collecte en dehors de l'orbite
		// courante
		// errorList
		// .add(new JSError(
		// "Non orbit equivalent at collect: found orbit " + orbiteCache
		// + " but is out of accessible orbit :" + tmp,
		// jsCollect.getLine(), jsCollect.getColumn(),
		// JSErrorEnumType.CRITICAL));
		// }
		orbiteCache = tmp;
		return jsCollect;
	}

	@Override
	public JSG_Expression accept(JSG_CollectEbd jsCollect) throws RuntimeException {
		JerboaOrbit tmp = orbiteCache;
		orbiteCache = getOrbit(jsCollect.getOrbit());
		jsCollect.getNode().visit(this);
		// if (!tmp.contains(orbiteCache)) { // si collecte en dehors de
		// l'orbite courante
		// errorList
		// .add(new JSError(
		// "Non equivalent orbit at collect: found orbit " + orbiteCache
		// + " but is out of accessible orbit :" + tmp,
		// jsCollect.getLine(), jsCollect.getColumn(),
		// JSErrorEnumType.CRITICAL));
		// }
		orbiteCache = tmp;
		return jsCollect;
	}

	@Override
	public JSG_Expression accept(JSG_Comment jsg_Comment) {
		return jsg_Comment;
	}

	@Override
	public JSG_Expression accept(JSG_Constructor jsg_Constructor) {
		for (JSG_Expression e : jsg_Constructor.getArguments())
			e.visit(this);
		return jsg_Constructor;
	}

	@Override
	public JSG_Expression accept(JSG_Double jsDouble) throws RuntimeException {
		return jsDouble;
	}

	@Override
	public JSG_Expression accept(JSG_EbdParam jsg_EbdParam) {
		return jsg_EbdParam;
	}

	@Override
	public JSG_Expression accept(JSG_Float jsFloat) throws RuntimeException {
		return jsFloat;
	}

	@Override
	public JSG_Expression accept(JSG_GetEbd jsg_GetEbd) {
		// TODO tester si c'est bien le plongement que l'on calcul sinon, voir
		// si il est bien inclu dans l'orbite du plongement courant
		if (jsg_GetEbd.getEbdInfo().getName().compareTo(glue.getEbdName()) == 0) {
			if (jsg_GetEbd.getLeft() instanceof JSG_Variable
					&& currentRuleHasLeftNodeNamedAs((JSG_Variable) jsg_GetEbd.getLeft())) {
				JSG_Variable jsVariable = (JSG_Variable) jsg_GetEbd.getLeft();
				JerboaOrbit leftNodeOrbit = glue.getLeftNodeOrbit(glue.getRuleName(), jsVariable.getName());
				// on prends l'orbite du noeud correspondant à gauche
				ArrayList<Integer> dimToTest = new ArrayList<>();

//				System.err.println(nodeOrbit + " .varname : " + jsVariable.getName() + " - " + leftNodeOrbit + " ##### ");
				for (int i = 0; i < nodeOrbit.size(); i++) {
					if (ebdOrbit.contains(nodeOrbit.get(i))) {
						dimToTest.add(leftNodeOrbit.get(i));
						// on prend les dimension avant renommage, donc a gauche
					}
				}
				JerboaOrbit orbitToTest = new JerboaOrbit(dimToTest);
				JerboaOrbit contextOrb = orbiteCache;
				//System.err.println(">>> " + contextOrb + " - " + orbitToTest + " ##### ");
				if (orbiteCache == null) {
					contextOrb = ebdOrbit;
				}
				for (int i = 0; i < orbitToTest.size(); i++) {
					if (!contextOrb.contains(orbitToTest.get(i))) {
						errorList.add(new JSError(
								"Orbit non equivalent for dimension : " + orbitToTest.get(i) + " at right node "
										+ glue.getOwnerName() + " for left referenced node : " + jsVariable.getName()
										+ " | test on orbit : " + contextOrb + " found " + orbitToTest,
								jsVariable.getLine(), jsVariable.getColumn(), JSErrorEnumType.CRITICAL));
					}
				}
			} else {
				System.err.println(
						"/!\\ Possibility of using a variable not define in left graph that disable preservation verification : "
								+ jsg_GetEbd.getLeft());
			}
		}
		return jsg_GetEbd;
	}

	@Override
	public JSG_Expression accept(JSG_GetEbdId jsg_GetEbdId) {
		return jsg_GetEbdId;
	}

	@Override
	public JSG_Expression accept(JSG_GetEbdName jsg_GetEbdName) {
		return jsg_GetEbdName;
	}

	@Override
	public JSG_Expression accept(JSG_GetEbdOrbit jsg_GetEbdOrbit) {
		return jsg_GetEbdOrbit;
	}

	@Override
	public JSG_Expression accept(JSG_GetNodeId jsg_GetId) {
		return jsg_GetId;
	}

	@Override
	public JSG_Expression accept(JSG_Index jsIndex) throws RuntimeException {
		jsIndex.getIndex().visit(this);
		jsIndex.getVariable().visit(this);
		return jsIndex;
	}

	@Override
	public JSG_Expression accept(JSG_IndexInRuleResult jsIndexInRuleResultSem) {
		jsIndexInRuleResultSem.getVariable().visit(this);
		if(jsIndexInRuleResultSem.getIndexFirst()!=null)
			jsIndexInRuleResultSem.getIndexFirst().visit(this);
		if(jsIndexInRuleResultSem.getIndexSecond()!=null)
			jsIndexInRuleResultSem.getIndexSecond().visit(this);
		return jsIndexInRuleResultSem;
	}

	@Override
	public JSG_Expression accept(JSG_IndexNodeInGmap jsIndexNodeInGmapSem) {
		return jsIndexNodeInGmapSem;
	}

	@Override
	public JSG_Expression accept(JSG_Indirection jsg_Indirection) {
		return jsg_Indirection.getExp().visit(this);
	}

	@Override
	public JSG_Expression accept(JSG_InScope jsInScope) throws RuntimeException {
		jsInScope.getLeft().visit(this);
		jsInScope.getRight().visit(this);
		return jsInScope;
	}

	@Override
	public JSG_Expression accept(JSG_InScopeStatic jsg_InScopeStatic) {
		return jsg_InScopeStatic.getRight().visit(this);
	}

	@Override
	public JSG_Expression accept(JSG_Integer jsInteger) throws RuntimeException {
		return jsInteger;
	}

	@Override
	public JSG_Expression accept(JSG_KeywordDimension jsKeywordDimension) {
		return jsKeywordDimension;
	}

	@Override
	public JSG_Expression accept(JSG_KeywordEbd jsKeywordEbd) {
		return jsKeywordEbd;
	}

	@Override
	public JSG_Expression accept(JSG_KeywordGmap jsGmapKeyword) throws RuntimeException {
		return jsGmapKeyword;
	}

	@Override
	public JSG_Expression accept(JSG_KeywordHook jsKeywordHook) {
		return jsKeywordHook;
	}

	@Override
	public JSG_Expression accept(JSG_KeywordModeler jsKeywordModeler) {
		return jsKeywordModeler;
	}

	@Override
	public JSG_Expression accept(JSG_List jsList) {
		return jsList;
	}

	@Override
	public JSG_Expression accept(JSG_Long jsLong) throws RuntimeException {
		return jsLong;
	}

	@Override
	public JSG_Expression accept(JSG_New jsg_New) {
		return jsg_New.getExp().visit(this);
	}

	@Override
	public JSG_Expression accept(JSG_Not jsNot) throws RuntimeException {
		return jsNot.getExpr().visit(this);
	}

	@Override
	public JSG_Expression accept(JSG_Null jsg_Null) {
		return jsg_Null;
	}

	@Override
	public JSG_Expression accept(JSG_Operator jsOperator) throws RuntimeException {
		for (JSG_Expression e : jsOperator.getOperands())
			if (e != null)
				e.visit(this);
		return jsOperator;
	}

	@Override
	public JSG_Expression accept(JSG_Orbit jsOrbit) throws RuntimeException {
		// TODO Auto-generated method stub
		return jsOrbit;
	}

	@Override
	public JSG_Expression accept(JSG_Rule jsRule) {
		return jsRule;
	}

	@Override
	public JSG_Expression accept(JSG_RuleNode jsRuleNodeSem) {
		return jsRuleNodeSem;
	}

	@Override
	public JSG_Expression accept(JSG_Size jsSizeSem) {
		return jsSizeSem;
	}

	@Override
	public JSG_Expression accept(JSG_String jsString) throws RuntimeException {
		return jsString;
	}

	@Override
	public JSG_Expression accept(JSG_LeftRuleNode jsVariable) {
		JerboaOrbit leftNodeOrbit = glue.getLeftNodeOrbit(glue.getRuleName(), jsVariable.getName());
		// on prends l'orbite du noeud correspondant à gauche
		ArrayList<Integer> dimToTest = new ArrayList<>();
		// TODO: je teste avec rightNodeOrbit plutot que nodeOrbit, mais c'est
		// pas certain
		//System.err.println(nodeOrbit + " - " + leftNodeOrbit + " ##### ");
		for (int i = 0; i < nodeOrbit.size(); i++) {
			// je suppose que les orbite ont la même taille (imposé par
			// l'éditeur normalement)
			if (ebdOrbit.contains(nodeOrbit.get(i))) {
				dimToTest.add(leftNodeOrbit.get(i));
				// on prend les dimension avant renommage, donc a gauche
			}
		}
		JerboaOrbit orbitToTest = new JerboaOrbit(dimToTest);
		JerboaOrbit contextOrb = orbiteCache;
		//System.err.println(">>> " + contextOrb + " - " + orbitToTest + " ##### ");
		if (orbiteCache == null) {
			contextOrb = ebdOrbit;
		}
		for (int i = 0; i < orbitToTest.size(); i++) {
			if (!contextOrb.contains(orbitToTest.get(i))) {
				errorList.add(new JSError(
						"Orbit non equivalent for dimension : " + orbitToTest.get(i) + " at right node "
								+ glue.getOwnerName() + " for left referenced node : " + jsVariable.getName()
								+ " | test on orbit : " + contextOrb + " found " + orbitToTest,
						jsVariable.getLine(), jsVariable.getColumn(), JSErrorEnumType.CRITICAL));
			}
		}

		return jsVariable;
	}

	@Override
	public JSG_Expression accept(JSG_Type jsType) {
		return jsType;
	}

	@Override
	public JSG_Expression accept(JSG_TypeBoolean jsg_TypeBoolean) {
		return jsg_TypeBoolean;
	}

	@Override
	public JSG_Expression accept(JSG_TypeJerboaDart jsTypeJerboaNodeSem) {
		return jsTypeJerboaNodeSem;
	}

	@Override
	public JSG_Expression accept(JSG_TypeJerboaHookList jsType) {
		return jsType;
	}

	@Override
	public JSG_Expression accept(JSG_TypeJerboaRule jsTypeJerboaRuleSem) {
		return jsTypeJerboaRuleSem;
	}

	@Override
	public JSG_Expression accept(JSG_TypeJerboaRuleResult jsTypeJerboaRuleResultSem) {
		return jsTypeJerboaRuleResultSem;
	}

	@Override
	public JSG_Expression accept(JSG_TypeMark jsTypeMark) {
		return jsTypeMark;
	}

	@Override
	public JSG_Expression accept(JSG_TypeOrbit jsTypeOrbit) {
		return jsTypeOrbit;
	}

	@Override
	public JSG_Expression accept(JSG_TypePrimitive jsg_TypePrimitive) {
		return jsg_TypePrimitive;
	}

	@Override
	public JSG_Expression accept(JSG_TypeTemplate jsTypeTemplate) {
		return jsTypeTemplate;
	}

	@Override
	public JSG_Expression accept(JSG_Unreference jsg_Unreference) {
		return jsg_Unreference.getExp().visit(this);
	}

	@Override
	public JSG_Expression accept(JSG_UserType jsUserTypeSem) {
		return jsUserTypeSem;
	}

	@Override
	public JSG_Expression accept(JSG_Variable jsVariable) throws RuntimeException {
		// if (mapVariableToValue.containsKey(jsVariable.getName())) {
		// mapVariableToValue.get(jsVariable.getName()).visit(this);
		// } else
		// for (int i : orbitePlongement) {
		// if (!orbiteCache.contains(i)) {
		// errorList.add(new JSError("Orbits are not equivalent for dimension "
		// + i, jsVariable.getLine(),
		// jsVariable.getColumn(),
		// JSErrorEnumType.CRITICAL));
		// }
		// }
		return mapVariableToValue.get(jsVariable.getName());
	}

	@Override
	public Boolean accept(JSG_FreeMarker jsg_FreeMarker) {
		return true;
	}

	@Override
	public JSG_Expression accept(JSG_GetMarker jsg_GetFreeMarker) {
		return jsg_GetFreeMarker;
	}

	@Override
	public JSG_Expression accept(JSG_RuleArg jsg_RuleArg) {
		return jsg_RuleArg;
	}

	public JerboaOrbit getOrbit(JSG_Orbit orb) {
		ArrayList<Integer> intList = new ArrayList<>();
		for (JSG_Expression i : orb.getDimensions())
			if (i instanceof JSG_Integer)
				intList.add(((JSG_Integer) i).getValue());
			else
				errorList.add(new JSError(
						"Orbit equivalence consistancy can't be established because of using not constant dimension",
						orb.getLine(), orb.getColumn(), JSErrorEnumType.WARNING));
		return new JerboaOrbit(intList);
	}

	@Override
	public JSG_Expression accept(JSG_IsMarked jsg_IsMarked) {
		jsg_IsMarked.getLeft().visit(this);
		jsg_IsMarked.getMark().visit(this);
		return jsg_IsMarked;
	}

	@Override
	public JSG_Expression accept(JSG_IsNotMarked jsg_IsNotMarked) {
		jsg_IsNotMarked.getLeft().visit(this);
		jsg_IsNotMarked.getMark().visit(this);
		return jsg_IsNotMarked;
	}

	@Override
	public Boolean accept(JSG_UnMark jsg_UnMark) {
		jsg_UnMark.getLeft().visit(this);
		jsg_UnMark.getMark().visit(this);
		return true;
	}

	@Override
	public Boolean accept(JSG_Mark jsg_Mark) {
		jsg_Mark.getLeft().visit(this);
		jsg_Mark.getMark().visit(this);
		return true;
	}

	@Override
	public JSG_Expression accept(JSG_IndexInLeftPattern jsg_IndexInLeftPattern) {
		return jsg_IndexInLeftPattern;
	}

	@Override
	public JSG_Expression accept(JSG_GMapSize jsg_GMapSize) {
		return jsg_GMapSize;
	}

	@Override
	public JSG_Expression accept(JSG_KeywordLeftFilter jsg_KeywordLeftFilter) {
		return jsg_KeywordLeftFilter;
	}

	@Override
	public JSG_Expression accept(JSG_KeywordRightFilter jsg_KeywordRightFilter) {
		return jsg_KeywordRightFilter;
	}

	@Override
	public JSG_Expression accept(JSG_IndexRuleNode jsg_IndexRuleNode) {
		return jsg_IndexRuleNode;
	}

	@Override
	public JSG_Expression accept(JSG_PackagedType jsType) {
		return jsType.getRight().visit(this);
	}

	@Override
	public JSG_Type accept(JSG_TypeString jsg_TypeString) {
		return jsg_TypeString;
	}

	@Override
	public Boolean accept(JSG_Continue jsg_Continue) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean accept(JSG_Throw jsg_Throw) {
		return true;
	}

	@Override
	public JSG_Type accept(JSG_GetTopoParam jsg_GetTopoParam) {
		if(jsg_GetTopoParam.getIndex()==null) {
			return new JSG_List(new JSG_TypeJerboaDart(-1, -1), -1,-1);
		}
		return new JSG_TypeJerboaDart(-1, -1);
	}

}
