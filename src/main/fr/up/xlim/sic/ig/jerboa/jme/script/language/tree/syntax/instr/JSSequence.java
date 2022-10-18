package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

public class JSSequence extends JSInstruction implements Iterable<JSInstruction> {
	private final ArrayList<JSInstruction> seq;

	public JSSequence(int l, int c) {
		super(l, c);
		seq = new ArrayList<>();
	}

	public JSSequence(int l, int c, Collection<JSInstruction> col) {
		super(l, c);
		seq = new ArrayList<>(col);
	}

	public void add(JSInstruction i) {
		seq.add(i);
	}

	@Override
	public Iterator<JSInstruction> iterator() {
		return seq.iterator();
	}

	public int size() {
		return seq.size();
	}

	@Override
	public <T, E extends Exception> T visit(JSInstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
