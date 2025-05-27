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
public final class JerboaOrbit implements Iterable/*<Integer>*/ {
	//@ invariant dim != null && (\forall int i; 0 <= i && i < dim.length; dim[i] >= -1);

	/** array that reminds the parameter of the current orbit */
	private /*@ spec_public */ int dim[];
	
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
	}
	
	/**
	 * Accessor on the inner array that reminds the alpha indexes.
	 * You must NOT modify the resulted array. If needed you must duplicate it before modification.
	 * 
	 * @return an array that contains all alpha indexes. 
	 */
	/*@ public normal_behavior
	  @ ensures \result == this.dim;
	  @ strictly_pure
	  @*/
	public int[] tab() {
		return dim; 
	}
	
	/**
	 * Getter for searching inside the orbit for a specific position.
	 * @param i the index in the orbit
	 * @return the value of the ith element in the orbit
	 */
	/*@ public normal_behavior
	  @ ensures \result == this.dim[i];
	  @ strictly_pure
	  @*/
	public int get(int i) {
		return dim[i];
	}
	
	/**
	 * This function search the maximal alpha index of the current orbit.
	 * The result is cached in order to avoid multiple re-computation. 
	 * @return The max alpha index of the current orbit.
	 */
	/*@ public normal_behavior
	  @ requires \invariant_for(this);
	  @ ensures (\forall int i; 0 <= i && i < dim.length; \result >= dim[i]);
	  @ ensures (dim.length > 0) ==> (\exists int i; 0 <= i && i < dim.length; \result == dim[i]);
	  @ ensures \invariant_for(this);
	  @ pure
	  @*/
	public int getMaxDim() {
		int max = -1;
		/*@ loop_invariant
		  @ 0 <= j && j <= dim.length
		  @  && (\forall int a; 0 <= a && a < j; max >= dim[a])
		  @  && ((max == -1 && (\forall int b; 0 <= b && b < j; -1 == dim[b])) || (j > 0 && (\exists int b; 0 <= b && b < j; max == dim[b])));
		  @ assignable max;
		  @ decreases dim.length - j;
		  @*/
        for (int j = 0; j < dim.length; j++) {
            int i = dim[j];
            if (max < i)
                max = i;
        }
		return max;
	}
	
	/**
	 * This function checks if the alpha index passed as argument exist in the current orbit.
	 * @param alpha is the searched alpha index
	 * @return Return true if the alpha index is present and false otherwise.
	 */
	/*@ public normal_behaviour
      @ ensures \result == (\exists int i; 0 <= i < dim.length; dim[i] == alpha);
	  @ pure
	  @*/
	public boolean contains(int alpha) {
		/*@ loop_invariant
		  @ 0 <= j && j <= dim.length
		  @  && (\forall int a; 0 <= a && a < j; dim[a] != alpha);
		  @ assignable \nothing;
		  @ decreases dim.length - j;
		  @*/
        for (int j = 0; j < dim.length; j++) {
            int i = dim[j];
            if (i == alpha)
                return true;
        }
		return false;
	}

	/*@ public normal_behaviour
      @ ensures !contains(alpha) ==> \result == -1;
      @ ensures contains(alpha) ==> (dim[\result] == alpha && (\forall int i; 0 <= i && i < \result; dim[i] != alpha));
	  @ pure
	  @*/
	public int indexOf(int alpha) {
		/*@ loop_invariant
		  @ 0 <= i && i <= dim.length
		  @  && (\forall int a; 0 <= a && a < i; dim[a] != alpha);
		  @ assignable \nothing;
		  @ decreases dim.length - i;
		  @*/
		for (int i = 0; i < size(); i ++){
			if (dim[i] == alpha)
				return i;
		} return -1;
	}

	/*@ public normal_behavior
	  @ ensures \result == dim.length;
	  @ strictly_pure
	  @*/
	public int size() {
		return dim.length;
	}
	
	/**
	 * Fonction qui renvoie un code de hashCode cohérent pour toutes les orbites.
	 * Attention la fonction échoue (RuntimeException) si une des dimensions de l'orbite
	 * est supérieur à 32! Il en est de même pour les orbites à trou!
	 */
	public int hashCode() {
		int result = 0;
		for(int i : dim) {
			result = result|(1 << i);
		}
		// return super.hashCode();
		return result;
	}

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

	public Iterator/*<Integer>*/ iterator() {
		return new JerboaOrbitIterator(dim);
	}

	class JerboaOrbitIterator implements Iterator/*<Integer>*/ {
		private int[] dim;
		private int pos;
		public JerboaOrbitIterator(int[] dim) {
			this.dim = dim;
			pos = 0;
		}

		public boolean hasNext() {
			return (pos < dim.length);
		}

		public Integer next() {
			return dim[pos++];
		}

		public void remove() {
			// not supported
		}
		
	}
}

