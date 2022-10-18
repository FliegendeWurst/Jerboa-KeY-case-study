JerboaEmbedding* $$RULE_NAME::$$RULE_NAMEExprR$$RULE_NODE_NAME$$RULE_EXPR_EBD_NAME::compute(
		const JerboaGMap* gmap,
		const JerboaRuleOperation *rule, 
		JerboaFilterRowMatrix *leftfilter,
		const JerboaRuleNode *rulenode)const{
    parentRule->curLeftFilter = leftfilter;

// ======== BEGIN CODE TRANSLATION FOR EXPRESSION COMPUTATION 

    $$EBD_EXPR_CODE
	
// ======== END CODE TRANSLATION FOR EXPRESSION COMPUTATION 

    
}
std::string $$RULE_NAME::$$RULE_NAMEExprR$$RULE_NODE_NAME$$RULE_EXPR_EBD_NAME::name() const{
    return "$$RULE_NAMEExprR$$RULE_NODE_NAME$$RULE_EXPR_EBD_NAME";
}

int $$RULE_NAME::$$RULE_NAMEExprR$$RULE_NODE_NAME$$RULE_EXPR_EBD_NAME::embeddingIndex() const{
    return $$RULE_EXPR_EBD_ID;
}