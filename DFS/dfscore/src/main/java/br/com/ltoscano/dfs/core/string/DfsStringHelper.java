package br.com.ltoscano.dfs.core.string;

/**
 *
 * @author ltosc
 */
public class DfsStringHelper 
{
    public static boolean isNullOrEmpty(String str)
    {
        return ((str == null) || str.isEmpty());
    }
    
    public static String capitalizeFirst(String str)
    {
        if(isNullOrEmpty(str))
        {
            throw new IllegalArgumentException("The string is null or empty");
        }
        
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static String capitalize(String str)
    {
        return str.toUpperCase();
    }
}
