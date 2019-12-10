package br.com.ltoscano.dfs.core.service.discovery.task;

import br.com.ltoscano.dfs.core.config.DfsConfigurator;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfo;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import br.com.ltoscano.dfs.core.task.DfsBaseTimerTask;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ltosc
 */
public class DfsPeerStatusTask extends DfsBaseTimerTask
{
    private final DfsPeerInfoTable peerTable;
    
    public DfsPeerStatusTask(DfsPeerInfoTable peerTable)
    {
        super(DfsPeerStatusTask.class, 
                "PeerStatus", 
                DfsConfigurator.getConfig().getTimeToCheckPeerStatus());
        
        this.peerTable = peerTable;
    }

    @Override
    public void performTask()
    {
        long currTimestamp = System.currentTimeMillis();
        int peerDowntime = DfsConfigurator.getConfig().getPeerDowntime();

        DfsPeerInfo peerInfo;
        Set<String> peerNameList = new HashSet<>(peerTable.getPeerNames());
        
        for(String peerName : peerNameList)
        {
            try 
            {
                peerInfo = peerTable.getPeer(peerName);
                
                if((currTimestamp - peerInfo.getTimestamp()) > peerDowntime)
                {
                    peerTable.removePeer(peerName);
                    logInfo("The peer '" + peerInfo.getName() + "' was removed for inactivity");
                }
            } 
            catch (DfsNotFoundException ex) 
            {
                handleError(ex);
            }
        }
    }
}
