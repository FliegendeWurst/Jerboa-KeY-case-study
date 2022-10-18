package fr.up.xlim.sic.ig.jerboa.jme.export;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import up.jerboa.core.JerboaOrbit;

public class JerboaLanguageGlue implements LanguageGlue {
	JMEModeler modeler;
	JMEElement owner;

	LanguageType type;
	LanguageState state;

	public JerboaLanguageGlue(JMEModeler _modeler) {
		modeler = _modeler;
		type = LanguageType.MODELER;
		owner = modeler;
		state = LanguageState.CLASSICAL;
	}
	
	public JerboaLanguageGlue(JMEEmbeddingInfo _ebdinfo, JMERule rule) {
		modeler = _ebdinfo.getModeler();
		type = LanguageType.EDBDEFCODE;
		owner = rule;
		state = LanguageState.CLASSICAL;
	}
	
	

	public JerboaLanguageGlue(JMERule rule, LanguageState _state) {
		modeler = rule.getModeler();
		owner = rule;
		if (rule instanceof JMEScript)
			type = LanguageType.SCRIPT;
		else
			type = LanguageType.RULE;
		state = _state;
	}

	public JerboaLanguageGlue(JMENodeExpression ebd) {
		modeler = ebd.getNode().getRule().getModeler();
		owner = ebd;
		type = LanguageType.EMBEDDING;
		state = LanguageState.CLASSICAL;
	}

	@Override
	public Collection<String> getRuleEbdParam(String ruleName) {
		Collection<String> colEbdName = new ArrayList<String>();
		for (JMERule r : modeler.getRules()) {
			if (r.getName().compareTo(ruleName) == 0) {
				for (JMEParamEbd e : r.getParamsEbd()) {
					colEbdName.add(e.getName());
				}
				break;
			}
		}
		return colEbdName;
	}

	@Override
	public Collection<String> getEbdParams() {
		Collection<String> colEbdName = new ArrayList<String>();
		switch (type) {
		case RULE:
		case SCRIPT:
			for (JMEParamEbd e : ((JMERule) owner).getParamsEbd()) {
				colEbdName.add(e.getName());
			}
			break;
		case EMBEDDING:
			for (JMEParamEbd e : ((JMENodeExpression) owner).getNode().getRule().getParamsEbd()) {
				colEbdName.add(e.getName());
			}
			break;
		case EDBDEFCODE: {
			break;
		}
		case MODELER:
			// TODO: pas de paramï¿½tre pour le modeleur ?
			// for (JMEParamEbd e : ((JMEModeler) owner).getParamsEbd()) {
			// colEbdName.add(e.getName());
			// }
			// break;
		default:
		}
		return colEbdName;
	}

	@Override
	public String getEbdParamType(String ebdName) {
		switch (type) {
		case RULE:
		case SCRIPT:
			for (JMEParamEbd e : ((JMERule) owner).getParamsEbd()) {
				if (e.getName().compareTo(ebdName) == 0) {
					return e.getType();
				}
			}
			break;
		case EMBEDDING:
			for (JMEParamEbd e : ((JMENodeExpression) owner).getNode().getRule().getParamsEbd()) {
				if (e.getName().compareTo(ebdName) == 0) {
					return e.getType();
				}
			}
		case EDBDEFCODE:
		case MODELER:
		default:
		}
		return null;
	}

	@Override
	public String getRuleEbdParamType(String ruleName, String ebdName) {
		for (JMERule r : modeler.getRules()) {
			if (r.getName().compareTo(ruleName) == 0) {
				for (JMEParamEbd e : r.getParamsEbd()) {
					if (e.getName().compareTo(ebdName) == 0) {
						return e.getType();
					}
				}
				break;
			}
		}
		return null;
	}

	@Override
	public Collection<String> getRuleTopoParam(String ruleName) {
		Collection<String> colTopoName = new ArrayList<String>();
		for (JMERule r : modeler.getRules()) {
			if (r.getName().compareTo(ruleName) == 0) {
				for (JMEParamTopo e : r.getParamsTopo()) {
					colTopoName.add(e.getNode().getName());
				}
				break;
			}
		}
		return colTopoName;
	}

	@Override
	public Collection<String> getRuleList() {
		Collection<String> colRuleName = new ArrayList<String>();
		for (JMERule r : modeler.getRules()) {
			colRuleName.add(r.getName());
		}
		return colRuleName;
	}

	@Override
	public Collection<String> getAtomicRuleList() {
		Collection<String> colRuleName = new ArrayList<String>();
		for (JMERule r : modeler.getRules()) {
			if (r instanceof JMERuleAtomic)
				colRuleName.add(r.getName());
		}
		return colRuleName;
	}

	@Override
	public Collection<String> getScriptRuleList() {
		Collection<String> colRuleName = new ArrayList<String>();
		for (JMERule r : modeler.getRules()) {
			if (r instanceof JMEScript)
				colRuleName.add(r.getName());
		}
		return colRuleName;
	}

	@Override
	public Collection<String> getEmbeddingList() {
		Collection<String> ebdList = new ArrayList<>();
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			ebdList.add(e.getName());
		}
		return ebdList;
	}

	@Override
	public boolean ebdExist(String ebdName) {
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if (e.getName().compareTo(ebdName) == 0)
				return true;
		}
		return false;
	}

	@Override
	public boolean hookExist(String hookName, String ruleName) {
		for (JMERule ri : modeler.getRules()) {
			if (ri.getName().compareTo(ruleName) == 0) {
				for (JMENode h : ri.getHooks()) {
					if (h.getName().compareTo(hookName) == 0)
						return true;
				}
				break;
			}
		}
		return false;
	}

	@Override
	public boolean ruleExist(String ruleName) {
		for (JMERule ri : modeler.getRules()) {
			if (ri.getName().compareTo(ruleName) == 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getEmbeddingType(String ebdName) {
		JMEEmbeddingInfo ebd = modeler.getEmbedding(ebdName);
		if (ebd != null) {
			return ebd.getType();
		}
		return null;
	}

	@Override
	public LanguageType getLangageType() {
		return type;
	}

	@Override
	public String getOwnerName() {
		switch (type) {
		case RULE:
		case SCRIPT:
			return ((JMERule) owner).getName();
		case EMBEDDING:
			return ((JMENodeExpression) owner).getNode().getName();
		case EDBDEFCODE:
			return ((JMEEmbeddingInfo)owner).getName();
		case MODELER:
			return ((JMEModeler) owner).getName();
		default:
			return null;
		}
	}

	@Override
	public JMEModeler getModeler() {
		switch (type) {
		case RULE:
		case SCRIPT:
			return ((JMERule) owner).getModeler();
		case EMBEDDING:
			return ((JMENodeExpression) owner).getNode().getRule().getModeler();
		case MODELER:
			return ((JMEModeler) owner);
		case EDBDEFCODE:
			if(owner!=null)
				return ((JMEEmbeddingInfo)owner).getModeler();
		default:
			return null;
		}
	}

	public JMEElement getOwner() {
		return owner;
	}

	@Override
	public Collection<String> getRuleLeftNodesParam(String ruleName) {
		Collection<String> colEbdName = new ArrayList<String>();
		for (JMERule r : modeler.getRules()) {
			if (r.getName().compareTo(ruleName) == 0) {
				for (JMENode e : r.getLeft().getNodes()) {
					colEbdName.add(e.getName());
				}
				break;
			}
		}
		return colEbdName;
	}

	@Override
	public LanguageState getLangagesState() {
		return state;
	}

	@Override
	public String getModelerName() {
		return modeler.getName();
	}

	@Override
	public JerboaOrbit getEbdOrbit(String ebdName) {
		for (JMEEmbeddingInfo ei : modeler.getEmbeddings())
			if (ei.getName().compareTo(ebdName) == 0)
				return ei.getOrbit();
		return null;
	}

	@Override
	public String getRuleName() {
		switch (type) {
		case RULE:
		case SCRIPT:
			return ((JMERule) owner).getName();
		case EMBEDDING:
			return ((JMENodeExpression) owner).getNode().getRule().getName();
		case MODELER:
			return null;
		case EDBDEFCODE:
			return null;
		default:
			return null;
		}
	}

	@Override
	public JerboaOrbit getLeftNodeOrbit(String ruleName, String nodeName) {
		for (JMERule r : modeler.getRules()) {
			if (r.getName().compareTo(ruleName) == 0) {
				if (r.getLeft().getMatchNode(nodeName) != null)
					return r.getLeft().getMatchNode(nodeName).getOrbit();
			}
		}
		return null;
	}

	@Override
	public JerboaOrbit getRightNodeOrbit(String ruleName, String nodeName) {
		for (JMERule r : modeler.getRules()) {
			if (r.getName().compareTo(ruleName) == 0) {
				return r.getRight().getMatchNode(nodeName).getOrbit();
			}
		}
		return null;
	}

	@Override
	public String getEbdName() {
		switch (type) {
		case EMBEDDING:
			return ((JMENodeExpression) owner).getEbdInfo().getName();
		default:
			return null;
		}
	}

	@Override
	public int getModelerDimension() {
		return modeler.getDimension();
	}

	@Override
	public JMERule getCurrentRule() {
		if(owner == null) return null;
		switch (type) {
		case RULE:
			return ((JMERule) owner);
		case SCRIPT:
			return ((JMERule) owner);
		case EMBEDDING:
			return ((JMENodeExpression) owner).getNode().getRule();
		case EDBDEFCODE:
			return ((JMERule) owner);
		default:
			return null;
		}
	}
}
