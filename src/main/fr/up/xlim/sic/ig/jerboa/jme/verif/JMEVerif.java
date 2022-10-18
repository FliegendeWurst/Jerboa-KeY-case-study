package fr.up.xlim.sic.ig.jerboa.jme.verif;

import java.util.ArrayList;
import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;

/**
 * Classe qui gère l'ensemble des vérifs et qui les lancent sur un objet
 * particulier.
 * 
 * @author Hakim Romain et Valentin
 *
 */
public class JMEVerif {

	private ArrayList<JMEVerifIterator> listVerifs;

	public JMEVerif() {
		super();
		this.listVerifs = new ArrayList<>();
	}

	public void addVerif(JMEVerifIterator verif) {
		this.listVerifs.add(verif);
	}

	public Collection<JMEError> run(JMEElementWindowable element) {
		ArrayList<JMEError> listErrors = new ArrayList<>();
		
		for (JMEVerifIterator verif : listVerifs) {
			try {
				Collection<JMEError> errs = verif.check(element);
				listErrors.addAll(errs);
			}
			catch(Throwable t) {
			}
		}
		// listErrors.add(new JMEError(JMEErrorSeverity.INFO,
		// JMEErrorType.TOPOLOGIC, element, element, "Test"));
		return listErrors;
	}
}
