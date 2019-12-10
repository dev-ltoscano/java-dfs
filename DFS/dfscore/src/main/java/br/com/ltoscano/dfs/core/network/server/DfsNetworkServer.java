package br.com.ltoscano.dfs.core.network.server;

import br.com.ltoscano.dfs.core.base.DfsBase;
import br.com.ltoscano.dfs.core.config.DfsConfigurator;
import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsServerType;
import br.com.ltoscano.dfs.core.event.DfsEventListener;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.event.DfsEventDispatcher;
import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import br.com.ltoscano.dfs.core.network.multicast.sender.DfsMulticastSender;
import br.com.ltoscano.dfs.core.network.tcp.sender.DfsTcpSender;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author ltosc
 */
public abstract class DfsNetworkServer extends DfsBase implements Runnable
{
    private final DfsServerType serverType;
    private final DfsNetworkAddress serverAddress;
    private final Thread serverThread;
    private boolean running;
    
    private final DfsEventDispatcher eventDispatcher;
    
    public DfsNetworkServer(Class derivedClass, DfsServerType serverType, DfsNetworkAddress serverAddress)
    {
        super(derivedClass);
        
        this.serverType = serverType;
        this.serverAddress = serverAddress;
        this.serverThread = new Thread(this);
        this.running = false;
        
        this.eventDispatcher = new DfsEventDispatcher();
    }
    
    public void start()
    {
        running = true;
        serverThread.start();
    }
    
    public void stop() throws Exception
    {
        try
        {
            running = false;
            
            switch(serverType)
            {
                case Multicast:
                {
                    DfsMulticastSender.send(serverAddress, new DfsEventMessage("STOP_SERVER"));
                    break;
                }
                case Tcp:
                {
                    DfsTcpSender.send(serverAddress, new DfsEventMessage("STOP_SERVER"));
                    break;
                }
            }
            
            serverThread.join(DfsConfigurator.getConfig().getWaitTimeForServerToStop());
        }
        catch(InterruptedException | IOException | DfsException ex)
        {
            handleErrorWithRethrow(ex);
        }
    }
    
    public void registerEventListener(DfsEventListener eventListener)
    {
        eventDispatcher.registerEventListener(eventListener);
    }
    
    public void unregisterEventListener(DfsEventListener eventListener)
    {
        eventDispatcher.unregisterEventListener(eventListener);
    }
    
    protected void dispatchEvent(DfsEventMessage eventMsg) throws DfsNotFoundException
    {
        getEventDispatcher().fireEvent(eventMsg);
    }
    
    protected void dispatchEvent(Socket clientSocket)
    {
        getEventDispatcher().fireEvent(clientSocket);
    }
    
    public DfsServerType getServerType()
    {
        return serverType;
    }
    
    public DfsNetworkAddress getServerAddress()
    {
        return serverAddress;
    }
    
    public boolean isRunning() 
    {
        return running;
    }
    
    protected DfsEventDispatcher getEventDispatcher()
    {
        return eventDispatcher;
    }
}
