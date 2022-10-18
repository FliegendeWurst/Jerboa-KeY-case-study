package fr.up.xlim.sic.ig.jerboa.jme.view;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;

public interface JMEElementView {
	public void reload();

	public void unlink();
	public JMEElement getSourceElement();
}
