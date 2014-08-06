package de.general.jettyserver;


import de.general.jettyserver.ClientRequest;
import de.general.jettyserver.ResponseContainer;
import java.util.*;

import de.general.jettyserver.*;
import de.general.util.*;
import de.general.log.*;


/**
 *
 * @author knauth
 */
public class RequestDispatcher<APPRUNTIME extends IAppRuntime> implements IRequestHandler<APPRUNTIME>
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	ArrayList<IRequestHandler> requestHandlers;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	/**
	 * Constructor.
	 */
	public RequestDispatcher()
	{
		requestHandlers = new ArrayList<>();
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	public void add(IRequestHandler handler)
	{
		requestHandlers.add(handler);
	}

	@Override
	public ResponseContainer processRequest(APPRUNTIME appRuntime, ClientRequest requestWrapper, ILogInterface log) throws Exception
	{
		for (int i = 0; i < requestHandlers.size(); i++) {
			ResponseContainer w = requestHandlers.get(i).processRequest(appRuntime, requestWrapper, log);
			if (w != null) return w;
		}
		return null;
	}

}
