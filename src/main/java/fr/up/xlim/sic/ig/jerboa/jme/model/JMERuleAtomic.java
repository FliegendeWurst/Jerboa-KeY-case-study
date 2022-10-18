package fr.up.xlim.sic.ig.jerboa.jme.model;

import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;

public class JMERuleAtomic extends JMERule implements Cloneable {
	public JMERuleAtomic(JMEModeler modeler, String name) {
		super(modeler, name);
		// attention a l'ordre
		// left = new JMEGraph(this, true);
		// right = new JMEGraph(this, false);
	}
	
	/**
	 * Change value of updateExprs for both lhs and rhs.
	 * @param updateExprs
	 */
	public void setUpdateExprs(boolean updateExprs) {
		getLeft().setUpdateExprs(updateExprs);
		getRight().setUpdateExprs(updateExprs);
	}

	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitRuleAtomic(this);
	}

	@Override
	public JMERule copy(JMEModeler modeler, String rulename) {
		JMERuleAtomic at = new JMERuleAtomic(modeler, rulename);
		at.category = category;
		at.comment = comment;
		at.gridsize = gridsize;
		at.magnetic = magnetic;
		at.header = header;
		at.preprocess = preprocess;
		at.midprocess = midprocess;
		at.postprocess = postprocess;
		at.precondition = precondition;
		
		left.copy(at.getLeft());
		right.copy(at.getRight());
		
		for (JMEParamEbd param : paramsebd) {
			JMEParamEbd newparam = param.copy(at);
			at.addParamEbd(newparam);
		}
		
		for(JMEParamTopo topo : paramstopo) {
			JMENode node = at.getLeft().searchNodeByName(topo.getName());
			at.addParamTopo(node.paramTopo);
		}
		
		return at;
	}

}
