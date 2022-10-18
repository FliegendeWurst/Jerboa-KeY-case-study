package fr.up.xlim.sic.ig.jerboa.jme.util;

import java.util.logging.Level;

public class Logger {

	private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("fr.up.xlim.sic.ig.jerboa.jme"); 


	public static void log(Level level, String mesg) {
		logger.log(level, mesg);
	}
	
	public static void log(Level level, Throwable t) {
		logger.log(level, t.getMessage(), t);
	}
	
}
