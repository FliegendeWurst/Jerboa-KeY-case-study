package fr.up.xlim.sic.ig.jerboa.jme.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JMENodeMultiplicity {
	private int min;
	private int max;
	
	public JMENodeMultiplicity(int min, int max) {
		this.min = min;
		this.max = max;
		if(min > max)
			throw new RuntimeException("min > max");
		if(min < 0)
			throw new RuntimeException("min < 0");
		if(max < 0)
			throw new RuntimeException("max < 0");
	}

	public int getMin() { return min; }
	public int getMax() { return max; }
	
	public String getMinChar() { 
		if(min == Integer.MAX_VALUE)
			return "*";
		else
			return String.valueOf(min);
	}
	
	public String getMaxChar() {
		if(max == Integer.MAX_VALUE)
			return "*";
		else
			return String.valueOf(max);
	}
	
	@Override
	public String toString() {
		if(min == Integer.MAX_VALUE && max == Integer.MAX_VALUE) {
			return "{*}";
		}
		else if(max == Integer.MAX_VALUE) {
			return "{"+min+"..*}";
		}
		else if(min == max) {
			return "{"+min+"}";
		}
		else {
			return "{"+min+".."+max+"}";
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof JMENodeMultiplicity) {
			JMENodeMultiplicity m = (JMENodeMultiplicity) obj;
			return (m.min == min) && (m.max == max); 
		}
		return super.equals(obj);
	}
	
	
	public static JMENodeMultiplicity parseMultiplicity(String multi) {
		String spattern = "(\\d+|[*]|[nm])";
		Pattern pattern = Pattern.compile(spattern);
		Matcher matcher = pattern.matcher(multi);
		ArrayList<Integer> values = new ArrayList<>(2);
		while(matcher.find()) {
			for(int i = 0;i < matcher.groupCount(); i++) {
				String pat = matcher.group(i);
				if(pat.equals("*") || pat.equals("n") || pat.equals("m"))
					values.add(Integer.MAX_VALUE);
				else
					values.add(Integer.parseInt(pat));
			}
		}
		
		JMENodeMultiplicity res;
		switch(values.size()) {
		case 0:
			res = new JMENodeMultiplicity(1, 1);
			break;
		case 1:
			res = new JMENodeMultiplicity(values.get(0), values.get(0));
			break;
		default:
			res = new JMENodeMultiplicity(values.get(0), values.get(1));
			break;
		}
		return res;
	}
}
