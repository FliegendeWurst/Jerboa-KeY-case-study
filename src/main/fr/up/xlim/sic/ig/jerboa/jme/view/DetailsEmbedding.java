package fr.up.xlim.sic.ig.jerboa.jme.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEVerif;
import fr.up.xlim.sic.ig.jerboa.jme.view.errorstree.ErrorsPanel;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.CommentArea;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ExpressionPanel;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JOrbitComponent;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.JPatternTextField;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ModifyListener;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.DockablePanelAdapter;
import fr.up.xlim.sic.ig.jerboa.jme.windowsmanager.WindowContainerInterface;
import net.miginfocom.swing.MigLayout;

public class DetailsEmbedding extends DockablePanelAdapter implements JMEElementWindowableView {
	private static final long serialVersionUID = 3395032800184630902L;

	private JMEEmbeddingInfo ebd;
	private JTextField textName;
	private JTextField textType;
	private JOrbitComponent orbitComponent;
	private EmbeddingExpressionHeaderPanel expressionHeader;
	private CommentArea commentArea;
	private JButton button;
	private JerboaModelerEditor owner;
	private JPanel panelColor;

	private ErrorsPanel panelError;
	
	private ExpressionPanel defCode;

	private boolean startCheck = false;

	public DetailsEmbedding(JerboaModelerEditor parent, JMEEmbeddingInfo ebd) {
		super(ebd.getName());
		this.owner = parent;
		this.ebd = ebd;
		this.ebd.addView(this);
		setLayout(new MigLayout("", "[103.00][grow,left]", "[][][][][][grow]"));

		JLabel lblName = new JLabel("Name:");
		add(lblName, "cell 0 0,alignx trailing");

		textName = new JPatternTextField(parent.getPreferences(), JPatternTextField.PATTERN_IDENT,
				new ModifyListener() {
					@Override
					public void action() {
						DetailsEmbedding.this.ebd.setName(textName.getText());
						check();
					}
				});
		lblName.setLabelFor(textName);
		add(textName, "cell 1 0,growx");
		textName.setColumns(10);

		JLabel lblOrbit = new JLabel("Orbit:");
		add(lblOrbit, "cell 0 1,alignx trailing");

		orbitComponent = new JOrbitComponent();
		orbitComponent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DetailsEmbedding.this.ebd.setOrbit(orbitComponent.getOrbit());
				check();
			}
		});
		orbitComponent.setColumns(10);
		add(orbitComponent, "cell 1 1,growx");

		JLabel lblType = new JLabel("Type:");
		add(lblType, "cell 0 2,alignx trailing");

		textType = new JPatternTextField(parent.getPreferences(), JPatternTextField.PATTERN_MODULE,
				new ModifyListener() {

					@Override
					public void action() {
						DetailsEmbedding.this.ebd.setType(textType.getText());
						check();

					}
				});
		add(textType, "cell 1 2,growx");
		textType.setColumns(10);
		
		//JTextField defCode = new JTextField();
		defCode = new ExpressionPanel("Default code", ebd.getModeler(), new JerboaLanguageGlue(ebd, null));
		defCode.setToolTipText("Define the default code, for avoiding redundant repetition");
		if(ebd.getDefaultCode() != null)
			defCode.setText(ebd.getDefaultCode());
		//defCode.setColumns(10);
		// add(defCode, "cell 1 3,growx");
		
		/*
		defCode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: proposer qqchose lorsqu'on change le code par defaut, genre cherche remplace dans les anciennes regles
				DetailsEmbedding.this.ebd.setDefaultCode(defCode.getText());
				check();
			}
		});
		*/
		
		defCode.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				DetailsEmbedding.this.ebd.setDefaultCode(defCode.getText());
				check();
			}
		});
		

		JLabel lblColor = new JLabel("Color:");
		add(lblColor, "cell 0 3,alignx right");

		JPanel panel_2 = new JPanel();
		add(panel_2, "cell 1 3,alignx left,growy");
		// panel_2.setLayout(new MigLayout("", "[grow,fill]", "[]"));

		panelColor = new JPanel();
		panelColor.setBorder(new BevelBorder(BevelBorder.RAISED));
		panel_2.add(panelColor, "cell 0 0,growy");
		panelColor.setLayout(new MigLayout("", "[grow,fill]", "[]"));
		Component rigidArea1 = Box.createRigidArea(new Dimension(20, 10));
		rigidArea1.setMaximumSize(new Dimension(60, 10));
		rigidArea1.setPreferredSize(new Dimension(60, 10));
		rigidArea1.setMinimumSize(new Dimension(60, 10));
		panelColor.add(rigidArea1);
		panelColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if(SwingUtilities.isLeftMouseButton(e)) {
					Color color = JColorChooser.showDialog(DetailsEmbedding.this, "Select fill color of "+DetailsEmbedding.this.ebd.getName(), DetailsEmbedding.this.ebd.getColor());
					if(color != null) {
						DetailsEmbedding.this.ebd.setColor(color);
					}
				}
			}
		});

		button = new JButton("...");
		panel_2.add(button, "cell 0 0");
		button.setRolloverEnabled(false);
		button.setRequestFocusEnabled(false);
		button.setBorderPainted(false);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeColor();
			}
		});

		buttonColor(owner.getPreferences().getColorExplicitExpr());

		JPanel panel_1 = new JPanel();
		add(panel_1, "cell 0 4 2 1,grow");

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		panel_1.add(btnApply);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reload();
			}
		});

		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		panel_1.add(rigidArea);
		panel_1.add(btnRefresh);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, "cell 0 5 2 1,grow");

		commentArea = new CommentArea();
		tabbedPane.addTab("Comment", commentArea);
		commentArea.addModifyListener(new ModifyListener() {

			@Override
			public void action() {
				ebd.setComment(commentArea.getText());
			}
		});

		expressionHeader = new EmbeddingExpressionHeaderPanel(this);
		tabbedPane.addTab("Header", null, expressionHeader, null);
		
		tabbedPane.addTab("Default code", null, defCode, null);
		

		panelError = new ErrorsPanel(ebd);
		tabbedPane.addTab("Errors", panelError);

		reload();
		startCheck = true;
	}

	protected void changeColor() {
		Color initColor = ebd.getColor();
		if (initColor == null)
			initColor = owner.getPreferences().getDefaultEbdColor();

		Color c = JColorChooser.showDialog(this, "Chooser Embedding color", initColor);

		if (c != null) {
			ebd.setColor(c);
			buttonColor(c);
		}

	}

	private void buttonColor(Color c) {
		panelColor.setBackground(c);
		// button.setBackground(c);
		// button.setForeground(new Color(255 - c.getRed(), 255 - c.getGreen(),
		// 255 - c.getBlue() ));
	}

	protected void save() {
		ebd.setName(textName.getText());
		ebd.setType(textType.getText());
		ebd.setOrbit(orbitComponent.getOrbit());
		ebd.setComment(commentArea.getText());
		check();
	}

	@Override
	public void reload() {
		textName.setText(ebd.getName());
		textType.setText(ebd.getType());
		orbitComponent.setText(ebd.getOrbit());

		commentArea.setText(ebd.getComment());

		expressionHeader.reload();

		if (ebd.getColor() != null) {
			buttonColor(ebd.getColor());
		} else
			buttonColor(owner.getPreferences().getDefaultEbdColor());
		reloadTitle();
	}

	@Override
	public void unlink() {
		ebd.removeView(this);
	}

	public JMEEmbeddingInfo getEmbedding() {
		return ebd;
	}

	@Override
	public void check() {
		if(startCheck) {
			JMEVerif verifs = owner.getPreferences().getVerif();
			Collection<JMEError> errors = verifs.run(ebd);
			ebd.setErrors(errors);

			owner.check();
			reloadTitle();
		}
	}

	@Override
	public void reloadTitle() {
		WindowContainerInterface window = getWindowContainer();
		if(window != null) {
			window.setTitle(getStateName());
		}
	}
	
	private String getStateName() {
		return ebd.getName() + (ebd.isModified()? "*" : "");
	}
	
	@Override
	public void OnClose() {
		super.OnClose();
		startCheck = false;
	}
}
