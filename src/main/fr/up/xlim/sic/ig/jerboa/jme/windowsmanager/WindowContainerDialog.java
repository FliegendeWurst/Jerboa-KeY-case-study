package fr.up.xlim.sic.ig.jerboa.jme.windowsmanager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;

public class WindowContainerDialog extends JDialog implements WindowContainerInterface, WindowListener, ComponentListener, FocusListener {
	private static final long serialVersionUID = -4619633157723417544L;
	private JerboaModelerEditor editor;
	private Component component;
	private DockablePanel panel;
	private JButton swap;
	
	public WindowContainerDialog(JerboaModelerEditor editor, DockablePanel panel) {
		super(editor.getWindow());
		this.editor = editor;
		this.panel = panel;
		panel.setWindowContainer(this);
		
		setModalityType(ModalityType.MODELESS);
		swap = new JButton("Redock");
		
		setLocation(panel.getPosX(), panel.getPosY());
		setSize(panel.getSizeX(), panel.getSizeY());
		setPreferredSize(new Dimension(panel.getSizeX(), panel.getSizeY()));
		
		component = panel.getRootComponent();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(swap, BorderLayout.NORTH);
		getContentPane().add(component);
		addWindowListener(this);
		
		addComponentListener(this);
		addFocusListener(this);
		
		setTitle(panel.getTitle());
		setResizable(true);
		
		
		if(panel.isMaximized()) {
			
		}
		
		swap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
				WindowContainerInternalFrame iframe = new WindowContainerInternalFrame(getEditor(), WindowContainerDialog.this.panel);
				iframe.activate();
			}
		});
		
		pack();
		
	}

	@Override
	public JerboaModelerEditor getEditor() {
		return editor;
	}
	

	@Override
	public void close() {
		panel.setWindowContainer(null);
		super.setVisible(false);
	}

	@Override
	public void activate() {
		if(!isVisible())
			this.setVisible(true);
		this.requestFocus();
	}

	@Override
	public void switchDialogFrame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		panel.OnClose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		panel.setWindowContainer(null);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		Dimension dim = getSize();
		panel.OnResize(dim.width, dim.height);
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		panel.OnFocus(e.isTemporary());
	}

	@Override
	public void focusLost(FocusEvent e) {
		panel.OnFocusLost(e.isTemporary());
		
	}
}
