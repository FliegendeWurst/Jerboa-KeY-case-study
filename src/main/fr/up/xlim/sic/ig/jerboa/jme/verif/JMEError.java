package fr.up.xlim.sic.ig.jerboa.jme.verif;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSError;

public class JMEError {
	// un code d'err avec une classe special pour les messages auraient ete plus
	// judicieux ici
	// pour economiser de la memoire
	private String message;
	private JMEErrorType type;
	private JMEErrorSeverity severity;
	private JMEElement target;

	private JMEElementWindowable locality;

	public JMEError(JMEErrorSeverity severity, JMEErrorType type, JMEElementWindowable locality, JMEElement target,
			String message) {
		this.target = target;
		this.message = message;
		this.type = type;
		this.locality = locality;
		this.severity = severity;
	}

	public JMEError(JSError error, JerboaLanguageGlue glue, JMEElement target) {
		this.target = target;
		switch (glue.getLangageType()) {
		case EMBEDDING:
			type = JMEErrorType.EMBEDDING;
			break;
		case MODELER:
			type = JMEErrorType.OTHER;
			break;
		case RULE:
			type = JMEErrorType.OTHER;
			break;
		case SCRIPT:
			type = JMEErrorType.SCRIPT;
			break;
		default:
			type = JMEErrorType.OTHER;
		}
		switch (error.getCriticality()) {
		case INFO:
			severity = JMEErrorSeverity.INFO;
			break;
		case WARNING:
			severity = JMEErrorSeverity.WARNING;
			break;
		case CRITICAL:
			severity = JMEErrorSeverity.CRITIQUE;
			break;
		case DEADCODE:
			severity = JMEErrorSeverity.WARNING;
			break;
		default:
			severity = JMEErrorSeverity.INFO;
		}
		this.message = error.toString();
		if (target instanceof JMERule)
			this.locality = (JMERule) target;
		else if (target instanceof JMEModeler)
			this.locality = (JMERule) target;
		else if (target instanceof JMENodeExpression)
			this.locality = ((JMENodeExpression) target).getNode().getRule();
		else
			locality = null;

	}

	public String getMessage() {
		return message;
	}

	public JMEElementWindowable getLocality() {
		return locality;
	}

	public JMEErrorSeverity getSeverity() {
		return severity;
	}

	public JMEErrorType getType() {
		return type;
	}

	public JMEElement getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JMEError) {
			JMEError err = (JMEError) obj;
			boolean b = true;
			b = message.equals(err.message) && type == err.type && target == err.target;
			return b;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(locality).append(" : ").append(type).append("<").append(target.getName()).append(">").append(": ")
		.append(message);
		return sb.toString();
	}
}
