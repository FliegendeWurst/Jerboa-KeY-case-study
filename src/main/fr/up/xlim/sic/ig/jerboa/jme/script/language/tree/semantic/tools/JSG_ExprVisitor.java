package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_AddInHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_AddInList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Alpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Choice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_ApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Boolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Call;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallListSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallRuleResHeight;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallRuleResWidth;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Cast;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Collect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Comment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Constructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Double;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_EbdParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Float;
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

public interface JSG_ExprVisitor<T, E extends Exception> {

	T accept(JSG_AddInHookList jsg_AddInHookList);

	T accept(JSG_AddInList jsAddInListSem);

	T accept(JSG_Alpha jsAlpha) throws E;

	T accept(JSG_Choice jsAlternativ);

	T accept(JSG_ApplyRule jsApplyRule) throws E;

	T accept(JSG_Boolean jsBoolean) throws E;

	T accept(JSG_Call jsCall) throws E;

	T accept(JSG_CallListSize jsg_CallListSize);

	T accept(JSG_CallRuleResHeight jsg_CallRuleResHeight);

	T accept(JSG_CallRuleResWidth jsg_CallRuleResWidth);

	T accept(JSG_Cast jsg_Cast);

	T accept(JSG_Collect jsCollect) throws E;

	T accept(JSG_CollectEbd jsCollect) throws E;

	T accept(JSG_Comment jsg_Comment);

	T accept(JSG_Constructor jsg_Constructor);

	T accept(JSG_Double jsDouble) throws E;

	T accept(JSG_EbdParam jsg_EbdParam);

	T accept(JSG_Float jsFloat) throws E;

	T accept(JSG_GetEbd jsg_GetEbd);

	T accept(JSG_GetEbdId jsg_GetEbdId);

	T accept(JSG_GetEbdName jsg_GetEbdName);

	T accept(JSG_GetEbdOrbit jsg_GetEbdOrbit);

	T accept(JSG_GetNodeId jsg_GetId);

	T accept(JSG_Index jsIndex) throws E;

	T accept(JSG_IndexInRuleResult jsIndexInRuleResultSem);

	T accept(JSG_IndexNodeInGmap jsIndexNodeInGmapSem);

	T accept(JSG_Indirection jsg_Indirection);

	T accept(JSG_InScope jsInScope) throws E;

	T accept(JSG_InScopeStatic jsg_InScopeStatic);

	T accept(JSG_Integer jsInteger) throws E;

	T accept(JSG_KeywordDimension jsKeywordDimension);

	T accept(JSG_KeywordEbd jsKeywordEbd);

	T accept(JSG_KeywordGmap jsGmapKeyword) throws E;

	T accept(JSG_KeywordModeler jsKeywordModeler);

	T accept(JSG_List jsList);

	T accept(JSG_Long jsLong) throws E;

	T accept(JSG_New jsg_New);

	T accept(JSG_Not jsNot) throws E;

	T accept(JSG_Null jsg_Null);

	T accept(JSG_Operator jsOperator) throws E;

	T accept(JSG_Orbit jsOrbit) throws E;

	T accept(JSG_Rule jsRule);

	T accept(JSG_RuleNode jsRuleNodeSem);

	T accept(JSG_Size jsSizeSem);

	T accept(JSG_String jsString) throws E;

	T accept(JSG_LeftRuleNode jsVariable);

	T accept(JSG_Type jsType);

	T accept(JSG_TypeBoolean jsg_TypeBoolean);

	T accept(JSG_TypeJerboaDart jsTypeJerboaNodeSem);

	T accept(JSG_TypeJerboaHookList jsType);

	T accept(JSG_PackagedType jsType);

	T accept(JSG_TypeJerboaRule jsTypeJerboaRuleSem);

	T accept(JSG_TypeJerboaRuleResult jsTypeJerboaRuleResultSem);

	T accept(JSG_TypeMark jsTypeMark);

	T accept(JSG_TypeOrbit jsTypeOrbit);

	T accept(JSG_TypePrimitive jsg_TypePrimitive);

	T accept(JSG_TypeTemplate jsTypeTemplate);

	T accept(JSG_Unreference jsg_Unreference);

	T accept(JSG_UserType jsUserTypeSem);

	T accept(JSG_Variable jsVariable) throws E;

	T accept(JSG_GetMarker jsg_GetFreeMarker);

	T accept(JSG_RuleArg jsg_RuleArg);

	T accept(JSG_KeywordHook jsg_KeywordHook);

	T accept(JSG_IsMarked jsg_IsMarked);

	T accept(JSG_IsNotMarked jsg_IsNotMarked);

	T accept(JSG_GMapSize jsg_GMapSize);

	T accept(JSG_KeywordLeftFilter jsg_KeywordLeftFilter);

	T accept(JSG_KeywordRightFilter jsg_KeywordRightFilter);

	T accept(JSG_IndexRuleNode jsg_IndexRuleNode);

	T accept(JSG_TypeString jsg_TypePrimitive);

	T accept(JSG_IndexInLeftPattern jsg_IndexInLeftPattern);

	T accept(JSG_GetTopoParam jsg_GetTopoParam);

}
