package de.general.jettyserver;


import de.general.util.*;
import de.general.log.*;



/**
 *
 * @author knauth
 */
public interface IRequestHandler<APPRUNTIME extends IAppRuntime>
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	/**
	 * Process a request.
	 *
	 * @param		appRuntime				The application runtime object
	 * @param		requestWrapper			A wrapper object that provides convenient ways to access
	 *										request data
	 * @param		log						A logger to use during request
	 * @return		Return a value other than <code>null</code> in order to indicate that the request has been
	 *				processed. Return <code>null</code> in order to allow other handlers to process the request.
	 */
	public ResponseContainer processRequest(APPRUNTIME appRuntime, ClientRequest requestWrapper, ILogInterface log) throws Exception;

}
