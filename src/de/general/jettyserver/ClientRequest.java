package de.general.jettyserver;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.util.HashMap;
import java.util.Set;
import sun.security.action.*;


/**
 * Instances of this class represents the HTTP request a client has issued.
 *
 * @author knauth
 */
public class ClientRequest
{

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String requestPath;
	private String requestQuery;
	private HashMap<String, String> parameters;
	private boolean bMapChanged;
	private String method;

	private String defaultEncoding;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ClientRequest(String requestPath, String requestQuery, String method)
	{
		this.requestPath = requestPath;
		this.requestQuery = requestQuery;
		this.method = method;

		this.defaultEncoding = AccessController.doPrivileged(
            new GetPropertyAction("file.encoding")
        );
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected HashMap<String, String> parseRequest()
	{
		HashMap<String, String> parameters = new HashMap<String, String>();
		String requestQuery = this.requestQuery;
		while (requestQuery != null) {
			int pos = requestQuery.indexOf('&');
			String s = null;
			if (pos >= 0) {
				s = requestQuery.substring(0, pos);
				requestQuery = requestQuery.substring(pos + 1);
			} else {
				s = requestQuery;
				requestQuery = null;
			}
			pos = s.indexOf('=');
			if (pos < 0) continue;

			String key = s.substring(0, pos);
			String value = s.substring(pos + 1);
			try {
				key = URLDecoder.decode(key, "UTF-8");
				value = URLDecoder.decode(value, "UTF-8");
			} catch (Exception ee) {
				try {
					key = URLDecoder.decode(key, defaultEncoding);
					value = URLDecoder.decode(value, defaultEncoding);
				} catch (Exception ee2) {
					throw new RuntimeException("Unexpected error: " + ee.toString(), ee);
				}
			}
			try {
				if (value != null) {
					value = value.trim();
					if (value.length() > 0) {
						parameters.put(key, value);
					}
				}
			} catch (Exception ee) {
				throw new RuntimeException("Unexpected error: " + ee.toString(), ee);
			}
		}
		return parameters;
	}

	protected String buildRequestQueryString()
	{
		StringBuilder sb = new StringBuilder();
		try {
			for (String key : parameters.keySet()) {
				String value = parameters.get(key);
				if (sb.length() > 0) {
					sb.append('&');
				}
				sb.append(URLEncoder.encode(key, "UTF-8"));
				sb.append('=');
				sb.append(URLEncoder.encode(value, "UTF-8"));
			}
		} catch (Exception ee) {
			throw new RuntimeException("Unexpected error: " + ee.toString(), ee);
		}
		return sb.toString();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Direct access to the parameter map. If you change the parameter map please be careful: Don't assign <code>null</code> to keys; remove them instead.
	 */
	public HashMap<String, String> parameters()
	{
		return parameters;
	}

	public void removeRequestParameter(String key)
	{
		if (parameters == null) {
			parameters = parseRequest();
		}
		parameters.remove(key);
		bMapChanged = true;
	}

	public void setRequestParameter(String key, String value)
	{
		if (parameters == null) {
			parameters = parseRequest();
		}
		parameters.put(key, value);
		bMapChanged = true;
	}

	public String getRequestParameter(String key)
	{
		if (parameters == null) {
			parameters = parseRequest();
		}
		return parameters.get(key);
	}

	public Set<String> getRequestParameterKeys()
	{
		if (parameters == null) {
			parameters = parseRequest();
		}
		return parameters.keySet();
	}

	/**
	 * The request path, such as "/mypage.html"
	 */
	public String getRequestPath()
	{
		return requestPath;
	}

	public String getMethod()
	{
		return method;
	}

	/**
	 * The request query string, such as "name=SomeName&whereToGo=SomePlace"
	 */
	public String getRequestQuery()
	{
		if (bMapChanged) {
			requestQuery = buildRequestQueryString();
			bMapChanged = false;
		}
		return requestQuery;
	}

}
