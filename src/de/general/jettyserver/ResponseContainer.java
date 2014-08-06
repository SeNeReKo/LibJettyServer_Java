package de.general.jettyserver;


import java.io.*;

import org.jdom2.*;

import de.general.util.*;
import de.general.log.*;
import de.general.io.*;
import de.general.json.JObject;
import de.general.xml.*;


/**
 * Instances of this class represent responses the server wishes to send back to the client.
 *
 * @author knauth
 */
public class ResponseContainer
{

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static final int RESPONSE_TEXT = 1;
	public static final int RESPONSE_HTML = 2;
	public static final int RESPONSE_BINARY = 3;
	public static final int RESPONSE_JSON = 4;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int responseType;
	private String mimeType;
	private String responseDataText;
	private JObject responseDataJSON;
	private byte[] responseDataBinary;
	private boolean bIsError;
	private EnumError errorReason;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ResponseContainer(int responseType)
	{
		this.responseType = responseType;
		switch (responseType) {
			case RESPONSE_TEXT:
				mimeType = "text/plain;charset=utf-8";
				break;
			case RESPONSE_HTML:
				mimeType = "text/html;charset=utf-8";
				break;
			case RESPONSE_BINARY:
				mimeType = "application/x-binary";
				break;
			case RESPONSE_JSON:
				mimeType = "application/json";
				break;
			default:
				throw new ImplementationErrorException("Invalid response type specified!");
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static ResponseContainer createHtmlResponse(String htmlData) throws Exception
	{
		return createHtmlResponse(null, htmlData);
	}

	public static ResponseContainer createHtmlResponse(EnumError errorReason, String htmlData) throws Exception
	{
		ResponseContainer ret = new ResponseContainer(RESPONSE_HTML);
		ret.responseDataText = htmlData;
		ret.bIsError = (errorReason != null) && errorReason.isError();
		ret.errorReason = errorReason;
		return ret;
	}

	public static ResponseContainer createHtmlResponse(String[] htmlData) throws Exception
	{
		return createHtmlResponse(null, htmlData);
	}

	public static ResponseContainer createHtmlResponse(EnumError errorReason, String[] htmlData) throws Exception
	{
		ResponseContainer ret = new ResponseContainer(RESPONSE_HTML);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < htmlData.length; i++) {
			sb.append(htmlData[i]);
			sb.append(XUtils.CRLF);
		}
		ret.responseDataText = sb.toString();
		ret.bIsError = (errorReason != null) && errorReason.isError();
		ret.errorReason = errorReason;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static ResponseContainer createJSONResponse(JObject jsonData) throws Exception
	{
		return createJSONResponse(null, jsonData);
	}

	public static ResponseContainer createJSONResponse(EnumError errorReason, JObject jsonData) throws Exception
	{
		ResponseContainer ret = new ResponseContainer(RESPONSE_JSON);
		ret.responseDataJSON = jsonData;
		ret.bIsError = (errorReason != null) && errorReason.isError();
		ret.errorReason = errorReason;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static ResponseContainer createTextResponse(String textData) throws Exception
	{
		return createTextResponse(null, textData);
	}

	public static ResponseContainer createTextResponse(EnumError errorReason, String textData) throws Exception
	{
		ResponseContainer ret = new ResponseContainer(RESPONSE_TEXT);
		ret.responseDataText = textData;
		if (errorReason == null) errorReason = EnumError.NONE;
		ret.bIsError = (errorReason != null) && errorReason.isError();
		ret.errorReason = errorReason;
		return ret;
	}

	public static ResponseContainer createTextResponse(String[] textData) throws Exception
	{
		return createTextResponse(null, textData);
	}

	public static ResponseContainer createTextResponse(EnumError errorReason, String[] textData) throws Exception
	{
		ResponseContainer ret = new ResponseContainer(RESPONSE_TEXT);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < textData.length; i++) {
			sb.append(textData[i]);
			sb.append(XUtils.CRLF);
		}
		ret.responseDataText = sb.toString();
		ret.bIsError = (errorReason != null) && errorReason.isError();
		ret.errorReason = errorReason;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static ResponseContainer createBinaryResponse(byte[] binData) throws Exception
	{
		return createBinaryResponse(null, binData);
	}

	public static ResponseContainer createBinaryResponse(EnumError errorReason, byte[] binData) throws Exception
	{
		ResponseContainer ret = new ResponseContainer(RESPONSE_BINARY);
		ret.responseDataBinary = binData;
		ret.bIsError = (errorReason != null) && errorReason.isError();
		ret.errorReason = errorReason;
		return ret;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getResponseType()
	{
		return responseType;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public String getResponseTextData()
	{
		return responseDataText;
	}

	public JObject getResponseJSONData()
	{
		return responseDataJSON;
	}

	public byte[] getResponseBinaryData()
	{
		return responseDataBinary;
	}

	public void setResponseJSONData(JObject responseDataJSON)
	{
		this.responseDataJSON = responseDataJSON;
		responseType = RESPONSE_JSON;
		mimeType = "application/json;charset=utf-8";
	}

	public boolean isError()
	{
		return bIsError;
	}

	public boolean isSuccess()
	{
		return !bIsError;
	}

	public void setError(boolean bIsError)
	{
		this.bIsError = bIsError;
	}

	public String getResponseDataAsText() throws Exception
	{
		switch (responseType) {
			case RESPONSE_JSON:
				return responseDataJSON.toJSON();
			case RESPONSE_TEXT:
				return responseDataText;
			case RESPONSE_HTML:
				return responseDataText;
			default:
				throw new ImplementationErrorException("Invalid response type encountered!");
		}
	}

	public String getContentType()
	{
		return mimeType;
	}

	public void setError(EnumError errorReason)
	{
		this.errorReason = errorReason;
	}

	public EnumError getErrorReason()
	{
		return errorReason;
	}

}
