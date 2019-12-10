package br.com.ltoscano.dfs.core.service;

import br.com.ltoscano.dfs.core.base.DfsBase;
import br.com.ltoscano.dfs.core.event.DfsEventListener;
import br.com.ltoscano.dfs.core.task.DfsBaseTask;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author ltosc
 */
public abstract class DfsService extends DfsBase
{
    private final String serviceName;
    
    private final HashMap<String, DfsEventListener> serviceEventListenerMap;
    private final HashMap<String, DfsBaseTask> serviceTaskMap;
    
    public DfsService(Class derivedClass, String serviceName)
    {
        super(derivedClass);
        
        this.serviceName = serviceName;
        this.serviceEventListenerMap = new HashMap<>();
        this.serviceTaskMap = new HashMap<>();
    }
    
    protected final void registerEventListener(DfsEventListener listener)
    {
        serviceEventListenerMap.put(listener.getEventName(), listener);
    }
    
    protected final void unregisterEventListener(String eventName)
    {
        serviceEventListenerMap.remove(eventName);
    }
    
    protected final void registerTask(DfsBaseTask task)
    {
        serviceTaskMap.put(task.getTaskName(), task);
    }
    
    protected final void unregisterTask(String taskName)
    {
        serviceTaskMap.remove(taskName);
    }
    
    public void start()
    {
        for(DfsBaseTask serviceTask : getServiceTasks())
        {
            serviceTask.start();
        }
    }
    
    public void stop()
    {
        for(DfsBaseTask serviceTask : getServiceTasks())
        {
            serviceTask.stop();
        }
    }
    
    public String getServiceName() 
    {
        return serviceName;
    }
    
    public Collection<DfsEventListener> getServiceEventListeners()
    {
        return serviceEventListenerMap.values();
    }
    
    public Collection<DfsBaseTask> getServiceTasks()
    {
        return serviceTaskMap.values();
    }
}
