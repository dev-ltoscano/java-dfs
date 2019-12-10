package br.com.ltoscano.dfs.core.peer.info;

import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author ltosc
 */
public class DfsPeerInfoTable 
{
    private final HashMap<String, DfsPeerInfo> peerMap;
    
    public DfsPeerInfoTable()
    {
        this.peerMap = new HashMap<>();
    }
    
    public synchronized void addPeer(DfsPeerInfo peer)
    {
        peerMap.put(peer.getName(), peer);
    }
    
    public synchronized void removePeer(String peerName)
    {
        peerMap.remove(peerName);
    }
    
    public synchronized boolean containsPeer(String peerName)
    {
        return peerMap.containsKey(peerName);
    }
    
    public synchronized DfsPeerInfo getPeer(String peerName) throws DfsNotFoundException
    {
        if (!containsPeer(peerName))
        {
            throw new DfsNotFoundException("The peer '" + peerName + "' not found");
        }

        return peerMap.get(peerName);
    }
    
    public synchronized Set<String> getPeerNames()
    {
        return peerMap.keySet();
    }
    
    public synchronized Collection<DfsPeerInfo> getPeers()
    {
        return peerMap.values();
    }
}
