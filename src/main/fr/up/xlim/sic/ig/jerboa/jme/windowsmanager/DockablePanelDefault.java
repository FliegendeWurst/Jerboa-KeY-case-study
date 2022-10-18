package fr.up.xlim.sic.ig.jerboa.jme.windowsmanager;

import java.awt.Component;

import javax.swing.JPanel;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElementWindowable;

public abstract class DockablePanelDefault extends JPanel implements DockablePanel {
	
	private static final long serialVersionUID = 2158153675825176892L;
	
	protected String title;
	protected JMEElementWindowable delegate;
	protected WindowContainerInterface parent;
	
	public DockablePanelDefault(String title, JMEElementWindowable component) {
			this.delegate = component;
			this.title = title;
			parent = null;
	}
	

	@Override
	public Component getRootComponent() {
		return this;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setWindowContainer(WindowContainerInterface parent) {
		this.parent = parent;
	}


	@Override
	public WindowContainerInterface getWindowContainer() {
		return parent;
	}


	@Override
	public int getSizeX() {
		return delegate.getSizeX();
	}


	@Override
	public int getSizeY() {
		return delegate.getSizeY();
	}


	@Override
	public boolean isMaximized() {
		return delegate.isIsfullscreen();
	}


	@Override
	public void OnResize(int width, int height) {
		delegate.setSizeX(width);
		delegate.setSizeY(height);
	}
	
	@Override
	public int getPosX() {
		return delegate.getPosX();
	}
	
	@Override
	public int getPosY() {
		return delegate.getPosY();
	}
	
	@Override
	public void OnMove(int x, int y) {
		delegate.setPosX(x);
		delegate.setPosY(y);
	}
	
	
	public void OnMaximize() {
		delegate.setIsfullscreen(true);
	}
	
	@Override
	public void OnUnMaximize() {
		delegate.setIsfullscreen(false);
	}

	@Override
	public JMEElement getSourceElement() {
		return delegate;
	}
}
