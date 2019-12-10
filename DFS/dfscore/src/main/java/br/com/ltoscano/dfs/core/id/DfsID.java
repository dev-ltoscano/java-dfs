package br.com.ltoscano.dfs.core.id;

import br.com.ltoscano.dfs.core.sha.DfsSHA;
import br.com.ltoscano.dfs.core.string.DfsStringHelper;

/**
 *
 * @author ltosc
 */
public final class DfsID 
{
    private DfsID()
    {
        
    }
    
    public static String generate(String input)
    {
        if(DfsStringHelper.isNullOrEmpty(input))
        {
            return null;
        }
        
        return DfsSHA.generate(input, DfsSHA.DfsSHAVersion.SHA1);
    }
}