/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.model.serialize;

import java.awt.Point;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEArc;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMELoop;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENodeExpression;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamEbd;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEScript;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;
import up.jerboa.core.JerboaOrbit;

/**
 * @author Hakim
 *
 */
public class JMESaveModeler implements JMEVisitor<Boolean> {

	private PrintStream output;
	private int indent;
	private static int TABSIZE = 4;
	public static int VERSION = 1;
	
	public static final String XML_TAG_OPEN_RULES = "openRules";

	/**
	 *
	 */
	public JMESaveModeler(OutputStream out) {
		this.output = new PrintStream(out);
		indent = 0;
	}

	public void save(JMEModeler modeler, JerboaModelerEditor editor) {
		output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
		output.print("<jerboamodelereditor version=\"" + VERSION + "\" >\n");
		
		for(JMERule rule : editor.getOpenRules().keySet()) {
			output.print("\t<" + XML_TAG_OPEN_RULES + ">");
			output.print(rule.getName());
			output.print("</" + XML_TAG_OPEN_RULES + ">\n");
		}
		
		modeler.visit(this);

		output.println("</jerboamodelereditor>");
		output.close();
		modeler.resetModification();
	}

	@Override
	public Boolean visitArc(JMEArc arc) {
		List<Point> pts = arc.getPoints();

		indent();
		output.printf("<arc dim=\"%d\" a=\"%s\" b=\"%s\" size=\"%d\">\n", arc.getDimension(), arc.getSource().getName(),
				arc.getDestination().getName(), pts.size());
		indent++;
		for (Point p : pts) {
			indent();
			output.printf("<point x=\"%d\" y=\"%d\" />\n", p.x, p.y);
		}
		indent--;
		indent();
		output.println("</arc>");
		return null;
	}

	@Override
	public Boolean visitLoopArc(JMELoop loop) {
		indent();
		output.println("<arcloop dim=\"" + loop.getDimension() + "\" node=\"" + loop.getSource().getName()
				+ "\" angle=\"" + loop.getAngle() + "\" >");

		indent();
		output.println("</arcloop>");
		return null;
	}

	@Override
	public Boolean visitNode(JMENode node) {

		indent();
		output.printf(
				"<node name=\"%s\" x=\"%d\" y=\"%d\" orbit=\"%s\" kind=\"%s\" multiplicity=\"%s\"  color=\"%d;%d;%d;%d\">",
				node.getName(), node.getX(), node.getY(), orbitToString(node.getOrbit()), node.getKind(),
				node.getMultiplicity(), node.getColor().getRed(), node.getColor().getGreen(), node.getColor().getBlue(),
				node.getColor().getAlpha());
		indent++;
		List<JMENodeExpression> explicits = node.getExplicitExprs();
		if (explicits.size() > 0)
			output.print("\n");
		for (JMENodeExpression n : explicits) {
			n.visit(this);
		}

		if (node.getPrecondition() != null && !node.getPrecondition().isEmpty()) {
			output.println();
			indent();
			output.printf("<pre>%s</pre>", getExpressionLanguage(node.getPrecondition()));
			output.println();
		}
		indent--;
		indent();
		output.println("</node>");
		return null;
	}

	@Override
	public Boolean visitGraph(JMEGraph graph) {

		List<JMENode> nodes = graph.getNodes();
		indent();
		output.println("<nodes count=\"" + nodes.size() + "\">");
		indent++;
		for (JMENode n : nodes) {
			n.visit(this);
		}
		indent--;
		indent();
		output.println("</nodes>");

		List<JMEArc> arcs = graph.getArcs();
		indent();
		output.println("<arcs count=\"" + arcs.size() + "\">");
		indent++;
		for (JMEArc a : arcs) {
			a.visit(this);
		}
		indent--;
		indent();
		output.println("</arcs>");

		return null;
	}

	@Override
	public Boolean visitExpression(JMENodeExpression expr) {
		indent();
		output.print("<expr ebd=\"" + expr.getEbdInfo().getName() + "\">");
		output.print(getExpressionLanguage(expr.getExpression()));
		output.println("</expr>");
		return null;
	}

	@Override
	public Boolean visitRuleAtomic(JMERuleAtomic at) {

		indent();
		output.printf(
				"<rule name=\"%s\" module=\"%s\" category=\"%s\" shape=\"%s\" magnetic=\"%s\" showgrid=\"%s\" gridsize=\"%s\" >\n",
				at.getName(), at.getCategory(), at.getCategory(), at.getShape().toString(), at.isMagnetic(),
				at.isShowGrid(), at.getGridsize().toString());
		indent++;

		indent();
		output.print("<header>");
		output.print(getExpressionLanguage(at.getHeader()));
		output.println("</header>");

		indent();
		output.print("<precondition>");
		output.print(getExpressionLanguage(at.getPrecondition()));
		output.println("</precondition>");

		indent();
		output.print("<comment>");
		output.print(getExpressionLanguage(at.getComment()));
		output.println("</comment>");

		indent();
		output.println("<left>");
		indent++;
		at.getLeft().visit(this);
		indent--;
		indent();
		output.println("</left>");

		indent();
		output.println("<right>");
		indent++;
		at.getRight().visit(this);
		indent--;
		indent();
		output.println("</right>");

		indent();
		output.print("<preprocess>");
		output.print(getExpressionLanguage(at.getPreProcess()));
		output.println("</preprocess>");

		indent();
		output.print("<postprocess>");
		output.print(getExpressionLanguage(at.getPostProcess()));
		output.println("</postprocess>");

		indent();
		output.print("<midprocess>");
		output.print(getExpressionLanguage(at.getMidProcess()));
		output.println("</midprocess>");

		// param ebd
		indent();
		output.println("<paramsebd>");
		indent++;
		for (JMEParamEbd e : at.getParamsEbd()) {
			e.visit(this);
		}
		indent--;
		indent();
		output.println("</paramsebd>");
		// end param ebd

		// param topo
		indent();
		output.println("<paramstopo>");
		indent++;
		for (JMEParamTopo e : at.getParamsTopo()) {
			e.visit(this);
		}
		indent--;
		indent();
		output.println("</paramstopo>");
		// end param topo

		indent--;
		indent();
		output.println("</rule>");

		return null;
	}

	public static String getExpressionLanguage(String e) {
		String res = e.replace("&", "&#38;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;")
				.replace("\\", "&#92;");
		byte[] utf8 = null;
		String strUtf = res;
		try {
			utf8 = res.getBytes("UTF-8");
			strUtf = new String(utf8);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return strUtf;
	}

	private void indent() {
		for (int i = 0; i < indent; i++) {
			for (int j = 0; j < TABSIZE; j++)
				output.print(" ");
		}
	}

	@Override
	public Boolean visitModeler(JMEModeler modeler) {
		indent();
		output.printf("<modeler name=\"%s\" module=\"%s\" dimension=\"%d\" destDir=\"%s\" projectDir=\"%s\">\n",
				modeler.getName(), modeler.getModule(), modeler.getDimension(), modeler.getDestDir(),
				modeler.getProjectDir());
		indent++;

		indent();
		output.print("<comment>");
		output.print(getExpressionLanguage(modeler.getComment()));
		output.println("</comment>");

		indent();
		output.print("<header>");
		output.print(getExpressionLanguage(modeler.getHeader()));
		output.println("</header>");

		output.print("<generationType>");
		switch (modeler.getGenerationType()) {
		case JAVA:
			output.print("java");
			break;
		case CPP:
			output.print("cpp");
			break;
		case JAVA_V2:
			output.print("java_v2");
			break;
		case CPP_V2:
			output.print("cpp_v2");
			break;
		default:
			output.print("ALL");
			break;
		}
		output.println("</generationType>");

		// liste of embeddings
		List<JMEEmbeddingInfo> ebds = modeler.getEmbeddings();
		indent();
		output.printf("<embeddings count=\"%d\">\n", ebds.size());
		indent++;
		for (JMEEmbeddingInfo ebd : ebds) {
			ebd.visit(this);
		}
		indent--;
		indent();
		output.println("</embeddings>");

		// liste of rules
		List<JMERule> rules = modeler.getRules();
		indent();
		output.printf("<rules count=\"%d\">\n", rules.size());
		indent++;
		for (JMERule rule : rules) {
			rule.visit(this);
		}
		indent--;
		indent();
		output.println("</rules>");
		
		// liste of inferredrules
		List<JMERule> inferrules = modeler.getInferedRules();
		indent();
		output.printf("<inferrules count=\"%d\">\n", inferrules.size());
		indent++;
		for (JMERule rule : inferrules) {
			rule.visit(this);
		}
		indent--;
		indent();
		output.println("</inferrules>");
		

		// end of modeler;
		indent--;
		indent();
		output.println("</modeler>");

		return null;
	}

	@Override
	public Boolean visitEmbeddingInfo(JMEEmbeddingInfo info) {

		String sorbit = orbitToString(info.getOrbit());

		indent();
		output.printf(
				"<embedding name=\"%s\" type=\"%s\" orbit=\"%s\" headerFile=\"%s\" ><comment>%s</comment>"
						+ "<defaultCode>%s</defaultCode></embedding>\n",
				info.getName(), getExpressionLanguage(info.getType()), sorbit.toString(), info.getFileHeader(),
				getExpressionLanguage(info.getComment()), getExpressionLanguage(info.getDefaultCode()));

		return null;
	}

	private String orbitToString(JerboaOrbit orbit) {
		StringBuilder sorbit = new StringBuilder();
		int[] tab = orbit.tab();
		for (int i = 0; i < tab.length; i++) {
			if (i != 0)
				sorbit.append(",");
			sorbit.append(tab[i]);
		}

		return sorbit.toString();
	}

	@Override
	public Boolean visitParamEbd(JMEParamEbd pe) {

		indent();
		output.printf("<paramebd name=\"%s\" type=\"%s\" order=\"%d\">%s</paramebd>", pe.getName(),
				getExpressionLanguage(pe.getType()), pe.getOrder(), getExpressionLanguage(pe.getInitValue()));
		output.println();
		return null;
	}

	@Override
	public Boolean visitParamTopo(JMEParamTopo pt) {
		indent();
		output.printf("<paramtopo name=\"%s\" order=\"%d\" />", pt.getNode().getName(), pt.getOrder());
		output.println();
		return null;
	}

	@Override
	public Boolean visitScript(JMEScript at) {
		indent();
		output.printf(
				"<script name=\"%s\" module=\"%s\" category=\"%s\"  shape=\"%s\" magnetic=\"%s\" showgrid=\"%s\" gridsize=\"%s\" >\n",
				at.getName(), at.getCategory(), at.getCategory(), at.getShape(), at.isMagnetic(), at.isShowGrid(),
				at.getGridsize());
		indent++;

		indent();
		output.print("<header>");
		output.print(getExpressionLanguage(at.getHeader()));
		output.println("</header>");

		indent();
		output.print("<precondition>");
		output.print(getExpressionLanguage(at.getPrecondition()));
		output.println("</precondition>");

		indent();
		output.print("<comment>");
		output.print(getExpressionLanguage(at.getComment()));
		output.println("</comment>");

		indent();
		output.println("<left>");
		indent++;
		at.getLeft().visit(this);
		indent--;
		indent();
		output.println("</left>");

		indent();
		output.println("<right>");
		indent++;
		at.getRight().visit(this);
		indent--;
		indent();
		output.println("</right>");

		indent();
		output.print("<content>");
		output.print(getExpressionLanguage(at.getContent()));
		output.println("</content>");

		indent();
		output.print("<preprocess>");
		output.print(getExpressionLanguage((at.getPreProcess())));
		output.println("</preprocess>");

		indent();
		output.print("<postprocess>");
		output.print(getExpressionLanguage(at.getPostProcess()));
		output.println("</postprocess>");

		indent();
		output.print("<midprocess>");
		output.print(getExpressionLanguage(at.getMidProcess()));
		output.println("</midprocess>");

		// param ebd
		indent();
		output.println("<paramsebd>");
		indent++;
		for (JMEParamEbd e : at.getParamsEbd()) {
			e.visit(this);
		}
		indent--;
		indent();
		output.println("</paramsebd>");
		// end param ebd

		// param topo
		indent();
		output.println("<paramstopo>");
		indent++;
		for (JMEParamTopo e : at.getParamsTopo()) {
			e.visit(this);
		}
		indent--;
		indent();
		output.println("</paramstopo>");
		// end param topo

		indent--;
		indent();
		output.println("</script>");

		return null;
	}

}
