package br.com.ltoscano.dfs.core.service.data;

import br.com.ltoscano.dfs.core.filesystem.DfsFileSystem;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import br.com.ltoscano.dfs.core.service.DfsService;
import br.com.ltoscano.dfs.core.service.data.listener.DfsGetBlockDataEventListener;
import br.com.ltoscano.dfs.core.service.data.task.DfsGetDataTask;
import java.io.File;
import java.nio.file.Paths;

/**
 *
 * @author ltosc
 */
public class DfsDataService extends DfsService
{
    public DfsDataService(String peerWorkDirPath, DfsPeerInfoTable peerTable, DfsFileSystem peerFileSystem)
    {
        super(DfsDataService.class, "Data");
        
        String dataDirPath = Paths.get(peerWorkDirPath, "data").toString();
        
        File peerDataDir = new File(dataDirPath);
        
        if(!peerDataDir.exists())
        {
            peerDataDir.mkdirs();
        }
        
        registerTask(new DfsGetDataTask(peerDataDir, peerTable, peerFileSystem));
        registerEventListener(new DfsGetBlockDataEventListener(peerDataDir, peerFileSystem));
    }
}
