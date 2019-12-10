package br.com.ltoscano.dfs.core.service.discovery;

import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfo;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import br.com.ltoscano.dfs.core.service.DfsService;
import br.com.ltoscano.dfs.core.service.discovery.listener.DfsPeerAnnouncementEventListener;
import br.com.ltoscano.dfs.core.service.discovery.task.DfsPeerAnnouncementTask;
import br.com.ltoscano.dfs.core.service.discovery.task.DfsPeerStatusTask;
import java.net.UnknownHostException;

/**
 *
 * @author ltosc
 */
public class DfsDiscoveryService extends DfsService
{
    public DfsDiscoveryService(DfsPeerInfo peerInfo, DfsPeerInfoTable peerTable) throws UnknownHostException
    {
        super(DfsDiscoveryService.class, "Discovery");
        
        registerTask(new DfsPeerAnnouncementTask(peerInfo));
        registerTask(new DfsPeerStatusTask(peerTable));
        
        registerEventListener(new DfsPeerAnnouncementEventListener(peerInfo, peerTable));
    }
}
