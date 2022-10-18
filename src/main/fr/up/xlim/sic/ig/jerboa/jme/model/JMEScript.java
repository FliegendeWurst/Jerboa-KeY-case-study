package fr.up.xlim.sic.ig.jerboa.jme.model;

import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;

public class JMEScript extends JMERule {
	protected String scriptContent;

	public JMEScript(JMEModeler modeler, String name) {
		super(modeler, name);
		scriptContent = "";
	}

	public String getContent() {
		return scriptContent;
	}

	public void setContent(String content) {
		scriptContent = content;
	}

	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		visitor.visitScript(this);
		return null;
	}

	@Override
	public JMERule copy(JMEModeler modeler, String rulename) {
		JMEScript script = new JMEScript(modeler, rulename);
		script.category = category;
		script.comment = comment;
		script.gridsize = gridsize;
		script.magnetic = magnetic;
		script.header = header;
		script.preprocess = preprocess;
		script.midprocess = midprocess;
		script.postprocess = postprocess;
		script.precondition = precondition;
		script.scriptContent = scriptContent;
		script.shape = shape;
		
		left.copy(script.left);
		right.copy(script.right);
		
		
		for (JMEParamEbd param : paramsebd) {
			JMEParamEbd newparam = param.copy(script);
			script.addParamEbd(newparam);
		}
		
		for(JMEParamTopo topo : paramstopo) {
			JMENode node = script.getLeft().searchNodeByName(topo.getName());
			script.addParamTopo(node.paramTopo);
		}
		
		return script;
	}

}
