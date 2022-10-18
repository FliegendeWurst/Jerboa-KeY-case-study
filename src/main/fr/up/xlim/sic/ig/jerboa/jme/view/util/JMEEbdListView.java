package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class JMEEbdListView extends JScrollPane implements JMEElementView {
	private static final long serialVersionUID = 3743917875650661031L;

	private Box listEbds;
	private JMEModeler modeler;
	private ArrayList<JMEEbdCheckboxView> ebdviews;

	private JPopupMenu popupMenuEmbeddings;

	private JerboaModelerEditor owner;

	private JMenuItem mntmNewEmbedding;
	private JMenuItem mntmModify;
	private JMenuItem mntmDelete;

	// ---------------------------------------------------------------------------------

	class JMEEbdCheckboxView extends JCheckBox implements JMEElementView, ItemListener {
		private static final long serialVersionUID = 3725854763359474887L;
		private JMEEmbeddingInfo ebd;

		JMEEbdCheckboxView(JMEEmbeddingInfo ebd) {
			super(ebd.toString(), ebd.isVisible());
			this.ebd = ebd;
			this.ebd.addView(this);
			addItemListener(this);
			this.setBackground(SystemColor.controlHighlight);
			setFocusable(true);
			setOpaque(false);
			reload();
		}

		@Override
		public void reload() {
			StringBuilder sb = new StringBuilder();
			sb.append(ebd.getName()).append(ebd.isModified()? "*" : "").append(":").append(ebd.getOrbit()).append(" -> ").append(ebd.getType());
			setText(sb.toString());
			if (isSelected() != ebd.isVisible())
				setSelected(ebd.isVisible());
		}

		@Override
		public void unlink() {
			ebd.removeView(this);
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (isSelected() != ebd.isVisible()) {
				ebd.setIsVisible(isSelected());
				modeler.update();
			}
		}

		@Override
		public JMEElement getSourceElement() {
			return ebd;
		}
	}

	// ---------------------------------------------------------------------------------

	public JMEEbdListView(JerboaModelerEditor _owner) {
		setBorder(null);
		listEbds = Box.createVerticalBox();// new JPanel(new GridLayout(0, 1));
		listEbds.setBorder(null);
		ebdviews = new ArrayList<>();

		setViewportView(listEbds);

		listEbds.setOpaque(true);
		listEbds.setBackground(Color.white);

		popupMenuEmbeddings = new JPopupMenu();
		addPopup(this, popupMenuEmbeddings);

		mntmNewEmbedding = new JMenuItem("New embedding...");
		popupMenuEmbeddings.add(mntmNewEmbedding);
		mntmNewEmbedding.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (modeler != null) {
					owner.createNewEmbeddingInfo();
				}
			}
		});

		mntmModify = new JMenuItem("Modify");
		popupMenuEmbeddings.add(mntmModify);
		mntmModify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selected != null) {
					JMEEbdCheckboxView c = selected;
					owner.openEbd(c.ebd);
				}
			}
		});

		mntmDelete = new JMenuItem("Delete");
		popupMenuEmbeddings.add(mntmDelete);
		mntmDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selected != null) {
					JMEEbdCheckboxView c = selected;

					int confirm = JOptionPane.showConfirmDialog(JMEEbdListView.this,
							"Are-you sure to delete the embedding " + c.ebd.getName() + "?", "Confirmation",
							JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						owner.closeEbd(c.ebd);
						modeler.removeEmbedding(c.ebd);
						reload();
					}
				}
			}
		});

		if (_owner != null && _owner instanceof JerboaModelerEditor) {
			this.owner = _owner;
			modeler = owner.getModeler();
			modeler.addView(this);
			reload();
		}
	}

	@Override
	public void reload() {
		listEbds.removeAll();
		for (JMEEbdCheckboxView e : ebdviews) {
			e.unlink();
		}
		ebdviews.clear();

		List<JMEEmbeddingInfo> list = modeler.getEmbeddings();
		for (JMEEmbeddingInfo e : list) {
			JMEEbdCheckboxView check = new JMEEbdCheckboxView(e);
			addPopup(check, popupMenuEmbeddings);
			ebdviews.add(check);
			listEbds.add(check);
		}
		repaint();
		revalidate();
	}

	@Override
	public void unlink() {
		listEbds.removeAll();
		for (JMEEbdCheckboxView e : ebdviews) {
			e.unlink();
		}
		modeler.removeView(this);
	}

	private transient JMEEbdCheckboxView selected;

	private void addPopup(final Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseListener() {
			private void showMenu(MouseEvent e) {
				if (e.getSource() instanceof JMEEbdCheckboxView) {
					mntmModify.setEnabled(true);
					mntmDelete.setEnabled(true);
					selected = (JMEEbdCheckboxView) e.getSource();
				} else {
					mntmModify.setEnabled(false);
					mntmDelete.setEnabled(false);
				}
				popup.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (e.getSource() instanceof JMEEbdCheckboxView) {
					((JMEEbdCheckboxView) e.getSource()).setOpaque(false);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (e.getSource() instanceof JMEEbdCheckboxView) {
					((JMEEbdCheckboxView) e.getSource()).setOpaque(true);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
	}

	@Override
	public JMEElement getSourceElement() {
		return modeler;
	}
}
