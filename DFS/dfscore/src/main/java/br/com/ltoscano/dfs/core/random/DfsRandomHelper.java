package br.com.ltoscano.dfs.core.random;

import java.util.Random;

/**
 *
 * @author ltosc
 */
public final class DfsRandomHelper 
{
    private static final Random random = new Random();
    
    private DfsRandomHelper()
    {
        
    }
    
    public static int randomInteger(int minValue, int maxValue)
    {
        return random.nextInt((maxValue - minValue) + 1) + minValue;
    }
}
