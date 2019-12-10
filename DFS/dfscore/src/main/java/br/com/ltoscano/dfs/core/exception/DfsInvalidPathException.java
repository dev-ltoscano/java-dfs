package br.com.ltoscano.dfs.core.exception;

/**
 *
 * @author ltosc
 */
public class DfsInvalidPathException extends DfsException
{
    public DfsInvalidPathException(String msg)
    {
        super(msg);
    }
    
    public DfsInvalidPathException(Exception ex)
    {
        super(ex);
    }
    
    public DfsInvalidPathException(String msg, Exception ex)
    {
        super(msg, ex);
    }
}
