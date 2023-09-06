package fr.up.xlim.sic.ig.jerboa.jme.forms;

import fr.up.xlim.sic.ig.jerboa.jme.export.CPPExport_New_Engine;
import fr.up.xlim.sic.ig.jerboa.jme.export.CPPExport_V2;
import fr.up.xlim.sic.ig.jerboa.jme.export.JavaExport;
import fr.up.xlim.sic.ig.jerboa.jme.export.JavaExport_V2;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.serialize.JMELoadModeler;
import org.apache.commons.cli.*;
import up.jerboa.core.util.Pair;

import java.io.File;
import java.util.ArrayList;

public class JerboaModelerEditorCommandLine {

    public static void main(String[] args) {
        Options options = new Options();

        Option opt_input = new Option("f", "jme", true, "Jme file path");
        opt_input.setRequired(false);
        options.addOption(opt_input);
        opt_input.setArgs(Option.UNLIMITED_VALUES);

        Option opt_output = new Option("o", "outputDir", true, "Output folder path");
        opt_output.setRequired(false);
        options.addOption(opt_output);
        opt_output.setArgs(Option.UNLIMITED_VALUES);

        CommandLineParser parser = new BasicParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        generate(cmd.getOptionValue("jme"), cmd.getOptionValue("outputDir"));
    }

    public static void generate(String modelerPath, String outputPath) {
        JMELoadModeler loader = new JMELoadModeler();
        JMEModeler modeler = loader.load(modelerPath).l();

        File modelerFile = new File(modelerPath);
        modeler.setFileJME(modelerFile.getAbsolutePath());

        if (outputPath != null) {
            modeler.setDestDir(outputPath);
        }

        switch (modeler.getGenerationType()) {
            case CPP:
                CPPExport_New_Engine.exportModeler(modeler);
                break;
            case CPP_V2:
                CPPExport_V2.exportModeler(modeler);
                break;
            case JAVA:
                JavaExport.exportModeler(modeler);
                break;
            case JAVA_V2:
                JavaExport_V2.exportModeler(modeler);
                break;
            default:
                System.err.println("Not a valid generation type " + modeler.getGenerationType());
        }
    }
}
