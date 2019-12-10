package br.com.ltoscano.dfs.core.id;

import java.util.UUID;

/**
 *
 * @author ltosc
 */
public final class DfsUUID 
{
    private DfsUUID()
    {
        
    }
    
    public static String generate()
    {
        return UUID.randomUUID().toString();
    }
}
