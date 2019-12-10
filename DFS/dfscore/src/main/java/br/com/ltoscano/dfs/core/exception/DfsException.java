package br.com.ltoscano.dfs.core.exception;

/**
 *
 * @author ltosc
 */
public class DfsException extends Exception
{
    public DfsException(String msg)
    {
        super(msg);
    }
    
    public DfsException(Exception ex)
    {
        super(ex);
    }
    
    public DfsException(String msg, Exception ex)
    {
        super(msg, ex);
    }
}
