package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import fr.up.xlim.sic.ig.jerboa.jme.export.JerboaLanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.GeneratedLanguage;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.Translator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.Translator.ExportLanguage;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;

public class ExpressionTraductionViewer extends JFrame {
	private static final long serialVersionUID = -3010416684799839195L;

	public ExpressionTraductionViewer(String expression, JerboaLanguageGlue glue, JMEModeler modeler) {
		super();
		GeneratedLanguage genLCPP = new GeneratedLanguage();
		try {
			genLCPP = Translator.translate(expression, glue, new TranslatorContextV2(modeler), genLCPP, modeler, ExportLanguage.CPP_V2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.err.println("passe");
		GeneratedLanguage genLJava = new GeneratedLanguage();
		try {
			genLJava = Translator.translate(expression, glue, new TranslatorContextV2(modeler), genLJava, modeler, ExportLanguage.JAVA_V2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String resCPP = "";
		String resJava = "";

		resCPP += "---------- Includes \n" + genLCPP.getInclude() 
		+ "\n\n---------- Content \n" + genLCPP.getContent()
		+ "\n\n---------- In class constructor \n" + genLCPP.getInClassConstructor()
		+ "\n\n---------- In class as private \n" + genLCPP.getInClassPrivate()
		+ "\n\n---------- In class as public \n" + genLCPP.getInClassPublic();

		resJava += "---------- Includes \n" + genLJava.getInclude() 
		+ "\n\n---------- Content \n" + genLJava.getContent() 
		+ "\n\n---------- In class constructor \n" + genLJava.getInClassConstructor()
		+ "\n\n---------- In class as private \n" + genLJava.getInClassPrivate()
		+ "\n\n---------- In class as public \n" + genLJava.getInClassPublic();

		JTextArea tareaCPP = new JTextArea(resCPP);
		tareaCPP.setWrapStyleWord(true);
		tareaCPP.setBackground(new Color(200, 255, 200));
		tareaCPP.setAutoscrolls(true);
		JScrollPane scrCPP = new JScrollPane(tareaCPP);
		JTextArea tareaJava = new JTextArea(resJava);
		tareaJava.setWrapStyleWord(true);
		tareaJava.setBackground(new Color(200, 200, 255));
		tareaJava.setAutoscrolls(true);
		JScrollPane scrJava = new JScrollPane(tareaJava);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(scrCPP);
		splitPane.setRightComponent(scrJava);
		splitPane.setResizeWeight(0.5);
		splitPane.setOneTouchExpandable(true);

		add(splitPane);
		setMinimumSize(new Dimension(600, 600));
		setBackground(Color.blue);
		setLocationRelativeTo(null);
	}

}
