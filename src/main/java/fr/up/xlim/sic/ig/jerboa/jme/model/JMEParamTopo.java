package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class JMEParamTopo implements JMEElement {

	// intrinsic parameters
	private boolean modified;
	private JMERule rule;
	private JMENode node;

	public JMEParamTopo(JMERule rule, JMENode node) {
		this.rule = rule;
		this.node = node;
		this.modified = false;

	}

	public int getOrder() {
		return rule.getParamsTopo().indexOf(this);
	}

	public JMENode getNode() {
		return node;
	}

	public JMERule getRule() {
		return rule;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getOrder()).append("-").append(node);

		return sb.toString();
	}
	
	@Override
	public String getName() {
		return node.getName();
	}
}
