package br.com.ltoscano.dfs.core.exception;

/**
 *
 * @author ltosc
 */
public class DfsAlreadyExistsException extends DfsException
{
    public DfsAlreadyExistsException()
    {
        super("The directory already exists");
    }
    
    public DfsAlreadyExistsException(String msg)
    {
        super(msg);
    }
    
    public DfsAlreadyExistsException(Exception ex)
    {
        super(ex);
    }
    
    public DfsAlreadyExistsException(String msg, Exception ex)
    {
        super(msg, ex);
    }
}
