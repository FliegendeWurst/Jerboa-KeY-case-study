package fr.up.xlim.sic.ig.jerboa.jme.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeKind;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.GeneratedLanguage;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.Translator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.Translator.ExportLanguage;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;
import up.jerboa.core.JerboaOrbit;


public class JavaExport {

	private static final String TAB = "    ";

	private static final String basicImport =
			"\nimport java.util.List;\n"
					+ "import java.util.ArrayList;\n"
					+ "import up.jerboa.core.rule.*;\n"
					+ "import up.jerboa.core.util.*;\n"
					+ "import up.jerboa.core.*;\n"
					+ "import up.jerboa.exception.JerboaException;\n\n";

	private static boolean isRebuildFramework = false;

	private static boolean isPrimaryType(String type) {
		return type.compareToIgnoreCase("boolean") == 0 || type.compareToIgnoreCase("int") == 0
				|| type.compareToIgnoreCase("float") == 0 || type.compareToIgnoreCase("double") == 0;
	}

	public static void exportModeler(JMEModeler modeler) {
		// imports of defined rules
		StringBuilder sbErrorRule = new StringBuilder();
		int countErrRule = 0;
		for (final JMERule r : modeler.getRules()) {
			if (!r.check()) {
				countErrRule++;
			}
		}

		if(countErrRule > 0) {
			if(JOptionPane.showConfirmDialog(null, "Somes rules are in errors: \n"+sbErrorRule+"\nDo you want continue the generation without rule in error?","Error",JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				System.err.println("GENERATION ABORTED");
				return;
			}
			else {
				System.err.println("GENERATION FORCED");
			}
		}
		exportModelerOnly(modeler);
		for (JMERule r : modeler.getRules()) {
			exportRule(r);
		}

	}

	public static void exportModelerOnly(JMEModeler modeler) {
		String string_expr = modeler.getHeader();
		GeneratedLanguage genResult = new GeneratedLanguage();
		try {
			genResult = Translator.translate(string_expr, new JerboaLanguageGlue(modeler), new TranslatorContextV2(modeler), genResult, modeler,
					ExportLanguage.JAVA);
		} catch (Exception e) {
			string_expr = "#ERROR in expression translation of the modeler # " + string_expr;
			System.err.println(e);
		}

		// ==================================================================
		// ==================================================================
		// ==================================================================

		String curpath = modeler.getFileJME(); // mis a jour auto par le fichier
		File fileJME = new File(curpath);
		System.setProperty("user.dir", fileJME.getParent());
		
		File newdestdir = new File(modeler.getDestDir());
		if(!newdestdir.isAbsolute()) {
			newdestdir = new File(fileJME.getParentFile(), modeler.getDestDir());
		}
			

		//String path = modeler.getDestDir().replace('\\', File.separatorChar).replace('/', File.separatorChar);
		final String subrep = modeler.getModule().replace('.', File.separatorChar) + File.separatorChar;
		File cr = new File(newdestdir, subrep);
		//cr = new File(cr.getAbsolutePath());
		
		/*String realPath = path + (path.endsWith(File.separatorChar + "") ? "" : File.separatorChar) + subrep;
		if (!realPath.endsWith(File.separator)) {
			realPath += File.separator;
		}
		realPath.replace("\\\\", "/").replaceAll("/+", File.separator);
		 */
		System.out.println("EXPORT MODELER DIR: "+cr.getAbsolutePath());
		final boolean created = cr.mkdirs();

		if (!created) {
			System.err.println("Creation of directories failed! (" + cr + ")");
		}
		String modelerName = modeler.getName();



		StringBuilder modBuf = new StringBuilder();
		StringBuilder modBasicImport = new StringBuilder();

		// BASICS IMPORTS

		modBuf.append("package ").append(modeler.getModule()).append(";\n");
		modBuf.append(basicImport);
		if(isRebuildFramework) {
			modBuf.append("import up.jerboa.rebuild.core.*;\n");
		}



		for (JMERule r : modeler.getRules()) {
			boolean ableToExport = r.check();
			if(r instanceof JMEScript) {
				// on empeche les script seulement si ils ont des erreurs non topo!
				ableToExport = true;//r.getAllErrors().stream().filter(err -> err.getType()!= JMEErrorType.TOPOLOGIC).collect(Collectors.toList()).size()<=0;
			}
			if(ableToExport) 
				modBuf.append("import ").append(genRuleImport(modeler, r)).append(";\n");
		
		}

		modBuf.append(modBasicImport.toString());
		modBuf.append("\n\n");

		// HEADER IMPORTS
		// if(header != null)
		modBuf.append(genResult.getInclude());

		// COMMENT PART

		modBuf.append("\n/**\n * ");
		modBuf.append(modeler.getComment().replace("\n", "\n * "));
		modBuf.append("\n */\n\n");


		// DECLARATION OF THE MODELER CLASS
		if(isRebuildFramework)
			modBuf.append("public class ").append(modelerName).append(" extends JerboaModelerRebuild {\n\n");
		else
			modBuf.append("public class ").append(modelerName).append(" extends JerboaModelerGeneric {\n\n");

		modBuf.append(TAB).append("// BEGIN LIST OF EMBEDDINGS").append("\n");

		for (JMEEmbeddingInfo ebd : modeler.getEmbeddings()) {
			String name = ebd.getName();
			modBuf.append(TAB).append("protected JerboaEmbeddingInfo ").append(name).append(";\n");
		}
		modBuf.append(TAB).append("// END LIST OF EMBEDDINGS").append("\n");
		modBuf.append("\n");

		// if(header != null) {
		// modBuf.append(TAB).append("// BEGIN USER DECLARATION").append("\n");
		// modBuf.append(header.r());
		// modBuf.append(TAB).append("// END USER DECLARATION").append("\n");
		// }
		modBuf.append(TAB).append("// BEGIN USER DECLARATION").append("\n");
		modBuf.append(genResult.getContent());
		modBuf.append(TAB).append("// END USER DECLARATION").append("\n");

		modBuf.append("\n");

		// CONSTRUCTOR DEFINITION

		modBuf.append(TAB);
		modBuf.append("public ");
		modBuf.append(modelerName);
		modBuf.append("() throws JerboaException {\n\n");

		modBuf.append(TAB).append(TAB);
		modBuf.append("super(").append(modeler.getDimension()).append(");\n\n");

		modBuf.append(TAB).append("// BEGIN USER HEAD CONSTRUCTOR TRANSLATION").append("\n");
		modBuf.append(genResult.getInClassConstructor() + "\n");
		modBuf.append(TAB).append("// END USER HEAD CONSTRUCTOR TRANSLATION").append("\n");

		// BEGIN INITIALISATION OF EMBEDDINGS
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			String name = e.getName();
			modBuf.append(TAB).append(TAB);
			modBuf.append(name);
			modBuf.append(" = new JerboaEmbeddingInfo(\"");
			modBuf.append(e.getName());
			modBuf.append("\", JerboaOrbit.orbit(");

			for (int j = 0; j < e.getOrbit().size(); j++) {
				int orbit = e.getOrbit().get(j);
				if (j > 0)
					modBuf.append(",");
				modBuf.append(String.valueOf(orbit));
			}
			modBuf.append("), ");
			modBuf.append(e.getType());
			modBuf.append(".class);\n");
		}
		// END INITIALISATION OF EMBEDDINGS

		modBuf.append("\n");

		// enregistrement des plongements et du reinit de la gmap
		modBuf.append(TAB).append(TAB);
		modBuf.append("this.registerEbdsAndResetGMAP(");
		boolean notfirst = false;
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if (notfirst)
				modBuf.append(",");
			else
				notfirst = true;
			modBuf.append(e.getName());
		}
		modBuf.append(");\n");

		modBuf.append("\n");

		for (JMERule r : modeler.getRules()) {
			boolean ableToExport = r.check();
			if(r instanceof JMEScript) {
				// on empeche les script seulement si ils ont des erreurs non topo! // TODO : Val : marre que Áa marche pas 
				ableToExport = true; // r.getAllErrors().stream().filter(err -> err.getType()!= JMEErrorType.TOPOLOGIC).collect(Collectors.toList()).size()<=0;
			}
			if(ableToExport) {
				modBuf.append(TAB).append(TAB);
				modBuf.append("this.registerRule(new ");
				modBuf.append(r.getName());
				modBuf.append("(this));\n");
				//System.out.println("REGISTER RULE: "+r.getName());
			}
		}

		modBuf.append(TAB).append("}\n\n");

		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			modBuf.append(TAB);
			String func = nameToProperty(e.getName());
			String field = e.getName();
			modBuf.append("public final JerboaEmbeddingInfo get").append(func).append("() {\n").append(TAB)
			.append(TAB).append("return ").append(field).append(";\n").append(TAB).append("}\n\n");
		}
		// end class
		modBuf.append("}\n");

		File classModeler = null;
		FileOutputStream modelerStream = null;

		classModeler = new File(cr, modelerName + ".java");
		// classModeler.createNewFile();
		System.out.println("\tExporting modeler: "+classModeler.getAbsolutePath());
		try {
			modelerStream = new FileOutputStream(classModeler.getAbsolutePath());
			modelerStream.write((modBuf.toString()).getBytes());
			modelerStream.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Modeler '"+modelerName+"' exported at " + cr.getAbsolutePath());
	}

	private static String genRuleImport(JMEModeler modeler, JMERule r) {
		String modPackage = genModPackage(modeler);
		String rPackage = r.getFullName().replaceAll("::", ".");
		return modPackage+"."+rPackage;
	}

	private static String genModImport(JMEModeler modeler) {
		String modPackage = modeler.getModule().replaceAll("::", ".");
		if(modPackage != null && !modPackage.isEmpty())
			modPackage = modPackage + ".";
		modPackage = modPackage + modeler.getName();
		return modPackage;
	}


	private static String genRulePackage(JMEModeler modeler, JMERule r) {
		String modPackage = genModPackage(modeler);
		String rPackage = r.getCategory().replaceAll("::", ".");
		if(rPackage.trim().isEmpty())
			return modPackage;
		else
			return modPackage+"."+rPackage;
	}

	private static String genModPackage(JMEModeler modeler) {
		String modPackage = modeler.getModule().replaceAll("::", ".");
		return modPackage;
	}

	public static void exportRule(JMERule r) {
		if(!(r instanceof JMEScript) && r.hasTopoErrors()) { // TODO : Val : j'enlËve pour les script car sinon on ne peut pas leur donner de graph droit
			System.out.println("EXPORT RULE ABORTED BECAUSE TOO MANY TOPOLOGICAL ERRORS: "+r);
			return;
		}

		JMEModeler modeler = r.getModeler();
/*		String curpath = modeler.getFileJME(); // mis a jour auto par le fichier
		// curpath = curpath.replace('.', File.separatorChar).replaceAll("::", File.separator);
		File fileJME = new File(curpath);
		System.setProperty("user.dir", fileJME.getParent());

		String path = modeler.getDestDir().replace('\\', File.separatorChar).replace('/', File.separatorChar);
		final String subrep = modeler.getModule().replace('.', File.separatorChar).replaceAll("::", File.separator);
		final File cr = new File(path, subrep);
*/
		
		String curpath = modeler.getFileJME(); // mis a jour auto par le fichier
		File fileJME = new File(curpath);
		System.setProperty("user.dir", fileJME.getParent());
		
		File newdestdir = new File(modeler.getDestDir());
		if(!newdestdir.isAbsolute()) {
			newdestdir = new File(fileJME.getParentFile(), modeler.getDestDir());
		}
		final File cr = new File(newdestdir, modeler.getModule().replace('.', File.separatorChar).replaceAll("::", File.separator));
		
		exportRule(cr, r);
	}

	private static void exportRule(File pathModeler, JMERule r) {
		String subrepcat = r.getCategory().replace('.', File.separatorChar).replaceAll("::", File.separator);
		String filename = r.getName()+".java";
		File fileRule;
		if(subrepcat.isEmpty()) {
			fileRule = new File(pathModeler, filename);
		}
		else {
			File pathRule = new File(pathModeler, subrepcat);
			System.out.println("DEST DIR RULE: "+pathRule.getAbsolutePath());

			if(!pathRule.exists()) {
				System.out.println("PATH: "+pathRule.getAbsolutePath()+" doesn't exist");
				boolean res = pathRule.mkdirs();
				System.out.println(" DEST DIR CREATION: "+res);
			}
			fileRule = new File(pathRule, filename);
		}
		System.out.println("PATH RULE: "+fileRule.getAbsolutePath());
		exportRuleCommon(fileRule, r);

	}

	private static void exportRuleCommon(File fileRule, JMERule r) {
		System.out.println("CURRENT DIR: "+ System.getProperty("user.dir"));
		System.out.println("\tExporting rule: "+r.getFullName()+" to "+fileRule);
		System.out.println("\tabsolute path: "+fileRule.getAbsolutePath());
		// Pair<String, String> header = null, preprocess = null, precondition = null, nodeprecondition = null,
		// midprocess = null, postprocess = null;
		GeneratedLanguage header = null, preprocess = null,
				precondition = null, nodeprecondition = null,
				midprocess = null, postprocess = null;


		if(r.getHeader() != null && !r.getHeader().isEmpty()) {
			try {
				header = Translator.translate(r.getHeader(), new JerboaLanguageGlue(r, LanguageState.HEADER), new TranslatorContextV2(r.getModeler()), new GeneratedLanguage(),
						r.getModeler(), ExportLanguage.JAVA);

			} catch (Exception e) {
				// header = new Pair<String, String>("#ERROR in header expression translation # "+e, "#ERROR in header
				// expression translation # "+e);
				System.err.println(e);
			}
		}

		if(r.getPreProcess() != null && !r.getPreProcess().trim().isEmpty()) {
			System.out.println("PREPROCESS: "+r.getPreProcess());

			try {
				preprocess = Translator.translate(r.getPreProcess(),
						new JerboaLanguageGlue(r, LanguageState.PREPROCESS), new TranslatorContextV2(r.getModeler()), new GeneratedLanguage(), r.getModeler(),
						ExportLanguage.JAVA);

			} catch (Exception e) {
				// preprocess = new Pair<String, String>("#ERROR in expression translation # "+e,"#ERROR in expression
				// translation # "+e);
				System.err.println(e);
			}
		}

		if(r.getPrecondition() != null && !r.getPrecondition().isEmpty()) {
			try {
				precondition = Translator.translate(r.getPrecondition(),
						new JerboaLanguageGlue(r, LanguageState.PRECONDITION), new TranslatorContextV2(r.getModeler()), new GeneratedLanguage(), r.getModeler(),
						ExportLanguage.JAVA);
			} catch (Exception e) {
				// precondition = new Pair<String, String>("#ERROR in precondition translation # "+e, "#ERROR in
				// precondition translation # "+e);
				System.err.println(e);
			}
		}

		/* try {
			nodeprecondition = Translator.translate(r.getPreProcess(), new JerboaLanguageGlue(r, LanguageState.PRECONDITION),	ExportLanguage.JAVA);

		} catch (Exception e) {
			nodeprecondition = new Pair<String, String>("#ERROR in node-precondition translation # "+e,
					"#ERROR in node-precondition translation # "+e);
			System.err.println(e);
		} */

		if(r.getMidProcess() != null && !r.getMidProcess().isEmpty()) {
			System.out.println("MIDPROCESS: "+r.getMidProcess());
			try {
				midprocess = Translator.translate(r.getMidProcess(),
						new JerboaLanguageGlue(r, LanguageState.MIDPROCESS), new TranslatorContextV2(r.getModeler()), new GeneratedLanguage(), r.getModeler(),
						ExportLanguage.JAVA);
			} catch (Exception e) {
				// midprocess = new Pair<String, String>("#ERROR in midprocess translation # "+e, "#ERROR in midprocess
				// translation # "+e);
				System.err.println(e);
			}
		}


		if(r.getPostProcess() != null && !r.getPostProcess().isEmpty()) {
			try {
				postprocess = Translator.translate(r.getPostProcess(),
						new JerboaLanguageGlue(r, LanguageState.POSTPROCESS), new TranslatorContextV2(r.getModeler()), new GeneratedLanguage(), r.getModeler(),
						ExportLanguage.JAVA);
			} catch (Exception e) {
				// postprocess = new Pair<String, String>("#ERROR in postprocess translation # "+e, "#ERROR in
				// postprocess translation # "+e);
				System.err.println(e);
			}
		}


		// ===========================================================================



		final JMEModeler modeler = r.getModeler();
		final String ruleName = r.getName();


		StringBuilder importStream = new StringBuilder();
		StringBuilder rulestream = new StringBuilder();

		// Debut des imports
		importStream.append("package ");
		importStream.append(genRulePackage(modeler, r));
		importStream.append(";\n\n");

		importStream.append(basicImport);
		if(isRebuildFramework) {
			importStream.append("import up.jerboa.rebuild.core.*;\n");
		}



		importStream.append("import ").append(genModImport(modeler)).append(";\n");

		// TODO: valentin tu dois harmoniser la gen C++ avec le nommage java comme convenu
		for (JMEEmbeddingInfo ebd : modeler.getEmbeddings()) {
			String type = ebd.getType().replaceAll("::", "\\.");
			if (!type.isEmpty() && !isPrimaryType(ebd.getType())) {
				importStream.append("import " + ebd.getType() + ";\n");
			}
		}

		if(header != null) {
			importStream.append(" // BEGIN HEADER IMPORT\n");
			importStream.append(header.getInclude()).append("\n");
			importStream.append(" // END HEADER IMPORT\n");
		}

		/* end imports **/

		/*
		 * Imports specifiques des scripts! -> detection des regles utilisee
		 * etc.
		 */
		if (r instanceof JMEScript) {
			importStream.append(getScriptImport((JMEScript) r));
		}

		rulestream.append("\n\n");

		rulestream.append("\n/**\n * ");
		rulestream.append(r.getComment().replace("\n", "\n * "));
		rulestream.append("\n */\n\n");

		rulestream.append("\n\n");

		// class definition
		rulestream.append("public class ").append(r.getName()).append(" extends ");
		if(r instanceof JMERuleAtomic) {
			if(isRebuildFramework)
				rulestream.append("JerboaRuleAtomicRebuild");
			else
				rulestream.append("JerboaRuleGenerated");
		}
		else
			rulestream.append("JerboaRuleScript");
		rulestream.append(" {\n\n");

		// definition of extra fields
		rulestream.append(TAB).append("private transient JerboaRowPattern curleftPattern;\n\n");

		rulestream.append("\n\t// BEGIN PARAMETERS Transformed \n\n");
		for (JMEParamEbd e : r.getParamsEbd()) {
			rulestream.append("\tprotected " + transforEbdType(e.getType(),modeler) + " " + e.getName() + "; \n");
		}
		rulestream.append("\n\t// END PARAMETERS \n\n");

		if(header != null) {
			rulestream.append(" // BEGIN COPY PASTE OF HEADER\n");
			rulestream.append(header.getInClassPrivate() + "\n");
			rulestream.append(header.getInClassPublic() + "\n");
			rulestream.append(header.getContent() + "\n");
			rulestream.append(" // END COPY PASTE OF HEADER\n");
		}
		rulestream.append("\n\n");


		/* Specific rule print **/

		exportConstructorAdditionalFunctions(r, rulestream, header);

		if (r instanceof JMEScript)
			exportScriptContent((JMEScript) r, rulestream);
		/* END rule print **/

		// BEGIN FONCTION PREPROCESS
		if(preprocess != null) {
			rulestream.append(TAB);
			rulestream.append("@Override\n");
			rulestream.append(TAB);
			rulestream.append("public boolean hasPreprocess() { return true; }\n");
			rulestream.append(TAB);
			rulestream.append("@Override\n");
			rulestream.append(TAB);
			rulestream.append("public boolean preprocess(JerboaGMap gmap, JerboaInputHooks hooks) throws JerboaException {\n");
			importStream.append(preprocess.getInclude()); // TODO: Val : changer pour mettre le bon contenu
			rulestream.append("\t// BEGIN PREPROCESS CODE\n");
			rulestream.append(preprocess.getContent());
			rulestream.append("\n\t// END PREPROCESS CODE\n");
			rulestream.append(TAB);
			rulestream.append("}");
			rulestream.append("\n\n");
		} // end gen preprocess
		// END FONCTION PREPROCESS

		// BEGIN FONCTION MIDPROCESS
		if(midprocess != null) {
			rulestream.append(TAB);
			rulestream.append("@Override\n");
			rulestream.append(TAB);
			rulestream.append("public boolean hasMidprocess() { return true; }\n");
			rulestream.append(TAB);
			rulestream.append("@Override\n");
			rulestream.append(TAB);
			rulestream.append("public boolean midprocess(JerboaGMap gmap, List<JerboaRowPattern> leftPattern) throws JerboaException {\n");
			importStream.append(midprocess.getInclude()); // TODO: Val : changer pour mettre le bon contenu
			rulestream.append("\t// BEGIN MIDPROCESS CODE\n");
			rulestream.append(midprocess.getContent());
			rulestream.append("\n\t// END MIDPROCESS CODE\n");
			rulestream.append(TAB);
			rulestream.append("}");
			rulestream.append("\n\n");
		} // end gen preprocess

		// END FONCTION MIDPROCESS

		// BEGIN FONCTION POSTPROCESS
		if(postprocess != null) {
			rulestream.append(TAB);
			rulestream.append("@Override\n");
			rulestream.append(TAB);
			rulestream.append("public boolean hasPostprocess() { return true; }\n");
			rulestream.append(TAB);
			rulestream.append("@Override\n");
			rulestream.append(TAB);
			rulestream.append("public void postprocess(JerboaGMap gmap, JerboaRuleResult res) throws JerboaException {\n");
			importStream.append(postprocess.getInclude()); // TODO: Val : changer pour mettre le bon contenu
			rulestream.append("\t// BEGIN POSTPROCESS CODE\n");
			rulestream.append(postprocess.getContent());
			rulestream.append("\n\t// END POSTPROCESS CODE\n");
			rulestream.append(TAB);
			rulestream.append("}");
			rulestream.append("\n\n");
		} // end gen postprocess
		// END FONCTION POSTPROCESS

		final List<JMENode> leftGraph = r.getLeft().getNodes();

		if (precondition != null) {
			rulestream.append(TAB);
			rulestream.append("@Override\n");
			rulestream.append(TAB);
			rulestream.append("public boolean hasPrecondition() { return true; }\n");
			rulestream.append(TAB);
			rulestream.append("public boolean evalPrecondition(final JerboaGMap gmap, final List<JerboaRowPattern> leftPattern) throws JerboaException {\n\n");
			importStream.append(precondition.getInclude()); // TODO: Val : changer pour mettre le bon contenu
			rulestream.append(TAB).append(TAB).append(TAB).append("// BEGIN PRECONDITION CODE\n");
			rulestream.append(precondition.getContent());
			rulestream.append(TAB).append(TAB).append(TAB).append("// END PRECONDITION CODE\n");
			rulestream.append("}\n\n");
		}

		// Facility for accessing to the dart
		if (leftGraph.size() > 0) {
			rulestream.append(TAB).append("// Facility for accessing to the dart\n");
			for (int i = 0; i < leftGraph.size(); i++) {
				JMENode n = leftGraph.get(i);
				String name = n.getName();
				if (!Character.isJavaIdentifierStart(name.charAt(0)))
					name = "_" + name;
				rulestream.append(TAB).append("private JerboaDart ").append(name).append("() {\n").append(TAB).append(TAB)
				.append("return ").append("curleftPattern.getNode(").append(n.getID()).append(");\n").append(TAB)
				.append("}\n\n");
			}
		}

		// getters and setters
		for (JMEParamEbd pebd : r.getParamsEbd()) {
			String ptype = transforEbdType(pebd.getType().replaceAll("::", "\\."),modeler);
			rulestream.append("\tpublic " + ptype + " get" + nameToProperty(pebd.getName()) + "()");
			rulestream.append("{\n\t\treturn " + pebd.getName()  + ";\n\t}\n");

			rulestream.append("\tpublic void set" + nameToProperty(pebd.getName()) + "(" + ptype + " _" + pebd.getName() + "){\n");
			rulestream.append("\t\tthis." + pebd.getName() + " = _" + pebd.getName() + ";\n\t}\n");
		}

		rulestream.append("} // end rule Class");

		FileOutputStream ruleStreamOUT = null;

		try {
			// fileRule.createNewFile();
			ruleStreamOUT = new FileOutputStream(fileRule.getAbsolutePath());
			ruleStreamOUT.write((importStream.toString()).getBytes());
			ruleStreamOUT.write((rulestream.toString()).getBytes());
			ruleStreamOUT.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String nameToProperty(String name) {
		String s = name.substring(0, 1).toUpperCase() + name.substring(1);
		return s;
	}

	private static void exportConstructorAdditionalFunctions(JMERule rule, StringBuilder buffer, GeneratedLanguage header) {
		final ArrayList<JMENodeExpression> expressions = new ArrayList<JMENodeExpression>();

		JMEModeler modeler = rule.getModeler();
		int dim = modeler.getDimension();

		// package
		StringBuilder leftJerboaEdge = new StringBuilder();
		StringBuilder leftJerboaNode = new StringBuilder();
		StringBuilder rightJerboaNodeAndEdge = new StringBuilder();
		StringBuilder hookJerboaNode = new StringBuilder();

		buffer.append(TAB);
		buffer.append("public ");
		buffer.append(rule.getName());
		buffer.append("(").append(modeler.getName()).append(" modeler) throws JerboaException {\n\n");

		buffer.append(TAB);
		buffer.append(TAB);
		buffer.append("super(modeler, \"");
		buffer.append(rule.getName());
		buffer.append("\", \"");
		buffer.append(rule.getCategory());
		buffer.append("\");\n\n");

		List<JMENode> leftGraph = rule.getLeft().getNodes();
		List<JMENode> rightGraph = rule.getRight().getNodes();

		// left graph
		buffer.append(TAB).append(TAB).append("// -------- LEFT GRAPH\n");

		for (JMENode n : leftGraph) {
			String name = n.getName();

			// left JerboaNodes
			buffer.append(TAB).append(TAB);
			buffer.append("JerboaRuleNode l").append(name);
			buffer.append(" = new JerboaRuleNode(\"").append(name);
			buffer.append("\", ").append(n.getID()).append(", JerboaOrbit.orbit(");
			for (int j = 0; j < n.getOrbit().size(); j++) {
				int orbit = n.getOrbit().get(j);
				if (j > 0)
					buffer.append(",");
				buffer.append(String.valueOf(orbit));
			}
			buffer.append("), ").append(dim);
			buffer.append(");\n");

			if(n.getPrecondition() != null && !n.getPrecondition().isEmpty()) {
				GeneratedLanguage precondition = new GeneratedLanguage();
				try {
					precondition = Translator.translate(n.getPrecondition(),
							new JerboaLanguageGlue(rule, LanguageState.PRECONDITION), new TranslatorContextV2(modeler), precondition, modeler,
							ExportLanguage.JAVA);
				} catch (Exception e) {
					// precondition = new Pair<String, String>("#ERROR in node-precondition translation # "+e, "#ERROR
					// in node-precondition translation # "+e);
					System.err.println(e);
				}
				buffer.append("l").append(name).append(".")

				.append("setNodePrecondition(new JerboaNodePrecondition() { ")
				.append("public boolean eval(JerboaGMap gmap, JerboaRowPattern row) {")
				.append(precondition.getInclude()).append("\n").append(precondition.getContent())
				.append("} });\n");
			}

			// left JerboaNodes list
			leftJerboaNode.append(TAB);
			leftJerboaNode.append(TAB);
			leftJerboaNode.append("left.add(l").append(n.getName());
			leftJerboaNode.append(");\n");

			// hook list
			if (n.getKind() == JMENodeKind.HOOK) {
				hookJerboaNode.append(TAB);
				hookJerboaNode.append(TAB);
				hookJerboaNode.append("hooks.add(l").append(n.getName());
				hookJerboaNode.append(");\n");
			}
		}

		for (JMEArc arc : rule.getLeft().getArcs()) {
			leftJerboaEdge.append(TAB).append(TAB);
			leftJerboaEdge.append("l").append(arc.getSource().getName());
			leftJerboaEdge.append(".setAlpha(").append(arc.getDimension());
			leftJerboaEdge.append(", l").append(arc.getDestination().getName()).append(");\n");
		}

		buffer.append(leftJerboaNode.toString());
		buffer.append(hookJerboaNode.toString());
		buffer.append(leftJerboaEdge.toString());

		buffer.append("\n").append(TAB).append(TAB).append("// -------- RIGHT GRAPH\n");

		// right graph
		for (JMENode n : rightGraph) {
			String name = n.getName();
			// right JerboaNodes

			buffer.append(TAB).append(TAB);
			buffer.append("JerboaRuleNode r").append(name);
			buffer.append(" = new JerboaRuleNode(\"").append(name);
			buffer.append("\", ").append(n.getID()).append(", JerboaOrbit.orbit(");

			for (int j = 0; j < n.getOrbit().size(); j++) {
				int orbit = n.getOrbit().get(j);
				if (j > 0)
					buffer.append(",");
				buffer.append(String.valueOf(orbit));
			}
			buffer.append("), ").append(dim);

			ArrayList<JMENodeExpression> allexprs = new ArrayList<>();
			allexprs.addAll(n.getExplicitExprs());
			allexprs.addAll(n.getDefaultExprs());
			for (JMENodeExpression expr : allexprs) {
				buffer.append(", new ");
				buffer.append(rule.getName());
				buffer.append("ExprR");
				buffer.append(name);
				buffer.append(expr.getEbdInfo().getName());
				buffer.append("()");
				expressions.add(expr);
			}
			buffer.append(");\n");

			// right JerboaNodes list

			rightJerboaNodeAndEdge.append(TAB);
			rightJerboaNodeAndEdge.append(TAB);
			rightJerboaNodeAndEdge.append("right.add(r").append(n.getName());
			rightJerboaNodeAndEdge.append(");\n");

		}

		for (JMEArc arc : rule.getRight().getArcs()) {
			rightJerboaNodeAndEdge.append(TAB);
			rightJerboaNodeAndEdge.append(TAB);
			rightJerboaNodeAndEdge.append("r").append(arc.getSource().getName());
			rightJerboaNodeAndEdge.append(".setAlpha(").append(arc.getDimension());
			rightJerboaNodeAndEdge.append(", r").append(arc.getDestination().getName()).append(");\n");
		}


		buffer.append(rightJerboaNodeAndEdge.toString()+";\n");

		if(rule instanceof JMERuleAtomic) {
			buffer.append(TAB).append(TAB).append("// ------- SPECIFIED FEATURE\n");
			buffer.append(TAB);
			buffer.append(TAB);
			buffer.append("computeEfficientTopoStructure();\n");
			buffer.append(TAB);
			buffer.append(TAB);
			buffer.append("computeSpreadOperation();\n");
		}

		buffer.append(TAB).append(TAB).append("// ------- USER DECLARATION \n");

		if(header != null) {
			buffer.append(" // BEGIN COPY PASTE OF HEADER\n");
			buffer.append(header.getInClassConstructor() + "\n");
			buffer.append(" // END COPY PASTE OF HEADER\n");
		}

		for (JMEParamEbd param : rule.getParamsEbd()) {
			if(param.getInitValue() != null && !param.getInitValue().isEmpty())
				buffer.append(TAB).append(TAB).append(param.getName()).append(" = ").append(transforEbdType(param.getInitValue(),modeler)).append(";");
		}

		buffer.append(TAB);
		buffer.append("}\n\n");


		// FONCTIONS ADDITIONNELLES POUR OPTIMISATION

		//buffer = new StringBuilder(TAB);
		buffer.append(TAB);
		buffer.append("public int reverseAssoc(int i) {\n");

		if (rightGraph.size() > 0) {
			buffer.append(TAB).append(TAB);
			buffer.append("switch(i) {\n");
		}
		for (JMENode rightNode : rightGraph) {
			JMENode leftNode = searchMatchLeftNode(rightNode);
			buffer.append(TAB).append(TAB);
			buffer.append("case ");
			buffer.append(rightNode.getID());
			buffer.append(": return ");
			if (leftNode != null)
				buffer.append(leftNode.getID());
			else
				buffer.append(-1);
			buffer.append(";\n");
		}
		if (rightGraph.size() > 0) {
			buffer.append(TAB).append(TAB);
			buffer.append("}\n");
		}

		buffer.append(TAB).append(TAB);
		buffer.append("return -1;\n");
		buffer.append(TAB);
		buffer.append("}\n\n");

		buffer.append(TAB);
		buffer.append("public int attachedNode(int i) {\n");
		if (rightGraph.size() > 0) {
			buffer.append(TAB).append(TAB);
			buffer.append("switch(i) {\n");
		}
		for (JMENode rightNode : rightGraph) {
			JMENode leftNode = searchRefHook(rightNode);
			buffer.append(TAB).append(TAB);
			buffer.append("case ");
			buffer.append(rightNode.getID());
			buffer.append(": return ");
			if (leftNode != null)
				buffer.append(leftNode.getID());
			else
				buffer.append(-1);
			buffer.append(";\n");
		}
		if (rightGraph.size() > 0) {
			buffer.append(TAB).append(TAB);
			buffer.append("}\n");
		}
		buffer.append(TAB).append(TAB);
		buffer.append("return -1;\n");
		buffer.append(TAB);
		buffer.append("}\n\n");

		/* Surcharge pour faire les appels au preprocess */
		boolean doPreprocess = rule.getPreProcess() != null && !rule.getPreProcess().isEmpty();
		boolean doPostprocess = rule.getPostProcess() != null && !rule.getPostProcess().isEmpty();
		if(doPreprocess || doPostprocess) {
			buffer.append(TAB).append("@Override\n");
			buffer.append(TAB).append("public JerboaRuleResult applyRule(JerboaGMap gmap, JerboaInputHooks _hooks) throws JerboaException {\n");
			if(doPreprocess)
				buffer.append(TAB).append(TAB).append("preprocess(gmap, _hooks);\n");
			buffer.append(TAB).append(TAB).append("JerboaRuleResult res = super.applyRule(gmap, _hooks);\n");
			if(doPostprocess)
				buffer.append(TAB).append(TAB).append("postprocess(gmap,res);\n");
			buffer.append(TAB).append(TAB).append("return res;\n");
			buffer.append(TAB).append("}\n");
		}


		exportDediApplyRule(rule, buffer);

		// FIN FONCTIONS ADDITIONNELLES POUR OPTIMISATION



		for (JMENodeExpression expr : expressions) {
			makeEbdComputationClass(rule, expr, buffer);

		}

	}

	private static void makeEbdComputationClass(JMERule rule, JMENodeExpression expr,
			StringBuilder buffer) {
		JMEModeler modeler = rule.getModeler();
		String name = expr.getNode().getName();
		buffer.append(TAB);
		buffer.append("private class ");
		buffer.append(rule.getName());
		buffer.append("ExprR");
		buffer.append(name);
		buffer.append(expr.getEbdInfo().getName());
		buffer.append(" implements JerboaRuleExpression {\n\n");

		buffer.append(TAB).append(TAB);
		buffer.append("@Override\n");
		buffer.append(TAB).append(TAB);
		buffer.append(
				"public Object compute(JerboaGMap gmap, JerboaRuleOperation rule,JerboaRowPattern leftPattern, JerboaRuleNode rulenode) throws JerboaException {\n");
		// Affectation of extra field
		// String vtype = expr.getEbdInfo().getType();
		// buffer.append(TAB).append(TAB).append(TAB).append(vtype).append(" value = null;\n");
		buffer.append(TAB).append(TAB).append(TAB).append("curleftPattern = leftPattern;\n");
		buffer.append("// ======== BEGIN CODE TRANSLATION FOR EXPRESSION COMPUTATION\n");
		buffer.append(TAB).append(TAB).append(TAB);

		String expression = expr.getExpression();
		GeneratedLanguage computeEbd = new GeneratedLanguage();
		try {
			computeEbd = Translator.translate(expression, new JerboaLanguageGlue(rule, LanguageState.CLASSICAL), new TranslatorContextV2(modeler),
					computeEbd, modeler, ExportLanguage.JAVA);
		} catch (Exception e) {
			// computeEbd = new Pair<String, String>("", "Error: "+e);
			System.err.println(e);
		}
		buffer.append(computeEbd.getInclude()); // TODO: Val : changer pour mettre le bon contenu
		buffer.append("// ======== SEPARATION CODE TRANSLATION FOR EXPRESSION COMPUTATION\n");
		buffer.append(computeEbd.getContent());
		buffer.append("// ======== END CODE TRANSLATION FOR EXPRESSION COMPUTATION\n");
		buffer.append(TAB).append(TAB);
		buffer.append("}\n\n");

		buffer.append(TAB).append(TAB);
		buffer.append("@Override\n");
		buffer.append(TAB).append(TAB);
		buffer.append("public String getName() {\n");
		buffer.append(TAB).append(TAB).append(TAB);
		buffer.append("return \"").append(expr.getEbdInfo().getName()).append("\";\n");
		buffer.append(TAB).append(TAB);
		buffer.append("}\n\n");
		// buffer.append(TAB);
		// buffer.append("}\n\n");

		buffer.append(TAB).append(TAB);
		buffer.append("@Override\n");
		buffer.append(TAB).append(TAB);
		buffer.append("public int getEmbedding() {\n");
		buffer.append(TAB).append(TAB).append(TAB);
		buffer.append("return ((").append(modeler.getName()).append(")modeler).get").append(nameToProperty(expr.getEbdInfo().getName())).append("().getID();\n");
		// buffer.append("return modeler.get").append(nameToProperty(expr.getEbdInfo().getName())).append("().getID();\n");
		buffer.append(TAB).append(TAB);
		buffer.append("}\n");
		buffer.append(TAB);
		buffer.append("}\n\n");
	}

	private static void exportDediApplyRule(JMERule rule, StringBuilder buffer) {
		/* D√©finition du applyRule avec les param√®tres ordonn√©s */
		StringBuilder enteteApply = new StringBuilder();
		StringBuilder contentApply = new StringBuilder();
		enteteApply.append(TAB).append("public JerboaRuleResult ");
		enteteApply.append("applyRule(JerboaGMap gmap");
		contentApply.append(TAB).append(TAB).append("JerboaInputHooksGeneric ____jme_hooks = new JerboaInputHooksGeneric();\n");

		ArrayList<JMEParamTopo> topos = new ArrayList<>(rule.getParamsTopo());
		topos.sort(new Comparator<JMEParamTopo>() {
			@Override
			public int compare(JMEParamTopo o1, JMEParamTopo o2) {
				return Integer.compare(o1.getOrder(), o2.getOrder());
			}
		});

		for (JMEParamTopo tp : topos) {

			enteteApply.append(", JerboaDart ");
			enteteApply.append(tp.getNode().getName());

			contentApply.append(TAB).append(TAB).append("____jme_hooks.addCol(").append(tp.getNode().getName()).append(");\n");
		}

		ArrayList<JMEParamEbd> params = new ArrayList<>(rule.getParamsEbd());
		params.sort(new Comparator<JMEParamEbd>() {
			@Override
			public int compare(JMEParamEbd o1, JMEParamEbd o2) {
				return Integer.compare(o1.getOrder(), o2.getOrder());
			}
		});
		for (JMEParamEbd ep : params) {
			enteteApply.append(", ");
			enteteApply.append(transforEbdType(ep.getType(),rule.getModeler())).append(" ").append(ep.getName());
			contentApply.append(TAB).append(TAB).append("set" + nameToProperty(ep.getName()) + "(" + ep.getName() + ");\n");
		}
		enteteApply.append(") throws JerboaException {\n");
		contentApply.append(TAB).append(TAB).append("return applyRule(gmap, ____jme_hooks);");


		buffer.append(enteteApply.toString());
		buffer.append(contentApply.toString());
		buffer.append("\n\t}\n\n");

		/* Fin de la d√©finition du applyRule */
	}

	private static void exportScriptContent(final JMEScript script, StringBuilder buffer) {
		GeneratedLanguage code = new GeneratedLanguage();
		try {
			code = Translator.translate(script.getContent(), new JerboaLanguageGlue(script, LanguageState.CLASSICAL), new TranslatorContextV2(script.getModeler()),
					code, script.getModeler(), ExportLanguage.JAVA);
		} catch (Exception e) {
			//code = new Pair<String, String>("#ERROR in expression translation # ",
			//"#ERROR in expression translation # ");
			code.appendContent("/*");
			code.appendContent(e.getStackTrace());
			code.appendContent("*/");
			e.printStackTrace();
		}

		// DEBUT FONCTION APPLY PAR DEFAUT DANS LES SCRIPTS
		buffer.append("@Override\n");
		buffer.append(TAB);
		buffer.append("public JerboaRuleResult apply(final JerboaGMap gmap, final JerboaInputHooks hooks) throws JerboaException {\n");

		if(script.getPostProcess() != null && !script.getPostProcess().isEmpty())
			buffer.append(TAB).append(TAB).append("JerboaRuleResult __result = new JerboaRuleResult(this);\n");

		if(script.getPreProcess() != null && !script.getPreProcess().isEmpty())
			buffer.append(TAB).append(TAB).append("preprocess(gmap, hooks);\n");

		//		buffer.append(code.getInclude().replaceAll("\\n", "\n\t\t")); // TODO : Val : Pas de Áa ici
		buffer.append("// BEGIN SCRIPT GENERATION\n");
		buffer.append(TAB).append(TAB);
		buffer.append(code.getContent().replaceAll("\\n", "\n\t\t"));
		buffer.append("// END SCRIPT GENERATION\n");

		//if(script.getPostProcess() != null && !script.getPostProcess().isEmpty())
		//buffer.append(TAB).append(TAB).append("postprocess(gmap, __result);\n");
		buffer.append("\n\t}\n");
		// END FONCTION APPLY PAR DEFAUT DANS LES SCRIPTS



		/* D√©finition du applyRule avec les param√®tres ordonn√©s */
		//		StringBuilder enteteApply = new StringBuilder();
		//		StringBuilder contentApply = new StringBuilder();
		//		enteteApply.append(TAB).append("public JerboaRuleResult ");
		//		enteteApply.append("apply(JerboaGMap gmap, JerboaRuleResultKind _kind");
		//		contentApply.append(TAB).append(TAB).append("JerboaInputHooksGeneric ____jme_hooks = new JerboaInputHooksGeneric();\n");
		//
		//		ArrayList<JMEParamTopo> topos = new ArrayList<>(script.getParamsTopo());
		//		topos.sort(new Comparator<JMEParamTopo>() {
		//			@Override
		//			public int compare(JMEParamTopo o1, JMEParamTopo o2) {
		//				return Integer.compare(o1.getOrder(), o2.getOrder());
		//			}
		//		});
		//
		//		for (JMEParamTopo tp : topos) {
		//			enteteApply.append(", List<JerboaDart> ");
		//			enteteApply.append(tp.getNode().getName());
		//			contentApply.append(TAB).append(TAB).append("____jme_hooks.addCol(").append(tp.getNode().getName()).append(");\n");
		//		}
		//		ArrayList<JMEParamEbd> params = new ArrayList<>(script.getParamsEbd());
		//		params.sort(new Comparator<JMEParamEbd>() {
		//
		//			@Override
		//			public int compare(JMEParamEbd o1, JMEParamEbd o2) {
		//				return Integer.compare(o1.getOrder(), o2.getOrder());
		//			}
		//		});
		//		for (JMEParamEbd ep : params) {
		//			enteteApply.append(", ");
		//			enteteApply.append(ep.getType() + " " + ep.getName());
		//			contentApply.append("\t\tset" + nameToProperty(ep.getName()) + "(" + ep.getName() + ");\n");
		//		}
		//		enteteApply.append(") throws JerboaException  {\n");
		//		contentApply.append(TAB).append(TAB).append("return apply(gmap, ____jme_hooks, _kind);");
		//
		//		buffer.append(enteteApply.toString());
		//		buffer.append(contentApply.toString());
		//		buffer.append("\n\t}\n\n");


		/* Fin de la d√©finition du applyRule */
	}

	private static String getScriptImport(JMEScript script) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		/* ** FIND RULE TO IMPORT ** */
		GeneratedLanguage parsed = new GeneratedLanguage();
		try {
			parsed = Translator.translate(script.getContent(), new JerboaLanguageGlue(script, LanguageState.CLASSICAL), new TranslatorContextV2(script.getModeler()),
					parsed, script.getModeler(), ExportLanguage.JAVA);
		} catch (Exception e) {
			// parsed = new Pair<String, String>("#ERROR in expression translation # ",
			// "#ERROR in expression translation # ");
			System.err.println(e);
		}
		// String parsedContent = parsed.getContent();
		String parsedHeader = parsed.getInclude();

		//ArrayList<String> listOfImportedRule = new ArrayList<String>();

		// Val : j'ai fais les import automatique lors de la gÈnÈration donc plus besoin de Áa
		//		Pattern patternAtRule = Pattern.compile("@rule<([a-zA-Z_][a-zA-Z0-9_]*)>");
		//		Matcher matcher = patternAtRule.matcher(script.getContent());
		//		System.out.println("IN SCRIPT "+script.getFullName());
		//
		//		while(matcher.find()) {
		//			String tmpS = matcher.group(1);
		//			System.out.print("\t- FIND <"+tmpS+">");
		//		
		//			boolean alreadyImported = false;
		//			for (String si : listOfImportedRule) {
		//				if (si.compareTo(tmpS) == 0) {
		//					alreadyImported = true;
		//				}
		//			}
		//			
		//			if (!alreadyImported) {
		//				System.out.println(" FIRST REGISTRATION");
		//				listOfImportedRule.add(tmpS);
		//				for (JMERule ruli : script.getModeler().getRules()) {
		//					if (ruli.getName().compareTo(tmpS) == 0 && ruli.check()) {
		//
		//						sb.append("import ") .append(ruli.getModeler().getModule()).append(".").append(ruli.getFullName()).append(";\n");
		//					}
		//				}
		//			}
		//			else {
		//				System.out.println(" ALREADY REGISTERED");
		//			}
		//
		//		}

		/* version Valentin qui ne marche pas trop...
		String manipForImport = new String(parsedContent);
		int i = manipForImport.indexOf("modeler.getRule(\"", 1);

		while (i >= 0 && i < manipForImport.length() - 1) {
			String tmpS = manipForImport.substring(i);
			tmpS = tmpS.substring(tmpS.indexOf("\"") + 1, tmpS.length() - 1);
			tmpS = tmpS.substring(0, tmpS.indexOf("\""));

			boolean alreadyImported = false;
			for (String si : listOfImportedRule) {
				if (si.compareTo(tmpS) == 0) {
					alreadyImported = true;
				}
			}

			if (!alreadyImported) {
				listOfImportedRule.add(tmpS);
				for (JMERule ruli : script.getModeler().getRules()) {
					if (ruli.getName().compareTo(tmpS) == 0 && ruli.check()) {

						sb.append("import ") .append(ruli.getModeler().getModule()).append(".").append(ruli.getFullName()).append(";\n");
					}
				}
			}

			i = manipForImport.indexOf("owner->rule(\"", i + 1);
		}*/
		/* ** END RULE TO IMPORT ** */

		sb.append("\n/* Raw Imports : */\n").append(parsedHeader).append("\n/* End raw Imports */\n");

		return sb.toString();
	}

	private static JMENode searchRefHook(JMENode rightNode) {
		int max = rightNode.getRule().getModeler().getDimension();
		ArrayList<Integer> connex = new ArrayList<>();
		for(int i = 0;i <= max; i++) {
			connex.add(i);
		}

		Set<JMENode> nodes = rightNode.getGraph().orbit(rightNode, JerboaOrbit.orbit(connex));
		for (JMENode node : nodes) {
			JMENode leftNode = searchMatchLeftNode(node);
			if(leftNode != null && leftNode.getKind() == JMENodeKind.HOOK)
				return leftNode;
		}
		
		JMENode leftNode = searchMatchLeftNode(rightNode);
		if(leftNode != null) {
			Set<JMENode> reachableleftnodes = leftNode.getGraph().orbit(leftNode, JerboaOrbit.orbit(connex));
			for (JMENode lnode : reachableleftnodes) {
				if(lnode != null && lnode.getKind() == JMENodeKind.HOOK)
					return lnode;
			}
		}
		
		return null;
	}

	private static JMENode searchMatchLeftNode( JMENode rightNode) {
		return rightNode.getRule().getLeft().getMatchNode(rightNode);
	}

	private static String transforEbdType(String s, JMEModeler modeler) {
		String res = s;
		for(JMEEmbeddingInfo einfo: modeler.getEmbeddings()) {
			res = res.replaceAll("@ebd<"+ einfo.getName() +">", einfo.getType());
		}
		return res;
	}

}
