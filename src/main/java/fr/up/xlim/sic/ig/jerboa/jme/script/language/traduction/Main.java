package fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.generated.MyLexer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generated.MyParser;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSSequence;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		final ComplexSymbolFactory factory = new ComplexSymbolFactory();
		// final MyLexer scanner = new MyLexer(new StringReader("if(
		// not(not(a)and b<9+3)){a=a+2}"), factory);

		for (int i = 0; i < args.length; i++) {
			System.out.println("reading file : " + new File(args[i]).toString());
			final MyLexer scanner = new MyLexer(new FileReader(new File(args[i])), factory);
			final MyParser parser = new MyParser(scanner, factory);

			try {
				final Symbol symbol = parser.parse();
				final JSSequence seq = (JSSequence) symbol.value;
				System.out.println();
				// final JSGenJerboaSource generator = new
				// JSGenJerboaSource(System.err);

				// System.out.println("\n ##### JAVA #### \n");
				// final JSGeneratorJava generator2 = new
				// JSGeneratorJava(System.out, new HashMap<String, String>());
				// generator2.init();
				// generator2.beginGeneration(seq);

				System.out.println("\n ##### END #### \n");

			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("fin");
		}
	}

}
