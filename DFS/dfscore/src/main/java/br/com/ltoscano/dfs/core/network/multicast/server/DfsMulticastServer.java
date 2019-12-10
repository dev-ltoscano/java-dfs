package br.com.ltoscano.dfs.core.network.multicast.server;

import br.com.ltoscano.dfs.core.config.DfsConfigurator;
import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsServerType;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddressHelper;
import br.com.ltoscano.dfs.core.network.server.DfsNetworkServer;
import br.com.ltoscano.dfs.core.network.socket.DfsSocketFactory;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author ltosc
 */
public class DfsMulticastServer extends DfsNetworkServer
{
    private MulticastSocket serverSocket;
    
    public DfsMulticastServer() throws UnknownHostException
    {
        this(new DfsNetworkAddress(DfsConfigurator.getConfig().getMulticastIP(), DfsConfigurator.getConfig().getMulticastPort()));
    }
    
    public DfsMulticastServer(DfsNetworkAddress serverAddress)
    {
        super(DfsMulticastServer.class, DfsServerType.Multicast, serverAddress);
    }

    @Override
    public void run() 
    {
        try 
        {
            serverSocket = DfsSocketFactory.createMulticastSocket(getServerAddress().getPort());
            serverSocket.joinGroup(getServerAddress().getIP());
            
            logInfo("The multicast server was started in " + getServerAddress().toString());
            logInfo("Waiting for packets...");
            
            byte[] receiveBuffer = new byte[1024];

            do 
            {
                try
                {
                    DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    serverSocket.receive(packet);
                    
                    logInfo("New package received from " + DfsNetworkAddressHelper.formatAddress(packet.getAddress(), packet.getPort()));

                    dispatchEvent(DfsEventMessage.fromJson(new String(packet.getData(), 0, packet.getLength())));
                }
                catch(IOException | DfsException ex)
                {
                    handleError(ex);
                }
            }
            while(isRunning());
        }
        catch (IOException ex) 
        {
            handleError(ex);
        }
        finally
        {
            if(serverSocket != null)
            {
                try
                {
                    serverSocket.leaveGroup(getServerAddress().getIP());
                }
                catch (IOException ex)
                {
                    handleError(ex);
                }
                
                serverSocket.close();
                logInfo("The multicast receiver was stopped");
            }
        }
    }
}
