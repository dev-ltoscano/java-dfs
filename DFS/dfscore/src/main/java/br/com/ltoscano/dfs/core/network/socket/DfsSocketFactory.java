package br.com.ltoscano.dfs.core.network.socket;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.ServerSocket;

/**
 *
 * @author ltosc
 */
public final class DfsSocketFactory
{
    private DfsSocketFactory()
    {
        
    }
    
    public static MulticastSocket createMulticastSocket(int port) throws IOException
    {
        return new MulticastSocket(port);
    }
    
    public static ServerSocket createServerSocket(int port) throws IOException
    {
        return new ServerSocket(port);
    }
}
