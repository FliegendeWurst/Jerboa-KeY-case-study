package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.CommentArea;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JPatternComboBox;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JPatternTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ModifyComboBoxListener;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ModifyListener;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.tab.JMEButtonTabComponent;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.tab.TabDescription;
import net.miginfocom.swing.MigLayout;

public class RuleDetailTab extends JPanel implements JMEElementView, TabDescription {

	private static final long serialVersionUID = 4874899233943603128L;
	private JTextField textName;
	private CommentArea textComment;
	// private JToggleButton tglbtnEdit;
	private JButton bntApply;
	private JButton btnReset;
	private JMERule rule;
	private JPatternComboBox textCategory;
	private RuleView owner;

	public RuleDetailTab(RuleView view, JMERule rulex) {
		this.owner = view;
		this.rule = rulex;
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

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(3, 1, 0, 1));
		panel_1.add(panel_4, BorderLayout.NORTH);

		textName = new JPatternTextField(view.getPreferences(), JPatternTextField.PATTERN_IDENT, new ModifyListener() {

			@Override
			public void action() {
				rule.setName(textName.getText());
				owner.check();
			}
		});
		panel_4.setLayout(new MigLayout("", "[31px][202.00px,grow,fill][49px][243px,grow,fill]", "[20px]"));

		JLabel lblName = new JLabel("Name:");
		panel_4.add(lblName, "cell 0 0,alignx left,aligny center");
		panel_4.add(textName, "cell 1 0,alignx center,aligny center");
		textName.setColumns(30);

		JLabel lblCategory = new JLabel("Category:");
		panel_4.add(lblCategory, "cell 2 0,alignx left,aligny center");

		textCategory = new JPatternComboBox(view.getEditor(), JPatternTextField.PATTERN_MODULE_OR_EMPTY,
				new ModifyComboBoxListener() {

					@Override
					public void setComboBoxModel(ComboBoxModel<String> model) {

					}

					@Override
					public void action() {
						String string = (String) textCategory.getSelectedItem();
						rule.setCategory(string);
						owner.check();
					}
				});
		textCategory.setPreferredSize(new Dimension(250, 20));

		panel_4.add(textCategory, "cell 3 0,alignx center,aligny center");
		// textCategory.setColumns(15);

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblComment = new JLabel("Comment (HTML):");
		lblComment.setVerticalAlignment(SwingConstants.TOP);
		panel_5.add(lblComment, BorderLayout.WEST);

		textComment = new CommentArea();
		textComment.setMinimumSize(new Dimension(1, 100));
		panel_5.add(textComment, BorderLayout.CENTER);
		textComment.addModifyListener(new ModifyListener() {
			@Override
			public void action() {
				rule.setComment(textComment.getText());
			}
		});

		tabcomp = new JMEButtonTabComponent(this.owner.getTabbedPane());
		reload();
	}

	@Override
	public void unlink() {
		rule.removeView(this);
	}

	@Override
	public void reload() {
		if(rule.getName().compareTo(textName.getText() )!=0)
			textName.setText(rule.getName());
		if(rule.getComment().compareTo(textComment.getText() )!=0)
			textComment.setText(rule.getComment());
		if(rule.getCategory().compareTo((String)textCategory.getSelectedItem())!=0) {
			for(int i=0;i<textCategory.getItemCount();i++) {
				if(rule.getCategory().compareTo(textCategory.getItemAt(i))==0) {
					textCategory.setSelectedIndex(i);
					textCategory.updateUI();
					System.err.println("compare : " + rule.getCategory() + " with " + textCategory.getItemAt(i));
					break;
				}
			}
		}
	}

	public void save() {
		rule.setName(textName.getText());
		rule.setCategory((String) textCategory.getSelectedItem());
		System.err.println("category for rule" + textName.getText() + " is set to " + rule.getCategory());
		rule.setComment(textComment.getText());
		owner.check();
		// rule.update();
	}

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

	private JLabel label = new JLabel(getInitialTitle());
	private JMEButtonTabComponent tabcomp;

	@Override
	public Component getTabComponent() {
		return label;
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
		return "Detail";
	}

	@Override
	public JMEElement getSourceElement() {
		return owner.getSourceElement();
	}

}
