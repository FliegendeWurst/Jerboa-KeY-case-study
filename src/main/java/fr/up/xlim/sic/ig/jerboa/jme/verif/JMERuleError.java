package fr.up.xlim.sic.ig.jerboa.jme.verif;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;

public class JMERuleError {
	private JMERuleErrorType type;
	private JMERuleErrorSeverity severity;
	private JMEElement target;
	private JMEElement locality;

	public JMERuleError(JMERuleErrorSeverity severity, JMERuleErrorType type, JMEElement locality, JMEElement target) {
		this.target = target;
		this.type = type;
		this.locality = locality;
		this.severity = severity;
	}

	public JMEElement getLocality() {
		return locality;
	}

	public JMERuleErrorSeverity getSeverity() {
		return severity;
	}

	public JMERuleErrorType getType() {
		return type;
	}

	public JMEElement getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JMERuleError) {
			JMERuleError err = (JMERuleError) obj;
			boolean b = true;
			b = type == err.type && target == err.target;
			return b;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(locality).append(" : ").append(type).append("<").append(target.getName()).append(">");
		return sb.toString();
	}
}
