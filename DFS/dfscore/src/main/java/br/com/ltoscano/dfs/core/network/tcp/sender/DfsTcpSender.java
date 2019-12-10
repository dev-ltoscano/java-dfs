package br.com.ltoscano.dfs.core.network.tcp.sender;

import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import br.com.ltoscano.dfs.core.network.tcp.DfsTcpConnection;
import java.io.IOException;

/**
 *
 * @author ltosc
 */
public final class DfsTcpSender 
{
    private DfsTcpSender()
    {
        
    }
    
    public static DfsTcpConnection open(DfsNetworkAddress peerAddress, DfsEventMessage eventArgs) throws IOException, DfsException
    {
        DfsTcpConnection connection = new DfsTcpConnection(peerAddress);
        connection.writeEvent(eventArgs);
        
        return connection;
    }
    
    public static DfsEventMessage send(DfsNetworkAddress peerAddress, DfsEventMessage eventArgs) throws IOException, DfsException
    {
        DfsTcpConnection connection = null;
        
        try 
        {
            connection = new DfsTcpConnection(peerAddress);
            connection.writeEvent(eventArgs);
            
            return connection.readEvent();
        }
        finally
        {
            if(connection != null)
            {
                connection.close();
            }
        }
    }
}
