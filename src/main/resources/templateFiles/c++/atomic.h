/**
$$RULE_HEADER
**/
#ifndef JERBOA_RULE_ATOMIC_$$RULE_NAME
#define JERBOA_RULE_ATOMIC_$$RULE_NAME

#include <cstdlib>
#include <string>

#include <core/jerboamodeler.h>
#include <coreutils/jerboagmaparray.h>
#include <core/jerboagmap.h>
#include <core/jerboaRuleOperation.h>
#include <serialization/jbaformat.h>

#include "$$MODELER_PATH.h"


#include <coreutils/jerboaRuleGenerated.h>
#include <core/jerboaRuleExpression.h>

/** BEGIN RAWS IMPORTS **/
$$RULE_IMPORT
/** END RAWS IMPORTS **/


namespace $$MODELER_NAMESPACE {

using namespace jerboa;

class $$RULE_NAME : public JerboaRuleGenerated{

protected:
	JerboaFilterRowMatrix *curLeftFilter;

	/** BEGIN PARAMETERS **/
$$RULE_PARAMETERS
	/** END PARAMETERS **/


public : 
    $$RULE_NAME(const $$MODELER_NAME *modeler);

    ~$$RULE_NAME(){
          //TODO: auto-generated Code, replace to have correct function
	}
    
$$RULE_EBD_EXPR_CLASSES_H

    /**
     * Facility for accessing to the dart
     */
$$RULE_DART_ACCESS_H


    /** BEGIN SPECIFIC APPLYRULE FUNCTIONS **/

	JerboaRuleResult* applyRuleParam(JerboaGMap* gmap, JerboaRuleResultType _kind$$RULE_PARAM_ARGS);

    /** END SPECIFIC APPLYRULE FUNCTIONS **/

    bool hasPrecondition()const;

$$RULE_PRECONDITION_H
    // PROCESSES
$$RULE_PRE_PROCESS_H
$$RULE_MID_PROCESS_H
$$RULE_POST_PROCESS_H
    ////////////


	std::string getComment()const;
	std::vector<std::string> getCategory()const;
	int reverseAssoc(int i)const;
	int attachedNode(int i)const;


    // Embeddings parameter getter/setter
    $$RULE_PARAMS_GETTER_SETTER

};// end rule class 

}	// namespace $$MODELER_MODULE
#endif