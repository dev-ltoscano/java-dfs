package br.com.ltoscano.dfs.core.torrent;

import br.com.ltoscano.dfs.core.base.DfsBase;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsBlockDescriptor;
import br.com.ltoscano.dfs.core.network.tcp.DfsTcpConnection;
import br.com.ltoscano.dfs.core.network.tcp.sender.DfsTcpSender;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfo;
import br.com.ltoscano.dfs.core.peer.info.DfsPeerInfoTable;
import br.com.ltoscano.dfs.core.random.DfsRandomHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author ltosc
 */
public class DfsBlockDownloader extends DfsBase implements Runnable
{
    private final String fileId;
    private final String fileVersionId;
    private final DfsBlockDescriptor block;
    private final File blockPath;
    private final DfsPeerInfoTable peerTable;
    
    public DfsBlockDownloader(String fileId, String fileVersionId, DfsBlockDescriptor block, File blockPath, DfsPeerInfoTable peerTable)
    {
        super(DfsBlockDownloader.class);
        
        this.fileId = fileId;
        this.fileVersionId = fileVersionId;
        this.block = block;
        this.blockPath = blockPath;
        this.peerTable = peerTable;
    }

    @Override
    public void run()
    {
        Collection<DfsPeerInfo> peerList = peerTable.getPeers();
        List<DfsPeerInfo> downloadPeerList = new ArrayList<>();

        for (DfsPeerInfo peer : peerList)
        {
            try 
            {
                boolean available = peer.getFileSystem()
                        .getFile(fileId)
                        .getFileVersion(fileVersionId)
                        .getBlockDescriptor(block.getHash())
                        .isAvailable();

                if (available) 
                {
                    downloadPeerList.add(peer);
                }
            } 
            catch (DfsNotFoundException ex)
            {

            }
        }
        
        if(!downloadPeerList.isEmpty())
        {
            DfsTcpConnection connection = null;

            DfsEventMessage getBlockEventArgs = new DfsEventMessage("GET_BLOCK_DATA");
            getBlockEventArgs.setArgs("FileId", fileId);
            getBlockEventArgs.setArgs("FileVersionId", fileVersionId);
            getBlockEventArgs.setArgs("BlockId", block.getHash());

            try
            {
                connection = DfsTcpSender.open(downloadPeerList.get(DfsRandomHelper.randomInteger(0, downloadPeerList.size() - 1)).getAddress(), getBlockEventArgs);

                DfsEventMessage response = connection.readEvent();
                boolean available = Boolean.valueOf(response.getArgs("Status"));

                if (available) 
                {
                    try 
                    {
                        byte[] buffer = Hex.decodeHex(response.getArgs("Data"));

                        try (FileOutputStream fileOutputStream = new FileOutputStream(blockPath)) 
                        {
                            fileOutputStream.write(buffer);
                        }

                        block.setAvailable(true);
                    }
                    catch (DecoderException ex)
                    {
                        Logger.getLogger(DfsBlockDownloader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } 
            catch (IOException | DfsException ex) 
            {
                handleError(ex);
            } 
            finally 
            {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (IOException ex) {
                        handleError(ex);
                    }

                    connection = null;
                }
            }
        }
    }
}
