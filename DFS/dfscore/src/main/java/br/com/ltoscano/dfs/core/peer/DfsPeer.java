package br.com.ltoscano.dfs.core.peer;

import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfo;
import br.com.ltoscano.dfs.core.base.DfsBase;
import br.com.ltoscano.dfs.core.config.DfsConfigurator;
import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsEventSource;
import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsServerType;
import br.com.ltoscano.dfs.core.event.DfsEventListener;
import br.com.ltoscano.dfs.core.filesystem.DfsFileSystem;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import br.com.ltoscano.dfs.core.network.server.DfsNetworkServer;
import br.com.ltoscano.dfs.core.service.DfsService;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author ltosc
 */
public abstract class DfsPeer extends DfsBase
{
    private final List<DfsNetworkServer> serverList;
    private final List<DfsService> serviceList;
    
    private String peerWorkDirPath;
    private final DfsPeerInfo peerInfo;
    private final DfsPeerInfoTable peerInfoTable;
    private final DfsFileSystem peerFileSystem;
    
    public DfsPeer(String workDirPath, String configFilePath, String metadataFilePath, DfsNetworkAddress tcpServerAddress)
    {
        super(DfsPeer.class);
        
        this.serverList = new ArrayList<>();
        this.serviceList = new ArrayList<>();
        
        DfsConfigurator.load(configFilePath);

        this.peerWorkDirPath = Paths.get(workDirPath, DfsConfigurator.getConfig().getPeerName()).toString();
        this.peerInfo = new DfsPeerInfo(DfsConfigurator.getConfig().getPeerName(), tcpServerAddress);
        this.peerInfoTable = new DfsPeerInfoTable();
        this.peerFileSystem = DfsFileSystem.load(metadataFilePath);
    }
    
    private void registerAllServiceEventListener(DfsNetworkServer server)
    {
        for(DfsService service : serviceList)
        {
            Collection<DfsEventListener> eventListenerList = service.getServiceEventListeners();
            
            for(DfsEventListener eventListener : eventListenerList)
            {
                DfsEventSource eventSource = eventListener.getEventSource();
                
                if (eventSource == DfsEventSource.Any)
                {
                    server.registerEventListener(eventListener);
                }
                else
                {
                    if (((eventSource == DfsEventSource.Tcp) && (server.getServerType() == DfsServerType.Tcp))
                            || ((eventSource == DfsEventSource.Multicast) && (server.getServerType() == DfsServerType.Multicast)))
                    {
                        server.registerEventListener(eventListener);
                    }
                }
            }
        }
    }
    
    private void registerAllServiceEventListener(DfsService service)
    {
        Collection<DfsEventListener> eventListenerList = service.getServiceEventListeners();

        for (DfsEventListener eventListener : eventListenerList)
        {
            DfsEventSource eventSource = eventListener.getEventSource();

            for (DfsNetworkServer server : serverList)
            {
                if (eventSource == DfsEventSource.Any) 
                {
                    server.registerEventListener(eventListener);
                }
                else 
                {
                    if (((eventSource == DfsEventSource.Tcp) && (server.getServerType() == DfsServerType.Tcp))
                            || ((eventSource == DfsEventSource.Multicast) && (server.getServerType() == DfsServerType.Multicast))) 
                    {
                        server.registerEventListener(eventListener);
                    }
                }
            }
        }
    }
    
    protected final void registerNetworkServer(DfsNetworkServer networkServer)
    {
        if(!serverList.contains(networkServer))
        {
            serverList.add(networkServer);
            registerAllServiceEventListener(networkServer);
        }
    }
    
    protected final void registerService(DfsService service)
    {
        if(!serviceList.contains(service))
        {
            serviceList.add(service);
            registerAllServiceEventListener(service);
        }
    }
    
    public final void start()
    {
        for(DfsNetworkServer server : serverList)
        {
            server.start();
        }
        
        for(DfsService service : serviceList)
        {
            service.start();
        }
    }
    
    public final void stop() throws Exception
    {
        for(DfsService service : serviceList)
        {
            service.stop();
        }
        
        for(DfsNetworkServer server : serverList)
        {
            server.stop();
        }
    }
    
    public final void save() throws IOException
    {
        DfsConfigurator.save(String.format("%s_config.json", getPeerInfo().getName()));
        getPeerFileSystem().save(String.format("%s_metadata.json", getPeerInfo().getName()));
    }

    public final String getPeerWorkDirPath() 
    {
        return peerWorkDirPath;
    }

    public final void setPeerWorkDirPath(String peerWorkDirPath) 
    {
        this.peerWorkDirPath = peerWorkDirPath;
    }

    public final DfsPeerInfo getPeerInfo() 
    {
        return peerInfo;
    }

    public final DfsPeerInfoTable getPeerInfoTable() 
    {
        return peerInfoTable;
    }

    public final DfsFileSystem getPeerFileSystem() 
    {
        return peerFileSystem;
    }
}