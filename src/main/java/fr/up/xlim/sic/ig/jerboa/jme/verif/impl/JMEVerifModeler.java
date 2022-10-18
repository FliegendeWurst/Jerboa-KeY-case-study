package fr.up.xlim.sic.ig.jerboa.jme.verif.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorSeverity;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorType;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEVerifIterator;

// Verifications sur le modeleur
public class JMEVerifModeler implements JMEVerifIterator {		

	@Override
	public Collection<JMEError> check(JMEElementWindowable element) {
		ArrayList<JMEError> errors = new ArrayList<>();
		if(element instanceof JMEModeler) {
			verifDuplicatedNameEbd((JMEModeler) element, errors);
			
			verifDuplicatedNameRules((JMEModeler) element, errors);
		}
		return errors;

	}

	private void verifDuplicatedNameRules(JMEModeler element, ArrayList<JMEError> errors) {
		List<JMERule> rules = element.getRules();
		for (JMERule ruleA : rules) {
			for (JMERule ruleB : rules) {
				if(ruleA != ruleB) {
					if(ruleA.getFullName().equalsIgnoreCase(ruleB.getFullName())) {
						errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.OTHER, ruleB, ruleB, "Duplicate rule fullname '"+ruleB.getFullName()+"'"));
					}
					else if(ruleA.getName().equalsIgnoreCase(ruleB.getName())) {
						errors.add(new JMEError(JMEErrorSeverity.WARNING, JMEErrorType.OTHER, ruleB, ruleB, "Duplicate rule name '"+ruleB.getFullName()+"' (depend of the generation code)"));
					}
				}
				
			}
		}
		
	}

	private void verifDuplicatedNameEbd(JMEModeler element, ArrayList<JMEError> errors) {
		List<JMEEmbeddingInfo> ebds = element.getEmbeddings();
		for (JMEEmbeddingInfo ebda : ebds) {
			for (JMEEmbeddingInfo ebdb : ebds) {
				if(ebda != ebdb && ebda.getName().equalsIgnoreCase(ebdb.getName())) {
					errors.add(new JMEError(JMEErrorSeverity.CRITIQUE, JMEErrorType.OTHER, ebdb, ebdb, "Duplicate embedding name '"+ebda.getName()+"'"));
				}
			}
		}
	}	
}
