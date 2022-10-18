package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_RuleNodeId extends JSG_2_Entity {
	public enum GraphSide{LEFT, RIGHT};
	
	private JMERule rule;
	private String ruleNodeName;
	private GraphSide graphSide;

	private int line, column;

	public JSG_2_RuleNodeId(JMERule _rule, String _ruleNodeName, GraphSide graphSide, int l, int c) {
		super(l,c);
		this.rule = _rule;
		this.ruleNodeName = _ruleNodeName;
		this.graphSide = graphSide;
	}

	public JMERule getRule() {
		return rule;
	}

	public String getNodeName() {
		return ruleNodeName;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
	

	public GraphSide getGraphside() {
		return graphSide;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
