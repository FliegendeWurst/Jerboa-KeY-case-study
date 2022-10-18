package fr.up.xlim.sic.ig.jerboa.jme.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;

/* Regarder Velocity */
public class CPPExport_New_Engine {
	
	private static ExportLanguage exportLanguage = ExportLanguage.CPP_V2;
	

	private static final String TAB = "    ";

	private static final String baseImport = "\n#include <core/jerboamodeler.h>\n"
			+ "#include <coreutils/jerboagmaparray.h>\n" + "#include <core/jerboagmap.h>\n"
			+ "#include <core/jerboaRuleOperation.h>\n" + "#include <serialization/jbaformat.h>\n";

	private static String getCategoryFolder(final String s) {
		String res = new String(s);
		if (s.length() > 0)
			return res.replaceAll("[\\.|:|;|,|\\\\]", "/");
		return "";
	}

	private static String getCategoryFolderForFile(final String s) {
		return getCategoryFolder(s).replaceAll("[/+\\.]", "/");
	}

	private static String getRulePath(JMERule r) {
		String path = getModelerModulePath(r.getModeler(), "/");
		path += getCategoryFolder(r.getCategory());
		if (path.length() > 0 && !path.endsWith("/"))
			path += "/";
		return path.replaceAll("\\.", "/") + r.getName();
	}

	private static void createPriFile(String modelerDirectory, JMEModeler modeler) {
		StringBuilder priFileContent = new StringBuilder();
		priFileContent.append("#-------------------------------------------------\n" + "#\n"
				+ "# Project created by JerboaModelerEditor \n" + "# Date : " + new Date() + "\n#\n" + "# "
				+ modeler.getComment().replace("\n", "\n# ") + "\n" + "#\n"
				+ "#-------------------------------------------------\n");
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
		StringBuilder proFileContent = new StringBuilder();
		File proFile = null;
		createPriFile(modelerDirectory, modeler);
		proFile = new File(modelerDirectory, modeler.getName() + ".pro");
		if (proFile.exists()) // on n'Ã©crase pas le .pro
			return;

		String bridgeName = createBridge(modelerDirectory, modeler);

		createMainFile(modelerDirectory, modeler, bridgeName);

		proFileContent.append("#-------------------------------------------------\n" + "#\n"
				+ "# Project created by JerboaModelerEditor \n" + "# Date : " + new Date() + "\n#\n" + "# "
				+ modeler.getComment().replace("\n", "\n# ") + "\n" + "#\n"
				+ "#-------------------------------------------------\n");
		proFileContent.append("include($$PWD/");
		proFileContent.append(modeler.getName());
		proFileContent.append(".pri)\n\n");

		proFileContent.append("QT       += gui widgets opengl\n"
				+ "unix:!macx {\n" + 
				"    QMAKE_CXXFLAGS += -std=c++11  -Wno-unused-parameter -Wno-unused-variable -Wno-sign-compare\n" + 
				"    QMAKE_CXXFLAGS += -fopenmp\n" + 
				"    LIBS += -fopenmp\n" + 
				"}\n" + 
				"win32-g++ {\n" + 
				"    QMAKE_CXXFLAGS += -std=c++11  -Wno-unused-parameter -Wno-unused-variable -Wno-sign-compare\n" + 
				"    QMAKE_CXXFLAGS += -fopenmp\n" + 
				"    LIBS += -fopenmp\n" + 
				"}" +
				"TARGET = " + modeler.getName() + "\n" + "TEMPLATE =  app\n" + "\n" +
				"INCLUDEPATH += $$PWD/" + getModelerModulePath(modeler, "/").replaceAll("\\.", "/") + "\n" + "\n" + "ARCH = \"_86\"\n"+
				"contains(QT_ARCH, i386) {\n" + 
				"    message(\"compilation for 32-bit\")\n" + 
				"}else{\n" + 
				"    message(\"compilation for 64-bit\")\n" + 
				"    ARCH =\"_64\"\n" + 
				"}\n" + 
				"INCLUDE = $$PWD/include\n" + 
				"BIN     = $$PWD/bin\n" + 
				"BUILD   = $$PWD/build\n" + 
				"SRC     = $$PWD/src\n" + 
				"\n" + 
				"CONFIG(debug, debug|release) {\n" + 
				"	DESTDIR = $$BIN/debug$$ARCH\n" + 
				"	OBJECTS_DIR = $$BUILD/debug$$ARCH/.obj\n" + 
				"	MOC_DIR = $$BUILD/debug$$ARCH/.moc\n" + 
				"	RCC_DIR = $$BUILD/debug$$ARCH/.rcc\n" + 
				"	UI_DIR = $$BUILD/debug$$ARCH/.ui\n" + 
				"	OBJECTS_DIR = $$BUILD/debug$$ARCH/object\n" + 
				"} else {\n" + 
				"	DESTDIR = $$BIN/release$$ARCH\n" + 
				"	OBJECTS_DIR = $$BUILD/release$$ARCH/.obj\n" + 
				"	MOC_DIR = $$BUILD/release$$ARCH/.moc\n" + 
				"	RCC_DIR = $$BUILD/release$$ARCH/.rcc\n" + 
				"	UI_DIR = $$BUILD/release$$ARCH/.ui\n" + 
				"	OBJECTS_DIR = $$BUILD/release$$ARCH/object\n" + 
				"}\n" + 
				"");

		proFileContent.append("SOURCES +=");
		proFileContent.append("\tmain" + modeler.getName() + ".cpp");
		proFileContent.append("\\\n\t" + bridgeName + ".cpp");
		proFileContent.append("\n\nHEADERS +=");
		proFileContent.append("\\\n\t" + bridgeName + ".h");

		/** Jerboa library **/

		proFileContent.append("\n\n\n# JERBOA library\n" + 
				"message(\"TODO: Change JERBOADIR in pro file\")" +
				"# TODO : Change JERBOADIR\n" + 
				"JERBOADIR = $$PWD/../../Jerboa++\n" + 
				"JERBOALIBDIR = $$JERBOADIR/lib/debug$$ARCH\n" + 
				"if(CONFIG(release, debug|release)){\n" + 
				"    JERBOALIBDIR = $$JERBOADIR/lib/release$$ARCH\n" + 
				"}\n" + 
				"LIBS += -L$$JERBOALIBDIR -lJerboa\n" + 
				"message(\"Jerboa lib is taken in : \" + $$JERBOALIBDIR)\n" + 
				"\n" + 
				"INCLUDEPATH += $$JERBOADIR/include\n" + 
				"DEPENDPATH += $$JERBOADIR/include\n" + 
				"\n\n");

		/** JerboaModelerViewer library **/
		proFileContent.append("\n# JeMoViewer library\n" + 
				"# TODO : Change JERBOA_MODELER_VIEWER_SRC_PATH\n" + 
				"message(\"TODO: Change JERBOA_MODELER_VIEWER_SRC_PATH in pro file\")\n" +
				"JERBOA_MODELER_VIEWER_SRC_PATH = $$PWD/../../JeMoViewer\n" + 
				"JERBOA_MODELER_VIEWERPATH = $$JERBOA_MODELER_VIEWER_SRC_PATH/lib/debug$$ARCH\n" + 
				"if(CONFIG(release, debug|release)){\n" + 
				"    JERBOA_MODELER_VIEWERPATH = $$JERBOA_MODELER_VIEWER_SRC_PATH/lib/release$$ARCH\n" + 
				"}\n" + 
				"LIBS += -L$$JERBOA_MODELER_VIEWERPATH -lJeMoViewer\n" + 
				"message(\"JeMoViewer lib is taken in : \" + $$JERBOA_MODELER_VIEWERPATH)\n" + 
				"\n" + 
				"INCLUDEPATH += $$JERBOA_MODELER_VIEWER_SRC_PATH/include\n" + 
				"DEPENDPATH += $$JERBOA_MODELER_VIEWER_SRC_PATH/include\n" + 
				"\n" + 
				"win32{ RC_FILE = $$JERBOA_MODELER_VIEWER_SRC_PATH/rc_icon_win.rc }\n" + 
				"unix:!macx{}\n" + 
				"macx{ ICON = $$JERBOA_MODELER_VIEWER_SRC_PATH/images.jerboaIcon.ics }\n");

		FileOutputStream proFileStream = null;

		try {
			proFile.createNewFile();
			proFileStream = new FileOutputStream(proFile);
			proFileStream.write((proFileContent.toString()).getBytes());
			proFileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getModelerModulePath(JMEModeler modeler, String endStringToAdd) {
		String module = modeler.getModule().replaceAll("[\\./:/]+", "/");
		if (!module.endsWith("/") && !module.endsWith("\\") && module.length() > 0) {
			module += endStringToAdd;
		}
		return module;
	}
	
	private static String getModelerModuleNamespace(JMEModeler modeler, String endStringToAdd) {
		String module = modeler.getModule().replaceAll("[\\./:/]+", "::");
		if (!module.endsWith(endStringToAdd) && !module.endsWith("\\") && module.length() > 0) {
			module += endStringToAdd;
		}
		return module;
	}
	
	
	private static String[] getModelerModuleSplited(JMEModeler modeler) {
		return modeler.getModule().split("[\\./:/]+");
	}

	private static String createBridge(String modelerDirectory, JMEModeler modeler) {

		StringBuilder bridgeFileContentH = new StringBuilder();
		StringBuilder bridgeFileContentCPP = new StringBuilder();

		final String serializerName = "Serializer_" + modeler.getName();
		final String bridgeName = "Bridge_" + modeler.getName();

		String modelerName = getModelerModuleNamespace(modeler, "::") + modeler.getName();

		// faire les import

		bridgeFileContentH.append("#ifndef __" + bridgeName + "__\n");
		bridgeFileContentH.append("#define __" + bridgeName + "__\n\n");
		bridgeFileContentH.append("#include <core/jemoviewer.h>\n" + 
				"#include <core/jerboamodeler.h>\n" + 
				"#include <serialization/serialization.h>\n" + 
				"#include <core/jerboadart.h>\n\n");

		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if(e.getFileHeader().length()>0)
				bridgeFileContentH.append("#include \"" + e.getFileHeader() + "\"\n");
		}

		bridgeFileContentH.append("#include \"" + getModelerModulePath(modeler, "/") + modeler.getName() + ".h\"\n\n");

		bridgeFileContentCPP.append("#include \"" + bridgeName + ".h\"\n\n");

		/** Begin Serializer **/

		bridgeFileContentH.append("\n\nclass " + serializerName + " : public jerboa::EmbeddginSerializer{\n");
		bridgeFileContentH.append("private :\n");
		bridgeFileContentH.append("\t" + modelerName + "* modeler;\n");
		bridgeFileContentH.append("public :\n");
		bridgeFileContentH.append("\t" + serializerName + "(){}\n");
		bridgeFileContentH.append("\t~" + serializerName + "(){}\n\n");

		bridgeFileContentH.append(
				"// Inherited\n" + 
				"    JerboaEmbedding* unserialize(std::string ebdName, std::string valueSerialized)const;\n" + 
				"    std::string serialize(JerboaEmbeddingInfo* ebdinf,JerboaEmbedding* ebd)const;\n" + 
				"\n" + 
				"    std::string ebdClassName(JerboaEmbeddingInfo* ebdinf)const;\n" + 
				"    int ebdId(std::string ebdName, JerboaOrbit orbit)const;\n" + 
				"\n" + 
				"    std::string positionEbd()const;\n" + 
				"    std::string colorEbd()const;\n" + 
				"    std::string orientEbd()const;\n\n" + 
				"}; // end Class");
		
		bridgeFileContentCPP.append("jerboa::JerboaEmbedding* " + serializerName
				+ "::unserialize(std::string ebdName, std::string valueSerialized)const{\n");
		bridgeFileContentCPP.append("\t/** TODO: replace by your own embeddings **/\n");
		bridgeFileContentCPP.append("\tint eid = ebdId(ebdName,JerboaOrbit());\n\t");
		int cpt=0;
		for(JMEEmbeddingInfo ei: modeler.getEmbeddings()) {
			bridgeFileContentCPP.append("if(eid=="+cpt+"){\n");
			bridgeFileContentCPP.append("\t\treturn " + ei.getType() + "::unserialize(valueSerialized);\n\t}else ");
			cpt++;
		}
		bridgeFileContentCPP.append("\n");
		bridgeFileContentCPP
		.append("\t\tstd::cerr << \"No serialization found for \" << ebdName << \" please see class <"
				+ serializerName + ">\" << std::endl;\n"+
				"\treturn NULL;\n"+
				"}\n");

		bridgeFileContentCPP.append(
				"std::string " + serializerName + "::ebdClassName(jerboa::JerboaEmbeddingInfo* ebdinf)const{\n");
		bridgeFileContentCPP.append("\t/** TODO: replace by your own embeddings **/\n");
		bridgeFileContentCPP.append("\tint eid = ebdId(ebdinf->name(),ebdinf->orbit());\n\t");
		cpt=0;
		for(JMEEmbeddingInfo ei: modeler.getEmbeddings()) {
			bridgeFileContentCPP.append("if(eid=="+cpt+"){\n");
			bridgeFileContentCPP.append("\t\treturn \"" + ei.getType() + "\";\n\t}else ");
			cpt++;
		}
		bridgeFileContentCPP.append("{\n");
		bridgeFileContentCPP.append("\t\tstd::cerr << \"No serialization found : please see class <" + serializerName
				+ ">\" << std::endl;\n");
		bridgeFileContentCPP.append("\t\treturn \"\";\n");
		bridgeFileContentCPP.append("\t}\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("std::string " + serializerName
				+ "::serialize(jerboa::JerboaEmbeddingInfo* ebdinf,jerboa::JerboaEmbedding* ebd)const{\n");
		bridgeFileContentCPP.append("\t/** TODO: replace by your own embeddings **/\n");
		bridgeFileContentCPP.append("\tif(!ebd) return \"NULL\";\n" + 
				"    int eid = ebdId(ebdinf->name(),ebdinf->orbit());\n\t");

		cpt=0;
		for(JMEEmbeddingInfo ei: modeler.getEmbeddings()) {
			bridgeFileContentCPP.append("if(eid=="+cpt+"){\n");
			bridgeFileContentCPP.append("\t\treturn ((" + ei.getType() + "*)ebd)->serialize();\n\t}else ");
			cpt++;
		}
		bridgeFileContentCPP.append("{\n");
		bridgeFileContentCPP.append("\t\tstd::cerr << \"No serialization found : please see class <" + serializerName
				+ ">\" << std::endl;\n");
		bridgeFileContentCPP.append("\t\treturn \"\";\n");
		bridgeFileContentCPP.append("\t}\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP
		.append("int " + serializerName + "::ebdId(std::string ebdName, jerboa::JerboaOrbit orbit)const{\n");
		bridgeFileContentCPP.append("\t/** TODO: replace by your own embeddings **/\n");
		bridgeFileContentCPP.append("\tif(ebdName.size()==0)return -1;\n\t");
		cpt = 0;
		for(JMEEmbeddingInfo ei: modeler.getEmbeddings()) {
			bridgeFileContentCPP.append("else if(ebdName==\""+ei.getName()+"\")");
			bridgeFileContentCPP.append("return " + cpt + ";\n\t");
			cpt++;
		}
		bridgeFileContentCPP.append("else return -1;\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("std::string " + serializerName + "::positionEbd() const{\n");
		bridgeFileContentCPP.append("\t/** TODO: replace by your own position Embedding name **/\n");
		bridgeFileContentCPP.append("\treturn \"point\";\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("std::string " + serializerName + "::colorEbd() const{\n");
		bridgeFileContentCPP.append("\t/** TODO: replace by your own color Embedding name **/\n");
		bridgeFileContentCPP.append("\treturn \"color\";\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("std::string " + serializerName + "::orientEbd() const{\n");
		bridgeFileContentCPP.append("\t/** TODO: replace by your own orientation Embedding name **/\n");
		bridgeFileContentCPP.append("\treturn \"orient\";\n");
		bridgeFileContentCPP.append("}\n");


		/** End Serializer **/

		bridgeFileContentCPP.append("\n\n/** Bridge Functions **/\n\n");

		/** Begin Bridge **/

		bridgeFileContentH.append("\n\nclass " + bridgeName + ": public jerboa::JerboaBridge{\n");
		bridgeFileContentH.append("private:\n");
		bridgeFileContentH.append("\tjerboa::JerboaGMap* gmap;\n");

		bridgeFileContentH.append("public:\n");

		bridgeFileContentH.append("\t" + bridgeName + "();\n");
		bridgeFileContentCPP.append("" + bridgeName + "::" + bridgeName + "():jerboa::JerboaBridge(new " + modelerName + "(),"
				+ "new " + serializerName + "()){\n");
		bridgeFileContentCPP.append("\tgmap=modeler->gmap();\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentH.append("\t~" + bridgeName + "();\n\n");
		bridgeFileContentCPP.append(bridgeName + "::~" + bridgeName + "(){\n");
		bridgeFileContentCPP.append("\tgmap=NULL;\n");
		bridgeFileContentCPP.append("}\n\n");

		bridgeFileContentH.append("    // Inherited\n" + 
				"    bool coord(const JerboaDart* n, QVector3D& coordinate)const;\n" + 
				"    std::string coordEbdName()const;\n" + 
				"\n" + 
				"    bool hasColor()const;\n" + 
				"    bool color(const JerboaDart* n, QColor& color)const;\n" + 
				"    std::string colorEbdName()const;\n" + 
				"\n" + 
				"    bool hasOrient()const;\n" + 
				"    bool orient(const JerboaDart* n)const;\n" + 
				"\n" + 
				"    bool hasNormal()const;\n" + 
				"    bool normal(JerboaDart* n, QVector3D& normal)const;\n" + 
				"\n" + 
				"    std::string toString(JerboaEmbedding* e)const;\n" + 
				"    JerboaOrbit getEbdOrbit(const std::string& name)const;\n" + 
				"\n" + 
				"    void extractInformationFromJBAFormat(std::string input);\n" + 
				"    std::string getExtraInformationForJBAFormat();\n" + 
				"\n" + 
				"    void doCommand(const std::string& cmd);\n" + 
				"    std::vector<std::pair<std::string,std::string>> getCommandLineHelper(const std::string& cmd)const;\n" + 
				"};// end Class\n");
		

		bridgeFileContentCPP.append("bool " + bridgeName + "::coord(const JerboaDart* n, QVector3D& coordinate)const{\n");
		bridgeFileContentCPP.append("\t/** TODO: do your own stuff here **/\n");
		bridgeFileContentCPP.append("    Point3D* v = (Point3D*)n->ebd(modeler->getEmbedding(coordEbdName())->id()); \n" +
				"// replace by the id of your position embedding (e.g. \"n->ebd(2)\") for more efficient computation\n" + 
				"    if(!v)return false;\n" + 
				"    coordinate = *v;\n" + 
				"    return true;\n" + 
				"}\n");

		bridgeFileContentCPP.append("std::string " + bridgeName + "::coordEbdName()const{\n");
		bridgeFileContentCPP.append("\t/** TODO: do your own stuff here **/\n");
		bridgeFileContentCPP.append("\treturn \"point\";\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("bool " + bridgeName + "::hasColor()const{\n");
		bridgeFileContentCPP.append("\t/** TODO: do your own stuff here **/\n");
		bridgeFileContentCPP.append("\treturn true;\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("bool " + bridgeName + "::color(const JerboaDart* n, QColor& color)const{\n" + 
				"/** TODO: do your own stuff here **/\n" +
				"    Color* c = (Color*)n->ebd(modeler->getEmbedding(colorEbdName())->id()); \n" + 
				"// replace by the id of your color embedding (e.g. \"n->ebd(2)\") for more efficient computation\n" + 
				"    if(!c)return false;\n" + 
				"    color = QColor(c->getR()*255,c->getG()*255,c->getB()*255,c->getA()*255);\n" + 
				"    return true;\n" + 
				"}\n");
		
		bridgeFileContentCPP.append("std::string " + bridgeName + "::colorEbdName()const{\n");
		bridgeFileContentCPP.append("\t/** TODO: do your own stuff here **/\n");
		bridgeFileContentCPP.append("\treturn \"color\";\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("bool " + bridgeName + "::hasOrient()const{\n");
		bridgeFileContentCPP.append("\t/** TODO: do your own stuff here **/\n");
		bridgeFileContentCPP.append("\treturn true;\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("bool " + bridgeName + "::orient(const JerboaDart* n)const{\n" + 
				"/** TODO: do your own stuff here **/\n" +
				"    JBoolean* b = (JBoolean*)n->ebd(0);\n" + 
				"    if(b)return b->val();\n" + 
				"    return false;\n" + 
				"}\n");
		
		bridgeFileContentCPP.append("bool " + bridgeName + "::hasNormal()const{\n");
		bridgeFileContentCPP.append("\t/** TODO: do your own stuff here **/\n");
		bridgeFileContentCPP.append("\treturn true;\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("bool " + bridgeName + "::normal(JerboaDart* n, QVector3D& normal)const{\n" + 
				"    if(hasOrient()){\n" + 
				"        // pour modele charge, on test juste si le plongement existe\n" + 
				"        jerboa::JerboaDart* tmp = n;\n" + 
				"        if(orient(n)){\n" + 
				"            tmp = tmp->alpha(1);\n" + 
				"        }\n" + 
				"        QVector3D p0,p1,p2;\n" + 
				"        if(coord(tmp,p0)){\n" + 
				"            if(coord(tmp->alpha(0),p1)){\n" + 
				"                if(coord(tmp->alpha(1)->alpha(0),p2)){\n" + 
				"                    normal = QVector3D::crossProduct(p1-p0,p2-p0);\n" + 
				"                    return true;\n" + 
				"                }\n" + 
				"            }\n" + 
				"        }\n" + 
				"        // If not worked\n" + 
				"        jerboa::JerboaMark markview = gmap->getFreeMarker();\n" + 
				"        while(tmp->isNotMarked(markview)){\n" + 
				"            tmp->mark(markview);\n" + 
				"            if(coord(tmp,p0)){\n" + 
				"                if(coord(tmp->alpha(0),p1)){\n" + 
				"                    if(coord(tmp->alpha(1)->alpha(0),p2)){\n" + 
				"                        normal = QVector3D::crossProduct(p1-p0,p2-p0);\n" + 
				"                        return true;\n" + 
				"                    }\n" + 
				"                }\n" + 
				"            }\n" + 
				"            tmp = tmp->alpha(0)->alpha(1);\n" + 
				"        }\n" + 
				"        gmap->freeMarker(markview);\n" + 
				"    }\n" + 
				"    return false;\n" + 
				"}\n");

		bridgeFileContentCPP.append("std::string " + bridgeName + "::toString(jerboa::JerboaEmbedding* e)const{\n");
		int cptFreshNameToStringBridge = 0;
		for(JMEEmbeddingInfo ei: modeler.getEmbeddings()) {
			bridgeFileContentCPP.append("\t" + ei.getType() + " je_" + cptFreshNameToStringBridge + ";\n");
			cptFreshNameToStringBridge++;
		}
		bridgeFileContentCPP.append("\n\t");
		cptFreshNameToStringBridge = 0;
		for(JMEEmbeddingInfo ei: modeler.getEmbeddings()) {
			bridgeFileContentCPP.append("if(typeid(*e)==typeid(je_" + cptFreshNameToStringBridge + "))\n");
			bridgeFileContentCPP.append("\t\treturn (("+ ei.getType() +"*)e)->toString();\n\telse ");
			cptFreshNameToStringBridge++;
		}
		bridgeFileContentCPP.append("return \"----\";\n}\n");
		
		bridgeFileContentCPP.append("jerboa::JerboaOrbit " + bridgeName + "::getEbdOrbit(const std::string& name)const{\n");
		bridgeFileContentCPP.append("\treturn modeler->getEmbedding(name)->orbit();\n");
		bridgeFileContentCPP.append("}\n");
		
		bridgeFileContentCPP.append("void " + bridgeName + "::extractInformationFromJBAFormat(std::string input){\n");
		bridgeFileContentCPP.append("\t/** TODO: fill to load specific information in jba files **/\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("std::string " + bridgeName + "::getExtraInformationForJBAFormat(){\n");
		bridgeFileContentCPP.append("\t/** TODO: fill to save specific information in jba files  **/\n\treturn \"\";\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("void " + bridgeName + "::doCommand(const std::string& cmd){\n");
		bridgeFileContentCPP.append("}\n");

		bridgeFileContentCPP.append("std::vector<std::pair<std::string,std::string>> " +
		bridgeName + "::getCommandLineHelper(const std::string& cmd)const{\n");
		bridgeFileContentCPP.append("\treturn std::vector<std::pair<std::string,std::string>>();\n");
		bridgeFileContentCPP.append("}\n");

	

		bridgeFileContentH.append("\n\n#endif");

		File bridgeFileH = null;
		FileOutputStream bridgeFileStreamH = null;
		File bridgeFileCPP = null;
		FileOutputStream bridgeFileStreamCPP = null;

		try {
			bridgeFileH = new File(modelerDirectory, bridgeName + ".h");
			if (!bridgeFileH.exists()) { // on ne gÃ©nÃ¨re rien si Ã§a existe
				// dÃ©jÃ 
				bridgeFileH.createNewFile();
				bridgeFileStreamH = new FileOutputStream(bridgeFileH);
				bridgeFileStreamH.write((bridgeFileContentH.toString()).getBytes());
				bridgeFileStreamH.close();
			}

			bridgeFileCPP = new File(modelerDirectory, bridgeName + ".cpp");
			if (!bridgeFileCPP.exists()) {
				bridgeFileCPP.createNewFile();
				bridgeFileStreamCPP = new FileOutputStream(bridgeFileCPP);
				bridgeFileStreamCPP.write((bridgeFileContentCPP.toString()).getBytes());
				bridgeFileStreamCPP.close();
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bridgeName;
	}

	private static void createMainFile(String modelerDirectory, JMEModeler modeler, String bridgeName) {

		StringBuilder mainFileContent = new StringBuilder();

		mainFileContent.append("/**-------------------------------------------------\n" + " *\n"
				+ " * Project created by JerboaModelerEditor \n" + " * Date : " + new Date() + "\n *\n" + " * "
				+ modeler.getComment().replace("\n", "\n * ") + "\n" + " *\n"
				+ " *-------------------------------------------------*/\n");

		mainFileContent.append("#include <core/jemoviewer.h>\n" + "#include <QApplication>\n" + "#include <QStyle>\n"
				+ "#include <QtWidgets>\n\n" + "#ifndef WIN32\n" + "#include <unistd.h>\n" + "#endif\n\n"
				+ "#include \"" + bridgeName + ".h\"" + "\n\n");

		mainFileContent.append("int main(int argc, char *argv[]){\n");
		mainFileContent.append("\tsrand(time(NULL));\n");
		mainFileContent.append("\tprintf(\"Compiled with Qt Version %s\", QT_VERSION_STR);\n");
		mainFileContent.append("\tQApplication app(argc, argv);\n");
		mainFileContent.append("\t" + bridgeName + " bridge;\n");
		mainFileContent.append("\tJeMoViewer w(NULL,NULL, &app);\n");
		mainFileContent.append("\tw.setModeler(bridge.getModeler(),(jerboa::JerboaBridge*)&bridge);\n");
		mainFileContent.append(
				"\tapp.setStyle(QStyleFactory::create(QStyleFactory::keys()[QStyleFactory::keys().size()-1]));\n");
		mainFileContent.append("\tapp.setFont(QFont(\"Century\",9));\n");
		mainFileContent.append("\tw.show();\n");

		mainFileContent.append("\tfor(int i=1;i<argc;i++){\n");
		mainFileContent.append("\t\tw.loadModel(argv[i]);\n");
		mainFileContent.append("\t}\n");

		mainFileContent.append("\tint resApp = app.exec();\n");

		mainFileContent.append("\treturn resApp;\n");
		mainFileContent.append("}");

		File proFile = null;
		FileOutputStream proFileStream = null;

		try {
			proFile = new File(modelerDirectory, "main" + modeler.getName() + ".cpp");
			if (!proFile.exists()) { // on ne re-gÃ©nÃ¨re pas si le fichier
				// existe
				// deja
				// TODO: attention cependant, si le modeleur change de nom, le
				// main ne fonctionnera plus
				proFile.createNewFile();
				proFileStream = new FileOutputStream(proFile);
				proFileStream.write((mainFileContent.toString()).getBytes());
				proFileStream.close();
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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
				// exportRule(r);
				// System.err.println("###> " + ruleCategory);
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

		createProFile(path, modeler);

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
		exportRuleCommon(realPath, r);
	}

	private static String getEbdType(String type) {
		return type.replaceAll("\\.", "::");
	}

	private static void exportRuleCommon(String path, JMERule r) {
		final JMEModeler modeler = r.getModeler();
		final String ruleName = r.getName();

		StringBuilder h_import = new StringBuilder();
		StringBuilder c_import = new StringBuilder();

		StringBuilder h_content = new StringBuilder();
		StringBuilder c_content = new StringBuilder();

		h_import.append("#ifndef __" + ruleName + "__\n#define __" + ruleName + "__\n");

		/** Debut des imports **/

		h_import.append("\n#include <cstdlib>\n");
		h_import.append("#include <string>\n");
		h_import.append(baseImport);
		h_import.append("// ## " + modeler.getModule());
		h_import.append("\n#include \"");
		
		if (r.getCategory().length() > 0)
			for (int i = 0; i < r.getCategory().split("[\\./:/]+").length; i++) {
				h_import.append("../");
			}
		h_import.append(modeler.getName() + ".h\"\n");

		c_import.append("#include \"" + getRulePath(r) + ".h\"\n");
		for (JMEEmbeddingInfo e : modeler.getEmbeddings()) {
			if(e.getFileHeader().length()>0)
			c_import.append("#include \"" + e.getFileHeader() + "\"\n");
		}

		String string_exprImport = "";
		String string_exprInClass = "";
		try {
			GeneratedLanguage headerTr = new GeneratedLanguage();
			headerTr = Translator.translate(r.getHeader(), new JerboaLanguageGlue(r, LanguageState.HEADER), new TranslatorContextV2(modeler), headerTr,
					modeler, exportLanguage);
			string_exprImport = headerTr.getInclude();
			string_exprInClass = headerTr.getContent();
		} catch (Exception e) {
			string_exprImport = "#ERROR in expression translation # " + r.getHeader();
			System.err.println("#ERROR in expression translation # ");
			e.printStackTrace();
		}
		h_import.append("/** BEGIN RULE IMPORT **/");
		h_import.append(string_exprImport + "\n\n");
		h_import.append("/** END RULE IMPORT **/\n");

		/**
		 * Imports specifiques des scripts! -> detection des regles utilisee etc.
		 */

		/** Fin des imports **/

		h_content.append("/**\n * " + r.getComment().replace("\n", "\n * ") + "\n */\n\n");

		for (String n : getModelerModuleSplited(modeler)) {
			if (n.replaceAll("\\s", "").length() > 0) {
				h_content.append("namespace " + n + " {\n\n");
				c_content.append("namespace " + n + " {\n\n");
			}
		}
		h_content.append("using namespace jerboa;\n\n");

		// class definition

		h_content.append("class ").append(ruleName).append(" : public ");
		if (r instanceof JMERuleAtomic) {
			h_import.append("#include <coreutils/jerboaRuleGenerated.h>\n");
			h_import.append("#include <core/jerboaRuleExpression.h>\n");
			h_content.append("JerboaRuleGenerated");
		} else if (r instanceof JMEScript) {
			h_import.append("#include <coreutils/jerboaRuleScript.h>\n");
			// c_import.append(getScriptImport((JMEScript) r));
			h_content.append("JerboaRuleScript");
		}

		h_content.append("{\n\n");
		if (string_exprInClass.replaceAll("\\s", "").length() > 0) {
			h_content.append("// --------------- BEGIN Header inclusion\n");
			h_content.append(string_exprInClass);
			h_content.append("// --------------- END Header inclusion\n");
		}
		h_content.append("\n\nprotected:\n");
		h_content.append("\tJerboaFilterRowMatrix *curLeftFilter;\n");

		h_content.append("\n\t/** BEGIN PARAMETERS **/\n\n");
		for (JMEParamEbd e : r.getParamsEbd()) {
			h_content.append("\t" + getEbdType(e.getType()) + " " + e.getName() + ";\n");
		}
		h_content.append("\n\t/** END PARAMETERS **/\n\n");

		h_content.append("\npublic : \n");

		/** Faire les appels aux spï¿½cificitï¿½es **/

		if (r instanceof JMERuleAtomic) {
			exportAtomicRule((JMERuleAtomic) r, h_content, c_content, h_import);
		} else if (r instanceof JMEScript)
			exportScript((JMEScript) r, h_content, c_content, h_import);

		/** Les fonctions classiques des rï¿½gles : **/

		/** DÃ©finition du applyRule avec les paramÃ¨tres ordonnÃ©s **/
		StringBuilder enteteApplyCPP = new StringBuilder();
		StringBuilder contentApplyCPP = new StringBuilder();
		String hookType = "JerboaDart* ";
		String hookListType = "JerboaInputHooksGeneric ";
		String funcAddToHookList = "addCol";
		String hookListName = "_hookList";
		if (r instanceof JMEScript) {
			hookType = "std::vector<JerboaDart*> ";
			// funcAddToHookList = "push_back";
		}

		h_content.append("\n/** BEGIN SPECIFIC APPLYRULE FUNCTIONS **/\n\n");
		h_content.append("\tJerboaRuleResult* applyRuleParam(JerboaGMap* gmap, JerboaRuleResultType _kind");
		// TODO: je change le nom car sinon Ã§a masque la fonction classique

		enteteApplyCPP.append("JerboaRuleResult* ");
		enteteApplyCPP.append(r.getName());
		enteteApplyCPP.append("::applyRuleParam(JerboaGMap* gmap, JerboaRuleResultType _kind");

		contentApplyCPP.append("\t" + hookListType + " " + hookListName + ";\n");

		for (JMEParamTopo tp : r.getParamsTopo()) {
			h_content.append(", ");
			enteteApplyCPP.append(", ");
			h_content.append(hookType + tp.getNode().getName());
			enteteApplyCPP.append(hookType + tp.getNode().getName());
			contentApplyCPP
			.append("\t" + hookListName + "." + funcAddToHookList + "(" + tp.getNode().getName() + ");\n");
		}
		for (JMEParamEbd ep : r.getParamsEbd()) {
			for (int i = 0; i < r.getParamsEbd().size(); i++) {
				if (ep.getOrder() == i) {
					h_content.append(", ");
					enteteApplyCPP.append(", ");
					h_content.append(getEbdType(ep.getType()) + " " + ep.getName());
					// TODO: a voir si on garde Ã§a ou pas.
					if (ep.getInitValue().replaceAll(" ", "").length() > 0) {
						h_content.append(" = " + ep.getInitValue());
					}
					enteteApplyCPP.append(getEbdType(ep.getType()) + " " + ep.getName());

					contentApplyCPP.append("\tset" + ep.getName() + "(" + ep.getName() + ");\n");
				}
			}
		}

		h_content.append(");\n");
		enteteApplyCPP.append("){\n");

		contentApplyCPP.append("\treturn applyRule(gmap, " + hookListName + ", _kind);");

		c_content.append(enteteApplyCPP.toString());
		c_content.append(contentApplyCPP.toString());
		c_content.append("\n}\n");

		// TODO: ajouter la prÃ©condition par ligne !
		if (r.getPrecondition().replaceAll("\\s", "").length() != 0) {
			h_content.append(
					"\n\tbool evalPrecondition(const JerboaGMap* gmap, const std::vector<JerboaFilterRowMatrix*> & leftfilter);\n");
			c_content.append("bool ");
			c_content.append(r.getName());
			c_content.append(
					"::evalPrecondition(const JerboaGMap* gmap, const std::vector<JerboaFilterRowMatrix*> & leftfilter){\n");
			GeneratedLanguage prec = new GeneratedLanguage();
			try {
				prec = Translator.translate(r.getPrecondition(), new JerboaLanguageGlue(r, LanguageState.PRECONDITION), new TranslatorContextV2(modeler),
						prec, modeler, exportLanguage);

				c_import.append(prec.getInclude() + "\n");
				c_content.append("\t" + prec.getContent().replaceAll("\\n", "\n\t"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			c_content.append("\n}\n");
		}

		if (r.getPreProcess().replaceAll("\\s", "").length() != 0) {
			h_content.append("\n\tbool preprocess(const JerboaGMap* gmap);\n");
			c_content.append("bool ");
			c_content.append(r.getName());
			c_content.append("::preprocess(const JerboaGMap* gmap){\n");
			GeneratedLanguage prec = new GeneratedLanguage();
			try {
				prec = Translator.translate(r.getPreProcess(), new JerboaLanguageGlue(r, LanguageState.PRECONDITION), new TranslatorContextV2(modeler),
						prec, modeler, exportLanguage);

				c_import.append(prec.getInclude() + "\n");
				c_content.append("\t" + prec.getContent().replaceAll("\\n", "\n\t"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			c_content.append("\n}\n");
		}

		// TODO: ajouter la prÃ©condition par ligne !
		if (r.getMidProcess().replaceAll("\\s", "").length() != 0) {
			h_content.append(
					"\n\tbool midprocess(const JerboaGMap* gmap, const std::vector<JerboaFilterRowMatrix*> & leftfilter);\n");
			c_content.append("bool ");
			c_content.append(r.getName());
			c_content.append(
					"::midprocess(const JerboaGMap* gmap, const std::vector<JerboaFilterRowMatrix*> & leftfilter){\n");
			GeneratedLanguage prec = new GeneratedLanguage();
			try {
				prec = Translator.translate(r.getMidProcess(), new JerboaLanguageGlue(r, LanguageState.PRECONDITION), new TranslatorContextV2(modeler),
						prec, modeler, exportLanguage);

				c_import.append(prec.getInclude() + "\n");
				c_content.append("\t" + prec.getContent().replaceAll("\\n", "\n\t"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			c_content.append("\n}\n");
		}

		if (r.getPostProcess().replaceAll("\\s", "").length() != 0) {
			h_content.append("\n\tbool postprocess(const JerboaGMap* gmap);\n");
			c_content.append("bool ");
			c_content.append(r.getName());
			c_content.append("::postprocess(const JerboaGMap* gmap){\n");
			GeneratedLanguage prec = new GeneratedLanguage();
			try {
				prec = Translator.translate(r.getPostProcess(), new JerboaLanguageGlue(r, LanguageState.PRECONDITION), new TranslatorContextV2(modeler),
						prec, modeler, exportLanguage);

				c_import.append(prec.getInclude() + "\n");
				c_content.append("\t" + prec.getContent().replaceAll("\\n", "\n\t"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			c_content.append("\n}\n");
		}

		h_content.append("\n/** END SPECIFIC APPLYRULE FUNCTIONS **/\n\n");
		/** Fin de la dÃ©finition du applyRule **/

		c_content.append("std::string " + ruleName + "::getComment() const{\n");
		c_content.append(TAB + "return \"");
		c_content.append(r.getComment().replaceAll("\n", "\\\\n").replace("\"", "\\\""));
		c_content.append("\";\n}\n\n");

		c_content.append("std::vector<std::string> " + ruleName + "::getCategory() const{\n");
		c_content.append(TAB + "std::vector<std::string> listFolders;\n");
		for (final String s : getCategoryFolder(r.getCategory()).split("/")) {
			if (s.replace(" ", "") != "") {
				c_content.append(TAB + "listFolders.push_back(\"" + s + "\");\n");
			}
		}
		c_content.append(TAB + "return listFolders;\n}\n\n");

		h_content.append("\tstd::string getComment()const;\n");
		h_content.append("\tstd::vector<std::string> getCategory()const;\n");
		h_content.append("\tint reverseAssoc(int i)const;\n");

		final List<JMENode> leftGraph = r.getLeft().getNodes();
		final List<JMENode> rightGraph = r.getRight().getNodes();

		c_content.append("int " + ruleName + "::reverseAssoc(int i)const {\n");
		for (int i = 0; i < rightGraph.size(); i++) {
			if (i == 0) {
				c_content.append(TAB + "switch(i) {\n");
			}
			final JMENode rightNode = rightGraph.get(i);
			final JMENode leftNode = r.getLeft().getMatchNode(rightNode);
			if (leftNode != null) {
				c_content.append(TAB + "case " + i + ": return " + leftGraph.indexOf(leftNode) + ";\n");
			}
			if (i == rightGraph.size() - 1) {
				c_content.append(TAB + "}\n");
			}
		}
		c_content.append(TAB + "return -1;\n" + "}\n\n");

		h_content.append("\tint attachedNode(int i)const;\n");
		c_content.append("int " + ruleName + "::attachedNode(int i)const {\n");
		for (int i = 0; i < rightGraph.size(); i++) {
			if (i == 0) {
				c_content.append(TAB + "switch(i) {\n");
			}
			final JMENode rightNode = rightGraph.get(i);
			final JMENode leftNode = r.getLeft().getMatchNode(rightNode);
			if (leftNode != null) {
				c_content.append(TAB + "case " + i + ": return " + leftGraph.indexOf(leftNode) + ";\n");
			}

			if (i == rightGraph.size() - 1) {
				c_content.append(TAB + "}\n");
			}
		}
		c_content.append(TAB + "return -1;\n" + "}\n\n");

		// getters and setters
		for (JMEParamEbd pebd : r.getParamsEbd()) {
			String ptype = getEbdType(pebd.getType());
			h_content.append(TAB + ptype + " get" + pebd.getName() + "();\n");
			c_content.append(ptype + " " + ruleName + "::get" + pebd.getName() + "()");
			c_content.append("{\n\treturn " + pebd.getName() + ";\n}\n");

			h_content.append(TAB + "void set" + pebd.getName() + "(" + ptype + " _" + pebd.getName() + ");\n");
			c_content.append("void " + ruleName + "::set" + pebd.getName() + "(" + ptype + " _" + pebd.getName() + ")");
			c_content.append("{\n\tthis->" + pebd.getName() + " = _" + pebd.getName() + ";\n}\n");
		}

		// TODO export preconditionInnerClass and parameters
		if (!r.getPrecondition().isEmpty()) {
			h_content.append("\tinline bool hasPrecondition()const{return true;}\n");

		} else {
			h_content.append("\tinline bool hasPrecondition()const{return false;}\n");
		}

		h_content.append("};// end rule class \n\n");

		for (String n : getModelerModuleSplited(modeler)) {
			if (n.replaceAll("\\s", "").length() > 0) {
				h_content.append("}	// namespace " + n + "\n");
				c_content.append("}	// namespace " + n + "\n");
			}
		}
		h_content.append("#endif");
		/** FIN des fonctions classiques des rï¿½gles : **/

		StringBuilder ruleH = new StringBuilder();
		StringBuilder ruleCPP = new StringBuilder();

		ruleH.append(h_import);
		ruleH.append(h_content);

		ruleCPP.append(c_import);
		ruleCPP.append(c_content);

		File classRuleH = null, classRuleCPP = null;
		FileOutputStream ruleStreamH = null, ruleStreamCPP = null;

		try {
			final File cr = new File(path, getCategoryFolderForFile(r.getCategory()));
			cr.mkdirs();
			classRuleH = new File(path + getCategoryFolderForFile(r.getCategory()), ruleName + ".h");
			classRuleH.createNewFile();

			classRuleCPP = new File(path + getCategoryFolderForFile(r.getCategory()), ruleName + ".cpp");
			classRuleCPP.createNewFile();
			ruleStreamH = new FileOutputStream(classRuleH);
			ruleStreamCPP = new FileOutputStream(classRuleCPP);

			ruleStreamCPP.write((ruleCPP.toString()).getBytes());
			ruleStreamH.write((ruleH.toString()).getBytes());
			ruleStreamCPP.close();
			ruleStreamH.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void exportAtomicRule(JMERuleAtomic rule, StringBuilder ruleH, StringBuilder ruleCPP,
			StringBuilder h_import) {

		final String nameRule = rule.getName();

		final List<JMENode> leftGraph = rule.getLeft().getNodes();

		ruleH.append(TAB + nameRule + "(const " + rule.getModeler().getName() + " *modeler);\n\n");
		ruleH.append(TAB + "~" + nameRule + "(){\n " + TAB + TAB
				+ " //TODO: auto-generated Code, replace to have correct function\n\t}\n");

		ruleCPP.append(nameRule + "::" + nameRule + "(const " + rule.getModeler().getName() + " *modeler)\n");
		ruleCPP.append(TAB + ": JerboaRuleGenerated(modeler,\"" + nameRule);// const_cast<JerboaModeler*>()
		ruleCPP.append("\")\n" + TAB + " {\n\n");

		/** Valeur par defaut des parametres : */

		if (rule.getParamsEbd().size() > 0) {
			for (JMEParamEbd pebd : rule.getParamsEbd()) {
				if (!pebd.getInitValue().replaceAll("\\s", "").isEmpty())
					ruleCPP.append("\t" + pebd.getName() + " = " + pebd.getInitValue() + ";\n");
			}
		}

		ArrayList<JMENodeExpression> expressions = printGraph(rule, ruleCPP, true);

		ruleCPP.append("\n// ------- COMMON FEATURE\n\n");

		ruleCPP.append(TAB + "computeEfficientTopoStructure();\n");
		ruleCPP.append(TAB + "computeSpreadOperation();\n");
		ruleCPP.append(TAB + "chooseBestEngine();\n");
		ruleCPP.append("}\n\n");

		/**
		 * Definitions des classes internes
		 */

		ArrayList<String> topoArgList = new ArrayList<String>();
		for (JMENode n : leftGraph) {
			topoArgList.add(n.getName());
		}

		// *******************{
		for (final JMENodeExpression expr : expressions) {
			final String name = expr.getNode().getName();
			final String competeEmbName = expr.getEbdInfo().getName();
			final String completeName = nameRule + "ExprR" + name + competeEmbName;

			ruleH.append(TAB + "class " + completeName);
			ruleH.append(": public JerboaRuleExpression {\n");
			ruleH.append("\tprivate:\n\t\tconst JerboaModeler* _owner;\n\t\t ").append(nameRule)
			.append(" *parentRule;\n");
			ruleH.append(TAB).append("public:\n");
			ruleH.append(TAB).append(TAB).append(completeName).append("(").append(nameRule)
			.append("* o){parentRule = o;_owner = parentRule->modeler(); }\n");
			ruleH.append(TAB).append(TAB).append("~").append(completeName).append("(){parentRule = NULL;_owner = NULL; }\n");
			
			ruleH.append(TAB).append(TAB).append(
					"JerboaEmbedding* compute(const JerboaGMap* gmap,const JerboaRuleOperation *rule, \n\t\t\tJerboaFilterRowMatrix *leftfilter,const JerboaRuleNode *rulenode)const;\n\n");

			ruleCPP.append("JerboaEmbedding* " + nameRule + "::" + completeName);
			ruleCPP.append(
					"::compute(const JerboaGMap* gmap,const JerboaRuleOperation *rule, JerboaFilterRowMatrix *leftfilter,\n\t\tconst JerboaRuleNode *rulenode)const{\n");
			// Affectation of extra field
			ruleCPP.append(TAB).append("parentRule->curLeftFilter = leftfilter;\n");

			String string_expr = expr.getExpression();
			if (string_expr != null) {
				try {
					GeneratedLanguage atomicExprTr = new GeneratedLanguage();
					atomicExprTr = Translator.translate(string_expr, new JerboaLanguageGlue(expr), new TranslatorContextV2(rule.getModeler()), atomicExprTr,
							rule.getModeler(), exportLanguage);
					string_expr = atomicExprTr.getContent();
					h_import.append(atomicExprTr.getInclude());
				} catch (Exception e) {
					string_expr = "#ERROR in expression translation # " + string_expr;
					System.err.println("#ERROR in expression translation # ");
					e.printStackTrace();
				}
				ruleCPP.append("\n// ======== BEGIN CODE TRANSLATION FOR EXPRESSION COMPUTATION \n\n");
				ruleCPP.append("\t" + string_expr.replaceAll("\\n", "\n\t"));
				ruleCPP.append("\n// ======== END CODE TRANSLATION FOR EXPRESSION COMPUTATION \n\n");
			}
			ruleCPP.append(TAB).append("\n}\n");

			ruleH.append(TAB).append(TAB).append("std::string name() const;\n");
			ruleCPP.append("std::string ").append(nameRule + "::").append(completeName).append("::name() const{\n");
			ruleCPP.append(TAB).append("return \"").append(completeName).append("\";\n}\n\n");

			ruleH.append(TAB).append(TAB).append("int embeddingIndex() const;\n");
			ruleCPP.append("int ").append(nameRule + "::").append(completeName).append("::embeddingIndex() const{\n");
			ruleCPP.append(TAB).append("return ").append("parentRule->_owner->getEmbedding(\"").append(competeEmbName)
			.append("\")->id()").append(";\n}\n\n");

			ruleH.append(TAB + "};// end Class\n\n");

		}

		// Facility for accessing to the dart
		if (leftGraph.size() > 0) {
			ruleH.append("/**\n  * Facility for accessing to the dart\n  */\n");
		}
		for (int i = 0; i < leftGraph.size(); i++) {
			final JMENode n = leftGraph.get(i);
			String name = n.getName();
			if (!Character.isJavaIdentifierStart(name.charAt(0))) {
				name = "_" + name;
			}
			ruleH.append(TAB).append("JerboaDart* ").append(name).append("() {\n").append(TAB).append(TAB)
			.append("return ").append("curLeftFilter->node(").append(i).append(");\n").append(TAB)
			.append("}\n\n");
		}

	}

	private static void exportScript(final JMEScript script, StringBuilder ruleH, StringBuilder ruleCPP,
			StringBuilder h_import) {

		final String nameRule = script.getName();
		final List<JMENode> leftGraph = script.getLeft().getNodes();

		ruleH.append("\t");
		ruleH.append(nameRule);
		ruleH.append("(const " + script.getModeler().getName() + " *modeler);\n\n");
		ruleH.append("\t~");
		ruleH.append(nameRule);
		ruleH.append("(){\n \t\t //TODO: auto-generated Code, replace to have correct function\n\t}\n");

		ruleCPP.append(nameRule);
		ruleCPP.append("::");
		ruleCPP.append(nameRule);
		ruleCPP.append("(const " + script.getModeler().getName() + " *modeler)\n\t");
		ruleCPP.append(": JerboaRuleScript(modeler,\"");// const_cast<JerboaModeler*>(
		ruleCPP.append(nameRule);
		ruleCPP.append("\")");

		/** Valeur par dï¿½faut des paramï¿½tres : */

		ruleCPP.append("\n\t {\n");
		if (script.getParamsEbd().size() > 0) {
			for (JMEParamEbd pebd : script.getParamsEbd()) {
				if (!pebd.getInitValue().replaceAll("\\s", "").isEmpty())
					ruleCPP.append("\t" + pebd.getName() + " = " + pebd.getInitValue() + ";\n");
			}
		}

		printGraph(script, ruleCPP, false);

		ruleCPP.append("\n");

		ruleCPP.append("}\n\n");

		/*** PARSING ***/
		GeneratedLanguage parsed = new GeneratedLanguage();
		try {
			parsed = Translator.translate(script.getContent(), new JerboaLanguageGlue(script, LanguageState.CLASSICAL), new TranslatorContextV2(script.getModeler()),
					parsed, script.getModeler(), exportLanguage);
		} catch (Exception e) {
			// parsed = new Pair<String, String>("#ERROR in expression
			// translation # ",
			// "#ERROR in expression translation # ");
			System.err.println("#ERROR in expression translation # ");
			e.printStackTrace();
		}
		String parsedContent = parsed.getContent();
		String parsedHeader = parsed.getInclude();

		String importString = "";

		String manipForImport = new String(parsedContent);
		int im = manipForImport.indexOf("owner->rule(\"", 1);
		ArrayList<String> listOfImportedRule = new ArrayList<String>();

		while (im >= 0 && im < manipForImport.length() - 1) {
			String tmpS = manipForImport.substring(im);
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
					if (ruli.getName().compareTo(tmpS) == 0) {
						importString += "#include \"" + getRulePath(ruli) + ".h\"\n";
					}
				}
			}

			im = manipForImport.indexOf("owner->rule(\"", im + 1);
		} /*** END RULE TO IMPORT ***/

		importString += "\n/** BEGIN RAWS IMPORTS **/\n" + parsedHeader + "\n/** END RAWS IMPORTS **/\n";

		h_import.append(importString);

		ruleCPP.append("JerboaRuleResult* ");
		ruleCPP.append(nameRule);
		ruleCPP.append("::apply(JerboaGMap* gmap, const JerboaInputHooks& sels,JerboaRuleResultType kind){\n");
		ruleCPP.append("\t");

		ruleH.append("\tJerboaRuleResult* ");
		ruleH.append("apply(JerboaGMap* gmap, const JerboaInputHooks& sels,JerboaRuleResultType kind);\n");

		if (parsedContent.length() > 0) {
			parsedContent = parsedContent.replaceAll("[\\n\\r]", "\n\t");
		}
		ruleCPP.append(parsedContent);
		ruleCPP.append("\n}\n\n");

		// TODO: faire l'export du applyRule avec les paramÃ¨tre spÃ©cifiÃ©s
		// ruleH.append("\tJerboaRuleResult ");
		// ruleH.append("applyRule(");

		for (int i = 0; i < leftGraph.size(); i++) {
			ruleH.append("\tint ");
			ruleH.append(leftGraph.get(i).getName());
			ruleH.append("();\n");

			ruleCPP.append("\tint ");
			ruleCPP.append(nameRule);
			ruleCPP.append("::");
			ruleCPP.append(leftGraph.get(i).getName());
			ruleCPP.append("(){\n\t\treturn ");
			ruleCPP.append(leftGraph.get(i).getID());
			ruleCPP.append(";\n\t}\n");
		}

		/*** END PARSING ***/

	}

	static ArrayList<JMENodeExpression> printGraph(JMERule rop, StringBuilder content, boolean printEmbeddingExpr) {
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
				// HAK
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
		return expressions;
	}
}
