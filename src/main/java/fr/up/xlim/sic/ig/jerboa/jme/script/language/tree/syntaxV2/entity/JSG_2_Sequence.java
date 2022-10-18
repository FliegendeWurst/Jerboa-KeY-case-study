package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;

public class JSG_2_Sequence extends JSG_2_Entity implements Iterable<JSG_2_Entity> {
	private final ArrayList<JSG_2_Entity> seq;

	public JSG_2_Sequence(int l, int c) {
		super(l, c);
		seq = new ArrayList<>();
	}

	public JSG_2_Sequence(Collection<JSG_2_Entity> col, int l, int c) {
		super(l, c);
		seq = new ArrayList<>();
		seq.addAll(col);
	}

	public void add(JSG_2_Entity i) {
		seq.add(i);
	}

	public void addAll(Collection<JSG_2_Entity> is) {
		seq.addAll(is);
	}

	public void addAll(JSG_2_Sequence preSequence) {
		seq.addAll(preSequence.seq);
	}

	@Override
	public Iterator<JSG_2_Entity> iterator() {
		return seq.iterator();
	}

	public int size() {
		return seq.size();
	}

	public JSG_2_Entity get(int i) {
		return seq.get(i);
	}

	public boolean isEmpty() {
		return seq.isEmpty();
	}
	

	@Override
	public <T, E extends Exception> T visit(JSG_2_EntityVisitor<T, E> visitor) throws E {
		return visitor.accept(this);
	}
}
