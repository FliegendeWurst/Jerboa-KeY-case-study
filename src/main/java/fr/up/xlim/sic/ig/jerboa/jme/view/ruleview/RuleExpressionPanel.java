package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ExpressionPanel;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.tab.JMEButtonTabComponent;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.tab.TabDescription;

public class RuleExpressionPanel extends JPanel implements JMEElementView, TabDescription {

	private static final long serialVersionUID = 4874899233943603128L;
	private ExpressionPanel textExpr;
	private JButton bntApply;
	private JButton btnReset;
	private RuleView owner;
	private JMENodeExpression model;
	private String title;
	private JMEButtonTabComponent tabComponent;

	private transient boolean saving; // evite la reentrance
	private JButton btnDelete;

	public RuleExpressionPanel(RuleView view, JMENodeExpression nodeexpr, String title) {
		this.owner = view;
		this.model = nodeexpr;
		this.model.addView(this);
		this.title = title;
		saving = false;
		tabComponent = new JMEButtonTabComponent(view.getTabbedPane());
		// this.rule.addView(this);
		setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_2.setFocusable(false);
		panel_2.setRequestFocusEnabled(false);
		add(panel_2, BorderLayout.SOUTH);

		bntApply = new JButton("Apply");
		bntApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		panel_2.add(bntApply);

		btnReset = new JButton("Refresh");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});
		panel_2.add(btnReset);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int res = JOptionPane.showConfirmDialog(RuleExpressionPanel.this, "Do you want delete this expression?",
						"Delete expression", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (res == JOptionPane.YES_OPTION) {
					deleteExpr();
				}
			}
		});
		panel_2.add(btnDelete);

		JPanel panel_1 = new JPanel();
		panel_1.setFocusable(false);
		panel_1.setRequestFocusEnabled(false);
		panel_1.setLayout(new BorderLayout(0, 0));
		add(panel_1, BorderLayout.CENTER);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JPanel panel_5 = new JPanel();
		panel_5.setFocusable(false);
		panel_5.setRequestFocusEnabled(false);
		panel_1.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));

		JPanel panel_6 = new JPanel();
		panel_6.setFocusable(false);
		panel_6.setRequestFocusEnabled(false);
		panel_5.add(panel_6, BorderLayout.NORTH);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[] { 109, 60, 0 };
		gbl_panel_6.rowHeights = new int[] { 24, 0 };
		gbl_panel_6.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_6.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_6.setLayout(gbl_panel_6);

		JLabel lblComment = new JLabel("Expression (" + title + ")");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.WEST;
		gbc_lblComment.insets = new Insets(0, 0, 0, 5);
		gbc_lblComment.gridx = 0;
		gbc_lblComment.gridy = 0;
		panel_6.add(lblComment, gbc_lblComment);

		// JScrollPane scrollPane = new JScrollPane();
		// panel_5.add(scrollPane, BorderLayout.CENTER);

		textExpr = new ExpressionPanel("Expression (" + title + ")", view.getRule().getModeler(),
				new JerboaLanguageGlue(model));
		lblComment.setLabelFor(textExpr);
		textExpr.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				save();
			}
		});
		textExpr.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}			
			@Override
			public void keyReleased(KeyEvent e) {
				save();
			}			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		panel_5.add(textExpr, BorderLayout.CENTER);
		// scrollPane.setViewportView(textExpr);
		// textPrecond.setContentType("");
		reload();

		addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() == null // on close event
						&& model.getExpression().replaceAll("[\\s|\\n|\\t]", "").length() == 0) {
					model.getNode().removeExpression(model);
				}
			}
		});
	}

	@Override
	public void unlink() {
		model.removeView(this);
	}

	@Override
	public void reload() {
		textExpr.setText(model.getExpression());
	}

	public void save() {
		if (saving)
			return;
		saving = true;
		try {
			String text = textExpr.getText().trim();
			if (text.isEmpty()) {
				int res = JOptionPane.showConfirmDialog(this, "Do you want delete this empty expression?",
						"Empty expression", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (res == JOptionPane.YES_OPTION) {
					deleteExpr();

				}
			} else {
				model.setExpression(textExpr.getText());
			}
			model.update();
			owner.refresh();
		} finally {
			saving = false;
			owner.check();
		}
	}

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	private void deleteExpr() {
		owner.getTabbedPane().removeTabDescription(this);
		model.getNode().removeExpression(model);
		unlink();
		owner.check();
	}

	@Override
	public Component getTabComponent() {
		return tabComponent;
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void OnClose() {
		save();
	}

	@Override
	public String getInitialTitle() {
		return title;
	}

	// TODO: voir si valentin trouve une idee sur comment donnez le focus sans
	// faire un click inutile
	public void fixFocus() {
		textExpr.getContentPanel().requestFocusInWindow();
	}

	@Override
	public JMEElement getSourceElement() {
		return owner.getSourceElement();
	}
}
