package br.com.ltoscano.dfs.core.service.metadata.task;

import br.com.ltoscano.dfs.core.config.DfsConfigurator;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.filesystem.DfsFileSystem;
import br.com.ltoscano.dfs.core.json.DfsJsonHelper;
import br.com.ltoscano.dfs.core.network.tcp.sender.DfsTcpSender;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfo;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import br.com.ltoscano.dfs.core.task.DfsBaseTimerTask;
import java.io.IOException;

/**
 *
 * @author ltosc
 */
public class DfsGetMetadataTask extends DfsBaseTimerTask
{
    private final DfsPeerInfoTable peerTable;
    private final DfsFileSystem peerFileSystem;
    
    public DfsGetMetadataTask(DfsPeerInfoTable peerTable, DfsFileSystem peerFileSystem)
    {
        super(DfsGetMetadataTask.class, 
                "GET_METADATA", 
                DfsConfigurator.getConfig().getMinTimeToGetMetadata(),
                DfsConfigurator.getConfig().getMaxTimeToGetMetadata());
        
        this.peerTable = peerTable;
        this.peerFileSystem = peerFileSystem;
    }
    
    @Override
    public void performTask()
    {
        DfsEventMessage metadataEventArgs = new DfsEventMessage("GET_METADATA");
        
        for(DfsPeerInfo peerInfo : peerTable.getPeers())
        {
            try 
            {
                logInfo("Getting metadata from '" + peerInfo.getName() + "'");
                
                DfsEventMessage response = DfsTcpSender.send(peerInfo.getAddress(), metadataEventArgs);
                
                DfsFileSystem neighborFileSystem = DfsJsonHelper.jsonToObj(response.getArgs("Metadata"), DfsFileSystem.class);
                peerInfo.setFileSystem(neighborFileSystem);
                
                peerFileSystem.mergeFileSystem(neighborFileSystem);
                
                logInfo("The metadata has been merged with metadata from '" + peerInfo.getName() + "'");
            } 
            catch (IOException | DfsException ex) 
            {
                handleError(ex);
            }
        }
    }
}
