package br.com.ltoscano.dfs.core.task;

import br.com.ltoscano.dfs.core.base.DfsBase;

/**
 *
 * @author ltosc
 */
public abstract class DfsBaseTask extends DfsBase
{
    private final String taskName;
    
    public DfsBaseTask(Class derivedClass, String taskName)
    {
        super(derivedClass);
        this.taskName = taskName;
    }
    
    /**
     * @return the taskName
     */
    public String getTaskName()
    {
        return taskName;
    }
    
    public abstract void start();
    public abstract void stop();
}
