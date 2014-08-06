package de.general.jettyserver.impl;


import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.util.log.*;

import de.general.util.*;
import de.general.jettyserver.IAppRuntime;
import de.general.log.*;
import de.general.jettyserver.MainRequestProcessor;
import de.general.jettyserver.ResponseContainer;


/**
 * This handler is called by JETTY to process HTML requests.
 */
public class JettyHandler extends AbstractHandler
{

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static MainRequestProcessor processor;
	private static IAppRuntime appRuntime;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public JettyHandler(IAppRuntime appRuntime, MainRequestProcessor processor)
	{
		this.appRuntime = appRuntime;
		this.processor = processor;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is called by JETTY to process an HTML request.
	 * The implementation will delegate any processings in accordance to the strategy pattern
	 * to an instance of <code>MainRequestProcessor</code>.
	 */
	public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        baseRequest.setHandled(true);
		BufferLogger blog = new BufferLogger();
		try {
			ResponseContainer myResponse = processor.processRequest(request.getPathInfo(), request.getQueryString(), request.getMethod(), blog);

			response.setContentType(myResponse.getContentType());
			if (myResponse.isError()) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
			}

			if (myResponse.getResponseType() == ResponseContainer.RESPONSE_BINARY) {
				byte[] data = myResponse.getResponseBinaryData();
				OutputStream os = response.getOutputStream();
				os.write(data);
				os.flush();
			} else {
				String s = myResponse.getResponseDataAsText();
				PrintWriter w = response.getWriter();
				w.println(s);
				w.flush();
			}
			blog.forwardTo(appRuntime.getMainLog());
        } catch (Throwable ee) {
			blog.debug("--------------------------------------------------------------------------------------------------------------------------------");
			blog.debug("Unexpected error:");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ee.printStackTrace(pw);
			String[] lines = XUtils.splitString(sw.toString());
			for (String line: lines) {
				blog.error("\t" + line);
			}
			blog.forwardTo(appRuntime.getMainLog());
			throw new ServletException(ee);
		}
    }

}
