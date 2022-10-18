package fr.up.xlim.sic.ig.jerboa.jme.verif;

import java.util.Collection;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;

public interface JMEVerifIterator {
	public Collection<JMEError> check(JMEElementWindowable element);
}
