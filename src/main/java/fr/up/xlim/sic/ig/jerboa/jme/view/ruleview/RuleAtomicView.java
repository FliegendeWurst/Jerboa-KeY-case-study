package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import fr.up.xlim.sic.ig.jerboa.jme.export.NewSVGExport;
import fr.up.xlim.sic.ig.jerboa.jme.forms.JerboaModelerEditor;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEElement;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.view.JMEElementView;

public class RuleAtomicView extends RuleView implements  JMEElementView {

	private static final long serialVersionUID = -8378138642313643524L;

	protected JMERuleAtomic atomic;

	public RuleAtomicView(JerboaModelerEditor editor, JMERuleAtomic atomic) {
		super(editor, atomic);
		this.atomic = atomic;
		if (atomic == null) {// debug pour le vieweur
			atomic = new JMERuleAtomic(null, "foo");
			this.atomic = atomic;
			this.atomic.addView(this);
		}
	}

	@Override
	public void exportToSVG(String path) {
		// SVGExport.export("test.svg", this);
		NewSVGExport svgexport = new NewSVGExport(this);
		String svg = svgexport.generate();

		FileOutputStream fos;
		if (!path.endsWith(".svg"))
			path += ".svg";
		try {
			fos = new FileOutputStream(path);
			PrintStream ps = new PrintStream(fos);
			ps.println(svg);
			ps.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
