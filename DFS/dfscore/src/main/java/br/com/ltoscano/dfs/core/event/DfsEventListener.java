package br.com.ltoscano.dfs.core.event;

import br.com.ltoscano.dfs.core.base.DfsBase;
import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsEventSource;
import br.com.ltoscano.dfs.core.string.DfsStringHelper;

/**
 *
 * @author ltosc
 */
public abstract class DfsEventListener extends DfsBase
{
    private final String eventName;
    private final DfsEventSource eventSource;
    
    public DfsEventListener(Class derivedClass, String eventName, DfsEventSource eventSource) 
    {
        super(derivedClass);
        
        this.eventName = DfsStringHelper.capitalize(eventName);
        this.eventSource = eventSource;
    }
    
    public abstract void onEvent(Object sender, DfsEventMessage eventMsg) throws Exception;

    /**
     * @return the eventName
     */
    public String getEventName()
    {
        return eventName;
    }

    /**
     * @return the eventSource
     */
    public DfsEventSource getEventSource() {
        return eventSource;
    }
}
