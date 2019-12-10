package br.com.ltoscano.dfs.core.service.data.task;

import br.com.ltoscano.dfs.core.config.DfsConfigurator;
import br.com.ltoscano.dfs.core.filesystem.DfsFile;
import br.com.ltoscano.dfs.core.filesystem.DfsFileSystem;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import br.com.ltoscano.dfs.core.task.DfsBaseTimerTask;
import br.com.ltoscano.dfs.core.torrent.DfsFileDownloader;
import java.io.File;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ltosc
 */
public class DfsGetDataTask extends DfsBaseTimerTask
{
    private final File peerDataDir;
    private final DfsPeerInfoTable peerTable;
    private final DfsFileSystem peerFileSystem;
    
    public DfsGetDataTask(File peerDataDir, DfsPeerInfoTable peerTable, DfsFileSystem peerFileSystem)
    {
        super(DfsGetDataTask.class, 
                "GET_DATA", 
                DfsConfigurator.getConfig().getMinTimeToGetData(),
                DfsConfigurator.getConfig().getMaxTimeToGetData());
        
        this.peerDataDir = peerDataDir;
        this.peerTable = peerTable;
        this.peerFileSystem = peerFileSystem;
    }

    @Override
    public void performTask() 
    {
        logInfo("Scanning for unavailable files");
        Collection<DfsFile> fileList = peerFileSystem.getFiles();
        
        ThreadPoolExecutor downloadFileThreadPool = new ThreadPoolExecutor(1, 8, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        
        for(DfsFile file : fileList)
        {
            if(!file.isAvailable())
            {
                downloadFileThreadPool.execute(new DfsFileDownloader(peerDataDir, peerTable, file));
            }
        }
        
        downloadFileThreadPool.shutdown();
        
        try 
        {
            downloadFileThreadPool.awaitTermination(1, TimeUnit.DAYS);
        } 
        catch (InterruptedException ex) 
        {
            handleError(ex);
        }
        
        logInfo("All files download completed");
    }
}
