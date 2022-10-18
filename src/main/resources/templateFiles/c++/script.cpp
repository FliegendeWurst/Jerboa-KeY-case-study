#include "$$RULE_PATH.h"

// Embeddings includes
$$EBD_INCLUDE
//////////////////////

namespace $$MODELER_NAMESPACE {

$$RULE_NAME::$$RULE_NAME(const ModelerPerf *modeler)
    : JerboaRuleScript(modeler,"$$RULE_NAME")
     {
$$RULE_CONSTRUCTOR_CONTENT
}


JerboaRuleResult* $$RULE_NAME::apply(JerboaGMap* gmap, const JerboaInputHooks& sels,JerboaRuleResultType kind){
    $$SCRIPT_CONTENT
}

JerboaRuleResult* $$RULE_NAME::applyRuleParam(JerboaGMap* gmap, JerboaRuleResultType _kind $$RULE_PARAM_ARGS){
	JerboaInputHooksGeneric  _hookList;
	$$RULE_PARAM_APPLY_CONTENT
	return applyRule(gmap, _hookList, _kind);
}


bool $$RULE_NAME::hasPrecondition()const{
    return $$RULE_HAS_PRECONDITION;
}

$$RULE_PRECONDITION_CPP


// PROCESSES

$$RULE_PRE_PROCESS_CPP

$$RULE_MID_PROCESS_CPP

$$RULE_POST_PROCESS_CPP

////////////


std::string $$RULE_NAME::getComment() const{
    return $$RULE_COMMENT;
}

std::vector<std::string> $$RULE_NAME::getCategory() const{
    std::vector<std::string> listFolders;
$$RULE_PARAM_CATEGORY_LISTER
    return listFolders;
}


// Embeddings parameter getter/setter
$$RULE_PARAMS_GETTER_SETTER


}	// namespace $$MODELER_MODULE
