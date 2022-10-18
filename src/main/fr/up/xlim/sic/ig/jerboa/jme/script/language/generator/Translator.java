package fr.up.xlim.sic.ig.jerboa.jme.script.language.generator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generated.MyLexer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generated.MyParser;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction.JSSyntaxToSemantic;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction.JSSyntaxToSemantic_ExpressionLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction.JSSyntaxToSemantic_Script;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction.JSSyntaxToSemantic_common;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction.SpecificOperatorRemover;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.generator.Generator_G_V2;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.generator.JSG_2_GeneratorCpp;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.generator.JSG_2_GeneratorJava;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.translator.ExpressionTraductionV2;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.translator.JSSyntaxToSemanticV2_common;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.translator.SpecificOperatorRemover_V2;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.verification.Verification_V2;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSError;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSG_Verification;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSG_VerificationEmbeddingExpression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.verification.JSG_VerificationHasReturn;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

public class Translator {
	public enum ExportLanguage {
		JAVA, CPP, CPP_V2, JAVA_V2;
	}

	/**
	 * Translate a code from a Jerboa script language to a specific programming
	 * language. The left part of the pair returned is the header code and the
	 * second is the effective code.
	 *
	 * @param content
	 * @param _glue
	 * @param language
	 * @return
	 * @throws Exception
	 */
	public static GeneratedLanguage translate(String content, LanguageGlue _glue, TranslatorContextV2 context, 
			GeneratedLanguage genLang,
			JMEModeler modeler, ExportLanguage language) throws Exception {
		if (content.replaceAll("\\s*", "").length() > 0) {
			final ComplexSymbolFactory factory = new ComplexSymbolFactory();
			final MyLexer scanner = new MyLexer(new StringReader(content), factory);
			final MyParser parser = new MyParser(scanner, factory);

		
			if(language == ExportLanguage.CPP_V2 || language== ExportLanguage.JAVA_V2) {
				Generator_G_V2 generator = null;
				switch (language) {
				case CPP_V2:
					generator = new JSG_2_GeneratorCpp(_glue, context, genLang, modeler);
					break;
				case JAVA_V2:
					generator = new JSG_2_GeneratorJava(_glue, context, genLang, modeler);
					break;
				default:
					break;
				}
				if (generator != null) {
					Symbol symbol;
					symbol = parser.parse();
					if (symbol == null || symbol.value == null) {
						System.err.println("nothing found");
						return new GeneratedLanguage(generator.getResult());
					}
					final JSInstruction seq = (JSInstruction) symbol.value;

					JSSyntaxToSemanticV2_common g_generator = new JSSyntaxToSemanticV2_common(context, _glue);
					JSG_2_Entity g_seq = seq.visit(g_generator);
					SpecificOperatorRemover_V2 sor = new SpecificOperatorRemover_V2(_glue, context);
					ExpressionTraductionV2 g_seqUnspecified = g_seq.visit(sor);

					// TODO : il faudrait tester si il y a des instructions prealable dans l'ExpressionTraductionV2
					//					g_seqUnspecified.getExpression().visit(generator);
					generator.beginGeneration(g_seqUnspecified.getExpression());
					GeneratedLanguage res2 = generator.getResult();
					return res2;
				}




			}else {
				Generator_G generator = null;
				switch (language) {
				case CPP:
					generator = new JSG_GeneratorCpp(_glue, genLang, modeler);
					break;
				case JAVA:
					generator = new JSG_GeneratorJava(_glue, genLang, modeler);
					break;
				default:
					break;
				}

				if (generator != null) {
					Symbol symbol;
					symbol = parser.parse();
					if (symbol == null || symbol.value == null) {
						System.err.println("rien trouve");
						return new GeneratedLanguage(generator.getResult());
					}
					final JSInstruction seq = (JSInstruction) symbol.value;

					JSSyntaxToSemantic_common g_generator = new JSSyntaxToSemantic(_glue, modeler);
					switch (_glue.getLangageType()) {
					case EMBEDDING:
						g_generator = new JSSyntaxToSemantic_ExpressionLang(_glue, modeler);
						break;
					case SCRIPT:
						if (_glue.getLangagesState() != LanguageState.HEADER)
							g_generator = new JSSyntaxToSemantic_Script(_glue, modeler);
						break;
					case RULE:
						if (_glue.getLangagesState() != LanguageState.HEADER)
							g_generator = new JSSyntaxToSemantic_Script(_glue, modeler);
						break;
					default:
						break;
					}

					JSG_Instruction g_seq = seq.visit(g_generator);
					SpecificOperatorRemover sor = new SpecificOperatorRemover(modeler, _glue);
					JSG_Instruction g_seqUnspecified = sor.accept((JSG_Sequence) g_seq);


					g_seqUnspecified.visit(generator);
					GeneratedLanguage res2 = generator.getResult();
					return res2;
				}
			}
		}
		return new GeneratedLanguage();
	}

	public static ArrayList<JSError> verif(String s, LanguageGlue _glue, JMEModeler modeler, boolean verifV2) {
		ArrayList<JSError> result = new ArrayList<JSError>();
		if (s.replaceAll("\\s*", "").length() > 0) {
			final ComplexSymbolFactory factory = new ComplexSymbolFactory();
			final MyLexer scanner = new MyLexer(new StringReader(s), factory);
			final MyParser parser = new MyParser(scanner, factory);

			try {
				final Symbol symbol = parser.parse();
				result.addAll(parser.getErrorList());
				if (symbol != null && symbol.value != null && symbol.value instanceof JSInstruction) {
					final JSInstruction seq = (JSInstruction) symbol.value;

					if(verifV2) {
						TranslatorContextV2 context = new TranslatorContextV2(modeler);
						JSSyntaxToSemanticV2_common g_generator_2 = new JSSyntaxToSemanticV2_common(context, _glue);
						JSG_2_Entity g_seq = seq.visit(g_generator_2);
						
						final Verification_V2 verificator = new Verification_V2(context, _glue);
						g_seq.visit(verificator);
						result.addAll(verificator.getErrors());

//						if (_glue.getLangageType() == LanguageType.EMBEDDING
//								|| (_glue.getLangageType() == LanguageType.SCRIPT
//								&& _glue.getLangagesState() == LanguageState.CLASSICAL)) {
//							final JSG_VerificationHasReturn verifReturn = new JSG_VerificationHasReturn();
//							result.addAll(verifReturn.beginVerif(g_seq));
//						}

//						if (_glue.getLangageType() == LanguageType.EMBEDDING) {
//							JSG_VerificationEmbeddingExpression verifExpr = new JSG_VerificationEmbeddingExpression(_glue);
//							g_seq.visit(verifExpr);
//							result.addAll(verifExpr.getErrors());
//						}
					}else {
						final JSSyntaxToSemantic g_generator = new JSSyntaxToSemantic(_glue, modeler);
						JSG_Instruction g_seq = seq.visit(g_generator);

						final JSG_Verification verificator = new JSG_Verification(_glue, modeler);
						g_seq.visit(verificator);
						result.addAll(verificator.getErrors());

						if (_glue.getLangageType() == LanguageType.EMBEDDING
								|| (_glue.getLangageType() == LanguageType.SCRIPT
								&& _glue.getLangagesState() == LanguageState.CLASSICAL)) {
							// Val : le classical peut etre surement testé pour les
							// plongements aussi mais a priori il n'y a
							// pas d'autre state pour les plongemts.
							final JSG_VerificationHasReturn verifReturn = new JSG_VerificationHasReturn();
							result.addAll(verifReturn.beginVerif(g_seq));
						}

						if (_glue.getLangageType() == LanguageType.EMBEDDING) {
							JSG_VerificationEmbeddingExpression verifExpr = new JSG_VerificationEmbeddingExpression(_glue);
							g_seq.visit(verifExpr);
							result.addAll(verifExpr.getErrors());
						}
					} 
				}else {
					System.err.println("#error in parsing, was not able to translate");
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return result;

	}

	/*
	 * public static String TranslateEmbeddingExpression_GInCPP(String s,
	 * Map<String, String> mapEbdToUserType, ArrayList<String> topoArgsList,
	 * ArrayList<String> outerArgs) { final ComplexSymbolFactory factory = new
	 * ComplexSymbolFactory();
	 *
	 * final MyLexer scanner = new MyLexer(new StringReader(s), factory); final
	 * MyParser parser = new MyParser(scanner, factory); OutputStream output =
	 * new OutputStream() { private StringBuilder string = new StringBuilder();
	 *
	 * @Override public void write(int b) throws IOException {
	 * this.string.append((char) b); }
	 *
	 * @Override public String toString() { return this.string.toString(); } };
	 *
	 * try { final Symbol symbol = parser.parse(); if (symbol == null ||
	 * symbol.value == null) { output.close(); return output.toString(); } final
	 * JSSequence seq = (JSSequence) symbol.value; // final JSGenJerboaSource
	 * generator = new // JSGenJerboaSource(System.err);
	 *
	 * final JSSyntaxToSemantic_ExpressionLang g_generator = new
	 * JSSyntaxToSemantic_ExpressionLang( mapEbdToUserType, topoArgsList,
	 * outerArgs); JSG_Sequence g_seq = g_generator.accept(seq); final
	 * JSG_GeneratorCpp code_gen = new JSG_GeneratorCpp(output,
	 * mapEbdToUserType); code_gen.beginGeneration(g_seq);
	 *
	 * } catch (final Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return output.toString();
	 *
	 * }
	 *
	 * public static String TranslateEmbeddingExpression_GInJava(String s,
	 * Map<String, String> mapEbdToUserType, ArrayList<String> topoArgsList,
	 * ArrayList<String> outerArgs) { final ComplexSymbolFactory factory = new
	 * ComplexSymbolFactory();
	 *
	 * final MyLexer scanner = new MyLexer(new StringReader(s), factory); final
	 * MyParser parser = new MyParser(scanner, factory); OutputStream output =
	 * new OutputStream() { private StringBuilder string = new StringBuilder();
	 *
	 * @Override public void write(int b) throws IOException {
	 * this.string.append((char) b); }
	 *
	 * @Override public String toString() { return this.string.toString(); } };
	 *
	 * try { final Symbol symbol = parser.parse(); if (symbol == null ||
	 * symbol.value == null) { output.close(); return output.toString(); } final
	 * JSSequence seq = (JSSequence) symbol.value; // final JSGenJerboaSource
	 * generator = new // JSGenJerboaSource(System.err);
	 *
	 * final JSSyntaxToSemantic_ExpressionLang g_generator = new
	 * JSSyntaxToSemantic_ExpressionLang( mapEbdToUserType, topoArgsList,
	 * outerArgs); JSG_Sequence g_seq = g_generator.accept(seq); final
	 * JSG_GeneratorJava code_gen = new JSG_GeneratorJava(output,
	 * mapEbdToUserType); code_gen.beginGeneration(g_seq);
	 *
	 * } catch (final Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return output.toString();
	 *
	 * }
	 */

}
