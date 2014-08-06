package de.general.jettyserver.impl;


import org.eclipse.jetty.util.log.*;

import de.general.util.*;
import de.general.log.*;


/**
 *
 * @author knauth
 */
public class JettyToStdLog implements Logger
{

	ILogInterface log;
	boolean bDebuggingEnabled = true;

	public JettyToStdLog(ILogInterface log)
	{
		this.log = log;
	}

    /**
     * @return the name of this logger
     */
    public String getName()
	{
		return "JettyToStdLog";
	}

    /**
     * Formats and logs at warn level.
     * @param msg the formatting string
     * @param args the optional arguments
     */
    public void warn(String msg, Object... args)
	{
		log.warn(msg);
	}

    /**
     * Logs the given Throwable information at warn level
     * @param thrown the Throwable to log
     */
    public void warn(Throwable thrown)
	{
		log.error(thrown);
	}

    /**
     * Logs the given message at warn level, with Throwable information.
     * @param msg the message to log
     * @param thrown the Throwable to log
     */
    public void warn(String msg, Throwable thrown)
	{
		log.warn(msg);
		log.error(thrown);
	}

    /**
     * Formats and logs at info level.
     * @param msg the formatting string
     * @param args the optional arguments
     */
    public void info(String msg, Object... args)
	{
		log.debug(msg);
	}

    /**
     * Logs the given Throwable information at info level
     * @param thrown the Throwable to log
     */
    public void info(Throwable thrown)
	{
		log.error(thrown);
	}

    /**
     * Logs the given message at info level, with Throwable information.
     * @param msg the message to log
     * @param thrown the Throwable to log
     */
    public void info(String msg, Throwable thrown)
	{
		log.debug(msg);
		log.error(thrown);
	}

    /**
     * @return whether the debug level is enabled
     */
    public boolean isDebugEnabled()
	{
		return bDebuggingEnabled;
	}

    /**
     * Mutator used to turn debug on programmatically.
     * @param enabled whether to enable the debug level
     */
    public void setDebugEnabled(boolean enabled)
	{
		this.bDebuggingEnabled = enabled;
	}

    /**
     * Formats and logs at debug level.
     * @param msg the formatting string
     * @param args the optional arguments
     */
    public void debug(String msg, Object... args)
	{
		log.debug(msg);
	}

    /**
     * Logs the given Throwable information at debug level
     * @param thrown the Throwable to log
     */
    public void debug(Throwable thrown)
	{
		log.error(thrown);
	}

    /**
     * Logs the given message at debug level, with Throwable information.
     * @param msg the message to log
     * @param thrown the Throwable to log
     */
    public void debug(String msg, Throwable thrown)
	{
		log.debug(msg);
		log.error(thrown);
	}

    /**
     * @param name the name of the logger
     * @return a logger with the given name
     */
    public Logger getLogger(String name)
	{
		return this;
	}

    /**
     * Ignore an exception.
     * <p>This should be used rather than an empty catch block.
     */
    public void ignore(Throwable ignored)
	{
	}

	@Override
	public void debug(String msg, long l)
	{
		log.debug(msg);
	}

}
