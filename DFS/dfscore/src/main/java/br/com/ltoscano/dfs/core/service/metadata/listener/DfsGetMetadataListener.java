package br.com.ltoscano.dfs.core.service.metadata.listener;

import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsEventSource;
import br.com.ltoscano.dfs.core.event.DfsEventListener;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.filesystem.DfsFileSystem;
import br.com.ltoscano.dfs.core.network.tcp.DfsTcpConnection;

/**
 *
 * @author ltosc
 */
public class DfsGetMetadataListener extends DfsEventListener
{
    private final DfsFileSystem peerFileSystem;
    
    public DfsGetMetadataListener(DfsFileSystem peerFileSystem)
    {
        super(DfsGetMetadataListener.class, "GET_METADATA", DfsEventSource.Tcp);
        this.peerFileSystem = peerFileSystem;
    }
    
    @Override
    public void onEvent(Object sender, DfsEventMessage eventArgs) throws Exception
    {
        DfsTcpConnection connection = (DfsTcpConnection)sender;

        DfsEventMessage eventArgsResponse = new DfsEventMessage("GET_METADATA_RESPONSE");
        eventArgsResponse.setArgs("Metadata", peerFileSystem.toString());

        connection.writeEvent(eventArgsResponse);
    }
}
