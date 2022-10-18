package fr.up.xlim.sic.ig.jerboa.jme.util;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public interface JMEDrawable extends JMEElementView {

	public JMEElement getModel();

	/**
	 * Fonction qui dessine les primitives dans une zone particuliere de la
	 * fenetre. Le composant devant etre redessiner en fonction du fait s'il est
	 * selectionne ou non.
	 *
	 * @param g
	 * @param range
	 * @param highlight
	 */
	public abstract void draw(Graphics2D g, Rectangle range, boolean highlight, double scaleFactor);

	public abstract void update(Graphics2D g, Rectangle range, boolean highlight);

	boolean isDirty();

	void setDirty(boolean dirty);

	public boolean inside(int x, int y);

	public void move(int dx, int dy);

	public int centerX();

	public int centerY();

	public void endMove(int x, int y);

	public void beginMove(int x, int y);

	public Rectangle bbox();

	void tryMove(int dx, int dy);

	void tryMove(int dx, int dy, int grainGrid);

}