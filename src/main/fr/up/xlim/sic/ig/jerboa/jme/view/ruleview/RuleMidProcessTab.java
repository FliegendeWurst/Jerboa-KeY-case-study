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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ExpressionPanel;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.tab.JPanelTabDescription;

public class RuleMidProcessTab extends JPanelTabDescription implements JMEElementView {

	private static final long serialVersionUID = 4874899233943603128L;
	private ExpressionPanel textMidprocess;
	private JButton bntApply;
	private JButton btnReset;
	private JMERule rule;
	private RuleView owner;

	public RuleMidProcessTab(RuleView view) {
		super(view.getTabbedPane(), "MidProcess");
		this.owner = view;
		this.rule = view.getRule();
		this.rule.addView(this);
		setLayout(new BorderLayout(0, 0));

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

		JLabel lblComment = new JLabel("MidProcess:(access to the left pattern via @leftpattern)");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.WEST;
		gbc_lblComment.insets = new Insets(0, 0, 0, 5);
		gbc_lblComment.gridx = 0;
		gbc_lblComment.gridy = 0;
		panel_6.add(lblComment, gbc_lblComment);

		// JScrollPane scrollPane = new JScrollPane();
		// panel_5.add(scrollPane, BorderLayout.CENTER);

		textMidprocess = new ExpressionPanel("midprocess", rule.getModeler(),
				new JerboaLanguageGlue(rule, LanguageState.MIDPROCESS));
		textMidprocess.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				save();
			}
		});
		textMidprocess.addKeyListener(new KeyListener() {
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
//		textMidprocess.addMouseListener(new MouseListener() {
//			@Override
//			public void mouseReleased(MouseEvent e) {}			
//			@Override
//			public void mousePressed(MouseEvent e) {}			
//			@Override
//			public void mouseExited(MouseEvent e) {}			
//			@Override
//			public void mouseEntered(MouseEvent e) {}			
//			@Override
//			public void mouseClicked(MouseEvent e) {}
//		});
		panel_5.add(textMidprocess, BorderLayout.CENTER);
		// scrollPane.setViewportView(textMidprocess);
		// textPrecond.setContentType("");
		reload();
	}

	@Override
	public void unlink() {
		rule.removeView(this);
	}

	@Override
	public void reload() {
		if(!rule.getMidProcess().equals(textMidprocess.getText()))
			textMidprocess.setText(rule.getMidProcess());
	}

	public void save() {
		rule.setMidProcess(textMidprocess.getText());
		rule.update();
		owner.check();
	}

	@Override
	public void OnClose() {
		save();
	}

	@Override
	public JMEElement getSourceElement() {
		return owner.getSourceElement();
	}

}
