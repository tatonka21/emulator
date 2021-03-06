/*
 *  @(#)ProtocolValidationException.java
 *
 *  Created:	Oct 19, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.protocol;

/**
 * Thrown when there is a problem validating a protocol, such as if the expected
 * protocol for a piece of data is non-existant, or there is a checksum error.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class ProtocolValidationException extends Exception {

	/**
	 * The serial version ID for this class.
	 */
	private static final long serialVersionUID = 6700091756425477495L;

	/**
	 * The basic constructor, creates an exception with a null message.
	 */
	public ProtocolValidationException() {
		super();
	}

	/**
	 * Constructs a new ProtocolValidationException which has a detail message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public ProtocolValidationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new ProtocolValidationException which as a detail message
	 * and a cause for the exception.
	 * 
	 * @param message
	 *            The detail message.
	 * @param cause
	 *            The cause for the exception.
	 */
	public ProtocolValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new ProtocolValidationException which has a cause for the
	 * exception and a message which indicates the cause.
	 * 
	 * @param cause
	 *            The cause for the exception.
	 */
	public ProtocolValidationException(Throwable cause) {
		super(cause);
	}

}
