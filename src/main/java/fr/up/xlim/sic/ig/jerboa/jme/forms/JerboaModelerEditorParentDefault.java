package fr.up.xlim.sic.ig.jerboa.jme.forms;

import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class JerboaModelerEditorParentDefault implements JerboaModelerEditorParent, JMEElementView {

	private JFrame frame;
	private JMEPreferences preferences;
	private JerboaModelerEditor jme; // current
	
	public static boolean DEBUG = true;
	public static boolean AUTOSCREEN = false;

	public JerboaModelerEditorParentDefault() {
		preferences = new JMEPreferences();
		try {
			preferences.load();
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame = new JFrame("JerboaModelerEditor");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if(preferences.getMaximizeWindow())
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.addWindowListener(winlis);
		
		
		final ImageIcon img = new ImageIcon(getClass().getResource("/image/logo.png"));
		frame.setIconImage(img.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
	}

	@Override
	public Window getParent() {
		return frame;
	}

	@Override
	public void change(JerboaModelerEditor jme) {
		if (frame == null) {
			System.out.println("FRAME IS NULL IN PARENT");
			frame = new JFrame("JerboaModelerEditor: " + jme.getModeler().getName());
			if(preferences.getMaximizeWindow())
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getContentPane().add(jme);
			frame.pack();
			frame.addWindowListener(winlis);
			frame.setVisible(true);
		} else {
			frame.getContentPane().removeAll();
			frame.getContentPane().add(jme);
			frame.pack();
			if (!frame.isVisible())
				frame.setVisible(true);
		}

		this.jme = jme;
		jme.getModeler().addView(this);
		this.jme.reload();
		reload();
	}

	@Override
	public JerboaModelerEditorParent newWindow(JerboaModelerEditor jme) {
		JerboaModelerEditorParentDefault parentBis = new JerboaModelerEditorParentDefault();
		parentBis.frame = new JFrame("JerboaModelerEditor: " + jme.getModeler().getName());
		parentBis.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if(preferences.getMaximizeWindow())
			parentBis.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		parentBis.frame.getContentPane().add(jme);
		parentBis.frame.pack();
		parentBis.frame.addWindowListener(winlis);
		parentBis.frame.setVisible(true);
		parentBis.jme = jme;
		parentBis.jme.reload();
		parentBis.jme.getModeler().addView(this);

		final ImageIcon img = new ImageIcon(getClass().getResource("/image/logo.png"));
		parentBis.frame.setIconImage(img.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		parentBis.reload();
		return parentBis;
	}

	@Override
	public void close() {
		frame.setVisible(false);
		frame.dispose();
	}

	@Override
	public void reload() {
		if (frame != null) {
			if (jme != null)
				frame.setTitle("JerboaModelerEditor: " + jme.getModeler().getName() + (jme.getModeler().isModified()? "*" : ""));
			else
				frame.setTitle("JerboaModelerEditor");
		}

	}

	@Override
	public void unlink() {

	}

	@Override
	public JMEPreferences getPreferences() {
		return preferences;
	}
	
	private static int countRootFrame = 0;
	private static WindowListener winlis = new WindowListener() {
		
		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			countRootFrame++;
			System.out.println("COUNTROOTFRAME: "+countRootFrame);
			
		}
		
		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void windowClosing(WindowEvent e) {
			
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			countRootFrame--;
			System.out.println("COUNTROOTFRAME: "+countRootFrame);
			if(countRootFrame <= 0)
				System.exit(0);
		}
		
		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	public JMEElement getSourceElement() {
		return jme.getSourceElement();
	}
}
