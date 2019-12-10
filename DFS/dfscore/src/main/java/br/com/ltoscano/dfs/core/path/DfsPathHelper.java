package br.com.ltoscano.dfs.core.path;

import br.com.ltoscano.dfs.core.exception.DfsInvalidPathException;
import java.util.regex.Pattern;

/**
 *
 * @author ltosc
 */
public final class DfsPathHelper 
{
    private static final String UNIX_PATH_PATTERN = "^/|^\\.|^\\.\\.|^~|(^\\.|^\\.\\.|^\\~)?(/\\.\\.)*(/[a-zA-Z0-9_-]+)*([a-zA-Z0-9_-]+/?)*((/|(\\.[a-zA-Z0-9]+)+)$)?";
    //private static final String BLOCK_PATH_PATTERN = "(^\\.|^\\.\\.|^\\~)?(/\\.\\.)*((/[a-zA-Z0-9_-]+)+|([a-zA-Z0-9_-]+))(\\.[a-zA-Z0-9]+)+(\\.block[0-9]+)$";
    
    private DfsPathHelper()
    {
        
    }
    
    public static boolean isValidPath(String path)
    {
        return Pattern.compile(UNIX_PATH_PATTERN).matcher(path).matches();
    }
    
    public static String getBasePath(String path) throws DfsInvalidPathException
    {
        if(!isValidPath(path))
        {
            throw new DfsInvalidPathException("The path '" + path + "' is invalid");
        }
        
        int index = path.lastIndexOf('/');
        
        if(index == 0)
        {
            if(path.length() == 1)
            {
                return null;
            }
            else
            {
                return "/";
            }
        }
        else
        {
            return path.substring(0, index);
        }
    }
}
