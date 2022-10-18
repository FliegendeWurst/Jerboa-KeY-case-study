package fr.up.xlim.sic.ig.jerboa.jme.model.util;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMELoop;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;

public interface JMEVisitor<T> {
	T visitArc(JMEArc arc);

	T visitNode(JMENode node);

	T visitGraph(JMEGraph graph);

	T visitExpression(JMENodeExpression expr);

	T visitRuleAtomic(JMERuleAtomic at);

	T visitScript(JMEScript at);

	T visitModeler(JMEModeler modeler);

	T visitEmbeddingInfo(JMEEmbeddingInfo info);

	T visitLoopArc(JMELoop loop);

	T visitParamEbd(JMEParamEbd jmeParamEbd);

	T visitParamTopo(JMEParamTopo jmeParamTopo);
}
