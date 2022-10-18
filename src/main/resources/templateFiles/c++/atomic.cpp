#include "$$RULE_PATH.h"

// Embeddings includes
$$EBD_INCLUDE
//////////////////////

namespace $$MODELER_NAMESPACE {

$$RULE_NAME::$$RULE_NAME(const JerboaModeler *modeler)
    : JerboaRuleGenerated(modeler,"$$RULE_NAME")
     {

$$RULE_CONSTRUCTOR_CONTENT

    computeEfficientTopoStructure();
    computeSpreadOperation();
    chooseBestEngine();
}

/** BEGIN EBD CLASSES **/

$$RULE_EBD_EXPR_CLASSES_CPP

/** END EBD CLASSES **/

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

int $$RULE_NAME::reverseAssoc(int i)const {
    $$RULE_REV_ASSOC
    return -1;
}

int $$RULE_NAME::attachedNode(int i)const {
    $$RULE_ATTACHED_NODE
    return -1;
}

// Embeddings parameter getter/setter
$$RULE_PARAMS_GETTER_SETTER


}	// namespace $$MODELER_MODULE
