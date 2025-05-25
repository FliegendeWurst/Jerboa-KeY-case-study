package fr.up.xlim.sic.ig.jerboa.jme.verif;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;

public final class JMERuleError {
	private JMERuleErrorType type;
	private JMERuleErrorSeverity severity;
	private JMEElement target;
	private JMEElement locality;

	/*@ public normal_behavior
	  @ ensures this != null;
	  @ strictly_pure
	  @*/
	public JMERuleError(JMERuleErrorSeverity severity, JMERuleErrorType type, JMEElement locality, JMEElement target) {
		this.target = target;
		this.type = type;
		this.locality = locality;
		this.severity = severity;
	}

	public boolean equals(Object obj) {
		if (obj instanceof JMERuleError) {
			JMERuleError err = (JMERuleError) obj;
			boolean b = true;
			b = type == err.type && target == err.target;
			return b;
		}
		return super.equals(obj);
	}
}
