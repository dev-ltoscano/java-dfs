package br.com.ltoscano.dfs.core.exception;

/**
 *
 * @author ltosc
 */
public class DfsRuntimeException extends RuntimeException
{
    public DfsRuntimeException(String msg)
    {
        super(msg);
    }
    
    public DfsRuntimeException(Exception ex)
    {
        super(ex);
    }
    
    public DfsRuntimeException(String msg, Exception ex)
    {
        super(msg, ex);
    }
}
