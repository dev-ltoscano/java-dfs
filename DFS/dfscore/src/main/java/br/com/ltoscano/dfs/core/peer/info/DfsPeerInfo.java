package br.com.ltoscano.dfs.core.peer.info;

import br.com.ltoscano.dfs.core.filesystem.DfsFileSystem;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;

/**
 *
 * @author ltosc
 */
public class DfsPeerInfo 
{
    private final String name;
    private DfsNetworkAddress address;
    private long timestamp;
    
    private DfsFileSystem fileSystem;
    
    public DfsPeerInfo(String name)
    {
        this(name, null);
    }
    
    public DfsPeerInfo(String name, DfsNetworkAddress address)
    {
        this.name = name;
        this.address = address;
        this.timestamp = System.currentTimeMillis();
        
        this.fileSystem = null;
    }

    public synchronized String getName() 
    {
        return name;
    }

    public synchronized DfsNetworkAddress getAddress()
    {
        return address;
    }
    
    public synchronized void setAddress(DfsNetworkAddress address) 
    {
        this.address = address;
    }

    public synchronized long getTimestamp()
    {
        return timestamp;
    }
    
    public synchronized void updateTimestamp()
    {
        timestamp = System.currentTimeMillis();
    }

    public synchronized DfsFileSystem getFileSystem() 
    {
        return fileSystem;
    }

    public synchronized void setFileSystem(DfsFileSystem fileSystem)
    {
        this.fileSystem = fileSystem;
    }
}
