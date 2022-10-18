package fr.up.xlim.sic.ig.jerboa.jme.windowsmanager;

import java.awt.Component;

import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementWindowableView;

/**
 * Interface des de bases des panneaux etant integre dans notre systeme de fenetres. 
 * @author hakim
 *
 */
public interface DockablePanel  extends JMEElementWindowableView {

	/**
	 * Renvoie le composant graphique (SWING) devant etre integre dans la fenetre.
	 * @return Renvoie le composant devant s'afficher dans la fenetre.
	 */
	Component getRootComponent();

	String getTitle();

	void setWindowContainer(WindowContainerInterface windowContainerDialog);
	WindowContainerInterface getWindowContainer();

	int getPosX();
	int getPosY();
	int getSizeX();
	int getSizeY();
	boolean isMaximized();

	void OnResize(int width, int height);
	void OnMove(int x, int y);
	void OnMaximize();
	void OnUnMaximize();
	void OnClose();
	
	void OnFocus(boolean temporary);
	void OnFocusLost(boolean temporary);
}
