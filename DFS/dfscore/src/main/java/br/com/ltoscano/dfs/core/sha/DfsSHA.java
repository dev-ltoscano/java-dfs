package br.com.ltoscano.dfs.core.sha;

import br.com.ltoscano.dfs.core.exception.DfsRuntimeException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author ltosc
 */
public final class DfsSHA
{
    public enum DfsSHAVersion { SHA1, SHA256 }
    
    private DfsSHA()
    {
        
    }
    
    public static String generate(String input, DfsSHAVersion shaVersion)
    {
        return generate(input.getBytes(StandardCharsets.UTF_8), shaVersion);
    }
    
    public static String generate(byte[] byteArray, DfsSHAVersion shaVersion)
    {
        MessageDigest msgDigest = null;
        
        try
        {
            switch (shaVersion) 
            {
                case SHA1:
                {
                    msgDigest = MessageDigest.getInstance("SHA-1");
                    break;
                }
                case SHA256: 
                {
                    msgDigest = MessageDigest.getInstance("SHA-256");
                    break;
                }
            }
        }
        catch(NoSuchAlgorithmException ex)
        {
            throw new DfsRuntimeException("The SHA algorithm version is invalid");
        }
        
        return Hex.encodeHexString(msgDigest.digest(byteArray));
    }
}
