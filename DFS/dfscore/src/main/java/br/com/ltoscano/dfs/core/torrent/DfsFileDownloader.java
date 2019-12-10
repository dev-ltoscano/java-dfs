package br.com.ltoscano.dfs.core.torrent;

import br.com.ltoscano.dfs.core.base.DfsBase;
import br.com.ltoscano.dfs.core.filesystem.DfsFile;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsBlockDescriptor;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsFileDescriptor;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ltosc
 */
public class DfsFileDownloader extends DfsBase implements Runnable
{
    private final File peerDataDir;
    private final DfsPeerInfoTable peerTable;
    private final DfsFile file;
    
    private final ThreadPoolExecutor downloadThreadPool;
    
    public DfsFileDownloader(File peerDataDir, DfsPeerInfoTable peerTable, DfsFile file)
    {
        super(DfsFileDownloader.class);
        
        this.peerDataDir = peerDataDir;
        this.peerTable = peerTable;
        this.file = file;
        
        this.downloadThreadPool = new ThreadPoolExecutor(1, 8, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    }

    @Override
    public void run() 
    {
        logInfo("Downloading the file: " + file.getPath());
        
        File fileDirPath = new File(Paths.get(peerDataDir.getAbsolutePath(), file.getId()).toString());
        
        if(!fileDirPath.exists())
        {
            file.setAvailable(false);
            fileDirPath.mkdir();
        }
        
        Collection<DfsFileDescriptor> fileVersionList = file.getFileDescriptorList();
        
        for(DfsFileDescriptor fileVersion : fileVersionList)
        {
            File fileVersionDirPath = new File(Paths.get(fileDirPath.getAbsolutePath(), fileVersion.getHash()).toString());
            
            if (!fileVersionDirPath.exists()) 
            {
                fileVersion.setAvailable(false);
                fileVersionDirPath.mkdir();
            }
            
            Collection<DfsBlockDescriptor> blockList = fileVersion.getBlockDescriptorList();
            
            for(DfsBlockDescriptor block : blockList)
            {
                File blockPath = new File(Paths.get(fileVersionDirPath.getAbsolutePath(), block.getHash()).toString());
                
                if(blockPath.exists())
                {
                    block.setAvailable(true);
                }
                else
                {
                    block.setAvailable(false);
                    
                    downloadThreadPool.execute(new DfsBlockDownloader(
                                file.getId(),
                                fileVersion.getHash(),
                                block, 
                                blockPath, 
                                peerTable));
                }
            }
        }
        
        downloadThreadPool.shutdown();
        
        try 
        {
            
            downloadThreadPool.awaitTermination(1, TimeUnit.DAYS);
        } 
        catch (InterruptedException ex)
        {
            handleError(ex);
        }
        
        boolean fileAvailable = true;
        
        for(DfsFileDescriptor fileVersion : fileVersionList)
        {
            Collection<DfsBlockDescriptor> blockList = fileVersion.getBlockDescriptorList();
            boolean versionAvailable = true;
            
            for(DfsBlockDescriptor block : blockList)
            {
                if(!block.isAvailable())
                {
                    versionAvailable = false;
                    break;
                }
            }
            
            fileVersion.setAvailable(versionAvailable);
            
            if(!versionAvailable)
            {
                fileAvailable = false;
                break;
            }
        }
        
        file.setAvailable(fileAvailable);
        
        logInfo("File download completed: " + file.getPath());
    }
}
