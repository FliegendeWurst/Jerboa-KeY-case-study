package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ExpressionPanel;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.tab.JPanelTabDescription;

public class RulePrecondNodeTab extends JPanelTabDescription implements JMEElementView {

	private static final long serialVersionUID = 4874899233943603128L;
	private ExpressionPanel textPrecond;
	private JButton bntApply;
	private JButton btnReset;
	private RuleView owner;
	private JMENode node;
	private JLabel lblComment;

	public RulePrecondNodeTab(RuleView view, JMENode node) {
		// super(view.getTabbedPane(), node.getName()+"#Precondition");
		super(node.getName()+"#Precondition");
		this.node = node;
		this.owner = view;
		this.node.addView(this);
		setLayout(new BorderLayout(0, 0));
		buildClosableTab(view.getTabbedPane(), node.getName()+"#Precondition");

		JPanel panel_2 = new JPanel();
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

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletePrecond();
			}
		});
		panel_2.add(btnDelete);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));
		add(panel_1, BorderLayout.CENTER);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));

		JPanel panel_6 = new JPanel();
		panel_5.add(panel_6, BorderLayout.NORTH);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[] { 109, 60, 0 };
		gbl_panel_6.rowHeights = new int[] { 24, 0 };
		gbl_panel_6.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_6.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_6.setLayout(gbl_panel_6);

		lblComment = new JLabel("Precondition:");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.WEST;
		gbc_lblComment.insets = new Insets(0, 0, 0, 5);
		gbc_lblComment.gridx = 0;
		gbc_lblComment.gridy = 0;
		panel_6.add(lblComment, gbc_lblComment);

		// JScrollPane scrollPane = new JScrollPane();
		// panel_5.add(scrollPane, BorderLayout.CENTER);

		textPrecond = new ExpressionPanel(node.getName()+"#precondition", owner.getModeler(),
				new JerboaLanguageGlue(node.getRule(), LanguageState.PRECONDITION)); // TODO: bon finalement ca ne colle pas ton glue
		textPrecond.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				save();
			}
		});
		textPrecond.addKeyListener(new KeyListener() {
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
		panel_5.add(textPrecond, BorderLayout.CENTER);
		// scrollPane.setViewportView(textPrecond);
		// textPrecond.setContentType("");
		reload();
	}

	protected void deletePrecond() {
		int res = JOptionPane.showConfirmDialog(this, "Do you want delete this expression?",
				"Delete expression", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			node.setPrecondition("");
		}
	}

	@Override
	public void unlink() {
		node.removeView(this);
	}

	@Override
	public void reload() {
		lblComment.setText("Precondition of "+node.getName());
		setTabTitle(node.getName()+"#Precondition");
		if(!node.getPrecondition().equals(textPrecond.getText()))
			textPrecond.setText(node.getPrecondition());
	}

	public void save() {
		node.setPrecondition(textPrecond.getText());
		owner.check();
	}

	@Override
	public void OnClose() {
		save();
	}

	@Override
	public JMEElement getSourceElement() {
		return node;
	}
}
