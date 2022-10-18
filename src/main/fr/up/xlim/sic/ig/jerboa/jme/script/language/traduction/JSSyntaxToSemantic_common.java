/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_AddInHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_AddInList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Alpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_ApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_ApplyRule.JSG_RuleReturnType;
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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexRuleNode.SIDE;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Indirection;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Integer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordDimension;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordGmap;
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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Continue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Declare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DeclareFunction;
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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.EBDRequest;
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

/**
 * @author Valentin
 *
 */

public class JSSyntaxToSemantic_common 
		implements JSExprVisitor<JSG_Expression, RuntimeException>, JSInstVisitor<JSG_Instruction, RuntimeException> {

	private boolean oldEngine = false;

	protected boolean gmapHasDirectAccess = false;

	protected final String nodeTypeName = "JerboaDart";
	protected final String ruleResultTypeName = "JerboaRuleResult";
	protected final String markTypeName = "JerboaMark";

	protected boolean inAssignement;
	protected int inScope;

	protected String assignementVarName;

	protected HashMap<String, JSG_TypeJerboaRule> mapVariableJerboaRuleToRule;

	protected HashMap<String, String> mapResultToRule;
	protected HashMap<String, JSG_Type> mapVariableToType;

	protected Map<String, Boolean> variableIsPointer;
	protected JSRule lastRuleApplied;
	protected JSG_Type lastType;
	protected String hookListName;

	protected ArrayList<ArrayList<String>> listOfDeclaration;

	protected LanguageGlue glue;

	protected boolean isInMainStream;

	protected boolean mainStreamHasReturn;

	protected JMEModeler modeler;

	protected TranslatorContext context;
	protected boolean inRuleResultAccess;
	

	private JSG_Type getType(String type) {
		JSSyntaxToSemantic_common translator = new JSSyntaxToSemantic_common(glue, modeler);

		return translator.accept(new JSType(type, -1, -1));
	}

	protected boolean isPrimitivType(String name) {
		if (name.compareToIgnoreCase("int") == 0 || name.compareToIgnoreCase("float") == 0
				|| name.compareToIgnoreCase("double") == 0 || name.compareToIgnoreCase("unsigned") == 0
				|| name.compareToIgnoreCase("unsigned int") == 0 || name.compareToIgnoreCase("integer") == 0
				|| name.compareToIgnoreCase("bool") == 0 || name.compareToIgnoreCase("boolean") == 0
				|| name.compareToIgnoreCase("long") == 0 || name.compareToIgnoreCase("ulong") == 0
				|| name.compareToIgnoreCase("unsigned long") == 0 || name.compareToIgnoreCase("long long") == 0
				|| name.compareToIgnoreCase("string") == 0)
			return true;
		return false;
	}

	protected boolean isEbdType(String name, JMEModeler modeler) {
		for (JMEEmbeddingInfo ei : modeler.getEmbeddings()) {
			if (ei.getType().compareToIgnoreCase(name) == 0)
				return true;
		}
		return false;
	}

	boolean isAPointerType(JSG_Type type) {
		return type instanceof JSG_TypeJerboaDart || type instanceof JSG_TypeJerboaRule
				|| (!oldEngine && type instanceof JSG_TypeJerboaRuleResult);
		// || type instanceof JSG_TypeJerboaRuleResult;
		// TODO : Tester le JSG_UserType qui peut etre un pointeur quand le type
		// est null (@ebd<>)
	}

	public JSSyntaxToSemantic_common(LanguageGlue _glue, JMEModeler _modeler) {
		lastRuleApplied = null;
		inAssignement = false;
		mapVariableJerboaRuleToRule = new HashMap<>();
		mapResultToRule = new HashMap<String, String>();
		variableIsPointer = new HashMap<String, Boolean>();
		mapVariableToType = new HashMap<String, JSG_Type>();
		listOfDeclaration = new ArrayList<>();
		inScope = 0;
		lastType = new JSG_TypePrimitive("", -1, -1);
		hookListName = "_hn";
		glue = _glue;
		modeler = _modeler;
		isInMainStream = true;
		mainStreamHasReturn = false;
		context = new TranslatorContext();
		// TODO: ajouter toutes les déclaration des variables de règles et de
		// modeleur!
		if (glue.getLangageType() == LanguageType.EMBEDDING)
			gmapHasDirectAccess = true;
		else
			gmapHasDirectAccess = false;
		inRuleResultAccess = false;
	}

	public JSG_Sequence translate(JSSequence js) {
		JSG_Sequence res = new JSG_Sequence(js.getLine(), js.getColumn());
		if (glue.getLangageType() == LanguageType.EMBEDDING)
			gmapHasDirectAccess = true;
		else
			gmapHasDirectAccess = false;
		inRuleResultAccess = false;
		res.add(accept(js));
		return res;
	}

	@Override
	public JSG_While accept(JSWhile jsWhile) throws RuntimeException {
		boolean wasInMainStream = isInMainStream;
		isInMainStream = false;
		JSG_While res = new JSG_While(jsWhile.getCondition().visit(this), jsWhile.getCorps().visit(this),
				jsWhile.getLine(), jsWhile.getColumn());
		isInMainStream = wasInMainStream;
		lastType = null;
		return res;
	}

	@Override
	public JSG_Instruction accept(JSAssignment jsAssignment) throws RuntimeException {
		inAssignement = true;
		if (jsAssignment.getVariable() instanceof JSVariable)
			assignementVarName = ((JSVariable) jsAssignment.getVariable()).getName();

		JSG_Instruction res = new JSG_NOP();// = new
		// JSG_Sequence(jsAssignment.getLine(),
		// jsAssignment.getColumn());

		final JSExpression e = jsAssignment.getValue();

		if (e != null && e instanceof JSApplyRule) {
			lastRuleApplied = ((JSApplyRule) e).getRule();
			// seq.add(prepareRule((JSApplyRule) e));
			if (jsAssignment.getVariable() instanceof JSVariable)
				mapResultToRule.put(((JSVariable) jsAssignment.getVariable()).getName(), lastRuleApplied.getName());
		} else if (e instanceof JSRule) {
			mapVariableJerboaRuleToRule.put(assignementVarName,
					new JSG_TypeJerboaRule(((JSRule) e).getName(), jsAssignment.getLine(), jsAssignment.getColumn()));
			mapVariableToType.put(assignementVarName,
					new JSG_TypeJerboaRule(((JSRule) e).getName(), jsAssignment.getLine(), jsAssignment.getColumn()));
		}

		try {
			res = new JSG_Assignment(jsAssignment.getVariable().visit(this), jsAssignment.getValue().visit(this),
					jsAssignment.getLine(), jsAssignment.getColumn());
		} catch (Exception e2) {
			if (jsAssignment.getVariable() instanceof JSVariable)
				System.err
						.println("ERROR: can not assign variable " + ((JSVariable) jsAssignment.getVariable()).getName()
								+ " with value " + jsAssignment.getValue());
		}

		JSG_Type rightType = lastType;
		inAssignement = false;
		if (jsAssignment.getVariable() instanceof JSVariable
				&& mapVariableToType.containsKey(((JSVariable) jsAssignment.getVariable()).getName())) {
			lastType = mapVariableToType.get(((JSVariable) jsAssignment.getVariable()).getName());
		} else
			lastType = rightType;
		return res;
	}

	@Override
	public JSG_Instruction accept(JSDeclare jsDeclare) throws RuntimeException {
		inAssignement = true;
		assignementVarName = jsDeclare.getName();
		if (listOfDeclaration.size() > 0 && jsDeclare.getType().getType().compareTo(ruleResultTypeName) == 0) {
			listOfDeclaration.get(listOfDeclaration.size() - 1).add(jsDeclare.getName());
		}

		mapVariableToType.put(jsDeclare.getName(), (JSG_Type) jsDeclare.getType().visit(this));

		JSG_Type type = (JSG_Type) jsDeclare.getType().visit(this);
		JSG_Instruction res = new JSG_NOP();
		JSG_Expression value = null;
		if (jsDeclare.getValue() != null) {
			value = jsDeclare.getValue().visit(this);
		}
		if (value instanceof JSG_ApplyRule) {
			mapVariableJerboaRuleToRule.put(assignementVarName,
					new JSG_TypeJerboaRule(((JSG_ApplyRule) value).getRule().getName(), jsDeclare.getLine(), jsDeclare.getColumn()));
			mapVariableToType.put(assignementVarName,
					new JSG_TypeJerboaRule(((JSG_ApplyRule) value).getRule().getName(), jsDeclare.getLine(), jsDeclare.getColumn()));
		}

		if (jsDeclare.getType().getType().compareTo(markTypeName) == 0) {
			// on a déclaré une mark
			res = new JSG_Declare(new JSG_TypeMark(jsDeclare.getLine(), jsDeclare.getColumn()), assignementVarName,
					new JSG_GetMarker(gmapHasDirectAccess), jsDeclare.getLine(), jsDeclare.getColumn());
		} else if (value != null) {
			if (value instanceof JSG_Rule) {
				mapVariableJerboaRuleToRule.put(assignementVarName, new JSG_TypeJerboaRule(((JSG_Rule) value).getName(),
						jsDeclare.getLine(), jsDeclare.getColumn()));
				mapVariableToType.put(jsDeclare.getName(), new JSG_TypeJerboaRule(((JSG_Rule) value).getName(),
						jsDeclare.getLine(), jsDeclare.getColumn()));
			} else if (jsDeclare.getValue() instanceof JSNew) {
				variableIsPointer.put(jsDeclare.getName(), true);
			} else
				variableIsPointer.put(jsDeclare.getName(), isAPointerType(type));

			if (jsDeclare.getValue() instanceof JSApplyRule) {
				lastRuleApplied = ((JSApplyRule) jsDeclare.getValue()).getRule();
				// res.add(prepareRule((JSApplyRule) jsDeclare.getValue()));
				res = new JSG_Declare(type, jsDeclare.getName(), value, jsDeclare.getLine(), jsDeclare.getColumn());
			} else if (jsDeclare.getValue() instanceof JSChoice) {
				res = new JSG_Declare((JSG_Type) jsDeclare.getType().visit(this), jsDeclare.getName(), value,
						jsDeclare.getLine(), jsDeclare.getColumn());
			} else {
				res = new JSG_Declare(type, jsDeclare.getName(), value, jsDeclare.getLine(), jsDeclare.getColumn());
			}
		} else {
			variableIsPointer.put(jsDeclare.getName(), isAPointerType(type));
			res = new JSG_Declare(type, jsDeclare.getName(), null, jsDeclare.getLine(), jsDeclare.getColumn());
		}
		inAssignement = false;

		context.declareVar(assignementVarName, type, value);
		return res;
	}

	@Override
	public JSG_Block accept(JSBlock jsBlock) throws RuntimeException {
		context.beginBlock();
		JSG_Block res;
		if (jsBlock.getBody() != null) {
			res = new JSG_Block(jsBlock.getBody().visit(this), jsBlock.hasBracket(), jsBlock.getLine(),
					jsBlock.getColumn());
		} else {
			res = new JSG_Block(new JSG_NOP(), true, jsBlock.getLine(), jsBlock.getColumn());
		}
		lastType = null;
		context.endBlock();
		return res;
	}

	@Override
	public JSG_For accept(JSFor jsFor) throws RuntimeException {
		JSG_Type type = null;
		JSG_Expression start = null;
		JSG_Expression end = null;
		JSG_Instruction step = null;
		boolean wasInMainStream = isInMainStream;
		isInMainStream = false;
		JSG_Block body = new JSG_Block(new JSG_NOP(), true, jsFor.getLine(), jsFor.getColumn());
		isInMainStream = wasInMainStream;
		// TODO : la variable n'est pas déclarée ici ?

		if (jsFor.getType() != null)
			type = (JSG_Type) jsFor.getType().visit(this);

		if (jsFor.getStart() != null)
			start = jsFor.getStart().visit(this);
		if (jsFor.getEnd() != null)
			end = jsFor.getEnd().visit(this);
		if (jsFor.getStep() != null)
			step = jsFor.getStep().visit(this);
		if (jsFor.getBody() != null)
			body = (JSG_Block) jsFor.getBody().visit(this);

		JSG_For res = new JSG_For(type, jsFor.getVariable(), start, end, step, body, jsFor.getLine(),
				jsFor.getColumn());
		lastType = null;
		return res;
	}

	@Override
	public JSG_ForEach accept(JSForEach jsForEach) throws RuntimeException {
		JSG_Type type = (JSG_Type) jsForEach.getType().visit(this);
		mapVariableToType.put(jsForEach.getName(), type);
		variableIsPointer.put(jsForEach.getName(), isAPointerType(type));
		JSG_Expression collection = jsForEach.getColl().visit(this);
		if (collection instanceof JSG_KeywordGmap) {
			// si GMap il faut l'indirection
			collection = new JSG_Indirection(collection);
		}
		boolean wasInMainStream = isInMainStream;
		isInMainStream = false;
		JSG_ForEach res = new JSG_ForEach(type, jsForEach.getName(), collection,
				jsForEach.getBody() != null ? (JSG_Block) jsForEach.getBody().visit(this) : null, jsForEach.getLine(),
				jsForEach.getColumn());
		isInMainStream = wasInMainStream;

		lastType = null;
		return res;
	}

	@Override
	public JSG_ForLoop accept(JSForLoop jsForLoop) throws RuntimeException {
		boolean wasInMainStream = isInMainStream;
		isInMainStream = false;
		JSG_ForLoop res = new JSG_ForLoop(jsForLoop.getInit().visit(this), jsForLoop.getCond().visit(this),
				jsForLoop.getStep().visit(this),
				jsForLoop.getBody() != null ? (JSG_Block) jsForLoop.getBody().visit(this) : null, jsForLoop.getLine(),
				jsForLoop.getColumn());
		isInMainStream = wasInMainStream;
		lastType = null;
		return res;
	}

	@Override
	public JSG_If accept(JSIf jsIf) throws RuntimeException {
		JSG_Expression cond = null;
		JSG_Instruction cons = new JSG_Block(new JSG_NOP(), true, jsIf.getLine(), jsIf.getColumn());
		JSG_Instruction alt = new JSG_NOP();
		if (jsIf.getCondition() != null)
			cond = jsIf.getCondition().visit(this);
		if (jsIf.getConsequence() != null)
			cons = jsIf.getConsequence().visit(this);
		if (jsIf.getAlternant() != null)
			alt = jsIf.getAlternant().visit(this);
		lastType = null;
		return new JSG_If(cond, cons, alt, jsIf.getLine(), jsIf.getColumn());
	}

	@Override
	public JSG_Sequence accept(JSSequence jsSequence) throws RuntimeException {
		ArrayList<JSG_Instruction> list = new ArrayList<>();
		for (final JSInstruction inst : jsSequence) {
			JSG_Instruction i = inst.visit(this);
			if (i != null)
				list.add(i);
		}
		return new JSG_Sequence(jsSequence.getLine(), jsSequence.getColumn(), list);
	}

	@Override
	public JSG_DoWhile accept(JSDoWhile jsDoWhile) throws RuntimeException {
		boolean wasInMainStream = isInMainStream;
		isInMainStream = false;
		JSG_DoWhile res = new JSG_DoWhile(jsDoWhile.getCondition().visit(this), jsDoWhile.getBody().visit(this),
				jsDoWhile.getLine(), jsDoWhile.getColumn());
		isInMainStream = wasInMainStream;
		lastType = null;
		return res;
	}

	@Override
	public JSG_Instruction accept(JSExprInstruction jsExprInstruction) throws RuntimeException {
		return new JSG_ExprInstruction(jsExprInstruction.getExpr().visit(this));
	}

	@Override
	public JSG_Expression accept(JSCall jsCall) throws RuntimeException {
		JSG_Type lastypeBuf = lastType; // a buffer for something like :
		// list.add(myElt);
		ArrayList<JSG_Expression> list = new ArrayList<>();
		for (JSExpression expr : jsCall.getArguments()) {
			list.add(expr.visit(this));
		}
		// System.out.println(lastypeBuf + " " + inScope + " -- " +
		// jsCall.getName() + " | "
		// + mapVariableToType.get(jsCall.getName()));

		if (inScope > 0 && (jsCall.getName().compareTo("add") == 0 || jsCall.getName().compareTo("push") == 0
				|| jsCall.getName().compareTo("push_back") == 0)) {
			if (lastypeBuf instanceof JSG_List)
				return new JSG_AddInList(list, new JSG_List(((JSG_List) lastypeBuf).getTypedList(), jsCall.getLine(), jsCall.getColumn()));
			else if (lastypeBuf instanceof JSG_TypeJerboaHookList) {
				return new JSG_AddInHookList(list);
			}
		} else if (mapVariableJerboaRuleToRule.containsKey(jsCall.getName())) {
			// mapVariableToType.containsKey(jsCall.getName())
			// && mapVariableToType.get(jsCall.getName()) instanceof
			// JSG_TypeJerboaRule) {
			// Si on est dans une variable qui est une JerboaRule on fait un
			// applyRule
			JSG_RuleReturnType returnType = JSG_RuleReturnType.NONE;
			if (inAssignement) {
				mapResultToRule.put(assignementVarName, lastRuleApplied.getName());
				returnType = JSG_RuleReturnType.FULL;
			}
			JSG_Expression rule = new JSG_Variable(jsCall.getName(), jsCall.getLine(), jsCall.getColumn());

			JMERule rulejme = null;
			if (mapVariableJerboaRuleToRule.containsKey(jsCall.getName())) {
				rule = new JSG_Cast(mapVariableJerboaRuleToRule.get(jsCall.getName()), rule);
				for (JMERule r : modeler.getRules()) {
					if (r.getName().compareToIgnoreCase(
							mapVariableJerboaRuleToRule.get(jsCall.getName()).getRuleName()) == 0) {
						rulejme = r;
					}
				}
			}
			return new JSG_ApplyRule(rulejme, rule, list, returnType, jsCall.getLine(), jsCall.getColumn());

		}
		String funcName = jsCall.getName();
		if (jsCall.getName().startsWith("@ebd")) {
			String ebdName = jsCall.getName().substring(jsCall.getName().indexOf("<") + 1,
					jsCall.getName().indexOf(">"));
			return new JSG_Constructor(
					new JSG_UserType(modeler.getEmbedding(ebdName), jsCall.getLine(), jsCall.getColumn()), list);
		}

		return new JSG_Call(funcName, list, jsCall.getLine(), jsCall.getColumn());
	}

	@Override
	public JSG_Expression accept(JSVariable jsVariable) throws RuntimeException {
		lastType = null;
		if (mapVariableToType.containsKey(jsVariable.getName())) {
			// TODO: faire autrement pour inclure les variable de modeleur et de
			// règle directement dans le contexte.
			// Il faut donc tester ici si elles existent dans ces derniers avant
			// ou pas car c'est des variables qui n'existent pas dans ce
			// contexte si elles sont masqué par des
			// variables locales. A voir
			lastType = mapVariableToType.get(jsVariable.getName());
		} else { // la variable locale masque le param�tre de r�gle
			JMERule rule = glue.getCurrentRule();
			if (glue.getLangageType() == LanguageType.EMBEDDING || glue.getLangageType() == LanguageType.RULE) {
				for (String e : glue.getRuleLeftNodesParam(glue.getRuleName())) {
					if (e.compareTo(jsVariable.getName()) == 0) {
						lastType = new JSG_TypeJerboaDart(jsVariable.getLine(), jsVariable.getColumn());
						return new JSG_LeftRuleNode(rule, jsVariable.getName(), jsVariable.getLine(),
								jsVariable.getColumn());
					}
				}
			} else if (glue.getLangageType() == LanguageType.SCRIPT) {
				for (String e : glue.getRuleLeftNodesParam(glue.getRuleName())) {
					if (e.compareTo(jsVariable.getName()) == 0) {
						lastType = new JSG_List(new JSG_TypeJerboaDart(jsVariable.getLine(), jsVariable.getColumn()),
								jsVariable.getLine(), jsVariable.getColumn());
						// return new JSG_Index(new JSG_KeywordHook(),
						// new JSG_Call(jsVariable.getName(), new
						// ArrayList<>()));
						return new JSG_LeftRuleNode(rule, jsVariable.getName(), jsVariable.getLine(),
								jsVariable.getColumn());
					}
				}
			}
			/**
			 * TODO: il faudrait tester aussi si les variables existent dans les
			 * param�tres de modeleur !
			 **/
			for (String e : glue.getEbdParams()) {
				if (e.compareTo(jsVariable.getName()) == 0) {
					lastType = getType(glue.getEbdParamType(e));
					JMEParamEbd param = null;
					if (rule != null) {
						for (JMEParamEbd pebd : rule.getParamsEbd()) {
							if (pebd.getName().compareTo(jsVariable.getName()) == 0) {
								param = pebd;
								break;
							}
						}
					}
					// TODO: mettre les paramètres de modeleur
					// if(param==null){
					// for(JMEParamEbd pebd : modeler.getParamEbd()){
					//
					// }
					// }
					if (glue.getLangageType() == LanguageType.EMBEDDING) {
						// si c'est une expression de plongement, il faut aller
						// chercher dans la classe m�re
						return new JSG_EbdParam(param, jsVariable.getName(), lastType, jsVariable.getLine(),
								jsVariable.getColumn());
					} else
						return new JSG_Variable(jsVariable.getName(), jsVariable.getLine(), jsVariable.getColumn());
				}
			}
		}
		return new JSG_Variable(jsVariable.getName(), jsVariable.getLine(), jsVariable.getColumn());
	}

	@Override
	public JSG_Orbit accept(JSOrbit jsOrbit) throws RuntimeException {
		final List<JSG_Expression> dim = new ArrayList<>();
		for (JSExpression e : jsOrbit.getDimensions()) {
			dim.add(e.visit(this));
		}
		lastType = new JSG_TypeOrbit(jsOrbit.getLine(), jsOrbit.getColumn());
		return new JSG_Orbit(dim, jsOrbit.getLine(), jsOrbit.getColumn());
	}

	@Override
	public JSG_Expression accept(JSInScope jsInScope) throws RuntimeException {
		inScope++;
		int line = jsInScope.getLine();
		int col = jsInScope.getColumn();

		if (jsInScope.getLeft() instanceof JSKeywordEbd && jsInScope.getRight() instanceof JSVariable) {
			JMEEmbeddingInfo ebdinfo = modeler.getEmbedding(((JSKeywordEbd) (jsInScope.getLeft())).getEbd());
			if (((JSVariable) jsInScope.getRight()).getName().compareToIgnoreCase("id") == 0) {
				lastType = new JSG_TypePrimitive("int", jsInScope.getLine(), jsInScope.getColumn());
				return new JSG_GetEbdId(ebdinfo);
			} else if (((JSVariable) jsInScope.getRight()).getName().compareToIgnoreCase("name") == 0) {
				lastType = new JSG_TypeString(jsInScope.getLine(), jsInScope.getColumn());
				return new JSG_GetEbdName(ebdinfo);
			} else if (((JSVariable) jsInScope.getRight()).getName().compareToIgnoreCase("type") == 0)
				return new JSG_UserType(ebdinfo, jsInScope.getLine(), jsInScope.getColumn());

		}

		JSG_Expression leftExp = jsInScope.getLeft().visit(this);
		JSG_Type leftType = lastType;
		JSG_Expression rightExp = jsInScope.getRight().visit(this);
		// JSG_Type rightType = lastType;
		// System.err.println("LEFT TYPE : " + leftType + " :" + leftExp + " ->
		// " + rightExp);

		boolean isAnEmbeddingGetting =
				// (jsInScope.getLeft() instanceof JSInScope
				// && ((JSInScope) jsInScope.getLeft()).getRight() instanceof
				// JSCall
				// && ((JSCall) ((JSInScope)
				// jsInScope.getLeft()).getRight()).getName().compareTo("ebd")
				// == 0)
				// ||
				(jsInScope.getRight() instanceof JSVariable
						&& glue.ebdExist(((JSVariable) jsInScope.getRight()).getName()));

		if (isAnEmbeddingGetting) {
			lastType = new JSG_UserType(modeler.getEmbedding(((JSVariable) jsInScope.getRight()).getName()),
					jsInScope.getLine(), jsInScope.getColumn());
			// return new JSG_Indirection(
			// new JSG_Cast(getType(glue.getEmbeddingType(((JSVariable)
			// jsInScope.getRight()).getName())),
			// new JSG_InScope(new JSG_Indirection(leftExp),
			// new JSG_GetEbd(((JSVariable) jsInScope.getRight()).getName()))));
			return new JSG_GetEbd(leftExp, modeler.getEmbedding(((JSVariable) jsInScope.getRight()).getName()));
		}

		String ebdName = null;
		if ((jsInScope.getRight() instanceof JSVariable
				&& glue.ebdExist(((JSVariable) jsInScope.getRight()).getName()))) {
			ebdName = glue.getEmbeddingType(((JSVariable) jsInScope.getRight()).getName());
		}

		boolean isPointer = (jsInScope.getLeft() instanceof JSVariable
				&& variableIsPointer.containsKey(((JSVariable) jsInScope.getLeft()).getName())
				&& variableIsPointer.get(((JSVariable) jsInScope.getLeft()).getName()))
				// on test si le nom de variable est ref en tant que pointer
				|| jsInScope.getLeft() instanceof JSKeywordGmap || jsInScope.getLeft() instanceof JSKeywordModeler
				|| leftType instanceof JSG_TypeJerboaDart || leftType instanceof JSG_TypeJerboaRule;

		if (isPointer) {// || (jsInScope.getLeft() instanceof JSInScope) &&
			// leftScopeWasAnEmbeddingGetting))
			// System.err.println(
			// "pointeur : " + jsInScope.getLine() + " " + jsInScope.getColumn()
			// + " " + jsInScope.getLeft());
			leftExp = new JSG_Indirection(leftExp);
			lastType = null; // TODO: fixer ça!
		}

		JSG_Expression res = null;
		if (jsInScope.getLeft() instanceof JSKeywordGmap || jsInScope.getLeft() instanceof JSKeywordModeler) {
			res = new JSG_InScope(leftExp, rightExp);
		} else if (jsInScope.getLeft() instanceof JSVariable
				&& mapResultToRule.containsKey(((JSVariable) jsInScope.getLeft()).getName())) {
			// si on est dans un "JerboaRuleResult.qqch"
			JMERule rule = null;
			if (glue.getModeler() != null) {
				for (JMERule mrule : glue.getModeler().getRules()) {
					if (mrule.getName()
							.compareTo(mapResultToRule.get(((JSVariable) jsInScope.getLeft()).getName())) == 0) {
						rule = mrule;
						break;
					}
				}
			}
			if (jsInScope.getRight() instanceof JSIndex) {
				lastType = new JSG_TypeJerboaDart(jsInScope.getLine(), jsInScope.getColumn());
//				JSG_RuleNode ruleNode = new JSG_RuleNode(jsInScope.getLine(), jsInScope.getColumn(),
//						new JSG_Rule(mapResultToRule.get(((JSVariable) jsInScope.getLeft()).getName()), rule, line,
//								col),
//						((JSIndex)jsInScope.getRight()).getVariable().visit(this));		
				JSG_IndexRuleNode nodeIndex = new JSG_IndexRuleNode(SIDE.RIGHT, rule,
						((JSVariable)jsInScope.getRight()).getName(), line, col);		
				res = new JSG_IndexInRuleResult(leftExp, nodeIndex,
						((JSIndex) jsInScope.getRight()).getIndex().visit(this));
			} else if (jsInScope.getRight() instanceof JSVariable) {
				String varName = ((JSVariable) jsInScope.getRight()).getName();
				if (varName.compareToIgnoreCase("height") == 0) {
					lastType = new JSG_TypePrimitive("int", jsInScope.getLine(), jsInScope.getColumn());
					return new JSG_InScope(leftExp, new JSG_CallRuleResHeight());
				} else if (varName.compareToIgnoreCase("width") == 0) {
					lastType = new JSG_TypePrimitive("int", jsInScope.getLine(), jsInScope.getColumn());
					return new JSG_InScope(leftExp, new JSG_CallRuleResWidth());
				} else if (varName.compareToIgnoreCase("size") == 0) {
					lastType = new JSG_TypePrimitive("int", jsInScope.getLine(), jsInScope.getColumn());
					return new JSG_InScope(leftExp, new JSG_CallListSize());
				}

				// TODO: faire le code en commentaire !
				// if (mapVariableToType.get(varName) instanceof
				// JSG_TypeJerboaRule) {
				// System.err.println("typeRule appellé -> faire un
				// applyRule");
				// return new JSG_ApplyRule()
				// } else {
				lastType = new JSG_List(new JSG_TypeJerboaDart(jsInScope.getLine(), jsInScope.getColumn()),
						jsInScope.getLine(), jsInScope.getColumn());
				JSG_IndexRuleNode nodeIndex = new JSG_IndexRuleNode(SIDE.RIGHT, rule,
						((JSVariable)jsInScope.getRight()).getName(), 
						jsInScope.getLine(), jsInScope.getColumn());
				res = new JSG_IndexInRuleResult(leftExp, nodeIndex, null);
			} else {
				res = new JSG_InScope(leftExp, rightExp);
			}
		} else {
			if (leftType instanceof JSG_TypeJerboaDart) {
				if (rightExp instanceof JSG_Call) {
					// System.err.println("No function call on JerboaDart type ! line " +
					// jsInScope.getLine() + ", col "
					// + jsInScope.getColumn());
					// return leftExp; // TODO: Val : ici j'enleve l'occultation de fonction
					// appellée sur JerboaDart !! attentions aux potentielles modifications topo !!
					res = new JSG_InScope(leftExp, rightExp);
				} else if (((jsInScope.getRight() instanceof JSVariable
						&& ((JSVariable) jsInScope.getRight()).getName().compareToIgnoreCase("id") == 0)
						|| (jsInScope.getRight() instanceof JSCall
								&& ((JSCall) jsInScope.getRight()).getName().compareToIgnoreCase("id") == 0))) {
					lastType = new JSG_TypePrimitive("int", jsInScope.getLine(), jsInScope.getColumn());
					res = new JSG_InScope(leftExp, new JSG_GetNodeId());
				}
			} else
				res = new JSG_InScope(leftExp, rightExp);
		}

		if (isAnEmbeddingGetting && ebdName != null) {
			res = new JSG_Cast(getType(glue.getEmbeddingType(ebdName)), res);
		}
		inScope--;
		return res;
	}

	@Override
	public JSG_Expression accept(JSCollectEbd jsCollect) throws RuntimeException {
		JSG_CollectEbd res = new JSG_CollectEbd((JSG_Orbit) jsCollect.getOrbit().visit(this), jsCollect.getEmbedding(),
				jsCollect.getNode().visit(this), glue.getEmbeddingType(jsCollect.getEmbedding()), gmapHasDirectAccess,
				jsCollect.getLine(), jsCollect.getColumn());
		lastType = new JSG_List(new JSG_UserType(modeler.getEmbedding(jsCollect.getEmbedding()), jsCollect.getLine(),
				jsCollect.getColumn()), jsCollect.getLine(), jsCollect.getColumn());
		return res;
	}

	@Override
	public JSG_Expression accept(JSCollect jsCollect) throws RuntimeException {
		JSG_Collect res = new JSG_Collect((JSG_Orbit) jsCollect.getOrbit().visit(this),
				(jsCollect.getSubOrbit() != null) ? (JSG_Orbit) jsCollect.getSubOrbit().visit(this) : null,
				jsCollect.getNode().visit(this), gmapHasDirectAccess, jsCollect.getLine(), jsCollect.getColumn());
		lastType = new JSG_List(new JSG_TypeJerboaDart(jsCollect.getLine(), jsCollect.getColumn()), jsCollect.getLine(),
				jsCollect.getColumn());
		return res;
	}

	@Override
	public JSG_Alpha accept(JSAlpha jsAlpha) throws RuntimeException {
		JSG_Alpha res = new JSG_Alpha(jsAlpha.getNode().visit(this), jsAlpha.getDim().visit(this), jsAlpha.getLine(),
				jsAlpha.getColumn());
		lastType = new JSG_TypeJerboaDart(jsAlpha.getLine(), jsAlpha.getColumn());
		return res;
	}

	@Override
	public JSG_Integer accept(JSInteger jsInteger) throws RuntimeException {
		lastType = new JSG_TypePrimitive("int", jsInteger.getLine(), jsInteger.getColumn());
		return new JSG_Integer(jsInteger.getValue(), jsInteger.getLine(), jsInteger.getColumn());
	}

	@Override
	public JSG_Float accept(JSFloat jsFloat) throws RuntimeException {
		lastType = new JSG_TypePrimitive("float", jsFloat.getLine(), jsFloat.getColumn());
		return new JSG_Float(jsFloat.getValue(), jsFloat.getLine(), jsFloat.getColumn());
	}

	@Override
	public JSG_Double accept(JSDouble jsDouble) throws RuntimeException {
		lastType = new JSG_TypePrimitive("double", jsDouble.getLine(), jsDouble.getColumn());
		return new JSG_Double(jsDouble.getValue(), jsDouble.getLine(), jsDouble.getColumn());
	}

	@Override
	public JSG_Boolean accept(JSBoolean jsBoolean) throws RuntimeException {
		lastType = new JSG_TypePrimitive("boolean", jsBoolean.getLine(), jsBoolean.getColumn());
		return new JSG_Boolean(jsBoolean.getValue(), jsBoolean.getLine(), jsBoolean.getColumn());
	}

	@Override
	public JSG_Long accept(JSLong jsLong) throws RuntimeException {
		lastType = new JSG_TypePrimitive("long", jsLong.getLine(), jsLong.getColumn());
		return new JSG_Long(jsLong.getValue(), jsLong.getLine(), jsLong.getColumn());
	}

	@Override
	public JSG_String accept(JSString jsString) throws RuntimeException {
		lastType = new JSG_TypePrimitive("string", jsString.getLine(), jsString.getColumn());
		return new JSG_String(jsString.getValue(), jsString.getLine(), jsString.getColumn());
	}

	@Override
	public JSG_Expression accept(JSApplyRule jsApplyRule) throws RuntimeException {
		/**
		 * TODO: Revoir quand on assigne a une variable le resultat, alors que des hooks
		 * sont passés à la règle
		 **/
		lastRuleApplied = jsApplyRule.getRule();
		JSG_RuleReturnType returnType = JSG_RuleReturnType.NONE;
		if (inAssignement) {
			mapResultToRule.put(assignementVarName, lastRuleApplied.getName());
			returnType = JSG_RuleReturnType.FULL;
		}
		ArrayList<JSG_Expression> ruleArgs = new ArrayList<>();

		lastType = new JSG_TypeJerboaRuleResult(jsApplyRule.getLine(), jsApplyRule.getColumn());

		for (JSRuleArg ra : jsApplyRule.getArgs()) {
			ruleArgs.add(ra.visit(this));
		}
		JMERule rule = null;
		for (JMERule r : modeler.getRules()) {
			if (r.getName().compareTo(jsApplyRule.getRuleName()) == 0) {
				rule = r;
				break;
			}
		}
		return new JSG_ApplyRule(rule, jsApplyRule.getRule().visit(this), ruleArgs, returnType, jsApplyRule.getLine(),
				jsApplyRule.getColumn());

		// ArrayList<JSG_AssocParam> listPar = new ArrayList<>();
		// ArrayList<JSG_HookCall> listH = new ArrayList<>();
		// if (!inAssignement)
		// seq.add(prepareRule(jsApplyRule));
		// for (JSRuleArg p : jsApplyRule.getArgs()) {
		// JSG_Expression e = p.visit(this);
		// if (e instanceof JSG_HookCall) {
		// listH.add((JSG_HookCall) e);
		// } else if (e instanceof JSG_AssocParam) {
		// listPar.add(new JSG_AssocParam((JSG_Rule)
		// jsApplyRule.getRule().visit(this),
		// ((JSG_AssocParam) e).getParamValue(), p.getArgName()));
		// }
		// }
		// if (inAssignement) {
		// return new JSG_ApplyRule((JSG_Rule)
		// jsApplyRule.getRule().visit(this), listH, listPar, "ROW",
		// jsApplyRule.getLine(), jsApplyRule.getColumn());
		// } else
		// seq.add(new JSG_ExprInstruction(new JSG_ApplyRule((JSG_Rule)
		// jsApplyRule.getRule().visit(this), listH,
		// listPar, "NONE", jsApplyRule.getLine(), jsApplyRule.getColumn())));
		// return new JSG_InstructionExpr(seq);
	}

	@Override
	public JSG_Operator accept(JSOperator jsOperator) throws RuntimeException {
		Collection<JSG_Expression> list = new ArrayList<>();
		for (JSExpression e : jsOperator.getOperands()) {
			if (e != null)
				list.add(e.visit(this));
			else
				list.add(null);
		}
		// the 'lastType' value will be the type of the last visited operand
		return new JSG_Operator(jsOperator.getLine(), jsOperator.getColumn(), jsOperator.getOperator(), list);
	}

	@Override
	public JSG_Expression accept(JSNot jsNot) throws RuntimeException {
		lastType = new JSG_TypeBoolean(jsNot.getLine(), jsNot.getColumn());
		JSG_Expression exp = jsNot.getExpr().visit(this);
		if (exp instanceof JSG_IsMarked) {
			return new JSG_IsNotMarked(((JSG_IsMarked) exp).getLeft(), ((JSG_IsMarked) exp).getMark(), jsNot.getLine(),
					jsNot.getColumn());
		} else if (exp instanceof JSG_IsNotMarked) {
			return new JSG_IsMarked(((JSG_IsNotMarked) exp).getLeft(), ((JSG_IsNotMarked) exp).getMark(),
					jsNot.getLine(), jsNot.getColumn());
		}
		return new JSG_Not(jsNot.getExpr().visit(this), jsNot.getLine(), jsNot.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSReturn jsReturn) throws RuntimeException {
		if (isInMainStream)
			mainStreamHasReturn = true;
		JSG_Expression returnVal = new JSG_Null();

		if (jsReturn.getExpression() != null && !(jsReturn.getExpression() instanceof JSNull)) {
			returnVal = jsReturn.getExpression().visit(this);
		}
		// else if (glue.getLangagesState() == LanguageState.CLASSICAL &&
		// glue.getLangageType() == LanguageType.SCRIPT)
		// {
		// returnVal = new JSG_Constructor(new
		// JSG_TypeJerboaRuleResult(jsReturn.getLine(), jsReturn.getColumn()),
		// new ArrayList<>());
		// }
		return new JSG_Return(jsReturn.getLine(), jsReturn.getColumn(), returnVal);
	}

	@Override
	public JSG_KeywordGmap accept(JSKeywordGmap jsGmapKeyword) throws RuntimeException {
		return new JSG_KeywordGmap(gmapHasDirectAccess, jsGmapKeyword.getLine(), jsGmapKeyword.getColumn());
	}

	@Override
	public JSG_Expression accept(JSIndex jsIndex) throws RuntimeException {
		//jsIndex.getVariable().visit(this);
		JSG_Expression varExpr = jsIndex.getVariable().visit(this);
		JSG_Type varType = lastType;
		
		int idxL = jsIndex.getLine();
		int idxC = jsIndex.getColumn();

		JSG_Expression indexExpr = jsIndex.getIndex().visit(this);

		if (varExpr  instanceof JSG_LeftRuleNode) {
			// si on a une recherche d'indexe dans le noeud de gauche
			JMENode nodeToCall = null;
			try {
				nodeToCall = glue.getCurrentRule().getLeft().getMatchNode(((JSG_LeftRuleNode)varExpr).getName());
			}catch(Exception e) {}
			if(nodeToCall!=null) {
//				if(glue.getLangageType() == LanguageType.EMBEDDING) {
					lastType = new JSG_TypeJerboaDart(jsIndex.getLine(), jsIndex.getColumn());
//				}else {
//					lastType = new JSG_List(new JSG_TypeJerboaDart(jsIndex.getLine(), jsIndex.getColumn()),
//							jsIndex.getLine(), jsIndex.getColumn());
//				}
				return new JSG_GetTopoParam(nodeToCall, indexExpr);
			}
//		}else if(varExpr instanceof JSG_RuleNode) { // Val: je crois que ça n'est plus utile.. a voir
//			JSG_RuleNode rlnode = (JSG_RuleNode) varExpr;
//			rlnode.setExp(jsIndex.getIndex().visit(this));
//			lastType = new JSG_TypeJerboaDart(jsIndex.getLine(), jsIndex.getColumn());
//			return rlnode;
		}else if (varExpr instanceof JSG_List) {
			lastType = ((JSG_List) varExpr).getTypedList();
		}else if (varType instanceof JSG_List) {
			lastType = ((JSG_List) varType).getTypedList();
		} else if (jsIndex.getVariable() instanceof JSKeywordGmap) {
			lastType = new JSG_TypeJerboaDart(idxL, idxC);
			// TODO: refaire une classe spécifique
			return new JSG_InScope(
					new JSG_Indirection(
							new JSG_KeywordGmap(gmapHasDirectAccess, idxL, idxC)),
					new JSG_IndexNodeInGmap(indexExpr));
		} else if (jsIndex.getVariable() instanceof JSVariable
				&& mapResultToRule.containsKey(((JSVariable) jsIndex.getVariable()).getName())) {
			// si on est dans un "JerboaRuleResult.qqch"
			JMERule rule = null;
			if (glue.getModeler() != null) {
				for (JMERule mrule : glue.getModeler().getRules()) {
					if (mrule.getName()
							.compareTo(mapResultToRule.get(((JSVariable) jsIndex.getVariable()).getName())) == 0) {
						rule = mrule;
						break;
					}
				}
			}
			if (jsIndex.getIndex() instanceof JSIndex) {
				lastType = new JSG_TypeJerboaDart(idxL, idxC);
//				JSG_RuleNode ruleNode = new JSG_RuleNode(idxL, idxC,
//						new JSG_Rule(mapResultToRule.get(((JSVariable) jsIndex.getVariable()).getName()), rule, idxL,
//								idxC),
//						((JSIndex)jsIndex.getIndex()).getVariable().visit(this));
				JSG_IndexRuleNode nodeIndex = new JSG_IndexRuleNode(SIDE.RIGHT, rule,
						((JSVariable)jsIndex.getIndex()).getName(), 
						idxL, idxC);
				return new JSG_IndexInRuleResult(varExpr, nodeIndex,
						((JSIndex) jsIndex.getIndex()).getIndex().visit(this));
			} else if (jsIndex.getIndex() instanceof JSVariable) {
				String varName = ((JSVariable) jsIndex.getIndex()).getName();
				if (varName.compareToIgnoreCase("height") == 0) {
					lastType = new JSG_TypePrimitive("int", idxL, idxC);
					return new JSG_InScope(varExpr, new JSG_CallRuleResHeight());
				} else if (varName.compareToIgnoreCase("width") == 0) {
					lastType = new JSG_TypePrimitive("int", idxL, idxC);
					return new JSG_InScope(varExpr, new JSG_CallRuleResWidth());
				} else if (varName.compareToIgnoreCase("size") == 0) {
					lastType = new JSG_TypePrimitive("int", idxL, idxC);
					return new JSG_InScope(varExpr, new JSG_CallListSize());
				}

				// TODO: faire le code en commentaire !
				// if (mapVariableToType.get(varName) instanceof
				// JSG_TypeJerboaRule) {
				// System.err.println("typeRule appellé -> faire un
				// applyRule");
				// return new JSG_ApplyRule()
				// } else {
				lastType = new JSG_List(new JSG_TypeJerboaDart(idxL, idxC), idxL, idxC);
				JSG_IndexRuleNode nodeIndex = new JSG_IndexRuleNode(SIDE.RIGHT, rule,
						((JSVariable)jsIndex.getIndex()).getName(), 
						idxL, idxC);
				return new JSG_IndexInRuleResult(varExpr, nodeIndex, null);
			}
		}/*
		else if (varType instanceof JSG_TypeJerboaRuleResult) {
			// acces au motif droit d'un resultat de regle
			
			lastType = new JSG_List(new JSG_TypeJerboaDart(jsIndex.getLine(), jsIndex.getColumn()), jsIndex.getLine(),
					jsIndex.getColumn());
			if(indexExpr instanceof JSG_Variable) {
				String lastRuleAppyed = mapResultToRule.get(((JSG_Variable)indexExpr).getName());
				if(lastRuleAppyed!=null) {
					JMERule ruleToFind = null;
					int indexRules = 0;
					while(indexRules < modeler.getRules().size() && ruleToFind == null) {
						if(modeler.getRules().get(indexRules).getName().compareTo(lastRuleAppyed)==0) {
							ruleToFind = modeler.getRules().get(indexRules);
						}
					}
					if(ruleToFind != null) {
//						JMENode node = ruleToFind.getRight().getMatchNode(((JSG_Variable) indexExpr).getName());
//						if(node != null) { // On teste plutot dans les v�rifs
//							JSG_Rule rule = new JSG_Rule(lastRuleAppyed, ruleToFind, ((JSG_Variable) indexExpr).getLine(), ((JSG_Variable) indexExpr).getColumn());
							indexExpr = new JSG_IndexRuleNode(SIDE.RIGHT, ruleToFind, ((JSG_Variable) indexExpr).getName(), 
									((JSG_Variable) indexExpr).getLine(), ((JSG_Variable) indexExpr).getColumn());
//							indexExpr = new JSG_RuleNode(((JSG_Variable) indexExpr).getLine(), ((JSG_Variable) indexExpr).getColumn(),
//									rule, indexExpr);
//						}
					}else {
						System.err.println("no rule named '"+modeler.getRules().get(indexRules).getName()+"' at line " + 
								((JSG_Variable) indexExpr).getLine() + " and column " + ((JSG_Variable) indexExpr).getColumn());
					}
					
				}else {
					System.err.println("no rule mapping for index in rule result at line " + 
							((JSG_Variable) indexExpr).getLine() + " and column " + ((JSG_Variable) indexExpr).getColumn());
				}
			}			
			return new JSG_IndexInRuleResult(varExpr, indexExpr, null);
		} */else if(varExpr  instanceof JSG_LeftRuleNode) {
			// si on a une recherche d'indexe dans le noeud de gauche
			JMENode nodeToCall = null;
			try {
				nodeToCall = glue.getCurrentRule().getLeft().getMatchNode(((JSG_LeftRuleNode)varExpr).getName());
			}catch(Exception e) {}
			if(nodeToCall!=null) {
				return new JSG_GetTopoParam(nodeToCall, indexExpr);
			}
		}else {
			lastType = null;
		}
		return new JSG_Index(varExpr, indexExpr);
	}

	@Override
	public JSG_Map accept(JSMap map) {
		return new JSG_Map((JSG_Type) map.getType().visit(this), map.getVar(), map.getBody().visit(this),
				map.getExpr().visit(this), map.getLine(), map.getColumn());
	}

	@Override
	public JSG_NOP accept(JSNOP jsEmpty) {
		return new JSG_NOP();
	}

	@Override
	public JSG_Type accept(JSType jsType) {
		JSG_Type res;
		if (jsType.getType().compareToIgnoreCase(ruleResultTypeName) == 0) {
			res = new JSG_TypeJerboaRuleResult(jsType.getLine(), jsType.getColumn());
		} else if (jsType.getType().compareToIgnoreCase("JerboaDarts") == 0) {
			res = new JSG_List(new JSG_TypeJerboaDart(jsType.getLine(), jsType.getColumn()), jsType.getLine(),
					jsType.getColumn());
		} else if (jsType.getType().compareToIgnoreCase("JerboaHooks") == 0
				|| jsType.getType().compareToIgnoreCase("JerboaHookList") == 0) {
			res = new JSG_TypeJerboaHookList(null, jsType.getLine(), jsType.getColumn()); // TODO:
			// attention
			// a
			// la
			// règle
			// nulle
		} else if (jsType.getType().compareToIgnoreCase(nodeTypeName) == 0) {
			res = new JSG_TypeJerboaDart(jsType.getLine(), jsType.getColumn());
		} else if (jsType.getType().compareToIgnoreCase("JerboaRule") == 0) {
			res = new JSG_TypeJerboaRule(null, jsType.getLine(), jsType.getColumn());
		} else if (jsType instanceof JSKeywordEbd) {
			res = new JSG_UserType(modeler.getEmbedding(((JSKeywordEbd) jsType).getEbd()), jsType.getLine(),
					jsType.getColumn());
		} else if (jsType.getType().compareToIgnoreCase("boolean") == 0
				|| jsType.getType().compareToIgnoreCase("bool") == 0) {
			res = new JSG_TypeBoolean(jsType.getLine(), jsType.getColumn());
		} else if (jsType.getType().compareToIgnoreCase("string") == 0) {
			res = new JSG_TypeString(jsType.getLine(), jsType.getColumn());
		} else if (jsType.getType().compareToIgnoreCase("JerboaOrbit") == 0) {
			res = new JSG_TypeOrbit(jsType.getLine(), jsType.getColumn());
		} else if (jsType.getType().compareToIgnoreCase("JerboaMark") == 0) {
			res = new JSG_TypeMark(jsType.getLine(), jsType.getColumn());
		} else
			res = new JSG_TypePrimitive(jsType.getType(), jsType.getLine(), jsType.getColumn());

		lastType = res;
		return res;
	}

	@Override
	public JSG_HookCall accept(JSHookCall jsHookCall) {
		return new JSG_HookCall(jsHookCall.getNodename().visit(this), jsHookCall.getHookname(), jsHookCall.getLine(),
				jsHookCall.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSAssocParam jsAssocParam) {
		return new JSG_AssocParam((JSG_Rule) jsAssocParam.getRule().visit(this),
				jsAssocParam.getParamValue().visit(this), jsAssocParam.getParamName(), jsAssocParam.getLine(),
				jsAssocParam.getColumn());
	}

	@Override
	public JSG_Choice accept(JSChoice jsAlternativ) {
		ArrayList<JSG_Expression> list = new ArrayList<JSG_Expression>();
		for (JSApplyRule r : jsAlternativ.getRules()) {
			lastRuleApplied = r.getRule();
			list.add(r.visit(this));
		}
		String varRes = "";
		if (inAssignement)
			varRes = assignementVarName;
		lastType = new JSG_TypeJerboaRuleResult(jsAlternativ.getLine(), jsAlternativ.getColumn());
		return new JSG_Choice(list, varRes, jsAlternativ.getLine(), jsAlternativ.getColumn());
	}

	@Override
	public JSG_Expression accept(JSKeywordEbd jsKeywordEbd) {
		if (jsKeywordEbd.getEbd() != null && jsKeywordEbd.getEbd().replaceAll("\\s", "").length() > 0) {
			JMEEmbeddingInfo ebdInfo = modeler.getEmbedding(jsKeywordEbd.getEbd());
			if (ebdInfo == null || jsKeywordEbd.getEbd() != null) {
				if (jsKeywordEbd.getRequest() == EBDRequest.NAME) {
					lastType = new JSG_TypeString(jsKeywordEbd.getLine(), jsKeywordEbd.getColumn());
					return new JSG_GetEbdName(ebdInfo);
				} else if (jsKeywordEbd.getRequest() == EBDRequest.IDT) {
					lastType = new JSG_TypePrimitive("int", jsKeywordEbd.getLine(), jsKeywordEbd.getColumn());
					return new JSG_GetEbdId(ebdInfo);
				} else if (jsKeywordEbd.getRequest() == EBDRequest.ORBITE) {
					lastType = new JSG_TypeOrbit(jsKeywordEbd.getLine(), jsKeywordEbd.getColumn());
					return new JSG_GetEbdOrbit(ebdInfo);
				} else
					return new JSG_UserType(ebdInfo, jsKeywordEbd.getLine(), jsKeywordEbd.getColumn());
			}
		}
		return new JSG_UserType(null, jsKeywordEbd.getLine(), jsKeywordEbd.getColumn());
	}

	@Override
	public JSG_List accept(JSList jsList) {
		// Out.print("le type de la list c'est : " + jsList.getTypedList());
		JSG_List res = new JSG_List((JSG_Type) jsList.getTypedList().visit(this), jsList.getLine(), jsList.getColumn());
		lastType = res;
		return res;
	}

	@Override
	public JSG_Rule accept(JSRule jsRule) {
		lastType = new JSG_TypeJerboaRule(jsRule.getName(), jsRule.getLine(), jsRule.getColumn());
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
		return new JSG_Rule(rule.getName(), rule, jsRule.getLine(), jsRule.getColumn());
	}

	@Override
	public JSG_Print accept(JSPrint jsPrint) {
		final List<JSG_Expression> args = new ArrayList<>();
		for (JSExpression e : jsPrint.getArguments()) {
			JSG_Expression exp = e.visit(this);
			if (isAPointerType(lastType)) { // TODO: ou alors on laisse l'erreur
				// pour l'utilisateur ?
				exp = new JSG_Indirection(exp);
			}
			args.add(exp);
		}
		return new JSG_Print(jsPrint.getLine(), jsPrint.getColumn(), args);
	}

	@Override
	public JSG_KeywordDimension accept(JSKeywordDimension jsKeywordDimension) {
		lastType = new JSG_TypePrimitive("int", 1, -1);
		return new JSG_KeywordDimension();
	}

	@Override
	public JSG_KeywordModeler accept(JSKeywordModeler jsKeywordModeler) {
		lastType = null; // TODO: Pas de type modeler pour l'instant mais pk pas
		return new JSG_KeywordModeler(modeler);// new
		// JSG_KeywordModeler(jsKeywordModeler.getLine(),
		// jsKeywordModeler.getColumn());
	}

	@Override
	public JSG_Expression accept(JSUnreference jsUnreference) {
		return new JSG_Unreference(jsUnreference.getExp().visit(this));
	}

	@Override
	public JSG_Expression accept(JSIndirection jsIndirection) {
		JSG_Expression exp = jsIndirection.getExp().visit(this);
		lastType = null; // TODO: fixer ça !
		return new JSG_Indirection(exp);
	}

	@Override
	public JSG_Expression accept(JSComment jsComment) {
		return new JSG_Comment(jsComment.getComment());
	}

	// private JSG_Sequence prepareRule(JSApplyRule apR) {
	// JSG_Sequence seq = new JSG_Sequence(apR.getLine(), apR.getColumn());
	// for (JSRuleArg p : apR.getArgs()) {
	// JSG_Instruction e = p.visit(this);
	// seq.add(e);
	// }
	// return seq;
	// }

	@Override
	public JSG_Instruction accept(JSDelete jsDelete) {
		if (jsDelete.getName() instanceof JSVariable) {
			if (mapVariableToType.get(((JSVariable) jsDelete.getName()).getName()) instanceof JSG_TypeMark) {
				return new JSG_FreeMarker(gmapHasDirectAccess, jsDelete.getName().visit(this), jsDelete.getLine(),
						jsDelete.getColumn());
			}
		}
		return new JSG_Delete(jsDelete.getName().visit(this), jsDelete.getLine(), jsDelete.getColumn());
	}

	@Override
	public JSG_Expression accept(JSNew jsNew) {
		return new JSG_New(jsNew.getExp().visit(this));
	}

	@Override
	public JSG_Expression accept(JSConstructor jsConstructor) {
		ArrayList<JSG_Expression> list = new ArrayList<>();
		for (JSExpression e : jsConstructor.getArguments()) {
			list.add(e.visit(this));
		}
		JSG_Type type = (JSG_Type) jsConstructor.getName().visit(this);
		if (mapVariableToType.containsKey(jsConstructor.getName().getType())
				&& mapVariableToType.get(jsConstructor.getName().getType()) instanceof JSG_TypeJerboaRule) {
			// Si on est dans une variable qui est une JerboaRule on fait un
			// applyRule
			JSG_RuleReturnType returnType = JSG_RuleReturnType.NONE;
			if (inAssignement) {
				mapResultToRule.put(assignementVarName, lastRuleApplied.getName());
				returnType = JSG_RuleReturnType.FULL;
			}
			JSG_Expression rule = new JSG_Variable(jsConstructor.getName().getType(), jsConstructor.getLine(),
					jsConstructor.getColumn());
			if (mapVariableJerboaRuleToRule.containsKey(jsConstructor.getName().getType())) {
				rule = new JSG_Cast(mapVariableJerboaRuleToRule.get(jsConstructor.getName().getType()), rule);
			}
			// TODO: corriger pour ne pas avoir de règle nulle !
			return new JSG_ApplyRule(null, rule, list, returnType, jsConstructor.getLine(), jsConstructor.getColumn());
		} else if (type instanceof JSG_TypePrimitive && !isPrimitivType(jsConstructor.getName().getType())
				&& !isEbdType(jsConstructor.getName().getType(), glue.getModeler())) {
			return new JSG_Call(jsConstructor.getName().getType(), list, jsConstructor.getLine(), jsConstructor.getColumn());
		}

		return new JSG_Constructor(jsConstructor.getName().visit(this), list);
	}

	@Override
	public JSG_Catch accept(JSCatch jsCatch) {
		boolean wasInMainStream = isInMainStream;
		isInMainStream = false;
		JSG_Instruction catchDeclar = new JSDeclare(jsCatch.getTypeExep(), jsCatch.getNameExep(), null,
				jsCatch.getLine(), jsCatch.getColumn()).visit(this);
		JSG_Block block = (JSG_Block) (jsCatch.getBlock() != null ? jsCatch.getBlock().visit(this) : null);
		if (block != null) {
			// if (block.getBody() instanceof JSG_Sequence) {
			// // ((JSG_Sequence) block.getBody()).add(new
			// // JSG_ClearHookList(jsCatch.getLine(), jsCatch.getColumn()));
			// } else {
			// JSG_Sequence seq = new JSG_Sequence(jsCatch.getLine(),
			// jsCatch.getColumn());
			// seq.add(block.getBody());
			// // seq.add(new JSG_ClearHookList(jsCatch.getLine(),
			// // jsCatch.getColumn()));
			// block = new JSG_Block(jsCatch.getLine(), jsCatch.getColumn(),
			// seq);
			// }
		} else {
			block = new JSG_Block(new JSG_NOP(), true, jsCatch.getLine(), jsCatch.getColumn());
		}
		JSG_Catch res = new JSG_Catch(block, catchDeclar, jsCatch.getLine(), jsCatch.getColumn());
		isInMainStream = wasInMainStream;
		return res;
	}

	@Override
	public JSG_Instruction accept(JSTry jsTry) {
		ArrayList<JSG_Catch> catchlist = new ArrayList<>();
		if (jsTry.getCatchList() != null) {
			for (JSCatch jsCatch : jsTry.getCatchList()) {
				catchlist.add((JSG_Catch) (jsCatch.visit(this)));
			}
		}

		return new JSG_Try((jsTry.getTryBlock() != null ? jsTry.getTryBlock().visit(this) : new JSG_NOP()), catchlist,
				(jsTry.getFinallyBlock() != null ? jsTry.getFinallyBlock().visit(this) : new JSG_NOP()),
				jsTry.getLine(), jsTry.getColumn());
	}

	@Override
	public JSG_DeclareFunction accept(JSDeclareFunction jsDeclareFunction) {
		boolean wasInMainStream = isInMainStream;
		boolean hadReturnInMainStream = mainStreamHasReturn;
		isInMainStream = true;
		mainStreamHasReturn = false;
		Collection<JSG_Instruction> args = new ArrayList<>();
		for (JSDeclare e : jsDeclareFunction.getArguments()) {
			args.add(((JSG_Sequence) (e.visit(this))).get(0));
		}
		JSG_DeclareFunction res = new JSG_DeclareFunction(
				(jsDeclareFunction.getReturnType() != null ? (JSG_Type) jsDeclareFunction.getReturnType().visit(this)
						: null),
				jsDeclareFunction.getName(), args, (JSG_Block) jsDeclareFunction.getBlock().visit(this),
				jsDeclareFunction.getLine(), jsDeclareFunction.getColumn());
		if (!mainStreamHasReturn && jsDeclareFunction.getReturnType() != null) {
			// si la fonction a un type de retour
		}
		mainStreamHasReturn = hadReturnInMainStream;
		isInMainStream = wasInMainStream;
		return res;
	}

	@Override
	public JSG_Expression accept(JSInScopeStatic jsInScopeStatic) {
		return new JSG_InScopeStatic((JSG_Type) jsInScopeStatic.getLeft().visit(this),
				jsInScopeStatic.getRight().visit(this));
	}

	@Override
	public JSG_Instruction accept(JSAtLang jsAtLang) {
		return new JSG_AtLang(jsAtLang.getLanguage(), jsAtLang.getCode(), jsAtLang.getLine(), jsAtLang.getColumn());
	}

	@Override
	public JSG_Expression accept(JSNull jsNull) {
		lastType = null;
		return new JSG_Null();
	}

	@Override
	public JSG_Instruction accept(JSBreak jsBreak) {
		return new JSG_Break(jsBreak.getLine(), jsBreak.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSHeader jsHeader) {
		return new JSG_Header(jsHeader.getLanguage(), jsHeader.getCode(), jsHeader.getLine(), jsHeader.getColumn());
	}

	@Override
	public JSG_TypeTemplate accept(JSTypeTemplate jsTypeTemplate) {
		Collection<JSG_Type> listType = new ArrayList<>();
		for (JSType t : jsTypeTemplate.getTypes()) {
			listType.add((JSG_Type) t.visit(this));
		}
		return new JSG_TypeTemplate((JSG_Type) jsTypeTemplate.getBaseType().visit(this), listType,
				jsTypeTemplate.getLine(), jsTypeTemplate.getColumn());
	}

	@Override
	public JSG_RuleArg accept(JSRuleArg jsRuleArg) {
		return new JSG_RuleArg(jsRuleArg.getArgName(), jsRuleArg.getArgValue().visit(this));
	}

	@Override
	public JSG_IsNotMarked accept(JSIsNotMarked jsIsNotMarked) {
		return new JSG_IsNotMarked(jsIsNotMarked.getLeft().visit(this), jsIsNotMarked.getMark().visit(this),
				jsIsNotMarked.getLine(), jsIsNotMarked.getColumn());
	}

	@Override
	public JSG_IsMarked accept(JSIsMarked jsIsMarked) {
		return new JSG_IsMarked(jsIsMarked.getLeft().visit(this), jsIsMarked.getMark().visit(this),
				jsIsMarked.getLine(), jsIsMarked.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSMark jsMark) {
		return new JSG_Mark(jsMark.getLeft().visit(this), jsMark.getMark().visit(this), gmapHasDirectAccess,
				jsMark.getLine(), jsMark.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSUnMark jsUnMark) {
		return new JSG_UnMark(jsUnMark.getLeft().visit(this), jsUnMark.getMark().visit(this), gmapHasDirectAccess,
				jsUnMark.getLine(), jsUnMark.getColumn());
	}

	@Override
	public JSG_Expression accept(JSGMapSize jsgMapSize) {
		return new JSG_GMapSize(gmapHasDirectAccess, jsgMapSize.getLine(), jsgMapSize.getColumn());
	}

	@Override
	public JSG_Expression accept(JSKeywordLeftFilter jsKeywordLeftFilter) {
		lastType = null;
		return new JSG_KeywordLeftFilter(jsKeywordLeftFilter.getLine(), jsKeywordLeftFilter.getColumn());
	}

	@Override
	public JSG_Expression accept(JSKeywordRightFilter jsKeywordRightFilter) {
		lastType = null;
		return new JSG_KeywordRightFilter(jsKeywordRightFilter.getLine(), jsKeywordRightFilter.getColumn());
	}

	@Override
	public JSG_Expression accept(JSPackagedType jsType) {
		return new JSG_PackagedType((JSG_Type) jsType.getLeft().visit(this), (JSG_Type) jsType.getRight().visit(this),
				jsType.getLine(), jsType.getColumn());
	}

	@Override
	public JSG_Expression accept(JSCast jsCast) {
		return new JSG_Cast((JSG_Type) jsCast.getType().visit(this), jsCast.getExpr().visit(this));
	}

	@Override
	public JSG_Instruction accept(JSContinue jsContinue) {
		return new JSG_Continue(jsContinue.getLine(), jsContinue.getColumn());
	}

	@Override
	public JSG_Expression accept(JSIndexInLeftPattern jsIndexInLeftPattern) {
		JSG_IndexInLeftPattern res =  new JSG_IndexInLeftPattern(
				jsIndexInLeftPattern.getHookIndex() != null ? jsIndexInLeftPattern.getHookIndex().visit(this) : null,
				jsIndexInLeftPattern.getIndexInDartList() != null
						? jsIndexInLeftPattern.getIndexInDartList().visit(this)
						: null,
				jsIndexInLeftPattern.getLine(), jsIndexInLeftPattern.getColumn());
		if(jsIndexInLeftPattern.getIndexInDartList()!=null) {
			lastType = new JSG_TypeJerboaDart(jsIndexInLeftPattern.getLine(), jsIndexInLeftPattern.getColumn());
		}else {
			lastType = new JSG_List(new JSG_TypeJerboaDart(jsIndexInLeftPattern.getLine(), jsIndexInLeftPattern.getColumn()),
					jsIndexInLeftPattern.getLine(), jsIndexInLeftPattern.getColumn());
		}
		return res;
	}

	@Override
	public JSG_Instruction accept(JSThrow jsThrow) {
		return new JSG_Throw(jsThrow.getExpr() != null ? jsThrow.getExpr().visit(this) : null, jsThrow.getLine(),
				jsThrow.getColumn());
	}

}
