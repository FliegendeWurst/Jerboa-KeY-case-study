package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools;

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

public interface JSG_2_EntityVisitor<T, E extends Exception> {

	T accept(JSG_2_AddInHookList jsg_2_AddInHookList);

	T accept(JSG_2_AddInList jsg_2_AddInList);

	T accept(JSG_2_Alpha jsg_2_Alpha);

	T accept(JSG_2_ApplyRule jsg_2_ApplyRule);

	T accept(JSG_2_Assignment jsg_2_Assignment);

	T accept(JSG_2_AssocParam jsg_2_AssocParam);

	T accept(JSG_2_AtLang jsg_2_AtLang);

	T accept(JSG_2_Block jsg_2_Block);

	T accept(JSG_2_Boolean jsg_2_Boolean);

	T accept(JSG_2_Break jsg_2_Break);

	T accept(JSG_2_Call jsg_2_Call);

	T accept(JSG_2_CallListSize jsg_2_CallListSize);

	T accept(JSG_2_CallRuleResHeight jsg_2_CallRuleResHeight);

	T accept(JSG_2_CallRuleResWidth jsg_2_CallRuleResWidth);

	T accept(JSG_2_Cast jsg_2_Cast);

	T accept(JSG_2_Catch jsg_2_Catch);

	T accept(JSG_2_Choice jsg_2_Choice);

	T accept(JSG_2_Collect jsg_2_Collect);

	T accept(JSG_2_CollectEbd jsg_2_CollectEbd);

	T accept(JSG_2_Comment jsg_2_Comment);

	T accept(JSG_2_Constructor jsg_2_Constructor);

	T accept(JSG_2_Continue jsg_2_Continue);

	T accept(JSG_2_Declare jsg_2_Declare);

	T accept(JSG_2_DeclareFunction jsg_2_DeclareFunction);

	T accept(JSG_2_DeclareMark jsg_2_DeclareMark);

	T accept(JSG_2_Delete jsg_2_Delete);

	T accept(JSG_2_Double jsg_2_Double);

	T accept(JSG_2_DoWhile jsg_2_DoWhile);

	T accept(JSG_2_EbdParam jsg_2_Variable);

	T accept(JSG_2_Float jsg_2_Float);

	T accept(JSG_2_For jsg_2_For);

	T accept(JSG_2_ForEach jsg_2_ForEach);

	T accept(JSG_2_ForLoop jsg_2_ForLoop);

	T accept(JSG_2_FreeMarker jsg_2_FreeMarker);

	T accept(JSG_2_GetDartId jsg_2_GetNodeId);

	T accept(JSG_2_GetEbd jsg_2_GetEbd);

	T accept(JSG_2_GetEbdId jsg_2_GetEbdId);

	T accept(JSG_2_GetEbdName jsg_2_GetEbdName);

	T accept(JSG_2_GetEbdOrbit jsg_2_GetEbdOrbit);

	T accept(JSG_2_GetMarker jsg_2_GetMarker);

	T accept(JSG_2_GMapSize jsg_2_GMapSize);

	T accept(JSG_2_Header jsg_2_Header);

	T accept(JSG_2_If jsg_2_If);

	T accept(JSG_2_Index jsg_2_Index);

	T accept(JSG_2_IndexInJerboaType jsg_2_IndexInJerboaType);

	T accept(JSG_2_Indirection jsg_2_Indirection);

	T accept(JSG_2_InScope jsg_2_InScope);

	T accept(JSG_2_Integer jsg_2_Integer);

	T accept(JSG_2_IsMarked jsg_2_IsMarked);

	T accept(JSG_2_IsNotMarked jsg_2_IsNotMarked);

	T accept(JSG_2_JerboaKeyword jsg_2_JerboaKeyword);

	T accept(@SuppressWarnings("rawtypes") JSG_2_Literal jsg_2_Literal);

	T accept(JSG_2_Long jsg_2_Long);

	T accept(JSG_2_Map jsg_2_Map);

	T accept(JSG_2_Mark jsg_2_Mark);

	T accept(JSG_2_New jsg_2_New);

	T accept(JSG_2_NOP jsg_2_NOP);

	T accept(JSG_2_Not jsg_2_Not);

	T accept(JSG_2_Null jsg_2_Null);

	T accept(JSG_2_Operator jsg_2_Operator);

	T accept(JSG_2_Orbit jsg_2_Orbit);

	T accept(JSG_2_PackagedType jsg_2_PackagedType);

	T accept(JSG_2_Print jsg_2_Print);
	
	T accept(JSG_2_Return jsg_2_Return);

	T accept(JSG_2_Rule jsg_2_Rule);

	T accept(JSG_2_RuleArg jsg_2_RuleArg);

	T accept(JSG_2_RuleNodeId jsg_2_RuleNode);

	T accept(JSG_2_Sequence jsg_2_Sequence);

	T accept(JSG_2_String jsg_2_String);
	
	T accept(JSG_2_Throw jsg_2_Throw);

	T accept(JSG_2_Try jsg_2_Try);

	T accept(JSG_2_Type jsg_2_Type);

	T accept(JSG_2_TypeBoolean jsg_2_TypeBoolean);

	T accept(JSG_2_TypeDart jsg_2_Type);

	T accept(JSG_2_TypeEmbedding jsg_2_Type);

	T accept(JSG_2_TypeGmap jsg_2_Type);

	T accept(JSG_2_TypeHookList jsg_2_TypeHookList);

	T accept(JSG_2_TypeList jsg_2_Type);

	T accept(JSG_2_TypeMark jsg_2_Type);

	T accept(JSG_2_TypeModeler jsg_2_Type);

	T accept(JSG_2_TypeOrbit jsg_2_Type);

	T accept(JSG_2_TypePointer jsg_2_TypePointer);

	T accept(JSG_2_TypeRule jsg_2_Type);

	T accept(JSG_2_TypeRuleResult jsg_2_Type);

	T accept(JSG_2_TypeString jsg_2_TypeString);

	T accept(JSG_2_TypeTemplate jsg_2_Type);

	T accept(JSG_2_UnMark jsg_2_UnMark);

	T accept(JSG_2_Unreference jsg_2_Unreference);

	T accept(JSG_2_Variable jsg_2_Variable);

	T accept(JSG_2_While jsg_2_While);



}
