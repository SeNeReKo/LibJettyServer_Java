package de.general.jettyserver;


import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.log.*;

import de.general.util.*;
import de.general.log.*;
import de.general.jettyserver.impl.*;


/**
 *
 * @author knauth
 */
public class JettyServer<APPRUNTIME extends IAppRuntime>
{

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	private RequestDispatcher requestDispatcher;
	private ILogInterface log;
	private Server server;
	private MainRequestProcessor<APPRUNTIME> processor;
	private boolean bStarted;
	private IAppRuntime appRuntime;
	private IRequestHandler<APPRUNTIME> defaultRequestHandler;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	/**
	 * Constructor that initializes the application object and prepares everything for server start.
	 */
	public JettyServer(Class appRuntimeClass) throws Exception
	{
		PrintLogger pl = new PrintLogger();
		log = pl;

		log.info("Instantiating application runtime object ...");
		appRuntime = (IAppRuntime)(appRuntimeClass.newInstance());

		log.info("Initializing application runtime object ...");
		appRuntime.initialize(pl);
		log = appRuntime.getMainLog();

		requestDispatcher = new RequestDispatcher();
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	/**
	 * Provide an implementation here to handle requests that have not been processed by other request handlers.
	 * Typically the task to be done in that case is to return an error message to the client. The default
	 * implementation simply issues a server error (500) and returns "Resource not found!"
	 */
	public IRequestHandler getDefaultRequestHandler()
	{
		if (defaultRequestHandler == null) {
			defaultRequestHandler = new IRequestHandler<APPRUNTIME>() {
				@Override
				public ResponseContainer processRequest(APPRUNTIME appRuntime, ClientRequest requestWrapper,
					ILogInterface log) throws Exception
				{
					return ResponseContainer.createTextResponse("Resource not found!");
				}
			};
		}
		return defaultRequestHandler;
	}

	/**
	 * Add a request handler.
	 */
	public void add(IRequestHandler<APPRUNTIME> handler)
	{
		requestDispatcher.add(handler);
	}

	/**
	 * Start the server. This method will use the defined logger in order to write log messages.
	 *
	 * @return		Returns <code>true</code> on success. If <code>false</code> either the server has already
	 *				been started or an error occurred.
	 */
	public boolean start()
	{
		if (bStarted) {
			log.error("Server already started!");
			return false;
		}

		try {

			log.info("Starting server ...");

			processor = new MainRequestProcessor(appRuntime, requestDispatcher, getDefaultRequestHandler());

			server = new Server(appRuntime.getWebServerPortFromConfiguration());
			server.setHandler(new JettyHandler(appRuntime, processor));

			server.start();
			log.info("Server startup completed.");
			server.join();

			bStarted = true;
			return true;

		} catch (Throwable ee) {
			Log.setLog(new JettyToStdLog(log));
			do {
				log.error(ee);
				if (ee.getCause() != null) {
					ee = ee.getCause();
				} else {
					break;
				}
			} while (true);

			return false;
		}
	}

	public boolean stop()
	{
		if (!bStarted) return false;

		log.info("Stopping the server ...");

		try {

			server.stop();

		} catch (Throwable ee) {
			Log.setLog(new JettyToStdLog(log));
			do {
				log.error(ee);
				if (ee.getCause() != null) {
					ee = ee.getCause();
				} else {
					break;
				}
			} while (true);

			return false;
		}

		bStarted = false;
		server = null;

		return true;
	}

}
