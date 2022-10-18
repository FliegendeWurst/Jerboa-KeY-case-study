package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.export.NewSVGExport;
import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;
import fr.up.xlim.sic.ig.jerboa.jme.view.util.ExpressionPanel;

public class RuleScriptView extends RuleView implements JMEElementView {

	private static final long serialVersionUID = 4330643749544299151L;
	protected JerboaModelerEditor editor;
	final static Icon newModelerIcon = new ImageIcon(
			new ImageIcon(RuleScriptView.class.getResource("/image/graph_icon.png")).getImage().getScaledInstance(20,
					20, Image.SCALE_SMOOTH));

	protected JMEScript script;
	private ExpressionPanel scriptPanel;
	private JMenuItem mntmGraph;

	public RuleScriptView(JerboaModelerEditor editor, JMEScript _script) {
		super(editor, _script);
		this.script = _script;
		if (script == null) {// debug pour le vieweur
			script = new JMEScript(null, "fooScript");
			this.script.addView(this);
		}

		scriptPanel = new ExpressionPanel(script.getName(), script.getModeler(),
				new JerboaLanguageGlue(script, LanguageState.CLASSICAL));
		scriptPanel.setText(script.getContent());
		// panelGraphs.add(scriptPanel);

		mntmGraph = new JMenuItem("Graphs");
		mntmGraph.setIcon(newModelerIcon);
		mntmGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(2);
			}
		});

		mnViews.add(mntmGraph, 2);

		scriptPanel.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				script.setContent(scriptPanel.getText());
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		splitEditor.setLeftComponent(scriptPanel);

		tabbedPane.addTab("Graphs", panelGraphs);

	}

	@Override
	public void exportToSVG(String path) {
		NewSVGExport svgexport = new NewSVGExport(this);
		String svg = svgexport.generate();

		FileOutputStream fos;
		if (!path.endsWith(".svg"))
			path += ".svg";
		try {
			fos = new FileOutputStream(path);
			PrintStream ps = new PrintStream(fos);
			ps.println(svg);
			ps.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reload() {
		super.reload();
		if (script != null && scriptPanel != null)
			scriptPanel.setText(script.getContent());
	}
	
	public ExpressionPanel getScriptPanel() {
		return scriptPanel;
	}


}
