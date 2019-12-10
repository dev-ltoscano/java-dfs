package br.com.ltoscano.dfs.core.service.data.listener;

import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsEventSource;
import br.com.ltoscano.dfs.core.event.DfsEventListener;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.filesystem.DfsFileSystem;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsBlockDescriptor;
import br.com.ltoscano.dfs.core.network.tcp.DfsTcpConnection;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author ltosc
 */
public class DfsGetBlockDataEventListener extends DfsEventListener
{
    private final File peerDataDir;
    private final DfsFileSystem peerFileSystem;
    
    public DfsGetBlockDataEventListener(File peerDataDir, DfsFileSystem peerFileSystem) 
    {
        super(DfsGetBlockDataEventListener.class, "GET_BLOCK_DATA", DfsEventSource.Tcp);
        
        this.peerDataDir = peerDataDir;
        this.peerFileSystem = peerFileSystem;
    }

    @Override
    public void onEvent(Object sender, DfsEventMessage eventArgs) throws Exception
    {
        DfsTcpConnection connection = (DfsTcpConnection)sender;
        
        String fileId = eventArgs.getArgs("FileId");
        String fileVersionId = eventArgs.getArgs("FileVersionId");
        String blockId = eventArgs.getArgs("BlockId");
        
        DfsBlockDescriptor blockDescriptor = peerFileSystem.getFile(fileId).getFileVersion(fileVersionId).getBlockDescriptor(blockId);
        boolean available = blockDescriptor.isAvailable();
        
        File blockPath = new File(Paths.get(peerDataDir.getAbsolutePath(), fileId, fileVersionId, blockId).toString());
        byte[] buffer = new byte[Math.toIntExact(blockDescriptor.getSize())];
        
        try(FileInputStream fileInputStream = new FileInputStream(blockPath))
        {
            fileInputStream.read(buffer);
        }
        
        DfsEventMessage response = new DfsEventMessage("GET_BLOCK_DATA_RESPONSE");
        response.setArgs("Status", String.valueOf(available));
        response.setArgs("Data", Hex.encodeHexString(buffer));
        
        connection.writeEvent(response);
    }
}