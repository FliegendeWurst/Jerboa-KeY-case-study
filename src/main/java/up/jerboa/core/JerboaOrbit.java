/**
 * 
 */
package up.jerboa.core;

import java.util.Iterator;

/**
 * This class represent an orbit in our topological modeler.
 * 
 * @author Hakim Belhaouari
 *
 */
public class JerboaOrbit implements Iterable<Integer> {
	/** array that reminds the parameter of the current orbit */
	private int dim[];
	
	/** variable used as cache in order to avoid recompute the max alpha of the dim array */
	private transient int max;
	
	/**
	 * Constructor useful for humans. With him you can enumerate all parameters of the orbit.
	 *  For instances, the orbit <a0,a1> will be written as new JerboaObit(0,1).
	 *  
	 *  Currently, the order is not significant. But these aspect may be changed in the future.
	 *  
	 *  <u>Caution</u> for efficiency reasons, we do not check the coherence of the orbit.
	 *  
	 *  @param dimensions is the sequence of integer that correspond to the alpha index of an orbit.
	 */
	public JerboaOrbit(int... dimensions) {
		dim = dimensions;
		max = -1;
	}

	public JerboaOrbit(JerboaOrbit orbit) {
		this.max = orbit.max;
		dim = new int[orbit.dim.length];
		for(int i =0;i < dim.length;i++)
			dim[i] = orbit.dim[i];
	}
	
	/**
	 * Accessor on the inner array that reminds the alpha indexes.
	 * You must NOT modify the resulted array. If needed you must duplicate it before modification.
	 * 
	 * @return an array that contains all alpha indexes. 
	 */
	public int[] tab() {
		return dim; 
	}
	
	/**
	 * Getter for searching inside the orbit for a specific position.
	 * @param i the index in the orbit
	 * @return the value of the ith element in the orbit
	 */
	public int get(int i) {
		return dim[i];
	}
	
	/**
	 * This function search the maximal alpha index of the current orbit.
	 * The result is cached in order to avoid multiple re-computation. 
	 * @return The max alpha index of the current orbit.
	 */
	public int getMaxDim() {
		if(max != -1)
			return max;
		for (int i : dim) {
			if(max < i)
				max = i;
		}
		return max;
	}
	
	/**
	 * This function checks if the alpha index passed as argument exist in the current orbit.
	 * @param alpha is the searched alpha index
	 * @return Return true if the alpha index is present and false otherwise.
	 */
	public boolean contains(int alpha) {
		for (int i : dim) {
			if(i == alpha)
				return true;
		}
		return false;
	}
	
	public int indexOf(int alpha) {
		for (int i = 0; i < size(); i ++){
			if (dim[i] == alpha)
				return i;
		} return -1;
	}
	
	public int size() {
		return dim.length;
	}
	
	/**
	 * Fonction qui renvoie un code de hashCode cohérent pour toutes les orbites.
	 * Attention la fonction échoue (RuntimeException) si une des dimensions de l'orbite
	 * est supérieur à 32! Il en est de même pour les orbites à trou!
	 */
	@Override
	public int hashCode() {
		int result = 0;
		for(int i : dim) {
			result = result|(1 << i);
		}
		// return super.hashCode();
		return result;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JerboaOrbit) {
			JerboaOrbit nn = (JerboaOrbit) obj;
			if(nn.getMaxDim() == getMaxDim()) {
				int[] ord = new int[getMaxDim()+2];
				for(int i : dim) {
					ord[(i+1)]++; // je met +1 car on autorise le -1 pour les orbites pour representer le vide
					// et uniquement lui
				}
				for(int i : nn.dim) {
					ord[(i+1)]--;
				}
				for(int i=0;i < ord.length;i++) {
					if(ord[i] != 0)
						return false;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		if(dim.length >= 1) {
			if (dim[0] == -1) sb.append("_");
			else sb.append("a").append(dim[0]);
			if(dim.length > 1) {
				for(int i=1;i < dim.length;i++) {
					if(dim[i] == -1)
						sb.append(", _");
					else
						sb.append(", a").append(dim[i]);
				}
			}
		}
		sb.append(">");
		return sb.toString();
	}

	@Override
	public Iterator<Integer> iterator() {
		return new JerboaOrbitIterator(dim);
	}

	class JerboaOrbitIterator implements Iterator<Integer> {
		private int[] dim;
		private int pos;
		public JerboaOrbitIterator(int[] dim) {
			this.dim = dim;
			pos = 0;
		}

		@Override
		public boolean hasNext() {
			return (pos < dim.length);
		}

		@Override
		public Integer next() {
			return dim[pos++];
		}

		@Override
		public void remove() {
			// not supported
		}
		
	}

	public boolean equalsStrict(JerboaOrbit orbit) {
		final int size = orbit.dim.length;
		if(size != dim.length)
			return false;
		for(int i = 0; i < size; i++) {
			if(dim[i] != orbit.dim[i])
				return false;
		}
		return true;
	}
}

