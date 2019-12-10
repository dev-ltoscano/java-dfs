package br.com.ltoscano.dfs.core.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ltosc
 */
public final class DfsLogger 
{
    private final Logger logger;
    
    public DfsLogger(Class classLogger)
    {
        this.logger = LogManager.getLogger(classLogger);
    }
    
    private void log(Level level, String msg, Throwable ex)
    {
        logger.log(level, msg, ex);
    }
    
    public void logFatal(String msg, Throwable ex)
    {
        log(Level.FATAL, msg, ex);
    }
    
    public void logError(String msg, Throwable ex)
    {
        log(Level.ERROR, msg, ex);
    }
    
    public void logWarn(String msg)
    {
        log(Level.WARN, msg, null);
    }
    
    public void logInfo(String msg)
    {
        log(Level.INFO, msg, null);
    }
    
    public void logDebug(String msg)
    {
        log(Level.DEBUG, msg, null);
    }
}
