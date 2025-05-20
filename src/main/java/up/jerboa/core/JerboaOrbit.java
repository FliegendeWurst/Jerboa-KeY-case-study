/**
 * 
 */
package up.jerboa.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import up.jerboa.core.util.JerboaOrbitter;
import up.jerboa.core.util.JerboaOrbitterDefault;
import up.jerboa.core.util.Pair;
import up.jerboa.core.util.JerboaOrbitterDefault.JerboaOrbitterEmpty;
import up.jerboa.exception.JerboaException;
import up.jerboa.exception.JerboaNoFreeMarkException;
import up.jerboa.exception.JerboaOrbitFormatException;
import up.jerboa.exception.JerboaOrbitIncompatibleException;
import up.jerboa.exception.MultiOrbitException;
import up.jerboa.util.StopWatch;

/**
 * This class represent an orbit in our topological modeler.
 * 
 * @author Hakim Belhaouari
 *
 */
public class JerboaOrbit implements Iterable<Integer> {
	
	private static HashMap<String, JerboaOrbit> poolOrbits;
	
	static {
		poolOrbits = new HashMap<>();
	}
	
	public static final int NOVALUE = -1;
	
	/** array that reminds the parameter of the current orbit */
	private int dim[];
	
	/** variable used as cache in order to avoid recompute the max alpha of the dim array */
	private transient int max;
	
	/* Represente l'id de l'orbite il prend en compte l'ordre des elements: <0,1> est different de <1,0>. Au passage, une orbite avec un 'trou' n'a pas d'ID.
	 */
	private String id;
	/*
	 * Retient l'Orbit ID qui traduit une equivalence de resultat independemment de son ordre. Attention il ne tient pas compte des trous. En bref, si l'id vaut zero ou moins il n'y a pas d'identifiant
	 */
	private int oid;
	
	private JerboaOrbitter orbitter;
	
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
		
		id = updateId(dimensions);
	}
	
	private JerboaOrbit(String id, int... dimensions) {
		dim = dimensions;
		max = -1;
		this.id = id;
	}
	
	/**
	 * Constructor of an orbit from any collections {@link Collection} of integer. 
	 * 
	 * @param dimensions
	 */
	public JerboaOrbit(Collection<Integer> dimensions) {
		this(convert(dimensions));		
	}
	
	/**
	 * Constructor of an orbit from a renaming function on an already defined
	 * orbit.
	 * 
	 * @param orbit : already defined orbit
	 * @param dimMap : renaming function
	 */
	public JerboaOrbit(JerboaOrbit orbit, Map<Integer, Integer> dimMap) {
		dim = new int[orbit.dim.length];
		for (int i=0; i<dim.length; i++) {
			if (dimMap.containsKey(orbit.dim[i]))
				dim[i] = dimMap.get(orbit.dim[i]);
			else
				dim[i] = JerboaOrbit.NOVALUE;
		}

		max = -1;
		id = updateId(dim);
	}
	
	
	private static String updateId(int[] dim) {
		String id = "";
		
		// next stream convention:
		//   -1 if not acceptable
		//    or any other is acceptable
		int acceptable = Arrays.stream(dim).reduce(0, (a,b) -> { 
			if(a < 0 || b < 0 || a > 512 || b > 512) return -1;
			else return a;
		});
		
		if (acceptable != -1) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0;i < dim.length;i++) {
				sb.append(i).append(dim[i]).append(".");
			}
			id = sb.toString();
		}
		else
			id = null;
		// System.out.println(Arrays.toString(dim)+ " -> code: " + id);

		return id;
	}
	
	public int calcOID() {		
		int res = 0;
		if(getMaxDim() > 31) {
			this.oid = 0;
			return 0;
		}
		
		for(int i : dim) {
			if(i >= 0)
				res = res | 1<<i;
		}
		
		this.oid = res;
		return oid;
	}

	public JerboaOrbit(JerboaOrbit orbit) {
		this.max = orbit.max;
		dim = new int[orbit.dim.length];
		for(int i =0;i < dim.length;i++)
			dim[i] = orbit.dim[i];
		
		this.id = orbit.id;
	}
	
	private static int[] convert(Collection<Integer> dimensions) {
		int[] dim = new int[dimensions.size()];
		int i = 0;
		Iterator<Integer> it = dimensions.iterator();
		while(it.hasNext()) {
			dim[i++] = it.next();
		}	
		return dim;
	}
	
	/*
	private JerboaOrbit(String id, Collection<Integer> dimensions) {
		dim = new int[dimensions.size()];
		int i = 0;
		max = -1;
		Iterator<Integer> it = dimensions.iterator();
		while(it.hasNext()) {
			dim[i++] = it.next();
		}
		this.id = id;
	}
	*/
	
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
	
	public boolean contains(JerboaOrbit orbit) {
		for (int i : orbit.dim) {
			if(!contains(i))
				return false;
		}
		return true;
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
	 * This function is used for simplifying computation during the collect process.
	 * More precisely, we generate a new orbit where all alpha indexes of the current orbit is presented in the argument.
	 * For instance, If the current orbit is <a1,a2> and the argument orbit is <a0,a1>, then the result is reduced to the orbit
	 * &gt;a1&lt;. 
	 * @param orbit is the main orbit
	 * @return Return a new orbit resulting of the filter.   
	 */
	public JerboaOrbit simplify(JerboaOrbit orbit) {
		ArrayList<Integer> res = new ArrayList<Integer>(dim.length);
		for (int i : dim) {
			if(orbit.contains(i))
				res.add(i);
		}
		return JerboaOrbit.orbit(res);
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
	
	public String javaCompliantString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i < dim.length;i++) {
			sb.append(dim[i]);
		}
		return sb.toString();
	}


	/**
	 * Converts the dimensions of the current object into a formatted string representation.
	 * Each dimension is appended to the string, separated by spaces. If a dimension has a 
	 * value of -1, it is represented as an underscore ("_") in the output string.
	 *
	 * To be used when writting to files.
	 * 
	 * @return A string representation of the dimensions, where each dimension is separated 
	 *         by a space, and dimensions with a value of -1 are replaced with underscores.
	 */
	public String fileExportString() {
		StringBuilder sb = new StringBuilder();
		if(dim.length >= 1) {
			if(dim[0] == -1)
				sb.append("_");
			else
				sb.append(dim[0]);
			for(int i=1;i < dim.length;i++) {
				if(dim[i] == -1)
					sb.append(" _");
				else
					sb.append(" ").append(dim[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * Parses the output in the format of fileExportString and builds a JerboaOrbit.
	 * 
	 * @param exportString The string representation of the orbit.
	 * @return A JerboaOrbit object built from the parsed string.
	 * @throws IllegalArgumentException if the input string is invalid.
	 */
	public static JerboaOrbit parseFileExportString(String exportString) {
		if (exportString == null) 
			throw new IllegalArgumentException("Input string cannot be null.");
	
		exportString = exportString.trim();
		if (exportString.isEmpty()) 
			return new JerboaOrbit(); // Return an empty orbit

		String[] parts = exportString.trim().split("\\s+");
		int[] dimensions = new int[parts.length];

		for (int i = 0; i < parts.length; i++) {
			if (parts[i].equals("_")) {
				dimensions[i] = -1; // Representing `_` as -1
			} else {
				try {
					dimensions[i] = Integer.parseInt(parts[i]);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Invalid dimension value: " + parts[i], e);
				}
			}
		}

		return new JerboaOrbit(dimensions);
	}

	public boolean[] diff(JerboaOrbit orbit) {
		boolean[] res = new boolean[Math.min(this.size(), orbit.size())];
		for(int i=0;i < res.length;i++) {
			if(orbit.dim[i] != dim[i])
				res[i] = true;
		}
		return res;
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
	
	/**
	 * Retrieve the internal value for identifying the current orbit. 
	 * @return Return a unique ID for this orbit.
	 */
	public String getId() {
		return id;
	}
	
	
	/**
	 * Cette fonction permet de creer un objet orbite a partir des differentes dimensions.
	 * Exemple: JerboaOrbit.orbit(0,1) correspond a l'orbite face 2D <a0,a1>.
	 * Normalement l'ordre ne doit pas avoir d'importance mais il est conseille de les ordonner dans l'ordre croissant
	 * par convention.
	 * @param dim: enumeration  
	 * @return Renvoie un objet representant l'orbite en question.
	 */
	public static JerboaOrbit orbit(int... dim) {
		
		String id = null;
		id = updateId(dim);
		if(poolOrbits.containsKey(id))
			return poolOrbits.get(id);
		else 
		{
			JerboaOrbit o = new JerboaOrbit(id,dim);
			if(id != null) {
				poolOrbits.put(id, o);
				// System.out.println("size pool orbit: " + poolOrbits.size());
			}
			return o;
		}
	}
	
	/**
	 * Cette fonction permet de creer un objet orbite a partir d'une collection d'entier.
	 * @param dim: collection d'entier
	 * @return Renvoie un objet representant l'orbite en question
	 */
	public static JerboaOrbit orbit(Collection<Integer> dimensions) {
		return orbit(convert(dimensions));
	}
	
	/**
	 * This function is a tool for converting a string into an orbit object.
	 * The strings must have a border with '<'/'>' or '('/')' and each dimension must be separated with a comma (with or without the alpha/a letter).
	 * Exemple: 
	 * <0,a2>
	 * (0,1,2,3,4)
	 * 
	 * Good convention: The official characters are <a0,a2> in order to readable for humans.
	 * @param orbit: corresponds to the string representation of an orbit
	 * @return Return the JerboaOrbit associated to the string representation.
	 */
	public static JerboaOrbit parseOrbit(String orbit) {
		orbit = orbit.trim();
		orbit = orbit.substring(1, orbit.length()-1);
		String[] ax = orbit.split(",");
		ArrayList<Integer> dims = new ArrayList<>();
		for (int i = 0; i < ax.length; i++) {
			String s = (ax[i]).trim();
			if (s.length() > 0) {
				try {
					dims.add(Integer.parseInt(s));
				} catch (NumberFormatException nfe) {
					dims.add(Integer.parseInt(s.substring(1)));
				}
			}
		}
		int[] tdims = new int[dims.size()];
		for (int i=0;i < tdims.length; i++) {
			tdims[i] = dims.get(i);
		}
		return orbit(tdims);
	}
	
	public static void main(String[] args) {
		String[] cases = { "<>","<a1>","<a1,a2>","<a1,a2,a3>","<a1,a2,a3,a4>" };
		
		for(int i = 0;i < cases.length;i++) {
			try {
				JerboaOrbit orbit = parseOrbit(cases[i]);
				System.out.println(orbit);
				System.out.println(orbit.javaCompliantString());
				System.out.println(orbit.fileExportString());
				System.out.println(parseFileExportString(orbit.fileExportString())+"\n");
			}
			catch(JerboaOrbitFormatException jofe) {
				System.out.println("Ex: "+jofe.getMessage());
			}
		}

		JerboaOrbit[] orbits = {
			new JerboaOrbit(1),
			new JerboaOrbit(-1),
			new JerboaOrbit(0, 1, -1),
			new JerboaOrbit(0, -1, 1),
			new JerboaOrbit(-1, 0,  1),
			new JerboaOrbit( 0, 1, 2),
		};
		for (int i = 0;i < orbits.length;i++) {
			System.out.println(orbits[i].javaCompliantString());
			System.out.println(orbits[i].fileExportString());
			System.out.println(parseFileExportString(orbits[i].fileExportString())+"\n");
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

	/**
	 * This function is an alternative of the parseOrbit, because it looks for any number in the input and consider it
	 * as a part of the orbit. In other words, it extracts an orbit from the input also it is not a real orbit.
	 * To use very carefully!!!
	 * @param orbits: input string with some number somewhere
	 * @return return the orbit extracted for the input.
	 * @see JerboaOrbit#parseOrbit(String)
	 */
	public static JerboaOrbit extractOrbit(String orbits) {
		
		ArrayList<String> list = new ArrayList<>();
		String spattern = "(_|-1|\\d+)";
		Pattern pattern = Pattern.compile(spattern);
		Matcher matcher = pattern.matcher(orbits);
		while (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) {
				String pat = matcher.group(i);
				if (pat.startsWith("-") || pat.startsWith("_"))
					list.add("_");
				else
					list.add(pat);
			}
		}

		orbits = "<";
		for (int i = 0; i < list.size(); i++) {
			String part = list.get(i);
			orbits += part;
			if (i != list.size() - 1)
				orbits += ", ";
		}
		orbits += ">";
		return parseOrbit(orbits);
	}
	
	
	public List<Pair<Integer, List<Integer>>> cycles() {
		return Arrays.stream(dim)
				.mapToObj(d -> {
					List<Integer> list = Arrays.stream(dim).filter(p -> { return (Math.abs(p - d) >= 2); }).boxed().collect(Collectors.toList());
					return new Pair<Integer, List<Integer>>(d, list);
				}).collect(Collectors.toList());
	}
	
	public List<JerboaDart> orbit(JerboaDart start) throws JerboaException {
		List<JerboaDart> res = new ArrayList<>();
		JerboaGMap gmap = start.getOwner();
		
		if(getMaxDim() > start.getDimension())
			throw new JerboaOrbitIncompatibleException("In orbit find a dimension "+getMaxDim()+" whereas max accepted is "+start.getDimension());

		
		if(orbitter == null) {
			orbitter = chooseOrbitter();
		}
		
		if(orbitter != null) {
			return orbitter.orbit(this, start);
		}
		else {
			JerboaMark marker = null;
			try {
				marker = gmap.creatFreeMarker();
				res = markOrbit(gmap, start, marker);
			} catch (JerboaNoFreeMarkException e) {
				e.printStackTrace();
				throw e;
			} catch (JerboaException e) {
				throw e;
			}
			finally {
				if(marker != null)
					gmap.freeMarker(marker);
			}
		}
		
		return res;
	}

	private JerboaOrbitter chooseOrbitter() throws JerboaException {
		calcOID();
		
		if(oid == 0) 
			return new JerboaOrbitterEmpty();
		
		
		if(dim.length == 2) {
			if(Math.abs(dim[0] - dim[1]) >= 2) {
				System.out.println("One cycle!");
				return new JerboaOrbitterDefault.JerboaOrbitterOneCycle(dim[0], dim[1]); 
			}
		}
		
		return new JerboaOrbitterDefault.JerboaOrbitterGenCycle(this);
	}

	private List<JerboaDart> markOrbit(JerboaGMap gmap, JerboaDart start, JerboaMark marker) throws JerboaException {
		ArrayList<JerboaDart> res = new ArrayList<JerboaDart>();
		ArrayDeque<JerboaDart> stack = new ArrayDeque<JerboaDart>();
		stack.push(start);
		{
			while(!stack.isEmpty()) {
				JerboaDart cur = stack.pop();
				if(cur.isNotMarked(marker)) {
					gmap.mark(marker,cur);
					res.add(cur);
					for(int a : tab()) {
						stack.push(cur.alpha(a));
					}
				}
			}
		}
		return res;
	}
	
	
	/**
	 * Compute orbits for multiples darts. The advantage of this function is to compute orbit simultaneously over all given darts.
	 * The current implementation use parallel stream of Java. There is no use of marker or Tag, but a wide array (for all darts of the gmap)
	 * is created and returned with mapping that darts that income from same orbit (second part). The left hand side of the returned result use the convention: index = id of the dart and the content indicate which dart that identifies the orbit.
	 * More precisely, for the cell i, you have information for the dart i and the content of the cell equals -1 if the dart i is not in searched orbit or 
	 * has the value of and input dart, the right hand side provides mapping between given dart and others if at least two darts are in the same orbit.
	 * @param gmap: input gmap
	 * @param orbit: wanted orbit
	 * @param starts: list of started darts
	 * @return a list where index correspond to a dart and the content indicates the input dart which identifies searched orbit, otherwise the content equals -1. 
	 * 		The second part give possible redundancy orbits.
	 */
	public static Pair<List<Integer>, Map<Integer,Integer>> multiorbit_fast(JerboaGMap gmap, JerboaOrbit orbit, Collection<JerboaDart> starts) {
		StopWatch sw = new StopWatch();
		sw.display("multiorbit "+orbit + " on (" + starts.size()+"): " + (starts.size() < 50? starts : "" ));
		int length = gmap.getLength();
		List<Integer> res = IntStream.range(0, length).map(i -> -1).boxed().collect(Collectors.toList());
		starts.stream().forEach(d -> res.set(d.getID(), d.getID()));
		
		Map<Integer, Integer> overlaps = new ConcurrentHashMap<>();
		
		sw.display("end preparation");
		//int loop = 1;
		boolean cont = true;
		while(cont) {
			cont = false;
			int sumModif = IntStream.range(0, length).parallel().map(id -> {
				int modif = 0;
				if(id != -1) {
					JerboaDart dart = gmap.getNode(id);
					for(int w : orbit.tab()) {
						final int vid = dart.alpha(w).getID();
						
						final int rid = res.get(id);
						final int rvid = res.get(vid);
						
						if(rid == -1 && rvid != -1) {
							res.set(id, rvid);
							modif++;
						}
						else if(rid != -1 && rvid != -1 && rid != rvid) {
							// collision
							int minrid = Math.min(rid, rvid);
							int maxrid = Math.max(rid, rvid);
							// synchronized(overlaps) {
								overlaps.merge(maxrid, minrid, Math::min);
							// }
						}
					}
					return modif;
				}
				else
					return 0;
			}).sum();
			cont = sumModif>0;
			//sw.display("\tloop=" + (loop++) + " modif= " + sumModif);
		}
		
		List<Integer> tmp = res;
		if(overlaps.size() > 0)
			tmp = IntStream.range(0, length).parallel().map(i -> {
				if(res.get(i)==-1) return -1;
				else {
					int key = res.get(i);
					if(overlaps.containsKey(key)) {
						key = overlaps.get(key);
						while(overlaps.containsKey(key))
							key = overlaps.get(key);
					}
					return key;
				}
			}).boxed().collect(Collectors.toList());
			
		sw.display("multiorbit done");
		return new Pair<>(tmp, overlaps);
	}
	
	/**
	 * Really similar to {@link #multiorbit_fast(JerboaGMap, JerboaOrbit, Collection)} but add a post-treatment to reorganize the result into a
	 * more readable form. This treatment impacts performance but may decrease memory usage by destroying the produced array.
	 *  
	 * @param gmap: input gmap
	 * @param orbit: wanted orbit
	 * @param starts: list of darts
	 * @see #multiorbit_fast(JerboaGMap, JerboaOrbit, Collection)
	 * @return
	 */
	public static Map<JerboaDart, List<JerboaDart>> multiorbit(JerboaGMap gmap, JerboaOrbit orbit, Collection<JerboaDart> starts) {
		Pair<List<Integer>, Map<Integer,Integer>> maps = multiorbit_fast(gmap, orbit, starts);
		List<Integer> res = maps.l();
		StopWatch sw = new StopWatch();
		sw.display("start presentation of results");
		Map<JerboaDart, List<JerboaDart>> rr = IntStream.range(0, res.size()).parallel().filter(id -> res.get(id) != -1).mapToObj(id -> gmap.getNode(id)).collect(
				Collectors.groupingByConcurrent(d -> gmap.getNode(res.get(d.getID())))
				);
		sw.display("multiorbit presentation done");
		return rr;
	}
	
	public static List<Integer> multiorbit(JerboaGMap gmap, List<JerboaOrbit> orbits, List<JerboaDart> darts) {
		MultiOrbitException moe = new MultiOrbitException();
		final int length = gmap.getLength();
		List<Integer> res = IntStream.range(0, length).map(i -> -1).boxed().collect(Collectors.toList());
		
		IntStream.range(0, darts.size()).parallel().forEach(i -> { res.set(darts.get(i).getID(), i);});
		
		boolean cont = true;
		while(cont) {
			cont = false;
			int sumModif = IntStream.range(0, length).parallel().map(id -> {
				int modif = 0;
				if(id != -1) {
					JerboaDart dart = gmap.getNode(id);
					int oid = res.get(id);
					if(oid != -1) {
						JerboaOrbit orbit = orbits.get(oid);
						for(int w : orbit.tab()) {
							final int vid = dart.alpha(w).getID();

							final int rid = res.get(id);
							final int rvid = res.get(vid);

							if(rid == -1 && rvid != -1) {
								res.set(id, rvid);
								modif++;
							}
							else if(rid != -1 && rvid != -1 && rid != rvid) {
								// collision
								int minrid = Math.min(rid, rvid);
								int maxrid = Math.max(rid, rvid);
							}
						}
					}
					return modif;
				}
				else
					return 0;
			}).sum();
			cont = sumModif>0;
			//sw.display("\tloop=" + (loop++) + " modif= " + sumModif);
		}
		
		return res;
	}
	
	
	public static JerboaDart refereeVertex(JerboaDart dart, JerboaOrbit orbVertex) {
		Comparator<JerboaDart> comparator = new Comparator<JerboaDart>() {

			@Override
			public int compare(JerboaDart o1, JerboaDart o2) {
				return Integer.compare(o1.getID(), o2.getID());
			}
		};
		JerboaDart res = dart;
		try {
			Optional<JerboaDart> r = dart.getOwner().orbit(dart, orbVertex).stream().min(comparator);
			if(r.isPresent())
				res =  r.get();
		} catch (JerboaException e) {
			e.printStackTrace();
		}
		return res;
	}
}

