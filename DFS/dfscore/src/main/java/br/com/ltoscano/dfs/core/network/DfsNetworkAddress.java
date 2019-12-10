package br.com.ltoscano.dfs.core.network;

import br.com.ltoscano.dfs.core.random.DfsRandomHelper;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author ltosc
 */
public class DfsNetworkAddress 
{
    private final InetAddress ip;
    private final int port;
    
    public DfsNetworkAddress() throws UnknownHostException
    {
        this(InetAddress.getLoopbackAddress(), DfsRandomHelper.randomInteger(49152, 65535));
    }
    
    public DfsNetworkAddress(String ip, int port) throws UnknownHostException
    {
        this(InetAddress.getByName(ip), port);
    }
    
    public DfsNetworkAddress(InetAddress ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress getIP() 
    {
        return ip;
    }
    
    public String getStringIP() 
    {
        return ip.getHostAddress();
    }

    public int getPort() 
    {
        return port;
    }
    
    public String getStringPort()
    {
        return String.valueOf(port);
    }
    
    @Override
    public String toString()
    {
        return (ip.getHostAddress() + ":" + port);
    }
}
