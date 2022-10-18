package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;

public class JSG_RuleNode implements JSG_Expression {

	private JSG_Rule rule;
	private JSG_Expression ruleNode;

	private int line, column;

	public JSG_RuleNode(int l, int c, JSG_Rule _rule, JSG_Expression _ruleNode) {
		this.line = l;
		this.column = c;
		this.rule = _rule;
		this.ruleNode = _ruleNode;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public JSG_Rule getRule() {
		return rule;
	}

	public JSG_Expression getNodeName() {
		return ruleNode;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

}
