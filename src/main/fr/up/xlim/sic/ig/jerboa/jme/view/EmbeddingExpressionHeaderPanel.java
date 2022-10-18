package fr.up.xlim.sic.ig.jerboa.jme.view;

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
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ExpressionPanel;

public class EmbeddingExpressionHeaderPanel extends JPanel implements JMEElementView {

	private static final long serialVersionUID = 4874899233943603128L;
	private ExpressionPanel textExpr;
	private JButton bntApply;
	private JButton btnReset;
	protected DetailsEmbedding owner;
	private JMEEmbeddingInfo ebd;

	public EmbeddingExpressionHeaderPanel(DetailsEmbedding owner) {
		this.owner = owner;
		ebd = owner.getEmbedding();

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

		JLabel lblComment = new JLabel("Modeler header");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.WEST;
		gbc_lblComment.insets = new Insets(0, 0, 0, 5);
		gbc_lblComment.gridx = 0;
		gbc_lblComment.gridy = 0;
		panel_6.add(lblComment, gbc_lblComment);

		// JScrollPane scrollPane = new JScrollPane();
		// panel_5.add(scrollPane, BorderLayout.CENTER);

		textExpr = new ExpressionPanel("Embedding header", ebd.getModeler(), null);
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

		if (ebd != null) {
			ebd.addView(this);
			reload();
		}
	}

	@Override
	public void unlink() {
		ebd.removeView(this);
	}

	@Override
	public void reload() {
		if (ebd != null)
			textExpr.setText(ebd.getFileHeader());
	}

	public void save() {
		ebd.setFileHeader(textExpr.getText());
		ebd.update(); // normalement doit etre inutile si le code est correct
		// mais ce n'est plus le cas, n'est ce pas Val :p
	}

	@Override
	public JMEElement getSourceElement() {
		return ebd;
	}
}
