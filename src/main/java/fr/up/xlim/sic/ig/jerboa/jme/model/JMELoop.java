/**
 * 
 */
package fr.up.xlim.sic.ig.jerboa.jme.model;

import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItem;
import fr.up.xlim.sic.ig.jerboa.jme.model.undo.UndoItemField;
import fr.up.xlim.sic.ig.jerboa.jme.model.util.JMEVisitor;


/**
 * Cette classe specialise les arcs classiques pour les boucles.
 * @author hakim
 *
 */
public class JMELoop extends JMEArc {

	protected double angle;
	
	
	public JMELoop(JMEGraph graph, JMENode node, int dim) {
		super(graph, node,node,dim);
	}
	
	
	
	@Override
	public <T> T visit(JMEVisitor<T> visitor) {
		return visitor.visitLoopArc(this);
	}



	public void setAngle(double a) {
		if(a != angle) {
			manager.registerUndo(new UndoItemField(this, "angle", this.angle, a, !modified));
			this.angle = a;
			modified = true;
			update();
		}
	}
	
	public double getAngle() {
		return angle;
	}
	
	@Override
	public void undo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch(fitem.field()) {
		case "angle": {
			this.angle = ((Number) fitem.value()).doubleValue();
			break;
		}
		}
		super.undo(item);
	}
	
	@Override
	public void redo(UndoItem item) {
		UndoItemField fitem = (UndoItemField) item;
		switch(fitem.field()) {
		case "angle": {
			this.angle = ((Number) fitem.newValue()).doubleValue();
			break;
		}
		}
		super.redo(item);
	}
	
	public JMEArc copy(JMEGraph g) {
		JMENode nodeA = g.searchNodeByName(a.getName());
		JMENode nodeB = g.searchNodeByName(b.getName());
		if(nodeA != null && nodeB != null) {
			if(nodeA == nodeB) {
				JMELoop loop = new JMELoop(g, nodeA, getDimension());
				loop.angle = angle;
				return loop;
			}
			else {
				JMEArc arc = new JMEArc(g, nodeA, nodeB, getDimension());
				return arc;
			}
		}
		return null;
	}
	
}
