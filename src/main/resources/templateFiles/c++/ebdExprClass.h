class $$RULE_NAMEExprR$$RULE_NODE_NAME$$RULE_EXPR_EBD_NAME: public JerboaRuleExpression {
private:
	const JerboaModeler* _owner;
	 $$RULE_NAME *parentRule;
public:
    $$RULE_NAMEExprR$$RULE_NODE_NAME$$RULE_EXPR_EBD_NAME($$RULE_NAME* o):_owner(o->modeler()),parentRule(o){}
    ~$$RULE_NAMEExprR$$RULE_NODE_NAME$$RULE_EXPR_EBD_NAME(){parentRule = nullptr;_owner = nullptr;}
    JerboaEmbedding* compute(const JerboaGMap* gmap,const JerboaRuleOperation *rule, 
		JerboaFilterRowMatrix *leftfilter,const JerboaRuleNode *rulenode)const;

    std::string name() const;
    int embeddingIndex() const;
};// end Class