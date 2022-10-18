package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.Translator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEVerifIterator;

/**
 * @author Valentin
 *
 */
public class JMEVerifRuleEbdExpression implements JMEVerifIterator {

	public JMEVerifRuleEbdExpression() {

	}

	@Override
	public Collection<JMEError> check(JMEElementWindowable element) {
		Collection<JMEError> errList = new ArrayList<>();
		if (element instanceof JMERule) {
			JMERule r = ((JMERule) element);
			for (JMENode n : r.getRight().getNodes()) {
				for (JMENodeExpression e : n.getExplicitExprs()) {
					JerboaLanguageGlue glue = new JerboaLanguageGlue(e);
					ArrayList<JSError> el = Translator.verif(e.getExpression(), glue, ((JMERule) element).getModeler(), true);
					for (JSError eri : el) {
						errList.add(new JMEError(eri, glue, e));
					}
				}
			}
		}
		return errList;
	}

}
