package fr.up.xlim.sic.ig.jerboa.jme.view.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanCppHeaderFile {

	public static void main(String[] args) {
		
		if(args.length > 0) {
			ScanCppHeaderFile scan = new ScanCppHeaderFile();
			for (String string : args) {
				scan.searchCppFile(new File(string));
			}
		}
		else {
			System.out.println("Run with C++ source directory");
		}
		
	}

	private HashSet<String> list;
	
	public ScanCppHeaderFile() {
		list = new HashSet<>();
	}
	
	public void searchCppFile(File dir) {
		System.out.println("DIR: "+dir.getAbsolutePath());
		if(dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File f : files) {
				final String filename = f.getName();
				if(filename.endsWith(".hpp") || filename.endsWith(".h") || filename.endsWith(".hh")) {
					extractClasses(f);
				}
				else if(f.isDirectory()) {
					searchCppFile(f);
				}
			}
		}
		else if(dir.exists() && dir.isFile()) {
			System.out.println(" TODO faire le chargement d'un fichier type par ligne");
		}
		
		System.out.println("======================================");
		for (String string : list) {
			System.out.println(string);
		}
		System.out.println("======================================");
		
	}

	private static Pattern pattern = Pattern.compile("(\\s*(namespace|class)\\s*(\\w+).*)");

	private transient String currentNS;
	
	private void extractClasses(File f) {
		System.out.println("FILE: "+f.getAbsolutePath());	
		currentNS = null;
		try {
			Files.newBufferedReader(f.toPath()).lines().forEach(s -> { Matcher m = pattern.matcher(s); 
				if(m.matches()) 
				{
					if("namespace".equals(m.group(2)))
						currentNS = m.group(3);
					else {
						if(currentNS != null)
							list.add(currentNS+"::"+m.group(3));
						else
							list.add(m.group(3));
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Set<String> getList() {
		return list;
	}
	
	public void reset() {
		list = new HashSet<>();
	}
}
