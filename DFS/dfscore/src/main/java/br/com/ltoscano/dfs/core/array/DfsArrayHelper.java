package br.com.ltoscano.dfs.core.array;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author ltosc
 */
public final class DfsArrayHelper 
{
    private DfsArrayHelper()
    {
        
    }
    
    public static byte[] concat(byte[] a, byte[] b) throws IOException
    {
        byte[] c;
        
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(a.length + b.length))
        {
            byteArrayOutputStream.write(a);
            byteArrayOutputStream.write(b);
            
            c = byteArrayOutputStream.toByteArray();
        }
        
        return c;
    }
}
