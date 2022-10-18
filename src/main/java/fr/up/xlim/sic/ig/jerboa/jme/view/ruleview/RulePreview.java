package fr.up.xlim.sic.ig.jerboa.jme.view.ruleview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.view.graph.RuleGraphView;

public class RulePreview extends JPopupMenu {
	private static final long serialVersionUID = 1308550658144629486L;
	private JMERule rule;

	public RulePreview(JMERule r, RuleView rv) {
		super();
		rule = r;

		RuleGraphView leftView = new RuleGraphView(rv, r, rule.getLeft(), false);
		RuleGraphView rightView = new RuleGraphView(rv, r, rule.getRight(), false);

		leftView.zoomFactor(0.3);
		rightView.zoomFactor(0.3);
		//
		// leftView.setSize(180, 180);
		leftView.setPreferredSize(new Dimension(180, 180));
		// leftView.setMinimumSize(new Dimension(180, 180));
		//
		// rightView.setSize(180, 180);
		rightView.setPreferredSize(new Dimension(180, 180));
		// rightView.setMinimumSize(new Dimension(180, 180));
		//
		// leftView.updateRangeDimension();
		// rightView.updateRangeDimension();

		JPanel separator = new JPanel();
		separator.setBackground(Color.BLACK);
		separator.setMaximumSize(new Dimension(2, 180));

		Box b = Box.createHorizontalBox();
		b.add(leftView);
		b.add(separator);
		b.add(rightView);
		add(b);
		// setMinimumSize(new Dimension(165, 180));
		// setMaximumSize(new Dimension(105, 50));
		// pack();
		// setBackground(Color.BLUE);

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				RulePreview.this.setVisible(false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	public JMERule getRule() {
		return rule;
	}

}
