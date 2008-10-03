/*
 *  Command.java
 *
 *  Created:	August 7, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public interface Command {

	final static public String A_WORD = "\\w+";

	final static public String WHITE_SPACE = "\\s+";

	final static public String COMMA_WITH_WS = "\\s*,\\s*";

	final static public String EQUALS_WITH_WS = "\\s*=\\s*";
	
	final static public String GREATER_THAN_WITH_WS = "\\s*>\\*";
	
	final static public String LESS_THAN_WITH_WS = "\\s*<\\s*";
	
	final static public String GREATER_THAN_EQUALS_W_WS = "\\s*>=\\*";
	
	final static public String LESS_THAN_EQUALS_W_WS = "\\s*<=\\*";
	
	final static public String NOT_EQUALS_W_WS = "\\s*<>\\s*";
	
	static public Pattern TOKENIZER = Pattern.compile(
			// anything less...
			"[^\\s\\w,<>=\\(\\)\\u0027]|"
					+
					// groups we are looking for...
					"\\w+|" + "\\u0027|" + "\\s*<>\\*|" + "\\s*>=\\s*|"
					+ "\\s*<=\\s*|" + "\\s*=\\s*|" + "\\s*,\\s*|"
					+ "\\s*>\\s*|" + "\\s*<\\s*|" + "\\s?+|" + "\\(|" + "\\)|",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	public ArrayList<Object> execute();

	/**
	 * 
	 * @return the original unmodified command sent to the constructor of the
	 *         command object
	 */
	public String toCommandString();
}
