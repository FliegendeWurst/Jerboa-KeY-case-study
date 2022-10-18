package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSEntity;

public class JSG_IndexRuleNode extends JSEntity implements JSG_Expression {
	public enum SIDE {
		LEFT, RIGHT
	};

	private SIDE side;
	private JMERule rule;
	private String nodeName;

	public JSG_IndexRuleNode(SIDE side, JMERule rule, String nodeName, int l, int c) {
		super(l, c);
		this.side = side;
		this.rule = rule;
		this.nodeName = nodeName;
	}

	@Override
	public <T, E extends Exception> T visit(JSG_ExprVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public SIDE getSide() {
		return side;
	}

	public JMERule getRule() {
		return rule;
	}

	public String getNodeName() {
		return nodeName;
	}

}
