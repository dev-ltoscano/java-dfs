package br.com.ltoscano.dfs.core.service.manager;

import br.com.ltoscano.dfs.core.service.DfsService;
import br.com.ltoscano.dfs.core.service.manager.listener.DfsStopServerEventListener;

/**
 *
 * @author ltosc
 */
public class DfsManagerService extends DfsService
{
    public DfsManagerService() 
    {
        super(DfsManagerService.class, "Manager");
        this.registerEventListener(new DfsStopServerEventListener());
    }
}
