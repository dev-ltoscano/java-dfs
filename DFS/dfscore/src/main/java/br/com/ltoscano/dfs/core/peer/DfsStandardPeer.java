package br.com.ltoscano.dfs.core.peer;

import br.com.ltoscano.dfs.core.exception.DfsRuntimeException;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import br.com.ltoscano.dfs.core.network.multicast.server.DfsMulticastServer;
import br.com.ltoscano.dfs.core.network.tcp.server.DfsTcpServer;
import br.com.ltoscano.dfs.core.service.data.DfsDataService;
import br.com.ltoscano.dfs.core.service.discovery.DfsDiscoveryService;
import br.com.ltoscano.dfs.core.service.manager.DfsManagerService;
import br.com.ltoscano.dfs.core.service.metadata.DfsMetadataService;
import java.net.UnknownHostException;

/**
 *
 * @author ltosc
 */
public class DfsStandardPeer extends DfsPeer
{
    public DfsStandardPeer(String workDirPath, String configFilePath, String metadataFilePath, DfsNetworkAddress tcpServerAddress)
    {
        super(workDirPath, configFilePath, metadataFilePath, tcpServerAddress);
        
        try 
        {
            registerNetworkServer(new DfsMulticastServer());
            registerNetworkServer(new DfsTcpServer(getPeerInfo().getAddress()));

            registerService(new DfsManagerService());
            registerService(new DfsDiscoveryService(getPeerInfo(), getPeerInfoTable()));
            registerService(new DfsMetadataService(getPeerInfoTable(), getPeerFileSystem()));
            registerService(new DfsDataService(getPeerWorkDirPath(), getPeerInfoTable(), getPeerFileSystem()));
        } 
        catch (UnknownHostException ex)
        {
            throw new DfsRuntimeException(ex.getMessage());
        }
    }
}
