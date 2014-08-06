/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.general.jettyserver;

/**
 *
 * @author knauth
 */
public class EnumError
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	/**
	 * No error occurred. This is the default.
	 */
	public static final EnumError NONE = new EnumError(0);

	/**
	 * Some kind of internal error occurred. Do not use this kind of error within regular request handlers: It is
	 * used by the framework only.
	 */
	public static final EnumError INTERNAL = new EnumError(1);

	/**
	 * Use this error code to indicate that an URL path could not be recognized: This is equivalent to a 404 error.
	 */
	public static final EnumError URI_NOT_FOUND = new EnumError(2);

	/**
	 * An error occurred during processing.
	 */
	public static final EnumError PROCESSING = new EnumError(3);

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	private int errType;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	private EnumError(int errType)
	{
		this.errType = errType;
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	public boolean isError()
	{
		return (errType > 0);
	}

	public int getErrorType()
	{
		return errType;
	}

}
