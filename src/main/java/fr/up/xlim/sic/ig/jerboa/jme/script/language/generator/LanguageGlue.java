package fr.up.xlim.sic.ig.jerboa.jme.script.language.generator;

import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import up.jerboa.core.JerboaOrbit;

public interface LanguageGlue {
	public enum LanguageType {
		// TODO: j'aurais renommer EMBEDDING en EBDCOMPUTE
		RULE, SCRIPT, MODELER, EMBEDDING, EDBDEFCODE;
	}

	public enum LanguageState {
		HEADER, PRECONDITION, CLASSICAL, PREPROCESS, POSTPROCESS, MIDPROCESS;
	}

	public Collection<String> getRuleEbdParam(String ruleName);

	public String getRuleEbdParamType(String ruleName, String ebdName);

	public Collection<String> getRuleLeftNodesParam(String ruleName);

	public Collection<String> getRuleTopoParam(String ruleName);

	public Collection<String> getRuleList();

	public Collection<String> getAtomicRuleList();

	public Collection<String> getScriptRuleList();

	public Collection<String> getEmbeddingList();

	public JerboaOrbit getEbdOrbit(String ebdName);

	public boolean ebdExist(String ebdName);

	public boolean hookExist(String hookName, String ruleName);

	public boolean ruleExist(String ruleName);

	public JMERule getCurrentRule();

	public String getEmbeddingType(String ebdName);

	public String getEbdParamType(String ebdName);

	public Collection<String> getEbdParams();

	public LanguageType getLangageType();

	public LanguageState getLangagesState();

	public JerboaOrbit getLeftNodeOrbit(String ruleName, String nodeName);

	public JerboaOrbit getRightNodeOrbit(String ruleName, String nodeName);

	/**
	 * Return the name of the current node, rule, or modeler depending on
	 * {@link LanguageType} value
	 *
	 * @return
	 */
	public String getOwnerName();

	public String getRuleName();

	public String getModelerName();

	public JMEModeler getModeler();

	public int getModelerDimension();

	/**
	 * Gives the name of the embedding that is being define by the expression.
	 * It return null for all type different to LanguageType.EMBEDDING
	 *
	 * @return
	 */
	public String getEbdName();


}
