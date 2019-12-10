package br.com.ltoscano.dfs.core.exception;

/**
 *
 * @author ltosc
 */
public class DfsNotFoundException extends DfsException
{
    public DfsNotFoundException()
    {
        super("The directory does not exist");
    }
    
    public DfsNotFoundException(String msg)
    {
        super(msg);
    }
    
    public DfsNotFoundException(Exception ex)
    {
        super(ex);
    }
    
    public DfsNotFoundException(String msg, Exception ex)
    {
        super(msg, ex);
    }
}
