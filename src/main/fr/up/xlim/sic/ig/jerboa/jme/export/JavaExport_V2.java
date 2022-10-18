package fr.up.xlim.sic.ig.jerboa.jme.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import fr.up.xlim.sic.ig.jerboa.jme.model.util.preferences.JMEPreferences;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.GeneratedLanguage;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.Translator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.Translator.ExportLanguage;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeDart;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;
import up.jerboa.core.JerboaOrbit;
import up.jerboa.core.util.Pair;
import up.jerboa.util.Triplet;

public class JavaExport_V2 {

	private static ExportLanguage exportLanguage = ExportLanguage.JAVA_V2;

	private static String DIRECTORY = "src";

	private static final String TAB = "    ";

	private static final String baseImport = "\nimport up.jerboa.core.JerboaModeler;\r\n" + 
			"import up.jerboa.core.JerboaGMapArray;\r\n" + 
			"import up.jerboa.core.JerboaRuleOperation;\r\n" + 
			"import up.jerboa.util.serialization.jba.JBAFormat;\r\n" + 
			"import up.jerboa.core.JerboaDart;\r\n" + 
			"import up.jerboa.core.JerboaEmbeddingInfo;\r\n" + 
			"import up.jerboa.core.JerboaOrbit;\r\n" + 
			"import up.jerboa.exception.JerboaException;\n";


	private static String nameToProperty(String name) {
		String s = name.substring(0, 1).toUpperCase() + name.substring(1);
		return s;
	}

	private static String createSerializer(JMEModeler modeler) {
		String modelerDirectory = getModelerDirectory(modeler, DIRECTORY+"/"+getModelerModulePath(modeler, "/tools/"));

		System.out.println("Serializer generated at " + modelerDirectory);

		try {
			String serialiserName = "Serializer_"+modeler.getName();
			File serialiserFileJAVA = new File(modelerDirectory, serialiserName + ".java");
			(new File(modelerDirectory)).mkdirs();
			if (!serialiserFileJAVA.exists()) {
				System.out.println("Serializer created at : " + serialiserFileJAVA.getPath());

				InputStream inputStreamJAVA = (InputStream) System.class.getResource("/templateFiles/java/serializer.jjava").getContent();
				String serialiserJAVA = new BufferedReader(new InputStreamReader(inputStreamJAVA)).lines()
						.parallel().collect(Collectors.joining("\n"));

				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$MODELER_PACKAGE", getModelerDestModulePath(modeler, "").replaceAll("/", "."));
				
				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$SERIALIZER_PACKAGE", getModelerModuleNamespace(modeler, ".tools"));

				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$SERIALIZER_NAME", serialiserName);

				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$MODELER_NAME", modeler.getName());

				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$MODELER_DIMENSION", modeler.getDimension()+"");
				
				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$EBD_INCLUDE", getEbdIncludes(modeler));

				String unserializeTest = getEbdUnserializeTest(modeler).replaceAll("\n", "\n\t");
				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$EBD_UNSERIALIZE", unserializeTest);
//				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$EBD_BINARY_UNSERIALIZE", unserializeTest);

				String serializeTest = getEbdSerializeTest(modeler).replaceAll("\n", "\n\t");
				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$EBD_SERIALIZE", serializeTest);
//				serialiserJAVA = serialiserJAVA.replaceAll("\\$\\$EBD_BINARY_SERIALIZE", serializeTest);
				
				(new File(modelerDirectory)).mkdirs();
				serialiserFileJAVA.createNewFile();
				FileOutputStream serialiserFileStreamJAVA = new FileOutputStream(serialiserFileJAVA);
				serialiserFileStreamJAVA.write((serialiserJAVA).getBytes());
				serialiserFileStreamJAVA.close();
			}
			return serialiserName;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return "ERROR_SERIALIZER_NOT_EXPORTED";
	}

	private static String createBridge(JMEModeler modeler, String serializerType) {

		String modelerDirectory = getModelerDirectory(modeler, DIRECTORY+"/"+getModelerModulePath(modeler, "/tools/"));

		System.out.println("Bridge generated at " + modelerDirectory);

		try {
			String bridgeName = "Bridge_"+modeler.getName();
			File bridgeFileJAVA = new File(modelerDirectory, bridgeName + ".java");
			(new File(modelerDirectory)).mkdirs();
			if (!bridgeFileJAVA.exists()) {
				System.out.println("Bridge created at : " + bridgeFileJAVA.getPath());

				InputStream inputStreamJAVA = (InputStream) System.class.getResource("/templateFiles/java/bridge.jjava").getContent();
				String bridgeJAVA = new BufferedReader(new InputStreamReader(inputStreamJAVA)).lines()
						.parallel().collect(Collectors.joining("\n"));

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$MODELER_PACKAGE", getModelerDestModulePath(modeler, "").replaceAll("/", "."));
				
				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$BRIDGE_PACKAGE", getModelerModuleNamespace(modeler, ".tools"));

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$BRIDGE_NAME", bridgeName);

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$MODELER_COMMENT", modeler.getComment());

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$MODELER_NAME", modeler.getName());

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$SERIALIZER_TYPE", serializerType);

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$EBD_TO_STRING", getEbdToString(modeler));

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$EBD_INCLUDE", getEbdIncludes(modeler));

				String unserializeTest = getEbdUnserializeTest(modeler);
				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$EBD_UNSERIALIZE", unserializeTest);
				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$EBD_BINARY_UNSERIALIZE", unserializeTest);

				String serializeTest = getEbdSerializeTest(modeler);
				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$EBD_SERIALIZE", serializeTest);
				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$EBD_BINARY_SERIALIZE", serializeTest);

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$EBD_CLASS_NAME", getEbdClassNameTest(modeler));

				bridgeJAVA = bridgeJAVA.replaceAll("\\$\\$EBD_ID", getEbdIdTest(modeler));


				String presetPrefix = "\\$\\$EBD_PRESET";

				String[] posMatcher = {"point", "position"};
				JMEEmbeddingInfo positionPreset = getEbdInfPreset(modeler, posMatcher) ;
				if(positionPreset!=null) {
					String presetPrefixPosition = presetPrefix + "_POSITION";
					bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixPosition+"_NAME", positionPreset.getName());
					bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixPosition+"_TYPE", positionPreset.getType());	
					int cpt = 0;
					for(JMEEmbeddingInfo einf : modeler.getEmbeddings()) {
						if(einf.getName().compareTo(positionPreset.getName())==0) {
							bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixPosition+"_ID",cpt+"");
							break;
						}
						cpt++;
					}	
				}

				String[] colMatcher = {"color", "col"};
				JMEEmbeddingInfo colorPreset = getEbdInfPreset(modeler, colMatcher) ;
				if(colorPreset!=null) {
					String presetPrefixColor = presetPrefix + "_COLOR";
					bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixColor+"_NAME", colorPreset.getName());
					bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixColor+"_TYPE", colorPreset.getType());	
					int cpt = 0;
					for(JMEEmbeddingInfo einf : modeler.getEmbeddings()) {
						if(einf.getName().compareTo(colorPreset.getName())==0) {
							bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixColor+"_ID",cpt+"");
							break;
						}
						cpt++;
					}				
				}

				String[] orientMatcher = {"orient"};
				JMEEmbeddingInfo orientPreset = getEbdInfPreset(modeler, orientMatcher) ;
				if(orientPreset!=null) {
					String presetPrefixOrient = presetPrefix + "_ORIENT";
					bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixOrient+"_NAME", orientPreset.getName());
					bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixOrient+"_TYPE", orientPreset.getType());
					int cpt = 0;
					for(JMEEmbeddingInfo einf : modeler.getEmbeddings()) {
						if(einf.getName().compareTo(orientPreset.getName())==0) {
							bridgeJAVA = bridgeJAVA.replaceAll(presetPrefixOrient+"_ID",cpt+"");
							break;
						}
						cpt++;
					}		
				}

				(new File(modelerDirectory)).mkdirs();
				bridgeFileJAVA.createNewFile();
				FileOutputStream bridgeFileStreamJAVA = new FileOutputStream(bridgeFileJAVA);
				bridgeFileStreamJAVA.write((bridgeJAVA).getBytes());
				bridgeFileStreamJAVA.close();
			}
			return bridgeName;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return "ERROR_BRIDGE_NOT_EXPORTED";
	}

	private static JMEEmbeddingInfo getEbdInfPreset(JMEModeler modeler, String[] namePartsMatcher) {
		for(String namePart : namePartsMatcher) {
			for(JMEEmbeddingInfo einf : modeler.getEmbeddings()) {
				if(einf.getName().toLowerCase().contains(namePart.toLowerCase())) {
					return einf;
				}
			}
		}
		return null;
	}

	private static void createMainFile(JMEModeler modeler, String bridgeName) {
		String modelerDirectory = getModelerDirectory(modeler, DIRECTORY+"/"+getModelerModulePath(modeler, "/tools/"));

		try {
			InputStream inputStreamPRO = (InputStream) System.class.getResource("/templateFiles/java/main.jjava").getContent();

			String mainFileContent = new BufferedReader(new InputStreamReader(inputStreamPRO)).lines()
					.parallel().collect(Collectors.joining("\n"));

			File mainFile = null;
			mainFile = new File(modelerDirectory, modeler.getName() + "Launcher.java");
			if (mainFile.exists()) // on n'ecrase pas le main
				return;
			else {
				(new File(modelerDirectory)).mkdirs();
			}

			mainFileContent = mainFileContent.replaceAll("\\$\\$MAIN_HEADER", getCommentHeader(modeler));
			
			mainFileContent = mainFileContent.replaceAll("\\$\\$BRIDGE_PACKAGE", getModelerModulePath(modeler, ".tools").replaceAll("/", "."));
			
			mainFileContent = mainFileContent.replaceAll("\\$\\$BRIDGE_NAME", bridgeName);
			
			// TODO: attention! il faut faire le namespace avant !!
			mainFileContent = mainFileContent.replaceAll("\\$\\$MODELER_PACKAGE", getModelerDestModulePath(modeler, "").replaceAll("/", "."));
			
			mainFileContent = mainFileContent.replaceAll("\\$\\$MODELER_NAMESPACE", getModelerDestModulePath(modeler, ".tools").replaceAll("/", "."));

			mainFileContent = mainFileContent.replaceAll("\\$\\$MODELER_NAME", modeler.getName());

			FileOutputStream proFileStream = null;

			mainFile.createNewFile();
			proFileStream = new FileOutputStream(mainFile);
			proFileStream.write((mainFileContent.toString()).getBytes());
			proFileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createProjectFiles(JMEModeler modeler) {
		
		String modelerDirectory = getModelerDirectory(modeler, "");

		try {
			InputStream inputStreamPRO = (InputStream) System.class.getResource("/templateFiles/java/.project").getContent();

			String projectFileContent = new BufferedReader(new InputStreamReader(inputStreamPRO)).lines()
					.parallel().collect(Collectors.joining("\n"));
			File projectFile = null;
			projectFile = new File(modelerDirectory, ".project");
			if (projectFile.exists()) // on n'ecrase pas le main
				return;

			projectFileContent = projectFileContent.replaceAll("\\$\\$MODELER_NAME", modeler.getName());

			FileOutputStream proFileStream = null;
			projectFile.createNewFile();
			proFileStream = new FileOutputStream(projectFile);
			proFileStream.write((projectFileContent.toString()).getBytes());
			proFileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			InputStream inputStreamClassPath = (InputStream) System.class.getResource("/templateFiles/java/.classpath").getContent();
			File classPathFile = null;
			classPathFile = new File(modelerDirectory, ".classpath");
			
			if (classPathFile.exists()) // on n'ecrase pas le main
				return;
			String classPathFileContent = new BufferedReader(new InputStreamReader(inputStreamClassPath)).lines()
					.parallel().collect(Collectors.joining("\n"));
			
			StringBuilder builderSrcDestDir = new StringBuilder();
			if(modeler.getDestDir().length()>0) {
				builderSrcDestDir.append("<classpathentry kind=\"src\" path=\""+modeler.getDestDir()+"\"/>");
			}
			classPathFileContent = classPathFileContent.replaceAll("\\$\\$DEST_DIR", builderSrcDestDir.toString());
			
			FileOutputStream proFileStream = null;
			classPathFile.createNewFile();
			proFileStream = new FileOutputStream(classPathFile);
			proFileStream.write((classPathFileContent.toString()).getBytes());
			proFileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// On ne genere le bridge que si le .project n'existe pas.
		String bridgeName = createBridge(modeler, createSerializer(modeler));
		createMainFile(modeler, bridgeName);

	}


	private static Triplet<String, String, GeneratedLanguage> getNodeEbdExprClass(JMENodeExpression expr, JMERule rule, 
			JerboaLanguageGlue glue, TranslatorContextV2 context){
		StringBuilder builderH = new StringBuilder();
		StringBuilder builderCPP = new StringBuilder();

		GeneratedLanguage translatedExpression = new GeneratedLanguage();

		try {
			InputStream inputStreamJAVA = (InputStream) System.class.getResource("/templateFiles/java/ebdExprClass.jjava").getContent();
			String ebdExprClassJAVA = new BufferedReader(new InputStreamReader(inputStreamJAVA)).lines()
					.parallel().collect(Collectors.joining("\n"));

			ebdExprClassJAVA = ebdExprClassJAVA.replaceAll("\\$\\$RULE_NAME", rule.getName());

			ebdExprClassJAVA = ebdExprClassJAVA.replaceAll("\\$\\$RULE_NODE_NAME", expr.getNode().getName());

			ebdExprClassJAVA = ebdExprClassJAVA.replaceAll("\\$\\$RULE_EXPR_EBD_NAME", expr.getEbdInfo().getName());

			int ebdId = -1;
			int cpt=0;
			for(JMEEmbeddingInfo einf : rule.getModeler().getEmbeddings()) {
				if(einf.getName().compareTo(expr.getEbdInfo().getName()) == 0) {
					ebdId=cpt;
				}
				cpt++;
			}
			ebdExprClassJAVA = ebdExprClassJAVA.replaceAll("\\$\\$RULE_EXPR_EBD_ID", ebdId+"");


			try {
				translatedExpression = Translator.translate(expr.getExpression(), glue, context, translatedExpression, rule.getModeler(), exportLanguage);				
				ebdExprClassJAVA = ebdExprClassJAVA.replaceAll("\\$\\$EBD_EXPR_CODE", translatedExpression.getContent().replaceAll("\n", "\n\t\t"));
			} catch (Exception e) {
				e.printStackTrace();
				ebdExprClassJAVA = ebdExprClassJAVA.replaceAll("\\$\\$EBD_EXPR_CODE", "");
			}

			builderH.append(ebdExprClassJAVA);
			builderCPP.append(ebdExprClassJAVA);

		}catch(Exception e) {
			e.printStackTrace();
		}

		return new Triplet<String, String, GeneratedLanguage>(builderH.toString(), builderCPP.toString(), translatedExpression);

	}

	private static Triplet<String, String, List<GeneratedLanguage>> getAllNodeEbdExprClass(JMERule rule, TranslatorContextV2 context){
		List<GeneratedLanguage> translatedList = new ArrayList<>();
		StringBuilder contentH = new StringBuilder();
		StringBuilder contentCPP = new StringBuilder();

		for(JMENode rNode : rule.getRight().getNodes()) {
			ArrayList<JMENodeExpression> allexprs = new ArrayList<>();
			allexprs.addAll(rNode.getExplicitExprs());
			allexprs.addAll(rNode.getDefaultExprs());
			for(JMENodeExpression expr : allexprs) {
				Triplet<String, String, GeneratedLanguage> exprClass = getNodeEbdExprClass(expr, rule, new JerboaLanguageGlue(expr), context);

				contentH.append("\t");
				contentH.append(exprClass.l());
				contentH.append("\n\n");

				contentCPP.append(exprClass.m());
				contentCPP.append("\n\n");

				translatedList.add(exprClass.r());
			}			
		}		

		return new Triplet<String, String, List<GeneratedLanguage>>(contentH.toString(), contentCPP.toString(), translatedList);
	}

	private static Pair<String, String> getRuleParamArgsAndApplyContent(JMERule rule, boolean printDefaultValue) {
		StringBuilder args = new StringBuilder();
		StringBuilder content = new StringBuilder();


		for (JMEParamTopo tp : rule.getParamsTopo()) {
			args.append(", JerboaDart ");
			args.append(tp.getNode().getName());

			content.append("_hookList.addCol(");
			content.append(tp.getNode().getName());
			content.append(");\n");
		}
		for (JMEParamEbd ep : rule.getParamsEbd()) {
			for (int i = 0; i < rule.getParamsEbd().size(); i++) {
				if (ep.getOrder() == i) {
					args.append(", ");
					args.append(transforEbdType(ep.getType(), rule.getModeler()) + " " + ep.getName());
					content.append("\tset" + nameToProperty(ep.getName()) + "(" + ep.getName() + ");\n");
				}
			}
		}

		return new Pair<String, String>(args.toString(), content.toString());
	}


	private static Pair<String, GeneratedLanguage> getProcess(JMERule rule, TranslatorContextV2 context, LanguageState langState){
		StringBuilder processJAVA   = new StringBuilder();

		GeneratedLanguage translatedProcess = new GeneratedLanguage();

		String preContent = "\t@Override\n";
		String processReturnType = "";
		String processSign = "";
		String processContent = "";

		switch (langState) {
		case PREPROCESS:
			processReturnType = "public boolean ";
			processSign = "preprocess(JerboaGMap gmap, JerboaInputHooks leftPattern) throws JerboaException";
			processContent = rule.getPreProcess();
			break;
		case PRECONDITION:
			processReturnType = "public boolean ";
			processSign = "evalPrecondition(JerboaGMap gmap, JerboaRuleResult leftPattern) throws JerboaException";
			processContent = rule.getPrecondition();
			break;	
		case MIDPROCESS:
			processReturnType = "public boolean ";
			processSign = "midprocess(JerboaGMap gmap, List<JerboaRowPattern> leftPattern) throws JerboaException";
			processContent = rule.getMidProcess();
			break;
		case POSTPROCESS:
			processReturnType = "public void ";
			processSign = "postprocess(JerboaGMap gmap, JerboaRuleResult res) throws JerboaException";
			processContent = rule.getPostProcess();
			break;
		case HEADER:
			preContent="";
			try {
				translatedProcess = Translator.translate(rule.getHeader(), new JerboaLanguageGlue(rule, langState), 
						context, translatedProcess, rule.getModeler(), exportLanguage);
				return new Pair<String, GeneratedLanguage>(translatedProcess.getContent(), translatedProcess);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// si pas marche on passe dans le default
		default:
			return new Pair<String, GeneratedLanguage>("", translatedProcess);
		}


		if(processContent.length()>0) {
			try {
				translatedProcess = Translator.translate(processContent, new JerboaLanguageGlue(rule, langState), 
						context, translatedProcess, rule.getModeler(), exportLanguage);
				processJAVA.append(preContent);
				processJAVA.append("\t");
				processJAVA.append(processReturnType);
				processJAVA.append(processSign);
				processJAVA.append("{\n\t\t");
				processJAVA.append(translatedProcess.getContent().replaceAll("\n", "\n\t\t"));
				processJAVA.append("\n\t}\n");
			} catch (Exception e) {
				//			e.printStackTrace();
				processJAVA.append("\t");
				processJAVA.append(processReturnType);
				processJAVA.append(processSign);
				processJAVA.append("{return true;}\n");
			}
		}else {
			processJAVA.append("\t");
			processJAVA.append(processReturnType);
			processJAVA.append(processSign);
			switch (langState) {
			case PREPROCESS:
			case PRECONDITION:
			case MIDPROCESS:
				processJAVA.append("{return true;}\n");
				break;
			case POSTPROCESS:
				processJAVA.append("{}\n");
				break;
			case HEADER:
			default:
					break;
			}
		}
		return new Pair<String,GeneratedLanguage>(processJAVA.toString(), translatedProcess);
	}

	private static String transforEbdType(String s, JMEModeler modeler) {
		String res = s;
		for(JMEEmbeddingInfo einfo: modeler.getEmbeddings()) {
			res = res.replaceAll("@ebd<"+ einfo.getName() +">", einfo.getType());
//			System.out.println(res+" = ebd transform : " + einfo.getName() + " - " + einfo.getType());
		}
		return res.replaceAll("\\:\\:", ".");
	}

	private static String getEbdParameterDeclarations(JMERule r) {
		StringBuilder builder = new StringBuilder();
		for(JMEParamEbd param : r.getParamsEbd()) {
			builder.append("\t");
			builder.append(transforEbdType(param.getType(), r.getModeler()));
			builder.append(" ");
			builder.append(param.getName());
			if(param.getInitValue().length()>0) {
				builder.append(" = ");
				builder.append(transforEbdType(param.getInitValue(), r.getModeler()));
			}
			builder.append(";\n");
		}
		return builder.toString();
	}

	private static void exportJMERule(JMERule rule, String modelerDirectory) {
		String ruleName = rule.getName();
		String category = rule.getCategory();

		try {
			StringBuilder ruleJAVAeader = new StringBuilder();

			String ruleFileName = "";
			if(rule instanceof JMERuleAtomic) {
				ruleFileName = "atomic";
			}else if(rule instanceof JMEScript){
				ruleFileName = "script";
			}

			InputStream inputStreamJAVA = (InputStream) System.class.getResource("/templateFiles/java/" +ruleFileName+ ".jjava").getContent();
			String ruleJAVA = new BufferedReader(new InputStreamReader(inputStreamJAVA)).lines()
					.parallel().collect(Collectors.joining("\n"));

			///////////////////

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_HEADER", getCommentHeader(rule));

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$MODELER_PATH", getModelerModuleNamespace(rule.getModeler(), rule.getModeler().getName()));
			
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$MODELER_PACKAGE", getModelerDestModulePath(rule.getModeler(), "").replaceAll("/", "."));

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$MODELER_MODULE_PATH", getModelerDestModulePath(rule.getModeler(), ""));
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PATH", getRuleModule(rule));

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_NAME", ruleName);

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$MODELER_NAME", rule.getModeler().getName());

			TranslatorContextV2 ruleContext = new TranslatorContextV2(rule.getModeler());

			String headers =  getEbdIncludes(rule.getModeler());
			Pair<String, GeneratedLanguage> ruleHeader = getProcess(rule, ruleContext, LanguageState.HEADER);
			headers += ( ruleHeader.r().getInclude().length()>0?"\n":"" ) + ruleHeader.r().getInclude();
			// TODO : Ajouter le content dans la class !
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$EBD_INCLUDE", headers);
			//TODO : il faudrait enlever ce qui n'est pas necessaire et/ou en double

			JSG_2_Type typeHook = null;
			if(rule instanceof JMERuleAtomic) {
				typeHook = new JSG_2_TypeDart(-1, -1);
			}else if(rule instanceof JMEScript){
				typeHook = new JSG_2_TypeList(new JSG_2_TypeDart(-1, -1), -1, -1) ;
			}
			for(JMENode hooks : rule.getHooks()) {
				ruleContext.assignVar(hooks.getName(), typeHook, null);
			}

			//TODO : il faudrait enrichir ce context avec le context de modeleur qu'on devrait demander en parametre
			Triplet<String, String, List<GeneratedLanguage>> exprNodeClasses = getAllNodeEbdExprClass(rule, ruleContext);
			for(GeneratedLanguage gl : exprNodeClasses.getRight()) {
				ruleJAVAeader.append(gl.getInclude());
			}

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PARAMETERS", getEbdParameterDeclarations(rule));

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_EBD_EXPR_CLASSES", exprNodeClasses.l());
			// TODO : faire les includes!

			String constructorContent = printGraph(rule, (rule instanceof JMERuleAtomic));
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CONSTRUCTOR_CONTENT", constructorContent);


			Pair<String, String> paramArgAndContent = getRuleParamArgsAndApplyContent(rule, true);
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PARAM_ARGS", paramArgAndContent.l());

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PARAM_APPLY_CONTENT", paramArgAndContent.r());
			
			StringBuilder ebdParamDefaultValue = new StringBuilder();
			for(JMEParamEbd paramEbd: rule.getParamsEbd()) {
				if(paramEbd.getInitValue().length()>0) {
					ebdParamDefaultValue.append("\t\t");
					ebdParamDefaultValue.append(paramEbd.getName());
					ebdParamDefaultValue.append(" = ");
					ebdParamDefaultValue.append(transforEbdType(paramEbd.getInitValue(), rule.getModeler()));
					ebdParamDefaultValue.append(";\n");
				}
			}
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CONSTRUCTOR_PARAMETER_DEFAULT_VALUE", ebdParamDefaultValue.toString());
			

			// processes 

			Pair<String, GeneratedLanguage> preCond  = getProcess(rule, ruleContext, LanguageState.PRECONDITION);
			Pair<String, GeneratedLanguage> preProc  = getProcess(rule, ruleContext, LanguageState.PREPROCESS);
			Pair<String, GeneratedLanguage> midProc  = getProcess(rule, ruleContext, LanguageState.MIDPROCESS);
			Pair<String, GeneratedLanguage> postProc = getProcess(rule, ruleContext, LanguageState.POSTPROCESS);
			Pair<String, GeneratedLanguage> header   = getProcess(rule, ruleContext, LanguageState.HEADER);

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PRECONDITION", 		preCond.l());
			ruleJAVAeader.append(preCond.r().getInclude());

			String ruleJAVAasPrecond = "true";
			if(preCond.l().length()<=0) {
				ruleJAVAasPrecond = "false";
			}
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_HAS_PRECONDITION", 	ruleJAVAasPrecond);


			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PRE_PROCESS", 		preProc.l());

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_MID_PROCESS", 		midProc.l());
			ruleJAVAeader.append(midProc.r().getInclude());

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_POST_PROCESS", 		postProc.l());
			ruleJAVAeader.append(postProc.r().getInclude());



			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CLASS_CONTENT", 		"\t"+header.l().replaceAll("\n", "\t\n"));
			//ruleJAVAeader.append(header.l()); // TODO : plutot passer le content dans la definition de classe
			ruleJAVAeader.append(header.r().getInclude());

			// fin process


			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_COMMENT", "\""+rule.getComment().replaceAll("\n", "\\\\n").replace("\"", "\\\"") + "\"" );


			List<JMENode> rightGraph = rule.getRight().getNodes();
			List<JMENode> leftGraph  = rule.getLeft().getNodes();
			
			StringBuilder revAssoc = new StringBuilder();
			StringBuilder dartAccess = new StringBuilder();
			StringBuilder attachedNode = new StringBuilder();
			
			if(rule instanceof JMERuleAtomic) {

				for (int i = 0; i < rightGraph.size(); i++) {
					if (i == 0) {
						revAssoc.append(TAB + "switch(i) {\n");
					}
					final JMENode rightNode = rightGraph.get(i);
					final JMENode leftNode = rule.getLeft().getMatchNode(rightNode);
					if (leftNode != null) {
						revAssoc.append(TAB + "case " + i + ": return " + leftGraph.indexOf(leftNode) + ";\n");
					}
					if (i == rightGraph.size() - 1) {
						revAssoc.append(TAB + "}\n");
					}
				}

				for (int i = 0; i < leftGraph.size(); i++) {
					final JMENode n = leftGraph.get(i);
					String name = n.getName();
					if (!Character.isJavaIdentifierStart(name.charAt(0))) {
						name = "_" + name;
					}
					dartAccess.append(TAB).append("JerboaDart ").append(name).append("() {\n").append(TAB).append(TAB)
					.append("return ").append("curLeftFilter.node(").append(i).append(");\n").append(TAB)
					.append("}\n\n");
				}

				for (int i = 0; i < rightGraph.size(); i++) {
					if (i == 0) {
						attachedNode.append(TAB + "switch(i) {\n");
					}
					final JMENode rightNode = rightGraph.get(i);
					// HAK NOUVEAU MOTEUR ATTENTION CONVENTION entre attachedNode et reverseAssoc
					final JMENode leftNode = searchRefHook(rightNode); //  rule.getLeft().getMatchNode(rightNode);
					
					if (leftNode != null) {
						attachedNode.append(TAB + "case " + i + ": return " + leftNode.getID() + ";\n");
					}

					if (i == rightGraph.size() - 1) {
						attachedNode.append(TAB + "}\n");
					}
				}
			}else if(rule instanceof JMEScript) { // Script Content 
				GeneratedLanguage translatedScriptContent = new GeneratedLanguage();
				try {
					translatedScriptContent = Translator.translate(((JMEScript)rule).getContent(), new JerboaLanguageGlue(rule, LanguageState.CLASSICAL), 
							ruleContext, translatedScriptContent, rule.getModeler(), exportLanguage);
					ruleJAVA = ruleJAVA.replaceAll("\\$\\$SCRIPT_CONTENT", translatedScriptContent.getContent().replaceAll("\n", "\n\t"));
					ruleJAVAeader.append("\n"+translatedScriptContent.getInclude());
				}catch(Exception e) {
					ruleJAVA = ruleJAVA.replaceAll("\\$\\$SCRIPT_CONTENT", "");
				}
			}

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_REV_ASSOC", revAssoc.toString());
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_DART_ACCESS_H", dartAccess.toString());
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_ATTACHED_NODE", attachedNode.toString());

			// getters and setters
			StringBuilder getterSetterJAVA   = new StringBuilder();

			for (JMEParamEbd pebd : rule.getParamsEbd()) {
				String pebdName = nameToProperty(pebd.getName());

				String ptype = transforEbdType(pebd.getType(), rule.getModeler());
				getterSetterJAVA.append("public " + ptype + " get" + pebdName + "()");
				getterSetterJAVA.append("{\n\treturn " + pebd.getName() + ";\n}\n");

				getterSetterJAVA.append("public void set" +  pebdName + "(" + ptype + " _" + pebdName + ")");
				getterSetterJAVA.append("{\n\tthis." + pebd.getName() + " = _" + pebdName + ";\n}\n");
			}
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PARAMS_GETTER_SETTER", "\t" + getterSetterJAVA.toString().replaceAll("\n", "\t\n"));


			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CONSTRUCTOR_CONTENT", ""); // TODO: FILL THIS 



			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_IMPORT", ruleJAVAeader.toString());

			StringBuilder categoryListCreator = new StringBuilder();
			int cptCategory = 0;
			for (final String s : getCategoryFolder(rule.getCategory()).split("/")) {
				if(cptCategory>0) {
					categoryListCreator.append(".");
				}
				if (s.replace(" ", "") != "") {
					categoryListCreator.append(s);
				}
				cptCategory++;
			}
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CATEGORY", categoryListCreator.toString());


			///////////////////

			File classruleJAVA = null;
			FileOutputStream ruleStreamJAVA = null;

			final File cr = new File(modelerDirectory, getCategoryFolderForFile(category));
			cr.mkdirs();
			classruleJAVA = new File(modelerDirectory + getCategoryFolderForFile(category), ruleName + ".java");
			classruleJAVA.createNewFile();

			ruleStreamJAVA = new FileOutputStream(classruleJAVA);

			ruleStreamJAVA.write((ruleJAVA).getBytes());
			ruleStreamJAVA.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String exportJMERuleToString(JMERule rule, boolean overrideName, String rulename) {
		String ruleName = rule.getName();
		String category = rule.getCategory();

		try {
			StringBuilder ruleJAVAeader = new StringBuilder();

			String ruleFileName = "";
			if(rule instanceof JMERuleAtomic) {
				ruleFileName = "atomic";
			}else if(rule instanceof JMEScript){
				ruleFileName = "script";
			}

			InputStream inputStreamJAVA = (InputStream) System.class.getResource("/templateFiles/java/" +ruleFileName+ ".jjava").getContent();
			String ruleJAVA = new BufferedReader(new InputStreamReader(inputStreamJAVA)).lines()
					.parallel().collect(Collectors.joining("\n"));

			///////////////////

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_HEADER", getCommentHeader(rule));

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$MODELER_PATH", getModelerModuleNamespace(rule.getModeler(), rule.getModeler().getName()));
			
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$MODELER_PACKAGE", getModelerDestModulePath(rule.getModeler(), "").replaceAll("/", "."));

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$MODELER_MODULE_PATH", getModelerDestModulePath(rule.getModeler(), ""));
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PATH", getRuleModule(rule));

			if(overrideName)
				ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_NAME", rulename);
			else
				ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_NAME", ruleName);

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$MODELER_NAME", rule.getModeler().getName());

			TranslatorContextV2 ruleContext = new TranslatorContextV2(rule.getModeler());

			String headers =  getEbdIncludes(rule.getModeler());
			Pair<String, GeneratedLanguage> ruleHeader = getProcess(rule, ruleContext, LanguageState.HEADER);
			headers += ( ruleHeader.r().getInclude().length()>0?"\n":"" ) + ruleHeader.r().getInclude();
			// TODO : Ajouter le content dans la class !
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$EBD_INCLUDE", headers);
			//TODO : il faudrait enlever ce qui n'est pas necessaire et/ou en double

			JSG_2_Type typeHook = null;
			if(rule instanceof JMERuleAtomic) {
				typeHook = new JSG_2_TypeDart(-1, -1);
			}else if(rule instanceof JMEScript){
				typeHook = new JSG_2_TypeList(new JSG_2_TypeDart(-1, -1), -1, -1) ;
			}
			for(JMENode hooks : rule.getHooks()) {
				ruleContext.assignVar(hooks.getName(), typeHook, null);
			}

			//TODO : il faudrait enrichir ce context avec le context de modeleur qu'on devrait demander en parametre
			Triplet<String, String, List<GeneratedLanguage>> exprNodeClasses = getAllNodeEbdExprClass(rule, ruleContext);
			for(GeneratedLanguage gl : exprNodeClasses.getRight()) {
				ruleJAVAeader.append(gl.getInclude());
			}

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PARAMETERS", getEbdParameterDeclarations(rule));

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_EBD_EXPR_CLASSES", exprNodeClasses.l());
			// TODO : faire les includes!

			String constructorContent = printGraph(rule, (rule instanceof JMERuleAtomic));
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CONSTRUCTOR_CONTENT", constructorContent);


			Pair<String, String> paramArgAndContent = getRuleParamArgsAndApplyContent(rule, true);
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PARAM_ARGS", paramArgAndContent.l());

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PARAM_APPLY_CONTENT", paramArgAndContent.r());
			
			StringBuilder ebdParamDefaultValue = new StringBuilder();
			for(JMEParamEbd paramEbd: rule.getParamsEbd()) {
				if(paramEbd.getInitValue().length()>0) {
					ebdParamDefaultValue.append("\t\t");
					ebdParamDefaultValue.append(paramEbd.getName());
					ebdParamDefaultValue.append(" = ");
					ebdParamDefaultValue.append(transforEbdType(paramEbd.getInitValue(), rule.getModeler()));
					ebdParamDefaultValue.append(";\n");
				}
			}
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CONSTRUCTOR_PARAMETER_DEFAULT_VALUE", ebdParamDefaultValue.toString());
			

			// processes 

			Pair<String, GeneratedLanguage> preCond  = getProcess(rule, ruleContext, LanguageState.PRECONDITION);
			Pair<String, GeneratedLanguage> preProc  = getProcess(rule, ruleContext, LanguageState.PREPROCESS);
			Pair<String, GeneratedLanguage> midProc  = getProcess(rule, ruleContext, LanguageState.MIDPROCESS);
			Pair<String, GeneratedLanguage> postProc = getProcess(rule, ruleContext, LanguageState.POSTPROCESS);
			Pair<String, GeneratedLanguage> header   = getProcess(rule, ruleContext, LanguageState.HEADER);

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PRECONDITION", 		preCond.l());
			ruleJAVAeader.append(preCond.r().getInclude());

			String ruleJAVAasPrecond = "true";
			if(preCond.l().length()<=0) {
				ruleJAVAasPrecond = "false";
			}
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_HAS_PRECONDITION", 	ruleJAVAasPrecond);


			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PRE_PROCESS", 		preProc.l());

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_MID_PROCESS", 		midProc.l());
			ruleJAVAeader.append(midProc.r().getInclude());

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_POST_PROCESS", 		postProc.l());
			ruleJAVAeader.append(postProc.r().getInclude());



			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CLASS_CONTENT", 		"\t"+header.l().replaceAll("\n", "\t\n"));
			//ruleJAVAeader.append(header.l()); // TODO : plutot passer le content dans la definition de classe
			ruleJAVAeader.append(header.r().getInclude());

			// fin process


			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_COMMENT", "\""+rule.getComment().replaceAll("\n", "\\\\n").replace("\"", "\\\"") + "\"" );


			List<JMENode> rightGraph = rule.getRight().getNodes();
			List<JMENode> leftGraph  = rule.getLeft().getNodes();
			
			StringBuilder revAssoc = new StringBuilder();
			StringBuilder dartAccess = new StringBuilder();
			StringBuilder attachedNode = new StringBuilder();
			
			if(rule instanceof JMERuleAtomic) {

				for (int i = 0; i < rightGraph.size(); i++) {
					if (i == 0) {
						revAssoc.append(TAB + "switch(i) {\n");
					}
					final JMENode rightNode = rightGraph.get(i);
					final JMENode leftNode = rule.getLeft().getMatchNode(rightNode);
					if (leftNode != null) {
						revAssoc.append(TAB + "case " + i + ": return " + leftGraph.indexOf(leftNode) + ";\n");
					}
					if (i == rightGraph.size() - 1) {
						revAssoc.append(TAB + "}\n");
					}
				}

				for (int i = 0; i < leftGraph.size(); i++) {
					final JMENode n = leftGraph.get(i);
					String name = n.getName();
					if (!Character.isJavaIdentifierStart(name.charAt(0))) {
						name = "_" + name;
					}
					dartAccess.append(TAB).append("JerboaDart ").append(name).append("() {\n").append(TAB).append(TAB)
					.append("return ").append("curLeftFilter.node(").append(i).append(");\n").append(TAB)
					.append("}\n\n");
				}

				for (int i = 0; i < rightGraph.size(); i++) {
					if (i == 0) {
						attachedNode.append(TAB + "switch(i) {\n");
					}
					final JMENode rightNode = rightGraph.get(i);
					final JMENode leftNode = rule.getLeft().getMatchNode(rightNode);
					if (leftNode != null) {
						attachedNode.append(TAB + "case " + i + ": return " + leftGraph.indexOf(leftNode) + ";\n");
					}

					if (i == rightGraph.size() - 1) {
						attachedNode.append(TAB + "}\n");
					}
				}
			}else if(rule instanceof JMEScript) { // Script Content 
				GeneratedLanguage translatedScriptContent = new GeneratedLanguage();
				try {
					translatedScriptContent = Translator.translate(((JMEScript)rule).getContent(), new JerboaLanguageGlue(rule, LanguageState.CLASSICAL), 
							ruleContext, translatedScriptContent, rule.getModeler(), exportLanguage);
					ruleJAVA = ruleJAVA.replaceAll("\\$\\$SCRIPT_CONTENT", translatedScriptContent.getContent().replaceAll("\n", "\n\t"));
					ruleJAVAeader.append("\n"+translatedScriptContent.getInclude());
				}catch(Exception e) {
					ruleJAVA = ruleJAVA.replaceAll("\\$\\$SCRIPT_CONTENT", "");
				}
			}

			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_REV_ASSOC", revAssoc.toString());
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_DART_ACCESS_H", dartAccess.toString());
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_ATTACHED_NODE", attachedNode.toString());

			// getters and setters
			StringBuilder getterSetterJAVA   = new StringBuilder();

			for (JMEParamEbd pebd : rule.getParamsEbd()) {
				String pebdName = nameToProperty(pebd.getName());

				String ptype = transforEbdType(pebd.getType(), rule.getModeler());
				getterSetterJAVA.append("public " + ptype + " get" + pebdName + "()");
				getterSetterJAVA.append("{\n\treturn " + pebd.getName() + ";\n}\n");

				getterSetterJAVA.append("public void set" +  pebdName + "(" + ptype + " _" + pebdName + ")");
				getterSetterJAVA.append("{\n\tthis." + pebd.getName() + " = _" + pebdName + ";\n}\n");
			}
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_PARAMS_GETTER_SETTER", "\t" + getterSetterJAVA.toString().replaceAll("\n", "\t\n"));


			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CONSTRUCTOR_CONTENT", ""); // TODO: FILL THIS 



			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_IMPORT", ruleJAVAeader.toString());

			StringBuilder categoryListCreator = new StringBuilder();
			int cptCategory = 0;
			for (final String s : getCategoryFolder(rule.getCategory()).split("/")) {
				if(cptCategory>0) {
					categoryListCreator.append(".");
				}
				if (s.replace(" ", "") != "") {
					categoryListCreator.append(s);
				}
				cptCategory++;
			}
			ruleJAVA = ruleJAVA.replaceAll("\\$\\$RULE_CATEGORY", categoryListCreator.toString());


			///////////////////

			return ruleJAVA;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static String getModelerDirectory(JMEModeler modeler, String endDirectory) {
		String path = "";//modeler.getDestDir();
		if (!(path.startsWith("/") || path.startsWith("\\"))) {
			int indexSep = (new StringBuilder(modeler.getFileJME()).reverse().toString()).indexOf('/');
			if (indexSep == -1)
				indexSep = (new StringBuilder(modeler.getFileJME()).reverse().toString()).indexOf('\\');
			path = (new StringBuilder(
					(new StringBuilder(modeler.getFileJME()).reverse().toString()).substring(indexSep)).reverse()
					.toString());
			if (!(path.endsWith("/") || path.endsWith("\\"))) {
				path += File.separatorChar;
			}
			path+=endDirectory;
			if (!(path.endsWith("/") || path.endsWith("\\"))) {
				path += File.separatorChar;
			}
		}
		return path;
	}

	public static ArrayList<JMERule> exportModelerOnly(JMEModeler modeler) {
		String path = getModelerDirectory(modeler, modeler.getDestDir());		

		File modelerDir = new File(path+File.separator);
		if(!modelerDir.exists()) {
			modelerDir.mkdirs();
		}
		createProjectFiles(modeler);

		System.err.println("Exporting modeler to directory : " + path);
		final String subrep = modeler.getModule().replace('.', File.separatorChar) + File.separatorChar;
		final File cr = new File(path, subrep);
		String realPath = path + (path.endsWith(File.separatorChar + "") ? "" : File.separatorChar) + subrep;
		if (!realPath.endsWith(File.separator)) {
			realPath += File.separator;
		}
		final boolean created = cr.mkdirs();
		

		if (!created) {
			System.err.println("Creation of directories failed! (" + cr + ")");
		}

		StringBuilder modelerJAVA = new StringBuilder();

		int indexOfModelerName = subrep.lastIndexOf(File.separatorChar);
		if (indexOfModelerName < 0)
			indexOfModelerName = 0;
		String modelerName = modeler.getName();

		modelerJAVA.append("package " + getModelerDestModulePath(modeler, "").replaceAll("/", ".") + ";\n");

		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if(e.getFileHeader().length()>0)
				modelerJAVA.append("import " + e.getFileHeader() + ";\n");
		}

		modelerJAVA.append(baseImport);
		modelerJAVA.append("\n/* Rules import */\n");

		StringBuilder sbErrorRule = new StringBuilder();
		int countErrRule = 0;

		ArrayList<JMERule> ruleToExport = new ArrayList<>();
		for (final JMERule r : modeler.getRules()) {
			if (r instanceof JMERuleAtomic && !r.check()) {
				System.err.println("Rule " + r.getName() + " is not correct");
				sbErrorRule.append("\t- ").append(r.getFullName()).append("\n");
				countErrRule++;
			} else {
				ruleToExport.add(r);
				String ruleCategory = getRuleModule(r);
				modelerJAVA.append(
						"import " + ruleCategory + (ruleCategory.length() > 0 ? "." : "") + r.getName() + ";\n");
			}
		}

		modelerJAVA.append("\n");

		if (countErrRule > 0) {
			if (JOptionPane.showConfirmDialog(null,
					"Somes rules are in errors: \n" + sbErrorRule
					+ "\nDo you want continue the generation without rule in error?",
					"Error", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				System.err.println("GENERATION ABORTED");
				return new ArrayList<>();
			} else {
				System.err.println("GENERATION FORCED");
			}
		}

		// generation correct ou force.

		GeneratedLanguage prec = new GeneratedLanguage();
		try {
			prec = Translator.translate(modeler.getHeader(), new JerboaLanguageGlue(modeler), new TranslatorContextV2(modeler), prec, modeler,
					exportLanguage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelerJAVA.append("// ## BEGIN Modler header\n");
		modelerJAVA.append(prec.getInclude());
		modelerJAVA.append("// ## END Modler header\n");

		// class definition

		modelerJAVA.append("/**\n * ");
		modelerJAVA.append(modeler.getComment().replaceAll("[\\n|\\r]", "\\n"));
		modelerJAVA.append("\n */\n\n");

		modelerJAVA.append("public class ").append(modelerName).append(" extends JerboaModeler {\n");
		modelerJAVA.append("// BEGIN USER DECLARATION\n");
		modelerJAVA.append(prec.getContent() + "\n");
		modelerJAVA.append("// END USER DECLARATION\n\n");

		// Ajout des plongements en attributs
		for (final JMEEmbeddingInfo ebd : modeler.getEmbeddings()) {
			final String name = ebd.getName();
			modelerJAVA.append(TAB).append("protected JerboaEmbeddingInfo ").append(name).append(";\n");

		}

		modelerJAVA.append("\n\tpublic ");

		// Constructor
		modelerJAVA.append(modelerName);
		modelerJAVA.append("() throws JerboaException {\n");
		modelerJAVA.append("\t\tsuper("+modeler.getDimension()+",0);\n");


		modelerJAVA.append(TAB).append(TAB).append("// BEGIN USER HEAD CONSTRUCTOR TRANSLATION").append("\n");
		modelerJAVA.append(TAB).append(prec.getInClassConstructor() + "\n");
		modelerJAVA.append(TAB).append(TAB).append("// END USER HEAD CONSTRUCTOR TRANSLATION").append("\n");

		modelerJAVA.append(TAB).append(TAB + "gmap = new JerboaGMapArray(this);\n\n");


		for (final JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			final String name = e.getName();
			modelerJAVA.append(TAB).append(TAB);
			modelerJAVA.append(name);
			modelerJAVA.append(" = new JerboaEmbeddingInfo(\"");
			modelerJAVA.append(e.getName());
			modelerJAVA.append("\", JerboaOrbit.orbit(");

			for (int j = 0; j < e.getOrbit().size(); j++) {
				final int orbit = e.getOrbit().get(j);
				if (j > 0) {
					modelerJAVA.append(",");
				}
				modelerJAVA.append(String.valueOf(orbit));
			}
			modelerJAVA.append("), ");
			final String eType = transforEbdType(e.getType(), modeler);
			modelerJAVA.append(eType);// TODO: verifier cette ligne :
			// .substring(eType.lastIndexOf("::") +
			// 1));
			modelerJAVA.append(".class);\n");
		}

		modelerJAVA.append(TAB).append(TAB);
		modelerJAVA.append("this.registerEbdsAndResetGMAP(");
		int cpt=0;
		for (final JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if(cpt>0 && cpt<modeler.getEmbeddings().size()) {
				modelerJAVA.append(", ");				
			}
			cpt++;
			modelerJAVA.append(e.getName());
		}
		modelerJAVA.append(");\n");

		modelerJAVA.append("\n" + TAB + TAB + "// Rules\n");
		for (final JMERule r : ruleToExport) {
			modelerJAVA.append(TAB).append(TAB);
			modelerJAVA.append("registerRule(new ");
			modelerJAVA.append(r.getName());
			modelerJAVA.append("(this));\n");
		}

		modelerJAVA.append("}\n\n");

		// *******************{
		for (final JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			modelerJAVA.append(TAB);
			String ename = e.getName();
			modelerJAVA.append("public JerboaEmbeddingInfo get").append(ename.substring(0,1).toUpperCase() + ename.substring(1))
			.append("(){\n").append(TAB).append(TAB).append("return ").append(ename).append(";\n\t}\n\n");
		}
		// *******************}
		// end class
		modelerJAVA.append("};// end modeler;\n\n");


		File classModelerJAVA = null;
		FileOutputStream modelerStreamJAVA = null;

		try {
			classModelerJAVA = new File(realPath + modelerName + ".java");
			classModelerJAVA.createNewFile();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			modelerStreamJAVA = new FileOutputStream(classModelerJAVA);
			modelerStreamJAVA.write((modelerJAVA.toString()).getBytes());

			modelerStreamJAVA.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Modeler exported at " + path);
		return ruleToExport;
	}

	public static void exportModeler(JMEModeler modeler) {
		ArrayList<JMERule> ruleToExport = exportModelerOnly(modeler);
		for (final JMERule r : ruleToExport) {
			System.err.println("GENERATION OF RULE: " + r.getFullName());
			exportRule(r);
		}
	}

	public static void exportRule(JMERule r) {
		String path = getModelerDirectory(r.getModeler(), r.getModeler().getDestDir());
		final String subrep = r.getModeler().getModule().replace('.', File.separatorChar) + File.separatorChar;//+r.getCategory().replace('.', File.separatorChar);
		String realPath = path + (path.endsWith(File.separatorChar + "") ? "" : File.separatorChar) + subrep;
		if (!realPath.endsWith(File.separator)) {
			realPath += File.separator;
		}

		exportJMERule(r, realPath);
		//		if(r instanceof JMERuleAtomic) {
		//			exportAtomicRule((JMERuleAtomic) r, realPath);
		//		}else if(r instanceof JMEScript) {
		//			exportScript((JMEScript) r, realPath);
		//		}
		//		exportRuleCommon(realPath, r);
	}



	private static String getCategoryFolder(final String s) {
		String res = new String(s);
		if (s.length() > 0)
			return res.replaceAll("[\\.|:|;|,|\\\\]", "/");
		return "";
	}

	private static String getCategoryFolderForFile(final String s) {
		return getCategoryFolder(s).replaceAll("[/+\\.]", "/");
	}

	private static String getCommentHeader(Object elt) {
		StringBuilder commentHeader = new StringBuilder();
		commentHeader.append("#-------------------------------------------------\n#\n");
		commentHeader.append("# Project created by JerboaModelerEditor \n" + "# Date : ");
		commentHeader.append(new Date());
		commentHeader.append("\n#\n# ");
		String comment = "";
		if(elt instanceof JMEModeler) {
			comment = ((JMEModeler)elt).getComment();
		}else if(elt instanceof JMERule) {
			comment = ((JMERule)elt).getComment();
		}
		commentHeader.append(comment.replaceAll("\n", "\n# "));
		commentHeader.append("\n#\n#-------------------------------------------------");
		return commentHeader.toString();
	}

	private static String getEbdClassNameTest(JMEModeler modeler) {
		StringBuilder builder = new StringBuilder();
		int cpt=0;
		for(JMEEmbeddingInfo einf: modeler.getEmbeddings()) {
			if(cpt<modeler.getEmbeddings().size()-1) {
				builder.append("\telse ");
			}
			builder.append("if(ebdinf->name()==\""+einf.getName()+"\"){\n");
			builder.append("\t\treturn \"" + einf.getType() + "\";\n\t}");
			cpt++;
		}
		return builder.toString();
	}

	private static String getEbdIdTest(JMEModeler modeler) {
		StringBuilder builder = new StringBuilder();

		int cpt=0;
		builder.append("\t");
		for(JMEEmbeddingInfo einf: modeler.getEmbeddings()) {
			if(cpt>0 && cpt<modeler.getEmbeddings().size()-1) {
				builder.append("\telse ");
			}
			builder.append("if(ebdName==\""+einf.getName()+"\")");
			builder.append("\t\treturn " + cpt + ";\n\t");
			cpt++;
		}
		return builder.toString();
	}

	private static String getEbdToString(JMEModeler modeler) {
		StringBuilder builder = new StringBuilder();
		builder.append("\treturn e->toString();");
		return builder.toString();
	}

	private static String getEbdIncludes(JMEModeler modeler) {
		StringBuilder builder = new StringBuilder();
		for(JMEEmbeddingInfo einf : modeler.getEmbeddings()) {
			if(einf.getType().length()>0
					&& !builder.toString().contains(einf.getType())) {
				builder.append("import ");
				builder.append(einf.getType());
				builder.append("; // Embedding '");
				builder.append(einf.getName());
				builder.append("'\n");
			}
		}
		return builder.toString();
	}

//	private static String getEbdType(String type) {
//		return type.replaceAll("\\:\\:", ".");
//	}

	private static String getEbdUnserializeTest(JMEModeler modeler) {
		StringBuilder builder = new StringBuilder();
		int cpt=0;
		for(JMEEmbeddingInfo einf: modeler.getEmbeddings()) {
			if(cpt>0) {
				builder.append("else ");
			}
			builder.append("if(ebdinf.getID()=="+cpt+"){\n");
			builder.append("\t\treturn new " + einf.getType() + ".unserialize(valueSerialized);\n\t}");
			cpt++;
		}
		return builder.toString();
	}

	private static String getEbdSerializeTest(JMEModeler modeler) {
		StringBuilder builder = new StringBuilder();
		int cpt=0;
		for(JMEEmbeddingInfo einf: modeler.getEmbeddings()) {
			if(cpt>0) {
				builder.append("else ");
			}
			builder.append("if(ebdinf.getID()=="+cpt+"){ //" + einf.getName() + "\n");
			builder.append("\t\treturn ((" + einf.getType() + ")ebd).serialize();\n\t}");
			cpt++;
		}
		return builder.toString();
	}

	private static String getModelerModuleNamespace(JMEModeler modeler, String endStringToAdd) {
		String module = modeler.getModule().replaceAll("[\\./:/]+", ".");
		if (!module.endsWith(endStringToAdd) && !module.endsWith("\\") && module.length() > 0) {
			module += endStringToAdd;
		}
		return module;
	}

	private static String getModelerModulePath(JMEModeler modeler, String endStringToAdd) {
		String module = modeler.getModule().replaceAll("[\\./:/]+", "/");
		if (!module.endsWith("/") && module.length() > 0) {
			module += endStringToAdd;
		}
		return module;
	}
	
	private static String getModelerDestModulePath(JMEModeler modeler, String endStringToAdd) {
		String module = ""; //modeler.getDestDir().replaceAll("/", ".");
//		if (!module.endsWith("/") && module.length() > 0 && modeler.getModule().length()>0) {
//			module+="/";
//		}
		
		module += modeler.getModule().replaceAll("[\\./:/]+", "/");
		if (!module.endsWith("/") && module.length() > 0) {
			module += endStringToAdd;
		}
		return module;
	}

//	private static String[] getModelerModuleSplited(JMEModeler modeler) {
//		return modeler.getModule().split("[\\./:/]+");
//	}

	private static String getRuleModule(JMERule r) {
		String path = getModelerDestModulePath(r.getModeler(), "");
		String category = getCategoryFolder(r.getCategory());
		if(category.length()>0) {
			path = path+"."+category;
		}
//		if (path.length() > 0 && !path.endsWith("/"))
//			path += "/";
		return path.replaceAll("/", ".");
	}

	static String printGraph(JMERule rop, boolean printEmbeddingExpr) {
		StringBuilder content = new StringBuilder();

		final StringBuilder leftJerboaEdge = new StringBuilder();
		final StringBuilder rightJerboaEdge = new StringBuilder();
		final StringBuilder leftJerboaDart = new StringBuilder();
		final StringBuilder rightJerboaDart = new StringBuilder();
		final StringBuilder hookJerboaDart = new StringBuilder();

		ArrayList<JMENodeExpression> expressions = new ArrayList<>();

		final List<JMENode> leftGraph = rop.getLeft().getNodes();
		final List<JMENode> rightGraph = rop.getRight().getNodes();

		// left graph
		for (int i = 0; i < leftGraph.size(); i++) {
			final JMENode n = leftGraph.get(i);
			final String name = "" + JMEPreferences.getSuitableName(n.getName());

			/**
			 * left JerboaDarts
			 */

			content.append(TAB + "JerboaRuleNode l").append(name);
			content.append(" = new JerboaRuleNode(\"").append(name);
			content.append("\", ").append(i);
			// On ne met pour l'instant pas la multiplicité
			/*.append(", JerboaRuleNodeMultiplicity");

			if (n.getMultiplicity().getMinChar() == "*" && n.getMultiplicity().getMaxChar() == "*") {
				content.append("()");
			} else {
				content.append("(").append(n.getMultiplicity().getMin()).append(",")
				.append(n.getMultiplicity().getMax()).append(")");
			}*/
			content.append(", JerboaOrbit.orbit(");

			for (int j = 0; j < n.getOrbit().size(); j++) {
				final int orbit = n.getOrbit().get(j);
				if (j > 0) {
					content.append(",");
				}
				content.append(String.valueOf(orbit));
			}
			content.append("), " + rop.getModeler().getDimension()); 
			
			// Position des noeuds dans le graphe
			//+n.getX() +","+n.getY()
			content.append(");\n");

			if (i == leftGraph.size() - 1) {
				content.append("\n");
			}

			// left JerboaDarts list
			leftJerboaDart.append(TAB);
			leftJerboaDart.append("this.left.add(l").append(name);
			leftJerboaDart.append(");\n");

			// hook list
			if (n.getKind() == JMENodeKind.HOOK) {
				hookJerboaDart.append(TAB);
				hookJerboaDart.append("this.hooks.add(l").append(name);
				hookJerboaDart.append(");\n");
			}
		}
		// left Edge
		for (JMEArc arc : rop.getLeft().getArcs()) {
			leftJerboaEdge.append(TAB);
			leftJerboaEdge.append("l").append(arc.getSource().getName());
			leftJerboaEdge.append(".setAlpha(").append(arc.getDimension());
			leftJerboaEdge.append(", l").append(arc.getDestination().getName()).append(");\n");
		}

		/**
		 * right graph
		 */

		int count = 0;
		for (JMENode n : rightGraph) {
			// embExpressions
			String nodeName = n.getName();
			
			// right JerboaDarts
			content.append(TAB).append("JerboaRuleNode r").append(nodeName);
			content.append(" = new JerboaRuleNode(\"").append(nodeName);
			content.append("\", ").append(count);
			
			/*.append(", JerboaRuleNodeMultiplicity");

			if (n.getMultiplicity().getMinChar() == "*" && n.getMultiplicity().getMaxChar() == "*") {
				content.append("()");
			} else {
				content.append("(").append(n.getMultiplicity().getMin()).append(",")
				.append(n.getMultiplicity().getMax()).append(")");
			}
			*/
			content.append(", JerboaOrbit.orbit(");

			for (int j = 0; j < n.getOrbit().size(); j++) {
				final int orbit = n.getOrbit().get(j);
				if (j > 0) {
					content.append(",");
				}
				content.append(String.valueOf(orbit));
			}
			content.append(")"); // fin Orbit

			
			//, "+n.getX() +","+n.getY()
			content.append(", "+ rop.getModeler().getDimension());
			
			if (printEmbeddingExpr) {
				ArrayList<JMENodeExpression> allexprs = new ArrayList<>();
				allexprs.addAll(n.getExplicitExprs());
				allexprs.addAll(n.getDefaultExprs());
				for (final JMENodeExpression expr : allexprs) {
					content.append(TAB).append(", new ");
					content.append(rop.getName());
					content.append("ExprR");
					content.append(nodeName);
					content.append(expr.getEbdInfo().getName());
					content.append("()");
					expressions.add(expr);
				}
			}
			
			content.append(");\n");// fin JerboaRuleNode

			// right JerboaDart list
			rightJerboaDart.append(TAB);
			rightJerboaDart.append("this.right.add(r").append(n.getName());
			rightJerboaDart.append(");\n\n");

			count++;
		}
		// right Edges
		for (JMEArc a : rop.getRight().getArcs()) {
			final JMENode target = a.getDestination();
			final JMENode source = a.getSource();
			rightJerboaEdge.append(TAB);
			rightJerboaEdge.append("r").append(source.getName());
			rightJerboaEdge.append(".setAlpha(").append(a.getDimension());
			rightJerboaEdge.append(", r").append(target.getName()).append(");\n");
		}

		content.append("\n");

		if (leftJerboaEdge.length() != 0) {
			content.append((leftJerboaEdge.append("\n").toString()));
		}
		if (rightJerboaEdge.length() != 0) {
			content.append((rightJerboaEdge.append("\n").toString()));
		}
		if (leftJerboaDart.length() != 0) {
			content.append("\n// ------- LEFT GRAPH \n\n");
			content.append((leftJerboaDart.append("\n").toString()));
		}
		if (rightJerboaDart.length() != 0) {
			content.append("\n// ------- RIGHT GRAPH \n\n");
			content.append((rightJerboaDart.append("\n").toString()));
		}
		if (hookJerboaDart.length() != 0) {
			content.append((hookJerboaDart.append("\n").toString()));
		}
		
		return content.toString();
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
}
