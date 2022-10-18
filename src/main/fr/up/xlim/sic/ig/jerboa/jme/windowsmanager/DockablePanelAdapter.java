package fr.up.xlim.sic.ig.jerboa.jme.windowsmanager;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;

public class DockablePanelAdapter extends JPanel implements DockablePanel {
	private static final long serialVersionUID = 313228200306015191L;
	private String title;
	private JComponent delegate;
	private WindowContainerInterface parent;
	
	private int posX;
	private int posY;
	private int sizeX;
	private int sizeY;
	private boolean isfullscreen;
	
	public DockablePanelAdapter(String title, JComponent component) {
			this.delegate = component;
			this.title = title;
			parent = null;
			sizeX = 800;
			sizeY = 600;
			isfullscreen = true;
			posX = 0;
			posY = 0;
	}
	
	protected DockablePanelAdapter(String title) {
		this.delegate = this;
		this.title = title;
		parent = null;
		sizeX = 800;
		sizeY = 600;
		isfullscreen = true;
		posX = 0;
		posY = 0;
	}

	
	
	@Override
	public void reload() {

	}

	@Override
	public void unlink() {

	}

	@Override
	public Component getRootComponent() {
		return delegate;
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
		return sizeX;
	}


	@Override
	public int getSizeY() {
		return sizeY;
	}


	@Override
	public boolean isMaximized() {
		return isfullscreen;
	}


	@Override
	public void OnResize(int width, int height) {
		sizeX = width;
		sizeY = height;
	}


	@Override
	public int getPosX() {
		return posX;
	}


	@Override
	public int getPosY() {
		return posY;
	}


	@Override
	public void OnMove(int x, int y) {
		this.posX = x;
		this.posY = y;
	}


	@Override
	public void OnMaximize() {
		isfullscreen = true;
	}


	@Override
	public void OnUnMaximize() {
		isfullscreen = false;
	}


	@Override
	public void OnClose() {
		
	}


	@Override
	public void check() {
		
	}


	@Override
	public void reloadTitle() {
		
	}

	@Override
	public void OnFocus(boolean b) {
		
	}

	@Override
	public void OnFocusLost(boolean temporary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JMEElement getSourceElement() {
		System.err.println("Normalement ce message n'est pas normal...");
		return null;
	}

}
