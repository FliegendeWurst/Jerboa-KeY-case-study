package fr.up.xlim.sic.ig.jerboa.jme.model;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoableObject;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;


public interface JMEElement extends UndoableObject{
	
	/**
	 * Methode qui ajoute une vue a l'objet en cours.
	 * @param view: vue qui souhaite s'ajouter au modele
	 */
	void addView(JMEElementView view);
	
	/**
	 * Methode qui retire la vue passee en parametre au modele en cours.
	 * @param view: vue devant etre retiree du modele.
	 */
	void removeView(JMEElementView view);
	
	/**
	 * Fonction qui demande aux differentes vues de se mettre a jour.
	 */
	void update();
	
	boolean isModified();
	void resetModification();

	String getName();

	<T> T visit(JMEVisitor<T> visitor);
}
