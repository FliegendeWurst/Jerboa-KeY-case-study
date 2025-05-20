package fr.up.xlim.sic.ig.jerboa.jme.model;


public class JMERuleAtomic extends JMERule {
	public JMERuleAtomic(JMEModeler modeler, String name) {
		super(modeler, name);
	}
	
	/**
	 * Change value of updateExprs for both lhs and rhs.
	 * @param updateExprs
	 */
	public void setUpdateExprs(boolean updateExprs) {
		getLeft().setUpdateExprs(updateExprs);
		getRight().setUpdateExprs(updateExprs);
	}

}
