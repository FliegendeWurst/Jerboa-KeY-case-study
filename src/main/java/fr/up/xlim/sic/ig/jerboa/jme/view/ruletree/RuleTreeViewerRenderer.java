package fr.up.xlim.sic.ig.jerboa.jme.view.ruletree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERuleAtomic;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEError;
import fr.up.xlim.sic.ig.jerboa.jme.verif.JMEErrorSeverity;

public class RuleTreeViewerRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -7047938412610406544L;

	private static Icon atomic = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/atomic_16x16_ps.png")).getImage()
					.getScaledInstance(17, 17, Image.SCALE_SMOOTH));
	private static final Icon script = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/script_16x16_ps.png")).getImage()
					.getScaledInstance(17, 17, Image.SCALE_SMOOTH));

	private static Icon atomic_error = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/atomic_error_16x16_ps.png")).getImage()
					.getScaledInstance(17, 17, Image.SCALE_SMOOTH));
	private static final Icon script_error = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/script_error_16x16_ps.png")).getImage()
					.getScaledInstance(17, 17, Image.SCALE_SMOOTH));

	private static final Icon atomic_warning = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/atomic_warning_ps.png")).getImage()
					.getScaledInstance(17, 17, Image.SCALE_SMOOTH));
	private static final Icon script_warning = new ImageIcon(
			new ImageIcon(RuleTreeViewerRenderer.class.getResource("/image/script_warning_ps.png")).getImage()
					.getScaledInstance(17, 17, Image.SCALE_SMOOTH));

	public RuleTreeViewerRenderer() {

	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		if (value instanceof RuleTreeNodeLeaf) {
			setFont(new Font(getFont().getName(), getFont().getStyle(), 16));
			RuleTreeNodeLeaf node = (RuleTreeNodeLeaf) value;
			JMERule model = node.getRule();
			JMEErrorSeverity sever = getMaxSeverity(model.getErrors());
			if (model instanceof JMERuleAtomic) {
				if (sever == JMEErrorSeverity.CRITIQUE)
					setLeafIcon(atomic_error); // a faire avant l'appel =
				// #getTreeCellRendererComponent
				else if (sever == JMEErrorSeverity.WARNING)
					setLeafIcon(atomic_warning);
				else
					setLeafIcon(atomic);
			} else {
				if (sever == JMEErrorSeverity.CRITIQUE)
					setLeafIcon(script_error); // a faire avant l'appel =
				// #getTreeCellRendererComponent
				else if (sever == JMEErrorSeverity.WARNING)
					setLeafIcon(script_warning);
				else
					setLeafIcon(script);
			}

			Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			comp.setFont(new Font(getFont().getName(), Font.PLAIN, getFont().getSize()));
			// System.err.println(sever);
			if (sever != null) {
				if (sever == JMEErrorSeverity.CRITIQUE)
					comp.setForeground(Color.RED);
				else if (sever == JMEErrorSeverity.WARNING)
					comp.setForeground(new Color(204, 150, 0));
			}
			return comp;
		} else {
			Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			comp.setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
			return comp;
		}
	}

	private JMEErrorSeverity getMaxSeverity(Collection<JMEError> ers) {
		JMEErrorSeverity sev = null;
		for (JMEError e : ers) {
			if (sev == null) {
				sev = e.getSeverity();
			} else if (e.getSeverity() == JMEErrorSeverity.CRITIQUE) {
				return JMEErrorSeverity.CRITIQUE;
			} else if (sev == JMEErrorSeverity.INFO && e.getSeverity() == JMEErrorSeverity.WARNING) {
				sev = JMEErrorSeverity.WARNING;
			}
		}
		return sev;
	}
}
