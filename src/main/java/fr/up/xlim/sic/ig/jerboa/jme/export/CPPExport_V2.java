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
import up.jerboa.core.util.Pair;
import up.jerboa.util.Triplet;

public class CPPExport_V2 {

	private static ExportLanguage exportLanguage = ExportLanguage.CPP_V2;


	private static final String TAB = "    ";

	private static final String baseImport = "\n#include <core/jerboamodeler.h>\n"
			+ "#include <coreutils/jerboagmaparray.h>\n" + "#include <core/jerboagmap.h>\n"
			+ "#include <core/jerboaRuleOperation.h>\n" + "#include <serialization/jbaformat.h>\n";

	private static String createBridge(String modelerDirectory, JMEModeler modeler) {

		try {
			String bridgeName = "Bridge_"+modeler.getName();

			InputStream inputStreamCPP = (InputStream) System.class.getResource("/templateFiles/c++/bridge.cpp").getContent();
			String bridgeCPP = new BufferedReader(new InputStreamReader(inputStreamCPP)).lines()
					.parallel().collect(Collectors.joining("\n"));


			InputStream inputStreamH = (InputStream) System.class.getResource("/templateFiles/c++/bridge.h").getContent();
			String bridgeH = new BufferedReader(new InputStreamReader(inputStreamH)).lines()
					.parallel().collect(Collectors.joining("\n"));


			bridgeH   = bridgeH.replaceAll("\\$\\$MODELER_PATH", getModelerModulePath(modeler, "/"+modeler.getName()));
			
			bridgeH   = bridgeH.replaceAll("\\$\\$MODELER_NAMESPACE", getModelerModuleNamespace(modeler, ""));
			bridgeCPP = bridgeCPP.replaceAll("\\$\\$MODELER_NAMESPACE", getModelerModuleNamespace(modeler, ""));

			bridgeH   = bridgeH.replaceAll("\\$\\$MODELER_NAMESPACE", modeler.getName());
			bridgeCPP = bridgeCPP.replaceAll("\\$\\$MODELER_NAMESPACE", modeler.getName());

			bridgeH   = bridgeH.replaceAll("\\$\\$MODELER_NAME", modeler.getName());
			bridgeCPP = bridgeCPP.replaceAll("\\$\\$MODELER_NAME", modeler.getName());

			bridgeCPP = bridgeCPP.replaceAll("\\$\\$EBD_TO_STRING", getEbdToString(modeler));


			bridgeCPP = bridgeCPP.replaceAll("\\$\\$EBD_INCLUDE", getEbdIncludes(modeler));

			String unserializeTest = getEbdUnserializeTest(modeler);
			bridgeCPP = bridgeCPP.replaceAll("\\$\\$EBD_UNSERIALIZE", unserializeTest);
			bridgeCPP = bridgeCPP.replaceAll("\\$\\$EBD_BINARY_UNSERIALIZE", unserializeTest);

			String serializeTest = getEbdSerializeTest(modeler);
			bridgeCPP = bridgeCPP.replaceAll("\\$\\$EBD_SERIALIZE", serializeTest);
			bridgeCPP = bridgeCPP.replaceAll("\\$\\$EBD_BINARY_SERIALIZE", serializeTest);

			bridgeCPP = bridgeCPP.replaceAll("\\$\\$EBD_CLASS_NAME", getEbdClassNameTest(modeler));

			bridgeCPP = bridgeCPP.replaceAll("\\$\\$EBD_ID", getEbdIdTest(modeler));


			String presetPrefix = "\\$\\$EBD_PRESET";

			String[] posMatcher = {"point", "position"};
			JMEEmbeddingInfo positionPreset = getEbdInfPreset(modeler, posMatcher) ;
			if(positionPreset!=null) {
				String presetPrefixPosition = presetPrefix + "_POSITION";
				bridgeCPP = bridgeCPP.replaceAll(presetPrefixPosition+"_NAME", positionPreset.getName());
				bridgeCPP = bridgeCPP.replaceAll(presetPrefixPosition+"_TYPE", positionPreset.getType());	
				int cpt = 0;
				for(JMEEmbeddingInfo einf : modeler.getEmbeddings()) {
					if(einf.getName().compareTo(positionPreset.getName())==0) {
						bridgeCPP = bridgeCPP.replaceAll(presetPrefixPosition+"_ID",cpt+"");
						break;
					}
					cpt++;
				}	
			}

			String[] colMatcher = {"color", "col"};
			JMEEmbeddingInfo colorPreset = getEbdInfPreset(modeler, colMatcher) ;
			if(colorPreset!=null) {
				String presetPrefixColor = presetPrefix + "_COLOR";
				bridgeCPP = bridgeCPP.replaceAll(presetPrefixColor+"_NAME", colorPreset.getName());
				bridgeCPP = bridgeCPP.replaceAll(presetPrefixColor+"_TYPE", colorPreset.getType());	
				int cpt = 0;
				for(JMEEmbeddingInfo einf : modeler.getEmbeddings()) {
					if(einf.getName().compareTo(colorPreset.getName())==0) {
						bridgeCPP = bridgeCPP.replaceAll(presetPrefixColor+"_ID",cpt+"");
						break;
					}
					cpt++;
				}				
			}

			String[] orientMatcher = {"orient"};
			JMEEmbeddingInfo orientPreset = getEbdInfPreset(modeler, orientMatcher) ;
			if(orientPreset!=null) {
				String presetPrefixOrient = presetPrefix + "_ORIENT";
				bridgeCPP = bridgeCPP.replaceAll(presetPrefixOrient+"_NAME", orientPreset.getName());
				bridgeCPP = bridgeCPP.replaceAll(presetPrefixOrient+"_TYPE", orientPreset.getType());
				int cpt = 0;
				for(JMEEmbeddingInfo einf : modeler.getEmbeddings()) {
					if(einf.getName().compareTo(orientPreset.getName())==0) {
						bridgeCPP = bridgeCPP.replaceAll(presetPrefixOrient+"_ID",cpt+"");
						break;
					}
					cpt++;
				}		
			}


			File bridgeFileH = null;
			FileOutputStream bridgeFileStreamH = null;
			File bridgeFileCPP = null;
			FileOutputStream bridgeFileStreamCPP = null;

			bridgeFileH = new File(modelerDirectory, bridgeName + ".h");
			if (!bridgeFileH.exists()) {
				bridgeFileH.createNewFile();
				bridgeFileStreamH = new FileOutputStream(bridgeFileH);
				bridgeFileStreamH.write((bridgeH).getBytes());
				bridgeFileStreamH.close();
			}

			bridgeFileCPP = new File(modelerDirectory, bridgeName + ".cpp");
			if (!bridgeFileCPP.exists()) {
				bridgeFileCPP.createNewFile();
				bridgeFileStreamCPP = new FileOutputStream(bridgeFileCPP);
				bridgeFileStreamCPP.write((bridgeCPP).getBytes());
				bridgeFileStreamCPP.close();
			}
			return bridgeName;
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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

	private static void createMainFile(String modelerDirectory, JMEModeler modeler, String bridgeName) {

		try {
			InputStream inputStreamPRO = (InputStream) System.class.getResource("/templateFiles/c++/main.cpp").getContent();

			String mailFileContent = new BufferedReader(new InputStreamReader(inputStreamPRO)).lines()
					.parallel().collect(Collectors.joining("\n"));

			File mainFile = null;
			createPriFile(modelerDirectory, modeler);
			mainFile = new File(modelerDirectory, "main" + modeler.getName() + ".cpp");
			if (mainFile.exists()) // on n'ecrase pas le main
				return;

			mailFileContent = mailFileContent.replaceAll("\\$\\$MAIN_HEADER", getCommentHeader(modeler));

			// TODO: attention! il faut faire le namespace avant !!
			mailFileContent = mailFileContent.replaceAll("\\$\\$MODELER_NAMESPACE", getModelerModuleNamespace(modeler, ""));

			mailFileContent = mailFileContent.replaceAll("\\$\\$MODELER_NAME", modeler.getName());

			FileOutputStream proFileStream = null;

			mainFile.createNewFile();
			proFileStream = new FileOutputStream(mainFile);
			proFileStream.write((mailFileContent.toString()).getBytes());
			proFileStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createPriFile(String modelerDirectory, JMEModeler modeler) {
		StringBuilder priFileContent = new StringBuilder();
		priFileContent.append(getCommentHeader(modeler));
		priFileContent.append("\n# Modeler files \n");

		priFileContent.append("SOURCES +=");
		priFileContent.append("\\\n\t" + getModelerModulePath(modeler, "/").replaceAll("\\.", "/") + modeler.getName() + ".cpp");

		ArrayList<JMERule> ruleToExport = new ArrayList<>();
		for (JMERule r : modeler.getRules()) {
			if (r instanceof JMEScript || r.check()) {
				ruleToExport.add(r);
				priFileContent.append("\\\n\t" + getRulePath(r) + ".cpp");
			}
		}

		priFileContent.append("\n\nHEADERS +=");
		priFileContent.append("\\\n\t" + getModelerModulePath(modeler, "/").replaceAll("\\.", "/") + modeler.getName() + ".h");

		for (JMERule r : ruleToExport) {
			priFileContent.append("\\\n\t" + getRulePath(r) + ".h");
		}
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			priFileContent.append("\\\n\t" + e.getFileHeader());
		}

		File priFile = null;
		FileOutputStream priFileStream = null;

		try {
			priFile = new File(modelerDirectory, modeler.getName() + ".pri");
			priFile.createNewFile();
			priFileStream = new FileOutputStream(priFile);
			priFileStream.write((priFileContent.toString()).getBytes());
			priFileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createProFile(String modelerDirectory, JMEModeler modeler) {
		try {
			File proFile = null;
			createPriFile(modelerDirectory, modeler);
			proFile = new File(modelerDirectory, modeler.getName() + ".pro");
			if (proFile.exists()) // on n'ecrase pas le .pro
				return;

			String bridgeName = createBridge(modelerDirectory, modeler);

			createMainFile(modelerDirectory, modeler, bridgeName);

			InputStream inputStreamPRO = (InputStream) System.class.getResource("/templateFiles/c++/proFile.pro").getContent();

			String proFileContent = new BufferedReader(new InputStreamReader(inputStreamPRO)).lines()
					.parallel().collect(Collectors.joining("\n"));

			proFileContent = proFileContent.replaceAll("\\$\\$PRO_HEADER", getCommentHeader(modeler));

			proFileContent = proFileContent.replaceAll("\\$\\$PRO_MODELER_NAME", modeler.getName());

			FileOutputStream proFileStream = null;

			proFile.createNewFile();
			proFileStream = new FileOutputStream(proFile);
			proFileStream.write((proFileContent.toString()).getBytes());
			proFileStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Triplet<String, String, GeneratedLanguage> getNodeEbdExprClass(JMENodeExpression expr, JMERule rule, 
			JerboaLanguageGlue glue, TranslatorContextV2 context){
		StringBuilder builderH = new StringBuilder();
		StringBuilder builderCPP = new StringBuilder();

		GeneratedLanguage translatedExpression = new GeneratedLanguage();

		try {
			InputStream inputStreamCPP = (InputStream) System.class.getResource("/templateFiles/c++/ebdExprClass.cpp").getContent();
			String ebdExprClassCPP = new BufferedReader(new InputStreamReader(inputStreamCPP)).lines()
					.parallel().collect(Collectors.joining("\n"));


			InputStream inputStreamH = (InputStream) System.class.getResource("/templateFiles/c++/ebdExprClass.h").getContent();
			String ebdExprClassH = new BufferedReader(new InputStreamReader(inputStreamH)).lines()
					.parallel().collect(Collectors.joining("\n"));

			ebdExprClassH = ebdExprClassH.replaceAll("\\$\\$RULE_NAME", rule.getName());
			ebdExprClassCPP = ebdExprClassCPP.replaceAll("\\$\\$RULE_NAME", rule.getName());

			ebdExprClassH = ebdExprClassH.replaceAll("\\$\\$RULE_NODE_NAME", expr.getNode().getName());
			ebdExprClassCPP = ebdExprClassCPP.replaceAll("\\$\\$RULE_NODE_NAME", expr.getNode().getName());

			ebdExprClassH = ebdExprClassH.replaceAll("\\$\\$RULE_EXPR_EBD_NAME", expr.getEbdInfo().getName());
			ebdExprClassCPP = ebdExprClassCPP.replaceAll("\\$\\$RULE_EXPR_EBD_NAME", expr.getEbdInfo().getName());

			int ebdId = -1;
			int cpt=0;
			for(JMEEmbeddingInfo einf : rule.getModeler().getEmbeddings()) {
				if(einf.getName().compareTo(expr.getEbdInfo().getName()) == 0) {
					ebdId=cpt;
				}
				cpt++;
			}
			ebdExprClassCPP = ebdExprClassCPP.replaceAll("\\$\\$RULE_EXPR_EBD_ID", ebdId+"");


			try {
				translatedExpression = Translator.translate(expr.getExpression(), glue, context, translatedExpression, rule.getModeler(), exportLanguage);				
				ebdExprClassCPP = ebdExprClassCPP.replaceAll("\\$\\$EBD_EXPR_CODE", translatedExpression.getContent().replaceAll("\n", "\n\t"));
			} catch (Exception e) {
				e.printStackTrace();
				ebdExprClassCPP = ebdExprClassCPP.replaceAll("\\$\\$EBD_EXPR_CODE", "");
			}

			builderH.append(ebdExprClassH);
			builderCPP.append(ebdExprClassCPP);

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
			args.append(", JerboaDart* ");
			args.append(tp.getNode().getName());

			content.append("_hookList.addCol(");
			content.append(tp.getNode().getName());
			content.append(");\n");
		}
		for (JMEParamEbd ep : rule.getParamsEbd()) {
			for (int i = 0; i < rule.getParamsEbd().size(); i++) {
				if (ep.getOrder() == i) {
					args.append(", ");
					args.append(getEbdType(ep.getType()) + " " + ep.getName());
					if (printDefaultValue && ep.getInitValue().replaceAll(" ", "").length() > 0) {
						args.append(" = " + ep.getInitValue());
					}
					content.append("\tset" + ep.getName() + "(" + ep.getName() + ");\n");
				}
			}
		}

		return new Pair<String, String>(args.toString(), content.toString());
	}


	private static Triplet<String, String, GeneratedLanguage> getProcess(JMERule rule, TranslatorContextV2 context, LanguageState langState){
		StringBuilder processH   = new StringBuilder();
		StringBuilder processCPP = new StringBuilder();

		GeneratedLanguage translatedProcess = new GeneratedLanguage();

		String processReturnType = "";
		String processSign = "";
		String processContent = "";

		switch (langState) {
		case PREPROCESS:
			processReturnType = "bool ";
			processSign = "preprocess(const JerboaGMap* gmap)";
			processContent = rule.getPreProcess();
			break;
		case PRECONDITION:
			processReturnType = "bool ";
			processSign = "evalPrecondition(const JerboaGMap* gmap, const std::vector<JerboaFilterRowMatrix*> & leftfilter)";
			processContent = rule.getPrecondition();
			break;	
		case MIDPROCESS:
			processReturnType = "bool ";
			processSign = "midprocess(const JerboaGMap* gmap, const std::vector<JerboaFilterRowMatrix*> & leftfilter)";
			processContent = rule.getMidProcess();
			break;
		case POSTPROCESS:
			processReturnType = "bool ";
			processSign = "postprocess(const JerboaGMap* gmap)";
			processContent = rule.getPostProcess();
			break;
		case HEADER:
			try {
				translatedProcess = Translator.translate(rule.getHeader(), new JerboaLanguageGlue(rule, langState), 
						context, translatedProcess, rule.getModeler(), exportLanguage);
				return new Triplet<String, String, GeneratedLanguage>("", translatedProcess.getContent(), translatedProcess);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// si pas marche on passe dans le default
		default:
			return new Triplet<String, String, GeneratedLanguage>("", "", translatedProcess);
		}


		if(processContent.length()>0) {
			try {
				translatedProcess = Translator.translate(processContent, new JerboaLanguageGlue(rule, langState), 
						context, translatedProcess, rule.getModeler(), exportLanguage);

				processH.append("\t");
				processH.append(processReturnType);
				processH.append(processSign);
				processH.append(";\n");

				processCPP.append(processReturnType);
				processCPP.append(rule.getName());
				processCPP.append("::");
				processCPP.append(processSign);
				processCPP.append("{\n\t");
				processCPP.append(translatedProcess.getContent().replaceAll("\n", "\n\t"));
				processCPP.append("\n}\n");
			} catch (Exception e) {
				//			e.printStackTrace();
				processH.append("\t");
				processH.append(processReturnType);
				processH.append(processSign);
				processH.append("{return true;}\n");
			}
		}else {
			processH.append("\t");
			processH.append(processReturnType);
			processH.append(processSign);
			processH.append("{return true;}\n");
		}
		return new Triplet<String,String,GeneratedLanguage>(processH.toString(), processCPP.toString(), translatedProcess);
	}

	private static String transforEbdType(String s, JMEModeler modeler) {
		String res = s;
		for(JMEEmbeddingInfo einfo: modeler.getEmbeddings()) {
			res = res.replaceAll("@ebd<"+ einfo.getName() +">", einfo.getType());
		}
		return res;
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
			StringBuilder ruleHeader = new StringBuilder();

			String ruleFileName = "";
			if(rule instanceof JMERuleAtomic) {
				ruleFileName = "atomic";
			}else if(rule instanceof JMEScript){
				ruleFileName = "script";
			}

			InputStream inputStreamCPP = (InputStream) System.class.getResource("/templateFiles/c++/" +ruleFileName+ ".cpp").getContent();
			String ruleCPP = new BufferedReader(new InputStreamReader(inputStreamCPP)).lines()
					.parallel().collect(Collectors.joining("\n"));


			InputStream inputStreamH = (InputStream) System.class.getResource("/templateFiles/c++/" +ruleFileName+ ".h").getContent();
			String ruleH = new BufferedReader(new InputStreamReader(inputStreamH)).lines()
					.parallel().collect(Collectors.joining("\n"));

			///////////////////

			ruleH = ruleH.replaceAll("\\$\\$RULE_HEADER", getCommentHeader(rule));

			ruleH = ruleH.replaceAll("\\$\\$MODELER_PATH", getModelerModulePath(rule.getModeler(), "/"+rule.getModeler().getName()));



			ruleCPP = ruleCPP.replaceAll("\\$\\$MODELER_MODULE_PATH", getModelerModulePath(rule.getModeler(), ""));
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_PATH", getRulePath(rule));

			ruleH = ruleH.replaceAll("\\$\\$RULE_NAME", ruleName);
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_NAME", ruleName);


			// TODO: attention! il faut faire le namespace avant !!
			ruleH = ruleH.replaceAll("\\$\\$MODELER_NAMESPACE", getModelerModuleNamespace(rule.getModeler(), ""));
			ruleCPP = ruleCPP.replaceAll("\\$\\$MODELER_NAMESPACE", getModelerModuleNamespace(rule.getModeler(), ""));

			ruleH = ruleH.replaceAll("\\$\\$MODELER_NAME", rule.getModeler().getName());
			ruleCPP = ruleCPP.replaceAll("\\$\\$MODELER_NAME", rule.getModeler().getName());

			TranslatorContextV2 ruleContext = new TranslatorContextV2(rule.getModeler());

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
				ruleHeader.append(gl.getInclude());
			}

			ruleH = ruleH.replaceAll("\\$\\$RULE_PARAMETERS", getEbdParameterDeclarations(rule));


			ruleH = ruleH.replaceAll("\\$\\$RULE_EBD_EXPR_CLASSES_H", exprNodeClasses.l());
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_EBD_EXPR_CLASSES_CPP", exprNodeClasses.m());
			// TODO : faire les includes!

			// HAK pour eviter plantage
			ruleCPP = ruleCPP.replaceAll("\\$\\$EBD_INCLUDE", getEbdIncludes(rule.getModeler()));
			
			
			String constructorContent = printGraph(rule, (rule instanceof JMERuleAtomic));
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_CONSTRUCTOR_CONTENT", constructorContent);


			Pair<String, String> paramArgAndContent = getRuleParamArgsAndApplyContent(rule, true);
			ruleH = ruleH.replaceAll("\\$\\$RULE_PARAM_ARGS", paramArgAndContent.l());
			Pair<String, String> paramArgAndContentNoDefaultValue = getRuleParamArgsAndApplyContent(rule, false);
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_PARAM_ARGS", paramArgAndContentNoDefaultValue.l());

			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_PARAM_APPLY_CONTENT", paramArgAndContent.r());

			// processes 

			Triplet<String, String, GeneratedLanguage> preCond  = getProcess(rule, ruleContext, LanguageState.PRECONDITION);
			Triplet<String, String, GeneratedLanguage> preProc  = getProcess(rule, ruleContext, LanguageState.PREPROCESS);
			Triplet<String, String, GeneratedLanguage> midProc  = getProcess(rule, ruleContext, LanguageState.MIDPROCESS);
			Triplet<String, String, GeneratedLanguage> postProc = getProcess(rule, ruleContext, LanguageState.POSTPROCESS);
			Triplet<String, String, GeneratedLanguage> header   = getProcess(rule, ruleContext, LanguageState.HEADER);

			ruleH = ruleH.replaceAll("\\$\\$RULE_PRECONDITION_H", 		preCond.l());
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_PRECONDITION_CPP", 	preCond.m());
			ruleHeader.append(preCond.r().getInclude());

			String ruleHasPrecond = "true";
			if(preCond.getMiddle().length()<=0) {
				ruleHasPrecond = "false";
			}
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_HAS_PRECONDITION", 	ruleHasPrecond);


			ruleH = ruleH.replaceAll("\\$\\$RULE_PRE_PROCESS_H", 		preProc.l());
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_PRE_PROCESS_CPP", 	preProc.m());
			ruleHeader.append(preProc.r().getInclude());

			ruleH = ruleH.replaceAll("\\$\\$RULE_MID_PROCESS_H", 		midProc.l());
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_MID_PROCESS_CPP", 	midProc.m());
			ruleHeader.append(midProc.r().getInclude());

			ruleH = ruleH.replaceAll("\\$\\$RULE_POST_PROCESS_H", 		postProc.l());
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_POST_PROCESS_CPP", 	postProc.m());
			ruleHeader.append(postProc.r().getInclude());
			

//			ruleH = ruleH.replaceAll("\\$\\$RULE_POST_PROCESS_H", 		postProc.l());
//			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_POST_PROCESS_CPP", 	postProc.m());
			ruleHeader.append(header.m()); // TODO : plutot passer le content dans la definition de classe
			ruleHeader.append(header.r().getInclude());
			
			// fin process


			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_COMMENT", "\""+rule.getComment().replaceAll("\n", "\\\\n").replace("\"", "\\\"") + "\"" );


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
					dartAccess.append(TAB).append("JerboaDart* ").append(name).append("() {\n").append(TAB).append(TAB)
					.append("return ").append("curLeftFilter->node(").append(i).append(");\n").append(TAB)
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
					ruleCPP = ruleCPP.replaceAll("\\$\\$SCRIPT_CONTENT", translatedScriptContent.getContent().replaceAll("\n", "\n\t"));
					ruleHeader.append("\n"+translatedScriptContent.getInclude());
				}catch(Exception e) {
					ruleCPP = ruleCPP.replaceAll("\\$\\$SCRIPT_CONTENT", "");
				}
			}

			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_REV_ASSOC", revAssoc.toString());
			ruleH   = ruleH.replaceAll("\\$\\$RULE_DART_ACCESS_H", dartAccess.toString());
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_ATTACHED_NODE", attachedNode.toString());

			// getters and setters
			StringBuilder getterSetterH   = new StringBuilder();
			StringBuilder getterSetterCPP = new StringBuilder();

			for (JMEParamEbd pebd : rule.getParamsEbd()) {
				String ptype = getEbdType(pebd.getType());
				getterSetterH.append(TAB + ptype + " get" + pebd.getName() + "();\n");
				getterSetterCPP.append(ptype + " " + ruleName + "::get" + pebd.getName() + "()");
				getterSetterCPP.append("{\n\treturn " + pebd.getName() + ";\n}\n");

				getterSetterH.append(TAB + "void set" + pebd.getName() + "(" + ptype + " _" + pebd.getName() + ");\n");
				getterSetterCPP.append("void " + ruleName + "::set" + pebd.getName() + "(" + ptype + " _" + pebd.getName() + ")");
				getterSetterCPP.append("{\n\tthis->" + pebd.getName() + " = _" + pebd.getName() + ";\n}\n");
			}
			ruleH = ruleH.replaceAll("\\$\\$RULE_PARAMS_GETTER_SETTER", getterSetterH.toString());
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_PARAMS_GETTER_SETTER", getterSetterCPP.toString());


			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_CONSTRUCTOR_CONTENT", ""); // TODO: FILL THIS 



			ruleH = ruleH.replaceAll("\\$\\$RULE_IMPORT", ruleHeader.toString());

			StringBuilder categoryListCreator = new StringBuilder();
			for (final String s : getCategoryFolder(rule.getCategory()).split("/")) {
				if (s.replace(" ", "") != "") {
					categoryListCreator.append(TAB + "listFolders.push_back(\"" + s + "\");\n");
				}
			}
			ruleCPP = ruleCPP.replaceAll("\\$\\$RULE_PARAM_CATEGORY_LISTER", categoryListCreator.toString());





			///////////////////

			File classRuleH = null, classRuleCPP = null;
			FileOutputStream ruleStreamH = null, ruleStreamCPP = null;

			final File cr = new File(modelerDirectory, getCategoryFolderForFile(category));
			cr.mkdirs();
			classRuleH = new File(modelerDirectory + getCategoryFolderForFile(category), ruleName + ".h");
			classRuleH.createNewFile();

			classRuleCPP = new File(modelerDirectory + getCategoryFolderForFile(category), ruleName + ".cpp");
			classRuleCPP.createNewFile();
			ruleStreamH = new FileOutputStream(classRuleH);
			ruleStreamCPP = new FileOutputStream(classRuleCPP);

			ruleStreamCPP.write((ruleCPP).getBytes());
			ruleStreamH.write((ruleH).getBytes());
			ruleStreamCPP.close();
			ruleStreamH.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportModeler(JMEModeler modeler) {
		ArrayList<JMERule> ruleToExport = exportModelerOnly(modeler);
		for (final JMERule r : ruleToExport) {
			System.err.println("GENERATION OF RULE: " + r.getFullName());
			exportRule(r);
		}
	}

	public static ArrayList<JMERule> exportModelerOnly(JMEModeler modeler) {
		String path = modeler.getDestDir();
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
			path += modeler.getDestDir();
		}
		

		createProFile(path, modeler);
		
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

		StringBuilder modelerH = new StringBuilder();
		StringBuilder modelerCPP = new StringBuilder();

		int indexOfModelerName = subrep.lastIndexOf(File.separatorChar);
		if (indexOfModelerName < 0)
			indexOfModelerName = 0;
		String modelerName = modeler.getName();

		modelerH.append("#ifndef __" + modelerName + "__\n#define __" + modelerName + "__\n");
		modelerCPP.append("#include \"" + modelerName + ".h\"\n\n");

		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if(e.getFileHeader().length()>0)
				modelerH.append("#include \"" + e.getFileHeader() + "\"\n");
		}

		modelerH.append(baseImport);
		modelerCPP.append("\n/* Rules import */\n");

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
				String ruleCategory = getCategoryFolder(r.getCategory());
				modelerCPP.append(
						"#include \"" + ruleCategory + (ruleCategory.length() > 0 ? "/" : "") + r.getName() + ".h\"\n");
			}
		}
		modelerCPP.append("\n\n");
		modelerH.append("\n");

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

		GeneratedLanguage prec = new GeneratedLanguage();
		try {
			prec = Translator.translate(modeler.getHeader(), new JerboaLanguageGlue(modeler), new TranslatorContextV2(modeler), prec, modeler,
					exportLanguage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelerH.append("// ## BEGIN Modler header\n");
		modelerH.append(prec.getInclude());
		modelerH.append("// ## END Modler header\n");

		// class definition

		modelerH.append("/**\n * ");
		modelerH.append(modeler.getComment().replaceAll("[\\n|\\r]", "\\n"));
		modelerH.append("\n */\n\n");
		modelerH.append("using namespace jerboa;\n\n");

		String[] modelerPackage = getModelerModuleSplited(modeler);
		for (String n : modelerPackage) {
			if (n.replaceAll("\\s", "").length() > 0) {
				modelerH.append("namespace " + n + " {\n\n");
				modelerCPP.append("namespace " + n + " {\n\n");
			}
		}

		modelerH.append("class ").append(modelerName).append(" : public JerboaModeler {\n");
		modelerH.append("// BEGIN USER DECLARATION\n");
		modelerH.append(prec.getContent() + "\n");
		modelerH.append("// END USER DECLARATION\n\n");

		// Ajout des plongements en attributs
		if (modeler.getEmbeddings().size() > 0) {
			modelerH.append("protected: \n");
		}
		for (final JMEEmbeddingInfo ebd : modeler.getEmbeddings()) {
			final String name = ebd.getName();
			modelerH.append(TAB).append("JerboaEmbeddingInfo* ").append(name).append(";\n");

		}

		modelerH.append("\npublic: \n");

		// Constructor
		modelerH.append(TAB);
		modelerH.append(modelerName);
		modelerH.append("();\n");

		modelerCPP.append(modelerName);
		modelerCPP.append("::").append(modelerName).append("() : JerboaModeler(\"");
		modelerCPP.append(modelerName);
		modelerCPP.append("\",").append(modeler.getDimension()).append("){\n\n");


		modelerCPP.append(TAB).append("// BEGIN USER HEAD CONSTRUCTOR TRANSLATION").append("\n");
		modelerCPP.append(prec.getInClassConstructor() + "\n");
		modelerCPP.append(TAB).append("// END USER HEAD CONSTRUCTOR TRANSLATION").append("\n");

		modelerCPP.append(TAB + "gmap_ = new JerboaGMapArray(this);\n\n");

		int countEmb = 0;
		for (final JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			final String name = e.getName();
			modelerCPP.append(TAB);
			modelerCPP.append(name);
			modelerCPP.append(" = new JerboaEmbeddingInfo(\"");
			modelerCPP.append(e.getName());
			modelerCPP.append("\", JerboaOrbit(" + (e.getOrbit().size() > 0 ? e.getOrbit().size() + "," : ""));

			for (int j = 0; j < e.getOrbit().size(); j++) {
				final int orbit = e.getOrbit().get(j);
				if (j > 0) {
					modelerCPP.append(",");
				}
				modelerCPP.append(String.valueOf(orbit));
			}
			modelerCPP.append(")/*, (JerboaEbdType)typeid(");
			final String eType = getEbdType(e.getType());
			modelerCPP.append(eType);// TODO: verifier cette ligne :
			// .substring(eType.lastIndexOf("::") +
			// 1));
			modelerCPP.append(")*/," + countEmb + ");\n");
			countEmb++;
		}

		modelerCPP.append(TAB);
		modelerCPP.append("this->init();\n");
		for (final JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			modelerCPP.append(TAB);
			modelerCPP.append("this->registerEbds(");
			modelerCPP.append(e.getName());
			modelerCPP.append(");\n");
		}

		modelerCPP.append("\n" + TAB + "// Rules\n");
		for (final JMERule r : ruleToExport) {
			modelerCPP.append(TAB);
			modelerCPP.append("registerRule(new ");
			modelerCPP.append(r.getName());
			modelerCPP.append("(this));\n");
		}

		modelerCPP.append("}\n\n");
		modelerH.append(TAB + "virtual ~").append(modelerName).append("();\n");

		// *******************{
		for (final JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			modelerH.append(TAB);
			final String ename = e.getName();
			modelerH.append("JerboaEmbeddingInfo* get").append(ename).append("()const;\n");
			modelerCPP.append("JerboaEmbeddingInfo* ").append(modelerName).append("::get").append(ename)
			.append("()const {\n").append(TAB).append("return ").append(ename).append(";\n").append("}\n\n");
		}
		// *******************}
		// end class
		modelerH.append("};// end modeler;\n\n");

		modelerCPP.append(modelerName).append("::~").append(modelerName).append("(){\n");
		for (final JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			final String name = e.getName();
			modelerCPP.append(TAB);
			modelerCPP.append(name);
			modelerCPP.append(" = NULL;\n");
		}
		modelerCPP.append("}\n");

		for (String n : modelerPackage) {
			if (n.replaceAll("\\s", "").length() > 0) {
				modelerH.append("}	// namespace " + n + "\n");
				modelerCPP.append("}	// namespace " + n + "\n");
			}
		}
		modelerH.append("#endif");


		File classModelerH = null, classModelerCPP = null;
		FileOutputStream modelerStreamCPP = null, modelerStreamH = null;

		try {
			classModelerH = new File(realPath + modelerName + ".h");
			classModelerH.createNewFile();
			classModelerCPP = new File(realPath + modelerName + ".cpp");
			classModelerCPP.createNewFile();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			modelerStreamH = new FileOutputStream(classModelerH);
			modelerStreamCPP = new FileOutputStream(classModelerCPP);
			modelerStreamCPP.write((modelerCPP.toString()).getBytes());
			modelerStreamH.write((modelerH.toString()).getBytes());

			modelerStreamH.close();
			modelerStreamCPP.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Modeler exported at " + path);
		return ruleToExport;
	}

	public static void exportRule(JMERule r) {
		String path = r.getModeler().getDestDir();
		if (!(path.startsWith("/") || path.startsWith("\\"))) {
			int indexSep = (new StringBuilder(r.getModeler().getFileJME()).reverse().toString()).indexOf('/');
			if (indexSep == -1)
				indexSep = (new StringBuilder(r.getModeler().getFileJME()).reverse().toString()).indexOf('\\');
			path = (new StringBuilder(
					(new StringBuilder(r.getModeler().getFileJME()).reverse().toString()).substring(indexSep)).reverse()
					.toString());
			if (!(path.endsWith("/") || path.endsWith("\\"))) {
				path += File.separatorChar;
			}
			path += r.getModeler().getDestDir();
		}
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
			if(einf.getFileHeader()!=null 
					&& einf.getFileHeader().length()>0
					&& !builder.toString().contains(einf.getFileHeader())) {
				builder.append("#include <");
				builder.append(einf.getFileHeader());
				builder.append("> // Embedding '");
				builder.append(einf.getName());
				builder.append("'\n");
			}
		}
		return builder.toString();
	}

	private static String getEbdType(String type) {
		return type.replaceAll("\\.", "::");
	}

	private static String getEbdUnserializeTest(JMEModeler modeler) {
		StringBuilder builder = new StringBuilder();
		int cpt=0;
		for(JMEEmbeddingInfo einf: modeler.getEmbeddings()) {
			if(cpt>0) {
				builder.append("else ");
			}
			builder.append("if(ebdName==\""+einf.getName()+"\"){\n");
			builder.append("\t\treturn " + einf.getType() + "::unserialize(valueSerialized);\n\t}");
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
			builder.append("if(ebdinf->id()=="+cpt+"){ //" + einf.getName() + "\n");
			builder.append("\t\treturn dynamic_cast<" + einf.getType() + "*>(ebd)->serialize();\n\t}");
			cpt++;
		}
		return builder.toString();
	}

	private static String getModelerModuleNamespace(JMEModeler modeler, String endStringToAdd) {
		String module = modeler.getModule().replaceAll("[\\./:/]+", "::");
		if (!module.endsWith(endStringToAdd) && !module.endsWith("\\") && module.length() > 0) {
			module += endStringToAdd;
		}
		return module;
	}

	private static String getModelerModulePath(JMEModeler modeler, String endStringToAdd) {
		String module = modeler.getModule().replaceAll("[\\./:/]+", "/");
		if (!module.endsWith("/") && !module.endsWith("\\") && module.length() > 0) {
			module += endStringToAdd;
		}
		return module;
	}

	private static String[] getModelerModuleSplited(JMEModeler modeler) {
		return modeler.getModule().split("[\\./:/]+");
	}

	private static String getRulePath(JMERule r) {
		String path = getModelerModulePath(r.getModeler(), "/");
		path += getCategoryFolder(r.getCategory());
		if (path.length() > 0 && !path.endsWith("/"))
			path += "/";
		return path.replaceAll("\\.", "/") + r.getName();
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

			content.append(TAB + "JerboaRuleNode* l").append(name);
			content.append(" = new JerboaRuleNode(this,\"").append(name);
			content.append("\", ").append(i).append(", JerboaRuleNodeMultiplicity");

			if (n.getMultiplicity().getMinChar() == "*" && n.getMultiplicity().getMaxChar() == "*") {
				content.append("()");
			} else {
				content.append("(").append(n.getMultiplicity().getMin()).append(",")
				.append(n.getMultiplicity().getMax()).append(")");
			}
			content.append(", JerboaOrbit(" + (n.getOrbit().size() > 0 ? n.getOrbit().size() + "," : ""));

			for (int j = 0; j < n.getOrbit().size(); j++) {
				final int orbit = n.getOrbit().get(j);
				if (j > 0) {
					content.append(",");
				}
				content.append(String.valueOf(orbit));
			}
			content.append("), "+n.getX() +","+n.getY()+");\n");

			if (i == leftGraph.size() - 1) {
				content.append("\n");
			}

			// left JerboaDarts list
			leftJerboaDart.append(TAB);
			leftJerboaDart.append("_left.push_back(l").append(name);
			leftJerboaDart.append(");\n");

			// hook list
			if (n.getKind() == JMENodeKind.HOOK) {
				hookJerboaDart.append(TAB);
				hookJerboaDart.append("_hooks.push_back(l").append(name);
				hookJerboaDart.append(");\n");
			}
		}
		// left Edge
		for (JMEArc arc : rop.getLeft().getArcs()) {
			leftJerboaEdge.append(TAB);
			leftJerboaEdge.append("l").append(arc.getSource().getName());
			leftJerboaEdge.append("->alpha(").append(arc.getDimension());
			leftJerboaEdge.append(", l").append(arc.getDestination().getName()).append(");\n");
		}

		/**
		 * right graph
		 */

		content.append(TAB).append("std::vector<JerboaRuleExpression*> exprVector;\n\n");
		int count = 0;
		for (JMENode n : rightGraph) {
			// embExpressions
			String nodeName = n.getName();
			if (printEmbeddingExpr) {
				ArrayList<JMENodeExpression> allexprs = new ArrayList<>();
				allexprs.addAll(n.getExplicitExprs());
				allexprs.addAll(n.getDefaultExprs());
				for (final JMENodeExpression expr : allexprs) {
					content.append(TAB).append("exprVector.push_back(new ");
					content.append(rop.getName());
					content.append("ExprR");
					content.append(nodeName);
					content.append(expr.getEbdInfo().getName());
					content.append("(this));\n");
					expressions.add(expr);
				}
			}
			// right JerboaDarts
			content.append(TAB).append("JerboaRuleNode* r").append(nodeName);
			content.append(" = new JerboaRuleNode(this,\"").append(nodeName);
			content.append("\", ").append(count).append(", JerboaRuleNodeMultiplicity");

			if (n.getMultiplicity().getMinChar() == "*" && n.getMultiplicity().getMaxChar() == "*") {
				content.append("()");
			} else {
				content.append("(").append(n.getMultiplicity().getMin()).append(",")
				.append(n.getMultiplicity().getMax()).append(")");
			}
			content.append(", JerboaOrbit(" + (n.getOrbit().size() > 0 ? n.getOrbit().size() + "," : ""));

			for (int j = 0; j < n.getOrbit().size(); j++) {
				final int orbit = n.getOrbit().get(j);
				if (j > 0) {
					content.append(",");
				}
				content.append(String.valueOf(orbit));
			}
			content.append(")"); // fin Orbit

			content.append(",exprVector, "+n.getX() +","+n.getY()+");\n");// fin JerboaRuleNode

			// right JerboaDart list
			rightJerboaDart.append(TAB);
			rightJerboaDart.append("_right.push_back(r").append(n.getName());
			rightJerboaDart.append(");\n");

			content.append(TAB).append("exprVector.clear();\n\n");
			count++;
		}
		// right Edges
		for (JMEArc a : rop.getRight().getArcs()) {
			final JMENode target = a.getDestination();
			final JMENode source = a.getSource();
			rightJerboaEdge.append(TAB);
			rightJerboaEdge.append("r").append(source.getName());
			rightJerboaEdge.append("->alpha(").append(a.getDimension());
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
}
