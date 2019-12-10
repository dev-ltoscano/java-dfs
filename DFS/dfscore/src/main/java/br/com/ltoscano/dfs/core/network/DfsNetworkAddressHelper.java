package br.com.ltoscano.dfs.core.network;

import java.net.InetAddress;

/**
 *
 * @author ltosc
 */
public final class DfsNetworkAddressHelper 
{
    private DfsNetworkAddressHelper()
    {
        
    }
    
    public static String formatAddress(DfsNetworkAddress networkAddress)
    {
        return networkAddress.toString();
    }
    
    public static String formatAddress(InetAddress ip, int port)
    {
        return (ip.getHostAddress() + ":" + port);
    }
}
