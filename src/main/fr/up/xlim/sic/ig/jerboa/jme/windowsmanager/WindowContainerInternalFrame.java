package fr.up.xlim.sic.ig.jerboa.jme.windowsmanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;

public class WindowContainerInternalFrame extends JInternalFrame
		implements WindowContainerInterface, InternalFrameListener, ComponentListener {

	private static final long serialVersionUID = -2453815687549880252L;
	private JerboaModelerEditor editor;
	private DockablePanel panel;
	private JDesktopPane desk;
	private JButton swap;

	public WindowContainerInternalFrame(JerboaModelerEditor editor, DockablePanel panel) {
		super(panel.getTitle(), true, // resizable
				true, // closable
				true, // maximizable
				true);// iconifiable
		this.panel = panel;
		this.editor = editor;

		this.panel.setWindowContainer(this);
		desk = editor.getDesktopPane();

		// Les lignes suivante permettent d'enlever l'affichage du menu popup
		// contenant : 'fermer, r�duire ...'
		// quand on appuie sur ctrl + space . Car cela va cr�er des conflicts
		// avec les raccourcits de l'�diteur
		// de script.
		KeyStroke remove = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK);
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		im.put(remove, "none");
		im = getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		im.put(remove, "none");
		im = getInputMap(WHEN_FOCUSED);
		im.put(remove, "none");

		swap = new JButton("Undock");

		final ImageIcon img = new ImageIcon(getClass().getResource("/image/logo.png"));
		setFrameIcon(img);

		// ...Then set the window size or call pack...
		setSize(panel.getSizeX(), panel.getSizeY());
		setPreferredSize(new Dimension(panel.getSizeX(), panel.getSizeY()));

		desk.add(this);
		setVisible(true); // necessary as of 1.3
		try {
			setSelected(true);
		} catch (PropertyVetoException e1) {
			e1.printStackTrace();
		}

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(swap, BorderLayout.NORTH);
		getContentPane().add(panel.getRootComponent(), BorderLayout.CENTER);
		addInternalFrameListener(this);
		addComponentListener(this);

		swap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
				WindowContainerDialog iframe = new WindowContainerDialog(getEditor(),
						WindowContainerInternalFrame.this.panel);
				iframe.activate();
			}
		});

		pack();

		try {
			if (panel.isMaximized()) {
				System.out.println("DEBUG: panel (" + this.getClass().getName() + ") isMaximized");
				setMaximum(true);
			}
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent arg0) {
		panel.OnFocus(false);
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent arg0) {
		panel.OnClose();
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent arg0) {
		panel.setWindowContainer(null);
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent arg0) {
		panel.OnFocusLost(false);
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameIconified(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameOpened(InternalFrameEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public JerboaModelerEditor getEditor() {
		return editor;
	}

	@Override
	public void close() {
		panel.setWindowContainer(null);
		desk.getDesktopManager().closeFrame(this);
		desk.remove(this);
	}

	@Override
	public void activate() {
		desk.getDesktopManager().activateFrame(this);
		this.grabFocus();
	}

	@Override
	public void switchDialogFrame() {

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {
		Dimension dim = getSize();
		panel.OnResize(dim.width, dim.height);
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
