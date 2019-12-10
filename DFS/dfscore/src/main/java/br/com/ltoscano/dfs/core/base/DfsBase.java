package br.com.ltoscano.dfs.core.base;

import br.com.ltoscano.dfs.core.logging.DfsLogger;

/**
 *
 * @author ltosc
 */
public abstract class DfsBase
{
    private final DfsLogger logger;
    
    public DfsBase(Class classLogger)
    {
        this.logger = new DfsLogger(classLogger);
    }
    
    public void logFatal(Throwable ex)
    {
        logFatal(ex.getMessage(), ex);
    }
    
    public void logFatal(String msg, Throwable ex)
    {
        logger.logFatal(msg, ex);
    }
    
    public void logError(Throwable ex)
    {
        logError(ex.getMessage(), ex);
    }
    
    public void logError(String msg, Throwable ex)
    {
        logger.logError(msg, ex);
    }
    
    public void logWarn(String msg)
    {
        logger.logWarn(msg);
    }
    
    public void logInfo(String msg)
    {
        logger.logInfo(msg);
    }
    
    public void logDebug(String msg)
    {
        logger.logDebug(msg);
    }
    
    public void handleFatalError(Exception ex)
    {
        handleFatalError(ex.getMessage(), ex);
    }
    
    public void handleFatalError(String msg, Exception ex)
    {
        logFatal(msg, ex);
        System.exit(-1);
    }
    
    public void handleErrorWithRethrow(Exception ex) throws Exception
    {
        handleError(ex);
        throw ex;
    }
    
    public void handleError(Exception ex)
    {
        handleError(ex.getMessage(), ex);
    }
    
    public void handleError(String msg, Exception ex)
    {
        logError(msg, ex);
    }
}
