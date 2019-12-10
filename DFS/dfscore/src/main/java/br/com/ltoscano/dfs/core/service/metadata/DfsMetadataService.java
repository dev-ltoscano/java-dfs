package br.com.ltoscano.dfs.core.service.metadata;

import br.com.ltoscano.dfs.core.filesystem.DfsFileSystem;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import br.com.ltoscano.dfs.core.service.DfsService;
import br.com.ltoscano.dfs.core.service.metadata.listener.DfsGetMetadataListener;
import br.com.ltoscano.dfs.core.service.metadata.task.DfsGetMetadataTask;

/**
 *
 * @author ltosc
 */
public class DfsMetadataService extends DfsService
{
    public DfsMetadataService(DfsPeerInfoTable peerTable, DfsFileSystem peerFileSystem) 
    {
        super(DfsMetadataService.class, "Metadata");
        
        registerTask(new DfsGetMetadataTask(peerTable, peerFileSystem));
        registerEventListener(new DfsGetMetadataListener(peerFileSystem));
    }   
}
