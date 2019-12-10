package br.com.ltoscano.dfs.core.service.discovery.task;

import br.com.ltoscano.dfs.core.config.DfsConfigurator;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import br.com.ltoscano.dfs.core.network.multicast.sender.DfsMulticastSender;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfo;
import br.com.ltoscano.dfs.core.task.DfsBaseTimerTask;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author ltosc
 */
public class DfsPeerAnnouncementTask extends DfsBaseTimerTask
{
    private final DfsNetworkAddress multicastAddress;
    private final DfsPeerInfo peerInfo;
    
    public DfsPeerAnnouncementTask(DfsPeerInfo peerInfo) throws UnknownHostException
    {
        super(DfsPeerAnnouncementTask.class, 
                "PEER_ANNOUNCEMENT", 
                DfsConfigurator.getConfig().getMinTimeForPeerAnnouncement(),
                DfsConfigurator.getConfig().getMaxTimeForPeerAnnouncement());
        
        this.multicastAddress = new DfsNetworkAddress(DfsConfigurator.getConfig().getMulticastIP(), DfsConfigurator.getConfig().getMulticastPort());
        this.peerInfo = peerInfo;
    }

    @Override
    public void performTask()
    {
        try 
        {
            DfsEventMessage announcementEventMsg = new DfsEventMessage("PEER_ANNOUNCEMENT");
            
            announcementEventMsg.setArgs("Name", peerInfo.getName());
            announcementEventMsg.setArgs("IP", peerInfo.getAddress().getStringIP());
            announcementEventMsg.setArgs("Port", peerInfo.getAddress().getStringPort());
            
            DfsMulticastSender.send(multicastAddress, announcementEventMsg);
            logInfo("The peer has been announced");
        }
        catch (IOException ex)
        {
            handleError(ex);
        }
    }
}
