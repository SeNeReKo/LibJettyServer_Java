package de.general.jettyserver;


import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;

import de.general.util.*;
import de.general.log.*;



/**
 *
 * @author knauth
 */
public class MainRequestProcessor<APPRUNTIME extends IAppRuntime>
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	APPRUNTIME appRuntime;
	IRequestHandler<APPRUNTIME> requestDispatcher;
	IRequestHandler<APPRUNTIME> defaultRequestDispatcher;

	public volatile boolean bLogResponseData = true;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	/**
	 * Constructor.
	 */
	public MainRequestProcessor(APPRUNTIME appRuntime, IRequestHandler<APPRUNTIME> requestDispatcher,
		IRequestHandler<APPRUNTIME> defaultRequestDispatcher)
	{
		this.requestDispatcher = requestDispatcher;
		this.appRuntime = appRuntime;
		this.defaultRequestDispatcher = defaultRequestDispatcher;
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	/**
	 * This method is called by JETTY to get a request processed.
	 *
	 * This method does not implement the request processing logic itself: This is done in the private method <code>__processRequest0()</code>.
	 * This method does primarily perform some debug logic.
	 *
	 * @param	pathInfo			The requested HTTP path
	 * @param	queryString			The requested HTTP query
	 * @param	method				The method requested - "GET" or "POST"
	 * @param	log					A logger
	 */
	public ResponseContainer processRequest(String pathInfo, String queryString, String method, ILogInterface log) throws Exception
	{
		ClientRequest myRequest = new ClientRequest(pathInfo, queryString, method);

		// String extendedErrorReasonDebugDir = appRuntime.getExtendedErrorReasonDebugDir();
		// DebugInformation debugInfo = new DebugInformation(myRequest, extendedErrorReasonDebugDir != null, log);
		// if (debugInfo.bDebuggingEnabled) log = debugInfo.multiLog;

		log.debug("--------------------------------------------------------------------------------------------------------------------------------");
		log.debug("##BEGIN##REQUEST################################################################################################################");
		log.info("#### Request:    Path: " + pathInfo + ", Query: " + queryString + ", Method: " + method);

		ResponseContainer myResponse = __processRequest0(myRequest, log);

		if (bLogResponseData) {
			log.debug("--");
			log.debug("Response (" + myResponse.getContentType() + "):");
			log.debug("--");

			String s = myResponse.getResponseDataAsText();
			String[] lines = XUtils.splitString(s);
			for (String line: lines) {
				log.debug("\t" + line);
			}
		}

		return myResponse;
	}

	/**
	 * This method implements the main procesing logic. It takes a request object as input and will return a response object for output.
	 *
	 * @param	myRequest		The request object representing the request
	 * @param	log				A logger
	 * @return		Returns a response.
	 */
	private ResponseContainer __processRequest0(ClientRequest myRequest, ILogInterface log) throws Exception
	{
		ResponseContainer w = requestDispatcher.processRequest(appRuntime, myRequest, log);
		if ((w == null) && (defaultRequestDispatcher != null)) {
			w = defaultRequestDispatcher.processRequest(appRuntime, myRequest, log);
		}
		if (w != null) return w;
		return ResponseContainer.createTextResponse(EnumError.INTERNAL, "Error: No handler returned a response container.");
	}

}
