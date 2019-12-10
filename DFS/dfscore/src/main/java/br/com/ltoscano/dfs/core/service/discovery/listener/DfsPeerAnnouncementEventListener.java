package br.com.ltoscano.dfs.core.service.discovery.listener;

import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsEventSource;
import br.com.ltoscano.dfs.core.event.DfsEventListener;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfo;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import java.net.UnknownHostException;

/**
 *
 * @author ltosc
 */
public class DfsPeerAnnouncementEventListener extends DfsEventListener
{
    private final DfsPeerInfo peerInfo;
    private final DfsPeerInfoTable peerTable;
    
    public DfsPeerAnnouncementEventListener(DfsPeerInfo peerInfo, DfsPeerInfoTable peerTable)
    {
        super(DfsPeerAnnouncementEventListener.class, "PEER_ANNOUNCEMENT", DfsEventSource.Multicast);
        
        this.peerInfo = peerInfo;
        this.peerTable = peerTable;
    }
    
    @Override
    public void onEvent(Object sender, DfsEventMessage eventArgs) throws Exception
    {
        DfsNetworkAddress neighborPeerAddress = null;
        
        try 
        {
            neighborPeerAddress = new DfsNetworkAddress(eventArgs.getArgs("IP"), Integer.valueOf(eventArgs.getArgs("Port")));
        } 
        catch (UnknownHostException ex) 
        {
            handleErrorWithRethrow(ex);
        }
        
        String neighborPeerName = null;
        
        try
        {
            neighborPeerName = eventArgs.getArgs("Name");
        }
        catch(DfsNotFoundException ex)
        {
            handleErrorWithRethrow(ex);
        }
        
        if(!neighborPeerName.equals(peerInfo.getName()))
        {
            if (peerTable.containsPeer(neighborPeerName))
            {
                DfsPeerInfo neighborPeerInfo = peerTable.getPeer(neighborPeerName);
                neighborPeerInfo.setAddress(neighborPeerAddress);
                neighborPeerInfo.updateTimestamp();
                
                logInfo("The peer '" + neighborPeerName + "' is active");
            } 
            else 
            {
                peerTable.addPeer(new DfsPeerInfo(neighborPeerName, neighborPeerAddress));
                logInfo("New peer discovered: " + neighborPeerName);
            }
        }
    }
}
