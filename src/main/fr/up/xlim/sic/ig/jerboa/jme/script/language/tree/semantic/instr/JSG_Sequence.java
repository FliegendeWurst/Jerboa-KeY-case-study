package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class JSG_Sequence extends JSG_Instruction implements Iterable<JSG_Instruction> {
	private final ArrayList<JSG_Instruction> seq;

	public JSG_Sequence(int l, int c) {
		super(l, c);
		seq = new ArrayList<>();
	}

	public JSG_Sequence(int l, int c, Collection<JSG_Instruction> col) {
		super(l, c);
		seq = new ArrayList<>();
		seq.addAll(col);
	}

	public void add(JSG_Instruction i) {
		seq.add(i);
	}

	public void addAll(Collection<JSG_Instruction> is) {
		seq.addAll(is);
	}

	public void addAll(JSG_Sequence preSequence) {
		seq.addAll(preSequence.seq);
	}

	@Override
	public Iterator<JSG_Instruction> iterator() {
		return seq.iterator();
	}

	public int size() {
		return seq.size();
	}

	public JSG_Instruction get(int i) {
		return seq.get(i);
	}

	@Override
	public <T, E extends Exception> T visit(JSG_InstVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}

	public boolean isEmpty() {
		return seq.isEmpty();
	}
}
