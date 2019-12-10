package br.com.ltoscano.dfs.core.network.tcp.server;

import br.com.ltoscano.dfs.core.config.DfsConfigurator;
import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsServerType;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddressHelper;
import br.com.ltoscano.dfs.core.network.server.DfsNetworkServer;
import br.com.ltoscano.dfs.core.network.socket.DfsSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author ltosc
 */
public class DfsTcpServer extends DfsNetworkServer
{
    private ServerSocket serverSocket;
    
    public DfsTcpServer() throws UnknownHostException
    {
        this(new DfsNetworkAddress(DfsConfigurator.getConfig().getTcpIP(), DfsConfigurator.getConfig().getTcpPort()));
    }
    
    public DfsTcpServer(DfsNetworkAddress serverAddress) 
    {
        super(DfsTcpServer.class, DfsServerType.Tcp, serverAddress);
        this.serverSocket = null;
    }

    @Override
    public void run() 
    {
        try 
        {
            serverSocket = DfsSocketFactory.createServerSocket(getServerAddress().getPort());
            
            logInfo("The TCP server was started in " + getServerAddress().toString());
            logInfo("Waiting for connections...");
            
            do 
            {
                try
                {
                    Socket clientSocket = serverSocket.accept();
                    logInfo("New connection received from " + DfsNetworkAddressHelper.formatAddress(clientSocket.getInetAddress(), clientSocket.getPort()));
                    
                    dispatchEvent(clientSocket);
                }
                catch(IOException ex)
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
                    serverSocket.close();
                    logInfo("The TCP server was stopped");
                }
                catch (IOException ex)
                {
                    handleError(ex);
                }
            }
        }
    }
}
