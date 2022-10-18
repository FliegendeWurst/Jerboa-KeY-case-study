/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.model.serialize;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMELoop;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModelerGenerationType;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeKind;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeMultiplicity;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMENodeShape;
import fr.up.xlim.sic.ig.jerboa.jme.util.RuleGraphViewGrid;
import up.jerboa.core.JerboaOrbit;
import up.jerboa.core.util.Pair;

/**
 * @author Valentin Gauthier
 *
 */
public class JMELoadModeler {

	private Document document;
	private DocumentBuilder builder;

	final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	/**
	 *
	 */
	public JMELoadModeler() {
	}

	public Pair<JMEModeler, ArrayList<JMERule>> load(final String filePath) {
		JMEModeler modeler = new JMEModeler("__ERROR__", "", 0);
		try {
			builder = factory.newDocumentBuilder();
			File f = new File(filePath);
			document = builder.parse(f);
			System.out.println("reading file : " + f);

			// // Affiche la version de XML
			// System.out.println(document.getXmlVersion());
			// // Affiche l'encodage
			// System.out.println(document.getXmlEncoding());
			// // Affiche s'il s'agit d'un document standalone
			// System.out.println(document.getXmlStandalone());

			final Element globalItem = document.getDocumentElement();
			// System.out.println(globalItem.getNodeName());
			String mversion = globalItem.getAttribute("version");
			// System.out.println("version " + mversion);
			if (Integer.parseInt(mversion) != JMESaveModeler.VERSION) {
				System.err.println("Wrong file version : found " + globalItem.getAttribute("version") + " but expected "
						+ JMESaveModeler.VERSION);
				return new Pair<JMEModeler, ArrayList<JMERule>>(modeler, new ArrayList<>());
			}
			for (int i = 0; i < globalItem.getChildNodes().getLength(); i++) {
				Node n = globalItem.getChildNodes().item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					System.out.println(e.getNodeName());
					if (e.getNodeName().compareTo("modeler") == 0) {
						loadModeler(modeler, e);
						System.out.println(modeler);
					}
				}
			}
			modeler.resetModification();
			
			ArrayList<JMERule> openRules = new ArrayList<>();
			NodeList nl_openRules = globalItem.getElementsByTagName(JMESaveModeler.XML_TAG_OPEN_RULES);
	        for(int i =0; i<nl_openRules.getLength(); i++) {
	            if (nl_openRules.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                Element openRuleElt = (Element) nl_openRules.item(i);
	                String openRuleName = openRuleElt.getTextContent();
	                System.err.println(openRuleName);
	                for(JMERule r : modeler.getRules()) {
	                	if(r.getName().compareTo(openRuleName)==0) {
	                		openRules.add(r);
	                	}
	                }
	            }
			}			
			return new Pair<JMEModeler, ArrayList<JMERule>>(modeler, openRules);
			// modeler.visit(this);
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void loadModeler(JMEModeler modeler, Element elt) {
		modeler.setName(elt.getAttribute("name"));
		modeler.setModule(elt.getAttribute("module"));
		modeler.setDimension(Integer.parseInt(elt.getAttribute("dimension")));
		modeler.setDestDir(elt.getAttribute("destDir"));
		modeler.setProjectDir(elt.getAttribute("projectDir"));

		for (int i = 0; i < elt.getChildNodes().getLength(); i++) {
			Node n = elt.getChildNodes().item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				if (e.getNodeName().compareTo("comment") == 0) {
					modeler.setComment(getExpressionLanguage(e.getTextContent()));

				} else if (e.getNodeName().compareTo("header") == 0) {
					modeler.setHeader(getExpressionLanguage(e.getTextContent()));
				} else if (e.getNodeName().compareTo("generationType") == 0) {
					if (e.getTextContent().compareToIgnoreCase("java") == 0) {
						modeler.setGenerationType(JMEModelerGenerationType.JAVA);
					} else if (e.getTextContent().compareToIgnoreCase("java_v2") == 0) {
						modeler.setGenerationType(JMEModelerGenerationType.JAVA_V2);
					} else if (e.getTextContent().compareToIgnoreCase("cpp") == 0) {
						modeler.setGenerationType(JMEModelerGenerationType.CPP);
					} else if (e.getTextContent().compareToIgnoreCase("cpp_v2") == 0) {
						modeler.setGenerationType(JMEModelerGenerationType.CPP_V2);
					} else
						modeler.setGenerationType(JMEModelerGenerationType.ALL);

				} else if (e.getNodeName().compareTo("embeddings") == 0) {
					for (int ei = 0; ei < n.getChildNodes().getLength(); ei++) {
						Node nei = n.getChildNodes().item(ei);
						if (nei != null && nei.getNodeType() == Node.ELEMENT_NODE) {
							Element eei = (Element) nei;
							modeler.addEmbedding(loadEbd(modeler, eei, ei - 1));
						}
					}
				} else if (e.getNodeName().compareTo("rules") == 0) {
					for (int ei = 0; ei < n.getChildNodes().getLength(); ei++) {
						Node nei = n.getChildNodes().item(ei);
						if (nei != null && nei.getNodeType() == Node.ELEMENT_NODE) {
							Element eei = (Element) nei;
							JMERule rule = loadRule(modeler, eei, ei - 1);
							if (rule != null)
								modeler.addRule(rule);
							else {
								System.err.print("A rule");
								try {
									System.err.print(" named '" + elt.getAttribute("name") + "'");
								} catch (Exception e2) {
								}
								System.err.println(" has not been imported");
							}
						}
					}
				} else if (e.getNodeName().compareTo("inferrules") == 0) {
					for (int ei = 0; ei < n.getChildNodes().getLength(); ei++) {
						Node nei = n.getChildNodes().item(ei);
						if (nei != null && nei.getNodeType() == Node.ELEMENT_NODE) {
							Element eei = (Element) nei;
							JMERule rule = loadRule(modeler, eei, ei - 1);
							if (rule != null)
								modeler.addInferRule(rule);
							else {
								System.err.print("An inferred rule");
								try {
									System.err.print(" named '" + elt.getAttribute("name") + "'");
								} catch (Exception e2) {
								}
								System.err.println(" has not been imported");
							}
						}
					}
				}
			}
		}
		modeler.getUndoManager().clear();
		modeler.update();
	}

	private JMEEmbeddingInfo loadEbd(JMEModeler modeler, Element elt, int id) {
		NodeList nl = elt.getElementsByTagName("comment");
		String comment = "";
		if (nl.getLength() > 0) {
			Element ecomment = (Element) nl.item(0);
			comment = getExpressionLanguage(ecomment.getTextContent());
		}

		JMEEmbeddingInfo ebd = new JMEEmbeddingInfo(modeler, id, elt.getAttribute("name"),
				loadOrbit(elt.getAttribute("orbit")), getExpressionLanguage(elt.getAttribute("type")), comment);
		ebd.setFileHeader(elt.getAttribute("headerFile"));
		
		
		
		NodeList nldefcode = elt.getElementsByTagName("defaultCode");
		String defcode = "";
		if(nldefcode.getLength() > 0) {
			Element edefcode = (Element) nldefcode.item(0);
			defcode = getExpressionLanguage(edefcode.getTextContent());
		}
		ebd.setDefaultCode(defcode);
		
		return ebd;
	}

	private JMERule loadRule(JMEModeler modeler, Element elt, int id) {
		JMERule rule = null;
		try {
			boolean isAtomic = true;
			if (elt.getNodeName().compareTo("rule") == 0) {
				rule = new JMERuleAtomic(modeler, elt.getAttribute("name"));
			} else if (elt.getNodeName().compareTo("script") == 0) {
				isAtomic = false;
				rule = new JMEScript(modeler, elt.getAttribute("name"));
			}

			rule.setCategory(elt.getAttribute("category"));
			try {
				// System.out.println("SIZE: " + elt.getAttribute("gridsize"));
				rule.setGridsize(RuleGraphViewGrid.valueOf(elt.getAttribute("gridsize")));
			} catch (Exception e) {
				rule.setGridsize(RuleGraphViewGrid.MEDIUM);
			}

			try {
				rule.setMagnetic(Boolean.parseBoolean(elt.getAttribute("magnetic")));
			} catch (Exception e) {
				rule.setMagnetic(true);
			}

			try {
				rule.setShowGrid(Boolean.parseBoolean(elt.getAttribute("showgrid")));
			} catch (Exception e) {
				rule.setShowGrid(false);
			}

			try {
				rule.setShape(JMENodeShape.valueOf(elt.getAttribute("shape")));
			} catch (Exception e) {
				rule.setShape(JMENodeShape.CIRCLE);
			}

			for (int i = 0; i < elt.getChildNodes().getLength(); i++) {
				Node ni = elt.getChildNodes().item(i);
				if (ni != null && ni.getNodeType() == Node.ELEMENT_NODE) {
					Element eni = (Element) ni;
					if (eni.getNodeName().compareTo("precondition") == 0) {
						rule.setPrecondition(getExpressionLanguage(eni.getTextContent()));
					} else if (eni.getNodeName().compareTo("comment") == 0) {
						rule.setComment(getExpressionLanguage(eni.getTextContent()));
					} else if (eni.getNodeName().compareTo("header") == 0) {
						rule.setHeader(getExpressionLanguage(eni.getTextContent()));
					} else if (eni.getNodeName().compareTo("preprocess") == 0) {
						rule.setPreProcess(getExpressionLanguage(eni.getTextContent()));
					} else if (eni.getNodeName().compareTo("postprocess") == 0) {
						rule.setPostProcess(getExpressionLanguage(eni.getTextContent()));
					} else if (eni.getNodeName().compareTo("midprocess") == 0) {
						rule.setMidProcess(getExpressionLanguage(eni.getTextContent()));
					} else if (eni.getNodeName().compareTo("left") == 0) {
						rule.setLeft(loadGraph(eni, rule, true, modeler));
						// System.err.println("LEFT DONE");
					} else if (eni.getNodeName().compareTo("right") == 0) {
						rule.setRight(loadGraph(eni, rule, false, modeler));
					} else if (!isAtomic && eni.getNodeName().compareTo("content") == 0) {
						((JMEScript) rule).setContent(getExpressionLanguage(eni.getTextContent()));
					} else if (eni.getNodeName().compareTo("paramsebd") == 0) {
						loadEbdParam(eni, rule, modeler);
					} else if (eni.getNodeName().compareTo("paramstopo") == 0) {
						loadTopoParam(eni, rule, modeler);
					}
					// TODO: param topos + param ebd !
				}
			}
			rule.getUndoManager().clear();
			return rule;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	private String getExpressionLanguage(String e) {
		return e.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&slash;", "\\").replaceAll("&quot;", "\"");
	}

	private void loadTopoParam(Element elt, JMERule rule, JMEModeler modeler) {
		ArrayList<Pair<Integer, JMEParamTopo>> all = new ArrayList<>();

		for (int i = 0; i < elt.getChildNodes().getLength(); i++) {
			Node ni = elt.getChildNodes().item(i);
			if (ni != null && ni.getNodeType() == Node.ELEMENT_NODE) {
				Element eni = (Element) ni;
				if (eni.getNodeName().compareTo("paramtopo") == 0) {
					JMENode hookFound = rule.getHookNode(eni.getAttribute("name"));
					if (hookFound != null) {
						int order = 0;
						try {
							order = Integer.parseInt(eni.getAttribute("order"));
						} catch (Exception e) {
						}
						all.add(new Pair<>(order, new JMEParamTopo(rule, hookFound)));

					}
				}
			}
		}

		Collections.sort(all, new Comparator<Pair<Integer, JMEParamTopo>>() {
			@Override
			public int compare(Pair<Integer, JMEParamTopo> o1, Pair<Integer, JMEParamTopo> o2) {
				return Integer.compare(o1.l(), o2.l());
			}
		});
		for (Pair<Integer, JMEParamTopo> pair : all) {
			rule.addParamTopo(pair.r());
		}
	}

	private void loadEbdParam(Element elt, JMERule rule, JMEModeler modeler) {
		for (int i = 0; i < elt.getChildNodes().getLength(); i++) {
			Node ni = elt.getChildNodes().item(i);
			if (ni != null && ni.getNodeType() == Node.ELEMENT_NODE) {
				Element eni = (Element) ni;
				if (eni.getNodeName().compareTo("paramebd") == 0) {
					String initValue = eni.getTextContent();
					int order = 0;
					try {
						order = Integer.parseInt(eni.getAttribute("order"));
					} catch (Exception e) {
					}
					rule.addParamEbd(new JMEParamEbd(rule, eni.getAttribute("name"),
							getExpressionLanguage(eni.getAttribute("type")), initValue, order));

				}
			}
		}
	}

	private JMEGraph loadGraph(Element elt, JMERule rule, boolean isLeft, JMEModeler modeler) {
		JMEGraph graph = new JMEGraph(rule, isLeft);
		for (int i = 0; i < elt.getChildNodes().getLength(); i++) {
			Node ni = elt.getChildNodes().item(i);
			if (ni != null && ni.getNodeType() == Node.ELEMENT_NODE) {
				Element eni = (Element) ni;
				if (eni.getNodeName().compareTo("nodes") == 0) {
					for (int ei = 0; ei < ni.getChildNodes().getLength(); ei++) {
						Node nei = ni.getChildNodes().item(ei);
						if (nei != null && nei.getNodeType() == Node.ELEMENT_NODE) {
							Element eei = (Element) nei;
							JMENode node = new JMENode(graph, eei.getAttribute("name"),
									Integer.parseInt(eei.getAttribute("x")), Integer.parseInt(eei.getAttribute("y")),
									loadKind(eei.getAttribute("kind")));

							node.setMultiplicity(
									JMENodeMultiplicity.parseMultiplicity(eei.getAttribute("multiplicity")));

							node.setOrbit(loadOrbit(eei.getAttribute("orbit")));

							if (eei.hasAttribute("color")) {
								String scolor = eei.getAttribute("color");
								String[] sparts = scolor.split(";");
								Color c = new Color(Integer.parseInt(sparts[0]), Integer.parseInt(sparts[1]),
										Integer.parseInt(sparts[2]), Integer.parseInt(sparts[3]));
								node.setColor(c);
							}

							loadExpr(eei, node, modeler);
							graph.addNode(node);
						}
					}
				} else if (eni.getNodeName().compareTo("arcs") == 0) {
					for (int ei = 0; ei < ni.getChildNodes().getLength(); ei++) {
						Node nei = ni.getChildNodes().item(ei);
						if (nei != null && nei.getNodeType() == Node.ELEMENT_NODE) {
							Element eei = (Element) nei;
							if (nei.getNodeName().compareTo("arcloop") == 0) {
								JMELoop loop = graph.creatLoop(graph.getMatchNode(eei.getAttribute("node")),
										Integer.parseInt(eei.getAttribute("dim")));
								try {
									loop.setAngle(Double.parseDouble(eei.getAttribute("angle")));
								} catch (Exception e) {
								} // en cas d'erreur on laisse a zero
							} else if (nei.getNodeName().compareTo("arc") == 0) {
								graph.creatArc(graph.getMatchNode(eei.getAttribute("a")),
										graph.getMatchNode(eei.getAttribute("b")),
										Integer.parseInt(eei.getAttribute("dim")));
							}
						}
					}
				}
			}
		}
		graph.getUndoManager().clear();
		return graph;
	}

	private void loadExpr(Element elt, JMENode node, JMEModeler modeler) {
		for (int i = 0; i < elt.getChildNodes().getLength(); i++) {
			Node ni = elt.getChildNodes().item(i);
			if (ni != null && ni.getNodeType() == Node.ELEMENT_NODE) {
				Element eni = (Element) ni;
				if (eni.getNodeName().compareTo("expr") == 0 && modeler.getEmbedding(eni.getAttribute("ebd")) != null) {
					node.addExplicitExpression(
							new JMENodeExpression(node, modeler.getEmbedding(eni.getAttribute("ebd")),
									getExpressionLanguage(eni.getTextContent())));
				}
				if (eni.getNodeName().compareTo("pre") == 0) {
					node.setPrecondition(getExpressionLanguage(eni.getTextContent()));
				}
			}
		}
	}

	private JMENodeKind loadKind(String s) {
		for (JMENodeKind k : JMENodeKind.values()) {
			if (s.compareTo(k.toString()) == 0) {
				return k;
			}
		}
		return null;
	}

	private JerboaOrbit loadOrbit(String s) {
		Collection<Integer> col = new ArrayList<>();
		if (s.replaceAll("\\s", "").toString().length() != 0)
			for (String t : s.replaceAll("\\s", "").split(","))
				try {
					col.add(Integer.parseInt(t));
				} catch (Exception e) {
					e.printStackTrace();
				}
		return new JerboaOrbit(col);
	}

	public static Pair<JMEModeler, ArrayList<JMERule>> loadModeler(File filePath) {

		JMELoadModeler loader = new JMELoadModeler();
		Pair<JMEModeler, ArrayList<JMERule>> modelerLoaded = loader.load(filePath.getAbsolutePath());
		if (modelerLoaded.l() != null)
			modelerLoaded.l().setFileJME(filePath.getAbsolutePath());

		System.out.println("Modeler loaded : " + filePath);
		return modelerLoaded;
	}

}
