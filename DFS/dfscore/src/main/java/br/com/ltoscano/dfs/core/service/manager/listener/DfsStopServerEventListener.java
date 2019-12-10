package br.com.ltoscano.dfs.core.service.manager.listener;

import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsEventSource;
import br.com.ltoscano.dfs.core.event.DfsEventListener;
import br.com.ltoscano.dfs.core.event.DfsEventMessage;

/**
 *
 * @author ltosc
 */
public class DfsStopServerEventListener extends DfsEventListener
{
    public DfsStopServerEventListener() 
    {
        super(DfsStopServerEventListener.class, "STOP_SERVER", DfsEventSource.Any);
    }
    
    @Override
    public void onEvent(Object sender, DfsEventMessage eventArgs) throws Exception 
    {
        logInfo("The server is shutting down...");
    }
}
